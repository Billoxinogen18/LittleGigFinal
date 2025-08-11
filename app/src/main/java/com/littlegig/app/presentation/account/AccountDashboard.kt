package com.littlegig.app.presentation.account

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.compose.ui.platform.LocalContext
import android.content.Intent
import androidx.compose.runtime.rememberCoroutineScope
import kotlinx.coroutines.launch
import com.littlegig.app.data.repository.SharingRepository
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.littlegig.app.data.model.User
import com.littlegig.app.data.model.UserRank
import com.littlegig.app.presentation.components.*
import com.littlegig.app.presentation.theme.*

@Composable
fun AccountDashboard(
    viewModel: AccountViewModel = hiltViewModel(),
    onNavigateToEditProfile: () -> Unit,
    onNavigateToSettings: () -> Unit,
    onNavigateToPayments: () -> Unit,
    onNavigateToTickets: () -> Unit,
    onSignOut: () -> Unit,
    onLinkAccount: () -> Unit = {}
) {
    val uiState by viewModel.uiState.collectAsState()
    val currentUser by viewModel.currentUser.collectAsState()
    
    val context = LocalContext.current
    val shareScope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    // Visual success/error toasts
    LaunchedEffect(uiState.isSuccess) {
        if (uiState.isSuccess) {
            scope.launch { snackbarHostState.showSnackbar("Success") }
            viewModel.clearSuccess()
        }
    }
    LaunchedEffect(uiState.error) {
        uiState.error?.let { msg ->
            scope.launch { snackbarHostState.showSnackbar(msg) }
            viewModel.clearError()
        }
    }
    Scaffold(
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) }
    ) { pad ->
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(pad).padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            // Active Now toggle with permission prompt
            AdvancedGlassmorphicCard {
                Column(modifier = Modifier.padding(20.dp)) {
                    Text(
                        text = "Active Now",
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.SemiBold),
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Spacer(Modifier.height(8.dp))
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        val isActiveNow by viewModel.isActiveNow.collectAsState()
                        Switch(
                            checked = isActiveNow,
                            onCheckedChange = { value ->
                                viewModel.toggleActiveNow(value)
                            }
                        )
                        Spacer(Modifier.width(12.dp))
                        Text(
                            text = if (isActiveNow) "Sharing activity and location" else "Inactive",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                    Spacer(Modifier.height(8.dp))
                    Text(
                        text = "We’ll ask for location permission when needed.",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }

        item {
            // Profile Header
            AdvancedGlassmorphicCard {
                Column(
                    modifier = Modifier.padding(20.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    // Profile Picture
                    Box(
                        modifier = Modifier
                            .size(100.dp)
                            .clip(CircleShape)
                            .background(LittleGigPrimary.copy(alpha = 0.1f)),
                        contentAlignment = Alignment.Center
                    ) {
                        if (currentUser?.profilePictureUrl?.isNotEmpty() == true) {
                            AsyncImage(
                                model = currentUser?.profilePictureUrl,
                                contentDescription = "Profile Picture",
                                modifier = Modifier.fillMaxSize(),
                                contentScale = ContentScale.Crop
                            )
                        } else {
                            Icon(
                                imageVector = Icons.Default.Person,
                                contentDescription = null,
                                modifier = Modifier.size(50.dp),
                                tint = LittleGigPrimary
                            )
                        }
                    }
                    
                    Spacer(modifier = Modifier.height(16.dp))
                    
                    // User Info
                    Text(
                        text = currentUser?.displayName ?: "User",
                        style = MaterialTheme.typography.headlineSmall.copy(
                            fontWeight = FontWeight.Bold
                        ),
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    
                    Text(
                        text = currentUser?.email ?: "",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    
                    Spacer(modifier = Modifier.height(8.dp))
                    
                    // Rank Badge
                    NeumorphicRankBadge(
                        rank = (currentUser?.rank ?: UserRank.NOVICE).name,
                        modifier = Modifier.padding(vertical = 8.dp)
                    )
                }
            }
        }
        
        item {
            // Quick Stats
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                StatCard(
                    title = "Events Created",
                    value = uiState.eventsCreated.toString(),
                    icon = Icons.Default.Event,
                    modifier = Modifier.weight(1f)
                )
                
                StatCard(
                    title = "Tickets Bought",
                    value = uiState.ticketsBought.toString(),
                    icon = Icons.Default.ConfirmationNumber,
                    modifier = Modifier.weight(1f)
                )
            }
        }
        
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                StatCard(
                    title = "Recaps Shared",
                    value = uiState.recapsShared.toString(),
                    icon = Icons.Default.PhotoCamera,
                    modifier = Modifier.weight(1f)
                )
                
                StatCard(
                    title = "Total Spent",
                    value = "KSH ${uiState.totalSpent}",
                    icon = Icons.Default.Payments,
                    modifier = Modifier.weight(1f)
                )
            }
        }
        
        item {
            // Account Actions
            AdvancedGlassmorphicCard {
                Column(
                    modifier = Modifier.padding(20.dp)
                ) {
                    Text(
                        text = "Account",
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.SemiBold
                        ),
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    
                    Spacer(modifier = Modifier.height(16.dp))
                    
                    AccountActionItem(
                        icon = Icons.Default.Edit,
                        title = "Edit Profile",
                        subtitle = "Update your information",
                        onClick = onNavigateToEditProfile
                    )
                    
                    AccountActionItem(
                        icon = Icons.Default.Payments,
                        title = "Payment History",
                        subtitle = "View your transactions",
                        onClick = onNavigateToPayments
                    )
                    
                    AccountActionItem(
                        icon = Icons.Default.ConfirmationNumber,
                        title = "My Tickets",
                        subtitle = "View purchased tickets",
                        onClick = onNavigateToTickets
                    )
                    
                    AccountActionItem(
                        icon = Icons.Default.Settings,
                        title = "Settings",
                        subtitle = "App preferences",
                        onClick = onNavigateToSettings
                    )

                    AccountActionItem(
                        icon = Icons.Default.Share,
                        title = "Invite friends",
                        subtitle = "Share your referral link",
                        onClick = {
                            // Simple app share link; for per-user referral, we’d include user id
                            shareScope.launch {
                                val sendIntent = Intent(Intent.ACTION_SEND).apply {
                                    type = "text/plain"
                                    putExtra(Intent.EXTRA_TEXT, "Check out LittleGig – discover and share events! https://littlegig.app/download")
                                }
                                val shareIntent = Intent.createChooser(sendIntent, "Invite Friends")
                                context.startActivity(shareIntent)
                            }
                        }
                    )
                }
            }
        }
        
        item {
            // Analytics Section
            AdvancedGlassmorphicCard {
                Column(
                    modifier = Modifier.padding(20.dp)
                ) {
                    Text(
                        text = "Analytics",
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.SemiBold
                        ),
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    
                    Spacer(modifier = Modifier.height(16.dp))
                    
                    AnalyticsItem(
                        title = "Engagement Score",
                        value = uiState.engagementScore.toString(),
                        progress = uiState.engagementScore / 1000f,
                        color = LittleGigPrimary
                    )
                    
                    AnalyticsItem(
                        title = "Events Attended",
                        value = uiState.eventsAttended.toString(),
                        progress = uiState.eventsAttended / 50f,
                        color = Color(0xFF10B981)
                    )
                    
                    AnalyticsItem(
                        title = "Recaps Created",
                        value = uiState.recapsCreated.toString(),
                        progress = uiState.recapsCreated / 20f,
                        color = Color(0xFFF59E0B)
                    )
                }
            }
        }
        
        item {
            // Link Account Button for Anonymous Users
            currentUser?.let { user ->
                if (user.email.isEmpty()) {
                    HapticButton(
                        onClick = onLinkAccount,
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
                                    imageVector = Icons.Default.AccountCircle,
                                    contentDescription = null,
                                    tint = LittleGigPrimary,
                                    modifier = Modifier.size(24.dp)
                                )
                                
                                Spacer(modifier = Modifier.width(12.dp))
                                
                                Text(
                                    text = "Link Account",
                                    style = MaterialTheme.typography.bodyLarge.copy(
                                        fontWeight = FontWeight.Medium
                                    ),
                                    color = LittleGigPrimary
                                )
                                
                                Spacer(modifier = Modifier.weight(1f))
                                
                                Text(
                                    text = "Anonymous User",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                                )
                            }
                        }
                    }
                }
            }
        }
        
        item {
            // Sign Out Button
            HapticButton(
                onClick = onSignOut,
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
                            imageVector = Icons.Default.Logout,
                            contentDescription = null,
                            tint = Color(0xFFEF4444),
                            modifier = Modifier.size(24.dp)
                        )
                        
                        Spacer(modifier = Modifier.width(12.dp))
                        
                        Text(
                            text = "Sign Out",
                            style = MaterialTheme.typography.bodyLarge.copy(
                                fontWeight = FontWeight.Medium
                            ),
                            color = Color(0xFFEF4444)
                        )
                    }
                }
            }
        }
    }
    }
}

@Composable
fun StatCard(
    title: String,
    value: String,
    icon: ImageVector,
    modifier: Modifier = Modifier
) {
    AdvancedNeumorphicCard(
        modifier = modifier
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = LittleGigPrimary,
                modifier = Modifier.size(24.dp)
            )
            
            Spacer(modifier = Modifier.height(8.dp))
            
            Text(
                text = value,
                style = MaterialTheme.typography.headlineSmall.copy(
                    fontWeight = FontWeight.Bold
                ),
                color = MaterialTheme.colorScheme.onSurface
            )
            
            Text(
                text = title,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

@Composable
fun AccountActionItem(
    icon: ImageVector,
    title: String,
    subtitle: String,
    onClick: () -> Unit
) {
    HapticButton(
        onClick = onClick,
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = LittleGigPrimary,
                modifier = Modifier.size(24.dp)
            )
            
            Spacer(modifier = Modifier.width(12.dp))
            
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.bodyLarge.copy(
                        fontWeight = FontWeight.Medium
                    ),
                    color = MaterialTheme.colorScheme.onSurface
                )
                
                Text(
                    text = subtitle,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            
            Icon(
                imageVector = Icons.Default.ChevronRight,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.size(20.dp)
            )
        }
    }
}

@Composable
fun AnalyticsItem(
    title: String,
    value: String,
    progress: Float,
    color: Color
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurface
            )
            
            Text(
                text = value,
                style = MaterialTheme.typography.bodyMedium.copy(
                    fontWeight = FontWeight.Bold
                ),
                color = color
            )
        }
        
        Spacer(modifier = Modifier.height(4.dp))
        
        LinearProgressIndicator(
            progress = progress.coerceIn(0f, 1f),
            modifier = Modifier
                .fillMaxWidth()
                .height(4.dp)
                .clip(RoundedCornerShape(2.dp)),
            color = color,
            trackColor = color.copy(alpha = 0.2f)
        )
    }
} 