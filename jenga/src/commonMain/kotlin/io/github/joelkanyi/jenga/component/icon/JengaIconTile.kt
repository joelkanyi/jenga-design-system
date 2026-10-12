package io.github.joelkanyi.jenga.component.icon

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import dev.drewhamilton.poko.Poko
import io.github.joelkanyi.jenga.component.badge.JengaBadgeTone
import io.github.joelkanyi.jenga.theme.JengaTheme

/** Size of a [JengaIconTile]. */
public enum class JengaIconTileSize { Small, Medium, Large }

/** Resolved colors for a [JengaIconTile]. Override via [JengaIconTileDefaults.colors]. */
@Poko
@Immutable
public class JengaIconTileColors(
    public val container: Color,
    public val content: Color,
) {
    public fun copy(
        container: Color = this.container,
        content: Color = this.content,
    ): JengaIconTileColors = JengaIconTileColors(container, content)
}

/** Defaults and token mappings for [JengaIconTile]. */
/**
 * Theme-wide metrics for one [JengaIconTileSize], part of a [JengaIconTileStyle]. A `null` value
 * keeps Jenga's default.
 *
 * @property tileSize the tile's edge length.
 * @property iconSize the icon's edge length.
 */
@Poko
@Immutable
public class JengaIconTileSizeStyle(
    public val tileSize: Dp? = null,
    public val iconSize: Dp? = null,
) {
    public fun copy(
        tileSize: Dp? = this.tileSize,
        iconSize: Dp? = this.iconSize,
    ): JengaIconTileSizeStyle = JengaIconTileSizeStyle(tileSize, iconSize)
}

/**
 * Theme-wide style for every [JengaIconTile], set through
 * [io.github.joelkanyi.jenga.theme.JengaComponentStyles.iconTile]. A `null` value keeps Jenga's
 * default; arguments passed to a call still win.
 *
 * @property shape the tile shape.
 * @property small metrics for [JengaIconTileSize.Small].
 * @property medium metrics for [JengaIconTileSize.Medium].
 * @property large metrics for [JengaIconTileSize.Large].
 */
@Poko
@Immutable
public class JengaIconTileStyle(
    public val shape: Shape? = null,
    public val small: JengaIconTileSizeStyle? = null,
    public val medium: JengaIconTileSizeStyle? = null,
    public val large: JengaIconTileSizeStyle? = null,
) {
    public fun copy(
        shape: Shape? = this.shape,
        small: JengaIconTileSizeStyle? = this.small,
        medium: JengaIconTileSizeStyle? = this.medium,
        large: JengaIconTileSizeStyle? = this.large,
    ): JengaIconTileStyle = JengaIconTileStyle(shape, small, medium, large)

    internal fun size(size: JengaIconTileSize): JengaIconTileSizeStyle? = when (size) {
        JengaIconTileSize.Small -> small
        JengaIconTileSize.Medium -> medium
        JengaIconTileSize.Large -> large
    }
}

/**
 * Defaults for [JengaIconTile]. [shape] follows the theme's [JengaIconTileStyle]; [tileSize] and
 * [iconSize] are Jenga's built-in values, and [JengaIconTile] resolves the theme's per-size values
 * over them.
 */
public object JengaIconTileDefaults {
    /** Themed colors per [tone]. */
    @Composable
    public fun colors(tone: JengaBadgeTone): JengaIconTileColors {
        val c = JengaTheme.colors
        return when (tone) {
            JengaBadgeTone.Neutral -> JengaIconTileColors(c.surfaceSunk, c.textMuted)
            JengaBadgeTone.Brand -> JengaIconTileColors(c.brandSubtle, c.onBrandSubtle)
            JengaBadgeTone.Success -> JengaIconTileColors(c.successContainer, c.onSuccessContainer)
            JengaBadgeTone.Warning -> JengaIconTileColors(c.warningContainer, c.onWarningContainer)
            JengaBadgeTone.Error -> JengaIconTileColors(c.errorContainer, c.onErrorContainer)
            JengaBadgeTone.Info -> JengaIconTileColors(c.infoContainer, c.onInfoContainer)
        }
    }

    /** Default tile shape. */
    public val shape: Shape
        @Composable get() = JengaTheme.components.iconTile?.shape ?: JengaTheme.shapes.control

    /** The tile's square edge length for a given [size]. */
    public fun tileSize(size: JengaIconTileSize): Dp = when (size) {
        JengaIconTileSize.Small -> 32.dp
        JengaIconTileSize.Medium -> 40.dp
        JengaIconTileSize.Large -> 48.dp
    }

    /** The icon's edge length inside a tile of the given [size]. */
    public fun iconSize(size: JengaIconTileSize): Dp = when (size) {
        JengaIconTileSize.Small -> 16.dp
        JengaIconTileSize.Medium -> 20.dp
        JengaIconTileSize.Large -> 24.dp
    }
}

/**
 * An icon on a rounded-square tile, tinted by [tone]. Use it as the leading
 * visual of a list row or card, where a bare [JengaIcon] reads too light.
 *
 * [JengaBadgeTone.Neutral] sits on a subtle surface; every other tone sits on
 * its soft container with the matching foreground.
 *
 * @sample io.github.joelkanyi.jenga.samples.JengaIconTileSample
 *
 * @param icon the icon to draw (e.g. from [JengaIcons]).
 * @param contentDescription accessibility label; `null` marks the tile decorative.
 * @param modifier the [Modifier] for this tile.
 * @param tone the semantic tone driving the tile and icon colors.
 * @param size the tile size; see [JengaIconTileSize].
 * @param colors the color set; defaults to [JengaIconTileDefaults.colors] for [tone].
 * @param shape the tile shape.
 * @param tileSize the tile's edge length; defaults to the theme's value for [size], else
 *   [JengaIconTileDefaults.tileSize].
 * @param iconSize the icon's edge length; defaults to the theme's value for [size], else
 *   [JengaIconTileDefaults.iconSize].
 */
@Composable
public fun JengaIconTile(
    icon: ImageVector,
    contentDescription: String?,
    modifier: Modifier = Modifier,
    tone: JengaBadgeTone = JengaBadgeTone.Neutral,
    size: JengaIconTileSize = JengaIconTileSize.Medium,
    colors: JengaIconTileColors = JengaIconTileDefaults.colors(tone),
    shape: Shape = JengaIconTileDefaults.shape,
    tileSize: Dp = JengaTheme.components.iconTile?.size(size)?.tileSize ?: JengaIconTileDefaults.tileSize(size),
    iconSize: Dp = JengaTheme.components.iconTile?.size(size)?.iconSize ?: JengaIconTileDefaults.iconSize(size),
) {
    Box(
        modifier = modifier
            .size(tileSize)
            .clip(shape)
            .background(colors.container),
        contentAlignment = Alignment.Center,
    ) {
        JengaIcon(
            imageVector = icon,
            contentDescription = contentDescription,
            tint = colors.content,
            size = iconSize,
        )
    }
}
