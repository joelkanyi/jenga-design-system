package io.github.joelkanyi.jenga.component.calendar

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.selection.selectable
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import dev.drewhamilton.poko.Poko
import io.github.joelkanyi.jenga.component.button.JengaIconButton
import io.github.joelkanyi.jenga.component.icon.JengaIcon
import io.github.joelkanyi.jenga.component.text.JengaText
import io.github.joelkanyi.jenga.theme.JengaTheme
import kotlinx.coroutines.launch
import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.DayOfWeek
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.YearMonth
import kotlinx.datetime.minus
import kotlinx.datetime.plus
import kotlinx.datetime.todayIn
import kotlin.time.Clock

/** Resolved colors for calendar day cells and the date picker. Override via [JengaCalendarDefaults.colors]. */
@Poko
@Immutable
public class JengaCalendarColors(
    public val selectedContainer: Color,
    public val selectedContent: Color,
    public val todayBorder: Color,
    public val content: Color,
    public val disabledContent: Color,
    public val weekdayContent: Color,
    public val titleContent: Color,
) {
    public fun copy(
        selectedContainer: Color = this.selectedContainer,
        selectedContent: Color = this.selectedContent,
        todayBorder: Color = this.todayBorder,
        content: Color = this.content,
        disabledContent: Color = this.disabledContent,
        weekdayContent: Color = this.weekdayContent,
        titleContent: Color = this.titleContent,
    ): JengaCalendarColors = JengaCalendarColors(
        selectedContainer,
        selectedContent,
        todayBorder,
        content,
        disabledContent,
        weekdayContent,
        titleContent,
    )
}

/** Defaults, token mappings and reusable cells for [JengaCalendar] and [JengaDatePicker]. */
public object JengaCalendarDefaults {
    /** Themed colors. */
    @Composable
    public fun colors(): JengaCalendarColors {
        val c = JengaTheme.colors
        return JengaCalendarColors(
            selectedContainer = c.brand,
            selectedContent = c.onBrand,
            todayBorder = c.brand,
            content = c.textPrimary,
            disabledContent = c.contentDisabled,
            weekdayContent = c.textMuted,
            titleContent = c.textPrimary,
        )
    }

    /** Today in the device's time zone. */
    public fun today(): LocalDate = Clock.System.todayIn(TimeZone.currentSystemDefault())

    /**
     * A standard day cell: the day number in a circle, filled when [selected] and
     * outlined when [isToday]. Days outside the shown month draw nothing.
     *
     * @param day the calendar day.
     * @param selected whether the day is selected.
     * @param enabled whether the day can be picked.
     * @param isToday whether the day is today.
     * @param onClick called when the day is tapped.
     * @param modifier the [Modifier] for this cell.
     * @param contentDescription accessibility label for the day (e.g. "Wednesday, 18 March").
     * @param colors the color set; defaults to [colors].
     */
    @Composable
    public fun Day(
        day: JengaCalendarDay,
        selected: Boolean,
        enabled: Boolean,
        isToday: Boolean,
        onClick: () -> Unit,
        modifier: Modifier = Modifier,
        contentDescription: String? = null,
        colors: JengaCalendarColors = colors(),
    ) {
        if (day.position != JengaCalendarDayPosition.InMonth) return
        val shape = JengaTheme.shapes.pill
        Box(
            modifier = modifier
                .fillMaxSize()
                .padding(2.dp)
                .clip(shape)
                .background(if (selected) colors.selectedContainer else Color.Transparent)
                .then(if (isToday && !selected) Modifier.border(1.dp, colors.todayBorder, shape) else Modifier)
                .selectable(selected = selected, enabled = enabled, role = Role.Button, onClick = onClick)
                .semantics { if (contentDescription != null) this.contentDescription = contentDescription },
            contentAlignment = Alignment.Center,
        ) {
            JengaText(
                text = day.date.day.toString(),
                style = JengaTheme.typography.bodySmall,
                color = when {
                    selected -> colors.selectedContent
                    !enabled -> colors.disabledContent
                    else -> colors.content
                },
            )
        }
    }

    /**
     * A row of weekday labels, one per column.
     *
     * @param daysOfWeek the days in display order.
     * @param label the label for a day (e.g. "Mo").
     * @param modifier the [Modifier] for this row.
     * @param colors the color set; defaults to [colors].
     */
    @Composable
    public fun WeekHeader(
        daysOfWeek: List<DayOfWeek>,
        label: (DayOfWeek) -> String,
        modifier: Modifier = Modifier,
        colors: JengaCalendarColors = colors(),
    ) {
        Row(modifier = modifier.fillMaxWidth().padding(vertical = JengaTheme.spacing.xs)) {
            daysOfWeek.forEach { dayOfWeek ->
                JengaText(
                    text = label(dayOfWeek),
                    style = JengaTheme.typography.caption,
                    color = colors.weekdayContent,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.weight(1f),
                    maxLines = 1,
                )
            }
        }
    }
}

/**
 * A single-date picker: a month title with previous/next buttons above a paging
 * [JengaCalendar]. Every visible string comes from the caller ([monthTitle],
 * [dayOfWeekLabel] and the button descriptions), so it follows the app's locale.
 *
 * @sample io.github.joelkanyi.jenga.samples.JengaDatePickerSample
 *
 * @param selectedDate the selected date, or null for none.
 * @param onSelectedDateChange called with a tapped date.
 * @param monthTitle the title for a month (e.g. "March 2026").
 * @param dayOfWeekLabel the column label for a day of the week (e.g. "Mo").
 * @param previousMonthContentDescription accessibility label for the previous-month button.
 * @param nextMonthContentDescription accessibility label for the next-month button.
 * @param modifier the [Modifier] for the picker.
 * @param state the calendar state; by default it spans ten years either side of
 *   the selected date (or today) and opens on its month.
 * @param isSelectable whether a date can be picked.
 * @param today the date marked as today.
 * @param dateContentDescription accessibility label for a day cell; the ISO date by default.
 * @param colors the color set; defaults to [JengaCalendarDefaults.colors].
 */
@Composable
public fun JengaDatePicker(
    selectedDate: LocalDate?,
    onSelectedDateChange: (LocalDate) -> Unit,
    monthTitle: (YearMonth) -> String,
    dayOfWeekLabel: (DayOfWeek) -> String,
    previousMonthContentDescription: String,
    nextMonthContentDescription: String,
    modifier: Modifier = Modifier,
    state: JengaCalendarState = rememberDefaultPickerState(selectedDate),
    isSelectable: (LocalDate) -> Boolean = { true },
    today: LocalDate = remember { JengaCalendarDefaults.today() },
    dateContentDescription: (LocalDate) -> String = { it.toString() },
    colors: JengaCalendarColors = JengaCalendarDefaults.colors(),
) {
    val scope = rememberCoroutineScope()
    val month = state.currentMonth
    Column(modifier = modifier, verticalArrangement = Arrangement.spacedBy(JengaTheme.spacing.xs)) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            JengaText(
                text = monthTitle(month),
                style = JengaTheme.typography.titleSmall,
                color = colors.titleContent,
                modifier = Modifier.weight(1f).padding(start = JengaTheme.spacing.sm),
            )
            JengaIconButton(
                onClick = { scope.launch { state.animateScrollToMonth(month.minus(1, DateTimeUnit.MONTH)) } },
                enabled = month > state.startMonth,
            ) {
                JengaIcon(
                    JengaTheme.icons.chevron,
                    contentDescription = previousMonthContentDescription,
                    modifier = Modifier.rotate(180f),
                )
            }
            JengaIconButton(
                onClick = { scope.launch { state.animateScrollToMonth(month.plus(1, DateTimeUnit.MONTH)) } },
                enabled = month < state.endMonth,
            ) {
                JengaIcon(JengaTheme.icons.chevron, contentDescription = nextMonthContentDescription)
            }
        }
        JengaCalendar(
            state = state,
            weekHeader = { days -> JengaCalendarDefaults.WeekHeader(days, dayOfWeekLabel, colors = colors) },
        ) { day ->
            val selectable = isSelectable(day.date)
            JengaCalendarDefaults.Day(
                day = day,
                selected = day.date == selectedDate,
                enabled = selectable,
                isToday = day.date == today,
                onClick = { onSelectedDateChange(day.date) },
                contentDescription = dateContentDescription(day.date),
                colors = colors,
            )
        }
    }
}

@Composable
private fun rememberDefaultPickerState(selectedDate: LocalDate?): JengaCalendarState {
    val anchor = remember { (selectedDate ?: JengaCalendarDefaults.today()).let { YearMonth(it.year, it.month) } }
    return rememberJengaCalendarState(
        startMonth = anchor.minus(DefaultSpanYears, DateTimeUnit.YEAR),
        endMonth = anchor.plus(DefaultSpanYears, DateTimeUnit.YEAR),
        firstVisibleMonth = anchor,
    )
}

private const val DefaultSpanYears = 10
