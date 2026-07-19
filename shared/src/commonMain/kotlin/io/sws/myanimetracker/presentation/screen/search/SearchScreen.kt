package io.sws.myanimetracker.presentation.screen.search

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import io.sws.myanimetracker.presentation.PreviewContainer
import io.sws.myanimetracker.presentation.PreviewData
import io.sws.myanimetracker.presentation.components.BrutalSearchBar
import io.sws.myanimetracker.presentation.components.EmptyState
import io.sws.myanimetracker.presentation.components.ErrorState
import io.sws.myanimetracker.presentation.components.HeroCarousel
import io.sws.myanimetracker.presentation.components.LoadingState
import io.sws.myanimetracker.presentation.components.PosterCard
import io.sws.myanimetracker.presentation.components.SectionHeader
import io.sws.myanimetracker.presentation.screen.browse.BrowseCategory
import io.sws.myanimetracker.presentation.theme.LocalBrutalColors
import io.sws.myanimetracker.presentation.theme.LocalBrutalDimensions
import io.sws.myanimetracker.presentation.theme.LocalBrutalTypography
import io.sws.myanimetracker.presentation.theme.brutalBlock
import myanimetracker.shared.generated.resources.Res
import myanimetracker.shared.generated.resources.ic_chevron_right
import org.jetbrains.compose.resources.painterResource
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun SearchScreen(
    modifier: Modifier = Modifier,
    viewModel: SearchViewModel = koinViewModel<SearchViewModel>()
) {
    val uiState by viewModel.uiState.collectAsState()
    SearchContent(
        uiState = uiState,
        onIntent = viewModel::onIntent,
        modifier = modifier
    )
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

    when {
        uiState.isLoading -> Box(
            modifier = modifier
                .fillMaxSize()
                .background(color = colors.background)
                .padding(horizontal = dims.paddingScreen)
        ) {
            LoadingState()
        }
        uiState.error != null -> Box(
            modifier = modifier
                .fillMaxSize()
                .background(color = colors.background)
                .padding(horizontal = dims.paddingScreen)
        ) {
            ErrorState(
                message = uiState.error ?: "",
                onRetry = { onIntent(SearchIntent.Search) }
            )
        }
        uiState.hasSearched && uiState.results.isEmpty() -> Box(
            modifier = modifier
                .fillMaxSize()
                .background(color = colors.background)
                .padding(horizontal = dims.paddingScreen)
        ) {
            EmptyState(message = "No anime found")
        }
        else -> {
            LazyVerticalGrid(
                columns = GridCells.Adaptive(minSize = 130.dp),
                modifier = modifier
                    .fillMaxSize()
                    .background(color = colors.background),
                horizontalArrangement = Arrangement.spacedBy(dims.gridSpacing),
                verticalArrangement = Arrangement.spacedBy(dims.gridSpacing),
                contentPadding = PaddingValues(
                    top = 0.dp,
                    start = dims.paddingScreen,
                    end = dims.paddingScreen,
                    bottom = dims.spacingXxl
                )
            ) {
                item(span = { GridItemSpan(maxLineSpan) }) {
                    Column {
                        Spacer(modifier = Modifier.height(dims.spacingLg))
                        Text(
                            text = "Discover",
                            style = typo.displaySmall,
                            color = colors.textPrimary,
                            modifier = Modifier.fillMaxWidth()
                        )
                        Spacer(modifier = Modifier.height(dims.spacingMd))
                        BrutalSearchBar(
                            query = uiState.query,
                            onQueryChange = { onIntent(SearchIntent.QueryChanged(it)) },
                            onSearch = { onIntent(SearchIntent.Search) }
                        )
                        Spacer(modifier = Modifier.height(dims.spacingLg))
                    }
                }

                item(span = { GridItemSpan(maxLineSpan) }) {
                    Column {
                        Text(
                            text = "Browse",
                            style = typo.titleLarge,
                            color = colors.textPrimary,
                            modifier = Modifier.fillMaxWidth()
                        )
                        Spacer(modifier = Modifier.height(dims.spacingMd))
                        BrowseCategory.values().forEach { category ->
                            BrowseRow(
                                category = category,
                                onClick = { onIntent(SearchIntent.BrowseClicked(category)) }
                            )
                            Spacer(modifier = Modifier.height(dims.spacingSm))
                        }
                        Spacer(modifier = Modifier.height(dims.spacingMd))
                    }
                }

                if (!uiState.hasSearched && uiState.results.isNotEmpty()) {
                    item(span = { GridItemSpan(maxLineSpan) }) {
                        HeroCarousel(
                            items = uiState.results.take(5),
                            onItemClick = { onIntent(SearchIntent.AnimeClicked(it, uiState.results.find { a -> a.malId == it })) },
                            sharedKey = { it.malId.toString() }
                        )
                        Spacer(modifier = Modifier.height(dims.spacingLg))
                    }
                    item(span = { GridItemSpan(maxLineSpan) }) {
                        SectionHeader(
                            title = "Top Rated",
                            modifier = Modifier.fillMaxWidth()
                        )
                        Spacer(modifier = Modifier.height(dims.spacingMd))
                    }
                }

                items(
                    items = uiState.results,
                    key = { anime -> anime.malId }
                ) { anime ->
                    PosterCard(
                        anime = anime,
                        onClick = { onIntent(SearchIntent.AnimeClicked(anime.malId, anime)) },
                        sharedKey = anime.malId.toString()
                    )
                }
            }
        }
    }
}

@Composable
private fun BrowseRow(category: BrowseCategory, onClick: () -> Unit) {
    val colors = LocalBrutalColors.current
    val dims = LocalBrutalDimensions.current
    val typo = LocalBrutalTypography.current
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .brutalBlock(cornerRadius = dims.radiusMd, shadowElevation = dims.radiusXs)
            .clickable(onClick = onClick)
            .padding(dims.spacingLg),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(text = category.label, style = typo.titleMedium, color = colors.textPrimary)
        Icon(
            painter = painterResource(Res.drawable.ic_chevron_right),
            contentDescription = null,
            tint = colors.primary
        )
    }
}

@Preview
@Composable
private fun SearchContentPreview() {
    PreviewContainer {
        SearchContent(
            uiState = SearchUiState(results = PreviewData.animeList),
            onIntent = {}
        )
    }
}

@Preview
@Composable
private fun SearchContentLoadingPreview() {
    PreviewContainer {
        SearchContent(
            uiState = SearchUiState(isLoading = true),
            onIntent = {}
        )
    }
}
