package io.sws.watchstack.presentation.screen.detail

import androidx.lifecycle.viewModelScope
import io.sws.watchstack.core.HapticFeedbackType
import io.sws.watchstack.core.performHaptic
import io.sws.watchstack.core.shareText
import io.sws.watchstack.domain.model.Anime
import io.sws.watchstack.domain.model.WatchStatus
import io.sws.watchstack.domain.usecase.GetAnimeDetailsUseCase
import io.sws.watchstack.domain.usecase.GetCharactersUseCase
import io.sws.watchstack.domain.usecase.GetRecommendationsUseCase
import io.sws.watchstack.domain.usecase.GetTrackedByMalIdUseCase
import io.sws.watchstack.domain.usecase.RemoveTrackedAnimeUseCase
import io.sws.watchstack.domain.usecase.TrackAnimeUseCase
import io.sws.watchstack.domain.usecase.UpdateTrackedAnimeUseCase
import io.sws.watchstack.presentation.BaseViewModel
import io.sws.watchstack.presentation.UiEffect
import io.sws.watchstack.presentation.navigation.DetailRoute
import io.sws.watchstack.presentation.navigation.Navigator
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

class AnimeDetailViewModel(
    private val getAnimeDetailsUseCase: GetAnimeDetailsUseCase,
    private val getTrackedByMalIdUseCase: GetTrackedByMalIdUseCase,
    private val getCharactersUseCase: GetCharactersUseCase,
    private val getRecommendationsUseCase: GetRecommendationsUseCase,
    private val trackAnimeUseCase: TrackAnimeUseCase,
    private val updateTrackedAnimeUseCase: UpdateTrackedAnimeUseCase,
    private val removeTrackedAnimeUseCase: RemoveTrackedAnimeUseCase,
    private val navigator: Navigator
) : BaseViewModel<DetailUiState, DetailIntent, UiEffect>() {

    override fun initialState() = DetailUiState()

    override fun onIntent(intent: DetailIntent) {
        when (intent) {
            is DetailIntent.LoadAnime -> loadAnime(intent.malId, intent.seed)
            is DetailIntent.SaveEdit -> saveEdit(
                intent.episodes,
                intent.rating,
                intent.whereToWatch,
                intent.notes
            )
            is DetailIntent.RemoveFromTracking -> removeTracking()
            is DetailIntent.ShowTrackDialog -> updateState {
                copy(
                    showTrackDialog = true,
                    trackDialogStatus = WatchStatus.WATCHLIST,
                    trackDialogWhere = "",
                    actionError = null
                )
            }
            is DetailIntent.ShowEditDialog -> {
                val t = uiState.value.tracked
                updateState {
                    copy(
                        showEditDialog = true,
                        editEpisodes = t?.episodesWatched?.toString() ?: "",
                        editRating = t?.userRating?.toString() ?: "",
                        editWhere = t?.whereToWatch ?: "",
                        editNotes = t?.notes ?: "",
                        actionError = null
                    )
                }
            }
            is DetailIntent.DismissDialog -> updateState {
                copy(showTrackDialog = false, showEditDialog = false, isSaving = false)
            }
            is DetailIntent.DismissActionError -> updateState { copy(actionError = null) }
            is DetailIntent.GoBack -> navigator.navigateBack()
            is DetailIntent.TrackDialogStatusChanged -> updateState {
                copy(trackDialogStatus = intent.status)
            }
            is DetailIntent.TrackDialogWhereChanged -> updateState {
                copy(trackDialogWhere = intent.where)
            }
            is DetailIntent.EditEpisodesChanged -> updateState {
                copy(editEpisodes = intent.episodes.filter { it.isDigit() })
            }
            is DetailIntent.EditRatingChanged -> updateState {
                copy(editRating = intent.rating.filter { it.isDigit() }.take(2))
            }
            is DetailIntent.EditWhereChanged -> updateState { copy(editWhere = intent.where) }
            is DetailIntent.EditNotesChanged -> updateState { copy(editNotes = intent.notes) }
            is DetailIntent.ConfirmTrack -> {
                val state = uiState.value
                trackAnime(state.trackDialogStatus, state.trackDialogWhere)
            }
            is DetailIntent.OpenRecommendation -> {
                navigator.navigate(DetailRoute(intent.malId, intent.anime))
            }
            is DetailIntent.OpenTrailer -> {
                sendEffect(UiEffect.Snackbar("Opening trailer…"))
            }
            is DetailIntent.Share -> {
                val anime = uiState.value.anime
                if (anime != null) {
                    shareText(
                        text = "${anime.title}\nhttps://myanimelist.net/anime/${anime.malId}",
                        title = anime.title
                    )
                    performHaptic(HapticFeedbackType.Light)
                }
            }
        }
    }

    private fun loadAnime(malId: Int, seed: Anime?) {
        viewModelScope.launch {
            updateState {
                copy(
                    isLoading = seed == null,
                    anime = seed ?: anime,
                    error = null,
                    actionError = null
                )
            }
            try {
                val details = getAnimeDetailsUseCase(malId)
                val tracked = getTrackedByMalIdUseCase(malId)
                details.fold(
                    onSuccess = { anime ->
                        updateState { copy(anime = anime, tracked = tracked, isLoading = false) }
                    },
                    onFailure = {
                        updateState { copy(error = it.message, isLoading = false) }
                    }
                )
                val charactersDef = async { getCharactersUseCase(malId) }
                val recsDef = async { getRecommendationsUseCase(malId) }
                charactersDef.await().onSuccess { updateState { copy(characters = it) } }
                recsDef.await().onSuccess { updateState { copy(recommendations = it) } }
            } catch (e: Exception) {
                updateState {
                    copy(error = e.message ?: "Failed to load anime", isLoading = false)
                }
            }
        }
    }

    private fun trackAnime(status: WatchStatus, whereToWatch: String) {
        val anime = uiState.value.anime ?: return
        if (uiState.value.isSaving) return
        viewModelScope.launch {
            updateState { copy(isSaving = true, actionError = null) }
            trackAnimeUseCase(anime, status, whereToWatch).fold(
                onSuccess = { tracked ->
                    performHaptic(HapticFeedbackType.Success)
                    updateState {
                        copy(tracked = tracked, showTrackDialog = false, isSaving = false)
                    }
                    sendEffect(UiEffect.Snackbar("Added to list"))
                },
                onFailure = {
                    updateState {
                        copy(actionError = it.message ?: "Failed to track", isSaving = false)
                    }
                }
            )
        }
    }

    private fun saveEdit(episodes: Int?, rating: Int?, whereToWatch: String, notes: String) {
        val tracked = uiState.value.tracked ?: return
        if (uiState.value.isSaving) return
        viewModelScope.launch {
            updateState { copy(isSaving = true, actionError = null) }
            val updated = tracked.copy(
                episodesWatched = episodes ?: tracked.episodesWatched,
                userRating = rating,
                whereToWatch = whereToWatch,
                notes = notes
            )
            updateTrackedAnimeUseCase(updated).fold(
                onSuccess = {
                    updateState {
                        copy(tracked = updated, showEditDialog = false, isSaving = false)
                    }
                    sendEffect(UiEffect.Snackbar("Saved"))
                },
                onFailure = {
                    updateState {
                        copy(actionError = it.message ?: "Failed to save", isSaving = false)
                    }
                }
            )
        }
    }

    private fun removeTracking() {
        val anime = uiState.value.anime ?: return
        if (uiState.value.isSaving) return
        viewModelScope.launch {
            updateState { copy(isSaving = true, actionError = null) }
            removeTrackedAnimeUseCase(anime.malId).fold(
                onSuccess = {
                    updateState { copy(tracked = null, isSaving = false) }
                    sendEffect(UiEffect.Snackbar("Removed from list"))
                },
                onFailure = {
                    updateState {
                        copy(actionError = it.message ?: "Failed to remove", isSaving = false)
                    }
                }
            )
        }
    }
}
