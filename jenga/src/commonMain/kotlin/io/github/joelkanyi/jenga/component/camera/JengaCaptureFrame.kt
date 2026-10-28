package io.github.joelkanyi.jenga.component.camera

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.Canvas
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.RoundRect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.PathFillType
import androidx.compose.ui.graphics.PathMeasure
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.toSize
import dev.drewhamilton.poko.Poko
import io.github.joelkanyi.jenga.theme.JengaTheme

/** The outline of a [JengaCaptureFrame]. */
@Immutable
public sealed interface JengaCaptureShape {
    /** An upright oval, for a face. */
    public data object Oval : JengaCaptureShape

    /** A rounded rectangle at [aspectRatio] (width / height), for a card or page. */
    @Poko
    public class Rectangle(public val aspectRatio: Float) : JengaCaptureShape
}

/** Resolved colors for a [JengaCaptureFrame]. Override via [JengaCaptureFrameDefaults.colors]. */
@Poko
@Immutable
public class JengaCaptureFrameColors(
    public val scrim: Color,
    public val border: Color,
    public val readyBorder: Color,
    public val progress: Color,
) {
    public fun copy(
        scrim: Color = this.scrim,
        border: Color = this.border,
        readyBorder: Color = this.readyBorder,
        progress: Color = this.progress,
    ): JengaCaptureFrameColors = JengaCaptureFrameColors(scrim, border, readyBorder, progress)
}

/** Defaults and token mappings for [JengaCaptureFrame]. */
public object JengaCaptureFrameDefaults {
    /** Width / height of the oval. */
    public const val OvalAspectRatio: Float = 0.76f

    /** Border width while not ready. */
    public val BorderWidth: Dp = 2.5.dp

    /** Border width once ready. */
    public val ReadyBorderWidth: Dp = 3.5.dp

    /** Width of the hold-progress line. */
    public val ProgressWidth: Dp = 4.dp

    /** Corner radius of a rectangle frame. */
    public val CornerRadius: Dp = 16.dp

    /** Themed colors. */
    @Composable
    public fun colors(): JengaCaptureFrameColors {
        val c = JengaTheme.colors
        return JengaCaptureFrameColors(
            scrim = c.scrim,
            border = c.onOverlay,
            readyBorder = c.success,
            progress = c.success,
        )
    }
}

/**
 * A capture guide drawn over a camera preview: a frame in [shape], with the area
 * outside it dimmed. The border turns to the ready color when [ready], and
 * [holdProgress] draws a line around the outline while the user holds still.
 *
 * The frame sits a little above the middle and is sized from the width, shrinking
 * to fit short (landscape) spaces.
 *
 * @sample io.github.joelkanyi.jenga.samples.JengaCaptureFrameSample
 *
 * @param shape the frame outline; see [JengaCaptureShape].
 * @param modifier the [Modifier] for the overlay (typically `fillMaxSize`).
 * @param ready whether the frame shows the ready color.
 * @param holdProgress progress from 0 to 1 drawn around the outline; null for none.
 * @param onFrameBoundsChange called with the frame's bounds in pixels, relative
 *   to this overlay, e.g. to crop the photo to the frame.
 * @param colors the color set; defaults to [JengaCaptureFrameDefaults.colors].
 */
@Composable
public fun JengaCaptureFrame(
    shape: JengaCaptureShape,
    modifier: Modifier = Modifier,
    ready: Boolean = false,
    holdProgress: Float? = null,
    onFrameBoundsChange: ((Rect) -> Unit)? = null,
    colors: JengaCaptureFrameColors = JengaCaptureFrameDefaults.colors(),
) {
    val borderColor by animateColorAsState(if (ready) colors.readyBorder else colors.border, label = "frameBorder")
    val borderWidth by animateDpAsState(
        if (ready) JengaCaptureFrameDefaults.ReadyBorderWidth else JengaCaptureFrameDefaults.BorderWidth,
        label = "frameBorderWidth",
    )
    val latestOnBoundsChange by rememberUpdatedState(onFrameBoundsChange)
    Canvas(
        modifier = modifier.onSizeChanged { latestOnBoundsChange?.invoke(captureFrameBounds(it.toSize(), shape)) },
    ) {
        val bounds = captureFrameBounds(size, shape)
        val outline = frameOutline(bounds, shape, JengaCaptureFrameDefaults.CornerRadius.toPx())
        val scrim = Path().apply {
            fillType = PathFillType.EvenOdd
            addRect(Rect(0f, 0f, size.width, size.height))
            addPath(outline)
        }
        drawPath(scrim, colors.scrim)
        drawPath(outline, borderColor, style = Stroke(borderWidth.toPx()))
        if (holdProgress != null) {
            drawHoldProgress(shape, bounds, outline, holdProgress.coerceIn(0f, 1f), colors.progress)
        }
    }
}

internal fun captureFrameBounds(space: Size, shape: JengaCaptureShape): Rect {
    val aspectRatio = when (shape) {
        JengaCaptureShape.Oval -> JengaCaptureFrameDefaults.OvalAspectRatio
        is JengaCaptureShape.Rectangle -> shape.aspectRatio
    }
    val widthShare = if (shape == JengaCaptureShape.Oval) OvalWidthShare else RectangleWidthShare
    var width = space.width * widthShare
    var height = width / aspectRatio
    val maxHeight = space.height * MaxHeightShare
    if (height > maxHeight) {
        height = maxHeight
        width = height * aspectRatio
    }
    val centerY = space.height * CenterShare
    return Rect(
        left = (space.width - width) / 2,
        top = centerY - height / 2,
        right = (space.width + width) / 2,
        bottom = centerY + height / 2,
    )
}

private fun frameOutline(bounds: Rect, shape: JengaCaptureShape, cornerRadius: Float): Path = Path().apply {
    when (shape) {
        JengaCaptureShape.Oval -> addOval(bounds)
        is JengaCaptureShape.Rectangle -> addRoundRect(RoundRect(bounds, CornerRadius(cornerRadius)))
    }
}

private fun DrawScope.drawHoldProgress(shape: JengaCaptureShape, bounds: Rect, outline: Path, progress: Float, color: Color) {
    val line = Stroke(JengaCaptureFrameDefaults.ProgressWidth.toPx(), cap = StrokeCap.Round)
    if (shape == JengaCaptureShape.Oval) {
        drawArc(color, -90f, 360f * progress, false, bounds.topLeft, bounds.size, style = line)
    } else {
        drawPath(progressAlong(outline, progress), color, style = line)
    }
}

private fun progressAlong(outline: Path, progress: Float): Path {
    val measure = PathMeasure()
    measure.setPath(outline, forceClosed = true)
    val segment = Path()
    measure.getSegment(0f, measure.length * progress, segment, startWithMoveTo = true)
    return segment
}

private const val OvalWidthShare = 0.63f
private const val RectangleWidthShare = 0.84f
private const val MaxHeightShare = 0.62f
private const val CenterShare = 0.45f
