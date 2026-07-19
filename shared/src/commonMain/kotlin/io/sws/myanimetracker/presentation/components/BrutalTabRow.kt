package io.sws.myanimetracker.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import io.sws.myanimetracker.domain.model.WatchStatus
import io.sws.myanimetracker.presentation.PreviewContainer
import io.sws.myanimetracker.presentation.theme.LocalBrutalColors
import io.sws.myanimetracker.presentation.theme.LocalBrutalDimensions
import io.sws.myanimetracker.presentation.theme.LocalBrutalTypography
import io.sws.myanimetracker.presentation.theme.brutalBlock

@Composable
fun BrutalTabRow(
    selectedTab: WatchStatus,
    onTabSelected: (WatchStatus) -> Unit,
    modifier: Modifier = Modifier,
    counts: (WatchStatus) -> Int = { 0 }
) {
    val colors = LocalBrutalColors.current
    val dims = LocalBrutalDimensions.current
    val typo = LocalBrutalTypography.current

    Row(
        modifier = modifier
            .fillMaxWidth()
            .brutalBlock(
                fill = colors.surface,
                borderWidth = dims.borderThin,
                cornerRadius = dims.radiusLg,
                shadowElevation = dims.radiusXs
            )
            .padding(dims.spacingXs),
        horizontalArrangement = Arrangement.spacedBy(dims.spacingXs)
    ) {
        WatchStatus.entries.forEach { status ->
            val isSelected = selectedTab == status
            val bgColor = when (status) {
                WatchStatus.WATCHLIST -> colors.watchlistColor
                WatchStatus.WATCHING -> colors.watchingColor
                WatchStatus.WATCHED -> colors.watchedColor
            }
            val shortLabel = when (status) {
                WatchStatus.WATCHLIST -> "PLAN"
                WatchStatus.WATCHING -> "WATCHING"
                WatchStatus.WATCHED -> "DONE"
            }
            val pill = RoundedCornerShape(dims.radiusMd)
            Column(
                modifier = Modifier
                    .weight(1f)
                    .clip(shape = pill)
                    .background(color = if (isSelected) bgColor else colors.surface)
                    .clickable { onTabSelected(status) }
                    .padding(vertical = dims.spacingMd, horizontal = dims.spacingXs),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "${counts(status)}",
                    style = typo.headlineMedium,
                    color = if (isSelected) colors.textInverse else colors.textPrimary
                )
                Text(
                    text = shortLabel,
                    style = typo.labelSmall,
                    color = if (isSelected) colors.textInverse else colors.textSecondary,
                    textAlign = TextAlign.Center,
                    maxLines = 1
                )
            }
        }
    }
}

@Preview
@Composable
private fun BrutalTabRowPreview() {
    PreviewContainer {
        BrutalTabRow(
            selectedTab = WatchStatus.WATCHING,
            onTabSelected = {},
            counts = { status ->
                when (status) {
                    WatchStatus.WATCHLIST -> 8
                    WatchStatus.WATCHING -> 3
                    WatchStatus.WATCHED -> 42
                }
            }
        )
    }
}
