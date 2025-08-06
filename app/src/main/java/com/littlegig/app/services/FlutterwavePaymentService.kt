package com.littlegig.app.services

import android.app.Activity
import android.content.Intent
import com.google.firebase.functions.FirebaseFunctions
import com.littlegig.app.data.model.PaymentResult
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FlutterwavePaymentService @Inject constructor(
    private val functions: FirebaseFunctions
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
            // Initialize Flutterwave payment
            val initData = hashMapOf(
                "amount" to totalAmount,
                "currency" to "KES",
                "email" to userEmail,
                "phone_number" to userPhone,
                "tx_ref" to "TICKET_${System.currentTimeMillis()}_${userId}",
                "customer_name" to userName,
                "payment_type" to "card",
                "meta" to hashMapOf(
                    "eventId" to eventId,
                    "userId" to userId,
                    "quantity" to quantity,
                    "type" to "ticket"
                )
            )
            
            val initResult = functions
                .getHttpsCallable("initializeFlutterwavePayment")
                .call(initData)
                .await()
            
            val initResultData = initResult.data as? Map<String, Any>
            val success = initResultData?.get("success") as? Boolean ?: false
            val paymentUrl = initResultData?.get("paymentUrl") as? String ?: ""
            val paymentId = initResultData?.get("paymentId") as? String ?: ""
            val message = initResultData?.get("message") as? String ?: ""
            
            if (success) {
                Result.success(PaymentResult(
                    success = true,
                    paymentId = paymentId,
                    message = message
                ))
            } else {
                Result.failure(Exception(message))
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
            // Initialize Flutterwave payment
            val initData = hashMapOf(
                "amount" to budget,
                "currency" to "KES",
                "email" to userEmail,
                "phone_number" to userPhone,
                "tx_ref" to "AD_${System.currentTimeMillis()}_${influencerId}",
                "customer_name" to userName,
                "payment_type" to "card",
                "meta" to hashMapOf(
                    "advertisementId" to advertisementId,
                    "influencerId" to influencerId,
                    "type" to "advertisement"
                )
            )
            
            val initResult = functions
                .getHttpsCallable("initializeFlutterwavePayment")
                .call(initData)
                .await()
            
            val initResultData = initResult.data as? Map<String, Any>
            val success = initResultData?.get("success") as? Boolean ?: false
            val paymentUrl = initResultData?.get("paymentUrl") as? String ?: ""
            val paymentId = initResultData?.get("paymentId") as? String ?: ""
            val message = initResultData?.get("message") as? String ?: ""
            
            if (success) {
                Result.success(PaymentResult(
                    success = true,
                    paymentId = paymentId,
                    message = message
                ))
            } else {
                Result.failure(Exception(message))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    suspend fun verifyPayment(transactionId: String): Result<PaymentResult> {
        return try {
            val verifyData = hashMapOf(
                "transaction_id" to transactionId
            )
            
            val verifyResult = functions
                .getHttpsCallable("verifyFlutterwavePayment")
                .call(verifyData)
                .await()
            
            val verifyResultData = verifyResult.data as? Map<String, Any>
            val success = verifyResultData?.get("success") as? Boolean ?: false
            val paymentId = verifyResultData?.get("paymentId") as? String ?: ""
            val message = verifyResultData?.get("message") as? String ?: ""
            
            if (success) {
                Result.success(PaymentResult(
                    success = true,
                    paymentId = paymentId,
                    message = message
                ))
            } else {
                Result.failure(Exception(message))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    suspend fun refundPayment(paymentId: String, reason: String): Result<Boolean> {
        return try {
            val refundData = hashMapOf(
                "paymentId" to paymentId,
                "reason" to reason
            )
            
            val refundResult = functions
                .getHttpsCallable("processRefund")
                .call(refundData)
                .await()
            
            val refundResultData = refundResult.data as? Map<String, Any>
            val success = refundResultData?.get("success") as? Boolean ?: false
            
            Result.success(success)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
} 