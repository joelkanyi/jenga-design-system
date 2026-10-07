package io.github.joelkanyi.jenga.component.progress

import androidx.compose.animation.core.AnimationSpec
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.layout.layout
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import dev.drewhamilton.poko.Poko
import io.github.joelkanyi.jenga.component.text.JengaText
import io.github.joelkanyi.jenga.theme.JengaTheme

/** Tone of a determinate progress bar. */
public enum class JengaProgressTone {
    /** In progress, in the brand color. */
    Default,

    /** Finished, in the success color. */
    Success,

    /** Stopped or paused, in a neutral color. */
    Neutral,
}

/** Resolved colors for a determinate progress bar. Override via [JengaProgressDefaults.colors]. */
@Poko
@Immutable
public class JengaProgressColors(
    public val track: Color,
    public val indicator: Color,
) {
    public fun copy(
        track: Color = this.track,
        indicator: Color = this.indicator,
    ): JengaProgressColors = JengaProgressColors(track, indicator)
}

/** Defaults for the Jenga progress bars. */
public object JengaProgressDefaults {
    /** Duration of one indeterminate sweep (ms). Loop animation, hence outside
     *  the discrete [io.github.joelkanyi.jenga.foundation.motion.JengaMotion] scale. */
    public const val IndeterminateDurationMillis: Int = 1100

    /** Default track height. */
    public val TrackHeight: Dp = 6.dp

    /** Default track and indicator shape. */
    public val shape: Shape
        @Composable get() = JengaTheme.shapes.pill

    /** How the indicator animates to a new progress value. */
    public val animationSpec: AnimationSpec<Float>
        @Composable get() = tween(JengaTheme.motion.durationSlow, easing = JengaTheme.motion.standard)

    /** Gap between the label row and the bar in [JengaLabelledProgress]. */
    public val labelSpacing: Dp
        @Composable get() = JengaTheme.spacing.sm

    /** Label text style for [JengaLabelledProgress]. */
    public val labelStyle: TextStyle
        @Composable get() = JengaTheme.typography.caption

    /** Themed colors for [tone]. */
    @Composable
    public fun colors(tone: JengaProgressTone): JengaProgressColors {
        val c = JengaTheme.colors
        return JengaProgressColors(
            track = c.surfaceSunk,
            indicator = when (tone) {
                JengaProgressTone.Default -> c.brand
                JengaProgressTone.Success -> c.success
                JengaProgressTone.Neutral -> c.textMuted
            },
        )
    }
}

/**
 * A determinate linear progress bar. Changes to [progress] animate.
 *
 * @param progress the progress in `0f..1f` (coerced into range).
 * @param modifier the [Modifier] for this bar.
 * @param tone the bar tone; see [JengaProgressTone].
 * @param colors the color set; defaults to [JengaProgressDefaults.colors] for [tone].
 * @param trackHeight the bar height.
 * @param shape the track and indicator shape.
 * @param animationSpec how the indicator animates to a new [progress].
 *
 * @sample io.github.joelkanyi.jenga.samples.JengaLinearProgressSample
 */
@Composable
public fun JengaLinearProgress(
    progress: Float,
    modifier: Modifier = Modifier,
    tone: JengaProgressTone = JengaProgressTone.Default,
    colors: JengaProgressColors = JengaProgressDefaults.colors(tone),
    trackHeight: Dp = JengaProgressDefaults.TrackHeight,
    shape: Shape = JengaProgressDefaults.shape,
    animationSpec: AnimationSpec<Float> = JengaProgressDefaults.animationSpec,
) {
    val animated by animateFloatAsState(progress.coerceIn(0f, 1f), animationSpec, label = "progress")
    Track(modifier = modifier, height = trackHeight, shape = shape, color = colors.track) {
        Box(
            modifier = Modifier
                .fillMaxHeight()
                .fillMaxWidth(animated)
                .clip(shape)
                .background(colors.indicator),
        )
    }
}

/**
 * A determinate progress bar with a label row above it: [label] at the start
 * (e.g. the current step, with a styled part) and an optional [valueLabel] at
 * the end (e.g. "60%").
 *
 * @sample io.github.joelkanyi.jenga.samples.JengaLabelledProgressSample
 *
 * @param progress the progress in `0f..1f` (coerced into range).
 * @param label the start label.
 * @param modifier the [Modifier] for this block.
 * @param valueLabel optional end label.
 * @param tone the bar tone; see [JengaProgressTone].
 * @param colors the color set; defaults to [JengaProgressDefaults.colors] for [tone].
 * @param trackHeight the bar height.
 * @param shape the track and indicator shape.
 * @param labelStyle the label text style.
 * @param spacing the gap between the label row and the bar.
 * @param animationSpec how the indicator animates to a new [progress].
 */
@Composable
public fun JengaLabelledProgress(
    progress: Float,
    label: AnnotatedString,
    modifier: Modifier = Modifier,
    valueLabel: String? = null,
    tone: JengaProgressTone = JengaProgressTone.Default,
    colors: JengaProgressColors = JengaProgressDefaults.colors(tone),
    trackHeight: Dp = JengaProgressDefaults.TrackHeight,
    shape: Shape = JengaProgressDefaults.shape,
    labelStyle: TextStyle = JengaProgressDefaults.labelStyle,
    spacing: Dp = JengaProgressDefaults.labelSpacing,
    animationSpec: AnimationSpec<Float> = JengaProgressDefaults.animationSpec,
) {
    Column(modifier = modifier, verticalArrangement = Arrangement.spacedBy(spacing)) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            JengaText(
                text = label,
                style = labelStyle,
                color = JengaTheme.colors.textSecondary,
                modifier = Modifier.weight(1f),
                maxLines = 1,
            )
            if (valueLabel != null) {
                JengaText(text = valueLabel, style = labelStyle, color = JengaTheme.colors.textSecondary, maxLines = 1)
            }
        }
        JengaLinearProgress(
            progress = progress,
            tone = tone,
            colors = colors,
            trackHeight = trackHeight,
            shape = shape,
            animationSpec = animationSpec,
        )
    }
}

/**
 * An indeterminate linear progress bar (a brand-colored bar sliding across the
 * track), for when progress can't be measured.
 *
 * @param modifier the [Modifier] for this bar.
 */
@Composable
public fun JengaLinearProgressIndeterminate(
    modifier: Modifier = Modifier,
) {
    val transition = rememberInfiniteTransition(label = "indeterminate")
    val fraction by transition.animateFloat(
        initialValue = -0.4f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = JengaProgressDefaults.IndeterminateDurationMillis, easing = LinearEasing),
            repeatMode = RepeatMode.Restart,
        ),
        label = "offset",
    )
    Track(
        modifier = modifier,
        height = JengaProgressDefaults.TrackHeight,
        shape = JengaTheme.shapes.pill,
        color = JengaTheme.colors.surfaceSunk,
    ) {
        val indicatorWidth = 0.4f
        Box(
            modifier = Modifier
                .fillMaxHeight()
                .fillMaxWidth(indicatorWidth)
                .layout { measurable, constraints ->
                    val placeable = measurable.measure(constraints)
                    val parentWidth = constraints.maxWidth
                    val x = (fraction * parentWidth).toInt()
                    layout(placeable.width, placeable.height) {
                        placeable.placeRelative(x, 0)
                    }
                }
                .clip(JengaTheme.shapes.pill)
                .background(JengaTheme.colors.brand),
        )
    }
}

@Composable
private fun Track(
    height: Dp,
    shape: Shape,
    color: Color,
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit,
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(height)
            .clip(shape)
            .background(color),
        content = { content() },
    )
}
