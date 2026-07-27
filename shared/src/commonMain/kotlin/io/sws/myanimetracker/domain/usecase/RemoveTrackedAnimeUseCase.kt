package io.sws.myanimetracker.domain.usecase

import io.sws.myanimetracker.domain.repository.TrackedAnimeRepository

class RemoveTrackedAnimeUseCase(private val repository: TrackedAnimeRepository) {
    suspend operator fun invoke(malId: Int): Result<Unit> = repository.removeTracked(malId)
}
