package io.github.joelkanyi.jenga.component.menu

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.MenuDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.Immutable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import dev.drewhamilton.poko.Poko
import io.github.joelkanyi.jenga.component.divider.JengaDivider
import io.github.joelkanyi.jenga.component.text.JengaText
import io.github.joelkanyi.jenga.theme.JengaTheme
import io.github.joelkanyi.jenga.theme.LocalJengaContentColor

/** Tone of a [JengaDropdownMenuItem]. */
public enum class JengaDropdownMenuItemTone {
    /** A regular action. */
    Default,

    /** A destructive action, drawn in the error color. */
    Danger,
}

/** Resolved colors for a [JengaDropdownMenuItem]. Override via [JengaDropdownMenuDefaults.itemColors]. */
@Poko
@Immutable
public class JengaDropdownMenuItemColors(
    public val content: Color,
    public val supporting: Color,
    public val dangerContent: Color,
    public val disabledContent: Color,
    public val disabledSupporting: Color,
) {
    public fun copy(
        content: Color = this.content,
        supporting: Color = this.supporting,
        dangerContent: Color = this.dangerContent,
        disabledContent: Color = this.disabledContent,
        disabledSupporting: Color = this.disabledSupporting,
    ): JengaDropdownMenuItemColors = JengaDropdownMenuItemColors(
        content,
        supporting,
        dangerContent,
        disabledContent,
        disabledSupporting,
    )
}

/** Defaults and token mappings for [JengaDropdownMenu] and [JengaDropdownMenuItem]. */
public object JengaDropdownMenuDefaults {
    /** Default menu shape. */
    public val shape: Shape
        @Composable get() = JengaTheme.shapes.md

    /** Default menu shadow elevation. */
    public val ShadowElevation: Dp = MenuDefaults.ShadowElevation

    /** Minimum item height. */
    public val ItemMinHeight: Dp = 48.dp

    /** Padding inside an item. */
    public val itemContentPadding: PaddingValues
        @Composable get() = PaddingValues(horizontal = JengaTheme.spacing.lg, vertical = JengaTheme.spacing.md)

    /** Item label text style. */
    public val itemTextStyle: TextStyle
        @Composable get() = JengaTheme.typography.bodyMedium

    /** Padding around a [JengaDropdownMenuDivider]. */
    public val dividerPadding: PaddingValues
        @Composable get() = PaddingValues(vertical = JengaTheme.spacing.xs)

    /** Themed item colors. */
    @Composable
    public fun itemColors(): JengaDropdownMenuItemColors {
        val c = JengaTheme.colors
        return JengaDropdownMenuItemColors(
            content = c.textPrimary,
            supporting = c.textFaint,
            dangerContent = c.error,
            disabledContent = c.contentDisabled,
            disabledSupporting = c.textFaint,
        )
    }
}

/**
 * A dropdown menu anchored to its caller, themed with Jenga tokens. Place
 * [JengaDropdownMenuItem]s in [content]. Control visibility via [expanded].
 *
 * @sample io.github.joelkanyi.jenga.samples.JengaDropdownMenuSample
 *
 * @param expanded whether the menu is shown.
 * @param onDismissRequest called when the menu should close (outside tap / back).
 * @param modifier the [Modifier] for the menu surface.
 * @param offset offset from the anchor.
 * @param shape the menu shape.
 * @param border optional outline around the menu.
 * @param shadowElevation the menu shadow elevation.
 * @param content the menu items, in a [ColumnScope].
 */
@Composable
public fun JengaDropdownMenu(
    expanded: Boolean,
    onDismissRequest: () -> Unit,
    modifier: Modifier = Modifier,
    offset: DpOffset = DpOffset(0.dp, 0.dp),
    shape: Shape = JengaDropdownMenuDefaults.shape,
    border: BorderStroke? = null,
    shadowElevation: Dp = JengaDropdownMenuDefaults.ShadowElevation,
    content: @Composable ColumnScope.() -> Unit,
) {
    DropdownMenu(
        expanded = expanded,
        onDismissRequest = onDismissRequest,
        modifier = modifier,
        offset = offset,
        shape = shape,
        containerColor = JengaTheme.colors.surface,
        shadowElevation = shadowElevation,
        border = border,
        content = content,
    )
}

/**
 * A single item in a [JengaDropdownMenu]. A disabled item stays visible and can
 * explain itself through [supportingText] (e.g. why the action is unavailable).
 *
 * @param text the item label.
 * @param onClick called when the item is tapped.
 * @param modifier the [Modifier] for this item.
 * @param leadingIcon optional leading icon (inherits content color).
 * @param enabled whether the item is interactive.
 * @param supportingText optional line under the label.
 * @param tone the item tone; see [JengaDropdownMenuItemTone].
 * @param textStyle the label text style.
 * @param minHeight the minimum item height.
 * @param contentPadding padding inside the item.
 * @param colors the color set; defaults to [JengaDropdownMenuDefaults.itemColors].
 */
@Composable
public fun JengaDropdownMenuItem(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    leadingIcon: (@Composable () -> Unit)? = null,
    enabled: Boolean = true,
    supportingText: String? = null,
    tone: JengaDropdownMenuItemTone = JengaDropdownMenuItemTone.Default,
    textStyle: TextStyle = JengaDropdownMenuDefaults.itemTextStyle,
    minHeight: Dp = JengaDropdownMenuDefaults.ItemMinHeight,
    contentPadding: PaddingValues = JengaDropdownMenuDefaults.itemContentPadding,
    colors: JengaDropdownMenuItemColors = JengaDropdownMenuDefaults.itemColors(),
) {
    val contentColor = when {
        !enabled -> colors.disabledContent
        tone == JengaDropdownMenuItemTone.Danger -> colors.dangerContent
        else -> colors.content
    }
    Row(
        modifier = modifier
            .fillMaxWidth()
            .clickable(enabled = enabled, onClick = onClick)
            .defaultMinSize(minHeight = minHeight)
            .padding(contentPadding),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(JengaTheme.spacing.md),
    ) {
        if (leadingIcon != null) {
            CompositionLocalProvider(LocalJengaContentColor provides contentColor) { leadingIcon() }
        }
        Column {
            JengaText(text = text, style = textStyle, color = contentColor, maxLines = 1)
            if (supportingText != null) {
                JengaText(
                    text = supportingText,
                    style = JengaTheme.typography.caption,
                    color = if (enabled) colors.supporting else colors.disabledSupporting,
                )
            }
        }
    }
}

/**
 * A thin divider between groups of [JengaDropdownMenuItem]s.
 *
 * @param modifier the [Modifier] for this divider.
 * @param contentPadding padding around the divider line.
 */
@Composable
public fun JengaDropdownMenuDivider(
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = JengaDropdownMenuDefaults.dividerPadding,
) {
    JengaDivider(modifier = modifier.padding(contentPadding))
}
