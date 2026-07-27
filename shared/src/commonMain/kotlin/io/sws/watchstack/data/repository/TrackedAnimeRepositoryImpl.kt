package io.sws.watchstack.data.repository

import io.sws.watchstack.core.runSafely
import io.sws.watchstack.data.local.TrackedAnimeLocalDataSource
import io.sws.watchstack.data.local.mapper.toDomain
import io.sws.watchstack.data.local.mapper.toEntity
import io.sws.watchstack.domain.model.TrackedAnime
import io.sws.watchstack.domain.model.WatchStatus
import io.sws.watchstack.domain.repository.TrackedAnimeRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class TrackedAnimeRepositoryImpl(
    private val localDataSource: TrackedAnimeLocalDataSource
) : TrackedAnimeRepository {

    override fun observeAll(): Flow<List<TrackedAnime>> =
        localDataSource.observeAll().map { it.map { e -> e.toDomain() } }

    override fun observeByStatus(status: WatchStatus): Flow<List<TrackedAnime>> =
        localDataSource.observeByStatus(status).map { it.map { e -> e.toDomain() } }

    override suspend fun trackAnime(tracked: TrackedAnime): Result<Unit> =
        runSafely { localDataSource.upsert(tracked.toEntity()) }

    override suspend fun updateTracked(tracked: TrackedAnime): Result<Unit> =
        runSafely { localDataSource.upsert(tracked.toEntity()) }

    override suspend fun removeTracked(malId: Int): Result<Unit> =
        runSafely { localDataSource.deleteByMalId(malId) }

    override suspend fun getTracked(malId: Int): TrackedAnime? =
        localDataSource.getByMalId(malId)?.toDomain()
}
