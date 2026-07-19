package io.sws.myanimetracker.presentation.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider

private val LightScheme = lightColorScheme(
    primary = BrutalPalette.primary, onPrimary = BrutalPalette.onPrimary,
    background = BrutalPalette.background, onBackground = BrutalPalette.onBackground,
    surface = BrutalPalette.surface, onSurface = BrutalPalette.onSurface,
    surfaceVariant = BrutalPalette.surfaceVariant, onSurfaceVariant = BrutalPalette.onSurfaceVariant,
    primaryContainer = BrutalPalette.primaryContainer, onPrimaryContainer = BrutalPalette.onPrimaryContainer,
    secondary = BrutalPalette.secondary, onSecondary = BrutalPalette.onSecondary,
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
    error = BrutalDarkPalette.error, onError = BrutalDarkPalette.onError,
    outline = BrutalDarkPalette.border
)

@Composable
fun BrutalTheme(darkTheme: Boolean = isSystemInDarkTheme(), content: @Composable () -> Unit) {
    CompositionLocalProvider(
        LocalBrutalColors provides if (darkTheme) BrutalDarkPalette else BrutalPalette,
        LocalBrutalTypography provides rememberBrutalTypography(),
        LocalBrutalDimensions provides BrutalDimensionsDefaults
    ) {
        MaterialTheme(colorScheme = if (darkTheme) DarkScheme else LightScheme, content = content)
    }
}
