package com.littlegig.app.data.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Event(
    val id: String = "",
    val title: String = "",
    val description: String = "",
    val category: ContentCategory = ContentCategory.EVENT,
    val imageUrls: List<String> = emptyList(),
    val location: Location = Location(),
    val dateTime: Long = 0L,
    val endDateTime: Long? = null,
    val price: Double = 0.0,
    val currency: String = "KSH",
    val capacity: Int = 0,
    val ticketsSold: Int = 0,
    val organizerId: String = "",
    val organizerName: String = "",
    val organizerImageUrl: String = "",
    val tags: List<String> = emptyList(),
    val active: Boolean = true,
    val featured: Boolean = false, // Featured status for events
    val createdAt: Long = System.currentTimeMillis(),
    val updatedAt: Long = System.currentTimeMillis(),
    val likes: Int = 0,
    val shares: Int = 0,
    val views: Int = 0,
    // Users who liked this event (for quick membership checks and counts)
    val likedBy: List<String> = emptyList(),
    // Per-user rating map (userId -> rating)
    val ratings: Map<String, Float> = emptyMap(),
    val metadata: Map<String, String> = emptyMap()
) : Parcelable

@Parcelize
data class Location(
    val name: String = "",
    val address: String = "",
    val latitude: Double = 0.0,
    val longitude: Double = 0.0,
    val city: String = "",
    val country: String = "Kenya"
) : Parcelable

enum class ContentCategory {
    EVENT,
    HOTEL,
    RESTAURANT,
    TOUR,
    CONCERT,
    WORKSHOP,
    CONFERENCE
}

// Alias for compatibility with services
typealias EventCategory = ContentCategory