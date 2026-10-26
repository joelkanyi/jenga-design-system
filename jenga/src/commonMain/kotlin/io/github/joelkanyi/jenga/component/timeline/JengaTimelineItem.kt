package io.github.joelkanyi.jenga.component.timeline

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.Immutable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.stateDescription
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import dev.drewhamilton.poko.Poko
import io.github.joelkanyi.jenga.component.icon.JengaIcon
import io.github.joelkanyi.jenga.component.text.JengaText
import io.github.joelkanyi.jenga.theme.JengaTheme
import io.github.joelkanyi.jenga.theme.LocalJengaContentColor

/** Progress state of a stage in a timeline. */
public enum class JengaTimelineState {
    /** The stage is finished. */
    Done,

    /** The stage is in progress. */
    Current,

    /** The stage was stopped and will not continue. */
    Stopped,

    /** The stage has not started. */
    ToDo,
}

/** Where a [JengaTimelineItem] sits in its timeline; the last stage draws no connector below. */
public enum class JengaTimelinePosition {
    /** The first stage. */
    First,

    /** A stage between others. */
    Middle,

    /** The last stage. */
    Last,

    /** The only stage. */
    Only,
    ;

    public companion object {
        /** The position of the item at [index] in a timeline of [count] items. */
        public fun of(index: Int, count: Int): JengaTimelinePosition = when {
            count == 1 -> Only
            index == 0 -> First
            index == count - 1 -> Last
            else -> Middle
        }
    }
}

/** How a [JengaTimelineMarker] is drawn. */
public enum class JengaTimelineMarkerStyle {
    /** A solid circle in the marker color. */
    Filled,

    /** A circle outline in the marker color. */
    Ring,
}

/** Resolved colors for a [JengaTimelineItem]. Override via [JengaTimelineDefaults.colors]. */
@Poko
@Immutable
public class JengaTimelineColors(
    public val done: Color,
    public val current: Color,
    public val stopped: Color,
    public val toDo: Color,
    public val onMarker: Color,
    public val connector: Color,
    public val reachedConnector: Color,
    public val title: Color,
    public val subtitle: Color,
) {
    public fun copy(
        done: Color = this.done,
        current: Color = this.current,
        stopped: Color = this.stopped,
        toDo: Color = this.toDo,
        onMarker: Color = this.onMarker,
        connector: Color = this.connector,
        reachedConnector: Color = this.reachedConnector,
        title: Color = this.title,
        subtitle: Color = this.subtitle,
    ): JengaTimelineColors = JengaTimelineColors(
        done,
        current,
        stopped,
        toDo,
        onMarker,
        connector,
        reachedConnector,
        title,
        subtitle,
    )
}

/** Defaults and token mappings for [JengaTimelineItem]. */
public object JengaTimelineDefaults {
    /** Diameter of the stage marker. */
    public val MarkerSize: Dp = 20.dp

    /** Width of the connector line. */
    public val ConnectorWidth: Dp = 2.dp

    /** Width of the outline of a [JengaTimelineMarkerStyle.Ring] marker. */
    public val RingWidth: Dp = 2.dp

    /** Padding around the stage text. */
    public val contentPadding: PaddingValues
        @Composable get() = PaddingValues(start = JengaTheme.spacing.md, bottom = JengaTheme.spacing.lg)

    /** Title text style. */
    public val titleStyle: TextStyle
        @Composable get() = JengaTheme.typography.titleSmall

    /** Subtitle text style. */
    public val subtitleStyle: TextStyle
        @Composable get() = JengaTheme.typography.bodySmall

    /** Themed colors. */
    @Composable
    public fun colors(): JengaTimelineColors {
        val c = JengaTheme.colors
        return JengaTimelineColors(
            done = c.success,
            current = c.brand,
            stopped = c.error,
            toDo = c.borderStrong,
            onMarker = c.onBrand,
            connector = c.border,
            reachedConnector = c.success,
            title = c.textPrimary,
            subtitle = c.textMuted,
        )
    }
}

/**
 * One stage of a vertical timeline: a state marker with a [title] and optional
 * [subtitle] beside it, and a connector line down to the next stage. Stack items
 * in a column or lazy list, giving each its [position]; each item draws its own
 * connector, so no measuring pass is needed. The connector below a done stage is
 * drawn in the reached color.
 *
 * @sample io.github.joelkanyi.jenga.samples.JengaTimelineSample
 *
 * @param state the stage's progress; see [JengaTimelineState].
 * @param title the stage name.
 * @param position where the stage sits; see [JengaTimelinePosition].
 * @param modifier the [Modifier] for this item.
 * @param subtitle optional line under the title (e.g. a date or who did it).
 * @param trailingContent optional end slot; inherits the subtitle color.
 * @param stateDescription spoken description of [state] (e.g. "Done").
 * @param markerSize the marker diameter.
 * @param contentPadding padding around the stage text.
 * @param titleStyle the title text style.
 * @param subtitleStyle the subtitle text style.
 * @param colors the color set; defaults to [JengaTimelineDefaults.colors].
 */
@Composable
public fun JengaTimelineItem(
    state: JengaTimelineState,
    title: String,
    position: JengaTimelinePosition,
    modifier: Modifier = Modifier,
    subtitle: String? = null,
    trailingContent: (@Composable () -> Unit)? = null,
    stateDescription: String? = null,
    markerSize: Dp = JengaTimelineDefaults.MarkerSize,
    contentPadding: PaddingValues = JengaTimelineDefaults.contentPadding,
    titleStyle: TextStyle = JengaTimelineDefaults.titleStyle,
    subtitleStyle: TextStyle = JengaTimelineDefaults.subtitleStyle,
    colors: JengaTimelineColors = JengaTimelineDefaults.colors(),
) {
    val lineColor = if (state == JengaTimelineState.Done) colors.reachedConnector else colors.connector
    JengaTimelineItem(
        position = position,
        marker = { StateMarker(state = state, size = markerSize, colors = colors) },
        modifier = modifier.semantics(mergeDescendants = true) {
            if (stateDescription != null) this.stateDescription = stateDescription
        },
        lineColor = lineColor,
        markerSize = markerSize,
        contentPadding = contentPadding,
    ) {
        StageText(title, subtitle, trailingContent, titleStyle, subtitleStyle, colors)
    }
}

@Composable
private fun StageText(
    title: String,
    subtitle: String?,
    trailingContent: (@Composable () -> Unit)?,
    titleStyle: TextStyle,
    subtitleStyle: TextStyle,
    colors: JengaTimelineColors,
) {
    Row(
        verticalAlignment = Alignment.Top,
        horizontalArrangement = Arrangement.spacedBy(JengaTheme.spacing.sm),
    ) {
        Column(modifier = Modifier.weight(1f)) {
            JengaText(text = title, style = titleStyle, color = colors.title)
            if (subtitle != null) {
                JengaText(text = subtitle, style = subtitleStyle, color = colors.subtitle)
            }
        }
        if (trailingContent != null) {
            CompositionLocalProvider(LocalJengaContentColor provides colors.subtitle) {
                trailingContent()
            }
        }
    }
}

/**
 * One item of a vertical timeline with any [marker] and any [content]: a marker with
 * the content beside it, and a line in [lineColor] from the marker down to the next
 * item. The line follows the item's height. Stack items in a column or give each its own
 * `item {}` in a lazy list, with [JengaTimelinePosition.of] for [position].
 *
 * @param position where the item sits; the last and only items draw no line below.
 * @param marker the marker, e.g. a [JengaTimelineMarker]; drawn in a [markerSize] wide column.
 * @param modifier the [Modifier] for this item.
 * @param lineColor the color of the line below this item.
 * @param markerSize the width of the marker column; the line starts this far below the top.
 * @param contentPadding padding around [content].
 * @param content the item body.
 */
@Composable
public fun JengaTimelineItem(
    position: JengaTimelinePosition,
    marker: @Composable () -> Unit,
    modifier: Modifier = Modifier,
    lineColor: Color = JengaTimelineDefaults.colors().connector,
    markerSize: Dp = JengaTimelineDefaults.MarkerSize,
    contentPadding: PaddingValues = JengaTimelineDefaults.contentPadding,
    content: @Composable () -> Unit,
) {
    val hasLineBelow = position == JengaTimelinePosition.First || position == JengaTimelinePosition.Middle
    Row(modifier = modifier.fillMaxWidth().height(IntrinsicSize.Min)) {
        Box(
            modifier = Modifier
                .width(markerSize)
                .fillMaxHeight()
                .drawBehind {
                    if (hasLineBelow) {
                        val x = size.width / 2
                        val stroke = JengaTimelineDefaults.ConnectorWidth.toPx()
                        drawLine(lineColor, Offset(x, markerSize.toPx()), Offset(x, size.height), stroke)
                    }
                },
            contentAlignment = Alignment.TopCenter,
        ) {
            marker()
        }
        Box(modifier = Modifier.weight(1f).padding(contentPadding)) {
            content()
        }
    }
}

/**
 * A round timeline marker in [color], filled or as a ring, with optional [content]
 * (e.g. a step number or an icon) centered inside.
 *
 * @param color the fill or outline color.
 * @param modifier the [Modifier] for this marker.
 * @param style filled or ring; see [JengaTimelineMarkerStyle].
 * @param size the marker diameter.
 * @param ringWidth the outline width of a ring marker.
 * @param contentColor the color provided to [content].
 * @param content optional centered content.
 */
@Composable
public fun JengaTimelineMarker(
    color: Color,
    modifier: Modifier = Modifier,
    style: JengaTimelineMarkerStyle = JengaTimelineMarkerStyle.Filled,
    size: Dp = JengaTimelineDefaults.MarkerSize,
    ringWidth: Dp = JengaTimelineDefaults.RingWidth,
    contentColor: Color = JengaTimelineDefaults.colors().onMarker,
    content: (@Composable () -> Unit)? = null,
) {
    val shape = JengaTheme.shapes.pill
    val fill = when (style) {
        JengaTimelineMarkerStyle.Filled -> Modifier.background(color)
        JengaTimelineMarkerStyle.Ring -> Modifier.border(ringWidth, color, shape)
    }
    Box(
        modifier = modifier.size(size).clip(shape).then(fill),
        contentAlignment = Alignment.Center,
    ) {
        if (content != null) {
            CompositionLocalProvider(LocalJengaContentColor provides contentColor, content = content)
        }
    }
}

@Composable
private fun StateMarker(state: JengaTimelineState, size: Dp, colors: JengaTimelineColors) {
    val iconSize = size * 0.6f
    when (state) {
        JengaTimelineState.Done -> JengaTimelineMarker(color = colors.done, size = size, contentColor = colors.onMarker) {
            JengaIcon(JengaTheme.icons.check, contentDescription = null, size = iconSize)
        }
        JengaTimelineState.Stopped -> JengaTimelineMarker(color = colors.stopped, size = size, contentColor = colors.onMarker) {
            JengaIcon(JengaTheme.icons.close, contentDescription = null, size = iconSize)
        }
        JengaTimelineState.Current -> JengaTimelineMarker(color = colors.current, style = JengaTimelineMarkerStyle.Ring, size = size) {
            Box(modifier = Modifier.size(size * 0.4f).clip(JengaTheme.shapes.pill).background(colors.current))
        }
        JengaTimelineState.ToDo -> JengaTimelineMarker(color = colors.toDo, style = JengaTimelineMarkerStyle.Ring, size = size)
    }
}
