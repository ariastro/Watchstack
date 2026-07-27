package io.sws.watchstack.domain.usecase

import io.sws.watchstack.domain.model.RecommendedAnime
import io.sws.watchstack.domain.repository.AnimeRepository

class GetRecommendationsUseCase(private val repository: AnimeRepository) {
    suspend operator fun invoke(malId: Int): Result<List<RecommendedAnime>> =
        repository.getRecommendations(malId)
}
