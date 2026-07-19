package io.sws.myanimetracker.presentation.screen.detail

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import myanimetracker.shared.generated.resources.Res
import myanimetracker.shared.generated.resources.ic_arrow_back
import org.jetbrains.compose.resources.painterResource
import coil3.compose.AsyncImage
import io.sws.myanimetracker.domain.model.Anime
import io.sws.myanimetracker.domain.model.Character
import io.sws.myanimetracker.domain.model.WatchStatus
import io.sws.myanimetracker.presentation.PreviewContainer
import io.sws.myanimetracker.presentation.PreviewData
import io.sws.myanimetracker.presentation.animation.LocalAnimatedVisibilityScope
import io.sws.myanimetracker.presentation.animation.LocalSharedTransitionScope
import io.sws.myanimetracker.presentation.components.BrutalButton
import io.sws.myanimetracker.presentation.components.BrutalChip
import io.sws.myanimetracker.presentation.components.ErrorState
import io.sws.myanimetracker.presentation.components.LoadingState
import io.sws.myanimetracker.presentation.components.PosterCard
import io.sws.myanimetracker.presentation.components.ScoreBadge
import io.sws.myanimetracker.presentation.components.SectionHeader
import io.sws.myanimetracker.presentation.components.StatusPill
import io.sws.myanimetracker.presentation.theme.LocalBrutalColors
import io.sws.myanimetracker.presentation.theme.LocalBrutalDimensions
import io.sws.myanimetracker.presentation.theme.LocalBrutalTypography
import io.sws.myanimetracker.presentation.theme.brutalBlock
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun DetailScreen(
    malId: Int,
    initialAnime: Anime? = null,
    modifier: Modifier = Modifier,
    viewModel: AnimeDetailViewModel = koinViewModel<AnimeDetailViewModel>()
) {
    val uiState by viewModel.uiState.collectAsState()
    LaunchedEffect(malId) { viewModel.onIntent(DetailIntent.LoadAnime(malId, initialAnime)) }
    DetailContent(
        uiState = uiState,
        onIntent = viewModel::onIntent,
        malId = malId,
        modifier = modifier
    )
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

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(color = colors.background)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(bottom = 84.dp)
        ) {
            if (uiState.anime != null) {
                DetailTopBar(
                    title = uiState.anime!!.title,
                    onBack = { onIntent(DetailIntent.GoBack) }
                )
            }
            when {
                uiState.isLoading -> {
                    Spacer(modifier = Modifier.height(dims.spacingXxl))
                    LoadingState(modifier = Modifier.padding(horizontal = dims.paddingScreen))
                }
                uiState.error != null -> {
                    Spacer(modifier = Modifier.height(dims.spacingXxl))
                    ErrorState(
                        message = uiState.error ?: "",
                        onRetry = { onIntent(DetailIntent.LoadAnime(malId)) },
                        modifier = Modifier.padding(horizontal = dims.paddingScreen)
                    )
                }
                uiState.anime != null -> DetailAnimeView(
                    uiState = uiState,
                    onIntent = onIntent,
                    sharedKey = uiState.anime!!.malId.toString()
                )
            }
        }

        if (uiState.anime != null) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(color = colors.surface.copy(alpha = 0.96f))
                    .align(Alignment.BottomCenter)
                    .padding(horizontal = dims.paddingScreen, vertical = dims.spacingMd),
                horizontalArrangement = Arrangement.spacedBy(dims.spacingMd)
            ) {
                if (uiState.tracked != null) {
                    BrutalButton(
                        text = "EDIT",
                        onClick = { onIntent(DetailIntent.ShowEditDialog) },
                        modifier = Modifier.weight(1f),
                        backgroundColor = colors.primaryContainer,
                        contentColor = colors.onPrimaryContainer
                    )
                    BrutalButton(
                        text = "REMOVE",
                        onClick = { onIntent(DetailIntent.RemoveFromTracking) },
                        modifier = Modifier.weight(1f),
                        backgroundColor = colors.error
                    )
                } else {
                    BrutalButton(
                        text = "+ ADD TO LIST",
                        onClick = { onIntent(DetailIntent.AddToWatchlist("")) },
                        modifier = Modifier.weight(1f)
                    )
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
    uiState: DetailUiState,
    onIntent: (DetailIntent) -> Unit,
    sharedKey: String? = null
) {
    val anime = uiState.anime!!
    val colors = LocalBrutalColors.current
    val dims = LocalBrutalDimensions.current
    val typo = LocalBrutalTypography.current
    val sharedScope = LocalSharedTransitionScope.current
    val visibilityScope = LocalAnimatedVisibilityScope.current

    var bannerModifier = Modifier
        .fillMaxWidth()
        .height(280.dp)
        .padding(horizontal = dims.paddingScreen)
        .clip(shape = RoundedCornerShape(dims.radiusLg))
        .background(color = colors.surfaceVariant)
    if (sharedKey != null && sharedScope != null && visibilityScope != null) {
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
                        colors = listOf(Color.Transparent, Color.Black.copy(alpha = 0.75f)),
                        startY = 400f,
                        endY = 1000f
                    )
                )
        )
        anime.score?.let {
            ScoreBadge(
                score = it,
                modifier = Modifier.align(Alignment.TopEnd).padding(12.dp)
            )
        }
        Column(modifier = Modifier.align(Alignment.BottomStart).padding(16.dp)) {
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
                BrutalChip(
                    text = "AIRING",
                    color = colors.watchingColor,
                    contentColor = Color.White
                )
            }
        }

        if (anime.genres.isNotEmpty()) {
            Spacer(modifier = Modifier.height(dims.spacingSm))
            FlowRow(
                horizontalArrangement = Arrangement.spacedBy(dims.spacingXs),
                verticalArrangement = Arrangement.spacedBy(dims.spacingXs)
            ) {
                anime.genres.forEach {
                    BrutalChip(text = it, color = colors.secondary, contentColor = Color.White)
                }
            }
        }

        Spacer(modifier = Modifier.height(dims.spacingLg))
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .brutalBlock(cornerRadius = dims.radiusMd, shadowElevation = dims.radiusXs)
                .padding(dims.spacingLg)
        ) {
            Text(text = "SYNOPSIS", style = typo.labelLarge, color = colors.primary)
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
                Text(text = "RATED: $it", style = typo.labelSmall, color = colors.textSecondary)
            }
            anime.duration?.let {
                Text(text = "DURATION: $it", style = typo.labelSmall, color = colors.textSecondary)
            }
        }
    }

    if (uiState.tracked != null) {
        Spacer(modifier = Modifier.height(dims.spacingLg))
        TrackingCard(uiState = uiState)
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
            LazyRow(
                contentPadding = PaddingValues(0.dp),
                horizontalArrangement = Arrangement.spacedBy(dims.gridSpacing)
            ) {
                items(
                    items = uiState.recommendations,
                    key = { it.anime.malId }
                ) { rec ->
                    PosterCard(
                        anime = rec.anime,
                        onClick = { onIntent(DetailIntent.OpenRecommendation(rec.anime.malId, rec.anime)) },
                        modifier = Modifier.width(dims.posterWidth),
                        sharedKey = rec.anime.malId.toString()
                    )
                }
            }
        }
    }
}

@Composable
private fun DetailTopBar(
    title: String,
    onBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    val colors = LocalBrutalColors.current
    val dims = LocalBrutalDimensions.current
    val typo = LocalBrutalTypography.current
    Row(
        modifier = modifier
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
private fun TrackingCard(uiState: DetailUiState) {
    val tracked = uiState.tracked!!
    val anime = uiState.anime!!
    val colors = LocalBrutalColors.current
    val dims = LocalBrutalDimensions.current
    val typo = LocalBrutalTypography.current

    Column(
        modifier = Modifier
            .padding(horizontal = dims.paddingScreen)
            .brutalBlock(cornerRadius = dims.radiusMd, shadowElevation = dims.radiusXs)
            .padding(dims.spacingLg)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(text = "TRACKING", style = typo.titleMedium, color = colors.textPrimary)
            Spacer(modifier = Modifier.width(dims.spacingSm))
            StatusPill(status = tracked.status)
        }
        Spacer(modifier = Modifier.height(dims.spacingMd))
        DetailInfoRow(label = "Episodes", value = "${tracked.episodesWatched}/${anime.episodes ?: "?"}")
        Spacer(modifier = Modifier.height(dims.spacingSm))
        DetailInfoRow(label = "Your rating", value = tracked.userRating?.let { "\u2605 $it" } ?: "\u2014")
        Spacer(modifier = Modifier.height(dims.spacingSm))
        DetailInfoRow(label = "Where to watch", value = tracked.whereToWatch.ifBlank { "\u2014" })
    }
}

@Composable
private fun CharacterSection(
    characters: List<Character>,
    modifier: Modifier = Modifier
) {
    val colors = LocalBrutalColors.current
    val dims = LocalBrutalDimensions.current
    val typo = LocalBrutalTypography.current
    Column(modifier = modifier.fillMaxWidth()) {
        SectionHeader(title = "Characters & Staff", modifier = Modifier.fillMaxWidth())
        Spacer(modifier = Modifier.height(dims.spacingMd))
        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(dims.gridSpacing)
        ) {
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
        character.voiceActorName?.let {
            Spacer(modifier = Modifier.height(2.dp))
            Text(
                text = "VA: $it",
                style = typo.labelSmall,
                color = colors.textSecondary,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
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
