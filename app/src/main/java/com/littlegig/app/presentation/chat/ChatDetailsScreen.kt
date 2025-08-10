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
import com.littlegig.app.data.model.Message
import com.littlegig.app.data.model.MessageType
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import com.littlegig.app.services.ChatMediaService
import androidx.compose.ui.platform.LocalContext

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
    val context = LocalContext.current
    val imagePicker = rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
        uri?.let { viewModel.uploadAndSendMedia(chatId, it, "image/*") }
    }
    
    LaunchedEffect(chatId) {
        viewModel.loadChat(chatId)
        viewModel.loadMessages(chatId)
    }
    
    LaunchedEffect(messages.size) {
        if (messages.isNotEmpty()) {
            listState.animateScrollToItem(messages.size - 1)
        }
    }
    
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
                    Box(
                        modifier = Modifier
                            .size(40.dp)
                            .clip(CircleShape)
                            .background(LittleGigPrimary.copy(alpha = 0.1f)),
                        contentAlignment = Alignment.Center
                    ) {
                        when (chat?.chatType) {
                            com.littlegig.app.data.model.ChatType.DIRECT -> Icon(Icons.Default.Person, contentDescription = null, tint = LittleGigPrimary)
                            com.littlegig.app.data.model.ChatType.GROUP -> Icon(Icons.Default.Group, contentDescription = null, tint = LittleGigPrimary)
                            com.littlegig.app.data.model.ChatType.EVENT -> Icon(Icons.Default.Event, contentDescription = null, tint = LittleGigPrimary)
                            else -> Icon(Icons.Default.Chat, contentDescription = null, tint = LittleGigPrimary)
                        }
                    }
                    Spacer(modifier = Modifier.width(12.dp))
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = chat?.name ?: "Chat",
                            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.SemiBold),
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Text(
                            text = "${chat?.participants?.size ?: 0} participants",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }
            
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
                    if (message.messageType == com.littlegig.app.data.model.MessageType.TICKET_SHARE && message.sharedTicket != null) {
                        TicketShareBubble(
                            messageId = message.id,
                            ticket = message.sharedTicket,
                            onRedeem = { mid, tid -> viewModel.redeemTicket(chatId, mid, tid) }
                        )
                    } else {
                        NeumorphicChatBubble(
                            message = message,
                            isFromCurrentUser = message.senderId == viewModel.currentUserId,
                            onLikeMessage = { /* TODO: reactions */ _ -> },
                            onShareTicket = { /* redeem flow triggers in chat viewmodel */ }
                        )
                    }
                }
            }
            
            NeumorphicChatInput(
                message = messageText,
                onMessageChange = {
                    messageText = it
                    viewModel.setTyping(chatId, it.isNotBlank())
                },
                onSendMessage = {
                    if (messageText.isNotBlank()) {
                        viewModel.sendMessage(chatId, messageText)
                        messageText = ""
                        viewModel.setTyping(chatId, false)
                    }
                },
                onAttachMedia = { imagePicker.launch("image/*") },
                onShareTicket = { viewModel.promptShareTicket(chatId) },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            )
        }
        
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
                            style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                            color = Color(0xFFEF4444)
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = uiState.error ?: "An error occurred",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        HapticButton(onClick = { viewModel.clearError() }) {
                            AdvancedNeumorphicCard { Text(text = "Retry", modifier = Modifier.padding(16.dp), color = LittleGigPrimary) }
                        }
                    }
                }
            }
        }
    }
}

@HiltViewModel
class ChatDetailsViewModel @Inject constructor(
    private val chatRepository: com.littlegig.app.data.repository.ChatRepository,
    private val authRepository: com.littlegig.app.data.repository.AuthRepository,
    private val chatMediaService: ChatMediaService
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
    private val _typingUsers = MutableStateFlow<List<String>>(emptyList())
    val typingUsers: StateFlow<List<String>> = _typingUsers.asStateFlow()
    private var shareTicketChatId: String? = null
    
    fun loadChat(chatId: String) {
        viewModelScope.launch {
            try {
                // TODO: fetch Chat doc
                _chat.value = com.littlegig.app.data.model.Chat(id = chatId)
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
                _uiState.value = _uiState.value.copy(isLoading = false, error = e.message)
            }
        }
    }

    fun observeTyping(chatId: String) {
        viewModelScope.launch {
            chatRepository.getTypingIndicators(chatId).collect { list ->
                val me = currentUserId
                _typingUsers.value = list.mapNotNull { it["userId"] as? String }.filter { it != me }
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
                        senderName = currentUser.displayName,
                        senderImageUrl = currentUser.profileImageUrl,
                        messageType = com.littlegig.app.data.model.MessageType.TEXT,
                        timestamp = System.currentTimeMillis(),
                        readBy = emptyList()
                    )
                    chatRepository.sendMessage(chatId, message)
                }
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(error = e.message)
            }
        }
    }

    fun promptShareTicket(chatId: String) {
        shareTicketChatId = chatId
        // This could open a bottom sheet listing user tickets; kept minimal here
        _uiState.value = _uiState.value.copy(error = "Open ticket share sheet (to be implemented)")
    }

    fun setTyping(chatId: String, isTyping: Boolean) {
        val me = currentUserId ?: return
        viewModelScope.launch { chatRepository.setTypingStatus(chatId, me, isTyping) }
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

    fun uploadAndSendMedia(chatId: String, uri: Uri, mime: String?) {
        viewModelScope.launch {
            try {
                chatMediaService.uploadChatMedia(chatId, uri, mime).collect { progress ->
                    if (progress.downloadUrl != null) {
                        val me = authRepository.currentUser.value ?: return@collect
                        val msg = com.littlegig.app.data.model.Message(
                            senderId = me.id,
                            senderName = me.displayName,
                            senderImageUrl = me.profileImageUrl,
                            content = if (mime?.contains("image") == true) "📷 Photo" else "🎬 Video",
                            messageType = if (mime?.contains("image") == true) com.littlegig.app.data.model.MessageType.IMAGE else com.littlegig.app.data.model.MessageType.VIDEO,
                            mediaUrls = listOf(progress.downloadUrl),
                            timestamp = System.currentTimeMillis()
                        )
                        chatRepository.sendMessage(chatId, msg)
                    }
                }
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(error = e.message)
            }
        }
    }

    fun redeemTicket(chatId: String, messageId: String, ticketId: String) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true)
            val res = chatRepository.redeemSharedTicket(chatId, messageId, ticketId)
            _uiState.value = _uiState.value.copy(isLoading = false,
                error = res.exceptionOrNull()?.message)
        }
    }
}

data class ChatDetailsUiState(
    val isLoading: Boolean = false,
    val error: String? = null
) 