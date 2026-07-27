package io.sws.watchstack.presentation.components

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.unit.dp
import io.sws.watchstack.presentation.theme.LocalBrutalColors
import io.sws.watchstack.presentation.theme.LocalBrutalDimensions

@Composable
fun ShimmerBox(modifier: Modifier = Modifier) {
    val colors = LocalBrutalColors.current
    val transition = rememberInfiniteTransition(label = "shimmer")
    val x by transition.animateFloat(
        initialValue = 0f,
        targetValue = 1000f,
        animationSpec = infiniteRepeatable(
            animation = tween(1200, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "shimmerX"
    )
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(12.dp))
            .background(
                brush = Brush.linearGradient(
                    colors = listOf(
                        colors.surfaceVariant,
                        colors.surface,
                        colors.surfaceVariant
                    ),
                    start = androidx.compose.ui.geometry.Offset(x - 200f, 0f),
                    end = androidx.compose.ui.geometry.Offset(x, 200f)
                )
            )
    )
}

@Composable
fun PosterRailSkeleton(titleWidth: Int = 120) {
    val dims = LocalBrutalDimensions.current
    Column(modifier = Modifier.fillMaxWidth()) {
        ShimmerBox(
            modifier = Modifier
                .padding(horizontal = dims.paddingScreen)
                .width(titleWidth.dp)
                .height(22.dp)
        )
        Spacer(modifier = Modifier.height(dims.spacingMd))
        LazyRow(
            contentPadding = PaddingValues(horizontal = dims.paddingScreen),
            horizontalArrangement = Arrangement.spacedBy(dims.gridSpacing)
        ) {
            items(5) {
                ShimmerBox(
                    modifier = Modifier
                        .width(dims.posterWidth)
                        .height(dims.posterHeight)
                )
            }
        }
    }
}

@Composable
fun HomeSkeleton() {
    val dims = LocalBrutalDimensions.current
    Column {
        ShimmerBox(
            modifier = Modifier
                .padding(horizontal = dims.paddingScreen)
                .fillMaxWidth()
                .height(dims.heroHeight)
        )
        Spacer(modifier = Modifier.height(dims.spacingXl))
        PosterRailSkeleton(140)
        Spacer(modifier = Modifier.height(dims.spacingXl))
        PosterRailSkeleton(100)
        Spacer(modifier = Modifier.height(dims.spacingXl))
        PosterRailSkeleton(110)
    }
}
