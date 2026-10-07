package io.github.joelkanyi.jenga.component.feedback

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.window.Dialog
import io.github.joelkanyi.jenga.component.button.JengaButton
import io.github.joelkanyi.jenga.component.text.JengaText
import io.github.joelkanyi.jenga.theme.JengaTheme

/** Defaults for [JengaDialog]. */
public object JengaDialogDefaults {
    /** Default dialog shape. */
    public val shape: Shape
        @Composable get() = JengaTheme.shapes.card

    /** Padding inside the dialog. */
    public val contentPadding: PaddingValues
        @Composable get() = PaddingValues(JengaTheme.spacing.xl)

    /** Title text style. */
    public val titleStyle: TextStyle
        @Composable get() = JengaTheme.typography.headingSmall

    /** Body text style. */
    public val textStyle: TextStyle
        @Composable get() = JengaTheme.typography.bodyMedium

    /** Gap between the actions. */
    public val actionSpacing: Dp
        @Composable get() = JengaTheme.spacing.sm
}

/**
 * A modal dialog with an optional title and body, plus confirm/dismiss actions.
 * For a destructive confirmation, pass a danger [JengaButton] as [confirmButton].
 *
 * @sample io.github.joelkanyi.jenga.samples.JengaDialogSample
 *
 * @param onDismissRequest called when the user dismisses (scrim tap or back).
 * @param confirmButton the primary action (e.g. a [JengaButton]).
 * @param modifier the [Modifier] for the dialog surface.
 * @param title optional title.
 * @param text optional body text.
 * @param dismissButton optional secondary action.
 * @param shape the dialog shape.
 * @param contentPadding padding inside the dialog.
 * @param titleStyle the [title] text style.
 * @param textStyle the [text] text style.
 * @param actionSpacing gap between the actions.
 */
@Composable
public fun JengaDialog(
    onDismissRequest: () -> Unit,
    confirmButton: @Composable () -> Unit,
    modifier: Modifier = Modifier,
    title: String? = null,
    text: String? = null,
    dismissButton: (@Composable () -> Unit)? = null,
    shape: Shape = JengaDialogDefaults.shape,
    contentPadding: PaddingValues = JengaDialogDefaults.contentPadding,
    titleStyle: TextStyle = JengaDialogDefaults.titleStyle,
    textStyle: TextStyle = JengaDialogDefaults.textStyle,
    actionSpacing: Dp = JengaDialogDefaults.actionSpacing,
) {
    Dialog(onDismissRequest = onDismissRequest) {
        JengaDialogSurface(
            confirmButton = confirmButton,
            modifier = modifier,
            title = title,
            text = text,
            dismissButton = dismissButton,
            shape = shape,
            contentPadding = contentPadding,
            titleStyle = titleStyle,
            textStyle = textStyle,
            actionSpacing = actionSpacing,
        )
    }
}

@Composable
internal fun JengaDialogSurface(
    confirmButton: @Composable () -> Unit,
    modifier: Modifier = Modifier,
    title: String? = null,
    text: String? = null,
    dismissButton: (@Composable () -> Unit)? = null,
    shape: Shape = JengaDialogDefaults.shape,
    contentPadding: PaddingValues = JengaDialogDefaults.contentPadding,
    titleStyle: TextStyle = JengaDialogDefaults.titleStyle,
    textStyle: TextStyle = JengaDialogDefaults.textStyle,
    actionSpacing: Dp = JengaDialogDefaults.actionSpacing,
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .clip(shape)
            .background(JengaTheme.colors.surface)
            .padding(contentPadding),
        verticalArrangement = Arrangement.spacedBy(JengaTheme.spacing.md),
    ) {
        if (title != null) {
            JengaText(text = title, style = titleStyle)
        }
        if (text != null) {
            JengaText(
                text = text,
                style = textStyle,
                color = JengaTheme.colors.textMuted,
            )
        }
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(actionSpacing, Alignment.End),
        ) {
            if (dismissButton != null) dismissButton()
            confirmButton()
        }
    }
}
