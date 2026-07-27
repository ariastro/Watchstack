package io.sws.watchstack.data.local

expect object DatabaseFactory {
    fun create(): io.sws.watchstack.db.AnimeDatabase
}
