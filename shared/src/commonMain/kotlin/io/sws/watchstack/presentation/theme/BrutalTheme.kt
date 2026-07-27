package io.sws.watchstack.presentation.theme

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.ui.graphics.Color

private const val ThemeAnimMs = 420

private val LightScheme = lightColorScheme(
    primary = BrutalPalette.primary, onPrimary = BrutalPalette.onPrimary,
    background = BrutalPalette.background, onBackground = BrutalPalette.onBackground,
    surface = BrutalPalette.surface, onSurface = BrutalPalette.onSurface,
    surfaceVariant = BrutalPalette.surfaceVariant, onSurfaceVariant = BrutalPalette.onSurfaceVariant,
    primaryContainer = BrutalPalette.primaryContainer, onPrimaryContainer = BrutalPalette.onPrimaryContainer,
    secondary = BrutalPalette.secondary, onSecondary = BrutalPalette.onSecondary,
    tertiary = BrutalPalette.accent, onTertiary = BrutalPalette.onAccent,
    error = BrutalPalette.error, onError = BrutalPalette.onError,
    outline = BrutalPalette.border
)

private val DarkScheme = darkColorScheme(
    primary = BrutalDarkPalette.primary, onPrimary = BrutalDarkPalette.onPrimary,
    background = BrutalDarkPalette.background, onBackground = BrutalDarkPalette.onBackground,
    surface = BrutalDarkPalette.surface, onSurface = BrutalDarkPalette.onSurface,
    surfaceVariant = BrutalDarkPalette.surfaceVariant, onSurfaceVariant = BrutalDarkPalette.onSurfaceVariant,
    primaryContainer = BrutalDarkPalette.primaryContainer, onPrimaryContainer = BrutalDarkPalette.onPrimaryContainer,
    secondary = BrutalDarkPalette.secondary, onSecondary = BrutalDarkPalette.onSecondary,
    tertiary = BrutalDarkPalette.accent, onTertiary = BrutalDarkPalette.onAccent,
    error = BrutalDarkPalette.error, onError = BrutalDarkPalette.onError,
    outline = BrutalDarkPalette.border
)

@Composable
private fun animateThemeColor(target: Color): Color {
    val animated by animateColorAsState(
        targetValue = target,
        animationSpec = tween(durationMillis = ThemeAnimMs),
        label = "themeColor"
    )
    return animated
}

@Composable
fun rememberAnimatedBrutalColors(darkTheme: Boolean): BrutalColors {
    val target = if (darkTheme) BrutalDarkPalette else BrutalPalette
    return BrutalColors(
        background = animateThemeColor(target.background),
        onBackground = animateThemeColor(target.onBackground),
        surface = animateThemeColor(target.surface),
        onSurface = animateThemeColor(target.onSurface),
        surfaceVariant = animateThemeColor(target.surfaceVariant),
        onSurfaceVariant = animateThemeColor(target.onSurfaceVariant),
        primary = animateThemeColor(target.primary),
        onPrimary = animateThemeColor(target.onPrimary),
        primaryContainer = animateThemeColor(target.primaryContainer),
        onPrimaryContainer = animateThemeColor(target.onPrimaryContainer),
        secondary = animateThemeColor(target.secondary),
        onSecondary = animateThemeColor(target.onSecondary),
        accent = animateThemeColor(target.accent),
        onAccent = animateThemeColor(target.onAccent),
        error = animateThemeColor(target.error),
        onError = animateThemeColor(target.onError),
        textPrimary = animateThemeColor(target.textPrimary),
        textSecondary = animateThemeColor(target.textSecondary),
        textInverse = animateThemeColor(target.textInverse),
        border = animateThemeColor(target.border),
        divider = animateThemeColor(target.divider),
        overlay = animateThemeColor(target.overlay),
        shadow = animateThemeColor(target.shadow),
        glass = animateThemeColor(target.glass),
        glassBorder = animateThemeColor(target.glassBorder),
        watchlistColor = animateThemeColor(target.watchlistColor),
        watchingColor = animateThemeColor(target.watchingColor),
        watchedColor = animateThemeColor(target.watchedColor)
    )
}

@Composable
fun BrutalTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val animatedColors = rememberAnimatedBrutalColors(darkTheme)
    val baseScheme = if (darkTheme) DarkScheme else LightScheme
    val animatedScheme = baseScheme.copy(
        primary = animatedColors.primary,
        onPrimary = animatedColors.onPrimary,
        background = animatedColors.background,
        onBackground = animatedColors.onBackground,
        surface = animatedColors.surface,
        onSurface = animatedColors.onSurface,
        surfaceVariant = animatedColors.surfaceVariant,
        onSurfaceVariant = animatedColors.onSurfaceVariant,
        primaryContainer = animatedColors.primaryContainer,
        onPrimaryContainer = animatedColors.onPrimaryContainer,
        secondary = animatedColors.secondary,
        onSecondary = animatedColors.onSecondary,
        tertiary = animatedColors.accent,
        onTertiary = animatedColors.onAccent,
        error = animatedColors.error,
        onError = animatedColors.onError,
        outline = animatedColors.border
    )

    CompositionLocalProvider(
        LocalBrutalColors provides animatedColors,
        LocalBrutalTypography provides rememberBrutalTypography(),
        LocalBrutalDimensions provides BrutalDimensionsDefaults
    ) {
        MaterialTheme(colorScheme = animatedScheme, content = content)
    }
}
