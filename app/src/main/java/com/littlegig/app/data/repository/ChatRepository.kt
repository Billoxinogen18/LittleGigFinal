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
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    close(error)
                    return@addSnapshotListener
                }

                val chats = snapshot?.documents
                    ?.mapNotNull { doc -> doc.toObject(Chat::class.java)?.copy(id = doc.id) }
                    ?.sortedByDescending { it.lastMessageTime }
                    ?: emptyList()

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

    fun getTypingIndicators(chatId: String): Flow<List<Map<String, Any>>> = callbackFlow {
        val listener = firestore.collection("chats")
            .document(chatId)
            .collection("typing")
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    close(error)
                    return@addSnapshotListener
                }
                val indicators = snapshot?.documents?.mapNotNull { it.data } ?: emptyList()
                trySend(indicators)
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
    
    suspend fun deleteMessage(chatId: String, messageId: String): Result<Unit> {
        return try {
            firestore.collection("chats")
                .document(chatId)
                .collection("messages")
                .document(messageId)
                .delete()
                .await()
            Result.success(Unit)
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

    suspend fun markMessageDelivered(chatId: String, messageId: String, userId: String): Result<Unit> {
        return try {
            firestore.collection("chats")
                .document(chatId)
                .collection("messages")
                .document(messageId)
                .update(mapOf("deliveredTo" to com.google.firebase.firestore.FieldValue.arrayUnion(userId)))
                .await()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getUserChatsPage(userId: String, limit: Int = 30, startAfterTime: Long? = null): Result<List<Chat>> {
        return try {
            var query = firestore.collection("chats")
                .whereArrayContains("participants", userId)
                .orderBy("lastMessageTime", Query.Direction.DESCENDING)
                .limit(limit.toLong())
            if (startAfterTime != null) {
                query = query.startAfter(startAfterTime)
            }
            val snapshot = query.get().await()
            val chats = snapshot.documents.mapNotNull { it.toObject(Chat::class.java)?.copy(id = it.id) }
            Result.success(chats)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun setTypingStatus(chatId: String, userId: String, isTyping: Boolean): Result<Unit> {
        return try {
            val typingDoc = firestore.collection("chats")
                .document(chatId)
                .collection("typing")
                .document(userId)
            val data = mapOf(
                "userId" to userId,
                "isTyping" to isTyping,
                "timestamp" to System.currentTimeMillis()
            )
            typingDoc.set(data).await()
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

    suspend fun createOrGetDirectChat(userAId: String, userBId: String): Result<String> {
        return try {
            // Try to find existing direct chat that contains both users
            val snapshot = firestore.collection("chats")
                .whereArrayContains("participants", userAId)
                .get()
                .await()

            val existing = snapshot.documents.firstOrNull { doc ->
                val participants = (doc.get("participants") as? List<*>)?.map { it as String } ?: emptyList()
                participants.contains(userBId) && participants.size == 2
            }

            if (existing != null) {
                return Result.success(existing.id)
            }

            // Create new direct chat
            val participants = listOf(userAId, userBId)
            val chat = Chat(
                participants = participants,
                chatType = ChatType.DIRECT,
                name = null,
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

    suspend fun pinChatForUser(userId: String, chatId: String): Result<Unit> {
        return try {
            val userRef = firestore.collection("users").document(userId)
            firestore.runTransaction { tx ->
                val doc = tx.get(userRef)
                val current = (doc.get("pinnedChats") as? List<String>) ?: emptyList()
                tx.update(userRef, "pinnedChats", (current + chatId).distinct())
            }.await()
            Result.success(Unit)
        } catch (e: Exception) { Result.failure(e) }
    }

    suspend fun unpinChatForUser(userId: String, chatId: String): Result<Unit> {
        return try {
            val userRef = firestore.collection("users").document(userId)
            firestore.runTransaction { tx ->
                val doc = tx.get(userRef)
                val current = (doc.get("pinnedChats") as? List<String>) ?: emptyList()
                tx.update(userRef, "pinnedChats", current - chatId)
            }.await()
            Result.success(Unit)
        } catch (e: Exception) { Result.failure(e) }
    }

    suspend fun searchChatsLocally(chats: List<Chat>, query: String): List<Chat> {
        val q = query.trim()
        if (q.isBlank()) return chats
        return chats.filter { c ->
            (c.name ?: "").contains(q, ignoreCase = true) || (c.lastMessage ?: "").contains(q, ignoreCase = true)
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

    suspend fun redeemSharedTicket(chatId: String, messageId: String, ticketId: String): Result<Unit> {
        return try {
            val data = mapOf(
                "chatId" to chatId,
                "messageId" to messageId,
                "ticketId" to ticketId
            )
            functions.getHttpsCallable("redeemTicket").call(data).await()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun toggleReaction(chatId: String, messageId: String, userId: String, reaction: String): Result<Unit> {
        return try {
            val docRef = firestore.collection("chats")
                .document(chatId)
                .collection("messages")
                .document(messageId)
            firestore.runTransaction { tx ->
                val snap = tx.get(docRef)
                val current = (snap.get("reactions") as? Map<String, String>) ?: emptyMap()
                val updated = if (current[userId] == reaction) current - userId else current + (userId to reaction)
                tx.update(docRef, "reactions", updated)
            }.await()
            Result.success(Unit)
        } catch (e: Exception) { Result.failure(e) }
    }
} 