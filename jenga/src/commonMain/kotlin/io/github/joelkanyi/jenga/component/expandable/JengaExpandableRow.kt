package io.github.joelkanyi.jenga.component.expandable

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.unit.dp
import dev.drewhamilton.poko.Poko
import io.github.joelkanyi.jenga.component.icon.JengaIcon
import io.github.joelkanyi.jenga.theme.JengaTheme

/** Resolved colors for a [JengaExpandableRow]. Override via [JengaExpandableRowDefaults.colors]. */
@Poko
@Immutable
public class JengaExpandableRowColors(
    public val container: Color,
    public val border: Color,
    public val chevron: Color,
) {
    public fun copy(
        container: Color = this.container,
        border: Color = this.border,
        chevron: Color = this.chevron,
    ): JengaExpandableRowColors = JengaExpandableRowColors(container, border, chevron)
}

/** Defaults and token mappings for [JengaExpandableRow]. */
public object JengaExpandableRowDefaults {
    /** Default card shape. */
    public val shape: Shape
        @Composable get() = JengaTheme.shapes.cardLarge

    /** Padding around the header row. */
    public val headerPadding: PaddingValues
        @Composable get() = PaddingValues(horizontal = JengaTheme.spacing.lg, vertical = JengaTheme.spacing.md)

    /** Padding around the expanded body. */
    public val contentPadding: PaddingValues
        @Composable get() = PaddingValues(
            start = JengaTheme.spacing.lg,
            end = JengaTheme.spacing.lg,
            bottom = JengaTheme.spacing.lg,
        )

    /**
     * Themed colors: an outlined surface card. For a row inside an existing card, pass
     * transparent container and border (and a rectangle [shape]).
     */
    @Composable
    public fun colors(): JengaExpandableRowColors {
        val c = JengaTheme.colors
        return JengaExpandableRowColors(container = c.surface, border = c.border, chevron = c.textFaint)
    }
}

/**
 * An outlined card whose [header] row toggles an expandable [content] body: a
 * generic accordion (day/agenda cards, FAQ rows, grouped settings). The [header]
 * is laid out in a [RowScope]; the block appends an optional rotating chevron.
 * Domain-neutral and token-driven. With transparent [colors] and a rectangle
 * [shape] it becomes a plain row for use inside another card.
 *
 * @param expanded whether the [content] is currently shown.
 * @param onExpandedChange called with the requested new state when the header is tapped.
 * @param header the header content, laid out before the chevron.
 * @param modifier the [Modifier] for the card.
 * @param showChevron whether to append a chevron that rotates when expanded.
 * @param chevronContentDescription accessibility label for the chevron.
 * @param shape the card shape.
 * @param colors the color set; defaults to [JengaExpandableRowDefaults.colors].
 * @param headerPadding padding around the header row.
 * @param contentPadding padding around the expanded body.
 * @param content the expandable body, shown when [expanded].
 */
@Composable
public fun JengaExpandableRow(
    expanded: Boolean,
    onExpandedChange: (Boolean) -> Unit,
    header: @Composable RowScope.() -> Unit,
    modifier: Modifier = Modifier,
    showChevron: Boolean = true,
    chevronContentDescription: String? = null,
    shape: Shape = JengaExpandableRowDefaults.shape,
    colors: JengaExpandableRowColors = JengaExpandableRowDefaults.colors(),
    headerPadding: PaddingValues = JengaExpandableRowDefaults.headerPadding,
    contentPadding: PaddingValues = JengaExpandableRowDefaults.contentPadding,
    content: @Composable ColumnScope.() -> Unit,
) {
    val chevronRotation by animateFloatAsState(if (expanded) 90f else 0f, label = "chevron")
    Column(
        modifier = modifier
            .fillMaxWidth()
            .clip(shape)
            .background(colors.container)
            .border(1.dp, colors.border, shape),
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clickable(role = Role.Button) { onExpandedChange(!expanded) }
                .padding(headerPadding),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            header()
            if (showChevron) {
                JengaIcon(
                    imageVector = JengaTheme.icons.chevron,
                    contentDescription = chevronContentDescription,
                    tint = colors.chevron,
                    modifier = Modifier.rotate(chevronRotation),
                )
            }
        }
        if (expanded) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(contentPadding),
            ) { content() }
        }
    }
}
