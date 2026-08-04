package io.sws.watchstack.domain.repository

import io.sws.watchstack.domain.model.Anime
import io.sws.watchstack.domain.model.Character
import io.sws.watchstack.domain.model.PagedAnime
import io.sws.watchstack.domain.model.RecommendedAnime

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