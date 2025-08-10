package com.littlegig.app.presentation.recaps

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.littlegig.app.data.model.Event
import com.littlegig.app.data.model.Recap
import com.littlegig.app.data.repository.AuthRepository
import com.littlegig.app.data.repository.EventRepository
import com.littlegig.app.data.repository.RecapRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.math.*

@HiltViewModel
class RecapsUploadViewModel @Inject constructor(
    private val eventRepository: EventRepository,
    private val recapRepository: RecapRepository,
    private val authRepository: AuthRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(RecapsUploadUiState())
    val uiState: StateFlow<RecapsUploadUiState> = _uiState.asStateFlow()

    private val _userEvents = MutableStateFlow<List<Event>>(emptyList())
    val userEvents: StateFlow<List<Event>> = _userEvents.asStateFlow()

    private val _selectedEvent = MutableStateFlow<Event?>(null)
    val selectedEvent: StateFlow<Event?> = _selectedEvent.asStateFlow()

    private val _isLocationValid = MutableStateFlow(false)
    val isLocationValid: StateFlow<Boolean> = _isLocationValid.asStateFlow()

    private val _selectedMedia = MutableStateFlow<List<Uri>>(emptyList())
    val selectedMedia: StateFlow<List<Uri>> = _selectedMedia.asStateFlow()

    fun loadUserEvents() {
        viewModelScope.launch {
            try {
                authRepository.currentUser.collect { currentUser ->
                    if (currentUser != null) {
                        val events = eventRepository.getUserEvents(currentUser.id)
                        _userEvents.value = events
                    }
                }
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    error = "Failed to load events: ${e.message}"
                )
            }
        }
    }

    fun verifyLocation(eventId: String, userLatitude: Double, userLongitude: Double) {
        viewModelScope.launch {
            try {
                val eventResult = eventRepository.getEvent(eventId)
                if (eventResult.isSuccess && eventResult.getOrNull() != null) {
                    val event = eventResult.getOrNull()!!
                    val distance = calculateDistance(
                        userLatitude, userLongitude,
                        event.location.latitude, event.location.longitude
                    )
                    
                    if (distance <= 3.0) { // 3km radius
                        _isLocationValid.value = true
                        _selectedEvent.value = event
                    } else {
                        _isLocationValid.value = false
                        _uiState.value = _uiState.value.copy(
                            error = "You must be within 3km of the event location to upload a recap"
                        )
                    }
                }
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    error = "Failed to verify location: ${e.message}"
                )
            }
        }
    }

    fun setUserLocationAndVerify(eventId: String, lat: Double, lon: Double) {
        verifyLocation(eventId, lat, lon)
    }

    fun uploadRecap(
        eventId: String,
        mediaUrls: List<String>,
        caption: String
    ) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, error = null, isSuccess = false)
            
            try {
                authRepository.currentUser.collect { currentUser ->
                    if (currentUser != null) {
                        recapRepository.uploadRecap(
                            eventId = eventId,
                            userId = currentUser.id,
                            mediaUrls = mediaUrls,
                            caption = caption,
                            userLocation = null,
                            eventLocation = null
                        )
                            .onSuccess {
                                _uiState.value = _uiState.value.copy(
                                    isLoading = false,
                                    isSuccess = true
                                )
                            }
                            .onFailure { error ->
                                _uiState.value = _uiState.value.copy(
                                    isLoading = false,
                                    error = error.message
                                )
                            }
                    } else {
                        _uiState.value = _uiState.value.copy(
                            isLoading = false,
                            error = "User not authenticated"
                        )
                    }
                }
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    error = e.message
                )
            }
        }
    }

    fun selectEvent(event: Event) {
        _selectedEvent.value = event
    }

    fun addMedia(uri: Uri) {
        val currentList = _selectedMedia.value.toMutableList()
        currentList.add(uri)
        _selectedMedia.value = currentList
    }

    fun removeMedia(uri: Uri) {
        val currentList = _selectedMedia.value.toMutableList()
        currentList.remove(uri)
        _selectedMedia.value = currentList
    }

    fun clearMedia() {
        _selectedMedia.value = emptyList()
    }

    fun clearError() {
        _uiState.value = _uiState.value.copy(error = null)
    }

    fun clearSuccess() {
        _uiState.value = _uiState.value.copy(isSuccess = false)
    }

    private fun calculateDistance(
        lat1: Double, lon1: Double,
        lat2: Double, lon2: Double
    ): Double {
        val r = 6371 // Earth's radius in kilometers
        val dLat = Math.toRadians(lat2 - lat1)
        val dLon = Math.toRadians(lon2 - lon1)
        val a = sin(dLat / 2) * sin(dLat / 2) +
                cos(Math.toRadians(lat1)) * cos(Math.toRadians(lat2)) *
                sin(dLon / 2) * sin(dLon / 2)
        val c = 2 * atan2(sqrt(a), sqrt(1 - a))
        return r * c
    }
}

data class RecapsUploadUiState(
    val isLoading: Boolean = false,
    val error: String? = null,
    val isSuccess: Boolean = false
) 