package com.littlegig.app.data.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Ticket(
    val id: String = "",
    val eventId: String = "",
    val eventTitle: String = "",
    val eventImageUrl: String = "",
    val eventLocation: String = "",
    val eventDateTime: Long = 0L,
    val userId: String = "",
    val userName: String = "",
    val userEmail: String = "",
    val quantity: Int = 1,
    val totalAmount: Double = 0.0,
    val commission: Double = 0.0, // 4% commission
    val currency: String = "KSH",
    val status: TicketStatus = TicketStatus.ACTIVE,
    val paymentId: String = "",
    val qrCode: String = "",
    val purchaseDate: Long = System.currentTimeMillis(),
    val usedDate: Long? = null
) : Parcelable

enum class TicketStatus {
    ACTIVE,
    USED,
    CANCELLED,
    EXPIRED
}