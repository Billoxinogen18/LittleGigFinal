package com.littlegig.app.presentation.upload

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.AddPhotoAlternate
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Error
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.PhotoCamera
import androidx.compose.material.icons.filled.Upload
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.littlegig.app.data.model.ContentCategory
import com.littlegig.app.data.model.UserType
import com.littlegig.app.presentation.components.*
import com.littlegig.app.presentation.theme.*
import com.littlegig.app.presentation.components.HapticButton
import com.littlegig.app.presentation.components.LoadingPulseAnimation
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
    
    // Places API integration
    val placeSuggestions by viewModel.placeSuggestions.collectAsState()
    val selectedPlace by viewModel.selectedPlace.collectAsState()
    val locationName by viewModel.locationName.collectAsState()
    val locationAddress by viewModel.locationAddress.collectAsState()

    // Form state
    var title by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var selectedCategory by remember { mutableStateOf(ContentCategory.EVENT) }
    var price by remember { mutableStateOf("") }
    var capacity by remember { mutableStateOf("") }
    var startDate by remember { mutableStateOf<Date?>(null) }

    var selectedImages by remember { mutableStateOf<List<Uri>>(emptyList()) }
    
    val context = LocalContext.current
    
    // Image compression function
    fun compressImage(uri: Uri): Uri {
        return try {
            val inputStream = context.contentResolver.openInputStream(uri)
            val originalBitmap = BitmapFactory.decodeStream(inputStream)
            
            // Calculate new dimensions (max 1024x1024)
            val maxSize = 1024
            val ratio = minOf(maxSize.toFloat() / originalBitmap.width, maxSize.toFloat() / originalBitmap.height)
            val newWidth = (originalBitmap.width * ratio).toInt()
            val newHeight = (originalBitmap.height * ratio).toInt()
            
            val compressedBitmap = Bitmap.createScaledBitmap(originalBitmap, newWidth, newHeight, true)
            
            // Save compressed image
            val compressedUri = Uri.parse("file://${context.cacheDir}/compressed_${System.currentTimeMillis()}.jpg")
            val outputStream = context.contentResolver.openOutputStream(compressedUri)
            if (outputStream != null) {
                compressedBitmap.compress(Bitmap.CompressFormat.JPEG, 80, outputStream)
                outputStream.close()
            }
            
            compressedUri
        } catch (e: Exception) {
            uri // Return original if compression fails
        }
    }
    
    val imagePickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetMultipleContents()
    ) { uris ->
        val compressedUris = uris.map { compressImage(it) }
        selectedImages = selectedImages + compressedUris
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
                                    selected = selectedCategory == cat,
                                    onClick = { selectedCategory = cat }
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
                            shape = RoundedCornerShape(12.dp),
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
                            shape = RoundedCornerShape(12.dp),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = LittleGigPrimary,
                                unfocusedBorderColor = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.3f)
                            )
                        )
                        
                        Spacer(modifier = Modifier.height(12.dp))
                        
                        // Beautiful Date Picker
                        NeumorphicDatePicker(
                            selectedDate = startDate,
                            onDateSelected = { startDate = it },
                            modifier = Modifier.fillMaxWidth()
                        )
                        
                        Spacer(modifier = Modifier.height(12.dp))
                        

                        
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
                                shape = RoundedCornerShape(12.dp),
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
                                shape = RoundedCornerShape(12.dp),
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
                        
                        // Location Input with Places Autocomplete
                        NeumorphicPlacesAutocomplete(
                            query = locationName,
                            onQueryChange = { newValue -> 
                                viewModel.updateLocationName(newValue)
                                viewModel.searchPlaces(newValue)
                            },
                            suggestions = placeSuggestions.map { suggestion ->
                                PlaceSuggestion(
                                    placeId = suggestion.placeId,
                                    name = suggestion.mainText,
                                    address = suggestion.secondaryText,
                                    latitude = 0.0,
                                    longitude = 0.0
                                )
                            },
                            onPlaceSelected = { place ->
                                viewModel.selectPlace(place.placeId)
                            },
                            modifier = Modifier.fillMaxWidth()
                        )
                        
                        Spacer(modifier = Modifier.height(12.dp))
                        
                        OutlinedTextField(
                            value = locationAddress,
                            onValueChange = { },
                            enabled = false,
                            label = { Text("Address") },
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(12.dp),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = LittleGigPrimary,
                                unfocusedBorderColor = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.3f)
                            )
                        )

                        if (viewModel.locationLatitude.collectAsState().value != 0.0 && viewModel.locationLongitude.collectAsState().value != 0.0) {
                            Spacer(Modifier.height(12.dp))
                            MiniMapPreview(
                                latitude = viewModel.locationLatitude.collectAsState().value,
                                longitude = viewModel.locationLongitude.collectAsState().value,
                                modifier = Modifier.fillMaxWidth()
                            )
                        }
                    }
                }
                
                Spacer(modifier = Modifier.height(20.dp))
                
                // Media Upload Section
                AdvancedGlassmorphicCard {
                    Column(
                        modifier = Modifier.padding(20.dp)
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = Icons.Default.PhotoCamera,
                                contentDescription = null,
                                tint = LittleGigPrimary,
                                modifier = Modifier.size(20.dp)
                            )
                            
                            Spacer(modifier = Modifier.width(8.dp))
                            
                            Text(
                                text = "Media",
                                style = MaterialTheme.typography.titleMedium.copy(
                                    fontWeight = FontWeight.SemiBold
                                ),
                                color = MaterialTheme.colorScheme.onSurface
                            )
                        }
                        
                        Spacer(modifier = Modifier.height(16.dp))
                        
                        // Add Media Button
                        HapticButton(
                            onClick = { imagePickerLauncher.launch("image/*") },
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            AdvancedNeumorphicCard(
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(16.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.AddPhotoAlternate,
                                        contentDescription = null,
                                        tint = LittleGigPrimary,
                                        modifier = Modifier.size(24.dp)
                                    )
                                    
                                    Spacer(modifier = Modifier.width(12.dp))
                                    
                                    Text(
                                        text = "Add Photos/Videos",
                                        style = MaterialTheme.typography.bodyLarge,
                                        color = LittleGigPrimary
                                    )
                                }
                            }
                        }
                        
                        // Selected Media Preview
                        if (selectedImages.isNotEmpty()) {
                            Spacer(modifier = Modifier.height(16.dp))
                            
                            LazyRow(
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                items(selectedImages) { uri ->
                                    Box(
                                        modifier = Modifier
                                            .size(80.dp)
                                            .clip(RoundedCornerShape(8.dp))
                                            .background(MaterialTheme.colorScheme.surfaceVariant)
                                    ) {
                                        AsyncImage(
                                            model = uri,
                                            contentDescription = null,
                                            modifier = Modifier.fillMaxSize(),
                                            contentScale = ContentScale.Crop
                                        )
                                        
                                        // Remove button
                                        IconButton(
                                            onClick = { 
                                                selectedImages = selectedImages - uri
                                            },
                                            modifier = Modifier
                                                .align(Alignment.TopEnd)
                                                .size(24.dp)
                                                .background(
                                                    Color.Red.copy(alpha = 0.8f),
                                                    CircleShape
                                                )
                                        ) {
                                            Icon(
                                                imageVector = Icons.Default.Close,
                                                contentDescription = "Remove",
                                                tint = Color.White,
                                                modifier = Modifier.size(16.dp)
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
                
                Spacer(modifier = Modifier.height(20.dp))
                
                // Post Event Button
                HapticButton(
                    onClick = {
                        viewModel.createContent(
                            title = title,
                            description = description,
                            category = selectedCategory,
                            locationName = locationName,
                            locationAddress = locationAddress,
                            city = locationAddress.split(",").firstOrNull() ?: "",
                            price = price.toDoubleOrNull() ?: 0.0,
                            capacity = capacity.toIntOrNull() ?: 0,
                            images = selectedImages,
                            startDate = startDate,
    
                        )
                    },
                    enabled = title.isNotBlank() && description.isNotBlank() && locationName.isNotBlank()
                ) {
                    AdvancedNeumorphicCard(
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            if (uiState.isLoading) {
                                LoadingPulseAnimation {
                                    CircularProgressIndicator(
                                        color = LittleGigPrimary,
                                        modifier = Modifier.size(24.dp)
                                    )
                                }
                            } else {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Upload,
                                        contentDescription = null,
                                        tint = LittleGigPrimary,
                                        modifier = Modifier.size(24.dp)
                                    )
                                    Spacer(modifier = Modifier.width(12.dp))
                                    Text(
                                        text = "Post Event",
                                        style = MaterialTheme.typography.titleMedium,
                                        color = LittleGigPrimary
                                    )
                                }
                            }
                        }
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