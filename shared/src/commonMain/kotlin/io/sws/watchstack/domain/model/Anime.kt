package io.sws.watchstack.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class Anime(
    val malId: Int,
    val title: String,
    val titleJapanese: String? = null,
    val imageUrl: String? = null,
    val synopsis: String? = null,
    val score: Double? = null,
    val episodes: Int? = null,
    val status: String? = null,
    val airing: Boolean = false,
    val rated: String? = null,
    val genres: List<String> = emptyList(),
    val type: String? = null,
    val source: String? = null,
    val duration: String? = null,
    val rating: String? = null,
    val year: Int? = null,
    val season: String? = null,
    val trailerUrl: String? = null,
    val trailerYoutubeId: String? = null
)

@Serializable
enum class WatchStatus {
    WATCHLIST,
    WATCHING,
    WATCHED
}

@Serializable
data class TrackedAnime(
    val anime: Anime,
    val status: WatchStatus,
    val episodesWatched: Int = 0,
    val userRating: Int? = null,
    val notes: String = "",
    val whereToWatch: String = "",
    val dateAdded: Long = 0L,
    val dateCompleted: Long? = null
)

@Serializable
data class Character(
    val malId: Int,
    val name: String,
    val imageUrl: String? = null,
    val role: String? = null,
    val voiceActorName: String? = null,
    val voiceActorImageUrl: String? = null
)

@Serializable
data class RecommendedAnime(
    val anime: Anime,
    val votes: Int = 0
)

data class PagedAnime(
    val items: List<Anime>,
    val page: Int,
    val hasNext: Boolean
)

data class LibraryStats(
    val total: Int,
    val watchlist: Int,
    val watching: Int,
    val watched: Int,
    val episodesWatched: Int,
    val averageRating: Double?,
    val topGenres: List<Pair<String, Int>>
)
