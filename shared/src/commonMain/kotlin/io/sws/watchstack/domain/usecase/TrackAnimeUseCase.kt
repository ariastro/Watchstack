package io.sws.watchstack.domain.usecase

import io.sws.watchstack.core.currentTimeMillis
import io.sws.watchstack.core.runSafely
import io.sws.watchstack.domain.model.Anime
import io.sws.watchstack.domain.model.TrackedAnime
import io.sws.watchstack.domain.model.WatchStatus
import io.sws.watchstack.domain.repository.TrackedAnimeRepository

class TrackAnimeUseCase(private val repository: TrackedAnimeRepository) {
    suspend operator fun invoke(anime: Anime, status: WatchStatus, whereToWatch: String = ""): Result<TrackedAnime> {
        val tracked = TrackedAnime(
            anime = anime,
            status = status,
            whereToWatch = whereToWatch,
            dateAdded = currentTimeMillis()
        )
        return runSafely {
            repository.trackAnime(tracked)
            tracked
        }
    }
}
