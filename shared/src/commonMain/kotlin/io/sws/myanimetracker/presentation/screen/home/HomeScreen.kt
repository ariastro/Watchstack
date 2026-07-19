package io.sws.myanimetracker.presentation.screen.home

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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.horizontalScroll
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import io.sws.myanimetracker.domain.model.Anime
import io.sws.myanimetracker.presentation.PreviewContainer
import io.sws.myanimetracker.presentation.PreviewData
import io.sws.myanimetracker.presentation.components.BrutalSearchBar
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
fun HomeScreen(
    modifier: Modifier = Modifier,
    onSearchClick: () -> Unit = {},
    onBrowse: (BrowseCategory) -> Unit = {},
    viewModel: HomeViewModel = koinViewModel<HomeViewModel>()
) {
    val uiState by viewModel.uiState.collectAsState()
    HomeContent(
        uiState = uiState,
        onIntent = viewModel::onIntent,
        onSearchClick = onSearchClick,
        onBrowse = onBrowse,
        modifier = modifier
    )
}

@Composable
private fun HomeContent(
    uiState: HomeUiState,
    onIntent: (HomeIntent) -> Unit,
    onSearchClick: () -> Unit,
    onBrowse: (BrowseCategory) -> Unit,
    modifier: Modifier = Modifier
) {
    val colors = LocalBrutalColors.current
    val dims = LocalBrutalDimensions.current
    val typo = LocalBrutalTypography.current

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(color = colors.background),
        contentPadding = PaddingValues(bottom = dims.spacingLg)
    ) {
        item {
            Column(modifier = Modifier.padding(horizontal = dims.paddingScreen)) {
                Spacer(modifier = Modifier.height(dims.spacingLg))
                Text(
                    text = "Anime Tracker",
                    style = typo.displaySmall,
                    color = colors.textPrimary
                )
                Spacer(modifier = Modifier.height(dims.spacingMd))
                BrutalSearchBar(
                    query = "",
                    onQueryChange = {},
                    onSearch = onSearchClick,
                    readOnly = true,
                    onClick = onSearchClick
                )
                Spacer(modifier = Modifier.height(dims.spacingLg))
            }
        }

        if (uiState.isLoading && uiState.topAnime.isEmpty()) {
            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(dims.spacingXxl),
                    contentAlignment = Alignment.Center
                ) { LoadingState() }
            }
        }

        if (uiState.error != null && uiState.topAnime.isEmpty()) {
            item {
                Box(modifier = Modifier.padding(horizontal = dims.paddingScreen)) {
                    ErrorState(message = uiState.error ?: "", onRetry = { onIntent(HomeIntent.Load) })
                }
            }
        }

        if (uiState.topAnime.isNotEmpty()) {
            item {
                HeroCarousel(
                    items = uiState.topAnime.take(5),
                    onItemClick = { onIntent(HomeIntent.AnimeClicked(it, uiState.topAnime.find { a -> a.malId == it })) },
                    sharedKey = { it.malId.toString() }
                )
                Spacer(modifier = Modifier.height(dims.spacingXl))
            }
        }

        item {
            Column(modifier = Modifier.padding(horizontal = dims.paddingScreen)) {
                Text(text = "Browse", style = typo.titleLarge, color = colors.textPrimary)
                Spacer(modifier = Modifier.height(dims.spacingMd))
                Row(
                    modifier = Modifier.fillMaxWidth().horizontalScroll(rememberScrollState()),
                    horizontalArrangement = Arrangement.spacedBy(dims.spacingSm)
                ) {
                    BrowseCategory.values().forEach { category ->
                        BrowseCategoryChip(
                            category = category,
                            onClick = { onBrowse(category) }
                        )
                    }
                }
            }
        }

        item { Spacer(modifier = Modifier.height(dims.spacingXl)) }

        if (uiState.topAnime.isNotEmpty()) {
            item {
                Column(modifier = Modifier.padding(horizontal = dims.paddingScreen)) {
                    SectionHeader(title = "Top Rated", modifier = Modifier.fillMaxWidth())
                    Spacer(modifier = Modifier.height(dims.spacingMd))
                    LazyRow(
                        horizontalArrangement = Arrangement.spacedBy(dims.gridSpacing)
                    ) {
                        items(
                            items = uiState.topAnime,
                            key = { it.malId }
                        ) { anime ->
                            PosterCard(
                                anime = anime,
                                onClick = { onIntent(HomeIntent.AnimeClicked(anime.malId, anime)) },
                                modifier = Modifier.width(130.dp),
                                sharedKey = anime.malId.toString()
                            )
                        }
                    }
                }
            }
        }

        item { Spacer(modifier = Modifier.height(dims.spacingXl)) }
    }
}

@Composable
private fun BrowseCategoryChip(category: BrowseCategory, onClick: () -> Unit) {
    val colors = LocalBrutalColors.current
    val dims = LocalBrutalDimensions.current
    val typo = LocalBrutalTypography.current
    Row(
        modifier = Modifier
            .brutalBlock(cornerRadius = dims.radiusMd, shadowElevation = dims.radiusXs)
            .clickable(onClick = onClick)
            .padding(horizontal = dims.spacingLg, vertical = dims.spacingMd),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(dims.spacingSm)
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
private fun HomeContentPreview() {
    PreviewContainer {
        HomeContent(
            uiState = HomeUiState(topAnime = PreviewData.animeList),
            onIntent = {},
            onSearchClick = {},
            onBrowse = {}
        )
    }
}
