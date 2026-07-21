package io.sws.myanimetracker.domain.usecase

import io.sws.myanimetracker.domain.model.LibraryStats
import io.sws.myanimetracker.domain.model.WatchStatus
import io.sws.myanimetracker.domain.repository.TrackedAnimeRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class GetLibraryStatsUseCase(private val repository: TrackedAnimeRepository) {
    operator fun invoke(): Flow<LibraryStats> = repository.observeAll().map { list ->
        val watchlist = list.count { it.status == WatchStatus.WATCHLIST }
        val watching = list.count { it.status == WatchStatus.WATCHING }
        val watched = list.count { it.status == WatchStatus.WATCHED }
        val episodes = list.sumOf { it.episodesWatched }
        val ratings = list.mapNotNull { it.userRating }
        val avg = if (ratings.isEmpty()) null else ratings.average()
        val genres = list
            .flatMap { it.anime.genres }
            .groupingBy { it }
            .eachCount()
            .entries
            .sortedByDescending { it.value }
            .take(5)
            .map { it.key to it.value }
        LibraryStats(
            total = list.size,
            watchlist = watchlist,
            watching = watching,
            watched = watched,
            episodesWatched = episodes,
            averageRating = avg,
            topGenres = genres
        )
    }
}
