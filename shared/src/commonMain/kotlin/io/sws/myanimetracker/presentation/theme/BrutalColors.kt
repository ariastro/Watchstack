package io.sws.myanimetracker.presentation.theme

import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color

data class BrutalColors(
    val background: Color, val onBackground: Color,
    val surface: Color, val onSurface: Color, val surfaceVariant: Color, val onSurfaceVariant: Color,
    val primary: Color, val onPrimary: Color, val primaryContainer: Color, val onPrimaryContainer: Color,
    val secondary: Color, val onSecondary: Color,
    val error: Color, val onError: Color,
    val textPrimary: Color, val textSecondary: Color, val textInverse: Color,
    val border: Color, val divider: Color, val overlay: Color, val shadow: Color,
    val watchlistColor: Color, val watchingColor: Color, val watchedColor: Color
)

// Modern mobile palette — dark-first, vibrant purple primary, soft surfaces.
val BrutalPalette = BrutalColors(
    background = Color(0xFFF6F7FB), onBackground = Color(0xFF1A1A2E),
    surface = Color(0xFFFFFFFF), onSurface = Color(0xFF1A1A2E),
    surfaceVariant = Color(0xFFEEF0F6), onSurfaceVariant = Color(0xFF6B6B80),
    primary = Color(0xFF6C5CE7), onPrimary = Color(0xFFFFFFFF),
    primaryContainer = Color(0xFFECE9FF), onPrimaryContainer = Color(0xFF4A3FBF),
    secondary = Color(0xFF00B8A9), onSecondary = Color(0xFFFFFFFF),
    error = Color(0xFFE5484D), onError = Color(0xFFFFFFFF),
    textPrimary = Color(0xFF1A1A2E), textSecondary = Color(0xFF6B6B80), textInverse = Color(0xFFFFFFFF),
    border = Color(0xFFE2E4EE), divider = Color(0xFFE8EAF2), overlay = Color(0x80000000),
    shadow = Color(0x14000000),
    watchlistColor = Color(0xFFFF6B9D), watchingColor = Color(0xFF4D8DFF), watchedColor = Color(0xFF2ECC71)
)

val BrutalDarkPalette = BrutalColors(
    background = Color(0xFF0E0E14), onBackground = Color(0xFFECECF4),
    surface = Color(0xFF1A1A24), onSurface = Color(0xFFECECF4),
    surfaceVariant = Color(0xFF24242F), onSurfaceVariant = Color(0xFF9A9AB0),
    primary = Color(0xFF8B7CF6), onPrimary = Color(0xFF0E0E14),
    primaryContainer = Color(0xFF2A2650), onPrimaryContainer = Color(0xFFC9C0FF),
    secondary = Color(0xFF2DD4BF), onSecondary = Color(0xFF0E0E14),
    error = Color(0xFFF0626A), onError = Color(0xFF0E0E14),
    textPrimary = Color(0xFFECECF4), textSecondary = Color(0xFF9A9AB0), textInverse = Color(0xFF0E0E14),
    border = Color(0xFF2C2C3A), divider = Color(0xFF262633), overlay = Color(0x80000000),
    shadow = Color(0x40000000),
    watchlistColor = Color(0xFFFF7FAB), watchingColor = Color(0xFF6BA8FF), watchedColor = Color(0xFF4FE08C)
)

val LocalBrutalColors = staticCompositionLocalOf { BrutalPalette }
