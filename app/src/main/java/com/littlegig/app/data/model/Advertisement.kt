package com.littlegig.app.data.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Advertisement(
    val id: String = "",
    val influencerId: String = "",
    val influencerName: String = "",
    val influencerImageUrl: String = "",
    val title: String = "",
    val description: String = "",
    val imageUrls: List<String> = emptyList(),
    val targetAudience: String = "",
    val budget: Double = 0.0,
    val currency: String = "KSH",
    val duration: Int = 7, // days
    val status: AdStatus = AdStatus.PENDING,
    val impressions: Int = 0,
    val clicks: Int = 0,
    val reach: Int = 0,
    val startDate: Long = 0L,
    val endDate: Long = 0L,
    val paymentId: String = "",
    val createdAt: Long = System.currentTimeMillis(),
    val updatedAt: Long = System.currentTimeMillis()
) : Parcelable

enum class AdStatus {
    PENDING,
    ACTIVE,
    PAUSED,
    COMPLETED,
    REJECTED
}