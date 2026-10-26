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
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.Immutable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import dev.drewhamilton.poko.Poko
import io.github.joelkanyi.jenga.component.icon.JengaIcon
import io.github.joelkanyi.jenga.component.text.JengaText
import io.github.joelkanyi.jenga.component.textfield.JengaTextField
import io.github.joelkanyi.jenga.component.textfield.JengaTextFieldColors
import io.github.joelkanyi.jenga.component.textfield.JengaTextFieldDefaults
import io.github.joelkanyi.jenga.theme.JengaTheme
import io.github.joelkanyi.jenga.theme.LocalJengaContentColor

/**
 * Theme-wide style for every [JengaSearchField] or every [JengaSearchTrigger], set through
 * [io.github.joelkanyi.jenga.theme.JengaComponentStyles.searchField] and
 * [io.github.joelkanyi.jenga.theme.JengaComponentStyles.searchTrigger]. A `null` value falls back to
 * the theme's text field style, then to Jenga's default; arguments passed to a call still win.
 *
 * @property shape the field shape; falls back to [JengaSearchFieldDefaults.shape], not the text
 *   field shape.
 * @property minHeight the minimum height.
 * @property contentPadding padding inside the field.
 * @property textStyle the query and placeholder text style.
 * @property colors the color set.
 */
@Poko
@Immutable
public class JengaSearchFieldStyle(
    public val shape: Shape? = null,
    public val minHeight: Dp? = null,
    public val contentPadding: PaddingValues? = null,
    public val textStyle: TextStyle? = null,
    public val colors: JengaTextFieldColors? = null,
) {
    public fun copy(
        shape: Shape? = this.shape,
        minHeight: Dp? = this.minHeight,
        contentPadding: PaddingValues? = this.contentPadding,
        textStyle: TextStyle? = this.textStyle,
        colors: JengaTextFieldColors? = this.colors,
    ): JengaSearchFieldStyle = JengaSearchFieldStyle(shape, minHeight, contentPadding, textStyle, colors)
}

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
 * @param shape the field shape; defaults to the theme's search field style, else
 *   [JengaSearchFieldDefaults.shape].
 * @param colors the color set; defaults to the theme's search field style, else
 *   [JengaTextFieldDefaults.colors].
 * @param textStyle the query and placeholder text style.
 * @param minHeight the minimum height of the field.
 * @param contentPadding padding inside the field.
 * @param keyboardOptions software-keyboard configuration (e.g. a number pad, the Search key).
 * @param keyboardActions IME action handlers (e.g. run the search when Search is pressed).
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
    shape: Shape = JengaTheme.components.searchField?.shape ?: JengaSearchFieldDefaults.shape,
    colors: JengaTextFieldColors = JengaTheme.components.searchField?.colors ?: JengaTextFieldDefaults.colors(),
    textStyle: TextStyle = JengaTheme.components.searchField?.textStyle ?: JengaTextFieldDefaults.textStyle,
    minHeight: Dp = JengaTheme.components.searchField?.minHeight ?: JengaTextFieldDefaults.minHeight,
    contentPadding: PaddingValues = JengaTheme.components.searchField?.contentPadding ?: JengaTextFieldDefaults.contentPadding,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    keyboardActions: KeyboardActions = KeyboardActions.Default,
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
            keyboardOptions = keyboardOptions,
            keyboardActions = keyboardActions,
        )
        if (supportingContent != null) {
            CompositionLocalProvider(LocalJengaContentColor provides JengaTheme.colors.textMuted) {
                supportingContent()
            }
        }
    }
}

@Deprecated("Kept for binary compatibility", level = DeprecationLevel.HIDDEN)
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
    shape: Shape = JengaTheme.components.searchField?.shape ?: JengaSearchFieldDefaults.shape,
    colors: JengaTextFieldColors = JengaTheme.components.searchField?.colors ?: JengaTextFieldDefaults.colors(),
    textStyle: TextStyle = JengaTheme.components.searchField?.textStyle ?: JengaTextFieldDefaults.textStyle,
    minHeight: Dp = JengaTheme.components.searchField?.minHeight ?: JengaTextFieldDefaults.minHeight,
    contentPadding: PaddingValues = JengaTheme.components.searchField?.contentPadding ?: JengaTextFieldDefaults.contentPadding,
) {
    JengaSearchField(
        value = value,
        onValueChange = onValueChange,
        placeholder = placeholder,
        clearContentDescription = clearContentDescription,
        modifier = modifier,
        enabled = enabled,
        onClear = onClear,
        trailingContent = trailingContent,
        supportingContent = supportingContent,
        shape = shape,
        colors = colors,
        textStyle = textStyle,
        minHeight = minHeight,
        contentPadding = contentPadding,
        keyboardOptions = KeyboardOptions.Default,
    )
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
 * @param shape the trigger shape; defaults to the theme's search trigger style, else
 *   [JengaSearchFieldDefaults.shape].
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
    shape: Shape = JengaTheme.components.searchTrigger?.shape ?: JengaSearchFieldDefaults.shape,
    colors: JengaTextFieldColors = JengaTheme.components.searchTrigger?.colors ?: JengaTextFieldDefaults.colors(),
    textStyle: TextStyle = JengaTheme.components.searchTrigger?.textStyle ?: JengaTextFieldDefaults.textStyle,
    minHeight: Dp = JengaTheme.components.searchTrigger?.minHeight ?: JengaTextFieldDefaults.minHeight,
    contentPadding: PaddingValues = JengaTheme.components.searchTrigger?.contentPadding ?: JengaTextFieldDefaults.contentPadding,
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
