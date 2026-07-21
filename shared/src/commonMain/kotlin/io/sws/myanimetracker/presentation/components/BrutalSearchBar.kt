package io.sws.myanimetracker.presentation.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import io.sws.myanimetracker.presentation.PreviewContainer
import io.sws.myanimetracker.presentation.theme.LocalBrutalColors
import io.sws.myanimetracker.presentation.theme.LocalBrutalDimensions
import io.sws.myanimetracker.presentation.theme.LocalBrutalTypography
import io.sws.myanimetracker.presentation.theme.glassSurface
import myanimetracker.shared.generated.resources.Res
import myanimetracker.shared.generated.resources.ic_close
import myanimetracker.shared.generated.resources.ic_search
import org.jetbrains.compose.resources.painterResource

@Composable
fun BrutalSearchBar(
    query: String,
    onQueryChange: (String) -> Unit,
    onSearch: () -> Unit,
    modifier: Modifier = Modifier,
    placeholder: String = "Search anime…",
    readOnly: Boolean = false,
    onClick: (() -> Unit)? = null,
    onClear: (() -> Unit)? = null
) {
    val colors = LocalBrutalColors.current
    val dims = LocalBrutalDimensions.current
    val typo = LocalBrutalTypography.current
    val keyboardController = LocalSoftwareKeyboardController.current
    var textFieldValue by remember { mutableStateOf(TextFieldValue(text = query)) }

    LaunchedEffect(query) {
        if (query != textFieldValue.text) {
            textFieldValue = TextFieldValue(text = query)
        }
    }

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
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .glassSurface(cornerRadius = dims.radiusPill)
                    .padding(horizontal = dims.spacingLg, vertical = dims.spacingMd),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    painter = painterResource(Res.drawable.ic_search),
                    contentDescription = null,
                    tint = colors.primary,
                    modifier = Modifier.size(20.dp)
                )
                Spacer(modifier = Modifier.width(dims.spacingMd))
                Box(modifier = Modifier.weight(1f)) {
                    if (textFieldValue.text.isEmpty()) {
                        Text(text = placeholder, style = typo.bodyLarge, color = colors.textSecondary)
                    }
                    innerTextField()
                }
                if (textFieldValue.text.isNotEmpty() && onClear != null) {
                    Spacer(modifier = Modifier.width(dims.spacingSm))
                    Icon(
                        painter = painterResource(Res.drawable.ic_close),
                        contentDescription = "Clear",
                        tint = colors.textSecondary,
                        modifier = Modifier
                            .size(18.dp)
                            .clickable {
                                textFieldValue = TextFieldValue()
                                onClear()
                            }
                    )
                }
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
        BrutalSearchBar(query = "cowboy bebop", onQueryChange = {}, onSearch = {}, onClear = {})
    }
}
