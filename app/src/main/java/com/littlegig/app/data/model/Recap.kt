package com.littlegig.app.data.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Recap(
    val id: String = "",
    val eventId: String = "",
    val userId: String = "",
    val userName: String = "",
    val userImageUrl: String = "",
    val mediaUrls: List<String> = emptyList(),
    val mediaType: MediaType = MediaType.IMAGE,
    val caption: String = "",
    val location: Location = Location(),
    val likes: Int = 0,
    val views: Int = 0,
    val createdAt: Long = System.currentTimeMillis(),
    val duration: Long = 5000L, // 5 seconds default
    val isActive: Boolean = true
) : Parcelable

enum class MediaType {
    IMAGE,
    VIDEO
}

@Parcelize
data class RecapStory(
    val recaps: List<Recap> = emptyList(),
    val currentIndex: Int = 0,
    val isPlaying: Boolean = false,
    val progress: Float = 0f
) : Parcelable

@Parcelize
data class RecapViewerState(
    val currentRecapIndex: Int = 0,
    val isPlaying: Boolean = false,
    val progress: Float = 0f,
    val totalRecaps: Int = 0,
    val recaps: List<Recap> = emptyList()
) : Parcelable 