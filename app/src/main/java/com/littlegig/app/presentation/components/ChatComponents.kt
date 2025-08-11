package com.littlegig.app.presentation.components

import androidx.compose.animation.core.*
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.filled.AttachFile
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.littlegig.app.data.model.*
import com.littlegig.app.presentation.theme.*
import androidx.compose.foundation.clickable
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.ui.draw.alpha
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.tween
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.foundation.gestures.detectTapGestures

// Unique LittleGig Chat Bubble with Neumorphic Design
@Composable
@OptIn(ExperimentalMaterial3Api::class)
fun NeumorphicChatBubble(
    message: Message,
    isFromCurrentUser: Boolean,
    onReact: (messageId: String, emoji: String) -> Unit,
    onMentionClick: (String) -> Unit = {},
    onShareTicket: (SharedTicket) -> Unit,
    onReplyReferenceClick: (String) -> Unit = {},
    searchQuery: String? = null,
    modifier: Modifier = Modifier
) {
    val isDark = isSystemInDarkTheme()
    var showReactions by remember { mutableStateOf(false) }
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    
    Column(
        modifier = modifier.fillMaxWidth(),
        horizontalAlignment = if (isFromCurrentUser) Alignment.End else Alignment.Start
    ) {
        // Message bubble
        AdvancedGlassmorphicCard(
            modifier = Modifier
                .padding(
                    start = if (isFromCurrentUser) 48.dp else 8.dp,
                    end = if (isFromCurrentUser) 8.dp else 48.dp,
                    top = 4.dp,
                    bottom = 4.dp
                )
                .pointerInput(message.id) {
                    detectTapGestures(
                        onDoubleTap = { onReact(message.id, "❤️") }
                    )
                }
        ) {
            Column(
                modifier = Modifier
                    .padding(8.dp)
            ) {
                // Inline reply reference (if any)
                if (!message.replyToMessageId.isNullOrBlank() && message.replyPreview != null) {
                    Surface(
                        onClick = { onReplyReferenceClick(message.replyToMessageId!!) },
                        shape = RoundedCornerShape(8.dp),
                        color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.08f),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 6.dp)
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 6.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(Icons.Default.Reply, contentDescription = null, tint = LittleGigPrimary, modifier = Modifier.size(16.dp))
                            Spacer(Modifier.width(6.dp))
                            Column(Modifier.weight(1f)) {
                                Text(message.replyPreview.senderName, style = MaterialTheme.typography.labelSmall, color = LittleGigPrimary)
                                Text(message.replyPreview.snippet, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurface, maxLines = 1, overflow = TextOverflow.Ellipsis)
                            }
                        }
                    }
                }
                // Message content based on type
                when (message.messageType) {
                    MessageType.TEXT -> {
                        val parts = remember(message.content) {
                            val regex = Regex("@([A-Za-z0-9_]{2,30})")
                            val result = mutableListOf<Pair<String, Boolean>>()
                            var lastIndex = 0
                            regex.findAll(message.content).forEach { m ->
                                if (m.range.first > lastIndex) {
                                    result.add(message.content.substring(lastIndex, m.range.first) to false)
                                }
                                result.add("@" + m.groupValues[1] to true)
                                lastIndex = m.range.last + 1
                            }
                            if (lastIndex < message.content.length) {
                                result.add(message.content.substring(lastIndex) to false)
                            }
                            result.toList()
                        }
                        Row {
                            parts.forEach { (seg, isMention) ->
                                if (isMention) {
                                    Text(
                                        text = seg,
                                        style = MaterialTheme.typography.bodyMedium,
                                        color = LittleGigPrimary,
                                        modifier = Modifier
                                            .padding(end = 2.dp)
                                            .let { it }
                                            .then(Modifier)
                                            .clickable { onMentionClick(seg.removePrefix("@")) }
                                    )
                                } else {
                                    val annotated = if (!searchQuery.isNullOrBlank()) {
                                        buildAnnotatedString {
                                            val lower = seg.lowercase()
                                            val q = searchQuery!!.lowercase()
                                            var idx = 0
                                            while (idx < seg.length) {
                                                val hit = lower.indexOf(q, idx)
                                                if (hit < 0) {
                                                    append(seg.substring(idx))
                                                    break
                                                }
                                                append(seg.substring(idx, hit))
                                                withStyle(SpanStyle(background = LittleGigPrimary.copy(alpha = 0.25f), color = MaterialTheme.colorScheme.onSurface)) {
                                                    append(seg.substring(hit, hit + q.length))
                                                }
                                                idx = hit + q.length
                                            }
                                        }
                                    } else buildAnnotatedString { append(seg) }
                                    Text(
                                        text = annotated,
                                        style = MaterialTheme.typography.bodyMedium,
                                        color = MaterialTheme.colorScheme.onSurface,
                                        maxLines = Int.MAX_VALUE,
                                        overflow = TextOverflow.Clip
                                    )
                                }
                            }
                        }
                    }
                    MessageType.IMAGE -> {
                        AsyncImage(
                            model = message.mediaUrls.firstOrNull(),
                            contentDescription = null,
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(200.dp)
                                .clip(RoundedCornerShape(12.dp)),
                            contentScale = ContentScale.Crop
                        )
                    }
                    MessageType.VIDEO -> {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(200.dp)
                                .clip(RoundedCornerShape(12.dp))
                                .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.1f)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.PlayArrow,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.onSurface,
                                modifier = Modifier.size(48.dp)
                            )
                        }
                    }
                    MessageType.TICKET_SHARE -> {
                        message.sharedTicket?.let { ticket ->
                            NeumorphicTicketShareCard(
                                ticket = ticket,
                                onRedeem = { onShareTicket(ticket) }
                            )
                        }
                    }
                    MessageType.LOCATION -> {
                        // Location message would go here
                        Text(
                            text = "📍 Location shared",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    }
                    MessageType.AUDIO -> {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(60.dp)
                                .clip(RoundedCornerShape(12.dp))
                                .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.1f)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.PlayArrow,
                                contentDescription = "Audio message",
                                tint = MaterialTheme.colorScheme.onSurface,
                                modifier = Modifier.size(32.dp)
                            )
                        }
                    }
                    MessageType.FILE -> {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(60.dp)
                                .clip(RoundedCornerShape(12.dp))
                                .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.1f)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.AttachFile,
                                contentDescription = "File attachment",
                                tint = MaterialTheme.colorScheme.onSurface,
                                modifier = Modifier.size(32.dp)
                            )
                        }
                    }
                    MessageType.EVENT_SHARE -> {
                        val title = message.metadata["eventTitle"] ?: message.content.ifBlank { "Event" }
                        val image = message.metadata["imageUrl"]
                        val date = message.metadata["dateMillis"]?.toLongOrNull()
                        NeumorphicEventShareCard(
                            title = title,
                            imageUrl = image,
                            dateTime = date,
                            onOpen = {}
                        )
                    }
                    MessageType.SYSTEM -> {
                        Text(
                            text = message.content,
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            modifier = Modifier.fillMaxWidth(),
                            textAlign = TextAlign.Center
                        )
                    }
                }
                
                // Message metadata
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = formatMessageTime(message.timestamp),
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    if (message.reactions.isNotEmpty()) {
                        val grouped = message.reactions.values.groupingBy { it }.eachCount()
                        grouped.entries.sortedByDescending { it.value }.take(3).forEach { (emoji, count) ->
                            AssistChip(onClick = {}, label = { Text("$emoji $count") })
                        }
                    }
                    
                    if (isFromCurrentUser) {
                        val delivered = message.deliveredTo.isNotEmpty()
                        val read = message.readBy.isNotEmpty()
                        if (read) {
                            Icon(
                                imageVector = Icons.Default.DoneAll,
                                contentDescription = "Read",
                                tint = LittleGigPrimary,
                                modifier = Modifier.size(16.dp)
                            )
                        } else if (delivered) {
                            Icon(
                                imageVector = Icons.Default.Done,
                                contentDescription = "Delivered",
                                tint = MaterialTheme.colorScheme.onSurfaceVariant,
                                modifier = Modifier.size(16.dp)
                            )
                        } else {
                            Icon(
                                imageVector = Icons.Default.Schedule,
                                contentDescription = "Sending",
                                tint = MaterialTheme.colorScheme.onSurfaceVariant,
                                modifier = Modifier.size(16.dp)
                            )
                        }
                    }
                    if (!isFromCurrentUser) {
                        IconButton(
                            onClick = { onReact(message.id, "❤️") },
                            modifier = Modifier.size(24.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Favorite,
                                contentDescription = "Like",
                                tint = MaterialTheme.colorScheme.onSurfaceVariant,
                                modifier = Modifier.size(16.dp)
                            )
                        }
                        IconButton(onClick = { showReactions = true }, modifier = Modifier.size(24.dp)) {
                            Icon(Icons.Default.AddReaction, contentDescription = "React", tint = MaterialTheme.colorScheme.onSurfaceVariant, modifier = Modifier.size(16.dp))
                        }
                    }
                }
            }
        }
        
        // Sender info (only for other users)
        if (!isFromCurrentUser) {
            Row(
                modifier = Modifier.padding(start = 12.dp, top = 4.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                AsyncImage(
                    model = message.senderImageUrl,
                    contentDescription = null,
                    modifier = Modifier
                        .size(24.dp)
                        .clip(CircleShape),
                    contentScale = ContentScale.Crop
                )
                
                Spacer(modifier = Modifier.width(8.dp))
                
                Text(
                    text = message.senderName,
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
    if (showReactions) {
        ModalBottomSheet(onDismissRequest = { showReactions = false }, sheetState = sheetState) {
            Row(Modifier.fillMaxWidth().padding(16.dp), horizontalArrangement = Arrangement.SpaceEvenly) {
                listOf("❤️","😄","🔥","👍","👏").forEach { emoji ->
                    Text(text = emoji, style = MaterialTheme.typography.headlineSmall, modifier = Modifier
                        .padding(8.dp)
                        .clickable {
                            onReact(message.id, emoji)
                            showReactions = false
                        })
                }
            }
        }
    }
}

@Composable
fun NeumorphicEventShareCard(
    title: String,
    imageUrl: String?,
    dateTime: Long?,
    onOpen: () -> Unit
) {
    AdvancedNeumorphicCard {
        Row(Modifier.padding(12.dp), verticalAlignment = Alignment.CenterVertically) {
            AsyncImage(
                model = imageUrl,
                contentDescription = null,
                modifier = Modifier.size(56.dp).clip(RoundedCornerShape(8.dp)),
                contentScale = ContentScale.Crop
            )
            Spacer(Modifier.width(12.dp))
            Column(Modifier.weight(1f)) {
                Text(title, style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurface)
                if (dateTime != null) {
                    Text(
                        text = java.text.SimpleDateFormat("MMM d, h:mm a").format(java.util.Date(dateTime)),
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
            OutlinedButton(onClick = onOpen, shape = RoundedCornerShape(12.dp)) {
                Icon(Icons.Default.Event, contentDescription = null)
                Spacer(Modifier.width(6.dp))
                Text("Open")
            }
        }
    }
}

// Beautiful Ticket Share Card
@Composable
fun NeumorphicTicketShareCard(
    ticket: SharedTicket,
    onRedeem: () -> Unit,
    modifier: Modifier = Modifier
) {
    val isDark = isSystemInDarkTheme()
    
    AdvancedNeumorphicCard(
        modifier = modifier
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Event image
            AsyncImage(
                model = ticket.eventImageUrl,
                contentDescription = null,
                modifier = Modifier
                    .size(60.dp)
                    .clip(RoundedCornerShape(8.dp)),
                contentScale = ContentScale.Crop
            )
            
            Spacer(modifier = Modifier.width(12.dp))
            
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = ticket.eventName,
                    style = MaterialTheme.typography.bodyLarge.copy(
                        fontWeight = FontWeight.Bold
                    ),
                    color = MaterialTheme.colorScheme.onSurface
                )
                
                Text(
                    text = ticket.ticketType,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                
                Text(
                    text = "KSH ${ticket.price}",
                    style = MaterialTheme.typography.bodyMedium.copy(
                        fontWeight = FontWeight.SemiBold
                    ),
                    color = LittleGigPrimary
                )
            }
            
            Spacer(modifier = Modifier.width(12.dp))
            
            // Redeem button
            if (!ticket.isRedeemed) {
                Surface(
                    onClick = onRedeem,
                    modifier = Modifier
                        .clip(RoundedCornerShape(16.dp)),
                    color = LittleGigPrimary.copy(alpha = 0.1f),
                    border = BorderStroke(
                        width = 1.dp,
                        color = LittleGigPrimary
                    )
                ) {
                    Text(
                        text = "Redeem",
                        modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
                        style = MaterialTheme.typography.labelMedium.copy(
                            fontWeight = FontWeight.Bold
                        ),
                        color = LittleGigPrimary
                    )
                }
            } else {
                Text(
                    text = "Redeemed",
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}

@Composable
fun TicketShareBubble(
    messageId: String,
    ticket: SharedTicket,
    onRedeem: (messageId: String, ticketId: String) -> Unit
) {
    NeumorphicTicketShareCard(ticket = ticket, onRedeem = { onRedeem(messageId, ticket.ticketId) })
}

@Composable
fun ChatDateHeader(dateText: String, modifier: Modifier = Modifier) {
    Box(modifier = modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
        AdvancedNeumorphicCard {
            Text(
                text = dateText,
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
            )
        }
    }
}

@Composable
fun SendingShimmerBubble(modifier: Modifier = Modifier) {
    val alphaAnim = rememberInfiniteTransition(label = "sending_transition").animateFloat(
        initialValue = 0.3f,
        targetValue = 0.9f,
        animationSpec = infiniteRepeatable(animation = tween(800), repeatMode = RepeatMode.Reverse),
        label = "sending_alpha"
    )
    AdvancedGlassmorphicCard(modifier = modifier) {
        Box(Modifier.size(width = 140.dp, height = 20.dp).alpha(alphaAnim.value)) {}
    }
}

// Unique Typing Indicator with Multi-colored Dots
@Composable
fun NeumorphicTypingIndicator(
    typingUsers: List<TypingIndicator>,
    modifier: Modifier = Modifier
) {
    if (typingUsers.isEmpty()) return
    
    val isDark = isSystemInDarkTheme()
    
    AdvancedGlassmorphicCard(
        modifier = modifier.padding(8.dp)
    ) {
        Row(
            modifier = Modifier.padding(12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            // Animated avatar pulses
            typingUsers.take(3).forEachIndexed { idx, user ->
                val pulse = rememberInfiniteTransition(label = "typing_avatar_$idx").animateFloat(
                    initialValue = 0.85f, targetValue = 1.05f,
                    animationSpec = infiniteRepeatable(animation = tween(700, delayMillis = idx * 120), repeatMode = RepeatMode.Reverse),
                    label = "typing_avatar_scale_$idx"
                )
                Box(
                    modifier = Modifier
                        .size(18.dp)
                        .clip(CircleShape)
                        .background(LittleGigPrimary.copy(alpha = 0.25f))
                        .graphicsLayer(scaleX = pulse.value, scaleY = pulse.value),
                    contentAlignment = Alignment.Center
                ) {
                    Text(user.userName.firstOrNull()?.uppercase() ?: "?", style = MaterialTheme.typography.labelSmall)
                }
            }
            // Names
            Text(
                text = typingUsers.joinToString(", ") { it.userName },
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

// Glassmorphic Chat Input
@Composable
fun NeumorphicChatInput(
    message: String,
    onMessageChange: (String) -> Unit,
    onSendMessage: () -> Unit,
    onAttachMedia: () -> Unit,
    onShareTicket: () -> Unit,
    modifier: Modifier = Modifier
) {
    val isDark = isSystemInDarkTheme()
    
    AdvancedGlassmorphicCard(
        modifier = modifier
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Media attachment button
            IconButton(
                onClick = onAttachMedia,
                modifier = Modifier.size(40.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.AttachFile,
                    contentDescription = "Attach",
                    tint = LittleGigPrimary
                )
            }
            
            // Ticket share button
            IconButton(
                onClick = onShareTicket,
                modifier = Modifier.size(40.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Receipt,
                    contentDescription = "Share Ticket",
                    tint = LittleGigPrimary
                )
            }
            
            Spacer(modifier = Modifier.width(8.dp))
            
            // Message input
            OutlinedTextField(
                value = message,
                onValueChange = onMessageChange,
                placeholder = { 
                    Text(
                        "Type a message...",
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                },
                modifier = Modifier
                    .weight(1f)
                    .heightIn(min = 40.dp, max = 120.dp),
                singleLine = false,
                maxLines = 4,
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = Color.Transparent,
                    unfocusedBorderColor = Color.Transparent,
                    focusedContainerColor = Color.Transparent,
                    unfocusedContainerColor = Color.Transparent
                )
            )
            
            Spacer(modifier = Modifier.width(8.dp))
            
            // Send button
            Surface(
                onClick = onSendMessage,
                modifier = Modifier.size(40.dp),
                shape = CircleShape,
                color = if (message.isNotBlank()) LittleGigPrimary else MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.3f)
            ) {
                Icon(
                    imageVector = Icons.Default.Send,
                    contentDescription = "Send",
                    tint = if (message.isNotBlank()) Color.White else MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(8.dp)
                )
            }
        }
    }
}

@Composable
fun ChatBubbleSkeleton(isMe: Boolean, modifier: Modifier = Modifier) {
    AdvancedGlassmorphicCard(modifier = modifier.padding(
        start = if (isMe) 48.dp else 8.dp,
        end = if (isMe) 8.dp else 48.dp,
        top = 4.dp,
        bottom = 4.dp
    )) {
        Box(Modifier.size(width = 160.dp, height = 18.dp)) { }
    }
}

private fun formatMessageTime(timestamp: Long): String {
    val now = System.currentTimeMillis()
    val diff = now - timestamp
    
    return when {
        diff < 60000 -> "Just now"
        diff < 3600000 -> "${diff / 60000}m ago"
        diff < 86400000 -> "${diff / 3600000}h ago"
        else -> "${diff / 86400000}d ago"
    }
} 