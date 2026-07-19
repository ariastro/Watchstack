package io.sws.myanimetracker.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import io.sws.myanimetracker.presentation.theme.LocalBrutalColors
import io.sws.myanimetracker.presentation.theme.LocalBrutalDimensions
import io.sws.myanimetracker.presentation.theme.LocalBrutalTypography

@Composable
fun BrutalChip(
    text: String,
    modifier: Modifier = Modifier,
    color: Color = LocalBrutalColors.current.surfaceVariant,
    contentColor: Color = LocalBrutalColors.current.textPrimary
) {
    val dims = LocalBrutalDimensions.current
    Text(
        text = text.uppercase(),
        style = LocalBrutalTypography.current.labelSmall,
        color = contentColor,
        modifier = modifier
            .background(color = color, shape = RoundedCornerShape(dims.radiusPill))
            .border(
                width = dims.borderThin,
                color = LocalBrutalColors.current.border,
                shape = RoundedCornerShape(dims.radiusPill)
            )
            .padding(horizontal = 10.dp, vertical = 4.dp)
    )
}

@Composable
fun BrutalBadge(
    text: String,
    backgroundColor: Color,
    contentColor: Color,
    modifier: Modifier = Modifier
) {
    val dims = LocalBrutalDimensions.current
    Text(
        text = text.uppercase(),
        style = LocalBrutalTypography.current.labelSmall,
        color = contentColor,
        modifier = modifier
            .background(color = backgroundColor, shape = RoundedCornerShape(dims.radiusPill))
            .padding(horizontal = 10.dp, vertical = 4.dp)
    )
}
