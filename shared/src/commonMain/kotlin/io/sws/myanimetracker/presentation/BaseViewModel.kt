package io.sws.myanimetracker.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

abstract class BaseViewModel<State, Intent, Effect> : ViewModel() {
    private val _uiState by lazy { MutableStateFlow(initialState()) }
    val uiState: StateFlow<State> by lazy { _uiState.asStateFlow() }

    private val _effects = Channel<Effect>(Channel.BUFFERED)
    open val effects: Flow<Effect> = _effects.receiveAsFlow()

    protected abstract fun initialState(): State
    abstract fun onIntent(intent: Intent)

    protected fun updateState(reducer: State.() -> State) {
        _uiState.value = _uiState.value.reducer()
    }

    protected fun sendEffect(effect: Effect) {
        viewModelScope.launch { _effects.send(effect) }
    }
}

/** Screens without one-shot effects. */
abstract class SimpleViewModel<State, Intent> : BaseViewModel<State, Intent, Unit>() {
    override val effects: Flow<Unit> = emptyFlow()
}
