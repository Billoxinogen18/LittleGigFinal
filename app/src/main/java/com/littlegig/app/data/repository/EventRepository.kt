package com.littlegig.app.data.repository

import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.storage.FirebaseStorage
import com.littlegig.app.data.model.ContentCategory
import com.littlegig.app.data.model.Event
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class EventRepository @Inject constructor(
    private val firestore: FirebaseFirestore,
    private val storage: FirebaseStorage
) {
    
    fun getAllEvents(): Flow<List<Event>> = callbackFlow {
        val listener = firestore.collection("events")
            .whereEqualTo("isActive", true)
            .orderBy("createdAt", Query.Direction.DESCENDING)
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    trySend(emptyList())
                    return@addSnapshotListener
                }
                
                val events = snapshot?.documents?.mapNotNull { doc ->
                    doc.toObject(Event::class.java)?.copy(id = doc.id)
                } ?: emptyList()
                
                trySend(events)
            }
            
        awaitClose { listener.remove() }
    }
    
    fun getEventsByCategory(category: ContentCategory): Flow<List<Event>> = callbackFlow {
        val listener = firestore.collection("events")
            .whereEqualTo("category", category.name)
            .whereEqualTo("isActive", true)
            .orderBy("createdAt", Query.Direction.DESCENDING)
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    trySend(emptyList())
                    return@addSnapshotListener
                }
                
                val events = snapshot?.documents?.mapNotNull { doc ->
                    doc.toObject(Event::class.java)?.copy(id = doc.id)
                } ?: emptyList()
                
                trySend(events)
            }
            
        awaitClose { listener.remove() }
    }
    
    fun getFeaturedEvents(): Flow<List<Event>> = callbackFlow {
        val listener = firestore.collection("events")
            .whereEqualTo("isFeatured", true)
            .whereEqualTo("isActive", true)
            .orderBy("createdAt", Query.Direction.DESCENDING)
            .limit(10)
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    trySend(emptyList())
                    return@addSnapshotListener
                }
                
                val events = snapshot?.documents?.mapNotNull { doc ->
                    doc.toObject(Event::class.java)?.copy(id = doc.id)
                } ?: emptyList()
                
                trySend(events)
            }
            
        awaitClose { listener.remove() }
    }
    
    suspend fun getEventById(eventId: String): Result<Event> {
        return try {
            val document = firestore.collection("events")
                .document(eventId)
                .get()
                .await()
                
            if (document.exists()) {
                val event = document.toObject(Event::class.java)?.copy(id = document.id)
                    ?: return Result.failure(Exception("Failed to parse event"))
                Result.success(event)
            } else {
                Result.failure(Exception("Event not found"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    suspend fun createEvent(event: Event): Result<String> {
        return try {
            val docRef = firestore.collection("events").add(event).await()
            Result.success(docRef.id)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    suspend fun updateEvent(eventId: String, event: Event): Result<Unit> {
        return try {
            firestore.collection("events")
                .document(eventId)
                .set(event.copy(updatedAt = System.currentTimeMillis()))
                .await()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    suspend fun deleteEvent(eventId: String): Result<Unit> {
        return try {
            firestore.collection("events")
                .document(eventId)
                .update("isActive", false)
                .await()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    fun searchEvents(query: String): Flow<List<Event>> = callbackFlow {
        val listener = firestore.collection("events")
            .whereEqualTo("isActive", true)
            .orderBy("title")
            .startAt(query)
            .endAt(query + "\uf8ff")
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    trySend(emptyList())
                    return@addSnapshotListener
                }
                
                val events = snapshot?.documents?.mapNotNull { doc ->
                    doc.toObject(Event::class.java)?.copy(id = doc.id)
                } ?: emptyList()
                
                trySend(events)
            }
            
        awaitClose { listener.remove() }
    }
    
    fun getEventsByLocation(city: String): Flow<List<Event>> = callbackFlow {
        val listener = firestore.collection("events")
            .whereEqualTo("location.city", city)
            .whereEqualTo("isActive", true)
            .orderBy("createdAt", Query.Direction.DESCENDING)
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    trySend(emptyList())
                    return@addSnapshotListener
                }
                
                val events = snapshot?.documents?.mapNotNull { doc ->
                    doc.toObject(Event::class.java)?.copy(id = doc.id)
                } ?: emptyList()
                
                trySend(events)
            }
            
        awaitClose { listener.remove() }
    }
}