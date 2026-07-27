package io.sws.myanimetracker.domain.usecase

import io.sws.myanimetracker.domain.model.TrackedAnime
import io.sws.myanimetracker.domain.repository.TrackedAnimeRepository

class UpdateTrackedAnimeUseCase(private val repository: TrackedAnimeRepository) {
    suspend operator fun invoke(tracked: TrackedAnime): Result<Unit> = repository.updateTracked(tracked)
}
