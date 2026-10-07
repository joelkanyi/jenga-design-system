package io.github.joelkanyi.jenga.component.picker

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import io.github.joelkanyi.jenga.component.calendar.JengaCalendarDefaults
import io.github.joelkanyi.jenga.theme.JengaTheme
import kotlinx.datetime.LocalDate
import kotlinx.datetime.Month
import kotlinx.datetime.YearMonth
import kotlinx.datetime.number

/** The order of the day, month and year wheels in a [JengaDateOfBirthPicker]. */
public enum class JengaDateFieldOrder {
    /** Day, month, year (e.g. 14 March 1990). */
    DayMonthYear,

    /** Month, day, year (e.g. March 14 1990). */
    MonthDayYear,

    /** Year, month, day (e.g. 1990 March 14). */
    YearMonthDay,
}

/** Defaults for [JengaDateOfBirthPicker]. */
public object JengaDateOfBirthPickerDefaults {
    /** The earliest selectable date. */
    public val MinDate: LocalDate = LocalDate(1900, 1, 1)
}

/**
 * A date-of-birth picker: day, month and year scroll wheels side by side. The
 * day is kept valid as the month or year changes (31 March becomes 28 or 29
 * February), and the date never leaves [minDate]..[maxDate].
 *
 * @sample io.github.joelkanyi.jenga.samples.JengaDateOfBirthPickerSample
 *
 * @param value the selected date.
 * @param onValueChange called with the new date once a wheel settles.
 * @param monthLabel the label for a month (e.g. "March").
 * @param modifier the [Modifier] for the picker.
 * @param minDate the earliest selectable date.
 * @param maxDate the latest selectable date; today by default.
 * @param order the order of the wheels; see [JengaDateFieldOrder].
 * @param dayContentDescription accessibility label for the day wheel.
 * @param monthContentDescription accessibility label for the month wheel.
 * @param yearContentDescription accessibility label for the year wheel.
 * @param visibleItemCount the number of rows visible on each wheel.
 * @param itemHeight the height of each row.
 * @param colors the wheel color set; defaults to [JengaWheelPickerDefaults.colors].
 */
@Composable
public fun JengaDateOfBirthPicker(
    value: LocalDate,
    onValueChange: (LocalDate) -> Unit,
    monthLabel: (Month) -> String,
    modifier: Modifier = Modifier,
    minDate: LocalDate = JengaDateOfBirthPickerDefaults.MinDate,
    maxDate: LocalDate = remember { JengaCalendarDefaults.today() },
    order: JengaDateFieldOrder = JengaDateFieldOrder.DayMonthYear,
    dayContentDescription: String? = null,
    monthContentDescription: String? = null,
    yearContentDescription: String? = null,
    visibleItemCount: Int = JengaWheelPickerDefaults.VisibleItemCount,
    itemHeight: Dp = JengaWheelPickerDefaults.ItemHeight,
    colors: JengaWheelPickerColors = JengaWheelPickerDefaults.colors(),
) {
    require(minDate <= maxDate) { "minDate must not be after maxDate" }
    val date = value.coerceIn(minDate, maxDate)
    val years = remember(minDate, maxDate) { (minDate.year..maxDate.year).toList() }
    val months = remember(date.year, minDate, maxDate) {
        val first = if (date.year == minDate.year) minDate.month.number else 1
        val last = if (date.year == maxDate.year) maxDate.month.number else 12
        (first..last).map { Month(it) }
    }
    val days = remember(date.year, date.month, minDate, maxDate) {
        val inMonth = YearMonth(date.year, date.month)
        val first = if (inMonth == YearMonth(minDate.year, minDate.month)) minDate.day else 1
        val last = if (inMonth == YearMonth(maxDate.year, maxDate.month)) maxDate.day else inMonth.numberOfDays
        (first..last).toList()
    }
    fun change(year: Int = date.year, month: Month = date.month, day: Int = date.day) {
        val length = YearMonth(year, month).numberOfDays
        onValueChange(LocalDate(year, month, day.coerceAtMost(length)).coerceIn(minDate, maxDate))
    }
    val wheel: @Composable (Wheel, Modifier) -> Unit = { which, wheelModifier ->
        when (which) {
            Wheel.Day -> JengaWheelPicker(
                items = days,
                selectedIndex = days.indexOf(date.day),
                onSelectedIndexChange = { change(day = days[it]) },
                modifier = wheelModifier,
                contentDescription = dayContentDescription,
                visibleItemCount = visibleItemCount,
                itemHeight = itemHeight,
                colors = colors,
            )
            Wheel.Month -> JengaWheelPicker(
                items = months,
                selectedIndex = months.indexOf(date.month),
                onSelectedIndexChange = { change(month = months[it]) },
                modifier = wheelModifier,
                itemLabel = monthLabel,
                contentDescription = monthContentDescription,
                visibleItemCount = visibleItemCount,
                itemHeight = itemHeight,
                colors = colors,
            )
            Wheel.Year -> JengaWheelPicker(
                items = years,
                selectedIndex = years.indexOf(date.year),
                onSelectedIndexChange = { change(year = years[it]) },
                modifier = wheelModifier,
                contentDescription = yearContentDescription,
                visibleItemCount = visibleItemCount,
                itemHeight = itemHeight,
                colors = colors,
            )
        }
    }
    Row(modifier = modifier, horizontalArrangement = Arrangement.spacedBy(JengaTheme.spacing.xs)) {
        val wheels = when (order) {
            JengaDateFieldOrder.DayMonthYear -> listOf(Wheel.Day, Wheel.Month, Wheel.Year)
            JengaDateFieldOrder.MonthDayYear -> listOf(Wheel.Month, Wheel.Day, Wheel.Year)
            JengaDateFieldOrder.YearMonthDay -> listOf(Wheel.Year, Wheel.Month, Wheel.Day)
        }
        wheels.forEach {
            val weight = when (it) {
                Wheel.Day -> 1f
                Wheel.Month -> 2f
                Wheel.Year -> 1.4f
            }
            wheel(it, Modifier.weight(weight))
        }
    }
}

private enum class Wheel { Day, Month, Year }
