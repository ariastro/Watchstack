package io.sws.myanimetracker.core

import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime

data class AnimeSeason(val year: Int, val season: String)

val SEASON_ORDER: List<String> = listOf("winter", "spring", "summer", "fall")

fun seasonLabel(season: String, year: Int): String =
    "${season.replaceFirstChar { it.uppercase() }} $year"

/**
 * Returns the current anime season (winter/spring/summer/fall) for the given date.
 * Seasons follow Jikan's convention: Winter (Jan-Mar), Spring (Apr-Jun),
 * Summer (Jul-Sep), Fall (Oct-Dec).
 */
fun currentSeason(now: LocalDate = Instant.fromEpochMilliseconds(currentTimeMillis()).toLocalDateTime(TimeZone.UTC).date): AnimeSeason {
    val (year, month) = now.year to now.monthNumber
    return when (month) {
        in 1..3 -> AnimeSeason(year, "winter")
        in 4..6 -> AnimeSeason(year, "spring")
        in 7..9 -> AnimeSeason(year, "summer")
        else -> AnimeSeason(year, "fall")
    }
}

/**
 * Returns a list of seasons relative to [center]: [past] seasons before and
 * [future] seasons after, in chronological order. Used to build a season picker.
 */
fun seasonWindow(center: AnimeSeason, past: Int = 4, future: Int = 8): List<AnimeSeason> {
    val centerIndex = SEASON_ORDER.indexOf(center.season).coerceAtLeast(0)
    val totalBefore = center.year * 4 + centerIndex
    val start = (totalBefore - past).coerceAtLeast(0)
    val end = totalBefore + future
    return (start..end).map { global ->
        val year = global / 4
        val season = SEASON_ORDER[global % 4]
        AnimeSeason(year, season)
    }
}
