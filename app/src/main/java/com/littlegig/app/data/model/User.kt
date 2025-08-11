package com.littlegig.app.data.model

import android.os.Parcelable
import com.google.firebase.firestore.IgnoreExtraProperties
import kotlinx.parcelize.Parcelize
import kotlinx.parcelize.RawValue
import java.util.Date

@IgnoreExtraProperties
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
    // Legacy field for backward compatibility
    val influencerLegacy: Boolean? = null,
    // Another legacy alias that may exist in old documents
    val influencer: Boolean? = null,
    // Primary field used by the app
    var isInfluencer: Boolean = false,
    val businessId: String? = null,
    val rank: UserRank = UserRank.NOVICE,
    val followers: List<String> = emptyList(),
    val following: List<String> = emptyList(),
    val pinnedChats: List<String> = emptyList(),
    // Events the user has liked (for quick membership checks)
    val likedEvents: List<String> = emptyList(),
    // Product analytics field populated by Cloud Functions
    val engagementScore: Double = 0.0,
    // Last time the user rank or engagement was updated server-side - make it flexible
    val lastRankUpdate: @RawValue Any? = null, // Accept any type (Date, Long, Timestamp)
    val bio: String? = null,
    // Make timestamp fields flexible to handle both Long and Timestamp
    val createdAt: @RawValue Any = System.currentTimeMillis(), // Accept any type (Long, Timestamp)
    val updatedAt: @RawValue Any = System.currentTimeMillis(), // Accept any type (Long, Timestamp)
    // Additional fields created by Cloud Functions
    val username_lower: String? = null,
    val email_lower: String? = null,
    val displayName_lower: String? = null,
    val lastSeen: @RawValue Any? = null, // Accept any type (Long, Timestamp)
    val online: Boolean = false
) : Parcelable

enum class UserType {
    REGULAR,
    BUSINESS,
    ADMIN
}