package io.sws.watchstack.presentation.screen.search

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
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import io.sws.watchstack.presentation.PreviewContainer
import io.sws.watchstack.presentation.PreviewData
import io.sws.watchstack.presentation.components.BrutalChip
import io.sws.watchstack.presentation.components.BrutalSearchBar
import io.sws.watchstack.presentation.components.EmptyState
import io.sws.watchstack.presentation.components.ErrorState
import io.sws.watchstack.presentation.components.LoadingState
import io.sws.watchstack.presentation.components.PosterCard
import io.sws.watchstack.presentation.theme.LocalBrutalColors
import io.sws.watchstack.presentation.theme.LocalBrutalDimensions
import io.sws.watchstack.presentation.theme.LocalBrutalTypography
import watchstack.shared.generated.resources.Res
import watchstack.shared.generated.resources.ic_search
import org.jetbrains.compose.resources.painterResource
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun SearchScreen(
    modifier: Modifier = Modifier,
    viewModel: SearchViewModel = koinViewModel<SearchViewModel>()
) {
    val uiState by viewModel.uiState.collectAsState()
    SearchContent(uiState = uiState, onIntent = viewModel::onIntent, modifier = modifier)
}

@Composable
private fun SearchContent(
    uiState: SearchUiState,
    onIntent: (SearchIntent) -> Unit,
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
                onIntent(SearchIntent.LoadMore)
            }
        }
    }

    Box(modifier = modifier.fillMaxSize().background(color = colors.background)) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(statusTop + 220.dp)
                .background(
                    brush = Brush.verticalGradient(
                        colors = listOf(
                            colors.secondary.copy(alpha = 0.22f),
                            colors.secondary.copy(alpha = 0.12f),
                            colors.background
                        )
                    )
                )
        )

        Column(modifier = Modifier.fillMaxSize()) {
            Column(modifier = Modifier.padding(horizontal = dims.paddingScreen)) {
                Spacer(modifier = Modifier.height(statusTop + dims.spacingLg))
                Text(text = "Find titles", style = typo.labelMedium, color = colors.primary)
                Spacer(modifier = Modifier.height(dims.spacingXs))
                Text(text = "Search", style = typo.displaySmall, color = colors.textPrimary)
                Spacer(modifier = Modifier.height(dims.spacingLg))
                BrutalSearchBar(
                    query = uiState.query,
                    onQueryChange = { onIntent(SearchIntent.QueryChanged(it)) },
                    onSearch = { onIntent(SearchIntent.Search) },
                    onClear = { onIntent(SearchIntent.Clear) },
                    placeholder = "Type a title…"
                )
                if (uiState.recentQueries.isNotEmpty() && !uiState.hasSearched) {
                    Spacer(modifier = Modifier.height(dims.spacingMd))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(text = "Recent", style = typo.titleMedium, color = colors.textPrimary)
                        Text(
                            text = "Clear",
                            style = typo.labelMedium,
                            color = colors.primary,
                            modifier = Modifier.clickable { onIntent(SearchIntent.ClearHistory) }
                        )
                    }
                    Spacer(modifier = Modifier.height(dims.spacingSm))
                    Row(
                        modifier = Modifier.horizontalScroll(rememberScrollState()),
                        horizontalArrangement = Arrangement.spacedBy(dims.spacingSm)
                    ) {
                        uiState.recentQueries.forEach { q ->
                            Box(modifier = Modifier.clickable { onIntent(SearchIntent.RecentClicked(q)) }) {
                                BrutalChip(
                                    text = q,
                                    color = colors.primaryContainer,
                                    contentColor = colors.onPrimaryContainer
                                )
                            }
                        }
                    }
                }
                Spacer(modifier = Modifier.height(dims.spacingLg))
            }

            when {
                uiState.isLoading -> Box(
                    modifier = Modifier.fillMaxSize().padding(horizontal = dims.paddingScreen),
                    contentAlignment = Alignment.TopCenter
                ) { LoadingState() }

                uiState.error != null -> {
                    val errorMessage = uiState.error
                    Box(
                        modifier = Modifier.fillMaxSize().padding(horizontal = dims.paddingScreen),
                        contentAlignment = Alignment.TopCenter
                    ) {
                        ErrorState(message = errorMessage, onRetry = { onIntent(SearchIntent.Search) })
                    }
                }

                !uiState.hasSearched -> Box(
                    modifier = Modifier.fillMaxSize().padding(horizontal = dims.paddingScreen),
                    contentAlignment = Alignment.TopCenter
                ) {
                    EmptyState(
                        message = "Search for anime by title.\nResults appear as you type.",
                        icon = painterResource(Res.drawable.ic_search)
                    )
                }

                uiState.results.isEmpty() -> Box(
                    modifier = Modifier.fillMaxSize().padding(horizontal = dims.paddingScreen),
                    contentAlignment = Alignment.TopCenter
                ) {
                    EmptyState(message = "No results for \"${uiState.query}\"")
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
                        bottom = dims.navBarHeight + dims.spacingXl
                    )
                ) {
                    item(span = { GridItemSpan(maxLineSpan) }) {
                        Text(
                            text = "${uiState.results.size} results",
                            style = typo.titleMedium,
                            color = colors.textSecondary,
                            modifier = Modifier.padding(bottom = dims.spacingSm)
                        )
                    }
                    items(
                        items = uiState.results.distinctBy { it.malId },
                        key = { it.malId },
                        contentType = { "anime-row" }
                    ) { anime ->
                        PosterCard(
                            anime = anime,
                            onClick = { onIntent(SearchIntent.AnimeClicked(anime.malId, anime)) },
                            sharedKey = anime.malId.toString()
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
}

@Preview
@Composable
private fun SearchContentPreview() {
    PreviewContainer {
        SearchContent(
            uiState = SearchUiState(
                results = PreviewData.animeList,
                hasSearched = true,
                query = "naruto",
                recentQueries = listOf("naruto", "one piece")
            ),
            onIntent = {}
        )
    }
}
