package io.sws.myanimetracker.presentation.components

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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.selected
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import io.sws.myanimetracker.core.HapticFeedbackType
import io.sws.myanimetracker.core.performHaptic
import io.sws.myanimetracker.domain.model.WatchStatus
import io.sws.myanimetracker.presentation.PreviewContainer
import io.sws.myanimetracker.presentation.theme.LocalBrutalColors
import io.sws.myanimetracker.presentation.theme.LocalBrutalDimensions
import io.sws.myanimetracker.presentation.theme.LocalBrutalTypography
import io.sws.myanimetracker.presentation.theme.glassSurface

@Composable
fun BrutalTabRow(
    selectedTab: WatchStatus,
    onTabSelected: (WatchStatus) -> Unit,
    modifier: Modifier = Modifier,
    counts: (WatchStatus) -> Int = { 0 }
) {
    val colors = LocalBrutalColors.current
    val dims = LocalBrutalDimensions.current
    val typo = LocalBrutalTypography.current
    val tabs = WatchStatus.entries
    val selectedIndex = tabs.indexOf(selectedTab).coerceAtLeast(0)
    val pillShape = RoundedCornerShape(dims.radiusMd)

    BoxWithConstraints(
        modifier = modifier
            .fillMaxWidth()
            .height(72.dp)
            .glassSurface(cornerRadius = dims.radiusLg)
            .padding(dims.spacingXs)
    ) {
        val tabWidth = maxWidth / tabs.size
        val indicatorOffset by animateDpAsState(
            targetValue = tabWidth * selectedIndex,
            animationSpec = spring(
                dampingRatio = Spring.DampingRatioMediumBouncy,
                stiffness = Spring.StiffnessMediumLow
            ),
            label = "tabIndicatorOffset"
        )
        val accent = tabAccent(selectedTab, colors)
        val accentSecondary = accent.copy(alpha = 0.72f)

        Box(
            modifier = Modifier
                .offset(x = indicatorOffset)
                .width(tabWidth)
                .fillMaxHeight()
                .padding(2.dp)
                .shadow(
                    elevation = 10.dp,
                    shape = pillShape,
                    ambientColor = accent.copy(alpha = 0.35f),
                    spotColor = accent.copy(alpha = 0.45f)
                )
                .clip(pillShape)
                .background(
                    brush = Brush.verticalGradient(
                        colors = listOf(
                            accent.copy(alpha = 0.98f),
                            accentSecondary
                        )
                    )
                )
                .border(
                    width = 1.dp,
                    brush = Brush.verticalGradient(
                        listOf(
                            Color.White.copy(alpha = 0.28f),
                            Color.White.copy(alpha = 0.06f)
                        )
                    ),
                    shape = pillShape
                )
        ) {
            Box(
                modifier = Modifier
                    .align(Alignment.TopCenter)
                    .fillMaxWidth(0.7f)
                    .height(14.dp)
                    .background(
                        brush = Brush.verticalGradient(
                            listOf(
                                Color.White.copy(alpha = 0.22f),
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
            tabs.forEach { status ->
                val isSelected = selectedTab == status
                val tabAccent = tabAccent(status, colors)
                val count = counts(status)
                val scale by animateFloatAsState(
                    targetValue = if (isSelected) 1f else 0.96f,
                    animationSpec = tween(220, easing = FastOutSlowInEasing),
                    label = "tabScale"
                )
                val labelColor by animateColorAsState(
                    targetValue = if (isSelected) colors.textInverse else colors.textSecondary,
                    animationSpec = tween(200),
                    label = "tabLabelColor"
                )
                val countColor by animateColorAsState(
                    targetValue = if (isSelected) colors.textInverse else colors.textPrimary,
                    animationSpec = tween(200),
                    label = "tabCountColor"
                )
                val interaction = remember(status) { MutableInteractionSource() }

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
                                if (!isSelected) {
                                    performHaptic(HapticFeedbackType.Light)
                                    onTabSelected(status)
                                }
                            }
                        )
                        .semantics { selected = isSelected }
                        .padding(horizontal = dims.spacingXs, vertical = dims.spacingSm),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Box(
                            modifier = Modifier
                                .size(6.dp)
                                .clip(CircleShape)
                                .background(
                                    if (isSelected) {
                                        Color.White.copy(alpha = 0.9f)
                                    } else {
                                        tabAccent
                                    }
                                )
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = shortLabel(status),
                            style = typo.labelSmall,
                            color = labelColor,
                            maxLines = 1
                        )
                    }
                    Spacer(modifier = Modifier.height(4.dp))
                    if (isSelected) {
                        Text(
                            text = count.toString(),
                            style = typo.headlineMedium.copy(fontWeight = FontWeight.Bold),
                            color = countColor,
                            textAlign = TextAlign.Center,
                            maxLines = 1
                        )
                    } else {
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(dims.radiusPill))
                                .background(tabAccent.copy(alpha = 0.14f))
                                .border(
                                    width = 1.dp,
                                    color = tabAccent.copy(alpha = 0.28f),
                                    shape = RoundedCornerShape(dims.radiusPill)
                                )
                                .padding(horizontal = 10.dp, vertical = 2.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = count.toString(),
                                style = typo.labelLarge.copy(fontWeight = FontWeight.SemiBold),
                                color = tabAccent,
                                maxLines = 1
                            )
                        }
                    }
                }
            }
        }
    }
}

private fun shortLabel(status: WatchStatus): String = when (status) {
    WatchStatus.WATCHLIST -> "Plan"
    WatchStatus.WATCHING -> "Watching"
    WatchStatus.WATCHED -> "Done"
}

private fun tabAccent(
    status: WatchStatus,
    colors: io.sws.myanimetracker.presentation.theme.BrutalColors
): Color = when (status) {
    WatchStatus.WATCHLIST -> colors.watchlistColor
    WatchStatus.WATCHING -> colors.watchingColor
    WatchStatus.WATCHED -> colors.watchedColor
}

@Preview
@Composable
private fun BrutalTabRowPreview() {
    PreviewContainer {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            BrutalTabRow(
                selectedTab = WatchStatus.WATCHING,
                onTabSelected = {},
                counts = {
                    when (it) {
                        WatchStatus.WATCHLIST -> 8
                        WatchStatus.WATCHING -> 3
                        WatchStatus.WATCHED -> 42
                    }
                }
            )
            BrutalTabRow(
                selectedTab = WatchStatus.WATCHLIST,
                onTabSelected = {},
                counts = {
                    when (it) {
                        WatchStatus.WATCHLIST -> 0
                        WatchStatus.WATCHING -> 0
                        WatchStatus.WATCHED -> 0
                    }
                }
            )
        }
    }
}
