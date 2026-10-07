package io.github.joelkanyi.jenga.component.list

import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.selection.toggleable
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.Role
import io.github.joelkanyi.jenga.component.selection.JengaCheckbox
import io.github.joelkanyi.jenga.component.selection.JengaRadioButton

/**
 * A list row with a leading [JengaRadioButton], for picking one option from a
 * list. The whole row is the touch target and is announced as a radio button.
 *
 * @sample io.github.joelkanyi.jenga.samples.JengaRadioListItemSample
 *
 * @param headline the option label.
 * @param selected whether this option is the selected one.
 * @param onClick called when the row is tapped.
 * @param modifier the [Modifier] for this row.
 * @param supporting optional secondary text below the headline.
 * @param enabled whether the row is interactive.
 */
@Composable
public fun JengaRadioListItem(
    headline: String,
    selected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    supporting: String? = null,
    enabled: Boolean = true,
) {
    JengaListItem(
        headline = headline,
        modifier = modifier.selectable(
            selected = selected,
            enabled = enabled,
            role = Role.RadioButton,
            onClick = onClick,
        ),
        supporting = supporting,
        leadingContent = { JengaRadioButton(selected = selected, onClick = null, enabled = enabled) },
        enabled = enabled,
    )
}

/**
 * A list row with a leading [JengaCheckbox], for picking several options from a
 * list. The whole row is the touch target and is announced as a checkbox.
 *
 * @sample io.github.joelkanyi.jenga.samples.JengaCheckboxListItemSample
 *
 * @param headline the option label.
 * @param checked whether this option is checked.
 * @param onCheckedChange called with the new checked state when the row is tapped.
 * @param modifier the [Modifier] for this row.
 * @param supporting optional secondary text below the headline.
 * @param enabled whether the row is interactive.
 */
@Composable
public fun JengaCheckboxListItem(
    headline: String,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    modifier: Modifier = Modifier,
    supporting: String? = null,
    enabled: Boolean = true,
) {
    JengaListItem(
        headline = headline,
        modifier = modifier.toggleable(
            value = checked,
            enabled = enabled,
            role = Role.Checkbox,
            onValueChange = onCheckedChange,
        ),
        supporting = supporting,
        leadingContent = { JengaCheckbox(checked = checked, onCheckedChange = null, enabled = enabled) },
        enabled = enabled,
    )
}
