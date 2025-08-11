package com.littlegig.app.presentation.components

import androidx.compose.animation.core.*
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.littlegig.app.data.model.*
import com.littlegig.app.presentation.theme.*
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.ui.draw.alpha
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.tween
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.foundation.gestures.detectTapGestures
import java.text.SimpleDateFormat
import java.util.*

// 🌟 GLASSMORPHIC CHAT COMPONENTS 🌟
// True glassmorphism with transparency, blur, and vibrant colors

@Composable
@OptIn(ExperimentalMaterial3Api::class)
fun GlassmorphicChatBubble(
    message: Message,
    isFromCurrentUser: Boolean,
    onReact: (messageId: String, emoji: String) -> Unit,
    onLongPress: (messageId: String) -> Unit = {},
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
        // Message bubble with glassmorphic design
        GlassmorphicCard(
            modifier = Modifier
                .padding(
                    start = if (isFromCurrentUser) 48.dp else 8.dp,
                    end = if (isFromCurrentUser) 8.dp else 48.dp,
                    top = 4.dp,
                    bottom = 4.dp
                )
                .pointerInput(message.id) {
                    detectTapGestures(
                        onDoubleTap = { onReact(message.id, "❤️") },
                        onLongPress = { onLongPress(message.id) }
                    )
                },
            cornerRadius = 20.dp,
            alpha = if (isFromCurrentUser) {
                if (isDark) 0.8f else 0.9f
            } else {
                if (isDark) 0.6f else 0.7f
            },
            borderColor = if (isFromCurrentUser) {
                LittleGigPrimary.copy(alpha = 0.3f)
            } else {
                if (isDark) Color.White.copy(alpha = 0.1f) else Color.Black.copy(alpha = 0.05f)
            }
        ) {
            Column(
                modifier = Modifier.padding(12.dp)
            ) {
                // Inline reply reference (if any)
                if (!message.replyToMessageId.isNullOrBlank() && message.replyPreview != null) {
                    GlassmorphicCard(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 8.dp)
                            .clickable { onReplyReferenceClick(message.replyToMessageId!!) },
                        cornerRadius = 12.dp,
                        alpha = if (isDark) 0.4f else 0.5f,
                        borderColor = if (isDark) Color.White.copy(alpha = 0.1f) else Color.Black.copy(alpha = 0.05f)
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = Icons.Default.Reply,
                                contentDescription = null,
                                tint = if (isDark) Color.White.copy(alpha = 0.7f) else Color.Black.copy(alpha = 0.5f),
                                modifier = Modifier.size(16.dp)
                            )
                            
                            Spacer(modifier = Modifier.width(8.dp))
                            
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = message.replyPreview.senderName,
                                    style = MaterialTheme.typography.labelSmall.copy(
                                        fontWeight = FontWeight.SemiBold
                                    ),
                                    color = if (isDark) Color.White.copy(alpha = 0.8f) else Color.Black.copy(alpha = 0.7f)
                                )
                                
                                Text(
                                    text = message.replyPreview.snippet,
                                    style = MaterialTheme.typography.bodySmall,
                                    color = if (isDark) Color.White.copy(alpha = 0.6f) else Color.Black.copy(alpha = 0.5f),
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis
                                )
                            }
                        }
                    }
                }
                
                // Message content
                when (message.messageType) {
                    MessageType.TEXT -> {
                        val highlightedText = if (searchQuery != null && searchQuery.isNotBlank()) {
                            buildAnnotatedString {
                                val text = message.content
                                val query = searchQuery.lowercase()
                                val startIndex = text.lowercase().indexOf(query)
                                
                                if (startIndex != -1) {
                                    append(text.substring(0, startIndex))
                                    withStyle(SpanStyle(
                                        background = LittleGigPrimary.copy(alpha = 0.3f),
                                        color = LittleGigPrimary
                                    )) {
                                        append(text.substring(startIndex, startIndex + query.length))
                                    }
                                    append(text.substring(startIndex + query.length))
                                } else {
                                    append(text)
                                }
                            }
                        } else {
                            buildAnnotatedString { append(message.content) }
                        }
                        
                        Text(
                            text = highlightedText,
                            style = MaterialTheme.typography.bodyMedium,
                            color = if (isDark) Color.White else Color.Black
                        )
                    }
                    
                    MessageType.IMAGE -> {
                        AsyncImage(
                            model = message.content,
                            contentDescription = "Image message",
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(200.dp)
                                .clip(RoundedCornerShape(12.dp)),
                            contentScale = ContentScale.Crop
                        )
                    }
                    
                    MessageType.TICKET_SHARE -> {
                        val sharedTicket = message.sharedTicket
                        if (sharedTicket != null) {
                            GlassmorphicTicketShareBubble(
                                sharedTicket = sharedTicket,
                                onShareTicket = onShareTicket
                            )
                        }
                    }
                    
                    else -> {
                        Text(
                            text = message.content,
                            style = MaterialTheme.typography.bodyMedium,
                            color = if (isDark) Color.White else Color.Black
                        )
                    }
                }
                
                // Message metadata
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 4.dp),
                    horizontalArrangement = if (isFromCurrentUser) Arrangement.End else Arrangement.Start,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    val formattedTime = SimpleDateFormat("HH:mm", Locale.getDefault()).format(Date(message.timestamp))
                    Text(
                        text = formattedTime,
                        style = MaterialTheme.typography.labelSmall,
                        color = if (isDark) Color.White.copy(alpha = 0.5f) else Color.Black.copy(alpha = 0.4f)
                    )
                    
                    if (isFromCurrentUser) {
                        Spacer(modifier = Modifier.width(4.dp))
                        
                        // Delivery status
                        when (message.status) {
                            MessageStatus.READ -> {
                                Icon(
                                    imageVector = Icons.Default.DoneAll,
                                    contentDescription = "Read",
                                    tint = LittleGigPrimary,
                                    modifier = Modifier.size(16.dp)
                                )
                            }
                            MessageStatus.DELIVERED -> {
                                Icon(
                                    imageVector = Icons.Default.DoneAll,
                                    contentDescription = "Delivered",
                                    tint = if (isDark) Color.White.copy(alpha = 0.5f) else Color.Black.copy(alpha = 0.4f),
                                    modifier = Modifier.size(16.dp)
                                )
                            }
                            else -> {
                                Icon(
                                    imageVector = Icons.Default.Done,
                                    contentDescription = "Sent",
                                    tint = if (isDark) Color.White.copy(alpha = 0.5f) else Color.Black.copy(alpha = 0.4f),
                                    modifier = Modifier.size(16.dp)
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
fun GlassmorphicTicketShareBubble(
    sharedTicket: SharedTicket,
    onShareTicket: (SharedTicket) -> Unit
) {
    val isDark = isSystemInDarkTheme()
    
    GlassmorphicCard(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onShareTicket(sharedTicket) },
        cornerRadius = 16.dp,
        alpha = if (isDark) 0.7f else 0.8f,
        borderColor = LittleGigPrimary.copy(alpha = 0.3f)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Default.Receipt,
                    contentDescription = null,
                    tint = LittleGigPrimary,
                    modifier = Modifier.size(24.dp)
                )
                
                Spacer(modifier = Modifier.width(12.dp))
                
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = sharedTicket.eventName,
                        style = MaterialTheme.typography.bodyMedium.copy(
                            fontWeight = FontWeight.SemiBold
                        ),
                        color = if (isDark) Color.White else Color.Black
                    )
                    
                    Text(
                        text = "Tap to redeem ticket",
                        style = MaterialTheme.typography.bodySmall,
                        color = if (isDark) Color.White.copy(alpha = 0.7f) else Color.Black.copy(alpha = 0.6f)
                    )
                }
                
                Icon(
                    imageVector = Icons.Default.Redeem,
                    contentDescription = "Redeem",
                    tint = LittleGigPrimary,
                    modifier = Modifier.size(20.dp)
                )
            }
        }
    }
}

@Composable
fun GlassmorphicChatInput(
    message: String,
    onMessageChange: (String) -> Unit,
    onSend: () -> Unit,
    onAttach: () -> Unit,
    onShareTicket: () -> Unit,
    modifier: Modifier = Modifier
) {
    val isDark = isSystemInDarkTheme()
    
    GlassmorphicCard(
        modifier = modifier,
        cornerRadius = 24.dp,
        alpha = if (isDark) 0.7f else 0.8f,
        borderColor = if (isDark) Color.White.copy(alpha = 0.15f) else Color.Black.copy(alpha = 0.08f)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Attach button
            Icon(
                imageVector = Icons.Default.AttachFile,
                contentDescription = "Attach",
                tint = if (isDark) Color.White.copy(alpha = 0.7f) else Color.Black.copy(alpha = 0.5f),
                modifier = Modifier
                    .size(24.dp)
                    .clickable { onAttach() }
            )
            
            Spacer(modifier = Modifier.width(12.dp))
            
            // Share ticket button
            Icon(
                imageVector = Icons.Default.Receipt,
                contentDescription = "Share ticket",
                tint = if (isDark) Color.White.copy(alpha = 0.7f) else Color.Black.copy(alpha = 0.5f),
                modifier = Modifier
                    .size(24.dp)
                    .clickable { onShareTicket() }
            )
            
            Spacer(modifier = Modifier.width(12.dp))
            
            // Message input
            androidx.compose.material3.TextField(
                value = message,
                onValueChange = onMessageChange,
                placeholder = {
                    Text(
                        text = "Type a message...",
                        color = if (isDark) Color.White.copy(alpha = 0.5f) else Color.Black.copy(alpha = 0.4f)
                    )
                },
                colors = androidx.compose.material3.TextFieldDefaults.colors(
                    focusedContainerColor = Color.Transparent,
                    unfocusedContainerColor = Color.Transparent,
                    disabledContainerColor = Color.Transparent,
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent,
                    disabledIndicatorColor = Color.Transparent,
                    focusedTextColor = if (isDark) Color.White else Color.Black,
                    unfocusedTextColor = if (isDark) Color.White else Color.Black
                ),
                modifier = Modifier.weight(1f)
            )
            
            Spacer(modifier = Modifier.width(12.dp))
            
            // Send button
            GlassmorphicButton(
                onClick = onSend,
                enabled = message.trim().isNotEmpty(),
                cornerRadius = 20.dp
            ) {
                Icon(
                    imageVector = Icons.Default.Send,
                    contentDescription = "Send",
                    tint = if (message.trim().isNotEmpty()) {
                        LittleGigPrimary
                    } else {
                        if (isDark) Color.White.copy(alpha = 0.3f) else Color.Black.copy(alpha = 0.2f)
                    },
                    modifier = Modifier.size(20.dp)
                )
            }
        }
    }
}

@Composable
fun GlassmorphicChatHeader(
    title: String,
    subtitle: String? = null,
    avatarUrl: String? = null,
    isOnline: Boolean = false,
    onBackClick: () -> Unit,
    onMoreClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val isDark = isSystemInDarkTheme()
    
    GlassmorphicCard(
        modifier = modifier,
        cornerRadius = 20.dp,
        alpha = if (isDark) 0.7f else 0.8f,
        borderColor = if (isDark) Color.White.copy(alpha = 0.15f) else Color.Black.copy(alpha = 0.08f)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Back button
            Icon(
                imageVector = Icons.Default.ArrowBack,
                contentDescription = "Back",
                tint = if (isDark) Color.White.copy(alpha = 0.8f) else Color.Black.copy(alpha = 0.6f),
                modifier = Modifier
                    .size(24.dp)
                    .clickable { onBackClick() }
            )
            
            Spacer(modifier = Modifier.width(12.dp))
            
            // Avatar
            Box {
                GlassmorphicAvatar(
                    imageUrl = avatarUrl,
                    size = 40.dp
                )
                
                if (isOnline) {
                    Box(
                        modifier = Modifier
                            .align(Alignment.BottomEnd)
                            .size(12.dp)
                            .clip(CircleShape)
                            .background(SuccessGreen)
                            .border(
                                width = 2.dp,
                                color = if (isDark) DarkBackground else LightBackground,
                                shape = CircleShape
                            )
                    )
                }
            }
            
            Spacer(modifier = Modifier.width(12.dp))
            
            // Title and subtitle
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.SemiBold
                    ),
                    color = if (isDark) Color.White else Color.Black
                )
                
                if (subtitle != null) {
                    Text(
                        text = subtitle,
                        style = MaterialTheme.typography.bodySmall,
                        color = if (isDark) Color.White.copy(alpha = 0.7f) else Color.Black.copy(alpha = 0.6f)
                    )
                }
            }
            
            // More options button
            Icon(
                imageVector = Icons.Default.MoreVert,
                contentDescription = "More options",
                tint = if (isDark) Color.White.copy(alpha = 0.8f) else Color.Black.copy(alpha = 0.6f),
                modifier = Modifier
                    .size(24.dp)
                    .clickable { onMoreClick() }
            )
        }
    }
}

@Composable
fun GlassmorphicTypingIndicator(
    isVisible: Boolean,
    modifier: Modifier = Modifier
) {
    if (!isVisible) return
    
    val isDark = isSystemInDarkTheme()
    
    GlassmorphicCard(
        modifier = modifier,
        cornerRadius = 20.dp,
        alpha = if (isDark) 0.6f else 0.7f,
        borderColor = if (isDark) Color.White.copy(alpha = 0.1f) else Color.Black.copy(alpha = 0.05f)
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Typing",
                style = MaterialTheme.typography.bodySmall,
                color = if (isDark) Color.White.copy(alpha = 0.7f) else Color.Black.copy(alpha = 0.6f)
            )
            
            Spacer(modifier = Modifier.width(8.dp))
            
            // Animated dots
            val infiniteTransition = rememberInfiniteTransition(label = "typing")
            val dot1Alpha by infiniteTransition.animateFloat(
                initialValue = 0.3f,
                targetValue = 1f,
                animationSpec = infiniteRepeatable(
                    animation = tween(600, delayMillis = 0),
                    repeatMode = RepeatMode.Reverse
                ),
                label = "dot1"
            )
            
            val dot2Alpha by infiniteTransition.animateFloat(
                initialValue = 0.3f,
                targetValue = 1f,
                animationSpec = infiniteRepeatable(
                    animation = tween(600, delayMillis = 200),
                    repeatMode = RepeatMode.Reverse
                ),
                label = "dot2"
            )
            
            val dot3Alpha by infiniteTransition.animateFloat(
                initialValue = 0.3f,
                targetValue = 1f,
                animationSpec = infiniteRepeatable(
                    animation = tween(600, delayMillis = 400),
                    repeatMode = RepeatMode.Reverse
                ),
                label = "dot3"
            )
            
            Row {
                Box(
                    modifier = Modifier
                        .size(4.dp)
                        .clip(CircleShape)
                        .background(
                            if (isDark) Color.White.copy(alpha = dot1Alpha) 
                            else Color.Black.copy(alpha = dot1Alpha)
                        )
                )
                
                Spacer(modifier = Modifier.width(4.dp))
                
                Box(
                    modifier = Modifier
                        .size(4.dp)
                        .clip(CircleShape)
                        .background(
                            if (isDark) Color.White.copy(alpha = dot2Alpha) 
                            else Color.Black.copy(alpha = dot2Alpha)
                        )
                )
                
                Spacer(modifier = Modifier.width(4.dp))
                
                Box(
                    modifier = Modifier
                        .size(4.dp)
                        .clip(CircleShape)
                        .background(
                            if (isDark) Color.White.copy(alpha = dot3Alpha) 
                            else Color.Black.copy(alpha = dot3Alpha)
                        )
                )
            }
        }
    }
}