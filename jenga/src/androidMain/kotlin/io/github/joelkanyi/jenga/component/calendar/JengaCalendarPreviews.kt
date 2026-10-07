package io.github.joelkanyi.jenga.component.calendar

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import io.github.joelkanyi.jenga.core.preview.JengaBlockPreviews
import io.github.joelkanyi.jenga.core.preview.RtlPreview
import io.github.joelkanyi.jenga.theme.JengaTheme
import kotlinx.datetime.DayOfWeek
import kotlinx.datetime.LocalDate
import kotlinx.datetime.Month
import kotlinx.datetime.YearMonth

// ---- Previews --------------------------------------------------------------

@JengaBlockPreviews
@Composable
internal fun JengaDatePickerPreview() {
    JengaTheme { JengaDatePickerShowcase() }
}

@Preview(name = "RTL", showBackground = true)
@Composable
internal fun JengaDatePickerRtlPreview() {
    JengaTheme { RtlPreview { JengaDatePickerShowcase() } }
}

@Composable
private fun JengaDatePickerShowcase() {
    val march = YearMonth(2026, Month.MARCH)
    JengaDatePicker(
        selectedDate = LocalDate(2026, 3, 18),
        onSelectedDateChange = {},
        monthTitle = { "${it.month.name.lowercase().replaceFirstChar(Char::uppercase)} ${it.year}" },
        dayOfWeekLabel = { it.name.take(2).lowercase().replaceFirstChar(Char::uppercase) },
        previousMonthContentDescription = "Previous month",
        nextMonthContentDescription = "Next month",
        modifier = Modifier.background(JengaTheme.colors.surface).padding(JengaTheme.spacing.md),
        state = rememberJengaCalendarState(march, march, march, DayOfWeek.MONDAY),
        isSelectable = { it.dayOfWeek != DayOfWeek.SUNDAY },
        today = LocalDate(2026, 3, 14),
    )
}
