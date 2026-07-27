package io.sws.watchstack.presentation.screen.home

import io.sws.watchstack.domain.model.Anime
import io.sws.watchstack.domain.model.TrackedAnime
import io.sws.watchstack.presentation.screen.browse.BrowseCategory

data class HomeUiState(
    val airingNow: List<Anime> = emptyList(),
    val topAnime: List<Anime> = emptyList(),
    val thisSeason: List<Anime> = emptyList(),
    val upcoming: List<Anime> = emptyList(),
    val continueWatching: List<TrackedAnime> = emptyList(),
    val isLoading: Boolean = false,
    val isRefreshing: Boolean = false,
    val error: String? = null
)

sealed interface HomeIntent {
    data object Load : HomeIntent
    data object Refresh : HomeIntent
    data object OpenSettings : HomeIntent
    data object OpenStats : HomeIntent
    data class AnimeClicked(val malId: Int, val anime: Anime? = null) : HomeIntent
    data class OpenBrowse(val category: BrowseCategory) : HomeIntent
}
