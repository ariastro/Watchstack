package io.sws.myanimetracker.data.remote.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class AnimeSearchResponse(
    val pagination: PaginationDto? = null,
    val data: List<AnimeDto> = emptyList()
)

@Serializable
data class TopAnimeResponse(
    val pagination: PaginationDto? = null,
    val data: List<AnimeDto> = emptyList()
)

@Serializable
data class SeasonalAnimeResponse(
    val pagination: PaginationDto? = null,
    val data: List<AnimeDto> = emptyList()
)

@Serializable
data class PaginationDto(
    val last_visible_page: Int = 0,
    val has_next_page: Boolean = false,
    val current_page: Int = 0,
    val items: PaginationItemsDto? = null
)

@Serializable
data class PaginationItemsDto(
    val count: Int = 0,
    val total: Int = 0,
    val per_page: Int = 0
)

@Serializable
data class AnimeDto(
    val mal_id: Int,
    val url: String? = null,
    val images: ImagesDto? = null,
    val trailer: TrailerDto? = null,
    val approved: Boolean = true,
    val titles: List<TitleDto> = emptyList(),
    val title: String = "",
    @SerialName("title_japanese") val titleJapanese: String? = null,
    @SerialName("title_english") val titleEnglish: String? = null,
    val episodes: Int? = null,
    val status: String? = null,
    val airing: Boolean = false,
    val aired: AiredDto? = null,
    val rating: String? = null,
    val score: Double? = null,
    val scored_by: Double? = null,
    val rank: Int? = null,
    val popularity: Int? = null,
    val members: Int? = null,
    val favorites: Int? = null,
    val synopsis: String? = null,
    val background: String? = null,
    val season: String? = null,
    val year: Int? = null,
    val broadcast: BroadcastDto? = null,
    val source: String? = null,
    val duration: String? = null,
    @SerialName("rating_content") val rating_content: String? = null,
    val genres: List<GenreDto> = emptyList(),
    val themes: List<GenreDto> = emptyList(),
    val demographics: List<GenreDto> = emptyList()
)

@Serializable
data class ImagesDto(val jpg: ImageSizeDto? = null, val webp: ImageSizeDto? = null)

@Serializable
data class ImageSizeDto(
    val image_url: String? = null,
    val small_image_url: String? = null,
    val large_image_url: String? = null
)

@Serializable
data class TrailerDto(
    val youtube_id: String? = null,
    val url: String? = null,
    val embed_url: String? = null,
    val images: TrailerImagesDto? = null
)

@Serializable
data class TrailerImagesDto(
    val medium_image_url: String? = null,
    val large_image_url: String? = null,
    val maximum_image_url: String? = null
)

@Serializable
data class TitleDto(val type: String? = null, val title: String? = null)

@Serializable
data class AiredDto(
    val from: String? = null,
    val to: String? = null,
    val prop: AiredPropDto? = null,
    val string: String? = null
)

@Serializable data class AiredPropDto(val from: DayDto? = null, val to: DayDto? = null)
@Serializable data class DayDto(val day: Int? = null, val month: Int? = null, val year: Int? = null)

@Serializable
data class BroadcastDto(
    val day: String? = null,
    val time: String? = null,
    val timezone: String? = null,
    val string: String? = null
)

@Serializable
data class GenreDto(val mal_id: Int, val type: String? = null, val name: String? = null, val url: String? = null)

@Serializable
data class AnimeDetailDto(val data: AnimeDto)

@Serializable
data class RecommendationsResponse(
    val pagination: PaginationDto? = null,
    val data: List<RecommendationNodeDto> = emptyList()
)

@Serializable
data class RecommendationNodeDto(
    val entry: AnimeDto? = null,
    val url: String? = null,
    val votes: Int = 0
)

@Serializable
data class CharactersResponse(
    val data: List<CharacterNodeDto> = emptyList()
)

@Serializable
data class CharacterNodeDto(
    val character: CharacterDto? = null,
    val role: String? = null,
    val voice_actors: List<VoiceActorDto> = emptyList()
)

@Serializable
data class CharacterDto(
    val mal_id: Int = 0,
    val url: String? = null,
    val images: ImagesDto? = null,
    val name: String = ""
)

@Serializable
data class VoiceActorDto(
    val person: PersonDto? = null,
    val language: String? = null
)

@Serializable
data class PersonDto(
    val mal_id: Int = 0,
    val url: String? = null,
    val images: ImagesDto? = null,
    val name: String = ""
)
