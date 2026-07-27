package io.sws.watchstack.data.local

import android.content.Context
import app.cash.sqldelight.driver.android.AndroidSqliteDriver
import io.sws.watchstack.db.AnimeDatabase

actual object DatabaseFactory {
    fun create(context: Context): AnimeDatabase {
        val driver = AndroidSqliteDriver(AnimeDatabase.Schema, context, "anime_tracker.db")
        return AnimeDatabase(driver)
    }

    actual fun create(): AnimeDatabase = error("Use create(context) on Android")
}
