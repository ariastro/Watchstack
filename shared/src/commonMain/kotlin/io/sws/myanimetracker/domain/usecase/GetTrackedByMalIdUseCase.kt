package io.sws.myanimetracker.domain.usecase

import io.sws.myanimetracker.domain.model.TrackedAnime
import io.sws.myanimetracker.domain.repository.TrackedAnimeRepository

class GetTrackedByMalIdUseCase(private val repository: TrackedAnimeRepository) {
    suspend operator fun invoke(malId: Int): TrackedAnime? = repository.getTracked(malId)
}
