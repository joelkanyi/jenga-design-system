package io.github.joelkanyi.jenga.component.list

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.selection.toggleable
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.unit.Dp
import io.github.joelkanyi.jenga.component.selection.JengaCheckbox
import io.github.joelkanyi.jenga.component.selection.JengaCheckboxDefaults
import io.github.joelkanyi.jenga.component.selection.JengaRadioButton

/** Where a selection list item draws its control. */
public enum class JengaControlPosition { Leading, Trailing }

/**
 * A list row with a [JengaRadioButton], for picking one option from a list.
 * The whole row is the touch target and is announced as a radio button.
 *
 * @sample io.github.joelkanyi.jenga.samples.JengaRadioListItemSample
 *
 * @param headline the option label.
 * @param selected whether this option is the selected one.
 * @param onClick called when the row is tapped.
 * @param modifier the [Modifier] for this row.
 * @param supporting optional secondary text below the headline.
 * @param enabled whether the row is interactive.
 * @param controlPosition whether the radio sits at the start or the end of the row.
 * @param minHeight the minimum row height.
 * @param contentPadding inner padding around the row content.
 */
@Composable
public fun JengaRadioListItem(
    headline: String,
    selected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    supporting: String? = null,
    enabled: Boolean = true,
    controlPosition: JengaControlPosition = JengaControlPosition.Leading,
    minHeight: Dp = JengaListItemDefaults.MinHeight,
    contentPadding: PaddingValues = JengaListItemDefaults.contentPadding,
) {
    val control: @Composable () -> Unit = { JengaRadioButton(selected = selected, onClick = null, enabled = enabled) }
    JengaListItem(
        headline = headline,
        modifier = modifier.selectable(
            selected = selected,
            enabled = enabled,
            role = Role.RadioButton,
            onClick = onClick,
        ),
        supporting = supporting,
        leadingContent = control.takeIf { controlPosition == JengaControlPosition.Leading },
        trailingContent = control.takeIf { controlPosition == JengaControlPosition.Trailing },
        enabled = enabled,
        minHeight = minHeight,
        contentPadding = contentPadding,
    )
}

/**
 * A list row with a [JengaCheckbox], for picking several options from a list.
 * The whole row is the touch target and is announced as a checkbox.
 *
 * @sample io.github.joelkanyi.jenga.samples.JengaCheckboxListItemSample
 *
 * @param headline the option label.
 * @param checked whether this option is checked.
 * @param onCheckedChange called with the new checked state when the row is tapped.
 * @param modifier the [Modifier] for this row.
 * @param supporting optional secondary text below the headline.
 * @param enabled whether the row is interactive.
 * @param controlPosition whether the checkbox sits at the start or the end of the row.
 * @param minHeight the minimum row height.
 * @param contentPadding inner padding around the row content.
 * @param checkboxShape the checkbox box shape.
 */
@Composable
public fun JengaCheckboxListItem(
    headline: String,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    modifier: Modifier = Modifier,
    supporting: String? = null,
    enabled: Boolean = true,
    controlPosition: JengaControlPosition = JengaControlPosition.Leading,
    minHeight: Dp = JengaListItemDefaults.MinHeight,
    contentPadding: PaddingValues = JengaListItemDefaults.contentPadding,
    checkboxShape: Shape = JengaCheckboxDefaults.shape,
) {
    val control: @Composable () -> Unit = {
        JengaCheckbox(checked = checked, onCheckedChange = null, enabled = enabled, shape = checkboxShape)
    }
    JengaListItem(
        headline = headline,
        modifier = modifier.toggleable(
            value = checked,
            enabled = enabled,
            role = Role.Checkbox,
            onValueChange = onCheckedChange,
        ),
        supporting = supporting,
        leadingContent = control.takeIf { controlPosition == JengaControlPosition.Leading },
        trailingContent = control.takeIf { controlPosition == JengaControlPosition.Trailing },
        enabled = enabled,
        minHeight = minHeight,
        contentPadding = contentPadding,
    )
}
