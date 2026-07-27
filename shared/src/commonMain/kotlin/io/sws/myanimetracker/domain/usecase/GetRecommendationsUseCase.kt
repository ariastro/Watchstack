package io.sws.myanimetracker.domain.usecase

import io.sws.myanimetracker.domain.model.RecommendedAnime
import io.sws.myanimetracker.domain.repository.AnimeRepository

class GetRecommendationsUseCase(private val repository: AnimeRepository) {
    suspend operator fun invoke(malId: Int): Result<List<RecommendedAnime>> =
        repository.getRecommendations(malId)
}
