package com.littlegig.app.data.repository

import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.google.firebase.remoteconfig.FirebaseRemoteConfigSettings
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ConfigRepository @Inject constructor(
    private val firestore: FirebaseFirestore
) {
    private val remoteConfig = FirebaseRemoteConfig.getInstance()
    
    init {
        val configSettings = FirebaseRemoteConfigSettings.Builder()
            .setMinimumFetchIntervalInSeconds(3600) // 1 hour
            .build()
        remoteConfig.setConfigSettingsAsync(configSettings)
        
        // Set defaults
        remoteConfig.setDefaultsAsync(mapOf(
            "feed_featured_weight" to 3.0,
            "feed_tickets_weight" to 2.0,
            "feed_price_weight" to -0.5,
            "feed_recency_weight" to -1.0
        ))
        
        // Fetch and activate
        remoteConfig.fetchAndActivate()
    }
    
    suspend fun getFeedWeights(): FeedWeights {
        return try {
            // Try Firestore first (for real-time updates)
            val doc = firestore.collection("config").document("feedWeights").get().await()
            FeedWeights(
                featuredWeight = (doc.getDouble("featuredWeight") ?: getRemoteConfigDouble("feed_featured_weight")),
                ticketsSoldWeight = (doc.getDouble("ticketsSoldWeight") ?: getRemoteConfigDouble("feed_tickets_weight")),
                priceWeight = (doc.getDouble("priceWeight") ?: getRemoteConfigDouble("feed_price_weight")),
                recencyWeight = (doc.getDouble("recencyWeight") ?: getRemoteConfigDouble("feed_recency_weight"))
            )
        } catch (e: Exception) {
            // Fallback to Remote Config defaults
            FeedWeights(
                featuredWeight = getRemoteConfigDouble("feed_featured_weight"),
                ticketsSoldWeight = getRemoteConfigDouble("feed_tickets_weight"),
                priceWeight = getRemoteConfigDouble("feed_price_weight"),
                recencyWeight = getRemoteConfigDouble("feed_recency_weight")
            )
        }
    }
    
    private fun getRemoteConfigDouble(key: String): Double {
        return remoteConfig.getDouble(key)
    }
}

data class FeedWeights(
    val featuredWeight: Double = 3.0,
    val ticketsSoldWeight: Double = 2.0,
    val priceWeight: Double = -0.5,
    val recencyWeight: Double = -1.0
)

