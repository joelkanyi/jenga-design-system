package io.github.joelkanyi.jenga.component.tabs

import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import dev.drewhamilton.poko.Poko
import io.github.joelkanyi.jenga.component.text.JengaText
import io.github.joelkanyi.jenga.theme.JengaTheme

/** How [JengaTabs] lays its tabs out. */
public enum class JengaTabsArrangement {
    /** Tabs share the row width equally; each has a full-width indicator track. */
    Fill,

    /** Tabs hug their labels from the start edge and scroll when they overflow. */
    Start,
}

/** Resolved colors for [JengaTabs]. Override via [JengaTabsDefaults.colors]. */
@Poko
@Immutable
public class JengaTabsColors(
    public val selectedContent: Color,
    public val unselectedContent: Color,
    public val indicator: Color,
    public val divider: Color,
    public val dot: Color = indicator,
) {
    public fun copy(
        selectedContent: Color = this.selectedContent,
        unselectedContent: Color = this.unselectedContent,
        indicator: Color = this.indicator,
        divider: Color = this.divider,
        dot: Color = this.dot,
    ): JengaTabsColors = JengaTabsColors(selectedContent, unselectedContent, indicator, divider, dot)
}

/** Defaults and token mappings for [JengaTabs]. */
public object JengaTabsDefaults {
    /** Thickness of the selected-tab indicator. */
    public val IndicatorThickness: Dp = 2.dp

    /** Diameter of the dot shown after a tab label. */
    public val DotSize: Dp = 6.dp

    /** Minimum tab height. */
    public val MinHeight: Dp = 0.dp

    /** Tab label text style. */
    public val textStyle: TextStyle
        @Composable get() = JengaTheme.typography.titleSmall

    /** Gap between tabs with [JengaTabsArrangement.Start]. */
    public val spacing: Dp
        @Composable get() = JengaTheme.spacing.xl

    /** Padding around the row of tabs. */
    public val contentPadding: PaddingValues
        @Composable get() = PaddingValues()

    /** Themed colors. */
    @Composable
    public fun colors(): JengaTabsColors {
        val c = JengaTheme.colors
        return JengaTabsColors(
            selectedContent = c.brand,
            unselectedContent = c.textMuted,
            indicator = c.brand,
            divider = c.border,
            dot = c.warning,
        )
    }
}

/**
 * A row of text tabs with an underline indicator under the selected tab.
 *
 * @sample io.github.joelkanyi.jenga.samples.JengaTabsSample
 *
 * @param selectedIndex the index of the selected tab.
 * @param tabs the tab titles.
 * @param onSelect called with the index of a tapped tab.
 * @param modifier the [Modifier] for the tab row.
 * @param arrangement how the tabs are laid out; see [JengaTabsArrangement].
 * @param dotted indices of tabs that show a small dot after their label (e.g. needs attention).
 * @param textStyle the tab label text style.
 * @param minHeight the minimum tab height.
 * @param spacing gap between tabs with [JengaTabsArrangement.Start].
 * @param contentPadding padding around the row of tabs.
 * @param colors the color set; defaults to [JengaTabsDefaults.colors].
 */
@Composable
public fun JengaTabs(
    selectedIndex: Int,
    tabs: List<String>,
    onSelect: (Int) -> Unit,
    modifier: Modifier = Modifier,
    arrangement: JengaTabsArrangement = JengaTabsArrangement.Fill,
    dotted: Set<Int> = emptySet(),
    textStyle: TextStyle = JengaTabsDefaults.textStyle,
    minHeight: Dp = JengaTabsDefaults.MinHeight,
    spacing: Dp = JengaTabsDefaults.spacing,
    contentPadding: PaddingValues = JengaTabsDefaults.contentPadding,
    colors: JengaTabsColors = JengaTabsDefaults.colors(),
) {
    val fill = arrangement == JengaTabsArrangement.Fill
    Box(
        modifier = modifier
            .fillMaxWidth()
            .drawBehind {
                if (!fill) {
                    val stroke = 1.dp.toPx()
                    drawRect(colors.divider, Offset(0f, size.height - stroke), Size(size.width, stroke))
                }
            },
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .then(if (fill) Modifier else Modifier.horizontalScroll(rememberScrollState()))
                .padding(contentPadding)
                .selectableGroup(),
            horizontalArrangement = if (fill) Arrangement.Start else Arrangement.spacedBy(spacing),
            verticalAlignment = Alignment.Top,
        ) {
            tabs.forEachIndexed { index, title ->
                val selected = index == selectedIndex
                val indicator = when {
                    selected -> colors.indicator
                    fill -> colors.divider
                    else -> Color.Transparent
                }
                Box(
                    modifier = Modifier
                        .then(if (fill) Modifier.weight(1f) else Modifier)
                        .selectable(selected = selected, role = Role.Tab, onClick = { onSelect(index) })
                        .defaultMinSize(minHeight = minHeight)
                        .drawBehind {
                            val thickness = JengaTabsDefaults.IndicatorThickness.toPx()
                            drawRect(indicator, Offset(0f, size.height - thickness), Size(size.width, thickness))
                        },
                    contentAlignment = Alignment.Center,
                ) {
                    Row(
                        modifier = Modifier.padding(
                            start = if (fill) JengaTheme.spacing.md else 0.dp,
                            end = if (fill) JengaTheme.spacing.md else 0.dp,
                            top = JengaTheme.spacing.md,
                            bottom = JengaTheme.spacing.md + JengaTabsDefaults.IndicatorThickness,
                        ),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(JengaTheme.spacing.xs),
                    ) {
                        JengaText(
                            text = title,
                            style = textStyle,
                            color = if (selected) colors.selectedContent else colors.unselectedContent,
                            maxLines = 1,
                        )
                        if (index in dotted) {
                            Box(
                                modifier = Modifier
                                    .size(JengaTabsDefaults.DotSize)
                                    .clip(JengaTheme.shapes.pill)
                                    .background(colors.dot),
                            )
                        }
                    }
                }
            }
        }
    }
}
