package io.sws.myanimetracker.data.local

import android.content.Context
import io.sws.myanimetracker.domain.model.ThemeMode

actual class ThemePreferences(context: Context) {
    private val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)

    actual fun getThemeMode(): ThemeMode =
        ThemeMode.fromKey(prefs.getString(KEY_THEME_MODE, ThemeMode.SYSTEM.key))

    actual fun setThemeMode(mode: ThemeMode) {
        prefs.edit().putString(KEY_THEME_MODE, mode.key).apply()
    }

    companion object {
        private const val PREFS_NAME = "anime_tracker_settings"
        private const val KEY_THEME_MODE = "theme_mode"
    }
}
