package io.sws.watchstack.domain.usecase

import io.sws.watchstack.domain.model.PagedAnime
import io.sws.watchstack.domain.repository.AnimeRepository

class SearchAnimeUseCase(private val repository: AnimeRepository) {
    suspend operator fun invoke(query: String, page: Int = 1): Result<PagedAnime> =
        repository.searchAnime(query, page)
}
