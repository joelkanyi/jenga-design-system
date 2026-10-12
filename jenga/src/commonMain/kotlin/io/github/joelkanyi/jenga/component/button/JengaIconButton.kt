package io.github.joelkanyi.jenga.component.button

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.material3.minimumInteractiveComponentSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.Immutable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import dev.drewhamilton.poko.Poko
import io.github.joelkanyi.jenga.component.icon.JengaIcon
import io.github.joelkanyi.jenga.theme.JengaTheme
import io.github.joelkanyi.jenga.theme.LocalJengaContentColor

/** Visual style of a [JengaIconButton]. */
public enum class JengaIconButtonVariant {
    /** Transparent: the default for app-bar and toolbar actions. */
    Standard,

    /** Filled brand: a prominent, single-icon action. */
    Filled,

    /** Soft brand-subtle fill, medium emphasis. */
    Tonal,

    /** Dark translucent circle for controls floating over a camera feed / photo /
     *  video. Uses the always-dark media-overlay tokens so it stays legible on any
     *  backdrop, in light or dark theme. */
    Overlay,

    /** Surface fill with a strong border, for a secondary action beside a primary button. */
    Outlined,
}

/** Resolved colors for a [JengaIconButton]. Override via [JengaIconButtonDefaults.colors]. */
@Poko
@Immutable
public class JengaIconButtonColors(
    public val container: Color,
    public val content: Color,
    public val disabledContainer: Color,
    public val disabledContent: Color,
    public val border: Color = Color.Transparent,
) {
    public fun copy(
        container: Color = this.container,
        content: Color = this.content,
        disabledContainer: Color = this.disabledContainer,
        disabledContent: Color = this.disabledContent,
        border: Color = this.border,
    ): JengaIconButtonColors = JengaIconButtonColors(container, content, disabledContainer, disabledContent, border)
}

/**
 * Theme-wide look for one [JengaIconButtonVariant], part of a [JengaIconButtonStyle]. A `null`
 * value keeps Jenga's default.
 *
 * @property size the visible button size.
 * @property shape the button shape.
 * @property colors the color set.
 */
@Poko
@Immutable
public class JengaIconButtonVariantStyle(
    public val size: Dp? = null,
    public val shape: Shape? = null,
    public val colors: JengaIconButtonColors? = null,
) {
    public fun copy(
        size: Dp? = this.size,
        shape: Shape? = this.shape,
        colors: JengaIconButtonColors? = this.colors,
    ): JengaIconButtonVariantStyle = JengaIconButtonVariantStyle(size, shape, colors)
}

/**
 * Theme-wide style for every [JengaIconButton], set through
 * [io.github.joelkanyi.jenga.theme.JengaComponentStyles.iconButton]. A `null` value keeps Jenga's
 * default; arguments passed to a [JengaIconButton] call still win.
 *
 * @property standard the look of [JengaIconButtonVariant.Standard].
 * @property filled the look of [JengaIconButtonVariant.Filled].
 * @property tonal the look of [JengaIconButtonVariant.Tonal].
 * @property overlay the look of [JengaIconButtonVariant.Overlay].
 * @property outlined the look of [JengaIconButtonVariant.Outlined].
 */
@Poko
@Immutable
public class JengaIconButtonStyle(
    public val standard: JengaIconButtonVariantStyle? = null,
    public val filled: JengaIconButtonVariantStyle? = null,
    public val tonal: JengaIconButtonVariantStyle? = null,
    public val overlay: JengaIconButtonVariantStyle? = null,
    public val outlined: JengaIconButtonVariantStyle? = null,
) {
    public fun copy(
        standard: JengaIconButtonVariantStyle? = this.standard,
        filled: JengaIconButtonVariantStyle? = this.filled,
        tonal: JengaIconButtonVariantStyle? = this.tonal,
        overlay: JengaIconButtonVariantStyle? = this.overlay,
        outlined: JengaIconButtonVariantStyle? = this.outlined,
    ): JengaIconButtonStyle = JengaIconButtonStyle(standard, filled, tonal, overlay, outlined)

    internal fun variant(variant: JengaIconButtonVariant): JengaIconButtonVariantStyle? = when (variant) {
        JengaIconButtonVariant.Standard -> standard
        JengaIconButtonVariant.Filled -> filled
        JengaIconButtonVariant.Tonal -> tonal
        JengaIconButtonVariant.Overlay -> overlay
        JengaIconButtonVariant.Outlined -> outlined
    }
}

/**
 * Defaults and token mappings for [JengaIconButton].
 *
 * [colors] follows the theme's [JengaIconButtonStyle]; [Size] and [shape] are Jenga's built-in
 * values.
 */
public object JengaIconButtonDefaults {
    /** Diameter of the button surface (the touch target is expanded to 48dp). */
    public val Size: Dp = 40.dp

    /** Default shape: a circle. */
    public val shape: Shape
        @Composable get() = JengaTheme.shapes.pill

    /** Themed colors for the given [variant]. */
    @Composable
    public fun colors(variant: JengaIconButtonVariant): JengaIconButtonColors {
        JengaTheme.components.iconButton?.variant(variant)?.colors?.let { return it }
        val c = JengaTheme.colors
        return when (variant) {
            JengaIconButtonVariant.Standard -> JengaIconButtonColors(
                container = Color.Transparent,
                content = c.textSecondary,
                disabledContainer = Color.Transparent,
                disabledContent = c.contentDisabled,
                border = Color.Transparent,
            )
            JengaIconButtonVariant.Filled -> JengaIconButtonColors(
                container = c.brand,
                content = c.onBrand,
                disabledContainer = c.surfaceDisabled,
                disabledContent = c.contentDisabled,
                border = Color.Transparent,
            )
            JengaIconButtonVariant.Tonal -> JengaIconButtonColors(
                container = c.brandSubtle,
                content = c.onBrandSubtle,
                disabledContainer = c.surfaceDisabled,
                disabledContent = c.contentDisabled,
                border = Color.Transparent,
            )
            JengaIconButtonVariant.Overlay -> JengaIconButtonColors(
                container = c.overlaySurface,
                content = c.onOverlay,
                disabledContainer = c.overlaySurface,
                disabledContent = c.onOverlayMuted,
                border = Color.Transparent,
            )
            JengaIconButtonVariant.Outlined -> JengaIconButtonColors(
                container = c.surface,
                content = c.textPrimary,
                disabledContainer = c.surfaceDisabled,
                disabledContent = c.contentDisabled,
                border = c.borderStrong,
            )
        }
    }
}

/**
 * A compact, icon-only button, for app-bar actions, toolbars, and inline
 * controls where a labelled [JengaButton] would be too heavy.
 *
 * The visible surface is [size], but the touch target is
 * always expanded to the 48dp accessibility minimum. The icon inherits the
 * resolved content color via [LocalJengaContentColor].
 *
 * @sample io.github.joelkanyi.jenga.samples.JengaIconButtonSample
 *
 * @param onClick called when tapped (ignored while disabled).
 * @param modifier the [Modifier] for this button.
 * @param variant the visual style; see [JengaIconButtonVariant].
 * @param enabled whether the button is interactive.
 * @param shape the button shape; defaults to the theme's [JengaIconButtonVariantStyle.shape] for
 *   [variant], else [JengaIconButtonDefaults.shape].
 * @param colors the color set; defaults to [JengaIconButtonDefaults.colors] for [variant].
 * @param size the visible button size; the touch target is never smaller than 48dp. Defaults to the
 *   theme's [JengaIconButtonVariantStyle.size] for [variant], else [JengaIconButtonDefaults.Size].
 * @param content the icon (typically a single [JengaIcon]); inherits content color.
 */
@Composable
public fun JengaIconButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    variant: JengaIconButtonVariant = JengaIconButtonVariant.Standard,
    enabled: Boolean = true,
    shape: Shape = JengaTheme.components.iconButton?.variant(variant)?.shape ?: JengaIconButtonDefaults.shape,
    colors: JengaIconButtonColors = JengaIconButtonDefaults.colors(variant),
    size: Dp = JengaTheme.components.iconButton?.variant(variant)?.size ?: JengaIconButtonDefaults.Size,
    content: @Composable () -> Unit,
) {
    val container = if (enabled) colors.container else colors.disabledContainer
    val contentColor = if (enabled) colors.content else colors.disabledContent
    Box(
        modifier = modifier
            .minimumInteractiveComponentSize()
            .clip(shape)
            .background(container)
            .border(1.dp, colors.border, shape)
            .clickable(enabled = enabled, role = Role.Button, onClick = onClick)
            .size(size),
        contentAlignment = Alignment.Center,
    ) {
        CompositionLocalProvider(LocalJengaContentColor provides contentColor) {
            content()
        }
    }
}
