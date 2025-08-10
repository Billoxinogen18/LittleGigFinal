package com.littlegig.app.services

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.graphics.Color
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.littlegig.app.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class LittleGigMessagingService : FirebaseMessagingService() {
    
    override fun onCreate() {
        super.onCreate()
        createNotificationChannel()
    }
    
    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        super.onMessageReceived(remoteMessage)
        Log.d(TAG, "From: ${remoteMessage.from}")
        if (remoteMessage.data.isNotEmpty()) {
            Log.d(TAG, "Message data payload: ${remoteMessage.data}")
            handleDataMessage(remoteMessage.data)
        }
        remoteMessage.notification?.let {
            Log.d(TAG, "Message Notification Body: ${it.body}")
            showNotification(it.title ?: getString(R.string.app_name), it.body ?: "")
        }
    }
    
    override fun onNewToken(token: String) {
        Log.d(TAG, "Refreshed token: $token")
        sendRegistrationToServer(token)
    }
    
    private fun handleDataMessage(data: Map<String, String>) {
        val type = data["type"]
        when (type) {
            "chat_message" -> {
                val title = data["title"] ?: getString(R.string.app_name)
                val body = data["body"] ?: "New message"
                showNotification(title, body)
            }
            "event_reminder" -> { showNotification("Event Reminder", "Happening soon!") }
            "ticket_update" -> { showNotification("Ticket Update", "Your ticket status changed") }
            "payment_confirmation" -> { showNotification("Payment", "Payment confirmed") }
        }
    }
    
    private fun showNotification(title: String, body: String) {
        val builder = NotificationCompat.Builder(this, CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_notification)
            .setContentTitle(title)
            .setContentText(body)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setColor(Color.parseColor("#6366F1"))
            .setAutoCancel(true)
        with(NotificationManagerCompat.from(this)) {
            notify((System.currentTimeMillis() % Int.MAX_VALUE).toInt(), builder.build())
        }
    }
    
    private fun sendRegistrationToServer(token: String) {
        try {
            val uid = FirebaseAuth.getInstance().currentUser?.uid
            if (uid != null) {
                FirebaseFirestore.getInstance().collection("users").document(uid)
                    .set(mapOf("fcmToken" to token), com.google.firebase.firestore.SetOptions.merge())
                    .addOnSuccessListener { Log.d(TAG, "FCM token saved for $uid") }
                    .addOnFailureListener { e -> Log.w(TAG, "Failed to save FCM token", e) }
            } else Log.d(TAG, "No signed-in user; token will be saved on next sign-in")
        } catch (e: Exception) {
            Log.w(TAG, "Error saving FCM token", e)
        }
    }

    private fun createNotificationChannel() {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            val name = "LittleGig"
            val descriptionText = "General notifications"
            val importance = NotificationManager.IMPORTANCE_HIGH
            val channel = NotificationChannel(CHANNEL_ID, name, importance).apply { description = descriptionText }
            val notificationManager: NotificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }
    
    companion object {
        private const val TAG = "LittleGigMessaging"
        private const val CHANNEL_ID = "littlegig_channel"
    }
}