package io.sws.watchstack.data.remote

import io.sws.watchstack.data.remote.dto.AnimeDetailDto
import io.sws.watchstack.data.remote.dto.AnimeSearchResponse
import io.sws.watchstack.data.remote.dto.CharactersResponse
import io.sws.watchstack.data.remote.dto.RecommendationsResponse
import io.sws.watchstack.data.remote.dto.SeasonalAnimeResponse
import io.sws.watchstack.data.remote.dto.TopAnimeResponse
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.ClientRequestException
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import kotlinx.coroutines.delay

class JikanApi(private val client: HttpClient) {
    companion object {
        private const val BASE_URL = "https://api.jikan.moe/v4"
        private const val RATE_LIMIT_DELAY_MS = 400L
        private const val MAX_RETRIES = 3
    }

    private suspend inline fun <reified T> rateLimitedGet(
        url: String,
        block: io.ktor.client.request.HttpRequestBuilder.() -> Unit = {}
    ): T {
        repeat(MAX_RETRIES) { attempt ->
            try {
                delay(RATE_LIMIT_DELAY_MS)
                return client.get(url, block).body()
            } catch (e: ClientRequestException) {
                if (e.response.status.value == 429 && attempt < MAX_RETRIES - 1) {
                    delay(1000L * (attempt + 1))
                } else {
                    throw e
                }
            }
        }
        error("Max retries exceeded")
    }

    suspend fun searchAnime(query: String, page: Int = 1): AnimeSearchResponse =
        rateLimitedGet("$BASE_URL/anime") {
            parameter("q", query)
            parameter("page", page)
            parameter("limit", 25)
        }

    suspend fun getAnimeDetails(malId: Int): AnimeDetailDto =
        rateLimitedGet("$BASE_URL/anime/$malId/full")

    suspend fun getTopAnime(page: Int = 1): TopAnimeResponse =
        rateLimitedGet("$BASE_URL/top/anime") {
            parameter("page", page)
            parameter("limit", 25)
        }

    suspend fun getSeasonalAnime(year: Int, season: String, page: Int = 1): SeasonalAnimeResponse =
        rateLimitedGet("$BASE_URL/seasons/$year/$season") {
            parameter("page", page)
            parameter("limit", 25)
        }

    suspend fun getAiringNow(page: Int = 1): SeasonalAnimeResponse =
        rateLimitedGet("$BASE_URL/seasons/now") {
            parameter("page", page)
            parameter("limit", 25)
        }

    suspend fun getUpcoming(page: Int = 1): SeasonalAnimeResponse =
        rateLimitedGet("$BASE_URL/seasons/upcoming") {
            parameter("page", page)
            parameter("limit", 25)
        }

    suspend fun getRecommendations(malId: Int): RecommendationsResponse =
        rateLimitedGet("$BASE_URL/anime/$malId/recommendations")

    suspend fun getCharacters(malId: Int): CharactersResponse =
        rateLimitedGet("$BASE_URL/anime/$malId/characters")
}
