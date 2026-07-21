package io.sws.myanimetracker.domain.usecase

import io.sws.myanimetracker.domain.model.PagedAnime
import io.sws.myanimetracker.domain.repository.AnimeRepository

class GetSeasonalAnimeUseCase(private val repository: AnimeRepository) {
    suspend operator fun invoke(year: Int, season: String, page: Int = 1): Result<PagedAnime> =
        repository.getSeasonalAnime(year, season, page)
}
