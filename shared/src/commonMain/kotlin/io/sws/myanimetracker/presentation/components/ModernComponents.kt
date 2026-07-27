package io.sws.myanimetracker.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import org.jetbrains.compose.resources.painterResource
import coil3.compose.AsyncImage
import io.sws.myanimetracker.domain.model.Anime
import myanimetracker.shared.generated.resources.Res
import myanimetracker.shared.generated.resources.ic_bookmark
import myanimetracker.shared.generated.resources.ic_home
import myanimetracker.shared.generated.resources.ic_search
import io.sws.myanimetracker.domain.model.WatchStatus
import io.sws.myanimetracker.presentation.PreviewContainer
import io.sws.myanimetracker.presentation.PreviewData
import io.sws.myanimetracker.presentation.animation.LocalAnimatedVisibilityScope
import io.sws.myanimetracker.presentation.animation.LocalSharedTransitionScope
import io.sws.myanimetracker.presentation.theme.LocalBrutalColors
import io.sws.myanimetracker.presentation.theme.LocalBrutalDimensions
import io.sws.myanimetracker.presentation.theme.LocalBrutalTypography

// ── Score badge (overlay on poster) ───────────────────────────────────────
@Composable
fun ScoreBadge(score: Double, modifier: Modifier = Modifier) {
    val colors = LocalBrutalColors.current
    val dims = LocalBrutalDimensions.current
    val typo = LocalBrutalTypography.current
    Box(
        modifier = modifier
            .clip(shape = RoundedCornerShape(dims.radiusPill))
            .background(color = colors.primaryContainer)
            .padding(horizontal = 8.dp, vertical = 4.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(text = "\u2605 $score", style = typo.labelSmall, color = colors.onPrimaryContainer)
    }
}

// ── Status pill (tracked list) ────────────────────────────────────────────
@Composable
fun StatusPill(status: WatchStatus, modifier: Modifier = Modifier) {
    val colors = LocalBrutalColors.current
    val dims = LocalBrutalDimensions.current
    val statusColor = when (status) {
        WatchStatus.WATCHLIST -> colors.watchlistColor
        WatchStatus.WATCHING -> colors.watchingColor
        WatchStatus.WATCHED -> colors.watchedColor
    }
    Box(
        modifier = modifier
            .clip(shape = RoundedCornerShape(dims.radiusPill))
            .background(color = statusColor)
            .padding(horizontal = 10.dp, vertical = 4.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = status.name.lowercase(),
            style = LocalBrutalTypography.current.labelSmall,
            color = Color.White
        )
    }
}

// ── Section header with optional "see all" ────────────────────────────────
@Composable
fun SectionHeader(
    title: String,
    modifier: Modifier = Modifier,
    actionLabel: String? = null,
    onAction: (() -> Unit)? = null
) {
    val colors = LocalBrutalColors.current
    val dims = LocalBrutalDimensions.current
    val typo = LocalBrutalTypography.current
    Row(
        modifier = modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = if (actionLabel != null) Arrangement.SpaceBetween else Arrangement.Start
    ) {
        Text(text = title, style = typo.titleLarge, color = colors.textPrimary)
        if (actionLabel != null && onAction != null) {
            Box(
                modifier = Modifier
                    .clickable(onClick = onAction)
                    .clip(shape = RoundedCornerShape(dims.radiusPill))
                    .background(color = colors.primaryContainer)
                    .padding(horizontal = 12.dp, vertical = 6.dp)
            ) {
                Text(text = actionLabel, style = typo.labelMedium, color = colors.onPrimaryContainer)
            }
        }
    }
}

// ── Poster grid card (for grid layouts) ───────────────────────────────────
@Composable
fun PosterCard(
    anime: Anime,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    statusBadge: WatchStatus? = null,
    sharedKey: String? = null
) {
    val colors = LocalBrutalColors.current
    val dims = LocalBrutalDimensions.current
    val typo = LocalBrutalTypography.current
    val sharedScope = LocalSharedTransitionScope.current
    val visibilityScope = LocalAnimatedVisibilityScope.current

    Column(
        modifier = modifier
            .fillMaxWidth()
            .clip(shape = RoundedCornerShape(dims.radiusMd))
            .clickable(onClick = onClick)
    ) {
        var posterModifier = Modifier
            .fillMaxWidth()
            .height(dims.posterHeight)
            .clip(shape = RoundedCornerShape(dims.radiusMd))
            .background(color = colors.surfaceVariant)
        if (sharedKey != null && sharedScope != null && visibilityScope != null) {
            with(sharedScope) {
                    posterModifier = posterModifier.sharedElement(
                        sharedContentState = rememberSharedContentState(key = "poster-$sharedKey"),
                        animatedVisibilityScope = visibilityScope
                    )
            }
        }
        Box(modifier = posterModifier) {
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
                            colors = listOf(Color.Transparent, Color.Black.copy(alpha = 0.55f)),
                            startY = 450f,
                            endY = 1000f
                        )
                    )
            )
            anime.score?.let {
                ScoreBadge(
                    score = it,
                    modifier = Modifier.align(Alignment.TopStart).padding(8.dp)
                )
            }
            statusBadge?.let {
                StatusPill(
                    status = it,
                    modifier = Modifier.align(Alignment.TopEnd).padding(8.dp)
                )
            }
            Text(
                text = anime.title,
                style = typo.titleSmall,
                color = Color.White,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.align(Alignment.BottomStart).padding(10.dp)
            )
        }
    }
}

// ── Hero carousel (featured top anime) ─────────────────────────────────────
@Composable
fun HeroCarousel(
    items: List<Anime>,
    onItemClick: (Int) -> Unit,
    modifier: Modifier = Modifier,
    sharedKey: (Anime) -> String? = { null }
) {
    val colors = LocalBrutalColors.current
    val dims = LocalBrutalDimensions.current
    val typo = LocalBrutalTypography.current
    val sharedScope = LocalSharedTransitionScope.current
    val visibilityScope = LocalAnimatedVisibilityScope.current
    val pagerState = rememberPagerState(initialPage = 0, pageCount = { items.size })

    Column(modifier = modifier.fillMaxWidth()) {
        HorizontalPager(
            state = pagerState,
            contentPadding = PaddingValues(horizontal = dims.paddingScreen),
            pageSpacing = dims.gridSpacing
        ) { page ->
            val anime = items[page]
            val key = sharedKey(anime)
            var heroModifier = Modifier
                .fillMaxWidth()
                .height(220.dp)
                .clip(shape = RoundedCornerShape(dims.radiusLg))
                .background(color = colors.surfaceVariant)
                .clickable { onItemClick(anime.malId) }
            if (key != null && sharedScope != null && visibilityScope != null) {
                with(sharedScope) {
                    heroModifier = heroModifier.sharedElement(
                        sharedContentState = rememberSharedContentState(key = "poster-$key"),
                        animatedVisibilityScope = visibilityScope
                    )
                }
            }
            Box(modifier = heroModifier) {
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
                                colors = listOf(Color.Transparent, Color.Black.copy(alpha = 0.7f)),
                                startY = 400f,
                                endY = 1000f
                            )
                        )
                )
                anime.score?.let {
                    ScoreBadge(
                        score = it,
                        modifier = Modifier.align(Alignment.TopStart).padding(12.dp)
                    )
                }
                Column(modifier = Modifier.align(Alignment.BottomStart).padding(16.dp)) {
                    Text(text = "FEATURED", style = typo.labelMedium, color = colors.primary)
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = anime.title,
                        style = typo.headlineLarge,
                        color = Color.White,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            }
        }
        val currentPage by remember { derivedStateOf { pagerState.currentPage } }
        val pageOffset by remember { derivedStateOf { pagerState.currentPageOffsetFraction } }
        Row(
            modifier = Modifier.fillMaxWidth().padding(top = 10.dp),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            repeat(items.size) { index ->
                val isSelected = index == currentPage
                val targetScale = when (index) {
                    currentPage -> 1f - pageOffset.coerceIn(0f, 1f)
                    currentPage + 1 -> pageOffset.coerceIn(0f, 1f)
                    else -> 0f
                }
                val width = (6 + 8 * targetScale).dp
                Box(
                    modifier = Modifier
                        .padding(horizontal = 3.dp)
                        .size(height = 6.dp, width = width)
                        .clip(shape = RoundedCornerShape(100.dp))
                        .background(color = if (isSelected || targetScale > 0.01f) colors.primary else colors.border)
                )
            }
        }
    }
}

// ── Bottom navigation bar (icon + label) ──────────────────────────────────
data class NavItem(val label: String, val icon: Painter)

@Composable
fun BottomNavBar(
    items: List<NavItem>,
    selectedIndex: Int,
    onSelect: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    val colors = LocalBrutalColors.current
    val dims = LocalBrutalDimensions.current
    val typo = LocalBrutalTypography.current
    Row(
        modifier = modifier
            .fillMaxWidth()
            .background(color = colors.surface)
            .border(
                width = dims.borderThin,
                color = colors.border,
                shape = RoundedCornerShape(topStart = dims.radiusLg, topEnd = dims.radiusLg)
            )
            .padding(vertical = 10.dp),
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        items.forEachIndexed { index, item ->
            val selected = index == selectedIndex
            Column(
                modifier = Modifier.clickable { onSelect(index) }.padding(horizontal = 16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Icon(
                    painter = item.icon,
                    contentDescription = item.label,
                    modifier = Modifier.size(if (selected) 26.dp else 22.dp),
                    tint = if (selected) colors.primary else colors.onSurfaceVariant
                )
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = item.label,
                    style = typo.labelSmall,
                    color = if (selected) colors.primary else colors.onSurfaceVariant
                )
            }
        }
    }
}

@Preview
@Composable
private fun PosterCardPreview() {
    PreviewContainer {
        PosterCard(
            anime = PreviewData.anime,
            onClick = {},
            modifier = Modifier.width(130.dp)
        )
    }
}

@Preview
@Composable
private fun HeroCarouselPreview() {
    PreviewContainer {
        HeroCarousel(items = PreviewData.animeList, onItemClick = {})
    }
}

@Preview
@Composable
private fun BottomNavPreview() {
    PreviewContainer {
        BottomNavBar(
            items = listOf(
                NavItem(label = "Search", icon = painterResource(Res.drawable.ic_search)),
                NavItem(label = "My List", icon = painterResource(Res.drawable.ic_bookmark))
            ),
            selectedIndex = 0,
            onSelect = {}
        )
    }
}
