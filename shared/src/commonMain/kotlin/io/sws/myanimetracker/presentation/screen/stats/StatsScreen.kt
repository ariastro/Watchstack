package io.sws.myanimetracker.presentation.screen.stats

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import io.sws.myanimetracker.domain.model.LibraryStats
import io.sws.myanimetracker.presentation.PreviewContainer
import io.sws.myanimetracker.presentation.theme.LocalBrutalColors
import io.sws.myanimetracker.presentation.theme.LocalBrutalDimensions
import io.sws.myanimetracker.presentation.theme.LocalBrutalTypography
import io.sws.myanimetracker.presentation.theme.brutalBlock
import io.sws.myanimetracker.presentation.theme.glassSurface
import myanimetracker.shared.generated.resources.Res
import myanimetracker.shared.generated.resources.ic_arrow_back
import org.jetbrains.compose.resources.painterResource
import org.koin.compose.viewmodel.koinViewModel
import kotlin.math.roundToInt

@Composable
fun StatsScreen(
    modifier: Modifier = Modifier,
    viewModel: StatsViewModel = koinViewModel<StatsViewModel>()
) {
    val uiState by viewModel.uiState.collectAsState()
    StatsContent(uiState = uiState, onIntent = viewModel::onIntent, modifier = modifier)
}

@Composable
private fun StatsContent(
    uiState: StatsUiState,
    onIntent: (StatsIntent) -> Unit,
    modifier: Modifier = Modifier
) {
    val colors = LocalBrutalColors.current
    val dims = LocalBrutalDimensions.current
    val typo = LocalBrutalTypography.current
    val stats = uiState.stats
    val statusTop = WindowInsets.statusBars.asPaddingValues().calculateTopPadding()

    Box(modifier = modifier.fillMaxSize().background(colors.background)) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(statusTop + 180.dp)
                .background(
                    brush = Brush.verticalGradient(
                        listOf(
                            colors.accent.copy(alpha = 0.24f),
                            colors.accent.copy(alpha = 0.12f),
                            colors.background
                        )
                    )
                )
        )
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(bottom = dims.spacingXxl)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = statusTop)
                    .padding(horizontal = dims.paddingScreen, vertical = dims.spacingMd),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(44.dp)
                        .glassSurface(cornerRadius = dims.radiusPill)
                        .clickable { onIntent(StatsIntent.GoBack) },
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        painter = painterResource(Res.drawable.ic_arrow_back),
                        contentDescription = "Back",
                        tint = colors.textPrimary
                    )
                }
                Spacer(modifier = Modifier.width(dims.spacingMd))
                Column {
                    Text(text = "Your progress", style = typo.labelMedium, color = colors.primary)
                    Text(text = "Stats", style = typo.headlineSmall, color = colors.textPrimary)
                }
            }

            Column(modifier = Modifier.padding(horizontal = dims.paddingScreen)) {
                Row(horizontalArrangement = Arrangement.spacedBy(dims.spacingMd)) {
                    StatCard("Total", "${stats.total}", Modifier.weight(1f))
                    StatCard("Episodes", "${stats.episodesWatched}", Modifier.weight(1f))
                }
                Spacer(modifier = Modifier.height(dims.spacingMd))
                Row(horizontalArrangement = Arrangement.spacedBy(dims.spacingMd)) {
                    StatCard("Plan", "${stats.watchlist}", Modifier.weight(1f))
                    StatCard("Watching", "${stats.watching}", Modifier.weight(1f))
                    StatCard("Done", "${stats.watched}", Modifier.weight(1f))
                }
                Spacer(modifier = Modifier.height(dims.spacingMd))
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .brutalBlock(cornerRadius = dims.radiusLg)
                        .padding(dims.spacingLg)
                ) {
                    Text(text = "Average rating", style = typo.labelLarge, color = colors.primary)
                    Spacer(modifier = Modifier.height(dims.spacingSm))
                    Text(
                        text = stats.averageRating?.let { "${(it * 10).roundToInt() / 10.0} / 10" }
                            ?: "—",
                        style = typo.displaySmall,
                        color = colors.textPrimary
                    )
                }
                Spacer(modifier = Modifier.height(dims.spacingMd))
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .brutalBlock(cornerRadius = dims.radiusLg)
                        .padding(dims.spacingLg)
                ) {
                    Text(text = "Top genres", style = typo.labelLarge, color = colors.primary)
                    Spacer(modifier = Modifier.height(dims.spacingMd))
                    if (stats.topGenres.isEmpty()) {
                        Text(
                            text = "Track titles to build genre stats.",
                            style = typo.bodyMedium,
                            color = colors.textSecondary
                        )
                    } else {
                        stats.topGenres.forEach { (genre, count) ->
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 6.dp),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text(text = genre, style = typo.titleMedium, color = colors.textPrimary)
                                Text(text = "×$count", style = typo.labelMedium, color = colors.primary)
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun StatCard(label: String, value: String, modifier: Modifier = Modifier) {
    val colors = LocalBrutalColors.current
    val dims = LocalBrutalDimensions.current
    val typo = LocalBrutalTypography.current
    Column(
        modifier = modifier
            .brutalBlock(cornerRadius = dims.radiusLg)
            .padding(dims.spacingLg)
    ) {
        Text(text = label, style = typo.labelMedium, color = colors.textSecondary)
        Spacer(modifier = Modifier.height(4.dp))
        Text(text = value, style = typo.headlineLarge, color = colors.textPrimary)
    }
}

@Preview
@Composable
private fun StatsPreview() {
    PreviewContainer {
        StatsContent(
            uiState = StatsUiState(
                stats = LibraryStats(
                    total = 42,
                    watchlist = 8,
                    watching = 3,
                    watched = 31,
                    episodesWatched = 860,
                    averageRating = 8.4,
                    topGenres = listOf("Action" to 12, "Drama" to 9)
                )
            ),
            onIntent = {}
        )
    }
}
