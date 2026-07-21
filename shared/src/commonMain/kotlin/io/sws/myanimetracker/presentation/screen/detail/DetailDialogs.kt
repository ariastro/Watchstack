package io.sws.myanimetracker.presentation.screen.detail

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import io.sws.myanimetracker.domain.model.WatchStatus
import io.sws.myanimetracker.presentation.PreviewContainer
import io.sws.myanimetracker.presentation.components.BrutalButton
import io.sws.myanimetracker.presentation.components.BrutalDialog
import io.sws.myanimetracker.presentation.theme.LocalBrutalColors
import io.sws.myanimetracker.presentation.theme.LocalBrutalDimensions
import io.sws.myanimetracker.presentation.theme.LocalBrutalTypography

@Composable
private fun Modifier.dialogField(): Modifier {
    val colors = LocalBrutalColors.current
    val dims = LocalBrutalDimensions.current
    val shape = RoundedCornerShape(dims.radiusMd)
    return this
        .fillMaxWidth()
        .background(color = colors.surfaceVariant, shape = shape)
        .border(width = dims.borderThin, color = colors.border, shape = shape)
        .padding(dims.spacingMd)
}

@Composable
fun TrackDialog(uiState: DetailUiState, onIntent: (DetailIntent) -> Unit) {
    val colors = LocalBrutalColors.current
    val dims = LocalBrutalDimensions.current
    val typo = LocalBrutalTypography.current

    BrutalDialog(onDismiss = { onIntent(DetailIntent.DismissDialog) }, title = "Add to list") {
        Text(text = "Status", style = typo.labelLarge, color = colors.primary)
        Spacer(modifier = Modifier.height(8.dp))
        WatchStatus.entries.forEach { status ->
            val selected = uiState.trackDialogStatus == status
            val accent = when (status) {
                WatchStatus.WATCHLIST -> colors.watchlistColor
                WatchStatus.WATCHING -> colors.watchingColor
                WatchStatus.WATCHED -> colors.watchedColor
            }
            val label = when (status) {
                WatchStatus.WATCHLIST -> "Plan to watch"
                WatchStatus.WATCHING -> "Watching"
                WatchStatus.WATCHED -> "Completed"
            }
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp)
                    .background(
                        color = if (selected) accent.copy(alpha = 0.2f) else colors.surfaceVariant,
                        shape = RoundedCornerShape(dims.radiusMd)
                    )
                    .clickable { onIntent(DetailIntent.TrackDialogStatusChanged(status)) }
                    .padding(horizontal = dims.spacingMd, vertical = dims.spacingMd),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(text = label, style = typo.titleMedium, color = colors.textPrimary)
                if (selected) {
                    Text(text = "Selected", style = typo.labelSmall, color = accent)
                }
            }
        }
        Spacer(modifier = Modifier.height(16.dp))
        Text(text = "Where to watch", style = typo.labelLarge, color = colors.primary)
        Spacer(modifier = Modifier.height(8.dp))
        BasicTextField(
            value = uiState.trackDialogWhere,
            onValueChange = { onIntent(DetailIntent.TrackDialogWhereChanged(it)) },
            textStyle = typo.bodyMedium.copy(color = colors.textPrimary),
            cursorBrush = SolidColor(colors.primary),
            modifier = Modifier.dialogField()
        )
        Spacer(modifier = Modifier.height(16.dp))
        BrutalButton(
            text = if (uiState.isSaving) "Saving…" else "Confirm",
            onClick = { onIntent(DetailIntent.ConfirmTrack) },
            modifier = Modifier.fillMaxWidth(),
            enabled = !uiState.isSaving
        )
        uiState.actionError?.let { err ->
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = err, style = typo.bodySmall, color = colors.error)
        }
    }
}

@Composable
fun EditDialog(uiState: DetailUiState, onIntent: (DetailIntent) -> Unit) {
    val colors = LocalBrutalColors.current
    val typo = LocalBrutalTypography.current

    BrutalDialog(onDismiss = { onIntent(DetailIntent.DismissDialog) }, title = "Edit tracking") {
        Text(text = "Episodes watched", style = typo.labelLarge, color = colors.primary)
        Spacer(modifier = Modifier.height(8.dp))
        BasicTextField(
            value = uiState.editEpisodes,
            onValueChange = { onIntent(DetailIntent.EditEpisodesChanged(it)) },
            textStyle = typo.bodyMedium.copy(color = colors.textPrimary),
            cursorBrush = SolidColor(colors.primary),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            modifier = Modifier.dialogField()
        )
        Spacer(modifier = Modifier.height(12.dp))
        Text(text = "Your rating (1-10)", style = typo.labelLarge, color = colors.primary)
        Spacer(modifier = Modifier.height(8.dp))
        BasicTextField(
            value = uiState.editRating,
            onValueChange = { onIntent(DetailIntent.EditRatingChanged(it)) },
            textStyle = typo.bodyMedium.copy(color = colors.textPrimary),
            cursorBrush = SolidColor(colors.primary),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            modifier = Modifier.dialogField()
        )
        Spacer(modifier = Modifier.height(12.dp))
        Text(text = "Where to watch", style = typo.labelLarge, color = colors.primary)
        Spacer(modifier = Modifier.height(8.dp))
        BasicTextField(
            value = uiState.editWhere,
            onValueChange = { onIntent(DetailIntent.EditWhereChanged(it)) },
            textStyle = typo.bodyMedium.copy(color = colors.textPrimary),
            cursorBrush = SolidColor(colors.primary),
            modifier = Modifier.dialogField()
        )
        Spacer(modifier = Modifier.height(12.dp))
        Text(text = "Notes", style = typo.labelLarge, color = colors.primary)
        Spacer(modifier = Modifier.height(8.dp))
        BasicTextField(
            value = uiState.editNotes,
            onValueChange = { onIntent(DetailIntent.EditNotesChanged(it)) },
            textStyle = typo.bodyMedium.copy(color = colors.textPrimary),
            cursorBrush = SolidColor(colors.primary),
            modifier = Modifier.dialogField()
        )
        Spacer(modifier = Modifier.height(16.dp))
        BrutalButton(
            text = if (uiState.isSaving) "Saving…" else "Save",
            onClick = {
                onIntent(
                    DetailIntent.SaveEdit(
                        episodes = uiState.editEpisodes.toIntOrNull(),
                        rating = uiState.editRating.toIntOrNull()?.coerceIn(1, 10),
                        whereToWatch = uiState.editWhere,
                        notes = uiState.editNotes
                    )
                )
            },
            modifier = Modifier.fillMaxWidth(),
            enabled = !uiState.isSaving
        )
        uiState.actionError?.let { err ->
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = err, style = typo.bodySmall, color = colors.error)
        }
    }
}

@Preview
@Composable
private fun TrackDialogPreview() {
    PreviewContainer {
        TrackDialog(
            uiState = DetailUiState(
                trackDialogStatus = WatchStatus.WATCHING,
                trackDialogWhere = "Netflix"
            ),
            onIntent = {}
        )
    }
}

@Preview
@Composable
private fun EditDialogPreview() {
    PreviewContainer {
        EditDialog(
            uiState = DetailUiState(
                editEpisodes = "12",
                editRating = "9",
                editWhere = "Netflix",
                editNotes = "Great"
            ),
            onIntent = {}
        )
    }
}
