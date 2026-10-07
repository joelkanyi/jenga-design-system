package io.github.joelkanyi.jenga.component.icon

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import io.github.joelkanyi.jenga.component.badge.JengaBadgeDefaults
import io.github.joelkanyi.jenga.component.badge.JengaBadgeTone
import io.github.joelkanyi.jenga.theme.JengaTheme

/** Size of a [JengaIconTile]. */
public enum class JengaIconTileSize { Small, Medium, Large }

/** Defaults for [JengaIconTile]. */
public object JengaIconTileDefaults {
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
 * Colors follow the badge tones: [JengaBadgeTone.Neutral] sits on a subtle
 * surface, every other tone on its soft container with the matching foreground.
 *
 * @sample io.github.joelkanyi.jenga.samples.JengaIconTileSample
 *
 * @param icon the icon to draw (e.g. from [JengaIcons]).
 * @param contentDescription accessibility label; `null` marks the tile decorative.
 * @param modifier the [Modifier] for this tile.
 * @param tone the semantic tone driving the tile and icon colors.
 * @param size the tile size; see [JengaIconTileSize].
 */
@Composable
public fun JengaIconTile(
    icon: ImageVector,
    contentDescription: String?,
    modifier: Modifier = Modifier,
    tone: JengaBadgeTone = JengaBadgeTone.Neutral,
    size: JengaIconTileSize = JengaIconTileSize.Medium,
) {
    val colors = JengaBadgeDefaults.colors(tone)
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
