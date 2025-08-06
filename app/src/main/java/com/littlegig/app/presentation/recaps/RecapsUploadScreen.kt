package com.littlegig.app.presentation.recaps

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.littlegig.app.presentation.components.*
import com.littlegig.app.presentation.theme.*
import com.littlegig.app.data.model.Event
import com.littlegig.app.data.model.ContentCategory
import androidx.compose.foundation.BorderStroke
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.delay
import java.util.*

@Composable
fun RecapsUploadScreen(
    navController: NavController,
    viewModel: RecapsUploadViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState(initial = RecapsUploadUiState())
    val isDark = isSystemInDarkTheme()
    
    var selectedImages by remember { mutableStateOf<List<Uri>>(emptyList()) }
    var caption by remember { mutableStateOf("") }
    var selectedEvent by remember { mutableStateOf<Event?>(null) }
    
    val imagePickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetMultipleContents()
    ) { uris ->
        selectedImages = uris
    }
    
    // Proper dark/light mode background
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                if (isDark) {
                    Brush.verticalGradient(
                        colors = listOf(
                            DarkBackground,
                            DarkSurface
                        )
                    )
                } else {
                    Brush.verticalGradient(
                        colors = listOf(
                            LightBackground,
                            LightSurface
                        )
                    )
                }
            )
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
                .verticalScroll(rememberScrollState())
        ) {
            // Header with neumorphic design
            AdvancedNeumorphicCard {
                Column(
                    modifier = Modifier.padding(20.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Create Recap",
                            style = MaterialTheme.typography.headlineMedium.copy(
                                fontWeight = FontWeight.Bold
                            ),
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        
                        IconButton(
                            onClick = { navController.navigateUp() }
                        ) {
                            Icon(
                                imageVector = Icons.Default.Close,
                                contentDescription = "Close",
                                tint = MaterialTheme.colorScheme.onSurface
                            )
                        }
                    }
                }
            }
            
            Spacer(modifier = Modifier.height(20.dp))
            
            // Event Selection
            AdvancedGlassmorphicCard {
                Column(
                    modifier = Modifier.padding(20.dp)
                ) {
                    Text(
                        text = "Select Event",
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.SemiBold
                        ),
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    
                    Spacer(modifier = Modifier.height(12.dp))
                    
                    // Event dropdown or list
                    if (uiState.userEvents.isNotEmpty()) {
                        LazyRow(
                            horizontalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            items(uiState.userEvents) { event ->
                                EventSelectionCard(
                                    event = event,
                                    isSelected = selectedEvent?.id == event.id,
                                    onClick = { selectedEvent = event }
                                )
                            }
                        }
                    } else {
                        Text(
                            text = "No events found. Create an event first!",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }
            
            Spacer(modifier = Modifier.height(20.dp))
            
            // Media Upload
            AdvancedGlassmorphicCard {
                Column(
                    modifier = Modifier.padding(20.dp)
                ) {
                    Text(
                        text = "Upload Photos/Videos",
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.SemiBold
                        ),
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    
                    Spacer(modifier = Modifier.height(12.dp))
                    
                    // Media picker button
                    Button(
                        onClick = { imagePickerLauncher.launch("image/*") },
                        modifier = Modifier.fillMaxWidth(),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = LittleGigPrimary
                        )
                    ) {
                        Icon(
                            imageVector = Icons.Default.PhotoCamera,
                            contentDescription = null,
                            modifier = Modifier.size(20.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Select Media")
                    }
                    
                    Spacer(modifier = Modifier.height(16.dp))
                    
                    // Selected media preview
                    if (selectedImages.isNotEmpty()) {
                        LazyRow(
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            items(selectedImages) { uri ->
                                AsyncImage(
                                    model = uri,
                                    contentDescription = null,
                                    modifier = Modifier
                                        .size(80.dp)
                                        .clip(RoundedCornerShape(8.dp)),
                                    contentScale = ContentScale.Crop
                                )
                            }
                        }
                    }
                }
            }
            
            Spacer(modifier = Modifier.height(20.dp))
            
            // Caption
            AdvancedGlassmorphicCard {
                Column(
                    modifier = Modifier.padding(20.dp)
                ) {
                    Text(
                        text = "Caption",
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.SemiBold
                        ),
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    
                    Spacer(modifier = Modifier.height(12.dp))
                    
                    OutlinedTextField(
                        value = caption,
                        onValueChange = { caption = it },
                        placeholder = { Text("Share your experience...") },
                        modifier = Modifier.fillMaxWidth(),
                        minLines = 3,
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = LittleGigPrimary,
                            unfocusedBorderColor = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.3f)
                        )
                    )
                }
            }
            
            Spacer(modifier = Modifier.height(20.dp))
            
            // Location Verification
            if (selectedEvent != null) {
                AdvancedGlassmorphicCard {
                    Column(
                        modifier = Modifier.padding(20.dp)
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = Icons.Default.LocationOn,
                                contentDescription = null,
                                tint = if (uiState.isLocationValid) Color.Green else Color.Red,
                                modifier = Modifier.size(20.dp)
                            )
                            
                            Spacer(modifier = Modifier.width(8.dp))
                            
                            Text(
                                text = if (uiState.isLocationValid) "Location Verified (Within 3km)" else "Location Check Required",
                                style = MaterialTheme.typography.bodyMedium,
                                color = if (uiState.isLocationValid) Color.Green else Color.Red
                            )
                        }
                        
                        if (!uiState.isLocationValid) {
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = "You must be within 3km of the event location to create a recap",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                }
                
                Spacer(modifier = Modifier.height(20.dp))
            }
            
            // Upload Button
            Button(
                onClick = {
                    selectedEvent?.let { event ->
                        viewModel.uploadRecap(
                            eventId = event.id,
                            images = selectedImages,
                            caption = caption
                        )
                    }
                },
                modifier = Modifier.fillMaxWidth(),
                enabled = selectedEvent != null && selectedImages.isNotEmpty() && uiState.isLocationValid,
                colors = ButtonDefaults.buttonColors(
                    containerColor = LittleGigPrimary
                )
            ) {
                Icon(
                    imageVector = Icons.Default.Upload,
                    contentDescription = null,
                    modifier = Modifier.size(20.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text("Upload Recap")
            }
            
            // Success/Error Messages
            if (uiState.isSuccess) {
                Spacer(modifier = Modifier.height(16.dp))
                AdvancedGlassmorphicCard {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.Check,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.size(24.dp)
                        )
                        Spacer(modifier = Modifier.width(12.dp))
                        Text(
                            text = "Recap uploaded successfully!",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.primary
                        )
                    }
                }
            }
            
            if (uiState.error != null) {
                Spacer(modifier = Modifier.height(16.dp))
                AdvancedGlassmorphicCard {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.Warning,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.error,
                            modifier = Modifier.size(24.dp)
                        )
                        Spacer(modifier = Modifier.width(12.dp))
                        Text(
                            text = uiState.error ?: "",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.error
                        )
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EventSelectionCard(
    event: Event,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Card(
        onClick = onClick,
        modifier = Modifier.width(200.dp),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (isSelected) LittleGigPrimary.copy(alpha = 0.1f) else MaterialTheme.colorScheme.surface
        ),
        border = if (isSelected) BorderStroke(2.dp, LittleGigPrimary) else null
    ) {
        Column(
            modifier = Modifier.padding(12.dp)
        ) {
            AsyncImage(
                model = event.imageUrls.firstOrNull() ?: "https://via.placeholder.com/200x120",
                contentDescription = event.title,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(80.dp)
                    .clip(RoundedCornerShape(8.dp)),
                contentScale = ContentScale.Crop
            )
            
            Spacer(modifier = Modifier.height(8.dp))
            
            Text(
                text = event.title,
                style = MaterialTheme.typography.bodyMedium.copy(
                    fontWeight = FontWeight.SemiBold
                ),
                color = MaterialTheme.colorScheme.onSurface,
                maxLines = 2
            )
            
            Text(
                text = event.dateTime.toString(),
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

data class RecapsUploadUiState(
    val userEvents: List<Event> = emptyList(),
    val isLocationValid: Boolean = false,
    val isLoading: Boolean = false,
    val isSuccess: Boolean = false,
    val error: String? = null
)

@HiltViewModel
class RecapsUploadViewModel @Inject constructor() : ViewModel() {
    
    private val _uiState = MutableStateFlow(RecapsUploadUiState())
    val uiState: StateFlow<RecapsUploadUiState> = _uiState.asStateFlow()
    
    init {
        loadUserEvents()
        checkLocationValidity()
    }
    
    private fun loadUserEvents() {
        viewModelScope.launch {
            // TODO: Load user's events from repository
            // For now, show mock data
            val mockEvents = listOf(
                Event(
                    id = "event1",
                    title = "Summer Music Festival",
                    description = "Amazing music festival",
                    dateTime = System.currentTimeMillis(),
                    price = 89.99,
                    capacity = 1000,
                    category = ContentCategory.EVENT,
                    imageUrls = emptyList(),
                    organizerId = "user1",
                    isActive = true
                )
            )
            
            _uiState.value = _uiState.value.copy(userEvents = mockEvents)
        }
    }
    
    private fun checkLocationValidity() {
        viewModelScope.launch {
            // TODO: Check if user is within 3km of event location
            // For now, assume valid
            _uiState.value = _uiState.value.copy(isLocationValid = true)
        }
    }
    
    fun uploadRecap(eventId: String, images: List<Uri>, caption: String) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, error = null)
            
            try {
                // TODO: Upload recap to backend
                // For now, simulate success
                delay(2000)
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    isSuccess = true
                )
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    error = e.message
                )
            }
        }
    }
} 