package com.littlegig.app.presentation.upload

import androidx.compose.animation.core.*
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.*
import androidx.compose.ui.graphics.*
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.littlegig.app.presentation.components.*
import com.littlegig.app.data.model.EventCategory

@Composable
fun ModernUploadScreen(
    navController: NavController,
    viewModel: UploadViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(
                    colors = if (isSystemInDarkTheme()) 
                        listOf(Color(0xFF0F0F23), Color(0xFF1A1A2E))
                    else 
                        listOf(Color(0xFFF8FAFC), Color(0xFFE2E8F0))
                )
            )
    ) {
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(20.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            item {
                ModernUploadHeader(
                    onBackClick = { navController.popBackStack() },
                    onPreviewClick = { /* Show preview */ }
                )
            }
            
            item {
                ModernEventImageUpload(
                    selectedImages = uiState.selectedImages,
                    onImageAdd = { viewModel.addImage(it) },
                    onImageRemove = { viewModel.removeImage(it) }
                )
            }
            
            item {
                ModernEventBasicInfo(
                    title = uiState.title,
                    description = uiState.description,
                    onTitleChange = { viewModel.updateTitle(it) },
                    onDescriptionChange = { viewModel.updateDescription(it) }
                )
            }
            
            item {
                ModernEventCategory(
                    selectedCategory = uiState.selectedCategory,
                    onCategorySelect = { viewModel.selectCategory(it) }
                )
            }
            
            item {
                ModernEventDateTime(
                    selectedDate = uiState.selectedDate,
                    selectedTime = uiState.selectedTime,
                    onDateSelect = { viewModel.selectDate(it) },
                    onTimeSelect = { viewModel.selectTime(it) }
                )
            }
            
            item {
                ModernEventLocation(
                    location = uiState.location,
                    onLocationSelect = { viewModel.selectLocation(it) }
                )
            }
            
            item {
                ModernEventPricing(
                    price = uiState.price,
                    currency = uiState.currency,
                    capacity = uiState.capacity,
                    onPriceChange = { viewModel.updatePrice(it) },
                    onCurrencyChange = { viewModel.updateCurrency(it) },
                    onCapacityChange = { viewModel.updateCapacity(it) }
                )
            }
            
            item {
                ModernPublishButton(
                    isLoading = uiState.isLoading,
                    onClick = {
                        viewModel.createEvent()
                        navController.popBackStack()
                    }
                )
            }
            
            item {
                Spacer(modifier = Modifier.height(100.dp)) // Bottom padding
            }
        }
    }
}

@Composable
private fun ModernUploadHeader(
    onBackClick: () -> Unit,
    onPreviewClick: () -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        GlassmorphicCard(
            onClick = onBackClick,
            shape = RoundedCornerShape(16.dp)
        ) {
            Icon(
                imageVector = Icons.Default.ArrowBack,
                contentDescription = "Back",
                tint = GlassOnSurface(),
                modifier = Modifier
                    .padding(12.dp)
                    .size(20.dp)
            )
        }
        
        Text(
            text = "Create Event",
            style = MaterialTheme.typography.headlineLarge.copy(
                fontWeight = FontWeight.ExtraBold,
                fontSize = 28.sp
            ),
            color = GlassOnSurface()
        )
        
        GlassmorphicCard(
            onClick = onPreviewClick,
            shape = RoundedCornerShape(16.dp)
        ) {
            Icon(
                imageVector = Icons.Default.Preview,
                contentDescription = "Preview",
                tint = GlassOnSurface(),
                modifier = Modifier
                    .padding(12.dp)
                    .size(20.dp)
            )
        }
    }
}

@Composable
private fun ModernEventImageUpload(
    selectedImages: List<String>,
    onImageAdd: (String) -> Unit,
    onImageRemove: (String) -> Unit
) {
    GlassmorphicCard(
        shape = RoundedCornerShape(20.dp)
    ) {
        Column(
            modifier = Modifier.padding(20.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Default.PhotoCamera,
                    contentDescription = null,
                    tint = GlassPrimary,
                    modifier = Modifier.size(24.dp)
                )
                Spacer(modifier = Modifier.width(12.dp))
                Text(
                    text = "Event Photos",
                    style = MaterialTheme.typography.titleLarge.copy(
                        fontWeight = FontWeight.Bold
                    ),
                    color = GlassOnSurface()
                )
            }
            
            Spacer(modifier = Modifier.height(16.dp))
            
            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                // Add photo button
                item {
                    GlassmorphicCard(
                        onClick = { /* Open image picker */ },
                        shape = RoundedCornerShape(16.dp),
                        modifier = Modifier.size(100.dp)
                    ) {
                        Column(
                            modifier = Modifier.fillMaxSize(),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.Add,
                                contentDescription = "Add Photo",
                                tint = GlassPrimary,
                                modifier = Modifier.size(32.dp)
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = "Add Photo",
                                style = MaterialTheme.typography.labelSmall,
                                color = GlassOnSurfaceVariant()
                            )
                        }
                    }
                }
                
                // Selected images
                items(selectedImages) { imageUrl ->
                    Box {
                        AsyncImage(
                            model = imageUrl,
                            contentDescription = null,
                            modifier = Modifier
                                .size(100.dp)
                                .clip(RoundedCornerShape(16.dp)),
                            contentScale = ContentScale.Crop
                        )
                        
                        GlassmorphicCard(
                            onClick = { onImageRemove(imageUrl) },
                            shape = CircleShape,
                            modifier = Modifier
                                .align(Alignment.TopEnd)
                                .offset(x = 8.dp, y = (-8).dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Close,
                                contentDescription = "Remove",
                                tint = Color.White,
                                modifier = Modifier
                                    .padding(4.dp)
                                    .size(16.dp)
                            )
                        }
                    }
                }
            }
            
            if (selectedImages.isEmpty()) {
                Spacer(modifier = Modifier.height(12.dp))
                Text(
                    text = "Add up to 5 photos to showcase your event",
                    style = MaterialTheme.typography.bodySmall,
                    color = GlassOnSurfaceVariant()
                )
            }
        }
    }
}

@Composable
private fun ModernEventBasicInfo(
    title: String,
    description: String,
    onTitleChange: (String) -> Unit,
    onDescriptionChange: (String) -> Unit
) {
    GlassmorphicCard(
        shape = RoundedCornerShape(20.dp)
    ) {
        Column(
            modifier = Modifier.padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Default.Edit,
                    contentDescription = null,
                    tint = GlassPrimary,
                    modifier = Modifier.size(24.dp)
                )
                Spacer(modifier = Modifier.width(12.dp))
                Text(
                    text = "Event Details",
                    style = MaterialTheme.typography.titleLarge.copy(
                        fontWeight = FontWeight.Bold
                    ),
                    color = GlassOnSurface()
                )
            }
            
            ModernTextField(
                value = title,
                onValueChange = onTitleChange,
                label = "Event Title",
                placeholder = "Enter event title"
            )
            
            ModernTextField(
                value = description,
                onValueChange = onDescriptionChange,
                label = "Description",
                placeholder = "Describe your event...",
                minLines = 3,
                maxLines = 5
            )
        }
    }
}

@Composable
private fun ModernEventCategory(
    selectedCategory: EventCategory?,
    onCategorySelect: (EventCategory) -> Unit
) {
    val categories = EventCategory.values()
    
    GlassmorphicCard(
        shape = RoundedCornerShape(20.dp)
    ) {
        Column(
            modifier = Modifier.padding(20.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Default.Category,
                    contentDescription = null,
                    tint = GlassPrimary,
                    modifier = Modifier.size(24.dp)
                )
                Spacer(modifier = Modifier.width(12.dp))
                Text(
                    text = "Category",
                    style = MaterialTheme.typography.titleLarge.copy(
                        fontWeight = FontWeight.Bold
                    ),
                    color = GlassOnSurface()
                )
            }
            
            Spacer(modifier = Modifier.height(16.dp))
            
            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(categories) { category ->
                    val isSelected = selectedCategory == category
                    
                    GlassmorphicCard(
                        onClick = { onCategorySelect(category) },
                        shape = RoundedCornerShape(16.dp)
                    ) {
                        Text(
                            text = category.name,
                            modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp),
                            style = MaterialTheme.typography.labelLarge.copy(
                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium
                            ),
                            color = if (isSelected) GlassPrimary else GlassOnSurfaceVariant()
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun ModernEventDateTime(
    selectedDate: String,
    selectedTime: String,
    onDateSelect: (String) -> Unit,
    onTimeSelect: (String) -> Unit
) {
    GlassmorphicCard(
        shape = RoundedCornerShape(20.dp)
    ) {
        Column(
            modifier = Modifier.padding(20.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Default.Schedule,
                    contentDescription = null,
                    tint = GlassPrimary,
                    modifier = Modifier.size(24.dp)
                )
                Spacer(modifier = Modifier.width(12.dp))
                Text(
                    text = "Date & Time",
                    style = MaterialTheme.typography.titleLarge.copy(
                        fontWeight = FontWeight.Bold
                    ),
                    color = GlassOnSurface()
                )
            }
            
            Spacer(modifier = Modifier.height(16.dp))
            
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                GlassmorphicCard(
                    onClick = { /* Show date picker */ },
                    shape = RoundedCornerShape(16.dp),
                    modifier = Modifier.weight(1f)
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp)
                    ) {
                        Text(
                            text = "Date",
                            style = MaterialTheme.typography.labelMedium,
                            color = GlassOnSurfaceVariant()
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = selectedDate.ifEmpty { "Select date" },
                            style = MaterialTheme.typography.bodyLarge.copy(
                                fontWeight = FontWeight.SemiBold
                            ),
                            color = GlassOnSurface()
                        )
                    }
                }
                
                GlassmorphicCard(
                    onClick = { /* Show time picker */ },
                    shape = RoundedCornerShape(16.dp),
                    modifier = Modifier.weight(1f)
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp)
                    ) {
                        Text(
                            text = "Time",
                            style = MaterialTheme.typography.labelMedium,
                            color = GlassOnSurfaceVariant()
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = selectedTime.ifEmpty { "Select time" },
                            style = MaterialTheme.typography.bodyLarge.copy(
                                fontWeight = FontWeight.SemiBold
                            ),
                            color = GlassOnSurface()
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun ModernEventLocation(
    location: String,
    onLocationSelect: (String) -> Unit
) {
    GlassmorphicCard(
        onClick = { /* Show location picker */ },
        shape = RoundedCornerShape(20.dp)
    ) {
        Column(
            modifier = Modifier.padding(20.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Default.LocationOn,
                    contentDescription = null,
                    tint = GlassPrimary,
                    modifier = Modifier.size(24.dp)
                )
                Spacer(modifier = Modifier.width(12.dp))
                Text(
                    text = "Location",
                    style = MaterialTheme.typography.titleLarge.copy(
                        fontWeight = FontWeight.Bold
                    ),
                    color = GlassOnSurface()
                )
            }
            
            Spacer(modifier = Modifier.height(12.dp))
            
            Text(
                text = location.ifEmpty { "Choose event location" },
                style = MaterialTheme.typography.bodyLarge,
                color = if (location.isEmpty()) GlassOnSurfaceVariant() else GlassOnSurface()
            )
            
            if (location.isNotEmpty()) {
                Spacer(modifier = Modifier.height(8.dp))
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.CheckCircle,
                        contentDescription = null,
                        tint = Color.Green,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "Location selected",
                        style = MaterialTheme.typography.bodySmall,
                        color = Color.Green
                    )
                }
            }
        }
    }
}

@Composable
private fun ModernEventPricing(
    price: String,
    currency: String,
    capacity: String,
    onPriceChange: (String) -> Unit,
    onCurrencyChange: (String) -> Unit,
    onCapacityChange: (String) -> Unit
) {
    GlassmorphicCard(
        shape = RoundedCornerShape(20.dp)
    ) {
        Column(
            modifier = Modifier.padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Default.AttachMoney,
                    contentDescription = null,
                    tint = GlassPrimary,
                    modifier = Modifier.size(24.dp)
                )
                Spacer(modifier = Modifier.width(12.dp))
                Text(
                    text = "Pricing & Capacity",
                    style = MaterialTheme.typography.titleLarge.copy(
                        fontWeight = FontWeight.Bold
                    ),
                    color = GlassOnSurface()
                )
            }
            
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                ModernTextField(
                    value = price,
                    onValueChange = onPriceChange,
                    label = "Price",
                    placeholder = "0",
                    modifier = Modifier.weight(2f),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                )
                
                ModernTextField(
                    value = currency,
                    onValueChange = onCurrencyChange,
                    label = "Currency",
                    placeholder = "KSH",
                    modifier = Modifier.weight(1f)
                )
            }
            
            ModernTextField(
                value = capacity,
                onValueChange = onCapacityChange,
                label = "Capacity",
                placeholder = "Maximum attendees",
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
            )
        }
    }
}

@Composable
private fun ModernPublishButton(
    isLoading: Boolean,
    onClick: () -> Unit
) {
    NeumorphicButton(
        onClick = onClick,
        modifier = Modifier.fillMaxWidth(),
        colors = ButtonDefaults.buttonColors(
            containerColor = GlassPrimary,
            contentColor = Color.White
        ),
        shape = RoundedCornerShape(20.dp),
        contentPadding = PaddingValues(vertical = 16.dp),
        enabled = !isLoading
    ) {
        if (isLoading) {
            CircularProgressIndicator(
                color = Color.White,
                modifier = Modifier.size(20.dp)
            )
        } else {
            Icon(
                imageVector = Icons.Default.Publish,
                contentDescription = null,
                modifier = Modifier.size(20.dp)
            )
            Spacer(modifier = Modifier.width(12.dp))
            Text(
                text = "Create Event",
                style = MaterialTheme.typography.titleMedium.copy(
                    fontWeight = FontWeight.Bold
                )
            )
        }
    }
}

@Composable
private fun ModernTextField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    placeholder: String,
    modifier: Modifier = Modifier,
    minLines: Int = 1,
    maxLines: Int = 1,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default
) {
    Column(modifier = modifier) {
        Text(
            text = label,
            style = MaterialTheme.typography.labelMedium.copy(
                fontWeight = FontWeight.SemiBold
            ),
            color = GlassOnSurfaceVariant()
        )
        
        Spacer(modifier = Modifier.height(4.dp))
        
        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            placeholder = {
                Text(
                    text = placeholder,
                    color = GlassOnSurfaceVariant().copy(alpha = 0.6f)
                )
            },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = GlassPrimary,
                unfocusedBorderColor = GlassOnSurfaceVariant().copy(alpha = 0.3f),
                focusedTextColor = GlassOnSurface(),
                unfocusedTextColor = GlassOnSurface()
            ),
            minLines = minLines,
            maxLines = maxLines,
            keyboardOptions = keyboardOptions
        )
    }
}
