package io.github.joelkanyi.jenga.component.textfield

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
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import io.github.joelkanyi.jenga.component.icon.JengaIcon
import io.github.joelkanyi.jenga.component.text.JengaText
import io.github.joelkanyi.jenga.theme.JengaTheme
import io.github.joelkanyi.jenga.theme.LocalJengaContentColor

/**
 * A field that shows a chosen value and opens a picker when tapped, instead of
 * taking typed text. It looks like a [JengaTextField]; pair it with a picker such
 * as [io.github.joelkanyi.jenga.component.feedback.JengaListSheet].
 *
 * @sample io.github.joelkanyi.jenga.samples.JengaSelectFieldSample
 *
 * @param value the chosen value's label, or null when nothing is chosen.
 * @param onClick called when the field is tapped (open the picker here).
 * @param modifier the [Modifier] for the whole field (label + box + support).
 * @param label optional label shown above the field.
 * @param placeholder optional hint shown while [value] is null.
 * @param status validation status; see [JengaTextFieldStatus].
 * @param supportingText optional helper/error text shown below the field.
 * @param enabled whether the field can be tapped.
 * @param leadingIcon optional icon at the start (inherits the icon color).
 * @param trailingIcon the end icon; a downward chevron by default.
 * @param shape the field shape; defaults to [JengaTextFieldDefaults.shape].
 * @param colors the color set; defaults to [JengaTextFieldDefaults.colors].
 * @param textStyle the value and placeholder text style.
 * @param minHeight the minimum height of the field box.
 * @param contentPadding padding inside the field box.
 * @param focusRingWidth space reserved around the box so it lines up with a [JengaTextField].
 */
@Composable
public fun JengaSelectField(
    value: String?,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    label: String? = null,
    placeholder: String? = null,
    status: JengaTextFieldStatus = JengaTextFieldStatus.Default,
    supportingText: String? = null,
    enabled: Boolean = true,
    leadingIcon: (@Composable () -> Unit)? = null,
    trailingIcon: @Composable () -> Unit = {
        JengaIcon(JengaTheme.icons.chevron, contentDescription = null, modifier = Modifier.rotate(90f))
    },
    shape: Shape = JengaTextFieldDefaults.shape,
    colors: JengaTextFieldColors = JengaTextFieldDefaults.colors(),
    textStyle: TextStyle = JengaTextFieldDefaults.textStyle,
    minHeight: Dp = JengaTextFieldDefaults.minHeight,
    contentPadding: PaddingValues = JengaTextFieldDefaults.contentPadding,
    focusRingWidth: Dp = JengaTextFieldDefaults.FocusRingWidth,
) {
    val c = JengaTheme.colors
    val borderColor = when {
        !enabled -> colors.disabledBorder
        status == JengaTextFieldStatus.Error -> c.error
        status == JengaTextFieldStatus.Success -> c.success
        else -> colors.border
    }
    val borderWidth = if (status != JengaTextFieldStatus.Default) 2.dp else 1.dp
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(JengaTheme.spacing.xs),
    ) {
        if (label != null) {
            JengaText(text = label, style = JengaTheme.typography.bodySmall, color = c.textSecondary)
        }
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(focusRingWidth)
                .clip(shape)
                .background(if (enabled) colors.container else colors.disabledContainer)
                .border(borderWidth, borderColor, shape)
                .clickable(enabled = enabled, role = Role.DropdownList, onClick = onClick)
                .defaultMinSize(minHeight = minHeight)
                .padding(contentPadding),
            horizontalArrangement = Arrangement.spacedBy(JengaTheme.spacing.sm),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            CompositionLocalProvider(LocalJengaContentColor provides colors.icon) {
                leadingIcon?.invoke()
            }
            Box(modifier = Modifier.weight(1f)) {
                JengaText(
                    text = value ?: placeholder.orEmpty(),
                    style = textStyle,
                    color = when {
                        !enabled -> colors.disabledText
                        value == null -> colors.placeholder
                        else -> colors.text
                    },
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                )
            }
            CompositionLocalProvider(LocalJengaContentColor provides colors.icon) {
                trailingIcon()
            }
        }
        if (supportingText != null) {
            JengaText(
                text = supportingText,
                style = JengaTheme.typography.caption,
                color = when (status) {
                    JengaTextFieldStatus.Error -> c.error
                    JengaTextFieldStatus.Success -> c.success
                    JengaTextFieldStatus.Default -> c.textMuted
                },
            )
        }
    }
}
