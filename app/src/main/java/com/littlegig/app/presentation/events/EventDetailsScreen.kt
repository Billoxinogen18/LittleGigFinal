package com.littlegig.app.presentation.events

import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import coil.compose.AsyncImage
import com.littlegig.app.data.model.Event
import com.littlegig.app.data.repository.EventRepository
import com.littlegig.app.presentation.components.*
import com.littlegig.app.presentation.theme.*
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun EventDetailsScreen(
    navController: NavController,
    eventId: String,
    viewModel: EventDetailsViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState(initial = EventDetailsUiState())
    val isDark = isSystemInDarkTheme()
    
    LaunchedEffect(eventId) {
        viewModel.loadEventDetails(eventId)
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
        when {
            uiState.isLoading -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(
                        color = LittleGigPrimary
                    )
                }
            }
            uiState.error != null -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    AdvancedGlassmorphicCard(
                        modifier = Modifier.padding(24.dp)
                    ) {
                        Column(
                            modifier = Modifier.padding(24.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Icon(
                                imageVector = Icons.Default.Error,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.error,
                                modifier = Modifier.size(48.dp)
                            )
                            Spacer(modifier = Modifier.height(16.dp))
                            Text(
                                text = "Error",
                                style = MaterialTheme.typography.headlineSmall.copy(
                                    fontWeight = FontWeight.Bold
                                ),
                                color = MaterialTheme.colorScheme.onSurface
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = uiState.error ?: "Failed to load event",
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                textAlign = TextAlign.Center
                            )
                            Spacer(modifier = Modifier.height(16.dp))
                            Button(
                                onClick = { navController.popBackStack() }
                            ) {
                                Text("Go Back")
                            }
                        }
                    }
                }
            }
            uiState.event != null -> {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(16.dp)
                ) {
                    item {
                        // Header with back button
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Surface(
                                onClick = { navController.popBackStack() },
                                modifier = Modifier.size(48.dp),
                                shape = CircleShape,
                                color = MaterialTheme.colorScheme.surface.copy(alpha = 0.8f)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.ArrowBack,
                                    contentDescription = "Back",
                                    modifier = Modifier
                                        .fillMaxSize()
                                        .padding(12.dp),
                                    tint = MaterialTheme.colorScheme.onSurface
                                )
                            }
                            
                            Text(
                                text = "Event Details",
                                style = MaterialTheme.typography.titleLarge.copy(
                                    fontWeight = FontWeight.Bold
                                ),
                                color = MaterialTheme.colorScheme.onSurface
                            )
                            
                            Spacer(modifier = Modifier.width(48.dp))
                        }
                        
                        Spacer(modifier = Modifier.height(16.dp))
                    }
                    
                    item {
                        // Event Image
                        AdvancedNeumorphicCard {
                            AsyncImage(
                                model = uiState.event?.imageUrls?.firstOrNull(),
                                contentDescription = null,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(200.dp)
                                    .clip(RoundedCornerShape(16.dp)),
                                contentScale = ContentScale.Crop
                            )
                        }
                        
                        Spacer(modifier = Modifier.height(16.dp))
                    }
                    
                    item {
                        // Event Title and Category
                        AdvancedGlassmorphicCard {
                            Column(
                                modifier = Modifier.padding(20.dp)
                            ) {
                                Text(
                                    text = uiState.event?.title ?: "",
                                    style = MaterialTheme.typography.headlineMedium.copy(
                                        fontWeight = FontWeight.Bold
                                    ),
                                    color = MaterialTheme.colorScheme.onSurface
                                )
                                
                                Spacer(modifier = Modifier.height(8.dp))
                                
                                NeumorphicCategoryChip(
                                    text = uiState.event?.category?.name?.lowercase()?.replaceFirstChar { it.uppercase() } ?: "",
                                    selected = true,
                                    onClick = { }
                                )
                            }
                        }
                        
                        Spacer(modifier = Modifier.height(16.dp))
                    }
                    
                    item {
                        // Event Details
                        AdvancedGlassmorphicCard {
                            Column(
                                modifier = Modifier.padding(20.dp)
                            ) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Info,
                                        contentDescription = null,
                                        tint = LittleGigPrimary,
                                        modifier = Modifier.size(20.dp)
                                    )
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text(
                                        text = "Event Details",
                                        style = MaterialTheme.typography.titleMedium.copy(
                                            fontWeight = FontWeight.SemiBold
                                        ),
                                        color = MaterialTheme.colorScheme.onSurface
                                    )
                                }
                                
                                Spacer(modifier = Modifier.height(16.dp))
                                
                                // Date and Time
                                Row(
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Schedule,
                                        contentDescription = null,
                                        tint = MaterialTheme.colorScheme.onSurfaceVariant,
                                        modifier = Modifier.size(20.dp)
                                    )
                                    Spacer(modifier = Modifier.width(12.dp))
                                    Text(
                                        text = formatEventDateTime(uiState.event?.dateTime),
                                        style = MaterialTheme.typography.bodyMedium,
                                        color = MaterialTheme.colorScheme.onSurface
                                    )
                                }
                                
                                Spacer(modifier = Modifier.height(12.dp))
                                
                                // Location
                                Row(
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.LocationOn,
                                        contentDescription = null,
                                        tint = MaterialTheme.colorScheme.onSurfaceVariant,
                                        modifier = Modifier.size(20.dp)
                                    )
                                    Spacer(modifier = Modifier.width(12.dp))
                                    Text(
                                        text = uiState.event?.location?.name ?: "",
                                        style = MaterialTheme.typography.bodyMedium,
                                        color = MaterialTheme.colorScheme.onSurface
                                    )
                                }
                                
                                Spacer(modifier = Modifier.height(12.dp))
                                
                                // Price
                                Row(
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.AttachMoney,
                                        contentDescription = null,
                                        tint = MaterialTheme.colorScheme.onSurfaceVariant,
                                        modifier = Modifier.size(20.dp)
                                    )
                                    Spacer(modifier = Modifier.width(12.dp))
                                    Text(
                                        text = "KSH ${uiState.event?.price ?: 0}",
                                        style = MaterialTheme.typography.bodyMedium,
                                        color = MaterialTheme.colorScheme.onSurface
                                    )
                                }
                                
                                Spacer(modifier = Modifier.height(12.dp))
                                
                                // Capacity
                                Row(
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.People,
                                        contentDescription = null,
                                        tint = MaterialTheme.colorScheme.onSurfaceVariant,
                                        modifier = Modifier.size(20.dp)
                                    )
                                    Spacer(modifier = Modifier.width(12.dp))
                                    Text(
                                        text = "${uiState.event?.ticketsSold ?: 0}/${uiState.event?.capacity ?: 0} tickets sold",
                                        style = MaterialTheme.typography.bodyMedium,
                                        color = MaterialTheme.colorScheme.onSurface
                                    )
                                }
                            }
                        }
                        
                        Spacer(modifier = Modifier.height(16.dp))
                    }
                    
                    item {
                        // Description
                        AdvancedGlassmorphicCard {
                            Column(
                                modifier = Modifier.padding(20.dp)
                            ) {
                                Text(
                                    text = "Description",
                                    style = MaterialTheme.typography.titleMedium.copy(
                                        fontWeight = FontWeight.SemiBold
                                    ),
                                    color = MaterialTheme.colorScheme.onSurface
                                )
                                
                                Spacer(modifier = Modifier.height(12.dp))
                                
                                Text(
                                    text = uiState.event?.description ?: "",
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = MaterialTheme.colorScheme.onSurface
                                )
                            }
                        }
                        
                        Spacer(modifier = Modifier.height(16.dp))
                    }
                    
                    item {
                        // Organizer Info
                        AdvancedGlassmorphicCard {
                            Column(
                                modifier = Modifier.padding(20.dp)
                            ) {
                                Text(
                                    text = "Organizer",
                                    style = MaterialTheme.typography.titleMedium.copy(
                                        fontWeight = FontWeight.SemiBold
                                    ),
                                    color = MaterialTheme.colorScheme.onSurface
                                )
                                
                                Spacer(modifier = Modifier.height(12.dp))
                                
                                Row(
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    AsyncImage(
                                        model = uiState.event?.organizerImageUrl,
                                        contentDescription = null,
                                        modifier = Modifier
                                            .size(40.dp)
                                            .clip(CircleShape),
                                        contentScale = ContentScale.Crop
                                    )
                                    
                                    Spacer(modifier = Modifier.width(12.dp))
                                    
                                    Text(
                                        text = uiState.event?.organizerName ?: "",
                                        style = MaterialTheme.typography.bodyMedium,
                                        color = MaterialTheme.colorScheme.onSurface
                                    )
                                }
                            }
                        }
                        
                        Spacer(modifier = Modifier.height(16.dp))
                    }
                    
                    item {
                        // Action Buttons
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            OutlinedButton(
                                onClick = { /* Share event */ },
                                modifier = Modifier.weight(1f)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Share,
                                    contentDescription = null,
                                    modifier = Modifier.size(20.dp)
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Text("Share")
                            }
                            
                            Button(
                                onClick = { /* Buy tickets */ },
                                modifier = Modifier.weight(1f),
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = LittleGigPrimary
                                )
                            ) {
                                Icon(
                                    imageVector = Icons.Default.ShoppingCart,
                                    contentDescription = null,
                                    modifier = Modifier.size(20.dp)
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Text("Buy Tickets")
                            }
                        }
                        
                        Spacer(modifier = Modifier.height(100.dp))
                    }
                }
            }
        }
    }
}

private fun formatEventDateTime(timestamp: Long?): String {
    if (timestamp == null) return "Date not set"
    
    val date = Date(timestamp)
    val formatter = SimpleDateFormat("MMM dd, yyyy 'at' HH:mm", Locale.getDefault())
    return formatter.format(date)
}

@HiltViewModel
class EventDetailsViewModel @Inject constructor(
    private val eventRepository: EventRepository
) : ViewModel() {
    
    private val _uiState = MutableStateFlow(EventDetailsUiState())
    val uiState: StateFlow<EventDetailsUiState> = _uiState.asStateFlow()
    
    fun loadEventDetails(eventId: String) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, error = null)
            
            try {
                val result = eventRepository.getEventById(eventId)
                result.onSuccess { event ->
                    _uiState.value = _uiState.value.copy(
                        event = event,
                        isLoading = false
                    )
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
}

data class EventDetailsUiState(
    val event: Event? = null,
    val isLoading: Boolean = false,
    val error: String? = null
) 