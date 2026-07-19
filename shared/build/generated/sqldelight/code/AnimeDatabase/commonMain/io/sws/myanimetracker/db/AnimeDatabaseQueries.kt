package io.sws.myanimetracker.db

import app.cash.sqldelight.Query
import app.cash.sqldelight.TransacterImpl
import app.cash.sqldelight.db.QueryResult
import app.cash.sqldelight.db.SqlCursor
import app.cash.sqldelight.db.SqlDriver
import kotlin.Any
import kotlin.Double
import kotlin.Long
import kotlin.String

public class AnimeDatabaseQueries(
  driver: SqlDriver,
) : TransacterImpl(driver) {
  public fun <T : Any> getAll(mapper: (
    mal_id: Long,
    title: String,
    title_japanese: String?,
    image_url: String?,
    synopsis: String?,
    score: Double?,
    episodes: Long?,
    status: String?,
    airing: Long,
    rated: String?,
    genres: String,
    source: String?,
    duration: String?,
    rating: String?,
    year: Long?,
    season: String?,
    watch_status: String,
    episodes_watched: Long,
    user_rating: Long?,
    notes: String,
    where_to_watch: String,
    date_added: Long,
    date_completed: Long?,
  ) -> T): Query<T> = Query(762_381_347, arrayOf("TrackedAnimeEntity"), driver, "AnimeDatabase.sq", "getAll", """
  |SELECT TrackedAnimeEntity.mal_id, TrackedAnimeEntity.title, TrackedAnimeEntity.title_japanese, TrackedAnimeEntity.image_url, TrackedAnimeEntity.synopsis, TrackedAnimeEntity.score, TrackedAnimeEntity.episodes, TrackedAnimeEntity.status, TrackedAnimeEntity.airing, TrackedAnimeEntity.rated, TrackedAnimeEntity.genres, TrackedAnimeEntity.source, TrackedAnimeEntity.duration, TrackedAnimeEntity.rating, TrackedAnimeEntity.year, TrackedAnimeEntity.season, TrackedAnimeEntity.watch_status, TrackedAnimeEntity.episodes_watched, TrackedAnimeEntity.user_rating, TrackedAnimeEntity.notes, TrackedAnimeEntity.where_to_watch, TrackedAnimeEntity.date_added, TrackedAnimeEntity.date_completed FROM TrackedAnimeEntity
  |ORDER BY date_added DESC
  """.trimMargin()) { cursor ->
    mapper(
      cursor.getLong(0)!!,
      cursor.getString(1)!!,
      cursor.getString(2),
      cursor.getString(3),
      cursor.getString(4),
      cursor.getDouble(5),
      cursor.getLong(6),
      cursor.getString(7),
      cursor.getLong(8)!!,
      cursor.getString(9),
      cursor.getString(10)!!,
      cursor.getString(11),
      cursor.getString(12),
      cursor.getString(13),
      cursor.getLong(14),
      cursor.getString(15),
      cursor.getString(16)!!,
      cursor.getLong(17)!!,
      cursor.getLong(18),
      cursor.getString(19)!!,
      cursor.getString(20)!!,
      cursor.getLong(21)!!,
      cursor.getLong(22)
    )
  }

  public fun getAll(): Query<TrackedAnimeEntity> = getAll(::TrackedAnimeEntity)

  public fun <T : Any> getByStatus(watch_status: String, mapper: (
    mal_id: Long,
    title: String,
    title_japanese: String?,
    image_url: String?,
    synopsis: String?,
    score: Double?,
    episodes: Long?,
    status: String?,
    airing: Long,
    rated: String?,
    genres: String,
    source: String?,
    duration: String?,
    rating: String?,
    year: Long?,
    season: String?,
    watch_status: String,
    episodes_watched: Long,
    user_rating: Long?,
    notes: String,
    where_to_watch: String,
    date_added: Long,
    date_completed: Long?,
  ) -> T): Query<T> = GetByStatusQuery(watch_status) { cursor ->
    mapper(
      cursor.getLong(0)!!,
      cursor.getString(1)!!,
      cursor.getString(2),
      cursor.getString(3),
      cursor.getString(4),
      cursor.getDouble(5),
      cursor.getLong(6),
      cursor.getString(7),
      cursor.getLong(8)!!,
      cursor.getString(9),
      cursor.getString(10)!!,
      cursor.getString(11),
      cursor.getString(12),
      cursor.getString(13),
      cursor.getLong(14),
      cursor.getString(15),
      cursor.getString(16)!!,
      cursor.getLong(17)!!,
      cursor.getLong(18),
      cursor.getString(19)!!,
      cursor.getString(20)!!,
      cursor.getLong(21)!!,
      cursor.getLong(22)
    )
  }

  public fun getByStatus(watch_status: String): Query<TrackedAnimeEntity> = getByStatus(watch_status, ::TrackedAnimeEntity)

  public fun <T : Any> getByMalId(mal_id: Long, mapper: (
    mal_id: Long,
    title: String,
    title_japanese: String?,
    image_url: String?,
    synopsis: String?,
    score: Double?,
    episodes: Long?,
    status: String?,
    airing: Long,
    rated: String?,
    genres: String,
    source: String?,
    duration: String?,
    rating: String?,
    year: Long?,
    season: String?,
    watch_status: String,
    episodes_watched: Long,
    user_rating: Long?,
    notes: String,
    where_to_watch: String,
    date_added: Long,
    date_completed: Long?,
  ) -> T): Query<T> = GetByMalIdQuery(mal_id) { cursor ->
    mapper(
      cursor.getLong(0)!!,
      cursor.getString(1)!!,
      cursor.getString(2),
      cursor.getString(3),
      cursor.getString(4),
      cursor.getDouble(5),
      cursor.getLong(6),
      cursor.getString(7),
      cursor.getLong(8)!!,
      cursor.getString(9),
      cursor.getString(10)!!,
      cursor.getString(11),
      cursor.getString(12),
      cursor.getString(13),
      cursor.getLong(14),
      cursor.getString(15),
      cursor.getString(16)!!,
      cursor.getLong(17)!!,
      cursor.getLong(18),
      cursor.getString(19)!!,
      cursor.getString(20)!!,
      cursor.getLong(21)!!,
      cursor.getLong(22)
    )
  }

  public fun getByMalId(mal_id: Long): Query<TrackedAnimeEntity> = getByMalId(mal_id, ::TrackedAnimeEntity)

  /**
   * @return The number of rows updated.
   */
  public fun upsert(
    mal_id: Long?,
    title: String,
    title_japanese: String?,
    image_url: String?,
    synopsis: String?,
    score: Double?,
    episodes: Long?,
    status: String?,
    airing: Long,
    rated: String?,
    genres: String,
    source: String?,
    duration: String?,
    rating: String?,
    year: Long?,
    season: String?,
    watch_status: String,
    episodes_watched: Long,
    user_rating: Long?,
    notes: String,
    where_to_watch: String,
    date_added: Long,
    date_completed: Long?,
  ): QueryResult<Long> {
    val result = driver.execute(1_173_353_191, """
        |INSERT OR REPLACE INTO TrackedAnimeEntity (
        |    mal_id, title, title_japanese, image_url, synopsis, score, episodes, status,
        |    airing, rated, genres, source, duration, rating, year, season,
        |    watch_status, episodes_watched, user_rating, notes, where_to_watch,
        |    date_added, date_completed
        |) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)
        """.trimMargin(), 23) {
          var parameterIndex = 0
          bindLong(parameterIndex++, mal_id)
          bindString(parameterIndex++, title)
          bindString(parameterIndex++, title_japanese)
          bindString(parameterIndex++, image_url)
          bindString(parameterIndex++, synopsis)
          bindDouble(parameterIndex++, score)
          bindLong(parameterIndex++, episodes)
          bindString(parameterIndex++, status)
          bindLong(parameterIndex++, airing)
          bindString(parameterIndex++, rated)
          bindString(parameterIndex++, genres)
          bindString(parameterIndex++, source)
          bindString(parameterIndex++, duration)
          bindString(parameterIndex++, rating)
          bindLong(parameterIndex++, year)
          bindString(parameterIndex++, season)
          bindString(parameterIndex++, watch_status)
          bindLong(parameterIndex++, episodes_watched)
          bindLong(parameterIndex++, user_rating)
          bindString(parameterIndex++, notes)
          bindString(parameterIndex++, where_to_watch)
          bindLong(parameterIndex++, date_added)
          bindLong(parameterIndex++, date_completed)
        }
    notifyQueries(1_173_353_191) { emit ->
      emit("TrackedAnimeEntity")
    }
    return result
  }

  /**
   * @return The number of rows updated.
   */
  public fun deleteByMalId(mal_id: Long): QueryResult<Long> {
    val result = driver.execute(-489_910_887, """
        |DELETE FROM TrackedAnimeEntity
        |WHERE mal_id = ?
        """.trimMargin(), 1) {
          var parameterIndex = 0
          bindLong(parameterIndex++, mal_id)
        }
    notifyQueries(-489_910_887) { emit ->
      emit("TrackedAnimeEntity")
    }
    return result
  }

  private inner class GetByStatusQuery<out T : Any>(
    public val watch_status: String,
    mapper: (SqlCursor) -> T,
  ) : Query<T>(mapper) {
    override fun addListener(listener: Query.Listener) {
      driver.addListener("TrackedAnimeEntity", listener = listener)
    }

    override fun removeListener(listener: Query.Listener) {
      driver.removeListener("TrackedAnimeEntity", listener = listener)
    }

    override fun <R> execute(mapper: (SqlCursor) -> QueryResult<R>): QueryResult<R> = driver.executeQuery(-1_815_830_041, """
    |SELECT TrackedAnimeEntity.mal_id, TrackedAnimeEntity.title, TrackedAnimeEntity.title_japanese, TrackedAnimeEntity.image_url, TrackedAnimeEntity.synopsis, TrackedAnimeEntity.score, TrackedAnimeEntity.episodes, TrackedAnimeEntity.status, TrackedAnimeEntity.airing, TrackedAnimeEntity.rated, TrackedAnimeEntity.genres, TrackedAnimeEntity.source, TrackedAnimeEntity.duration, TrackedAnimeEntity.rating, TrackedAnimeEntity.year, TrackedAnimeEntity.season, TrackedAnimeEntity.watch_status, TrackedAnimeEntity.episodes_watched, TrackedAnimeEntity.user_rating, TrackedAnimeEntity.notes, TrackedAnimeEntity.where_to_watch, TrackedAnimeEntity.date_added, TrackedAnimeEntity.date_completed FROM TrackedAnimeEntity
    |WHERE watch_status = ?
    |ORDER BY date_added DESC
    """.trimMargin(), mapper, 1) {
      var parameterIndex = 0
      bindString(parameterIndex++, watch_status)
    }

    override fun toString(): String = "AnimeDatabase.sq:getByStatus"
  }

  private inner class GetByMalIdQuery<out T : Any>(
    public val mal_id: Long,
    mapper: (SqlCursor) -> T,
  ) : Query<T>(mapper) {
    override fun addListener(listener: Query.Listener) {
      driver.addListener("TrackedAnimeEntity", listener = listener)
    }

    override fun removeListener(listener: Query.Listener) {
      driver.removeListener("TrackedAnimeEntity", listener = listener)
    }

    override fun <R> execute(mapper: (SqlCursor) -> QueryResult<R>): QueryResult<R> = driver.executeQuery(-1_865_788_418, """
    |SELECT TrackedAnimeEntity.mal_id, TrackedAnimeEntity.title, TrackedAnimeEntity.title_japanese, TrackedAnimeEntity.image_url, TrackedAnimeEntity.synopsis, TrackedAnimeEntity.score, TrackedAnimeEntity.episodes, TrackedAnimeEntity.status, TrackedAnimeEntity.airing, TrackedAnimeEntity.rated, TrackedAnimeEntity.genres, TrackedAnimeEntity.source, TrackedAnimeEntity.duration, TrackedAnimeEntity.rating, TrackedAnimeEntity.year, TrackedAnimeEntity.season, TrackedAnimeEntity.watch_status, TrackedAnimeEntity.episodes_watched, TrackedAnimeEntity.user_rating, TrackedAnimeEntity.notes, TrackedAnimeEntity.where_to_watch, TrackedAnimeEntity.date_added, TrackedAnimeEntity.date_completed FROM TrackedAnimeEntity
    |WHERE mal_id = ?
    """.trimMargin(), mapper, 1) {
      var parameterIndex = 0
      bindLong(parameterIndex++, mal_id)
    }

    override fun toString(): String = "AnimeDatabase.sq:getByMalId"
  }
}
