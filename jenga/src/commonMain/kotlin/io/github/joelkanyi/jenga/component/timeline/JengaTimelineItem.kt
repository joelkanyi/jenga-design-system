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
    val hasBelow = position == JengaTimelinePosition.First || position == JengaTimelinePosition.Middle
    val belowColor = if (state == JengaTimelineState.Done) colors.reachedConnector else colors.connector
    Row(
        modifier = modifier
            .fillMaxWidth()
            .height(IntrinsicSize.Min)
            .semantics(mergeDescendants = true) {
                if (stateDescription != null) this.stateDescription = stateDescription
            },
    ) {
        Box(
            modifier = Modifier
                .width(markerSize)
                .fillMaxHeight()
                .drawBehind {
                    if (hasBelow) {
                        val x = size.width / 2
                        val stroke = JengaTimelineDefaults.ConnectorWidth.toPx()
                        drawLine(belowColor, Offset(x, markerSize.toPx()), Offset(x, size.height), stroke)
                    }
                },
            contentAlignment = Alignment.TopCenter,
        ) {
            Marker(state = state, size = markerSize, colors = colors)
        }
        Row(
            modifier = Modifier
                .weight(1f)
                .padding(contentPadding),
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
}

@Composable
private fun Marker(state: JengaTimelineState, size: Dp, colors: JengaTimelineColors) {
    val shape = JengaTheme.shapes.pill
    val iconSize = size * 0.6f
    when (state) {
        JengaTimelineState.Done -> Box(
            modifier = Modifier.size(size).clip(shape).background(colors.done),
            contentAlignment = Alignment.Center,
        ) {
            JengaIcon(JengaTheme.icons.check, contentDescription = null, tint = colors.onMarker, size = iconSize)
        }
        JengaTimelineState.Stopped -> Box(
            modifier = Modifier.size(size).clip(shape).background(colors.stopped),
            contentAlignment = Alignment.Center,
        ) {
            JengaIcon(JengaTheme.icons.close, contentDescription = null, tint = colors.onMarker, size = iconSize)
        }
        JengaTimelineState.Current -> Box(
            modifier = Modifier.size(size).clip(shape).border(ConnectorBorder, colors.current, shape),
            contentAlignment = Alignment.Center,
        ) {
            Box(modifier = Modifier.size(size * 0.4f).clip(shape).background(colors.current))
        }
        JengaTimelineState.ToDo -> Box(
            modifier = Modifier.size(size).clip(shape).border(ConnectorBorder, colors.toDo, shape),
        )
    }
}

private val ConnectorBorder: Dp = 2.dp
