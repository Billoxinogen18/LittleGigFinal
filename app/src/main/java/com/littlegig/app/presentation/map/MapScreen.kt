package com.littlegig.app.presentation.map

import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.MyLocation
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MapStyleOptions
import com.google.maps.android.compose.*
import com.littlegig.app.data.model.Event
import com.littlegig.app.presentation.components.*
import com.littlegig.app.presentation.theme.*

@Composable
fun MapScreen(
    navController: NavController,
    viewModel: MapViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState(initial = MapUiState())
    val context = LocalContext.current
    val isDark = isSystemInDarkTheme()
    
    // Default location (Nairobi, Kenya)
    val defaultLocation = LatLng(-1.2921, 36.8219)
    var cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(defaultLocation, 12f)
    }
    
    // Proper dark/light mode background
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                if (isDark) {
                    androidx.compose.ui.graphics.Brush.verticalGradient(
                        colors = listOf(
                            DarkBackground,
                            DarkSurface
                        )
                    )
                } else {
                    androidx.compose.ui.graphics.Brush.verticalGradient(
                        colors = listOf(
                            LightBackground,
                            LightSurface
                        )
                    )
                }
            )
    ) {
        GoogleMap(
            modifier = Modifier.fillMaxSize(),
            cameraPositionState = cameraPositionState,
            properties = MapProperties(
                isMyLocationEnabled = false, // Will be enabled after permission check
                mapStyleOptions = if (isDark) {
                    MapStyleOptions.loadRawResourceStyle(context, com.littlegig.app.R.raw.map_style_dark)
                } else {
                    MapStyleOptions.loadRawResourceStyle(context, com.littlegig.app.R.raw.map_style_light)
                }
            ),
            uiSettings = MapUiSettings(
                zoomControlsEnabled = false,
                myLocationButtonEnabled = false
            )
        ) {
            // Add markers for events
            uiState.events.forEach { event ->
                if (event.location.latitude != 0.0 && event.location.longitude != 0.0) {
                    Marker(
                        state = MarkerState(
                            position = LatLng(
                                event.location.latitude,
                                event.location.longitude
                            )
                        ),
                        title = event.title,
                        snippet = "${event.location.name} • ${event.currency} ${event.price}",
                        onInfoWindowClick = {
                            // Navigate to event details
                        }
                    )
                }
            }
        }
        
        // Header Card with neumorphic design
        AdvancedGlassmorphicCard(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Column(
                modifier = Modifier.padding(16.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = "LittleMap",
                            style = MaterialTheme.typography.headlineSmall.copy(
                                fontWeight = FontWeight.Bold
                            ),
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Text(
                            text = "${uiState.events.size} events nearby",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                    
                    FloatingActionButton(
                        onClick = {
                            // Center map on user location
                        },
                        containerColor = LittleGigPrimary,
                        contentColor = androidx.compose.ui.graphics.Color.White
                    ) {
                        Icon(
                            imageVector = Icons.Default.MyLocation,
                            contentDescription = "My Location"
                        )
                    }
                }
            }
        }
    }
}