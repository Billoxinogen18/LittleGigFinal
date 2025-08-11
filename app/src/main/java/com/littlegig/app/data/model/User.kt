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
    // Map legacy Firestore field "influencer" without creating conflicting getters
    @com.google.firebase.firestore.PropertyName("influencer")
    val influencerLegacy: Boolean? = null,
    // Primary field used by the app - map to both "influencer" and "isInfluencer" for compatibility
    @com.google.firebase.firestore.PropertyName("influencer")
    val isInfluencer: Boolean = false,
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
    val lastRankUpdate: java.util.Date? = null,
    val bio: String? = null,
    val createdAt: Long = System.currentTimeMillis(),
    val updatedAt: Long = System.currentTimeMillis(),
    // Additional fields created by Cloud Functions
    val username_lower: String? = null,
    val email_lower: String? = null,
    val displayName_lower: String? = null,
    val lastSeen: Long? = null,
    val online: Boolean = false
) : Parcelable

enum class UserType {
    REGULAR,
    BUSINESS,
    ADMIN
}