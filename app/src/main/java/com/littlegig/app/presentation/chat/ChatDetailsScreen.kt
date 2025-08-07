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
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.littlegig.app.presentation.components.*
import com.littlegig.app.presentation.theme.*
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun ChatDetailsScreen(
    chatId: String,
    navController: NavController,
    viewModel: ChatDetailsViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val messages by viewModel.messages.collectAsState()
    val chat by viewModel.chat.collectAsState()
    val isDark = isSystemInDarkTheme()
    var messageText by remember { mutableStateOf("") }
    val listState = rememberLazyListState()
    
    LaunchedEffect(chatId) {
        viewModel.loadChat(chatId)
        viewModel.loadMessages(chatId)
    }
    
    LaunchedEffect(messages.size) {
        if (messages.isNotEmpty()) {
            listState.animateScrollToItem(messages.size - 1)
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
            modifier = Modifier.fillMaxSize()
        ) {
            // Header with announcement and pin controls for admins
            AdvancedNeumorphicCard {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(
                        onClick = { navController.popBackStack() }
                    ) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Back",
                            tint = MaterialTheme.colorScheme.onSurface
                        )
                    }
                    
                    Spacer(modifier = Modifier.width(12.dp))
                    
                    // Chat Avatar
                    Box(
                        modifier = Modifier
                            .size(40.dp)
                            .clip(CircleShape)
                            .background(LittleGigPrimary.copy(alpha = 0.1f)),
                        contentAlignment = Alignment.Center
                    ) {
                        when (chat?.chatType) {
                            com.littlegig.app.data.model.ChatType.DIRECT -> {
                                Icon(
                                    imageVector = Icons.Default.Person,
                                    contentDescription = null,
                                    modifier = Modifier.size(24.dp),
                                    tint = LittleGigPrimary
                                )
                            }
                            com.littlegig.app.data.model.ChatType.GROUP -> {
                                Icon(
                                    imageVector = Icons.Default.Group,
                                    contentDescription = null,
                                    modifier = Modifier.size(24.dp),
                                    tint = LittleGigPrimary
                                )
                            }
                            com.littlegig.app.data.model.ChatType.EVENT -> {
                                Icon(
                                    imageVector = Icons.Default.Event,
                                    contentDescription = null,
                                    modifier = Modifier.size(24.dp),
                                    tint = LittleGigPrimary
                                )
                            }
                            else -> {
                                Icon(
                                    imageVector = Icons.Default.Chat,
                                    contentDescription = null,
                                    modifier = Modifier.size(24.dp),
                                    tint = LittleGigPrimary
                                )
                            }
                        }
                    }
                    
                    Spacer(modifier = Modifier.width(12.dp))
                    
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = chat?.name ?: "Chat",
                            style = MaterialTheme.typography.titleMedium.copy(
                                fontWeight = FontWeight.SemiBold
                            ),
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        
                        Text(
                            text = "${chat?.participants?.size ?: 0} participants",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                    
                    if (viewModel.isAdmin()) {
                        var announceText by remember { mutableStateOf("") }
                        IconButton(onClick = { viewModel.showAdminActions = true }) {
                            Icon(
                                imageVector = Icons.Default.MoreVert,
                                contentDescription = "Admin Actions",
                                tint = MaterialTheme.colorScheme.onSurface
                            )
                        }
                        if (viewModel.showAdminActions) {
                            DropdownMenu(expanded = true, onDismissRequest = { viewModel.showAdminActions = false }) {
                                DropdownMenuItem(text = { Text("Set announcement") }, onClick = {
                                    viewModel.showAdminActions = false
                                    viewModel.promptAnnouncement = true
                                })
                            }
                        }
                        if (viewModel.promptAnnouncement) {
                            AlertDialog(
                                onDismissRequest = { viewModel.promptAnnouncement = false },
                                confirmButton = {
                                    TextButton(onClick = {
                                        viewModel.setAnnouncement(chatId, announceText)
                                        viewModel.promptAnnouncement = false
                                    }) { Text("Save") }
                                },
                                dismissButton = { TextButton(onClick = { viewModel.promptAnnouncement = false }) { Text("Cancel") } },
                                title = { Text("Announcement") },
                                text = {
                                    OutlinedTextField(value = announceText, onValueChange = { announceText = it }, placeholder = { Text("Type announcement") })
                                }
                            )
                        }
                    }
                }
            }
            
            // Announcement banner
            chat?.announcement?.let { annStr ->
                if (!annStr.isNullOrBlank()) {
                    AdvancedGlassmorphicCard(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp, vertical = 8.dp)
                    ) {
                        Row(
                            modifier = Modifier.padding(12.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(Icons.Default.Campaign, contentDescription = null, tint = LittleGigPrimary)
                            Spacer(Modifier.width(8.dp))
                            Text(annStr, style = MaterialTheme.typography.bodyMedium)
                        }
                    }
                }
            }

            // Messages
            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .padding(horizontal = 16.dp),
                state = listState,
                verticalArrangement = Arrangement.spacedBy(8.dp),
                contentPadding = PaddingValues(vertical = 16.dp)
            ) {
                items(messages) { message ->
                    MessageBubble(
                        message = message,
                        isOwnMessage = message.senderId == viewModel.currentUserId,
                        isPinned = chat?.pinnedMessageId == message.id,
                        onLongPress = {
                            if (viewModel.isAdmin()) viewModel.pinMessage(chatId, message.id)
                        }
                    )
                }
            }
            
            // Message Input
            AdvancedGlassmorphicCard(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    OutlinedTextField(
                        value = messageText,
                        onValueChange = { messageText = it },
                        placeholder = { Text("Type a message...") },
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(24.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = LittleGigPrimary,
                            unfocusedBorderColor = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.3f)
                        ),
                        maxLines = 4
                    )
                    
                    Spacer(modifier = Modifier.width(12.dp))
                    
                    HapticButton(
                        onClick = {
                            if (messageText.isNotBlank()) {
                                viewModel.sendMessage(chatId, messageText)
                                messageText = ""
                            }
                        },
                        enabled = messageText.isNotBlank()
                    ) {
                        AdvancedNeumorphicCard(
                            modifier = Modifier.size(48.dp)
                        ) {
                            Box(
                                modifier = Modifier.fillMaxSize(),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Send,
                                    contentDescription = "Send",
                                    tint = if (messageText.isNotBlank()) LittleGigPrimary else MaterialTheme.colorScheme.onSurfaceVariant,
                                    modifier = Modifier.size(24.dp)
                                )
                            }
                        }
                    }
                }
            }
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

@Composable
fun MessageBubble(
    message: com.littlegig.app.data.model.Message,
    isOwnMessage: Boolean,
    isPinned: Boolean = false,
    onLongPress: (() -> Unit)? = null
) {
    val isDark = isSystemInDarkTheme()
    val dateFormat = remember { SimpleDateFormat("HH:mm", Locale.getDefault()) }
    
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .then(
                if (onLongPress != null) Modifier.pointerInput(Unit) {
                    detectTapGestures(onLongPress = { onLongPress() })
                } else Modifier
            ),
        horizontalArrangement = if (isOwnMessage) Arrangement.End else Arrangement.Start
    ) {
        AdvancedNeumorphicCard(
            modifier = Modifier.widthIn(max = 280.dp),
            cornerRadius = 20.dp
        ) {
            Column(
                modifier = Modifier.padding(12.dp)
            ) {
                if (isPinned) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.PushPin, contentDescription = null, tint = LittleGigPrimary, modifier = Modifier.size(16.dp))
                        Spacer(Modifier.width(4.dp))
                        Text("Pinned", style = MaterialTheme.typography.labelSmall, color = LittleGigPrimary)
                    }
                    Spacer(Modifier.height(4.dp))
                }
                Text(
                    text = message.content,
                    style = MaterialTheme.typography.bodyMedium,
                    color = if (isOwnMessage) Color.White else MaterialTheme.colorScheme.onSurface
                )
                
                Spacer(modifier = Modifier.height(4.dp))
                
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = dateFormat.format(Date(message.timestamp)),
                        style = MaterialTheme.typography.labelSmall,
                        color = if (isOwnMessage) Color.White.copy(alpha = 0.7f) else MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    
                    if (isOwnMessage) {
                        Spacer(modifier = Modifier.width(4.dp))
                        Icon(
                            imageVector = if (message.readBy.isNotEmpty()) Icons.Default.Done else Icons.Default.Schedule,
                            contentDescription = null,
                            tint = if (isOwnMessage) Color.White.copy(alpha = 0.7f) else MaterialTheme.colorScheme.onSurfaceVariant,
                            modifier = Modifier.size(16.dp)
                        )
                    }
                }
            }
        }
    }
}

@HiltViewModel
class ChatDetailsViewModel @Inject constructor(
    private val chatRepository: com.littlegig.app.data.repository.ChatRepository,
    private val authRepository: com.littlegig.app.data.repository.AuthRepository
) : ViewModel() {
    
    private val _uiState = MutableStateFlow(ChatDetailsUiState())
    val uiState: StateFlow<ChatDetailsUiState> = _uiState.asStateFlow()
    
    private val _messages = MutableStateFlow<List<com.littlegig.app.data.model.Message>>(emptyList())
    val messages: StateFlow<List<com.littlegig.app.data.model.Message>> = _messages.asStateFlow()
    
    private val _chat = MutableStateFlow<com.littlegig.app.data.model.Chat?>(null)
    val chat: StateFlow<com.littlegig.app.data.model.Chat?> = _chat.asStateFlow()
    
    val currentUserId: String?
        get() = authRepository.currentUser.value?.id
    var showAdminActions by mutableStateOf(false)
    var promptAnnouncement by mutableStateOf(false)
    
    fun loadChat(chatId: String) {
        viewModelScope.launch {
            try {
                // In a real app, you would fetch chat details
                // For now, we'll create a placeholder chat
                _chat.value = com.littlegig.app.data.model.Chat(
                    id = chatId,
                    name = "Chat",
                    participants = listOf(),
                    chatType = com.littlegig.app.data.model.ChatType.DIRECT,
                    lastMessage = null,
                    lastMessageTime = 0L,
                    lastMessageSenderId = null,
                    createdAt = System.currentTimeMillis()
                )
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(error = e.message)
            }
        }
    }
    
    fun loadMessages(chatId: String) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, error = null)
            
            try {
                chatRepository.getChatMessages(chatId).collect { chatMessages ->
                    _messages.value = chatMessages
                }
                _uiState.value = _uiState.value.copy(isLoading = false)
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    error = e.message
                )
            }
        }
    }
    
    fun sendMessage(chatId: String, content: String) {
        viewModelScope.launch {
            try {
                val currentUser = authRepository.currentUser.value
                if (currentUser != null) {
                    val message = com.littlegig.app.data.model.Message(
                        content = content,
                        senderId = currentUser.id,
                        messageType = com.littlegig.app.data.model.MessageType.TEXT,
                        timestamp = System.currentTimeMillis(),
                        readBy = emptyList()
                    )
                    chatRepository.sendMessage(chatId, message)
                    // Reload messages to show the new message
                    loadMessages(chatId)
                }
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(error = e.message)
            }
        }
    }
    
    fun clearError() {
        _uiState.value = _uiState.value.copy(error = null)
    }

    fun isAdmin(): Boolean {
        val me = currentUserId
        val c = chat.value
        return me != null && (c?.admins?.contains(me) == true)
    }

    fun setAnnouncement(chatId: String, text: String) {
        viewModelScope.launch {
            try {
                chatRepository.setAnnouncement(chatId, text)
                // Update local chat state
                _chat.value = _chat.value?.copy(announcement = text)
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(error = e.message)
            }
        }
    }

    fun pinMessage(chatId: String, messageId: String) {
        viewModelScope.launch {
            try {
                chatRepository.pinMessage(chatId, messageId)
                _chat.value = _chat.value?.copy(pinnedMessageId = messageId)
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(error = e.message)
            }
        }
    }
}

data class ChatDetailsUiState(
    val isLoading: Boolean = false,
    val error: String? = null
) 