package io.sws.watchstack.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import io.sws.watchstack.presentation.PreviewContainer
import io.sws.watchstack.presentation.theme.LocalBrutalColors
import io.sws.watchstack.presentation.theme.LocalBrutalDimensions
import io.sws.watchstack.presentation.theme.LocalBrutalTypography
import io.sws.watchstack.presentation.theme.brutalShadow
import androidx.compose.ui.tooling.preview.Preview

@Composable
fun AnimePoster(
    imageUrl: String?,
    contentDescription: String?,
    modifier: Modifier = Modifier,
    contentScale: ContentScale = ContentScale.Crop
) {
    val colors = LocalBrutalColors.current
    val dims = LocalBrutalDimensions.current
    val typo = LocalBrutalTypography.current
    val shape = RoundedCornerShape(dims.radiusSm)

    Box(
        modifier = modifier
            .clip(shape)
            .background(colors.surface)
            .border(dims.borderMedium, colors.border, shape),
        contentAlignment = Alignment.Center
    ) {
        if (!imageUrl.isNullOrBlank()) {
            AsyncImage(
                model = imageUrl,
                contentDescription = contentDescription,
                contentScale = contentScale,
                modifier = Modifier.fillMaxSize()
            )
        } else {
            Text("NO\nIMG", style = typo.labelSmall, color = colors.textSecondary)
        }
    }
}

@Preview
@Composable
private fun AnimePosterPreview() {
    PreviewContainer {
        AnimePoster(
            imageUrl = null,
            contentDescription = "poster",
            modifier = Modifier.width(120.dp).height(160.dp)
        )
    }
}
