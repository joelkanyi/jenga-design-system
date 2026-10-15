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

    /** Failed or over a limit, in the error color. */
    Error,
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
/**
 * Theme-wide style for every [JengaLinearProgress], [JengaLabelledProgress] and
 * [JengaLinearProgressIndeterminate], set through
 * [io.github.joelkanyi.jenga.theme.JengaComponentStyles.progress]. A `null` value keeps Jenga's
 * default; arguments passed to a call still win.
 *
 * @property trackHeight the track height.
 * @property shape the track and indicator shape.
 * @property default colors for [JengaProgressTone.Default], also used by the indeterminate bar.
 * @property success colors for [JengaProgressTone.Success].
 * @property neutral colors for [JengaProgressTone.Neutral].
 * @property labelStyle the label text style of [JengaLabelledProgress].
 * @property labelSpacing the gap between the label row and the bar.
 * @property error colors for [JengaProgressTone.Error].
 */
@Poko
@Immutable
public class JengaProgressStyle(
    public val trackHeight: Dp? = null,
    public val shape: Shape? = null,
    public val default: JengaProgressColors? = null,
    public val success: JengaProgressColors? = null,
    public val neutral: JengaProgressColors? = null,
    public val labelStyle: TextStyle? = null,
    public val labelSpacing: Dp? = null,
    public val error: JengaProgressColors? = null,
) {
    @Deprecated("Kept for binary compatibility", level = DeprecationLevel.HIDDEN)
    public constructor(
        trackHeight: Dp? = null,
        shape: Shape? = null,
        default: JengaProgressColors? = null,
        success: JengaProgressColors? = null,
        neutral: JengaProgressColors? = null,
        labelStyle: TextStyle? = null,
        labelSpacing: Dp? = null,
    ) : this(trackHeight, shape, default, success, neutral, labelStyle, labelSpacing, error = null)

    public fun copy(
        trackHeight: Dp? = this.trackHeight,
        shape: Shape? = this.shape,
        default: JengaProgressColors? = this.default,
        success: JengaProgressColors? = this.success,
        neutral: JengaProgressColors? = this.neutral,
        labelStyle: TextStyle? = this.labelStyle,
        labelSpacing: Dp? = this.labelSpacing,
        error: JengaProgressColors? = this.error,
    ): JengaProgressStyle = JengaProgressStyle(trackHeight, shape, default, success, neutral, labelStyle, labelSpacing, error)

    @Deprecated("Kept for binary compatibility", level = DeprecationLevel.HIDDEN)
    public fun copy(
        trackHeight: Dp? = this.trackHeight,
        shape: Shape? = this.shape,
        default: JengaProgressColors? = this.default,
        success: JengaProgressColors? = this.success,
        neutral: JengaProgressColors? = this.neutral,
        labelStyle: TextStyle? = this.labelStyle,
        labelSpacing: Dp? = this.labelSpacing,
    ): JengaProgressStyle = copy(trackHeight, shape, default, success, neutral, labelStyle, labelSpacing, this.error)

    internal fun colors(tone: JengaProgressTone): JengaProgressColors? = when (tone) {
        JengaProgressTone.Default -> default
        JengaProgressTone.Success -> success
        JengaProgressTone.Neutral -> neutral
        JengaProgressTone.Error -> error
    }
}

public object JengaProgressDefaults {
    /** Duration of one indeterminate sweep (ms). Loop animation, hence outside
     *  the discrete [io.github.joelkanyi.jenga.foundation.motion.JengaMotion] scale. */
    public const val IndeterminateDurationMillis: Int = 1100

    /** Default track height. */
    public val TrackHeight: Dp = 6.dp

    /** Default track and indicator shape. */
    public val shape: Shape
        @Composable get() = JengaTheme.components.progress?.shape ?: JengaTheme.shapes.pill

    /** How the indicator animates to a new progress value. */
    public val animationSpec: AnimationSpec<Float>
        @Composable get() = tween(JengaTheme.motion.durationSlow, easing = JengaTheme.motion.standard)

    /** Gap between the label row and the bar in [JengaLabelledProgress]. */
    public val labelSpacing: Dp
        @Composable get() = JengaTheme.components.progress?.labelSpacing ?: JengaTheme.spacing.sm

    /** Label text style for [JengaLabelledProgress]. */
    public val labelStyle: TextStyle
        @Composable get() = JengaTheme.components.progress?.labelStyle ?: JengaTheme.typography.caption

    /** Themed colors for [tone]. */
    @Composable
    public fun colors(tone: JengaProgressTone): JengaProgressColors {
        JengaTheme.components.progress?.colors(tone)?.let { return it }
        val c = JengaTheme.colors
        return JengaProgressColors(
            track = c.surfaceSunk,
            indicator = when (tone) {
                JengaProgressTone.Default -> c.brand
                JengaProgressTone.Success -> c.success
                JengaProgressTone.Neutral -> c.textMuted
                JengaProgressTone.Error -> c.error
            },
        )
    }

    @Composable
    internal fun trackHeight(): Dp = JengaTheme.components.progress?.trackHeight ?: TrackHeight
}

/**
 * A determinate linear progress bar. Changes to [progress] animate.
 *
 * @param progress the progress in `0f..1f` (coerced into range).
 * @param modifier the [Modifier] for this bar.
 * @param tone the bar tone; see [JengaProgressTone].
 * @param colors the color set; defaults to [JengaProgressDefaults.colors] for [tone].
 * @param trackHeight the bar height; defaults to the theme's [JengaProgressStyle.trackHeight], else
 *   [JengaProgressDefaults.TrackHeight].
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
    trackHeight: Dp = JengaProgressDefaults.trackHeight(),
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
 * @param trackHeight the bar height; defaults to the theme's [JengaProgressStyle.trackHeight], else
 *   [JengaProgressDefaults.TrackHeight].
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
    trackHeight: Dp = JengaProgressDefaults.trackHeight(),
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
    val colors = JengaProgressDefaults.colors(JengaProgressTone.Default)
    val shape = JengaProgressDefaults.shape
    Track(
        modifier = modifier,
        height = JengaProgressDefaults.trackHeight(),
        shape = shape,
        color = colors.track,
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
                .clip(shape)
                .background(colors.indicator),
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
