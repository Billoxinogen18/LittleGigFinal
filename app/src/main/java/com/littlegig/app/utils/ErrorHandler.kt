package com.littlegig.app.utils

import android.content.Context
import com.google.firebase.crashlytics.FirebaseCrashlytics
import kotlinx.coroutines.CoroutineExceptionHandler
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ErrorHandler @Inject constructor(
    private val context: Context
) {
    
    private val crashlytics = FirebaseCrashlytics.getInstance()
    
    fun logException(throwable: Throwable, customData: Map<String, String> = emptyMap()) {
        customData.forEach { (key, value) ->
            crashlytics.setCustomKey(key, value)
        }
        crashlytics.recordException(throwable)
    }
    
    fun logNonFatalError(message: String, customData: Map<String, String> = emptyMap()) {
        customData.forEach { (key, value) ->
            crashlytics.setCustomKey(key, value)
        }
        crashlytics.log(message)
    }
    
    fun setUserIdentifier(userId: String) {
        crashlytics.setUserId(userId)
    }
    
    fun setUserProperty(key: String, value: String) {
        crashlytics.setCustomKey(key, value)
    }
    
    val coroutineExceptionHandler = CoroutineExceptionHandler { _, throwable ->
        logException(throwable, mapOf("context" to "coroutine"))
    }
    
    fun handleNetworkError(error: Throwable, operation: String) {
        logException(error, mapOf(
            "operation" to operation,
            "error_type" to "network"
        ))
    }
    
    fun handleFirebaseError(error: Throwable, operation: String) {
        logException(error, mapOf(
            "operation" to operation,
            "error_type" to "firebase"
        ))
    }
    
    fun handlePaymentError(error: Throwable, operation: String) {
        logException(error, mapOf(
            "operation" to operation,
            "error_type" to "payment"
        ))
    }
} 