package io.sws.myanimetracker.presentation.screen.settings

import io.sws.myanimetracker.domain.model.ThemeMode

data class SettingsUiState(
    val themeMode: ThemeMode = ThemeMode.SYSTEM
)

sealed interface SettingsIntent {
    data class ThemeSelected(val mode: ThemeMode) : SettingsIntent
    data object GoBack : SettingsIntent
}
