package com.littlegig.app.data.model

data class SearchResult(
    val query: SearchQuery,
    val events: List<Event>,
    val totalCount: Int,
    val searchTime: Long,
    val suggestions: List<String> = emptyList(),
    val facets: Map<String, List<String>> = emptyMap(),
    val error: String? = null
)
