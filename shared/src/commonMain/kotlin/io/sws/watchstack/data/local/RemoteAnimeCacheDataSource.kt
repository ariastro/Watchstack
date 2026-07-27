package io.sws.watchstack.data.local

import io.sws.watchstack.core.currentTimeMillis
import io.sws.watchstack.db.AnimeDatabase
import io.sws.watchstack.db.DetailAnimeCache
import io.sws.watchstack.db.RemoteListItem
import io.sws.watchstack.domain.model.Anime
import io.sws.watchstack.domain.model.PagedAnime
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.withContext

class RemoteAnimeCacheDataSource(
    private val database: AnimeDatabase,
    private val ttlMs: Long = DEFAULT_TTL_MS
) {
    private val queries get() = database.animeDatabaseQueries

    suspend fun getPaged(key: String, now: Long = currentTimeMillis()): PagedAnime? =
        withContext(Dispatchers.IO) {
            val meta = queries.getRemoteListMeta(key).executeAsOneOrNull() ?: return@withContext null
            if (now - meta.stored_at > ttlMs) return@withContext null
            val items = queries.getRemoteListItems(key).executeAsList().map { it.toAnime() }
            if (items.isEmpty()) return@withContext null
            PagedAnime(
                items = items,
                page = meta.page.toInt(),
                hasNext = meta.has_next == 1L
            )
        }

    suspend fun putPaged(key: String, data: PagedAnime, now: Long = currentTimeMillis()) {
        withContext(Dispatchers.IO) {
            database.transaction {
                queries.upsertRemoteListMeta(
                    cache_key = key,
                    page = data.page.toLong(),
                    has_next = if (data.hasNext) 1L else 0L,
                    stored_at = now
                )
                queries.deleteRemoteListItems(key)
                data.items.forEachIndexed { index, anime ->
                    queries.insertRemoteListItem(
                        cache_key = key,
                        position = index.toLong(),
                        mal_id = anime.malId.toLong(),
                        title = anime.title,
                        title_japanese = anime.titleJapanese,
                        image_url = anime.imageUrl,
                        synopsis = anime.synopsis,
                        score = anime.score,
                        episodes = anime.episodes?.toLong(),
                        status = anime.status,
                        airing = if (anime.airing) 1L else 0L,
                        rated = anime.rated,
                        genres = anime.genres.joinToString(","),
                        type = anime.type,
                        source = anime.source,
                        duration = anime.duration,
                        rating = anime.rating,
                        year = anime.year?.toLong(),
                        season = anime.season,
                        trailer_url = anime.trailerUrl,
                        trailer_youtube_id = anime.trailerYoutubeId
                    )
                }
            }
        }
    }

    suspend fun getAnime(malId: Int, now: Long = currentTimeMillis()): Anime? =
        withContext(Dispatchers.IO) {
            val row = queries.getDetailCache(malId.toLong()).executeAsOneOrNull() ?: return@withContext null
            if (now - row.stored_at > ttlMs) return@withContext null
            row.toAnime()
        }

    suspend fun putAnime(anime: Anime, now: Long = currentTimeMillis()) {
        withContext(Dispatchers.IO) {
            queries.upsertDetailCache(
                mal_id = anime.malId.toLong(),
                title = anime.title,
                title_japanese = anime.titleJapanese,
                image_url = anime.imageUrl,
                synopsis = anime.synopsis,
                score = anime.score,
                episodes = anime.episodes?.toLong(),
                status = anime.status,
                airing = if (anime.airing) 1L else 0L,
                rated = anime.rated,
                genres = anime.genres.joinToString(","),
                type = anime.type,
                source = anime.source,
                duration = anime.duration,
                rating = anime.rating,
                year = anime.year?.toLong(),
                season = anime.season,
                trailer_url = anime.trailerUrl,
                trailer_youtube_id = anime.trailerYoutubeId,
                stored_at = now
            )
        }
    }

    private fun RemoteListItem.toAnime(): Anime = Anime(
        malId = mal_id.toInt(),
        title = title,
        titleJapanese = title_japanese,
        imageUrl = image_url,
        synopsis = synopsis,
        score = score,
        episodes = episodes?.toInt(),
        status = status,
        airing = airing == 1L,
        rated = rated,
        genres = genres.split(",").filter { it.isNotBlank() },
        type = type,
        source = source,
        duration = duration,
        rating = rating,
        year = year?.toInt(),
        season = season,
        trailerUrl = trailer_url,
        trailerYoutubeId = trailer_youtube_id
    )

    private fun DetailAnimeCache.toAnime(): Anime = Anime(
        malId = mal_id.toInt(),
        title = title,
        titleJapanese = title_japanese,
        imageUrl = image_url,
        synopsis = synopsis,
        score = score,
        episodes = episodes?.toInt(),
        status = status,
        airing = airing == 1L,
        rated = rated,
        genres = genres.split(",").filter { it.isNotBlank() },
        type = type,
        source = source,
        duration = duration,
        rating = rating,
        year = year?.toInt(),
        season = season,
        trailerUrl = trailer_url,
        trailerYoutubeId = trailer_youtube_id
    )

    companion object {
        const val DEFAULT_TTL_MS = 24 * 60 * 60 * 1000L
    }
}
