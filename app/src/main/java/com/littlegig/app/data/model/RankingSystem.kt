package com.littlegig.app.data.model

import kotlin.math.*

// Ranking System for LittleGig
enum class UserRank {
    NOVICE,           // 0-10% engagement
    BEGINNER,         // 10-25% engagement
    PARTY_POPPER,     // 25-40% engagement
    POPULAR,          // 40-55% engagement
    INFLUENCER,       // 55-70% engagement
    FAMOUS,           // 70-85% engagement
    ROCKSTAR,         // 85-95% engagement
    SUPERSTAR         // 95-100% engagement
}

data class UserEngagement(
    val userId: String,
    val eventEngagement: Double = 0.0,    // 40% weight
    val recapEngagement: Double = 0.0,    // 35% weight
    val eventsAttended: Int = 0,          // 15% weight
    val platformActivity: Double = 0.0,   // 10% weight
    val totalScore: Double = 0.0,
    val rank: UserRank = UserRank.NOVICE,
    val rankPercentage: Double = 0.0
)

class RankingAlgorithm {
    
    companion object {
        private const val EVENT_ENGAGEMENT_WEIGHT = 0.40
        private const val RECAP_ENGAGEMENT_WEIGHT = 0.35
        private const val EVENTS_ATTENDED_WEIGHT = 0.15
        private const val PLATFORM_ACTIVITY_WEIGHT = 0.10
        
        // Calculate user's engagement score
        fun calculateUserEngagement(
            eventEngagement: Double,
            recapEngagement: Double,
            eventsAttended: Int,
            platformActivity: Double
        ): UserEngagement {
            
            val normalizedEventEngagement = normalizeEventEngagement(eventEngagement)
            val normalizedRecapEngagement = normalizeRecapEngagement(recapEngagement)
            val normalizedEventsAttended = normalizeEventsAttended(eventsAttended)
            val normalizedPlatformActivity = normalizePlatformActivity(platformActivity)
            
            val totalScore = (normalizedEventEngagement * EVENT_ENGAGEMENT_WEIGHT) +
                           (normalizedRecapEngagement * RECAP_ENGAGEMENT_WEIGHT) +
                           (normalizedEventsAttended * EVENTS_ATTENDED_WEIGHT) +
                           (normalizedPlatformActivity * PLATFORM_ACTIVITY_WEIGHT)
            
            val rank = calculateRank(totalScore)
            val rankPercentage = calculateRankPercentage(totalScore)
            
            return UserEngagement(
                userId = "", // This will be set by the caller
                eventEngagement = eventEngagement,
                recapEngagement = recapEngagement,
                eventsAttended = eventsAttended,
                platformActivity = platformActivity,
                totalScore = totalScore,
                rank = rank,
                rankPercentage = rankPercentage
            )
        }
        
        // Event engagement calculation
        private fun normalizeEventEngagement(eventEngagement: Double): Double {
            // Event engagement factors:
            // - Events created
            // - Average attendance per event
            // - Event ratings
            // - Event shares
            return minOf(eventEngagement / 100.0, 1.0) // Normalize to 0-1
        }
        
        // Recap engagement calculation
        private fun normalizeRecapEngagement(recapEngagement: Double): Double {
            // Recap engagement factors:
            // - Recaps posted
            // - Average views per recap
            // - Average likes per recap
            // - Recap shares
            return minOf(recapEngagement / 50.0, 1.0) // Normalize to 0-1
        }
        
        // Events attended calculation
        private fun normalizeEventsAttended(eventsAttended: Int): Double {
            // Events attended factors:
            // - Number of events attended
            // - Event diversity (different categories)
            // - Event ratings given
            return minOf(eventsAttended / 20.0, 1.0) // Normalize to 0-1
        }
        
        // Platform activity calculation
        private fun normalizePlatformActivity(platformActivity: Double): Double {
            // Platform activity factors:
            // - Daily login streak
            // - Profile completeness
            // - Social interactions (follows, comments)
            // - App usage time
            return minOf(platformActivity / 30.0, 1.0) // Normalize to 0-1
        }
        
        // Calculate rank based on total score
        private fun calculateRank(totalScore: Double): UserRank {
            return when {
                totalScore >= 0.95 -> UserRank.SUPERSTAR
                totalScore >= 0.85 -> UserRank.ROCKSTAR
                totalScore >= 0.70 -> UserRank.FAMOUS
                totalScore >= 0.55 -> UserRank.INFLUENCER
                totalScore >= 0.40 -> UserRank.POPULAR
                totalScore >= 0.25 -> UserRank.PARTY_POPPER
                totalScore >= 0.10 -> UserRank.BEGINNER
                else -> UserRank.NOVICE
            }
        }
        
        // Calculate rank percentage (for real-time ranking)
        private fun calculateRankPercentage(totalScore: Double): Double {
            return totalScore * 100.0
        }
        
        // Calculate engagement metrics for events
        fun calculateEventEngagement(
            eventsCreated: Int,
            totalAttendance: Int,
            averageRating: Double,
            totalShares: Int
        ): Double {
            val eventScore = eventsCreated * 10.0
            val attendanceScore = totalAttendance * 0.5
            val ratingScore = averageRating * 20.0
            val shareScore = totalShares * 2.0
            
            return eventScore + attendanceScore + ratingScore + shareScore
        }
        
        // Calculate engagement metrics for recaps
        fun calculateRecapEngagement(
            recapsPosted: Int,
            totalViews: Int,
            totalLikes: Int,
            totalShares: Int
        ): Double {
            val recapScore = recapsPosted * 5.0
            val viewScore = totalViews * 0.1
            val likeScore = totalLikes * 0.5
            val shareScore = totalShares * 1.0
            
            return recapScore + viewScore + likeScore + shareScore
        }
        
        // Calculate platform activity score
        fun calculatePlatformActivity(
            loginStreak: Int,
            profileCompleteness: Double,
            socialInteractions: Int,
            usageTimeHours: Double
        ): Double {
            val streakScore = loginStreak * 2.0
            val profileScore = profileCompleteness * 10.0
            val socialScore = socialInteractions * 0.5
            val usageScore = usageTimeHours * 0.1
            
            return streakScore + profileScore + socialScore + usageScore
        }
    }
} 