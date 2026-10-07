package io.github.joelkanyi.jenga.component.search

import androidx.compose.foundation.clickable
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import io.github.joelkanyi.jenga.component.icon.JengaIcon
import io.github.joelkanyi.jenga.component.textfield.JengaTextField
import io.github.joelkanyi.jenga.theme.JengaTheme

/**
 * A search input: a pill-shaped [JengaTextField] with a leading search icon and
 * a clear button that appears once there's text.
 *
 * @sample io.github.joelkanyi.jenga.samples.JengaSearchFieldSample
 *
 * @param value the current query.
 * @param onValueChange called when the query changes.
 * @param placeholder hint shown when empty (e.g. "Search tickets").
 * @param clearContentDescription accessibility label for the clear button.
 * @param modifier the [Modifier] for this field.
 * @param enabled whether the field is editable.
 * @param onClear called when the clear button is tapped; defaults to clearing the text.
 */
@Composable
public fun JengaSearchField(
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String,
    clearContentDescription: String,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    onClear: (() -> Unit)? = null,
) {
    JengaTextField(
        value = value,
        onValueChange = onValueChange,
        modifier = modifier,
        placeholder = placeholder,
        enabled = enabled,
        singleLine = true,
        shape = JengaTheme.shapes.pill,
        leadingIcon = { JengaIcon(JengaTheme.icons.search, contentDescription = null) },
        trailingIcon = if (value.isNotEmpty()) {
            {
                JengaIcon(
                    imageVector = JengaTheme.icons.close,
                    contentDescription = clearContentDescription,
                    modifier = Modifier.clickable {
                        if (onClear != null) onClear() else onValueChange("")
                    },
                )
            }
        } else {
            null
        },
    )
}
