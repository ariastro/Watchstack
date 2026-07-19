package io.sws.myanimetracker.presentation.screen.search

import io.sws.myanimetracker.domain.usecase.GetTopAnimeUseCase
import io.sws.myanimetracker.domain.usecase.SearchAnimeUseCase
import io.sws.myanimetracker.presentation.BaseViewModel
import io.sws.myanimetracker.presentation.navigation.DetailRoute
import io.sws.myanimetracker.presentation.navigation.Navigator
import kotlinx.coroutines.launch
import androidx.lifecycle.viewModelScope

class SearchViewModel(
    private val searchAnimeUseCase: SearchAnimeUseCase,
    private val getTopAnimeUseCase: GetTopAnimeUseCase,
    private val navigator: Navigator
) : BaseViewModel<SearchUiState, SearchIntent>() {

    override fun initialState() = SearchUiState()

    init { loadTopAnime() }

    override fun onIntent(intent: SearchIntent) {
        when (intent) {
            is SearchIntent.QueryChanged -> updateState { copy(query = intent.query) }
            is SearchIntent.Search -> performSearch()
            is SearchIntent.ClearError -> updateState { copy(error = null) }
            is SearchIntent.AnimeClicked -> navigator.navigate(DetailRoute(intent.malId, intent.anime))
            is SearchIntent.BrowseClicked -> navigator.navigate(
                io.sws.myanimetracker.presentation.navigation.BrowseRoute(intent.category)
            )
        }
    }

    private fun loadTopAnime() {
        viewModelScope.launch {
            updateState { copy(isLoading = true) }
            getTopAnimeUseCase().fold(
                onSuccess = { updateState { copy(results = it, isLoading = false, hasSearched = false) } },
                onFailure = { updateState { copy(error = it.message, isLoading = false) } }
            )
        }
    }

    private fun performSearch() {
        val query = uiState.value.query.trim()
        if (query.isBlank()) { loadTopAnime(); return }
        viewModelScope.launch {
            updateState { copy(isLoading = true, error = null) }
            searchAnimeUseCase(query).fold(
                onSuccess = { updateState { copy(results = it, isLoading = false, hasSearched = true) } },
                onFailure = { updateState { copy(error = it.message, isLoading = false) } }
            )
        }
    }
}
