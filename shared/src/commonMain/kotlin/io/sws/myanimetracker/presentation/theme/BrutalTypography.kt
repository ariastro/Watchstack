package io.sws.myanimetracker.presentation.theme

import androidx.compose.runtime.Composable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.sp
import myanimetracker.shared.generated.resources.Res
import myanimetracker.shared.generated.resources.inter_medium
import myanimetracker.shared.generated.resources.inter_regular
import myanimetracker.shared.generated.resources.inter_semibold
import myanimetracker.shared.generated.resources.poppins_black
import myanimetracker.shared.generated.resources.poppins_bold
import myanimetracker.shared.generated.resources.poppins_extrabold
import myanimetracker.shared.generated.resources.poppins_medium
import myanimetracker.shared.generated.resources.poppins_semibold
import myanimetracker.shared.generated.resources.poppins_regular
import myanimetracker.shared.generated.resources.space_mono_bold
import myanimetracker.shared.generated.resources.space_mono_regular
import org.jetbrains.compose.resources.Font

data class BrutalTypography(
    val displayLarge: TextStyle, val displayMedium: TextStyle, val displaySmall: TextStyle,
    val headlineLarge: TextStyle, val headlineMedium: TextStyle, val headlineSmall: TextStyle,
    val titleLarge: TextStyle, val titleMedium: TextStyle, val titleSmall: TextStyle,
    val bodyLarge: TextStyle, val bodyMedium: TextStyle, val bodySmall: TextStyle,
    val labelLarge: TextStyle, val labelMedium: TextStyle, val labelSmall: TextStyle,
    val mono: TextStyle
)

private fun brutal(
    family: FontFamily,
    fontSize: TextUnit,
    fontWeight: FontWeight,
    lineHeight: TextUnit,
    letterSpacing: TextUnit? = null
): TextStyle = TextStyle.Default.copy(
    fontFamily = family,
    fontWeight = fontWeight,
    fontSize = fontSize,
    lineHeight = lineHeight,
    letterSpacing = letterSpacing ?: TextUnit.Unspecified
)

/**
 * Builds the app typography from bundled fonts.
 * - Poppins: oversized geometric display / headlines / titles
 * - Inter: readable body copy
 * - Space Mono: technical labels + mono (the brutalist texture)
 */
@Composable
fun rememberBrutalTypography(): BrutalTypography {
    val poppins = FontFamily(
        Font(Res.font.poppins_regular, FontWeight.Normal),
        Font(Res.font.poppins_medium, FontWeight.Medium),
        Font(Res.font.poppins_semibold, FontWeight.SemiBold),
        Font(Res.font.poppins_bold, FontWeight.Bold),
        Font(Res.font.poppins_extrabold, FontWeight.ExtraBold),
        Font(Res.font.poppins_black, FontWeight.Black),
    )
    val inter = FontFamily(
        Font(Res.font.inter_regular, FontWeight.Normal),
        Font(Res.font.inter_medium, FontWeight.Medium),
        Font(Res.font.inter_semibold, FontWeight.SemiBold),
    )
    val mono = FontFamily(
        Font(Res.font.space_mono_regular, FontWeight.Normal),
        Font(Res.font.space_mono_bold, FontWeight.Bold),
    )

    return BrutalTypography(
        displayLarge = brutal(poppins, 48.sp, FontWeight.Black, 50.sp, (-2).sp),
        displayMedium = brutal(poppins, 36.sp, FontWeight.Black, 38.sp, (-1.5).sp),
        displaySmall = brutal(poppins, 28.sp, FontWeight.ExtraBold, 30.sp, (-1).sp),
        headlineLarge = brutal(poppins, 24.sp, FontWeight.Bold, 28.sp, (-0.5).sp),
        headlineMedium = brutal(poppins, 20.sp, FontWeight.Bold, 24.sp),
        headlineSmall = brutal(poppins, 18.sp, FontWeight.SemiBold, 22.sp),
        titleLarge = brutal(poppins, 16.sp, FontWeight.SemiBold, 20.sp),
        titleMedium = brutal(poppins, 14.sp, FontWeight.SemiBold, 18.sp),
        titleSmall = brutal(poppins, 12.sp, FontWeight.Medium, 16.sp),
        bodyLarge = brutal(inter, 16.sp, FontWeight.Normal, 24.sp),
        bodyMedium = brutal(inter, 14.sp, FontWeight.Normal, 20.sp),
        bodySmall = brutal(inter, 12.sp, FontWeight.Normal, 16.sp),
        labelLarge = brutal(mono, 14.sp, FontWeight.Bold, 18.sp, 1.sp),
        labelMedium = brutal(mono, 12.sp, FontWeight.Bold, 16.sp, 1.sp),
        labelSmall = brutal(mono, 10.sp, FontWeight.Bold, 14.sp, 1.5.sp),
        mono = brutal(mono, 13.sp, FontWeight.Normal, 18.sp)
    )
}

// Fallback (system fonts) used only as the CompositionLocal default before the
// themed typography is provided.
private fun brutalFallback(
    fontSize: TextUnit, fontWeight: FontWeight, lineHeight: TextUnit, letterSpacing: TextUnit? = null
) = brutal(FontFamily.Default, fontSize, fontWeight, lineHeight, letterSpacing)

val BrutalTypographyDefaults = BrutalTypography(
    displayLarge = brutalFallback(48.sp, FontWeight.Black, 52.sp, (-2).sp),
    displayMedium = brutalFallback(36.sp, FontWeight.Black, 40.sp, (-1.5).sp),
    displaySmall = brutalFallback(28.sp, FontWeight.Bold, 32.sp, (-1).sp),
    headlineLarge = brutalFallback(24.sp, FontWeight.Bold, 28.sp, (-0.5).sp),
    headlineMedium = brutalFallback(20.sp, FontWeight.Bold, 24.sp),
    headlineSmall = brutalFallback(18.sp, FontWeight.SemiBold, 22.sp),
    titleLarge = brutalFallback(16.sp, FontWeight.Bold, 20.sp),
    titleMedium = brutalFallback(14.sp, FontWeight.SemiBold, 18.sp),
    titleSmall = brutalFallback(12.sp, FontWeight.Medium, 16.sp),
    bodyLarge = brutalFallback(16.sp, FontWeight.Normal, 24.sp),
    bodyMedium = brutalFallback(14.sp, FontWeight.Normal, 20.sp),
    bodySmall = brutalFallback(12.sp, FontWeight.Normal, 16.sp),
    labelLarge = brutalFallback(14.sp, FontWeight.Bold, 18.sp, 1.sp),
    labelMedium = brutalFallback(12.sp, FontWeight.Bold, 16.sp, 1.sp),
    labelSmall = brutalFallback(10.sp, FontWeight.Bold, 14.sp, 1.5.sp),
    mono = brutalFallback(13.sp, FontWeight.Normal, 18.sp)
)

val LocalBrutalTypography = staticCompositionLocalOf { BrutalTypographyDefaults }
