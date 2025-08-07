package com.littlegig.app.service

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.littlegig.app.presentation.MainActivity
import com.littlegig.app.R
import com.littlegig.app.data.repository.NotificationRepository
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class LittleGigFirebaseMessagingService : FirebaseMessagingService() {
    
    @Inject
    lateinit var notificationRepository: NotificationRepository
    
    companion object {
        private const val CHANNEL_ID = "littlegig_channel"
        private const val CHANNEL_NAME = "LittleGig Notifications"
        private const val CHANNEL_DESCRIPTION = "Notifications from LittleGig app"
    }
    
    override fun onCreate() {
        super.onCreate()
        createNotificationChannel()
    }
    
    override fun onNewToken(token: String) {
        super.onNewToken(token)
        // Token will be updated when user logs in
    }
    
    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        super.onMessageReceived(remoteMessage)
        
        // Handle data messages
        remoteMessage.data.isNotEmpty().let {
            val title = remoteMessage.data["title"] ?: "LittleGig"
            val body = remoteMessage.data["body"] ?: "You have a new notification"
            val type = remoteMessage.data["type"] ?: "general"
            val eventId = remoteMessage.data["eventId"]
            val chatId = remoteMessage.data["chatId"]
            
            showNotification(title, body, type, eventId, chatId)
        }
        
        // Handle notification messages
        remoteMessage.notification?.let { notification ->
            val title = notification.title ?: "LittleGig"
            val body = notification.body ?: "You have a new notification"
            
            showNotification(title, body, "general", null, null)
        }
    }
    
    private fun showNotification(
        title: String,
        body: String,
        type: String,
        eventId: String?,
        chatId: String?
    ) {
        val intent = Intent(this, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            when (type) {
                "event" -> putExtra("navigate_to", "event_details")
                "chat" -> putExtra("navigate_to", "chat")
                "payment" -> putExtra("navigate_to", "payments")
                else -> putExtra("navigate_to", "home")
            }
            eventId?.let { putExtra("event_id", it) }
            chatId?.let { putExtra("chat_id", it) }
        }
        
        val pendingIntent = PendingIntent.getActivity(
            this, 0, intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
        
        val notification = NotificationCompat.Builder(this, CHANNEL_ID)
            .setContentTitle(title)
            .setContentText(body)
            .setSmallIcon(R.drawable.ic_notification)
            .setAutoCancel(true)
            .setContentIntent(pendingIntent)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .build()
        
        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.notify(System.currentTimeMillis().toInt(), notification)
    }
    
    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                CHANNEL_ID,
                CHANNEL_NAME,
                NotificationManager.IMPORTANCE_HIGH
            ).apply {
                description = CHANNEL_DESCRIPTION
                enableLights(true)
                enableVibration(true)
            }
            
            val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }
} 