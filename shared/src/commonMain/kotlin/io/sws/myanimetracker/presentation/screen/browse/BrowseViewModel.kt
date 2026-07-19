package io.sws.myanimetracker.presentation.screen.browse

import io.sws.myanimetracker.core.currentSeason
import io.sws.myanimetracker.domain.usecase.GetAiringNowUseCase
import io.sws.myanimetracker.domain.usecase.GetSeasonalAnimeUseCase
import io.sws.myanimetracker.domain.usecase.GetTopAnimeUseCase
import io.sws.myanimetracker.domain.usecase.GetUpcomingUseCase
import io.sws.myanimetracker.presentation.BaseViewModel
import io.sws.myanimetracker.presentation.navigation.DetailRoute
import io.sws.myanimetracker.presentation.navigation.Navigator
import kotlinx.coroutines.launch
import androidx.lifecycle.viewModelScope

class BrowseViewModel(
    private val category: BrowseCategory,
    private val getAiringNowUseCase: GetAiringNowUseCase,
    private val getTopAnimeUseCase: GetTopAnimeUseCase,
    private val getSeasonalAnimeUseCase: GetSeasonalAnimeUseCase,
    private val getUpcomingUseCase: GetUpcomingUseCase,
    private val navigator: Navigator
) : BaseViewModel<BrowseUiState, BrowseIntent>() {

    override fun initialState() = BrowseUiState(category = category, selectedSeason = currentSeason())

    init { load() }

    override fun onIntent(intent: BrowseIntent) {
        when (intent) {
            is BrowseIntent.Load -> load()
            is BrowseIntent.ClearError -> updateState { copy(error = null) }
            is BrowseIntent.AnimeClicked -> navigator.navigate(DetailRoute(intent.malId, intent.anime))
            is BrowseIntent.SeasonSelected -> {
                updateState { copy(selectedSeason = intent.season) }
                load()
            }
        }
    }

    private fun load() {
        viewModelScope.launch {
            updateState { copy(isLoading = true, error = null) }
            val result = when (category) {
                BrowseCategory.AIRING -> getAiringNowUseCase()
                BrowseCategory.TOP -> getTopAnimeUseCase()
                BrowseCategory.SEASON -> {
                    val season = uiState.value.selectedSeason
                    getSeasonalAnimeUseCase(season.year, season.season)
                }
                BrowseCategory.UPCOMING -> getUpcomingUseCase()
            }
            result.fold(
                onSuccess = { updateState { copy(anime = it, isLoading = false) } },
                onFailure = { updateState { copy(error = it.message, isLoading = false) } }
            )
        }
    }
}
