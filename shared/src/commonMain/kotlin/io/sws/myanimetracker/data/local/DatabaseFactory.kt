package io.sws.myanimetracker.data.local

expect object DatabaseFactory {
    fun create(): io.sws.myanimetracker.db.AnimeDatabase
}
