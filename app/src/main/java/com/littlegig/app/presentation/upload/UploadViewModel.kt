package com.littlegig.app.presentation.upload

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.littlegig.app.data.model.ContentCategory
import com.littlegig.app.data.model.Event
import com.littlegig.app.data.model.Location
import com.littlegig.app.data.repository.AuthRepository
import com.littlegig.app.data.repository.EventRepository
// import com.littlegig.app.services.PlacesService
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import java.util.*
import javax.inject.Inject

@HiltViewModel
class UploadViewModel @Inject constructor(
    private val eventRepository: EventRepository,
    private val authRepository: AuthRepository
) : ViewModel() {
    
    private val _uiState = MutableStateFlow(UploadUiState())
    val uiState: StateFlow<UploadUiState> = _uiState.asStateFlow()
    
    val currentUser = authRepository.currentUser
    
    fun createContent(
        title: String,
        description: String,
        category: ContentCategory,
        locationName: String,
        locationAddress: String,
        city: String,
        price: Double,
        capacity: Int,
        images: List<Uri>,
        startDate: Date?,
        endDate: Date?
    ) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, error = null, isSuccess = false)
            
            authRepository.currentUser.first()?.let { user ->
                // In a real app, you would upload images to Firebase Storage first
                // and get their download URLs
                val imageUrls = images.map { it.toString() } // Placeholder
                
                val event = Event(
                    title = title,
                    description = description,
                    category = category,
                    imageUrls = imageUrls,
                    location = Location(
                        name = locationName,
                        address = locationAddress,
                        city = city,
                        // In a real app, you would geocode the address to get lat/lng
                        latitude = 0.0,
                        longitude = 0.0
                    ),
                    dateTime = startDate?.time ?: System.currentTimeMillis(),
                    endDateTime = endDate?.time,
                    price = price,
                    capacity = capacity,
                    organizerId = user.id,
                    organizerName = user.displayName,
                    organizerImageUrl = user.profileImageUrl
                )
                
                eventRepository.createEvent(event)
                    .onSuccess { eventId ->
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
            } ?: run {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    error = "User not authenticated"
                )
            }
        }
    }
    
    fun clearMessages() {
        _uiState.value = _uiState.value.copy(error = null, isSuccess = false)
    }
    
    fun searchPlaces(query: String) {
        // TODO: Implement Google Places API
    }
    
    fun setSelectedPlace(place: com.littlegig.app.presentation.components.PlaceSuggestion) {
        // TODO: Implement place selection
    }
}

data class UploadUiState(
    val isLoading: Boolean = false,
    val isSuccess: Boolean = false,
    val error: String? = null
)