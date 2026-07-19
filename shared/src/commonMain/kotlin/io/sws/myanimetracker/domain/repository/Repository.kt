package io.sws.myanimetracker.domain.repository

import io.sws.myanimetracker.domain.model.Anime
import io.sws.myanimetracker.domain.model.Character
import io.sws.myanimetracker.domain.model.RecommendedAnime
import io.sws.myanimetracker.domain.model.TrackedAnime
import io.sws.myanimetracker.domain.model.WatchStatus
import kotlinx.coroutines.flow.Flow

interface AnimeRepository {
    suspend fun searchAnime(query: String): Result<List<Anime>>
    suspend fun getAnimeDetails(malId: Int): Result<Anime>
    suspend fun getTopAnime(): Result<List<Anime>>
    suspend fun getAiringNow(): Result<List<Anime>>
    suspend fun getUpcoming(): Result<List<Anime>>
    suspend fun getSeasonalAnime(year: Int, season: String): Result<List<Anime>>
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
