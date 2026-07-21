package io.sws.myanimetracker.domain.repository

import io.sws.myanimetracker.domain.model.Anime
import io.sws.myanimetracker.domain.model.Character
import io.sws.myanimetracker.domain.model.PagedAnime
import io.sws.myanimetracker.domain.model.RecommendedAnime
import io.sws.myanimetracker.domain.model.TrackedAnime
import io.sws.myanimetracker.domain.model.WatchStatus
import kotlinx.coroutines.flow.Flow

interface AnimeRepository {
    suspend fun searchAnime(query: String, page: Int = 1): Result<PagedAnime>
    suspend fun getAnimeDetails(malId: Int): Result<Anime>
    suspend fun getTopAnime(page: Int = 1): Result<PagedAnime>
    suspend fun getAiringNow(page: Int = 1): Result<PagedAnime>
    suspend fun getUpcoming(page: Int = 1): Result<PagedAnime>
    suspend fun getSeasonalAnime(year: Int, season: String, page: Int = 1): Result<PagedAnime>
    suspend fun getRecommendations(malId: Int): Result<List<RecommendedAnime>>
    suspend fun getCharacters(malId: Int): Result<List<Character>>
}

interface TrackedAnimeRepository {
    fun observeAll(): Flow<List<TrackedAnime>>
    fun observeByStatus(status: WatchStatus): Flow<List<TrackedAnime>>
    suspend fun trackAnime(tracked: TrackedAnime): Result<Unit>
    suspend fun updateTracked(tracked: TrackedAnime): Result<Unit>
    suspend fun removeTracked(malId: Int): Result<Unit>
    suspend fun getTracked(malId: Int): TrackedAnime?
}

interface SearchHistoryRepository {
    fun observeRecent(): Flow<List<String>>
    suspend fun addQuery(query: String)
    suspend fun clear()
}
