package io.sws.myanimetracker.presentation.screen.browse

import androidx.lifecycle.viewModelScope
import io.sws.myanimetracker.core.currentSeason
import io.sws.myanimetracker.domain.model.Anime
import io.sws.myanimetracker.domain.usecase.GetAiringNowUseCase
import io.sws.myanimetracker.domain.usecase.GetSeasonalAnimeUseCase
import io.sws.myanimetracker.domain.usecase.GetTopAnimeUseCase
import io.sws.myanimetracker.domain.usecase.GetUpcomingUseCase
import io.sws.myanimetracker.presentation.SimpleViewModel
import io.sws.myanimetracker.presentation.navigation.DetailRoute
import io.sws.myanimetracker.presentation.navigation.Navigator
import kotlinx.coroutines.launch

class BrowseViewModel(
    private val category: BrowseCategory,
    private val getAiringNowUseCase: GetAiringNowUseCase,
    private val getTopAnimeUseCase: GetTopAnimeUseCase,
    private val getSeasonalAnimeUseCase: GetSeasonalAnimeUseCase,
    private val getUpcomingUseCase: GetUpcomingUseCase,
    private val navigator: Navigator
) : SimpleViewModel<BrowseUiState, BrowseIntent>() {

    override fun initialState() = BrowseUiState(
        category = category,
        selectedSeason = currentSeason()
    )

    init { load(reset = true) }

    override fun onIntent(intent: BrowseIntent) {
        when (intent) {
            is BrowseIntent.Load -> load(reset = true)
            is BrowseIntent.LoadMore -> load(reset = false)
            is BrowseIntent.GoBack -> navigator.navigateBack()
            is BrowseIntent.AnimeClicked -> navigator.navigate(DetailRoute(intent.malId, intent.anime))
            is BrowseIntent.SeasonSelected -> {
                updateState { copy(selectedSeason = intent.season) }
                load(reset = true)
            }
            is BrowseIntent.SortSelected -> {
                updateState { copy(sort = intent.sort) }
                applyFilters()
            }
            is BrowseIntent.TypeFilterSelected -> {
                updateState { copy(typeFilter = intent.type) }
                applyFilters()
            }
            is BrowseIntent.MinScoreSelected -> {
                updateState { copy(minScore = intent.score) }
                applyFilters()
            }
        }
    }

    private fun load(reset: Boolean) {
        val state = uiState.value
        if (!reset && (!state.hasNext || state.isLoadingMore || state.isLoading)) return
        val nextPage = if (reset) 1 else state.page + 1
        viewModelScope.launch {
            updateState {
                copy(
                    isLoading = reset,
                    isLoadingMore = !reset,
                    error = null
                )
            }
            try {
                val result = when (category) {
                    BrowseCategory.AIRING -> getAiringNowUseCase(nextPage)
                    BrowseCategory.TOP -> getTopAnimeUseCase(nextPage)
                    BrowseCategory.SEASON -> {
                        val season = uiState.value.selectedSeason
                        getSeasonalAnimeUseCase(season.year, season.season, nextPage)
                    }
                    BrowseCategory.UPCOMING -> getUpcomingUseCase(nextPage)
                }
                result.fold(
                    onSuccess = { paged ->
                        updateState {
                            val merged = if (reset) paged.items else anime + paged.items
                            copy(
                                anime = merged,
                                page = paged.page,
                                hasNext = paged.hasNext,
                                isLoading = false,
                                isLoadingMore = false,
                                availableTypes = merged.mapNotNull { it.type }.distinct().sorted()
                            )
                        }
                        applyFilters()
                    },
                    onFailure = {
                        updateState {
                            copy(error = it.message, isLoading = false, isLoadingMore = false)
                        }
                    }
                )
            } catch (e: Exception) {
                updateState {
                    copy(error = e.message ?: "Failed to load", isLoading = false, isLoadingMore = false)
                }
            }
        }
    }

    private fun applyFilters() {
        updateState {
            copy(filteredAnime = anime.applyBrowseFilters(sort, typeFilter, minScore))
        }
    }
}

fun List<Anime>.applyBrowseFilters(
    sort: BrowseSort,
    typeFilter: String?,
    minScore: Double?
): List<Anime> {
    var list = this
    if (typeFilter != null) list = list.filter { it.type.equals(typeFilter, ignoreCase = true) }
    if (minScore != null) list = list.filter { (it.score ?: 0.0) >= minScore }
    return when (sort) {
        BrowseSort.DEFAULT -> list
        BrowseSort.SCORE -> list.sortedByDescending { it.score ?: 0.0 }
        BrowseSort.TITLE -> list.sortedBy { it.title.lowercase() }
        BrowseSort.YEAR -> list.sortedByDescending { it.year ?: 0 }
    }
}
