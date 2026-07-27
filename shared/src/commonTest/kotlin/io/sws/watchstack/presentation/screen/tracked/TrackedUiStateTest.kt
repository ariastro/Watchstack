package io.sws.watchstack.presentation.screen.tracked

import io.sws.watchstack.domain.model.Anime
import io.sws.watchstack.domain.model.TrackedAnime
import io.sws.watchstack.domain.model.WatchStatus
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class TrackedUiStateTest {

    private fun anime(id: Int, title: String, episodes: Int? = 12) =
        Anime(malId = id, title = title, episodes = episodes)

    private fun tracked(
        id: Int,
        title: String,
        status: WatchStatus = WatchStatus.WATCHING,
        ep: Int = 0,
        rating: Int? = null,
        date: Long = 0L
    ) = TrackedAnime(
        anime = anime(id, title),
        status = status,
        episodesWatched = ep,
        userRating = rating,
        dateAdded = date
    )

    @Test
    fun visibleList_filtersByQuery() {
        val state = TrackedUiState(
            activeTab = WatchStatus.WATCHING,
            watching = listOf(
                tracked(1, "Naruto"),
                tracked(2, "One Piece")
            ),
            query = "naru"
        )
        assertEquals(listOf(1), state.visibleList.map { it.anime.malId })
    }

    @Test
    fun visibleList_sortsByTitle() {
        val state = TrackedUiState(
            activeTab = WatchStatus.WATCHING,
            watching = listOf(
                tracked(1, "Zeta"),
                tracked(2, "Alpha")
            ),
            sort = TrackedSort.TITLE
        )
        assertEquals(listOf("Alpha", "Zeta"), state.visibleList.map { it.anime.title })
    }

    @Test
    fun visibleList_sortsByProgress() {
        val state = TrackedUiState(
            activeTab = WatchStatus.WATCHING,
            watching = listOf(
                tracked(1, "A", ep = 2),
                tracked(2, "B", ep = 10)
            ),
            sort = TrackedSort.PROGRESS
        )
        assertEquals(listOf(2, 1), state.visibleList.map { it.anime.malId })
    }

    @Test
    fun progressFraction_capsAtOne() {
        val t = tracked(1, "Done", ep = 20).copy(anime = anime(1, "Done", episodes = 12))
        assertEquals(1f, t.progressFraction())
    }

    @Test
    fun progressLabel_withTotal() {
        val t = tracked(1, "X", ep = 3).copy(anime = anime(1, "X", episodes = 12))
        assertEquals("Ep 3 / 12", t.progressLabel())
    }

    @Test
    fun totalCount_sumsTabs() {
        val state = TrackedUiState(
            watchlist = listOf(tracked(1, "A", WatchStatus.WATCHLIST)),
            watching = listOf(tracked(2, "B"), tracked(3, "C")),
            watched = emptyList()
        )
        assertEquals(3, state.totalCount)
        assertEquals(1, state.copy(activeTab = WatchStatus.WATCHLIST).rawList.size)
        assertEquals(2, state.copy(activeTab = WatchStatus.WATCHING).rawList.size)
    }
}
