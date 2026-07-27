package io.sws.myanimetracker.presentation.screen.browse

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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import io.sws.myanimetracker.core.currentSeason
import io.sws.myanimetracker.core.seasonLabel
import io.sws.myanimetracker.core.seasonWindow
import io.sws.myanimetracker.domain.model.Anime
import myanimetracker.shared.generated.resources.Res
import myanimetracker.shared.generated.resources.ic_arrow_back
import myanimetracker.shared.generated.resources.ic_search_off
import org.jetbrains.compose.resources.painterResource
import io.sws.myanimetracker.presentation.PreviewContainer
import io.sws.myanimetracker.presentation.PreviewData
import io.sws.myanimetracker.presentation.components.EmptyState
import io.sws.myanimetracker.presentation.components.ErrorState
import io.sws.myanimetracker.presentation.components.LoadingState
import io.sws.myanimetracker.presentation.components.PosterCard
import io.sws.myanimetracker.presentation.theme.LocalBrutalColors
import io.sws.myanimetracker.presentation.theme.LocalBrutalDimensions
import io.sws.myanimetracker.presentation.theme.LocalBrutalTypography
import io.sws.myanimetracker.presentation.theme.brutalBlock
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.parameter.parametersOf

@Composable
fun BrowseScreen(
    category: BrowseCategory,
    modifier: Modifier = Modifier,
    onBack: () -> Unit = {},
    viewModel: BrowseViewModel = koinViewModel<BrowseViewModel>(parameters = { parametersOf(category) })
) {
    val uiState by viewModel.uiState.collectAsState()
    BrowseContent(
        uiState = uiState,
        onIntent = viewModel::onIntent,
        onBack = onBack,
        modifier = modifier
    )
}

@Composable
private fun BrowseContent(
    uiState: BrowseUiState,
    onIntent: (BrowseIntent) -> Unit,
    onBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    val colors = LocalBrutalColors.current
    val dims = LocalBrutalDimensions.current
    val typo = LocalBrutalTypography.current

    Column(modifier = modifier.fillMaxSize().background(color = colors.background)) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(color = colors.background)
                .padding(horizontal = dims.paddingScreen, vertical = dims.spacingMd),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .clip(shape = CircleShape)
                    .background(color = colors.surface)
                    .clickable(onClick = onBack),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    painter = painterResource(Res.drawable.ic_arrow_back),
                    contentDescription = "Back",
                    tint = colors.textPrimary
                )
            }
            Spacer(modifier = Modifier.width(dims.spacingMd))
            Text(
                text = uiState.category.label,
                style = typo.titleLarge,
                color = colors.textPrimary,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.weight(1f)
            )
        }

        if (uiState.category == BrowseCategory.SEASON) {
            val options = seasonWindow(center = currentSeason())
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .horizontalScroll(rememberScrollState())
                    .padding(horizontal = dims.paddingScreen, vertical = dims.spacingMd),
                horizontalArrangement = Arrangement.spacedBy(dims.spacingSm)
            ) {
                options.forEach { season ->
                    val selected = season == uiState.selectedSeason
                    Box(
                        modifier = Modifier
                            .brutalBlock(
                                fill = if (selected) colors.primary else colors.surface,
                                cornerRadius = dims.radiusMd
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

        when {
            uiState.isLoading -> Box(
                modifier = Modifier.fillMaxSize().padding(dims.spacingXxl),
                contentAlignment = Alignment.Center
            ) { LoadingState() }
            uiState.error != null -> Box(
                modifier = Modifier.fillMaxSize().padding(horizontal = dims.paddingScreen),
                contentAlignment = Alignment.Center
            ) { ErrorState(message = uiState.error ?: "", onRetry = { onIntent(BrowseIntent.Load) }) }
            uiState.anime.isEmpty() -> Box(
                modifier = Modifier.fillMaxSize().padding(horizontal = dims.paddingScreen),
                contentAlignment = Alignment.Center
            ) { EmptyState(message = "Nothing here.", icon = painterResource(Res.drawable.ic_search_off)) }
            else -> LazyVerticalGrid(
                columns = GridCells.Adaptive(minSize = 130.dp),
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
                    items = uiState.anime,
                    key = { it.malId }
                ) { anime ->
                    PosterCard(
                        anime = anime,
                        onClick = { onIntent(BrowseIntent.AnimeClicked(anime.malId, anime)) },
                        modifier = Modifier.fillMaxWidth(),
                        sharedKey = anime.malId.toString()
                    )
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
            uiState = BrowseUiState(category = BrowseCategory.TOP, anime = PreviewData.animeList),
            onIntent = {},
            onBack = {}
        )
    }
}
