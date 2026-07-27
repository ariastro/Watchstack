package io.sws.watchstack.presentation.screen.browse

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import io.sws.watchstack.core.currentSeason
import io.sws.watchstack.core.seasonLabel
import io.sws.watchstack.core.seasonWindow
import io.sws.watchstack.presentation.PreviewContainer
import io.sws.watchstack.presentation.PreviewData
import io.sws.watchstack.presentation.components.BrutalChip
import io.sws.watchstack.presentation.components.EmptyState
import io.sws.watchstack.presentation.components.ErrorState
import io.sws.watchstack.presentation.components.LoadingState
import io.sws.watchstack.presentation.components.PosterCard
import io.sws.watchstack.presentation.theme.LocalBrutalColors
import io.sws.watchstack.presentation.theme.LocalBrutalDimensions
import io.sws.watchstack.presentation.theme.LocalBrutalTypography
import io.sws.watchstack.presentation.theme.glassSurface
import watchstack.shared.generated.resources.Res
import org.jetbrains.compose.resources.painterResource
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.parameter.parametersOf
import watchstack.shared.generated.resources.ic_arrow_left
import watchstack.shared.generated.resources.ic_search

@Composable
fun BrowseScreen(
    category: BrowseCategory,
    modifier: Modifier = Modifier,
    viewModel: BrowseViewModel = koinViewModel<BrowseViewModel>(
        key = category.name,
        parameters = { parametersOf(category) }
    )
) {
    val uiState by viewModel.uiState.collectAsState()
    BrowseContent(uiState = uiState, onIntent = viewModel::onIntent, modifier = modifier)
}

@Composable
private fun BrowseContent(
    uiState: BrowseUiState,
    onIntent: (BrowseIntent) -> Unit,
    modifier: Modifier = Modifier
) {
    val colors = LocalBrutalColors.current
    val dims = LocalBrutalDimensions.current
    val typo = LocalBrutalTypography.current
    val gridState = rememberLazyGridState()
    val statusTop = WindowInsets.statusBars.asPaddingValues().calculateTopPadding()

    LaunchedEffect(gridState, uiState.hasNext, uiState.isLoadingMore) {
        snapshotFlow {
            val info = gridState.layoutInfo
            val last = info.visibleItemsInfo.lastOrNull()?.index ?: 0
            last to info.totalItemsCount
        }.collect { (last, total) ->
            if (total > 0 && last >= total - 4 && uiState.hasNext && !uiState.isLoadingMore) {
                onIntent(BrowseIntent.LoadMore)
            }
        }
    }

    Column(modifier = modifier.fillMaxSize().background(color = colors.background)) {
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
                    .clickable { onIntent(BrowseIntent.GoBack) },
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    painter = painterResource(Res.drawable.ic_arrow_left),
                    contentDescription = "Back",
                    tint = colors.textPrimary
                )
            }
            Spacer(modifier = Modifier.width(dims.spacingMd))
            Column(modifier = Modifier.weight(1f)) {
                Text(text = "Browse", style = typo.labelMedium, color = colors.primary)
                Text(
                    text = uiState.category.label,
                    style = typo.headlineSmall,
                    color = colors.textPrimary,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }

        if (uiState.category == BrowseCategory.SEASON) {
            val options = seasonWindow(center = currentSeason())
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .horizontalScroll(rememberScrollState())
                    .padding(horizontal = dims.paddingScreen, vertical = dims.spacingSm),
                horizontalArrangement = Arrangement.spacedBy(dims.spacingSm)
            ) {
                options.forEach { season ->
                    val selected = season == uiState.selectedSeason
                    Box(
                        modifier = Modifier
                            .glassSurface(
                                cornerRadius = dims.radiusPill,
                                fill = if (selected) colors.primary else colors.glass,
                                borderColor = if (selected) colors.primary else colors.glassBorder
                            )
                            .clickable { onIntent(BrowseIntent.SeasonSelected(season)) }
                            .padding(horizontal = dims.spacingLg, vertical = dims.spacingSm)
                    ) {
                        Text(
                            text = seasonLabel(season.season, season.year),
                            style = typo.labelMedium,
                            color = if (selected) colors.onPrimary else colors.textPrimary
                        )
                    }
                }
            }
        }

        // Filters
        Column(modifier = Modifier.padding(horizontal = dims.paddingScreen)) {
            Text(text = "Sort", style = typo.labelMedium, color = colors.textSecondary)
            Spacer(modifier = Modifier.size(6.dp))
            Row(
                modifier = Modifier.horizontalScroll(rememberScrollState()),
                horizontalArrangement = Arrangement.spacedBy(dims.spacingSm)
            ) {
                BrowseSort.entries.forEach { sort ->
                    val selected = uiState.sort == sort
                    Box(modifier = Modifier.clickable { onIntent(BrowseIntent.SortSelected(sort)) }) {
                        BrutalChip(
                            text = sort.label,
                            color = if (selected) colors.primaryContainer else colors.surfaceVariant,
                            contentColor = if (selected) colors.onPrimaryContainer else colors.textPrimary
                        )
                    }
                }
            }
            Spacer(modifier = Modifier.size(10.dp))
            Text(text = "Type", style = typo.labelMedium, color = colors.textSecondary)
            Spacer(modifier = Modifier.size(6.dp))
            Row(
                modifier = Modifier.horizontalScroll(rememberScrollState()),
                horizontalArrangement = Arrangement.spacedBy(dims.spacingSm)
            ) {
                Box(modifier = Modifier.clickable { onIntent(BrowseIntent.TypeFilterSelected(null)) }) {
                    BrutalChip(
                        text = "All",
                        color = if (uiState.typeFilter == null) colors.primaryContainer else colors.surfaceVariant,
                        contentColor = if (uiState.typeFilter == null) colors.onPrimaryContainer else colors.textPrimary
                    )
                }
                uiState.availableTypes.forEach { type ->
                    val selected = uiState.typeFilter == type
                    Box(modifier = Modifier.clickable { onIntent(BrowseIntent.TypeFilterSelected(type)) }) {
                        BrutalChip(
                            text = type,
                            color = if (selected) colors.primaryContainer else colors.surfaceVariant,
                            contentColor = if (selected) colors.onPrimaryContainer else colors.textPrimary
                        )
                    }
                }
            }
            Spacer(modifier = Modifier.size(10.dp))
            Text(text = "Min score", style = typo.labelMedium, color = colors.textSecondary)
            Spacer(modifier = Modifier.size(6.dp))
            Row(
                modifier = Modifier.horizontalScroll(rememberScrollState()),
                horizontalArrangement = Arrangement.spacedBy(dims.spacingSm)
            ) {
                listOf(null to "Any", 7.0 to "7+", 8.0 to "8+", 9.0 to "9+").forEach { (score, label) ->
                    val selected = uiState.minScore == score
                    Box(modifier = Modifier.clickable { onIntent(BrowseIntent.MinScoreSelected(score)) }) {
                        BrutalChip(
                            text = label,
                            color = if (selected) colors.primaryContainer else colors.surfaceVariant,
                            contentColor = if (selected) colors.onPrimaryContainer else colors.textPrimary
                        )
                    }
                }
            }
            Spacer(modifier = Modifier.size(dims.spacingMd))
        }

        when {
            uiState.isLoading -> Box(
                modifier = Modifier.fillMaxSize().padding(dims.spacingXxl),
                contentAlignment = Alignment.Center
            ) { LoadingState() }

            uiState.error != null -> {
                val errorMessage = uiState.error
                Box(
                    modifier = Modifier.fillMaxSize().padding(horizontal = dims.paddingScreen),
                    contentAlignment = Alignment.Center
                ) {
                    ErrorState(message = errorMessage, onRetry = { onIntent(BrowseIntent.Load) })
                }
            }

            uiState.filteredAnime.isEmpty() -> Box(
                modifier = Modifier.fillMaxSize().padding(horizontal = dims.paddingScreen),
                contentAlignment = Alignment.Center
            ) {
                EmptyState(
                    message = "Nothing matches filters.",
                    icon = painterResource(Res.drawable.ic_search)
                )
            }

            else -> LazyVerticalGrid(
                columns = GridCells.Adaptive(minSize = 130.dp),
                state = gridState,
                modifier = Modifier.fillMaxSize(),
                horizontalArrangement = Arrangement.spacedBy(dims.gridSpacing),
                verticalArrangement = Arrangement.spacedBy(dims.gridSpacing),
                contentPadding = PaddingValues(
                    start = dims.paddingScreen,
                    end = dims.paddingScreen,
                    bottom = dims.spacingXxl
                )
            ) {
                items(
                    items = uiState.filteredAnime.distinctBy { it.malId },
                    key = { it.malId },
                    contentType = { "poster" }
                ) { anime ->
                    PosterCard(
                        anime = anime,
                        onClick = { onIntent(BrowseIntent.AnimeClicked(anime.malId, anime)) },
                        modifier = Modifier.fillMaxWidth()
                    )
                }
                if (uiState.isLoadingMore) {
                    item(span = { GridItemSpan(maxLineSpan) }) {
                        Box(
                            modifier = Modifier.fillMaxWidth().padding(16.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            CircularProgressIndicator(color = colors.primary)
                        }
                    }
                }
            }
        }
    }
}

@Preview
@Composable
private fun BrowseContentPreview() {
    PreviewContainer {
        BrowseContent(
            uiState = BrowseUiState(
                category = BrowseCategory.TOP,
                anime = PreviewData.animeList,
                filteredAnime = PreviewData.animeList,
                availableTypes = listOf("TV", "Movie")
            ),
            onIntent = {}
        )
    }
}
