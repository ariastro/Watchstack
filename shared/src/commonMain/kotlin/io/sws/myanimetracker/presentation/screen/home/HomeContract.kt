package io.sws.myanimetracker.presentation.screen.home

import io.sws.myanimetracker.domain.model.Anime

data class HomeUiState(
    val airingNow: List<Anime> = emptyList(),
    val topAnime: List<Anime> = emptyList(),
    val thisSeason: List<Anime> = emptyList(),
    val upcoming: List<Anime> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null
)

sealed interface HomeIntent {
    data object Load : HomeIntent
    data class AnimeClicked(val malId: Int, val anime: Anime? = null) : HomeIntent
    data object ClearError : HomeIntent
}
