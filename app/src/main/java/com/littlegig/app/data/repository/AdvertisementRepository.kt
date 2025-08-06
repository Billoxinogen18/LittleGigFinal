package com.littlegig.app.data.repository

import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.littlegig.app.data.model.AdStatus
import com.littlegig.app.data.model.Advertisement
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AdvertisementRepository @Inject constructor(
    private val firestore: FirebaseFirestore
) {
    
    fun getInfluencerAds(influencerId: String): Flow<List<Advertisement>> = callbackFlow {
        val listener = firestore.collection("advertisements")
            .whereEqualTo("influencerId", influencerId)
            .orderBy("createdAt", Query.Direction.DESCENDING)
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    trySend(emptyList())
                    return@addSnapshotListener
                }
                
                val ads = snapshot?.documents?.mapNotNull { doc ->
                    doc.toObject(Advertisement::class.java)?.copy(id = doc.id)
                } ?: emptyList()
                
                trySend(ads)
            }
            
        awaitClose { listener.remove() }
    }
    
    fun getActiveAds(): Flow<List<Advertisement>> = callbackFlow {
        val currentTime = System.currentTimeMillis()
        val listener = firestore.collection("advertisements")
            .whereEqualTo("status", AdStatus.ACTIVE.name)
            .whereLessThanOrEqualTo("startDate", currentTime)
            .whereGreaterThanOrEqualTo("endDate", currentTime)
            .orderBy("createdAt", Query.Direction.DESCENDING)
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    trySend(emptyList())
                    return@addSnapshotListener
                }
                
                val ads = snapshot?.documents?.mapNotNull { doc ->
                    doc.toObject(Advertisement::class.java)?.copy(id = doc.id)
                } ?: emptyList()
                
                trySend(ads)
            }
            
        awaitClose { listener.remove() }
    }
    
    suspend fun createAdvertisement(advertisement: Advertisement): Result<String> {
        return try {
            val docRef = firestore.collection("advertisements")
                .add(advertisement)
                .await()
            Result.success(docRef.id)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    suspend fun updateAdvertisement(adId: String, advertisement: Advertisement): Result<Unit> {
        return try {
            firestore.collection("advertisements")
                .document(adId)
                .set(advertisement.copy(updatedAt = System.currentTimeMillis()))
                .await()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    suspend fun updateAdStatus(adId: String, status: AdStatus): Result<Unit> {
        return try {
            firestore.collection("advertisements")
                .document(adId)
                .update(
                    mapOf(
                        "status" to status.name,
                        "updatedAt" to System.currentTimeMillis()
                    )
                )
                .await()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    suspend fun incrementAdMetrics(
        adId: String, 
        impressions: Int = 0, 
        clicks: Int = 0
    ): Result<Unit> {
        return try {
            val docRef = firestore.collection("advertisements").document(adId)
            firestore.runTransaction { transaction ->
                val document = transaction.get(docRef)
                val currentImpressions = document.getLong("impressions") ?: 0
                val currentClicks = document.getLong("clicks") ?: 0
                
                transaction.update(
                    docRef, 
                    mapOf(
                        "impressions" to currentImpressions + impressions,
                        "clicks" to currentClicks + clicks,
                        "updatedAt" to System.currentTimeMillis()
                    )
                )
            }.await()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    suspend fun getAdvertisementById(adId: String): Result<Advertisement> {
        return try {
            val document = firestore.collection("advertisements")
                .document(adId)
                .get()
                .await()
                
            if (document.exists()) {
                val advertisement = document.toObject(Advertisement::class.java)?.copy(id = document.id)
                    ?: return Result.failure(Exception("Failed to parse advertisement"))
                Result.success(advertisement)
            } else {
                Result.failure(Exception("Advertisement not found"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    suspend fun getTotalAdSpend(influencerId: String): Result<Double> {
        return try {
            val snapshot = firestore.collection("advertisements")
                .whereEqualTo("influencerId", influencerId)
                .whereIn("status", listOf(AdStatus.ACTIVE.name, AdStatus.COMPLETED.name))
                .get()
                .await()
                
            val totalSpend = snapshot.documents.sumOf { doc ->
                val ad = doc.toObject(Advertisement::class.java)
                ad?.budget ?: 0.0
            }
            
            Result.success(totalSpend)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    suspend fun getAdAnalytics(influencerId: String): Result<AdAnalytics> {
        return try {
            val snapshot = firestore.collection("advertisements")
                .whereEqualTo("influencerId", influencerId)
                .get()
                .await()
                
            var totalSpend = 0.0
            var totalImpressions = 0
            var totalClicks = 0
            var totalReach = 0
            var activeAds = 0
            
            snapshot.documents.forEach { doc ->
                val ad = doc.toObject(Advertisement::class.java)
                ad?.let {
                    totalSpend += it.budget
                    totalImpressions += it.impressions
                    totalClicks += it.clicks
                    totalReach += it.reach
                    if (it.status == AdStatus.ACTIVE) activeAds++
                }
            }
            
            val analytics = AdAnalytics(
                totalSpend = totalSpend,
                totalImpressions = totalImpressions,
                totalClicks = totalClicks,
                totalReach = totalReach,
                activeAds = activeAds,
                ctr = if (totalImpressions > 0) (totalClicks.toDouble() / totalImpressions) * 100 else 0.0
            )
            
            Result.success(analytics)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}

data class AdAnalytics(
    val totalSpend: Double,
    val totalImpressions: Int,
    val totalClicks: Int,
    val totalReach: Int,
    val activeAds: Int,
    val ctr: Double // Click-through rate percentage
)