package io.sws.watchstack.presentation.screen.detail

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
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
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import io.sws.watchstack.domain.model.Anime
import io.sws.watchstack.domain.model.Character
import io.sws.watchstack.domain.model.TrackedAnime
import io.sws.watchstack.presentation.PreviewContainer
import io.sws.watchstack.presentation.PreviewData
import io.sws.watchstack.presentation.animation.LocalAnimatedVisibilityScope
import io.sws.watchstack.presentation.animation.LocalSharedTransitionScope
import io.sws.watchstack.presentation.components.BrutalButton
import io.sws.watchstack.presentation.components.BrutalChip
import io.sws.watchstack.presentation.components.ErrorState
import io.sws.watchstack.presentation.components.LoadingState
import io.sws.watchstack.presentation.components.PosterCard
import io.sws.watchstack.presentation.components.ScoreBadge
import io.sws.watchstack.presentation.components.SectionHeader
import io.sws.watchstack.presentation.components.StatusPill
import io.sws.watchstack.presentation.theme.LocalBrutalColors
import io.sws.watchstack.presentation.theme.LocalBrutalDimensions
import io.sws.watchstack.presentation.theme.LocalBrutalTypography
import io.sws.watchstack.presentation.theme.brutalBlock
import io.sws.watchstack.presentation.theme.glassSurface
import watchstack.shared.generated.resources.Res
import org.jetbrains.compose.resources.painterResource
import org.koin.compose.viewmodel.koinViewModel
import watchstack.shared.generated.resources.ic_arrow_left

@Composable
fun DetailScreen(
    malId: Int,
    initialAnime: Anime? = null,
    modifier: Modifier = Modifier,
    viewModel: AnimeDetailViewModel = koinViewModel<AnimeDetailViewModel>(key = malId.toString())
) {
    val uiState by viewModel.uiState.collectAsState()
    var snackbar by remember { mutableStateOf<String?>(null) }
    LaunchedEffect(malId) { viewModel.onIntent(DetailIntent.LoadAnime(malId, initialAnime)) }
    LaunchedEffect(viewModel) {
        viewModel.effects.collect { effect ->
            when (effect) {
                is io.sws.watchstack.presentation.UiEffect.Snackbar -> snackbar = effect.message
            }
        }
    }
    Box(modifier = modifier.fillMaxSize()) {
        DetailContent(
            uiState = uiState,
            onIntent = viewModel::onIntent,
            malId = malId
        )
        io.sws.watchstack.presentation.components.AppSnackbarHost(
            message = snackbar,
            onDismiss = { snackbar = null },
            modifier = Modifier.align(Alignment.BottomCenter).padding(bottom = 100.dp)
        )
    }
}

@Composable
private fun DetailContent(
    uiState: DetailUiState,
    onIntent: (DetailIntent) -> Unit,
    malId: Int,
    modifier: Modifier = Modifier
) {
    val colors = LocalBrutalColors.current
    val dims = LocalBrutalDimensions.current
    val anime = uiState.anime

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(color = colors.background)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(
                    bottom = 100.dp +
                        WindowInsets.navigationBars.asPaddingValues().calculateBottomPadding()
                )
        ) {
            if (anime != null) {
                DetailTopBar(
                    title = anime.title,
                    onBack = { onIntent(DetailIntent.GoBack) }
                )
            }
            when {
                uiState.isLoading && anime == null -> {
                    Spacer(modifier = Modifier.height(dims.spacingXxl))
                    LoadingState(modifier = Modifier.padding(horizontal = dims.paddingScreen))
                }
                uiState.error != null && anime == null -> {
                    val errorMessage = uiState.error
                    Spacer(modifier = Modifier.height(dims.spacingXxl))
                    ErrorState(
                        message = errorMessage,
                        onRetry = { onIntent(DetailIntent.LoadAnime(malId)) },
                        modifier = Modifier.padding(horizontal = dims.paddingScreen)
                    )
                }
                anime != null -> DetailAnimeView(
                    anime = anime,
                    uiState = uiState,
                    onIntent = onIntent
                )
            }
        }

        if (anime != null) {
            // Continuous surface into system nav area (same bg as screen — no white strip).
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.BottomCenter)
                    .background(color = colors.background)
                    .border(
                        width = dims.borderThin,
                        color = colors.divider,
                        shape = RoundedCornerShape(topStart = dims.radiusLg, topEnd = dims.radiusLg)
                    )
                    .windowInsetsPadding(WindowInsets.navigationBars)
            ) {
                uiState.actionError?.let { actionError ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(colors.error.copy(alpha = 0.15f))
                            .clickable { onIntent(DetailIntent.DismissActionError) }
                            .padding(horizontal = dims.paddingScreen, vertical = dims.spacingSm),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = actionError,
                            style = LocalBrutalTypography.current.bodySmall,
                            color = colors.error,
                            modifier = Modifier.weight(1f)
                        )
                        Text(
                            text = "Dismiss",
                            style = LocalBrutalTypography.current.labelSmall,
                            color = colors.error
                        )
                    }
                }
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = dims.paddingScreen, vertical = dims.spacingMd),
                    horizontalArrangement = Arrangement.spacedBy(dims.spacingMd)
                ) {
                    if (uiState.tracked != null) {
                        BrutalButton(
                            text = "Edit",
                            onClick = { onIntent(DetailIntent.ShowEditDialog) },
                            modifier = Modifier.weight(1f),
                            backgroundColor = colors.primaryContainer,
                            contentColor = colors.onPrimaryContainer,
                            gradient = false,
                            enabled = !uiState.isSaving
                        )
                        BrutalButton(
                            text = "Remove",
                            onClick = { onIntent(DetailIntent.RemoveFromTracking) },
                            modifier = Modifier.weight(1f),
                            backgroundColor = colors.error,
                            contentColor = Color.White,
                            gradient = false,
                            enabled = !uiState.isSaving
                        )
                    } else {
                        BrutalButton(
                            text = if (uiState.isSaving) "Saving…" else "Add to list",
                            onClick = { onIntent(DetailIntent.ShowTrackDialog) },
                            modifier = Modifier.weight(1f),
                            enabled = !uiState.isSaving
                        )
                    }
                }
            }
        }
    }

    if (uiState.showTrackDialog) TrackDialog(uiState, onIntent)
    if (uiState.showEditDialog && uiState.tracked != null) EditDialog(uiState, onIntent)
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun DetailAnimeView(
    anime: Anime,
    uiState: DetailUiState,
    onIntent: (DetailIntent) -> Unit
) {
    val colors = LocalBrutalColors.current
    val dims = LocalBrutalDimensions.current
    val typo = LocalBrutalTypography.current
    val sharedScope = LocalSharedTransitionScope.current
    val visibilityScope = LocalAnimatedVisibilityScope.current
    val sharedKey = anime.malId.toString()

    var bannerModifier = Modifier
        .fillMaxWidth()
        .height(320.dp)
        .padding(horizontal = dims.paddingScreen)
        .clip(shape = RoundedCornerShape(dims.radiusXl))
        .background(color = colors.surfaceVariant)
    if (sharedScope != null && visibilityScope != null) {
        with(sharedScope) {
            bannerModifier = bannerModifier.sharedElement(
                sharedContentState = rememberSharedContentState(key = "poster-$sharedKey"),
                animatedVisibilityScope = visibilityScope
            )
        }
    }

    Box(modifier = bannerModifier) {
        if (!anime.imageUrl.isNullOrBlank()) {
            AsyncImage(
                model = anime.imageUrl,
                contentDescription = anime.title,
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
            )
        }
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    brush = Brush.verticalGradient(
                        colors = listOf(
                            Color.Black.copy(alpha = 0.15f),
                            Color.Transparent,
                            Color.Black.copy(alpha = 0.55f),
                            Color.Black.copy(alpha = 0.9f)
                        )
                    )
                )
        )
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    brush = Brush.horizontalGradient(
                        colors = listOf(
                            colors.primary.copy(alpha = 0.2f),
                            Color.Transparent,
                            colors.accent.copy(alpha = 0.15f)
                        )
                    )
                )
        )
        anime.score?.let {
            ScoreBadge(score = it, modifier = Modifier.align(Alignment.TopEnd).padding(16.dp))
        }
        Column(modifier = Modifier.align(Alignment.BottomStart).padding(20.dp)) {
            Text(
                text = anime.title,
                style = typo.headlineLarge,
                color = Color.White,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )
            anime.titleJapanese?.let {
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = it,
                    style = typo.bodySmall,
                    color = Color.White.copy(alpha = 0.8f),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
            Spacer(modifier = Modifier.height(10.dp))
            val uriHandler = LocalUriHandler.current
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                if (!anime.trailerUrl.isNullOrBlank() || !anime.trailerYoutubeId.isNullOrBlank()) {
                    BrutalButton(
                        text = "Trailer",
                        onClick = {
                            val url = anime.trailerUrl
                                ?: "https://www.youtube.com/watch?v=${anime.trailerYoutubeId}"
                            runCatching { uriHandler.openUri(url) }
                            onIntent(DetailIntent.OpenTrailer)
                        },
                        gradient = true
                    )
                }
                BrutalButton(
                    text = "Share",
                    onClick = { onIntent(DetailIntent.Share) },
                    backgroundColor = colors.surface.copy(alpha = 0.85f),
                    contentColor = colors.textPrimary,
                    gradient = false
                )
            }
        }
    }

    Spacer(modifier = Modifier.height(dims.spacingLg))
    Column(modifier = Modifier.padding(horizontal = dims.paddingScreen)) {
        FlowRow(
            horizontalArrangement = Arrangement.spacedBy(dims.spacingXs),
            verticalArrangement = Arrangement.spacedBy(dims.spacingXs)
        ) {
            anime.type?.let {
                BrutalChip(
                    text = it,
                    color = colors.primaryContainer,
                    contentColor = colors.onPrimaryContainer
                )
            }
            anime.episodes?.let {
                if (it > 0) BrutalChip(text = "$it episodes", color = colors.surfaceVariant)
            }
            anime.year?.let { BrutalChip(text = "$it", color = colors.surfaceVariant) }
            anime.status?.let { BrutalChip(text = it, color = colors.surfaceVariant) }
            if (anime.airing) {
                BrutalChip(text = "Airing", color = colors.watchingColor, contentColor = Color.White)
            }
        }

        if (anime.genres.isNotEmpty()) {
            Spacer(modifier = Modifier.height(dims.spacingSm))
            FlowRow(
                horizontalArrangement = Arrangement.spacedBy(dims.spacingXs),
                verticalArrangement = Arrangement.spacedBy(dims.spacingXs)
            ) {
                anime.genres.forEach {
                    BrutalChip(
                        text = it,
                        color = colors.secondary.copy(alpha = 0.2f),
                        contentColor = colors.secondary
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(dims.spacingLg))
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .brutalBlock(cornerRadius = dims.radiusLg, shadowElevation = dims.radiusXs)
                .padding(dims.spacingLg)
        ) {
            Text(text = "Synopsis", style = typo.labelLarge, color = colors.primary)
            Spacer(modifier = Modifier.height(dims.spacingSm))
            Text(
                text = anime.synopsis ?: "No synopsis available.",
                style = typo.bodyMedium,
                color = colors.textPrimary
            )
        }

        Spacer(modifier = Modifier.height(dims.spacingMd))
        Row(horizontalArrangement = Arrangement.spacedBy(dims.spacingLg)) {
            anime.rating?.let {
                Text(text = "Rated: $it", style = typo.labelSmall, color = colors.textSecondary)
            }
            anime.duration?.let {
                Text(text = "Duration: $it", style = typo.labelSmall, color = colors.textSecondary)
            }
        }
    }

    uiState.tracked?.let { tracked ->
        Spacer(modifier = Modifier.height(dims.spacingLg))
        TrackingCard(tracked = tracked, anime = anime)
    }

    if (uiState.characters.isNotEmpty()) {
        Spacer(modifier = Modifier.height(dims.spacingLg))
        CharacterSection(
            characters = uiState.characters,
            modifier = Modifier.padding(horizontal = dims.paddingScreen)
        )
    }

    if (uiState.recommendations.isNotEmpty()) {
        Spacer(modifier = Modifier.height(dims.spacingLg))
        Column(modifier = Modifier.padding(horizontal = dims.paddingScreen)) {
            SectionHeader(title = "Recommended", modifier = Modifier.fillMaxWidth())
            Spacer(modifier = Modifier.height(dims.spacingMd))
            LazyRow(horizontalArrangement = Arrangement.spacedBy(dims.gridSpacing)) {
                items(items = uiState.recommendations, key = { it.anime.malId }) { rec ->
                    PosterCard(
                        anime = rec.anime,
                        onClick = {
                            onIntent(DetailIntent.OpenRecommendation(rec.anime.malId, rec.anime))
                        },
                        modifier = Modifier.width(dims.posterWidth),
                        sharedKey = rec.anime.malId.toString()
                    )
                }
            }
        }
    }
}

@Composable
private fun DetailTopBar(title: String, onBack: () -> Unit, modifier: Modifier = Modifier) {
    val colors = LocalBrutalColors.current
    val dims = LocalBrutalDimensions.current
    val typo = LocalBrutalTypography.current
    val statusTop = WindowInsets.statusBars.asPaddingValues().calculateTopPadding()
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(top = statusTop)
            .padding(horizontal = dims.paddingScreen, vertical = dims.spacingMd),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(44.dp)
                .glassSurface(cornerRadius = dims.radiusPill)
                .clickable(onClick = onBack),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                painter = painterResource(Res.drawable.ic_arrow_left),
                contentDescription = "Back",
                tint = colors.textPrimary
            )
        }
        Spacer(modifier = Modifier.width(dims.spacingMd))
        Text(
            text = title,
            style = typo.titleMedium,
            color = colors.textPrimary,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier.weight(1f)
        )
    }
}

@Composable
private fun TrackingCard(tracked: TrackedAnime, anime: Anime) {
    val colors = LocalBrutalColors.current
    val dims = LocalBrutalDimensions.current
    val typo = LocalBrutalTypography.current

    Column(
        modifier = Modifier
            .padding(horizontal = dims.paddingScreen)
            .brutalBlock(cornerRadius = dims.radiusLg, shadowElevation = dims.radiusXs)
            .padding(dims.spacingLg)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(text = "Tracking", style = typo.titleMedium, color = colors.textPrimary)
            Spacer(modifier = Modifier.width(dims.spacingSm))
            StatusPill(status = tracked.status)
        }
        Spacer(modifier = Modifier.height(dims.spacingMd))
        DetailInfoRow(
            label = "Episodes",
            value = "${tracked.episodesWatched}/${anime.episodes ?: "?"}"
        )
        Spacer(modifier = Modifier.height(dims.spacingSm))
        DetailInfoRow(
            label = "Your rating",
            value = tracked.userRating?.let { "\u2605 $it" } ?: "\u2014"
        )
        Spacer(modifier = Modifier.height(dims.spacingSm))
        DetailInfoRow(
            label = "Where to watch",
            value = tracked.whereToWatch.ifBlank { "\u2014" }
        )
    }
}

@Composable
private fun CharacterSection(characters: List<Character>, modifier: Modifier = Modifier) {
    val dims = LocalBrutalDimensions.current
    Column(modifier = modifier.fillMaxWidth()) {
        SectionHeader(title = "Characters & Staff", modifier = Modifier.fillMaxWidth())
        Spacer(modifier = Modifier.height(dims.spacingMd))
        LazyRow(horizontalArrangement = Arrangement.spacedBy(dims.gridSpacing)) {
            items(items = characters, key = { it.malId }) { character ->
                CharacterCard(character = character)
            }
        }
    }
}

@Composable
private fun CharacterCard(character: Character) {
    val colors = LocalBrutalColors.current
    val dims = LocalBrutalDimensions.current
    val typo = LocalBrutalTypography.current
    Column(
        modifier = Modifier
            .width(110.dp)
            .brutalBlock(cornerRadius = dims.radiusMd, shadowElevation = dims.radiusXs)
            .padding(dims.spacingMd),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .size(72.dp)
                .clip(shape = CircleShape)
                .background(color = colors.surfaceVariant)
        ) {
            if (!character.imageUrl.isNullOrBlank()) {
                AsyncImage(
                    model = character.imageUrl,
                    contentDescription = character.name,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize()
                )
            }
        }
        Spacer(modifier = Modifier.height(dims.spacingSm))
        Text(
            text = character.name,
            style = typo.labelSmall,
            color = colors.textPrimary,
            maxLines = 2,
            overflow = TextOverflow.Ellipsis
        )
    }
}

@Composable
private fun DetailInfoRow(label: String, value: String) {
    val colors = LocalBrutalColors.current
    val typo = LocalBrutalTypography.current
    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
        Text(text = label, style = typo.labelSmall, color = colors.textSecondary)
        Text(text = value, style = typo.bodyMedium, color = colors.textPrimary)
    }
}

@Preview
@Composable
private fun DetailContentTrackedPreview() {
    PreviewContainer {
        DetailContent(
            uiState = DetailUiState(anime = PreviewData.anime, tracked = PreviewData.tracked),
            onIntent = {},
            malId = 1
        )
    }
}

@Preview
@Composable
private fun DetailContentUntrackedPreview() {
    PreviewContainer {
        DetailContent(
            uiState = DetailUiState(anime = PreviewData.anime),
            onIntent = {},
            malId = 1
        )
    }
}
