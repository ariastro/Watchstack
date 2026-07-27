package io.sws.watchstack.presentation.screen.detail

import io.sws.watchstack.domain.model.Anime
import io.sws.watchstack.domain.model.Character
import io.sws.watchstack.domain.model.RecommendedAnime
import io.sws.watchstack.domain.model.TrackedAnime
import io.sws.watchstack.domain.model.WatchStatus

data class DetailUiState(
    val anime: Anime? = null,
    val tracked: TrackedAnime? = null,
    val characters: List<Character> = emptyList(),
    val recommendations: List<RecommendedAnime> = emptyList(),
    val isLoading: Boolean = false,
    val isSaving: Boolean = false,
    val error: String? = null,
    val actionError: String? = null,
    val showTrackDialog: Boolean = false,
    val showEditDialog: Boolean = false,
    val trackDialogStatus: WatchStatus = WatchStatus.WATCHLIST,
    val trackDialogWhere: String = "",
    val editEpisodes: String = "",
    val editRating: String = "",
    val editWhere: String = "",
    val editNotes: String = ""
)

sealed interface DetailIntent {
    data class LoadAnime(val malId: Int, val seed: Anime? = null) : DetailIntent
    data class SaveEdit(
        val episodes: Int?,
        val rating: Int?,
        val whereToWatch: String,
        val notes: String
    ) : DetailIntent
    data object RemoveFromTracking : DetailIntent
    data object ShowTrackDialog : DetailIntent
    data object ShowEditDialog : DetailIntent
    data object DismissDialog : DetailIntent
    data object DismissActionError : DetailIntent
    data object GoBack : DetailIntent
    data class TrackDialogStatusChanged(val status: WatchStatus) : DetailIntent
    data class TrackDialogWhereChanged(val where: String) : DetailIntent
    data class EditEpisodesChanged(val episodes: String) : DetailIntent
    data class EditRatingChanged(val rating: String) : DetailIntent
    data class EditWhereChanged(val where: String) : DetailIntent
    data class EditNotesChanged(val notes: String) : DetailIntent
    data object ConfirmTrack : DetailIntent
    data class OpenRecommendation(val malId: Int, val anime: Anime? = null) : DetailIntent
    data object OpenTrailer : DetailIntent
    data object Share : DetailIntent
}
