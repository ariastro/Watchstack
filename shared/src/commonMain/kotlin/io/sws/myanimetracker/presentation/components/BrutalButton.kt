package io.sws.myanimetracker.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import io.sws.myanimetracker.presentation.PreviewContainer
import io.sws.myanimetracker.presentation.theme.LocalBrutalColors
import io.sws.myanimetracker.presentation.theme.LocalBrutalDimensions
import io.sws.myanimetracker.presentation.theme.LocalBrutalTypography

@Composable
fun BrutalButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    backgroundColor: Color = LocalBrutalColors.current.primary,
    contentColor: Color = LocalBrutalColors.current.onPrimary,
    enabled: Boolean = true
) {
    val colors = LocalBrutalColors.current
    val dims = LocalBrutalDimensions.current
    val bg = if (backgroundColor == Color.Unspecified) colors.primary else backgroundColor
    val fg = if (contentColor == Color.Unspecified) colors.onPrimary else contentColor
    val shape = RoundedCornerShape(dims.radiusPill)
    Text(
        text = text.uppercase(),
        style = LocalBrutalTypography.current.labelLarge,
        color = if (enabled) fg else fg.copy(alpha = 0.5f),
        textAlign = TextAlign.Center,
        modifier = modifier
            .clip(shape = shape)
            .background(color = if (enabled) bg else bg.copy(alpha = 0.5f))
            .border(width = dims.borderThin, color = colors.border, shape = shape)
            .clickable(enabled = enabled, onClick = onClick)
            .padding(horizontal = dims.spacingXl, vertical = dims.spacingMd)
    )
}

@Composable
fun BrutalTextButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    color: Color = LocalBrutalColors.current.primary
) {
    val colors = LocalBrutalColors.current
    val dims = LocalBrutalDimensions.current
    val c = if (color == Color.Unspecified) colors.primary else color
    val shape = RoundedCornerShape(dims.radiusPill)
    Text(
        text = text.uppercase(),
        style = LocalBrutalTypography.current.labelMedium,
        color = c,
        modifier = modifier
            .clip(shape = shape)
            .border(width = dims.borderThin, color = c, shape = shape)
            .clickable(onClick = onClick)
            .padding(horizontal = dims.spacingLg, vertical = dims.spacingSm)
    )
}

@Preview
@Composable
private fun BrutalButtonPreview() {
    PreviewContainer {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            BrutalButton(text = "CONFIRM", onClick = {})
            BrutalButton(
                text = "REMOVE",
                onClick = {},
                backgroundColor = LocalBrutalColors.current.error
            )
            BrutalButton(text = "DISABLED", onClick = {}, enabled = false)
            BrutalTextButton(text = "\u2190 BACK", onClick = {})
        }
    }
}
