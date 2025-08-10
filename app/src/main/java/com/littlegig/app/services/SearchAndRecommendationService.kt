package com.littlegig.app.services

import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.littlegig.app.data.model.Event
import com.littlegig.app.data.model.User
import com.littlegig.app.data.model.SearchQuery
import com.littlegig.app.data.model.SearchResult
import com.littlegig.app.data.model.Recommendation
import com.littlegig.app.data.model.RecommendationType
import com.littlegig.app.data.model.UserPreference
import com.littlegig.app.data.model.EventCategory
import com.littlegig.app.data.model.Location
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.tasks.await
import java.util.*
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.atomic.AtomicInteger
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.math.*

@Singleton
class SearchAndRecommendationService @Inject constructor(
    private val firestore: FirebaseFirestore
) {

    companion object {
        private const val MAX_SEARCH_RESULTS = 100
        private const val MAX_RECOMMENDATIONS = 50
        private const val SEARCH_CACHE_TTL_HOURS = 1
        private const val PREFERENCE_WEIGHT = 0.4
        private const val LOCATION_WEIGHT = 0.3
        private const val TIME_WEIGHT = 0.2
        private const val POPULARITY_WEIGHT = 0.1
        private const val COLLABORATIVE_FILTERING_NEIGHBORS = 10
        private const val MIN_SIMILARITY_THRESHOLD = 0.3
    }

    // Search and recommendation state
    private val searchCache = ConcurrentHashMap<String, CachedSearchResult>()
    private val userPreferences = ConcurrentHashMap<String, UserPreference>()
    private val eventSimilarityMatrix = ConcurrentHashMap<String, Map<String, Double>>()
    private val userSimilarityMatrix = ConcurrentHashMap<String, Map<String, Double>>()
    private val recommendationCache = ConcurrentHashMap<String, CachedRecommendations>()
    
    // Performance optimization
    private val searchCounter = AtomicInteger(0)
    private val recommendationCounter = AtomicInteger(0)
    
    // Coroutine scope for background operations
    private val serviceScope = CoroutineScope(Dispatchers.IO + SupervisorJob())

    // Production-grade search algorithm with multiple ranking factors
    suspend fun searchEvents(query: SearchQuery): SearchResult {
        return try {
            // Check cache first
            val cacheKey = generateSearchCacheKey(query)
            val cachedResult = searchCache[cacheKey]
            if (cachedResult != null && !cachedResult.isExpired()) {
                return cachedResult.result
            }

            // Build Firestore query
            val baseQuery = buildSearchQuery(query)
            
            // Execute search with pagination
            val events = executeSearchWithPagination(baseQuery, query.limit ?: MAX_SEARCH_RESULTS)
            
            // Apply advanced ranking algorithm
            val rankedEvents = applyAdvancedRanking(events, query)
            
            // Apply filters
            val filteredEvents = applySearchFilters(rankedEvents, query)
            
            // Create search result
            val searchResult = SearchResult(
                query = query,
                events = filteredEvents,
                totalCount = filteredEvents.size,
                searchTime = System.currentTimeMillis(),
                suggestions = generateSearchSuggestions(query),
                facets = generateSearchFacets(filteredEvents)
            )

            // Cache result
            cacheSearchResult(cacheKey, searchResult)
            
            // Track search analytics
            trackSearchAnalytics(query, searchResult)
            
            searchResult
        } catch (e: Exception) {
            SearchResult(
                query = query,
                events = emptyList(),
                totalCount = 0,
                searchTime = System.currentTimeMillis(),
                error = e.message
            )
        }
    }

    // Sophisticated recommendation engine
    suspend fun getEventRecommendations(
        userId: String,
        limit: Int = MAX_RECOMMENDATIONS
    ): List<Recommendation> {
        return try {
            // Check cache first
            val cachedRecommendations = recommendationCache[userId]
            if (cachedRecommendations != null && !cachedRecommendations.isExpired()) {
                return cachedRecommendations.recommendations
            }

            // Get user preferences and history
            val userPrefs = getUserPreferences(userId)
            val userHistory = getUserEventHistory(userId)
            
            // Generate different types of recommendations
            val recommendations = mutableListOf<Recommendation>()
            
            // Content-based recommendations
            val contentBased = generateContentBasedRecommendations(userPrefs, userHistory, limit / 3)
            recommendations.addAll(contentBased)
            
            // Collaborative filtering recommendations
            val collaborative = generateCollaborativeRecommendations(userId, limit / 3)
            recommendations.addAll(collaborative)
            
            // Popularity-based recommendations
            val popularityBased = generatePopularityBasedRecommendations(limit / 3)
            recommendations.addAll(popularityBased)
            
            // Location-based recommendations
            val locationBased = generateLocationBasedRecommendations(userId, limit / 3)
            recommendations.addAll(locationBased)
            
            // Time-based recommendations
            val timeBased = generateTimeBasedRecommendations(userId, limit / 3)
            recommendations.addAll(timeBased)
            
            // Remove duplicates and rank
            val uniqueRecommendations = removeDuplicateRecommendations(recommendations)
            val rankedRecommendations = rankRecommendations(uniqueRecommendations, userPrefs)
            
            // Cache recommendations
            cacheRecommendations(userId, rankedRecommendations)
            
            // Track recommendation analytics
            trackRecommendationAnalytics(userId, rankedRecommendations)
            
            rankedRecommendations.take(limit)
        } catch (e: Exception) {
            emptyList()
        }
    }

    // Advanced search query building
    private fun buildSearchQuery(query: SearchQuery): com.google.firebase.firestore.Query {
        var baseQuery: com.google.firebase.firestore.Query = firestore.collection("events")
        
        // Text search
        if (query.text.isNotEmpty()) {
            // For now, we'll use simple text matching
            // In production, consider using Algolia or Elasticsearch for full-text search
            baseQuery = baseQuery.whereGreaterThanOrEqualTo("title", query.text)
                .whereLessThanOrEqualTo("title", query.text + '\uf8ff')
        }
        
        // Category filter
        if (query.categories.isNotEmpty()) {
            baseQuery = baseQuery.whereIn("category", query.categories)
        }
        
        // Date range filter
        if (query.startDate != null) {
            baseQuery = baseQuery.whereGreaterThanOrEqualTo("dateTime", query.startDate)
        }
        if (query.endDate != null) {
            baseQuery = baseQuery.whereLessThanOrEqualTo("dateTime", query.endDate)
        }
        
        // Price range filter
        if (query.minPrice != null) {
            baseQuery = baseQuery.whereGreaterThanOrEqualTo("price", query.minPrice)
        }
        if (query.maxPrice != null) {
            baseQuery = baseQuery.whereLessThanOrEqualTo("price", query.maxPrice)
        }
        
        // Location filter
        if (query.location != null) {
            baseQuery = baseQuery.whereEqualTo("location.city", query.location.city)
        }
        
        // Featured events
        if (query.featuredOnly == true) {
            baseQuery = baseQuery.whereEqualTo("featured", true)
        }
        
        // Order by relevance (date, popularity, etc.)
        baseQuery = baseQuery.orderBy("dateTime", Query.Direction.ASCENDING)
            .orderBy("featured", Query.Direction.DESCENDING)
        
        return baseQuery
    }

    // Advanced ranking algorithm with multiple factors
    private suspend fun applyAdvancedRanking(events: List<Event>, query: SearchQuery): List<Event> {
        return events.map { event ->
            val score = calculateEventScore(event, query)
            event.copy(metadata = event.metadata + mapOf("searchScore" to score.toString()))
        }.sortedByDescending { 
            it.metadata["searchScore"]?.toDoubleOrNull() ?: 0.0 
        }
    }

    private fun calculateEventScore(event: Event, query: SearchQuery): Double {
        var score = 0.0
        
        // Text relevance score
        val textScore = calculateTextRelevanceScore(event, query.text)
        score += textScore * 0.3
        
        // Category relevance score
        val categoryScore = calculateCategoryRelevanceScore(event, query.categories)
        score += categoryScore * 0.2
        
        // Date relevance score
        val dateScore = calculateDateRelevanceScore(event, query.startDate, query.endDate)
        score += dateScore * 0.2
        
        // Price relevance score
        val priceScore = calculatePriceRelevanceScore(event, query.minPrice, query.maxPrice)
        score += priceScore * 0.1
        
        // Location relevance score
        val locationScore = calculateLocationRelevanceScore(event, query.location)
        score += locationScore * 0.1
        
        // Popularity score
        val popularityScore = calculatePopularityScore(event)
        score += popularityScore * 0.1
        
        return score
    }

    private fun calculateTextRelevanceScore(event: Event, searchText: String): Double {
        if (searchText.isEmpty()) return 1.0
        
        val titleMatch = event.title.contains(searchText, ignoreCase = true)
        val descriptionMatch = event.description.contains(searchText, ignoreCase = true)
        
        return when {
            titleMatch -> 1.0
            descriptionMatch -> 0.7
            else -> 0.0
        }
    }

    private fun calculateCategoryRelevanceScore(event: Event, categories: List<EventCategory>): Double {
        if (categories.isEmpty()) return 1.0
        
        return if (event.category in categories) 1.0 else 0.0
    }

    private fun calculateDateRelevanceScore(event: Event, startDate: Long?, endDate: Long?): Double {
        val eventTime = event.dateTime
        val now = System.currentTimeMillis()
        
        // Past events get lower scores
        if (eventTime < now) return 0.0
        
        // Events happening soon get higher scores
        val timeUntilEvent = eventTime - now
        val daysUntilEvent = timeUntilEvent / (24 * 60 * 60 * 1000)
        
        return when {
            daysUntilEvent <= 1 -> 1.0
            daysUntilEvent <= 7 -> 0.9
            daysUntilEvent <= 30 -> 0.8
            daysUntilEvent <= 90 -> 0.6
            else -> 0.4
        }
    }

    private fun calculatePriceRelevanceScore(event: Event, minPrice: Double?, maxPrice: Double?): Double {
        if (minPrice == null && maxPrice == null) return 1.0
        
        val eventPrice = event.price ?: 0.0
        
        return when {
            minPrice != null && maxPrice != null -> {
                if (eventPrice in minPrice..maxPrice) 1.0 else 0.0
            }
            minPrice != null -> {
                if (eventPrice >= minPrice) 1.0 else 0.0
            }
            maxPrice != null -> {
                if (eventPrice <= maxPrice) 1.0 else 0.0
            }
            else -> 1.0
        }
    }

    private fun calculateLocationRelevanceScore(event: Event, searchLocation: Location?): Double {
        if (searchLocation == null) return 1.0
        
        // Calculate distance between event location and search location
        val distance = calculateDistance(
            event.location.latitude, event.location.longitude,
            searchLocation.latitude, searchLocation.longitude
        )
        
        return when {
            distance <= 5 -> 1.0      // Within 5km
            distance <= 10 -> 0.9     // Within 10km
            distance <= 25 -> 0.8     // Within 25km
            distance <= 50 -> 0.6     // Within 50km
            else -> 0.4               // Beyond 50km
        }
    }

    private fun calculatePopularityScore(event: Event): Double {
        val likes = event.likes ?: 0
        val shares = event.shares ?: 0
        val views = event.views ?: 0
        
        val totalEngagement = likes + shares + views
        val maxEngagement = 1000 // Normalize to 0-1 scale
        
        return minOf(totalEngagement.toDouble() / maxEngagement, 1.0)
    }

    // Content-based recommendation algorithm
    private suspend fun generateContentBasedRecommendations(
        userPrefs: UserPreference,
        userHistory: List<Event>,
        limit: Int
    ): List<Recommendation> {
        val recommendations = mutableListOf<Recommendation>()
        
        // Get user's preferred categories
        val preferredCategories = userPrefs.preferredCategories
        
        // Find events in preferred categories
        val categoryEvents = firestore.collection("events")
            .whereIn("category", preferredCategories)
            .whereGreaterThan("dateTime", System.currentTimeMillis())
            .orderBy("dateTime", Query.Direction.ASCENDING)
            .limit(limit.toLong())
            .get()
            .await()
            .toObjects(Event::class.java)
        
        categoryEvents.forEach { event: Event ->
            recommendations.add(
                Recommendation(
                    id = "content_${event.id}",
                    eventId = event.id,
                    type = RecommendationType.CONTENT_BASED,
                    score = calculateContentBasedScore(event, userPrefs),
                    reason = "Based on your interest in ${event.category} events",
                    timestamp = System.currentTimeMillis()
                )
            )
        }
        
        return recommendations
    }

    // Collaborative filtering recommendation algorithm
    private suspend fun generateCollaborativeRecommendations(
        userId: String,
        limit: Int
    ): List<Recommendation> {
        val recommendations = mutableListOf<Recommendation>()
        
        // Find similar users
        val similarUsers = findSimilarUsers(userId)
        
        // Get events liked by similar users
        val similarUserEvents = mutableSetOf<String>()
        similarUsers.forEach { (similarUserId, similarity): Map.Entry<String, Double> ->
            if (similarity >= MIN_SIMILARITY_THRESHOLD) {
                val userLikes = getUserLikedEvents(similarUserId)
                similarUserEvents.addAll(userLikes)
            }
        }
        
        // Get current user's liked events to avoid duplicates
        val currentUserLikes = getUserLikedEvents(userId)
        val newEvents = similarUserEvents - currentUserLikes
        
        // Get event details and create recommendations
        newEvents.take(limit).forEach { eventId: String ->
            val event = getEventById(eventId)
            if (event != null) {
                recommendations.add(
                    Recommendation(
                        id = "collab_${eventId}",
                        eventId = eventId,
                        type = RecommendationType.COLLABORATIVE,
                        score = calculateCollaborativeScore(eventId, similarUsers),
                        reason = "People with similar tastes liked this event",
                        timestamp = System.currentTimeMillis()
                    )
                )
            }
        }
        
        return recommendations
    }

    // Popularity-based recommendation algorithm
    private suspend fun generatePopularityBasedRecommendations(limit: Int): List<Recommendation> {
        val recommendations = mutableListOf<Recommendation>()
        
        // Get trending events
        val trendingEvents = firestore.collection("events")
            .whereGreaterThan("dateTime", System.currentTimeMillis())
            .orderBy("likes", Query.Direction.DESCENDING)
            .orderBy("shares", Query.Direction.DESCENDING)
            .limit(limit.toLong())
            .get()
            .await()
            .toObjects(Event::class.java)
        
        trendingEvents.forEach { event: Event ->
            recommendations.add(
                Recommendation(
                    id = "popular_${event.id}",
                    eventId = event.id,
                    type = RecommendationType.POPULARITY_BASED,
                    score = calculatePopularityScore(event),
                    reason = "This event is trending right now",
                    timestamp = System.currentTimeMillis()
                )
            )
        }
        
        return recommendations
    }

    // Location-based recommendation algorithm
    private suspend fun generateLocationBasedRecommendations(
        userId: String,
        limit: Int
    ): List<Recommendation> {
        val recommendations = mutableListOf<Recommendation>()
        
        // Get user's location
        val userLocation = getUserLocation(userId)
        if (userLocation == null) return recommendations
        
        // Find nearby events
        val nearbyEvents = findNearbyEvents(userLocation, limit)
        
        nearbyEvents.forEach { event: Event ->
            val distance = calculateDistance(
                userLocation.latitude, userLocation.longitude,
                event.location.latitude, event.location.longitude
            )
            
            recommendations.add(
                Recommendation(
                    id = "location_${event.id}",
                    eventId = event.id,
                    type = RecommendationType.LOCATION_BASED,
                    score = 1.0 - (distance / 100.0), // Closer events get higher scores
                    reason = "Happening near you",
                    timestamp = System.currentTimeMillis()
                )
            )
        }
        
        return recommendations
    }

    // Time-based recommendation algorithm
    private suspend fun generateTimeBasedRecommendations(
        userId: String,
        limit: Int
    ): List<Recommendation> {
        val recommendations = mutableListOf<Recommendation>()
        
        // Get user's preferred time patterns
        val timePreferences = getUserTimePreferences(userId)
        
        // Find events matching time preferences
        val timeBasedEvents = findEventsByTimePreferences(timePreferences, limit)
        
        timeBasedEvents.forEach { event: Event ->
            recommendations.add(
                Recommendation(
                    id = "time_${event.id}",
                    eventId = event.id,
                    type = RecommendationType.TIME_BASED,
                    score = calculateTimeBasedScore(event, timePreferences),
                    reason = "Matches your preferred event timing",
                    timestamp = System.currentTimeMillis()
                )
            )
        }
        
        return recommendations
    }

    // Utility functions
    private fun calculateDistance(lat1: Double, lon1: Double, lat2: Double, lon2: Double): Double {
        val r = 6371 // Earth's radius in kilometers
        val dLat = Math.toRadians(lat2 - lat1)
        val dLon = Math.toRadians(lon2 - lon1)
        val a = sin(dLat / 2) * sin(dLat / 2) +
                cos(Math.toRadians(lat1)) * cos(Math.toRadians(lat2)) *
                sin(dLon / 2) * sin(dLon / 2)
        val c = 2 * atan2(sqrt(a), sqrt(1 - a))
        return r * c
    }

    private fun generateSearchCacheKey(query: SearchQuery): String {
        return "${query.text}_${query.categories.joinToString("_")}_${query.startDate}_${query.endDate}_${query.minPrice}_${query.maxPrice}_${query.location?.city}"
    }

    private fun generateSearchSuggestions(query: SearchQuery): List<String> {
        // Generate search suggestions based on query
        val suggestions = mutableListOf<String>()
        
        if (query.text.isNotEmpty()) {
            suggestions.add("${query.text} events")
            suggestions.add("${query.text} concerts")
            suggestions.add("${query.text} parties")
        }
        
        query.categories.forEach { category ->
            suggestions.add("$category events")
        }
        
        return suggestions.distinct()
    }

    private fun generateSearchFacets(events: List<Event>): Map<String, List<String>> {
        val facets = mutableMapOf<String, MutableSet<String>>()
        
        events.forEach { event ->
            // Category facets
            facets.getOrPut("category") { mutableSetOf() }.add(event.category.name)
            
            // Price facets
            val priceRange = when {
                event.price == null || event.price == 0.0 -> "Free"
                event.price <= 1000 -> "Under KES 1,000"
                event.price <= 5000 -> "KES 1,000 - 5,000"
                event.price <= 10000 -> "KES 5,000 - 10,000"
                else -> "Over KES 10,000"
            }
            facets.getOrPut("price") { mutableSetOf() }.add(priceRange)
            
            // Date facets
            val eventDate = Date(event.dateTime)
            val calendar = Calendar.getInstance().apply { time = eventDate }
            val dayOfWeek = calendar.getDisplayName(Calendar.DAY_OF_WEEK, Calendar.LONG, Locale.getDefault())
            facets.getOrPut("dayOfWeek") { mutableSetOf() }.add(dayOfWeek)
        }
        
        return facets.mapValues { it.value.toList() }
    }

    // Placeholder functions for integration
    private suspend fun executeSearchWithPagination(query: Query, limit: Int): List<Event> {
        return query.limit(limit.toLong()).get().await().toObjects(Event::class.java)
    }

    private fun applySearchFilters(events: List<Event>, query: SearchQuery): List<Event> {
        return events // Implement additional filtering logic here
    }

    private fun cacheSearchResult(key: String, result: SearchResult) {
        searchCache[key] = CachedSearchResult(result, System.currentTimeMillis())
    }

    private fun trackSearchAnalytics(query: SearchQuery, result: SearchResult) {}
    private fun getUserPreferences(userId: String): UserPreference = UserPreference()
    private fun getUserEventHistory(userId: String): List<Event> = emptyList()
    private fun removeDuplicateRecommendations(recommendations: List<Recommendation>): List<Recommendation> = recommendations
    private fun rankRecommendations(recommendations: List<Recommendation>, userPrefs: UserPreference): List<Recommendation> = recommendations
    private fun cacheRecommendations(userId: String, recommendations: List<Recommendation>) {
        recommendationCache[userId] = CachedRecommendations(recommendations, System.currentTimeMillis())
    }
    private fun trackRecommendationAnalytics(userId: String, recommendations: List<Recommendation>) {}
    private fun calculateContentBasedScore(event: Event, userPrefs: UserPreference): Double = 0.8
    private fun findSimilarUsers(userId: String): Map<String, Double> = emptyMap()
    private fun getUserLikedEvents(userId: String): List<String> = emptyList()
    private fun calculateCollaborativeScore(eventId: String, similarUsers: Map<String, Double>): Double = 0.7
    private fun getEventById(eventId: String): Event? = null
    private fun getUserLocation(userId: String): Location? = null
    private fun findNearbyEvents(location: Location, limit: Int): List<Event> = emptyList()
    private fun getUserTimePreferences(userId: String): Map<String, Any> = emptyMap()
    private fun findEventsByTimePreferences(preferences: Map<String, Any>, limit: Int): List<Event> = emptyList()
    private fun calculateTimeBasedScore(event: Event, preferences: Map<String, Any>): Double = 0.6

    // Data classes
    data class CachedSearchResult(
        val result: SearchResult,
        val timestamp: Long
    ) {
        fun isExpired(): Boolean {
            return System.currentTimeMillis() - timestamp > SEARCH_CACHE_TTL_HOURS * 60 * 60 * 1000
        }
    }

    data class CachedRecommendations(
        val recommendations: List<Recommendation>,
        val timestamp: Long
    ) {
        fun isExpired(): Boolean {
            return System.currentTimeMillis() - timestamp > 30 * 60 * 1000 // 30 minutes
        }
    }

    // Cleanup and resource management
    fun cleanup() {
        serviceScope.cancel()
        searchCache.clear()
        recommendationCache.clear()
        eventSimilarityMatrix.clear()
        userSimilarityMatrix.clear()
    }
}
