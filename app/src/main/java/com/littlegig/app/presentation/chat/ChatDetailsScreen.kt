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
import com.littlegig.app.presentation.components.ChatDateHeader
import com.littlegig.app.presentation.components.SendingShimmerBubble
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
                    IconButton(onClick = { searchOpen = !searchOpen }) {
                        Icon(Icons.Default.Search, contentDescription = "Search in chat", tint = LittleGigPrimary)
                    }
                }
            }
            if (searchOpen) {
                val uiScope = rememberCoroutineScope()
                Row(Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 8.dp), verticalAlignment = Alignment.CenterVertically) {
                    OutlinedTextField(
                        value = searchQuery,
                        onValueChange = { searchQuery = it },
                        placeholder = { Text("Find in chat...") },
                        modifier = Modifier.weight(1f)
                    )
                    Spacer(Modifier.width(8.dp))
                    Text(text = if (matchPositions.isNotEmpty()) "${currentMatchIndex + 1}/${matchPositions.size}" else "0/0", style = MaterialTheme.typography.labelMedium)
                    IconButton(onClick = {
                        if (matchPositions.isNotEmpty()) {
                            currentMatchIndex = (currentMatchIndex - 1 + matchPositions.size) % matchPositions.size
                            val pos = matchPositions[currentMatchIndex]
                            uiScope.launch { listState.animateScrollToItem(pos) }
                        }
                    }) { Icon(Icons.Default.ArrowUpward, contentDescription = "Prev") }
                    IconButton(onClick = {
                        if (matchPositions.isNotEmpty()) {
                            currentMatchIndex = (currentMatchIndex + 1) % matchPositions.size
                            val pos = matchPositions[currentMatchIndex]
                            uiScope.launch { listState.animateScrollToItem(pos) }
                        }
                    }) { Icon(Icons.Default.ArrowDownward, contentDescription = "Next") }
                }
            }

            var lastDate: String? = null
            LazyColumn(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
                    .padding(bottom = 96.dp),
                state = listState,
                verticalArrangement = Arrangement.spacedBy(6.dp),
                contentPadding = PaddingValues(top = 12.dp, bottom = 12.dp)
            ) {
                items(messages) { message ->
                    val dateText = java.text.SimpleDateFormat("MMM d, yyyy").format(java.util.Date(message.timestamp))
                    if (dateText != lastDate) {
                        lastDate = dateText
                        ChatDateHeader(dateText = dateText, modifier = Modifier.padding(vertical = 8.dp))
                    }
                    // mark last incoming as read
                    if (message.senderId != viewModel.currentUserId) viewModel.markAsReadIfNeeded(chatId, message)
                    if (message.senderId != viewModel.currentUserId) viewModel.markAsDeliveredIfNeeded(chatId, message)
                    val isTicketShare = message.messageType == com.littlegig.app.data.model.MessageType.TICKET_SHARE && message.sharedTicket != null
                    var offsetX by remember(message.id) { mutableStateOf(0f) }
                    val animatedOffset by animateFloatAsState(targetValue = offsetX, label = "swipe_offset")
                    Box(modifier = Modifier
                        .offset { IntOffset(animatedOffset.roundToInt(), 0) }
                        .pointerInput(Unit) {
                            detectDragGestures(
                                onDragEnd = {
                                    if (offsetX > 120f) {
                                        viewModel.chooseReplyTarget(message)
                                    }
                                    offsetX = 0f
                                },
                                onDrag = { change, dragAmount ->
                                    change.consume()
                                    val (dx, _) = dragAmount
                                    if (dx > 0) offsetX = (offsetX + dx).coerceAtMost(180f)
                                }
                            )
                        }
                    ) {
                    if (isTicketShare) {
                        TicketShareBubble(
                            messageId = message.id,
                            ticket = message.sharedTicket!!,
                            onRedeem = { mid, tid -> viewModel.redeemTicket(chatId, mid, tid) }
                        )
                    } else {
                        NeumorphicChatBubble(
                            message = message,
                            isFromCurrentUser = message.senderId == viewModel.currentUserId,
                            onReact = { msgId, emoji -> viewModel.toggleReaction(chatId, msgId, emoji) },
                            onMentionClick = { username -> navController.navigate("profile/$username") },
                            onShareTicket = { /* no-op */ },
                            onReplyReferenceClick = { refId ->
                                val index = messages.indexOfFirst { it.id == refId }
                                if (index >= 0) {
                                    uiScope.launch { listState.animateScrollToItem(index) }
                                }
                            },
                            searchQuery = if (searchOpen) searchQuery else null,
                            modifier = Modifier
                                .fillMaxWidth()
                                .pointerInput(Unit) {
                                    detectTapGestures(onLongPress = {
                                        actionSheetFor = message
                                    })
                                }
                        )
                        // Remove pending shimmer to avoid confusing clocks; messages are persisted immediately
                    }
                    }
                }
            }

            // typing indicator
            if (typing.isNotEmpty()) {
                val indicators = typing.map { userName -> ModelTypingIndicator(userName = userName, isTyping = true) }
                NeumorphicTypingIndicator(typingUsers = indicators, modifier = Modifier.align(Alignment.Start))
            }

            // reply preview above input
            viewModel.replyTarget?.let { target ->
                AdvancedGlassmorphicCard(
                    modifier = Modifier
                        .padding(horizontal = 16.dp)
                        .fillMaxWidth()
                ) {
                    Row(modifier = Modifier.padding(12.dp), verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.Reply, contentDescription = null, tint = LittleGigPrimary)
                        Spacer(Modifier.width(8.dp))
                        Column(Modifier.weight(1f)) {
                            Text(text = target.senderName, style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                            Text(text = target.content.take(80), style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurface)
                        }
                        IconButton(onClick = { viewModel.clearReplyTarget() }) { Icon(Icons.Default.Close, contentDescription = null) }
                    }
                }
                Spacer(Modifier.height(8.dp))
            }

            if (actionSheetFor != null) {
                val m = actionSheetFor!!
                ModalBottomSheet(onDismissRequest = { actionSheetFor = null }) {
                    Column(Modifier.fillMaxWidth().padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        TextButton(onClick = { viewModel.chooseReplyTarget(m); actionSheetFor = null }) { Text("Reply") }
                        TextButton(onClick = { clipboard.setText(AnnotatedString(m.content)); actionSheetFor = null }) { Text("Copy") }
                        TextButton(onClick = { /* forward placeholder: navigate to chat picker */ actionSheetFor = null }) { Text("Forward") }
                        if (m.senderId == viewModel.currentUserId) {
                            TextButton(onClick = { viewModel.deleteMessage(chatId, m.id); actionSheetFor = null }) { Text("Delete", color = MaterialTheme.colorScheme.error) }
                        }
                    }
                }
            }

            // Chat input anchored above the floating bottom nav pill (3dp gap)
            NeumorphicChatInput(
                message = messageText,
                onMessageChange = {
                    messageText = it
                    // typing indicator updates could be triggered here
                },
                onSendMessage = {
                    if (messageText.isNotBlank()) {
                        viewModel.sendMessage(chatId, messageText)
                        messageText = ""
                    }
                },
                onAttachMedia = { imagePicker.launch("image/*") },
                onShareTicket = { showTicketPicker = true },
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .padding(start = 16.dp, end = 16.dp, top = 8.dp, bottom = 3.dp + 88.dp)
            )

            if (showTicketPicker) {
                val ticketsVm: TicketsViewModel = hiltViewModel()
                val ticketsState by ticketsVm.uiState.collectAsState()
                ModalBottomSheet(onDismissRequest = { showTicketPicker = false }) {
                    Column(Modifier.fillMaxWidth().padding(16.dp)) {
                        Text("Share a ticket", style = MaterialTheme.typography.titleMedium)
                        Spacer(Modifier.height(8.dp))
                        ticketsState.tickets.forEach { t: Ticket ->
                            AdvancedNeumorphicCard {
                                Row(Modifier.fillMaxWidth().padding(12.dp), verticalAlignment = Alignment.CenterVertically) {
                                    Column(Modifier.weight(1f)) {
                                        Text(t.eventTitle, style = MaterialTheme.typography.bodyMedium)
                                        Text("${t.status}", style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                                    }
                                    TextButton(onClick = {
                                        viewModel.shareTicketToChat(chatId, t)
                                        showTicketPicker = false
                                    }) { Text("Share") }
                                }
                            }
                            Spacer(Modifier.height(8.dp))
                        }
                        if (ticketsState.tickets.isEmpty()) {
                            ChatBubbleSkeleton(isMe = true)
                            ChatBubbleSkeleton(isMe = false)
                        }
                    }
                }
            }
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