package io.github.joelkanyi.jenga.component.camera

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import io.github.joelkanyi.jenga.theme.JengaTheme

/** Defaults for [JengaShutterButton]. */
public object JengaShutterButtonDefaults {
    /** The outer diameter of the button. */
    public val Size: Dp = 72.dp

    /** The width of the outer ring. */
    public val RingWidth: Dp = 4.dp

    /** The diameter of the inner disc. */
    public val DiscSize: Dp = 56.dp
}

/**
 * A round camera shutter button: a ring around a filled disc, in the overlay
 * content color so it reads on a camera preview. The disc shrinks while pressed.
 *
 * @sample io.github.joelkanyi.jenga.samples.JengaShutterButtonSample
 *
 * @param onClick called when the button is tapped.
 * @param contentDescription accessibility label (e.g. "Take photo").
 * @param modifier the [Modifier] for the button.
 * @param enabled whether the button can be tapped; dimmed when false.
 */
@Composable
public fun JengaShutterButton(
    onClick: () -> Unit,
    contentDescription: String,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
) {
    val color = JengaTheme.colors.onOverlay
    val interaction = remember { MutableInteractionSource() }
    val pressed by interaction.collectIsPressedAsState()
    val discScale by animateFloatAsState(if (pressed) PressedScale else 1f, label = "shutter")
    Box(
        modifier = modifier
            .size(JengaShutterButtonDefaults.Size)
            .alpha(if (enabled) 1f else DisabledAlpha)
            .clip(JengaTheme.shapes.pill)
            .border(JengaShutterButtonDefaults.RingWidth, color, JengaTheme.shapes.pill)
            .clickable(interactionSource = interaction, indication = null, enabled = enabled, role = Role.Button, onClick = onClick)
            .semantics { this.contentDescription = contentDescription },
        contentAlignment = Alignment.Center,
    ) {
        Box(
            modifier = Modifier
                .size(JengaShutterButtonDefaults.DiscSize)
                .scale(discScale)
                .clip(JengaTheme.shapes.pill)
                .background(color),
        )
    }
}

private const val PressedScale = 0.88f
private const val DisabledAlpha = 0.4f
