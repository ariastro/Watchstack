package io.sws.myanimetracker.domain.usecase

import io.sws.myanimetracker.domain.model.PagedAnime
import io.sws.myanimetracker.domain.repository.AnimeRepository

class SearchAnimeUseCase(private val repository: AnimeRepository) {
    suspend operator fun invoke(query: String, page: Int = 1): Result<PagedAnime> =
        repository.searchAnime(query, page)
}
