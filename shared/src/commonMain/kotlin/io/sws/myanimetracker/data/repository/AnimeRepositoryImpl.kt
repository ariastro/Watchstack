package io.sws.myanimetracker.data.repository

import io.sws.myanimetracker.core.runSafely
import io.sws.myanimetracker.data.remote.JikanApi
import io.sws.myanimetracker.data.remote.mapper.toDomain
import io.sws.myanimetracker.domain.model.Anime
import io.sws.myanimetracker.domain.model.Character
import io.sws.myanimetracker.domain.model.RecommendedAnime
import io.sws.myanimetracker.domain.repository.AnimeRepository

class AnimeRepositoryImpl(private val api: JikanApi) : AnimeRepository {
    override suspend fun searchAnime(query: String): Result<List<Anime>> =
        runSafely { api.searchAnime(query).data.map { it.toDomain() } }

    override suspend fun getAnimeDetails(malId: Int): Result<Anime> =
        runSafely { api.getAnimeDetails(malId).data.toDomain() }

    override suspend fun getTopAnime(): Result<List<Anime>> =
        runSafely { api.getTopAnime().data.map { it.toDomain() } }

    override suspend fun getAiringNow(): Result<List<Anime>> =
        runSafely { api.getAiringNow().data.map { it.toDomain() } }

    override suspend fun getUpcoming(): Result<List<Anime>> =
        runSafely { api.getUpcoming().data.map { it.toDomain() } }

    override suspend fun getSeasonalAnime(year: Int, season: String): Result<List<Anime>> =
        runSafely { api.getSeasonalAnime(year, season).data.map { it.toDomain() } }

    override suspend fun getRecommendations(malId: Int): Result<List<RecommendedAnime>> =
        runSafely { api.getRecommendations(malId).data.map { it.toDomain() } }

    override suspend fun getCharacters(malId: Int): Result<List<Character>> =
        runSafely { api.getCharacters(malId).data.map { it.toDomain() } }
}
