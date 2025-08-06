package com.littlegig.app.services

import android.util.Log
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

class LittleGigMessagingService : FirebaseMessagingService() {
    
    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        super.onMessageReceived(remoteMessage)
        
        Log.d(TAG, "From: ${remoteMessage.from}")
        
        // Check if message contains a data payload
        if (remoteMessage.data.isNotEmpty()) {
            Log.d(TAG, "Message data payload: ${remoteMessage.data}")
            
            // Handle data payload
            handleDataMessage(remoteMessage.data)
        }
        
        // Check if message contains a notification payload
        remoteMessage.notification?.let {
            Log.d(TAG, "Message Notification Body: ${it.body}")
            // Handle notification
        }
    }
    
    override fun onNewToken(token: String) {
        Log.d(TAG, "Refreshed token: $token")
        
        // Send token to server
        sendRegistrationToServer(token)
    }
    
    private fun handleDataMessage(data: Map<String, String>) {
        val type = data["type"]
        
        when (type) {
            "event_reminder" -> {
                // Handle event reminder notification
            }
            "ticket_update" -> {
                // Handle ticket status update
            }
            "new_event" -> {
                // Handle new event notification
            }
            "payment_confirmation" -> {
                // Handle payment confirmation
            }
        }
    }
    
    private fun sendRegistrationToServer(token: String) {
        // In a real app, send this token to your server
        // so it can send push notifications to this device
        Log.d(TAG, "Sending token to server: $token")
    }
    
    companion object {
        private const val TAG = "LittleGigMessaging"
    }
}