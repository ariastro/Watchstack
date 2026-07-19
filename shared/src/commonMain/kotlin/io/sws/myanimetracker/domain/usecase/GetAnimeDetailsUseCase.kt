package io.sws.myanimetracker.domain.usecase

import io.sws.myanimetracker.domain.model.Anime
import io.sws.myanimetracker.domain.repository.AnimeRepository

class GetAnimeDetailsUseCase(private val repository: AnimeRepository) {
    suspend operator fun invoke(malId: Int): Result<Anime> = repository.getAnimeDetails(malId)
}
