package com.littlegig.app.presentation.events

import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
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
import com.littlegig.app.data.model.Event
import com.littlegig.app.presentation.components.*
import com.littlegig.app.presentation.theme.*
import com.littlegig.app.presentation.events.NeumorphicCategoryChip
import androidx.compose.ui.unit.dp
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.foundation.clickable

@Composable
fun EventsScreen(
    navController: NavController,
    viewModel: EventsViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState(initial = EventsUiState())
    val isDark = isSystemInDarkTheme()
    
    // TikTok-style full screen events feed
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
        when {
            uiState.isLoading -> {
                // Loading state with neumorphic design
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    AdvancedGlassmorphicCard {
                        Column(
                            modifier = Modifier.padding(32.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            CircularProgressIndicator(
                                color = LittleGigPrimary,
                                modifier = Modifier.size(48.dp)
                            )
                            Spacer(modifier = Modifier.height(16.dp))
                            Text(
                                text = "Loading events...",
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                        }
                    }
                }
            }
            
            uiState.events.isEmpty() -> {
                // Beautiful empty state with neumorphic design
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    AdvancedGlassmorphicCard {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(32.dp),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center
                        ) {
                            // Glowing orb effect
                            Box(
                                modifier = Modifier.size(80.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Event,
                                    contentDescription = null,
                                    modifier = Modifier
                                        .fillMaxSize()
                                        .padding(16.dp),
                                    tint = MaterialTheme.colorScheme.onSurface
                                )
                            }
                            
                            Spacer(modifier = Modifier.height(24.dp))
                            
                            Text(
                                text = "No events found",
                                style = MaterialTheme.typography.headlineSmall.copy(
                                    fontWeight = FontWeight.Bold
                                ),
                                color = MaterialTheme.colorScheme.onSurface
                            )
                            
                            Spacer(modifier = Modifier.height(8.dp))
                            
                            Text(
                                text = "Be the first to create an amazing event!",
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                textAlign = androidx.compose.ui.text.style.TextAlign.Center
                            )
                            
                            Spacer(modifier = Modifier.height(20.dp))
                            
                            Button(
                                onClick = { /* TODO: Navigate to create event */ },
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = LittleGigPrimary
                                )
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Add,
                                    contentDescription = null,
                                    modifier = Modifier.size(16.dp)
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Text("Create Event")
                            }
                        }
                    }
                }
            }
            
            else -> {
                // TikTok-style full screen events feed
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(0.dp),
                    verticalArrangement = Arrangement.spacedBy(0.dp)
                ) {
                    items(uiState.events) { event ->
                        TikTokStyleEventCard(
                            event = event,
                            onEventClick = { /* Navigate to event details */ },
                            onLikeClick = { viewModel.toggleEventLike(event.id) },
                            onRateClick = { rating -> viewModel.rateEvent(event.id, rating) },
                            onAttendeesClick = { /* Show attendees */ },
                            onShareClick = { /* Share event */ }
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun TikTokStyleEventCard(
    event: Event,
    onEventClick: () -> Unit,
    onLikeClick: () -> Unit,
    onRateClick: (Float) -> Unit,
    onAttendeesClick: () -> Unit,
    onShareClick: () -> Unit
) {
    var isLiked by remember { mutableStateOf(false) }
    var currentRating by remember { mutableStateOf(0f) }
    
    // Full screen event card
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(600.dp)
            .clickable { onEventClick() }
    ) {
        // Event image background
        AsyncImage(
            model = event.imageUrls.firstOrNull() ?: "https://via.placeholder.com/400x600",
            contentDescription = event.title,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )
        
        // Gradient overlay for text readability
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(
                        colors = listOf(
                            Color.Transparent,
                            Color.Black.copy(alpha = 0.7f)
                        )
                    )
                )
        )
        
        // Event content
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(20.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            // Top section - Event info
            Column {
                Text(
                    text = event.title,
                    style = MaterialTheme.typography.headlineMedium.copy(
                        fontWeight = FontWeight.Bold
                    ),
                    color = Color.White
                )
                
                Spacer(modifier = Modifier.height(8.dp))
                
                Text(
                    text = event.description,
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color.White.copy(alpha = 0.9f),
                    maxLines = 3
                )
            }
            
            // Bottom section - Action buttons
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Bottom
            ) {
                // Left side - Event actions
                Column(
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    // Like button
                    IconButton(
                        onClick = {
                            isLiked = !isLiked
                            onLikeClick()
                        }
                    ) {
                        Icon(
                            imageVector = if (isLiked) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                            contentDescription = "Like",
                            tint = if (isLiked) Color.Red else Color.White,
                            modifier = Modifier.size(32.dp)
                        )
                    }
                    
                    // Rating stars
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        repeat(5) { index ->
                            IconButton(
                                onClick = {
                                    currentRating = (index + 1).toFloat()
                                    onRateClick(currentRating)
                                }
                            ) {
                                Icon(
                                    imageVector = if (index < currentRating) Icons.Default.Star else Icons.Default.StarBorder,
                                    contentDescription = "Rate ${index + 1}",
                                    tint = if (index < currentRating) Color.Yellow else Color.White,
                                    modifier = Modifier.size(24.dp)
                                )
                            }
                        }
                    }
                    
                    // Attendees button
                    IconButton(
                        onClick = onAttendeesClick
                    ) {
                        Icon(
                            imageVector = Icons.Default.People,
                            contentDescription = "Attendees",
                            tint = Color.White,
                            modifier = Modifier.size(32.dp)
                        )
                    }
                    
                    // Share button
                    IconButton(
                        onClick = onShareClick
                    ) {
                        Icon(
                            imageVector = Icons.Default.Share,
                            contentDescription = "Share",
                            tint = Color.White,
                            modifier = Modifier.size(32.dp)
                        )
                    }
                }
                
                // Right side - Event stats
                Column(
                    horizontalAlignment = Alignment.End
                ) {
                    Text(
                        text = "$${event.price}",
                        style = MaterialTheme.typography.headlineSmall.copy(
                            fontWeight = FontWeight.Bold
                        ),
                        color = Color.White
                    )
                    
                    Spacer(modifier = Modifier.height(8.dp))
                    
                    Text(
                        text = "${event.capacity} spots",
                        style = MaterialTheme.typography.bodySmall,
                        color = Color.White.copy(alpha = 0.8f)
                    )
                }
            }
        }
    }
}