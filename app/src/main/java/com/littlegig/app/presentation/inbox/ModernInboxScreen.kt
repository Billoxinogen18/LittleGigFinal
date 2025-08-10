package com.littlegig.app.presentation.inbox

import androidx.compose.animation.core.*
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
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
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.littlegig.app.presentation.components.*
import com.littlegig.app.data.model.Notification
import com.littlegig.app.data.model.NotificationType

@Composable
fun ModernInboxScreen(
    navController: NavController,
    viewModel: InboxViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val selectedFilter by viewModel.selectedFilter.collectAsState()
    
    LaunchedEffect(Unit) {
        viewModel.load()
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
            ModernInboxHeader(
                unreadCount = uiState.notifications.count { !it.isRead },
                onBackClick = { navController.popBackStack() },
                onMarkAllReadClick = { viewModel.markAllAsRead() }
            )
            
            // Filter chips
            ModernInboxFilters(
                selectedFilter = selectedFilter,
                onFilterChange = { viewModel.updateFilter(it) },
                notifications = uiState.notifications
            )
            
            // Notifications list
            if (uiState.isLoading) {
                ModernInboxLoadingState()
            } else if (uiState.notifications.isEmpty()) {
                ModernEmptyInboxState()
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(horizontal = 20.dp, vertical = 16.dp, bottom = 100.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    // Quick actions
                    item {
                        ModernQuickActions(
                            onClearAllClick = { viewModel.clearAllNotifications() },
                            onSettingsClick = { navController.navigate("notification_settings") }
                        )
                    }
                    
                    // Notifications
                    items(uiState.notifications) { notification ->
                        ModernNotificationItem(
                            notification = notification,
                            onClick = {
                                viewModel.markAsRead(notification.id)
                                handleNotificationClick(notification, navController)
                            },
                            onDismiss = { viewModel.deleteNotification(notification.id) }
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun ModernInboxHeader(
    unreadCount: Int,
    onBackClick: () -> Unit,
    onMarkAllReadClick: () -> Unit
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
                text = "Notifications",
                style = MaterialTheme.typography.headlineLarge.copy(
                    fontWeight = FontWeight.ExtraBold,
                    fontSize = 28.sp
                ),
                color = GlassOnSurface()
            )
            
            if (unreadCount > 0) {
                Text(
                    text = "$unreadCount unread",
                    style = MaterialTheme.typography.bodySmall,
                    color = GlassPrimary
                )
            }
        }
        
        GlassmorphicCard(
            onClick = onMarkAllReadClick,
            shape = RoundedCornerShape(16.dp)
        ) {
            Icon(
                imageVector = Icons.Default.DoneAll,
                contentDescription = "Mark All Read",
                tint = GlassOnSurface(),
                modifier = Modifier
                    .padding(12.dp)
                    .size(20.dp)
            )
        }
    }
}

@Composable
private fun ModernInboxFilters(
    selectedFilter: String,
    onFilterChange: (String) -> Unit,
    notifications: List<Notification>
) {
    val filters = mapOf(
        "All" to notifications.size,
        "Unread" to notifications.count { !it.isRead },
        "Events" to notifications.count { it.type == NotificationType.EVENT_REMINDER },
        "Messages" to notifications.count { it.type == NotificationType.NEW_MESSAGE },
        "Tickets" to notifications.count { it.type == NotificationType.TICKET_PURCHASED }
    )
    
    LazyRow(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        items(filters.toList()) { (filter, count) ->
            GlassmorphicCard(
                onClick = { onFilterChange(filter) },
                shape = RoundedCornerShape(20.dp)
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = filter,
                        style = MaterialTheme.typography.labelLarge.copy(
                            fontWeight = if (selectedFilter == filter) FontWeight.Bold else FontWeight.Medium
                        ),
                        color = if (selectedFilter == filter) GlassPrimary else GlassOnSurfaceVariant()
                    )
                    
                    if (count > 0) {
                        Spacer(modifier = Modifier.width(8.dp))
                        Box(
                            modifier = Modifier
                                .background(
                                    if (selectedFilter == filter) GlassPrimary else GlassOnSurfaceVariant(),
                                    CircleShape
                                )
                                .padding(horizontal = 6.dp, vertical = 2.dp)
                        ) {
                            Text(
                                text = count.toString(),
                                style = MaterialTheme.typography.labelSmall.copy(
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

@Composable
private fun ModernQuickActions(
    onClearAllClick: () -> Unit,
    onSettingsClick: () -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        GlassmorphicCard(
            onClick = onClearAllClick,
            shape = RoundedCornerShape(16.dp),
            modifier = Modifier.weight(1f)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                Icon(
                    imageVector = Icons.Default.ClearAll,
                    contentDescription = null,
                    tint = GlassOnSurfaceVariant(),
                    modifier = Modifier.size(18.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "Clear All",
                    style = MaterialTheme.typography.labelLarge.copy(
                        fontWeight = FontWeight.SemiBold
                    ),
                    color = GlassOnSurfaceVariant()
                )
            }
        }
        
        GlassmorphicCard(
            onClick = onSettingsClick,
            shape = RoundedCornerShape(16.dp),
            modifier = Modifier.weight(1f)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Settings,
                    contentDescription = null,
                    tint = GlassOnSurfaceVariant(),
                    modifier = Modifier.size(18.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "Settings",
                    style = MaterialTheme.typography.labelLarge.copy(
                        fontWeight = FontWeight.SemiBold
                    ),
                    color = GlassOnSurfaceVariant()
                )
            }
        }
    }
}

@Composable
private fun ModernNotificationItem(
    notification: Notification,
    onClick: () -> Unit,
    onDismiss: () -> Unit
) {
    GlassmorphicCard(
        onClick = onClick,
        shape = RoundedCornerShape(20.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.Top
        ) {
            // Notification icon
            GlassmorphicCard(
                shape = RoundedCornerShape(16.dp)
            ) {
                Box(
                    modifier = Modifier
                        .size(48.dp)
                        .background(
                            brush = Brush.radialGradient(
                                colors = when (notification.type) {
                                    NotificationType.EVENT_REMINDER -> listOf(GlassPrimary, GlassSecondary)
                                    NotificationType.NEW_MESSAGE -> listOf(GlassAccent, GlassPrimary)
                                    NotificationType.TICKET_PURCHASED -> listOf(Color.Green, GlassAccent)
                                    NotificationType.EVENT_UPDATE -> listOf(GlassSecondary, GlassPrimary)
                                    else -> listOf(GlassOnSurfaceVariant(), GlassOnSurfaceVariant())
                                }
                            ),
                            shape = RoundedCornerShape(16.dp)
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = when (notification.type) {
                            NotificationType.EVENT_REMINDER -> Icons.Default.Event
                            NotificationType.NEW_MESSAGE -> Icons.Default.Message
                            NotificationType.TICKET_PURCHASED -> Icons.Default.ConfirmationNumber
                            NotificationType.EVENT_UPDATE -> Icons.Default.Update
                            else -> Icons.Default.Notifications
                        },
                        contentDescription = null,
                        tint = Color.White,
                        modifier = Modifier.size(24.dp)
                    )
                }
            }
            
            Spacer(modifier = Modifier.width(16.dp))
            
            // Notification content
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.Top
                ) {
                    Text(
                        text = notification.title,
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = if (!notification.isRead) FontWeight.Bold else FontWeight.SemiBold
                        ),
                        color = GlassOnSurface(),
                        modifier = Modifier.weight(1f)
                    )
                    
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = formatNotificationTime(notification.timestamp),
                            style = MaterialTheme.typography.bodySmall,
                            color = if (!notification.isRead) GlassPrimary else GlassOnSurfaceVariant()
                        )
                        
                        Spacer(modifier = Modifier.width(8.dp))
                        
                        GlassmorphicCard(
                            onClick = onDismiss,
                            shape = CircleShape
                        ) {
                            Icon(
                                imageVector = Icons.Default.Close,
                                contentDescription = "Dismiss",
                                tint = GlassOnSurfaceVariant(),
                                modifier = Modifier
                                    .padding(4.dp)
                                    .size(16.dp)
                            )
                        }
                    }
                }
                
                Spacer(modifier = Modifier.height(4.dp))
                
                Text(
                    text = notification.message,
                    style = MaterialTheme.typography.bodyMedium,
                    color = if (!notification.isRead) GlassOnSurface() else GlassOnSurfaceVariant(),
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
                
                // Unread indicator
                if (!notification.isRead) {
                    Spacer(modifier = Modifier.height(8.dp))
                    Box(
                        modifier = Modifier
                            .size(8.dp)
                            .background(GlassPrimary, CircleShape)
                    )
                }
            }
        }
    }
}

@Composable
private fun ModernInboxLoadingState() {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(20.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        items(8) {
            ModernNotificationItemSkeleton()
        }
    }
}

@Composable
private fun ModernNotificationItemSkeleton() {
    GlassmorphicCard(
        shape = RoundedCornerShape(20.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.Top
        ) {
            // Icon skeleton
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .background(
                        GlassOnSurfaceVariant().copy(alpha = 0.3f),
                        RoundedCornerShape(16.dp)
                    )
            )
            
            Spacer(modifier = Modifier.width(16.dp))
            
            Column(
                modifier = Modifier.weight(1f)
            ) {
                // Title skeleton
                Box(
                    modifier = Modifier
                        .fillMaxWidth(0.6f)
                        .height(16.dp)
                        .background(
                            GlassOnSurfaceVariant().copy(alpha = 0.3f),
                            RoundedCornerShape(8.dp)
                        )
                )
                
                Spacer(modifier = Modifier.height(8.dp))
                
                // Message skeleton
                Box(
                    modifier = Modifier
                        .fillMaxWidth(0.9f)
                        .height(14.dp)
                        .background(
                            GlassOnSurfaceVariant().copy(alpha = 0.2f),
                            RoundedCornerShape(7.dp)
                        )
                )
                
                Spacer(modifier = Modifier.height(4.dp))
                
                Box(
                    modifier = Modifier
                        .fillMaxWidth(0.7f)
                        .height(14.dp)
                        .background(
                            GlassOnSurfaceVariant().copy(alpha = 0.2f),
                            RoundedCornerShape(7.dp)
                        )
                )
            }
        }
    }
}

@Composable
private fun ModernEmptyInboxState() {
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
                imageVector = Icons.Default.Notifications,
                contentDescription = null,
                tint = GlassPrimary,
                modifier = Modifier
                    .padding(32.dp)
                    .size(64.dp)
            )
        }
        
        Spacer(modifier = Modifier.height(24.dp))
        
        Text(
            text = "All Caught Up!",
            style = MaterialTheme.typography.headlineMedium.copy(
                fontWeight = FontWeight.Bold
            ),
            color = GlassOnSurface()
        )
        
        Spacer(modifier = Modifier.height(12.dp))
        
        Text(
            text = "You're all set! New notifications\nwill appear here when they arrive.",
            style = MaterialTheme.typography.bodyLarge,
            color = GlassOnSurfaceVariant(),
            textAlign = androidx.compose.ui.text.style.TextAlign.Center
        )
    }
}

private fun handleNotificationClick(
    notification: Notification,
    navController: NavController
) {
    when (notification.type) {
        NotificationType.EVENT_REMINDER -> {
            notification.eventId?.let { eventId ->
                navController.navigate("event_details/$eventId")
            }
        }
        NotificationType.NEW_MESSAGE -> {
            notification.chatId?.let { chatId ->
                navController.navigate("chat_details/$chatId")
            }
        }
        NotificationType.TICKET_PURCHASED -> {
            navController.navigate("tickets")
        }
        NotificationType.EVENT_UPDATE -> {
            notification.eventId?.let { eventId ->
                navController.navigate("event_details/$eventId")
            }
        }
        else -> {
            // Handle other notification types
        }
    }
}

@Composable
private fun formatNotificationTime(timestamp: Long): String {
    val now = System.currentTimeMillis()
    val diff = now - timestamp
    
    return when {
        diff < 60_000 -> "now"
        diff < 3600_000 -> "${diff / 60_000}m"
        diff < 86400_000 -> "${diff / 3600_000}h"
        diff < 604800_000 -> "${diff / 86400_000}d"
        else -> {
            val sdf = java.text.SimpleDateFormat("MMM d", java.util.Locale.getDefault())
            sdf.format(java.util.Date(timestamp))
        }
    }
}
