package com.littlegig.app.presentation.chat

import androidx.compose.animation.core.*
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.BasicTextField
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
import com.littlegig.app.data.model.Chat

@Composable
fun ModernChatScreen(
    navController: NavController,
    viewModel: ChatViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    var searchQuery by remember { mutableStateOf("") }
    
    LaunchedEffect(Unit) {
        viewModel.loadChats()
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
            ModernChatHeader(
                onBackClick = { navController.popBackStack() },
                onNewChatClick = { navController.navigate("new_chat") }
            )
            
            // Search Bar
            ModernChatSearchBar(
                searchQuery = searchQuery,
                onSearchQueryChange = { searchQuery = it },
                onSearchClick = { /* Perform search */ }
            )
            
            // Chat List
            if (uiState.isLoading) {
                ModernChatLoadingState()
            } else if (uiState.chats.isEmpty()) {
                ModernEmptyChatState(
                    onStartChattingClick = { navController.navigate("new_chat") }
                )
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(horizontal = 20.dp, vertical = 16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    // Online users section
                    item {
                        ModernOnlineUsersSection(
                            onlineUsers = emptyList(), // Simplified for now
                            onUserClick = { user ->
                                navController.navigate("chat_details/${user.id}")
                            }
                        )
                    }
                    
                    // Recent chats header
                    item {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 8.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "Recent Chats",
                                style = MaterialTheme.typography.titleMedium.copy(
                                    fontWeight = FontWeight.Bold
                                ),
                                color = GlassOnSurface()
                            )
                            
                            Text(
                                text = "${uiState.chats.size} conversations",
                                style = MaterialTheme.typography.bodySmall,
                                color = GlassOnSurfaceVariant()
                            )
                        }
                    }
                    
                    // Chat items
                    items(uiState.chats) { chat ->
                        ModernChatItem(
                            chat = chat,
                            onClick = {
                                navController.navigate("chat_details/${chat.id}")
                            },
                            onLongClick = { /* Show chat options */ }
                        )
                    }
                }
            }
        }
        
        // Floating New Chat Button
        FloatingActionButton(
            onClick = { navController.navigate("new_chat") },
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(20.dp),
            containerColor = GlassPrimary,
            contentColor = Color.White,
            shape = CircleShape
        ) {
            Icon(
                imageVector = Icons.Default.Edit,
                contentDescription = "New Chat",
                modifier = Modifier.size(24.dp)
            )
        }
    }
}

@Composable
private fun ModernChatHeader(
    onBackClick: () -> Unit,
    onNewChatClick: () -> Unit
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
            text = "Messages",
            style = MaterialTheme.typography.headlineLarge.copy(
                fontWeight = FontWeight.ExtraBold,
                fontSize = 28.sp
            ),
            color = GlassOnSurface()
        )
        
        GlassmorphicCard(
            onClick = onNewChatClick,
            shape = RoundedCornerShape(16.dp)
        ) {
            Icon(
                imageVector = Icons.Default.Add,
                contentDescription = "New Chat",
                tint = GlassOnSurface(),
                modifier = Modifier
                    .padding(12.dp)
                    .size(20.dp)
            )
        }
    }
}

@Composable
private fun ModernChatSearchBar(
    searchQuery: String,
    onSearchQueryChange: (String) -> Unit,
    onSearchClick: () -> Unit
) {
    GlassmorphicCard(
        shape = RoundedCornerShape(20.dp),
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Default.Search,
                contentDescription = null,
                tint = GlassOnSurfaceVariant(),
                modifier = Modifier.size(20.dp)
            )
            
            Spacer(modifier = Modifier.width(12.dp))
            
            BasicTextField(
                value = searchQuery,
                onValueChange = onSearchQueryChange,
                modifier = Modifier.weight(1f),
                decorationBox = { innerTextField ->
                    if (searchQuery.isEmpty()) {
                        Text(
                            text = "Search messages...",
                            style = MaterialTheme.typography.bodyMedium,
                            color = GlassOnSurfaceVariant().copy(alpha = 0.6f)
                        )
                    }
                    innerTextField()
                },
                textStyle = MaterialTheme.typography.bodyMedium.copy(
                    color = GlassOnSurface()
                )
            )
            
            if (searchQuery.isNotEmpty()) {
                GlassmorphicCard(
                    onClick = { onSearchQueryChange("") },
                    shape = CircleShape
                ) {
                    Icon(
                        imageVector = Icons.Default.Clear,
                        contentDescription = "Clear",
                        tint = GlassOnSurfaceVariant(),
                        modifier = Modifier
                            .padding(4.dp)
                            .size(16.dp)
                    )
                }
            }
        }
    }
}

@Composable
private fun ModernOnlineUsersSection(
    onlineUsers: List<com.littlegig.app.data.model.User>,
    onUserClick: (com.littlegig.app.data.model.User) -> Unit
) {
    if (onlineUsers.isNotEmpty()) {
        Column {
            Text(
                text = "Active Now",
                style = MaterialTheme.typography.titleMedium.copy(
                    fontWeight = FontWeight.Bold
                ),
                color = GlassOnSurface(),
                modifier = Modifier.padding(bottom = 12.dp)
            )
            
            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                items(onlineUsers.take(10)) { user ->
                    ModernOnlineUserItem(
                        user = user,
                        onClick = { onUserClick(user) }
                    )
                }
            }
            
            Spacer(modifier = Modifier.height(20.dp))
        }
    }
}

@Composable
private fun ModernOnlineUserItem(
    user: com.littlegig.app.data.model.User,
    onClick: () -> Unit
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        GlassmorphicCard(
            onClick = onClick,
            shape = CircleShape
        ) {
            Box {
                if (user.profileImageUrl.isNotEmpty()) {
                    AsyncImage(
                        model = user.profileImageUrl,
                        contentDescription = null,
                        modifier = Modifier
                            .size(56.dp)
                            .clip(CircleShape),
                        contentScale = ContentScale.Crop
                    )
                } else {
                    Box(
                        modifier = Modifier
                            .size(56.dp)
                            .background(GlassPrimary.copy(alpha = 0.2f), CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = user.displayName.firstOrNull()?.uppercase() ?: "U",
                            style = MaterialTheme.typography.titleMedium.copy(
                                fontWeight = FontWeight.Bold
                            ),
                            color = GlassPrimary
                        )
                    }
                }
                
                // Online indicator
                Box(
                    modifier = Modifier
                        .size(16.dp)
                        .background(Color.Green, CircleShape)
                        .border(2.dp, Color.White, CircleShape)
                        .align(Alignment.BottomEnd)
                )
            }
        }
        
        Spacer(modifier = Modifier.height(8.dp))
        
        Text(
            text = user.displayName.take(8),
            style = MaterialTheme.typography.bodySmall,
            color = GlassOnSurfaceVariant(),
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
    }
}

@Composable
private fun ModernChatItem(
    chat: Chat,
    onClick: () -> Unit,
    onLongClick: () -> Unit
) {
    GlassmorphicCard(
        onClick = onClick,
        shape = RoundedCornerShape(20.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Avatar
            Box {
                if (chat.imageUrl.isNotEmpty()) {
                    AsyncImage(
                        model = chat.imageUrl,
                        contentDescription = null,
                        modifier = Modifier
                            .size(56.dp)
                            .clip(CircleShape),
                        contentScale = ContentScale.Crop
                    )
                } else {
                    Box(
                        modifier = Modifier
                            .size(56.dp)
                            .background(GlassPrimary.copy(alpha = 0.2f), CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = (chat.name?.firstOrNull() ?: "C").uppercase(),
                            style = MaterialTheme.typography.titleMedium.copy(
                                fontWeight = FontWeight.Bold
                            ),
                            color = GlassPrimary
                        )
                    }
                }
                
                // Online status (simplified)
                Box(
                    modifier = Modifier
                        .size(16.dp)
                        .background(Color.Green, CircleShape)
                        .border(2.dp, Color.White, CircleShape)
                        .align(Alignment.BottomEnd)
                )
            }
            
            Spacer(modifier = Modifier.width(16.dp))
            
            // Chat info
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.Top
                ) {
                    Text(
                        text = chat.name ?: "Chat",
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = if (chat.unreadCount > 0) FontWeight.Bold else FontWeight.SemiBold
                        ),
                        color = GlassOnSurface(),
                        modifier = Modifier.weight(1f)
                    )
                    
                    Text(
                        text = formatChatTime(chat.lastMessageTime),
                        style = MaterialTheme.typography.bodySmall,
                        color = if (chat.unreadCount > 0) GlassPrimary else GlassOnSurfaceVariant()
                    )
                }
                
                Spacer(modifier = Modifier.height(4.dp))
                
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = chat.lastMessage ?: "No messages yet",
                        style = MaterialTheme.typography.bodyMedium,
                        color = if (chat.unreadCount > 0) GlassOnSurface() else GlassOnSurfaceVariant(),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier.weight(1f)
                    )
                    
                    if (chat.unreadCount > 0) {
                        Spacer(modifier = Modifier.width(8.dp))
                        Box(
                            modifier = Modifier
                                .background(GlassPrimary, CircleShape)
                                .padding(horizontal = 8.dp, vertical = 4.dp)
                        ) {
                            Text(
                                text = if (chat.unreadCount > 99) "99+" else chat.unreadCount.toString(),
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
private fun ModernChatLoadingState() {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(20.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        items(6) {
            ModernChatItemSkeleton()
        }
    }
}

@Composable
private fun ModernChatItemSkeleton() {
    GlassmorphicCard(
        shape = RoundedCornerShape(20.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Avatar skeleton
            Box(
                modifier = Modifier
                    .size(56.dp)
                    .background(
                        GlassOnSurfaceVariant().copy(alpha = 0.3f),
                        CircleShape
                    )
            )
            
            Spacer(modifier = Modifier.width(16.dp))
            
            Column(
                modifier = Modifier.weight(1f)
            ) {
                // Name skeleton
                Box(
                    modifier = Modifier
                        .fillMaxWidth(0.4f)
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
private fun ModernEmptyChatState(
    onStartChattingClick: () -> Unit
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
                imageVector = Icons.Default.Chat,
                contentDescription = null,
                tint = GlassPrimary,
                modifier = Modifier
                    .padding(32.dp)
                    .size(64.dp)
            )
        }
        
        Spacer(modifier = Modifier.height(24.dp))
        
        Text(
            text = "No Messages Yet",
            style = MaterialTheme.typography.headlineMedium.copy(
                fontWeight = FontWeight.Bold
            ),
            color = GlassOnSurface()
        )
        
        Spacer(modifier = Modifier.height(12.dp))
        
        Text(
            text = "Start a conversation with fellow event-goers\nand make new connections!",
            style = MaterialTheme.typography.bodyLarge,
            color = GlassOnSurfaceVariant(),
            textAlign = androidx.compose.ui.text.style.TextAlign.Center
        )
        
        Spacer(modifier = Modifier.height(32.dp))
        
        NeumorphicButton(
            onClick = onStartChattingClick,
            colors = ButtonDefaults.buttonColors(
                containerColor = GlassPrimary,
                contentColor = Color.White
            ),
            shape = RoundedCornerShape(20.dp),
            contentPadding = PaddingValues(horizontal = 32.dp, vertical = 16.dp)
        ) {
            Icon(
                imageVector = Icons.Default.Chat,
                contentDescription = null,
                modifier = Modifier.size(20.dp)
            )
            Spacer(modifier = Modifier.width(12.dp))
            Text(
                text = "Start Chatting",
                style = MaterialTheme.typography.titleMedium.copy(
                    fontWeight = FontWeight.Bold
                )
            )
        }
    }
}

@Composable
private fun formatChatTime(timestamp: Long): String {
    val now = System.currentTimeMillis()
    val diff = now - timestamp
    
    return when {
        diff < 60_000 -> "now"
        diff < 3600_000 -> "${diff / 60_000}m"
        diff < 86400_000 -> "${diff / 3600_000}h"
        else -> {
            val sdf = java.text.SimpleDateFormat("MMM d", java.util.Locale.getDefault())
            sdf.format(java.util.Date(timestamp))
        }
    }
}
