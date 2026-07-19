package io.sws.myanimetracker.presentation.screen.tracked

import io.sws.myanimetracker.domain.model.Anime
import io.sws.myanimetracker.domain.model.TrackedAnime
import io.sws.myanimetracker.domain.model.WatchStatus

data class TrackedUiState(
    val activeTab: WatchStatus = WatchStatus.WATCHLIST,
    val watchlist: List<TrackedAnime> = emptyList(),
    val watching: List<TrackedAnime> = emptyList(),
    val watched: List<TrackedAnime> = emptyList(),
    val error: String? = null
)

sealed interface TrackedIntent {
    data class TabSelected(val tab: WatchStatus) : TrackedIntent
    data class AnimeClicked(val malId: Int, val anime: Anime? = null) : TrackedIntent
    data class RemoveAnime(val malId: Int) : TrackedIntent
    data object ClearError : TrackedIntent
}
