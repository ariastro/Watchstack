package io.sws.myanimetracker.domain.usecase

import io.sws.myanimetracker.core.currentTimeMillis
import io.sws.myanimetracker.core.runSafely
import io.sws.myanimetracker.domain.model.Anime
import io.sws.myanimetracker.domain.model.TrackedAnime
import io.sws.myanimetracker.domain.model.WatchStatus
import io.sws.myanimetracker.domain.repository.TrackedAnimeRepository

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
