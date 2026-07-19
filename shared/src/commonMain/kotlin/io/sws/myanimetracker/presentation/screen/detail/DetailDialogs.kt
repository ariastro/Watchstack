package io.sws.myanimetracker.presentation.screen.detail

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import io.sws.myanimetracker.domain.model.WatchStatus
import io.sws.myanimetracker.presentation.PreviewContainer
import io.sws.myanimetracker.presentation.components.BrutalBadge
import io.sws.myanimetracker.presentation.components.BrutalButton
import io.sws.myanimetracker.presentation.components.BrutalDialog
import io.sws.myanimetracker.presentation.theme.LocalBrutalColors
import io.sws.myanimetracker.presentation.theme.LocalBrutalDimensions
import io.sws.myanimetracker.presentation.theme.LocalBrutalTypography

@Composable
private fun Modifier.dialogField(): Modifier {
    val colors = LocalBrutalColors.current
    val dims = LocalBrutalDimensions.current
    return this
        .fillMaxWidth()
        .background(color = colors.surface)
        .border(width = dims.borderThin, color = colors.border)
        .padding(dims.spacingMd)
}

@Composable
fun TrackDialog(uiState: DetailUiState, onIntent: (DetailIntent) -> Unit) {
    val colors = LocalBrutalColors.current
    val dims = LocalBrutalDimensions.current
    val typo = LocalBrutalTypography.current

    BrutalDialog(onDismiss = { onIntent(DetailIntent.DismissDialog) }, title = "TRACK ANIME") {
        Text(text = "STATUS", style = typo.labelLarge, color = colors.primary)
        WatchStatus.entries.forEach { status ->
            Row(
                modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                BrutalBadge(
                    text = if (uiState.trackDialogStatus == status) "■" else "□",
                    backgroundColor = if (uiState.trackDialogStatus == status) colors.primary else colors.surface,
                    contentColor = if (uiState.trackDialogStatus == status) colors.textInverse else colors.textPrimary
                )
                Text(
                    text = status.name,
                    style = typo.titleMedium,
                    color = colors.textPrimary
                )
            }
        }
        Spacer(modifier = Modifier.height(16.dp))
        Text(text = "WHERE TO WATCH", style = typo.labelLarge, color = colors.primary)
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
            text = "CONFIRM",
            onClick = { onIntent(DetailIntent.ConfirmTrack) },
            modifier = Modifier.fillMaxWidth()
        )
    }
}

@Composable
fun EditDialog(uiState: DetailUiState, onIntent: (DetailIntent) -> Unit) {
    val colors = LocalBrutalColors.current
    val dims = LocalBrutalDimensions.current
    val typo = LocalBrutalTypography.current

    BrutalDialog(onDismiss = { onIntent(DetailIntent.DismissDialog) }, title = "EDIT TRACKING") {
        Text(text = "EPISODES WATCHED", style = typo.labelLarge, color = colors.primary)
        Spacer(modifier = Modifier.height(8.dp))
        BasicTextField(
            value = uiState.editEpisodes,
            onValueChange = { onIntent(DetailIntent.EditEpisodesChanged(it)) },
            textStyle = typo.bodyMedium.copy(color = colors.textPrimary),
            cursorBrush = SolidColor(colors.primary),
            modifier = Modifier.dialogField()
        )
        Spacer(modifier = Modifier.height(12.dp))
        Text(text = "YOUR RATING (1-10)", style = typo.labelLarge, color = colors.primary)
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
        Text(text = "WHERE TO WATCH", style = typo.labelLarge, color = colors.primary)
        Spacer(modifier = Modifier.height(8.dp))
        BasicTextField(
            value = uiState.editWhere,
            onValueChange = { onIntent(DetailIntent.EditWhereChanged(it)) },
            textStyle = typo.bodyMedium.copy(color = colors.textPrimary),
            cursorBrush = SolidColor(colors.primary),
            modifier = Modifier.dialogField()
        )
        Spacer(modifier = Modifier.height(12.dp))
        Text(text = "NOTES", style = typo.labelLarge, color = colors.primary)
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
            text = "SAVE",
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
            modifier = Modifier.fillMaxWidth()
        )
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
