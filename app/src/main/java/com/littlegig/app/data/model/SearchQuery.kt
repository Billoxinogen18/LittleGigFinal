package com.littlegig.app.data.model

data class SearchQuery(
    val text: String = "",
    val categories: List<ContentCategory> = emptyList(),
    val startDate: Long? = null,
    val endDate: Long? = null,
    val minPrice: Double? = null,
    val maxPrice: Double? = null,
    val location: Location? = null,
    val featuredOnly: Boolean? = null,
    val limit: Int? = null,
    val sortBy: SortOption = SortOption.RELEVANCE,
    val filters: Map<String, Any> = emptyMap()
)

enum class SortOption {
    RELEVANCE,
    DATE_ASC,
    DATE_DESC,
    PRICE_ASC,
    PRICE_DESC,
    POPULARITY,
    DISTANCE
}
