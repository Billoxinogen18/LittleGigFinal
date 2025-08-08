package com.littlegig.app.data.repository

import android.content.Context
import android.net.Uri
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.littlegig.app.data.model.User
import com.littlegig.app.data.model.UserRank
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import javax.inject.Singleton
import java.util.*

@Singleton
class UserRepository @Inject constructor(
    private val firestore: FirebaseFirestore,
    private val storage: FirebaseStorage,
    private val context: Context
) {
    
    suspend fun getUserById(userId: String): Result<User?> {
        return try {
            val document = firestore.collection("users").document(userId).get().await()
            
            if (document.exists()) {
                val user = document.toObject(User::class.java)?.copy(id = document.id)
                Result.success(user)
            } else {
                Result.success(null)
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    suspend fun updateUserProfile(userId: String, updates: Map<String, Any>): Result<Unit> {
        return try {
            firestore.collection("users").document(userId).update(updates).await()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    suspend fun uploadProfilePicture(userId: String, imageUri: Uri): Result<String> {
        return try {
            val imageRef = storage.reference.child("profile_pictures/$userId.jpg")
            val uploadTask = imageRef.putFile(imageUri).await()
            val downloadUrl = imageRef.downloadUrl.await()
            
            // Update user profile with new image URL
            firestore.collection("users").document(userId).update(
                mapOf("profilePictureUrl" to downloadUrl.toString())
            ).await()
            
            Result.success(downloadUrl.toString())
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    suspend fun toggleFollow(followerId: String, followingId: String): Result<Unit> {
        return try {
            val followerRef = firestore.collection("users").document(followerId)
            val followingRef = firestore.collection("users").document(followingId)
            
            firestore.runTransaction { transaction ->
                val followerDoc = transaction.get(followerRef)
                val followingDoc = transaction.get(followingRef)
                
                val followerFollowing = followerDoc.get("following") as? List<String> ?: emptyList()
                val followingFollowers = followingDoc.get("followers") as? List<String> ?: emptyList()
                
                if (followerId in followingFollowers) {
                    // Unfollow
                    transaction.update(followerRef, "following", followerFollowing - followingId)
                    transaction.update(followingRef, "followers", followingFollowers - followerId)
                } else {
                    // Follow
                    transaction.update(followerRef, "following", followerFollowing + followingId)
                    transaction.update(followingRef, "followers", followingFollowers + followerId)
                }
            }.await()
            
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    suspend fun isFollowing(followerId: String, followingId: String): Result<Boolean> {
        return try {
            val followerDoc = firestore.collection("users").document(followerId).get().await()
            val following = followerDoc.get("following") as? List<String> ?: emptyList()
            Result.success(followingId in following)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    suspend fun searchUsers(query: String): List<User> {
        return try {
            val queryLower = query.lowercase()
            
            // Search by username, email, displayName, and phoneNumber
            val results = mutableListOf<User>()
            
            // Search by username
            val usernameQuery = firestore.collection("users")
                .whereGreaterThanOrEqualTo("username", queryLower)
                .whereLessThanOrEqualTo("username", queryLower + '\uf8ff')
                .limit(10)
                .get()
                .await()
            
            usernameQuery.documents.forEach { doc ->
                val user = doc.toObject(User::class.java)?.copy(id = doc.id)
                if (user != null && user !in results) {
                    results.add(user)
                }
            }
            
            // Search by email
            val emailQuery = firestore.collection("users")
                .whereGreaterThanOrEqualTo("email", queryLower)
                .whereLessThanOrEqualTo("email", queryLower + '\uf8ff')
                .limit(10)
                .get()
                .await()
            
            emailQuery.documents.forEach { doc ->
                val user = doc.toObject(User::class.java)?.copy(id = doc.id)
                if (user != null && user !in results) {
                    results.add(user)
                }
            }
            
            // Search by displayName
            val nameQuery = firestore.collection("users")
                .whereGreaterThanOrEqualTo("displayName", queryLower)
                .whereLessThanOrEqualTo("displayName", queryLower + '\uf8ff')
                .limit(10)
                .get()
                .await()
            
            nameQuery.documents.forEach { doc ->
                val user = doc.toObject(User::class.java)?.copy(id = doc.id)
                if (user != null && user !in results) {
                    results.add(user)
                }
            }
            
            // Search by phoneNumber
            val phoneQuery = firestore.collection("users")
                .whereGreaterThanOrEqualTo("phoneNumber", query)
                .whereLessThanOrEqualTo("phoneNumber", query + '\uf8ff')
                .limit(10)
                .get()
                .await()
            
            phoneQuery.documents.forEach { doc ->
                val user = doc.toObject(User::class.java)?.copy(id = doc.id)
                if (user != null && user !in results) {
                    results.add(user)
                }
            }
            
            // Remove duplicates and limit results
            results.distinctBy { it.id }.take(20)
        } catch (e: Exception) {
            emptyList()
        }
    }
    
    suspend fun followUser(followerId: String, followingId: String): Result<Unit> {
        return try {
            val followerRef = firestore.collection("users").document(followerId)
            val followingRef = firestore.collection("users").document(followingId)
            
            firestore.runTransaction { transaction ->
                val followerDoc = transaction.get(followerRef)
                val followingDoc = transaction.get(followingRef)
                
                val followerFollowing = followerDoc.get("following") as? List<String> ?: emptyList()
                val followingFollowers = followingDoc.get("followers") as? List<String> ?: emptyList()
                
                transaction.update(followerRef, "following", followerFollowing + followingId)
                transaction.update(followingRef, "followers", followingFollowers + followerId)
            }.await()
            
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    suspend fun unfollowUser(followerId: String, followingId: String): Result<Unit> {
        return try {
            val followerRef = firestore.collection("users").document(followerId)
            val followingRef = firestore.collection("users").document(followingId)
            
            firestore.runTransaction { transaction ->
                val followerDoc = transaction.get(followerRef)
                val followingDoc = transaction.get(followingRef)
                
                val followerFollowing = followerDoc.get("following") as? List<String> ?: emptyList()
                val followingFollowers = followingDoc.get("followers") as? List<String> ?: emptyList()
                
                transaction.update(followerRef, "following", followerFollowing - followingId)
                transaction.update(followingRef, "followers", followingFollowers - followerId)
            }.await()
            
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    suspend fun getFollowers(userId: String): Result<List<User>> {
        return try {
            val userDoc = firestore.collection("users").document(userId).get().await()
            val followerIds = userDoc.get("followers") as? List<String> ?: emptyList()
            
            val followers = mutableListOf<User>()
            for (followerId in followerIds) {
                val followerDoc = firestore.collection("users").document(followerId).get().await()
                if (followerDoc.exists()) {
                    val follower = followerDoc.toObject(User::class.java)?.copy(id = followerDoc.id)
                    if (follower != null) {
                        followers.add(follower)
                    }
                }
            }
            
            Result.success(followers)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    suspend fun getFollowing(userId: String): Result<List<User>> {
        return try {
            val userDoc = firestore.collection("users").document(userId).get().await()
            val followingIds = userDoc.get("following") as? List<String> ?: emptyList()
            
            val following = mutableListOf<User>()
            for (followingId in followingIds) {
                val followingDoc = firestore.collection("users").document(followingId).get().await()
                if (followingDoc.exists()) {
                    val user = followingDoc.toObject(User::class.java)?.copy(id = followingDoc.id)
                    if (user != null) {
                        following.add(user)
                    }
                }
            }
            
            Result.success(following)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    

    
    suspend fun updateUserRank(userId: String, newRank: UserRank): Result<Unit> {
        return try {
            firestore.collection("users").document(userId).update(
                mapOf("rank" to newRank.name)
            ).await()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    suspend fun getUserStats(userId: String): Result<UserStats> {
        return try {
            val userDoc = firestore.collection("users").document(userId).get().await()
            
            val followersCount = (userDoc.get("followers") as? List<*>)?.size ?: 0
            val followingCount = (userDoc.get("following") as? List<*>)?.size ?: 0
            
            // Get events created by user
            val eventsSnapshot = firestore.collection("events")
                .whereEqualTo("organizerId", userId)
                .get()
                .await()
            val eventsCreated = eventsSnapshot.size()
            
            // Get events attended by user
            val ticketsSnapshot = firestore.collection("tickets")
                .whereEqualTo("userId", userId)
                .get()
                .await()
            val eventsAttended = ticketsSnapshot.size()
            
            Result.success(
                UserStats(
                    followersCount = followersCount,
                    followingCount = followingCount,
                    eventsCreated = eventsCreated,
                    eventsAttended = eventsAttended
                )
            )
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    suspend fun deleteUser(userId: String): Result<Unit> {
        return try {
            // Delete user's profile picture from storage
            try {
                storage.reference.child("profile_pictures/$userId.jpg").delete().await()
            } catch (e: Exception) {
                // Ignore if file doesn't exist
            }
            
            // Delete user document
            firestore.collection("users").document(userId).delete().await()
            
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}

data class UserStats(
    val followersCount: Int,
    val followingCount: Int,
    val eventsCreated: Int,
    val eventsAttended: Int
) 