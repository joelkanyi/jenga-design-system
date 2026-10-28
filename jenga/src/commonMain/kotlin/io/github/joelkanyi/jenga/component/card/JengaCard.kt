package io.github.joelkanyi.jenga.component.card

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.disabled
import androidx.compose.ui.semantics.selected
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import dev.drewhamilton.poko.Poko
import io.github.joelkanyi.jenga.theme.JengaTheme

/** Visual style of a [JengaCard]. */
public enum class JengaCardVariant {
    /** Raised surface with a shadow. */
    Elevated,

    /** Flat surface with a hairline border. */
    Outlined,

    /** Flat, tinted surface (no border, no shadow). */
    Filled,
}

/** Resolved colors for a [JengaCard]. */
@Poko
@Immutable
public class JengaCardColors(
    public val container: Color,
    public val border: Color,
) {
    public fun copy(
        container: Color = this.container,
        border: Color = this.border,
    ): JengaCardColors = JengaCardColors(container, border)
}

/**
 * Theme-wide style for every [JengaCard], set through
 * [io.github.joelkanyi.jenga.theme.JengaComponentStyles.card]. A `null` value keeps Jenga's
 * default; arguments passed to a [JengaCard] call still win.
 *
 * @property shape the card shape.
 * @property contentPadding inner padding.
 * @property borderWidth width of the border.
 * @property disabledAlpha opacity of a disabled card.
 * @property elevated colors for [JengaCardVariant.Elevated].
 * @property outlined colors for [JengaCardVariant.Outlined].
 * @property filled colors for [JengaCardVariant.Filled].
 */
@Poko
@Immutable
public class JengaCardStyle(
    public val shape: Shape? = null,
    public val contentPadding: PaddingValues? = null,
    public val borderWidth: Dp? = null,
    public val disabledAlpha: Float? = null,
    public val elevated: JengaCardColors? = null,
    public val outlined: JengaCardColors? = null,
    public val filled: JengaCardColors? = null,
) {
    public fun copy(
        shape: Shape? = this.shape,
        contentPadding: PaddingValues? = this.contentPadding,
        borderWidth: Dp? = this.borderWidth,
        disabledAlpha: Float? = this.disabledAlpha,
        elevated: JengaCardColors? = this.elevated,
        outlined: JengaCardColors? = this.outlined,
        filled: JengaCardColors? = this.filled,
    ): JengaCardStyle = JengaCardStyle(shape, contentPadding, borderWidth, disabledAlpha, elevated, outlined, filled)

    internal fun colors(variant: JengaCardVariant): JengaCardColors? = when (variant) {
        JengaCardVariant.Elevated -> elevated
        JengaCardVariant.Outlined -> outlined
        JengaCardVariant.Filled -> filled
    }
}

/**
 * Defaults and token mappings for [JengaCard].
 *
 * The composable members follow the theme's [JengaCardStyle]; the constants are Jenga's built-in
 * values.
 */
public object JengaCardDefaults {
    /** Default card shape. */
    public val shape: Shape
        @Composable get() = JengaTheme.components.card?.shape ?: JengaTheme.shapes.card

    /** Width of the border drawn inside the card edge when [JengaCardColors.border] is set. */
    public val BorderWidth: Dp = 1.dp

    /** Width of the border of a selected card. */
    public val SelectedBorderWidth: Dp = 2.dp

    /** Opacity of the whole card when it is not enabled. */
    public const val DisabledAlpha: Float = 0.38f

    /** Default inner padding. */
    public val contentPadding: PaddingValues = PaddingValues(16.dp)

    /** Resting elevation per [variant], from [JengaTheme.elevation]. */
    @Composable
    public fun elevation(variant: JengaCardVariant): Dp = when (variant) {
        JengaCardVariant.Elevated -> JengaTheme.elevation.sm
        JengaCardVariant.Outlined -> JengaTheme.elevation.none
        JengaCardVariant.Filled -> JengaTheme.elevation.none
    }

    /** Themed colors per [variant]; a [selected] card takes the brand color as its border. */
    @Composable
    public fun colors(variant: JengaCardVariant, selected: Boolean = false): JengaCardColors {
        val c = JengaTheme.colors
        val colors = JengaTheme.components.card?.colors(variant) ?: when (variant) {
            JengaCardVariant.Elevated -> JengaCardColors(c.surface, if (c.isLight) Color.Transparent else c.border)
            JengaCardVariant.Outlined -> JengaCardColors(c.surface, c.border)
            JengaCardVariant.Filled -> JengaCardColors(c.surfaceVariant, Color.Transparent)
        }
        return if (selected) colors.copy(border = c.brand) else colors
    }

    @Deprecated("Kept for binary compatibility", level = DeprecationLevel.HIDDEN)
    @Composable
    public fun colors(variant: JengaCardVariant): JengaCardColors = colors(variant, selected = false)
}

/**
 * A surface that groups related content.
 *
 * @sample io.github.joelkanyi.jenga.samples.JengaCardSample
 *
 * @param modifier the [Modifier] for this card.
 * @param variant the visual style; see [JengaCardVariant].
 * @param onClick optional click handler; when set, the card becomes focusable
 *   and clickable with a ripple.
 * @param enabled when false, the card is drawn at [disabledAlpha], [onClick] is ignored and
 *   the card is reported as disabled to accessibility services.
 * @param selected whether the card is the chosen one; draws a brand border of
 *   [JengaCardDefaults.SelectedBorderWidth] and reports the card as selected to accessibility services.
 * @param shape the card shape; defaults to [JengaCardDefaults.shape].
 * @param colors the color set; defaults to [JengaCardDefaults.colors] for [variant] and [selected].
 * @param elevation resting shadow depth; defaults to [JengaCardDefaults.elevation].
 * @param contentPadding inner padding around [content]; defaults to the theme's [JengaCardStyle], else
 *   [JengaCardDefaults.contentPadding].
 * @param borderWidth width of the border, drawn inside the card edge; defaults to
 *   [JengaCardDefaults.SelectedBorderWidth] when [selected], else the theme's [JengaCardStyle], else
 *   [JengaCardDefaults.BorderWidth].
 * @param disabledAlpha opacity of the whole card when [enabled] is false; defaults to the theme's
 *   [JengaCardStyle], else [JengaCardDefaults.DisabledAlpha].
 * @param content the card body, laid out in a [ColumnScope].
 */
@Composable
public fun JengaCard(
    modifier: Modifier = Modifier,
    variant: JengaCardVariant = JengaCardVariant.Elevated,
    onClick: (() -> Unit)? = null,
    enabled: Boolean = true,
    selected: Boolean = false,
    shape: Shape = JengaCardDefaults.shape,
    colors: JengaCardColors = JengaCardDefaults.colors(variant, selected),
    elevation: Dp = JengaCardDefaults.elevation(variant),
    contentPadding: PaddingValues = JengaTheme.components.card?.contentPadding ?: JengaCardDefaults.contentPadding,
    borderWidth: Dp = if (selected) {
        JengaCardDefaults.SelectedBorderWidth
    } else {
        JengaTheme.components.card?.borderWidth ?: JengaCardDefaults.BorderWidth
    },
    disabledAlpha: Float = JengaTheme.components.card?.disabledAlpha ?: JengaCardDefaults.DisabledAlpha,
    content: @Composable ColumnScope.() -> Unit,
) {
    Column(
        modifier = modifier
            .then(
                if (enabled) {
                    Modifier
                } else {
                    Modifier.alpha(disabledAlpha).semantics { disabled() }
                },
            )
            .then(if (selected) Modifier.semantics { this.selected = true } else Modifier)
            .shadow(elevation, shape)
            .clip(shape)
            .background(colors.container)
            .then(
                if (colors.border != Color.Transparent) {
                    Modifier.border(borderWidth, colors.border, shape)
                } else {
                    Modifier
                },
            )
            .then(
                if (onClick != null) {
                    Modifier.clickable(enabled = enabled, role = Role.Button, onClick = onClick)
                } else {
                    Modifier
                },
            )
            .padding(contentPadding),
        content = content,
    )
}

@Deprecated("Kept for binary compatibility", level = DeprecationLevel.HIDDEN)
@Composable
public fun JengaCard(
    modifier: Modifier = Modifier,
    variant: JengaCardVariant = JengaCardVariant.Elevated,
    onClick: (() -> Unit)? = null,
    enabled: Boolean = true,
    shape: Shape = JengaCardDefaults.shape,
    colors: JengaCardColors = JengaCardDefaults.colors(variant, selected = false),
    elevation: Dp = JengaCardDefaults.elevation(variant),
    contentPadding: PaddingValues = JengaTheme.components.card?.contentPadding ?: JengaCardDefaults.contentPadding,
    borderWidth: Dp = JengaTheme.components.card?.borderWidth ?: JengaCardDefaults.BorderWidth,
    disabledAlpha: Float = JengaTheme.components.card?.disabledAlpha ?: JengaCardDefaults.DisabledAlpha,
    content: @Composable ColumnScope.() -> Unit,
) {
    JengaCard(
        modifier = modifier,
        variant = variant,
        onClick = onClick,
        enabled = enabled,
        selected = false,
        shape = shape,
        colors = colors,
        elevation = elevation,
        contentPadding = contentPadding,
        borderWidth = borderWidth,
        disabledAlpha = disabledAlpha,
        content = content,
    )
}
