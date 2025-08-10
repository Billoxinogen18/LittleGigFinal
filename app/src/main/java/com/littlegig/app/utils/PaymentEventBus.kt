package com.littlegig.app.utils

import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow

object PaymentEventBus {
    private val _events = MutableSharedFlow<PaymentVerificationEvent>(extraBufferCapacity = 1)
    val events: SharedFlow<PaymentVerificationEvent> = _events

    suspend fun emit(event: PaymentVerificationEvent) {
        _events.emit(event)
    }
}

data class PaymentVerificationEvent(
    val reference: String,
    val success: Boolean
)