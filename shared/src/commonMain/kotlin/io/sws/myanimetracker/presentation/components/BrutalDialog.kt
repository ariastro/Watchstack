package io.sws.myanimetracker.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import io.sws.myanimetracker.presentation.PreviewContainer
import io.sws.myanimetracker.presentation.theme.LocalBrutalColors
import io.sws.myanimetracker.presentation.theme.LocalBrutalDimensions
import io.sws.myanimetracker.presentation.theme.LocalBrutalTypography
import io.sws.myanimetracker.presentation.theme.brutalBlock
import androidx.compose.ui.tooling.preview.Preview

@Composable
fun BrutalDialog(
    onDismiss: () -> Unit,
    title: String,
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) {
    val colors = LocalBrutalColors.current
    val dims = LocalBrutalDimensions.current
    Dialog(onDismissRequest = onDismiss) {
        Column(
            modifier = modifier
                .brutalBlock(
                    fill = colors.surface,
                    borderWidth = dims.borderThin,
                    cornerRadius = dims.radiusLg,
                    shadowElevation = dims.radiusLg
                )
                .padding(dims.spacingXl)
                .verticalScroll(rememberScrollState())
        ) {
            Text(
                text = title,
                style = LocalBrutalTypography.current.headlineMedium,
                color = colors.textPrimary
            )
            Spacer(modifier = Modifier.height(dims.spacingLg))
            content()
        }
    }
}

@Preview
@Composable
private fun BrutalDialogPreview() {
    PreviewContainer {
        BrutalDialog(onDismiss = {}, title = "TRACK ANIME") {
            val colors = LocalBrutalColors.current
            Text(
                text = "Dialog body content goes here.",
                style = LocalBrutalTypography.current.bodyMedium,
                color = colors.textPrimary
            )
            Spacer(modifier = Modifier.height(16.dp))
            BrutalButton(
                text = "CONFIRM",
                onClick = {},
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}
