package io.sws.myanimetracker.db

import app.cash.sqldelight.Transacter
import app.cash.sqldelight.db.QueryResult
import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.db.SqlSchema
import io.sws.myanimetracker.db.shared.newInstance
import io.sws.myanimetracker.db.shared.schema
import kotlin.Unit

public interface AnimeDatabase : Transacter {
  public val animeDatabaseQueries: AnimeDatabaseQueries

  public companion object {
    public val Schema: SqlSchema<QueryResult.Value<Unit>>
      get() = AnimeDatabase::class.schema

    public operator fun invoke(driver: SqlDriver): AnimeDatabase = AnimeDatabase::class.newInstance(driver)
  }
}
