package io.sws.myanimetracker.presentation.screen.settings

import androidx.lifecycle.viewModelScope
import io.sws.myanimetracker.core.HapticFeedbackType
import io.sws.myanimetracker.core.performHaptic
import io.sws.myanimetracker.presentation.SimpleViewModel
import io.sws.myanimetracker.presentation.navigation.Navigator
import io.sws.myanimetracker.presentation.theme.ThemeController
import kotlinx.coroutines.launch

class SettingsViewModel(
    private val themeController: ThemeController,
    private val navigator: Navigator
) : SimpleViewModel<SettingsUiState, SettingsIntent>() {

    override fun initialState() = SettingsUiState(themeMode = themeController.themeMode.value)

    init {
        viewModelScope.launch {
            themeController.themeMode.collect { mode ->
                updateState { copy(themeMode = mode) }
            }
        }
    }

    override fun onIntent(intent: SettingsIntent) {
        when (intent) {
            is SettingsIntent.ThemeSelected -> {
                themeController.setThemeMode(intent.mode)
                performHaptic(HapticFeedbackType.Light)
            }
            is SettingsIntent.GoBack -> navigator.navigateBack()
        }
    }
}
