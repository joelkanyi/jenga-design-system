package io.github.joelkanyi.jenga.component.button

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.minimumInteractiveComponentSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import dev.drewhamilton.poko.Poko
import io.github.joelkanyi.jenga.component.text.JengaText
import io.github.joelkanyi.jenga.theme.JengaTheme
import io.github.joelkanyi.jenga.theme.LocalJengaContentColor

/** Visual style of a [JengaButton]. */
public enum class JengaButtonVariant {
    /** Primary call to action: filled brand orange. */
    Primary,

    /** High-emphasis alternative: filled ink (navy). */
    Ink,

    /** Low-emphasis, transparent, for tertiary actions. */
    Ghost,

    /** Secondary: filled neutral (surface-variant), no border, for the calm
     *  companion to a Primary action (e.g. an "Another"/"Skip" beside "Confirm"). */
    Neutral,

    /** Medium-emphasis: bordered on a surface fill. */
    Outline,

    /** Destructive action: bordered, error-colored. */
    Danger,

    /** Medium-emphasis in the brand color: brand border and label on a surface fill. */
    BrandOutline,
}

/** Size of a [JengaButton]; controls height, padding and label style. */
public enum class JengaButtonSize { Small, Medium, Large }

/**
 * Resolved colors for a [JengaButton] in a single state set. Obtain a themed
 * instance from [JengaButtonDefaults.colors] and `copy(...)` to override.
 */
@Poko
@androidx.compose.runtime.Immutable
public class JengaButtonColors(
    public val container: Color,
    public val content: Color,
    public val border: Color,
    public val disabledContainer: Color,
    public val disabledContent: Color,
    public val disabledBorder: Color,
) {
    public fun copy(
        container: Color = this.container,
        content: Color = this.content,
        border: Color = this.border,
        disabledContainer: Color = this.disabledContainer,
        disabledContent: Color = this.disabledContent,
        disabledBorder: Color = this.disabledBorder,
    ): JengaButtonColors = JengaButtonColors(container, content, border, disabledContainer, disabledContent, disabledBorder)
}

/**
 * Theme-wide metrics for one [JengaButtonSize], part of a [JengaButtonStyle]. A `null` value keeps
 * Jenga's default.
 *
 * @property minHeight the minimum height.
 * @property shape the button shape.
 * @property contentPadding inner padding.
 * @property textStyle the label text style.
 */
@Poko
@androidx.compose.runtime.Immutable
public class JengaButtonSizeStyle(
    public val minHeight: Dp? = null,
    public val shape: Shape? = null,
    public val contentPadding: PaddingValues? = null,
    public val textStyle: TextStyle? = null,
) {
    public fun copy(
        minHeight: Dp? = this.minHeight,
        shape: Shape? = this.shape,
        contentPadding: PaddingValues? = this.contentPadding,
        textStyle: TextStyle? = this.textStyle,
    ): JengaButtonSizeStyle = JengaButtonSizeStyle(minHeight, shape, contentPadding, textStyle)
}

/**
 * Theme-wide style for every [JengaButton], set through
 * [io.github.joelkanyi.jenga.theme.JengaComponentStyles.button]. A `null` value keeps Jenga's
 * default; arguments passed to a [JengaButton] call still win.
 *
 * @property small metrics for [JengaButtonSize.Small].
 * @property medium metrics for [JengaButtonSize.Medium].
 * @property large metrics for [JengaButtonSize.Large].
 * @property primary colors for [JengaButtonVariant.Primary].
 * @property ink colors for [JengaButtonVariant.Ink].
 * @property ghost colors for [JengaButtonVariant.Ghost].
 * @property neutral colors for [JengaButtonVariant.Neutral].
 * @property outline colors for [JengaButtonVariant.Outline].
 * @property danger colors for [JengaButtonVariant.Danger].
 * @property brandOutline colors for [JengaButtonVariant.BrandOutline].
 */
@Poko
@androidx.compose.runtime.Immutable
public class JengaButtonStyle(
    public val small: JengaButtonSizeStyle? = null,
    public val medium: JengaButtonSizeStyle? = null,
    public val large: JengaButtonSizeStyle? = null,
    public val primary: JengaButtonColors? = null,
    public val ink: JengaButtonColors? = null,
    public val ghost: JengaButtonColors? = null,
    public val neutral: JengaButtonColors? = null,
    public val outline: JengaButtonColors? = null,
    public val danger: JengaButtonColors? = null,
    public val brandOutline: JengaButtonColors? = null,
) {
    @Deprecated("Kept for binary compatibility", level = DeprecationLevel.HIDDEN)
    public constructor(
        small: JengaButtonSizeStyle? = null,
        medium: JengaButtonSizeStyle? = null,
        large: JengaButtonSizeStyle? = null,
        primary: JengaButtonColors? = null,
        ink: JengaButtonColors? = null,
        ghost: JengaButtonColors? = null,
        neutral: JengaButtonColors? = null,
        outline: JengaButtonColors? = null,
        danger: JengaButtonColors? = null,
    ) : this(small, medium, large, primary, ink, ghost, neutral, outline, danger, brandOutline = null)

    public fun copy(
        small: JengaButtonSizeStyle? = this.small,
        medium: JengaButtonSizeStyle? = this.medium,
        large: JengaButtonSizeStyle? = this.large,
        primary: JengaButtonColors? = this.primary,
        ink: JengaButtonColors? = this.ink,
        ghost: JengaButtonColors? = this.ghost,
        neutral: JengaButtonColors? = this.neutral,
        outline: JengaButtonColors? = this.outline,
        danger: JengaButtonColors? = this.danger,
        brandOutline: JengaButtonColors? = this.brandOutline,
    ): JengaButtonStyle = JengaButtonStyle(small, medium, large, primary, ink, ghost, neutral, outline, danger, brandOutline)

    @Deprecated("Kept for binary compatibility", level = DeprecationLevel.HIDDEN)
    public fun copy(
        small: JengaButtonSizeStyle? = this.small,
        medium: JengaButtonSizeStyle? = this.medium,
        large: JengaButtonSizeStyle? = this.large,
        primary: JengaButtonColors? = this.primary,
        ink: JengaButtonColors? = this.ink,
        ghost: JengaButtonColors? = this.ghost,
        neutral: JengaButtonColors? = this.neutral,
        outline: JengaButtonColors? = this.outline,
        danger: JengaButtonColors? = this.danger,
    ): JengaButtonStyle = copy(small, medium, large, primary, ink, ghost, neutral, outline, danger, this.brandOutline)

    internal fun size(size: JengaButtonSize): JengaButtonSizeStyle? = when (size) {
        JengaButtonSize.Small -> small
        JengaButtonSize.Medium -> medium
        JengaButtonSize.Large -> large
    }

    internal fun colors(variant: JengaButtonVariant): JengaButtonColors? = when (variant) {
        JengaButtonVariant.Primary -> primary
        JengaButtonVariant.Ink -> ink
        JengaButtonVariant.Ghost -> ghost
        JengaButtonVariant.Neutral -> neutral
        JengaButtonVariant.Outline -> outline
        JengaButtonVariant.Danger -> danger
        JengaButtonVariant.BrandOutline -> brandOutline
    }
}

/**
 * Default values and token mappings for [JengaButton]. Override any of these per call.
 *
 * [textStyle] and [colors] follow the theme's [JengaButtonStyle]; [shape], the plain functions and
 * the constants are Jenga's built-in values, and [JengaButton] resolves the theme's per-size values
 * over them.
 */
public object JengaButtonDefaults {

    /** Border width used by bordered variants. */
    public val BorderWidth: Dp = 1.dp

    /** Diameter of the inline loading spinner. */
    public val SpinnerSize: Dp = 18.dp

    /** The default button shape (control radius), before any per-size theme shape. */
    public val shape: Shape
        @Composable get() = JengaTheme.shapes.control

    /** Minimum touch height per [size] (Medium meets the 48dp accessibility target). */
    public fun minHeight(size: JengaButtonSize): Dp = when (size) {
        JengaButtonSize.Small -> 36.dp
        JengaButtonSize.Medium -> 48.dp
        JengaButtonSize.Large -> 56.dp
    }

    /** Content padding per [size]. */
    public fun contentPadding(size: JengaButtonSize): PaddingValues = when (size) {
        JengaButtonSize.Small -> PaddingValues(horizontal = 14.dp, vertical = 8.dp)
        JengaButtonSize.Medium -> PaddingValues(horizontal = 20.dp, vertical = 12.dp)
        JengaButtonSize.Large -> PaddingValues(horizontal = 24.dp, vertical = 14.dp)
    }

    /** Label text style per [size]. */
    @Composable
    public fun textStyle(size: JengaButtonSize): TextStyle {
        JengaTheme.components.button?.size(size)?.textStyle?.let { return it }
        val base = JengaTheme.typography.button
        return when (size) {
            JengaButtonSize.Small -> base.copy(fontSize = 14.sp, lineHeight = 18.sp)
            JengaButtonSize.Medium -> base
            JengaButtonSize.Large -> base.copy(fontSize = 17.sp, lineHeight = 22.sp)
        }
    }

    /** Themed colors for the given [variant]. */
    @Composable
    public fun colors(variant: JengaButtonVariant): JengaButtonColors {
        JengaTheme.components.button?.colors(variant)?.let { return it }
        val c = JengaTheme.colors
        return when (variant) {
            JengaButtonVariant.Primary -> JengaButtonColors(
                container = c.brand,
                content = c.onBrand,
                border = Color.Transparent,
                disabledContainer = c.surfaceDisabled,
                disabledContent = c.contentDisabled,
                disabledBorder = Color.Transparent,
            )
            JengaButtonVariant.Ink -> JengaButtonColors(
                container = c.ink,
                content = c.onInk,
                border = Color.Transparent,
                disabledContainer = c.surfaceDisabled,
                disabledContent = c.contentDisabled,
                disabledBorder = Color.Transparent,
            )
            JengaButtonVariant.Ghost -> JengaButtonColors(
                container = Color.Transparent,
                content = c.textSecondary,
                border = Color.Transparent,
                disabledContainer = Color.Transparent,
                disabledContent = c.contentDisabled,
                disabledBorder = Color.Transparent,
            )
            JengaButtonVariant.Neutral -> JengaButtonColors(
                container = c.surfaceVariant,
                content = c.textPrimary,
                border = Color.Transparent,
                disabledContainer = c.surfaceDisabled,
                disabledContent = c.contentDisabled,
                disabledBorder = Color.Transparent,
            )
            JengaButtonVariant.Outline -> JengaButtonColors(
                container = c.surface,
                content = c.textPrimary,
                border = c.borderStrong,
                disabledContainer = c.surfaceDisabled,
                disabledContent = c.contentDisabled,
                disabledBorder = c.borderDisabled,
            )
            JengaButtonVariant.Danger -> JengaButtonColors(
                container = Color.Transparent,
                content = c.error,
                border = c.borderStrong,
                disabledContainer = Color.Transparent,
                disabledContent = c.contentDisabled,
                disabledBorder = c.borderDisabled,
            )
            JengaButtonVariant.BrandOutline -> JengaButtonColors(
                container = c.surface,
                content = c.brand,
                border = c.brand,
                disabledContainer = c.surfaceDisabled,
                disabledContent = c.contentDisabled,
                disabledBorder = c.borderDisabled,
            )
        }
    }
}

/**
 * A Jenga button: the primary way to trigger an action.
 *
 * This is the convenience overload that renders a text label with the
 * size-appropriate style. For custom content (e.g. icon + multi-style text),
 * use the slot overload below.
 *
 * @sample io.github.joelkanyi.jenga.samples.JengaButtonSample
 *
 * @param text the button label.
 * @param onClick called when the button is tapped (ignored while disabled/loading).
 * @param modifier the [Modifier] for this button.
 * @param variant the visual style; see [JengaButtonVariant].
 * @param size the button size; see [JengaButtonSize].
 * @param enabled whether the button is interactive.
 * @param loading when true, shows a spinner and blocks clicks.
 * @param leadingIcon optional icon before the label.
 * @param trailingIcon optional icon after the label.
 * @param colors the color set; defaults to [JengaButtonDefaults.colors] for [variant].
 * @param loadingText optional label shown beside the spinner while [loading].
 * @param contentPadding inner padding; defaults to the theme's [JengaButtonSizeStyle.contentPadding]
 *   for [size], else [JengaButtonDefaults.contentPadding].
 * @param textStyle the label style; defaults to [JengaButtonDefaults.textStyle] for [size].
 */
@Composable
public fun JengaButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    variant: JengaButtonVariant = JengaButtonVariant.Primary,
    size: JengaButtonSize = JengaButtonSize.Medium,
    enabled: Boolean = true,
    loading: Boolean = false,
    leadingIcon: (@Composable () -> Unit)? = null,
    trailingIcon: (@Composable () -> Unit)? = null,
    colors: JengaButtonColors = JengaButtonDefaults.colors(variant),
    loadingText: String? = null,
    contentPadding: PaddingValues = JengaTheme.components.button?.size(size)?.contentPadding
        ?: JengaButtonDefaults.contentPadding(size),
    textStyle: TextStyle = JengaButtonDefaults.textStyle(size),
) {
    JengaButton(
        onClick = onClick,
        modifier = modifier,
        variant = variant,
        size = size,
        enabled = enabled && !loading,
        loading = loading && loadingText == null,
        leadingIcon = if (loading && loadingText != null) {
            { JengaButtonSpinner() }
        } else {
            leadingIcon
        },
        trailingIcon = trailingIcon.takeUnless { loading && loadingText != null },
        colors = colors,
        contentPadding = contentPadding,
    ) {
        JengaText(
            text = if (loading && loadingText != null) loadingText else text,
            style = textStyle,
            maxLines = 1,
        )
    }
}

/**
 * A Jenga button with a custom content slot.
 *
 * @param onClick called when the button is tapped (ignored while disabled/loading).
 * @param modifier the [Modifier] for this button.
 * @param variant the visual style; see [JengaButtonVariant].
 * @param size the button size; see [JengaButtonSize].
 * @param enabled whether the button is interactive.
 * @param loading when true, shows a spinner and blocks clicks.
 * @param leadingIcon optional icon before the content.
 * @param trailingIcon optional icon after the content.
 * @param shape the button shape; defaults to the theme's [JengaButtonSizeStyle.shape] for [size],
 *   else [JengaButtonDefaults.shape].
 * @param colors the color set; defaults to [JengaButtonDefaults.colors] for [variant].
 * @param contentPadding inner padding; defaults to the theme's [JengaButtonSizeStyle.contentPadding]
 *   for [size], else [JengaButtonDefaults.contentPadding].
 * @param content the button content; rendered with the resolved content color.
 */
@Composable
public fun JengaButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    variant: JengaButtonVariant = JengaButtonVariant.Primary,
    size: JengaButtonSize = JengaButtonSize.Medium,
    enabled: Boolean = true,
    loading: Boolean = false,
    leadingIcon: (@Composable () -> Unit)? = null,
    trailingIcon: (@Composable () -> Unit)? = null,
    shape: Shape = JengaTheme.components.button?.size(size)?.shape ?: JengaButtonDefaults.shape,
    colors: JengaButtonColors = JengaButtonDefaults.colors(variant),
    contentPadding: PaddingValues = JengaTheme.components.button?.size(size)?.contentPadding
        ?: JengaButtonDefaults.contentPadding(size),
    content: @Composable RowScope.() -> Unit,
) {
    val isEnabled = enabled && !loading
    val container = if (isEnabled) colors.container else colors.disabledContainer
    val contentColor = if (isEnabled) colors.content else colors.disabledContent
    val borderColor = if (isEnabled) colors.border else colors.disabledBorder
    val minHeight = JengaTheme.components.button?.size(size)?.minHeight ?: JengaButtonDefaults.minHeight(size)

    Row(
        modifier = modifier
            .minimumInteractiveComponentSize()
            .clip(shape)
            .background(container)
            .then(
                if (borderColor != Color.Transparent) {
                    Modifier.border(JengaButtonDefaults.BorderWidth, borderColor, shape)
                } else {
                    Modifier
                },
            )
            .clickable(
                enabled = isEnabled,
                role = Role.Button,
                onClick = onClick,
            )
            .defaultMinSize(minHeight = minHeight)
            .padding(contentPadding),
        horizontalArrangement = Arrangement.spacedBy(JengaTheme.spacing.sm, Alignment.CenterHorizontally),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        val rowScope = this
        CompositionLocalProvider(LocalJengaContentColor provides contentColor) {
            if (loading) {
                JengaButtonSpinner()
            } else {
                leadingIcon?.invoke()
                rowScope.content()
                trailingIcon?.invoke()
            }
        }
    }
}

@Composable
private fun JengaButtonSpinner() {
    CircularProgressIndicator(
        modifier = Modifier.size(JengaButtonDefaults.SpinnerSize),
        color = LocalJengaContentColor.current,
        strokeWidth = 2.dp,
    )
}

@Deprecated("Kept for binary compatibility", level = DeprecationLevel.HIDDEN)
@Composable
public fun JengaButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    variant: JengaButtonVariant = JengaButtonVariant.Primary,
    size: JengaButtonSize = JengaButtonSize.Medium,
    enabled: Boolean = true,
    loading: Boolean = false,
    leadingIcon: (@Composable () -> Unit)? = null,
    trailingIcon: (@Composable () -> Unit)? = null,
    colors: JengaButtonColors = JengaButtonDefaults.colors(variant),
    loadingText: String? = null,
) {
    JengaButton(
        text = text,
        onClick = onClick,
        modifier = modifier,
        variant = variant,
        size = size,
        enabled = enabled,
        loading = loading,
        leadingIcon = leadingIcon,
        trailingIcon = trailingIcon,
        colors = colors,
        loadingText = loadingText,
        textStyle = JengaButtonDefaults.textStyle(size),
    )
}

@Deprecated("Kept for binary compatibility", level = DeprecationLevel.HIDDEN)
@Composable
public fun JengaButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    variant: JengaButtonVariant = JengaButtonVariant.Primary,
    size: JengaButtonSize = JengaButtonSize.Medium,
    enabled: Boolean = true,
    loading: Boolean = false,
    leadingIcon: (@Composable () -> Unit)? = null,
    trailingIcon: (@Composable () -> Unit)? = null,
    colors: JengaButtonColors = JengaButtonDefaults.colors(variant),
) {
    JengaButton(
        text = text,
        onClick = onClick,
        modifier = modifier,
        variant = variant,
        size = size,
        enabled = enabled,
        loading = loading,
        leadingIcon = leadingIcon,
        trailingIcon = trailingIcon,
        colors = colors,
        loadingText = null,
    )
}
