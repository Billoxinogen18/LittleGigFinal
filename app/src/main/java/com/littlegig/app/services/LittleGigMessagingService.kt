package com.littlegig.app.services

import android.util.Log
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class LittleGigMessagingService : FirebaseMessagingService() {
    
    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        super.onMessageReceived(remoteMessage)
        Log.d(TAG, "From: ${remoteMessage.from}")
        if (remoteMessage.data.isNotEmpty()) {
            Log.d(TAG, "Message data payload: ${remoteMessage.data}")
            handleDataMessage(remoteMessage.data)
        }
        remoteMessage.notification?.let {
            Log.d(TAG, "Message Notification Body: ${it.body}")
        }
    }
    
    override fun onNewToken(token: String) {
        Log.d(TAG, "Refreshed token: $token")
        sendRegistrationToServer(token)
    }
    
    private fun handleDataMessage(data: Map<String, String>) {
        val type = data["type"]
        when (type) {
            "event_reminder" -> { }
            "ticket_update" -> { }
            "new_event" -> { }
            "payment_confirmation" -> { }
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
            } else {
                Log.d(TAG, "No signed-in user; token will be saved on next sign-in")
            }
        } catch (e: Exception) {
            Log.w(TAG, "Error saving FCM token", e)
        }
    }
    
    companion object {
        private const val TAG = "LittleGigMessaging"
    }
}