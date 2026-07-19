package io.sws.myanimetracker.presentation.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.clickable
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import io.sws.myanimetracker.presentation.PreviewContainer
import io.sws.myanimetracker.presentation.theme.LocalBrutalColors
import io.sws.myanimetracker.presentation.theme.LocalBrutalDimensions
import io.sws.myanimetracker.presentation.theme.LocalBrutalTypography
import io.sws.myanimetracker.presentation.theme.brutalBlock

@Composable
fun BrutalSearchBar(
    query: String,
    onQueryChange: (String) -> Unit,
    onSearch: () -> Unit,
    modifier: Modifier = Modifier,
    placeholder: String = "SEARCH ANIME…",
    readOnly: Boolean = false,
    onClick: (() -> Unit)? = null
) {
    val colors = LocalBrutalColors.current
    val dims = LocalBrutalDimensions.current
    val typo = LocalBrutalTypography.current
    val keyboardController = LocalSoftwareKeyboardController.current
    var textFieldValue by remember { mutableStateOf(TextFieldValue(text = query)) }

    BasicTextField(
        value = textFieldValue,
        onValueChange = {
            textFieldValue = it
            onQueryChange(it.text)
        },
        textStyle = typo.bodyLarge.copy(color = colors.textPrimary),
        cursorBrush = SolidColor(colors.primary),
        singleLine = true,
        readOnly = readOnly,
        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
        keyboardActions = KeyboardActions(onSearch = {
            onSearch()
            keyboardController?.hide()
        }),
        decorationBox = { innerTextField ->
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .brutalBlock(
                        fill = colors.surface,
                        borderWidth = dims.borderThin,
                        cornerRadius = dims.radiusPill,
                        shadowElevation = dims.radiusXs
                    )
                    .padding(horizontal = dims.spacingLg, vertical = dims.spacingMd)
            ) {
                if (textFieldValue.text.isEmpty()) {
                    Text(
                        text = placeholder,
                        style = typo.bodyLarge,
                        color = colors.textSecondary
                    )
                }
                innerTextField()
            }
        },
        modifier = modifier
            .fillMaxWidth()
            .let { base -> if (readOnly && onClick != null) base.clickable(onClick = onClick) else base }
    )
}

@Preview
@Composable
private fun BrutalSearchBarPreview() {
    PreviewContainer {
        BrutalSearchBar(
            query = "cowboy bebop",
            onQueryChange = {},
            onSearch = {}
        )
    }
}

@Preview
@Composable
private fun BrutalSearchBarEmptyPreview() {
    PreviewContainer {
        BrutalSearchBar(
            query = "",
            onQueryChange = {},
            onSearch = {}
        )
    }
}
