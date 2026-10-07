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
    public val border: Color,
) {
    public fun copy(
        container: Color = this.container,
        content: Color = this.content,
        disabledContainer: Color = this.disabledContainer,
        disabledContent: Color = this.disabledContent,
        border: Color = this.border,
    ): JengaIconButtonColors = JengaIconButtonColors(container, content, disabledContainer, disabledContent, border)
}

/** Defaults and token mappings for [JengaIconButton]. */
public object JengaIconButtonDefaults {
    /** Diameter of the button surface (the touch target is expanded to 48dp). */
    public val Size: Dp = 40.dp

    /** Default shape: a circle. */
    public val shape: Shape
        @Composable get() = JengaTheme.shapes.pill

    /** Themed colors for the given [variant]. */
    @Composable
    public fun colors(variant: JengaIconButtonVariant): JengaIconButtonColors {
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
 * @param shape the button shape; defaults to [JengaIconButtonDefaults.shape].
 * @param colors the color set; defaults to [JengaIconButtonDefaults.colors] for [variant].
 * @param size the visible button size; the touch target is never smaller than 48dp.
 * @param content the icon (typically a single [JengaIcon]); inherits content color.
 */
@Composable
public fun JengaIconButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    variant: JengaIconButtonVariant = JengaIconButtonVariant.Standard,
    enabled: Boolean = true,
    shape: Shape = JengaIconButtonDefaults.shape,
    colors: JengaIconButtonColors = JengaIconButtonDefaults.colors(variant),
    size: Dp = JengaIconButtonDefaults.Size,
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
