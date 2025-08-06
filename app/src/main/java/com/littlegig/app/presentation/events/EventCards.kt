package com.littlegig.app.presentation.events

import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
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
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.littlegig.app.data.model.Event
import com.littlegig.app.presentation.components.*
import com.littlegig.app.presentation.theme.*
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun LiquidGlassEventCard(
    event: Event,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val isDark = isSystemInDarkTheme()
    
    LiquidGlassCard(
        modifier = modifier,
        onClick = onClick,
        glowEffect = event.isFeatured
    ) {
        Column {
            // Event Image with overlay
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
            ) {
                if (event.imageUrls.isNotEmpty()) {
                    AsyncImage(
                        model = event.imageUrls.first(),
                        contentDescription = event.title,
                        modifier = Modifier
                            .fillMaxSize()
                            .clip(RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp)),
                        contentScale = ContentScale.Crop
                    )
                } else {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(
                                Brush.linearGradient(
                                    colors = listOf(
                                        LittleGigPrimary.copy(alpha = 0.3f),
                                        LittleGigSecondary.copy(alpha = 0.3f)
                                    )
                                )
                            )
                            .clip(RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Event,
                            contentDescription = null,
                            modifier = Modifier.size(64.dp),
                            tint = Color.White.copy(alpha = 0.8f)
                        )
                    }
                }
                
                // Gradient overlay
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(
                            Brush.verticalGradient(
                                colors = listOf(
                                    Color.Transparent,
                                    Color.Black.copy(alpha = 0.3f)
                                )
                            )
                        )
                )
                
                // Featured badge
                if (event.isFeatured) {
                    Surface(
                        modifier = Modifier
                            .align(Alignment.TopEnd)
                            .padding(12.dp),
                        shape = RoundedCornerShape(12.dp),
                        color = LittleGigAccent
                    ) {
                        Text(
                            text = "✨ Featured",
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                            style = MaterialTheme.typography.labelSmall,
                            color = Color.White,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
            
            // Event Details
            Column(
                modifier = Modifier.padding(20.dp)
            ) {
                // Event Title
                Text(
                    text = event.title,
                    style = MaterialTheme.typography.titleLarge.copy(
                        fontWeight = FontWeight.Bold
                    ),
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                    color = if (isDark) DarkOnSurface else LightOnSurface
                )
                
                Spacer(modifier = Modifier.height(8.dp))
                
                // Event Description
                Text(
                    text = event.description,
                    style = MaterialTheme.typography.bodyMedium,
                    color = if (isDark) DarkOnSurfaceVariant else LightOnSurfaceVariant,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
                
                Spacer(modifier = Modifier.height(16.dp))
                
                // Date and Location Row
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        // Date with icon
                        Row(
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = Icons.Default.CalendarToday,
                                contentDescription = null,
                                modifier = Modifier.size(16.dp),
                                tint = LittleGigPrimary
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = SimpleDateFormat("MMM dd, yyyy", Locale.getDefault())
                                    .format(Date(event.dateTime)),
                                style = MaterialTheme.typography.bodySmall,
                                color = LittleGigPrimary,
                                fontWeight = FontWeight.Medium
                            )
                        }
                        
                        Spacer(modifier = Modifier.height(4.dp))
                        
                        // Location with icon
                        Row(
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = Icons.Default.LocationOn,
                                contentDescription = null,
                                modifier = Modifier.size(16.dp),
                                tint = if (isDark) DarkOnSurfaceVariant else LightOnSurfaceVariant
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = event.location.name,
                                style = MaterialTheme.typography.bodySmall,
                                color = if (isDark) DarkOnSurfaceVariant else LightOnSurfaceVariant,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                        }
                    }
                    
                    // Price Badge
                    NeumorphicPriceButton(
                        price = event.price,
                        currency = event.currency
                    )
                }
                
                Spacer(modifier = Modifier.height(16.dp))
                
                // Tickets availability
                if (event.capacity > 0) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "${event.ticketsSold}/${event.capacity} tickets sold",
                            style = MaterialTheme.typography.bodySmall,
                            color = if (isDark) DarkOnSurfaceVariant else LightOnSurfaceVariant
                        )
                        
                        Spacer(modifier = Modifier.width(12.dp))
                        
                        LinearProgressIndicator(
                            progress = if (event.capacity > 0) event.ticketsSold.toFloat() / event.capacity.toFloat() else 0f,
                            modifier = Modifier
                                .weight(1f)
                                .height(4.dp)
                                .clip(RoundedCornerShape(2.dp)),
                            color = LittleGigSecondary,
                            trackColor = if (isDark) DarkSurfaceVariant else LightSurfaceVariant
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun LiquidGlassFeaturedEventCard(
    event: Event,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val isDark = isSystemInDarkTheme()
    
    LiquidGlassCard(
        modifier = modifier.width(280.dp),
        onClick = onClick,
        glowEffect = true
    ) {
        Column {
            // Event Image
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(160.dp)
            ) {
                if (event.imageUrls.isNotEmpty()) {
                    AsyncImage(
                        model = event.imageUrls.first(),
                        contentDescription = event.title,
                        modifier = Modifier
                            .fillMaxSize()
                            .clip(RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp)),
                        contentScale = ContentScale.Crop
                    )
                } else {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(
                                Brush.linearGradient(
                                    colors = listOf(
                                        LittleGigAccent.copy(alpha = 0.4f),
                                        LittleGigPrimary.copy(alpha = 0.4f)
                                    )
                                )
                            )
                            .clip(RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Star,
                            contentDescription = null,
                            modifier = Modifier.size(48.dp),
                            tint = Color.White.copy(alpha = 0.9f)
                        )
                    }
                }
                
                // Shimmer overlay for featured events
                AnimatedShimmerOverlay()
            }
            
            Column(
                modifier = Modifier.padding(16.dp)
            ) {
                Text(
                    text = event.title,
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.Bold
                    ),
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                    color = if (isDark) DarkOnSurface else LightOnSurface
                )
                
                Spacer(modifier = Modifier.height(8.dp))
                
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = SimpleDateFormat("MMM dd", Locale.getDefault())
                                .format(Date(event.dateTime)),
                            style = MaterialTheme.typography.bodySmall,
                            color = LittleGigPrimary,
                            fontWeight = FontWeight.Medium
                        )
                        
                        Text(
                            text = event.location.name,
                            style = MaterialTheme.typography.bodySmall,
                            color = if (isDark) DarkOnSurfaceVariant else LightOnSurfaceVariant,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                    
                    if (event.price > 0) {
                        Surface(
                            shape = CircleShape,
                            color = LittleGigPrimary.copy(alpha = 0.1f)
                        ) {
                            Text(
                                text = "${event.currency} ${event.price}",
                                style = MaterialTheme.typography.labelMedium.copy(
                                    fontWeight = FontWeight.Bold
                                ),
                                color = LittleGigPrimary,
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun NeumorphicPriceButton(
    price: Double,
    currency: String,
    modifier: Modifier = Modifier
) {
    val isDark = isSystemInDarkTheme()
    
    if (price > 0) {
        NeumorphicButton(
            onClick = { /* Handle ticket purchase */ },
            modifier = modifier,
            glowEffect = true
        ) {
            Icon(
                imageVector = Icons.Default.LocalOffer,
                contentDescription = null,
                modifier = Modifier.size(18.dp),
                tint = LittleGigPrimary
            )
            Spacer(modifier = Modifier.width(6.dp))
            Text(
                text = "$currency $price",
                style = MaterialTheme.typography.titleSmall.copy(
                    fontWeight = FontWeight.Bold
                ),
                color = LittleGigPrimary
            )
        }
    } else {
        Surface(
            modifier = modifier,
            shape = RoundedCornerShape(16.dp),
            color = LittleGigSecondary.copy(alpha = 0.1f)
        ) {
            Row(
                modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Default.Stars,
                    contentDescription = null,
                    modifier = Modifier.size(18.dp),
                    tint = LittleGigSecondary
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = "FREE",
                    style = MaterialTheme.typography.titleSmall.copy(
                        fontWeight = FontWeight.Bold
                    ),
                    color = LittleGigSecondary
                )
            }
        }
    }
}

@Composable
fun NeumorphicCategoryChip(
    text: String,
    selected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val isDark = isSystemInDarkTheme()
    
    val animatedAlpha by animateFloatAsState(
        targetValue = if (selected) 1f else 0.7f,
        animationSpec = tween(300),
        label = "chip_alpha"
    )
    
    val animatedScale by animateFloatAsState(
        targetValue = if (selected) 1.05f else 1f,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessLow
        ),
        label = "chip_scale"
    )
    
    Surface(
        onClick = onClick,
        modifier = modifier
            .graphicsLayer {
                alpha = animatedAlpha
                scaleX = animatedScale
                scaleY = animatedScale
            },
        shape = RoundedCornerShape(20.dp),
        color = if (selected) {
            if (isDark) DarkSurface else LightSurface
        } else {
            if (isDark) DarkSurfaceVariant else LightSurfaceVariant
        },
        border = if (selected) {
            androidx.compose.foundation.BorderStroke(
                1.dp,
                Brush.linearGradient(
                    colors = listOf(
                        LittleGigPrimary.copy(alpha = 0.5f),
                        LittleGigSecondary.copy(alpha = 0.5f)
                    )
                )
            )
        } else null
    ) {
        Box(
            modifier = Modifier
                .background(
                    if (selected) {
                        Brush.linearGradient(
                            colors = listOf(
                                LittleGigPrimary.copy(alpha = 0.1f),
                                LittleGigSecondary.copy(alpha = 0.1f)
                            )
                        )
                    } else Brush.linearGradient(
                        colors = listOf(Color.Transparent, Color.Transparent)
                    )
                )
                .padding(horizontal = 16.dp, vertical = 12.dp)
        ) {
            Text(
                text = text,
                style = MaterialTheme.typography.labelMedium.copy(
                    fontWeight = if (selected) FontWeight.Bold else FontWeight.Medium
                ),
                color = if (selected) LittleGigPrimary else {
                    if (isDark) DarkOnSurfaceVariant else LightOnSurfaceVariant
                }
            )
        }
    }
}

@Composable
private fun AnimatedShimmerOverlay() {
    val infiniteTransition = rememberInfiniteTransition(label = "shimmer")
    
    val shimmerAlpha by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(1500, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "shimmer_alpha"
    )
    
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.linearGradient(
                    colors = listOf(
                        Color.White.copy(alpha = shimmerAlpha * 0.2f),
                        Color.Transparent,
                        Color.White.copy(alpha = shimmerAlpha * 0.2f)
                    ),
                    start = androidx.compose.ui.geometry.Offset(0f, 0f),
                    end = androidx.compose.ui.geometry.Offset(Float.POSITIVE_INFINITY, Float.POSITIVE_INFINITY)
                )
            )
    )
}