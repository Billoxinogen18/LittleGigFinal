package com.littlegig.app.data.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class User(
    val id: String = "",
    val email: String = "",
    val displayName: String = "",
    val phoneNumber: String = "",
    val profileImageUrl: String = "",
    val userType: UserType = UserType.REGULAR,
    val isInfluencer: Boolean = false,
    val businessId: String? = null,
    val createdAt: Long = System.currentTimeMillis(),
    val updatedAt: Long = System.currentTimeMillis()
) : Parcelable

enum class UserType {
    REGULAR,
    BUSINESS,
    ADMIN
}