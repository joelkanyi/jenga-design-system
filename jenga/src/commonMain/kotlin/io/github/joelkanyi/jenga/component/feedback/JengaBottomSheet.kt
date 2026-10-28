package io.github.joelkanyi.jenga.component.feedback

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.ModalBottomSheetDefaults
import androidx.compose.material3.ModalBottomSheetProperties
import androidx.compose.material3.SheetState
import androidx.compose.material3.SheetValue
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.backhandler.BackHandler
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import io.github.joelkanyi.jenga.theme.JengaTheme
import kotlinx.coroutines.launch

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
    internal val asksBeforeClosing: Boolean,
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
 * @param confirmDismiss called before the sheet closes, whether from a drag, a
 *   scrim tap, Back or [JengaSheetState.hide]; null (the default) always allows
 *   it. Return false to keep the sheet open, for example to ask the user first;
 *   once they agree, make it return true and call [JengaSheetState.hide]. It can
 *   run more than once for one close, so it should only read or set state. When
 *   set, Back skips the system's shrink animation, so a refused close leaves the
 *   sheet as it was.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
public fun rememberJengaSheetState(
    skipPartiallyExpanded: Boolean = true,
    confirmDismiss: (() -> Boolean)? = null,
): JengaSheetState {
    val latestConfirmDismiss by rememberUpdatedState(confirmDismiss)
    val confirmValueChange: (SheetValue) -> Boolean = remember {
        { value -> value != SheetValue.Hidden || latestConfirmDismiss?.invoke() ?: true }
    }
    val m3State = rememberModalBottomSheetState(
        skipPartiallyExpanded = skipPartiallyExpanded,
        confirmValueChange = confirmValueChange,
    )
    val asksBeforeClosing = confirmDismiss != null
    return remember(m3State, asksBeforeClosing) { JengaSheetState(m3State, asksBeforeClosing) }
}

@Deprecated("Kept for binary compatibility", level = DeprecationLevel.HIDDEN)
@Composable
public fun rememberJengaSheetState(skipPartiallyExpanded: Boolean = true): JengaSheetState = rememberJengaSheetState(skipPartiallyExpanded = skipPartiallyExpanded, confirmDismiss = null)

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
 * @sample io.github.joelkanyi.jenga.samples.JengaBottomSheetSample
 *
 * @param onDismissRequest called after the sheet has closed (drag down, scrim or Back).
 * @param modifier the [Modifier] for the sheet.
 * @param content the sheet body, laid out in a [ColumnScope].
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
public fun JengaBottomSheet(
    onDismissRequest: () -> Unit,
    modifier: Modifier = Modifier,
    content: @Composable ColumnScope.() -> Unit,
) {
    JengaBottomSheet(
        onDismissRequest = onDismissRequest,
        sheetState = rememberJengaSheetState(),
        modifier = modifier,
        content = content,
    )
}

/**
 * A modal bottom sheet whose visibility is driven by an external [sheetState], so
 * it can be shown/hidden imperatively (the MVI pattern: a ViewModel effect calls
 * `sheetState.hide()`, then composition removes the sheet).
 *
 * @param onDismissRequest called after the sheet has closed (drag down, scrim or
 *   Back). Not called when the state's `confirmDismiss` keeps it open.
 * @param sheetState the externally-owned state from [rememberJengaSheetState].
 * @param modifier the [Modifier] for the sheet.
 * @param gesturesEnabled whether the sheet can be dragged. When false, the drag
 *   handle stays visible, the sheet stays put, and gestures inside the content
 *   (a drawing pad, a list) still work.
 * @param content the sheet body, laid out in a [ColumnScope].
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
public fun JengaBottomSheet(
    onDismissRequest: () -> Unit,
    sheetState: JengaSheetState,
    modifier: Modifier = Modifier,
    gesturesEnabled: Boolean = true,
    content: @Composable ColumnScope.() -> Unit,
) {
    ModalBottomSheet(
        onDismissRequest = {
            if (!sheetState.isVisible) onDismissRequest()
        },
        modifier = modifier,
        sheetState = sheetState.m3State,
        sheetGesturesEnabled = gesturesEnabled,
        containerColor = JengaTheme.colors.surface,
        contentColor = JengaTheme.colors.textPrimary,
        scrimColor = JengaTheme.colors.scrim,
        dragHandle = { JengaDragHandle() },
        properties = if (sheetState.asksBeforeClosing) AskingSheetProperties else ModalBottomSheetDefaults.properties,
    ) {
        if (sheetState.asksBeforeClosing) {
            AskBeforeClosingOnBack(sheetState, onDismissRequest)
        }
        content()
    }
}

@OptIn(ExperimentalMaterial3Api::class)
private val AskingSheetProperties = ModalBottomSheetProperties(shouldDismissOnBackPress = false)

@Suppress("DEPRECATION")
@OptIn(ExperimentalComposeUiApi::class)
@Composable
private fun AskBeforeClosingOnBack(sheetState: JengaSheetState, onDismissRequest: () -> Unit) {
    val scope = rememberCoroutineScope()
    BackHandler {
        scope.launch { sheetState.hide() }.invokeOnCompletion {
            if (!sheetState.isVisible) onDismissRequest()
        }
    }
}

@Deprecated("Kept for binary compatibility", level = DeprecationLevel.HIDDEN)
@Composable
public fun JengaBottomSheet(
    onDismissRequest: () -> Unit,
    sheetState: JengaSheetState,
    modifier: Modifier = Modifier,
    content: @Composable ColumnScope.() -> Unit,
) {
    JengaBottomSheet(
        onDismissRequest = onDismissRequest,
        sheetState = sheetState,
        modifier = modifier,
        gesturesEnabled = true,
        content = content,
    )
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
