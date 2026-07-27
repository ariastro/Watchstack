package io.sws.watchstack.data.local.mapper

import io.sws.watchstack.db.TrackedAnimeEntity
import io.sws.watchstack.domain.model.Anime
import io.sws.watchstack.domain.model.TrackedAnime
import io.sws.watchstack.domain.model.WatchStatus

fun TrackedAnimeEntity.toDomain(): TrackedAnime = TrackedAnime(
    anime = Anime(
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
        source = source,
        duration = duration,
        rating = rating,
        year = year?.toInt(),
        season = season
    ),
    status = WatchStatus.valueOf(watch_status),
    episodesWatched = episodes_watched.toInt(),
    userRating = user_rating?.toInt(),
    notes = notes,
    whereToWatch = where_to_watch,
    dateAdded = date_added,
    dateCompleted = date_completed
)

fun TrackedAnime.toEntity(): TrackedAnimeEntity = TrackedAnimeEntity(
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
    source = anime.source,
    duration = anime.duration,
    rating = anime.rating,
    year = anime.year?.toLong(),
    season = anime.season,
    watch_status = status.name,
    episodes_watched = episodesWatched.toLong(),
    user_rating = userRating?.toLong(),
    notes = notes,
    where_to_watch = whereToWatch,
    date_added = dateAdded,
    date_completed = dateCompleted
)
