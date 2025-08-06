package com.littlegig.app.presentation.chat

import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
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
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.graphics.Color
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.littlegig.app.presentation.components.*
import com.littlegig.app.presentation.theme.*
import com.littlegig.app.data.model.UserRank
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

@Composable
fun ChatScreen(
    navController: NavController,
    viewModel: ChatViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState(initial = ChatUiState())
    val isDark = isSystemInDarkTheme()
    var showSearch by remember { mutableStateOf(false) }
    var searchQuery by remember { mutableStateOf("") }
    
    // Proper dark/light mode background
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                if (isDark) {
                    androidx.compose.ui.graphics.Brush.verticalGradient(
                        colors = listOf(
                            DarkBackground,
                            DarkSurface
                        )
                    )
                } else {
                    androidx.compose.ui.graphics.Brush.verticalGradient(
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
            // Header with search functionality
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
                            text = "Chats",
                            style = MaterialTheme.typography.headlineMedium.copy(
                                fontWeight = FontWeight.Bold
                            ),
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        
                        IconButton(
                            onClick = { showSearch = !showSearch }
                        ) {
                            Icon(
                                imageVector = if (showSearch) Icons.Default.Close else Icons.Default.Search,
                                contentDescription = if (showSearch) "Close Search" else "Search Users",
                                tint = LittleGigPrimary
                            )
                        }
                    }
                    
                    // Search Bar
                    if (showSearch) {
                        Spacer(modifier = Modifier.height(12.dp))
                        
                        OutlinedTextField(
                            value = searchQuery,
                            onValueChange = { 
                                searchQuery = it
                                viewModel.searchUsers(it)
                            },
                            placeholder = { Text("Search users to start a chat...") },
                            modifier = Modifier.fillMaxWidth(),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = LittleGigPrimary,
                                unfocusedBorderColor = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.3f)
                            ),
                            leadingIcon = {
                                Icon(
                                    imageVector = Icons.Default.Search,
                                    contentDescription = null,
                                    tint = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        )
                    }
                }
            }
            
            Spacer(modifier = Modifier.height(20.dp))
            
            when {
                uiState.isLoading -> {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        AdvancedGlassmorphicCard {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(32.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                CircularProgressIndicator(
                                    color = LittleGigPrimary,
                                    strokeWidth = 3.dp
                                )
                            }
                        }
                    }
                }
                showSearch && uiState.searchResults.isNotEmpty() -> {
                    // Search Results
                    LazyColumn(
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        items(uiState.searchResults) { user ->
                            UserSearchCard(
                                user = user,
                                onClick = { 
                                    viewModel.startChat(user.id)
                                    showSearch = false
                                    searchQuery = ""
                                }
                            )
                        }
                    }
                }
                showSearch && searchQuery.isNotBlank() && uiState.searchResults.isEmpty() -> {
                    // No search results
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
                                Icon(
                                    imageVector = Icons.Default.Search,
                                    contentDescription = null,
                                    modifier = Modifier.size(48.dp),
                                    tint = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                                
                                Spacer(modifier = Modifier.height(16.dp))
                                
                                Text(
                                    text = "No users found",
                                    style = MaterialTheme.typography.titleMedium.copy(
                                        fontWeight = FontWeight.Bold
                                    ),
                                    color = MaterialTheme.colorScheme.onSurface
                                )
                                
                                Spacer(modifier = Modifier.height(8.dp))
                                
                                Text(
                                    text = "Try a different search term",
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                                    textAlign = androidx.compose.ui.text.style.TextAlign.Center
                                )
                            }
                        }
                    }
                }
                uiState.chats.isEmpty() && !showSearch -> {
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
                                            // Outer glow
                                            drawCircle(
                                                color = LittleGigPrimary.copy(alpha = 0.3f),
                                                center = androidx.compose.ui.geometry.Offset(size.width / 2, size.height / 2),
                                                radius = 40.dp.toPx()
                                            )
                                            // Inner glow
                                            drawCircle(
                                                color = LittleGigPrimary.copy(alpha = 0.6f),
                                                center = androidx.compose.ui.geometry.Offset(size.width / 2, size.height / 2),
                                                radius = 30.dp.toPx()
                                            )
                                            // Core
                                            drawCircle(
                                                color = LittleGigPrimary,
                                                center = androidx.compose.ui.geometry.Offset(size.width / 2, size.height / 2),
                                                radius = 20.dp.toPx()
                                            )
                                        }
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Chat,
                                        contentDescription = null,
                                        modifier = Modifier
                                            .fillMaxSize()
                                            .padding(16.dp),
                                        tint = MaterialTheme.colorScheme.onSurface
                                    )
                                }
                                
                                Spacer(modifier = Modifier.height(24.dp))
                                
                                Text(
                                    text = "No chats yet",
                                    style = MaterialTheme.typography.headlineSmall.copy(
                                        fontWeight = FontWeight.Bold
                                    ),
                                    color = MaterialTheme.colorScheme.onSurface
                                )
                                
                                Spacer(modifier = Modifier.height(8.dp))
                                
                                Text(
                                    text = "Search for users to start conversations",
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                                    textAlign = androidx.compose.ui.text.style.TextAlign.Center
                                )
                                
                                Spacer(modifier = Modifier.height(16.dp))
                                
                                Button(
                                    onClick = { showSearch = true },
                                    colors = ButtonDefaults.buttonColors(
                                        containerColor = LittleGigPrimary
                                    )
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Search,
                                        contentDescription = null,
                                        modifier = Modifier.size(16.dp)
                                    )
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text("Search Users")
                                }
                            }
                        }
                    }
                }
                else -> {
                    LazyColumn(
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        items(uiState.chats) { chat ->
                            ChatCard(
                                chat = chat,
                                onClick = { 
                                    // Navigate to chat details
                                }
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
fun UserSearchCard(
    user: UserSearchResult,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        onClick = onClick,
        modifier = modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Profile Image
            AsyncImage(
                model = user.profileImageUrl,
                contentDescription = user.name,
                modifier = Modifier
                    .size(50.dp)
                    .clip(CircleShape),
                contentScale = ContentScale.Crop
            )
            
            Spacer(modifier = Modifier.width(12.dp))
            
            Column(
                modifier = Modifier.weight(1f)
            ) {
                // Name
                Text(
                    text = user.name,
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.Bold
                    ),
                    color = MaterialTheme.colorScheme.onSurface
                )
                
                Spacer(modifier = Modifier.height(4.dp))
                
                // Rank
                Text(
                    text = user.rank.name,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            
            // Start Chat Button
            Button(
                onClick = onClick,
                colors = ButtonDefaults.buttonColors(
                    containerColor = LittleGigPrimary
                )
            ) {
                Icon(
                    imageVector = Icons.Default.Chat,
                    contentDescription = null,
                    modifier = Modifier.size(16.dp)
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text("Chat")
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChatCard(
    chat: ChatPreview,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        onClick = onClick,
        modifier = modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Profile Image
            AsyncImage(
                model = chat.profileImageUrl,
                contentDescription = chat.name,
                modifier = Modifier
                    .size(50.dp)
                    .clip(CircleShape),
                contentScale = ContentScale.Crop
            )
            
            Spacer(modifier = Modifier.width(12.dp))
            
            Column(
                modifier = Modifier.weight(1f)
            ) {
                // Name
                Text(
                    text = chat.name,
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.Bold
                    ),
                    color = MaterialTheme.colorScheme.onSurface
                )
                
                Spacer(modifier = Modifier.height(4.dp))
                
                // Last Message
                Text(
                    text = chat.lastMessage,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    maxLines = 2
                )
                
                Spacer(modifier = Modifier.height(4.dp))
                
                // Time
                Text(
                    text = chat.lastMessageTime,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            
            // Unread count
            if (chat.unreadCount > 0) {
                Surface(
                    shape = CircleShape,
                    color = LittleGigPrimary
                ) {
                    Text(
                        text = chat.unreadCount.toString(),
                        style = MaterialTheme.typography.labelSmall,
                        color = Color.White,
                        modifier = Modifier.padding(8.dp)
                    )
                }
            }
        }
    }
}

data class ChatPreview(
    val id: String,
    val name: String,
    val profileImageUrl: String,
    val lastMessage: String,
    val lastMessageTime: String,
    val unreadCount: Int = 0
)

data class UserSearchResult(
    val id: String,
    val name: String,
    val profileImageUrl: String,
    val rank: UserRank
)

@HiltViewModel
class ChatViewModel @Inject constructor() : ViewModel() {
    
    private val _uiState = MutableStateFlow(ChatUiState())
    val uiState: StateFlow<ChatUiState> = _uiState.asStateFlow()
    
    init {
        loadChats()
    }
    
    private fun loadChats() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true)
            
            // TODO: Load chats from repository
            // For now, show empty state
            _uiState.value = _uiState.value.copy(
                chats = emptyList(),
                isLoading = false
            )
        }
    }
    
    fun searchUsers(query: String) {
        viewModelScope.launch {
            if (query.isBlank()) {
                _uiState.value = _uiState.value.copy(searchResults = emptyList())
                return@launch
            }
            
            // TODO: Implement actual user search
            // For now, show mock results
            val mockUsers = listOf(
                UserSearchResult(
                    id = "1",
                    name = "John Doe",
                    profileImageUrl = "",
                    rank = UserRank.POPULAR
                ),
                UserSearchResult(
                    id = "2", 
                    name = "Jane Smith",
                    profileImageUrl = "",
                    rank = UserRank.INFLUENCER
                )
            ).filter { it.name.contains(query, ignoreCase = true) }
            
            _uiState.value = _uiState.value.copy(searchResults = mockUsers)
        }
    }
    
    fun startChat(userId: String) {
        viewModelScope.launch {
            // TODO: Create chat with user
            // For now, just add to chats list
            val newChat = ChatPreview(
                id = "chat_$userId",
                name = "User $userId",
                profileImageUrl = "",
                lastMessage = "Chat started!",
                lastMessageTime = "Now"
            )
            
            _uiState.value = _uiState.value.copy(
                chats = _uiState.value.chats + newChat
            )
        }
    }
}

data class ChatUiState(
    val chats: List<ChatPreview> = emptyList(),
    val searchResults: List<UserSearchResult> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null
) 