package com.littlegig.app.services

import android.app.NotificationChannel as AndroidNotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
import com.google.firebase.messaging.FirebaseMessaging
import com.google.firebase.messaging.RemoteMessage
import com.google.firebase.firestore.FirebaseFirestore
import com.littlegig.app.R
import com.littlegig.app.data.model.Notification
import com.littlegig.app.data.model.NotificationType
import com.littlegig.app.data.model.NotificationPriority
import com.littlegig.app.presentation.MainActivity
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.tasks.await
import java.util.*
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.atomic.AtomicLong
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class NotificationService @Inject constructor(
    private val context: Context,
    private val firestore: FirebaseFirestore
) {

    companion object {
        private const val CHANNEL_ID_CHAT = "chat_notifications"
        private const val CHANNEL_ID_EVENTS = "event_notifications"
        private const val CHANNEL_ID_PAYMENTS = "payment_notifications"
        private const val CHANNEL_ID_SYSTEM = "system_notifications"
        private const val CHANNEL_ID_MARKETING = "marketing_notifications"
        
        private const val MAX_NOTIFICATIONS = 100
        private const val NOTIFICATION_TTL_HOURS = 168 // 1 week
        private const val BATCH_UPDATE_SIZE = 50
        private const val RATE_LIMIT_PER_MINUTE = 10
    }

    // Notification management
    private val activeNotifications = ConcurrentHashMap<String, Notification>()
    private val notificationQueue = Channel<Notification>(Channel.UNLIMITED)
    private val rateLimiter = RateLimiter(RATE_LIMIT_PER_MINUTE)
    private val notificationCounter = AtomicLong(0L)
    
    // User preferences and settings
    private val userNotificationPreferences = ConcurrentHashMap<String, NotificationPreferences>()
    private val mutedUsers = ConcurrentHashMap<String, Long>() // userId to muteUntil timestamp
    private val mutedChats = ConcurrentHashMap<String, Long>() // chatId to muteUntil timestamp
    private val mutedEvents = ConcurrentHashMap<String, Long>() // eventId to muteUntil timestamp
    
    // Performance optimization
    private val notificationCache = ConcurrentHashMap<String, Notification>(1000)
    private val userCache = ConcurrentHashMap<String, UserNotificationSettings>(500)
    
    // Coroutine scope for background operations
    private val serviceScope = CoroutineScope(Dispatchers.IO + SupervisorJob())

    init {
        createNotificationChannels()
        startNotificationProcessor()
        startNotificationCleanup()
        initializeUserPreferences()
    }

    // Production-grade notification delivery algorithm
    suspend fun sendNotification(notification: Notification): Result<String> {
        return try {
            // Rate limiting check
            if (!rateLimiter.tryAcquire()) {
                return Result.failure(Exception("Rate limit exceeded"))
            }

            // Generate unique notification ID
            val notificationId = generateNotificationId()
            val enhancedNotification = notification.copy(
                id = notificationId,
                timestamp = System.currentTimeMillis(),
                sequenceNumber = notificationCounter.incrementAndGet()
            )

            // Check user preferences and mute settings
            if (shouldMuteNotification(enhancedNotification)) {
                return Result.success(notificationId)
            }

            // Add to queue for processing
            notificationQueue.send(enhancedNotification)

            // Store in Firestore
            val result = storeNotificationInFirestore(enhancedNotification)
            
            if (result.isSuccess) {
                // Update local cache
                activeNotifications[notificationId] = enhancedNotification
                notificationCache[notificationId] = enhancedNotification
                
                // Trigger real-time updates
                broadcastNotificationUpdate(enhancedNotification)
            }

            result
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    // Sophisticated notification muting algorithm
    private fun shouldMuteNotification(notification: Notification): Boolean {
        val currentTime = System.currentTimeMillis()
        val userId = notification.userId
        
        // Check global user mute
        val userMuteUntil = mutedUsers[userId]
        if (userMuteUntil != null && currentTime < userMuteUntil) {
            return true
        }

        // Check specific entity mutes
        when (notification.type) {
            NotificationType.CHAT_MESSAGE -> {
                val chatId = notification.entityId
                val chatMuteUntil = mutedChats[chatId]
                if (chatMuteUntil != null && currentTime < chatMuteUntil) {
                    return true
                }
            }
            NotificationType.EVENT_UPDATE -> {
                val eventId = notification.entityId
                val eventMuteUntil = mutedEvents[eventId]
                if (eventMuteUntil != null && currentTime < eventMuteUntil) {
                    return true
                }
            }
            else -> { /* No specific muting for other types */ }
        }

        // Check user preferences
        val userPrefs = userNotificationPreferences[userId]
        if (userPrefs != null) {
            return !userPrefs.isEnabled(notification.type)
        }

        return false
    }

    // Advanced notification processing with priority queuing
    private fun startNotificationProcessor() {
        serviceScope.launch {
            notificationQueue.consumeAsFlow()
                .buffer(Channel.UNLIMITED)
                .collect { notification ->
                    try {
                        processNotification(notification)
                    } catch (e: Exception) {
                        // Handle processing errors
                        handleNotificationError(notification, e)
                    }
                }
        }
    }

    private suspend fun processNotification(notification: Notification) {
        // Determine notification priority
        val priority = calculateNotificationPriority(notification)
        
        // Apply smart delivery timing
        val deliveryDelay = calculateDeliveryDelay(notification, priority)
        
        if (deliveryDelay > 0) {
            delay(deliveryDelay)
        }

        // Check if notification is still relevant
        if (!isNotificationStillRelevant(notification)) {
            return
        }

        // Send notification based on type and priority
        when (notification.type) {
            NotificationType.CHAT_MESSAGE -> sendChatNotification(notification)
            NotificationType.EVENT_UPDATE -> sendEventNotification(notification)
            NotificationType.PAYMENT_SUCCESS -> sendPaymentNotification(notification)
            NotificationType.SYSTEM_ALERT -> sendSystemNotification(notification)
            NotificationType.MARKETING -> sendMarketingNotification(notification)
            else -> sendGenericNotification(notification)
        }

        // Update delivery status
        updateNotificationDeliveryStatus(notification.id, "delivered")
        
        // Trigger engagement tracking
        trackNotificationEngagement(notification)
    }

    // Sophisticated priority calculation algorithm
    private fun calculateNotificationPriority(notification: Notification): NotificationPriority {
        var priority = NotificationPriority.NORMAL
        
        // Boost priority based on user engagement
        val userEngagement = getUserEngagementScore(notification.userId)
        if (userEngagement > 0.8) {
            priority = priority.increase()
        }
        
        // Boost priority for urgent notifications
        when (notification.type) {
            NotificationType.PAYMENT_SUCCESS -> priority = NotificationPriority.HIGH
            NotificationType.SYSTEM_ALERT -> priority = NotificationPriority.URGENT
            NotificationType.EVENT_UPDATE -> {
                if (isEventStartingSoon(notification.entityId)) {
                    priority = NotificationPriority.HIGH
                }
            }
            else -> { /* Default priority */ }
        }
        
        // Boost priority for VIP users
        if (isVIPUser(notification.userId)) {
            priority = priority.increase()
        }
        
        return priority
    }

    // Smart delivery timing algorithm
    private fun calculateDeliveryDelay(notification: Notification, priority: NotificationPriority): Long {
        val currentHour = Calendar.getInstance().get(Calendar.HOUR_OF_DAY)
        
        // Don't send notifications during quiet hours unless urgent
        if (priority != NotificationPriority.URGENT && isQuietHours(currentHour)) {
            return calculateNextActiveTime(currentHour)
        }
        
        // Add small random delay to prevent notification storms
        return (Math.random() * 5000).toLong()
    }

    private fun isQuietHours(hour: Int): Boolean {
        return hour < 8 || hour > 22
    }

    private fun calculateNextActiveTime(currentHour: Int): Long {
        val nextActiveHour = if (currentHour < 8) 8 else 8
        val hoursToWait = (nextActiveHour - currentHour + 24) % 24
        return hoursToWait * 3600 * 1000L
    }

    // Notification delivery methods
    private fun sendChatNotification(notification: Notification) {
        val intent = Intent(context, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            putExtra("chatId", notification.entityId)
            putExtra("notificationId", notification.id)
        }
        
        val pendingIntent = PendingIntent.getActivity(
            context, notification.id.hashCode(), intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val builder = NotificationCompat.Builder(context, CHANNEL_ID_CHAT)
            .setSmallIcon(R.drawable.ic_notification)
            .setContentTitle(notification.title)
            .setContentText(notification.body)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setAutoCancel(true)
            .setContentIntent(pendingIntent)
            .setCategory(NotificationCompat.CATEGORY_MESSAGE)
            .setStyle(NotificationCompat.BigTextStyle().bigText(notification.body))

        // Add rich media if available
        notification.imageUrl?.let { imageUrl ->
            builder.setLargeIcon(android.graphics.BitmapFactory.decodeFile(imageUrl))
        }

        // Add action buttons
        builder.addAction(
                            R.drawable.ic_notification,
            "Reply",
            createReplyPendingIntent(notification)
        )

        showNotification(notification.id.hashCode(), builder.build())
    }

    private fun sendEventNotification(notification: Notification) {
        val intent = Intent(context, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            putExtra("eventId", notification.entityId)
            putExtra("notificationId", notification.id)
        }
        
        val pendingIntent = PendingIntent.getActivity(
            context, notification.id.hashCode(), intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val builder = NotificationCompat.Builder(context, CHANNEL_ID_EVENTS)
            .setSmallIcon(R.drawable.ic_notification)
            .setContentTitle(notification.title)
            .setContentText(notification.body)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setAutoCancel(true)
            .setContentIntent(pendingIntent)
            .setCategory(NotificationCompat.CATEGORY_EVENT)

        showNotification(notification.id.hashCode(), builder.build())
    }

    private fun sendPaymentNotification(notification: Notification) {
        val intent = Intent(context, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            putExtra("paymentId", notification.entityId)
            putExtra("notificationId", notification.id)
        }
        
        val pendingIntent = PendingIntent.getActivity(
            context, notification.id.hashCode(), intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val builder = NotificationCompat.Builder(context, CHANNEL_ID_PAYMENTS)
            .setSmallIcon(R.drawable.ic_notification)
            .setContentTitle(notification.title)
            .setContentText(notification.body)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setAutoCancel(true)
            .setContentIntent(pendingIntent)
            .setCategory(NotificationCompat.CATEGORY_STATUS)

        showNotification(notification.id.hashCode(), builder.build())
    }

    private fun sendSystemNotification(notification: Notification) {
        val intent = Intent(context, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            putExtra("notificationId", notification.id)
        }
        
        val pendingIntent = PendingIntent.getActivity(
            context, notification.id.hashCode(), intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val builder = NotificationCompat.Builder(context, CHANNEL_ID_SYSTEM)
            .setSmallIcon(R.drawable.ic_notification)
            .setContentTitle(notification.title)
            .setContentText(notification.body)
            .setPriority(NotificationCompat.PRIORITY_LOW)
            .setAutoCancel(true)
            .setContentIntent(pendingIntent)
            .setCategory(NotificationCompat.CATEGORY_SERVICE)

        showNotification(notification.id.hashCode(), builder.build())
    }

    private fun sendMarketingNotification(notification: Notification) {
        val intent = Intent(context, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            putExtra("marketingId", notification.entityId)
            putExtra("notificationId", notification.id)
        }
        
        val pendingIntent = PendingIntent.getActivity(
            context, notification.id.hashCode(), intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val builder = NotificationCompat.Builder(context, CHANNEL_ID_MARKETING)
            .setSmallIcon(R.drawable.ic_notification)
            .setContentTitle(notification.title)
            .setContentText(notification.body)
            .setPriority(NotificationCompat.PRIORITY_LOW)
            .setAutoCancel(true)
            .setContentIntent(pendingIntent)
            .setCategory(NotificationCompat.CATEGORY_PROMO)

        showNotification(notification.id.hashCode(), builder.build())
    }

    private fun sendGenericNotification(notification: Notification) {
        val intent = Intent(context, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            putExtra("notificationId", notification.id)
        }
        
        val pendingIntent = PendingIntent.getActivity(
            context, notification.id.hashCode(), intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val builder = NotificationCompat.Builder(context, CHANNEL_ID_SYSTEM)
            .setSmallIcon(R.drawable.ic_notification)
            .setContentTitle(notification.title)
            .setContentText(notification.body)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setAutoCancel(true)
            .setContentIntent(pendingIntent)

        showNotification(notification.id.hashCode(), builder.build())
    }

    private fun showNotification(id: Int, notification: android.app.Notification) {
        with(NotificationManagerCompat.from(context)) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                if (ContextCompat.checkSelfPermission(context, android.Manifest.permission.POST_NOTIFICATIONS) == 
                    android.content.pm.PackageManager.PERMISSION_GRANTED) {
                    notify(id, notification)
                }
            } else {
                notify(id, notification)
            }
        }
    }

    // Notification channel creation
    private fun createNotificationChannels() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val notificationManager = context.getSystemService(NotificationManager::class.java)
            
            val channels = listOf(
                AndroidNotificationChannel(
                    CHANNEL_ID_CHAT,
                    "Chat Messages",
                    NotificationManager.IMPORTANCE_HIGH
                ).apply {
                    description = "Notifications for new chat messages"
                    enableVibration(true)
                    enableLights(true)
                },
                AndroidNotificationChannel(
                    CHANNEL_ID_EVENTS,
                    "Event Updates",
                    NotificationManager.IMPORTANCE_DEFAULT
                ).apply {
                    description = "Notifications for event updates and reminders"
                    enableVibration(true)
                },
                AndroidNotificationChannel(
                    CHANNEL_ID_PAYMENTS,
                    "Payment Notifications",
                    NotificationManager.IMPORTANCE_HIGH
                ).apply {
                    description = "Notifications for payment confirmations and updates"
                    enableVibration(true)
                },
                AndroidNotificationChannel(
                    CHANNEL_ID_SYSTEM,
                    "System Notifications",
                    NotificationManager.IMPORTANCE_LOW
                ).apply {
                    description = "System and service notifications"
                },
                AndroidNotificationChannel(
                    CHANNEL_ID_MARKETING,
                    "Marketing & Promotions",
                    NotificationManager.IMPORTANCE_LOW
                ).apply {
                    description = "Marketing and promotional notifications"
                }
            )
            
            channels.forEach { channel ->
                notificationManager.createNotificationChannel(channel)
            }
        }
    }

    // Utility functions
    private fun generateNotificationId(): String {
        return "notif_${System.currentTimeMillis()}_${UUID.randomUUID().toString().substring(0, 8)}"
    }

    private fun createReplyPendingIntent(notification: Notification): PendingIntent {
        val intent = Intent(context, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            putExtra("chatId", notification.entityId)
            putExtra("action", "reply")
        }
        
        return PendingIntent.getActivity(
            context, 
            "${notification.id}_reply".hashCode(), 
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
    }

    // Placeholder functions for integration
    private suspend fun storeNotificationInFirestore(notification: Notification): Result<String> {
        return try {
            firestore.collection("notifications")
                .document(notification.id)
                .set(notification)
                .await()
            Result.success(notification.id)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    private fun broadcastNotificationUpdate(notification: Notification) {}
    private fun updateNotificationDeliveryStatus(notificationId: String, status: String) {}
    private fun trackNotificationEngagement(notification: Notification) {}
    private fun getUserEngagementScore(userId: String): Double = 0.5
    private fun isEventStartingSoon(eventId: String): Boolean = false
    private fun isVIPUser(userId: String): Boolean = false
    private fun isNotificationStillRelevant(notification: Notification): Boolean = true
    private fun handleNotificationError(notification: Notification, error: Exception) {}
    private fun startNotificationCleanup() {}
    private fun initializeUserPreferences() {}

    // Rate limiting implementation
    private class RateLimiter(private val maxRequestsPerMinute: Int) {
        private val requests = mutableListOf<Long>()
        
        fun tryAcquire(): Boolean {
            val now = System.currentTimeMillis()
            val oneMinuteAgo = now - 60000
            
            // Remove old requests
            requests.removeAll { it < oneMinuteAgo }
            
            // Check if we can make a new request
            return if (requests.size < maxRequestsPerMinute) {
                requests.add(now)
                true
            } else {
                false
            }
        }
    }

    // Data classes
    data class NotificationPreferences(
        val userId: String,
        val chatEnabled: Boolean = true,
        val eventEnabled: Boolean = true,
        val paymentEnabled: Boolean = true,
        val systemEnabled: Boolean = true,
        val marketingEnabled: Boolean = false,
        val quietHoursStart: Int = 22,
        val quietHoursEnd: Int = 8
    ) {
        fun isEnabled(type: NotificationType): Boolean {
            return when (type) {
                NotificationType.CHAT_MESSAGE -> chatEnabled
                NotificationType.EVENT_UPDATE -> eventEnabled
                NotificationType.PAYMENT_SUCCESS -> paymentEnabled
                NotificationType.SYSTEM_ALERT -> systemEnabled
                NotificationType.MARKETING -> marketingEnabled
                else -> true
            }
        }
    }

    data class UserNotificationSettings(
        val userId: String,
        val preferences: NotificationPreferences,
        val lastNotificationTime: Long = 0L,
        val totalNotifications: Int = 0,
        val engagementRate: Double = 0.0
    )

    // Extension functions
    fun NotificationPriority.increase(): NotificationPriority {
        return when (this) {
            NotificationPriority.LOW -> NotificationPriority.NORMAL
            NotificationPriority.NORMAL -> NotificationPriority.HIGH
            NotificationPriority.HIGH -> NotificationPriority.URGENT
            NotificationPriority.URGENT -> NotificationPriority.URGENT
        }
    }

    // Cleanup and resource management
    fun cleanup() {
        serviceScope.cancel()
        activeNotifications.clear()
        notificationCache.clear()
        userCache.clear()
    }
}
