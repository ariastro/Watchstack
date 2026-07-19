package io.sws.myanimetracker.presentation.screen.detail

import io.sws.myanimetracker.domain.model.Anime
import io.sws.myanimetracker.domain.model.WatchStatus
import io.sws.myanimetracker.domain.usecase.*
import io.sws.myanimetracker.presentation.BaseViewModel
import io.sws.myanimetracker.presentation.navigation.DetailRoute
import io.sws.myanimetracker.presentation.navigation.Navigator
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import androidx.lifecycle.viewModelScope

class AnimeDetailViewModel(
    private val getAnimeDetailsUseCase: GetAnimeDetailsUseCase,
    private val getTrackedByMalIdUseCase: GetTrackedByMalIdUseCase,
    private val getCharactersUseCase: GetCharactersUseCase,
    private val getRecommendationsUseCase: GetRecommendationsUseCase,
    private val trackAnimeUseCase: TrackAnimeUseCase,
    private val updateTrackedAnimeUseCase: UpdateTrackedAnimeUseCase,
    private val removeTrackedAnimeUseCase: RemoveTrackedAnimeUseCase,
    private val navigator: Navigator
) : BaseViewModel<DetailUiState, DetailIntent>() {

    override fun initialState() = DetailUiState()

    override fun onIntent(intent: DetailIntent) {
        when (intent) {
            is DetailIntent.LoadAnime -> loadAnime(intent.malId, intent.seed)
            is DetailIntent.AddToWatchlist -> trackAnime(WatchStatus.WATCHLIST, intent.whereToWatch)
            is DetailIntent.StartWatching -> trackAnime(WatchStatus.WATCHING, intent.whereToWatch)
            is DetailIntent.MarkWatched -> trackAnime(WatchStatus.WATCHED, intent.whereToWatch)
            is DetailIntent.SaveEdit -> saveEdit(intent.episodes, intent.rating, intent.whereToWatch, intent.notes)
            is DetailIntent.RemoveFromTracking -> removeTracking()
            is DetailIntent.ShowTrackDialog -> updateState {
                copy(showTrackDialog = true, trackDialogStatus = WatchStatus.WATCHLIST, trackDialogWhere = "")
            }
            is DetailIntent.ShowEditDialog -> {
                val t = uiState.value.tracked
                updateState {
                    copy(
                        showEditDialog = true,
                        editEpisodes = t?.episodesWatched?.toString() ?: "",
                        editRating = t?.userRating?.toString() ?: "",
                        editWhere = t?.whereToWatch ?: "",
                        editNotes = t?.notes ?: ""
                    )
                }
            }
            is DetailIntent.DismissDialog -> updateState { copy(showTrackDialog = false, showEditDialog = false) }
            is DetailIntent.ClearError -> updateState { copy(error = null) }
            is DetailIntent.GoBack -> navigator.navigateBack()
            is DetailIntent.TrackDialogStatusChanged -> updateState { copy(trackDialogStatus = intent.status) }
            is DetailIntent.TrackDialogWhereChanged -> updateState { copy(trackDialogWhere = intent.where) }
            is DetailIntent.EditEpisodesChanged -> updateState { copy(editEpisodes = intent.episodes.filter { it.isDigit() }) }
            is DetailIntent.EditRatingChanged -> updateState { copy(editRating = intent.rating.filter { it.isDigit() }.take(2)) }
            is DetailIntent.EditWhereChanged -> updateState { copy(editWhere = intent.where) }
            is DetailIntent.EditNotesChanged -> updateState { copy(editNotes = intent.notes) }
            is DetailIntent.ConfirmTrack -> {
                val state = uiState.value
                trackAnime(state.trackDialogStatus, state.trackDialogWhere)
            }
            is DetailIntent.OpenRecommendation -> navigator.navigate(DetailRoute(intent.malId, intent.anime))
        }
    }

    private fun loadAnime(malId: Int, seed: Anime?) {
        viewModelScope.launch {
            updateState { copy(isLoading = seed == null, anime = seed ?: anime) }
            val details = getAnimeDetailsUseCase(malId)
            val tracked = getTrackedByMalIdUseCase(malId)
            details.fold(
                onSuccess = { anime ->
                    updateState { copy(anime = anime, tracked = tracked, isLoading = false) }
                },
                onFailure = { updateState { copy(error = it.message, isLoading = false) } }
            )
            val charactersDef = async { getCharactersUseCase(malId) }
            val recsDef = async { getRecommendationsUseCase(malId) }
            charactersDef.await().fold(
                onSuccess = { updateState { copy(characters = it) } },
                onFailure = {}
            )
            recsDef.await().fold(
                onSuccess = { updateState { copy(recommendations = it) } },
                onFailure = {}
            )
        }
    }

    private fun trackAnime(status: WatchStatus, whereToWatch: String) {
        val anime = uiState.value.anime ?: return
        viewModelScope.launch {
            trackAnimeUseCase(anime, status, whereToWatch).fold(
                onSuccess = { tracked -> updateState { copy(tracked = tracked, showTrackDialog = false) } },
                onFailure = { updateState { copy(error = it.message) } }
            )
        }
    }

    private fun saveEdit(episodes: Int?, rating: Int?, whereToWatch: String, notes: String) {
        val tracked = uiState.value.tracked ?: return
        viewModelScope.launch {
            val updated = tracked.copy(
                episodesWatched = episodes ?: tracked.episodesWatched,
                userRating = rating,
                whereToWatch = whereToWatch,
                notes = notes
            )
            updateTrackedAnimeUseCase(updated).fold(
                onSuccess = { updateState { copy(tracked = updated, showEditDialog = false) } },
                onFailure = { updateState { copy(error = it.message) } }
            )
        }
    }

    private fun removeTracking() {
        val anime = uiState.value.anime ?: return
        viewModelScope.launch {
            removeTrackedAnimeUseCase(anime.malId).fold(
                onSuccess = { updateState { copy(tracked = null) } },
                onFailure = { updateState { copy(error = it.message) } }
            )
        }
    }
}
