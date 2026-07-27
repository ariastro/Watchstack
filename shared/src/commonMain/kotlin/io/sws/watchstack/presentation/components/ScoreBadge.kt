package io.sws.watchstack.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import io.sws.watchstack.presentation.PreviewContainer
import io.sws.watchstack.presentation.theme.LocalBrutalColors
import io.sws.watchstack.presentation.theme.LocalBrutalDimensions
import io.sws.watchstack.presentation.theme.LocalBrutalTypography
import org.jetbrains.compose.resources.painterResource
import watchstack.shared.generated.resources.Res
import watchstack.shared.generated.resources.ic_star

@Composable
fun ScoreBadge(score: Double, modifier: Modifier = Modifier) {
    val colors = LocalBrutalColors.current
    val dims = LocalBrutalDimensions.current
    val typo = LocalBrutalTypography.current
    Row(
        modifier = modifier
            .clip(shape = RoundedCornerShape(dims.radiusPill))
            .background(
                brush = Brush.horizontalGradient(
                    colors = listOf(colors.primary, colors.accent)
                )
            )
            .padding(horizontal = 10.dp, vertical = 5.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        Icon(
            painter = painterResource(Res.drawable.ic_star),
            contentDescription = null,
            tint = Color.White,
            modifier = Modifier.size(12.dp),
        )
        Text(
            text = score.toString(),
            style = typo.labelSmall,
            color = Color.White
        )
    }
}

@Preview
@Composable
fun ScoreBadgePreview() {
    PreviewContainer {
        ScoreBadge(8.9)
    }
}