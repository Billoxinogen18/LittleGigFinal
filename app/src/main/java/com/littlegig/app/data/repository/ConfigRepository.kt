package com.littlegig.app.data.repository

import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ConfigRepository @Inject constructor(
    private val firestore: FirebaseFirestore
) {
    suspend fun getFeedWeights(): FeedWeights {
        return try {
            val doc = firestore.collection("config").document("feedWeights").get().await()
            FeedWeights(
                featuredWeight = (doc.getDouble("featuredWeight") ?: 3.0),
                ticketsSoldWeight = (doc.getDouble("ticketsSoldWeight") ?: 2.0),
                priceWeight = (doc.getDouble("priceWeight") ?: -0.5),
                recencyWeight = (doc.getDouble("recencyWeight") ?: -1.0)
            )
        } catch (e: Exception) {
            FeedWeights()
        }
    }
}

data class FeedWeights(
    val featuredWeight: Double = 3.0,
    val ticketsSoldWeight: Double = 2.0,
    val priceWeight: Double = -0.5,
    val recencyWeight: Double = -1.0
)

