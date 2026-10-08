package io.github.joelkanyi.jenga.component.feedback

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.MutableTransitionState
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.slideInHorizontally
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import io.github.joelkanyi.jenga.theme.JengaTheme

/** Defaults for [JengaSideSheet]. */
public object JengaSideSheetDefaults {
    /** Default sheet width. */
    public val Width: Dp = 360.dp

    /** Default shape: rounded on the start edge only. */
    public val shape: Shape
        @Composable get() = JengaTheme.shapes.xl.copy(topEnd = CornerSize(0.dp), bottomEnd = CornerSize(0.dp))

    /** Padding around the title, subtitle and header action. */
    public val headerPadding: PaddingValues
        @Composable get() = PaddingValues(
            start = JengaTheme.spacing.xl,
            top = JengaTheme.spacing.lg,
            end = JengaTheme.spacing.xl,
            bottom = JengaTheme.spacing.md,
        )
}

/**
 * A modal sheet that slides in from the end edge over a scrim, with the same
 * layout as [JengaListSheet]: a [title], a lazily laid out body, and an optional
 * [footer] pinned to the bottom. Use it in place of a bottom sheet on wide or
 * landscape layouts.
 *
 * Control visibility by conditional composition. Tapping the scrim or pressing
 * back calls [onDismissRequest].
 *
 * @sample io.github.joelkanyi.jenga.samples.JengaSideSheetSample
 *
 * @param onDismissRequest called when the sheet should close.
 * @param title the heading shown at the top of the sheet.
 * @param modifier the [Modifier] for the full-window container holding the scrim and sheet.
 * @param subtitle optional supporting line under the [title].
 * @param headerAction optional slot at the end of the title row (e.g. a close button).
 * @param footer optional row pinned to the bottom of the sheet, for actions.
 * @param width the sheet width.
 * @param shape the sheet shape.
 * @param titleStyle the [title] text style.
 * @param subtitleStyle the [subtitle] text style.
 * @param headerPadding padding around the header.
 * @param contentPadding padding around the lazy body.
 * @param footerPadding padding around the footer row.
 * @param footerSpacing gap between footer children.
 * @param content the sheet body, laid out lazily in a [LazyListScope].
 */
@Composable
public fun JengaSideSheet(
    onDismissRequest: () -> Unit,
    title: String,
    modifier: Modifier = Modifier,
    subtitle: String? = null,
    headerAction: (@Composable () -> Unit)? = null,
    footer: (@Composable RowScope.() -> Unit)? = null,
    width: Dp = JengaSideSheetDefaults.Width,
    shape: Shape = JengaSideSheetDefaults.shape,
    titleStyle: TextStyle = JengaListSheetDefaults.titleStyle,
    subtitleStyle: TextStyle = JengaListSheetDefaults.subtitleStyle,
    headerPadding: PaddingValues = JengaSideSheetDefaults.headerPadding,
    contentPadding: PaddingValues = JengaListSheetDefaults.contentPadding,
    footerPadding: PaddingValues = JengaListSheetDefaults.footerPadding,
    footerSpacing: Dp = JengaListSheetDefaults.footerSpacing,
    content: LazyListScope.() -> Unit,
) {
    JengaSideSheetImpl(
        onDismissRequest = onDismissRequest,
        title = title,
        modifier = modifier,
        subtitle = subtitle?.let(::AnnotatedString),
        headerAction = headerAction,
        footer = footer,
        width = width,
        shape = shape,
        titleStyle = titleStyle,
        subtitleStyle = subtitleStyle,
        headerPadding = headerPadding,
        contentPadding = contentPadding,
        footerPadding = footerPadding,
        footerSpacing = footerSpacing,
        content = content,
    )
}

/**
 * A [JengaSideSheet] whose subtitle is styled text (e.g. a monospaced id beside plain words).
 *
 * @param onDismissRequest called when the sheet should close.
 * @param title the heading shown at the top of the sheet.
 * @param subtitle the supporting line under the [title], with styled spans.
 * @param modifier the [Modifier] for the full-window container holding the scrim and sheet.
 * @param headerAction optional slot at the end of the title row (e.g. a close button).
 * @param footer optional row pinned to the bottom of the sheet, for actions.
 * @param width the sheet width.
 * @param shape the sheet shape.
 * @param titleStyle the [title] text style.
 * @param subtitleStyle the [subtitle] text style.
 * @param headerPadding padding around the header.
 * @param contentPadding padding around the lazy body.
 * @param footerPadding padding around the footer row.
 * @param footerSpacing gap between footer children.
 * @param content the sheet body, laid out lazily in a [LazyListScope].
 */
@Composable
public fun JengaSideSheet(
    onDismissRequest: () -> Unit,
    title: String,
    subtitle: AnnotatedString,
    modifier: Modifier = Modifier,
    headerAction: (@Composable () -> Unit)? = null,
    footer: (@Composable RowScope.() -> Unit)? = null,
    width: Dp = JengaSideSheetDefaults.Width,
    shape: Shape = JengaSideSheetDefaults.shape,
    titleStyle: TextStyle = JengaListSheetDefaults.titleStyle,
    subtitleStyle: TextStyle = JengaListSheetDefaults.subtitleStyle,
    headerPadding: PaddingValues = JengaSideSheetDefaults.headerPadding,
    contentPadding: PaddingValues = JengaListSheetDefaults.contentPadding,
    footerPadding: PaddingValues = JengaListSheetDefaults.footerPadding,
    footerSpacing: Dp = JengaListSheetDefaults.footerSpacing,
    content: LazyListScope.() -> Unit,
) {
    JengaSideSheetImpl(
        onDismissRequest = onDismissRequest,
        title = title,
        modifier = modifier,
        subtitle = subtitle,
        headerAction = headerAction,
        footer = footer,
        width = width,
        shape = shape,
        titleStyle = titleStyle,
        subtitleStyle = subtitleStyle,
        headerPadding = headerPadding,
        contentPadding = contentPadding,
        footerPadding = footerPadding,
        footerSpacing = footerSpacing,
        content = content,
    )
}

@Composable
private fun JengaSideSheetImpl(
    onDismissRequest: () -> Unit,
    title: String,
    subtitle: AnnotatedString?,
    modifier: Modifier = Modifier,
    headerAction: (@Composable () -> Unit)? = null,
    footer: (@Composable RowScope.() -> Unit)? = null,
    width: Dp = JengaSideSheetDefaults.Width,
    shape: Shape = JengaSideSheetDefaults.shape,
    titleStyle: TextStyle = JengaListSheetDefaults.titleStyle,
    subtitleStyle: TextStyle = JengaListSheetDefaults.subtitleStyle,
    headerPadding: PaddingValues = JengaSideSheetDefaults.headerPadding,
    contentPadding: PaddingValues = JengaListSheetDefaults.contentPadding,
    footerPadding: PaddingValues = JengaListSheetDefaults.footerPadding,
    footerSpacing: Dp = JengaListSheetDefaults.footerSpacing,
    content: LazyListScope.() -> Unit,
) {
    val visible = remember { MutableTransitionState(false).apply { targetState = true } }
    val motion = JengaTheme.motion
    val isRtl = LocalLayoutDirection.current == LayoutDirection.Rtl
    JengaFullWindowPopup(onDismissRequest = onDismissRequest) {
        Box(modifier = modifier.fillMaxSize()) {
            AnimatedVisibility(
                visibleState = visible,
                enter = fadeIn(tween(motion.durationMedium)),
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(JengaTheme.colors.scrim)
                        .clickable(
                            interactionSource = remember { MutableInteractionSource() },
                            indication = null,
                            onClick = onDismissRequest,
                        ),
                )
            }
            AnimatedVisibility(
                visibleState = visible,
                modifier = Modifier.align(Alignment.CenterEnd),
                enter = slideInHorizontally(tween(motion.durationSlowest, easing = motion.emphasized)) { if (isRtl) -it else it },
            ) {
                JengaListSheetLayout(
                    title = title,
                    subtitle = subtitle,
                    footer = footer,
                    modifier = Modifier
                        .width(width)
                        .fillMaxHeight()
                        .clip(shape)
                        .background(JengaTheme.colors.surface)
                        .windowInsetsPadding(WindowInsets.safeDrawing),
                    headerAction = headerAction,
                    titleStyle = titleStyle,
                    subtitleStyle = subtitleStyle,
                    headerPadding = headerPadding,
                    contentPadding = contentPadding,
                    footerPadding = footerPadding,
                    footerSpacing = footerSpacing,
                    fillHeight = true,
                    content = content,
                )
            }
        }
    }
}
