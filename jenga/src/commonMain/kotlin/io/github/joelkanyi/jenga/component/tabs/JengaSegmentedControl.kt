package io.github.joelkanyi.jenga.component.tabs

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import dev.drewhamilton.poko.Poko
import io.github.joelkanyi.jenga.component.text.JengaText
import io.github.joelkanyi.jenga.theme.JengaTheme

/** Resolved colors for a [JengaSegmentedControl]. Override via [JengaSegmentedControlDefaults.colors]. */
@Poko
@Immutable
public class JengaSegmentedControlColors(
    public val track: Color,
    public val selectedSegment: Color,
    public val selectedContent: Color,
    public val unselectedContent: Color,
    public val disabledContent: Color = unselectedContent.copy(alpha = 0.38f),
) {
    public fun copy(
        track: Color = this.track,
        selectedSegment: Color = this.selectedSegment,
        selectedContent: Color = this.selectedContent,
        unselectedContent: Color = this.unselectedContent,
        disabledContent: Color = this.disabledContent,
    ): JengaSegmentedControlColors = JengaSegmentedControlColors(
        track,
        selectedSegment,
        selectedContent,
        unselectedContent,
        disabledContent,
    )
}

/** Defaults and token mappings for [JengaSegmentedControl]. */
/**
 * Theme-wide style for every [JengaSegmentedControl], set through
 * [io.github.joelkanyi.jenga.theme.JengaComponentStyles.segmentedControl]. A `null` value keeps
 * Jenga's default; arguments passed to a call still win.
 *
 * @property shape the outer shape.
 * @property segmentShape the shape of each segment; falls back to the outer shape.
 * @property segmentMinHeight the minimum segment height.
 * @property trackPadding padding inside the track, around the segments.
 * @property selectedElevation shadow under the selected segment.
 * @property textStyle the segment label text style.
 * @property selectedTextStyle merged onto the label text style of the selected segment, so set only
 *   what changes, e.g. `TextStyle(fontWeight = FontWeight.SemiBold)`.
 * @property colors the color set.
 */
@Poko
@Immutable
public class JengaSegmentedControlStyle(
    public val shape: Shape? = null,
    public val segmentShape: Shape? = null,
    public val segmentMinHeight: Dp? = null,
    public val trackPadding: Dp? = null,
    public val selectedElevation: Dp? = null,
    public val textStyle: TextStyle? = null,
    public val selectedTextStyle: TextStyle? = null,
    public val colors: JengaSegmentedControlColors? = null,
) {
    public fun copy(
        shape: Shape? = this.shape,
        segmentShape: Shape? = this.segmentShape,
        segmentMinHeight: Dp? = this.segmentMinHeight,
        trackPadding: Dp? = this.trackPadding,
        selectedElevation: Dp? = this.selectedElevation,
        textStyle: TextStyle? = this.textStyle,
        selectedTextStyle: TextStyle? = this.selectedTextStyle,
        colors: JengaSegmentedControlColors? = this.colors,
    ): JengaSegmentedControlStyle = JengaSegmentedControlStyle(
        shape,
        segmentShape,
        segmentMinHeight,
        trackPadding,
        selectedElevation,
        textStyle,
        selectedTextStyle,
        colors,
    )
}

/**
 * Defaults for [JengaSegmentedControl]. The composable members follow the theme's
 * [JengaSegmentedControlStyle]; the constants are Jenga's built-in values, and
 * [JengaSegmentedControl] resolves the theme's values over them.
 */
public object JengaSegmentedControlDefaults {
    /** Padding inside the track, around the segments. */
    public val TrackPadding: Dp = 4.dp

    /** Minimum segment height. */
    public val SegmentMinHeight: Dp = 36.dp

    /** Shadow under the selected segment. */
    public val SelectedElevation: Dp = 0.dp

    /** Default outer shape. */
    public val shape: Shape
        @Composable get() = JengaTheme.components.segmentedControl?.shape ?: JengaTheme.shapes.pill

    /** Segment label text style. */
    public val textStyle: TextStyle
        @Composable get() = JengaTheme.components.segmentedControl?.textStyle ?: JengaTheme.typography.titleSmall

    /** Themed colors. */
    @Composable
    public fun colors(): JengaSegmentedControlColors {
        JengaTheme.components.segmentedControl?.colors?.let { return it }
        val c = JengaTheme.colors
        return JengaSegmentedControlColors(
            track = c.surfaceSunk,
            selectedSegment = c.surface,
            selectedContent = c.textPrimary,
            unselectedContent = c.textMuted,
            disabledContent = c.contentDisabled,
        )
    }
}

/**
 * A segmented control: a small set of mutually-exclusive options in a pill
 * track, with the selected segment raised. A null [selectedIndex] shows no
 * selection (e.g. an unanswered yes/no question).
 *
 * @sample io.github.joelkanyi.jenga.samples.JengaSegmentedControlSample
 *
 * @param selectedIndex the selected segment index, or null for none.
 * @param segments the segment labels.
 * @param onSelect called with the index of a tapped segment.
 * @param modifier the [Modifier] for the control.
 * @param shape the outer shape; defaults to [JengaSegmentedControlDefaults.shape].
 * @param colors the color set; defaults to [JengaSegmentedControlDefaults.colors].
 * @param isEnabled whether the segment at an index can be selected.
 * @param segmentShape the shape of each segment; defaults to the theme's segment shape, else [shape].
 * @param segmentMinHeight the minimum segment height.
 * @param trackPadding padding inside the track, around the segments.
 * @param selectedElevation shadow under the selected segment.
 * @param textStyle the segment label text style.
 * @param selectedTextStyle the label text style of the selected segment; defaults to [textStyle] with
 *   the theme's [JengaSegmentedControlStyle.selectedTextStyle] merged on top.
 */
@Composable
public fun JengaSegmentedControl(
    selectedIndex: Int?,
    segments: List<String>,
    onSelect: (Int) -> Unit,
    modifier: Modifier = Modifier,
    shape: Shape = JengaSegmentedControlDefaults.shape,
    colors: JengaSegmentedControlColors = JengaSegmentedControlDefaults.colors(),
    isEnabled: (Int) -> Boolean = { true },
    segmentShape: Shape = JengaTheme.components.segmentedControl?.segmentShape ?: shape,
    segmentMinHeight: Dp = JengaTheme.components.segmentedControl?.segmentMinHeight
        ?: JengaSegmentedControlDefaults.SegmentMinHeight,
    trackPadding: Dp = JengaTheme.components.segmentedControl?.trackPadding ?: JengaSegmentedControlDefaults.TrackPadding,
    selectedElevation: Dp = JengaTheme.components.segmentedControl?.selectedElevation
        ?: JengaSegmentedControlDefaults.SelectedElevation,
    textStyle: TextStyle = JengaSegmentedControlDefaults.textStyle,
    selectedTextStyle: TextStyle = textStyle.merge(JengaTheme.components.segmentedControl?.selectedTextStyle),
) {
    Row(
        modifier = modifier
            .clip(shape)
            .background(colors.track)
            .padding(PaddingValues(trackPadding))
            .selectableGroup(),
        horizontalArrangement = Arrangement.spacedBy(trackPadding),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        segments.forEachIndexed { index, segment ->
            val selected = index == selectedIndex
            val enabled = isEnabled(index)
            Box(
                modifier = Modifier
                    .weight(1f)
                    .then(if (selected) Modifier.shadow(selectedElevation, segmentShape) else Modifier)
                    .clip(segmentShape)
                    .background(if (selected) colors.selectedSegment else Color.Transparent)
                    .selectable(
                        selected = selected,
                        enabled = enabled,
                        role = Role.Tab,
                        onClick = { onSelect(index) },
                    )
                    .defaultMinSize(minHeight = segmentMinHeight)
                    .padding(horizontal = JengaTheme.spacing.md),
                contentAlignment = Alignment.Center,
            ) {
                JengaText(
                    text = segment,
                    style = if (selected) selectedTextStyle else textStyle,
                    color = when {
                        !enabled -> colors.disabledContent
                        selected -> colors.selectedContent
                        else -> colors.unselectedContent
                    },
                    maxLines = 1,
                )
            }
        }
    }
}
