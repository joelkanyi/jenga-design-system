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
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
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
import kotlinx.coroutines.launch

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
        ViewerTopBar(onClose, title, closeContentDescription, titleStyle, colors, actions)
        ZoomableBox(maxScale = maxScale, modifier = Modifier.weight(1f).fillMaxWidth(), content = content)
    }
}

/**
 * A full-screen viewer for several images: swipe sideways between [pageCount]
 * pages, each pinch-zoomed on its own. Pages swipe only while the current page is
 * not zoomed in, and a page's zoom resets when it leaves the screen. Pressing back
 * or the close button calls [onDismissRequest].
 *
 * Control visibility by conditional composition.
 *
 * @sample io.github.joelkanyi.jenga.samples.JengaImageViewerPagesSample
 *
 * @param pageCount the number of pages.
 * @param onDismissRequest called when the viewer should close.
 * @param title the title for a page (e.g. "Photo 2 of 3").
 * @param closeContentDescription accessibility label for the close button.
 * @param modifier the [Modifier] for the viewer.
 * @param initialPage the page shown first.
 * @param actions trailing actions in the top row for the shown page.
 * @param bottomBar content under the pages (e.g. a strip of thumbnails), given the
 *   shown page and a function that moves to another page.
 * @param titleStyle the title text style.
 * @param maxScale the largest zoom factor; at least 1.
 * @param colors the color set; defaults to [JengaImageViewerDefaults.colors].
 * @param pageContent the image for a page.
 */
@Composable
public fun JengaImageViewer(
    pageCount: Int,
    onDismissRequest: () -> Unit,
    title: (page: Int) -> String,
    closeContentDescription: String,
    modifier: Modifier = Modifier,
    initialPage: Int = 0,
    actions: @Composable RowScope.(page: Int) -> Unit = {},
    bottomBar: @Composable (page: Int, goToPage: (Int) -> Unit) -> Unit = { _, _ -> },
    titleStyle: TextStyle = JengaImageViewerDefaults.titleStyle,
    maxScale: Float = JengaImageViewerDefaults.MaxScale,
    colors: JengaImageViewerColors = JengaImageViewerDefaults.colors(),
    pageContent: @Composable (page: Int) -> Unit,
) {
    require(maxScale >= 1f) { "maxScale must be at least 1" }
    val pagerState = rememberPagerState(initialPage = initialPage) { pageCount }
    val scope = rememberCoroutineScope()
    val page = pagerState.currentPage
    JengaFullWindowPopup(onDismissRequest = onDismissRequest) {
        Column(
            modifier = modifier
                .fillMaxSize()
                .background(colors.container)
                .windowInsetsPadding(WindowInsets.safeDrawing),
        ) {
            ViewerTopBar(onDismissRequest, title(page), closeContentDescription, titleStyle, colors) { actions(page) }
            HorizontalPager(state = pagerState, modifier = Modifier.weight(1f).fillMaxWidth()) { shown ->
                ZoomableBox(maxScale = maxScale, modifier = Modifier.fillMaxSize()) { pageContent(shown) }
            }
            bottomBar(page) { target -> scope.launch { pagerState.animateScrollToPage(target) } }
        }
    }
}

@Composable
private fun ViewerTopBar(
    onClose: () -> Unit,
    title: String,
    closeContentDescription: String,
    titleStyle: TextStyle,
    colors: JengaImageViewerColors,
    actions: @Composable RowScope.() -> Unit,
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
}

@Composable
private fun ZoomableBox(maxScale: Float, modifier: Modifier = Modifier, content: @Composable () -> Unit) {
    val zoom = remember(maxScale) { ZoomState(maxScale) }
    val transformState = rememberTransformableState { zoomChange, panChange, _ -> zoom.transform(zoomChange, panChange) }
    Box(
        modifier = modifier
            .onSizeChanged { zoom.size = it }
            .pointerInput(zoom) { detectTapGestures(onDoubleTap = { zoom.toggle() }) }
            .transformable(transformState, canPan = { zoom.scale > 1f })
            .graphicsLayer {
                scaleX = zoom.scale
                scaleY = zoom.scale
                translationX = zoom.offset.x
                translationY = zoom.offset.y
            },
        contentAlignment = Alignment.Center,
    ) {
        content()
    }
}

internal class ZoomState(private val maxScale: Float) {
    var scale by mutableFloatStateOf(1f)
        private set
    var offset by mutableStateOf(Offset.Zero)
        private set
    var size: IntSize = IntSize.Zero

    fun transform(zoomChange: Float, panChange: Offset) {
        scale = (scale * zoomChange).coerceIn(1f, maxScale)
        val maxX = size.width * (scale - 1f) / 2f
        val maxY = size.height * (scale - 1f) / 2f
        val moved = offset + panChange
        offset = Offset(moved.x.coerceIn(-maxX, maxX), moved.y.coerceIn(-maxY, maxY))
    }

    fun toggle() {
        scale = if (scale > 1f) 1f else JengaImageViewerDefaults.DoubleTapScale.coerceAtMost(maxScale)
        offset = Offset.Zero
    }
}
