package io.sws.watchstack.presentation

import androidx.compose.runtime.Composable
import io.sws.watchstack.domain.model.Anime
import io.sws.watchstack.domain.model.TrackedAnime
import io.sws.watchstack.domain.model.WatchStatus
import io.sws.watchstack.presentation.theme.BrutalTheme

/**
 * Sample data and helpers used exclusively by @Preview functions.
 */
object PreviewData {
    val anime = Anime(
        malId = 1,
        title = "Cowboy Bebop",
        titleJapanese = "カウボーイビバップ",
        imageUrl = null,
        synopsis = "In the year 2071, humanity has colonized several of the planets and moons of the " +
            "solar system leaving the now uninhabitable surface of planet Earth behind. A ragtag " +
            "crew of bounty hunters chases down the galaxy's most dangerous criminals.",
        score = 8.75,
        episodes = 26,
        status = "Finished Airing",
        genres = listOf("Action", "Sci-Fi", "Adventure"),
        type = "TV",
        source = "Original",
        duration = "24 min per ep",
        rating = "R - 17+",
        year = 1998,
        season = "spring"
    )

    val animeList = listOf(
        anime,
        anime.copy(malId = 2, title = "Steins;Gate", titleJapanese = "シュタインズ・ゲート", score = 9.07, episodes = 24, year = 2011),
        anime.copy(malId = 3, title = "Fullmetal Alchemist: Brotherhood", score = 9.1, episodes = 64, year = 2009),
    )

    val tracked = TrackedAnime(
        anime = anime,
        status = WatchStatus.WATCHING,
        episodesWatched = 12,
        userRating = 9,
        notes = "Peak fiction.",
        whereToWatch = "Netflix",
        dateAdded = 0L
    )

    val trackedList = listOf(
        tracked,
        TrackedAnime(anime = animeList[1], status = WatchStatus.WATCHING, episodesWatched = 5, whereToWatch = "Crunchyroll"),
        TrackedAnime(anime = animeList[2], status = WatchStatus.WATCHING, episodesWatched = 30),
    )
}

/** Wraps preview content in the app theme so themed composables render correctly. */
@Composable
fun PreviewContainer(content: @Composable () -> Unit) {
    BrutalTheme(content = content)
}
