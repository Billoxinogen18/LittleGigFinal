package com.littlegig.app.data.repository

import android.content.Context
import android.net.Uri
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.littlegig.app.data.model.Recap
import com.littlegig.app.data.model.RecapType
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.channels.awaitClose
import javax.inject.Inject
import javax.inject.Singleton
import java.util.*

@Singleton
class RecapRepository @Inject constructor(
    private val firestore: FirebaseFirestore,
    private val storage: FirebaseStorage,
    private val context: Context
) {
    
    fun getEventRecaps(eventId: String): Flow<List<Recap>> = callbackFlow {
        val listener = firestore.collection("recaps")
            .whereEqualTo("eventId", eventId)
            .orderBy("createdAt", com.google.firebase.firestore.Query.Direction.DESCENDING)
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    close(error)
                    return@addSnapshotListener
                }
                
                val recaps = snapshot?.documents?.mapNotNull { doc ->
                    doc.toObject(Recap::class.java)?.copy(id = doc.id)
                } ?: emptyList()
                
                trySend(recaps)
            }
            
        awaitClose { listener.remove() }
    }
    
    fun getUserRecaps(userId: String): Flow<List<Recap>> = callbackFlow {
        val listener = firestore.collection("recaps")
            .whereEqualTo("userId", userId)
            .orderBy("createdAt", com.google.firebase.firestore.Query.Direction.DESCENDING)
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    close(error)
                    return@addSnapshotListener
                }
                
                val recaps = snapshot?.documents?.mapNotNull { doc ->
                    doc.toObject(Recap::class.java)?.copy(id = doc.id)
                } ?: emptyList()
                
                trySend(recaps)
            }
            
        awaitClose { listener.remove() }
    }
    
    suspend fun uploadRecap(
        eventId: String,
        userId: String,
        mediaUrls: List<String>,
        caption: String?,
        userLocation: Pair<Double, Double>?,
        eventLocation: Pair<Double, Double>?
    ): Result<String> {
        return try {
            // Verify location if provided
            if (userLocation != null && eventLocation != null) {
                val distance = calculateDistance(
                    userLocation.first, userLocation.second,
                    eventLocation.first, eventLocation.second
                )
                
                if (distance > 3.0) { // 3km radius
                    return Result.failure(Exception("You must be within 3km of the event to upload a recap"))
                }
            }
            
            val recap = Recap(
                eventId = eventId,
                userId = userId,
                mediaUrls = mediaUrls,
                caption = caption,
                recapType = RecapType.PHOTO,
                createdAt = System.currentTimeMillis(),
                likes = 0,
                views = 0
            )
            
            val recapRef = firestore.collection("recaps").add(recap).await()
            
            Result.success(recapRef.id)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    suspend fun uploadRecapMedia(
        userId: String,
        eventId: String,
        mediaUri: Uri,
        mediaType: String
    ): Result<String> {
        return try {
            val fileName = "recaps/${eventId}/${userId}_${System.currentTimeMillis()}.$mediaType"
            val mediaRef = storage.reference.child(fileName)
            
            val uploadTask = mediaRef.putFile(mediaUri).await()
            val downloadUrl = mediaRef.downloadUrl.await()
            
            Result.success(downloadUrl.toString())
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    suspend fun likeRecap(recapId: String, userId: String): Result<Unit> {
        return try {
            val recapRef = firestore.collection("recaps").document(recapId)
            
            firestore.runTransaction { transaction ->
                val recapDoc = transaction.get(recapRef)
                val likedBy = recapDoc.get("likedBy") as? List<String> ?: emptyList()
                val currentLikes = recapDoc.getLong("likes") ?: 0
                
                if (userId in likedBy) {
                    // Unlike
                    transaction.update(recapRef, mapOf(
                        "likedBy" to likedBy - userId,
                        "likes" to currentLikes - 1
                    ))
                } else {
                    // Like
                    transaction.update(recapRef, mapOf(
                        "likedBy" to likedBy + userId,
                        "likes" to currentLikes + 1
                    ))
                }
            }.await()
            
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    suspend fun viewRecap(recapId: String, userId: String): Result<Unit> {
        return try {
            val recapRef = firestore.collection("recaps").document(recapId)
            
            firestore.runTransaction { transaction ->
                val recapDoc = transaction.get(recapRef)
                val viewedBy = recapDoc.get("viewedBy") as? List<String> ?: emptyList()
                val currentViews = recapDoc.getLong("views") ?: 0
                
                if (userId !in viewedBy) {
                    transaction.update(recapRef, mapOf(
                        "viewedBy" to viewedBy + userId,
                        "views" to currentViews + 1
                    ))
                }
            }.await()
            
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    suspend fun addRecapComment(
        recapId: String,
        userId: String,
        comment: String
    ): Result<Unit> {
        return try {
            val commentData = mapOf(
                "userId" to userId,
                "comment" to comment,
                "timestamp" to System.currentTimeMillis()
            )
            
            firestore.collection("recaps")
                .document(recapId)
                .collection("comments")
                .add(commentData)
                .await()
            
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    suspend fun getRecapComments(recapId: String): Result<List<RecapComment>> {
        return try {
            val snapshot = firestore.collection("recaps")
                .document(recapId)
                .collection("comments")
                .orderBy("timestamp", com.google.firebase.firestore.Query.Direction.ASCENDING)
                .get()
                .await()
            
            val comments = snapshot.documents.mapNotNull { doc ->
                RecapComment(
                    id = doc.id,
                    userId = doc.getString("userId") ?: "",
                    comment = doc.getString("comment") ?: "",
                    timestamp = doc.getTimestamp("timestamp")?.toDate() ?: Date()
                )
            }
            
            Result.success(comments)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    suspend fun deleteRecap(recapId: String, userId: String): Result<Unit> {
        return try {
            // Verify ownership
            val recapDoc = firestore.collection("recaps").document(recapId).get().await()
            val recapUserId = recapDoc.getString("userId")
            
            if (recapUserId != userId) {
                return Result.failure(Exception("You can only delete your own recaps"))
            }
            
            // Delete media files from storage
            val mediaUrls = recapDoc.get("mediaUrls") as? List<String> ?: emptyList()
            for (mediaUrl in mediaUrls) {
                try {
                    storage.getReferenceFromUrl(mediaUrl).delete().await()
                } catch (e: Exception) {
                    // Ignore if file doesn't exist
                }
            }
            
            // Delete comments
            val commentsSnapshot = firestore.collection("recaps")
                .document(recapId)
                .collection("comments")
                .get()
                .await()
            
            val batch = firestore.batch()
            commentsSnapshot.documents.forEach { doc ->
                batch.delete(doc.reference)
            }
            
            // Delete recap document
            batch.delete(firestore.collection("recaps").document(recapId))
            
            batch.commit().await()
            
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    suspend fun getRecapStats(recapId: String): Result<RecapStats> {
        return try {
            val recapDoc = firestore.collection("recaps").document(recapId).get().await()
            
            val likes = recapDoc.getLong("likes")?.toInt() ?: 0
            val views = recapDoc.getLong("views")?.toInt() ?: 0
            
            // Get comment count
            val commentsSnapshot = firestore.collection("recaps")
                .document(recapId)
                .collection("comments")
                .get()
                .await()
            
            val commentCount = commentsSnapshot.size()
            
            Result.success(
                RecapStats(
                    likes = likes,
                    views = views,
                    comments = commentCount
                )
            )
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    private fun calculateDistance(
        lat1: Double, lon1: Double,
        lat2: Double, lon2: Double
    ): Double {
        val r = 6371 // Earth's radius in kilometers
        
        val latDistance = Math.toRadians(lat2 - lat1)
        val lonDistance = Math.toRadians(lon2 - lon1)
        
        val a = kotlin.math.sin(latDistance / 2) * kotlin.math.sin(latDistance / 2) +
                kotlin.math.cos(Math.toRadians(lat1)) * kotlin.math.cos(Math.toRadians(lat2)) *
                kotlin.math.sin(lonDistance / 2) * kotlin.math.sin(lonDistance / 2)
        
        val c = 2 * kotlin.math.atan2(kotlin.math.sqrt(a), kotlin.math.sqrt(1 - a))
        
        return r * c
    }
}

data class RecapComment(
    val id: String,
    val userId: String,
    val comment: String,
    val timestamp: Date
)

data class RecapStats(
    val likes: Int,
    val views: Int,
    val comments: Int
) 