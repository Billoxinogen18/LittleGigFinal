package com.littlegig.app.presentation.upload

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.littlegig.app.data.model.ContentCategory
import com.littlegig.app.data.model.Event
import com.littlegig.app.data.model.Location
import com.littlegig.app.data.repository.AuthRepository
import com.littlegig.app.data.repository.EventRepository
import com.littlegig.app.services.PlacesService
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
    private val authRepository: AuthRepository,
    private val placesService: PlacesService
) : ViewModel() {
    
    private val _uiState = MutableStateFlow(UploadUiState())
    val uiState: StateFlow<UploadUiState> = _uiState.asStateFlow()
    
    val currentUser = authRepository.currentUser
    
    // Location state variables
    private val _locationName = MutableStateFlow("")
    val locationName: StateFlow<String> = _locationName.asStateFlow()
    
    private val _locationAddress = MutableStateFlow("")
    val locationAddress: StateFlow<String> = _locationAddress.asStateFlow()
    
    private val _locationLatitude = MutableStateFlow(0.0)
    val locationLatitude: StateFlow<Double> = _locationLatitude.asStateFlow()
    
    private val _locationLongitude = MutableStateFlow(0.0)
    val locationLongitude: StateFlow<Double> = _locationLongitude.asStateFlow()
    
    // Google Places API integration
    private val _placeSuggestions = MutableStateFlow<List<PlacesService.PlaceSuggestion>>(emptyList())
    val placeSuggestions: StateFlow<List<PlacesService.PlaceSuggestion>> = _placeSuggestions.asStateFlow()
    
    private val _selectedPlace = MutableStateFlow<PlacesService.PlaceDetails?>(null)
    val selectedPlace: StateFlow<PlacesService.PlaceDetails?> = _selectedPlace.asStateFlow()
    
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
                        latitude = _locationLatitude.value,
                        longitude = _locationLongitude.value
                    ),
                    dateTime = startDate?.time ?: System.currentTimeMillis(),

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
        viewModelScope.launch {
            if (query.length >= 3) {
                val suggestions = placesService.getPlaceSuggestions(query)
                _placeSuggestions.value = suggestions
            } else {
                _placeSuggestions.value = emptyList()
            }
        }
    }
    
    fun selectPlace(placeId: String) {
        viewModelScope.launch {
            val placeDetails = placesService.getPlaceDetails(placeId)
            _selectedPlace.value = placeDetails
            placeDetails?.let { place ->
                _locationName.value = place.name
                _locationAddress.value = place.formattedAddress
                _locationLatitude.value = place.latLng.latitude
                _locationLongitude.value = place.latLng.longitude
            }
            _placeSuggestions.value = emptyList()
        }
    }
    
    fun clearPlaceSuggestions() {
        _placeSuggestions.value = emptyList()
    }
    
    fun setSelectedPlace(place: com.littlegig.app.presentation.components.PlaceSuggestion) {
        selectPlace(place.placeId)
    }
}

data class UploadUiState(
    val isLoading: Boolean = false,
    val isSuccess: Boolean = false,
    val error: String? = null
)