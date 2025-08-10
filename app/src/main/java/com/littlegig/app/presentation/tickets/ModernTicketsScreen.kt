package com.littlegig.app.presentation.tickets

import androidx.compose.animation.core.*
import androidx.compose.foundation.*
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
import com.littlegig.app.data.model.Ticket
import com.littlegig.app.data.model.TicketStatus
import androidx.compose.ui.window.WindowManager
import android.view.WindowManager as AndroidWindowManager

@Composable
fun ModernTicketsScreen(
    navController: NavController,
    viewModel: TicketsViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    
    LaunchedEffect(Unit) {
        viewModel.loadTickets()
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
            ModernTicketsHeader(
                onBackClick = { navController.popBackStack() },
                onSearchClick = { /* Search tickets */ }
            )
            
            if (uiState.isLoading) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(color = GlassPrimary)
                }
            } else if (uiState.tickets.isEmpty()) {
                ModernEmptyTicketsState(
                    onBrowseEventsClick = { navController.navigate("events") }
                )
            } else {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 20.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp),
                    contentPadding = PaddingValues(vertical = 20.dp, bottom = 100.dp)
                ) {
                    // Filter chips
                    item {
                        ModernFilterChips(
                            selectedFilter = uiState.selectedFilter,
                            onFilterChange = { viewModel.filterTickets(it) }
                        )
                    }
                    
                    // Active tickets stats
                    if (uiState.tickets.any { it.status == TicketStatus.ACTIVE }) {
                        item {
                            ModernTicketStats(tickets = uiState.tickets)
                        }
                    }
                    
                    // Tickets list
                    items(uiState.tickets) { ticket ->
                        ModernTicketCard(
                            ticket = ticket,
                            onTicketClick = { /* View ticket details */ },
                            onQRCodeClick = { /* Show QR */ },
                            onAddToWalletClick = { viewModel.addToGoogleWallet(ticket.id) },
                            onShareClick = { navController.navigate("chat") }
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun ModernTicketsHeader(
    onBackClick: () -> Unit,
    onSearchClick: () -> Unit
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
        
        Text(
            text = "My Tickets",
            style = MaterialTheme.typography.headlineLarge.copy(
                fontWeight = FontWeight.ExtraBold,
                fontSize = 28.sp
            ),
            color = GlassOnSurface()
        )
        
        GlassmorphicCard(
            onClick = onSearchClick,
            shape = RoundedCornerShape(16.dp)
        ) {
            Icon(
                imageVector = Icons.Default.Search,
                contentDescription = "Search",
                tint = GlassOnSurface(),
                modifier = Modifier
                    .padding(12.dp)
                    .size(20.dp)
            )
        }
    }
}

@Composable
private fun ModernFilterChips(
    selectedFilter: String,
    onFilterChange: (String) -> Unit
) {
    val filters = listOf("All", "Active", "Used", "Expired")
    
    LazyRow(
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        contentPadding = PaddingValues(horizontal = 4.dp)
    ) {
        items(filters) { filter ->
            GlassmorphicCard(
                onClick = { onFilterChange(filter) },
                shape = RoundedCornerShape(20.dp)
            ) {
                Text(
                    text = filter,
                    modifier = Modifier.padding(horizontal = 20.dp, vertical = 12.dp),
                    style = MaterialTheme.typography.labelLarge.copy(
                        fontWeight = if (selectedFilter == filter) FontWeight.Bold else FontWeight.Medium
                    ),
                    color = if (selectedFilter == filter) GlassPrimary else GlassOnSurfaceVariant()
                )
            }
        }
    }
}

@Composable
private fun ModernTicketStats(
    tickets: List<Ticket>
) {
    val activeTickets = tickets.count { it.status == TicketStatus.ACTIVE }
    val totalValue = tickets.filter { it.status == TicketStatus.ACTIVE }.sumOf { it.price }
    
    GlassmorphicCard(
        shape = RoundedCornerShape(24.dp)
    ) {
        Box {
            // Background gradient
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(120.dp)
                    .background(
                        brush = Brush.linearGradient(
                            colors = listOf(GlassPrimary, GlassSecondary)
                        ),
                        shape = RoundedCornerShape(24.dp)
                    )
            )
            
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(24.dp),
                horizontalArrangement = Arrangement.SpaceAround
            ) {
                StatColumn(
                    value = activeTickets.toString(),
                    label = "Active Tickets",
                    icon = Icons.Default.ConfirmationNumber
                )
                
                StatColumn(
                    value = "KSH $totalValue",
                    label = "Total Value",
                    icon = Icons.Default.AttachMoney
                )
            }
        }
    }
}

@Composable
private fun StatColumn(
    value: String,
    label: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = Color.White.copy(alpha = 0.9f),
            modifier = Modifier.size(24.dp)
        )
        
        Spacer(modifier = Modifier.height(8.dp))
        
        Text(
            text = value,
            style = MaterialTheme.typography.titleLarge.copy(
                fontWeight = FontWeight.Bold,
                fontSize = 20.sp
            ),
            color = Color.White
        )
        
        Text(
            text = label,
            style = MaterialTheme.typography.bodySmall,
            color = Color.White.copy(alpha = 0.8f)
        )
    }
}

@Composable
private fun ModernTicketCard(
    ticket: Ticket,
    onTicketClick: () -> Unit,
    onQRCodeClick: () -> Unit,
    onAddToWalletClick: () -> Unit,
    onShareClick: () -> Unit
) {
    GlassmorphicCard(
        onClick = onTicketClick,
        shape = RoundedCornerShape(20.dp)
    ) {
        Column {
            // Ticket Header with Event Image
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(140.dp)
            ) {
                AsyncImage(
                    model = ticket.eventImageUrl,
                    contentDescription = null,
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )
                
                // Gradient overlay
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(
                            brush = Brush.verticalGradient(
                                colors = listOf(
                                    Color.Transparent,
                                    Color.Black.copy(alpha = 0.6f)
                                )
                            )
                        )
                )
                
                // Status badge
                GlassmorphicCard(
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .padding(12.dp)
                ) {
                    Text(
                        text = ticket.status.name,
                        style = MaterialTheme.typography.labelSmall.copy(
                            fontWeight = FontWeight.SemiBold
                        ),
                        color = when (ticket.status) {
                            TicketStatus.ACTIVE -> Color.Green
                            TicketStatus.USED -> Color.Gray
                            TicketStatus.EXPIRED -> Color.Red
                            else -> GlassOnSurface()
                        },
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
                    )
                }
                
                // Event title
                Text(
                    text = ticket.eventTitle,
                    style = MaterialTheme.typography.titleLarge.copy(
                        fontWeight = FontWeight.Bold
                    ),
                    color = Color.White,
                    modifier = Modifier
                        .align(Alignment.BottomStart)
                        .padding(16.dp)
                )
            }
            
            // Ticket Details
            Column(
                modifier = Modifier.padding(20.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column {
                        Text(
                            text = "Date & Time",
                            style = MaterialTheme.typography.bodySmall,
                            color = GlassOnSurfaceVariant()
                        )
                        Text(
                            text = formatTicketDateTime(ticket.eventDateTime),
                            style = MaterialTheme.typography.bodyMedium.copy(
                                fontWeight = FontWeight.SemiBold
                            ),
                            color = GlassOnSurface()
                        )
                    }
                    
                    Column(
                        horizontalAlignment = Alignment.End
                    ) {
                        Text(
                            text = "Price",
                            style = MaterialTheme.typography.bodySmall,
                            color = GlassOnSurfaceVariant()
                        )
                        Text(
                            text = "${ticket.currency} ${ticket.price}",
                            style = MaterialTheme.typography.bodyMedium.copy(
                                fontWeight = FontWeight.Bold
                            ),
                            color = GlassPrimary
                        )
                    }
                }
                
                Spacer(modifier = Modifier.height(16.dp))
                
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column {
                        Text(
                            text = "Location",
                            style = MaterialTheme.typography.bodySmall,
                            color = GlassOnSurfaceVariant()
                        )
                        Text(
                            text = ticket.venue,
                            style = MaterialTheme.typography.bodyMedium.copy(
                                fontWeight = FontWeight.SemiBold
                            ),
                            color = GlassOnSurface()
                        )
                    }
                    
                    Column(
                        horizontalAlignment = Alignment.End
                    ) {
                        Text(
                            text = "Ticket ID",
                            style = MaterialTheme.typography.bodySmall,
                            color = GlassOnSurfaceVariant()
                        )
                        Text(
                            text = "#${ticket.id.take(8)}",
                            style = MaterialTheme.typography.bodyMedium.copy(
                                fontWeight = FontWeight.SemiBold
                            ),
                            color = GlassOnSurface()
                        )
                    }
                }
                
                Spacer(modifier = Modifier.height(20.dp))
                
                // Action buttons
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    if (ticket.status == TicketStatus.ACTIVE) {
                        NeumorphicButton(
                            onClick = onQRCodeClick,
                            modifier = Modifier.weight(1f),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = GlassPrimary,
                                contentColor = Color.White
                            ),
                            shape = RoundedCornerShape(16.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.QrCode,
                                contentDescription = null,
                                modifier = Modifier.size(18.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "Show QR",
                                style = MaterialTheme.typography.labelLarge.copy(
                                    fontWeight = FontWeight.SemiBold
                                )
                            )
                        }
                    }
                    
                    GlassmorphicCard(
                        onClick = onAddToWalletClick,
                        shape = RoundedCornerShape(16.dp),
                        modifier = Modifier.weight(1f)
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(12.dp),
                            horizontalArrangement = Arrangement.Center,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = Icons.Default.Wallet,
                                contentDescription = null,
                                tint = GlassOnSurface(),
                                modifier = Modifier.size(18.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "Add to Wallet",
                                style = MaterialTheme.typography.labelLarge.copy(
                                    fontWeight = FontWeight.SemiBold
                                ),
                                color = GlassOnSurface()
                            )
                        }
                    }
                    
                    GlassmorphicCard(
                        onClick = onShareClick,
                        shape = RoundedCornerShape(16.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Share,
                            contentDescription = "Share",
                            tint = GlassOnSurface(),
                            modifier = Modifier
                                .padding(12.dp)
                                .size(20.dp)
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun ModernEmptyTicketsState(
    onBrowseEventsClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(40.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        GlassmorphicCard(
            shape = RoundedCornerShape(30.dp)
        ) {
            Icon(
                imageVector = Icons.Default.ConfirmationNumber,
                contentDescription = null,
                tint = GlassPrimary,
                modifier = Modifier
                    .padding(32.dp)
                    .size(64.dp)
            )
        }
        
        Spacer(modifier = Modifier.height(24.dp))
        
        Text(
            text = "No Tickets Yet",
            style = MaterialTheme.typography.headlineMedium.copy(
                fontWeight = FontWeight.Bold
            ),
            color = GlassOnSurface()
        )
        
        Spacer(modifier = Modifier.height(12.dp))
        
        Text(
            text = "You haven't purchased any tickets yet.\nStart exploring events to find your next adventure!",
            style = MaterialTheme.typography.bodyLarge,
            color = GlassOnSurfaceVariant(),
            textAlign = androidx.compose.ui.text.style.TextAlign.Center
        )
        
        Spacer(modifier = Modifier.height(32.dp))
        
        NeumorphicButton(
            onClick = onBrowseEventsClick,
            colors = ButtonDefaults.buttonColors(
                containerColor = GlassPrimary,
                contentColor = Color.White
            ),
            shape = RoundedCornerShape(20.dp),
            contentPadding = PaddingValues(horizontal = 32.dp, vertical = 16.dp)
        ) {
            Icon(
                imageVector = Icons.Default.Explore,
                contentDescription = null,
                modifier = Modifier.size(20.dp)
            )
            Spacer(modifier = Modifier.width(12.dp))
            Text(
                text = "Browse Events",
                style = MaterialTheme.typography.titleMedium.copy(
                    fontWeight = FontWeight.Bold
                )
            )
        }
    }
}

@Composable
private fun formatTicketDateTime(timestamp: Long): String {
    val sdf = java.text.SimpleDateFormat("MMM d • h:mm a", java.util.Locale.getDefault())
    return sdf.format(java.util.Date(timestamp))
}
