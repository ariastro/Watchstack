package io.sws.watchstack.domain.usecase

import io.sws.watchstack.domain.model.TrackedAnime
import io.sws.watchstack.domain.repository.TrackedAnimeRepository

class UpdateTrackedAnimeUseCase(private val repository: TrackedAnimeRepository) {
    suspend operator fun invoke(tracked: TrackedAnime): Result<Unit> = repository.updateTracked(tracked)
}
