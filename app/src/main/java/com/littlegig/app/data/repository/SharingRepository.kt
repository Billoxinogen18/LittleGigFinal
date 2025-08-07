package com.littlegig.app.data.repository

import com.google.firebase.functions.FirebaseFunctions
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SharingRepository @Inject constructor(
    private val functions: FirebaseFunctions
) {
    suspend fun createEventDynamicLink(eventId: String, title: String, imageUrl: String?): Result<String> {
        return try {
            val target = "https://littlegig.app/event?e=$eventId"
            val data = mapOf(
                "link" to target,
                "title" to title,
                "imageUrl" to (imageUrl ?: "")
            )
            val result = functions
                .getHttpsCallable("createDynamicLink")
                .call(data)
                .await()
            val response = result.data as? Map<*, *>
            val shortLink = response?.get("shortLink") as? String
            if (shortLink != null) Result.success(shortLink) else Result.failure(Exception("No link"))
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}

