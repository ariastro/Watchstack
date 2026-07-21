package io.sws.myanimetracker.presentation.theme

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

@Composable
expect fun ApplySystemBars(
    darkTheme: Boolean,
    background: Color
)
