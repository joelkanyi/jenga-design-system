package io.github.joelkanyi.jenga.component.list

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.selection.toggleable
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
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
 * @param contentSpacing the gap between the control and the text.
 * @param trailingContent optional end slot (e.g. a count), before a trailing control; tapping it selects the row.
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
    contentSpacing: Dp = JengaListItemDefaults.contentSpacing,
    trailingContent: (@Composable () -> Unit)? = null,
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
        trailingContent = trailingSlot(trailingContent, control.takeIf { controlPosition == JengaControlPosition.Trailing }, contentSpacing),
        enabled = enabled,
        minHeight = minHeight,
        contentPadding = contentPadding,
        contentSpacing = contentSpacing,
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
 * @param contentSpacing the gap between the control and the text.
 * @param trailingContent optional end slot (e.g. a count), before a trailing control; tapping it toggles the row.
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
    contentSpacing: Dp = JengaListItemDefaults.contentSpacing,
    trailingContent: (@Composable () -> Unit)? = null,
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
        trailingContent = trailingSlot(trailingContent, control.takeIf { controlPosition == JengaControlPosition.Trailing }, contentSpacing),
        enabled = enabled,
        minHeight = minHeight,
        contentPadding = contentPadding,
        contentSpacing = contentSpacing,
    )
}

private fun trailingSlot(
    content: (@Composable () -> Unit)?,
    control: (@Composable () -> Unit)?,
    spacing: Dp,
): (@Composable () -> Unit)? = when {
    content == null -> control
    control == null -> content
    else -> {
        {
            Row(horizontalArrangement = Arrangement.spacedBy(spacing), verticalAlignment = Alignment.CenterVertically) {
                content()
                control()
            }
        }
    }
}
