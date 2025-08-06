package com.littlegig.app.data.model

data class PaymentResult(
    val success: Boolean,
    val paymentId: String,
    val message: String
) 