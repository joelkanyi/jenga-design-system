package io.github.joelkanyi.jenga.component.stepindicator

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.clearAndSetSemantics
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.selected
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import dev.drewhamilton.poko.Poko
import io.github.joelkanyi.jenga.component.icon.JengaIcon
import io.github.joelkanyi.jenga.component.icon.JengaIcons
import io.github.joelkanyi.jenga.component.text.JengaText
import io.github.joelkanyi.jenga.component.timeline.JengaTimelineMarker
import io.github.joelkanyi.jenga.component.timeline.JengaTimelineMarkerStyle
import io.github.joelkanyi.jenga.theme.JengaTheme

/** The state of one step in a [JengaStepIndicator]. */
public enum class JengaStepState {
    /** Finished: a filled circle with a check. */
    Done,

    /** The step the user is on: a ring with a dot. */
    Current,

    /** Not reached yet: an outline ring. */
    Pending,

    /** Finished and no longer editable: a filled circle with a lock. */
    Locked,
}

/** One step in a [JengaStepIndicator]: its [label] and [state]. */
@Poko
@Immutable
public class JengaStep(
    public val label: String,
    public val state: JengaStepState,
)

/** Resolved colors for a [JengaStepIndicator]. Override via [JengaStepIndicatorDefaults.colors]. */
@Poko
@Immutable
public class JengaStepIndicatorColors(
    public val active: Color,
    public val onActive: Color,
    public val pending: Color,
    public val line: Color,
    public val label: Color,
    public val currentLabel: Color,
) {
    public fun copy(
        active: Color = this.active,
        onActive: Color = this.onActive,
        pending: Color = this.pending,
        line: Color = this.line,
        label: Color = this.label,
        currentLabel: Color = this.currentLabel,
    ): JengaStepIndicatorColors = JengaStepIndicatorColors(active, onActive, pending, line, label, currentLabel)
}

/** Defaults and token mappings for [JengaStepIndicator]. */
public object JengaStepIndicatorDefaults {
    /** The diameter of a step circle. */
    public val NodeSize: Dp = 28.dp

    /** The widest a step column gets; columns shrink equally when the row is narrower. */
    public val ColumnWidth: Dp = 60.dp

    /** Themed colors. */
    @Composable
    public fun colors(): JengaStepIndicatorColors {
        val c = JengaTheme.colors
        return JengaStepIndicatorColors(
            active = c.brand,
            onActive = c.onBrand,
            pending = c.borderStrong,
            line = c.border,
            label = c.textMuted,
            currentLabel = c.textPrimary,
        )
    }
}

/**
 * A horizontal row of steps joined by lines, with a label under each step, for
 * multi-step flows such as sign-up, verification or checkout.
 *
 * A line is drawn in the active color once the step on its left is done and the
 * next one has been reached (done, current or locked).
 *
 * @sample io.github.joelkanyi.jenga.samples.JengaStepIndicatorSample
 *
 * @param steps the steps, in order.
 * @param stepContentDescription accessibility label for a step, given its index
 *   (e.g. "Step 2 of 3, Payment, current step").
 * @param modifier the [Modifier] for the row.
 * @param onStepClick called with a step's index when it is tapped; null makes the
 *   steps not tappable. Which taps to act on is up to the caller.
 * @param colors the color set; defaults to [JengaStepIndicatorDefaults.colors].
 */
@Composable
public fun JengaStepIndicator(
    steps: List<JengaStep>,
    stepContentDescription: (index: Int, step: JengaStep) -> String,
    modifier: Modifier = Modifier,
    onStepClick: ((index: Int) -> Unit)? = null,
    colors: JengaStepIndicatorColors = JengaStepIndicatorDefaults.colors(),
) {
    BoxWithConstraints(modifier = modifier.fillMaxWidth()) {
        val columnWidth = columnWidth(rowWidth = maxWidth, stepCount = steps.size)
        Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.Top) {
            steps.forEachIndexed { index, step ->
                StepColumn(
                    step = step,
                    width = columnWidth,
                    index = index,
                    description = stepContentDescription(index, step),
                    onClick = onStepClick,
                    colors = colors,
                )
                val next = steps.getOrNull(index + 1)
                if (next != null) {
                    StepLine(reached = lineReached(step, next), colors = colors, modifier = Modifier.weight(1f))
                }
            }
        }
    }
}

@Composable
private fun StepColumn(
    step: JengaStep,
    width: Dp,
    index: Int,
    description: String,
    onClick: ((index: Int) -> Unit)?,
    colors: JengaStepIndicatorColors,
) {
    val tap = if (onClick == null) Modifier else Modifier.clickable(role = Role.Button) { onClick(index) }
    Column(
        modifier = Modifier
            .width(width)
            .then(tap)
            .semantics(mergeDescendants = true) {
                contentDescription = description
                selected = step.state == JengaStepState.Current
            },
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(6.dp),
    ) {
        StepNode(state = step.state, colors = colors)
        JengaText(
            text = step.label,
            style = JengaTheme.typography.caption.copy(fontWeight = labelWeight(step.state)),
            color = labelColor(step.state, colors),
            textAlign = TextAlign.Center,
            maxLines = 2,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier.clearAndSetSemantics {},
        )
    }
}

@Composable
private fun StepNode(state: JengaStepState, colors: JengaStepIndicatorColors) {
    val size = JengaStepIndicatorDefaults.NodeSize
    when (state) {
        JengaStepState.Done -> JengaTimelineMarker(color = colors.active, size = size, contentColor = colors.onActive) {
            JengaIcon(JengaIcons.Check, contentDescription = null, size = size * 0.55f)
        }
        JengaStepState.Locked -> JengaTimelineMarker(color = colors.active, size = size, contentColor = colors.onActive) {
            JengaIcon(JengaIcons.Lock, contentDescription = null, size = size * 0.45f)
        }
        JengaStepState.Current -> JengaTimelineMarker(
            color = colors.active,
            style = JengaTimelineMarkerStyle.Ring,
            size = size,
            ringWidth = 2.dp,
        ) {
            Box(modifier = Modifier.size(size * 0.4f).clip(JengaTheme.shapes.pill).background(colors.active))
        }
        JengaStepState.Pending -> JengaTimelineMarker(
            color = colors.pending,
            style = JengaTimelineMarkerStyle.Ring,
            size = size,
            ringWidth = 1.5.dp,
        )
    }
}

@Composable
private fun StepLine(reached: Boolean, colors: JengaStepIndicatorColors, modifier: Modifier = Modifier) {
    val color = if (reached) colors.active else colors.line
    val animatedColor by animateColorAsState(color, animationSpec = tween(durationMillis = 300), label = "stepLine")
    Box(modifier = modifier.height(JengaStepIndicatorDefaults.NodeSize), contentAlignment = Alignment.Center) {
        Box(
            modifier = Modifier
                .padding(horizontal = 4.dp)
                .fillMaxWidth()
                .height(2.dp)
                .clip(JengaTheme.shapes.pill)
                .background(animatedColor),
        )
    }
}

private fun columnWidth(rowWidth: Dp, stepCount: Int): Dp {
    if (stepCount == 0) return JengaStepIndicatorDefaults.ColumnWidth
    val lineSpace = MinLineWidth * (stepCount - 1)
    return minOf(JengaStepIndicatorDefaults.ColumnWidth, (rowWidth - lineSpace) / stepCount)
}

private fun lineReached(left: JengaStep, right: JengaStep): Boolean = left.state == JengaStepState.Done && right.state != JengaStepState.Pending

private fun labelWeight(state: JengaStepState): FontWeight = when (state) {
    JengaStepState.Current -> FontWeight.Bold
    JengaStepState.Done -> FontWeight.SemiBold
    JengaStepState.Pending, JengaStepState.Locked -> FontWeight.Normal
}

private fun labelColor(state: JengaStepState, colors: JengaStepIndicatorColors): Color = when (state) {
    JengaStepState.Current -> colors.currentLabel
    JengaStepState.Done, JengaStepState.Locked -> colors.active
    JengaStepState.Pending -> colors.label
}

private val MinLineWidth = 12.dp
