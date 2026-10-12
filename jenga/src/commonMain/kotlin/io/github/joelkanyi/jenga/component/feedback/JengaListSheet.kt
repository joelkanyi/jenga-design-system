package io.github.joelkanyi.jenga.component.feedback

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.Dp
import dev.drewhamilton.poko.Poko
import io.github.joelkanyi.jenga.component.divider.JengaDivider
import io.github.joelkanyi.jenga.component.text.JengaText
import io.github.joelkanyi.jenga.theme.JengaTheme

/** Defaults for [JengaListSheet]. */
/**
 * Theme-wide style for every [JengaListSheet] and [JengaSideSheet], set through
 * [io.github.joelkanyi.jenga.theme.JengaComponentStyles.listSheet]. A `null` value keeps Jenga's
 * default; arguments passed to a call still win.
 *
 * @property titleStyle the title text style.
 * @property subtitleStyle the subtitle text style.
 * @property subtitleColor the subtitle color.
 * @property headerPadding padding around the title, subtitle and header action.
 * @property contentPadding padding around the lazy body.
 * @property footerPadding padding around the footer row.
 * @property footerSpacing the gap between footer children.
 */
@Poko
@Immutable
public class JengaListSheetStyle(
    public val titleStyle: TextStyle? = null,
    public val subtitleStyle: TextStyle? = null,
    public val subtitleColor: Color? = null,
    public val headerPadding: PaddingValues? = null,
    public val contentPadding: PaddingValues? = null,
    public val footerPadding: PaddingValues? = null,
    public val footerSpacing: Dp? = null,
) {
    public fun copy(
        titleStyle: TextStyle? = this.titleStyle,
        subtitleStyle: TextStyle? = this.subtitleStyle,
        subtitleColor: Color? = this.subtitleColor,
        headerPadding: PaddingValues? = this.headerPadding,
        contentPadding: PaddingValues? = this.contentPadding,
        footerPadding: PaddingValues? = this.footerPadding,
        footerSpacing: Dp? = this.footerSpacing,
    ): JengaListSheetStyle = JengaListSheetStyle(
        titleStyle,
        subtitleStyle,
        subtitleColor,
        headerPadding,
        contentPadding,
        footerPadding,
        footerSpacing,
    )
}

/** Defaults for [JengaListSheet] and [JengaSideSheet]. Each one follows the theme's [JengaListSheetStyle]. */
public object JengaListSheetDefaults {
    /** Title text style. */
    public val titleStyle: TextStyle
        @Composable get() = JengaTheme.components.listSheet?.titleStyle ?: JengaTheme.typography.titleLarge

    /** Subtitle text style. */
    public val subtitleStyle: TextStyle
        @Composable get() = JengaTheme.components.listSheet?.subtitleStyle ?: JengaTheme.typography.bodySmall

    /** Subtitle color. */
    public val subtitleColor: Color
        @Composable get() = JengaTheme.components.listSheet?.subtitleColor ?: JengaTheme.colors.textMuted

    /** Padding around the title, subtitle and header action. */
    public val headerPadding: PaddingValues
        @Composable get() = JengaTheme.components.listSheet?.headerPadding ?: PaddingValues(
            start = JengaTheme.spacing.xl,
            end = JengaTheme.spacing.xl,
            bottom = JengaTheme.spacing.md,
        )

    /** Padding around the lazy body. */
    public val contentPadding: PaddingValues
        @Composable get() = JengaTheme.components.listSheet?.contentPadding ?: PaddingValues()

    /** Padding around the footer row. */
    public val footerPadding: PaddingValues
        @Composable get() = JengaTheme.components.listSheet?.footerPadding
            ?: PaddingValues(horizontal = JengaTheme.spacing.xl, vertical = JengaTheme.spacing.lg)

    /** Gap between footer children. */
    public val footerSpacing: Dp
        @Composable get() = JengaTheme.components.listSheet?.footerSpacing ?: JengaTheme.spacing.sm
}

/**
 * A modal bottom sheet with a [title], a lazily laid out body, and an optional
 * [footer] pinned to the bottom, above the keyboard. Use it for pickers and
 * short forms; for free-form content use [JengaBottomSheet].
 *
 * Control visibility by conditional composition, as with [JengaBottomSheet].
 *
 * @sample io.github.joelkanyi.jenga.samples.JengaListSheetSample
 *
 * @param onDismissRequest called when the sheet is dismissed (drag down or scrim).
 * @param title the heading shown at the top of the sheet.
 * @param modifier the [Modifier] for the sheet.
 * @param sheetState the sheet state; pass one from [rememberJengaSheetState] to
 *   show or hide the sheet imperatively.
 * @param subtitle optional supporting line under the [title].
 * @param headerAction optional slot at the end of the title row (e.g. a close button).
 * @param footer optional row pinned to the bottom of the sheet, for actions.
 * @param titleStyle the [title] text style.
 * @param subtitleStyle the [subtitle] text style.
 * @param subtitleColor the [subtitle] color.
 * @param headerPadding padding around the header.
 * @param contentPadding padding around the lazy body.
 * @param footerPadding padding around the footer row.
 * @param footerSpacing gap between footer children.
 * @param content the sheet body, laid out lazily in a [LazyListScope].
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
public fun JengaListSheet(
    onDismissRequest: () -> Unit,
    title: String,
    modifier: Modifier = Modifier,
    sheetState: JengaSheetState = rememberJengaSheetState(),
    subtitle: String? = null,
    headerAction: (@Composable () -> Unit)? = null,
    footer: (@Composable RowScope.() -> Unit)? = null,
    titleStyle: TextStyle = JengaListSheetDefaults.titleStyle,
    subtitleStyle: TextStyle = JengaListSheetDefaults.subtitleStyle,
    subtitleColor: Color = JengaListSheetDefaults.subtitleColor,
    headerPadding: PaddingValues = JengaListSheetDefaults.headerPadding,
    contentPadding: PaddingValues = JengaListSheetDefaults.contentPadding,
    footerPadding: PaddingValues = JengaListSheetDefaults.footerPadding,
    footerSpacing: Dp = JengaListSheetDefaults.footerSpacing,
    content: LazyListScope.() -> Unit,
) {
    JengaListSheetImpl(
        onDismissRequest = onDismissRequest,
        title = title,
        modifier = modifier,
        sheetState = sheetState,
        subtitle = subtitle?.let(::AnnotatedString),
        headerAction = headerAction,
        footer = footer,
        titleStyle = titleStyle,
        subtitleStyle = subtitleStyle,
        subtitleColor = subtitleColor,
        headerPadding = headerPadding,
        contentPadding = contentPadding,
        footerPadding = footerPadding,
        footerSpacing = footerSpacing,
        content = content,
    )
}

/**
 * A [JengaListSheet] whose subtitle is styled text (e.g. a monospaced id beside plain words).
 *
 * @param onDismissRequest called when the sheet is dismissed (drag down or scrim).
 * @param title the heading shown at the top of the sheet.
 * @param subtitle the supporting line under the [title], with styled spans.
 * @param modifier the [Modifier] for the sheet.
 * @param sheetState the sheet state; pass one from [rememberJengaSheetState] to
 *   show or hide the sheet imperatively.
 * @param headerAction optional slot at the end of the title row (e.g. a close button).
 * @param footer optional row pinned to the bottom of the sheet, for actions.
 * @param titleStyle the [title] text style.
 * @param subtitleStyle the [subtitle] text style.
 * @param subtitleColor the [subtitle] color.
 * @param headerPadding padding around the header.
 * @param contentPadding padding around the lazy body.
 * @param footerPadding padding around the footer row.
 * @param footerSpacing gap between footer children.
 * @param content the sheet body, laid out lazily in a [LazyListScope].
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
public fun JengaListSheet(
    onDismissRequest: () -> Unit,
    title: String,
    subtitle: AnnotatedString,
    modifier: Modifier = Modifier,
    sheetState: JengaSheetState = rememberJengaSheetState(),
    headerAction: (@Composable () -> Unit)? = null,
    footer: (@Composable RowScope.() -> Unit)? = null,
    titleStyle: TextStyle = JengaListSheetDefaults.titleStyle,
    subtitleStyle: TextStyle = JengaListSheetDefaults.subtitleStyle,
    subtitleColor: Color = JengaListSheetDefaults.subtitleColor,
    headerPadding: PaddingValues = JengaListSheetDefaults.headerPadding,
    contentPadding: PaddingValues = JengaListSheetDefaults.contentPadding,
    footerPadding: PaddingValues = JengaListSheetDefaults.footerPadding,
    footerSpacing: Dp = JengaListSheetDefaults.footerSpacing,
    content: LazyListScope.() -> Unit,
) {
    JengaListSheetImpl(
        onDismissRequest = onDismissRequest,
        title = title,
        modifier = modifier,
        sheetState = sheetState,
        subtitle = subtitle,
        headerAction = headerAction,
        footer = footer,
        titleStyle = titleStyle,
        subtitleStyle = subtitleStyle,
        subtitleColor = subtitleColor,
        headerPadding = headerPadding,
        contentPadding = contentPadding,
        footerPadding = footerPadding,
        footerSpacing = footerSpacing,
        content = content,
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun JengaListSheetImpl(
    onDismissRequest: () -> Unit,
    title: String,
    subtitle: AnnotatedString?,
    modifier: Modifier = Modifier,
    sheetState: JengaSheetState = rememberJengaSheetState(),
    headerAction: (@Composable () -> Unit)? = null,
    footer: (@Composable RowScope.() -> Unit)? = null,
    titleStyle: TextStyle = JengaListSheetDefaults.titleStyle,
    subtitleStyle: TextStyle = JengaListSheetDefaults.subtitleStyle,
    subtitleColor: Color = JengaListSheetDefaults.subtitleColor,
    headerPadding: PaddingValues = JengaListSheetDefaults.headerPadding,
    contentPadding: PaddingValues = JengaListSheetDefaults.contentPadding,
    footerPadding: PaddingValues = JengaListSheetDefaults.footerPadding,
    footerSpacing: Dp = JengaListSheetDefaults.footerSpacing,
    content: LazyListScope.() -> Unit,
) {
    ModalBottomSheet(
        onDismissRequest = onDismissRequest,
        modifier = modifier,
        sheetState = sheetState.m3State,
        containerColor = JengaTheme.colors.surface,
        contentColor = JengaTheme.colors.textPrimary,
        scrimColor = JengaTheme.colors.scrim,
        dragHandle = { JengaDragHandle() },
    ) {
        JengaListSheetLayout(
            title = title,
            subtitle = subtitle,
            headerAction = headerAction,
            footer = footer,
            titleStyle = titleStyle,
            subtitleStyle = subtitleStyle,
            subtitleColor = subtitleColor,
            headerPadding = headerPadding,
            contentPadding = contentPadding,
            footerPadding = footerPadding,
            footerSpacing = footerSpacing,
            content = content,
        )
    }
}

@Composable
internal fun JengaListSheetLayout(
    title: String,
    subtitle: AnnotatedString?,
    footer: (@Composable RowScope.() -> Unit)?,
    modifier: Modifier = Modifier,
    headerAction: (@Composable () -> Unit)? = null,
    titleStyle: TextStyle = JengaListSheetDefaults.titleStyle,
    subtitleStyle: TextStyle = JengaListSheetDefaults.subtitleStyle,
    subtitleColor: Color = JengaListSheetDefaults.subtitleColor,
    headerPadding: PaddingValues = JengaListSheetDefaults.headerPadding,
    contentPadding: PaddingValues = JengaListSheetDefaults.contentPadding,
    footerPadding: PaddingValues = JengaListSheetDefaults.footerPadding,
    footerSpacing: Dp = JengaListSheetDefaults.footerSpacing,
    fillHeight: Boolean = false,
    content: LazyListScope.() -> Unit,
) {
    Column(modifier = modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(headerPadding),
            verticalAlignment = Alignment.Top,
        ) {
            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(JengaTheme.spacing.xxs),
            ) {
                JengaText(text = title, style = titleStyle)
                if (subtitle != null) {
                    JengaText(text = subtitle, style = subtitleStyle, color = subtitleColor)
                }
            }
            if (headerAction != null) {
                headerAction()
            }
        }
        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f, fill = fillHeight),
            contentPadding = contentPadding,
            content = content,
        )
        if (footer != null) {
            JengaDivider()
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(footerPadding),
                horizontalArrangement = Arrangement.spacedBy(footerSpacing),
                verticalAlignment = Alignment.CenterVertically,
                content = footer,
            )
        }
    }
}
