package io.sws.myanimetracker.presentation.theme

import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color

data class BrutalColors(
    val background: Color, val onBackground: Color,
    val surface: Color, val onSurface: Color, val surfaceVariant: Color, val onSurfaceVariant: Color,
    val primary: Color, val onPrimary: Color, val primaryContainer: Color, val onPrimaryContainer: Color,
    val secondary: Color, val onSecondary: Color,
    val accent: Color, val onAccent: Color,
    val error: Color, val onError: Color,
    val textPrimary: Color, val textSecondary: Color, val textInverse: Color,
    val border: Color, val divider: Color, val overlay: Color, val shadow: Color,
    val glass: Color, val glassBorder: Color,
    val watchlistColor: Color, val watchingColor: Color, val watchedColor: Color
)

val BrutalPalette = BrutalColors(
    background = Color(0xFFF1F5F9), onBackground = Color(0xFF0F172A),
    surface = Color(0xFFFFFFFF), onSurface = Color(0xFF0F172A),
    surfaceVariant = Color(0xFFE2E8F0), onSurfaceVariant = Color(0xFF64748B),
    primary = Color(0xFF4338CA), onPrimary = Color(0xFFFFFFFF),
    primaryContainer = Color(0xFFE0E7FF), onPrimaryContainer = Color(0xFF312E81),
    secondary = Color(0xFF6366F1), onSecondary = Color(0xFFFFFFFF),
    accent = Color(0xFF7C3AED), onAccent = Color(0xFFFFFFFF),
    error = Color(0xFFDC2626), onError = Color(0xFFFFFFFF),
    textPrimary = Color(0xFF0F172A), textSecondary = Color(0xFF64748B), textInverse = Color(0xFFFFFFFF),
    border = Color(0x1A0F172A), divider = Color(0x140F172A), overlay = Color(0x99000000),
    shadow = Color(0x1A0F172A),
    glass = Color(0xE6FFFFFF), glassBorder = Color(0x1A0F172A),
    watchlistColor = Color(0xFFEC4899), watchingColor = Color(0xFF3B82F6), watchedColor = Color(0xFF10B981)
)

val BrutalDarkPalette = BrutalColors(
    background = Color(0xFF0F172A), onBackground = Color(0xFFF8FAFC),
    surface = Color(0xFF131936), onSurface = Color(0xFFF8FAFC),
    surfaceVariant = Color(0xFF1E293B), onSurfaceVariant = Color(0xFF94A3B8),
    primary = Color(0xFF818CF8), onPrimary = Color(0xFF0F172A),
    primaryContainer = Color(0xFF312E81), onPrimaryContainer = Color(0xFFC7D2FE),
    secondary = Color(0xFF6366F1), onSecondary = Color(0xFFFFFFFF),
    accent = Color(0xFFA78BFA), onAccent = Color(0xFF0F172A),
    error = Color(0xFFF87171), onError = Color(0xFF0F172A),
    textPrimary = Color(0xFFF8FAFC), textSecondary = Color(0xFF94A3B8), textInverse = Color(0xFF0F172A),
    border = Color(0x14FFFFFF), divider = Color(0x0FFFFFFF), overlay = Color(0xB3000000),
    shadow = Color(0x66000000),
    glass = Color(0xCC131936), glassBorder = Color(0x1AFFFFFF),
    watchlistColor = Color(0xFFF472B6), watchingColor = Color(0xFF60A5FA), watchedColor = Color(0xFF34D399)
)

val LocalBrutalColors = staticCompositionLocalOf { BrutalDarkPalette }
