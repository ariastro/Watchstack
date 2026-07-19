package io.sws.myanimetracker.presentation.screen.browse

import io.sws.myanimetracker.core.AnimeSeason
import io.sws.myanimetracker.domain.model.Anime

enum class BrowseCategory(val key: String, val label: String) {
    AIRING("airing", "Now Airing"),
    TOP("top", "Top Anime"),
    SEASON("season", "Seasons"),
    UPCOMING("upcoming", "Upcoming")
}

data class BrowseUiState(
    val category: BrowseCategory = BrowseCategory.TOP,
    val anime: List<Anime> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null,
    val selectedSeason: AnimeSeason = AnimeSeason(0, "winter")
)

sealed interface BrowseIntent {
    data object Load : BrowseIntent
    data class AnimeClicked(val malId: Int, val anime: Anime? = null) : BrowseIntent
    data object ClearError : BrowseIntent
    data class SeasonSelected(val season: AnimeSeason) : BrowseIntent
}
