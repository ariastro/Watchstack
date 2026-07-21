package io.sws.myanimetracker.domain.usecase

import io.sws.myanimetracker.domain.model.PagedAnime
import io.sws.myanimetracker.domain.repository.AnimeRepository

class GetUpcomingUseCase(private val repository: AnimeRepository) {
    suspend operator fun invoke(page: Int = 1): Result<PagedAnime> = repository.getUpcoming(page)
}
