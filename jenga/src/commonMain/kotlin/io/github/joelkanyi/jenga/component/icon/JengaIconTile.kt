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
 */
@Composable
public fun JengaIconTile(
    icon: ImageVector,
    contentDescription: String?,
    modifier: Modifier = Modifier,
    tone: JengaBadgeTone = JengaBadgeTone.Neutral,
    size: JengaIconTileSize = JengaIconTileSize.Medium,
    colors: JengaIconTileColors = JengaIconTileDefaults.colors(tone),
) {
    Box(
        modifier = modifier
            .size(JengaIconTileDefaults.tileSize(size))
            .clip(JengaTheme.shapes.control)
            .background(colors.container),
        contentAlignment = Alignment.Center,
    ) {
        JengaIcon(
            imageVector = icon,
            contentDescription = contentDescription,
            tint = colors.content,
            size = JengaIconTileDefaults.iconSize(size),
        )
    }
}
