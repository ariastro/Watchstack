package io.sws.watchstack.domain.usecase

import io.sws.watchstack.domain.model.PagedAnime
import io.sws.watchstack.domain.repository.AnimeRepository

class GetSeasonalAnimeUseCase(private val repository: AnimeRepository) {
    suspend operator fun invoke(year: Int, season: String, page: Int = 1): Result<PagedAnime> =
        repository.getSeasonalAnime(year, season, page)
}
