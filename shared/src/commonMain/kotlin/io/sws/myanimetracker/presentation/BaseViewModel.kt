package io.sws.myanimetracker.presentation

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import androidx.lifecycle.ViewModel

abstract class BaseViewModel<State, Intent> : ViewModel() {
    private val _uiState by lazy { MutableStateFlow(initialState()) }
    val uiState: StateFlow<State> by lazy { _uiState.asStateFlow() }

    protected abstract fun initialState(): State
    abstract fun onIntent(intent: Intent)

    protected fun updateState(reducer: State.() -> State) {
        _uiState.value = _uiState.value.reducer()
    }
}
