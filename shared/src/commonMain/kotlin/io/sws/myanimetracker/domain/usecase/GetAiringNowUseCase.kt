package io.sws.myanimetracker.domain.usecase

import io.sws.myanimetracker.domain.model.Anime
import io.sws.myanimetracker.domain.repository.AnimeRepository

class GetAiringNowUseCase(private val repository: AnimeRepository) {
    suspend operator fun invoke(): Result<List<Anime>> = repository.getAiringNow()
}
