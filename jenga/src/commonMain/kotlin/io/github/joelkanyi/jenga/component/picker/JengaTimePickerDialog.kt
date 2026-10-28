package io.github.joelkanyi.jenga.component.picker

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.window.Dialog
import io.github.joelkanyi.jenga.component.feedback.JengaDialogActions
import io.github.joelkanyi.jenga.component.feedback.jengaDialogCard
import io.github.joelkanyi.jenga.component.text.JengaText
import io.github.joelkanyi.jenga.theme.JengaTheme
import kotlinx.datetime.LocalTime

/**
 * A dialog holding a [JengaTimeInput], with dismiss and confirm actions. The time
 * is only reported when the confirm action is tapped.
 *
 * @sample io.github.joelkanyi.jenga.samples.JengaTimePickerDialogSample
 *
 * @param onDismissRequest called when the dialog is dismissed (dismiss action, scrim tap or back).
 * @param onConfirm called with the picked time when the confirm action is tapped.
 * @param title the dialog title (e.g. "Delivery time").
 * @param confirmLabel the confirm action label.
 * @param dismissLabel the dismiss action label.
 * @param initialTime the time shown when the dialog opens.
 * @param hourLabel the label for the hour wheel or field.
 * @param minuteLabel the label for the minute wheel or field.
 * @param amLabel the label for the morning half.
 * @param pmLabel the label for the afternoon half.
 * @param modifier the [Modifier] for the dialog surface.
 * @param style wheels or typed fields; see [JengaTimeInputStyle].
 * @param is24Hour whether hours run 0 to 23 with no AM / PM choice.
 * @param minuteStep the gap between minutes on the minute wheel.
 */
@Composable
public fun JengaTimePickerDialog(
    onDismissRequest: () -> Unit,
    onConfirm: (LocalTime) -> Unit,
    title: String,
    confirmLabel: String,
    dismissLabel: String,
    initialTime: LocalTime,
    hourLabel: String,
    minuteLabel: String,
    amLabel: String,
    pmLabel: String,
    modifier: Modifier = Modifier,
    style: JengaTimeInputStyle = JengaTimeInputStyle.Wheel,
    is24Hour: Boolean = false,
    minuteStep: Int = 1,
) {
    var time by remember { mutableStateOf(initialTime) }
    Dialog(onDismissRequest = onDismissRequest) {
        Column(modifier = modifier.jengaDialogCard(), verticalArrangement = Arrangement.spacedBy(JengaTheme.spacing.md)) {
            JengaText(title, style = JengaTheme.typography.titleMedium, color = JengaTheme.colors.textPrimary)
            JengaTimeInput(
                time = time,
                onTimeChange = { time = it },
                hourLabel = hourLabel,
                minuteLabel = minuteLabel,
                amLabel = amLabel,
                pmLabel = pmLabel,
                modifier = Modifier.fillMaxWidth(),
                style = style,
                is24Hour = is24Hour,
                minuteStep = minuteStep,
            )
            JengaDialogActions(
                confirmLabel = confirmLabel,
                dismissLabel = dismissLabel,
                confirmEnabled = true,
                onConfirm = { onConfirm(time) },
                onDismiss = onDismissRequest,
            )
        }
    }
}
