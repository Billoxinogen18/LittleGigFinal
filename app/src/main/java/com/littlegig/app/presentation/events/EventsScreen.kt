package com.littlegig.app.presentation.events

import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.pager.VerticalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.ExperimentalFoundationApi
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.littlegig.app.data.model.Event
import com.littlegig.app.presentation.components.*
import com.littlegig.app.presentation.theme.*
import com.littlegig.app.presentation.events.NeumorphicCategoryChip
import androidx.compose.ui.unit.dp
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.foundation.clickable
import com.littlegig.app.presentation.components.HapticButton
import com.littlegig.app.presentation.components.LoadingPulseAnimation
import com.littlegig.app.presentation.components.ShimmerLoadingCard
import com.littlegig.app.presentation.components.FloatingActionButton
import com.littlegig.app.data.model.ContentCategory
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.platform.LocalContext
import android.content.Intent
import androidx.compose.runtime.rememberCoroutineScope
import kotlinx.coroutines.launch
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState

@Composable
fun EventsScreen(
    navController: NavController,
    viewModel: EventsViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val searchQuery by viewModel.searchQuery.collectAsState()
    val isDark = isSystemInDarkTheme()
    
    // SwipeRefresh state  
    val swipeRefreshState = rememberSwipeRefreshState(isRefreshing = uiState.isLoading)
    
    LaunchedEffect(Unit) {
        viewModel.loadEvents()
    }
    
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colors = if (isDark) listOf(
                        Color(0xFF0B0E1A),
                        Color(0xFF141B2E)
                    ) else listOf(
                        Color(0xFFF8FAFF),
                        Color.White
                    )
                )
            )
    ) {
        val snackbarHostState = remember { SnackbarHostState() }
        LaunchedEffect(uiState.snackbarMessage) {
            uiState.snackbarMessage?.let {
                snackbarHostState.showSnackbar(it)
                viewModel.clearSnackbar()
            }
        }
        SnackbarHost(hostState = snackbarHostState)
        SwipeRefresh(state = swipeRefreshState, onRefresh = { viewModel.loadEvents() }) {
            Column(
                modifier = Modifier.fillMaxSize()
            ) {
            // Header with search and account - Beautiful neumorphic design
            AdvancedGlassmorphicCard(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                Column(
                    modifier = Modifier.padding(20.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Discover amazing events",
                            style = MaterialTheme.typography.titleMedium.copy(
                                fontWeight = FontWeight.SemiBold
                            ),
                            color = if (isDark) Color.White else Color.Black
                        )
                        
                        HapticButton(
                            onClick = {
                                navController.navigate("account")
                            }
                        ) {
                            Icon(
                                imageVector = Icons.Default.AccountCircle,
                                contentDescription = "Account",
                                tint = if (isDark) Color.White else Color.Black,
                                modifier = Modifier.size(28.dp)
                            )
                        }
                    }
                    Spacer(modifier = Modifier.height(16.dp))
                    OutlinedTextField(
                        value = searchQuery,
                        onValueChange = { viewModel.updateSearchQuery(it) },
                        placeholder = { Text("Search events...") },
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(12.dp)),
                        leadingIcon = {
                            Icon(
                                imageVector = Icons.Default.Search,
                                contentDescription = null,
                                tint = if (isDark) Color.White.copy(alpha = 0.7f) else Color.Black.copy(alpha = 0.7f),
                                modifier = Modifier.size(20.dp)
                            )
                        },
                        shape = RoundedCornerShape(12.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = LittleGigPrimary,
                            unfocusedBorderColor = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.3f),
                            focusedLabelColor = LittleGigPrimary,
                            unfocusedLabelColor = if (isDark) Color.White.copy(alpha = 0.7f) else Color.Black.copy(alpha = 0.7f),
                            focusedContainerColor = if (isDark) Color.White.copy(alpha = 0.05f) else Color.Black.copy(alpha = 0.02f),
                            unfocusedContainerColor = if (isDark) Color.White.copy(alpha = 0.03f) else Color.Black.copy(alpha = 0.01f)
                        )
                    )
                }
            }
            
            // Category filters - Beautiful neumorphic design
            AdvancedGlassmorphicCard(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
            ) {
                Column(
                    modifier = Modifier.padding(16.dp)
                ) {
                    Text(
                        text = "Categories",
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.SemiBold
                        ),
                        color = if (isDark) Color.White else Color.Black
                    )
                    
                    Spacer(modifier = Modifier.height(12.dp))
                    
                    LazyRow(
                        horizontalArrangement = Arrangement.spacedBy(12.dp),
                        contentPadding = PaddingValues(horizontal = 4.dp)
                    ) {
                        items(ContentCategory.values()) { category ->
                            NeumorphicCategoryChip(
                                text = category.name.lowercase().replaceFirstChar { it.uppercase() },
                                selected = uiState.selectedCategory == category,
                                onClick = { viewModel.selectCategory(category) }
                            )
                        }
                    }
                }
            }
            
            Spacer(modifier = Modifier.height(16.dp))
            
            // Events content
            if (uiState.isLoading) {
                // Loading state with shimmer effects
                LazyColumn(
                    modifier = Modifier.fillMaxWidth(),
                    contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    items(6) {
                        ShimmerLoadingCard()
                    }
                }
            } else if (uiState.events.isEmpty()) {
                // Empty state with enhanced animations
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.padding(32.dp)
                    ) {
                        LoadingPulseAnimation {
                            Box(
                                modifier = Modifier
                                    .size(120.dp)
                                    .drawBehind {
                                        drawCircle(
                                            brush = Brush.radialGradient(
                                                colors = listOf(
                                                    LittleGigPrimary.copy(alpha = 0.3f),
                                                    LittleGigPrimary.copy(alpha = 0.1f),
                                                    Color.Transparent
                                                )
                                            ),
                                            radius = size.minDimension / 2
                                        )
                                    }
                                    .clip(CircleShape)
                                    .background(
                                        Brush.radialGradient(
                                            colors = listOf(
                                                LittleGigPrimary.copy(alpha = 0.2f),
                                                Color.Transparent
                                            )
                                        )
                                    ),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Event,
                                    contentDescription = null,
                                    tint = LittleGigPrimary,
                                    modifier = Modifier.size(48.dp)
                                )
                            }
                        }
                        
                        Spacer(modifier = Modifier.height(24.dp))
                        
                        Text(
                            text = "No events found",
                            style = MaterialTheme.typography.headlineSmall,
                            color = if (isDark) Color.White else Color.Black,
                            textAlign = TextAlign.Center
                        )
                        
                        Spacer(modifier = Modifier.height(8.dp))
                        
                        Text(
                            text = "Be the first to create an amazing event!",
                            style = MaterialTheme.typography.bodyMedium,
                            color = if (isDark) Color.White.copy(alpha = 0.7f) else Color.Black.copy(alpha = 0.7f),
                            textAlign = TextAlign.Center
                        )
                        
                        Spacer(modifier = Modifier.height(32.dp))
                        
                        HapticButton(
                            onClick = {
                                // Navigate to upload tab
                                navController.navigate("upload")
                            }
                        ) {
                            AdvancedNeumorphicCard(
                                modifier = Modifier.padding(8.dp)
                            ) {
                                Row(
                                    modifier = Modifier.padding(16.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Add,
                                        contentDescription = null,
                                        tint = LittleGigPrimary,
                                        modifier = Modifier.size(24.dp)
                                    )
                                    Spacer(modifier = Modifier.width(12.dp))
                                    Text(
                                        text = "Create Event",
                                        style = MaterialTheme.typography.titleMedium,
                                        color = LittleGigPrimary
                                    )
                                }
                            }
                        }
                    }
                }
            } else {
                // Full-screen snapping feed (TikTok-style)
                @OptIn(ExperimentalFoundationApi::class)
                val context = LocalContext.current
                val shareScope = rememberCoroutineScope()
                val pagerState = rememberPagerState(pageCount = { uiState.events.size })
                LaunchedEffect(pagerState.currentPage, uiState.events.size) {
                    if (uiState.events.isNotEmpty() && pagerState.currentPage >= uiState.events.size - 3) {
                        viewModel.loadMoreEvents()
                    }
                }
                VerticalPager(
                    state = pagerState,
                    modifier = Modifier.fillMaxSize()
                ) { page ->
                    val event = uiState.events[page]
                    Box(Modifier.fillMaxSize()) {
                        TikTokStyleEventCard(
                            event = event,
                            onEventClick = { navController.navigate("event_details/${event.id}") },
                            onLikeClick = { viewModel.toggleEventLike(event.id) },
                            onRateClick = { /* no-op */ },
                            onAttendeesClick = { /* no-op */ },
                            onShareClick = {
                                shareScope.launch {
                                    val link = viewModel.createEventShareLink(event)
                                    link?.let {
                                        val sendIntent = android.content.Intent(android.content.Intent.ACTION_SEND).apply {
                                            type = "text/plain"
                                            putExtra(android.content.Intent.EXTRA_TEXT, it)
                                        }
                                        val shareIntent = android.content.Intent.createChooser(sendIntent, "Share Event")
                                        context.startActivity(shareIntent)
                                    }
                                }
                            }
                        )
                    }
                }
            }
        }
        }
        
        // Floating action button for quick upload, ensure above bottom nav and content
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            contentAlignment = Alignment.BottomEnd
        ) {
            FloatingActionButton(
                onClick = {
                    // Navigate to upload tab
                    navController.navigate("upload")
                },
                // place above raised bottom nav bar height (88dp) plus increased gap
                modifier = Modifier.padding(bottom = 140.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "Create Event",
                    tint = Color.White,
                    modifier = Modifier.size(24.dp)
                )
            }
        }
        
        // Error state
        if (uiState.error != null) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Black.copy(alpha = 0.5f)),
                contentAlignment = Alignment.Center
            ) {
                AdvancedNeumorphicCard(
                    modifier = Modifier.padding(32.dp)
                ) {
                    Column(
                        modifier = Modifier.padding(24.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Icon(
                            imageVector = Icons.Default.Error,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.error,
                            modifier = Modifier.size(48.dp)
                        )
                        
                        Spacer(modifier = Modifier.height(16.dp))
                        
                        Text(
                            text = "Oops!",
                            style = MaterialTheme.typography.headlineSmall,
                            color = MaterialTheme.colorScheme.error
                        )
                        
                        Spacer(modifier = Modifier.height(8.dp))
                        
                        Text(
                            text = uiState.error ?: "Something went wrong",
                            style = MaterialTheme.typography.bodyMedium,
                            textAlign = TextAlign.Center
                        )
                        
                        Spacer(modifier = Modifier.height(24.dp))
                        
                        HapticButton(
                            onClick = {
                                viewModel.clearError()
                                viewModel.loadEvents()
                            }
                        ) {
                            AdvancedNeumorphicCard(
                                modifier = Modifier.padding(8.dp)
                            ) {
                                Text(
                                    text = "Try Again",
                                    modifier = Modifier.padding(16.dp),
                                    style = MaterialTheme.typography.titleMedium,
                                    color = LittleGigPrimary
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
fun TikTokStyleEventCard(
    event: Event,
    onEventClick: () -> Unit,
    onLikeClick: () -> Unit,
    onRateClick: (Float) -> Unit,
    onAttendeesClick: () -> Unit,
    onShareClick: () -> Unit
) {
    var isLiked by remember { mutableStateOf(false) }
    var currentRating by remember { mutableStateOf(0f) }
    
    // Full screen event card
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(600.dp)
            .clickable { onEventClick() }
    ) {
        // Event image background
        AsyncImage(
            model = event.imageUrls.firstOrNull() ?: "https://via.placeholder.com/400x600",
            contentDescription = event.title,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )
        
        // Gradient overlay for text readability
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
        
        // Event content
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(20.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            // Top section - Event info
            Column {
                Text(
                    text = event.title,
                    style = MaterialTheme.typography.headlineMedium.copy(
                        fontWeight = FontWeight.Bold
                    ),
                    color = Color.White
                )
                
                Spacer(modifier = Modifier.height(8.dp))
                
                Text(
                    text = event.description,
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color.White.copy(alpha = 0.9f),
                    maxLines = 3
                )
            }
            
            // Bottom section - Action buttons
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Bottom
            ) {
                // Left side - Event actions
                Column(
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    // Like button
                    IconButton(
                        onClick = {
                            isLiked = !isLiked
                            onLikeClick()
                        }
                    ) {
                        Icon(
                            imageVector = if (isLiked) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                            contentDescription = "Like",
                            tint = if (isLiked) Color.Red else Color.White,
                            modifier = Modifier.size(32.dp)
                        )
                    }
                    
                    // Rating stars
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        repeat(5) { index ->
                            IconButton(
                                onClick = {
                                    currentRating = (index + 1).toFloat()
                                    onRateClick(currentRating)
                                }
                            ) {
                                Icon(
                                    imageVector = if (index < currentRating) Icons.Default.Star else Icons.Default.StarBorder,
                                    contentDescription = "Rate ${index + 1}",
                                    tint = if (index < currentRating) Color.Yellow else Color.White,
                                    modifier = Modifier.size(24.dp)
                                )
                            }
                        }
                    }
                    
                    // Attendees button
                    IconButton(
                        onClick = onAttendeesClick
                    ) {
                        Icon(
                            imageVector = Icons.Default.People,
                            contentDescription = "Attendees",
                            tint = Color.White,
                            modifier = Modifier.size(32.dp)
                        )
                    }
                    
                    // Share button
                    IconButton(
                        onClick = onShareClick
                    ) {
                        Icon(
                            imageVector = Icons.Default.Share,
                            contentDescription = "Share",
                            tint = Color.White,
                            modifier = Modifier.size(32.dp)
                        )
                    }
                }
                
                // Right side - Event stats
                Column(
                    horizontalAlignment = Alignment.End
                ) {
                    Text(
                        text = "$${event.price}",
                        style = MaterialTheme.typography.headlineSmall.copy(
                            fontWeight = FontWeight.Bold
                        ),
                        color = Color.White
                    )
                    
                    Spacer(modifier = Modifier.height(8.dp))
                    
                    Text(
                        text = "${event.capacity} spots",
                        style = MaterialTheme.typography.bodySmall,
                        color = Color.White.copy(alpha = 0.8f)
                    )
                }
            }
        }
    }
}