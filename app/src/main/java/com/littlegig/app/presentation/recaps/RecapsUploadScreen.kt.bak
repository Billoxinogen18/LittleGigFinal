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
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.platform.LocalContext
import android.Manifest
import androidx.compose.runtime.SideEffect
import androidx.activity.compose.ManagedActivityResultLauncher
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts.RequestPermission
import com.google.android.gms.location.LocationServices

@Composable
fun RecapsUploadScreen(
    navController: NavController,
    viewModel: RecapsUploadViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState(initial = RecapsUploadUiState())
    val userEvents by viewModel.userEvents.collectAsState()
    val selectedEvent by viewModel.selectedEvent.collectAsState()
    val isLocationValid by viewModel.isLocationValid.collectAsState()
    val selectedMedia by viewModel.selectedMedia.collectAsState()
    val isDark = isSystemInDarkTheme()
    
    var selectedImages by remember { mutableStateOf<List<Uri>>(emptyList()) }
    var caption by remember { mutableStateOf("") }
    var selectedChallenge by remember { mutableStateOf<String?>(null) }
    
    val imagePickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetMultipleContents()
    ) { uris ->
        selectedImages = uris
    }
    val context = LocalContext.current
    val coarsePermissionLauncher = rememberLauncherForActivityResult(RequestPermission()) { granted ->
        if (granted) {
            val fused = LocationServices.getFusedLocationProviderClient(context)
            fused.lastLocation.addOnSuccessListener { loc ->
                selectedEvent?.let { if (loc != null) viewModel.setUserLocationAndVerify(it.id, loc.latitude, loc.longitude) }
            }
        }
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
            
            Spacer(modifier = Modifier.height(12.dp))
            // Glass step progress - Event → Media → Caption → Location → Upload
            val currentStep = remember(selectedEvent, selectedImages, caption, isLocationValid) {
                var step = 1
                if (selectedEvent != null) step = 2
                if (selectedImages.isNotEmpty()) step = 3
                if (caption.isNotBlank()) step = 4
                if (isLocationValid) step = 5
                step
            }
            GlassStepProgress(
                steps = listOf("Event", "Media", "Caption", "Location", "Upload"),
                currentStep = currentStep
            )

            Spacer(modifier = Modifier.height(20.dp))
            
            // Event Selection
            AdvancedLiquidGlassCard {
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
                    if (userEvents.isNotEmpty()) {
                        LazyRow(
                            horizontalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            items(userEvents) { event ->
                                EventSelectionCard(
                                    event = event,
                                    isSelected = selectedEvent?.id == event.id,
                                    onClick = {
                                        viewModel.selectEvent(event)
                                        // Request location permission then verify location
                                        coarsePermissionLauncher.launch(Manifest.permission.ACCESS_COARSE_LOCATION)
                                    }
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
            AdvancedLiquidGlassCard {
                Column(
                    modifier = Modifier.padding(20.dp)
                ) {
                    // Weekly Challenge Selector
                    Text(
                        text = "Weekly Challenges",
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.SemiBold),
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    val challenges = listOf("#NightVibes", "#StreetBeats", "#CampusWave", "#FoodieFinds")
                    LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        items(challenges) { tag ->
                            AssistChip(
                                onClick = { selectedChallenge = if (selectedChallenge == tag) null else tag },
                                label = { Text(tag) }
                            )
                        }
                    }
                    Spacer(modifier = Modifier.height(16.dp))
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
            AdvancedLiquidGlassCard {
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
                AdvancedLiquidGlassCard {
                    Column(
                        modifier = Modifier.padding(20.dp)
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = Icons.Default.LocationOn,
                                contentDescription = null,
                                tint = if (isLocationValid) Color.Green else Color.Red,
                                modifier = Modifier.size(20.dp)
                            )
                            
                            Spacer(modifier = Modifier.width(8.dp))
                            
                            Text(
                                text = if (isLocationValid) "Location Verified (Within 3km)" else "Location Check Required",
                                style = MaterialTheme.typography.bodyMedium,
                                color = if (isLocationValid) Color.Green else Color.Red
                            )
                        }
                        
                        if (!isLocationValid) {
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
                        // Upload recap to backend
                        viewModel.uploadRecap(
                            eventId = selectedEvent?.id ?: "",
                            mediaUrls = selectedImages.map { it.toString() },
                            caption = caption
                        )
                    }
                },
                modifier = Modifier.fillMaxWidth(),
                enabled = selectedEvent != null && selectedImages.isNotEmpty() && isLocationValid,
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
                AdvancedLiquidGlassCard {
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
                AdvancedLiquidGlassCard {
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

@Composable
private fun GlassStepProgress(steps: List<String>, currentStep: Int) {
    AdvancedLiquidGlassCard {
        Column(Modifier.fillMaxWidth().padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                steps.forEachIndexed { idx, label ->
                    val active = (idx + 1) <= currentStep
                    AssistChip(
                        onClick = {},
                        label = { Text(label) },
                        leadingIcon = { if (active) Icon(Icons.Default.Check, contentDescription = null, tint = LittleGigPrimary) },
                        enabled = false
                    )
                }
            }
            Spacer(Modifier.height(12.dp))
            val progress = (currentStep - 1).coerceIn(0, steps.lastIndex) / steps.lastIndex.toFloat()
            LinearProgressIndicator(
                progress = progress,
                modifier = Modifier.fillMaxWidth().height(8.dp).clip(RoundedCornerShape(8.dp)),
                color = LittleGigPrimary,
                trackColor = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.2f)
            )
        }
    }
} 