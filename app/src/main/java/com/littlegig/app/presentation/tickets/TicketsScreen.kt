package com.littlegig.app.presentation.tickets

import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
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
import androidx.compose.ui.draw.drawBehind
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
import com.littlegig.app.data.model.Ticket
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun TicketsScreen(
    navController: NavController,
    viewModel: TicketsViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState(initial = TicketsUiState())
    val isDark = isSystemInDarkTheme()
    
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
                            text = "My Tickets",
                            style = MaterialTheme.typography.headlineMedium.copy(
                                fontWeight = FontWeight.Bold
                            ),
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        
                        IconButton(
                            onClick = { /* TODO: Scan QR code */ }
                        ) {
                            Icon(
                                imageVector = Icons.Default.QrCodeScanner,
                                contentDescription = "Scan QR Code",
                                tint = LittleGigPrimary
                            )
                        }
                    }
                }
            }
            
            Spacer(modifier = Modifier.height(20.dp))
            
            when {
                uiState.isLoading -> {
                    // Loading state
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
                                    text = "Loading tickets...",
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = MaterialTheme.colorScheme.onSurface
                                )
                            }
                        }
                    }
                }
                
                uiState.tickets.isEmpty() -> {
                    // Beautiful empty state
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
                                    modifier = Modifier
                                        .size(80.dp)
                                        .drawBehind {
                                            drawCircle(
                                                color = LittleGigPrimary.copy(alpha = 0.3f),
                                                center = androidx.compose.ui.geometry.Offset(size.width / 2, size.height / 2),
                                                radius = 40.dp.toPx()
                                            )
                                            drawCircle(
                                                color = LittleGigPrimary.copy(alpha = 0.6f),
                                                center = androidx.compose.ui.geometry.Offset(size.width / 2, size.height / 2),
                                                radius = 30.dp.toPx()
                                            )
                                            drawCircle(
                                                color = LittleGigPrimary,
                                                center = androidx.compose.ui.geometry.Offset(size.width / 2, size.height / 2),
                                                radius = 20.dp.toPx()
                                            )
                                        }
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.QrCode,
                                        contentDescription = null,
                                        modifier = Modifier
                                            .fillMaxSize()
                                            .padding(16.dp),
                                        tint = MaterialTheme.colorScheme.onSurface
                                    )
                                }
                                
                                Spacer(modifier = Modifier.height(24.dp))
                                
                                Text(
                                    text = "No tickets yet",
                                    style = MaterialTheme.typography.headlineSmall.copy(
                                        fontWeight = FontWeight.Bold
                                    ),
                                    color = MaterialTheme.colorScheme.onSurface
                                )
                                
                                Spacer(modifier = Modifier.height(8.dp))
                                
                                Text(
                                    text = "Browse events and get your first ticket!",
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                                    textAlign = androidx.compose.ui.text.style.TextAlign.Center
                                )
                                
                                Spacer(modifier = Modifier.height(20.dp))
                                
                                Button(
                                    onClick = { /* TODO: Navigate to events */ },
                                    colors = ButtonDefaults.buttonColors(
                                        containerColor = LittleGigPrimary
                                    )
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.QrCode,
                                        contentDescription = null,
                                        modifier = Modifier.size(16.dp)
                                    )
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text("Browse Events")
                                }
                            }
                        }
                    }
                }
                
                else -> {
                    // Beautiful ticket carousel
                    LazyRow(
                        horizontalArrangement = Arrangement.spacedBy(16.dp),
                        contentPadding = PaddingValues(horizontal = 8.dp)
                    ) {
                        items(uiState.tickets) { ticket ->
                            BeautifulTicketCard(
                                ticket = ticket,
                                onTicketClick = { /* Show ticket details */ },
                                onQrCodeClick = { /* Show QR code */ },
                                onShareClick = { /* Share ticket */ }
                            )
                        }
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BeautifulTicketCard(
    ticket: Ticket,
    onTicketClick: () -> Unit,
    onQrCodeClick: () -> Unit,
    onShareClick: () -> Unit
) {
    val isDark = isSystemInDarkTheme()
    
    Card(
        onClick = onTicketClick,
        modifier = Modifier
            .width(300.dp)
            .height(200.dp),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (isDark) DarkSurface else LightSurface
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
    ) {
        Box(
            modifier = Modifier.fillMaxSize()
        ) {
            // Background image
            AsyncImage(
                model = ticket.eventImageUrl.ifEmpty { "https://via.placeholder.com/300x200" },
                contentDescription = ticket.eventTitle,
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop
            )
            
            // Gradient overlay
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
            
            // Ticket content
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                // Top section - Event info
                Column {
                    Text(
                        text = ticket.eventTitle,
                        style = MaterialTheme.typography.titleLarge.copy(
                            fontWeight = FontWeight.Bold
                        ),
                        color = Color.White
                    )
                    
                    Spacer(modifier = Modifier.height(4.dp))
                    
                    Text(
                        text = java.text.SimpleDateFormat("MMM dd, yyyy", java.util.Locale.getDefault()).format(java.util.Date(ticket.eventDateTime)),
                        style = MaterialTheme.typography.bodyMedium,
                        color = Color.White.copy(alpha = 0.9f)
                    )
                    
                    Spacer(modifier = Modifier.height(4.dp))
                    
                    Text(
                        text = ticket.eventLocation,
                        style = MaterialTheme.typography.bodySmall,
                        color = Color.White.copy(alpha = 0.8f)
                    )
                }
                
                // Bottom section - Ticket actions
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.Bottom
                ) {
                    // Ticket details
                    Column {
                        Text(
                            text = "Ticket #${ticket.id}",
                            style = MaterialTheme.typography.bodySmall,
                            color = Color.White.copy(alpha = 0.7f)
                        )
                        
                        Spacer(modifier = Modifier.height(4.dp))
                        
                        Text(
                            text = "$${ticket.totalAmount}",
                            style = MaterialTheme.typography.titleMedium.copy(
                                fontWeight = FontWeight.Bold
                            ),
                            color = Color.White
                        )
                    }
                    
                    // Action buttons
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        // QR Code button
                        IconButton(
                            onClick = onQrCodeClick
                        ) {
                            Icon(
                                imageVector = Icons.Default.QrCode,
                                contentDescription = "Show QR Code",
                                tint = Color.White,
                                modifier = Modifier.size(24.dp)
                            )
                        }
                        
                        // Share button
                        IconButton(
                            onClick = onShareClick
                        ) {
                            Icon(
                                imageVector = Icons.Default.Share,
                                contentDescription = "Share Ticket",
                                tint = Color.White,
                                modifier = Modifier.size(24.dp)
                            )
                        }
                    }
                }
            }
        }
    }
}



