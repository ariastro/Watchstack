package io.sws.watchstack.presentation.screen.home

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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import io.sws.watchstack.core.currentSeason
import io.sws.watchstack.core.seasonLabel
import io.sws.watchstack.domain.model.TrackedAnime
import io.sws.watchstack.presentation.PreviewContainer
import io.sws.watchstack.presentation.PreviewData
import io.sws.watchstack.presentation.components.ErrorState
import io.sws.watchstack.presentation.components.HeroCarousel
import io.sws.watchstack.presentation.components.HomeSkeleton
import io.sws.watchstack.presentation.components.PosterCard
import io.sws.watchstack.presentation.components.PosterRail
import io.sws.watchstack.presentation.components.SectionHeader
import io.sws.watchstack.presentation.screen.browse.BrowseCategory
import io.sws.watchstack.presentation.theme.LocalBrutalColors
import io.sws.watchstack.presentation.theme.LocalBrutalDimensions
import io.sws.watchstack.presentation.theme.LocalBrutalTypography
import io.sws.watchstack.presentation.theme.glassSurface
import watchstack.shared.generated.resources.Res
import watchstack.shared.generated.resources.ic_settings
import watchstack.shared.generated.resources.ic_star
import org.jetbrains.compose.resources.painterResource
import org.koin.compose.viewmodel.koinViewModel
import watchstack.shared.generated.resources.ic_arrow_right

@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    viewModel: HomeViewModel = koinViewModel<HomeViewModel>()
) {
    val uiState by viewModel.uiState.collectAsState()
    HomeContent(uiState = uiState, onIntent = viewModel::onIntent, modifier = modifier)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun HomeContent(
    uiState: HomeUiState,
    onIntent: (HomeIntent) -> Unit,
    modifier: Modifier = Modifier
) {
    val colors = LocalBrutalColors.current
    val dims = LocalBrutalDimensions.current
    val typo = LocalBrutalTypography.current
    val season = currentSeason()
    val statusTop = WindowInsets.statusBars.asPaddingValues().calculateTopPadding()
    val hasContent = uiState.topAnime.isNotEmpty() ||
        uiState.airingNow.isNotEmpty() ||
        uiState.thisSeason.isNotEmpty() ||
        uiState.upcoming.isNotEmpty()

    PullToRefreshBox(
        isRefreshing = uiState.isRefreshing,
        onRefresh = { onIntent(HomeIntent.Refresh) },
        modifier = modifier.fillMaxSize().background(color = colors.background)
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(statusTop + 340.dp)
                    .background(
                        brush = Brush.verticalGradient(
                            colors = listOf(
                                colors.primary.copy(alpha = 0.28f),
                                colors.primary.copy(alpha = 0.16f),
                                colors.accent.copy(alpha = 0.08f),
                                colors.background
                            )
                        )
                    )
            )

            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .windowInsetsPadding(WindowInsets.statusBars),
                contentPadding = PaddingValues(bottom = dims.navBarHeight + dims.spacingXl)
            ) {
                item(key = "header", contentType = "header") {
                    Column(modifier = Modifier.padding(horizontal = dims.paddingScreen)) {
                        Spacer(modifier = Modifier.height(dims.spacingLg))
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column(modifier = Modifier.weight(1f)) {
                                Text(text = "Welcome back", style = typo.labelMedium, color = colors.primary)
                                Spacer(modifier = Modifier.height(dims.spacingXs))
                                Text(text = "Discover", style = typo.displaySmall, color = colors.textPrimary)
                            }
                            Box(
                                modifier = Modifier
                                    .size(44.dp)
                                    .glassSurface(cornerRadius = dims.radiusPill)
                                    .clickable { onIntent(HomeIntent.OpenStats) },
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    painter = painterResource(Res.drawable.ic_star),
                                    contentDescription = "Stats",
                                    tint = colors.primary,
                                    modifier = Modifier.size(20.dp)
                                )
                            }
                            Spacer(modifier = Modifier.width(dims.spacingSm))
                            Box(
                                modifier = Modifier
                                    .size(44.dp)
                                    .glassSurface(cornerRadius = dims.radiusPill)
                                    .clickable { onIntent(HomeIntent.OpenSettings) },
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    painter = painterResource(Res.drawable.ic_settings),
                                    contentDescription = "Settings",
                                    tint = colors.textPrimary,
                                    modifier = Modifier.size(22.dp)
                                )
                            }
                        }
                        Spacer(modifier = Modifier.height(dims.spacingXl))
                    }
                }

                if (uiState.isLoading && !hasContent) {
                    item(key = "skeleton", contentType = "skeleton") { HomeSkeleton() }
                }

                uiState.error?.let { errorMessage ->
                    if (!hasContent) {
                        item(key = "error", contentType = "error") {
                            Box(modifier = Modifier.padding(horizontal = dims.paddingScreen)) {
                                ErrorState(
                                    message = errorMessage,
                                    onRetry = { onIntent(HomeIntent.Load) }
                                )
                            }
                        }
                    }
                }

                if (uiState.continueWatching.isNotEmpty()) {
                    item(key = "continue", contentType = "rail") {
                        ContinueWatchingRail(
                            items = uiState.continueWatching,
                            onItemClick = { onIntent(HomeIntent.AnimeClicked(it.anime.malId, it.anime)) }
                        )
                        Spacer(modifier = Modifier.height(dims.spacingXl))
                    }
                }

                if (uiState.topAnime.isNotEmpty()) {
                    item(key = "hero", contentType = "hero") {
                        HeroCarousel(
                            items = uiState.topAnime,
                            onItemClick = {
                                onIntent(
                                    HomeIntent.AnimeClicked(
                                        it,
                                        uiState.topAnime.find { a -> a.malId == it }
                                    )
                                )
                            }
                        )
                        Spacer(modifier = Modifier.height(dims.spacingXl))
                    }
                }

                item(key = "categories", contentType = "categories") {
                    Column(modifier = Modifier.padding(horizontal = dims.paddingScreen)) {
                        Text(text = "Categories", style = typo.headlineSmall, color = colors.textPrimary)
                        Spacer(modifier = Modifier.height(dims.spacingMd))
                        Row(
                            modifier = Modifier.fillMaxWidth().horizontalScroll(rememberScrollState()),
                            horizontalArrangement = Arrangement.spacedBy(dims.spacingSm)
                        ) {
                            BrowseCategory.entries.forEach { category ->
                                CategoryChip(
                                    label = category.label,
                                    onClick = { onIntent(HomeIntent.OpenBrowse(category)) }
                                )
                            }
                        }
                    }
                    Spacer(modifier = Modifier.height(dims.spacingXl))
                }

                if (uiState.airingNow.isNotEmpty()) {
                    item(key = "rail-airing", contentType = "rail") {
                        PosterRail(
                            title = "Now Airing",
                            items = uiState.airingNow,
                            railId = "airing",
                            onItemClick = { onIntent(HomeIntent.AnimeClicked(it.malId, it)) },
                            onAction = { onIntent(HomeIntent.OpenBrowse(BrowseCategory.AIRING)) }
                        )
                        Spacer(modifier = Modifier.height(dims.spacingXl))
                    }
                }

                if (uiState.thisSeason.isNotEmpty()) {
                    item(key = "rail-season", contentType = "rail") {
                        PosterRail(
                            title = seasonLabel(season.season, season.year),
                            items = uiState.thisSeason,
                            railId = "season",
                            onItemClick = { onIntent(HomeIntent.AnimeClicked(it.malId, it)) },
                            onAction = { onIntent(HomeIntent.OpenBrowse(BrowseCategory.SEASON)) }
                        )
                        Spacer(modifier = Modifier.height(dims.spacingXl))
                    }
                }

                if (uiState.topAnime.isNotEmpty()) {
                    item(key = "rail-top", contentType = "rail") {
                        PosterRail(
                            title = "Top Rated",
                            items = uiState.topAnime,
                            railId = "top",
                            onItemClick = { onIntent(HomeIntent.AnimeClicked(it.malId, it)) },
                            onAction = { onIntent(HomeIntent.OpenBrowse(BrowseCategory.TOP)) }
                        )
                        Spacer(modifier = Modifier.height(dims.spacingXl))
                    }
                }

                if (uiState.upcoming.isNotEmpty()) {
                    item(key = "rail-upcoming", contentType = "rail") {
                        PosterRail(
                            title = "Upcoming",
                            items = uiState.upcoming,
                            railId = "upcoming",
                            onItemClick = { onIntent(HomeIntent.AnimeClicked(it.malId, it)) },
                            onAction = { onIntent(HomeIntent.OpenBrowse(BrowseCategory.UPCOMING)) }
                        )
                        Spacer(modifier = Modifier.height(dims.spacingXl))
                    }
                }
            }
        }
    }
}

@Composable
private fun ContinueWatchingRail(
    items: List<TrackedAnime>,
    onItemClick: (TrackedAnime) -> Unit
) {
    val dims = LocalBrutalDimensions.current
    val railItems = items.distinctBy { it.anime.malId }.take(12)
    Column(modifier = Modifier.fillMaxWidth()) {
        SectionHeader(
            title = "Continue watching",
            modifier = Modifier.padding(horizontal = dims.paddingScreen)
        )
        Spacer(modifier = Modifier.height(dims.spacingMd))
        LazyRow(
            contentPadding = PaddingValues(horizontal = dims.paddingScreen),
            horizontalArrangement = Arrangement.spacedBy(dims.gridSpacing)
        ) {
            items(
                items = railItems,
                key = { "continue-${it.anime.malId}" },
                contentType = { "poster" }
            ) { tracked ->
                PosterCard(
                    anime = tracked.anime,
                    statusBadge = tracked.status,
                    onClick = { onItemClick(tracked) },
                    modifier = Modifier.width(dims.posterWidth)
                )
            }
        }
    }
}

@Composable
private fun CategoryChip(label: String, onClick: () -> Unit) {
    val colors = LocalBrutalColors.current
    val dims = LocalBrutalDimensions.current
    val typo = LocalBrutalTypography.current
    Row(
        modifier = Modifier
            .glassSurface(cornerRadius = dims.radiusMd)
            .clickable(onClick = onClick)
            .padding(horizontal = dims.spacingLg, vertical = dims.spacingMd),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(dims.spacingSm)
    ) {
        Text(text = label, style = typo.titleMedium, color = colors.textPrimary)
        Icon(
            painter = painterResource(Res.drawable.ic_arrow_right),
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
            uiState = HomeUiState(
                topAnime = PreviewData.animeList,
                airingNow = PreviewData.animeList,
                continueWatching = PreviewData.trackedList
            ),
            onIntent = {}
        )
    }
}
