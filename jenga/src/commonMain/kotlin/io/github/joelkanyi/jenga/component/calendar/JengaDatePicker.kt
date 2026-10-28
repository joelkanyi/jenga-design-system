package io.github.joelkanyi.jenga.component.calendar

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.foundation.selection.selectable
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.clearAndSetSemantics
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.role
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import dev.drewhamilton.poko.Poko
import io.github.joelkanyi.jenga.component.button.JengaIconButton
import io.github.joelkanyi.jenga.component.icon.JengaIcon
import io.github.joelkanyi.jenga.component.icon.JengaIcons
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
 * @param dateRange the earliest and latest dates that can be picked; days outside
 *   it are disabled and the month buttons stop at its first and last month. With
 *   no selected date the picker opens on the month nearest today inside the range.
 * @param state the calendar state; by default it spans [dateRange], or ten years
 *   either side of the selected date (or today), and opens on the selected month.
 * @param isSelectable whether a date can be picked.
 * @param today the date marked as today.
 * @param dateContentDescription accessibility label for a day cell; the ISO date by default.
 * @param colors the color set; defaults to [JengaCalendarDefaults.colors].
 * @param yearPickerLabel when set, the month title becomes a button that swaps the
 *   days for a grid of years; this is its accessibility label for the shown month
 *   (e.g. "Choose year, October 2008"). Picking a year returns to the days on the
 *   same month in that year, without selecting a day.
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
    dateRange: ClosedRange<LocalDate>? = null,
    state: JengaCalendarState = rememberDefaultPickerState(selectedDate, dateRange),
    isSelectable: (LocalDate) -> Boolean = { true },
    today: LocalDate = remember { JengaCalendarDefaults.today() },
    dateContentDescription: (LocalDate) -> String = { it.toString() },
    colors: JengaCalendarColors = JengaCalendarDefaults.colors(),
    yearPickerLabel: ((YearMonth) -> String)? = null,
) {
    var choosingYear by remember { mutableStateOf(false) }
    Column(modifier = modifier, verticalArrangement = Arrangement.spacedBy(JengaTheme.spacing.xs)) {
        DatePickerHeader(
            state = state,
            title = monthTitle(state.currentMonth),
            choosingYear = choosingYear,
            yearPickerLabel = yearPickerLabel?.invoke(state.currentMonth),
            onTitleClick = { choosingYear = !choosingYear },
            previousMonthContentDescription = previousMonthContentDescription,
            nextMonthContentDescription = nextMonthContentDescription,
            colors = colors,
        )
        DaysOrYears(state = state, choosingYear = choosingYear, onYearPick = { choosingYear = false }, colors = colors) { gridModifier ->
            DayGrid(
                state = state,
                selectedDate = selectedDate,
                onSelectedDateChange = onSelectedDateChange,
                dayOfWeekLabel = dayOfWeekLabel,
                isEnabled = { date -> (dateRange == null || date in dateRange) && isSelectable(date) },
                today = today,
                dateContentDescription = dateContentDescription,
                colors = colors,
                modifier = gridModifier,
            )
        }
    }
}

@Deprecated("Kept for binary compatibility", level = DeprecationLevel.HIDDEN)
@Composable
public fun JengaDatePicker(
    selectedDate: LocalDate?,
    onSelectedDateChange: (LocalDate) -> Unit,
    monthTitle: (YearMonth) -> String,
    dayOfWeekLabel: (DayOfWeek) -> String,
    previousMonthContentDescription: String,
    nextMonthContentDescription: String,
    modifier: Modifier = Modifier,
    state: JengaCalendarState = rememberDefaultPickerState(selectedDate, null),
    isSelectable: (LocalDate) -> Boolean = { true },
    today: LocalDate = remember { JengaCalendarDefaults.today() },
    dateContentDescription: (LocalDate) -> String = { it.toString() },
    colors: JengaCalendarColors = JengaCalendarDefaults.colors(),
) {
    JengaDatePicker(
        selectedDate = selectedDate,
        onSelectedDateChange = onSelectedDateChange,
        monthTitle = monthTitle,
        dayOfWeekLabel = dayOfWeekLabel,
        previousMonthContentDescription = previousMonthContentDescription,
        nextMonthContentDescription = nextMonthContentDescription,
        modifier = modifier,
        state = state,
        isSelectable = isSelectable,
        today = today,
        dateContentDescription = dateContentDescription,
        colors = colors,
        dateRange = null,
        yearPickerLabel = null,
    )
}

@Composable
private fun DaysOrYears(
    state: JengaCalendarState,
    choosingYear: Boolean,
    onYearPick: () -> Unit,
    colors: JengaCalendarColors,
    dayGrid: @Composable (Modifier) -> Unit,
) {
    var daysHeight by remember { mutableStateOf(0.dp) }
    val density = LocalDensity.current
    if (choosingYear) {
        YearGrid(state = state, onYearClick = onYearPick, colors = colors, modifier = Modifier.height(daysHeight))
    } else {
        dayGrid(Modifier.onSizeChanged { daysHeight = with(density) { it.height.toDp() } })
    }
}

@Composable
private fun DayGrid(
    state: JengaCalendarState,
    selectedDate: LocalDate?,
    onSelectedDateChange: (LocalDate) -> Unit,
    dayOfWeekLabel: (DayOfWeek) -> String,
    isEnabled: (LocalDate) -> Boolean,
    today: LocalDate,
    dateContentDescription: (LocalDate) -> String,
    colors: JengaCalendarColors,
    modifier: Modifier = Modifier,
) {
    JengaCalendar(
        state = state,
        modifier = modifier,
        weekHeader = { days -> JengaCalendarDefaults.WeekHeader(days, dayOfWeekLabel, colors = colors) },
    ) { day ->
        JengaCalendarDefaults.Day(
            day = day,
            selected = day.date == selectedDate,
            enabled = isEnabled(day.date),
            isToday = day.date == today,
            onClick = { onSelectedDateChange(day.date) },
            contentDescription = dateContentDescription(day.date),
            colors = colors,
        )
    }
}

@Composable
private fun DatePickerHeader(
    state: JengaCalendarState,
    title: String,
    choosingYear: Boolean,
    yearPickerLabel: String?,
    onTitleClick: () -> Unit,
    previousMonthContentDescription: String,
    nextMonthContentDescription: String,
    colors: JengaCalendarColors,
) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        if (yearPickerLabel == null) {
            JengaText(
                text = title,
                style = JengaTheme.typography.titleSmall,
                color = colors.titleContent,
                modifier = Modifier.weight(1f).padding(start = JengaTheme.spacing.sm),
            )
        } else {
            YearPickerTitle(
                title = title,
                label = yearPickerLabel,
                open = choosingYear,
                onClick = onTitleClick,
                colors = colors,
                modifier = Modifier.weight(1f),
            )
        }
        if (!choosingYear) {
            MonthArrows(state, previousMonthContentDescription, nextMonthContentDescription)
        }
    }
}

@Composable
private fun MonthArrows(
    state: JengaCalendarState,
    previousMonthContentDescription: String,
    nextMonthContentDescription: String,
) {
    val scope = rememberCoroutineScope()
    val month = state.currentMonth
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

@Composable
private fun YearPickerTitle(
    title: String,
    label: String,
    open: Boolean,
    onClick: () -> Unit,
    colors: JengaCalendarColors,
    modifier: Modifier = Modifier,
) {
    Box(modifier = modifier, contentAlignment = Alignment.CenterStart) {
        Row(
            modifier = Modifier
                .clip(JengaTheme.shapes.pill)
                .clickable(role = Role.Button, onClick = onClick)
                .clearAndSetSemantics {
                    contentDescription = label
                    role = Role.Button
                }
                .padding(JengaTheme.spacing.sm),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(JengaTheme.spacing.xs),
        ) {
            JengaText(text = title, style = JengaTheme.typography.titleSmall, color = colors.titleContent)
            JengaIcon(
                if (open) JengaIcons.ChevronUp else JengaIcons.ChevronDown,
                contentDescription = null,
                tint = colors.titleContent,
            )
        }
    }
}

@Composable
private fun YearGrid(
    state: JengaCalendarState,
    onYearClick: () -> Unit,
    colors: JengaCalendarColors,
    modifier: Modifier = Modifier,
) {
    val scope = rememberCoroutineScope()
    val shownMonth = state.currentMonth
    val years = (state.startMonth.year..state.endMonth.year).toList()
    val gridState = rememberLazyGridState(initialFirstVisibleItemIndex = years.indexOf(shownMonth.year))
    LazyVerticalGrid(
        columns = GridCells.Fixed(YearColumns),
        state = gridState,
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceEvenly,
    ) {
        items(years, key = { it }) { year ->
            YearCell(year = year, shown = year == shownMonth.year, colors = colors) {
                scope.launch { state.scrollToMonth(YearMonth(year, shownMonth.month)) }
                onYearClick()
            }
        }
    }
}

@Composable
private fun YearCell(year: Int, shown: Boolean, colors: JengaCalendarColors, onClick: () -> Unit) {
    Box(
        modifier = Modifier
            .padding(vertical = JengaTheme.spacing.xs)
            .size(width = 72.dp, height = 36.dp)
            .clip(JengaTheme.shapes.pill)
            .background(if (shown) colors.selectedContainer else Color.Transparent)
            .selectable(selected = shown, role = Role.Button, onClick = onClick),
        contentAlignment = Alignment.Center,
    ) {
        JengaText(
            text = year.toString(),
            style = JengaTheme.typography.bodyMedium,
            color = if (shown) colors.selectedContent else colors.content,
        )
    }
}

@Composable
private fun rememberDefaultPickerState(selectedDate: LocalDate?, dateRange: ClosedRange<LocalDate>?): JengaCalendarState {
    if (dateRange == null) {
        val anchor = remember { (selectedDate ?: JengaCalendarDefaults.today()).yearMonth }
        return rememberJengaCalendarState(
            startMonth = anchor.minus(DefaultSpanYears, DateTimeUnit.YEAR),
            endMonth = anchor.plus(DefaultSpanYears, DateTimeUnit.YEAR),
            firstVisibleMonth = anchor,
        )
    }
    val opening = remember(dateRange) {
        val selectedInRange = selectedDate?.takeIf { it in dateRange }
        (selectedInRange ?: JengaCalendarDefaults.today().coerceIn(dateRange.start, dateRange.endInclusive)).yearMonth
    }
    return rememberJengaCalendarState(
        startMonth = dateRange.start.yearMonth,
        endMonth = dateRange.endInclusive.yearMonth,
        firstVisibleMonth = opening,
    )
}

private val LocalDate.yearMonth: YearMonth get() = YearMonth(year, month)

private const val DefaultSpanYears = 10

private const val YearColumns = 3
