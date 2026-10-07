package io.github.joelkanyi.jenga.component.search

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import io.github.joelkanyi.jenga.component.icon.JengaIcon
import io.github.joelkanyi.jenga.component.text.JengaText
import io.github.joelkanyi.jenga.component.textfield.JengaTextField
import io.github.joelkanyi.jenga.component.textfield.JengaTextFieldColors
import io.github.joelkanyi.jenga.component.textfield.JengaTextFieldDefaults
import io.github.joelkanyi.jenga.theme.JengaTheme
import io.github.joelkanyi.jenga.theme.LocalJengaContentColor

/** Defaults for [JengaSearchField] and [JengaSearchTrigger]. */
public object JengaSearchFieldDefaults {
    /** Default shape: a pill. */
    public val shape: Shape
        @Composable get() = JengaTheme.shapes.pill
}

/**
 * A search input: a [JengaTextField] with a leading search icon and a clear
 * button that appears once there's text. While the query is empty, the
 * [trailingContent] slot (e.g. a scan button) takes the clear button's place.
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
 * @param trailingContent optional end slot shown while the query is empty.
 * @param supportingContent optional slot below the field (e.g. a hint).
 * @param shape the field shape; defaults to [JengaSearchFieldDefaults.shape].
 * @param colors the color set; defaults to [JengaTextFieldDefaults.colors].
 * @param textStyle the query and placeholder text style.
 * @param minHeight the minimum height of the field.
 * @param contentPadding padding inside the field.
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
    trailingContent: (@Composable () -> Unit)? = null,
    supportingContent: (@Composable () -> Unit)? = null,
    shape: Shape = JengaSearchFieldDefaults.shape,
    colors: JengaTextFieldColors = JengaTextFieldDefaults.colors(),
    textStyle: TextStyle = JengaTextFieldDefaults.textStyle,
    minHeight: Dp = JengaTextFieldDefaults.minHeight,
    contentPadding: PaddingValues = JengaTextFieldDefaults.contentPadding,
) {
    val clear: @Composable () -> Unit = {
        JengaIcon(
            imageVector = JengaTheme.icons.close,
            contentDescription = clearContentDescription,
            modifier = Modifier.clickable {
                if (onClear != null) onClear() else onValueChange("")
            },
        )
    }
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(JengaTheme.spacing.xs),
    ) {
        JengaTextField(
            value = value,
            onValueChange = onValueChange,
            modifier = Modifier.fillMaxWidth(),
            placeholder = placeholder,
            enabled = enabled,
            singleLine = true,
            leadingIcon = { JengaIcon(JengaTheme.icons.search, contentDescription = null) },
            trailingIcon = if (value.isNotEmpty()) clear else trailingContent,
            shape = shape,
            colors = colors,
            textStyle = textStyle,
            minHeight = minHeight,
            contentPadding = contentPadding,
        )
        if (supportingContent != null) {
            CompositionLocalProvider(LocalJengaContentColor provides JengaTheme.colors.textMuted) {
                supportingContent()
            }
        }
    }
}

/**
 * A button that looks like a [JengaSearchField], for entry points that open a
 * search screen or mode instead of taking text in place. The [trailingContent]
 * slot (e.g. a scan button) handles its own clicks.
 *
 * @sample io.github.joelkanyi.jenga.samples.JengaSearchTriggerSample
 *
 * @param placeholder the hint text shown in the trigger.
 * @param onClick called when the trigger is tapped.
 * @param modifier the [Modifier] for this trigger.
 * @param trailingContent optional end slot.
 * @param shape the trigger shape; defaults to [JengaSearchFieldDefaults.shape].
 * @param colors the color set; reads the container, border, placeholder and icon colors.
 * @param textStyle the placeholder text style.
 * @param minHeight the minimum height of the trigger.
 * @param contentPadding padding inside the trigger.
 */
@Composable
public fun JengaSearchTrigger(
    placeholder: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    trailingContent: (@Composable () -> Unit)? = null,
    shape: Shape = JengaSearchFieldDefaults.shape,
    colors: JengaTextFieldColors = JengaTextFieldDefaults.colors(),
    textStyle: TextStyle = JengaTextFieldDefaults.textStyle,
    minHeight: Dp = JengaTextFieldDefaults.minHeight,
    contentPadding: PaddingValues = JengaTextFieldDefaults.contentPadding,
) {
    Row(
        modifier = modifier
            .clip(shape)
            .background(colors.container)
            .border(1.dp, colors.border, shape)
            .clickable(role = Role.Button, onClick = onClick)
            .defaultMinSize(minHeight = minHeight)
            .padding(contentPadding),
        horizontalArrangement = Arrangement.spacedBy(JengaTheme.spacing.sm),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        CompositionLocalProvider(LocalJengaContentColor provides colors.icon) {
            JengaIcon(JengaTheme.icons.search, contentDescription = null)
        }
        Box(modifier = Modifier.weight(1f)) {
            JengaText(text = placeholder, style = textStyle, color = colors.placeholder, maxLines = 1)
        }
        if (trailingContent != null) {
            CompositionLocalProvider(LocalJengaContentColor provides colors.icon) {
                trailingContent()
            }
        }
    }
}
