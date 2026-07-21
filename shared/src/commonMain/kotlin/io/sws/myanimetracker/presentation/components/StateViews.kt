package io.sws.myanimetracker.presentation.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import io.sws.myanimetracker.presentation.PreviewContainer
import io.sws.myanimetracker.presentation.theme.LocalBrutalColors
import io.sws.myanimetracker.presentation.theme.LocalBrutalDimensions
import io.sws.myanimetracker.presentation.theme.LocalBrutalTypography
import io.sws.myanimetracker.presentation.theme.glassSurface
import myanimetracker.shared.generated.resources.Res
import myanimetracker.shared.generated.resources.ic_close
import myanimetracker.shared.generated.resources.ic_search_off
import org.jetbrains.compose.resources.painterResource

@Composable
fun LoadingState(modifier: Modifier = Modifier) {
    val colors = LocalBrutalColors.current
    val dims = LocalBrutalDimensions.current
    val typo = LocalBrutalTypography.current
    Column(
        modifier = modifier
            .fillMaxWidth()
            .glassSurface(cornerRadius = dims.radiusLg)
            .padding(dims.spacingXxl),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        CircularProgressIndicator(
            modifier = Modifier.size(40.dp),
            color = colors.primary,
            strokeWidth = 3.dp
        )
        Spacer(modifier = Modifier.height(dims.spacingLg))
        Text(text = "Loading…", style = typo.labelMedium, color = colors.textSecondary)
    }
}

@Composable
fun ErrorState(
    message: String,
    onRetry: (() -> Unit)? = null,
    modifier: Modifier = Modifier
) {
    val colors = LocalBrutalColors.current
    val dims = LocalBrutalDimensions.current
    val typo = LocalBrutalTypography.current
    Column(
        modifier = modifier
            .fillMaxWidth()
            .glassSurface(cornerRadius = dims.radiusLg)
            .padding(dims.spacingXxl),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(
            painter = painterResource(Res.drawable.ic_close),
            contentDescription = null,
            modifier = Modifier.size(48.dp),
            tint = colors.error
        )
        Spacer(modifier = Modifier.height(dims.spacingMd))
        Text(
            text = message,
            textAlign = TextAlign.Center,
            style = typo.bodyMedium,
            color = colors.textSecondary
        )
        onRetry?.let {
            Spacer(modifier = Modifier.height(dims.spacingLg))
            BrutalButton(
                text = "Retry",
                onClick = it,
                backgroundColor = colors.error,
                contentColor = colors.onError,
                gradient = false
            )
        }
    }
}

@Composable
fun EmptyState(
    message: String,
    modifier: Modifier = Modifier,
    icon: Painter = painterResource(Res.drawable.ic_search_off),
    actionLabel: String? = null,
    onAction: (() -> Unit)? = null
) {
    val colors = LocalBrutalColors.current
    val dims = LocalBrutalDimensions.current
    val typo = LocalBrutalTypography.current
    Column(
        modifier = modifier
            .fillMaxWidth()
            .glassSurface(cornerRadius = dims.radiusLg)
            .padding(dims.spacingXxl),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(
            painter = icon,
            contentDescription = null,
            modifier = Modifier.size(48.dp),
            tint = colors.textSecondary
        )
        Spacer(modifier = Modifier.height(dims.spacingMd))
        Text(
            text = message,
            textAlign = TextAlign.Center,
            style = typo.bodyMedium,
            color = colors.textSecondary
        )
        if (actionLabel != null && onAction != null) {
            Spacer(modifier = Modifier.height(dims.spacingLg))
            BrutalButton(text = actionLabel, onClick = onAction)
        }
    }
}

@Preview
@Composable
private fun LoadingStatePreview() {
    PreviewContainer { LoadingState() }
}

@Preview
@Composable
private fun ErrorStatePreview() {
    PreviewContainer {
        ErrorState(message = "Something went wrong.", onRetry = {})
    }
}

@Preview
@Composable
private fun EmptyStatePreview() {
    PreviewContainer {
        EmptyState(message = "Nothing here yet.")
    }
}
