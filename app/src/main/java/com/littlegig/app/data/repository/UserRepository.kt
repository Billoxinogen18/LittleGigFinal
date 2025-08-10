package com.littlegig.app.data.repository

import android.content.Context
import android.net.Uri
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import com.google.firebase.storage.FirebaseStorage
import com.littlegig.app.data.model.User
import com.littlegig.app.data.model.UserRank
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import javax.inject.Singleton
import java.util.*
import com.littlegig.app.services.PhoneNumberService

@Singleton
class UserRepository @Inject constructor(
    private val firestore: FirebaseFirestore,
    private val storage: FirebaseStorage,
    private val context: Context,
    private val phoneNumberService: PhoneNumberService
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
    
    suspend fun listAllUsers(limit: Int = 50): Result<List<User>> {
        return try {
            val snapshot = firestore.collection("users")
                .limit(limit.toLong())
                .get()
                .await()
            val users = snapshot.documents.mapNotNull { it.toObject(User::class.java)?.copy(id = it.id) }
            Result.success(users)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun listUsersPage(limit: Int = 50, startAfterId: String? = null): Result<List<User>> {
        return try {
            var query = firestore.collection("users").limit(limit.toLong())
            if (startAfterId != null) {
                val doc = firestore.collection("users").document(startAfterId).get().await()
                if (doc.exists()) {
                    query = query.startAfter(doc)
                }
            }
            val snapshot = query.get().await()
            val users = snapshot.documents.mapNotNull { it.toObject(User::class.java)?.copy(id = it.id) }
            Result.success(users)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getUsersByPhoneNumbers(phoneNumbers: List<String>): Result<List<User>> {
        return try {
            if (phoneNumbers.isEmpty()) return Result.success(emptyList())
            val normalized = phoneNumberService.normalizeMany(phoneNumbers)
            if (normalized.isEmpty()) return Result.success(emptyList())
            val batchSize = 10
            val results = mutableListOf<User>()
            for (chunk in normalized.chunked(batchSize)) {
                val snap = firestore.collection("users")
                    .whereIn("phoneNumber_e164", chunk)
                    .get()
                    .await()
                snap.documents.forEach { doc ->
                    val u = doc.toObject(User::class.java)?.copy(id = doc.id)
                    if (u != null) results.add(u)
                }
            }
            Result.success(results.distinctBy { it.id })
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun updateUserProfile(userId: String, updates: Map<String, Any>): Result<Unit> {
        return try {
            // derive normalized/index fields
            val lower = mutableMapOf<String, Any>()
            updates["username"]?.let { lower["username_lower"] = it.toString().lowercase() }
            updates["email"]?.let { lower["email_lower"] = it.toString().lowercase() }
            updates["displayName"]?.let { lower["displayName_lower"] = it.toString().lowercase() }
            updates["phoneNumber"]?.let {
                val e164 = phoneNumberService.normalizeToE164(it.toString())
                if (e164 != null) lower["phoneNumber_e164"] = e164
            }
            
            val merged = updates + lower
            // Use merge to create doc if it doesn't exist (handles anonymous users)
            firestore.collection("users").document(userId).set(merged, SetOptions.merge()).await()
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
            val q = query.trim()
            if (q.length < 2) return emptyList()
            val queryLower = q.lowercase()
            val results = mutableListOf<User>()

            // Server-side prefix queries on lowercase fields
            val usernameQuery = firestore.collection("users")
                .whereGreaterThanOrEqualTo("username_lower", queryLower)
                .whereLessThanOrEqualTo("username_lower", queryLower + '\uf8ff')
                .limit(20)
                .get()
                .await()
            usernameQuery.documents.forEach { doc ->
                doc.toObject(User::class.java)?.copy(id = doc.id)?.let { if (it !in results) results.add(it) }
            }

            val emailQuery = firestore.collection("users")
                .whereGreaterThanOrEqualTo("email_lower", queryLower)
                .whereLessThanOrEqualTo("email_lower", queryLower + '\uf8ff')
                .limit(20)
                .get()
                .await()
            emailQuery.documents.forEach { doc ->
                doc.toObject(User::class.java)?.copy(id = doc.id)?.let { if (it !in results) results.add(it) }
            }

            val nameQuery = firestore.collection("users")
                .whereGreaterThanOrEqualTo("displayName_lower", queryLower)
                .whereLessThanOrEqualTo("displayName_lower", queryLower + '\uf8ff')
                .limit(20)
                .get()
                .await()
            nameQuery.documents.forEach { doc ->
                doc.toObject(User::class.java)?.copy(id = doc.id)?.let { if (it !in results) results.add(it) }
            }

            // Phone: normalize possible phone query to E.164 and/or prefix match
            val normalizedPhone = phoneNumberService.normalizeToE164(q)
            if (normalizedPhone != null) {
                val phoneEq = firestore.collection("users")
                    .whereEqualTo("phoneNumber_e164", normalizedPhone)
                    .limit(20)
                    .get()
                    .await()
                phoneEq.documents.forEach { doc ->
                    doc.toObject(User::class.java)?.copy(id = doc.id)?.let { if (it !in results) results.add(it) }
                }
            }

            var deduped = results.distinctBy { it.id }
            if (deduped.isEmpty()) {
                // Fallback: fetch a page and filter on client, case-insensitive
                val page = firestore.collection("users").limit(100).get().await()
                val all = page.documents.mapNotNull { it.toObject(User::class.java)?.copy(id = it.id) }
                deduped = all.filter { u ->
                    u.username.contains(q, ignoreCase = true) ||
                    u.displayName.contains(q, ignoreCase = true) ||
                    u.email.contains(q, ignoreCase = true) ||
                    u.phoneNumber.contains(q, ignoreCase = true)
                }
            }
            deduped.take(20)
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

    suspend fun blockUser(requesterId: String, targetUserId: String): Result<Unit> {
        return try {
            val ref = firestore.collection("users").document(requesterId)
            firestore.runTransaction { tx ->
                val doc = tx.get(ref)
                val blocked = (doc.get("blockedUsers") as? List<String>) ?: emptyList()
                tx.update(ref, "blockedUsers", (blocked + targetUserId).distinct())
            }.await()
            Result.success(Unit)
        } catch (e: Exception) { Result.failure(e) }
    }

    suspend fun unblockUser(requesterId: String, targetUserId: String): Result<Unit> {
        return try {
            val ref = firestore.collection("users").document(requesterId)
            firestore.runTransaction { tx ->
                val doc = tx.get(ref)
                val blocked = (doc.get("blockedUsers") as? List<String>) ?: emptyList()
                tx.update(ref, "blockedUsers", blocked - targetUserId)
            }.await()
            Result.success(Unit)
        } catch (e: Exception) { Result.failure(e) }
    }

    suspend fun reportUser(reporterId: String, targetUserId: String, reason: String): Result<Unit> {
        return try {
            val data = mapOf(
                "reporterId" to reporterId,
                "targetUserId" to targetUserId,
                "reason" to reason,
                "createdAt" to System.currentTimeMillis()
            )
            firestore.collection("reports").add(data).await()
            Result.success(Unit)
        } catch (e: Exception) { Result.failure(e) }
    }
}

data class UserStats(
    val followersCount: Int,
    val followingCount: Int,
    val eventsCreated: Int,
    val eventsAttended: Int
) 