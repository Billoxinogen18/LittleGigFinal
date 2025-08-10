package com.littlegig.app.data.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import java.util.Date

@Parcelize
data class User(
    val id: String = "",
    val email: String = "",
    val name: String = "",
    val displayName: String = "",
    val username: String = "",
    val phoneNumber: String = "",
    val profilePictureUrl: String = "",
    val profileImageUrl: String = "",
    val userType: UserType = UserType.REGULAR,
    // Firestore historical alias; some documents use "influencer" instead of "isInfluencer"
    val influencer: Boolean? = null,
    // Prefer isInfluencer; default to true when legacy "influencer" is true
    val isInfluencer: Boolean = (influencer == true),
    val businessId: String? = null,
    val rank: UserRank = UserRank.NOVICE,
    val followers: List<String> = emptyList(),
    val following: List<String> = emptyList(),
    val pinnedChats: List<String> = emptyList(),
    // Events the user has liked (for quick membership checks)
    val likedEvents: List<String> = emptyList(),
    // Product analytics field populated by Cloud Functions
    val engagementScore: Double = 0.0,
    // Last time the user rank or engagement was updated server-side
    val lastRankUpdate: Date? = null,
    val bio: String? = null,
    val createdAt: Long = System.currentTimeMillis(),
    val updatedAt: Long = System.currentTimeMillis()
) : Parcelable

enum class UserType {
    REGULAR,
    BUSINESS,
    ADMIN
}