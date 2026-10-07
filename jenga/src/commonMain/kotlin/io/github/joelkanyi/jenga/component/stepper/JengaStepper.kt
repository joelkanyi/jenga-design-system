package io.github.joelkanyi.jenga.component.stepper

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.minimumInteractiveComponentSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import dev.drewhamilton.poko.Poko
import io.github.joelkanyi.jenga.component.icon.JengaIcon
import io.github.joelkanyi.jenga.component.text.JengaText
import io.github.joelkanyi.jenga.theme.JengaTheme

/** Resolved colors for a [JengaStepper]. Override via [JengaStepperDefaults.colors]. */
@Poko
@Immutable
public class JengaStepperColors(
    public val track: Color,
    public val button: Color,
    public val buttonContent: Color,
    public val value: Color,
    public val disabledButton: Color,
    public val disabledContent: Color,
) {
    public fun copy(
        track: Color = this.track,
        button: Color = this.button,
        buttonContent: Color = this.buttonContent,
        value: Color = this.value,
        disabledButton: Color = this.disabledButton,
        disabledContent: Color = this.disabledContent,
    ): JengaStepperColors = JengaStepperColors(track, button, buttonContent, value, disabledButton, disabledContent)
}

/** Defaults and token mappings for [JengaStepper]. */
public object JengaStepperDefaults {
    /** The pill track shape. */
    public val shape: Shape
        @Composable get() = JengaTheme.shapes.pill

    /** Diameter of each round +/- button (its touch target expands to 48dp). */
    public val ButtonSize: Dp = 38.dp

    /** Minimum width of the value between the buttons. */
    public val ValueMinWidth: Dp = 34.dp

    /** Shape of the +/- buttons. */
    public val buttonShape: Shape
        @Composable get() = JengaTheme.shapes.pill

    /** Shadow under an enabled +/- button. */
    public val buttonElevation: Dp
        @Composable get() = JengaTheme.elevation.sm

    /** Padding inside the track, around the buttons. */
    public val trackPadding: Dp
        @Composable get() = JengaTheme.spacing.xs

    /** Value text style. */
    public val textStyle: TextStyle
        @Composable get() = JengaTheme.typography.titleLarge

    /** Themed colors for the track, buttons and value. */
    @Composable
    public fun colors(): JengaStepperColors {
        val c = JengaTheme.colors
        return JengaStepperColors(
            track = c.surfaceVariant,
            button = c.surface,
            buttonContent = c.textPrimary,
            value = c.textPrimary,
            disabledButton = c.surfaceDisabled,
            disabledContent = c.contentDisabled,
        )
    }
}

/**
 * A compact numeric stepper: a pill track with round decrement/increment
 * buttons around a centered value. Replaces bare `−`/`+` glyph pairs and gives
 * both buttons real 48dp touch targets and content descriptions.
 *
 * Generic and domain-neutral: use it for servings, quantities, household size,
 * times, or any bounded integer. Colors and icons come from Jenga tokens.
 *
 * With [editable] the value can also be typed; a typed number outside
 * [min]..[max] is not applied. With [onRemove], the decrement button becomes a
 * remove button once another step down would pass [min] (e.g. a cart quantity).
 *
 * @param value the current value.
 * @param onValueChange called with the clamped new value when a button is tapped.
 * @param modifier the [Modifier] for this stepper.
 * @param min the inclusive lower bound; the decrement button disables at it.
 * @param max the inclusive upper bound; the increment button disables at it.
 * @param step the increment/decrement amount.
 * @param enabled whether the whole control is interactive.
 * @param decrementContentDescription accessibility label for the `−` button.
 * @param incrementContentDescription accessibility label for the `+` button.
 * @param valueLabel formats the centered value (e.g. to add a unit); not used while [editable].
 * @param colors the color set; defaults to [JengaStepperDefaults.colors].
 * @param editable whether the value can be typed.
 * @param onRemove called when the remove button is tapped; null keeps a disabled `−` at [min].
 * @param removeContentDescription accessibility label for the remove button.
 * @param shape the track shape.
 * @param buttonShape the +/- button shape.
 * @param buttonSize the +/- button size.
 * @param buttonElevation shadow under an enabled +/- button.
 * @param trackPadding padding inside the track, around the buttons.
 * @param valueMinWidth minimum width of the value.
 * @param textStyle the value text style.
 */
@Composable
public fun JengaStepper(
    value: Int,
    onValueChange: (Int) -> Unit,
    modifier: Modifier = Modifier,
    min: Int = 0,
    max: Int = Int.MAX_VALUE,
    step: Int = 1,
    enabled: Boolean = true,
    decrementContentDescription: String? = null,
    incrementContentDescription: String? = null,
    valueLabel: (Int) -> String = { it.toString() },
    colors: JengaStepperColors = JengaStepperDefaults.colors(),
    editable: Boolean = false,
    onRemove: (() -> Unit)? = null,
    removeContentDescription: String? = null,
    shape: Shape = JengaStepperDefaults.shape,
    buttonShape: Shape = JengaStepperDefaults.buttonShape,
    buttonSize: Dp = JengaStepperDefaults.ButtonSize,
    buttonElevation: Dp = JengaStepperDefaults.buttonElevation,
    trackPadding: Dp = JengaStepperDefaults.trackPadding,
    valueMinWidth: Dp = JengaStepperDefaults.ValueMinWidth,
    textStyle: TextStyle = JengaStepperDefaults.textStyle,
) {
    val canDecrement = enabled && value - step >= min
    val canIncrement = enabled && value + step <= max
    val showRemove = enabled && !canDecrement && onRemove != null
    val button: @Composable (ImageVector, String?, Boolean, () -> Unit) -> Unit = { icon, description, active, onClick ->
        StepperButton(
            icon = icon,
            contentDescription = description,
            enabled = active,
            colors = colors,
            shape = buttonShape,
            size = buttonSize,
            elevation = buttonElevation,
            onClick = onClick,
        )
    }
    Row(
        modifier = modifier
            .clip(shape)
            .background(colors.track)
            .padding(trackPadding),
        horizontalArrangement = Arrangement.spacedBy(JengaTheme.spacing.xxs),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        if (showRemove) {
            button(JengaTheme.icons.trash, removeContentDescription, true) { onRemove?.invoke() }
        } else {
            button(JengaTheme.icons.remove, decrementContentDescription, canDecrement) { onValueChange(value - step) }
        }
        val valueColor = if (enabled) colors.value else colors.disabledContent
        val valueModifier = Modifier
            .defaultMinSize(minWidth = valueMinWidth)
            .padding(horizontal = JengaTheme.spacing.xs)
        if (editable) {
            var text by remember(value) { mutableStateOf(value.toString()) }
            BasicTextField(
                value = text,
                onValueChange = { typed ->
                    text = typed.filter { it.isDigit() || (it == '-' && min < 0) }
                    text.toIntOrNull()?.takeIf { it in min..max }?.let(onValueChange)
                },
                modifier = valueModifier
                    .widthIn(min = valueMinWidth)
                    .onFocusChanged { if (!it.isFocused) text = value.toString() },
                enabled = enabled,
                textStyle = textStyle.copy(color = valueColor, textAlign = TextAlign.Center),
                cursorBrush = SolidColor(JengaTheme.colors.brand),
                singleLine = true,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            )
        } else {
            JengaText(
                text = valueLabel(value),
                style = textStyle,
                color = valueColor,
                textAlign = TextAlign.Center,
                modifier = valueModifier,
                maxLines = 1,
            )
        }
        button(JengaTheme.icons.add, incrementContentDescription, canIncrement) { onValueChange(value + step) }
    }
}

@Composable
private fun StepperButton(
    icon: ImageVector,
    contentDescription: String?,
    enabled: Boolean,
    colors: JengaStepperColors,
    shape: Shape,
    size: Dp,
    elevation: Dp,
    onClick: () -> Unit,
) {
    Box(
        modifier = Modifier
            .minimumInteractiveComponentSize()
            .size(size)
            .then(if (enabled) Modifier.shadow(elevation, shape) else Modifier)
            .clip(shape)
            .background(if (enabled) colors.button else colors.disabledButton)
            .clickable(
                enabled = enabled,
                role = Role.Button,
                onClick = onClick,
            ),
        contentAlignment = Alignment.Center,
    ) {
        JengaIcon(
            imageVector = icon,
            contentDescription = contentDescription,
            tint = if (enabled) colors.buttonContent else colors.disabledContent,
            size = JengaTheme.sizing.iconMedium,
        )
    }
}
