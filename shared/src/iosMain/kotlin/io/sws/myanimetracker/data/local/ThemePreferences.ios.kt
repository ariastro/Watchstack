package io.sws.myanimetracker.data.local

import io.sws.myanimetracker.domain.model.ThemeMode
import platform.Foundation.NSUserDefaults

actual class ThemePreferences {
    private val defaults = NSUserDefaults.standardUserDefaults

    actual fun getThemeMode(): ThemeMode =
        ThemeMode.fromKey(defaults.stringForKey(KEY_THEME_MODE))

    actual fun setThemeMode(mode: ThemeMode) {
        defaults.setObject(mode.key, forKey = KEY_THEME_MODE)
    }

    companion object {
        private const val KEY_THEME_MODE = "theme_mode"
    }
}
