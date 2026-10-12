package io.github.joelkanyi.jenga.component.navigation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.selection.selectable
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.Immutable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import dev.drewhamilton.poko.Poko
import io.github.joelkanyi.jenga.component.text.JengaText
import io.github.joelkanyi.jenga.theme.JengaTheme
import io.github.joelkanyi.jenga.theme.LocalJengaContentColor

/** Resolved colors for a [JengaNavigationRailItem]. Override via [JengaNavigationRailItemDefaults.colors]. */
@Poko
@Immutable
public class JengaNavigationRailItemColors(
    public val selectedContainer: Color,
    public val selectedContent: Color,
    public val unselectedContent: Color,
    public val disabledContent: Color,
    public val dot: Color,
) {
    public fun copy(
        selectedContainer: Color = this.selectedContainer,
        selectedContent: Color = this.selectedContent,
        unselectedContent: Color = this.unselectedContent,
        disabledContent: Color = this.disabledContent,
        dot: Color = this.dot,
    ): JengaNavigationRailItemColors = JengaNavigationRailItemColors(
        selectedContainer,
        selectedContent,
        unselectedContent,
        disabledContent,
        dot,
    )
}

/**
 * Theme-wide style for every [JengaNavigationRailItem], set through
 * [io.github.joelkanyi.jenga.theme.JengaComponentStyles.navigationRailItem]. A `null` value keeps
 * Jenga's default; arguments passed to a call still win.
 *
 * @property minHeight the minimum item height.
 * @property shape the item shape.
 * @property contentPadding padding inside the item.
 * @property textStyle the label text style.
 * @property selectedTextStyle merged onto the label text style while selected, so set only what
 *   changes, e.g. `TextStyle(fontWeight = FontWeight.SemiBold)`.
 * @property colors the color set.
 */
@Poko
@Immutable
public class JengaNavigationRailItemStyle(
    public val minHeight: Dp? = null,
    public val shape: Shape? = null,
    public val contentPadding: PaddingValues? = null,
    public val textStyle: TextStyle? = null,
    public val selectedTextStyle: TextStyle? = null,
    public val colors: JengaNavigationRailItemColors? = null,
) {
    public fun copy(
        minHeight: Dp? = this.minHeight,
        shape: Shape? = this.shape,
        contentPadding: PaddingValues? = this.contentPadding,
        textStyle: TextStyle? = this.textStyle,
        selectedTextStyle: TextStyle? = this.selectedTextStyle,
        colors: JengaNavigationRailItemColors? = this.colors,
    ): JengaNavigationRailItemStyle = JengaNavigationRailItemStyle(
        minHeight,
        shape,
        contentPadding,
        textStyle,
        selectedTextStyle,
        colors,
    )
}

/**
 * Defaults and token mappings for [JengaNavigationRailItem]. The composable members follow the theme's
 * [JengaNavigationRailItemStyle]; the constants are Jenga's built-in values, and
 * [JengaNavigationRailItem] resolves the theme's values over them.
 */
public object JengaNavigationRailItemDefaults {
    /** Minimum item height. */
    public val MinHeight: Dp = 48.dp

    /** Diameter of the attention dot. */
    public val DotSize: Dp = 6.dp

    /** Default item shape. */
    public val shape: Shape
        @Composable get() = JengaTheme.components.navigationRailItem?.shape ?: JengaTheme.shapes.md

    /** Padding inside the item. */
    public val contentPadding: PaddingValues
        @Composable get() = JengaTheme.components.navigationRailItem?.contentPadding ?: PaddingValues(horizontal = JengaTheme.spacing.md)

    /** Label text style. */
    public val textStyle: TextStyle
        @Composable get() = JengaTheme.components.navigationRailItem?.textStyle ?: JengaTheme.typography.bodyMedium

    /** Themed colors. */
    @Composable
    public fun colors(): JengaNavigationRailItemColors {
        JengaTheme.components.navigationRailItem?.colors?.let { return it }
        val c = JengaTheme.colors
        return JengaNavigationRailItemColors(
            selectedContainer = c.brandSubtle,
            selectedContent = c.brand,
            unselectedContent = c.textSecondary,
            disabledContent = c.contentDisabled,
            dot = c.warning,
        )
    }
}

/**
 * A full-width destination row for a side navigation list (e.g. the rail of a
 * landscape or tablet layout): an icon and a label, with a filled background
 * when [selected]. Stack items in a column; the caller owns the layout.
 *
 * @sample io.github.joelkanyi.jenga.samples.JengaNavigationRailItemSample
 *
 * @param label the destination name.
 * @param selected whether this item is the current destination.
 * @param onClick called when the item is tapped.
 * @param icon the destination icon; inherits the content color.
 * @param modifier the [Modifier] for this item.
 * @param enabled whether the item is interactive.
 * @param showDot whether a small attention dot follows the label.
 * @param minHeight the minimum item height; defaults to the theme's value, else
 *   [JengaNavigationRailItemDefaults.MinHeight].
 * @param shape the item shape.
 * @param contentPadding padding inside the item.
 * @param textStyle the label text style.
 * @param selectedTextStyle the label text style while selected; defaults to [textStyle] with the
 *   theme's [JengaNavigationRailItemStyle.selectedTextStyle] merged on top.
 * @param colors the color set; defaults to [JengaNavigationRailItemDefaults.colors].
 */
@Composable
public fun JengaNavigationRailItem(
    label: String,
    selected: Boolean,
    onClick: () -> Unit,
    icon: @Composable () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    showDot: Boolean = false,
    minHeight: Dp = JengaTheme.components.navigationRailItem?.minHeight ?: JengaNavigationRailItemDefaults.MinHeight,
    shape: Shape = JengaNavigationRailItemDefaults.shape,
    contentPadding: PaddingValues = JengaNavigationRailItemDefaults.contentPadding,
    textStyle: TextStyle = JengaNavigationRailItemDefaults.textStyle,
    selectedTextStyle: TextStyle = textStyle.merge(JengaTheme.components.navigationRailItem?.selectedTextStyle),
    colors: JengaNavigationRailItemColors = JengaNavigationRailItemDefaults.colors(),
) {
    val contentColor = when {
        !enabled -> colors.disabledContent
        selected -> colors.selectedContent
        else -> colors.unselectedContent
    }
    Row(
        modifier = modifier
            .fillMaxWidth()
            .clip(shape)
            .background(if (selected) colors.selectedContainer else Color.Transparent)
            .selectable(selected = selected, enabled = enabled, role = Role.Tab, onClick = onClick)
            .defaultMinSize(minHeight = minHeight)
            .padding(contentPadding),
        horizontalArrangement = Arrangement.spacedBy(JengaTheme.spacing.sm),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        CompositionLocalProvider(LocalJengaContentColor provides contentColor) {
            icon()
            JengaText(
                text = label,
                style = if (selected) selectedTextStyle else textStyle,
                color = contentColor,
                maxLines = 1,
                modifier = Modifier.weight(1f, fill = false),
            )
            if (showDot) {
                Box(
                    modifier = Modifier
                        .size(JengaNavigationRailItemDefaults.DotSize)
                        .clip(JengaTheme.shapes.pill)
                        .background(colors.dot),
                )
            }
        }
    }
}
