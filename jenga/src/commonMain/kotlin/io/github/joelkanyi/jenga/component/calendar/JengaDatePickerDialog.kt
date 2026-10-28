package io.github.joelkanyi.jenga.component.calendar

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.window.Dialog
import io.github.joelkanyi.jenga.component.feedback.JengaDialogActions
import io.github.joelkanyi.jenga.component.feedback.jengaDialogCard
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
 * @param dateRange the earliest and latest dates that can be picked; see [JengaDatePicker].
 * @param yearPickerLabel when set, the month title opens a grid of years; see [JengaDatePicker].
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
    dateRange: ClosedRange<LocalDate>? = null,
    yearPickerLabel: ((YearMonth) -> String)? = null,
) {
    var selected by remember { mutableStateOf(initialSelectedDate) }
    Dialog(onDismissRequest = onDismissRequest) {
        Column(
            modifier = modifier.jengaDialogCard(),
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
                dateRange = dateRange,
                yearPickerLabel = yearPickerLabel,
            )
            JengaDialogActions(
                confirmLabel = confirmLabel,
                dismissLabel = dismissLabel,
                confirmEnabled = selected != null,
                onConfirm = { selected?.let(onConfirm) },
                onDismiss = onDismissRequest,
            )
        }
    }
}

@Deprecated("Kept for binary compatibility", level = DeprecationLevel.HIDDEN)
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
    JengaDatePickerDialog(
        onDismissRequest = onDismissRequest,
        onConfirm = onConfirm,
        confirmLabel = confirmLabel,
        dismissLabel = dismissLabel,
        monthTitle = monthTitle,
        dayOfWeekLabel = dayOfWeekLabel,
        previousMonthContentDescription = previousMonthContentDescription,
        nextMonthContentDescription = nextMonthContentDescription,
        modifier = modifier,
        initialSelectedDate = initialSelectedDate,
        isSelectable = isSelectable,
        dateContentDescription = dateContentDescription,
        colors = colors,
        dateRange = null,
        yearPickerLabel = null,
    )
}
