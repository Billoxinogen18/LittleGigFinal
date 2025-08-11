package com.littlegig.app.data.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import java.util.Date

@Parcelize
data class Chat(
    val id: String = "",
    val name: String? = null,
    val chatType: ChatType = ChatType.DIRECT,
    val participants: List<String> = emptyList(),
    val admins: List<String> = emptyList(),
    val lastMessage: String? = null,
    val lastMessageTime: Long = System.currentTimeMillis(),
    val lastMessageSenderId: String? = null,
    val unreadCount: Int = 0,
    val isActive: Boolean = true,
    val createdAt: Long = System.currentTimeMillis(),
    val eventId: String? = null, // For event-based groups
    val imageUrl: String = "",
    val pinnedMessageId: String? = null,
    val announcement: String? = null
) : Parcelable

enum class ChatType {
    DIRECT,     // One-on-one chat
    GROUP,      // Group chat
    EVENT       // Event-based group
}

@Parcelize
data class Message(
    val id: String = "",
    val chatId: String = "",
    val senderId: String = "",
    val senderName: String = "",
    val senderImageUrl: String = "",
    val content: String = "",
    val messageType: MessageType = MessageType.TEXT,
    val mediaUrls: List<String> = emptyList(),
    val metadata: Map<String, String> = emptyMap(),
    val timestamp: Long = System.currentTimeMillis(),
    val isRead: Boolean = false,
    val readBy: List<String> = emptyList(),
    val sharedTicket: SharedTicket? = null,
    val status: MessageStatus = MessageStatus.SENT,
    val optimisticId: String = "",
    val reactions: Map<String, String> = emptyMap(),
    val mentions: List<String> = emptyList(),
    val hashtags: List<String> = emptyList(),
    val deliveredTo: List<String> = emptyList(),
    val isDeleted: Boolean = false,
    val deletedAt: Long = 0L,
    val editedAt: Long = 0L,
    val replyToMessageId: String? = null,
    val replyPreview: ReplyPreview? = null,
    val forwardFromMessageId: String? = null,
    val fileSize: Long = 0L,
    val duration: Long = 0L,
    val width: Int = 0,
    val height: Int = 0
) : Parcelable

@Parcelize
data class ReplyPreview(
    val senderName: String = "",
    val snippet: String = ""
) : Parcelable

enum class MessageType {
    TEXT,
    IMAGE,
    VIDEO,
    AUDIO,
    FILE,
    TICKET_SHARE,
    LOCATION,
    EVENT_SHARE,
    SYSTEM
}

enum class MessageStatus {
    SENT,
    DELIVERED,
    READ,
    FAILED
}

@Parcelize
data class SharedTicket(
    val ticketId: String = "",
    val eventName: String = "",
    val eventImageUrl: String = "",
    val ticketType: String = "",
    val price: Double = 0.0,
    val isRedeemed: Boolean = false,
    val redeemedBy: String? = null,
    val redeemedAt: Long? = null
) : Parcelable

@Parcelize
data class TypingIndicator(
    val userId: String = "",
    val userName: String = "",
    val isTyping: Boolean = false,
    val timestamp: Long = System.currentTimeMillis()
) : Parcelable

@Parcelize
data class ChatState(
    val currentChat: Chat? = null,
    val messages: List<Message> = emptyList(),
    val typingUsers: List<TypingIndicator> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null
) : Parcelable 