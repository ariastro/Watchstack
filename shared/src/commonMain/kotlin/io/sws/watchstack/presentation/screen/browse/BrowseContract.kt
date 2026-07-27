package io.sws.watchstack.presentation.screen.browse

import io.sws.watchstack.core.AnimeSeason
import io.sws.watchstack.domain.model.Anime

@kotlinx.serialization.Serializable
enum class BrowseCategory(val key: String, val label: String) {
    AIRING("airing", "Now Airing"),
    TOP("top", "Top Anime"),
    SEASON("season", "Seasons"),
    UPCOMING("upcoming", "Upcoming")
}

enum class BrowseSort(val label: String) {
    DEFAULT("Default"),
    SCORE("Score"),
    TITLE("Title"),
    YEAR("Year")
}

data class BrowseUiState(
    val category: BrowseCategory = BrowseCategory.TOP,
    val anime: List<Anime> = emptyList(),
    val filteredAnime: List<Anime> = emptyList(),
    val isLoading: Boolean = false,
    val isLoadingMore: Boolean = false,
    val error: String? = null,
    val selectedSeason: AnimeSeason = AnimeSeason(0, "winter"),
    val page: Int = 1,
    val hasNext: Boolean = false,
    val sort: BrowseSort = BrowseSort.DEFAULT,
    val typeFilter: String? = null,
    val minScore: Double? = null,
    val availableTypes: List<String> = emptyList()
)

sealed interface BrowseIntent {
    data object Load : BrowseIntent
    data object LoadMore : BrowseIntent
    data object GoBack : BrowseIntent
    data class AnimeClicked(val malId: Int, val anime: Anime? = null) : BrowseIntent
    data class SeasonSelected(val season: AnimeSeason) : BrowseIntent
    data class SortSelected(val sort: BrowseSort) : BrowseIntent
    data class TypeFilterSelected(val type: String?) : BrowseIntent
    data class MinScoreSelected(val score: Double?) : BrowseIntent
}
