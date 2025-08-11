package com.littlegig.app.presentation.chat

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChatScreen(
    navController: NavController,
    viewModel: ChatViewModel = hiltViewModel()
) {
    println("🚨🚨🚨 ChatScreen COMPOSABLE STARTED 🚨🚨🚨")
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
    
    println("🚨🚨🚨 ABOUT TO RENDER UI BOX 🚨🚨🚨")
    
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            // Header
            ChatHeader(
                showSearch = showSearch,
                onShowSearchChange = { showSearch = it },
                searchQuery = searchQuery,
                onSearchQueryChange = {
                    searchQuery = it
                    viewModel.searchUsers(it)
                },
                onNavigateToDiscovery = { navController.navigate("people_discovery") }
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Main Content
            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
            ) {
                if (showSearch) {
                    // People discovery inline: glass cards and chips
                    if (allUsers.isEmpty() && searchResults.isEmpty()) {
                        GlassEmptyState(
                            icon = Icons.Default.PersonSearch,
                            title = "Find people",
                            message = "Search by name, username or phone",
                            primaryActionLabel = "Discover",
                            onPrimaryAction = { navController.navigate("people_discovery") },
                            modifier = Modifier.align(Alignment.Center)
                        )
                    } else {
                        AdvancedGlassmorphicCard {
                            LazyColumn(modifier = Modifier.fillMaxSize()) {
                                val itemsSrc = if (searchQuery.isNotBlank()) searchResults else allUsers
                                items(itemsSrc) { user ->
                                    Surface(onClick = {
                                        viewModel.createChatWithUser(user.id)
                                        showSearch = false
                                    }, color = Color.Transparent) {
                                        Row(
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .padding(12.dp),
                                            verticalAlignment = Alignment.CenterVertically
                                        ) {
                                            Box(
                                                modifier = Modifier.size(40.dp).clip(CircleShape)
                                                    .background(LittleGigPrimary.copy(alpha = 0.15f)),
                                                contentAlignment = Alignment.Center
                                            ) {
                                                Text(user.displayName.firstOrNull()?.uppercase() ?: "?")
                                            }
                                            Spacer(Modifier.width(12.dp))
                                            Column(Modifier.weight(1f)) {
                                                Text(user.displayName, style = MaterialTheme.typography.bodyMedium)
                                                Text("@${user.username}", style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                                            }
                                            AssistChip(onClick = { }, label = { Text("Chat") })
                                        }
                                    }
                                }
                            }
                        }
                    }
                } else {
                    // Chat Mode: Show existing chats
                    if (chats.isNotEmpty()) {
                        LazyColumn {
                            items(chats) { chat ->
                                ChatListItem(
                                    chat = chat,
                                    viewModel = viewModel,
                                    onClick = {
                                        navController.navigate("chat_details/${chat.id}")
                                    }
                                )
                            }
                        }
                    } else {
                        NoChatsEmptyState(
                            onFindPeopleClick = { showSearch = true },
                            onCreateTestChat = { viewModel.createTestChat() }
                        )
                    }
                }
            }
        }

        // Loading and Error Overlays
        if (uiState.isLoading) {
            LoadingOverlay()
        }
        if (uiState.error != null) {
            ErrorOverlay(error = uiState.error!!, onRetry = { viewModel.clearError() })
        }
    }
}

@Composable
private fun ChatHeader(
    showSearch: Boolean,
    onShowSearchChange: (Boolean) -> Unit,
    searchQuery: String,
    onSearchQueryChange: (String) -> Unit,
    onNavigateToDiscovery: () -> Unit
) {
    AdvancedGlassmorphicCard {
        Column(modifier = Modifier.padding(20.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                            Text(
                                text = "Chats",
                    style = MaterialTheme.typography.headlineMedium.copy(fontWeight = FontWeight.Bold),
                                color = MaterialTheme.colorScheme.onSurface
                            )
                Row {
                    IconButton(onClick = { onShowSearchChange(!showSearch) }) {
                                Icon(
                                    imageVector = if (showSearch) Icons.Default.Close else Icons.Default.Search,
                                    contentDescription = if (showSearch) "Close Search" else "Search Users",
                                    tint = LittleGigPrimary
                                )
                            }
                    IconButton(onClick = onNavigateToDiscovery) {
                                Icon(
                                    imageVector = Icons.Default.Add,
                                    contentDescription = "New Chat",
                                    tint = LittleGigPrimary
                                )
                            }
                        }
                    }
                    
            AnimatedVisibility(visible = showSearch) {
                Column {
                        Spacer(modifier = Modifier.height(12.dp))
                        // Pill search
                        Surface(
                            shape = RoundedCornerShape(28.dp),
                            color = Color.Transparent
                        ) {
                            Row(Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 10.dp), verticalAlignment = Alignment.CenterVertically) {
                                Icon(Icons.Default.Search, contentDescription = null, tint = LittleGigPrimary)
                                Spacer(Modifier.width(8.dp))
                                OutlinedTextField(
                                    value = searchQuery,
                                    onValueChange = onSearchQueryChange,
                                    placeholder = { Text("Search people…") },
                                    modifier = Modifier.weight(1f),
                                    singleLine = true,
                                    colors = OutlinedTextFieldDefaults.colors(
                                        focusedBorderColor = Color.Transparent,
                                        unfocusedBorderColor = Color.Transparent,
                                        focusedContainerColor = Color.Transparent,
                                        unfocusedContainerColor = Color.Transparent
                                    )
                                )
                            }
                        }
                        Spacer(modifier = Modifier.height(10.dp))
                        // Gradient chips (view all / contacts)
                        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            GradientChip(label = "All")
                            GradientChip(label = "Contacts")
                        }
                    }
                }
            }
        }
    }

@Composable
private fun GradientChip(label: String) {
    val brush = Brush.horizontalGradient(listOf(LittleGigPrimary, LittleGigPrimary.copy(alpha = 0.6f)))
    Surface(shape = RoundedCornerShape(20.dp), color = Color.Transparent) {
        Box(Modifier.background(brush = brush, alpha = 0.15f).padding(horizontal = 12.dp, vertical = 6.dp)) {
            Text(label, color = LittleGigPrimary, style = MaterialTheme.typography.labelMedium)
        }
    }
}

@Composable
private fun ChatListView(
    chats: List<com.littlegig.app.data.model.Chat>,
    viewModel: ChatViewModel,
    navController: NavController,
    onFindPeopleClick: () -> Unit
) {
    println("🔥 DEBUG: ChatListView - chats.size: ${chats.size}")
    
    if (chats.isNotEmpty()) {
        println("🔥 DEBUG: SHOWING CHAT LIST IN LAZY COLUMN")
        LazyColumn(verticalArrangement = Arrangement.spacedBy(12.dp)) {
            items(chats, key = { it.id }) { chat ->
                ChatListItem(
                    chat = chat,
                    viewModel = viewModel,
                    onClick = { navController.navigate("chat_details/${chat.id}") }
                )
            }
        }
                            } else {
        println("🔥 DEBUG: SHOWING NO CHATS EMPTY STATE")
        NoChatsEmptyState(
            onFindPeopleClick = onFindPeopleClick,
            onCreateTestChat = { /* No test chat in this context */ }
        )
    }
}



@Composable
private fun UserDiscoveryView(
    searchQuery: String,
    allUsers: List<com.littlegig.app.data.model.User>,
    searchResults: List<com.littlegig.app.data.model.User>,
    viewModel: ChatViewModel,
    onUserClick: (String) -> Unit
) {
    val usersToShow = if (searchQuery.isNotBlank()) searchResults else allUsers

    if (usersToShow.isNotEmpty()) {
                        AdvancedGlassmorphicCard {
                            Column(modifier = Modifier.padding(16.dp)) {
                                Text(
                    text = if (searchQuery.isNotBlank()) "Search Results" else "People on LittleGig",
                                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.SemiBold),
                                    color = MaterialTheme.colorScheme.onSurface
                                )
                                Spacer(modifier = Modifier.height(12.dp))
                LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    items(usersToShow, key = { it.id }) { user ->
                        UserListItem(user = user, onClick = { onUserClick(user.id) })
                    }
                }
            }
        }
         } else {
         Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
             AdvancedGlassmorphicCard {
                 Column(
                     modifier = Modifier.padding(32.dp),
                     horizontalAlignment = Alignment.CenterHorizontally
                 ) {
                     Icon(
                         imageVector = Icons.Default.SearchOff,
                         contentDescription = null,
                         modifier = Modifier.size(64.dp),
                         tint = MaterialTheme.colorScheme.onSurfaceVariant
                     )
                     Spacer(modifier = Modifier.height(16.dp))
                     Text(
                         text = "No Users Found",
                         style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.Bold),
                         color = MaterialTheme.colorScheme.onSurface
                     )
                     Spacer(modifier = Modifier.height(8.dp))
                     Text(
                         text = "Try a different search or invite some friends!",
                         style = MaterialTheme.typography.bodyMedium,
                         color = MaterialTheme.colorScheme.onSurfaceVariant
                     )
                 }
             }
         }
     }
}

// Helper composables (UserListItem, NoChatsEmptyState, LoadingOverlay, ErrorOverlay)
// These would contain the specific UI for each item/state, keeping the main screen logic clean.

@Composable
private fun ChatListItem(
    chat: com.littlegig.app.data.model.Chat,
    viewModel: ChatViewModel,
    onClick: () -> Unit
) {
    val pinnedChatIds by viewModel.pinnedChatIds.collectAsState()

    HapticButton(onClick = onClick) {
        AdvancedNeumorphicCard(modifier = Modifier.fillMaxWidth()) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Avatar
                Box(
                    modifier = Modifier
                        .size(50.dp)
                        .clip(CircleShape)
                        .background(LittleGigPrimary.copy(alpha = 0.1f)),
                    contentAlignment = Alignment.Center
                ) {
                    when (chat.chatType) {
                        com.littlegig.app.data.model.ChatType.DIRECT -> Icon(Icons.Default.Person, null, tint = LittleGigPrimary, modifier = Modifier.size(30.dp))
                        com.littlegig.app.data.model.ChatType.GROUP -> Icon(Icons.Default.Group, null, tint = LittleGigPrimary, modifier = Modifier.size(30.dp))
                        com.littlegig.app.data.model.ChatType.EVENT -> Icon(Icons.Default.Event, null, tint = LittleGigPrimary, modifier = Modifier.size(30.dp))
                    }
                }

                Spacer(modifier = Modifier.width(16.dp))

                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = chat.name ?: "Chat",
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.SemiBold),
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    chat.lastMessage?.let {
                        Text(
                            text = it,
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            maxLines = 2
                        )
                    }
                    Text(
                        text = "Participants: ${chat.participants.size}",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }

                if (chat.unreadCount > 0) {
                    Box(
                        modifier = Modifier
                            .size(20.dp)
                            .clip(CircleShape)
                            .background(LittleGigPrimary),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = chat.unreadCount.toString(),
                            style = MaterialTheme.typography.labelSmall,
                            color = Color.White
                        )
                    }
                }

                if (pinnedChatIds.contains(chat.id)) {
                    IconButton(onClick = { viewModel.unpinChat(chat.id) }) {
                        Icon(Icons.Default.PushPin, contentDescription = "Unpin", tint = LittleGigPrimary)
                    }
                } else {
                    IconButton(onClick = { viewModel.pinChat(chat.id) }) {
                        Icon(Icons.Default.PushPin, contentDescription = "Pin")
                    }
                }
            }
        }
    }
}

@Composable
private fun UserListItem(user: com.littlegig.app.data.model.User, onClick: () -> Unit) {
    Card(
        onClick = onClick,
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape)
                    .background(LittleGigPrimary.copy(alpha = 0.1f)),
                contentAlignment = Alignment.Center
            ) {
                if (user.profilePictureUrl.isNotEmpty()) {
                    AsyncImage(
                        model = user.profilePictureUrl,
                        contentDescription = null,
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop
                    )
                } else {
                    Icon(
                        imageVector = Icons.Default.Person,
                        contentDescription = null,
                        modifier = Modifier.size(24.dp),
                        tint = LittleGigPrimary
                    )
                }
            }
            Spacer(modifier = Modifier.width(12.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = user.displayName.ifEmpty { user.name },
                    style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Medium),
                    color = MaterialTheme.colorScheme.onSurface
                )
                Text(
                    text = "@${user.username}",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}

@Composable
private fun NoChatsEmptyState(onFindPeopleClick: () -> Unit, onCreateTestChat: () -> Unit) {
    println("🔥 DEBUG: RENDERING NoChatsEmptyState")
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Red.copy(alpha = 0.2f)), // DEBUG: Make it visible
        contentAlignment = Alignment.Center
    ) {
        Card(
            modifier = Modifier.padding(24.dp),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surface
            )
        ) {
            Column(
                modifier = Modifier.padding(32.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Icon(
                    imageVector = Icons.Default.Chat,
                    contentDescription = null,
                    modifier = Modifier.size(64.dp),
                    tint = LittleGigPrimary
                )
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = "No Chats Yet",
                    style = MaterialTheme.typography.headlineMedium.copy(fontWeight = FontWeight.Bold),
                    color = MaterialTheme.colorScheme.onSurface
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "Start a conversation with someone!",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    textAlign = TextAlign.Center
                )
                Spacer(modifier = Modifier.height(24.dp))
                
                Button(
                    onClick = onFindPeopleClick,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Icon(
                        imageVector = Icons.Default.Search,
                        contentDescription = null
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Find People")
                }
                
                Spacer(modifier = Modifier.height(16.dp))
                
                // 🔥 DEBUG: TEST CHAT BUTTON 🔥
                Button(
                    onClick = onCreateTestChat,
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFF00AA00)
                    )
                ) {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = null
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Create Test Chat")
                }
            }
        }
    }
}

@Composable
fun LoadingOverlay() {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Black.copy(alpha = 0.5f)),
                contentAlignment = Alignment.Center
            ) {
                LoadingPulseAnimation {
                    CircularProgressIndicator(
                        color = LittleGigPrimary,
                        modifier = Modifier.size(48.dp)
                    )
                }
            }
        }
        
@Composable
fun ErrorOverlay(error: String, onRetry: () -> Unit) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Black.copy(alpha = 0.5f)),
                contentAlignment = Alignment.Center
            ) {
        AdvancedNeumorphicCard(modifier = Modifier.padding(32.dp)) {
                    Column(
                        modifier = Modifier.padding(24.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                Icon(imageVector = Icons.Default.Error, contentDescription = null, tint = Color(0xFFEF4444), modifier = Modifier.size(48.dp))
                        Spacer(modifier = Modifier.height(16.dp))
                Text("Error", style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold), color = Color(0xFFEF4444))
                        Spacer(modifier = Modifier.height(8.dp))
                Text(text = error, style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
                        Spacer(modifier = Modifier.height(16.dp))
                HapticButton(onClick = onRetry) {
                            AdvancedNeumorphicCard {
                        Text(text = "Retry", style = MaterialTheme.typography.bodyLarge, color = LittleGigPrimary, modifier = Modifier.padding(16.dp))
                    }
                }
            }
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