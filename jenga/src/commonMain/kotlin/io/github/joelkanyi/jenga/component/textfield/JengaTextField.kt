package io.github.joelkanyi.jenga.component.textfield

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsFocusedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import dev.drewhamilton.poko.Poko
import io.github.joelkanyi.jenga.component.text.JengaText
import io.github.joelkanyi.jenga.theme.JengaTheme
import io.github.joelkanyi.jenga.theme.LocalJengaContentColor

/** Validation status of a [JengaTextField], driving border and supporting-text color. */
public enum class JengaTextFieldStatus { Default, Error, Success }

/** Resolved colors for a [JengaTextField]. Override via [JengaTextFieldDefaults.colors]. */
@Poko
@Immutable
public class JengaTextFieldColors(
    public val container: Color,
    public val focusedContainer: Color,
    public val disabledContainer: Color,
    public val border: Color,
    public val focusedBorder: Color,
    public val disabledBorder: Color,
    public val focusRing: Color,
    public val text: Color,
    public val disabledText: Color,
    public val placeholder: Color,
    public val icon: Color,
) {
    public fun copy(
        container: Color = this.container,
        focusedContainer: Color = this.focusedContainer,
        disabledContainer: Color = this.disabledContainer,
        border: Color = this.border,
        focusedBorder: Color = this.focusedBorder,
        disabledBorder: Color = this.disabledBorder,
        focusRing: Color = this.focusRing,
        text: Color = this.text,
        disabledText: Color = this.disabledText,
        placeholder: Color = this.placeholder,
        icon: Color = this.icon,
    ): JengaTextFieldColors = JengaTextFieldColors(
        container,
        focusedContainer,
        disabledContainer,
        border,
        focusedBorder,
        disabledBorder,
        focusRing,
        text,
        disabledText,
        placeholder,
        icon,
    )
}

/** Defaults and token mappings for [JengaTextField]. */
public object JengaTextFieldDefaults {
    /** Default field shape. */
    public val shape: Shape
        @Composable get() = JengaTheme.shapes.control

    /** Width of the soft focus halo around the field; reserved even when unfocused. */
    public val FocusRingWidth: Dp = 3.dp

    /** Minimum height of the input box. */
    public val minHeight: Dp
        @Composable get() = JengaTheme.sizing.fieldHeight

    /** Padding inside the input box. */
    public val contentPadding: PaddingValues
        @Composable get() = PaddingValues(horizontal = JengaTheme.spacing.lg, vertical = JengaTheme.spacing.md)

    /** Input and placeholder text style. */
    public val textStyle: TextStyle
        @Composable get() = JengaTheme.typography.bodyMedium

    /** Themed colors. */
    @Composable
    public fun colors(): JengaTextFieldColors {
        val c = JengaTheme.colors
        return JengaTextFieldColors(
            container = c.surface,
            focusedContainer = c.surface,
            disabledContainer = c.surfaceDisabled,
            border = c.borderStrong,
            focusedBorder = c.brand,
            disabledBorder = c.borderDisabled,
            focusRing = c.focusRing,
            text = c.textPrimary,
            disabledText = c.contentDisabled,
            placeholder = c.textFaint,
            icon = c.textMuted,
        )
    }
}

/**
 * A single- or multi-line text input.
 *
 * Built on Compose Foundation's `BasicTextField`, so its look is governed
 * entirely by Jenga tokens. The border reflects focus and [status]; an optional
 * [supportingText] sits below and is colored to match the status.
 *
 * @sample io.github.joelkanyi.jenga.samples.JengaTextFieldSample
 *
 * @param value the current text.
 * @param onValueChange called when the text changes.
 * @param modifier the [Modifier] for the whole field (label + box + support).
 * @param label optional label shown above the field.
 * @param placeholder optional hint shown when [value] is empty.
 * @param status validation status; see [JengaTextFieldStatus].
 * @param supportingText optional helper/error text shown below the field.
 * @param enabled whether the field is editable.
 * @param readOnly whether the field is read-only (focusable but not editable).
 * @param singleLine whether the field is constrained to one line.
 * @param leadingIcon optional icon at the start (inherits the content color).
 * @param trailingIcon optional icon at the end (inherits the content color).
 * @param visualTransformation transforms the displayed text (e.g. password).
 * @param keyboardOptions software-keyboard configuration.
 * @param keyboardActions IME action handlers.
 * @param shape the field shape; defaults to [JengaTextFieldDefaults.shape].
 * @param colors the color set; defaults to [JengaTextFieldDefaults.colors].
 * @param textStyle the input and placeholder text style.
 * @param minHeight the minimum height of the input box.
 * @param contentPadding padding inside the input box.
 * @param focusRingWidth width of the soft focus halo; `0.dp` removes it.
 */
@Composable
public fun JengaTextField(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    label: String? = null,
    placeholder: String? = null,
    status: JengaTextFieldStatus = JengaTextFieldStatus.Default,
    supportingText: String? = null,
    enabled: Boolean = true,
    readOnly: Boolean = false,
    singleLine: Boolean = true,
    leadingIcon: (@Composable () -> Unit)? = null,
    trailingIcon: (@Composable () -> Unit)? = null,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    keyboardActions: KeyboardActions = KeyboardActions.Default,
    shape: Shape = JengaTextFieldDefaults.shape,
    colors: JengaTextFieldColors = JengaTextFieldDefaults.colors(),
    textStyle: TextStyle = JengaTextFieldDefaults.textStyle,
    minHeight: Dp = JengaTextFieldDefaults.minHeight,
    contentPadding: PaddingValues = JengaTextFieldDefaults.contentPadding,
    focusRingWidth: Dp = JengaTextFieldDefaults.FocusRingWidth,
) {
    val c = JengaTheme.colors
    val interactionSource = remember { MutableInteractionSource() }
    val focused by interactionSource.collectIsFocusedAsState()

    val borderColor = when {
        !enabled -> colors.disabledBorder
        status == JengaTextFieldStatus.Error -> c.error
        status == JengaTextFieldStatus.Success -> c.success
        focused -> colors.focusedBorder
        else -> colors.border
    }
    val borderWidth = if (focused || status != JengaTextFieldStatus.Default) 2.dp else 1.dp
    val container = when {
        !enabled -> colors.disabledContainer
        focused -> colors.focusedContainer
        else -> colors.container
    }
    val contentColor = if (enabled) colors.text else colors.disabledText

    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(JengaTheme.spacing.xs),
    ) {
        if (label != null) {
            JengaText(
                text = label,
                style = JengaTheme.typography.bodySmall,
                color = c.textSecondary,
            )
        }

        BasicTextField(
            value = value,
            onValueChange = onValueChange,
            modifier = Modifier.fillMaxWidth(),
            enabled = enabled,
            readOnly = readOnly,
            textStyle = textStyle.copy(color = contentColor),
            cursorBrush = SolidColor(c.brand),
            singleLine = singleLine,
            visualTransformation = visualTransformation,
            keyboardOptions = keyboardOptions,
            keyboardActions = keyboardActions,
            interactionSource = interactionSource,
            decorationBox = { innerTextField ->
                Row(
                    modifier = Modifier
                        // Soft focus halo that hugs the field: a translucent fill,
                        // not a second hard stroke. 3dp is reserved always so focus
                        // doesn't shift layout.
                        .clip(shape)
                        .background(if (focused) colors.focusRing else Color.Transparent)
                        .padding(focusRingWidth)
                        .clip(shape)
                        .background(container)
                        .border(borderWidth, borderColor, shape)
                        .defaultMinSize(minHeight = minHeight)
                        .padding(contentPadding),
                    horizontalArrangement = Arrangement.spacedBy(JengaTheme.spacing.sm),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    CompositionLocalProvider(LocalJengaContentColor provides colors.icon) {
                        leadingIcon?.invoke()
                    }
                    Box(modifier = Modifier.weight(1f)) {
                        if (value.isEmpty() && placeholder != null) {
                            JengaText(
                                text = placeholder,
                                style = textStyle,
                                color = colors.placeholder,
                                maxLines = 1,
                            )
                        }
                        innerTextField()
                    }
                    CompositionLocalProvider(LocalJengaContentColor provides colors.icon) {
                        trailingIcon?.invoke()
                    }
                }
            },
        )

        if (supportingText != null) {
            val supportColor = when (status) {
                JengaTextFieldStatus.Error -> c.error
                JengaTextFieldStatus.Success -> c.success
                JengaTextFieldStatus.Default -> c.textMuted
            }
            JengaText(
                text = supportingText,
                style = JengaTheme.typography.caption,
                color = supportColor,
            )
        }
    }
}
