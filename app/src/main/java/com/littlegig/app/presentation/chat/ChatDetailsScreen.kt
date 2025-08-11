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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.ui.platform.ClipboardManager
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.ui.unit.IntOffset
import kotlin.math.roundToInt
import com.littlegig.app.presentation.tickets.TicketsViewModel
import com.littlegig.app.data.model.Ticket
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.animation.core.animateFloatAsState
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.littlegig.app.presentation.components.*
import com.littlegig.app.presentation.theme.*
import androidx.compose.material3.ExperimentalMaterial3Api
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
import com.littlegig.app.data.model.TypingIndicator as ModelTypingIndicator

@OptIn(ExperimentalMaterial3Api::class)
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
    val clipboard: ClipboardManager = LocalClipboardManager.current
    var actionSheetFor by remember { mutableStateOf<com.littlegig.app.data.model.Message?>(null) }
    val imagePicker = rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
        uri?.let { viewModel.uploadAndSendMedia(chatId, it, "image/*") }
    }
    val typing by viewModel.typingUsers.collectAsState()
    var showTicketPicker by remember { mutableStateOf(false) }
    var searchOpen by remember { mutableStateOf(false) }
    var searchQuery by remember { mutableStateOf("") }
    val uiScope = rememberCoroutineScope()
    val matchPositions = remember(messages, searchQuery) {
        if (searchQuery.isBlank()) emptyList() else messages.mapIndexedNotNull { idx, m -> if (m.content.contains(searchQuery, true)) idx else null }
    }
    var currentMatchIndex by remember(searchQuery, matchPositions) { mutableStateOf(if (matchPositions.isNotEmpty()) 0 else -1) }

    LaunchedEffect(chatId) {
        viewModel.loadChat(chatId)
        viewModel.loadMessages(chatId)
        viewModel.observeTyping(chatId)
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
            modifier = Modifier.fillMaxSize()
        ) {
            // Glassmorphic Header
            GlassmorphicChatHeader(
                title = chat?.name ?: "Chat",
                subtitle = chat?.participants?.size?.let { "$it participants" },
                avatarUrl = null, // Would need to get from participants list
                isOnline = true,
                onBackClick = { navController.navigateUp() },
                onMoreClick = { /* Show chat options */ },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            )

            // Messages List
            LazyColumn(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                state = listState,
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(messages) { message ->
                    GlassmorphicChatBubble(
                        message = message,
                        isFromCurrentUser = message.senderId == viewModel.currentUserId,
                        onReact = { messageId, emoji ->
                            viewModel.toggleReaction(chatId, messageId, emoji)
                        },
                        onLongPress = { messageId ->
                            actionSheetFor = messages.find { it.id == messageId }
                        },
                        onMentionClick = { username ->
                            navController.navigate("profile/$username")
                        },
                        onShareTicket = { sharedTicket ->
                            viewModel.redeemTicket(chatId, message.id, sharedTicket.ticketId)
                        },
                        onReplyReferenceClick = { replyToId ->
                            val replyIndex = messages.indexOfFirst { it.id == replyToId }
                            if (replyIndex != -1) {
                                uiScope.launch {
                                    listState.animateScrollToItem(replyIndex)
                                }
                            }
                        },
                        searchQuery = searchQuery
                    )
                }
                
                // Typing indicator
                if (typing.isNotEmpty()) {
                    item {
                        GlassmorphicTypingIndicator(
                            isVisible = true,
                            modifier = Modifier.padding(start = 8.dp, end = 48.dp)
                        )
                    }
                }
            }

            // Glassmorphic Chat Input
            GlassmorphicChatInput(
                message = messageText,
                onMessageChange = { messageText = it },
                onSend = {
                    if (messageText.trim().isNotEmpty()) {
                        viewModel.sendMessage(chatId, messageText.trim())
                        messageText = ""
                    }
                },
                onAttach = { imagePicker.launch("image/*") },
                onShareTicket = { showTicketPicker = true },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            )
        }

        // Search overlay
        if (searchOpen) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Black.copy(alpha = 0.5f))
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                ) {
                    GlassmorphicInputField(
                        value = searchQuery,
                        onValueChange = { searchQuery = it },
                        placeholder = "Search messages...",
                        leadingIcon = Icons.Default.Search,
                        trailingIcon = Icons.Default.Close,
                        onTrailingIconClick = { searchOpen = false },
                        modifier = Modifier.fillMaxWidth()
                    )
                    
                    if (searchQuery.isNotEmpty() && matchPositions.isNotEmpty()) {
                        Spacer(modifier = Modifier.height(16.dp))
                        
                        GlassmorphicCard(
                            modifier = Modifier.fillMaxWidth(),
                            cornerRadius = 16.dp,
                            alpha = if (isDark) 0.8f else 0.9f
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(16.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = "${currentMatchIndex + 1} of ${matchPositions.size}",
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = if (isDark) Color.White else Color.Black
                                )
                                
                                Row {
                                    GlassmorphicButton(
                                        onClick = {
                                            if (currentMatchIndex > 0) {
                                                currentMatchIndex--
                                                val targetIndex = matchPositions[currentMatchIndex]
                                                uiScope.launch {
                                                    listState.animateScrollToItem(targetIndex)
                                                }
                                            }
                                        },
                                        enabled = currentMatchIndex > 0,
                                        cornerRadius = 20.dp
                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.KeyboardArrowUp,
                                            contentDescription = "Previous",
                                            tint = if (currentMatchIndex > 0) LittleGigPrimary else Color.Gray,
                                            modifier = Modifier.size(20.dp)
                                        )
                                    }
                                    
                                    Spacer(modifier = Modifier.width(8.dp))
                                    
                                    GlassmorphicButton(
                                        onClick = {
                                            if (currentMatchIndex < matchPositions.size - 1) {
                                                currentMatchIndex++
                                                val targetIndex = matchPositions[currentMatchIndex]
                                                uiScope.launch {
                                                    listState.animateScrollToItem(targetIndex)
                                                }
                                            }
                                        },
                                        enabled = currentMatchIndex < matchPositions.size - 1,
                                        cornerRadius = 20.dp
                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.KeyboardArrowDown,
                                            contentDescription = "Next",
                                            tint = if (currentMatchIndex < matchPositions.size - 1) LittleGigPrimary else Color.Gray,
                                            modifier = Modifier.size(20.dp)
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
}

@HiltViewModel
class ChatDetailsViewModel @Inject constructor(
    private val chatRepository: com.littlegig.app.data.repository.ChatRepository,
    private val authRepository: com.littlegig.app.data.repository.AuthRepository,
    private val chatMediaService: ChatMediaService,
    private val notificationRepository: com.littlegig.app.data.repository.NotificationRepository
) : ViewModel() { 
    fun deleteMessage(chatId: String, messageId: String) {
        viewModelScope.launch { chatRepository.deleteMessage(chatId, messageId) }
    }

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
    var replyTarget by mutableStateOf<com.littlegig.app.data.model.Message?>(null)
    private var shareTicketChatId: String? = null
    private var subscribedChatId: String? = null

    fun loadChat(chatId: String) {
        // Stream the chat document so header updates (title, announcement, pins)
        viewModelScope.launch {
            try {
                if (subscribedChatId != chatId) {
                    try { notificationRepository.subscribeToChatTopic(chatId) } catch (_: Exception) {}
                    subscribedChatId = chatId
                }
                chatRepository.getChat(chatId).collect { doc ->
                    _chat.value = doc ?: com.littlegig.app.data.model.Chat(id = chatId)
                }
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(error = e.message)
            }
        }
    }

    fun loadMessages(chatId: String) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, error = null)
            com.littlegig.app.utils.PerfMonitor.startTrace("chat_load_messages")
            try {
                var first = true
                chatRepository.getChatMessages(chatId).collect { chatMessages ->
                    _messages.value = chatMessages
                    if (first) {
                        com.littlegig.app.utils.PerfMonitor.putMetric("chat_load_messages", "count", chatMessages.size.toLong())
                        com.littlegig.app.utils.PerfMonitor.stopTrace("chat_load_messages")
                        _uiState.value = _uiState.value.copy(isLoading = false)
                        first = false
                    }
                }
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(isLoading = false, error = e.message)
                com.littlegig.app.utils.PerfMonitor.stopTrace("chat_load_messages")
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
                    val mentions = extractMentions(content)
                    val message = com.littlegig.app.data.model.Message(
                        content = content,
                        senderId = currentUser.id,
                        senderName = currentUser.displayName,
                        senderImageUrl = currentUser.profileImageUrl,
                        messageType = com.littlegig.app.data.model.MessageType.TEXT,
                        timestamp = System.currentTimeMillis(),
                        readBy = emptyList(),
                        mentions = mentions,
                        replyToMessageId = replyTarget?.id
                    )
                    chatRepository.sendMessage(chatId, message)
                    replyTarget = null
                }
            } catch (e: Exception) { _uiState.value = _uiState.value.copy(error = e.message) }
        }
    }

    private fun extractMentions(text: String): List<String> {
        val regex = Regex("@([A-Za-z0-9_]{2,30})")
        return regex.findAll(text).map { it.groupValues[1] }.toList()
    }

    fun toggleReaction(chatId: String, messageId: String, reaction: String) {
        val me = currentUserId ?: return
        viewModelScope.launch {
            try { chatRepository.toggleReaction(chatId, messageId, me, reaction) } catch (e: Exception) {
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

    fun shareTicketToChat(chatId: String, ticket: com.littlegig.app.data.model.Ticket) {
        viewModelScope.launch {
            try {
                val me = currentUserId ?: return@launch
                val shared = com.littlegig.app.data.model.SharedTicket(
                    ticketId = ticket.id,
                    eventName = ticket.eventTitle,
                    eventImageUrl = ticket.eventImageUrl,
                    ticketType = "General",
                    price = ticket.totalAmount,
                    isRedeemed = false
                )
                val message = com.littlegig.app.data.model.Message(
                    senderId = me,
                    senderName = authRepository.currentUser.value?.displayName ?: "",
                    senderImageUrl = authRepository.currentUser.value?.profileImageUrl ?: "",
                    content = "Shared a ticket",
                    messageType = com.littlegig.app.data.model.MessageType.TICKET_SHARE,
                    sharedTicket = shared,
                    timestamp = System.currentTimeMillis()
                )
                chatRepository.sendMessage(chatId, message)
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

    fun markAsReadIfNeeded(chatId: String, message: com.littlegig.app.data.model.Message) {
        val me = currentUserId ?: return
        if (!message.readBy.contains(me)) {
            viewModelScope.launch { chatRepository.markMessageAsRead(chatId, message.id, me) }
        }
    }

    fun markAsDeliveredIfNeeded(chatId: String, message: com.littlegig.app.data.model.Message) {
        val me = currentUserId ?: return
        if (!message.deliveredTo.contains(me)) {
            viewModelScope.launch { chatRepository.markMessageDelivered(chatId, message.id, me) }
        }
    }

    fun chooseReplyTarget(message: com.littlegig.app.data.model.Message) { replyTarget = message }
    fun clearReplyTarget() { replyTarget = null }

    override fun onCleared() {
        super.onCleared()
        subscribedChatId?.let { cid ->
            viewModelScope.launch { try { notificationRepository.unsubscribeFromChatTopic(cid) } catch (_: Exception) {} }
        }
    }
}

data class ChatDetailsUiState(
    val isLoading: Boolean = false,
    val error: String? = null
) 