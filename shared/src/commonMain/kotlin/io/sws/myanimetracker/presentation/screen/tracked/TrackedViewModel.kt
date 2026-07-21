package io.sws.myanimetracker.presentation.screen.tracked

import androidx.lifecycle.viewModelScope
import io.sws.myanimetracker.core.HapticFeedbackType
import io.sws.myanimetracker.core.currentTimeMillis
import io.sws.myanimetracker.core.performHaptic
import io.sws.myanimetracker.domain.model.TrackedAnime
import io.sws.myanimetracker.domain.model.WatchStatus
import io.sws.myanimetracker.domain.usecase.GetTrackedAnimeUseCase
import io.sws.myanimetracker.domain.usecase.RemoveTrackedAnimeUseCase
import io.sws.myanimetracker.domain.usecase.TrackAnimeUseCase
import io.sws.myanimetracker.domain.usecase.UpdateTrackedAnimeUseCase
import io.sws.myanimetracker.presentation.BaseViewModel
import io.sws.myanimetracker.presentation.UiEffect
import io.sws.myanimetracker.presentation.navigation.DetailRoute
import io.sws.myanimetracker.presentation.navigation.Navigator
import io.sws.myanimetracker.presentation.navigation.SearchRoute
import kotlinx.coroutines.launch

class TrackedViewModel(
    private val getTrackedAnimeUseCase: GetTrackedAnimeUseCase,
    private val updateTrackedAnimeUseCase: UpdateTrackedAnimeUseCase,
    private val removeTrackedAnimeUseCase: RemoveTrackedAnimeUseCase,
    private val trackAnimeUseCase: TrackAnimeUseCase,
    private val navigator: Navigator
) : BaseViewModel<TrackedUiState, TrackedIntent, UiEffect>() {

    private var lastRemoved: TrackedAnime? = null

    override fun initialState() = TrackedUiState()

    init {
        viewModelScope.launch {
            getTrackedAnimeUseCase(WatchStatus.WATCHLIST).collect {
                updateState { copy(watchlist = it) }
            }
        }
        viewModelScope.launch {
            getTrackedAnimeUseCase(WatchStatus.WATCHING).collect {
                updateState { copy(watching = it) }
            }
        }
        viewModelScope.launch {
            getTrackedAnimeUseCase(WatchStatus.WATCHED).collect {
                updateState { copy(watched = it) }
            }
        }
    }

    override fun onIntent(intent: TrackedIntent) {
        when (intent) {
            is TrackedIntent.TabSelected -> updateState { copy(activeTab = intent.tab) }
            is TrackedIntent.QueryChanged -> updateState { copy(query = intent.query) }
            is TrackedIntent.SortSelected -> updateState { copy(sort = intent.sort) }
            is TrackedIntent.AnimeClicked -> navigator.navigate(DetailRoute(intent.malId, intent.anime))
            is TrackedIntent.RemoveAnime -> remove(intent.malId)
            is TrackedIntent.ChangeStatus -> changeStatus(intent.malId, intent.status)
            is TrackedIntent.IncrementEpisode -> incrementEpisode(intent.malId)
            is TrackedIntent.UndoRemove -> undoRemove()
            is TrackedIntent.OpenSearch -> navigator.navigateRoot(SearchRoute)
        }
    }

    private fun findTracked(malId: Int): TrackedAnime? {
        val state = uiState.value
        return (state.watchlist + state.watching + state.watched)
            .firstOrNull { it.anime.malId == malId }
    }

    private fun remove(malId: Int) {
        val tracked = findTracked(malId) ?: return
        viewModelScope.launch {
            removeTrackedAnimeUseCase(malId).fold(
                onSuccess = {
                    lastRemoved = tracked
                    performHaptic(HapticFeedbackType.Warning)
                    sendEffect(
                        UiEffect.Snackbar(
                            message = "Removed “${tracked.anime.title}”",
                            actionLabel = "Undo",
                            actionId = TRACKED_UNDO_ACTION
                        )
                    )
                },
                onFailure = { sendEffect(UiEffect.Snackbar(it.message ?: "Remove failed")) }
            )
        }
    }

    private fun undoRemove() {
        val restored = lastRemoved ?: return
        viewModelScope.launch {
            // Re-upsert full snapshot (status, progress, notes, dates)
            updateTrackedAnimeUseCase(restored).fold(
                onSuccess = {
                    lastRemoved = null
                    sendEffect(UiEffect.Snackbar("Restored “${restored.anime.title}”"))
                },
                onFailure = {
                    // Fallback path via track if needed
                    trackAnimeUseCase(
                        anime = restored.anime,
                        status = restored.status,
                        whereToWatch = restored.whereToWatch
                    ).fold(
                        onSuccess = {
                            updateTrackedAnimeUseCase(restored)
                            lastRemoved = null
                            sendEffect(UiEffect.Snackbar("Restored “${restored.anime.title}”"))
                        },
                        onFailure = { e ->
                            sendEffect(UiEffect.Snackbar(e.message ?: "Undo failed"))
                        }
                    )
                }
            )
        }
    }

    private fun changeStatus(malId: Int, status: WatchStatus) {
        val tracked = findTracked(malId) ?: return
        viewModelScope.launch {
            val updated = tracked.copy(
                status = status,
                dateCompleted = if (status == WatchStatus.WATCHED) {
                    currentTimeMillis()
                } else tracked.dateCompleted
            )
            updateTrackedAnimeUseCase(updated).fold(
                onSuccess = {
                    performHaptic(HapticFeedbackType.Light)
                    val label = when (status) {
                        WatchStatus.WATCHLIST -> "Plan"
                        WatchStatus.WATCHING -> "Watching"
                        WatchStatus.WATCHED -> "Done"
                    }
                    sendEffect(UiEffect.Snackbar("Moved to $label"))
                },
                onFailure = { sendEffect(UiEffect.Snackbar(it.message ?: "Update failed")) }
            )
        }
    }

    private fun incrementEpisode(malId: Int) {
        val tracked = findTracked(malId) ?: return
        val total = tracked.anime.episodes
        val next = tracked.episodesWatched + 1
        if (total != null && next > total) {
            sendEffect(UiEffect.Snackbar("Already at final episode"))
            return
        }
        viewModelScope.launch {
            val completed = total != null && next >= total
            val updated = tracked.copy(
                episodesWatched = next,
                status = if (completed) WatchStatus.WATCHED else {
                    if (tracked.status == WatchStatus.WATCHLIST) WatchStatus.WATCHING
                    else tracked.status
                },
                dateCompleted = if (completed) currentTimeMillis() else tracked.dateCompleted
            )
            updateTrackedAnimeUseCase(updated).fold(
                onSuccess = {
                    performHaptic(
                        if (completed) HapticFeedbackType.Success else HapticFeedbackType.Medium
                    )
                    val msg = if (completed) {
                        "Finished · Ep $next"
                    } else {
                        "Ep $next${total?.let { "/$it" } ?: ""}"
                    }
                    sendEffect(UiEffect.Snackbar(msg))
                },
                onFailure = { sendEffect(UiEffect.Snackbar(it.message ?: "Update failed")) }
            )
        }
    }
}
