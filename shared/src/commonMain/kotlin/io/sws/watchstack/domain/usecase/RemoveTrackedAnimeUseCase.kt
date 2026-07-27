package io.sws.watchstack.domain.usecase

import io.sws.watchstack.domain.repository.TrackedAnimeRepository

class RemoveTrackedAnimeUseCase(private val repository: TrackedAnimeRepository) {
    suspend operator fun invoke(malId: Int): Result<Unit> = repository.removeTracked(malId)
}
