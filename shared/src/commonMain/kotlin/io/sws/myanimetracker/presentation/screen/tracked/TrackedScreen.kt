package io.sws.myanimetracker.presentation.screen.tracked

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import io.sws.myanimetracker.domain.model.WatchStatus
import io.sws.myanimetracker.presentation.PreviewContainer
import io.sws.myanimetracker.presentation.PreviewData
import io.sws.myanimetracker.presentation.components.BrutalTabRow
import io.sws.myanimetracker.presentation.components.EmptyState
import io.sws.myanimetracker.presentation.components.PosterCard
import io.sws.myanimetracker.presentation.theme.LocalBrutalColors
import io.sws.myanimetracker.presentation.theme.LocalBrutalDimensions
import io.sws.myanimetracker.presentation.theme.LocalBrutalTypography
import myanimetracker.shared.generated.resources.Res
import myanimetracker.shared.generated.resources.ic_bookmark
import myanimetracker.shared.generated.resources.ic_search
import myanimetracker.shared.generated.resources.ic_star
import org.jetbrains.compose.resources.painterResource
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun TrackedScreen(
    modifier: Modifier = Modifier,
    viewModel: TrackedViewModel = koinViewModel<TrackedViewModel>()
) {
    val uiState by viewModel.uiState.collectAsState()
    TrackedContent(
        uiState = uiState,
        onIntent = viewModel::onIntent,
        modifier = modifier
    )
}

@Composable
private fun TrackedContent(
    uiState: TrackedUiState,
    onIntent: (TrackedIntent) -> Unit,
    modifier: Modifier = Modifier
) {
    val colors = LocalBrutalColors.current
    val dims = LocalBrutalDimensions.current
    val typo = LocalBrutalTypography.current

    val currentList = when (uiState.activeTab) {
        WatchStatus.WATCHLIST -> uiState.watchlist
        WatchStatus.WATCHING -> uiState.watching
        WatchStatus.WATCHED -> uiState.watched
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(color = colors.background)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = dims.paddingScreen)
        ) {
            Spacer(modifier = Modifier.height(dims.spacingLg))
            Text(
                text = "My List",
                style = typo.displaySmall,
                color = colors.textPrimary,
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(dims.spacingLg))
        }

        BrutalTabRow(
            selectedTab = uiState.activeTab,
            onTabSelected = { onIntent(TrackedIntent.TabSelected(it)) },
            counts = {
                when (it) {
                    WatchStatus.WATCHLIST -> uiState.watchlist.size
                    WatchStatus.WATCHING -> uiState.watching.size
                    WatchStatus.WATCHED -> uiState.watched.size
                }
            },
            modifier = Modifier.padding(horizontal = dims.paddingScreen)
        )

        Spacer(modifier = Modifier.height(dims.spacingLg))

        if (currentList.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = dims.paddingScreen),
                contentAlignment = Alignment.Center
            ) {
                val icon = when (uiState.activeTab) {
                    WatchStatus.WATCHLIST -> painterResource(Res.drawable.ic_bookmark)
                    WatchStatus.WATCHING -> painterResource(Res.drawable.ic_search)
                    WatchStatus.WATCHED -> painterResource(Res.drawable.ic_star)
                }
                val message = when (uiState.activeTab) {
                    WatchStatus.WATCHLIST -> "Your watchlist is empty.\nSearch to add anime!"
                    WatchStatus.WATCHING -> "Nothing in progress."
                    WatchStatus.WATCHED -> "No completed anime yet."
                }
                EmptyState(message = message, icon = icon)
            }
        } else {
            LazyVerticalGrid(
                columns = GridCells.Adaptive(minSize = 130.dp),
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = dims.paddingScreen),
                horizontalArrangement = Arrangement.spacedBy(dims.gridSpacing),
                verticalArrangement = Arrangement.spacedBy(dims.gridSpacing),
                contentPadding = PaddingValues(bottom = dims.spacingXxl)
            ) {
                items(
                    items = currentList,
                    key = { tracked -> tracked.anime.malId }
                ) { tracked ->
                    PosterCard(
                        anime = tracked.anime,
                        statusBadge = tracked.status,
                        onClick = { onIntent(TrackedIntent.AnimeClicked(tracked.anime.malId, tracked.anime)) },
                        sharedKey = tracked.anime.malId.toString()
                    )
                }
            }
        }
    }
}

@Preview
@Composable
private fun TrackedContentPreview() {
    PreviewContainer {
        TrackedContent(
            uiState = TrackedUiState(
                activeTab = WatchStatus.WATCHING,
                watching = PreviewData.trackedList
            ),
            onIntent = {}
        )
    }
}

@Preview
@Composable
private fun TrackedContentEmptyPreview() {
    PreviewContainer {
        TrackedContent(uiState = TrackedUiState(), onIntent = {})
    }
}
