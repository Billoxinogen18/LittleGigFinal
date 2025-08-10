package com.littlegig.app.presentation.events

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import com.littlegig.app.data.model.Event
import com.littlegig.app.data.model.User
import com.littlegig.app.data.repository.EventRepository
import com.littlegig.app.data.repository.AuthRepository
import com.littlegig.app.data.repository.UserRepository
import com.littlegig.app.data.repository.PaymentRepository
import android.content.Intent
import android.net.Uri
import android.content.Context
import java.util.Date
import com.littlegig.app.data.repository.NotificationRepository

@HiltViewModel
class EventDetailsViewModel @Inject constructor(
    private val eventRepository: EventRepository,
    private val authRepository: AuthRepository,
    private val userRepository: UserRepository,
    private val paymentRepository: PaymentRepository,
    private val notificationRepository: NotificationRepository
) : ViewModel() {
    
    private val _uiState = MutableStateFlow(EventDetailsUiState())
    val uiState: StateFlow<EventDetailsUiState> = _uiState.asStateFlow()
    
    fun loadEventDetails(eventId: String) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, error = null)
            
            try {
                val result = eventRepository.getEvent(eventId)
                result.onSuccess { event ->
                    if (event != null) {
                        // Load organizer details
                        val organizer = userRepository.getUserById(event.organizerId).getOrNull()
                        
                        // Check if current user is following organizer
                        val currentUser = authRepository.currentUser.value
                        val isFollowing = if (currentUser != null && organizer != null) {
                            userRepository.isFollowing(currentUser.id, organizer.id).getOrNull() ?: false
                        } else false
                        
                        // Check if current user liked the event
                        val isLiked = if (currentUser != null) {
                            eventRepository.isEventLikedByUser(eventId, currentUser.id).getOrNull() ?: false
                        } else false
                        
                        _uiState.value = _uiState.value.copy(
                            event = event,
                            organizer = organizer,
                            isFollowingOrganizer = isFollowing,
                            isLiked = isLiked,
                            isLoading = false
                        )
                        try { notificationRepository.subscribeToEventTopic(event.id) } catch (_: Exception) {}
                    } else {
                        _uiState.value = _uiState.value.copy(
                            event = null,
                            isLoading = false
                        )
                    }
                }.onFailure { error ->
                    _uiState.value = _uiState.value.copy(
                        error = error.message,
                        isLoading = false
                    )
                }
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    error = e.message,
                    isLoading = false
                )
            }
        }
    }
    
    fun toggleEventLike() {
        viewModelScope.launch {
            try {
                val currentUser = authRepository.currentUser.value
                val event = _uiState.value.event
                if (currentUser != null && event != null) {
                    val newLikeState = !_uiState.value.isLiked
                    // Optimistic UI
                    _uiState.value = _uiState.value.copy(isLiked = newLikeState)
                    val result = eventRepository.toggleEventLike(event.id, currentUser.id)
                    result.onSuccess {
                        // no-op; already reflected optimistically
                    }.onFailure { e ->
                        _uiState.value = _uiState.value.copy(
                            isLiked = !newLikeState,
                            error = e.message
                        )
                    }
                }
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(error = e.message)
            }
        }
    }
    
    fun followOrganizer(organizerId: String?) {
        val currentUser = authRepository.currentUser.value
        
        if (currentUser != null && organizerId != null) {
            // Optimistic update for immediate UI feedback
            val newFollowState = !_uiState.value.isFollowingOrganizer
            _uiState.value = _uiState.value.copy(
                isFollowingOrganizer = newFollowState,
                isFollowProcessing = true
            )
            
            viewModelScope.launch {
                try {
                    val result = userRepository.toggleFollow(currentUser.id, organizerId)
                    result.onSuccess {
                        _uiState.value = _uiState.value.copy(
                            isFollowProcessing = false
                        )
                    }.onFailure { error ->
                        // Revert optimistic update on failure
                        _uiState.value = _uiState.value.copy(
                            isFollowingOrganizer = !newFollowState,
                            isFollowProcessing = false,
                            error = error.message
                        )
                    }
                } catch (e: Exception) {
                    // Revert optimistic update on exception
                    _uiState.value = _uiState.value.copy(
                        isFollowingOrganizer = !newFollowState,
                        isFollowProcessing = false,
                        error = e.message
                    )
                }
            }
        }
    }
    
    fun shareEvent(context: Context) {
        val event = _uiState.value.event
        if (event != null) {
            val shareText = """
                Check out this amazing event: ${event.title}
                
                📅 Date: ${Date(event.dateTime)}
                📍 Location: ${event.location.name}
                💰 Price: KSH ${event.price}
                
                Download LittleGig to join the party! 🎉
            """.trimIndent()
            
            val shareIntent = Intent().apply {
                action = Intent.ACTION_SEND
                type = "text/plain"
                putExtra(Intent.EXTRA_TEXT, shareText)
                putExtra(Intent.EXTRA_SUBJECT, "Check out this event on LittleGig!")
            }
            
            val chooser = Intent.createChooser(shareIntent, "Share via")
            chooser.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            context.startActivity(chooser)
            
            _uiState.value = _uiState.value.copy(
                shareText = shareText
            )
        }
    }
    
    fun buyTicket() {
        viewModelScope.launch {
            val currentUser = authRepository.currentUser.value
            val event = _uiState.value.event
            
            if (currentUser != null && event != null) {
                try {
                    _uiState.value = _uiState.value.copy(isProcessingPayment = true)
                    
                    val result = paymentRepository.processTicketPurchase(
                        eventId = event.id,
                        userId = currentUser.id,
                        amount = event.price,
                        eventTitle = event.title
                    )
                    
                    result.onSuccess { paymentUrl ->
                        _uiState.value = _uiState.value.copy(
                            paymentUrl = paymentUrl,
                            isProcessingPayment = false
                        )
                    }.onFailure { error ->
                        _uiState.value = _uiState.value.copy(
                            error = error.message,
                            isProcessingPayment = false
                        )
                    }
                } catch (e: Exception) {
                    _uiState.value = _uiState.value.copy(
                        error = e.message,
                        isProcessingPayment = false
                    )
                }
            }
        }
    }
    fun getPaymentUrl(eventId: String, amount: Double, title: String): String? {
        return _uiState.value.paymentUrl
    }
    fun getCurrentUserId(): String? = authRepository.currentUser.value?.id
    fun getCurrentUserName(): String = authRepository.currentUser.value?.displayName ?: ""
    fun getCurrentUserImage(): String = authRepository.currentUser.value?.profileImageUrl ?: ""
    
    fun clearError() {
        _uiState.value = _uiState.value.copy(error = null)
    }

    override fun onCleared() {
        super.onCleared()
        _uiState.value.event?.id?.let { eid ->
            viewModelScope.launch { try { notificationRepository.unsubscribeFromEventTopic(eid) } catch (_: Exception) {} }
        }
    }
}

data class EventDetailsUiState(
    val event: Event? = null,
    val organizer: User? = null,
    val isFollowingOrganizer: Boolean = false,
    val isLiked: Boolean = false,
    val isLoading: Boolean = false,
    val isProcessingPayment: Boolean = false,
    val isLikeProcessing: Boolean = false,
    val isFollowProcessing: Boolean = false,
    val error: String? = null,
    val shareText: String? = null,
    val paymentUrl: String? = null
) 