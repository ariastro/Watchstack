package io.sws.watchstack.data.local

import io.sws.watchstack.domain.model.ThemeMode

expect class ThemePreferences {
    fun getThemeMode(): ThemeMode
    fun setThemeMode(mode: ThemeMode)
}
