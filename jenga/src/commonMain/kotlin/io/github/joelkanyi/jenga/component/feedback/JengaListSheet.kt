package io.github.joelkanyi.jenga.component.feedback

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
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
import io.github.joelkanyi.jenga.component.divider.JengaDivider
import io.github.joelkanyi.jenga.component.text.JengaText
import io.github.joelkanyi.jenga.theme.JengaTheme

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
 * @param footer optional row pinned to the bottom of the sheet, for actions.
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
    footer: (@Composable RowScope.() -> Unit)? = null,
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
        JengaListSheetLayout(title = title, subtitle = subtitle, footer = footer, content = content)
    }
}

@Composable
internal fun JengaListSheetLayout(
    title: String,
    subtitle: String?,
    footer: (@Composable RowScope.() -> Unit)?,
    modifier: Modifier = Modifier,
    content: LazyListScope.() -> Unit,
) {
    Column(modifier = modifier.fillMaxWidth()) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = JengaTheme.spacing.xl)
                .padding(bottom = JengaTheme.spacing.md),
            verticalArrangement = Arrangement.spacedBy(JengaTheme.spacing.xxs),
        ) {
            JengaText(text = title, style = JengaTheme.typography.titleLarge)
            if (subtitle != null) {
                JengaText(
                    text = subtitle,
                    style = JengaTheme.typography.bodySmall,
                    color = JengaTheme.colors.textMuted,
                )
            }
        }
        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f, fill = false),
            content = content,
        )
        if (footer != null) {
            JengaDivider()
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = JengaTheme.spacing.xl, vertical = JengaTheme.spacing.lg),
                horizontalArrangement = Arrangement.spacedBy(JengaTheme.spacing.sm),
                verticalAlignment = Alignment.CenterVertically,
                content = footer,
            )
        }
    }
}
