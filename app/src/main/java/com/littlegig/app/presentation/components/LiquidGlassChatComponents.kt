package com.littlegig.app.presentation.components

import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.*
import androidx.compose.ui.graphics.*
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import kotlin.math.roundToInt
import com.littlegig.app.presentation.theme.*
import java.text.SimpleDateFormat
import java.util.*

// 🌊 LIQUID GLASS CHAT COMPONENTS 🌊
// Advanced chat components with real-time refraction and liquid effects

@Composable
fun LiquidGlassChatBubble(
    message: String,
    isFromMe: Boolean,
    timestamp: Long,
    modifier: Modifier = Modifier,
    showAvatar: Boolean = true,
    avatarUrl: String? = null,
    isRead: Boolean = false,
    isDelivered: Boolean = false,
    onLongClick: (() -> Unit)? = null,
    onDoubleClick: (() -> Unit)? = null,
    onReply: (() -> Unit)? = null,
    onReact: ((String) -> Unit)? = null,
    reactions: Map<String, String> = emptyMap(),
    isReplyingTo: com.littlegig.app.data.model.Message? = null
) {
    val isDark = isSystemInDarkTheme()
    var showReactions by remember { mutableStateOf(false) }
    var offsetX by remember { mutableStateOf(0f) }
    val animatedOffsetX by animateFloatAsState(
        targetValue = offsetX,
        animationSpec = spring(dampingRatio = Spring.DampingRatioMediumBouncy),
        label = "swipe_offset"
    )
    
    // Swipe to reply gesture
    val swipeableModifier = if (onReply != null) {
        Modifier
            .offset { IntOffset(animatedOffsetX.roundToInt(), 0) }
            .pointerInput(Unit) {
                detectDragGestures(
                    onDragEnd = {
                        if (offsetX < -100f) {
                            onReply()
                        }
                        offsetX = 0f
                    }
                ) { _, dragAmount ->
                    if (dragAmount.x < 0) {
                        offsetX = (offsetX + dragAmount.x).coerceAtMost(0f)
                    }
                }
            }
    } else {
        Modifier
    }
    
    // Double click and long click gestures
    val gestureModifier = Modifier.pointerInput(Unit) {
        detectTapGestures(
            onDoubleTap = { onDoubleClick?.invoke() },
            onLongPress = { onLongClick?.invoke() }
        )
    }
    
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = if (isFromMe) Arrangement.End else Arrangement.Start
    ) {
        if (!isFromMe && showAvatar) {
            LiquidGlassAvatar(
                imageUrl = avatarUrl,
                modifier = Modifier.padding(end = 8.dp),
                size = 36.dp
            )
        }
        
        Column(
            horizontalAlignment = if (isFromMe) Alignment.End else Alignment.Start
        ) {
            // Reply preview
            if (isReplyingTo != null) {
                LiquidGlassCard(
                    modifier = Modifier
                        .widthIn(max = 280.dp)
                        .padding(bottom = 4.dp),
                    cornerRadius = 12.dp,
                    alpha = if (isDark) 0.6f else 0.7f
                ) {
                    Row(
                        modifier = Modifier.padding(8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.Reply,
                            contentDescription = "Replying to",
                            tint = LittleGigPrimary,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = isReplyingTo.content.take(50) + if (isReplyingTo.content.length > 50) "..." else "",
                            style = MaterialTheme.typography.bodySmall,
                            color = if (isDark) Color.White.copy(alpha = 0.7f) else Color.Black.copy(alpha = 0.6f)
                        )
                    }
                }
            }
            
            // Main message bubble
            LiquidGlassCard(
                modifier = swipeableModifier
                    .widthIn(max = 280.dp)
                    .then(gestureModifier),
                cornerRadius = 20.dp,
                alpha = if (isFromMe) {
                    if (isDark) 0.8f else 0.85f
                } else {
                    if (isDark) 0.7f else 0.75f
                },
                borderColor = if (isFromMe) {
                    LittleGigPrimary.copy(alpha = 0.3f)
                } else {
                    if (isDark) Color.White.copy(alpha = 0.15f) else Color.Black.copy(alpha = 0.08f)
                }
            ) {
                Column(
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp)
                ) {
                    Text(
                        text = message,
                        style = MaterialTheme.typography.bodyMedium,
                        color = if (isFromMe) {
                            if (isDark) Color.White else Color.Black
                        } else {
                            if (isDark) Color.White else Color.Black
                        }
                    )
                    
                    Spacer(modifier = Modifier.height(4.dp))
                    
                    // Reactions row
                    if (reactions.isNotEmpty()) {
                        Row(
                            modifier = Modifier.padding(bottom = 4.dp),
                            horizontalArrangement = Arrangement.Start
                        ) {
                            reactions.forEach { (emoji, users) ->
                                if (users.isNotEmpty()) {
                                    LiquidGlassChip(
                                        text = "$emoji ${users.split(",").size}",
                                        onClick = { },
                                        modifier = Modifier.padding(end = 4.dp)
                                    )
                                }
                            }
                        }
                    }
                    
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.End
                    ) {
                        Text(
                            text = SimpleDateFormat("HH:mm", Locale.getDefault()).format(Date(timestamp)),
                            style = MaterialTheme.typography.labelSmall,
                            color = if (isFromMe) {
                                if (isDark) Color.White.copy(alpha = 0.7f) else Color.Black.copy(alpha = 0.6f)
                            } else {
                                if (isDark) Color.White.copy(alpha = 0.7f) else Color.Black.copy(alpha = 0.6f)
                            }
                        )
                        
                        if (isFromMe) {
                            Spacer(modifier = Modifier.width(4.dp))
                            Icon(
                                imageVector = when {
                                    isRead -> Icons.Default.DoneAll
                                    isDelivered -> Icons.Default.Done
                                    else -> Icons.Default.Schedule
                                },
                                contentDescription = when {
                                    isRead -> "Read"
                                    isDelivered -> "Delivered"
                                    else -> "Sending"
                                },
                                tint = when {
                                    isRead -> LittleGigPrimary
                                    isDelivered -> if (isDark) Color.White.copy(alpha = 0.7f) else Color.Black.copy(alpha = 0.6f)
                                    else -> if (isDark) Color.White.copy(alpha = 0.5f) else Color.Black.copy(alpha = 0.4f)
                                },
                                modifier = Modifier.size(16.dp)
                            )
                        }
                    }
                }
            }
            
            // Reaction options (appears on long press)
            if (showReactions && onReact != null) {
                LiquidGlassCard(
                    modifier = Modifier.padding(top = 4.dp),
                    cornerRadius = 16.dp,
                    alpha = if (isDark) 0.9f else 0.95f
                ) {
                    Row(
                        modifier = Modifier.padding(8.dp),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        listOf("👍", "❤️", "😂", "😮", "😢", "😡").forEach { emoji ->
                            Text(
                                text = emoji,
                                modifier = Modifier
                                    .size(32.dp)
                                    .clickable {
                                        onReact(emoji)
                                        showReactions = false
                                    }
                                    .padding(4.dp),
                                style = MaterialTheme.typography.titleMedium
                            )
                        }
                    }
                }
            }
        }
        
        if (isFromMe && showAvatar) {
            LiquidGlassAvatar(
                imageUrl = avatarUrl,
                modifier = Modifier.padding(start = 8.dp),
                size = 36.dp
            )
        }
    }
}

@Composable
fun LiquidGlassTicketShareBubble(
    sharedTicket: com.littlegig.app.data.model.SharedTicket,
    isFromMe: Boolean,
    timestamp: Long,
    onRedeem: () -> Unit,
    modifier: Modifier = Modifier,
    showAvatar: Boolean = true,
    avatarUrl: String? = null
) {
    val isDark = isSystemInDarkTheme()
    
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = if (isFromMe) Arrangement.End else Arrangement.Start
    ) {
        if (!isFromMe && showAvatar) {
            LiquidGlassAvatar(
                imageUrl = avatarUrl,
                modifier = Modifier.padding(end = 8.dp),
                size = 36.dp
            )
        }
        
        Column(
            horizontalAlignment = if (isFromMe) Alignment.End else Alignment.Start
        ) {
            LiquidGlassCard(
                modifier = Modifier.widthIn(max = 320.dp),
                cornerRadius = 20.dp,
                alpha = if (isFromMe) {
                    if (isDark) 0.85f else 0.9f
                } else {
                    if (isDark) 0.75f else 0.8f
                },
                borderColor = if (isFromMe) {
                    LittleGigPrimary.copy(alpha = 0.4f)
                } else {
                    if (isDark) Color.White.copy(alpha = 0.2f) else Color.Black.copy(alpha = 0.1f)
                }
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
                            modifier = Modifier.size(20.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "Shared Ticket",
                            style = MaterialTheme.typography.labelMedium.copy(
                                fontWeight = FontWeight.SemiBold
                            ),
                            color = LittleGigPrimary
                        )
                    }
                    
                    Spacer(modifier = Modifier.height(12.dp))
                    
                    Text(
                        text = sharedTicket.eventName,
                        style = MaterialTheme.typography.bodyMedium.copy(
                            fontWeight = FontWeight.SemiBold
                        ),
                        color = if (isDark) Color.White else Color.Black
                    )
                    
                    Spacer(modifier = Modifier.height(4.dp))
                    
                    Text(
                        text = "Ticket ID: ${sharedTicket.ticketId}",
                        style = MaterialTheme.typography.bodySmall,
                        color = if (isDark) Color.White.copy(alpha = 0.7f) else Color.Black.copy(alpha = 0.6f)
                    )
                    
                    Spacer(modifier = Modifier.height(12.dp))
                    
                    LiquidGlassButton(
                        onClick = onRedeem,
                        cornerRadius = 16.dp
                    ) {
                        Text(
                            text = "Redeem Ticket",
                            style = MaterialTheme.typography.bodySmall.copy(
                                fontWeight = FontWeight.SemiBold
                            ),
                            color = LittleGigPrimary,
                            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
                        )
                    }
                    
                    Spacer(modifier = Modifier.height(8.dp))
                    
                    Text(
                        text = SimpleDateFormat("HH:mm", Locale.getDefault()).format(Date(timestamp)),
                        style = MaterialTheme.typography.labelSmall,
                        color = if (isDark) Color.White.copy(alpha = 0.7f) else Color.Black.copy(alpha = 0.6f)
                    )
                }
            }
        }
        
        if (isFromMe && showAvatar) {
            LiquidGlassAvatar(
                imageUrl = avatarUrl,
                modifier = Modifier.padding(start = 8.dp),
                size = 36.dp
            )
        }
    }
}

@Composable
fun LiquidGlassChatInput(
    value: String,
    onValueChange: (String) -> Unit,
    onSend: () -> Unit,
    modifier: Modifier = Modifier,
    placeholder: String = "Type a message...",
    isTyping: Boolean = false,
    replyTo: com.littlegig.app.data.model.Message? = null,
    onCancelReply: (() -> Unit)? = null
) {
    val isDark = isSystemInDarkTheme()
    
    Column {
        // Reply preview
        if (replyTo != null) {
            LiquidGlassCard(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 8.dp),
                cornerRadius = 16.dp,
                alpha = if (isDark) 0.8f else 0.9f
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.Reply,
                        contentDescription = "Replying to",
                        tint = LittleGigPrimary,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = replyTo.content.take(50) + if (replyTo.content.length > 50) "..." else "",
                        style = MaterialTheme.typography.bodySmall,
                        color = if (isDark) Color.White.copy(alpha = 0.7f) else Color.Black.copy(alpha = 0.6f),
                        modifier = Modifier.weight(1f)
                    )
                    if (onCancelReply != null) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = "Cancel reply",
                            tint = if (isDark) Color.White.copy(alpha = 0.5f) else Color.Black.copy(alpha = 0.4f),
                            modifier = Modifier
                                .size(16.dp)
                                .clickable { onCancelReply() }
                        )
                    }
                }
            }
        }
        
        LiquidGlassCard(
            modifier = modifier,
            cornerRadius = 28.dp,
            alpha = if (isDark) 0.75f else 0.8f,
            borderColor = if (isDark) {
                Color.White.copy(alpha = 0.2f)
            } else {
                Color.Black.copy(alpha = 0.1f)
            }
        ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp, vertical = 16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            androidx.compose.material3.TextField(
                value = value,
                onValueChange = onValueChange,
                placeholder = {
                    Text(
                        text = placeholder,
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
                modifier = Modifier.weight(1f),
                maxLines = 4
            )
            
            Spacer(modifier = Modifier.width(12.dp))
            
            LiquidGlassButton(
                onClick = onSend,
                cornerRadius = 20.dp
            ) {
                Icon(
                    imageVector = Icons.Default.Send,
                    contentDescription = "Send",
                    tint = LittleGigPrimary,
                    modifier = Modifier.size(20.dp)
                )
            }
        }
    }
    }
}

@Composable
fun LiquidGlassTypingIndicator(
    modifier: Modifier = Modifier
) {
    val isDark = isSystemInDarkTheme()
    
    // Typing animation
    val typingAnimation by rememberInfiniteTransition(label = "typing").animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(1000, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "typing"
    )
    
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.Start
    ) {
        LiquidGlassAvatar(
            imageUrl = null,
            modifier = Modifier.padding(end = 8.dp),
            size = 36.dp
        )
        
        LiquidGlassCard(
            cornerRadius = 20.dp,
            alpha = if (isDark) 0.7f else 0.75f,
            borderColor = if (isDark) {
                Color.White.copy(alpha = 0.15f)
            } else {
                Color.Black.copy(alpha = 0.08f)
            }
        ) {
            Row(
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                repeat(3) { index ->
                    Box(
                        modifier = Modifier
                            .size(8.dp)
                            .background(
                                color = if (isDark) Color.White.copy(alpha = 0.6f) else Color.Black.copy(alpha = 0.4f),
                                shape = CircleShape
                            )
                            .scale(
                                if (index == (typingAnimation * 3).toInt()) 1.2f else 0.8f
                            )
                    )
                    
                    if (index < 2) {
                        Spacer(modifier = Modifier.width(4.dp))
                    }
                }
            }
        }
    }
}

@Composable
fun LiquidGlassChatHeader(
    title: String,
    subtitle: String? = null,
    avatarUrl: String? = null,
    onBackClick: () -> Unit,
    onMoreClick: (() -> Unit)? = null,
    modifier: Modifier = Modifier
) {
    val isDark = isSystemInDarkTheme()
    
    LiquidGlassCard(
        modifier = modifier,
        cornerRadius = 0.dp,
        alpha = if (isDark) 0.8f else 0.85f,
        borderColor = Color.Transparent
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp, vertical = 16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            LiquidGlassButton(
                onClick = onBackClick,
                cornerRadius = 16.dp
            ) {
                Icon(
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription = "Back",
                    tint = if (isDark) Color.White else Color.Black,
                    modifier = Modifier.size(20.dp)
                )
            }
            
            Spacer(modifier = Modifier.width(16.dp))
            
            LiquidGlassAvatar(
                imageUrl = avatarUrl,
                modifier = Modifier.padding(end = 12.dp),
                size = 40.dp
            )
            
            Column(
                modifier = Modifier.weight(1f)
            ) {
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
            
            if (onMoreClick != null) {
                LiquidGlassButton(
                    onClick = onMoreClick,
                    cornerRadius = 16.dp
                ) {
                    Icon(
                        imageVector = Icons.Default.MoreVert,
                        contentDescription = "More",
                        tint = if (isDark) Color.White else Color.Black,
                        modifier = Modifier.size(20.dp)
                    )
                }
            }
        }
    }
}

@Composable
fun LiquidGlassUserCard(
    user: com.littlegig.app.data.model.User,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    showStatus: Boolean = true
) {
    val isDark = isSystemInDarkTheme()
    
    LiquidGlassCard(
        modifier = modifier
            .fillMaxWidth()
            .clickable { onClick() },
        cornerRadius = 20.dp,
        alpha = if (isDark) 0.7f else 0.75f,
        borderColor = if (isDark) {
            Color.White.copy(alpha = 0.15f)
        } else {
            Color.Black.copy(alpha = 0.08f)
        }
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            LiquidGlassAvatar(
                imageUrl = user.profilePictureUrl,
                modifier = Modifier.padding(end = 16.dp),
                size = 48.dp
            )
            
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = user.displayName.ifEmpty { user.name }.ifEmpty { user.username }.ifEmpty { "Unknown User" },
                    style = MaterialTheme.typography.bodyLarge.copy(
                        fontWeight = FontWeight.SemiBold
                    ),
                    color = if (isDark) Color.White else Color.Black
                )
                
                if (showStatus) {
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                                            text = "Online", // Simplified for now
                    style = MaterialTheme.typography.bodySmall,
                    color = SuccessGreen
                    )
                }
            }
            
            Icon(
                imageVector = Icons.Default.ChevronRight,
                contentDescription = null,
                tint = if (isDark) Color.White.copy(alpha = 0.6f) else Color.Black.copy(alpha = 0.5f),
                modifier = Modifier.size(20.dp)
            )
        }
    }
}

@Composable
fun LiquidGlassChatCard(
    chat: com.littlegig.app.data.model.Chat,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val isDark = isSystemInDarkTheme()
    
    LiquidGlassCard(
        modifier = modifier
            .fillMaxWidth()
            .clickable { onClick() },
        cornerRadius = 20.dp,
        alpha = if (isDark) 0.7f else 0.75f,
        borderColor = if (isDark) {
            Color.White.copy(alpha = 0.15f)
        } else {
            Color.Black.copy(alpha = 0.08f)
        }
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            LiquidGlassAvatar(
                imageUrl = null, // Chat doesn't have direct avatar
                modifier = Modifier.padding(end = 16.dp),
                size = 48.dp
            )
            
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = chat.name?.ifEmpty { 
                        chat.participants.joinToString(", ") { participant -> participant?.ifEmpty { "Unknown" } ?: "Unknown" }
                    } ?: "Unknown Chat",
                    style = MaterialTheme.typography.bodyLarge.copy(
                        fontWeight = FontWeight.SemiBold
                    ),
                    color = if (isDark) Color.White else Color.Black
                )
                
                Spacer(modifier = Modifier.height(4.dp))
                
                Text(
                    text = chat.lastMessage?.ifEmpty { "No messages yet" } ?: "No messages yet",
                    style = MaterialTheme.typography.bodyMedium,
                    color = if (isDark) Color.White.copy(alpha = 0.7f) else Color.Black.copy(alpha = 0.6f),
                    maxLines = 2,
                    overflow = androidx.compose.ui.text.style.TextOverflow.Ellipsis
                )
                
                Spacer(modifier = Modifier.height(4.dp))
                
                Text(
                    text = SimpleDateFormat("MMM dd, HH:mm", Locale.getDefault()).format(Date(chat.lastMessageTime)),
                    style = MaterialTheme.typography.bodySmall,
                    color = if (isDark) Color.White.copy(alpha = 0.6f) else Color.Black.copy(alpha = 0.5f)
                )
            }
            
            Icon(
                imageVector = Icons.Default.ChevronRight,
                contentDescription = null,
                tint = if (isDark) Color.White.copy(alpha = 0.6f) else Color.Black.copy(alpha = 0.5f),
                modifier = Modifier.size(20.dp)
            )
        }
    }
}