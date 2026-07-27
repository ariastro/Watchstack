package io.sws.myanimetracker.data.local

import app.cash.sqldelight.driver.native.NativeSqliteDriver
import io.sws.myanimetracker.db.AnimeDatabase

actual object DatabaseFactory {
    actual fun create(): AnimeDatabase {
        val driver = NativeSqliteDriver(AnimeDatabase.Schema, "anime_tracker.db")
        return AnimeDatabase(driver)
    }
}
