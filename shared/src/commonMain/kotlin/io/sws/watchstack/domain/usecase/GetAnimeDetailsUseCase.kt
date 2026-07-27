package io.sws.watchstack.domain.usecase

import io.sws.watchstack.domain.model.Anime
import io.sws.watchstack.domain.repository.AnimeRepository

class GetAnimeDetailsUseCase(private val repository: AnimeRepository) {
    suspend operator fun invoke(malId: Int): Result<Anime> = repository.getAnimeDetails(malId)
}
