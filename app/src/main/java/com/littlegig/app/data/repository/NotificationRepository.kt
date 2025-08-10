package com.littlegig.app.data.repository

import android.content.Context
import com.google.firebase.messaging.FirebaseMessaging
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.channels.awaitClose
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class NotificationRepository @Inject constructor(
    private val firestore: FirebaseFirestore,
    private val context: Context
) {
    
    // Get FCM token and save to user profile
    suspend fun updateFcmToken(userId: String) {
        try {
            val token = FirebaseMessaging.getInstance().token.await()
            firestore.collection("users").document(userId).update(
                mapOf(
                    "fcmToken" to token,
                    "lastTokenUpdate" to System.currentTimeMillis()
                )
            )
        } catch (e: Exception) {
            // Handle token generation failure
        }
    }
    
    // Subscribe to event topic
    suspend fun subscribeToEventTopic(eventId: String) {
        try {
            FirebaseMessaging.getInstance().subscribeToTopic("event_$eventId")
        } catch (e: Exception) {
            // Handle subscription failure
        }
    }
    
    // Unsubscribe from event topic
    suspend fun unsubscribeFromEventTopic(eventId: String) {
        try {
            FirebaseMessaging.getInstance().unsubscribeFromTopic("event_$eventId")
        } catch (e: Exception) {
            // Handle unsubscription failure
        }
    }

    // Subscribe to chat topic
    suspend fun subscribeToChatTopic(chatId: String) {
        try {
            FirebaseMessaging.getInstance().subscribeToTopic("chat_" + chatId)
        } catch (e: Exception) {
            // Handle subscription failure
        }
    }

    // Unsubscribe from chat topic
    suspend fun unsubscribeFromChatTopic(chatId: String) {
        try {
            FirebaseMessaging.getInstance().unsubscribeFromTopic("chat_" + chatId)
        } catch (e: Exception) {
            // Handle unsubscription failure
        }
    }
    
    // Get user notification settings
    suspend fun getNotificationSettings(userId: String): Map<String, Boolean> {
        return try {
            val doc = firestore.collection("users").document(userId).get().await()
            doc.data?.get("notificationSettings") as? Map<String, Boolean> 
                ?: mapOf(
                    "eventReminders" to true,
                    "newMessages" to true,
                    "paymentUpdates" to true,
                    "rankUpdates" to true
                )
        } catch (e: Exception) {
            mapOf(
                "eventReminders" to true,
                "newMessages" to true,
                "paymentUpdates" to true,
                "rankUpdates" to true
            )
        }
    }
    
    // Update notification settings
    suspend fun updateNotificationSettings(userId: String, settings: Map<String, Boolean>) {
        try {
            firestore.collection("users").document(userId).update(
                mapOf("notificationSettings" to settings)
            )
        } catch (e: Exception) {
            // Handle update failure
        }
    }
    
    // Get notification history
    fun getNotificationHistory(userId: String): Flow<List<NotificationRecord>> = callbackFlow {
        val listener = firestore.collection("notifications")
            .whereEqualTo("userId", userId)
            .orderBy("timestamp", com.google.firebase.firestore.Query.Direction.DESCENDING)
            .limit(50)
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    close(error)
                    return@addSnapshotListener
                }
                
                val notifications = snapshot?.documents?.mapNotNull { doc ->
                    doc.toObject(NotificationRecord::class.java)?.copy(id = doc.id)
                } ?: emptyList()
                
                trySend(notifications)
            }
        
        awaitClose { listener.remove() }
    }
    
    // Mark notification as read
    suspend fun markNotificationAsRead(notificationId: String) {
        try {
            firestore.collection("notifications").document(notificationId).update(
                mapOf("isRead" to true)
            )
        } catch (e: Exception) {
            // Handle update failure
        }
    }
    
    // Delete notification
    suspend fun deleteNotification(notificationId: String) {
        try {
            firestore.collection("notifications").document(notificationId).delete()
        } catch (e: Exception) {
            // Handle deletion failure
        }
    }
}

data class NotificationRecord(
    val id: String = "",
    val userId: String = "",
    val title: String = "",
    val body: String = "",
    val type: NotificationType = NotificationType.GENERAL,
    val data: Map<String, String> = emptyMap(),
    val timestamp: Long = System.currentTimeMillis(),
    val isRead: Boolean = false
)

enum class NotificationType {
    EVENT_REMINDER,
    NEW_MESSAGE,
    PAYMENT_UPDATE,
    RANK_UPDATE,
    GENERAL
} 