package io.sws.watchstack.presentation.components

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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import io.sws.watchstack.presentation.PreviewContainer
import io.sws.watchstack.presentation.theme.LocalBrutalColors
import io.sws.watchstack.presentation.theme.LocalBrutalDimensions
import io.sws.watchstack.presentation.theme.LocalBrutalTypography

@Composable
fun BrutalButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    backgroundColor: Color = Color.Unspecified,
    contentColor: Color = Color.Unspecified,
    enabled: Boolean = true,
    gradient: Boolean = true
) {
    val colors = LocalBrutalColors.current
    val dims = LocalBrutalDimensions.current
    val useGradient = gradient && backgroundColor == Color.Unspecified
    val bg = if (backgroundColor == Color.Unspecified) colors.primary else backgroundColor
    val fg = if (contentColor == Color.Unspecified) {
        if (useGradient) Color.White else colors.onPrimary
    } else contentColor
    val shape = RoundedCornerShape(dims.radiusPill)
    val bgModifier = if (useGradient) {
        Modifier.background(
            brush = Brush.horizontalGradient(listOf(colors.primary, colors.accent)),
            shape = shape
        )
    } else {
        Modifier.background(color = if (enabled) bg else bg.copy(alpha = 0.5f), shape = shape)
    }
    Text(
        text = text,
        style = LocalBrutalTypography.current.labelLarge,
        color = if (enabled) fg else fg.copy(alpha = 0.5f),
        textAlign = TextAlign.Center,
        modifier = modifier
            .clip(shape = shape)
            .then(bgModifier)
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
        text = text,
        style = LocalBrutalTypography.current.labelMedium,
        color = c,
        modifier = modifier
            .clip(shape = shape)
            .border(width = dims.borderThin, color = c.copy(alpha = 0.5f), shape = shape)
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
            BrutalButton(text = "Confirm", onClick = {})
            BrutalButton(
                text = "Remove",
                onClick = {},
                backgroundColor = LocalBrutalColors.current.error,
                contentColor = Color.White,
                gradient = false
            )
            BrutalButton(text = "Disabled", onClick = {}, enabled = false)
            BrutalTextButton(text = "Back", onClick = {})
        }
    }
}
