package com.littlegig.app.data.repository

import com.google.firebase.functions.FirebaseFunctions
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PaymentRepository @Inject constructor(
    private val functions: FirebaseFunctions
) {
    
    suspend fun processTicketPurchase(
        eventId: String,
        userId: String,
        amount: Double,
        eventTitle: String
    ): Result<String> {
        return try {
            val data = mapOf(
                "eventId" to eventId,
                "userId" to userId,
                "amount" to amount,
                "eventTitle" to eventTitle,
                "currency" to "KES"
            )
            
            val result = functions
                .getHttpsCallable("processTicketPurchase")
                .call(data)
                .await()
            
            val response = result.data as? Map<*, *>
            val paymentUrl = response?.get("paymentUrl") as? String
            
            if (paymentUrl != null) {
                Result.success(paymentUrl)
            } else {
                Result.failure(Exception("Failed to get payment URL"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    suspend fun verifyPayment(paymentReference: String): Result<Boolean> {
        return try {
            val data = mapOf(
                "paymentReference" to paymentReference
            )
            
            val result = functions
                .getHttpsCallable("verifyPayment")
                .call(data)
                .await()
            
            val response = result.data as? Map<*, *>
            val isSuccessful = response?.get("success") as? Boolean ?: false
            
            Result.success(isSuccessful)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    suspend fun getPaymentHistory(userId: String): Result<List<PaymentRecord>> {
        return try {
            val data = mapOf(
                "userId" to userId
            )
            
            val result = functions
                .getHttpsCallable("getPaymentHistory")
                .call(data)
                .await()
            
            val response = result.data as? Map<*, *>
            val payments = response?.get("payments") as? List<Map<*, *>> ?: emptyList()
            
            val paymentRecords = payments.mapNotNull { payment ->
                PaymentRecord(
                    id = payment["id"] as? String ?: "",
                    eventId = payment["eventId"] as? String ?: "",
                    eventTitle = payment["eventTitle"] as? String ?: "",
                    amount = (payment["amount"] as? Number)?.toDouble() ?: 0.0,
                    status = payment["status"] as? String ?: "pending",
                    date = (payment["date"] as? Number)?.toLong() ?: 0L,
                    paymentReference = payment["paymentReference"] as? String ?: ""
                )
            }
            
            Result.success(paymentRecords)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    suspend fun upgradeToBusinessAccount(userId: String, amount: Double = 5000.0): Result<String> {
        return try {
            val data = mapOf(
                "userId" to userId,
                "amount" to amount,
                "type" to "business_upgrade",
                "currency" to "KES"
            )
            
            val result = functions
                .getHttpsCallable("upgradeToBusinessAccount")
                .call(data)
                .await()
            
            val response = result.data as? Map<*, *>
            val paymentUrl = response?.get("paymentUrl") as? String
            
            if (paymentUrl != null) {
                Result.success(paymentUrl)
            } else {
                Result.failure(Exception("Failed to get payment URL for business upgrade"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}

data class PaymentRecord(
    val id: String,
    val eventId: String,
    val eventTitle: String,
    val amount: Double,
    val status: String,
    val date: Long,
    val paymentReference: String
)