package io.sws.myanimetracker.presentation.theme

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Immutable
data class BrutalDimensions(
    val borderThin: Dp = 1.dp, val borderMedium: Dp = 1.5.dp,
    val radiusXs: Dp = 8.dp, val radiusSm: Dp = 12.dp, val radiusMd: Dp = 16.dp,
    val radiusLg: Dp = 24.dp, val radiusXl: Dp = 32.dp, val radiusPill: Dp = 100.dp,
    val spacingXxs: Dp = 2.dp, val spacingXs: Dp = 4.dp, val spacingSm: Dp = 8.dp,
    val spacingMd: Dp = 12.dp, val spacingLg: Dp = 16.dp, val spacingXl: Dp = 24.dp,
    val spacingXxl: Dp = 32.dp,
    val paddingScreen: Dp = 16.dp, val paddingCard: Dp = 12.dp,
    val posterWidth: Dp = 130.dp, val posterHeight: Dp = 190.dp,
    val gridSpacing: Dp = 12.dp
)

val BrutalDimensionsDefaults = BrutalDimensions()
val LocalBrutalDimensions = staticCompositionLocalOf { BrutalDimensionsDefaults }
