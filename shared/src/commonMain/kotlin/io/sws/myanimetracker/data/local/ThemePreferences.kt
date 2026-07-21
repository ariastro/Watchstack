package io.sws.myanimetracker.data.local

import io.sws.myanimetracker.domain.model.ThemeMode

expect class ThemePreferences {
    fun getThemeMode(): ThemeMode
    fun setThemeMode(mode: ThemeMode)
}
