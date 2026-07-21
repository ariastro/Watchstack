package io.sws.myanimetracker.presentation.screen.home

import androidx.lifecycle.viewModelScope
import io.sws.myanimetracker.core.currentSeason
import io.sws.myanimetracker.domain.model.Anime
import io.sws.myanimetracker.domain.model.PagedAnime
import io.sws.myanimetracker.domain.model.WatchStatus
import io.sws.myanimetracker.domain.usecase.GetAiringNowUseCase
import io.sws.myanimetracker.domain.usecase.GetSeasonalAnimeUseCase
import io.sws.myanimetracker.domain.usecase.GetTopAnimeUseCase
import io.sws.myanimetracker.domain.usecase.GetTrackedAnimeUseCase
import io.sws.myanimetracker.domain.usecase.GetUpcomingUseCase
import io.sws.myanimetracker.presentation.SimpleViewModel
import io.sws.myanimetracker.presentation.navigation.BrowseRoute
import io.sws.myanimetracker.presentation.navigation.DetailRoute
import io.sws.myanimetracker.presentation.navigation.Navigator
import io.sws.myanimetracker.presentation.navigation.SearchRoute
import io.sws.myanimetracker.presentation.navigation.SettingsRoute
import io.sws.myanimetracker.presentation.navigation.StatsRoute
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch

class HomeViewModel(
    private val getTopAnimeUseCase: GetTopAnimeUseCase,
    private val getAiringNowUseCase: GetAiringNowUseCase,
    private val getSeasonalAnimeUseCase: GetSeasonalAnimeUseCase,
    private val getUpcomingUseCase: GetUpcomingUseCase,
    private val getTrackedAnimeUseCase: GetTrackedAnimeUseCase,
    private val navigator: Navigator
) : SimpleViewModel<HomeUiState, HomeIntent>() {

    override fun initialState() = HomeUiState()

    init {
        load(isRefresh = false)
        viewModelScope.launch {
            getTrackedAnimeUseCase(WatchStatus.WATCHING).collect { watching ->
                updateState {
                    copy(
                        continueWatching = watching
                            .sortedByDescending { it.dateAdded }
                            .take(20)
                    )
                }
            }
        }
    }

    override fun onIntent(intent: HomeIntent) {
        when (intent) {
            is HomeIntent.Load -> load(isRefresh = false)
            is HomeIntent.Refresh -> load(isRefresh = true)
            is HomeIntent.OpenSearch -> navigator.navigateRoot(SearchRoute)
            is HomeIntent.OpenSettings -> navigator.navigate(SettingsRoute)
            is HomeIntent.OpenStats -> navigator.navigate(StatsRoute)
            is HomeIntent.AnimeClicked -> navigator.navigate(DetailRoute(intent.malId, intent.anime))
            is HomeIntent.OpenBrowse -> navigator.navigate(BrowseRoute(intent.category))
        }
    }

    private fun load(isRefresh: Boolean) {
        viewModelScope.launch {
            updateState {
                copy(
                    isLoading = !isRefresh && !hasAnyContent(),
                    isRefreshing = isRefresh,
                    error = null
                )
            }
            try {
                val season = currentSeason()
                val results = coroutineScope {
                    val top = async { getTopAnimeUseCase() }
                    val airing = async { getAiringNowUseCase() }
                    val seasonal = async { getSeasonalAnimeUseCase(season.year, season.season) }
                    val upcoming = async { getUpcomingUseCase() }
                    listOf(top.await(), airing.await(), seasonal.await(), upcoming.await())
                }
                val topList = results[0].getOrDefault(PagedAnime(emptyList(), 1, false)).items
                    .distinctBy { it.malId }
                val airingList = results[1].getOrDefault(PagedAnime(emptyList(), 1, false)).items
                    .distinctBy { it.malId }
                val seasonalList = results[2].getOrDefault(PagedAnime(emptyList(), 1, false)).items
                    .distinctBy { it.malId }
                val upcomingList = results[3].getOrDefault(PagedAnime(emptyList(), 1, false)).items
                    .distinctBy { it.malId }
                val hasContent = listOf(topList, airingList, seasonalList, upcomingList)
                    .any { it.isNotEmpty() }
                val firstError = results.mapNotNull { it.exceptionOrNull()?.message }.firstOrNull()
                updateState {
                    copy(
                        topAnime = topList,
                        airingNow = airingList,
                        thisSeason = seasonalList,
                        upcoming = upcomingList,
                        error = if (!hasContent) firstError else null
                    )
                }
            } catch (e: Exception) {
                updateState { copy(error = e.message ?: "Failed to load home") }
            } finally {
                updateState { copy(isLoading = false, isRefreshing = false) }
            }
        }
    }

    private fun HomeUiState.hasAnyContent(): Boolean =
        topAnime.isNotEmpty() || airingNow.isNotEmpty() ||
            thisSeason.isNotEmpty() || upcoming.isNotEmpty()
}
