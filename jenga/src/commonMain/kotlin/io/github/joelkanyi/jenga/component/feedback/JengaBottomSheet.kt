package io.github.joelkanyi.jenga.component.feedback

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SheetState
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import io.github.joelkanyi.jenga.component.divider.JengaDivider
import io.github.joelkanyi.jenga.component.text.JengaText
import io.github.joelkanyi.jenga.theme.JengaTheme

/**
 * The visibility state of a [JengaBottomSheet]. Create one with
 * [rememberJengaSheetState] and drive it imperatively (e.g. from an MVI effect):
 * `scope.launch { sheetState.hide() }`. Keeps the underlying Material state out of
 * Jenga's public API.
 */
@Stable
@OptIn(ExperimentalMaterial3Api::class)
public class JengaSheetState internal constructor(
    internal val m3State: SheetState,
) {
    /** Whether the sheet is currently visible. */
    public val isVisible: Boolean get() = m3State.isVisible

    /** Animates the sheet to its expanded (visible) position. */
    public suspend fun show(): Unit = m3State.show()

    /** Animates the sheet fully off-screen. Pair with dismissing the sheet from composition. */
    public suspend fun hide(): Unit = m3State.hide()
}

/**
 * Remembers a [JengaSheetState] for an externally-controlled [JengaBottomSheet].
 *
 * @param skipPartiallyExpanded when true (the default), the sheet has no
 *   half-expanded detent; it is either fully shown or hidden.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
public fun rememberJengaSheetState(skipPartiallyExpanded: Boolean = true): JengaSheetState {
    val m3State = rememberModalBottomSheetState(skipPartiallyExpanded = skipPartiallyExpanded)
    return remember(m3State) { JengaSheetState(m3State) }
}

/**
 * A modal bottom sheet, themed with Jenga tokens.
 *
 * Control visibility by conditional composition: render this only while the
 * sheet should be shown:
 * ```
 * if (showSheet) {
 *     JengaBottomSheet(onDismissRequest = { showSheet = false }) { /* content */ }
 * }
 * ```
 *
 * For imperative control (e.g. animating the sheet closed in response to a
 * ViewModel effect before navigating), use the overload that takes a
 * [JengaSheetState] from [rememberJengaSheetState].
 *
 * Pass a [title], [subtitle] or [footer] to get the structured layout: the header
 * sits at the top, [content] scrolls in between, and the footer (typically the
 * sheet's actions) stays pinned at the bottom, above the keyboard. In that layout
 * [content] is already vertically scrollable, so do not put a lazy list inside it.
 * With all three null, [content] fills the sheet exactly as given.
 *
 * @sample io.github.joelkanyi.jenga.samples.JengaBottomSheetSample
 *
 * @param onDismissRequest called when the sheet is dismissed (drag down or scrim).
 * @param modifier the [Modifier] for the sheet.
 * @param title optional heading shown at the top of the sheet.
 * @param subtitle optional supporting line under the [title].
 * @param footer optional row pinned to the bottom of the sheet, for actions.
 * @param content the sheet body, laid out in a [ColumnScope].
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
public fun JengaBottomSheet(
    onDismissRequest: () -> Unit,
    modifier: Modifier = Modifier,
    title: String? = null,
    subtitle: String? = null,
    footer: (@Composable RowScope.() -> Unit)? = null,
    content: @Composable ColumnScope.() -> Unit,
) {
    JengaBottomSheet(
        onDismissRequest = onDismissRequest,
        sheetState = rememberJengaSheetState(),
        modifier = modifier,
        title = title,
        subtitle = subtitle,
        footer = footer,
        content = content,
    )
}

/**
 * A modal bottom sheet whose visibility is driven by an external [sheetState], so
 * it can be shown/hidden imperatively (the MVI pattern: a ViewModel effect calls
 * `sheetState.hide()`, then composition removes the sheet).
 *
 * The [title], [subtitle] and [footer] behave as in the other overload.
 *
 * @param onDismissRequest called when the sheet is dismissed (drag down or scrim).
 * @param sheetState the externally-owned state from [rememberJengaSheetState].
 * @param modifier the [Modifier] for the sheet.
 * @param title optional heading shown at the top of the sheet.
 * @param subtitle optional supporting line under the [title].
 * @param footer optional row pinned to the bottom of the sheet, for actions.
 * @param content the sheet body, laid out in a [ColumnScope].
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
public fun JengaBottomSheet(
    onDismissRequest: () -> Unit,
    sheetState: JengaSheetState,
    modifier: Modifier = Modifier,
    title: String? = null,
    subtitle: String? = null,
    footer: (@Composable RowScope.() -> Unit)? = null,
    content: @Composable ColumnScope.() -> Unit,
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
        if (title == null && subtitle == null && footer == null) {
            content()
        } else {
            JengaSheetLayout(title = title, subtitle = subtitle, footer = footer, content = content)
        }
    }
}

@Composable
internal fun JengaSheetLayout(
    title: String?,
    subtitle: String?,
    footer: (@Composable RowScope.() -> Unit)?,
    content: @Composable ColumnScope.() -> Unit,
) {
    Column(modifier = Modifier.fillMaxWidth()) {
        if (title != null || subtitle != null) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = JengaTheme.spacing.xl)
                    .padding(bottom = JengaTheme.spacing.md),
                verticalArrangement = Arrangement.spacedBy(JengaTheme.spacing.xxs),
            ) {
                if (title != null) {
                    JengaText(text = title, style = JengaTheme.typography.titleLarge)
                }
                if (subtitle != null) {
                    JengaText(
                        text = subtitle,
                        style = JengaTheme.typography.bodySmall,
                        color = JengaTheme.colors.textMuted,
                    )
                }
            }
        }
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f, fill = false)
                .verticalScroll(rememberScrollState()),
            content = content,
        )
        if (footer != null) {
            JengaDivider()
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .imePadding()
                    .padding(horizontal = JengaTheme.spacing.xl, vertical = JengaTheme.spacing.lg),
                horizontalArrangement = Arrangement.spacedBy(JengaTheme.spacing.sm),
                verticalAlignment = Alignment.CenterVertically,
                content = footer,
            )
        }
    }
}

@Composable
internal fun JengaDragHandle() {
    Box(
        modifier = Modifier.padding(vertical = JengaTheme.spacing.md),
        contentAlignment = Alignment.Center,
    ) {
        Box(
            modifier = Modifier
                .size(width = 36.dp, height = 4.dp)
                .clip(JengaTheme.shapes.pill)
                .background(JengaTheme.colors.borderStrong),
        )
    }
}
