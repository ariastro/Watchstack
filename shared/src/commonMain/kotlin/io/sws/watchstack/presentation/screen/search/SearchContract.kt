package io.sws.watchstack.presentation.screen.search

import io.sws.watchstack.domain.model.Anime

data class SearchUiState(
    val query: String = "",
    val results: List<Anime> = emptyList(),
    val recentQueries: List<String> = emptyList(),
    val page: Int = 1,
    val hasNext: Boolean = false,
    val isLoading: Boolean = false,
    val isLoadingMore: Boolean = false,
    val error: String? = null,
    val hasSearched: Boolean = false
)

sealed interface SearchIntent {
    data class QueryChanged(val query: String) : SearchIntent
    data object Search : SearchIntent
    data object LoadMore : SearchIntent
    data object Clear : SearchIntent
    data object ClearHistory : SearchIntent
    data class RecentClicked(val query: String) : SearchIntent
    data class AnimeClicked(val malId: Int, val anime: Anime? = null) : SearchIntent
}
