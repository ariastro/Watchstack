package io.sws.myanimetracker.presentation.screen.search

import io.sws.myanimetracker.domain.model.Anime
import io.sws.myanimetracker.presentation.screen.browse.BrowseCategory

data class SearchUiState(
    val query: String = "",
    val results: List<Anime> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null,
    val hasSearched: Boolean = false
)

sealed interface SearchIntent {
    data class QueryChanged(val query: String) : SearchIntent
    data object Search : SearchIntent
    data object ClearError : SearchIntent
    data class AnimeClicked(val malId: Int, val anime: Anime? = null) : SearchIntent
    data class BrowseClicked(val category: BrowseCategory) : SearchIntent
}
