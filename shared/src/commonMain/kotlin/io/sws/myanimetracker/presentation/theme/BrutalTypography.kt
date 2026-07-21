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

private fun style(
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
        displayLarge = style(poppins, 44.sp, FontWeight.Bold, 50.sp, (-1).sp),
        displayMedium = style(poppins, 34.sp, FontWeight.Bold, 40.sp, (-0.5).sp),
        displaySmall = style(poppins, 28.sp, FontWeight.Bold, 34.sp, (-0.25).sp),
        headlineLarge = style(poppins, 24.sp, FontWeight.SemiBold, 30.sp),
        headlineMedium = style(poppins, 20.sp, FontWeight.SemiBold, 26.sp),
        headlineSmall = style(poppins, 18.sp, FontWeight.SemiBold, 24.sp),
        titleLarge = style(poppins, 16.sp, FontWeight.SemiBold, 22.sp),
        titleMedium = style(poppins, 14.sp, FontWeight.Medium, 20.sp),
        titleSmall = style(poppins, 12.sp, FontWeight.Medium, 16.sp),
        bodyLarge = style(inter, 16.sp, FontWeight.Normal, 24.sp),
        bodyMedium = style(inter, 14.sp, FontWeight.Normal, 20.sp),
        bodySmall = style(inter, 12.sp, FontWeight.Normal, 16.sp),
        labelLarge = style(poppins, 14.sp, FontWeight.SemiBold, 18.sp, 0.3.sp),
        labelMedium = style(poppins, 12.sp, FontWeight.SemiBold, 16.sp, 0.3.sp),
        labelSmall = style(poppins, 10.sp, FontWeight.SemiBold, 14.sp, 0.5.sp),
        mono = style(mono, 13.sp, FontWeight.Normal, 18.sp)
    )
}

private fun fallback(
    fontSize: TextUnit, fontWeight: FontWeight, lineHeight: TextUnit, letterSpacing: TextUnit? = null
) = style(FontFamily.Default, fontSize, fontWeight, lineHeight, letterSpacing)

val BrutalTypographyDefaults = BrutalTypography(
    displayLarge = fallback(44.sp, FontWeight.Bold, 50.sp, (-1).sp),
    displayMedium = fallback(34.sp, FontWeight.Bold, 40.sp, (-0.5).sp),
    displaySmall = fallback(28.sp, FontWeight.Bold, 34.sp),
    headlineLarge = fallback(24.sp, FontWeight.SemiBold, 30.sp),
    headlineMedium = fallback(20.sp, FontWeight.SemiBold, 26.sp),
    headlineSmall = fallback(18.sp, FontWeight.SemiBold, 24.sp),
    titleLarge = fallback(16.sp, FontWeight.SemiBold, 22.sp),
    titleMedium = fallback(14.sp, FontWeight.Medium, 20.sp),
    titleSmall = fallback(12.sp, FontWeight.Medium, 16.sp),
    bodyLarge = fallback(16.sp, FontWeight.Normal, 24.sp),
    bodyMedium = fallback(14.sp, FontWeight.Normal, 20.sp),
    bodySmall = fallback(12.sp, FontWeight.Normal, 16.sp),
    labelLarge = fallback(14.sp, FontWeight.SemiBold, 18.sp, 0.3.sp),
    labelMedium = fallback(12.sp, FontWeight.SemiBold, 16.sp, 0.3.sp),
    labelSmall = fallback(10.sp, FontWeight.SemiBold, 14.sp, 0.5.sp),
    mono = fallback(13.sp, FontWeight.Normal, 18.sp)
)

val LocalBrutalTypography = staticCompositionLocalOf { BrutalTypographyDefaults }
