package io.sws.watchstack.domain.usecase

import io.sws.watchstack.domain.model.TrackedAnime
import io.sws.watchstack.domain.repository.TrackedAnimeRepository

class GetTrackedByMalIdUseCase(private val repository: TrackedAnimeRepository) {
    suspend operator fun invoke(malId: Int): TrackedAnime? = repository.getTracked(malId)
}
