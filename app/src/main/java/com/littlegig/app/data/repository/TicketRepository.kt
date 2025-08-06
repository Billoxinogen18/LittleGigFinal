package com.littlegig.app.data.repository

import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.littlegig.app.data.model.Ticket
import com.littlegig.app.data.model.TicketStatus
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TicketRepository @Inject constructor(
    private val firestore: FirebaseFirestore
) {
    
    fun getUserTickets(userId: String): Flow<List<Ticket>> = callbackFlow {
        val listener = firestore.collection("tickets")
            .whereEqualTo("userId", userId)
            .orderBy("purchaseDate", Query.Direction.DESCENDING)
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    trySend(emptyList())
                    return@addSnapshotListener
                }
                
                val tickets = snapshot?.documents?.mapNotNull { doc ->
                    doc.toObject(Ticket::class.java)?.copy(id = doc.id)
                } ?: emptyList()
                
                trySend(tickets)
            }
            
        awaitClose { listener.remove() }
    }
    
    fun getEventTickets(eventId: String): Flow<List<Ticket>> = callbackFlow {
        val listener = firestore.collection("tickets")
            .whereEqualTo("eventId", eventId)
            .orderBy("purchaseDate", Query.Direction.DESCENDING)
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    trySend(emptyList())
                    return@addSnapshotListener
                }
                
                val tickets = snapshot?.documents?.mapNotNull { doc ->
                    doc.toObject(Ticket::class.java)?.copy(id = doc.id)
                } ?: emptyList()
                
                trySend(tickets)
            }
            
        awaitClose { listener.remove() }
    }
    
    suspend fun purchaseTicket(ticket: Ticket): Result<String> {
        return try {
            // Calculate 4% commission
            val commission = ticket.totalAmount * 0.04
            val ticketWithCommission = ticket.copy(
                commission = commission,
                qrCode = generateQRCode(ticket)
            )
            
            val docRef = firestore.collection("tickets")
                .add(ticketWithCommission)
                .await()
                
            // Update event ticket count
            firestore.collection("events")
                .document(ticket.eventId)
                .get()
                .await()
                .let { eventDoc ->
                    if (eventDoc.exists()) {
                        val currentSold = eventDoc.getLong("ticketsSold") ?: 0
                        firestore.collection("events")
                            .document(ticket.eventId)
                            .update("ticketsSold", currentSold + ticket.quantity)
                            .await()
                    }
                }
                
            Result.success(docRef.id)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    suspend fun cancelTicket(ticketId: String): Result<Unit> {
        return try {
            firestore.collection("tickets")
                .document(ticketId)
                .update("status", TicketStatus.CANCELLED.name)
                ?.await()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    suspend fun useTicket(ticketId: String): Result<Unit> {
        return try {
            firestore.collection("tickets")
                .document(ticketId)
                .update(
                    mapOf(
                        "status" to TicketStatus.USED.name,
                        "usedDate" to System.currentTimeMillis()
                    )
                )
                .await()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    suspend fun getTicketById(ticketId: String): Result<Ticket> {
        return try {
            val document = firestore.collection("tickets")
                .document(ticketId)
                .get()
                .await()
                
            if (document.exists()) {
                val ticket = document.toObject(Ticket::class.java)?.copy(id = document.id)
                    ?: return Result.failure(Exception("Failed to parse ticket"))
                Result.success(ticket)
            } else {
                Result.failure(Exception("Ticket not found"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    suspend fun getTotalRevenue(organizerId: String): Result<Double> {
        return try {
            val snapshot = firestore.collection("tickets")
                .whereEqualTo("organizerId", organizerId)
                .whereIn("status", listOf(TicketStatus.ACTIVE.name, TicketStatus.USED.name))
                .get()
                .await()
                
            val totalRevenue = snapshot.documents.sumOf { doc ->
                val ticket = doc.toObject(Ticket::class.java)
                (ticket?.totalAmount ?: 0.0) - (ticket?.commission ?: 0.0)
            }
            
            Result.success(totalRevenue)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    private fun generateQRCode(ticket: Ticket): String {
        // Simple QR code generation logic - in production, use proper QR code library
        return "LG-${ticket.eventId}-${ticket.userId}-${System.currentTimeMillis()}"
    }
}