package com.littlegig.app.services

import android.app.Activity
import com.google.firebase.FirebaseException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthOptions
import com.google.firebase.auth.PhoneAuthProvider
import java.util.concurrent.TimeUnit
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PhoneAuthService @Inject constructor(
    private val firebaseAuth: FirebaseAuth
) {
    interface Callbacks {
        fun onCodeSent(verificationId: String, token: PhoneAuthProvider.ForceResendingToken) {}
        fun onVerificationCompleted(credential: PhoneAuthCredential) {}
        fun onVerificationFailed(error: Exception) {}
    }

    fun startPhoneNumberVerification(
        activity: Activity,
        phoneNumberE164: String,
        timeoutSeconds: Long = 60,
        callbacks: Callbacks
    ) {
        val cb = object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            override fun onVerificationCompleted(credential: PhoneAuthCredential) {
                callbacks.onVerificationCompleted(credential)
            }
            override fun onVerificationFailed(e: FirebaseException) {
                callbacks.onVerificationFailed(e)
            }
            override fun onCodeSent(verificationId: String, token: PhoneAuthProvider.ForceResendingToken) {
                callbacks.onCodeSent(verificationId, token)
            }
        }
        val options = PhoneAuthOptions.newBuilder(firebaseAuth)
            .setPhoneNumber(phoneNumberE164)
            .setTimeout(timeoutSeconds, TimeUnit.SECONDS)
            .setActivity(activity)
            .setCallbacks(cb)
            .build()
        PhoneAuthProvider.verifyPhoneNumber(options)
    }

    fun resendVerificationCode(
        activity: Activity,
        phoneNumberE164: String,
        token: PhoneAuthProvider.ForceResendingToken,
        timeoutSeconds: Long = 60,
        callbacks: Callbacks
    ) {
        val cb = object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            override fun onVerificationCompleted(credential: PhoneAuthCredential) {
                callbacks.onVerificationCompleted(credential)
            }
            override fun onVerificationFailed(e: FirebaseException) {
                callbacks.onVerificationFailed(e)
            }
            override fun onCodeSent(verificationId: String, newToken: PhoneAuthProvider.ForceResendingToken) {
                callbacks.onCodeSent(verificationId, newToken)
            }
        }
        val options = PhoneAuthOptions.newBuilder(firebaseAuth)
            .setPhoneNumber(phoneNumberE164)
            .setTimeout(timeoutSeconds, TimeUnit.SECONDS)
            .setActivity(activity)
            .setCallbacks(cb)
            .setForceResendingToken(token)
            .build()
        PhoneAuthProvider.verifyPhoneNumber(options)
    }

    fun getCredential(verificationId: String, code: String): PhoneAuthCredential {
        return PhoneAuthProvider.getCredential(verificationId, code)
    }
}