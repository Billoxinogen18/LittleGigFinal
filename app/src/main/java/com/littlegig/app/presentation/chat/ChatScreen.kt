package com.littlegig.app.presentation.chat

import androidx.compose.foundation.background
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
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
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
import android.Manifest
import android.content.pm.PackageManager
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import android.provider.ContactsContract
import timber.log.Timber
import androidx.compose.runtime.snapshotFlow

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChatScreen(
    navController: NavController,
    viewModel: ChatViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState(initial = ChatUiState())
    val chats by viewModel.chats.collectAsState()
    val searchResults by viewModel.searchResults.collectAsState()
    val isDark = isSystemInDarkTheme()
    val contactsUsers by viewModel.contactsUsers.collectAsState()
    val allUsers by viewModel.allUsers.collectAsState()
    val context = LocalContext.current
    var showSearch by remember { mutableStateOf(false) }
    var searchQuery by remember { mutableStateOf("") }
    var showContactsOnly by remember { mutableStateOf(false) }

    // Proactively load initial users to avoid empty main list
    LaunchedEffect(Unit) {
        viewModel.loadAllUsers()
    }
    
    LaunchedEffect(contactsUsers.size, allUsers.size) {
        Timber.i("ChatSearch counts: contacts=${contactsUsers.size}, all=${allUsers.size}")
    }

    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { granted ->
        if (granted) {
            val phones = fetchPhoneNumbersFromContacts(context)
            viewModel.loadContacts(phones)
            viewModel.loadAllUsers()
        } else {
            viewModel.loadAllUsers()
        }
    }
    
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
                        Column(Modifier.weight(1f)) {
                            Text(
                                text = "Chats",
                                style = MaterialTheme.typography.headlineMedium.copy(
                                    fontWeight = FontWeight.Bold
                                ),
                                color = MaterialTheme.colorScheme.onSurface
                            )
                            OutlinedTextField(
                                value = viewModel.chatSearchQuery.collectAsState().value,
                                onValueChange = { viewModel.updateChatSearchQuery(it) },
                                placeholder = { Text("Search chats...") },
                                singleLine = true,
                                modifier = Modifier.fillMaxWidth(),
                                shape = RoundedCornerShape(12.dp)
                            )
                        }
                        
                        Row {
                            IconButton(
                                onClick = { showSearch = !showSearch }
                            ) {
                                Icon(
                                    imageVector = if (showSearch) Icons.Default.Close else Icons.Default.Search,
                                    contentDescription = if (showSearch) "Close Search" else "Search Users",
                                    tint = LittleGigPrimary
                                )
                            }
                            
                            IconButton(
                                onClick = { 
                                    navController.navigate("people_discovery")
                                }
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Add,
                                    contentDescription = "New Chat",
                                    tint = LittleGigPrimary
                                )
                            }
                        }
                    }
                    
                    if (showSearch) {
                        Spacer(modifier = Modifier.height(16.dp))

                        // Contacts vs All toggle
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            FilterChip(
                                selected = showContactsOnly,
                                onClick = {
                                    showContactsOnly = true
                                    // Ensure contacts are loaded when switching
                                    val hasPermission = ContextCompat.checkSelfPermission(
                                        context, Manifest.permission.READ_CONTACTS
                                    ) == PackageManager.PERMISSION_GRANTED
                                    if (hasPermission && contactsUsers.isEmpty()) {
                                        val phones = fetchPhoneNumbersFromContacts(context)
                                        viewModel.loadContacts(phones)
                                    }
                                },
                                label = { Text("Contacts on LittleGig") }
                            )
                            FilterChip(
                                selected = !showContactsOnly,
                                onClick = {
                                    showContactsOnly = false
                                    // Load all users list once
                                    if (allUsers.isEmpty()) viewModel.loadAllUsers()
                                },
                                label = { Text("All users") }
                            )
                        }
                        Spacer(modifier = Modifier.height(12.dp))
                        
                        OutlinedTextField(
                            value = searchQuery,
                            onValueChange = { 
                                searchQuery = it
                                viewModel.searchUsers(it)
                            },
                            label = { Text("Search users...") },
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(12.dp),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = LittleGigPrimary,
                                unfocusedBorderColor = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.3f)
                            ),
                            leadingIcon = {
                                Icon(
                                    imageVector = Icons.Default.Search,
                                    contentDescription = null,
                                    tint = LittleGigPrimary
                                )
                            }
                        )
                    }
                }
            }
            
            Spacer(modifier = Modifier.height(16.dp))
            
            // Single viewport area for lists to avoid stacking LazyColumns
            Box(modifier = Modifier.weight(1f)) {
                when {
                    // Search results visible
                    showSearch && (remember(searchResults, contactsUsers, allUsers, showContactsOnly, searchQuery) {
                        val base = if (searchQuery.isBlank()) {
                            if (showContactsOnly) contactsUsers else allUsers
                        } else {
                            if (showContactsOnly) {
                                val ids = contactsUsers.map { it.id }.toSet()
                                searchResults.filter { ids.contains(it.id) }
                            } else searchResults
                        }
                        base
                    }).isNotEmpty() -> {
                        // Render Search Results list
                        val displayedUsers = remember(searchResults, contactsUsers, allUsers, showContactsOnly, searchQuery) {
                            val base = if (searchQuery.isBlank()) {
                                if (showContactsOnly) contactsUsers else allUsers
                            } else {
                                if (showContactsOnly) {
                                    val ids = contactsUsers.map { it.id }.toSet()
                                    searchResults.filter { ids.contains(it.id) }
                                } else searchResults
                            }
                            base
                        }
                        AdvancedGlassmorphicCard {
                            Column(modifier = Modifier.padding(16.dp)) {
                                Text(
                                    text = "Search Results",
                                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.SemiBold),
                                    color = MaterialTheme.colorScheme.onSurface
                                )
                                Spacer(modifier = Modifier.height(12.dp))
                                val userListState = rememberLazyListState()
                                LaunchedEffect(userListState) {
                                    snapshotFlow { userListState.layoutInfo.visibleItemsInfo.lastOrNull()?.index to userListState.layoutInfo.totalItemsCount }
                                        .collect { (last, total) ->
                                            if (last != null && total > 0 && last >= total - 3 && !showContactsOnly) {
                                                viewModel.loadMoreAllUsers()
                                            }
                                        }
                                }
                                LazyColumn(state = userListState, verticalArrangement = Arrangement.spacedBy(8.dp)) {
                                    items(displayedUsers) { user ->
                                        HapticButton(onClick = {
                                            viewModel.createChatWithUser(user.id)
                                            showSearch = false
                                            searchQuery = ""
                                        }) {
                                            AdvancedNeumorphicCard(modifier = Modifier.fillMaxWidth()) {
                                                Row(
                                                    modifier = Modifier
                                                        .fillMaxWidth()
                                                        .padding(12.dp),
                                                    verticalAlignment = Alignment.CenterVertically
                                                ) {
                                                    // User Avatar
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
                                                            style = MaterialTheme.typography.bodyLarge.copy(
                                                                fontWeight = FontWeight.Medium
                                                            ),
                                                            color = MaterialTheme.colorScheme.onSurface
                                                        )
                                                        
                                                        Text(
                                                            text = user.email,
                                                            style = MaterialTheme.typography.bodySmall,
                                                            color = MaterialTheme.colorScheme.onSurfaceVariant
                                                        )
                                                    }
                                                    
                                                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                                        IconButton(onClick = { viewModel.blockUser(user.id) }) {
                                                            Icon(Icons.Default.Block, contentDescription = "Block", tint = MaterialTheme.colorScheme.error)
                                                        }
                                                        IconButton(onClick = { viewModel.reportUser(user.id, "abuse") }) {
                                                            Icon(Icons.Default.Report, contentDescription = "Report", tint = MaterialTheme.colorScheme.tertiary)
                                                        }
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                    // Main Users list (not searching) when we have users
                    !showSearch && allUsers.isNotEmpty() -> {
                        AdvancedGlassmorphicCard {
                            Column(modifier = Modifier.padding(16.dp)) {
                                Text(
                                    text = "People on LittleGig",
                                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.SemiBold),
                                    color = MaterialTheme.colorScheme.onSurface
                                )
                                Spacer(modifier = Modifier.height(12.dp))
                                val userListState = rememberLazyListState()
                                LaunchedEffect(userListState) {
                                    snapshotFlow { userListState.layoutInfo.visibleItemsInfo.lastOrNull()?.index to userListState.layoutInfo.totalItemsCount }
                                        .collect { (last, total) ->
                                            if (last != null && total > 0 && last >= total - 3) {
                                                viewModel.loadMoreAllUsers()
                                            }
                                        }
                                }
                                LazyColumn(state = userListState, verticalArrangement = Arrangement.spacedBy(8.dp)) {
                                    items(allUsers) { user ->
                                        HapticButton(onClick = { viewModel.createChatWithUser(user.id) }) {
                                            AdvancedNeumorphicCard(modifier = Modifier.fillMaxWidth()) {
                                                Row(
                                                    modifier = Modifier
                                                        .fillMaxWidth()
                                                        .padding(12.dp),
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
                                    }
                                }
                            }
                        }
                    }
                    // Otherwise show Chats or Empty states
                    else -> {
                        if (chats.isNotEmpty()) {
                            val chatsListState = rememberLazyListState()
                            LaunchedEffect(chatsListState) {
                                snapshotFlow { chatsListState.layoutInfo.visibleItemsInfo.lastOrNull()?.index to chatsListState.layoutInfo.totalItemsCount }
                                    .collect { (last, total) ->
                                        if (last != null && total > 0 && last >= total - 3) {
                                            viewModel.loadMoreChats()
                                        }
                                    }
                            }
                            LazyColumn(state = chatsListState, verticalArrangement = Arrangement.spacedBy(12.dp)) {
                                items(chats) { chat ->
                                    HapticButton(onClick = { navController.navigate("chat_details/${chat.id}") }) {
                                        AdvancedNeumorphicCard(modifier = Modifier.fillMaxWidth()) {
                                            Row(
                                                modifier = Modifier
                                                    .fillMaxWidth()
                                                    .padding(16.dp),
                                                verticalAlignment = Alignment.CenterVertically
                                            ) {
                                                // Chat Avatar
                                                Box(
                                                    modifier = Modifier
                                                        .size(50.dp)
                                                        .clip(CircleShape)
                                                        .background(LittleGigPrimary.copy(alpha = 0.1f)),
                                                    contentAlignment = Alignment.Center
                                                ) {
                                                    when (chat.chatType) {
                                                        com.littlegig.app.data.model.ChatType.DIRECT -> {
                                                            if (chat.participants.isNotEmpty()) {
                                                                // Show first participant avatar
                                                                Icon(
                                                                    imageVector = Icons.Default.Person,
                                                                    contentDescription = null,
                                                                    modifier = Modifier.size(30.dp),
                                                                    tint = LittleGigPrimary
                                                                )
                                                            } else {
                                                                Icon(
                                                                    imageVector = Icons.Default.Person,
                                                                    contentDescription = null,
                                                                    modifier = Modifier.size(30.dp),
                                                                    tint = LittleGigPrimary
                                                                )
                                                            }
                                                        }
                                                        com.littlegig.app.data.model.ChatType.GROUP -> {
                                                            Icon(
                                                                imageVector = Icons.Default.Group,
                                                                contentDescription = null,
                                                                modifier = Modifier.size(30.dp),
                                                                tint = LittleGigPrimary
                                                            )
                                                        }
                                                        com.littlegig.app.data.model.ChatType.EVENT -> {
                                                            Icon(
                                                                imageVector = Icons.Default.Event,
                                                                contentDescription = null,
                                                                modifier = Modifier.size(30.dp),
                                                                tint = LittleGigPrimary
                                                            )
                                                        }
                                                    }
                                                }
                                                
                                                Spacer(modifier = Modifier.width(16.dp))
                                                
                                                Column(modifier = Modifier.weight(1f)) {
                                                    Text(
                                                        text = chat.name ?: "Chat",
                                                        style = MaterialTheme.typography.titleMedium.copy(
                                                            fontWeight = FontWeight.SemiBold
                                                        ),
                                                        color = MaterialTheme.colorScheme.onSurface
                                                    )
                                                    
                                                    if (chat.lastMessage != null) {
                                                        Text(
                                                            text = chat.lastMessage,
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
                                                
                                                // Unread indicator
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
                                                val mePinned by viewModel.pinnedChatIds.collectAsState()
                                                if (mePinned.contains(chat.id)) {
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
                            }
                        } else {
                            // Empty state when no chats and no users
                            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                                AdvancedGlassmorphicCard {
                                    Column(
                                        modifier = Modifier.padding(32.dp),
                                        horizontalAlignment = Alignment.CenterHorizontally
                                    ) {
                                        Icon(imageVector = Icons.Default.Chat, contentDescription = null, modifier = Modifier.size(64.dp), tint = LittleGigPrimary)
                                        Spacer(modifier = Modifier.height(16.dp))
                                        Text(
                                            text = "No Chats Yet",
                                            style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.Bold),
                                            color = MaterialTheme.colorScheme.onSurface
                                        )
                                        Spacer(modifier = Modifier.height(8.dp))
                                        Text(
                                            text = "Start a conversation with other users",
                                            style = MaterialTheme.typography.bodyMedium,
                                            color = MaterialTheme.colorScheme.onSurfaceVariant
                                        )
                                        Spacer(modifier = Modifier.height(24.dp))
                                        HapticButton(onClick = { showSearch = true }) {
                                            AdvancedNeumorphicCard {
                                                Row(modifier = Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
                                                    Icon(imageVector = Icons.Default.Search, contentDescription = null, tint = LittleGigPrimary, modifier = Modifier.size(24.dp))
                                                    Spacer(modifier = Modifier.width(12.dp))
                                                    Text(text = "Find People to Chat", style = MaterialTheme.typography.bodyLarge, color = LittleGigPrimary)
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
            
            Spacer(modifier = Modifier.height(16.dp))
        }
        
        // Loading state
        if (uiState.isLoading) {
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
                            tint = Color(0xFFEF4444),
                            modifier = Modifier.size(48.dp)
                        )
                        
                        Spacer(modifier = Modifier.height(16.dp))
                        
                        Text(
                            text = "Error",
                            style = MaterialTheme.typography.titleLarge.copy(
                                fontWeight = FontWeight.Bold
                            ),
                            color = Color(0xFFEF4444)
                        )
                        
                        Spacer(modifier = Modifier.height(8.dp))
                        
                        Text(
                            text = uiState.error ?: "An error occurred",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        
                        Spacer(modifier = Modifier.height(16.dp))
                        
                        HapticButton(
                            onClick = { viewModel.clearError() }
                        ) {
                            AdvancedNeumorphicCard {
                                Text(
                                    text = "Retry",
                                    style = MaterialTheme.typography.bodyLarge,
                                    color = LittleGigPrimary,
                                    modifier = Modifier.padding(16.dp)
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

fun fetchPhoneNumbersFromContacts(context: android.content.Context): List<String> {
    val phones = mutableSetOf<String>()
    val resolver = context.contentResolver
    val cursor = resolver.query(
        android.provider.ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
        arrayOf(
            android.provider.ContactsContract.CommonDataKinds.Phone.NUMBER
        ),
        null,
        null,
        null
    )
    if (cursor != null) {
        while (cursor.moveToNext()) {
            val phone = cursor.getString(0)
            if (!phone.isNullOrBlank()) phones.add(phone)
        }
        cursor.close()
    }
    return phones.toList()
}

 