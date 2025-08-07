package com.littlegig.app.data.repository

import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.functions.FirebaseFunctions
import com.google.firebase.firestore.Query
import com.littlegig.app.data.model.Chat
import com.littlegig.app.data.model.Message
import com.littlegig.app.data.model.ChatType
import com.littlegig.app.data.model.MessageType
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.channels.awaitClose
import javax.inject.Inject
import javax.inject.Singleton
import java.util.*

@Singleton
class ChatRepository @Inject constructor(
    private val firestore: FirebaseFirestore,
    private val functions: FirebaseFunctions
) {
    
    fun getUserChats(userId: String): Flow<List<Chat>> = callbackFlow {
        val listener = firestore.collection("chats")
            .whereArrayContains("participants", userId)
            .orderBy("lastMessageTime", Query.Direction.DESCENDING)
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    close(error)
                    return@addSnapshotListener
                }
                
                val chats = snapshot?.documents?.mapNotNull { doc ->
                    doc.toObject(Chat::class.java)?.copy(id = doc.id)
                } ?: emptyList()
                
                trySend(chats)
            }
            
        awaitClose { listener.remove() }
    }

    suspend fun setAnnouncement(chatId: String, text: String): Result<Unit> {
        return try {
            val data = mapOf("chatId" to chatId, "text" to text)
            functions.getHttpsCallable("setAnnouncement").call(data).await()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun pinMessage(chatId: String, messageId: String): Result<Unit> {
        return try {
            val data = mapOf("chatId" to chatId, "messageId" to messageId)
            functions.getHttpsCallable("pinMessage").call(data).await()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    fun getChatMessages(chatId: String): Flow<List<Message>> = callbackFlow {
        val listener = firestore.collection("chats")
            .document(chatId)
            .collection("messages")
            .orderBy("timestamp", Query.Direction.ASCENDING)
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    close(error)
                    return@addSnapshotListener
                }
                
                val messages = snapshot?.documents?.mapNotNull { doc ->
                    doc.toObject(Message::class.java)?.copy(id = doc.id)
                } ?: emptyList()
                
                trySend(messages)
            }
            
        awaitClose { listener.remove() }
    }

    fun getChat(chatId: String): Flow<Chat?> = callbackFlow {
        val listener = firestore.collection("chats")
            .document(chatId)
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    close(error)
                    return@addSnapshotListener
                }
                val chat = snapshot?.toObject(Chat::class.java)?.copy(id = snapshot.id)
                trySend(chat)
            }
        awaitClose { listener.remove() }
    }

    suspend fun getMessage(chatId: String, messageId: String): Result<Message?> {
        return try {
            val doc = firestore.collection("chats")
                .document(chatId)
                .collection("messages")
                .document(messageId)
                .get()
                .await()
            val message = if (doc.exists()) doc.toObject(Message::class.java)?.copy(id = doc.id) else null
            Result.success(message)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    suspend fun sendMessage(chatId: String, message: Message): Result<Unit> {
        return try {
            val messageRef = firestore.collection("chats")
                .document(chatId)
                .collection("messages")
                .add(message)
                .await()
            
            // Update chat's last message
            firestore.collection("chats").document(chatId).update(
                mapOf(
                    "lastMessage" to message.content,
                    "lastMessageTime" to message.timestamp,
                    "lastMessageSenderId" to message.senderId
                )
            ).await()
            
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    suspend fun createChat(
        participants: List<String>,
        chatType: ChatType,
        name: String? = null,
        eventId: String? = null
    ): Result<String> {
        return try {
            val chat = Chat(
                participants = participants,
                chatType = chatType,
                name = name,
                eventId = eventId,
                createdAt = System.currentTimeMillis(),
                lastMessageTime = System.currentTimeMillis()
            )
            
            val chatRef = firestore.collection("chats").add(chat).await()
            
            Result.success(chatRef.id)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    suspend fun createEventChat(eventId: String, organizerId: String): Result<String> {
        return try {
            // Get event details
            val eventDoc = firestore.collection("events").document(eventId).get().await()
            val eventTitle = eventDoc.getString("title") ?: "Event Chat"
            
            val chat = Chat(
                participants = listOf(organizerId),
                chatType = ChatType.EVENT,
                name = "$eventTitle Chat",
                eventId = eventId,
                createdAt = System.currentTimeMillis(),
                lastMessageTime = System.currentTimeMillis()
            )
            
            val chatRef = firestore.collection("chats").add(chat).await()
            
            Result.success(chatRef.id)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    suspend fun addParticipantToChat(chatId: String, userId: String): Result<Unit> {
        return try {
            firestore.collection("chats").document(chatId).update(
                mapOf("participants" to com.google.firebase.firestore.FieldValue.arrayUnion(userId))
            ).await()
            
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    suspend fun removeParticipantFromChat(chatId: String, userId: String): Result<Unit> {
        return try {
            firestore.collection("chats").document(chatId).update(
                mapOf("participants" to com.google.firebase.firestore.FieldValue.arrayRemove(userId))
            ).await()
            
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    suspend fun shareTicketInChat(
        chatId: String,
        senderId: String,
        eventId: String,
        ticketCount: Int
    ): Result<Unit> {
        return try {
            val eventDoc = firestore.collection("events").document(eventId).get().await()
            val eventTitle = eventDoc.getString("title") ?: "Event"
            
            val message = Message(
                senderId = senderId,
                content = "🎫 Sharing $ticketCount ticket(s) for $eventTitle",
                messageType = MessageType.TICKET_SHARE,
                metadata = mapOf(
                    "eventId" to eventId,
                    "ticketCount" to ticketCount.toString(),
                    "eventTitle" to eventTitle
                ),
                timestamp = System.currentTimeMillis()
            )
            
            sendMessage(chatId, message)
            
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    suspend fun markMessageAsRead(chatId: String, messageId: String, userId: String): Result<Unit> {
        return try {
            firestore.collection("chats")
                .document(chatId)
                .collection("messages")
                .document(messageId)
                .update(
                    mapOf("readBy" to com.google.firebase.firestore.FieldValue.arrayUnion(userId))
                ).await()
            
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    suspend fun deleteChat(chatId: String): Result<Unit> {
        return try {
            // Delete all messages in the chat
            val messagesSnapshot = firestore.collection("chats")
                .document(chatId)
                .collection("messages")
                .get()
                .await()
            
            val batch = firestore.batch()
            messagesSnapshot.documents.forEach { doc ->
                batch.delete(doc.reference)
            }
            
            // Delete the chat document
            batch.delete(firestore.collection("chats").document(chatId))
            
            batch.commit().await()
            
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    suspend fun getUnreadMessageCount(userId: String): Result<Int> {
        return try {
            val snapshot = firestore.collection("chats")
                .whereArrayContains("participants", userId)
                .get()
                .await()
            
            var totalUnread = 0
            
            for (chatDoc in snapshot.documents) {
                val lastMessageSenderId = chatDoc.getString("lastMessageSenderId")
                if (lastMessageSenderId != userId) {
                    // Check if user has read the last message
                    val lastMessageTime = chatDoc.getTimestamp("lastMessageTime")
                    if (lastMessageTime != null) {
                        // This is a simplified check - in a real app you'd track read status per message
                        totalUnread++
                    }
                }
            }
            
            Result.success(totalUnread)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
} 