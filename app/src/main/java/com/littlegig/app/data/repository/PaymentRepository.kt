package com.littlegig.app.data.repository

import android.app.Activity
import com.google.firebase.firestore.FirebaseFirestore
import com.littlegig.app.data.model.PaymentResult
import com.littlegig.app.services.FlutterwavePaymentService
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PaymentRepository @Inject constructor(
    private val firestore: FirebaseFirestore,
    private val flutterwaveService: FlutterwavePaymentService
) {
    
    suspend fun processTicketPayment(
        activity: Activity,
        eventId: String,
        userId: String,
        quantity: Int,
        totalAmount: Double,
        userEmail: String,
        userName: String,
        userPhone: String
    ): Result<PaymentResult> {
        return try {
            val result = flutterwaveService.processTicketPayment(
                activity = activity,
                eventId = eventId,
                userId = userId,
                quantity = quantity,
                totalAmount = totalAmount,
                userEmail = userEmail,
                userName = userName,
                userPhone = userPhone
            )
            
            if (result.isSuccess) {
                val paymentResult = result.getOrNull()!!
                
                // Save payment record to Firestore
                val paymentRecord = PaymentRecord(
                    userId = userId,
                    type = "ticket",
                    amount = totalAmount,
                    currency = "KES",
                    status = if (paymentResult.success) "completed" else "failed",
                    paymentId = paymentResult.paymentId,
                    description = "Ticket purchase for event $eventId",
                    timestamp = System.currentTimeMillis()
                )
                
                firestore.collection("payments").add(paymentRecord).await()
                
                Result.success(paymentResult)
            } else {
                Result.failure(result.exceptionOrNull() ?: Exception("Payment failed"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    suspend fun processInfluencerAdPayment(
        activity: Activity,
        advertisementId: String,
        influencerId: String,
        budget: Double,
        userEmail: String,
        userName: String,
        userPhone: String
    ): Result<PaymentResult> {
        return try {
            val result = flutterwaveService.processInfluencerAdPayment(
                activity = activity,
                advertisementId = advertisementId,
                influencerId = influencerId,
                budget = budget,
                userEmail = userEmail,
                userName = userName,
                userPhone = userPhone
            )
            
            if (result.isSuccess) {
                val paymentResult = result.getOrNull()!!
                
                // Save payment record to Firestore
                val paymentRecord = PaymentRecord(
                    userId = influencerId,
                    type = "advertisement",
                    amount = budget,
                    currency = "KES",
                    status = if (paymentResult.success) "completed" else "failed",
                    paymentId = paymentResult.paymentId,
                    description = "Advertisement payment for ad $advertisementId",
                    timestamp = System.currentTimeMillis()
                )
                
                firestore.collection("payments").add(paymentRecord).await()
                
                Result.success(paymentResult)
            } else {
                Result.failure(result.exceptionOrNull() ?: Exception("Payment failed"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    suspend fun getPaymentHistory(userId: String): Result<List<PaymentRecord>> {
        return try {
            val snapshot = firestore.collection("payments")
                .whereEqualTo("userId", userId)
                .orderBy("timestamp", com.google.firebase.firestore.Query.Direction.DESCENDING)
                .get()
                .await()
            
            val payments = snapshot.documents.mapNotNull { doc ->
                doc.toObject(PaymentRecord::class.java)?.copy(id = doc.id)
            }
            
            Result.success(payments)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    suspend fun refundPayment(paymentId: String, reason: String): Result<Boolean> {
        return try {
            // Update payment record status to refunded
            val paymentQuery = firestore.collection("payments")
                .whereEqualTo("paymentId", paymentId)
                .get()
                .await()
            
            if (!paymentQuery.isEmpty) {
                val paymentDoc = paymentQuery.documents.first()
                paymentDoc.reference.update(
                    mapOf(
                        "status" to "refunded",
                        "refundReason" to reason,
                        "refundDate" to System.currentTimeMillis()
                    )
                ).await()
                
                Result.success(true)
            } else {
                Result.failure(Exception("Payment not found"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}

data class PaymentRecord(
    val id: String = "",
    val userId: String = "",
    val type: String = "", // "ticket" or "advertisement"
    val amount: Double = 0.0,
    val currency: String = "KSH",
    val status: String = "", // "completed", "failed", "refunded"
    val paymentId: String = "",
    val description: String = "",
    val timestamp: Long = 0L
)