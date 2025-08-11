package com.littlegig.app.presentation.chat

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.littlegig.app.data.model.User
import com.littlegig.app.presentation.components.*
import com.littlegig.app.presentation.theme.*

@Composable
fun NeumorphicChip(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) {
    LiquidGlassCard(
        modifier = modifier
    ) {
        Surface(
            onClick = onClick,
            modifier = Modifier
                .fillMaxWidth()
                .padding(4.dp),
            shape = RoundedCornerShape(12.dp),
            color = Color.Transparent
        ) {
            Box(
                modifier = Modifier.padding(12.dp),
                contentAlignment = Alignment.Center
            ) {
                content()
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChatSearchScreen(
    navController: NavController,
    viewModel: ChatSearchViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val searchQuery by viewModel.searchQuery.collectAsState()
    val searchResults by viewModel.searchResults.collectAsState()
    val currentUser by viewModel.currentUser.collectAsState()
    
    var selectedTab by remember { mutableStateOf(0) }
    val tabs = listOf("All Users", "Followers", "Following")
    
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colors = listOf(
                        Color(0xFF0B0E1A),
                        Color(0xFF141B2E)
                    )
                )
            )
    ) {
        // Header
        LiquidGlassCard(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Column(
                modifier = Modifier.padding(16.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(
                        onClick = { navController.popBackStack() }
                    ) {
                        Icon(
                            Icons.Default.ArrowBack,
                            contentDescription = "Back",
                            tint = Color.White
                        )
                    }
                    
                    Spacer(modifier = Modifier.width(8.dp))
                    
                    Text(
                        text = "Start Chat",
                        style = MaterialTheme.typography.headlineSmall.copy(
                            fontWeight = FontWeight.Bold
                        ),
                        color = Color.White
                    )
                }
                
                Spacer(modifier = Modifier.height(16.dp))
                
                // Search input with autocomplete
                NeumorphicTextField(
                    value = searchQuery,
                    onValueChange = { viewModel.updateSearchQuery(it) },
                    label = "Search by username, email, name, or phone",
                    leadingIcon = Icons.Default.Search,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }
        
        // Tab selector
        LiquidGlassCard(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
        ) {
            Row(
                modifier = Modifier.padding(8.dp)
            ) {
                tabs.forEachIndexed { index, tab ->
                    NeumorphicChip(
                        onClick = { selectedTab = index },
                        modifier = Modifier.weight(1f)
                    ) {
                        Text(
                            text = tab,
                            color = if (selectedTab == index) Color.White else Color.White.copy(alpha = 0.7f)
                        )
                    }
                    if (index < tabs.size - 1) {
                        Spacer(modifier = Modifier.width(8.dp))
                    }
                }
            }
        }
        
        Spacer(modifier = Modifier.height(16.dp))
        
        // Search results
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(searchResults) { user ->
                UserSearchResultCard(
                    user = user,
                    currentUser = currentUser,
                    onUserClick = { selectedUser ->
                        viewModel.startChat(selectedUser)
                        navController.popBackStack()
                    },
                    onFollowClick = { userToFollow ->
                        viewModel.toggleFollow(userToFollow)
                    }
                )
            }
        }
    }
}

@Composable
fun UserSearchResultCard(
    user: User,
    currentUser: User?,
    onUserClick: (User) -> Unit,
    onFollowClick: (User) -> Unit
) {
    val isFollowing = currentUser?.following?.contains(user.id) == true
    val isFollower = currentUser?.followers?.contains(user.id) == true
    
            LiquidGlassCard(
            modifier = Modifier.fillMaxWidth()
        ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clickable { onUserClick(user) }
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Profile picture
            Box(
                modifier = Modifier
                    .size(50.dp)
                    .clip(RoundedCornerShape(25.dp))
                    .background(
                        Brush.radialGradient(
                            colors = listOf(
                                LittleGigPrimary,
                                LittleGigSecondary
                            )
                        )
                    ),
                contentAlignment = Alignment.Center
            ) {
                if (user.profilePictureUrl.isNotEmpty()) {
                    // Load profile image here
                    Icon(
                        Icons.Default.Person,
                        contentDescription = null,
                        tint = Color.White,
                        modifier = Modifier.size(30.dp)
                    )
                } else {
                    Text(
                        text = user.displayName.take(1).uppercase(),
                        style = MaterialTheme.typography.titleLarge.copy(
                            fontWeight = FontWeight.Bold
                        ),
                        color = Color.White
                    )
                }
            }
            
            Spacer(modifier = Modifier.width(12.dp))
            
            // User info
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = user.displayName,
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.Bold
                    ),
                    color = Color.White
                )
                
                Text(
                    text = "@${user.username}",
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color.White.copy(alpha = 0.7f)
                )
                
                if (user.bio?.isNotEmpty() == true) {
                    Text(
                        text = user.bio,
                        style = MaterialTheme.typography.bodySmall,
                        color = Color.White.copy(alpha = 0.6f),
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis
                    )
                }
                
                // Status indicators
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    if (isFollowing) {
                        Icon(
                            Icons.Default.Check,
                            contentDescription = "Following",
                            tint = LittleGigPrimary,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = "Following",
                            style = MaterialTheme.typography.bodySmall,
                            color = LittleGigPrimary
                        )
                    }
                    
                    if (isFollower) {
                        if (isFollowing) {
                            Spacer(modifier = Modifier.width(8.dp))
                        }
                        Icon(
                            Icons.Default.Person,
                            contentDescription = "Follower",
                            tint = LittleGigSecondary,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = "Follower",
                            style = MaterialTheme.typography.bodySmall,
                            color = LittleGigSecondary
                        )
                    }
                }
            }
            
            // Action buttons
            Column(
                horizontalAlignment = Alignment.End
            ) {
                // Chat button
                NeumorphicButton(
                    onClick = { onUserClick(user) },
                    modifier = Modifier.size(40.dp)
                ) {
                    Icon(
                        Icons.Default.Chat,
                        contentDescription = "Start Chat",
                        modifier = Modifier.size(20.dp)
                    )
                }
                
                Spacer(modifier = Modifier.height(8.dp))
                
                // Follow/Unfollow button
                if (currentUser?.id != user.id) {
                    NeumorphicButton(
                        onClick = { onFollowClick(user) },
                        modifier = Modifier.size(40.dp)
                    ) {
                        Icon(
                            if (isFollowing) Icons.Default.PersonRemove else Icons.Default.PersonAdd,
                            contentDescription = if (isFollowing) "Unfollow" else "Follow",
                            modifier = Modifier.size(20.dp)
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun NeumorphicTextField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    leadingIcon: androidx.compose.ui.graphics.vector.ImageVector,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    modifier: Modifier = Modifier
) {
    var isFocused by remember { mutableStateOf(false) }
    
    val animatedBorderColor by animateColorAsState(
        targetValue = if (isFocused) LittleGigPrimary else Color.Transparent,
        animationSpec = tween(300),
        label = "border_color"
    )
    
    LiquidGlassCard(
        modifier = modifier
    ) {
        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            label = { 
                Text(
                    label,
                    color = Color.White.copy(alpha = 0.7f)
                )
            },
            leadingIcon = { 
                Icon(
                    leadingIcon, 
                    contentDescription = null,
                    tint = if (isFocused) LittleGigPrimary else Color.White.copy(alpha = 0.7f)
                ) 
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(4.dp),
            singleLine = true,
            keyboardOptions = keyboardOptions,
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = animatedBorderColor,
                unfocusedBorderColor = Color.Transparent,
                focusedContainerColor = Color.Transparent,
                unfocusedContainerColor = Color.Transparent,
                focusedTextColor = Color.White,
                unfocusedTextColor = Color.White,
                cursorColor = LittleGigPrimary
            ),
            shape = RoundedCornerShape(16.dp)
        )
    }
} 