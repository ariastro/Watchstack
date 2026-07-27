package io.sws.watchstack.presentation.screen.tracked

import io.sws.watchstack.domain.model.Anime
import io.sws.watchstack.domain.model.TrackedAnime
import io.sws.watchstack.domain.model.WatchStatus
import io.sws.watchstack.presentation.UiEffect

enum class TrackedSort(val label: String) {
    DATE_ADDED("Recent"),
    TITLE("Title"),
    PROGRESS("Progress"),
    RATING("Rating")
}

data class TrackedUiState(
    val activeTab: WatchStatus = WatchStatus.WATCHLIST,
    val watchlist: List<TrackedAnime> = emptyList(),
    val watching: List<TrackedAnime> = emptyList(),
    val watched: List<TrackedAnime> = emptyList(),
    val query: String = "",
    val sort: TrackedSort = TrackedSort.DATE_ADDED
) {
    val rawList: List<TrackedAnime>
        get() = when (activeTab) {
            WatchStatus.WATCHLIST -> watchlist
            WatchStatus.WATCHING -> watching
            WatchStatus.WATCHED -> watched
        }

    val visibleList: List<TrackedAnime>
        get() {
            val filtered = if (query.isBlank()) {
                rawList
            } else {
                val q = query.trim()
                rawList.filter {
                    it.anime.title.contains(q, ignoreCase = true) ||
                        it.anime.titleJapanese.orEmpty().contains(q, ignoreCase = true) ||
                        it.whereToWatch.contains(q, ignoreCase = true) ||
                        it.notes.contains(q, ignoreCase = true)
                }
            }
            return when (sort) {
                TrackedSort.DATE_ADDED -> filtered.sortedByDescending { it.dateAdded }
                TrackedSort.TITLE -> filtered.sortedBy { it.anime.title.lowercase() }
                TrackedSort.PROGRESS -> filtered.sortedByDescending { it.progressFraction() }
                TrackedSort.RATING -> filtered.sortedByDescending { it.userRating ?: -1 }
            }
        }

    val totalCount: Int get() = watchlist.size + watching.size + watched.size
}

fun TrackedAnime.progressFraction(): Float {
    val total = anime.episodes
    if (total != null && total > 0) {
        return (episodesWatched.toFloat() / total).coerceIn(0f, 1f)
    }
    return if (episodesWatched > 0) 0.15f.coerceAtMost(1f) else 0f
}

fun TrackedAnime.progressLabel(): String {
    val total = anime.episodes
    return if (total != null && total > 0) {
        "Ep $episodesWatched / $total"
    } else {
        "Ep $episodesWatched"
    }
}

sealed interface TrackedIntent {
    data class TabSelected(val tab: WatchStatus) : TrackedIntent
    data class QueryChanged(val query: String) : TrackedIntent
    data class SortSelected(val sort: TrackedSort) : TrackedIntent
    data class AnimeClicked(val malId: Int, val anime: Anime? = null) : TrackedIntent
    data class RemoveAnime(val malId: Int) : TrackedIntent
    data class ChangeStatus(val malId: Int, val status: WatchStatus) : TrackedIntent
    data class IncrementEpisode(val malId: Int) : TrackedIntent
    data object UndoRemove : TrackedIntent
    data object OpenSearch : TrackedIntent
}

typealias TrackedEffect = UiEffect

const val TRACKED_UNDO_ACTION = "undo_remove"
