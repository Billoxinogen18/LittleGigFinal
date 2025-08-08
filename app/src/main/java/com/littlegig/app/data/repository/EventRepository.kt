package com.littlegig.app.data.repository

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.storage.FirebaseStorage
import com.littlegig.app.data.model.ContentCategory
import com.littlegig.app.data.model.Event
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.retry
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class EventRepository @Inject constructor(
    private val firestore: FirebaseFirestore,
    private val storage: FirebaseStorage,
    private val context: Context
) {
    
    // Offline cache for events
    private val eventCache = mutableMapOf<String, List<Event>>()
    private val cacheExpiry = mutableMapOf<String, Long>()
    private val CACHE_DURATION = 5 * 60 * 1000L // 5 minutes
    
    private fun isNetworkAvailable(): Boolean {
        val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val network = connectivityManager.activeNetwork ?: return false
        val activeNetwork = connectivityManager.getNetworkCapabilities(network) ?: return false
        
        return when {
            activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
            activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
            else -> false
        }
    }
    
    private fun getCachedEvents(key: String): List<Event>? {
        val expiry = cacheExpiry[key] ?: return null
        if (System.currentTimeMillis() > expiry) {
            eventCache.remove(key)
            cacheExpiry.remove(key)
            return null
        }
        return eventCache[key]
    }
    
    private fun cacheEvents(key: String, events: List<Event>) {
        eventCache[key] = events
        cacheExpiry[key] = System.currentTimeMillis() + CACHE_DURATION
    }
    
    fun getAllEvents(): Flow<List<Event>> = callbackFlow {
        val cacheKey = "all_events"
        
        // Try cache first
        getCachedEvents(cacheKey)?.let { cachedEvents ->
            println("🔥 DEBUG: Returning cached events: ${cachedEvents.size}")
            trySend(cachedEvents)
        }
        
        if (!isNetworkAvailable()) {
            // Return cached data if available, otherwise empty list
            val cachedData = getCachedEvents(cacheKey) ?: emptyList()
            println("🔥 DEBUG: No network, returning cached data: ${cachedData.size}")
            trySend(cachedData)
            return@callbackFlow
        }
        
        println("🔥 DEBUG: Starting Firestore query for events")
        val listener = firestore.collection("events")
            .whereEqualTo("isActive", true)
            .orderBy("createdAt", Query.Direction.DESCENDING)
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    println("🔥 DEBUG: Firestore error: ${error.message}")
                    // Try to return cached data on error
                    val cachedData = getCachedEvents(cacheKey) ?: emptyList()
                    println("🔥 DEBUG: Returning cached data on error: ${cachedData.size}")
                    trySend(cachedData)
                    return@addSnapshotListener
                }
                
                val events = snapshot?.documents?.mapNotNull { doc ->
                    doc.toObject(Event::class.java)?.copy(id = doc.id)
                } ?: emptyList()
                
                println("🔥 DEBUG: Firestore returned ${events.size} events")
                events.forEach { event ->
                    println("🔥 DEBUG: Event: ${event.title} - ${event.id}")
                }
                
                // Cache the events
                cacheEvents(cacheKey, events)
                trySend(events)
            }
            
        awaitClose { listener.remove() }
    }.retry(3) { cause ->
        // Retry up to 3 times with exponential backoff
        println("🔥 DEBUG: Retrying event query due to: ${cause.message}")
        cause is Exception && isNetworkAvailable()
    }.catch { error ->
        // Return cached data on error
        println("🔥 DEBUG: Caught error in event query: ${error.message}")
        val cachedData = getCachedEvents("all_events") ?: emptyList()
        println("🔥 DEBUG: Returning cached data on catch: ${cachedData.size}")
        emit(cachedData)
    }
    
    fun getEventsByCategory(category: ContentCategory): Flow<List<Event>> = callbackFlow {
        val cacheKey = "events_${category.name}"
        
        // Try cache first
        getCachedEvents(cacheKey)?.let { cachedEvents ->
            trySend(cachedEvents)
        }
        
        if (!isNetworkAvailable()) {
            trySend(getCachedEvents(cacheKey) ?: emptyList())
            return@callbackFlow
        }
        
        val listener = firestore.collection("events")
            .whereEqualTo("category", category.name)
            .whereEqualTo("isActive", true)
            .orderBy("createdAt", Query.Direction.DESCENDING)
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    val cachedData = getCachedEvents(cacheKey) ?: emptyList()
                    trySend(cachedData)
                    return@addSnapshotListener
                }
                
                val events = snapshot?.documents?.mapNotNull { doc ->
                    doc.toObject(Event::class.java)?.copy(id = doc.id)
                } ?: emptyList()
                
                cacheEvents(cacheKey, events)
                trySend(events)
            }
            
        awaitClose { listener.remove() }
    }.retry(3) { cause ->
        cause is Exception && isNetworkAvailable()
    }.catch { error ->
        val cachedData = getCachedEvents("events_${category.name}") ?: emptyList()
        emit(cachedData)
    }
    
    fun getFeaturedEvents(): Flow<List<Event>> = callbackFlow {
        val cacheKey = "featured_events"
        
        getCachedEvents(cacheKey)?.let { cachedEvents ->
            trySend(cachedEvents)
        }
        
        if (!isNetworkAvailable()) {
            trySend(getCachedEvents(cacheKey) ?: emptyList())
            return@callbackFlow
        }
        
        val listener = firestore.collection("events")
            .whereEqualTo("isFeatured", true)
            .whereEqualTo("isActive", true)
            .orderBy("createdAt", Query.Direction.DESCENDING)
            .limit(10)
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    val cachedData = getCachedEvents(cacheKey) ?: emptyList()
                    trySend(cachedData)
                    return@addSnapshotListener
                }
                
                val events = snapshot?.documents?.mapNotNull { doc ->
                    doc.toObject(Event::class.java)?.copy(id = doc.id)
                } ?: emptyList()
                
                cacheEvents(cacheKey, events)
                trySend(events)
            }
            
        awaitClose { listener.remove() }
    }.retry(3) { cause ->
        cause is Exception && isNetworkAvailable()
    }.catch { error ->
        val cachedData = getCachedEvents("featured_events") ?: emptyList()
        emit(cachedData)
    }
    
    suspend fun getEventById(eventId: String): Result<Event> {
        val cacheKey = "event_$eventId"
        
        // Try cache first
        getCachedEvents(cacheKey)?.firstOrNull()?.let { cachedEvent ->
            return Result.success(cachedEvent)
        }
        
        if (!isNetworkAvailable()) {
            return Result.failure(Exception("No network connection"))
        }
        
        return try {
            val document = firestore.collection("events")
                .document(eventId)
                .get()
                .await()
                
            if (document.exists()) {
                val event = document.toObject(Event::class.java)?.copy(id = document.id)
                    ?: return Result.failure(Exception("Failed to parse event"))
                
                // Cache the event
                cacheEvents(cacheKey, listOf(event))
                Result.success(event)
            } else {
                Result.failure(Exception("Event not found"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    suspend fun createEvent(event: Event): Result<Event> {
        if (!isNetworkAvailable()) {
            return Result.failure(Exception("No network connection"))
        }
        
        return try {
            val documentRef = firestore.collection("events")
                .add(event)
                .await()
            
            val createdEvent = event.copy(id = documentRef.id)
            
            // Clear cache to force refresh
            eventCache.clear()
            cacheExpiry.clear()
            
            Result.success(createdEvent)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    suspend fun updateEvent(eventId: String, updates: Map<String, Any>): Result<Unit> {
        if (!isNetworkAvailable()) {
            return Result.failure(Exception("No network connection"))
        }
        
        return try {
            firestore.collection("events")
                .document(eventId)
                .update(updates)
                .await()
            
            // Clear cache to force refresh
            eventCache.clear()
            cacheExpiry.clear()
            
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    suspend fun deleteEvent(eventId: String): Result<Unit> {
        if (!isNetworkAvailable()) {
            return Result.failure(Exception("No network connection"))
        }
        
        return try {
            firestore.collection("events")
                .document(eventId)
                .delete()
                .await()
            
            // Clear cache to force refresh
            eventCache.clear()
            cacheExpiry.clear()
            
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    suspend fun toggleEventLike(eventId: String, userId: String): Result<Unit> {
        return try {
            val eventRef = firestore.collection("events").document(eventId)
            val userRef = firestore.collection("users").document(userId)
            
            firestore.runTransaction { transaction ->
                val eventDoc = transaction.get(eventRef)
                val userDoc = transaction.get(userRef)
                
                val likedBy = eventDoc.get("likedBy") as? List<String> ?: emptyList()
                val userLikedEvents = userDoc.get("likedEvents") as? List<String> ?: emptyList()
                
                if (userId in likedBy) {
                    // Unlike
                    transaction.update(eventRef, "likedBy", likedBy - userId)
                    transaction.update(userRef, "likedEvents", userLikedEvents - eventId)
                } else {
                    // Like
                    transaction.update(eventRef, "likedBy", likedBy + userId)
                    transaction.update(userRef, "likedEvents", userLikedEvents + eventId)
                }
            }.await()
            
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    suspend fun isEventLikedByUser(eventId: String, userId: String): Result<Boolean> {
        return try {
            val eventDoc = firestore.collection("events").document(eventId).get().await()
            val likedBy = eventDoc.get("likedBy") as? List<String> ?: emptyList()
            Result.success(userId in likedBy)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    suspend fun rateEvent(eventId: String, userId: String, rating: Float): Result<Unit> {
        return try {
            val eventRef = firestore.collection("events").document(eventId)
            
            firestore.runTransaction { transaction ->
                val eventDoc = transaction.get(eventRef)
                val ratings = eventDoc.get("ratings") as? Map<String, Float> ?: emptyMap()
                
                val updatedRatings = ratings.toMutableMap()
                updatedRatings[userId] = rating
                
                transaction.update(eventRef, "ratings", updatedRatings)
            }.await()
            
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    suspend fun getUserEvents(userId: String): List<Event> {
        return try {
            val snapshot = firestore.collection("events")
                .whereEqualTo("organizerId", userId)
                .orderBy("createdAt", Query.Direction.DESCENDING)
                .get()
                .await()
            
            snapshot.documents.mapNotNull { doc ->
                doc.toObject(Event::class.java)?.copy(id = doc.id)
            }
        } catch (e: Exception) {
            emptyList()
        }
    }
    
    suspend fun getEvent(eventId: String): Result<Event?> {
        return try {
            val document = firestore.collection("events").document(eventId).get().await()
            
            if (document.exists()) {
                val event = document.toObject(Event::class.java)?.copy(id = document.id)
                Result.success(event)
            } else {
                Result.success(null)
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    // --- Growth features helpers ---
    
    /**
     * Count how many of the given followingIds have active tickets for the event.
     * Firestore limitation: whereIn supports up to 10 values. We chunk and sum.
     */
    suspend fun getFriendsGoingCount(eventId: String, followingIds: List<String>): Int {
        if (followingIds.isEmpty()) return 0
        return try {
            val chunks = followingIds.chunked(10)
            var total = 0
            for (chunk in chunks) {
                val snapshot = firestore.collection("tickets")
                    .whereEqualTo("eventId", eventId)
                    .whereIn("userId", chunk)
                    .get()
                    .await()
                total += snapshot.size()
            }
            total
        } catch (e: Exception) {
            0
        }
    }
    
    /** Add current user to the event waitlist. */
    suspend fun joinWaitlist(eventId: String, userId: String): Result<Unit> {
        return try {
            val doc = mapOf(
                "userId" to userId,
                "createdAt" to System.currentTimeMillis()
            )
            firestore.collection("events")
                .document(eventId)
                .collection("waitlist")
                .document(userId)
                .set(doc)
                .await()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    /** Subscribe current user to price-drop alerts for the event. */
    suspend fun subscribePriceDrop(eventId: String, userId: String): Result<Unit> {
        return try {
            val doc = mapOf(
                "userId" to userId,
                "createdAt" to System.currentTimeMillis()
            )
            firestore.collection("events")
                .document(eventId)
                .collection("priceAlerts")
                .document(userId)
                .set(doc)
                .await()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    /** RSVP to an event to receive reminders. */
    suspend fun rsvpEvent(eventId: String, userId: String): Result<Unit> {
        return try {
            val doc = mapOf(
                "userId" to userId,
                "createdAt" to System.currentTimeMillis()
            )
            firestore.collection("events")
                .document(eventId)
                .collection("rsvps")
                .document(userId)
                .set(doc)
                .await()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    fun clearCache() {
        eventCache.clear()
        cacheExpiry.clear()
    }
}