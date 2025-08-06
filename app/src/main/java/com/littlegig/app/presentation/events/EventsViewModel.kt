package com.littlegig.app.presentation.events

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.littlegig.app.data.model.ContentCategory
import com.littlegig.app.data.model.Event
import com.littlegig.app.data.repository.EventRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class EventsViewModel @Inject constructor(
    private val eventRepository: EventRepository
) : ViewModel() {
    
    private val _uiState = MutableStateFlow(EventsUiState())
    val uiState: StateFlow<EventsUiState> = _uiState.asStateFlow()
    
    private val _searchQuery = MutableStateFlow("")
    private val _selectedCategory = MutableStateFlow<ContentCategory?>(null)
    
    init {
        loadEvents()
        loadFeaturedEvents()
    }
    
    private fun loadEvents() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true)
            
            combine(
                eventRepository.getAllEvents(),
                _searchQuery,
                _selectedCategory
            ) { events, query, category ->
                var filteredEvents = events
                
                // Filter by category
                if (category != null) {
                    filteredEvents = filteredEvents.filter { it.category == category }
                }
                
                // Filter by search query
                if (query.isNotBlank()) {
                    filteredEvents = filteredEvents.filter { event ->
                        event.title.contains(query, ignoreCase = true) ||
                        event.description.contains(query, ignoreCase = true) ||
                        event.location.name.contains(query, ignoreCase = true) ||
                        event.location.city.contains(query, ignoreCase = true) ||
                        event.tags.any { it.contains(query, ignoreCase = true) }
                    }
                }
                
                filteredEvents
            }.collect { filteredEvents ->
                _uiState.value = _uiState.value.copy(
                    events = filteredEvents,
                    isLoading = false
                )
            }
        }
    }
    
    private fun loadFeaturedEvents() {
        viewModelScope.launch {
            eventRepository.getFeaturedEvents().collect { featuredEvents ->
                _uiState.value = _uiState.value.copy(featuredEvents = featuredEvents)
            }
        }
    }
    
    fun searchEvents(query: String) {
        _searchQuery.value = query
    }
    
    fun filterByCategory(category: ContentCategory?) {
        _selectedCategory.value = category
    }
    
    fun refreshEvents() {
        loadEvents()
        loadFeaturedEvents()
    }
    
    fun toggleEventLike(eventId: String) {
        viewModelScope.launch {
            // TODO: Toggle event like in backend
        }
    }
    
    fun rateEvent(eventId: String, rating: Float) {
        viewModelScope.launch {
            // TODO: Rate event in backend
        }
    }
}

data class EventsUiState(
    val events: List<Event> = emptyList(),
    val featuredEvents: List<Event> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null
)