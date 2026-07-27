package io.sws.watchstack.presentation.theme

import io.sws.watchstack.data.local.ThemePreferences
import io.sws.watchstack.domain.model.ThemeMode
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class ThemeController(
    private val preferences: ThemePreferences
) {
    private val _themeMode = MutableStateFlow(preferences.getThemeMode())
    val themeMode: StateFlow<ThemeMode> = _themeMode.asStateFlow()

    fun setThemeMode(mode: ThemeMode) {
        if (_themeMode.value == mode) return
        preferences.setThemeMode(mode)
        _themeMode.value = mode
    }
}

fun ThemeMode.resolveDark(systemDark: Boolean): Boolean = when (this) {
    ThemeMode.SYSTEM -> systemDark
    ThemeMode.LIGHT -> false
    ThemeMode.DARK -> true
}
