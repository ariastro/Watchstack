package io.sws.watchstack.presentation.theme

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.Dp

@Composable
fun Modifier.brutalShadow(
    elevation: Dp = LocalBrutalDimensions.current.radiusSm,
    cornerRadius: Dp = LocalBrutalDimensions.current.radiusMd,
    color: Color = LocalBrutalColors.current.shadow
): Modifier = this.shadow(
    elevation = elevation,
    shape = RoundedCornerShape(cornerRadius),
    ambientColor = color,
    spotColor = color
)

@Composable
fun Modifier.brutalBlock(
    fill: Color = LocalBrutalColors.current.surface,
    borderColor: Color = LocalBrutalColors.current.border,
    borderWidth: Dp = LocalBrutalDimensions.current.borderThin,
    cornerRadius: Dp = LocalBrutalDimensions.current.radiusMd,
    shadowElevation: Dp = LocalBrutalDimensions.current.radiusXs,
    shadowColor: Color = LocalBrutalColors.current.shadow
): Modifier {
    val shape: Shape = RoundedCornerShape(cornerRadius)
    return this
        .shadow(elevation = shadowElevation, shape = shape, ambientColor = shadowColor, spotColor = shadowColor)
        .clip(shape)
        .background(fill)
        .border(borderWidth, borderColor, shape)
}

@Composable
fun Modifier.glassSurface(
    cornerRadius: Dp = LocalBrutalDimensions.current.radiusLg,
    fill: Color = LocalBrutalColors.current.glass,
    borderColor: Color = LocalBrutalColors.current.glassBorder
): Modifier {
    val shape = RoundedCornerShape(cornerRadius)
    return this
        .clip(shape)
        .background(fill)
        .border(LocalBrutalDimensions.current.borderThin, borderColor, shape)
}
