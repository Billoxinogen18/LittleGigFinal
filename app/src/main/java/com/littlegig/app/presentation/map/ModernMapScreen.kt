package com.littlegig.app.presentation.map

import androidx.compose.animation.core.*
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.littlegig.app.presentation.components.*
import com.littlegig.app.data.model.Event

@Composable
fun ModernMapScreen(
    navController: NavController,
    viewModel: MapViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val selectedCategory by viewModel.selectedCategory.collectAsState()
    val nearbyEvents by viewModel.nearbyEvents.collectAsState()
    
    LaunchedEffect(Unit) {
        viewModel.loadNearbyEvents()
    }
    
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
        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            // Modern Header
            ModernMapHeader(
                onBackClick = { navController.popBackStack() },
                onLocationClick = { viewModel.getCurrentLocation() },
                onFilterClick = { /* Show filter modal */ }
            )
            
            // Category filters
            ModernCategoryFilters(
                selectedCategory = selectedCategory,
                onCategorySelect = { viewModel.selectCategory(it) }
            )
            
            // Map Section (placeholder for now)
            ModernMapView(
                modifier = Modifier.weight(1f),
                events = nearbyEvents,
                onEventMarkerClick = { event ->
                    navController.navigate("event_details/${event.id}")
                }
            )
            
            // Nearby Events Bottom Sheet
            ModernNearbyEventsSheet(
                events = nearbyEvents,
                onEventClick = { event ->
                    navController.navigate("event_details/${event.id}")
                },
                onViewAllClick = { navController.navigate("events") }
            )
        }
        
        // Floating Action Buttons
        Column(
            modifier = Modifier
                .align(Alignment.CenterEnd)
                .padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            ModernMapFAB(
                icon = Icons.Default.MyLocation,
                onClick = { viewModel.centerOnUserLocation() }
            )
            
            ModernMapFAB(
                icon = Icons.Default.Layers,
                onClick = { /* Toggle map layers */ }
            )
        }
    }
}

@Composable
private fun ModernMapHeader(
    onBackClick: () -> Unit,
    onLocationClick: () -> Unit,
    onFilterClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(20.dp),
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
        
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "LittleMap",
                style = MaterialTheme.typography.headlineMedium.copy(
                    fontWeight = FontWeight.ExtraBold
                ),
                color = GlassOnSurface()
            )
            
            GlassmorphicCard(
                onClick = onLocationClick,
                shape = RoundedCornerShape(12.dp)
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.LocationOn,
                        contentDescription = null,
                        tint = GlassPrimary,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "Current Location",
                        style = MaterialTheme.typography.bodySmall,
                        color = GlassOnSurfaceVariant()
                    )
                }
            }
        }
        
        GlassmorphicCard(
            onClick = onFilterClick,
            shape = RoundedCornerShape(16.dp)
        ) {
            Icon(
                imageVector = Icons.Default.FilterList,
                contentDescription = "Filter",
                tint = GlassOnSurface(),
                modifier = Modifier
                    .padding(12.dp)
                    .size(20.dp)
            )
        }
    }
}

@Composable
private fun ModernCategoryFilters(
    selectedCategory: String?,
    onCategorySelect: (String?) -> Unit
) {
    val categories = listOf(
        "All" to Icons.Default.Category,
        "Music" to Icons.Default.MusicNote,
        "Conference" to Icons.Default.Business,
        "Sports" to Icons.Default.SportsBaseball,
        "Food" to Icons.Default.Restaurant,
        "Art" to Icons.Default.Palette
    )
    
    LazyRow(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        contentPadding = PaddingValues(horizontal = 20.dp)
    ) {
        items(categories) { (category, icon) ->
            val isSelected = selectedCategory == category || (selectedCategory == null && category == "All")
            
            GlassmorphicCard(
                onClick = { 
                    onCategorySelect(if (category == "All") null else category)
                },
                shape = RoundedCornerShape(20.dp)
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = icon,
                        contentDescription = null,
                        tint = if (isSelected) GlassPrimary else GlassOnSurfaceVariant(),
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = category,
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

@Composable
private fun ModernMapView(
    modifier: Modifier = Modifier,
    events: List<Event>,
    onEventMarkerClick: (Event) -> Unit
) {
    // Placeholder for actual map implementation
    GlassmorphicCard(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp, vertical = 16.dp),
        shape = RoundedCornerShape(24.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    brush = Brush.radialGradient(
                        colors = listOf(
                            GlassPrimary.copy(alpha = 0.1f),
                            GlassSecondary.copy(alpha = 0.05f)
                        )
                    )
                ),
            contentAlignment = Alignment.Center
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Icon(
                    imageVector = Icons.Default.Map,
                    contentDescription = null,
                    tint = GlassPrimary.copy(alpha = 0.6f),
                    modifier = Modifier.size(64.dp)
                )
                
                Spacer(modifier = Modifier.height(16.dp))
                
                Text(
                    text = "Interactive Map",
                    style = MaterialTheme.typography.titleLarge.copy(
                        fontWeight = FontWeight.Bold
                    ),
                    color = GlassOnSurface()
                )
                
                Text(
                    text = "Explore ${events.size} nearby events",
                    style = MaterialTheme.typography.bodyMedium,
                    color = GlassOnSurfaceVariant()
                )
                
                Spacer(modifier = Modifier.height(16.dp))
                
                // Simulated event markers
                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    repeat(minOf(events.size, 5)) { index ->
                        GlassmorphicCard(
                            onClick = { 
                                if (index < events.size) {
                                    onEventMarkerClick(events[index])
                                }
                            },
                            shape = CircleShape
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(40.dp)
                                    .background(GlassPrimary, CircleShape),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = "${index + 1}",
                                    style = MaterialTheme.typography.labelMedium.copy(
                                        fontWeight = FontWeight.Bold
                                    ),
                                    color = Color.White
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun ModernNearbyEventsSheet(
    events: List<Event>,
    onEventClick: (Event) -> Unit,
    onViewAllClick: () -> Unit
) {
    GlassmorphicCard(
        shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp)
        ) {
            // Handle bar
            Box(
                modifier = Modifier
                    .size(width = 40.dp, height = 4.dp)
                    .background(
                        GlassOnSurfaceVariant().copy(alpha = 0.3f),
                        RoundedCornerShape(2.dp)
                    )
                    .align(Alignment.CenterHorizontally)
            )
            
            Spacer(modifier = Modifier.height(16.dp))
            
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = "Nearby Events",
                        style = MaterialTheme.typography.titleLarge.copy(
                            fontWeight = FontWeight.Bold
                        ),
                        color = GlassOnSurface()
                    )
                    
                    Text(
                        text = "${events.size} events found",
                        style = MaterialTheme.typography.bodyMedium,
                        color = GlassOnSurfaceVariant()
                    )
                }
                
                TextButton(
                    onClick = onViewAllClick
                ) {
                    Text(
                        text = "View All",
                        color = GlassPrimary,
                        style = MaterialTheme.typography.labelLarge.copy(
                            fontWeight = FontWeight.SemiBold
                        )
                    )
                }
            }
            
            Spacer(modifier = Modifier.height(16.dp))
            
            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                items(events.take(5)) { event ->
                    ModernEventMapCard(
                        event = event,
                        onClick = { onEventClick(event) }
                    )
                }
            }
        }
    }
}

@Composable
private fun ModernEventMapCard(
    event: Event,
    onClick: () -> Unit
) {
    GlassmorphicCard(
        onClick = onClick,
        shape = RoundedCornerShape(16.dp),
        modifier = Modifier.width(200.dp)
    ) {
        Column {
            // Event image
            AsyncImage(
                model = event.imageUrls.firstOrNull(),
                contentDescription = null,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(100.dp),
                contentScale = ContentScale.Crop
            )
            
            Column(
                modifier = Modifier.padding(12.dp)
            ) {
                Text(
                    text = event.title,
                    style = MaterialTheme.typography.titleSmall.copy(
                        fontWeight = FontWeight.SemiBold
                    ),
                    color = GlassOnSurface(),
                    maxLines = 2
                )
                
                Spacer(modifier = Modifier.height(4.dp))
                
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.LocationOn,
                        contentDescription = null,
                        tint = GlassOnSurfaceVariant(),
                        modifier = Modifier.size(14.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = event.location.name,
                        style = MaterialTheme.typography.bodySmall,
                        color = GlassOnSurfaceVariant(),
                        maxLines = 1
                    )
                }
                
                Spacer(modifier = Modifier.height(8.dp))
                
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = if (event.price > 0) "${event.currency} ${event.price}" else "Free",
                        style = MaterialTheme.typography.labelMedium.copy(
                            fontWeight = FontWeight.Bold
                        ),
                        color = GlassPrimary
                    )
                    
                    Text(
                        text = "${event.ticketsSold}/${event.capacity}",
                        style = MaterialTheme.typography.bodySmall,
                        color = GlassOnSurfaceVariant()
                    )
                }
            }
        }
    }
}

@Composable
private fun ModernMapFAB(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    onClick: () -> Unit
) {
    GlassmorphicCard(
        onClick = onClick,
        shape = CircleShape
    ) {
        Box(
            modifier = Modifier
                .size(56.dp)
                .background(
                    brush = Brush.radialGradient(
                        colors = listOf(GlassPrimary, GlassSecondary)
                    ),
                    shape = CircleShape
                ),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = Color.White,
                modifier = Modifier.size(24.dp)
            )
        }
    }
}
