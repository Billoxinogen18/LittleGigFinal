package com.littlegig.app.presentation.upload

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.PhotoCamera
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.littlegig.app.data.model.ContentCategory
import com.littlegig.app.data.model.UserType
import com.littlegig.app.presentation.components.*
import com.littlegig.app.presentation.theme.*
// import com.littlegig.app.services.PlacesService
import com.littlegig.app.presentation.events.NeumorphicCategoryChip
import com.littlegig.app.presentation.components.NeumorphicDatePicker
import com.littlegig.app.presentation.components.NeumorphicPlacesAutocomplete
import com.littlegig.app.presentation.components.PlaceSuggestion
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UploadScreen(
    navController: NavController,
    viewModel: UploadViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState(initial = UploadUiState())
    val currentUser by viewModel.currentUser.collectAsState(initial = null)
    val isDark = isSystemInDarkTheme()
    
    var title by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var category by remember { mutableStateOf(ContentCategory.EVENT) }
    var locationName by remember { mutableStateOf("") }
    var locationAddress by remember { mutableStateOf("") }
    var city by remember { mutableStateOf("") }
    var price by remember { mutableStateOf("") }
    var capacity by remember { mutableStateOf("") }
    var selectedImages by remember { mutableStateOf<List<Uri>>(emptyList()) }
    var selectedDate by remember { mutableStateOf<Date?>(null) }
    var selectedEndDate by remember { mutableStateOf<Date?>(null) }
    
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
        // Check if user can upload content
        if (currentUser?.userType != UserType.BUSINESS && currentUser?.userType != UserType.ADMIN) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                AdvancedNeumorphicCard(
                    modifier = Modifier.padding(24.dp)
                ) {
                    Column(
                        modifier = Modifier.padding(24.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "Business Account Required",
                            style = MaterialTheme.typography.titleLarge.copy(
                                fontWeight = FontWeight.Bold
                            ),
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "You need a business account to upload events and content",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Button(
                            onClick = { 
                                // Navigate to account upgrade
                                navController.navigate("account")
                            }
                        ) {
                            Text("Upgrade Account")
                        }
                    }
                }
            }
        } else {
            // Main upload content
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .padding(16.dp)
            ) {
                // Header with neumorphic design
                AdvancedNeumorphicCard {
                    Column(
                        modifier = Modifier.padding(20.dp)
                    ) {
                        Text(
                            text = "Create Content",
                            style = MaterialTheme.typography.headlineMedium.copy(
                                fontWeight = FontWeight.Bold
                            ),
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        
                        Spacer(modifier = Modifier.height(8.dp))
                        
                        Text(
                            text = "Share events, venues, and experiences with the community",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
                
                Spacer(modifier = Modifier.height(20.dp))
                
                // Content Type Selection
                AdvancedGlassmorphicCard {
                    Column(
                        modifier = Modifier.padding(20.dp)
                    ) {
                        Text(
                            text = "Content Type",
                            style = MaterialTheme.typography.titleMedium.copy(
                                fontWeight = FontWeight.SemiBold
                            ),
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        
                        Spacer(modifier = Modifier.height(12.dp))
                        
                        LazyRow(
                            horizontalArrangement = Arrangement.spacedBy(12.dp),
                            contentPadding = PaddingValues(horizontal = 4.dp)
                        ) {
                            items(ContentCategory.values()) { cat ->
                                NeumorphicCategoryChip(
                                    text = cat.name.lowercase().replaceFirstChar { it.uppercase() },
                                    selected = category == cat,
                                    onClick = { category = cat }
                                )
                            }
                        }
                    }
                }
                
                Spacer(modifier = Modifier.height(20.dp))
                
                // Basic Information
                AdvancedGlassmorphicCard {
                    Column(
                        modifier = Modifier.padding(20.dp)
                    ) {
                        Text(
                            text = "Basic Information",
                            style = MaterialTheme.typography.titleMedium.copy(
                                fontWeight = FontWeight.SemiBold
                            ),
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        
                        Spacer(modifier = Modifier.height(16.dp))
                        
                        OutlinedTextField(
                            value = title,
                            onValueChange = { title = it },
                            label = { Text("Title") },
                            modifier = Modifier.fillMaxWidth(),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = LittleGigPrimary,
                                unfocusedBorderColor = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.3f)
                            )
                        )
                        
                        Spacer(modifier = Modifier.height(12.dp))
                        
                        OutlinedTextField(
                            value = description,
                            onValueChange = { description = it },
                            label = { Text("Description") },
                            modifier = Modifier.fillMaxWidth(),
                            minLines = 3,
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = LittleGigPrimary,
                                unfocusedBorderColor = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.3f)
                            )
                        )
                        
                        Spacer(modifier = Modifier.height(12.dp))
                        
                        // Beautiful Date Picker
                        NeumorphicDatePicker(
                            selectedDate = selectedDate,
                            onDateSelected = { selectedDate = it },
                            modifier = Modifier.fillMaxWidth()
                        )
                        
                        Spacer(modifier = Modifier.height(12.dp))
                        
                        // End Date Picker
                        NeumorphicDatePicker(
                            selectedDate = selectedEndDate,
                            onDateSelected = { selectedEndDate = it },
                            modifier = Modifier.fillMaxWidth()
                        )
                        
                        Spacer(modifier = Modifier.height(12.dp))
                        
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            OutlinedTextField(
                                value = price,
                                onValueChange = { price = it },
                                label = { Text("Price (KSH)") },
                                modifier = Modifier.weight(1f),
                                colors = OutlinedTextFieldDefaults.colors(
                                    focusedBorderColor = LittleGigPrimary,
                                    unfocusedBorderColor = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.3f)
                                )
                            )
                            
                            OutlinedTextField(
                                value = capacity,
                                onValueChange = { capacity = it },
                                label = { Text("Capacity") },
                                modifier = Modifier.weight(1f),
                                colors = OutlinedTextFieldDefaults.colors(
                                    focusedBorderColor = LittleGigPrimary,
                                    unfocusedBorderColor = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.3f)
                                )
                            )
                        }
                    }
                }
                
                Spacer(modifier = Modifier.height(20.dp))
                
                // Location Information
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
                                tint = LittleGigPrimary,
                                modifier = Modifier.size(20.dp)
                            )
                            
                            Spacer(modifier = Modifier.width(8.dp))
                            
                            Text(
                                text = "Location",
                                style = MaterialTheme.typography.titleMedium.copy(
                                    fontWeight = FontWeight.SemiBold
                                ),
                                color = MaterialTheme.colorScheme.onSurface
                            )
                        }
                        
                        Spacer(modifier = Modifier.height(16.dp))
                        
                        // Places Autocomplete
                        NeumorphicPlacesAutocomplete(
                            query = locationName,
                            onQueryChange = { locationName = it },
                            suggestions = emptyList(), // TODO: Implement Google Places API
                            onPlaceSelected = { place ->
                                locationName = place.name
                                // TODO: Save place details including lat/lng
                            },
                            modifier = Modifier.fillMaxWidth()
                        )
                        
                        Spacer(modifier = Modifier.height(12.dp))
                        
                        OutlinedTextField(
                            value = locationAddress,
                            onValueChange = { locationAddress = it },
                            label = { Text("Address") },
                            modifier = Modifier.fillMaxWidth(),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = LittleGigPrimary,
                                unfocusedBorderColor = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.3f)
                            )
                        )
                    }
                }
                
                Spacer(modifier = Modifier.height(20.dp))
                
                // Post Event Button
                AdvancedNeumorphicCard {
                    Button(
                        onClick = {
                            viewModel.createContent(
                                title = title,
                                description = description,
                                category = category,
                                locationName = locationName,
                                locationAddress = locationAddress,
                                city = city,
                                price = price.toDoubleOrNull() ?: 0.0,
                                capacity = capacity.toIntOrNull() ?: 0,
                                images = selectedImages,
                                startDate = selectedDate,
                                endDate = selectedEndDate
                            )
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(20.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = LittleGigPrimary
                        ),
                        enabled = title.isNotBlank() && description.isNotBlank() && locationName.isNotBlank()
                    ) {
                        Icon(
                            imageVector = Icons.Default.Add,
                            contentDescription = null,
                            modifier = Modifier.size(20.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "Post Event",
                            style = MaterialTheme.typography.titleMedium.copy(
                                fontWeight = FontWeight.Bold
                            )
                        )
                    }
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
                                text = "Event posted successfully!",
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
                
                // Bottom padding for navigation bar
                Spacer(modifier = Modifier.height(100.dp))
            }
        }
    }
}