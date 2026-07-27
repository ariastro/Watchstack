package io.sws.watchstack.presentation.screen.tracked

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
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.SwipeToDismissBox
import androidx.compose.material3.SwipeToDismissBoxValue
import androidx.compose.material3.Text
import androidx.compose.material3.rememberSwipeToDismissBoxState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import io.sws.watchstack.domain.model.TrackedAnime
import io.sws.watchstack.domain.model.WatchStatus
import io.sws.watchstack.presentation.PreviewContainer
import io.sws.watchstack.presentation.PreviewData
import io.sws.watchstack.presentation.UiEffect
import io.sws.watchstack.presentation.components.AppSnackbarHost
import io.sws.watchstack.presentation.components.BrutalChip
import io.sws.watchstack.presentation.components.BrutalSearchBar
import io.sws.watchstack.presentation.components.BrutalTabRow
import io.sws.watchstack.presentation.components.EmptyState
import io.sws.watchstack.presentation.components.StatusPill
import io.sws.watchstack.presentation.theme.LocalBrutalColors
import io.sws.watchstack.presentation.theme.LocalBrutalDimensions
import io.sws.watchstack.presentation.theme.LocalBrutalTypography
import io.sws.watchstack.presentation.theme.brutalBlock
import watchstack.shared.generated.resources.Res
import watchstack.shared.generated.resources.ic_bookmark
import watchstack.shared.generated.resources.ic_search
import watchstack.shared.generated.resources.ic_star
import org.jetbrains.compose.resources.painterResource
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun TrackedScreen(
    modifier: Modifier = Modifier,
    viewModel: TrackedViewModel = koinViewModel<TrackedViewModel>()
) {
    val uiState by viewModel.uiState.collectAsState()
    var snackbar by remember { mutableStateOf<UiEffect.Snackbar?>(null) }

    LaunchedEffect(viewModel) {
        viewModel.effects.collect { effect ->
            when (effect) {
                is UiEffect.Snackbar -> snackbar = effect
            }
        }
    }

    Box(modifier = modifier.fillMaxSize()) {
        TrackedContent(uiState = uiState, onIntent = viewModel::onIntent)
        AppSnackbarHost(
            message = snackbar?.message,
            actionLabel = snackbar?.actionLabel,
            onAction = {
                if (snackbar?.actionId == TRACKED_UNDO_ACTION) {
                    viewModel.onIntent(TrackedIntent.UndoRemove)
                }
            },
            onDismiss = { snackbar = null },
            modifier = Modifier.align(Alignment.BottomCenter).padding(bottom = 90.dp)
        )
    }
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
    val visible = uiState.visibleList
    val rawEmpty = uiState.rawList.isEmpty()
    val filteredEmpty = !rawEmpty && visible.isEmpty()
    val statusTop = WindowInsets.statusBars.asPaddingValues().calculateTopPadding()

    Box(modifier = modifier.fillMaxSize().background(color = colors.background)) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(statusTop + 200.dp)
                .background(
                    brush = Brush.verticalGradient(
                        colors = listOf(
                            colors.accent.copy(alpha = 0.22f),
                            colors.accent.copy(alpha = 0.12f),
                            colors.background
                        )
                    )
                )
        )

        Column(modifier = Modifier.fillMaxSize()) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = dims.paddingScreen)
            ) {
                Spacer(modifier = Modifier.height(statusTop + dims.spacingLg))
                Text(text = "Your library", style = typo.labelMedium, color = colors.primary)
                Spacer(modifier = Modifier.height(dims.spacingXs))
                Text(text = "My List", style = typo.displaySmall, color = colors.textPrimary)
                if (uiState.totalCount > 0) {
                    Spacer(modifier = Modifier.height(dims.spacingXs))
                    Text(
                        text = "${uiState.totalCount} titles · swipe → +1 · ← remove",
                        style = typo.bodySmall,
                        color = colors.textSecondary
                    )
                }
                Spacer(modifier = Modifier.height(dims.spacingLg))
                BrutalSearchBar(
                    query = uiState.query,
                    onQueryChange = { onIntent(TrackedIntent.QueryChanged(it)) },
                    onSearch = {},
                    onClear = { onIntent(TrackedIntent.QueryChanged("")) },
                    placeholder = "Search your list…"
                )
                Spacer(modifier = Modifier.height(dims.spacingMd))
                Row(
                    modifier = Modifier.horizontalScroll(rememberScrollState()),
                    horizontalArrangement = Arrangement.spacedBy(dims.spacingSm)
                ) {
                    TrackedSort.entries.forEach { sort ->
                        val selected = uiState.sort == sort
                        Box(modifier = Modifier.clickable { onIntent(TrackedIntent.SortSelected(sort)) }) {
                            BrutalChip(
                                text = sort.label,
                                color = if (selected) colors.primaryContainer else colors.surfaceVariant,
                                contentColor = if (selected) colors.onPrimaryContainer else colors.textPrimary
                            )
                        }
                    }
                }
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

            when {
                rawEmpty -> {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(horizontal = dims.paddingScreen),
                    ) {
                        val icon = when (uiState.activeTab) {
                            WatchStatus.WATCHLIST -> painterResource(Res.drawable.ic_bookmark)
                            WatchStatus.WATCHING -> painterResource(Res.drawable.ic_search)
                            WatchStatus.WATCHED -> painterResource(Res.drawable.ic_star)
                        }
                        val message = when (uiState.activeTab) {
                            WatchStatus.WATCHLIST -> "Plan list is empty.\nFind something to watch."
                            WatchStatus.WATCHING -> "Nothing in progress yet."
                            WatchStatus.WATCHED -> "No completed titles yet."
                        }
                        EmptyState(
                            message = message,
                            icon = icon,
                            actionLabel = "Search anime",
                            onAction = { onIntent(TrackedIntent.OpenSearch) }
                        )
                    }
                }
                filteredEmpty -> {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(horizontal = dims.paddingScreen),
                    ) {
                        EmptyState(
                            message = "No matches for “${uiState.query}”.",
                            actionLabel = "Clear search",
                            onAction = { onIntent(TrackedIntent.QueryChanged("")) }
                        )
                    }
                }
                else -> {
                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        contentPadding = PaddingValues(
                            start = dims.paddingScreen,
                            end = dims.paddingScreen,
                            bottom = dims.navBarHeight + dims.spacingXl
                        ),
                        verticalArrangement = Arrangement.spacedBy(dims.spacingMd)
                    ) {
                        items(
                            items = visible.distinctBy { it.anime.malId },
                            key = { it.anime.malId },
                            contentType = { "tracked-row" }
                        ) { tracked ->
                            SwipeTrackedItem(
                                tracked = tracked,
                                onOpen = {
                                    onIntent(
                                        TrackedIntent.AnimeClicked(
                                            tracked.anime.malId,
                                            tracked.anime
                                        )
                                    )
                                },
                                onIncrement = {
                                    onIntent(TrackedIntent.IncrementEpisode(tracked.anime.malId))
                                },
                                onStatus = {
                                    onIntent(TrackedIntent.ChangeStatus(tracked.anime.malId, it))
                                },
                                onRemove = {
                                    onIntent(TrackedIntent.RemoveAnime(tracked.anime.malId))
                                }
                            )
                        }
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun SwipeTrackedItem(
    tracked: TrackedAnime,
    onOpen: () -> Unit,
    onIncrement: () -> Unit,
    onStatus: (WatchStatus) -> Unit,
    onRemove: () -> Unit
) {
    val colors = LocalBrutalColors.current
    val typo = LocalBrutalTypography.current
    val total = tracked.anime.episodes
    val atCap = total != null && tracked.episodesWatched >= total
    val dismissState = rememberSwipeToDismissBoxState(
        confirmValueChange = { value ->
            when (value) {
                SwipeToDismissBoxValue.StartToEnd -> {
                    if (!atCap) onIncrement()
                    false
                }
                SwipeToDismissBoxValue.EndToStart -> {
                    onRemove()
                    true
                }
                SwipeToDismissBoxValue.Settled -> false
            }
        }
    )
    SwipeToDismissBox(
        state = dismissState,
        backgroundContent = {
            val target = dismissState.targetValue
            val isRemove = target == SwipeToDismissBoxValue.EndToStart
            val isPlus = target == SwipeToDismissBoxValue.StartToEnd
            val bg = when {
                isRemove -> colors.error
                isPlus -> colors.primary
                else -> colors.surfaceVariant
            }
            val label = when {
                isRemove -> "Remove"
                isPlus -> "+1 ep"
                else -> ""
            }
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .clip(RoundedCornerShape(LocalBrutalDimensions.current.radiusMd))
                    .background(bg)
                    .padding(horizontal = 20.dp),
                contentAlignment = when {
                    isRemove -> Alignment.CenterEnd
                    isPlus -> Alignment.CenterStart
                    else -> Alignment.Center
                }
            ) {
                Text(text = label, style = typo.labelLarge, color = Color.White)
            }
        },
        enableDismissFromStartToEnd = !atCap,
        enableDismissFromEndToStart = true
    ) {
        TrackedListItem(
            tracked = tracked,
            onOpen = onOpen,
            onIncrement = onIncrement,
            onStatus = onStatus,
            onRemove = onRemove
        )
    }
}

@Composable
private fun TrackedListItem(
    tracked: TrackedAnime,
    onOpen: () -> Unit,
    onIncrement: () -> Unit,
    onStatus: (WatchStatus) -> Unit,
    onRemove: () -> Unit
) {
    val colors = LocalBrutalColors.current
    val dims = LocalBrutalDimensions.current
    val typo = LocalBrutalTypography.current
    var expanded by remember { mutableStateOf(false) }
    val progress = tracked.progressFraction()
    val total = tracked.anime.episodes
    val atCap = total != null && tracked.episodesWatched >= total

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .brutalBlock(cornerRadius = dims.radiusMd)
            .padding(dims.spacingMd)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Box(
                modifier = Modifier
                    .size(width = 56.dp, height = 80.dp)
                    .clip(RoundedCornerShape(dims.radiusSm))
                    .background(colors.surfaceVariant)
                    .clickable(onClick = onOpen)
            ) {
                if (!tracked.anime.imageUrl.isNullOrBlank()) {
                    AsyncImage(
                        model = tracked.anime.imageUrl,
                        contentDescription = tracked.anime.title,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.fillMaxSize()
                    )
                }
            }
            Spacer(modifier = Modifier.size(dims.spacingMd))
            Column(
                modifier = Modifier
                    .weight(1f)
                    .clickable(onClick = onOpen)
            ) {
                Text(
                    text = tracked.anime.title,
                    style = typo.titleMedium,
                    color = colors.textPrimary,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
                Spacer(modifier = Modifier.height(4.dp))
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    StatusPill(status = tracked.status)
                    tracked.userRating?.let {
                        Text(
                            text = "★ $it",
                            style = typo.labelSmall,
                            color = colors.primary
                        )
                    }
                }
                Spacer(modifier = Modifier.height(8.dp))
                ProgressBar(progress = progress)
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = tracked.progressLabel(),
                    style = typo.bodySmall,
                    color = colors.textSecondary
                )
                if (tracked.whereToWatch.isNotBlank()) {
                    Text(
                        text = tracked.whereToWatch,
                        style = typo.labelSmall,
                        color = colors.textSecondary,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            }
            Spacer(modifier = Modifier.width(8.dp))
            IncrementButton(
                enabled = !atCap,
                onClick = onIncrement
            )
        }
        Spacer(modifier = Modifier.height(dims.spacingSm))
        Text(
            text = if (expanded) "Hide actions" else "More actions",
            style = typo.labelMedium,
            color = colors.primary,
            modifier = Modifier
                .clickable { expanded = !expanded }
                .padding(vertical = 4.dp)
        )
        if (expanded) {
            Spacer(modifier = Modifier.height(dims.spacingSm))
            Row(horizontalArrangement = Arrangement.spacedBy(dims.spacingSm)) {
                ActionChip("Plan", onClick = { onStatus(WatchStatus.WATCHLIST) })
                ActionChip("Watch", onClick = { onStatus(WatchStatus.WATCHING) })
                ActionChip("Done", onClick = { onStatus(WatchStatus.WATCHED) })
                ActionChip("Remove", onClick = onRemove, danger = true)
            }
        }
    }
}

@Composable
private fun ProgressBar(progress: Float) {
    val colors = LocalBrutalColors.current
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(6.dp)
            .clip(RoundedCornerShape(100.dp))
            .background(colors.surfaceVariant)
    ) {
        Box(
            modifier = Modifier
                .fillMaxHeight()
                .fillMaxWidth(progress.coerceIn(0.02f, 1f).takeIf { progress > 0f } ?: 0f)
                .clip(RoundedCornerShape(100.dp))
                .background(
                    brush = Brush.horizontalGradient(
                        listOf(colors.primary, colors.accent)
                    )
                )
        )
    }
}

@Composable
private fun IncrementButton(enabled: Boolean, onClick: () -> Unit) {
    val colors = LocalBrutalColors.current
    val typo = LocalBrutalTypography.current
    Box(
        modifier = Modifier
            .size(48.dp)
            .clip(CircleShape)
            .background(
                if (enabled) {
                    Brush.horizontalGradient(listOf(colors.primary, colors.accent))
                } else {
                    Brush.horizontalGradient(
                        listOf(
                            colors.surfaceVariant,
                            colors.surfaceVariant
                        )
                    )
                }
            )
            .clickable(enabled = enabled, onClick = onClick),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = "+1",
            style = typo.labelLarge,
            color = if (enabled) Color.White else colors.textSecondary
        )
    }
}

@Composable
private fun ActionChip(label: String, onClick: () -> Unit, danger: Boolean = false) {
    val colors = LocalBrutalColors.current
    val typo = LocalBrutalTypography.current
    Text(
        text = label,
        style = typo.labelSmall,
        color = if (danger) colors.onError else colors.onPrimaryContainer,
        modifier = Modifier
            .clip(RoundedCornerShape(100.dp))
            .background(if (danger) colors.error else colors.primaryContainer)
            .clickable(onClick = onClick)
            .padding(horizontal = 10.dp, vertical = 6.dp)
    )
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
