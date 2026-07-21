package io.sws.myanimetracker.presentation.screen.stats

import io.sws.myanimetracker.domain.model.LibraryStats

data class StatsUiState(
    val stats: LibraryStats = LibraryStats(
        total = 0,
        watchlist = 0,
        watching = 0,
        watched = 0,
        episodesWatched = 0,
        averageRating = null,
        topGenres = emptyList()
    )
)

sealed interface StatsIntent {
    data object GoBack : StatsIntent
}
