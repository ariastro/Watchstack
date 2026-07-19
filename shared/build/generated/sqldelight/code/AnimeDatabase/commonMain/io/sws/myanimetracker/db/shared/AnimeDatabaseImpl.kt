package io.sws.myanimetracker.db.shared

import app.cash.sqldelight.TransacterImpl
import app.cash.sqldelight.db.AfterVersion
import app.cash.sqldelight.db.QueryResult
import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.db.SqlSchema
import io.sws.myanimetracker.db.AnimeDatabase
import io.sws.myanimetracker.db.AnimeDatabaseQueries
import kotlin.Long
import kotlin.Unit
import kotlin.reflect.KClass

internal val KClass<AnimeDatabase>.schema: SqlSchema<QueryResult.Value<Unit>>
  get() = AnimeDatabaseImpl.Schema

internal fun KClass<AnimeDatabase>.newInstance(driver: SqlDriver): AnimeDatabase = AnimeDatabaseImpl(driver)

private class AnimeDatabaseImpl(
  driver: SqlDriver,
) : TransacterImpl(driver),
    AnimeDatabase {
  override val animeDatabaseQueries: AnimeDatabaseQueries = AnimeDatabaseQueries(driver)

  public object Schema : SqlSchema<QueryResult.Value<Unit>> {
    override val version: Long
      get() = 1

    override fun create(driver: SqlDriver): QueryResult.Value<Unit> {
      driver.execute(null, """
          |CREATE TABLE TrackedAnimeEntity (
          |    mal_id INTEGER NOT NULL PRIMARY KEY,
          |    title TEXT NOT NULL,
          |    title_japanese TEXT,
          |    image_url TEXT,
          |    synopsis TEXT,
          |    score REAL,
          |    episodes INTEGER,
          |    status TEXT,
          |    airing INTEGER NOT NULL DEFAULT 0,
          |    rated TEXT,
          |    genres TEXT NOT NULL DEFAULT '',
          |    source TEXT,
          |    duration TEXT,
          |    rating TEXT,
          |    year INTEGER,
          |    season TEXT,
          |    watch_status TEXT NOT NULL,
          |    episodes_watched INTEGER NOT NULL DEFAULT 0,
          |    user_rating INTEGER,
          |    notes TEXT NOT NULL DEFAULT '',
          |    where_to_watch TEXT NOT NULL DEFAULT '',
          |    date_added INTEGER NOT NULL DEFAULT 0,
          |    date_completed INTEGER
          |)
          """.trimMargin(), 0)
      return QueryResult.Unit
    }

    override fun migrate(
      driver: SqlDriver,
      oldVersion: Long,
      newVersion: Long,
      vararg callbacks: AfterVersion,
    ): QueryResult.Value<Unit> = QueryResult.Unit
  }
}
