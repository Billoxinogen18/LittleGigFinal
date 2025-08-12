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
import androidx.compose.ui.text.style.TextAlign
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
import android.content.Intent
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import android.app.Activity
import androidx.compose.foundation.BorderStroke
import androidx.compose.material.icons.outlined.Wallet

@Composable
fun TicketsScreen(
    navController: NavController,
    viewModel: TicketsViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState(initial = TicketsUiState())
    val isDark = isSystemInDarkTheme()
    
    // QR Code scanner launcher
    val qrScannerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val scannedData = result.data?.getStringExtra("SCAN_RESULT")
            scannedData?.let { qrData ->
                // Handle QR code data - could be a ticket ID or event URL
                viewModel.handleQrCodeScan(qrData)
            }
        }
    }

    // QR Scanner Dialog
    if (uiState.showQrScanner) {
        AlertDialog(
            onDismissRequest = { viewModel.hideQrScanner() },
            title = {
                Text(
                    text = "QR Code Scanner",
                    style = MaterialTheme.typography.titleLarge,
                    color = Color.White
                )
            },
            text = {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.QrCodeScanner,
                        contentDescription = null,
                        modifier = Modifier.size(64.dp),
                        tint = LittleGigPrimary
                    )
                    Text(
                        text = "QR Scanner functionality will be implemented soon. For now, you can manually enter ticket codes.",
                        style = MaterialTheme.typography.bodyMedium,
                        color = Color.White.copy(alpha = 0.8f),
                        textAlign = TextAlign.Center
                    )
                }
            },
            confirmButton = {
                NeumorphicButton(
                    onClick = { 
                        viewModel.hideQrScanner()
                        // Simulate QR scan for demo
                        viewModel.handleQrCodeScan("DEMO_TICKET_123")
                    }
                ) {
                    Text("Demo Scan")
                }
            },
            dismissButton = {
                NeumorphicButton(
                    onClick = { viewModel.hideQrScanner() }
                ) {
                    Text("Cancel")
                }
            },
            containerColor = Color(0xFF1E293B),
            titleContentColor = Color.White,
            textContentColor = Color.White
        )
    }
    
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
                            onClick = { 
                                // Show QR scanner dialog instead of launching external app
                                viewModel.showQrScanner()
                            }
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
                        AdvancedLiquidGlassCard {
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
                        AdvancedLiquidGlassCard {
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
                                    onClick = { 
                                        navController.navigate("events")
                                    },
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
    
    AdvancedNeumorphicCard {
        Column(
            modifier = Modifier
                .width(280.dp)
                .padding(16.dp)
        ) {
            // Event image
            AsyncImage(
                model = ticket.eventImageUrl,
                contentDescription = null,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(140.dp)
                    .clip(RoundedCornerShape(12.dp)),
                contentScale = ContentScale.Crop
            )
            
            Spacer(modifier = Modifier.height(12.dp))
            
            Text(
                text = ticket.eventTitle,
                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.SemiBold),
                color = MaterialTheme.colorScheme.onSurface
            )
            Text(
                text = "${java.text.SimpleDateFormat("MMM d, h:mm a").format(java.util.Date(ticket.eventDateTime))}",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            
            Spacer(modifier = Modifier.height(12.dp))
            
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                OutlinedButton(onClick = onTicketClick, shape = RoundedCornerShape(12.dp)) {
                    Icon(Icons.Default.ConfirmationNumber, contentDescription = null)
                    Spacer(Modifier.width(6.dp))
                    Text("View")
                }
                OutlinedButton(onClick = onQrCodeClick, shape = RoundedCornerShape(12.dp)) {
                    Icon(Icons.Default.QrCode, contentDescription = null)
                    Spacer(Modifier.width(6.dp))
                    Text("QR")
                }
                OutlinedButton(onClick = onShareClick, shape = RoundedCornerShape(12.dp)) {
                    Icon(Icons.Default.Share, contentDescription = null)
                    Spacer(Modifier.width(6.dp))
                    Text("Share")
                }
            }
            
            Spacer(modifier = Modifier.height(8.dp))
            
            // Perforation + barcode glass slot
            Spacer(Modifier.height(12.dp))
            // Perforation divider
            val perforationColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.15f)
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(1.dp)
                    .drawBehind {
                        val dot = 6.dp.toPx()
                        val gap = 6.dp.toPx()
                        var x = 0f
                        while (x < size.width) {
                            drawRect(color = perforationColor, topLeft = androidx.compose.ui.geometry.Offset(x, 0f), size = androidx.compose.ui.geometry.Size(dot, 1.dp.toPx()))
                            x += dot + gap
                        }
                    }
            )
            Spacer(Modifier.height(12.dp))
            AdvancedLiquidGlassCard {
                Row(Modifier.fillMaxWidth().padding(12.dp), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.SpaceBetween) {
                    Column(Modifier.weight(1f)) {
                        Text("${ticket.eventLocation}", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                        Text(ticket.qrCode.ifBlank { "RSD0000000000" }, style = MaterialTheme.typography.labelMedium, color = MaterialTheme.colorScheme.onSurface)
                    }
                    Icon(Icons.Default.QrCode, contentDescription = null, tint = LittleGigPrimary)
                }
            }
        }
    }
}



