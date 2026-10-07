package io.github.joelkanyi.jenga.component.chart

import androidx.compose.animation.core.AnimationSpec
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import dev.drewhamilton.poko.Poko
import io.github.joelkanyi.jenga.component.text.JengaText
import io.github.joelkanyi.jenga.theme.JengaTheme

/** One bar of a [JengaBarChart]: a [label] under the bar and a non-negative [value]. */
@Poko
@Immutable
public class JengaBarChartEntry(
    public val label: String,
    public val value: Float,
)

/** How a [JengaBarChart] lays its bars out. */
public sealed interface JengaBarChartArrangement {
    /** Bars share the chart width equally; for short series. */
    public data object Fill : JengaBarChartArrangement

    /** Each bar gets a fixed [slotWidth] and the chart scrolls horizontally; for long series. */
    @Poko
    public class Scroll(public val slotWidth: Dp) : JengaBarChartArrangement
}

/** Resolved colors for a [JengaBarChart]. Override via [JengaBarChartDefaults.colors]. */
@Poko
@Immutable
public class JengaBarChartColors(
    public val bar: Color,
    public val selectedBar: Color,
    public val unselectedBar: Color,
    public val label: Color,
    public val selectedLabel: Color,
) {
    public fun copy(
        bar: Color = this.bar,
        selectedBar: Color = this.selectedBar,
        unselectedBar: Color = this.unselectedBar,
        label: Color = this.label,
        selectedLabel: Color = this.selectedLabel,
    ): JengaBarChartColors = JengaBarChartColors(bar, selectedBar, unselectedBar, label, selectedLabel)
}

/** Defaults and token mappings for [JengaBarChart]. */
public object JengaBarChartDefaults {
    /** Height of the tallest bar. */
    public val PlotHeight: Dp = 96.dp

    /** Height of a bar whose value is zero. */
    public val MinBarHeight: Dp = 0.dp

    /** Widest a bar may be; the rest of its slot stays empty. */
    public val BarMaxWidth: Dp = 24.dp

    /** Default bar shape: a rounded tip and a square base. */
    public val barShape: Shape
        @Composable get() = JengaTheme.shapes.xs.copy(bottomStart = CornerSize(0.dp), bottomEnd = CornerSize(0.dp))

    /** Gap between bar slots. */
    public val barSpacing: Dp
        @Composable get() = JengaTheme.spacing.sm

    /** Gap between a bar and its label. */
    public val labelSpacing: Dp
        @Composable get() = JengaTheme.spacing.xs

    /** Padding around the chart. */
    public val contentPadding: PaddingValues
        @Composable get() = PaddingValues()

    /** Label text style. */
    public val labelStyle: TextStyle
        @Composable get() = JengaTheme.typography.caption

    /** Label text style under the selected bar. */
    public val selectedLabelStyle: TextStyle
        @Composable get() = JengaTheme.typography.caption.copy(fontWeight = FontWeight.SemiBold)

    /** How bar heights animate when the data changes. */
    public val animationSpec: AnimationSpec<Float>
        @Composable get() = tween(JengaTheme.motion.durationSlow, easing = JengaTheme.motion.standard)

    /**
     * Themed colors. With no selection every bar uses `bar`; once a bar is selected it uses
     * `selectedBar` and the others `unselectedBar`.
     */
    @Composable
    public fun colors(): JengaBarChartColors {
        val c = JengaTheme.colors
        return JengaBarChartColors(
            bar = c.brand,
            selectedBar = c.brand,
            unselectedBar = c.borderStrong,
            label = c.textSecondary,
            selectedLabel = c.textPrimary,
        )
    }
}

/**
 * A vertical bar chart: one bar per entry, growing from a shared baseline, with its label
 * underneath. Bar heights are relative to [maxValue] and animate when the data changes.
 * With [onSelect] each bar's whole slot is a tap target and the chart reads as a group of
 * selectable bars; without it the chart is static. There are no axes; put the figure the
 * selection refers to next to the chart, or add a [valueLabel] at each bar's tip.
 *
 * @sample io.github.joelkanyi.jenga.samples.JengaBarChartSample
 *
 * @param entries the bars, in display order; values must not be negative.
 * @param barContentDescription accessibility label for a bar (e.g. "May, 12,000").
 * @param modifier the [Modifier] for the chart.
 * @param selectedIndex the selected bar, or null for none.
 * @param onSelect called with a tapped bar's index; null makes the chart static.
 * @param maxValue the value of a full-height bar; the largest entry by default.
 * @param arrangement how bars are laid out; see [JengaBarChartArrangement].
 * @param plotHeight the height of a full-height bar.
 * @param minBarHeight the height of a zero-value bar.
 * @param barMaxWidth the widest a bar may be.
 * @param barSpacing the gap between bar slots.
 * @param barShape the bar shape.
 * @param labelSpacing the gap between a bar and its label.
 * @param contentPadding padding around the chart.
 * @param labelStyle the label text style.
 * @param selectedLabelStyle the label text style under the selected bar.
 * @param colors the color set; defaults to [JengaBarChartDefaults.colors].
 * @param animationSpec how bar heights animate.
 * @param valueLabel optional content above each bar's tip, given the entry and whether it is selected.
 * @param label the content under each bar, given the entry and whether it is selected.
 */
@Composable
public fun JengaBarChart(
    entries: List<JengaBarChartEntry>,
    barContentDescription: (JengaBarChartEntry) -> String,
    modifier: Modifier = Modifier,
    selectedIndex: Int? = null,
    onSelect: ((Int) -> Unit)? = null,
    maxValue: Float? = null,
    arrangement: JengaBarChartArrangement = JengaBarChartArrangement.Fill,
    plotHeight: Dp = JengaBarChartDefaults.PlotHeight,
    minBarHeight: Dp = JengaBarChartDefaults.MinBarHeight,
    barMaxWidth: Dp = JengaBarChartDefaults.BarMaxWidth,
    barSpacing: Dp = JengaBarChartDefaults.barSpacing,
    barShape: Shape = JengaBarChartDefaults.barShape,
    labelSpacing: Dp = JengaBarChartDefaults.labelSpacing,
    contentPadding: PaddingValues = JengaBarChartDefaults.contentPadding,
    labelStyle: TextStyle = JengaBarChartDefaults.labelStyle,
    selectedLabelStyle: TextStyle = JengaBarChartDefaults.selectedLabelStyle,
    colors: JengaBarChartColors = JengaBarChartDefaults.colors(),
    animationSpec: AnimationSpec<Float> = JengaBarChartDefaults.animationSpec,
    valueLabel: (@Composable (entry: JengaBarChartEntry, selected: Boolean) -> Unit)? = null,
    label: @Composable (entry: JengaBarChartEntry, selected: Boolean) -> Unit = { entry, selected ->
        JengaText(
            text = entry.label,
            style = if (selected) selectedLabelStyle else labelStyle,
            color = if (selected) colors.selectedLabel else colors.label,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
        )
    },
) {
    require(entries.all { it.value >= 0f }) { "JengaBarChart values must not be negative" }
    require(minBarHeight <= plotHeight) { "minBarHeight must not exceed plotHeight" }
    val max = maxValue ?: entries.maxOfOrNull { it.value } ?: 0f
    val slot: @Composable (Int, JengaBarChartEntry, Modifier) -> Unit = { index, entry, slotModifier ->
        BarSlot(
            entry = entry,
            fraction = if (max > 0f) (entry.value / max).coerceIn(0f, 1f) else 0f,
            selected = index == selectedIndex,
            hasSelection = selectedIndex != null,
            onClick = onSelect?.let { { it(index) } },
            contentDescription = barContentDescription(entry),
            plotHeight = plotHeight,
            minBarHeight = minBarHeight,
            barMaxWidth = barMaxWidth,
            barShape = barShape,
            labelSpacing = labelSpacing,
            colors = colors,
            animationSpec = animationSpec,
            valueLabel = valueLabel,
            label = label,
            modifier = slotModifier,
        )
    }
    when (arrangement) {
        JengaBarChartArrangement.Fill -> Row(
            modifier = modifier
                .fillMaxWidth()
                .padding(contentPadding)
                .selectableGroup(),
            horizontalArrangement = Arrangement.spacedBy(barSpacing),
            verticalAlignment = Alignment.Bottom,
        ) {
            entries.forEachIndexed { index, entry -> slot(index, entry, Modifier.weight(1f)) }
        }
        is JengaBarChartArrangement.Scroll -> LazyRow(
            modifier = modifier
                .fillMaxWidth()
                .selectableGroup(),
            contentPadding = contentPadding,
            horizontalArrangement = Arrangement.spacedBy(barSpacing),
            verticalAlignment = Alignment.Bottom,
        ) {
            itemsIndexed(entries) { index, entry -> slot(index, entry, Modifier.width(arrangement.slotWidth)) }
        }
    }
}

@Composable
private fun BarSlot(
    entry: JengaBarChartEntry,
    fraction: Float,
    selected: Boolean,
    hasSelection: Boolean,
    onClick: (() -> Unit)?,
    contentDescription: String,
    plotHeight: Dp,
    minBarHeight: Dp,
    barMaxWidth: Dp,
    barShape: Shape,
    labelSpacing: Dp,
    colors: JengaBarChartColors,
    animationSpec: AnimationSpec<Float>,
    valueLabel: (@Composable (JengaBarChartEntry, Boolean) -> Unit)?,
    label: @Composable (JengaBarChartEntry, Boolean) -> Unit,
    modifier: Modifier = Modifier,
) {
    val animated by animateFloatAsState(fraction, animationSpec, label = "bar")
    val barColor = when {
        !hasSelection -> colors.bar
        selected -> colors.selectedBar
        else -> colors.unselectedBar
    }
    Column(
        modifier = modifier
            .then(
                if (onClick != null) {
                    Modifier.selectable(selected = selected, role = Role.Tab, onClick = onClick)
                } else {
                    Modifier
                },
            )
            .semantics(mergeDescendants = true) { this.contentDescription = contentDescription },
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        valueLabel?.invoke(entry, selected)
        Box(
            modifier = Modifier
                .widthIn(max = barMaxWidth)
                .fillMaxWidth()
                .height(minBarHeight + (plotHeight - minBarHeight) * animated)
                .clip(barShape)
                .background(barColor),
        )
        Box(modifier = Modifier.padding(top = labelSpacing)) {
            label(entry, selected)
        }
    }
}
