package io.sws.myanimetracker.domain.usecase

import io.sws.myanimetracker.domain.model.Anime
import io.sws.myanimetracker.domain.repository.AnimeRepository

class SearchAnimeUseCase(private val repository: AnimeRepository) {
    suspend operator fun invoke(query: String): Result<List<Anime>> = repository.searchAnime(query)
}
