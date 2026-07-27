package io.sws.watchstack.presentation.theme

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Immutable
data class BrutalDimensions(
    val borderThin: Dp = 1.dp, val borderMedium: Dp = 1.5.dp,
    val radiusXs: Dp = 10.dp, val radiusSm: Dp = 14.dp, val radiusMd: Dp = 18.dp,
    val radiusLg: Dp = 24.dp, val radiusXl: Dp = 32.dp, val radiusPill: Dp = 100.dp,
    val spacingXxs: Dp = 2.dp, val spacingXs: Dp = 4.dp, val spacingSm: Dp = 8.dp,
    val spacingMd: Dp = 12.dp, val spacingLg: Dp = 16.dp, val spacingXl: Dp = 24.dp,
    val spacingXxl: Dp = 32.dp, val spacingXxxl: Dp = 48.dp,
    val paddingScreen: Dp = 20.dp, val paddingCard: Dp = 14.dp,
    val posterWidth: Dp = 136.dp, val posterHeight: Dp = 200.dp,
    val heroHeight: Dp = 300.dp,
    val gridSpacing: Dp = 14.dp,
    val navBarHeight: Dp = 80.dp,
    val touchTarget: Dp = 48.dp
)

val BrutalDimensionsDefaults = BrutalDimensions()
val LocalBrutalDimensions = staticCompositionLocalOf { BrutalDimensionsDefaults }
