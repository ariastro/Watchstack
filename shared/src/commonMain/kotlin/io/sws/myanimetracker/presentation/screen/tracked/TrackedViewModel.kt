package io.sws.myanimetracker.presentation.screen.tracked

import io.sws.myanimetracker.domain.model.WatchStatus
import io.sws.myanimetracker.domain.usecase.GetTrackedAnimeUseCase
import io.sws.myanimetracker.domain.usecase.RemoveTrackedAnimeUseCase
import io.sws.myanimetracker.presentation.BaseViewModel
import io.sws.myanimetracker.presentation.navigation.DetailRoute
import io.sws.myanimetracker.presentation.navigation.Navigator
import kotlinx.coroutines.launch
import androidx.lifecycle.viewModelScope

class TrackedViewModel(
    private val getTrackedAnimeUseCase: GetTrackedAnimeUseCase,
    private val removeTrackedAnimeUseCase: RemoveTrackedAnimeUseCase,
    private val navigator: Navigator
) : BaseViewModel<TrackedUiState, TrackedIntent>() {

    override fun initialState() = TrackedUiState()

    init {
        viewModelScope.launch {
            getTrackedAnimeUseCase(WatchStatus.WATCHLIST).collect { updateState { copy(watchlist = it) } }
        }
        viewModelScope.launch {
            getTrackedAnimeUseCase(WatchStatus.WATCHING).collect { updateState { copy(watching = it) } }
        }
        viewModelScope.launch {
            getTrackedAnimeUseCase(WatchStatus.WATCHED).collect { updateState { copy(watched = it) } }
        }
    }

    override fun onIntent(intent: TrackedIntent) {
        when (intent) {
            is TrackedIntent.TabSelected -> updateState { copy(activeTab = intent.tab) }
            is TrackedIntent.AnimeClicked -> navigator.navigate(DetailRoute(intent.malId, intent.anime))
            is TrackedIntent.RemoveAnime -> viewModelScope.launch {
                removeTrackedAnimeUseCase(intent.malId).onFailure { e ->
                    updateState { copy(error = e.message ?: "Failed to remove") }
                }
            }
            is TrackedIntent.ClearError -> updateState { copy(error = null) }
        }
    }
}
