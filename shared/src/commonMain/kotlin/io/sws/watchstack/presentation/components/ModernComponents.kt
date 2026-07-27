package io.sws.watchstack.presentation.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
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
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.selected
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import io.sws.watchstack.core.HapticFeedbackType
import io.sws.watchstack.core.performHaptic
import io.sws.watchstack.domain.model.Anime
import io.sws.watchstack.domain.model.WatchStatus
import io.sws.watchstack.presentation.PreviewContainer
import io.sws.watchstack.presentation.PreviewData
import io.sws.watchstack.presentation.animation.LocalAnimatedVisibilityScope
import io.sws.watchstack.presentation.animation.LocalSharedTransitionScope
import io.sws.watchstack.presentation.theme.LocalBrutalColors
import io.sws.watchstack.presentation.theme.LocalBrutalDimensions
import io.sws.watchstack.presentation.theme.LocalBrutalTypography
import io.sws.watchstack.presentation.theme.glassSurface
import watchstack.shared.generated.resources.Res
import watchstack.shared.generated.resources.ic_bookmark
import watchstack.shared.generated.resources.ic_home
import watchstack.shared.generated.resources.ic_search
import org.jetbrains.compose.resources.painterResource

@Composable
fun ScoreBadge(score: Double, modifier: Modifier = Modifier) {
    val colors = LocalBrutalColors.current
    val dims = LocalBrutalDimensions.current
    val typo = LocalBrutalTypography.current
    Box(
        modifier = modifier
            .clip(shape = RoundedCornerShape(dims.radiusPill))
            .background(
                brush = Brush.horizontalGradient(listOf(colors.primary, colors.accent))
            )
            .padding(horizontal = 10.dp, vertical = 5.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(text = "\u2605 $score", style = typo.labelSmall, color = Color.White)
    }
}

@Composable
fun StatusPill(status: WatchStatus, modifier: Modifier = Modifier) {
    val colors = LocalBrutalColors.current
    val dims = LocalBrutalDimensions.current
    val statusColor = when (status) {
        WatchStatus.WATCHLIST -> colors.watchlistColor
        WatchStatus.WATCHING -> colors.watchingColor
        WatchStatus.WATCHED -> colors.watchedColor
    }
    val label = when (status) {
        WatchStatus.WATCHLIST -> "Plan"
        WatchStatus.WATCHING -> "Watching"
        WatchStatus.WATCHED -> "Done"
    }
    Box(
        modifier = modifier
            .clip(shape = RoundedCornerShape(dims.radiusPill))
            .background(color = statusColor.copy(alpha = 0.92f))
            .padding(horizontal = 10.dp, vertical = 4.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(text = label, style = LocalBrutalTypography.current.labelSmall, color = Color.White)
    }
}

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
        Text(text = title, style = typo.headlineSmall, color = colors.textPrimary)
        if (actionLabel != null && onAction != null) {
            Box(
                modifier = Modifier
                    .clip(shape = RoundedCornerShape(dims.radiusPill))
                    .background(color = colors.primaryContainer)
                    .clickable(onClick = onAction)
                    .padding(horizontal = 14.dp, vertical = 7.dp)
            ) {
                Text(text = actionLabel, style = typo.labelMedium, color = colors.onPrimaryContainer)
            }
        }
    }
}

@Composable
fun PosterRail(
    title: String,
    items: List<Anime>,
    onItemClick: (Anime) -> Unit,
    modifier: Modifier = Modifier,
    actionLabel: String? = "See all",
    onAction: (() -> Unit)? = null,
    railId: String = title,
    maxItems: Int = 12,
    sharedKey: (Anime) -> String? = { null }
) {
    val dims = LocalBrutalDimensions.current
    val railItems = remember(items, maxItems) {
        items.distinctBy { it.malId }.take(maxItems)
    }
    if (railItems.isEmpty()) return
    Column(modifier = modifier.fillMaxWidth()) {
        SectionHeader(
            title = title,
            actionLabel = actionLabel,
            onAction = onAction,
            modifier = Modifier.padding(horizontal = dims.paddingScreen)
        )
        Spacer(modifier = Modifier.height(dims.spacingMd))
        LazyRow(
            contentPadding = PaddingValues(horizontal = dims.paddingScreen),
            horizontalArrangement = Arrangement.spacedBy(dims.gridSpacing)
        ) {
            items(
                items = railItems,
                key = { anime -> "$railId-${anime.malId}" },
                contentType = { "poster" }
            ) { anime ->
                PosterCard(
                    anime = anime,
                    onClick = { onItemClick(anime) },
                    modifier = Modifier.width(dims.posterWidth),
                    sharedKey = sharedKey(anime)
                )
            }
        }
    }
}

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
                            colors = listOf(
                                Color.Transparent,
                                Color.Black.copy(alpha = 0.12f),
                                Color.Black.copy(alpha = 0.78f)
                            )
                        )
                    )
            )
            anime.score?.let {
                ScoreBadge(score = it, modifier = Modifier.align(Alignment.TopStart).padding(8.dp))
            }
            statusBadge?.let {
                StatusPill(status = it, modifier = Modifier.align(Alignment.TopEnd).padding(8.dp))
            }
            Text(
                text = anime.title,
                style = typo.titleSmall,
                color = Color.White,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.align(Alignment.BottomStart).padding(12.dp)
            )
        }
    }
}

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
    val heroItems = remember(items) { items.distinctBy { it.malId }.take(6) }
    if (heroItems.isEmpty()) return
    val pagerState = rememberPagerState(initialPage = 0, pageCount = { heroItems.size })

    Column(modifier = modifier.fillMaxWidth()) {
        HorizontalPager(
            state = pagerState,
            contentPadding = PaddingValues(horizontal = dims.paddingScreen),
            pageSpacing = dims.gridSpacing,
            beyondViewportPageCount = 1,
            key = { page -> "hero-${heroItems[page].malId}" }
        ) { page ->
            val anime = heroItems[page]
            val key = sharedKey(anime)
            var heroModifier = Modifier
                .fillMaxWidth()
                .height(dims.heroHeight)
                .clip(shape = RoundedCornerShape(dims.radiusXl))
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
                                colors = listOf(
                                    Color.Black.copy(alpha = 0.12f),
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
                                    colors.primary.copy(alpha = 0.22f),
                                    Color.Transparent,
                                    colors.accent.copy(alpha = 0.18f)
                                )
                            )
                        )
                )
                anime.score?.let {
                    ScoreBadge(score = it, modifier = Modifier.align(Alignment.TopStart).padding(16.dp))
                }
                Column(modifier = Modifier.align(Alignment.BottomStart).padding(20.dp)) {
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(dims.radiusPill))
                            .background(colors.primary.copy(alpha = 0.88f))
                            .padding(horizontal = 10.dp, vertical = 4.dp)
                    ) {
                        Text(text = "Featured", style = typo.labelSmall, color = Color.White)
                    }
                    Spacer(modifier = Modifier.height(10.dp))
                    Text(
                        text = anime.title,
                        style = typo.headlineLarge,
                        color = Color.White,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis
                    )
                    if (anime.genres.isNotEmpty()) {
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            text = anime.genres.take(2).joinToString(" · "),
                            style = typo.bodySmall,
                            color = Color.White.copy(alpha = 0.75f),
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                }
            }
        }
        val currentPage by remember { derivedStateOf { pagerState.currentPage } }
        Row(
            modifier = Modifier.fillMaxWidth().padding(top = 14.dp),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            repeat(heroItems.size) { index ->
                val isSelected = index == currentPage
                Box(
                    modifier = Modifier
                        .padding(horizontal = 3.dp)
                        .size(height = 6.dp, width = if (isSelected) 20.dp else 6.dp)
                        .clip(shape = RoundedCornerShape(100.dp))
                        .background(color = if (isSelected) colors.primary else colors.border)
                )
            }
        }
    }
}

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
    val safeIndex = selectedIndex.coerceIn(0, (items.size - 1).coerceAtLeast(0))
    val dockShape = RoundedCornerShape(dims.radiusXl)
    val pillShape = RoundedCornerShape(dims.radiusLg)

    Box(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = dims.paddingScreen, vertical = dims.spacingMd)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(72.dp)
                .shadow(
                    elevation = 18.dp,
                    shape = dockShape,
                    ambientColor = colors.primary.copy(alpha = 0.18f),
                    spotColor = colors.accent.copy(alpha = 0.22f)
                )
                .clip(dockShape)
                .background(
                    brush = Brush.verticalGradient(
                        listOf(
                            colors.glass.copy(alpha = 0.98f),
                            colors.surface.copy(alpha = 0.94f)
                        )
                    )
                )
                .border(
                    width = 1.dp,
                    brush = Brush.verticalGradient(
                        listOf(
                            Color.White.copy(alpha = 0.22f),
                            colors.glassBorder
                        )
                    ),
                    shape = dockShape
                )
                .padding(6.dp)
        ) {
            BoxWithConstraints(modifier = Modifier.fillMaxSize()) {
                val tabWidth = if (items.isEmpty()) maxWidth else maxWidth / items.size
                val indicatorOffset by animateDpAsState(
                    targetValue = tabWidth * safeIndex,
                    animationSpec = spring(
                        dampingRatio = Spring.DampingRatioMediumBouncy,
                        stiffness = Spring.StiffnessMediumLow
                    ),
                    label = "navIndicatorOffset"
                )

                Box(
                    modifier = Modifier
                        .offset(x = indicatorOffset)
                        .width(tabWidth)
                        .fillMaxHeight()
                        .padding(horizontal = 2.dp)
                        .shadow(
                            elevation = 12.dp,
                            shape = pillShape,
                            ambientColor = colors.primary.copy(alpha = 0.35f),
                            spotColor = colors.accent.copy(alpha = 0.4f)
                        )
                        .clip(pillShape)
                        .background(
                            brush = Brush.horizontalGradient(
                                listOf(colors.primary, colors.accent)
                            )
                        )
                        .border(
                            width = 1.dp,
                            brush = Brush.verticalGradient(
                                listOf(
                                    Color.White.copy(alpha = 0.35f),
                                    Color.White.copy(alpha = 0.08f)
                                )
                            ),
                            shape = pillShape
                        )
                ) {
                    Box(
                        modifier = Modifier
                            .align(Alignment.TopCenter)
                            .fillMaxWidth(0.75f)
                            .height(12.dp)
                            .background(
                                brush = Brush.verticalGradient(
                                    listOf(
                                        Color.White.copy(alpha = 0.28f),
                                        Color.Transparent
                                    )
                                )
                            )
                    )
                }

                Row(
                    modifier = Modifier.fillMaxSize(),
                    horizontalArrangement = Arrangement.SpaceEvenly,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    items.forEachIndexed { index, item ->
                        val selected = index == safeIndex
                        val scale by animateFloatAsState(
                            targetValue = if (selected) 1f else 0.94f,
                            animationSpec = tween(220, easing = FastOutSlowInEasing),
                            label = "navItemScale"
                        )
                        val iconTint by animateColorAsState(
                            targetValue = if (selected) Color.White else colors.onSurfaceVariant,
                            animationSpec = tween(200),
                            label = "navIconTint"
                        )
                        val labelColor by animateColorAsState(
                            targetValue = if (selected) Color.White else colors.textSecondary,
                            animationSpec = tween(200),
                            label = "navLabelColor"
                        )
                        val iconSize by animateDpAsState(
                            targetValue = if (selected) 24.dp else 22.dp,
                            animationSpec = tween(200),
                            label = "navIconSize"
                        )
                        val interaction = remember(index) { MutableInteractionSource() }

                        Column(
                            modifier = Modifier
                                .weight(1f)
                                .fillMaxHeight()
                                .graphicsLayer {
                                    scaleX = scale
                                    scaleY = scale
                                }
                                .clip(pillShape)
                                .clickable(
                                    interactionSource = interaction,
                                    indication = null,
                                    role = Role.Tab,
                                    onClick = {
                                        if (!selected) {
                                            performHaptic(HapticFeedbackType.Light)
                                            onSelect(index)
                                        }
                                    }
                                )
                                .semantics { this.selected = selected }
                                .padding(horizontal = 4.dp, vertical = 6.dp),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                if (selected) {
                                    Box(
                                        modifier = Modifier
                                            .size(34.dp)
                                            .clip(CircleShape)
                                            .background(Color.White.copy(alpha = 0.14f))
                                    )
                                }
                                Icon(
                                    painter = item.icon,
                                    contentDescription = item.label,
                                    modifier = Modifier.size(iconSize),
                                    tint = iconTint
                                )
                            }
                            Spacer(modifier = Modifier.height(3.dp))
                            Text(
                                text = item.label,
                                style = typo.labelSmall.copy(
                                    fontWeight = if (selected) FontWeight.SemiBold else FontWeight.Medium
                                ),
                                color = labelColor,
                                maxLines = 1
                            )
                        }
                    }
                }
            }
        }
    }
}

@Preview
@Composable
private fun PosterCardPreview() {
    PreviewContainer {
        PosterCard(anime = PreviewData.anime, onClick = {}, modifier = Modifier.width(130.dp))
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
                NavItem(label = "Home", icon = painterResource(Res.drawable.ic_home)),
                NavItem(label = "Search", icon = painterResource(Res.drawable.ic_search)),
                NavItem(label = "List", icon = painterResource(Res.drawable.ic_bookmark))
            ),
            selectedIndex = 0,
            onSelect = {}
        )
    }
}
