package com.littlegig.app.presentation.events

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.littlegig.app.data.model.ContentCategory
import com.littlegig.app.data.model.Event
import com.littlegig.app.data.repository.EventRepository
import com.littlegig.app.data.repository.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.retry
import kotlinx.coroutines.launch
import kotlinx.coroutines.flow.first
import com.littlegig.app.data.repository.SharingRepository
import com.littlegig.app.data.repository.ConfigRepository
import com.littlegig.app.data.repository.FeedWeights
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.merge
import javax.inject.Inject

@HiltViewModel
class EventsViewModel @Inject constructor(
    private val eventRepository: EventRepository,
    private val authRepository: AuthRepository,
    private val sharingRepository: SharingRepository,
    private val configRepository: ConfigRepository
) : ViewModel() {
    
    private val _uiState = MutableStateFlow(EventsUiState())
    val uiState: StateFlow<EventsUiState> = _uiState.asStateFlow()
    
    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()
    
    // Throttling for like/rate actions to reduce write bursts
    private val likeEvents = MutableSharedFlow<Pair<String, Boolean>>()
    private val rateEvents = MutableSharedFlow<Pair<String, Float>>()
    
    init {
        loadEvents()
        setupThrottledActions()
    }
    
    private fun setupThrottledActions() {
        viewModelScope.launch {
            // Debounce likes by 500ms
            likeEvents.debounce(500).collect { (eventId, isLiked) ->
                try {
                    val currentUser = authRepository.currentUser.value
                    if (currentUser != null) {
                        eventRepository.toggleEventLike(eventId, currentUser.id)
                    }
                } catch (e: Exception) {
                    _uiState.value = _uiState.value.copy(
                        error = "Failed to like event: ${e.message}"
                    )
                }
            }
        }
        
        viewModelScope.launch {
            // Debounce ratings by 1 second
            rateEvents.debounce(1000).collect { (eventId, rating) ->
                try {
                    val currentUser = authRepository.currentUser.value
                    if (currentUser != null) {
                        eventRepository.rateEvent(eventId, currentUser.id, rating)
                    }
                } catch (e: Exception) {
                    _uiState.value = _uiState.value.copy(
                        error = "Failed to rate event: ${e.message}"
                    )
                }
            }
        }
    }
    
    fun loadEvents() {
        viewModelScope.launch {
            println("🔥 DEBUG: EventsViewModel.loadEvents() called")
            _uiState.value = _uiState.value.copy(isLoading = true, error = null)
            com.littlegig.app.utils.PerfMonitor.startTrace("events_load")
            
            try {
                eventRepository.getAllEvents()
                    .retry(3) { cause ->
                        cause is Exception
                    }
                    .catch { error ->
                        _uiState.value = _uiState.value.copy(
                            isLoading = false,
                            error = "Failed to load events: ${error.message}"
                        )
                        com.littlegig.app.utils.PerfMonitor.stopTrace("events_load")
                    }
                    .collect { events ->
                        println("🔥 DEBUG: EventsViewModel received ${events.size} events")
                        events.forEach { event ->
                            println("🔥 DEBUG: Event: ${event.title} - ${event.id}")
                        }
                        val filteredEvents = personalizeAndFilterEvents(events)
                        println("🔥 DEBUG: After filtering: ${filteredEvents.size} events")
                        val currentUser = authRepository.currentUser.value
                        var friendsGoingMap: Map<String, Int> = emptyMap()
                        if (currentUser != null) {
                            val followingIds = currentUser.following
                            val mutable = mutableMapOf<String, Int>()
                            for (event in filteredEvents) {
                                val count = eventRepository.getFriendsGoingCount(event.id, followingIds)
                                mutable[event.id] = count
                            }
                            friendsGoingMap = mutable
                        }
                        _uiState.value = _uiState.value.copy(
                            events = filteredEvents,
                            isLoading = false,
                            error = null,
                            eventIdToFriendsGoing = friendsGoingMap
                        )
                        com.littlegig.app.utils.PerfMonitor.putMetric("events_load", "count", events.size.toLong())
                        com.littlegig.app.utils.PerfMonitor.stopTrace("events_load")
                        println("🔥 DEBUG: UI state updated with ${filteredEvents.size} events")
                    }
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    error = "Network error: ${e.message}"
                )
                com.littlegig.app.utils.PerfMonitor.stopTrace("events_load")
            }
        }
    }
    
    fun selectCategory(category: ContentCategory?) {
        _uiState.value = _uiState.value.copy(selectedCategory = category)
        loadEvents()
    }
    
    fun updateSearchQuery(query: String) {
        _searchQuery.value = query
        loadEvents()
    }
    
    private suspend fun fetchWeights(): FeedWeights = try { configRepository.getFeedWeights() } catch (_: Exception) { FeedWeights() }

    private fun scoreEvent(e: Event, w: FeedWeights): Double {
        val isFeaturedScore = if (e.featured) 1.0 else 0.0
        val ticketsScore = e.ticketsSold.toDouble()
        val priceScore = e.price
        val recencyScore = kotlin.math.abs(e.dateTime - System.currentTimeMillis()) / (60_000.0)
        return isFeaturedScore * w.featuredWeight + ticketsScore * w.ticketsSoldWeight + priceScore * w.priceWeight + recencyScore * w.recencyWeight
    }

    private suspend fun personalizeAndFilterEvents(events: List<Event>): List<Event> {
        var filteredEvents = events
        
        // Filter by category
        _uiState.value.selectedCategory?.let { category ->
            filteredEvents = filteredEvents.filter { it.category == category }
        }
        
        // Filter by search query
        val query = _searchQuery.value.trim()
        if (query.isNotEmpty()) {
            filteredEvents = filteredEvents.filter { event ->
                event.title.contains(query, ignoreCase = true) ||
                event.description.contains(query, ignoreCase = true) ||
                event.location.name.contains(query, ignoreCase = true) ||
                event.location.city.contains(query, ignoreCase = true) ||
                event.organizerName.contains(query, ignoreCase = true)
            }
        }

        val weights = fetchWeights()
        filteredEvents = filteredEvents.sortedByDescending { scoreEvent(it, weights) }

        return filteredEvents
    }
    
    fun toggleEventLike(eventId: String) {
        viewModelScope.launch {
            likeEvents.emit(Pair(eventId, true))
        }
    }
    
    fun joinWaitlist(eventId: String) {
        viewModelScope.launch {
            try {
                val currentUser = authRepository.currentUser.value
                if (currentUser != null) {
                    eventRepository.joinWaitlist(eventId, currentUser.id)
                }
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    error = "Failed to join waitlist: ${e.message}"
                )
            }
        }
    }

    fun subscribePriceDrop(eventId: String) {
        viewModelScope.launch {
            try {
                val currentUser = authRepository.currentUser.value
                if (currentUser != null) {
                    eventRepository.subscribePriceDrop(eventId, currentUser.id)
                }
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    error = "Failed to subscribe to price drop: ${e.message}"
                )
            }
        }
    }

    fun rsvp(eventId: String) {
        viewModelScope.launch {
            try {
                val currentUser = authRepository.currentUser.value
                if (currentUser != null) {
                    eventRepository.rsvpEvent(eventId, currentUser.id)
                }
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    error = "Failed to RSVP: ${e.message}"
                )
            }
        }
    }

    suspend fun createEventShareLink(event: Event): String? {
        return try {
            val link = sharingRepository.createEventDynamicLink(
                eventId = event.id,
                title = event.title,
                imageUrl = event.imageUrls.firstOrNull()
            )
            link.getOrNull()
        } catch (e: Exception) {
            null
        }
    }

    fun rateEvent(eventId: String, rating: Float) {
        viewModelScope.launch {
            rateEvents.emit(Pair(eventId, rating))
        }
    }
    
    fun clearError() {
        _uiState.value = _uiState.value.copy(error = null)
    }
    
    fun refreshEvents() {
        loadEvents()
    }
}

data class EventsUiState(
    val events: List<Event> = emptyList(),
    val selectedCategory: ContentCategory? = null,
    val isLoading: Boolean = false,
    val error: String? = null,
    val eventIdToFriendsGoing: Map<String, Int> = emptyMap()
)