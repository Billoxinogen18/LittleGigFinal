package com.littlegig.app.services

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.*
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import com.google.android.gms.tasks.CancellationToken
import com.google.android.gms.tasks.CancellationTokenSource
import com.google.android.gms.tasks.OnTokenCanceledListener
import com.littlegig.app.data.repository.AuthRepository
import com.google.firebase.functions.FirebaseFunctions
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.launch
import kotlinx.coroutines.delay
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class LocationService @Inject constructor(
    @ApplicationContext private val context: Context,
    private val authRepository: AuthRepository,
    private val functions: FirebaseFunctions
) {
    
    private val fusedLocationClient: FusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(context)
    
    private val _isActiveNow = MutableStateFlow(false)
    val isActiveNow: StateFlow<Boolean> = _isActiveNow.asStateFlow()
    
    private val _locationPermissionGranted = MutableStateFlow(false)
    val locationPermissionGranted: StateFlow<Boolean> = _locationPermissionGranted.asStateFlow()
    private var pingsJob: kotlinx.coroutines.Job? = null

    fun requestLocationPermission(activity: Activity) {
        if (ContextCompat.checkSelfPermission(
                activity,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                activity,
                arrayOf(
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                ),
                LOCATION_PERMISSION_REQUEST_CODE
            )
        } else {
            _locationPermissionGranted.value = true
        }
    }

    fun ensurePermission(activity: Activity?) {
        if (activity != null) requestLocationPermission(activity)
    }
    
    suspend fun toggleActiveNow(isActive: Boolean) {
        try {
            val currentUser = authRepository.currentUser.value
            if (currentUser != null) {
                // Always update the local state first
                _isActiveNow.value = isActive
                if (isActive && pingsJob == null) startPings() else if (!isActive) stopPings()
                
                // Check permission status
                val hasPermission = ContextCompat.checkSelfPermission(
                    context,
                    Manifest.permission.ACCESS_FINE_LOCATION
                ) == PackageManager.PERMISSION_GRANTED
                _locationPermissionGranted.value = hasPermission
                
                if (hasPermission) {
                    // Update backend with active status
                    val location = getCurrentLocation()
                    if (location != null) {
                        // Call Firebase Cloud Function to update active status
                        try {
                            val uid = authRepository.currentUser.value?.id
                            functions.getHttpsCallable("updateUserLocation").call(
                                mapOf(
                                    "latitude" to location.latitude,
                                    "longitude" to location.longitude,
                                    "isActive" to isActive,
                                    "userId" to (uid ?: "")
                                )
                            ).await()
                        } catch (e: Exception) {
                            println("🔥 DEBUG: Failed to update location: ${e.message}")
                        }
                    } else {
                        // Still update backend even without precise location
                        try {
                            val uid = authRepository.currentUser.value?.id
                            functions.getHttpsCallable("updateUserLocation").call(
                                mapOf(
                                    "latitude" to 0.0,
                                    "longitude" to 0.0,
                                    "isActive" to isActive,
                                    "userId" to (uid ?: "")
                                )
                            ).await()
                        } catch (e: Exception) {
                            println("🔥 DEBUG: Failed to update active status (no location): ${e.message}")
                        }
                    }
                } else {
                    // No permission granted: still mark active state in backend (without location)
                    try {
                        val uid = authRepository.currentUser.value?.id
                        functions.getHttpsCallable("updateUserLocation").call(
                            mapOf(
                                "latitude" to 0.0,
                                "longitude" to 0.0,
                                "isActive" to isActive,
                                "userId" to (uid ?: "")
                            )
                        ).await()
                    } catch (e: Exception) {
                        println("🔥 DEBUG: Failed to update active status (permission missing): ${e.message}")
                    }
                }
            }
        } catch (e: Exception) {
            println("🔥 DEBUG: toggleActiveNow error: ${e.message}")
        }
    }
    
    private suspend fun getCurrentLocation(): android.location.Location? {
        return try {
            if (_locationPermissionGranted.value) {
                fusedLocationClient.getCurrentLocation(
                    Priority.PRIORITY_HIGH_ACCURACY,
                    object : CancellationToken() {
                        override fun onCanceledRequested(listener: OnTokenCanceledListener) = CancellationTokenSource().token
                        override fun isCancellationRequested() = false
                    }
                ).await()
            } else {
                null
            }
        } catch (e: Exception) {
            null
        }
    }
    
    private fun startPings() {
        stopPings()
        pingsJob = kotlinx.coroutines.CoroutineScope(kotlinx.coroutines.Dispatchers.IO).launch {
            while (_isActiveNow.value) {
                try {
                    // reuse toggle to send heartbeat without changing state
                    val location = getCurrentLocation()
                    val uid = authRepository.currentUser.value?.id
                    functions.getHttpsCallable("updateUserLocation").call(
                        mapOf(
                            "latitude" to (location?.latitude ?: 0.0),
                            "longitude" to (location?.longitude ?: 0.0),
                            "isActive" to true,
                            "userId" to (uid ?: "")
                        )
                    ).await()
                } catch (_: Exception) {}
                kotlinx.coroutines.delay(15000)
            }
        }
    }
    
    private fun stopPings() {
        pingsJob?.cancel()
        pingsJob = null
    }
    
    companion object {
        const val LOCATION_PERMISSION_REQUEST_CODE = 1001
    }
} 