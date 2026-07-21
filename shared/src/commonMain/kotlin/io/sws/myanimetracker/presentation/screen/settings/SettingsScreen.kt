package io.sws.myanimetracker.presentation.screen.settings

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import io.sws.myanimetracker.domain.model.ThemeMode
import io.sws.myanimetracker.presentation.PreviewContainer
import io.sws.myanimetracker.presentation.theme.LocalBrutalColors
import io.sws.myanimetracker.presentation.theme.LocalBrutalDimensions
import io.sws.myanimetracker.presentation.theme.LocalBrutalTypography
import io.sws.myanimetracker.presentation.theme.brutalBlock
import io.sws.myanimetracker.presentation.theme.glassSurface
import myanimetracker.shared.generated.resources.Res
import myanimetracker.shared.generated.resources.ic_arrow_back
import myanimetracker.shared.generated.resources.ic_bookmark
import myanimetracker.shared.generated.resources.ic_home
import myanimetracker.shared.generated.resources.ic_star
import org.jetbrains.compose.resources.painterResource
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun SettingsScreen(
    modifier: Modifier = Modifier,
    viewModel: SettingsViewModel = koinViewModel<SettingsViewModel>()
) {
    val uiState by viewModel.uiState.collectAsState()
    SettingsContent(
        uiState = uiState,
        onIntent = viewModel::onIntent,
        modifier = modifier
    )
}

@Composable
private fun SettingsContent(
    uiState: SettingsUiState,
    onIntent: (SettingsIntent) -> Unit,
    modifier: Modifier = Modifier
) {
    val colors = LocalBrutalColors.current
    val dims = LocalBrutalDimensions.current
    val typo = LocalBrutalTypography.current
    val statusTop = WindowInsets.statusBars.asPaddingValues().calculateTopPadding()

    Box(modifier = modifier.fillMaxSize().background(color = colors.background)) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(statusTop + 200.dp)
                .background(
                    brush = Brush.verticalGradient(
                        colors = listOf(
                            colors.primary.copy(alpha = 0.24f),
                            colors.primary.copy(alpha = 0.12f),
                            colors.background
                        )
                    )
                )
        )

        Column(modifier = Modifier.fillMaxSize()) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = statusTop)
                    .padding(horizontal = dims.paddingScreen, vertical = dims.spacingMd),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(44.dp)
                        .glassSurface(cornerRadius = dims.radiusPill)
                        .clickable { onIntent(SettingsIntent.GoBack) },
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        painter = painterResource(Res.drawable.ic_arrow_back),
                        contentDescription = "Back",
                        tint = colors.textPrimary
                    )
                }
                Spacer(modifier = Modifier.width(dims.spacingMd))
                Column {
                    Text(text = "Preferences", style = typo.labelMedium, color = colors.primary)
                    Text(text = "Settings", style = typo.headlineSmall, color = colors.textPrimary)
                }
            }

            Spacer(modifier = Modifier.height(dims.spacingLg))

            Column(modifier = Modifier.padding(horizontal = dims.paddingScreen)) {
                Text(text = "Appearance", style = typo.titleLarge, color = colors.textPrimary)
                Spacer(modifier = Modifier.height(dims.spacingXs))
                Text(
                    text = "Theme changes animate across the app.",
                    style = typo.bodySmall,
                    color = colors.textSecondary
                )
                Spacer(modifier = Modifier.height(dims.spacingLg))

                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .brutalBlock(cornerRadius = dims.radiusLg, shadowElevation = dims.radiusXs)
                        .padding(dims.spacingMd),
                    verticalArrangement = Arrangement.spacedBy(dims.spacingSm)
                ) {
                    ThemeMode.entries.forEach { mode ->
                        ThemeOptionRow(
                            mode = mode,
                            selected = uiState.themeMode == mode,
                            onClick = { onIntent(SettingsIntent.ThemeSelected(mode)) }
                        )
                    }
                }

                Spacer(modifier = Modifier.height(dims.spacingXl))

                ThemePreviewCard(selectedMode = uiState.themeMode)
            }
        }
    }
}

@Composable
private fun ThemeOptionRow(
    mode: ThemeMode,
    selected: Boolean,
    onClick: () -> Unit
) {
    val colors = LocalBrutalColors.current
    val dims = LocalBrutalDimensions.current
    val typo = LocalBrutalTypography.current
    val scale by animateFloatAsState(
        targetValue = if (selected) 1f else 0.98f,
        animationSpec = tween(220),
        label = "themeOptionScale"
    )
    val bg by animateColorAsState(
        targetValue = if (selected) colors.primaryContainer else colors.surfaceVariant,
        animationSpec = tween(320),
        label = "themeOptionBg"
    )
    val borderColor by animateColorAsState(
        targetValue = if (selected) colors.primary else Color.Transparent,
        animationSpec = tween(320),
        label = "themeOptionBorder"
    )
    val shape = RoundedCornerShape(dims.radiusMd)

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .scale(scale)
            .clip(shape)
            .background(bg)
            .border(width = 1.5.dp, color = borderColor, shape = shape)
            .clickable(onClick = onClick)
            .padding(horizontal = dims.spacingLg, vertical = dims.spacingMd),
        verticalAlignment = Alignment.CenterVertically
    ) {
        ThemeModeIcon(mode = mode, selected = selected)
        Spacer(modifier = Modifier.width(dims.spacingMd))
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = mode.label,
                style = typo.titleMedium,
                color = if (selected) colors.onPrimaryContainer else colors.textPrimary
            )
            Text(
                text = when (mode) {
                    ThemeMode.SYSTEM -> "Match device setting"
                    ThemeMode.LIGHT -> "Bright cinema light"
                    ThemeMode.DARK -> "Night indigo dark"
                },
                style = typo.bodySmall,
                color = if (selected) colors.onPrimaryContainer.copy(alpha = 0.8f) else colors.textSecondary
            )
        }
        Box(
            modifier = Modifier
                .size(22.dp)
                .clip(CircleShape)
                .border(
                    width = 2.dp,
                    color = if (selected) colors.primary else colors.border,
                    shape = CircleShape
                )
                .padding(4.dp)
        ) {
            if (selected) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .clip(CircleShape)
                        .background(colors.primary)
                )
            }
        }
    }
}

@Composable
private fun ThemeModeIcon(mode: ThemeMode, selected: Boolean) {
    val colors = LocalBrutalColors.current
    val dims = LocalBrutalDimensions.current
    val iconTint by animateColorAsState(
        targetValue = if (selected) colors.primary else colors.onSurfaceVariant,
        animationSpec = tween(280),
        label = "themeIconTint"
    )
    val iconBg by animateColorAsState(
        targetValue = if (selected) colors.primary.copy(alpha = 0.15f) else colors.surface,
        animationSpec = tween(280),
        label = "themeIconBg"
    )
    Box(
        modifier = Modifier
            .size(40.dp)
            .clip(RoundedCornerShape(dims.radiusSm))
            .background(iconBg),
        contentAlignment = Alignment.Center
    ) {
        Icon(
            painter = painterResource(
                when (mode) {
                    ThemeMode.SYSTEM -> Res.drawable.ic_home
                    ThemeMode.LIGHT -> Res.drawable.ic_star
                    ThemeMode.DARK -> Res.drawable.ic_bookmark
                }
            ),
            contentDescription = null,
            tint = iconTint,
            modifier = Modifier.size(20.dp)
        )
    }
}

@Composable
private fun ThemePreviewCard(selectedMode: ThemeMode) {
    val colors = LocalBrutalColors.current
    val dims = LocalBrutalDimensions.current
    val typo = LocalBrutalTypography.current

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .brutalBlock(cornerRadius = dims.radiusLg, shadowElevation = dims.radiusXs)
            .padding(dims.spacingLg)
    ) {
        Text(text = "Live preview", style = typo.labelLarge, color = colors.primary)
        Spacer(modifier = Modifier.height(dims.spacingSm))
        Text(
            text = "Current: ${selectedMode.label}",
            style = typo.titleMedium,
            color = colors.textPrimary
        )
        Spacer(modifier = Modifier.height(dims.spacingMd))
        Row(horizontalArrangement = Arrangement.spacedBy(dims.spacingSm)) {
            listOf(colors.primary, colors.accent, colors.secondary, colors.surfaceVariant).forEach { swatch ->
                Box(
                    modifier = Modifier
                        .size(36.dp)
                        .clip(RoundedCornerShape(dims.radiusSm))
                        .background(swatch)
                        .border(1.dp, colors.border, RoundedCornerShape(dims.radiusSm))
                )
            }
        }
    }
}

@Preview
@Composable
private fun SettingsContentPreview() {
    PreviewContainer {
        SettingsContent(
            uiState = SettingsUiState(themeMode = ThemeMode.DARK),
            onIntent = {}
        )
    }
}
