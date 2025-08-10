package com.littlegig.app.data.model

data class Recommendation(
    val id: String,
    val eventId: String,
    val type: RecommendationType,
    val score: Double,
    val reason: String,
    val timestamp: Long,
    val metadata: Map<String, Any> = emptyMap()
)

enum class RecommendationType {
    CONTENT_BASED,
    COLLABORATIVE,
    POPULARITY_BASED,
    LOCATION_BASED,
    TIME_BASED,
    HYBRID
}
