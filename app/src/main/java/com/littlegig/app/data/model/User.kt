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
    val isInfluencer: Boolean = false,
    val businessId: String? = null,
    val rank: UserRank = UserRank.NOVICE,
    val followers: List<String> = emptyList(),
    val following: List<String> = emptyList(),
    val bio: String? = null,
    val createdAt: Long = System.currentTimeMillis(),
    val updatedAt: Long = System.currentTimeMillis()
) : Parcelable

enum class UserType {
    REGULAR,
    BUSINESS,
    ADMIN
}