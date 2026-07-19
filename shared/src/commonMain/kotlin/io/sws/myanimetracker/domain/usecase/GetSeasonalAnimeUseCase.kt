package io.sws.myanimetracker.domain.usecase

import io.sws.myanimetracker.domain.model.Anime
import io.sws.myanimetracker.domain.repository.AnimeRepository

class GetSeasonalAnimeUseCase(private val repository: AnimeRepository) {
    suspend operator fun invoke(year: Int, season: String): Result<List<Anime>> =
        repository.getSeasonalAnime(year, season)
}
