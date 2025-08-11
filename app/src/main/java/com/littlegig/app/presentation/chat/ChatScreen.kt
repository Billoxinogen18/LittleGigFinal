package com.littlegig.app.presentation.chat

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.littlegig.app.presentation.components.*
import com.littlegig.app.presentation.theme.*
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChatScreen(
    navController: NavController,
    viewModel: ChatViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState(initial = ChatUiState())
    val chats by viewModel.chats.collectAsState()
    val searchResults by viewModel.searchResults.collectAsState()
    val allUsers by viewModel.allUsers.collectAsState()
    val isDark = isSystemInDarkTheme()

    var showSearch by remember { mutableStateOf(false) }
    var searchQuery by remember { mutableStateOf("") }

    // Proactively load initial users so they are ready for search.
    LaunchedEffect(Unit) {
        if (allUsers.isEmpty()) {
            viewModel.loadAllUsers()
        }
    }
    
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(
                    colors = if (isDark) {
                        listOf(
                            DarkBackground,
                            DarkBackground.copy(alpha = 0.95f)
                        )
                    } else {
                        listOf(
                            LightBackground,
                            LightBackground.copy(alpha = 0.95f)
                        )
                    }
                )
            )
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            // Liquid Glass Header
            LiquidGlassChatHeader(
                title = "Chat",
                subtitle = "${chats.size} conversations",
                onBackClick = { /* Navigation handled by parent */ },
                onMoreClick = { /* Show options */ },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Search Toggle
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                LiquidGlassChip(
                    text = "Conversations",
                    onClick = { showSearch = false },
                    isSelected = !showSearch,
                    icon = Icons.Default.Chat,
                    modifier = Modifier.weight(1f)
                )
                
                Spacer(modifier = Modifier.width(12.dp))
                
                LiquidGlassChip(
                    text = "Find People",
                    onClick = { showSearch = true },
                    isSelected = showSearch,
                    icon = Icons.Default.PersonSearch,
                    modifier = Modifier.weight(1f)
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Main Content
            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
            ) {
                if (showSearch) {
                    // People discovery with glassmorphic design
                    if (allUsers.isEmpty() && searchResults.isEmpty()) {
                        LiquidGlassEmptyState(
                            icon = Icons.Default.PersonSearch,
                            title = "Find People",
                            message = "Search for users to start conversations",
                            primaryActionLabel = "Discover People",
                            onPrimaryAction = { navController.navigate("people_discovery") },
                            modifier = Modifier.fillMaxSize()
                        )
                    } else {
                        LazyColumn(
                            verticalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            if (searchQuery.isBlank()) {
                                item {
                                    Text(
                                        text = "All Users",
                                        style = MaterialTheme.typography.titleMedium.copy(
                                            fontWeight = FontWeight.SemiBold
                                        ),
                                        color = if (isDark) Color.White else Color.Black,
                                        modifier = Modifier.padding(bottom = 8.dp)
                                    )
                                }
                                
                                items(allUsers) { user ->
                                    LiquidGlassUserCard(
                                        user = user,
                                        onClick = {
                                            viewModel.createChatWithUser(user.id)
                                            navController.navigate("chat_details/${user.id}")
                                        }
                                    )
                                }
                            } else {
                                item {
                                    Text(
                                        text = "Search Results",
                                        style = MaterialTheme.typography.titleMedium.copy(
                                            fontWeight = FontWeight.SemiBold
                                        ),
                                        color = if (isDark) Color.White else Color.Black,
                                        modifier = Modifier.padding(bottom = 8.dp)
                                    )
                                }
                                
                                items(searchResults) { user ->
                                    LiquidGlassUserCard(
                                        user = user,
                                        onClick = {
                                            viewModel.createChatWithUser(user.id)
                                            navController.navigate("chat_details/${user.id}")
                                        }
                                    )
                                }
                            }
                        }
                    }
                } else {
                    // Chat conversations with glassmorphic design
                    if (chats.isEmpty()) {
                        LiquidGlassEmptyState(
                            icon = Icons.Default.Chat,
                            title = "No Conversations",
                            message = "Start chatting with people to see your conversations here",
                            primaryActionLabel = "Find People",
                            onPrimaryAction = { showSearch = true },
                            modifier = Modifier.fillMaxSize()
                        )
                    } else {
                        LazyColumn(
                            verticalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            items(chats) { chat ->
                                LiquidGlassChatCard(
                                    chat = chat,
                                    onClick = {
                                        navController.navigate("chat_details/${chat.id}")
                                    }
                                )
                            }
                        }
                    }
                }
            }
        }
        
        // Search input overlay
        if (showSearch) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp)
            ) {
                LiquidGlassInputField(
                    value = searchQuery,
                    onValueChange = {
                        searchQuery = it
                        viewModel.searchUsers(it)
                    },
                    placeholder = "Search users...",
                    leadingIcon = Icons.Default.Search,
                    modifier = Modifier
                        .align(Alignment.TopCenter)
                        .fillMaxWidth()
                )
            }
        }
    }
}

@Composable
fun GlassmorphicUserCard(
    user: com.littlegig.app.data.model.User,
    onClick: () -> Unit
) {
    val isDark = isSystemInDarkTheme()
    
    GlassmorphicCard(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
        cornerRadius = 16.dp,
        alpha = if (isDark) 0.6f else 0.7f,
        borderColor = if (isDark) Color.White.copy(alpha = 0.1f) else Color.Black.copy(alpha = 0.05f)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Avatar
            GlassmorphicAvatar(
                imageUrl = user.profileImageUrl,
                size = 48.dp
            )
            
            Spacer(modifier = Modifier.width(16.dp))
            
            // User info
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = user.displayName.ifEmpty { user.name }.ifEmpty { user.username }.ifEmpty { "Unknown User" },
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.SemiBold
                    ),
                    color = if (isDark) Color.White else Color.Black
                )
                
                if (!user.bio.isNullOrBlank()) {
                    Text(
                        text = user.bio,
                        style = MaterialTheme.typography.bodySmall,
                        color = if (isDark) Color.White.copy(alpha = 0.7f) else Color.Black.copy(alpha = 0.6f),
                        maxLines = 2,
                        overflow = androidx.compose.ui.text.style.TextOverflow.Ellipsis
                    )
                }
            }
            
            // Start chat button
            GlassmorphicButton(
                onClick = onClick,
                cornerRadius = 20.dp
            ) {
                Icon(
                    imageVector = Icons.Default.Chat,
                    contentDescription = "Start chat",
                    tint = LittleGigPrimary,
                    modifier = Modifier.size(20.dp)
                )
            }
        }
    }
}

@Composable
fun GlassmorphicChatCard(
    chat: com.littlegig.app.data.model.Chat,
    onClick: () -> Unit
) {
    val isDark = isSystemInDarkTheme()
    
    GlassmorphicCard(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
        cornerRadius = 16.dp,
        alpha = if (isDark) 0.6f else 0.7f,
        borderColor = if (isDark) Color.White.copy(alpha = 0.1f) else Color.Black.copy(alpha = 0.05f)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Chat avatar (group or individual)
            Box {
                GlassmorphicAvatar(
                    imageUrl = null, // Chat doesn't have direct avatar, would need to get from participants
                    size = 48.dp
                )
                
                if (chat.unreadCount > 0) {
                    Box(
                        modifier = Modifier
                            .align(Alignment.TopEnd)
                            .size(20.dp)
                            .clip(CircleShape)
                            .background(LittleGigPrimary)
                            .border(
                                width = 2.dp,
                                color = if (isDark) DarkBackground else LightBackground,
                                shape = CircleShape
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = chat.unreadCount.coerceAtMost(99).toString(),
                            style = MaterialTheme.typography.labelSmall,
                            color = Color.White
                        )
                    }
                }
            }
            
            Spacer(modifier = Modifier.width(16.dp))
            
            // Chat info
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = chat.name ?: "Chat",
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.SemiBold
                    ),
                    color = if (isDark) Color.White else Color.Black
                )
                
                if (chat.lastMessage != null) {
                    Text(
                        text = chat.lastMessage,
                        style = MaterialTheme.typography.bodySmall,
                        color = if (isDark) Color.White.copy(alpha = 0.7f) else Color.Black.copy(alpha = 0.6f),
                        maxLines = 1,
                        overflow = androidx.compose.ui.text.style.TextOverflow.Ellipsis
                    )
                }
                
                if (chat.lastMessageTime > 0) {
                    val formattedTime = SimpleDateFormat("HH:mm", Locale.getDefault()).format(Date(chat.lastMessageTime))
                    Text(
                        text = formattedTime,
                        style = MaterialTheme.typography.labelSmall,
                        color = if (isDark) Color.White.copy(alpha = 0.5f) else Color.Black.copy(alpha = 0.4f)
                    )
                }
            }
            
            // Chat status indicator - removed isPinned since it's not in the model
        }
    }
}


fun fetchPhoneNumbersFromContacts(context: Context): List<String> {
    val phones = mutableSetOf<String>()
    try {
    val resolver = context.contentResolver
    val cursor = resolver.query(
        android.provider.ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
            arrayOf(android.provider.ContactsContract.CommonDataKinds.Phone.NUMBER),
            null, null, null
        )
        cursor?.use {
            while (it.moveToNext()) {
                val phone = it.getString(0)
            if (!phone.isNullOrBlank()) phones.add(phone)
            }
        }
    } catch (e: Exception) {
        // Handle potential SecurityException if permissions are revoked
    }
    return phones.toList()
}