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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.Dp
import io.github.joelkanyi.jenga.component.divider.JengaDivider
import io.github.joelkanyi.jenga.component.text.JengaText
import io.github.joelkanyi.jenga.theme.JengaTheme

/** Defaults for [JengaListSheet]. */
public object JengaListSheetDefaults {
    /** Title text style. */
    public val titleStyle: TextStyle
        @Composable get() = JengaTheme.typography.titleLarge

    /** Subtitle text style. */
    public val subtitleStyle: TextStyle
        @Composable get() = JengaTheme.typography.bodySmall

    /** Padding around the title, subtitle and header action. */
    public val headerPadding: PaddingValues
        @Composable get() = PaddingValues(
            start = JengaTheme.spacing.xl,
            end = JengaTheme.spacing.xl,
            bottom = JengaTheme.spacing.md,
        )

    /** Padding around the lazy body. */
    public val contentPadding: PaddingValues
        @Composable get() = PaddingValues()

    /** Padding around the footer row. */
    public val footerPadding: PaddingValues
        @Composable get() = PaddingValues(horizontal = JengaTheme.spacing.xl, vertical = JengaTheme.spacing.lg)

    /** Gap between footer children. */
    public val footerSpacing: Dp
        @Composable get() = JengaTheme.spacing.sm
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
    subtitle: String?,
    footer: (@Composable RowScope.() -> Unit)?,
    modifier: Modifier = Modifier,
    headerAction: (@Composable () -> Unit)? = null,
    titleStyle: TextStyle = JengaListSheetDefaults.titleStyle,
    subtitleStyle: TextStyle = JengaListSheetDefaults.subtitleStyle,
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
                    JengaText(text = subtitle, style = subtitleStyle, color = JengaTheme.colors.textMuted)
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
