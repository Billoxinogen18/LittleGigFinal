package com.littlegig.app.data.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class UserProfile(
    val userId: String = "",
    val displayName: String = "",
    val email: String = "",
    val profileImageUrl: String = "",
    val bio: String = "",
    val userType: UserType = UserType.REGULAR,
    val rank: UserRank = UserRank.NOVICE,
    val rankPercentage: Double = 0.0,
    val followersCount: Int = 0,
    val followingCount: Int = 0,
    val eventsCreated: Int = 0,
    val eventsAttended: Int = 0,
    val recapsPosted: Int = 0,
    val isActiveNow: Boolean = false,
    val isPublic: Boolean = true,
    val lastActive: Long = System.currentTimeMillis(),
    val createdAt: Long = System.currentTimeMillis()
) : Parcelable

@Parcelize
data class FollowRelationship(
    val id: String = "",
    val followerId: String = "",
    val followingId: String = "",
    val createdAt: Long = System.currentTimeMillis(),
    val isActive: Boolean = true
) : Parcelable

@Parcelize
data class SocialState(
    val currentUser: UserProfile? = null,
    val followers: List<UserProfile> = emptyList(),
    val following: List<UserProfile> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null
) : Parcelable 