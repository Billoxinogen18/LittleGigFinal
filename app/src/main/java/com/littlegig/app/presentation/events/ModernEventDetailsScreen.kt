package com.littlegig.app.presentation.events

import androidx.compose.animation.core.*
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.littlegig.app.presentation.components.*
import androidx.browser.customtabs.CustomTabsIntent
import android.net.Uri
import kotlinx.coroutines.launch

@Composable
fun ModernEventDetailsScreen(
    eventId: String,
    navController: NavController,
    viewModel: EventDetailsViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    
    LaunchedEffect(eventId) {
        viewModel.loadEventDetails(eventId)
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
        if (uiState.isLoading) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(color = GlassPrimary)
            }
        } else {
            uiState.event?.let { event ->
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .verticalScroll(rememberScrollState())
                ) {
                    // Hero Section
                    ModernEventHero(
                        event = event,
                        onBackClick = { navController.popBackStack() },
                        onLikeClick = { viewModel.toggleEventLike() },
                        onShareClick = { navController.navigate("chat") }
                    )
                    
                    // Content Section
                    Column(
                        modifier = Modifier.padding(20.dp),
                        verticalArrangement = Arrangement.spacedBy(20.dp)
                    ) {
                        // Event Info Card
                        ModernEventInfoCard(event = event)
                        
                        // Organizer Card
                        ModernOrganizerCard(event = event)
                        
                        // Action Buttons
                        ModernActionButtons(
                            onBuyTicketClick = {
                                scope.launch {
                                    val paymentUrl = viewModel.getPaymentUrl(eventId, event.price, event.title)
                                    paymentUrl?.let { url ->
                                        val intent = CustomTabsIntent.Builder()
                                            .setShowTitle(true)
                                            .build()
                                        intent.launchUrl(context, Uri.parse(url))
                                    }
                                }
                            },
                            onAddRecapClick = { navController.navigate("recaps_upload/$eventId") },
                            onViewRecapsClick = { navController.navigate("recaps_viewer/$eventId") }
                        )
                        
                        // Recent Recaps Preview
                        ModernRecapsPreview(
                            onViewAllClick = { navController.navigate("recaps_viewer/$eventId") }
                        )
                        
                        Spacer(modifier = Modifier.height(100.dp)) // Bottom padding
                    }
                }
            }
        }
    }
}

@Composable
private fun ModernEventHero(
    event: com.littlegig.app.data.model.Event,
    onBackClick: () -> Unit,
    onLikeClick: () -> Unit,
    onShareClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(400.dp)
    ) {
        // Background Image
        AsyncImage(
            model = event.imageUrls.firstOrNull() ?: "",
            contentDescription = null,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )
        
        // Gradient Overlay
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    brush = Brush.verticalGradient(
                        colors = listOf(
                            Color.Black.copy(alpha = 0.3f),
                            Color.Transparent,
                            Color.Black.copy(alpha = 0.8f)
                        )
                    )
                )
        )
        
        // Top Controls
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
                    ModernGlassmorphicCard(
            onClick = onBackClick
        ) {
                Icon(
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription = "Back",
                    tint = Color.White,
                    modifier = Modifier
                        .padding(12.dp)
                        .size(20.dp)
                )
            }
            
            Row(
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                            ModernGlassmorphicCard(
                onClick = onLikeClick
            ) {
                    Icon(
                        imageVector = Icons.Default.FavoriteBorder,
                        contentDescription = "Like",
                        tint = Color.White,
                        modifier = Modifier
                            .padding(12.dp)
                            .size(20.dp)
                    )
                }
                
                ModernGlassmorphicCard(
                    onClick = onShareClick
                ) {
                    Icon(
                        imageVector = Icons.Default.Share,
                        contentDescription = "Share",
                        tint = Color.White,
                        modifier = Modifier
                            .padding(12.dp)
                            .size(20.dp)
                    )
                }
            }
        }
        
        // Event Title and Info
        Column(
            modifier = Modifier
                .align(Alignment.BottomStart)
                .padding(20.dp)
        ) {
            // Category Badge
            ModernGlassmorphicCard() {
                Text(
                    text = event.category.name,
                    style = MaterialTheme.typography.labelMedium.copy(
                        fontWeight = FontWeight.SemiBold
                    ),
                    color = Color.White,
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
                )
            }
            
            Spacer(modifier = Modifier.height(12.dp))
            
            Text(
                text = event.title,
                style = MaterialTheme.typography.headlineLarge.copy(
                    fontWeight = FontWeight.Bold,
                    fontSize = 32.sp
                ),
                color = Color.White
            )
        }
    }
}

@Composable
private fun ModernEventInfoCard(
    event: com.littlegig.app.data.model.Event
) {
    ModernGlassmorphicCard() {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(24.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            Text(
                text = "Event Details",
                style = MaterialTheme.typography.titleLarge.copy(
                    fontWeight = FontWeight.Bold
                ),
                color = GlassOnSurface()
            )
            
            // Date & Time
            ModernInfoRow(
                icon = Icons.Default.Schedule,
                title = "Date & Time",
                subtitle = formatEventDateTime(event.dateTime)
            )
            
            // Location
            ModernInfoRow(
                icon = Icons.Default.LocationOn,
                title = "Location",
                subtitle = "${event.location.name}\n${event.location.address}"
            )
            
            // Price
            ModernInfoRow(
                icon = Icons.Default.AttachMoney,
                title = "Price",
                subtitle = if (event.price > 0) "${event.currency} ${event.price}" else "Free"
            )
            
            // Capacity
            ModernInfoRow(
                icon = Icons.Default.People,
                title = "Capacity",
                subtitle = "${event.ticketsSold}/${event.capacity} attending"
            )
            
            // Description
            if (event.description.isNotEmpty()) {
                Column {
                    Text(
                        text = "About",
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.SemiBold
                        ),
                        color = GlassOnSurface()
                    )
                    
                    Spacer(modifier = Modifier.height(8.dp))
                    
                    Text(
                        text = event.description,
                        style = MaterialTheme.typography.bodyMedium,
                        color = GlassOnSurfaceVariant()
                    )
                }
            }
        }
    }
}

@Composable
private fun ModernInfoRow(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    title: String,
    subtitle: String
) {
    Row(
        verticalAlignment = Alignment.Top
    ) {
        ModernGlassmorphicCard() {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = GlassPrimary,
                modifier = Modifier
                    .padding(8.dp)
                    .size(20.dp)
            )
        }
        
        Spacer(modifier = Modifier.width(16.dp))
        
        Column {
            Text(
                text = title,
                style = MaterialTheme.typography.titleSmall.copy(
                    fontWeight = FontWeight.SemiBold
                ),
                color = GlassOnSurface()
            )
            
            Text(
                text = subtitle,
                style = MaterialTheme.typography.bodyMedium,
                color = GlassOnSurfaceVariant()
            )
        }
    }
}

@Composable
private fun ModernOrganizerCard(
    event: com.littlegig.app.data.model.Event
) {
    ModernGlassmorphicCard(
        shape = RoundedCornerShape(20.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Organizer Avatar
            if (event.organizerImageUrl.isNotEmpty()) {
                AsyncImage(
                    model = event.organizerImageUrl,
                    contentDescription = "Organizer",
                    modifier = Modifier
                        .size(50.dp)
                        .clip(CircleShape),
                    contentScale = ContentScale.Crop
                )
            } else {
                Box(
                    modifier = Modifier
                        .size(50.dp)
                        .background(GlassPrimary.copy(alpha = 0.2f), CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = event.organizerName.firstOrNull()?.uppercase() ?: "O",
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.Bold
                        ),
                        color = GlassPrimary
                    )
                }
            }
            
            Spacer(modifier = Modifier.width(16.dp))
            
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = "Organized by",
                    style = MaterialTheme.typography.bodySmall,
                    color = GlassOnSurfaceVariant()
                )
                
                Text(
                    text = event.organizerName,
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.SemiBold
                    ),
                    color = GlassOnSurface()
                )
            }
            
            Button(
                onClick = { /* Follow organizer */ },
                colors = ButtonDefaults.buttonColors(
                    containerColor = GlassPrimary,
                    contentColor = Color.White
                ),
                contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp)
            ) {
                Text(
                    text = "Follow",
                    style = MaterialTheme.typography.labelMedium.copy(
                        fontWeight = FontWeight.SemiBold
                    )
                )
            }
        }
    }
}

@Composable
private fun ModernActionButtons(
    onBuyTicketClick: () -> Unit,
    onAddRecapClick: () -> Unit,
    onViewRecapsClick: () -> Unit
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        // Buy Ticket Button
        Button(
            onClick = onBuyTicketClick,
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(
                containerColor = GlassPrimary,
                contentColor = Color.White
            ),
            contentPadding = PaddingValues(vertical = 16.dp)
        ) {
            Icon(
                imageVector = Icons.Default.ShoppingCart,
                contentDescription = null,
                modifier = Modifier.size(20.dp)
            )
            Spacer(modifier = Modifier.width(12.dp))
            Text(
                text = "Buy Ticket",
                style = MaterialTheme.typography.titleMedium.copy(
                    fontWeight = FontWeight.Bold
                )
            )
        }
        
        // Secondary Actions
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Button(
                onClick = onAddRecapClick,
                modifier = Modifier.weight(1f),
                colors = ButtonDefaults.buttonColors(
                    containerColor = GlassSecondary,
                    contentColor = Color.White
                ),
                shape = RoundedCornerShape(16.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = null,
                    modifier = Modifier.size(18.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "Add Recap",
                    style = MaterialTheme.typography.labelLarge.copy(
                        fontWeight = FontWeight.SemiBold
                    )
                )
            }
            
            Button(
                onClick = onViewRecapsClick,
                modifier = Modifier.weight(1f),
                colors = ButtonDefaults.buttonColors(
                    containerColor = GlassAccent,
                    contentColor = Color.White
                ),
                shape = RoundedCornerShape(16.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.PlayArrow,
                    contentDescription = null,
                    modifier = Modifier.size(18.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "Recaps",
                    style = MaterialTheme.typography.labelLarge.copy(
                        fontWeight = FontWeight.SemiBold
                    )
                )
            }
        }
    }
}

@Composable
private fun ModernRecapsPreview(
    onViewAllClick: () -> Unit
) {
    ModernGlassmorphicCard(
        shape = RoundedCornerShape(20.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Recent Recaps",
                    style = MaterialTheme.typography.titleLarge.copy(
                        fontWeight = FontWeight.Bold
                    ),
                    color = GlassOnSurface()
                )
                
                TextButton(onClick = onViewAllClick) {
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
            
            // Placeholder for recap previews
            Text(
                text = "No recaps available yet. Be the first to share!",
                style = MaterialTheme.typography.bodyMedium,
                color = GlassOnSurfaceVariant()
            )
        }
    }
}

@Composable
private fun formatEventDateTime(timestamp: Long): String {
    val sdf = java.text.SimpleDateFormat("MMM d, yyyy • h:mm a", java.util.Locale.getDefault())
    return sdf.format(java.util.Date(timestamp))
}
