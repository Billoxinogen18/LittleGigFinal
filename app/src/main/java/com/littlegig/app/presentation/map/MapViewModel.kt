package com.littlegig.app.presentation.map

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.gms.maps.model.LatLng
import com.littlegig.app.data.model.Event
import com.littlegig.app.data.repository.EventRepository
import com.littlegig.app.data.repository.AuthRepository
import com.google.firebase.functions.FirebaseFunctions
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlinx.coroutines.tasks.await

@HiltViewModel
class MapViewModel @Inject constructor(
    private val eventRepository: EventRepository,
    private val authRepository: AuthRepository,
    private val functions: FirebaseFunctions
) : ViewModel() {
    
    private val _uiState = MutableStateFlow(MapUiState())
    val uiState: StateFlow<MapUiState> = _uiState.asStateFlow()
    
    init {
        loadEventsWithLocation()
    }
    
    private fun loadEventsWithLocation() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true)
            
            eventRepository.getAllEvents().collect { allEvents ->
                // Filter events that have valid location coordinates
                val eventsWithLocation = allEvents.filter { event ->
                    event.location.latitude != 0.0 && event.location.longitude != 0.0
                }
                
                _uiState.value = _uiState.value.copy(
                    events = eventsWithLocation,
                    isLoading = false
                )
            }
        }
    }
    
    fun getCurrentLocation() {
        // In a real implementation, you would request location permission
        // and get the actual user location using LocationManager or FusedLocationProviderClient
        // For now, we'll use a default location (Nairobi, Kenya)
        val defaultLocation = LatLng(-1.2921, 36.8219)
        _uiState.value = _uiState.value.copy(userLocation = defaultLocation)
    }
    
    suspend fun getActiveUsersNearby(lat: Double, lon: Double, radiusKm: Int = 5): List<LatLng> {
        return try {
            val data = mapOf(
                "latitude" to lat,
                "longitude" to lon,
                "radius" to radiusKm
            )
            val result = functions.getHttpsCallable("getActiveUsersNearby").call(data).await().data as? Map<*, *>
            val users = result?.get("users") as? List<Map<*, *>> ?: emptyList()
            users.mapNotNull {
                val u = it as? Map<String, Any>
                val loc = (u?.get("location") as? Map<String, Any>)
                val la = (loc?.get("latitude") as? Number)?.toDouble()
                val lo = (loc?.get("longitude") as? Number)?.toDouble()
                if (la != null && lo != null) LatLng(la, lo) else null
            }
        } catch (e: Exception) {
            emptyList()
        }
    }
    
    fun searchEventsNearLocation(location: LatLng, radiusKm: Double = 10.0) {
        viewModelScope.launch {
            eventRepository.getAllEvents().collect { allEvents ->
                val nearbyEvents = allEvents.filter { event ->
                    if (event.location.latitude == 0.0 || event.location.longitude == 0.0) {
                        false
                    } else {
                        val distance = calculateDistance(
                            location.latitude, location.longitude,
                            event.location.latitude, event.location.longitude
                        )
                        distance <= radiusKm
                    }
                }
                
                _uiState.value = _uiState.value.copy(events = nearbyEvents)
            }
        }
    }
    
    private fun calculateDistance(
        lat1: Double, lon1: Double,
        lat2: Double, lon2: Double
    ): Double {
        val earthRadius = 6371.0 // Earth's radius in kilometers
        
        val dLat = Math.toRadians(lat2 - lat1)
        val dLon = Math.toRadians(lon2 - lon1)
        
        val a = Math.sin(dLat / 2) * Math.sin(dLat / 2) +
                Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2)) *
                Math.sin(dLon / 2) * Math.sin(dLon / 2)
        
        val c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a))
        
        return earthRadius * c
    }
    
    fun refreshEvents() {
        loadEventsWithLocation()
    }
    
    fun clearError() {
        _uiState.value = _uiState.value.copy(error = null)
    }
}

data class MapUiState(
    val events: List<Event> = emptyList(),
    val userLocation: LatLng? = null,
    val isLoading: Boolean = false,
    val error: String? = null
)