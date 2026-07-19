package io.sws.myanimetracker.data.local

import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToList
import io.sws.myanimetracker.db.AnimeDatabase
import io.sws.myanimetracker.db.TrackedAnimeEntity
import io.sws.myanimetracker.domain.model.WatchStatus
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext

class TrackedAnimeLocalDataSource(private val database: AnimeDatabase) {
    private val queries get() = database.animeDatabaseQueries

    fun observeAll(): Flow<List<TrackedAnimeEntity>> =
        queries.getAll().asFlow().mapToList(Dispatchers.Default)

    fun observeByStatus(status: WatchStatus): Flow<List<TrackedAnimeEntity>> =
        queries.getByStatus(status.name).asFlow().mapToList(Dispatchers.Default)

    suspend fun getByMalId(malId: Int): TrackedAnimeEntity? =
        withContext(Dispatchers.IO) { queries.getByMalId(malId.toLong()).executeAsOneOrNull() }

    suspend fun upsert(entity: TrackedAnimeEntity) {
        withContext(Dispatchers.IO) {
            queries.upsert(
                mal_id = entity.mal_id,
                title = entity.title,
                title_japanese = entity.title_japanese,
                image_url = entity.image_url,
                synopsis = entity.synopsis,
                score = entity.score,
                episodes = entity.episodes,
                status = entity.status,
                airing = entity.airing,
                rated = entity.rated,
                genres = entity.genres,
                source = entity.source,
                duration = entity.duration,
                rating = entity.rating,
                year = entity.year,
                season = entity.season,
                watch_status = entity.watch_status,
                episodes_watched = entity.episodes_watched,
                user_rating = entity.user_rating,
                notes = entity.notes,
                where_to_watch = entity.where_to_watch,
                date_added = entity.date_added,
                date_completed = entity.date_completed
            )
        }
    }

    suspend fun deleteByMalId(malId: Int) {
        withContext(Dispatchers.IO) { queries.deleteByMalId(malId.toLong()) }
    }
}
