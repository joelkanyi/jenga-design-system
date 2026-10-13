package io.github.joelkanyi.jenga.component.list

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.selection.toggleable
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import dev.drewhamilton.poko.Poko
import io.github.joelkanyi.jenga.component.progress.JengaCircularProgressIndeterminate
import io.github.joelkanyi.jenga.component.selection.JengaCheckbox
import io.github.joelkanyi.jenga.component.selection.JengaCheckboxColors
import io.github.joelkanyi.jenga.component.selection.JengaCheckboxDefaults
import io.github.joelkanyi.jenga.component.selection.JengaRadioButton
import io.github.joelkanyi.jenga.component.selection.JengaRadioButtonDefaults
import io.github.joelkanyi.jenga.theme.JengaTheme

/**
 * Theme-wide style for every [JengaRadioListItem] and [JengaCheckboxListItem], set through
 * [io.github.joelkanyi.jenga.theme.JengaComponentStyles.selectionListItem]. A `null` value falls
 * back to the theme's list item style, then to Jenga's default; arguments passed to a call still win.
 *
 * @property minHeight the minimum row height.
 * @property contentPadding inner padding around the row content.
 * @property contentSpacing the gap between the control and the text.
 * @property headlineStyle the headline text style.
 * @property supportingStyle the supporting text style.
 * @property colors the row color set.
 * @property radioSize the diameter of the radio.
 * @property checkboxShape the checkbox box shape.
 */
@Poko
@Immutable
public class JengaSelectionListItemStyle(
    public val minHeight: Dp? = null,
    public val contentPadding: PaddingValues? = null,
    public val contentSpacing: Dp? = null,
    public val headlineStyle: TextStyle? = null,
    public val supportingStyle: TextStyle? = null,
    public val colors: JengaListItemColors? = null,
    public val radioSize: Dp? = null,
    public val checkboxShape: Shape? = null,
) {
    public fun copy(
        minHeight: Dp? = this.minHeight,
        contentPadding: PaddingValues? = this.contentPadding,
        contentSpacing: Dp? = this.contentSpacing,
        headlineStyle: TextStyle? = this.headlineStyle,
        supportingStyle: TextStyle? = this.supportingStyle,
        colors: JengaListItemColors? = this.colors,
        radioSize: Dp? = this.radioSize,
        checkboxShape: Shape? = this.checkboxShape,
    ): JengaSelectionListItemStyle = JengaSelectionListItemStyle(
        minHeight,
        contentPadding,
        contentSpacing,
        headlineStyle,
        supportingStyle,
        colors,
        radioSize,
        checkboxShape,
    )
}

/**
 * Defaults for [JengaRadioListItem] and [JengaCheckboxListItem]. The composable members follow the
 * theme's [JengaSelectionListItemStyle], then its list item style.
 */
public object JengaSelectionListItemDefaults {
    /** Diameter of the spinner shown in place of the checkbox while loading. */
    public val LoadingIndicatorSize: Dp = 18.dp

    /** Minimum row height. */
    public val minHeight: Dp
        @Composable get() = JengaTheme.components.selectionListItem?.minHeight
            ?: JengaTheme.components.listItem?.minHeight
            ?: JengaListItemDefaults.MinHeight

    /** Inner padding around the row content. */
    public val contentPadding: PaddingValues
        @Composable get() = JengaTheme.components.selectionListItem?.contentPadding ?: JengaListItemDefaults.contentPadding

    /** Gap between the control and the text. */
    public val contentSpacing: Dp
        @Composable get() = JengaTheme.components.selectionListItem?.contentSpacing ?: JengaListItemDefaults.contentSpacing

    /** Headline text style. */
    public val headlineStyle: TextStyle
        @Composable get() = JengaTheme.components.selectionListItem?.headlineStyle ?: JengaListItemDefaults.headlineStyle

    /** Supporting text style. */
    public val supportingStyle: TextStyle
        @Composable get() = JengaTheme.components.selectionListItem?.supportingStyle ?: JengaListItemDefaults.supportingStyle

    /** Diameter of the radio. */
    public val radioSize: Dp
        @Composable get() = JengaTheme.components.selectionListItem?.radioSize ?: JengaRadioButtonDefaults.Size

    /** Checkbox box shape. */
    public val checkboxShape: Shape
        @Composable get() = JengaTheme.components.selectionListItem?.checkboxShape ?: JengaCheckboxDefaults.shape

    /** Row colors. */
    @Composable
    public fun colors(): JengaListItemColors = JengaTheme.components.selectionListItem?.colors ?: JengaListItemDefaults.colors()
}

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
 * @param headlineStyle the [headline] text style.
 * @param supportingStyle the [supporting] text style.
 * @param headlineMaxLines the maximum lines for [headline] before it is truncated.
 * @param supportingMaxLines the maximum lines for [supporting] before it is truncated.
 * @param radioSize the diameter of the radio.
 * @param colors the row color set (e.g. a warning-colored supporting line); defaults to [JengaSelectionListItemDefaults.colors].
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
    minHeight: Dp = JengaSelectionListItemDefaults.minHeight,
    contentPadding: PaddingValues = JengaSelectionListItemDefaults.contentPadding,
    contentSpacing: Dp = JengaSelectionListItemDefaults.contentSpacing,
    headlineStyle: TextStyle = JengaSelectionListItemDefaults.headlineStyle,
    supportingStyle: TextStyle = JengaSelectionListItemDefaults.supportingStyle,
    headlineMaxLines: Int = 1,
    supportingMaxLines: Int = 1,
    radioSize: Dp = JengaSelectionListItemDefaults.radioSize,
    colors: JengaListItemColors = JengaSelectionListItemDefaults.colors(),
    trailingContent: (@Composable () -> Unit)? = null,
) {
    val control: @Composable () -> Unit = {
        JengaRadioButton(selected = selected, onClick = null, enabled = enabled, size = radioSize)
    }
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
        headlineStyle = headlineStyle,
        supportingStyle = supportingStyle,
        headlineMaxLines = headlineMaxLines,
        supportingMaxLines = supportingMaxLines,
        colors = colors,
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
 * @param headlineStyle the [headline] text style.
 * @param supportingStyle the [supporting] text style.
 * @param headlineMaxLines the maximum lines for [headline] before it is truncated.
 * @param supportingMaxLines the maximum lines for [supporting] before it is truncated.
 * @param trailingContent optional end slot (e.g. a count), before a trailing control; tapping it toggles the row.
 * @param checkboxShape the checkbox box shape.
 * @param colors the row color set; its disabled colors apply when [enabled] is false, so a locked row
 *   can keep its label in the normal text color while only the box fades.
 * @param checkboxColors the checkbox color set (e.g. a faded checked fill when disabled).
 * @param loading whether a spinner replaces the checkbox (e.g. while the change saves); taps are ignored.
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
    minHeight: Dp = JengaSelectionListItemDefaults.minHeight,
    contentPadding: PaddingValues = JengaSelectionListItemDefaults.contentPadding,
    contentSpacing: Dp = JengaSelectionListItemDefaults.contentSpacing,
    headlineStyle: TextStyle = JengaSelectionListItemDefaults.headlineStyle,
    supportingStyle: TextStyle = JengaSelectionListItemDefaults.supportingStyle,
    headlineMaxLines: Int = 1,
    supportingMaxLines: Int = 1,
    trailingContent: (@Composable () -> Unit)? = null,
    checkboxShape: Shape = JengaSelectionListItemDefaults.checkboxShape,
    colors: JengaListItemColors = JengaSelectionListItemDefaults.colors(),
    checkboxColors: JengaCheckboxColors = JengaCheckboxDefaults.colors(),
    loading: Boolean = false,
) {
    val control: @Composable () -> Unit = {
        if (loading) {
            Box(modifier = Modifier.size(JengaCheckboxDefaults.Size), contentAlignment = Alignment.Center) {
                JengaCircularProgressIndeterminate(size = JengaSelectionListItemDefaults.LoadingIndicatorSize)
            }
        } else {
            JengaCheckbox(
                checked = checked,
                onCheckedChange = null,
                enabled = enabled,
                shape = checkboxShape,
                colors = checkboxColors,
            )
        }
    }
    JengaListItem(
        headline = headline,
        modifier = modifier.toggleable(
            value = checked,
            enabled = enabled && !loading,
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
        headlineStyle = headlineStyle,
        supportingStyle = supportingStyle,
        headlineMaxLines = headlineMaxLines,
        supportingMaxLines = supportingMaxLines,
        colors = colors,
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
