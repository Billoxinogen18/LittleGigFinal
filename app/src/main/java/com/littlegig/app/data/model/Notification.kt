package com.littlegig.app.data.model

import com.google.firebase.firestore.DocumentId
import com.google.firebase.firestore.ServerTimestamp
import java.util.*

data class Notification(
    @DocumentId
    val id: String = "",
    val userId: String = "",
    val type: NotificationType = NotificationType.SYSTEM_ALERT,
    val title: String = "",
    val body: String = "",
    val entityId: String = "", // ID of the related entity (event, chat, payment, etc.)
    val entityType: String = "", // Type of the related entity
    val imageUrl: String? = null,
    val deepLink: String = "",
    val priority: NotificationPriority = NotificationPriority.NORMAL,
    val timestamp: Long = System.currentTimeMillis(),
    @ServerTimestamp
    val serverTimestamp: Date? = null,
    val sequenceNumber: Long = 0L,
    val status: NotificationStatus = NotificationStatus.PENDING,
    val isRead: Boolean = false,
    val readAt: Long = 0L,
    val isDelivered: Boolean = false,
    val deliveredAt: Long = 0L,
    val isClicked: Boolean = false,
    val clickedAt: Long = 0L,
    val expiresAt: Long = 0L,
    val metadata: Map<String, Any> = emptyMap(),
    val tags: List<String> = emptyList(),
    val campaignId: String = "", // For marketing notifications
    val segmentId: String = "", // For targeted notifications
    val abTestId: String = "", // For A/B testing
    val variant: String = "", // A/B test variant
    val engagementMetrics: EngagementMetrics = EngagementMetrics(),
    val deliveryAttempts: Int = 0,
    val lastDeliveryAttempt: Long = 0L,
    val failureReason: String = "",
    val retryCount: Int = 0,
    val maxRetries: Int = 3,
    val scheduledFor: Long = 0L, // For scheduled notifications
    val timezone: String = "UTC",
    val locale: String = "en",
    val deviceToken: String = "", // FCM device token
    val platform: String = "android",
    val appVersion: String = "",
    val osVersion: String = ""
)

enum class NotificationType {
    CHAT_MESSAGE,
    EVENT_UPDATE,
    EVENT_REMINDER,
    EVENT_CANCELLATION,
    EVENT_RSVP,
    PAYMENT_SUCCESS,
    PAYMENT_FAILED,
    PAYMENT_REFUND,
    TICKET_PURCHASED,
    TICKET_CANCELLED,
    TICKET_REFUNDED,
    BUSINESS_ACCOUNT_UPGRADED,
    NEW_FOLLOWER,
    FOLLOW_REQUEST,
    LIKE_RECEIVED,
    COMMENT_RECEIVED,
    SHARE_RECEIVED,
    SYSTEM_ALERT,
    SYSTEM_MAINTENANCE,
    SYSTEM_UPDATE,
    SECURITY_ALERT,
    MARKETING,
    PROMOTIONAL_OFFER,
    NEWSLETTER,
    FEATURED_EVENT,
    RECOMMENDATION,
    ACHIEVEMENT_UNLOCKED,
    REWARD_EARNED,
    RANKING_UPDATE,
    CHALLENGE_COMPLETED,
    SOCIAL_INTERACTION,
    NETWORK_UPDATE,
    VERIFICATION_REQUIRED,
    ACCOUNT_SUSPENDED,
    ACCOUNT_RESTORED,
    PRIVACY_UPDATE,
    TERMS_UPDATE,
    DATA_EXPORT,
    DATA_DELETION,
    CUSTOM
}

enum class NotificationPriority {
    LOW,
    NORMAL,
    HIGH,
    URGENT
}

enum class NotificationStatus {
    PENDING,
    SCHEDULED,
    SENDING,
    SENT,
    DELIVERED,
    READ,
    CLICKED,
    FAILED,
    CANCELLED,
    EXPIRED
}

data class EngagementMetrics(
    val impressions: Int = 0,
    val clicks: Int = 0,
    val dismissals: Int = 0,
    val shares: Int = 0,
    val replies: Int = 0,
    val timeSpent: Long = 0L, // Time spent viewing notification in milliseconds
    val conversionValue: Double = 0.0, // For marketing notifications
    val lastInteraction: Long = 0L,
    val interactionCount: Int = 0,
    val engagementScore: Double = 0.0 // Calculated engagement score
)

// Extension functions for notification utilities
fun Notification.isExpired(): Boolean {
    return expiresAt > 0 && System.currentTimeMillis() > expiresAt
}

fun Notification.isScheduled(): Boolean {
    return scheduledFor > 0 && System.currentTimeMillis() < scheduledFor
}

fun Notification.canRetry(): Boolean {
    return retryCount < maxRetries && status == NotificationStatus.FAILED
}

fun Notification.isHighPriority(): Boolean {
    return priority in listOf(NotificationPriority.HIGH, NotificationPriority.URGENT)
}

fun Notification.isUrgent(): Boolean {
    return priority == NotificationPriority.URGENT
}

fun Notification.isMarketing(): Boolean {
    return type in listOf(
        NotificationType.MARKETING,
        NotificationType.PROMOTIONAL_OFFER,
        NotificationType.NEWSLETTER,
        NotificationType.FEATURED_EVENT,
        NotificationType.RECOMMENDATION
    )
}

fun Notification.isSystem(): Boolean {
    return type in listOf(
        NotificationType.SYSTEM_ALERT,
        NotificationType.SYSTEM_MAINTENANCE,
        NotificationType.SYSTEM_UPDATE,
        NotificationType.SECURITY_ALERT
    )
}

fun Notification.isEventRelated(): Boolean {
    return type in listOf(
        NotificationType.EVENT_UPDATE,
        NotificationType.EVENT_REMINDER,
        NotificationType.EVENT_CANCELLATION,
        NotificationType.EVENT_RSVP
    )
}

fun Notification.isPaymentRelated(): Boolean {
    return type in listOf(
        NotificationType.PAYMENT_SUCCESS,
        NotificationType.PAYMENT_FAILED,
        NotificationType.PAYMENT_REFUND
    )
}

fun Notification.isTicketRelated(): Boolean {
    return type in listOf(
        NotificationType.TICKET_PURCHASED,
        NotificationType.TICKET_CANCELLED,
        NotificationType.TICKET_REFUNDED
    )
}

fun Notification.isSocial(): Boolean {
    return type in listOf(
        NotificationType.NEW_FOLLOWER,
        NotificationType.FOLLOW_REQUEST,
        NotificationType.LIKE_RECEIVED,
        NotificationType.COMMENT_RECEIVED,
        NotificationType.SHARE_RECEIVED,
        NotificationType.SOCIAL_INTERACTION
    )
}

fun Notification.getFormattedTimestamp(): String {
    val now = System.currentTimeMillis()
    val diff = now - timestamp
    
    return when {
        diff < 60000 -> "Just now" // Less than 1 minute
        diff < 3600000 -> "${diff / 60000}m ago" // Less than 1 hour
        diff < 86400000 -> "${diff / 3600000}h ago" // Less than 1 day
        diff < 604800000 -> "${diff / 86400000}d ago" // Less than 1 week
        else -> {
            val calendar = Calendar.getInstance().apply { timeInMillis = timestamp }
            "${calendar.get(Calendar.DAY_OF_MONTH)}/${calendar.get(Calendar.MONTH) + 1}/${calendar.get(Calendar.YEAR)}"
        }
    }
}

fun Notification.getScheduledTimeFormatted(): String {
    if (scheduledFor <= 0) return ""
    
    val calendar = Calendar.getInstance().apply { timeInMillis = scheduledFor }
    return "${calendar.get(Calendar.DAY_OF_MONTH)}/${calendar.get(Calendar.MONTH) + 1}/${calendar.get(Calendar.YEAR)} at ${calendar.get(Calendar.HOUR_OF_DAY)}:${String.format("%02d", calendar.get(Calendar.MINUTE))}"
}

fun Notification.getExpiryTimeFormatted(): String {
    if (expiresAt <= 0) return "Never expires"
    
    val calendar = Calendar.getInstance().apply { timeInMillis = expiresAt }
    return "${calendar.get(Calendar.DAY_OF_MONTH)}/${calendar.get(Calendar.MONTH) + 1}/${calendar.get(Calendar.YEAR)} at ${calendar.get(Calendar.HOUR_OF_DAY)}:${String.format("%02d", calendar.get(Calendar.MINUTE))}"
}

fun Notification.getEngagementRate(): Double {
    return if (engagementMetrics.impressions > 0) {
        engagementMetrics.clicks.toDouble() / engagementMetrics.impressions.toDouble()
    } else {
        0.0
    }
}

fun Notification.isActionable(): Boolean {
    return deepLink.isNotEmpty() || entityId.isNotEmpty()
}

fun Notification.requiresUserAction(): Boolean {
    return type in listOf(
        NotificationType.VERIFICATION_REQUIRED,
        NotificationType.FOLLOW_REQUEST,
        NotificationType.SECURITY_ALERT,
        NotificationType.ACCOUNT_SUSPENDED
    )
}

fun Notification.getCategory(): String {
    return when {
        isMarketing() -> "Marketing"
        isSystem() -> "System"
        isEventRelated() -> "Events"
        isPaymentRelated() -> "Payments"
        isTicketRelated() -> "Tickets"
        isSocial() -> "Social"
        else -> "General"
    }
}

fun Notification.getIconResource(): Int {
    return when (type) {
        NotificationType.CHAT_MESSAGE -> android.R.drawable.ic_dialog_email
        NotificationType.EVENT_UPDATE -> android.R.drawable.ic_menu_myplaces
        NotificationType.PAYMENT_SUCCESS -> android.R.drawable.ic_menu_send
        NotificationType.SYSTEM_ALERT -> android.R.drawable.ic_dialog_alert
        NotificationType.MARKETING -> android.R.drawable.ic_menu_share
        else -> android.R.drawable.ic_dialog_info
    }
}

fun Notification.getColorResource(): Int {
    return when (priority) {
        NotificationPriority.LOW -> android.R.color.holo_blue_light
        NotificationPriority.NORMAL -> android.R.color.holo_green_light
        NotificationPriority.HIGH -> android.R.color.holo_orange_light
        NotificationPriority.URGENT -> android.R.color.holo_red_light
    }
}

// Builder pattern for creating notifications
class NotificationBuilder {
    private var notification = Notification()
    
    fun setId(id: String): NotificationBuilder {
        notification = notification.copy(id = id)
        return this
    }
    
    fun setUserId(userId: String): NotificationBuilder {
        notification = notification.copy(userId = userId)
        return this
    }
    
    fun setType(type: NotificationType): NotificationBuilder {
        notification = notification.copy(type = type)
        return this
    }
    
    fun setTitle(title: String): NotificationBuilder {
        notification = notification.copy(title = title)
        return this
    }
    
    fun setBody(body: String): NotificationBuilder {
        notification = notification.copy(body = body)
        return this
    }
    
                fun setEntityId(entityId: String): NotificationBuilder {
                notification = notification.copy(entityId = entityId)
                return this
            }
            
            fun setEntityType(entityType: String): NotificationBuilder {
                notification = notification.copy(entityType = entityType)
                return this
            }
    
    fun setPriority(priority: NotificationPriority): NotificationBuilder {
        notification = notification.copy(priority = priority)
        return this
    }
    
    fun setImageUrl(imageUrl: String?): NotificationBuilder {
        notification = notification.copy(imageUrl = imageUrl)
        return this
    }
    
    fun setDeepLink(deepLink: String): NotificationBuilder {
        notification = notification.copy(deepLink = deepLink)
        return this
    }
    
    fun setScheduledFor(scheduledFor: Long): NotificationBuilder {
        notification = notification.copy(scheduledFor = scheduledFor)
        return this
    }
    
    fun setExpiresAt(expiresAt: Long): NotificationBuilder {
        notification = notification.copy(expiresAt = expiresAt)
        return this
    }
    
    fun setMetadata(metadata: Map<String, Any>): NotificationBuilder {
        notification = notification.copy(metadata = metadata)
        return this
    }
    
    fun addTag(tag: String): NotificationBuilder {
        notification = notification.copy(tags = notification.tags + tag)
        return this
    }
    
    fun build(): Notification {
        return notification.copy(
            timestamp = System.currentTimeMillis(),
            sequenceNumber = System.currentTimeMillis()
        )
    }
}

// Factory functions for common notification types
fun createChatNotification(
    userId: String,
    chatId: String,
    senderName: String,
    message: String
): Notification {
    return NotificationBuilder()
        .setUserId(userId)
        .setType(NotificationType.CHAT_MESSAGE)
        .setTitle("New message from $senderName")
        .setBody(message)
        .setEntityId(chatId)
        .setEntityType("chat")
        .setPriority(NotificationPriority.HIGH)
        .setDeepLink("littlegig://chat/$chatId")
        .build()
}

fun createEventReminderNotification(
    userId: String,
    eventId: String,
    eventTitle: String,
    eventTime: Long
): Notification {
    return NotificationBuilder()
        .setUserId(userId)
        .setType(NotificationType.EVENT_REMINDER)
        .setTitle("Event Reminder")
        .setBody("Your event '$eventTitle' starts in 1 hour")
        .setEntityId(eventId)
        .setEntityType("event")
        .setPriority(NotificationPriority.HIGH)
        .setDeepLink("littlegig://event/$eventId")
        .build()
}

fun createPaymentSuccessNotification(
    userId: String,
    paymentId: String,
    amount: Double,
    eventTitle: String
): Notification {
    return NotificationBuilder()
        .setUserId(userId)
        .setType(NotificationType.PAYMENT_SUCCESS)
        .setTitle("Payment Successful")
        .setBody("Your payment of KES $amount for '$eventTitle' was successful")
        .setEntityId(paymentId)
        .setEntityType("payment")
        .setPriority(NotificationPriority.HIGH)
        .setDeepLink("littlegig://payment/$paymentId")
        .build()
}

fun createSystemAlertNotification(
    userId: String,
    title: String,
    message: String,
    priority: NotificationPriority = NotificationPriority.NORMAL
): Notification {
    return NotificationBuilder()
        .setUserId(userId)
        .setType(NotificationType.SYSTEM_ALERT)
        .setTitle(title)
        .setBody(message)
        .setPriority(priority)
        .build()
}
