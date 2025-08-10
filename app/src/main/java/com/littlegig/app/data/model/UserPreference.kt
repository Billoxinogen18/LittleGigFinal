package com.littlegig.app.data.model

data class UserPreference(
    val userId: String = "",
    val preferredCategories: List<ContentCategory> = emptyList(),
    val preferredLocations: List<Location> = emptyList(),
    val preferredPriceRange: PriceRange? = null,
    val preferredTimeSlots: List<TimeSlot> = emptyList(),
    val notificationPreferences: NotificationPreferences = NotificationPreferences(),
    val privacySettings: PrivacySettings = PrivacySettings(),
    val lastUpdated: Long = System.currentTimeMillis()
)

data class PriceRange(
    val min: Double,
    val max: Double
)

data class TimeSlot(
    val dayOfWeek: Int,
    val startHour: Int,
    val endHour: Int
)

data class NotificationPreferences(
    val chatNotifications: Boolean = true,
    val eventNotifications: Boolean = true,
    val marketingNotifications: Boolean = false,
    val quietHours: QuietHours = QuietHours()
)

data class QuietHours(
    val enabled: Boolean = false,
    val startHour: Int = 22,
    val endHour: Int = 8
)

data class PrivacySettings(
    val profileVisibility: ProfileVisibility = ProfileVisibility.PUBLIC,
    val locationSharing: Boolean = false,
    val activityVisibility: ActivityVisibility = ActivityVisibility.FRIENDS
)

enum class ProfileVisibility {
    PUBLIC,
    FRIENDS,
    PRIVATE
}

enum class ActivityVisibility {
    PUBLIC,
    FRIENDS,
    PRIVATE
}
