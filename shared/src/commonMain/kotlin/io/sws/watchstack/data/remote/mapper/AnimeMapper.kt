package io.sws.watchstack.data.remote.mapper

import io.sws.watchstack.data.remote.dto.AnimeDto
import io.sws.watchstack.data.remote.dto.CharacterNodeDto
import io.sws.watchstack.data.remote.dto.PaginationDto
import io.sws.watchstack.data.remote.dto.RecommendationNodeDto
import io.sws.watchstack.domain.model.Anime
import io.sws.watchstack.domain.model.Character
import io.sws.watchstack.domain.model.PagedAnime
import io.sws.watchstack.domain.model.RecommendedAnime

fun AnimeDto.toDomain(): Anime = Anime(
    malId = mal_id,
    title = title,
    titleJapanese = titleJapanese,
    imageUrl = images?.jpg?.large_image_url ?: images?.jpg?.image_url,
    synopsis = synopsis,
    score = score,
    episodes = episodes,
    status = status,
    airing = airing,
    rated = rating,
    genres = genres.mapNotNull { it.name },
    type = type,
    source = source,
    duration = duration,
    rating = rating,
    year = year,
    season = season,
    trailerUrl = trailer?.url ?: trailer?.embed_url,
    trailerYoutubeId = trailer?.youtube_id
)

fun RecommendationNodeDto.toDomain(): RecommendedAnime = RecommendedAnime(
    anime = entry?.toDomain() ?: Anime(malId = 0, title = ""),
    votes = votes
)

fun CharacterNodeDto.toDomain(): Character {
    val va = voice_actors.firstOrNull { it.language == "Japanese" } ?: voice_actors.firstOrNull()
    return Character(
        malId = character?.mal_id ?: 0,
        name = character?.name ?: "",
        imageUrl = character?.images?.jpg?.image_url ?: character?.images?.jpg?.large_image_url,
        role = role,
        voiceActorName = va?.person?.name,
        voiceActorImageUrl = va?.person?.images?.jpg?.image_url ?: va?.person?.images?.jpg?.large_image_url
    )
}

fun PaginationDto?.toHasNext(defaultPage: Int = 1): Boolean =
    this?.has_next_page ?: false

fun toPaged(items: List<Anime>, page: Int, pagination: PaginationDto?): PagedAnime =
    PagedAnime(
        items = items,
        page = pagination?.current_page?.takeIf { it > 0 } ?: page,
        hasNext = pagination.toHasNext()
    )
