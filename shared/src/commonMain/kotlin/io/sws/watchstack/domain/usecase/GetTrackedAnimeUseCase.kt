package io.sws.watchstack.domain.usecase

import io.sws.watchstack.domain.model.TrackedAnime
import io.sws.watchstack.domain.model.WatchStatus
import io.sws.watchstack.domain.repository.TrackedAnimeRepository
import kotlinx.coroutines.flow.Flow

class GetTrackedAnimeUseCase(private val repository: TrackedAnimeRepository) {
    operator fun invoke(status: WatchStatus): Flow<List<TrackedAnime>> = repository.observeByStatus(status)
    operator fun invoke(): Flow<List<TrackedAnime>> = repository.observeAll()
}
