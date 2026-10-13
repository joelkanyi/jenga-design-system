package io.github.joelkanyi.jenga.component.fab

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.minimumInteractiveComponentSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.Immutable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.DefaultShadowColor
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import dev.drewhamilton.poko.Poko
import io.github.joelkanyi.jenga.component.text.JengaText
import io.github.joelkanyi.jenga.theme.JengaTheme
import io.github.joelkanyi.jenga.theme.LocalJengaContentColor

/** Resolved colors for a [JengaFab]. Override via [JengaFabDefaults.colors]. */
@Poko
@Immutable
public class JengaFabColors(
    public val container: Color,
    public val content: Color,
) {
    public fun copy(
        container: Color = this.container,
        content: Color = this.content,
    ): JengaFabColors = JengaFabColors(container, content)
}

/**
 * Theme-wide style for every [JengaFab] and [JengaExtendedFab], set through
 * [io.github.joelkanyi.jenga.theme.JengaComponentStyles.fab]. A `null` value keeps Jenga's default;
 * arguments passed to a call still win.
 *
 * @property shape the [JengaFab] shape.
 * @property size the [JengaFab] edge length.
 * @property extendedShape the [JengaExtendedFab] shape.
 * @property extendedHeight the [JengaExtendedFab] height.
 * @property extendedContentPadding padding inside a [JengaExtendedFab].
 * @property elevation the shadow elevation.
 * @property shadowColor the shadow color.
 * @property textStyle the [JengaExtendedFab] label text style.
 * @property colors the color set.
 */
@Poko
@Immutable
public class JengaFabStyle(
    public val shape: Shape? = null,
    public val size: Dp? = null,
    public val extendedShape: Shape? = null,
    public val extendedHeight: Dp? = null,
    public val extendedContentPadding: PaddingValues? = null,
    public val elevation: Dp? = null,
    public val shadowColor: Color? = null,
    public val textStyle: TextStyle? = null,
    public val colors: JengaFabColors? = null,
) {
    public fun copy(
        shape: Shape? = this.shape,
        size: Dp? = this.size,
        extendedShape: Shape? = this.extendedShape,
        extendedHeight: Dp? = this.extendedHeight,
        extendedContentPadding: PaddingValues? = this.extendedContentPadding,
        elevation: Dp? = this.elevation,
        shadowColor: Color? = this.shadowColor,
        textStyle: TextStyle? = this.textStyle,
        colors: JengaFabColors? = this.colors,
    ): JengaFabStyle = JengaFabStyle(
        shape,
        size,
        extendedShape,
        extendedHeight,
        extendedContentPadding,
        elevation,
        shadowColor,
        textStyle,
        colors,
    )
}

/**
 * Defaults and token mappings for [JengaFab]. The composable members follow the theme's
 * [JengaFabStyle]; the constants are Jenga's built-in values, and [JengaFab] and [JengaExtendedFab]
 * resolve the theme's values over them.
 */
public object JengaFabDefaults {
    public val Size: Dp = 56.dp

    /** Height of an extended (labelled) FAB. */
    public val ExtendedHeight: Dp = 52.dp

    /** Default FAB shape. */
    public val shape: Shape
        @Composable get() = JengaTheme.components.fab?.shape ?: JengaTheme.shapes.lg

    /** Extended-FAB shape: a full pill, so the label reads as one confident tap target. */
    public val extendedShape: Shape = RoundedCornerShape(percent = 50)

    /** Resting elevation. */
    public val elevation: Dp
        @Composable get() = JengaTheme.components.fab?.elevation ?: JengaTheme.elevation.lg

    /** Shadow color; tint it (e.g. with the brand color) for a colored glow. */
    public val ShadowColor: Color = DefaultShadowColor

    /** Padding inside an extended FAB. */
    public val extendedContentPadding: PaddingValues
        @Composable get() = JengaTheme.components.fab?.extendedContentPadding ?: PaddingValues(horizontal = JengaTheme.spacing.lg)

    /** Extended FAB label text style. */
    public val textStyle: TextStyle
        @Composable get() = JengaTheme.components.fab?.textStyle ?: JengaTheme.typography.titleSmall

    /** Themed colors. */
    @Composable
    public fun colors(): JengaFabColors = JengaTheme.components.fab?.colors
        ?: JengaFabColors(container = JengaTheme.colors.brand, content = JengaTheme.colors.onBrand)
}

/**
 * A floating action button: a prominent, elevated action with an icon.
 *
 * @sample io.github.joelkanyi.jenga.samples.JengaFabSample
 *
 * @param onClick called when tapped.
 * @param modifier the [Modifier] for this FAB.
 * @param shape the FAB shape; defaults to [JengaFabDefaults.shape].
 * @param colors the color set; defaults to [JengaFabDefaults.colors].
 * @param elevation the shadow elevation.
 * @param shadowColor the shadow color; defaults to the theme's value, else [JengaFabDefaults.ShadowColor].
 * @param size the FAB edge length; defaults to the theme's value, else [JengaFabDefaults.Size].
 * @param content the FAB content (typically a single icon); inherits content color.
 */
@Composable
public fun JengaFab(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    shape: Shape = JengaFabDefaults.shape,
    colors: JengaFabColors = JengaFabDefaults.colors(),
    elevation: Dp = JengaFabDefaults.elevation,
    shadowColor: Color = JengaTheme.components.fab?.shadowColor ?: JengaFabDefaults.ShadowColor,
    size: Dp = JengaTheme.components.fab?.size ?: JengaFabDefaults.Size,
    content: @Composable () -> Unit,
) {
    Box(
        modifier = modifier
            .minimumInteractiveComponentSize()
            .shadow(elevation, shape, ambientColor = shadowColor, spotColor = shadowColor)
            .clip(shape)
            .background(colors.container)
            .clickable(role = Role.Button, onClick = onClick)
            .size(size),
        contentAlignment = Alignment.Center,
    ) {
        CompositionLocalProvider(LocalJengaContentColor provides colors.content) {
            content()
        }
    }
}

/**
 * An extended floating action button: a prominent, elevated pill carrying an
 * optional leading [icon] and a text [label]. Use it when the primary action
 * needs a word, not just a glyph. Same elevation language as [JengaFab]; the
 * [colors] default to brand and can be overridden with any pair (e.g. a neutral
 * inverse) so it reads correctly on any surface.
 *
 * @param label the action text.
 * @param onClick called when tapped.
 * @param modifier the [Modifier] for this FAB.
 * @param icon an optional leading icon; inherits the content color.
 * @param shape the FAB shape; defaults to the theme's extended shape, else a full pill
 *   ([JengaFabDefaults.extendedShape]).
 * @param colors the color set; defaults to [JengaFabDefaults.colors].
 * @param elevation the shadow elevation.
 * @param shadowColor the shadow color; defaults to the theme's value, else [JengaFabDefaults.ShadowColor].
 * @param height the FAB height; defaults to the theme's value, else [JengaFabDefaults.ExtendedHeight].
 * @param contentPadding padding inside the FAB.
 * @param textStyle the label text style.
 */
@Composable
public fun JengaExtendedFab(
    label: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    icon: (@Composable () -> Unit)? = null,
    shape: Shape = JengaTheme.components.fab?.extendedShape ?: JengaFabDefaults.extendedShape,
    colors: JengaFabColors = JengaFabDefaults.colors(),
    elevation: Dp = JengaFabDefaults.elevation,
    shadowColor: Color = JengaTheme.components.fab?.shadowColor ?: JengaFabDefaults.ShadowColor,
    height: Dp = JengaTheme.components.fab?.extendedHeight ?: JengaFabDefaults.ExtendedHeight,
    contentPadding: PaddingValues = JengaFabDefaults.extendedContentPadding,
    textStyle: TextStyle = JengaFabDefaults.textStyle,
) {
    Row(
        modifier = modifier
            .minimumInteractiveComponentSize()
            .shadow(elevation, shape, ambientColor = shadowColor, spotColor = shadowColor)
            .clip(shape)
            .background(colors.container)
            .clickable(role = Role.Button, onClick = onClick)
            .height(height)
            .padding(contentPadding),
        horizontalArrangement = Arrangement.spacedBy(JengaTheme.spacing.sm),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        CompositionLocalProvider(LocalJengaContentColor provides colors.content) {
            if (icon != null) icon()
            JengaText(text = label, style = textStyle, color = colors.content)
        }
    }
}
