package io.sws.watchstack.data.repository

import io.sws.watchstack.core.runSafely
import io.sws.watchstack.data.local.MemoryAnimeCache
import io.sws.watchstack.data.local.RemoteAnimeCacheDataSource
import io.sws.watchstack.data.remote.JikanApi
import io.sws.watchstack.data.remote.dto.PaginationDto
import io.sws.watchstack.data.remote.mapper.toDomain
import io.sws.watchstack.data.remote.mapper.toPaged
import io.sws.watchstack.domain.model.Anime
import io.sws.watchstack.domain.model.Character
import io.sws.watchstack.domain.model.PagedAnime
import io.sws.watchstack.domain.model.RecommendedAnime
import io.sws.watchstack.domain.repository.AnimeRepository

class AnimeRepositoryImpl(
    private val api: JikanApi,
    private val memoryCache: MemoryAnimeCache,
    private val diskCache: RemoteAnimeCacheDataSource
) : AnimeRepository {

    override suspend fun searchAnime(query: String, page: Int): Result<PagedAnime> {
        val key = "search:$query:$page"
        return fetchPaged(key, page) {
            val response = api.searchAnime(query, page)
            response.data.map { it.toDomain() } to response.pagination
        }
    }

    override suspend fun getAnimeDetails(malId: Int): Result<Anime> =
        runSafely {
            val anime = api.getAnimeDetails(malId).data.toDomain()
            memoryCache.putAnime(anime)
            diskCache.putAnime(anime)
            anime
        }.recoverCatching {
            memoryCache.getAnime(malId)
                ?: diskCache.getAnime(malId)
                ?: throw it
        }

    override suspend fun getTopAnime(page: Int): Result<PagedAnime> {
        val key = "top:$page"
        return fetchPaged(key, page) {
            val response = api.getTopAnime(page)
            response.data.map { it.toDomain() } to response.pagination
        }
    }

    override suspend fun getAiringNow(page: Int): Result<PagedAnime> {
        val key = "airing:$page"
        return fetchPaged(key, page) {
            val response = api.getAiringNow(page)
            response.data.map { it.toDomain() } to response.pagination
        }
    }

    override suspend fun getUpcoming(page: Int): Result<PagedAnime> {
        val key = "upcoming:$page"
        return fetchPaged(key, page) {
            val response = api.getUpcoming(page)
            response.data.map { it.toDomain() } to response.pagination
        }
    }

    override suspend fun getSeasonalAnime(year: Int, season: String, page: Int): Result<PagedAnime> {
        val key = "season:$year:$season:$page"
        return fetchPaged(key, page) {
            val response = api.getSeasonalAnime(year, season, page)
            response.data.map { it.toDomain() } to response.pagination
        }
    }

    override suspend fun getRecommendations(malId: Int): Result<List<RecommendedAnime>> =
        runSafely { api.getRecommendations(malId).data.map { it.toDomain() } }

    override suspend fun getCharacters(malId: Int): Result<List<Character>> =
        runSafely { api.getCharacters(malId).data.map { it.toDomain() } }

    private suspend fun fetchPaged(
        key: String,
        page: Int,
        block: suspend () -> Pair<List<Anime>, PaginationDto?>
    ): Result<PagedAnime> =
        runSafely {
            val (items, pagination) = block()
            val paged = toPaged(items, page, pagination)
            memoryCache.put(key, paged)
            diskCache.putPaged(key, paged)
            paged
        }.recoverCatching {
            memoryCache.get(key)
                ?: diskCache.getPaged(key)
                ?: throw it
        }
}
