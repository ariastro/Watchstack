package io.sws.myanimetracker.db

import kotlin.Double
import kotlin.Long
import kotlin.String

public data class TrackedAnimeEntity(
  public val mal_id: Long,
  public val title: String,
  public val title_japanese: String?,
  public val image_url: String?,
  public val synopsis: String?,
  public val score: Double?,
  public val episodes: Long?,
  public val status: String?,
  public val airing: Long,
  public val rated: String?,
  public val genres: String,
  public val source: String?,
  public val duration: String?,
  public val rating: String?,
  public val year: Long?,
  public val season: String?,
  public val watch_status: String,
  public val episodes_watched: Long,
  public val user_rating: Long?,
  public val notes: String,
  public val where_to_watch: String,
  public val date_added: Long,
  public val date_completed: Long?,
)
