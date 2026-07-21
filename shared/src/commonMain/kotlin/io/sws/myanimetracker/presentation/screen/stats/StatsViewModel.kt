package io.sws.myanimetracker.presentation.screen.stats

import androidx.lifecycle.viewModelScope
import io.sws.myanimetracker.domain.usecase.GetLibraryStatsUseCase
import io.sws.myanimetracker.presentation.SimpleViewModel
import io.sws.myanimetracker.presentation.navigation.Navigator
import kotlinx.coroutines.launch

class StatsViewModel(
    private val getLibraryStatsUseCase: GetLibraryStatsUseCase,
    private val navigator: Navigator
) : SimpleViewModel<StatsUiState, StatsIntent>() {

    override fun initialState() = StatsUiState()

    init {
        viewModelScope.launch {
            getLibraryStatsUseCase().collect { stats ->
                updateState { copy(stats = stats) }
            }
        }
    }

    override fun onIntent(intent: StatsIntent) {
        when (intent) {
            is StatsIntent.GoBack -> navigator.navigateBack()
        }
    }
}
