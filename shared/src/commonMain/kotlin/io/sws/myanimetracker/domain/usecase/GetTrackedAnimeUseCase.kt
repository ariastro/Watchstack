package io.sws.myanimetracker.domain.usecase

import io.sws.myanimetracker.domain.model.TrackedAnime
import io.sws.myanimetracker.domain.model.WatchStatus
import io.sws.myanimetracker.domain.repository.TrackedAnimeRepository
import kotlinx.coroutines.flow.Flow

class GetTrackedAnimeUseCase(private val repository: TrackedAnimeRepository) {
    operator fun invoke(status: WatchStatus): Flow<List<TrackedAnime>> = repository.observeByStatus(status)
    operator fun invoke(): Flow<List<TrackedAnime>> = repository.observeAll()
}
