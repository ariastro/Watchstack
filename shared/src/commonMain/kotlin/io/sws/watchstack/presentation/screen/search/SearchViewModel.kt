package io.sws.watchstack.presentation.screen.search

import androidx.lifecycle.viewModelScope
import io.sws.watchstack.domain.usecase.AddSearchHistoryUseCase
import io.sws.watchstack.domain.usecase.ClearSearchHistoryUseCase
import io.sws.watchstack.domain.usecase.ObserveSearchHistoryUseCase
import io.sws.watchstack.domain.usecase.SearchAnimeUseCase
import io.sws.watchstack.presentation.SimpleViewModel
import io.sws.watchstack.presentation.navigation.DetailRoute
import io.sws.watchstack.presentation.navigation.Navigator
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class SearchViewModel(
    private val searchAnimeUseCase: SearchAnimeUseCase,
    private val observeSearchHistoryUseCase: ObserveSearchHistoryUseCase,
    private val addSearchHistoryUseCase: AddSearchHistoryUseCase,
    private val clearSearchHistoryUseCase: ClearSearchHistoryUseCase,
    private val navigator: Navigator
) : SimpleViewModel<SearchUiState, SearchIntent>() {

    private var searchJob: Job? = null

    override fun initialState() = SearchUiState()

    init {
        viewModelScope.launch {
            observeSearchHistoryUseCase().collect { recent ->
                updateState { copy(recentQueries = recent) }
            }
        }
    }

    override fun onIntent(intent: SearchIntent) {
        when (intent) {
            is SearchIntent.QueryChanged -> {
                updateState { copy(query = intent.query) }
                debounceSearch(intent.query)
            }
            is SearchIntent.Search -> performSearch(reset = true)
            is SearchIntent.LoadMore -> loadMore()
            is SearchIntent.Clear -> {
                searchJob?.cancel()
                updateState {
                    copy(
                        query = "",
                        results = emptyList(),
                        hasSearched = false,
                        error = null,
                        isLoading = false,
                        page = 1,
                        hasNext = false
                    )
                }
            }
            is SearchIntent.ClearHistory -> viewModelScope.launch { clearSearchHistoryUseCase() }
            is SearchIntent.RecentClicked -> {
                updateState { copy(query = intent.query) }
                performSearch(reset = true, queryOverride = intent.query)
            }
            is SearchIntent.AnimeClicked -> navigator.navigate(DetailRoute(intent.malId, intent.anime))
        }
    }

    private fun debounceSearch(query: String) {
        searchJob?.cancel()
        val trimmed = query.trim()
        if (trimmed.isBlank()) {
            updateState {
                copy(
                    results = emptyList(),
                    hasSearched = false,
                    isLoading = false,
                    error = null,
                    page = 1,
                    hasNext = false
                )
            }
            return
        }
        if (trimmed.length < 2) return
        searchJob = viewModelScope.launch {
            delay(400)
            performSearchInternal(trimmed, page = 1, append = false)
        }
    }

    private fun performSearch(reset: Boolean, queryOverride: String? = null) {
        val query = (queryOverride ?: uiState.value.query).trim()
        if (query.isBlank()) {
            searchJob?.cancel()
            updateState {
                copy(results = emptyList(), hasSearched = false, isLoading = false, page = 1, hasNext = false)
            }
            return
        }
        searchJob?.cancel()
        searchJob = viewModelScope.launch {
            performSearchInternal(query, page = 1, append = !reset && false)
        }
    }

    private fun loadMore() {
        val state = uiState.value
        if (!state.hasNext || state.isLoadingMore || state.isLoading) return
        val query = state.query.trim()
        if (query.isBlank()) return
        searchJob?.cancel()
        searchJob = viewModelScope.launch {
            performSearchInternal(query, page = state.page + 1, append = true)
        }
    }

    private suspend fun performSearchInternal(query: String, page: Int, append: Boolean) {
        updateState {
            copy(
                isLoading = !append,
                isLoadingMore = append,
                error = null
            )
        }
        try {
            searchAnimeUseCase(query, page).fold(
                onSuccess = { paged ->
                    addSearchHistoryUseCase(query)
                    updateState {
                        copy(
                            results = if (append) results + paged.items else paged.items,
                            page = paged.page,
                            hasNext = paged.hasNext,
                            isLoading = false,
                            isLoadingMore = false,
                            hasSearched = true
                        )
                    }
                },
                onFailure = {
                    updateState {
                        copy(
                            error = it.message,
                            isLoading = false,
                            isLoadingMore = false,
                            hasSearched = true
                        )
                    }
                }
            )
        } catch (e: Exception) {
            updateState {
                copy(
                    error = e.message ?: "Search failed",
                    isLoading = false,
                    isLoadingMore = false,
                    hasSearched = true
                )
            }
        }
    }
}
