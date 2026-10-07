package io.github.joelkanyi.jenga.component.media

import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.gestures.rememberTransformableState
import androidx.compose.foundation.gestures.transformable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.IntSize
import dev.drewhamilton.poko.Poko
import io.github.joelkanyi.jenga.component.button.JengaIconButton
import io.github.joelkanyi.jenga.component.button.JengaIconButtonVariant
import io.github.joelkanyi.jenga.component.feedback.JengaFullWindowPopup
import io.github.joelkanyi.jenga.component.icon.JengaIcon
import io.github.joelkanyi.jenga.component.text.JengaText
import io.github.joelkanyi.jenga.theme.JengaTheme
import io.github.joelkanyi.jenga.theme.LocalJengaContentColor

/** Resolved colors for a [JengaImageViewer]. Override via [JengaImageViewerDefaults.colors]. */
@Poko
@Immutable
public class JengaImageViewerColors(
    public val container: Color,
    public val content: Color,
) {
    public fun copy(
        container: Color = this.container,
        content: Color = this.content,
    ): JengaImageViewerColors = JengaImageViewerColors(container, content)
}

/** Defaults and token mappings for [JengaImageViewer]. */
public object JengaImageViewerDefaults {
    /** Largest zoom factor. */
    public const val MaxScale: Float = 4f

    /** Zoom factor applied by a double tap. */
    public const val DoubleTapScale: Float = 2.5f

    /** Title text style. */
    public val titleStyle: TextStyle
        @Composable get() = JengaTheme.typography.titleSmall

    /** Themed colors: an always-dark backdrop in both light and dark themes. */
    @Composable
    public fun colors(): JengaImageViewerColors = JengaImageViewerColors(
        container = JengaTheme.colors.overlaySurface.copy(alpha = 1f),
        content = JengaTheme.colors.onOverlay,
    )
}

/**
 * A full-screen image viewer on a dark backdrop: a top row with a close button,
 * a [title] and trailing [actions] (e.g. download, share), above the image in
 * [content]. The image can be pinch-zoomed and panned, and a double tap toggles
 * zoom. Pressing back or the close button calls [onDismissRequest].
 *
 * Control visibility by conditional composition.
 *
 * @sample io.github.joelkanyi.jenga.samples.JengaImageViewerSample
 *
 * @param onDismissRequest called when the viewer should close.
 * @param title the title shown in the top row (e.g. the file name).
 * @param closeContentDescription accessibility label for the close button.
 * @param modifier the [Modifier] for the viewer.
 * @param actions trailing actions in the top row; inherit the content color.
 * @param titleStyle the [title] text style.
 * @param maxScale the largest zoom factor; at least 1.
 * @param colors the color set; defaults to [JengaImageViewerDefaults.colors].
 * @param content the image (e.g. a `JengaImage` that fills the space).
 */
@Composable
public fun JengaImageViewer(
    onDismissRequest: () -> Unit,
    title: String,
    closeContentDescription: String,
    modifier: Modifier = Modifier,
    actions: @Composable RowScope.() -> Unit = {},
    titleStyle: TextStyle = JengaImageViewerDefaults.titleStyle,
    maxScale: Float = JengaImageViewerDefaults.MaxScale,
    colors: JengaImageViewerColors = JengaImageViewerDefaults.colors(),
    content: @Composable () -> Unit,
) {
    require(maxScale >= 1f) { "maxScale must be at least 1" }
    JengaFullWindowPopup(onDismissRequest = onDismissRequest) {
        JengaImageViewerContent(
            onClose = onDismissRequest,
            title = title,
            closeContentDescription = closeContentDescription,
            modifier = modifier,
            actions = actions,
            titleStyle = titleStyle,
            maxScale = maxScale,
            colors = colors,
            content = content,
        )
    }
}

@Composable
internal fun JengaImageViewerContent(
    onClose: () -> Unit,
    title: String,
    closeContentDescription: String,
    modifier: Modifier = Modifier,
    actions: @Composable RowScope.() -> Unit = {},
    titleStyle: TextStyle = JengaImageViewerDefaults.titleStyle,
    maxScale: Float = JengaImageViewerDefaults.MaxScale,
    colors: JengaImageViewerColors = JengaImageViewerDefaults.colors(),
    content: @Composable () -> Unit,
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(colors.container)
            .windowInsetsPadding(WindowInsets.safeDrawing),
    ) {
        CompositionLocalProvider(LocalJengaContentColor provides colors.content) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = JengaTheme.spacing.xs),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(JengaTheme.spacing.xs),
            ) {
                JengaIconButton(onClick = onClose, variant = JengaIconButtonVariant.Overlay) {
                    JengaIcon(JengaTheme.icons.close, contentDescription = closeContentDescription)
                }
                JengaText(
                    text = title,
                    style = titleStyle,
                    color = colors.content,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.weight(1f),
                )
                actions()
            }
        }
        ZoomableBox(maxScale = maxScale, modifier = Modifier.weight(1f).fillMaxWidth(), content = content)
    }
}

@Composable
private fun ZoomableBox(maxScale: Float, modifier: Modifier = Modifier, content: @Composable () -> Unit) {
    var scale by remember { mutableFloatStateOf(1f) }
    var offset by remember { mutableStateOf(Offset.Zero) }
    var size by remember { mutableStateOf(IntSize.Zero) }
    fun clamp(raw: Offset, atScale: Float): Offset {
        val maxX = size.width * (atScale - 1f) / 2f
        val maxY = size.height * (atScale - 1f) / 2f
        return Offset(raw.x.coerceIn(-maxX, maxX), raw.y.coerceIn(-maxY, maxY))
    }
    val transformState = rememberTransformableState { zoomChange, panChange, _ ->
        scale = (scale * zoomChange).coerceIn(1f, maxScale)
        offset = clamp(offset + panChange, scale)
    }
    Box(
        modifier = modifier
            .onSizeChanged { size = it }
            .pointerInput(maxScale) {
                detectTapGestures(
                    onDoubleTap = {
                        scale = if (scale > 1f) 1f else JengaImageViewerDefaults.DoubleTapScale.coerceAtMost(maxScale)
                        offset = Offset.Zero
                    },
                )
            }
            .transformable(transformState)
            .graphicsLayer {
                scaleX = scale
                scaleY = scale
                translationX = offset.x
                translationY = offset.y
            },
        contentAlignment = Alignment.Center,
    ) {
        content()
    }
}
