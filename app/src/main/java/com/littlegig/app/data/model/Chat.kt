package com.littlegig.app.data.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Chat(
    val id: String = "",
    val name: String = "",
    val type: ChatType = ChatType.DIRECT,
    val participants: List<String> = emptyList(),
    val admins: List<String> = emptyList(),
    val lastMessage: Message? = null,
    val lastMessageTime: Long = 0L,
    val unreadCount: Int = 0,
    val isActive: Boolean = true,
    val createdAt: Long = System.currentTimeMillis(),
    val eventId: String? = null, // For event-based groups
    val imageUrl: String = ""
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
    val timestamp: Long = System.currentTimeMillis(),
    val isRead: Boolean = false,
    val sharedTicket: SharedTicket? = null
) : Parcelable

enum class MessageType {
    TEXT,
    IMAGE,
    VIDEO,
    TICKET_SHARE,
    LOCATION
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