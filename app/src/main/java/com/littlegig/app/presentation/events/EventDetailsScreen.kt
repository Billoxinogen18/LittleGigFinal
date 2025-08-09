package com.littlegig.app.presentation.events

import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
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
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.littlegig.app.data.model.Event
import com.littlegig.app.data.model.User
import com.littlegig.app.presentation.components.*
import com.littlegig.app.presentation.theme.*
import androidx.hilt.navigation.compose.hiltViewModel
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun EventDetailsScreen(
    eventId: String,
    navController: NavController,
    viewModel: EventDetailsViewModel = hiltViewModel()
) {
    val context = LocalContext.current
    val isDark = isSystemInDarkTheme()
    
    LaunchedEffect(eventId) {
        viewModel.loadEventDetails(eventId)
    }
    
    val uiState by viewModel.uiState.collectAsState()
    
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                if (isDark) DarkBackground else LightBackground
            )
    ) {
        when {
            uiState.isLoading -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    ShimmerLoadingCard()
                }
            }
            
            uiState.event == null -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    AdvancedNeumorphicCard(
                        modifier = Modifier.padding(16.dp)
                    ) {
                        Column(
                            modifier = Modifier.padding(24.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Icon(
                                imageVector = Icons.Default.Error,
                                contentDescription = null,
                                tint = LittleGigPrimary,
                                modifier = Modifier.size(64.dp)
                            )
                            Spacer(modifier = Modifier.height(16.dp))
                            Text(
                                text = "Event not found",
                                style = MaterialTheme.typography.titleLarge,
                                color = if (isDark) Color.White else Color.Black
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = "This event may have been removed or is no longer available",
                                style = MaterialTheme.typography.bodyMedium,
                                color = if (isDark) Color.White.copy(alpha = 0.7f) else Color.Black.copy(alpha = 0.7f)
                            )
                            Spacer(modifier = Modifier.height(16.dp))
                            HapticButton(
                                onClick = { navController.popBackStack() }
                            ) {
                                AdvancedNeumorphicCard {
                                    Text(
                                        text = "Go Back",
                                        modifier = Modifier.padding(16.dp),
                                        color = LittleGigPrimary
                                    )
                                }
                            }
                        }
                    }
                }
            }
            
            else -> {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(bottom = 100.dp)
                ) {
                    item {
                        // Event Header with Image
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(300.dp)
                        ) {
                            AsyncImage(
                                model = uiState.event!!.imageUrls.firstOrNull(),
                                contentDescription = null,
                                modifier = Modifier.fillMaxSize(),
                                contentScale = ContentScale.Crop
                            )
                            
                            // Back button overlay
                            HapticButton(
                                onClick = { navController.popBackStack() },
                                modifier = Modifier
                                    .padding(16.dp)
                                    .size(48.dp)
                                    .clip(RoundedCornerShape(24.dp))
                                    .background(Color.Black.copy(alpha = 0.5f))
                            ) {
                                Icon(
                                    imageVector = Icons.Default.ArrowBack,
                                    contentDescription = "Back",
                                    tint = Color.White,
                                    modifier = Modifier.padding(12.dp)
                                )
                            }
                        }
                    }
                    
                    item {
                        // Event Details Card
                        AdvancedGlassmorphicCard(
                            modifier = Modifier.padding(16.dp)
                        ) {
                            Column(
                                modifier = Modifier.padding(20.dp)
                            ) {
                                Text(
                                    text = uiState.event!!.title,
                                    style = MaterialTheme.typography.headlineMedium.copy(
                                        fontWeight = FontWeight.Bold
                                    ),
                                    color = if (isDark) Color.White else Color.Black
                                )
                                
                                Spacer(modifier = Modifier.height(8.dp))
                                
                                Text(
                                    text = uiState.event!!.description,
                                    style = MaterialTheme.typography.bodyLarge,
                                    color = if (isDark) Color.White.copy(alpha = 0.8f) else Color.Black.copy(alpha = 0.8f)
                                )
                                
                                Spacer(modifier = Modifier.height(16.dp))
                                
                                // Event Info Row
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Column {
                                        Text(
                                            text = "Price",
                                            style = MaterialTheme.typography.bodySmall,
                                            color = if (isDark) Color.White.copy(alpha = 0.6f) else Color.Black.copy(alpha = 0.6f)
                                        )
                                        Text(
                                            text = "KSH ${uiState.event!!.price}",
                                            style = MaterialTheme.typography.titleMedium.copy(
                                                fontWeight = FontWeight.SemiBold
                                            ),
                                            color = LittleGigPrimary
                                        )
                                    }
                                    
                                    Column {
                                        Text(
                                            text = "Capacity",
                                            style = MaterialTheme.typography.bodySmall,
                                            color = if (isDark) Color.White.copy(alpha = 0.6f) else Color.Black.copy(alpha = 0.6f)
                                        )
                                        Text(
                                            text = "${uiState.event!!.capacity} people",
                                            style = MaterialTheme.typography.titleMedium.copy(
                                                fontWeight = FontWeight.SemiBold
                                            ),
                                            color = if (isDark) Color.White else Color.Black
                                        )
                                    }
                                    
                                    Column {
                                        Text(
                                            text = "Category",
                                            style = MaterialTheme.typography.bodySmall,
                                            color = if (isDark) Color.White.copy(alpha = 0.6f) else Color.Black.copy(alpha = 0.6f)
                                        )
                                        Text(
                                            text = uiState.event!!.category.name.lowercase().replaceFirstChar { it.uppercase() },
                                            style = MaterialTheme.typography.titleMedium.copy(
                                                fontWeight = FontWeight.SemiBold
                                            ),
                                            color = if (isDark) Color.White else Color.Black
                                        )
                                    }
                                }
                                
                                Spacer(modifier = Modifier.height(16.dp))
                                
                                // Date and Time
                                Row(
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Schedule,
                                        contentDescription = null,
                                        tint = LittleGigPrimary,
                                        modifier = Modifier.size(20.dp)
                                    )
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text(
                                        text = SimpleDateFormat("MMM dd, yyyy 'at' HH:mm", Locale.getDefault())
                                            .format(Date(uiState.event!!.dateTime)),
                                        style = MaterialTheme.typography.bodyLarge,
                                        color = if (isDark) Color.White else Color.Black
                                    )
                                }
                                
                                Spacer(modifier = Modifier.height(8.dp))
                                
                                // Location
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
                                        text = uiState.event!!.location.name,
                                        style = MaterialTheme.typography.bodyLarge,
                                        color = if (isDark) Color.White else Color.Black
                                    )
                                }
                            }
                        }
                    }
                    
                    item {
                        // Organizer Info
                        AdvancedGlassmorphicCard(
                            modifier = Modifier.padding(horizontal = 16.dp)
                        ) {
                            Row(
                                modifier = Modifier.padding(20.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                AsyncImage(
                                    model = uiState.organizer?.profilePictureUrl ?: "",
                                    contentDescription = null,
                                    modifier = Modifier
                                        .size(48.dp)
                                        .clip(RoundedCornerShape(24.dp)),
                                    contentScale = ContentScale.Crop
                                )
                                
                                Spacer(modifier = Modifier.width(12.dp))
                                
                                Column(modifier = Modifier.weight(1f)) {
                                    Text(
                                        text = uiState.organizer?.name ?: "Unknown Organizer",
                                        style = MaterialTheme.typography.titleMedium.copy(
                                            fontWeight = FontWeight.SemiBold
                                        ),
                                        color = if (isDark) Color.White else Color.Black
                                    )
                                    
                                    if (uiState.organizer != null) {
                                        NeumorphicRankBadge(
                                            rank = uiState.organizer!!.rank,
                                            modifier = Modifier.padding(top = 4.dp)
                                        )
                                    }
                                }
                                
                                HapticButton(
                                    onClick = { viewModel.followOrganizer(uiState.organizer?.id) }
                                ) {
                                    AdvancedNeumorphicCard {
                                        Text(
                                            text = if (uiState.isFollowingOrganizer) "Following" else "Follow",
                                            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
                                            color = if (uiState.isFollowingOrganizer) LittleGigPrimary else Color.Gray
                                        )
                                    }
                                }
                            }
                        }
                    }
                    
                    item {
                        // Action Buttons
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            horizontalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            HapticButton(
                                onClick = { viewModel.toggleEventLike() },
                                modifier = Modifier.weight(1f)
                            ) {
                                AdvancedNeumorphicCard {
                                    Row(
                                        modifier = Modifier.padding(16.dp),
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.Center
                                    ) {
                                        Icon(
                                            imageVector = if (uiState.isLiked) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                                            contentDescription = null,
                                            tint = if (uiState.isLiked) Color.Red else LittleGigPrimary,
                                            modifier = Modifier.size(20.dp)
                                        )
                                        Spacer(modifier = Modifier.width(8.dp))
                                        Text(
                                            text = "Like",
                                            color = if (isDark) Color.White else Color.Black
                                        )
                                    }
                                }
                            }
                            
                            HapticButton(
                                onClick = { viewModel.shareEvent() },
                                modifier = Modifier.weight(1f)
                            ) {
                                AdvancedNeumorphicCard {
                                    Row(
                                        modifier = Modifier.padding(16.dp),
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.Center
                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.Share,
                                            contentDescription = null,
                                            tint = LittleGigPrimary,
                                            modifier = Modifier.size(20.dp)
                                        )
                                        Spacer(modifier = Modifier.width(8.dp))
                                        Text(
                                            text = "Share",
                                            color = if (isDark) Color.White else Color.Black
                                        )
                                    }
                                }
                            }
                        }
                    }
                    
                    item {
                        // Buy Ticket Button
                        HapticButton(
                            onClick = { viewModel.buyTicket() },
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp)
                        ) {
                            AdvancedNeumorphicCard {
                                Row(
                                    modifier = Modifier.padding(20.dp),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.Center
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.ShoppingCart,
                                        contentDescription = null,
                                        tint = Color.White,
                                        modifier = Modifier.size(24.dp)
                                    )
                                    Spacer(modifier = Modifier.width(12.dp))
                                    Text(
                                        text = "Buy Ticket - KSH ${uiState.event!!.price}",
                                        style = MaterialTheme.typography.titleMedium.copy(
                                            fontWeight = FontWeight.SemiBold
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
} 