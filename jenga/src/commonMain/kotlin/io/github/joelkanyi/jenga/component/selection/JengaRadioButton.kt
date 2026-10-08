package io.github.joelkanyi.jenga.component.selection

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.selection.selectable
import androidx.compose.material3.minimumInteractiveComponentSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import dev.drewhamilton.poko.Poko
import io.github.joelkanyi.jenga.theme.JengaTheme

/** Resolved colors for a [JengaRadioButton]. Override via [JengaRadioButtonDefaults.colors]. */
@Poko
@Immutable
public class JengaRadioButtonColors(
    public val selectedRing: Color,
    public val unselectedRing: Color,
    public val disabledRing: Color,
    public val dot: Color,
    public val disabledDot: Color,
) {
    public fun copy(
        selectedRing: Color = this.selectedRing,
        unselectedRing: Color = this.unselectedRing,
        disabledRing: Color = this.disabledRing,
        dot: Color = this.dot,
        disabledDot: Color = this.disabledDot,
    ): JengaRadioButtonColors = JengaRadioButtonColors(
        selectedRing,
        unselectedRing,
        disabledRing,
        dot,
        disabledDot,
    )
}

/** Defaults and token mappings for [JengaRadioButton]. */
public object JengaRadioButtonDefaults {
    public val Size: Dp = 22.dp

    /** Themed colors for all states. */
    @Composable
    public fun colors(): JengaRadioButtonColors {
        val c = JengaTheme.colors
        return JengaRadioButtonColors(
            selectedRing = c.brand,
            unselectedRing = c.borderStrong,
            disabledRing = c.borderDisabled,
            dot = c.brand,
            disabledDot = c.contentDisabled,
        )
    }
}

/**
 * A radio button for single-choice selection within a group.
 *
 * @sample io.github.joelkanyi.jenga.samples.JengaRadioButtonSample
 *
 * @param selected whether this option is selected.
 * @param onClick called when tapped; pass `null` for read-only/externally-driven (e.g. inside a
 *   selectable row), which also drops the control's own 48dp touch area.
 * @param modifier the [Modifier] for this radio button.
 * @param enabled whether the control is interactive.
 * @param colors the color set; defaults to [JengaRadioButtonDefaults.colors].
 * @param size the diameter of the radio.
 */
@Composable
public fun JengaRadioButton(
    selected: Boolean,
    onClick: (() -> Unit)?,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    colors: JengaRadioButtonColors = JengaRadioButtonDefaults.colors(),
    size: Dp = JengaRadioButtonDefaults.Size,
) {
    val ring = when {
        !enabled -> colors.disabledRing
        selected -> colors.selectedRing
        else -> colors.unselectedRing
    }
    val dot = if (enabled) colors.dot else colors.disabledDot

    val selectableModifier = if (onClick != null) {
        Modifier.selectable(selected = selected, enabled = enabled, role = Role.RadioButton, onClick = onClick)
    } else {
        Modifier
    }

    Canvas(
        modifier = modifier
            .then(if (onClick != null) Modifier.minimumInteractiveComponentSize() else Modifier)
            .then(selectableModifier)
            .size(size),
    ) {
        val stroke = 2.dp.toPx()
        val radius = (this.size.minDimension - stroke) / 2f
        drawCircle(color = ring, radius = radius, style = androidx.compose.ui.graphics.drawscope.Stroke(width = stroke))
        if (selected) {
            drawCircle(color = dot, radius = this.size.minDimension * 0.25f)
        }
    }
}
