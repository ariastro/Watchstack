package io.sws.myanimetracker.presentation.screen.home

import io.sws.myanimetracker.domain.usecase.GetTopAnimeUseCase
import io.sws.myanimetracker.presentation.BaseViewModel
import io.sws.myanimetracker.presentation.navigation.DetailRoute
import io.sws.myanimetracker.presentation.navigation.Navigator
import kotlinx.coroutines.launch
import androidx.lifecycle.viewModelScope

class HomeViewModel(
    private val getTopAnimeUseCase: GetTopAnimeUseCase,
    private val navigator: Navigator
) : BaseViewModel<HomeUiState, HomeIntent>() {

    override fun initialState() = HomeUiState()

    init { load() }

    override fun onIntent(intent: HomeIntent) {
        when (intent) {
            is HomeIntent.Load -> load()
            is HomeIntent.ClearError -> updateState { copy(error = null) }
            is HomeIntent.AnimeClicked -> navigator.navigate(DetailRoute(intent.malId, intent.anime))
        }
    }

    private fun load() {
        viewModelScope.launch {
            updateState { copy(isLoading = true, error = null) }
            getTopAnimeUseCase().fold(
                onSuccess = { updateState { copy(topAnime = it, isLoading = false) } },
                onFailure = { updateState { copy(error = it.message, isLoading = false) } }
            )
        }
    }
}
