package io.sws.watchstack.domain.usecase

import io.sws.watchstack.domain.model.PagedAnime
import io.sws.watchstack.domain.repository.AnimeRepository

class GetTopAnimeUseCase(private val repository: AnimeRepository) {
    suspend operator fun invoke(page: Int = 1): Result<PagedAnime> = repository.getTopAnime(page)
}
