package io.sws.myanimetracker.presentation.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import io.sws.myanimetracker.presentation.theme.LocalBrutalColors
import io.sws.myanimetracker.presentation.theme.LocalBrutalDimensions
import io.sws.myanimetracker.presentation.theme.LocalBrutalTypography
import io.sws.myanimetracker.presentation.theme.glassSurface
import kotlinx.coroutines.delay

@Composable
fun AppSnackbarHost(
    message: String?,
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier,
    actionLabel: String? = null,
    onAction: (() -> Unit)? = null
) {
    val colors = LocalBrutalColors.current
    val dims = LocalBrutalDimensions.current
    val typo = LocalBrutalTypography.current
    var visible by remember(message) { mutableStateOf(message != null) }

    LaunchedEffect(message) {
        if (message != null) {
            visible = true
            delay(if (actionLabel != null) 4000 else 2800)
            visible = false
            onDismiss()
        }
    }

    Box(modifier = modifier.fillMaxWidth(), contentAlignment = Alignment.BottomCenter) {
        AnimatedVisibility(
            visible = visible && message != null,
            enter = fadeIn() + slideInVertically { it },
            exit = fadeOut() + slideOutVertically { it }
        ) {
            Row(
                modifier = Modifier
                    .padding(horizontal = dims.paddingScreen, vertical = 12.dp)
                    .glassSurface(cornerRadius = dims.radiusLg)
                    .padding(horizontal = 16.dp, vertical = 12.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Text(
                    text = message.orEmpty(),
                    style = typo.bodyMedium,
                    color = colors.textPrimary,
                    modifier = Modifier.weight(1f, fill = false)
                )
                if (actionLabel != null && onAction != null) {
                    Text(
                        text = actionLabel,
                        style = typo.labelLarge,
                        color = colors.primary,
                        modifier = Modifier.clickable {
                            onAction()
                            visible = false
                            onDismiss()
                        }
                    )
                }
            }
        }
    }
}
