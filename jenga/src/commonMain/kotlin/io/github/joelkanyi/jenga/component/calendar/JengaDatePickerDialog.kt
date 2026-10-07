package io.github.joelkanyi.jenga.component.calendar

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.window.Dialog
import io.github.joelkanyi.jenga.component.button.JengaButton
import io.github.joelkanyi.jenga.component.button.JengaButtonSize
import io.github.joelkanyi.jenga.component.button.JengaButtonVariant
import io.github.joelkanyi.jenga.component.feedback.JengaDialogDefaults
import io.github.joelkanyi.jenga.theme.JengaTheme
import kotlinx.datetime.DayOfWeek
import kotlinx.datetime.LocalDate
import kotlinx.datetime.YearMonth

/**
 * A modal dialog holding a [JengaDatePicker] with confirm and dismiss actions.
 * The confirm action is enabled once a date is picked.
 *
 * @sample io.github.joelkanyi.jenga.samples.JengaDatePickerDialogSample
 *
 * @param onDismissRequest called when the dialog is dismissed (dismiss action, scrim tap or back).
 * @param onConfirm called with the picked date when the confirm action is tapped.
 * @param confirmLabel the confirm action label.
 * @param dismissLabel the dismiss action label.
 * @param monthTitle the title for a month (e.g. "March 2026").
 * @param dayOfWeekLabel the column label for a day of the week (e.g. "Mo").
 * @param previousMonthContentDescription accessibility label for the previous-month button.
 * @param nextMonthContentDescription accessibility label for the next-month button.
 * @param modifier the [Modifier] for the dialog surface.
 * @param initialSelectedDate the date selected when the dialog opens.
 * @param isSelectable whether a date can be picked.
 * @param dateContentDescription accessibility label for a day cell; the ISO date by default.
 * @param colors the color set; defaults to [JengaCalendarDefaults.colors].
 */
@Composable
public fun JengaDatePickerDialog(
    onDismissRequest: () -> Unit,
    onConfirm: (LocalDate) -> Unit,
    confirmLabel: String,
    dismissLabel: String,
    monthTitle: (YearMonth) -> String,
    dayOfWeekLabel: (DayOfWeek) -> String,
    previousMonthContentDescription: String,
    nextMonthContentDescription: String,
    modifier: Modifier = Modifier,
    initialSelectedDate: LocalDate? = null,
    isSelectable: (LocalDate) -> Boolean = { true },
    dateContentDescription: (LocalDate) -> String = { it.toString() },
    colors: JengaCalendarColors = JengaCalendarDefaults.colors(),
) {
    var selected by remember { mutableStateOf(initialSelectedDate) }
    Dialog(onDismissRequest = onDismissRequest) {
        Column(
            modifier = modifier
                .fillMaxWidth()
                .clip(JengaDialogDefaults.shape)
                .background(JengaTheme.colors.surface)
                .padding(JengaTheme.spacing.lg),
            verticalArrangement = Arrangement.spacedBy(JengaTheme.spacing.md),
        ) {
            JengaDatePicker(
                selectedDate = selected,
                onSelectedDateChange = { selected = it },
                monthTitle = monthTitle,
                dayOfWeekLabel = dayOfWeekLabel,
                previousMonthContentDescription = previousMonthContentDescription,
                nextMonthContentDescription = nextMonthContentDescription,
                isSelectable = isSelectable,
                dateContentDescription = dateContentDescription,
                colors = colors,
            )
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(JengaTheme.spacing.sm, Alignment.End),
            ) {
                JengaButton(
                    text = dismissLabel,
                    onClick = onDismissRequest,
                    variant = JengaButtonVariant.Ghost,
                    size = JengaButtonSize.Small,
                )
                JengaButton(
                    text = confirmLabel,
                    onClick = { selected?.let(onConfirm) },
                    size = JengaButtonSize.Small,
                    enabled = selected != null,
                )
            }
        }
    }
}
