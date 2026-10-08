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

/** Defaults and token mappings for [JengaCard]. */
public object JengaCardDefaults {
    /** Default card shape. */
    public val shape: Shape
        @Composable get() = JengaTheme.shapes.card

    /** Width of the border drawn inside the card edge when [JengaCardColors.border] is set. */
    public val BorderWidth: Dp = 1.dp

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

    /** Themed colors per [variant]. */
    @Composable
    public fun colors(variant: JengaCardVariant): JengaCardColors {
        val c = JengaTheme.colors
        return when (variant) {
            JengaCardVariant.Elevated -> JengaCardColors(c.surface, Color.Transparent)
            JengaCardVariant.Outlined -> JengaCardColors(c.surface, c.border)
            JengaCardVariant.Filled -> JengaCardColors(c.surfaceVariant, Color.Transparent)
        }
    }
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
 * @param shape the card shape; defaults to [JengaCardDefaults.shape].
 * @param colors the color set; defaults to [JengaCardDefaults.colors] for [variant].
 * @param elevation resting shadow depth; defaults to [JengaCardDefaults.elevation].
 * @param contentPadding inner padding around [content].
 * @param borderWidth width of the border, drawn inside the card edge (e.g. 2dp for a selected card).
 * @param disabledAlpha opacity of the whole card when [enabled] is false.
 * @param content the card body, laid out in a [ColumnScope].
 */
@Composable
public fun JengaCard(
    modifier: Modifier = Modifier,
    variant: JengaCardVariant = JengaCardVariant.Elevated,
    onClick: (() -> Unit)? = null,
    enabled: Boolean = true,
    shape: Shape = JengaCardDefaults.shape,
    colors: JengaCardColors = JengaCardDefaults.colors(variant),
    elevation: Dp = JengaCardDefaults.elevation(variant),
    contentPadding: PaddingValues = JengaCardDefaults.contentPadding,
    borderWidth: Dp = JengaCardDefaults.BorderWidth,
    disabledAlpha: Float = JengaCardDefaults.DisabledAlpha,
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
