package io.sws.watchstack.domain.repository

import io.sws.watchstack.domain.model.TrackedAnime
import io.sws.watchstack.domain.model.WatchStatus
import kotlinx.coroutines.flow.Flow

interface TrackedAnimeRepository {
    fun observeAll(): Flow<List<TrackedAnime>>
    fun observeByStatus(status: WatchStatus): Flow<List<TrackedAnime>>
    suspend fun trackAnime(tracked: TrackedAnime): Result<Unit>
    suspend fun updateTracked(tracked: TrackedAnime): Result<Unit>
    suspend fun removeTracked(malId: Int): Result<Unit>
    suspend fun getTracked(malId: Int): TrackedAnime?
}