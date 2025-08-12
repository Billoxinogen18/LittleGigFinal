package com.littlegig.app.presentation.events

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.pager.VerticalPager
import androidx.compose.foundation.pager.rememberPagerState
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
import com.littlegig.app.data.repository.AuthRepository
import javax.inject.Inject
import coil.compose.AsyncImage
import com.littlegig.app.presentation.components.*
import kotlinx.coroutines.launch
import android.content.Intent
import androidx.compose.foundation.ExperimentalFoundationApi

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ModernEventsScreen(
    navController: NavController,
    viewModel: EventsViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val searchQuery by viewModel.searchQuery.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }
    val context = LocalContext.current
    val shareScope = rememberCoroutineScope()
    
    // Auto-paging setup for VerticalPager
    val pagerState = rememberPagerState(pageCount = { uiState.events.size })
    val (isSearchExpanded, setSearchExpanded) = remember { mutableStateOf(false) }
    
    LaunchedEffect(pagerState.currentPage, uiState.events.size) {
        if (uiState.events.isNotEmpty() && pagerState.currentPage >= uiState.events.size - 3) {
            viewModel.loadMoreEvents()
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                if (isSystemInDarkTheme()) {
                    Color(0xFF0F0F23) // Pure dark blue, no grey gradients
                } else {
                    Color(0xFFF8FAFC) // Pure light, no grey gradients  
                }
            )
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            // Modern Header
            ModernEventsHeader(
                onSearchClick = { setSearchExpanded(!isSearchExpanded) },
                onNotificationClick = { navController.navigate("inbox") },
                onProfileClick = { navController.navigate("account") },
                searchQuery = searchQuery,
                isSearchExpanded = isSearchExpanded,
                onSearchQueryChange = { viewModel.updateSearchQuery(it) }
            )

            // Full-screen vertical pager with smooth snapping
            if (uiState.events.isNotEmpty()) {
                VerticalPager(
                    state = pagerState,
                    modifier = Modifier.fillMaxSize()
                ) { page ->
                    val event = uiState.events[page]
                    ModernFullScreenEventCard(
                        event = event,
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(horizontal = 16.dp, vertical = 8.dp),
                        onEventClick = { navController.navigate("event_details/${event.id}") },
                        onLikeClick = { viewModel.toggleEventLike(event.id) },
                        onShareClick = {
                            shareScope.launch {
                                val link = viewModel.createEventShareLink(event)
                                link?.let {
                                    val sendIntent = Intent(Intent.ACTION_SEND).apply {
                                        type = "text/plain"
                                        putExtra(Intent.EXTRA_TEXT, it)
                                    }
                                    val shareIntent = Intent.createChooser(sendIntent, "Share Event")
                                    context.startActivity(shareIntent)
                                }
                            }
                        }
                    )
                }
            } else {
                // Empty state
                ModernEmptyState(
                    onCreateEvent = { navController.navigate("upload") }
                )
            }
        }

        // Modern FAB for creating events
        GlassFAB(
            onClick = { navController.navigate("upload") },
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(end = 24.dp, bottom = 112.dp),
            icon = Icons.Default.Add,
            containerColor = GlassPrimary
        )

        // Loading indicator
        if (uiState.isLoading && uiState.events.isEmpty()) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(color = GlassPrimary)
            }
        }

        // Snackbar host
        SnackbarHost(
            hostState = snackbarHostState,
            modifier = Modifier.align(Alignment.BottomCenter)
        )
    }

    // Handle snackbar messages
    LaunchedEffect(uiState.snackbarMessage) {
        uiState.snackbarMessage?.let { message ->
            snackbarHostState.showSnackbar(message)
            viewModel.clearSnackbar()
        }
    }

    // Load events on first composition
    LaunchedEffect(Unit) {
        if (uiState.events.isEmpty()) {
            viewModel.loadEvents()
        }
    }
}

@Composable
private fun ModernEventsHeader(
    onSearchClick: () -> Unit,
    onNotificationClick: () -> Unit,
    onProfileClick: () -> Unit,
    searchQuery: String = "",
    isSearchExpanded: Boolean = false,
    onSearchQueryChange: (String) -> Unit = {}
) {
    Column {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp, vertical = 24.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Title with gradient text effect
        Text(
            text = "Events",
            style = MaterialTheme.typography.headlineLarge.copy(
                fontWeight = FontWeight.ExtraBold,
                fontSize = 32.sp
            ),
            color = GlassOnSurface()
        )
        
        // Action buttons
        Row(
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Search button
            ModernLiquidGlassCard(
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
            
            // Notification button with badge
            ModernLiquidGlassCard(
                onClick = onNotificationClick,
                shape = RoundedCornerShape(16.dp)
            ) {
                Box {
                    Icon(
                        imageVector = Icons.Default.Notifications,
                        contentDescription = "Notifications",
                        tint = GlassOnSurface(),
                        modifier = Modifier
                            .padding(12.dp)
                            .size(20.dp)
                    )
                    // Notification badge
                    Box(
                        modifier = Modifier
                            .size(8.dp)
                            .background(GlassPink, CircleShape)
                            .align(Alignment.TopEnd)
                            .offset(x = (-4).dp, y = 4.dp)
                    )
                }
            }
            
            // Profile button
            ModernLiquidGlassCard(
                onClick = onProfileClick,
                shape = RoundedCornerShape(16.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Person,
                    contentDescription = "Account",
                    tint = GlassOnSurface(),
                    modifier = Modifier
                        .padding(12.dp)
                        .size(20.dp)
                )
            }
        }
    } // End Row
    
    // Expandable Search Field
    AnimatedVisibility(
        visible = isSearchExpanded,
        enter = slideInVertically() + fadeIn(),
        exit = slideOutVertically() + fadeOut()
    ) {
        OutlinedTextField(
            value = searchQuery,
            onValueChange = onSearchQueryChange,
            placeholder = { Text("Search events...", color = GlassOnSurface().copy(alpha = 0.6f)) },
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp)
                .padding(bottom = 16.dp)
                .clip(RoundedCornerShape(16.dp)),
            leadingIcon = {
                Icon(
                    imageVector = Icons.Default.Search,
                    contentDescription = null,
                    tint = GlassOnSurface().copy(alpha = 0.7f),
                    modifier = Modifier.size(20.dp)
                )
            },
            trailingIcon = if (searchQuery.isNotEmpty()) {
                {
                    IconButton(onClick = { onSearchQueryChange("") }) {
                        Icon(
                            imageVector = Icons.Default.Clear,
                            contentDescription = "Clear search",
                            tint = GlassOnSurface().copy(alpha = 0.7f),
                            modifier = Modifier.size(18.dp)
                        )
                    }
                }
            } else null,
            
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = GlassPink,
                unfocusedBorderColor = GlassOnSurface().copy(alpha = 0.3f),
                focusedContainerColor = GlassOnSurface().copy(alpha = 0.05f),
                unfocusedContainerColor = GlassOnSurface().copy(alpha = 0.03f),
                focusedTextColor = GlassOnSurface(),
                unfocusedTextColor = GlassOnSurface()
            ),
            singleLine = true
        )
    }
    } // End Column
}

@Composable
private fun ModernFullScreenEventCard(
    event: com.littlegig.app.data.model.Event,
    modifier: Modifier = Modifier,
    onEventClick: () -> Unit,
    onLikeClick: () -> Unit,
    onShareClick: () -> Unit,
    viewModel: EventsViewModel = hiltViewModel()
) {
    ModernLiquidGlassCard(
        modifier = modifier.clickable { onEventClick() },
        shape = RoundedCornerShape(24.dp)
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            // Background Image with gradient overlay
            AsyncImage(
                model = event.imageUrls.firstOrNull() ?: "",
                contentDescription = null,
                modifier = Modifier
                    .fillMaxSize()
                    .clip(RoundedCornerShape(24.dp)),
                contentScale = ContentScale.Crop
            )
            
            // Gradient overlays for better text visibility
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        brush = Brush.verticalGradient(
                            colors = listOf(
                                Color.Black.copy(alpha = 0.3f),
                                Color.Transparent,
                                Color.Black.copy(alpha = 0.8f)
                            ),
                            startY = 0f,
                            endY = Float.POSITIVE_INFINITY
                        )
                    )
            )
            
            // Content overlay
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(24.dp),
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                // Top section - Category and actions
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.Top
                ) {
                    // Category badge
                    ModernLiquidGlassCard(
                        shape = RoundedCornerShape(20.dp)
                    ) {
                        Text(
                            text = event.category.name,
                            style = MaterialTheme.typography.labelMedium.copy(
                                fontWeight = FontWeight.SemiBold
                            ),
                            color = Color.White,
                            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
                        )
                    }
                    
                    // Action buttons
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        ModernLiquidGlassCard(
                            modifier = Modifier.clickable { onLikeClick() },
                            shape = RoundedCornerShape(16.dp)
                        ) {
                            Icon(
                                imageVector = if (event.likedBy.contains(viewModel.currentUserId)) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                                contentDescription = "Like",
                                tint = if (event.likedBy.contains(viewModel.currentUserId)) Color.Red else Color.White,
                                modifier = Modifier
                                    .padding(12.dp)
                                    .size(20.dp)
                            )
                        }
                        
                        ModernLiquidGlassCard(
                            modifier = Modifier.clickable { onShareClick() },
                            shape = RoundedCornerShape(16.dp)
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
                
                // Bottom section - Event details
                Column(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    // Event title
                    Text(
                        text = event.title,
                        style = MaterialTheme.typography.headlineMedium.copy(
                            fontWeight = FontWeight.Bold,
                            fontSize = 28.sp
                        ),
                        color = Color.White
                    )
                    
                    Spacer(modifier = Modifier.height(8.dp))
                    
                    // Event details
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.LocationOn,
                            contentDescription = null,
                            tint = Color.White.copy(alpha = 0.8f),
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = event.location.name,
                            style = MaterialTheme.typography.bodyMedium,
                            color = Color.White.copy(alpha = 0.9f)
                        )
                    }
                    
                    Spacer(modifier = Modifier.height(4.dp))
                    
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.Schedule,
                            contentDescription = null,
                            tint = Color.White.copy(alpha = 0.8f),
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = formatEventDateTime(event.dateTime),
                            style = MaterialTheme.typography.bodyMedium,
                            color = Color.White.copy(alpha = 0.9f)
                        )
                    }
                    
                    Spacer(modifier = Modifier.height(12.dp))
                    
                    // Attendees and Join button
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = Icons.Default.Person,
                                contentDescription = null,
                                tint = Color.White.copy(alpha = 0.8f),
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                                                    Text(
                            text = "+12 attending", // Will implement attendeeCount later
                                style = MaterialTheme.typography.bodySmall,
                                color = Color.White.copy(alpha = 0.8f)
                            )
                        }
                        
                        // Join button
                        Button(
                            onClick = { /* Join event */ },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = GlassPrimary,
                                contentColor = Color.White
                            ),
                            contentPadding = PaddingValues(horizontal = 24.dp, vertical = 12.dp)
                        ) {
                            Text(
                                text = "Join",
                                style = MaterialTheme.typography.labelLarge.copy(
                                    fontWeight = FontWeight.SemiBold
                                )
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun ModernEmptyState(
    onCreateEvent: () -> Unit
) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Icon(
                imageVector = Icons.Default.Event,
                contentDescription = null,
                tint = GlassOnSurfaceVariant(),
                modifier = Modifier.size(64.dp)
            )
            
            Spacer(modifier = Modifier.height(16.dp))
            
            Text(
                text = "No events yet",
                style = MaterialTheme.typography.headlineSmall.copy(
                    fontWeight = FontWeight.SemiBold
                ),
                color = GlassOnSurface()
            )
            
            Spacer(modifier = Modifier.height(8.dp))
            
            Text(
                text = "Be the first to create an amazing event",
                style = MaterialTheme.typography.bodyMedium,
                color = GlassOnSurfaceVariant()
            )
            
            Spacer(modifier = Modifier.height(24.dp))
            
            Button(
                onClick = onCreateEvent,
                colors = ButtonDefaults.buttonColors(
                    containerColor = GlassPrimary,
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
                    text = "Create Event",
                    style = MaterialTheme.typography.labelLarge.copy(
                        fontWeight = FontWeight.SemiBold
                    )
                )
            }
        }
    }
}

@Composable
private fun formatEventDateTime(timestamp: Long): String {
    val sdf = java.text.SimpleDateFormat("MMM d • h:mm a", java.util.Locale.getDefault())
    return sdf.format(java.util.Date(timestamp))
}
