package io.github.joelkanyi.jenga.component.calendar

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.key
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import dev.drewhamilton.poko.Poko
import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.DayOfWeek
import kotlinx.datetime.LocalDate
import kotlinx.datetime.YearMonth
import kotlinx.datetime.isoDayNumber
import kotlinx.datetime.minus
import kotlinx.datetime.monthsUntil
import kotlinx.datetime.plus

/** Where a [JengaCalendarDay] falls relative to the month being shown. */
public enum class JengaCalendarDayPosition {
    /** A day of the shown month. */
    InMonth,

    /** A trailing day of the previous month, filling the first week. */
    PreviousMonth,

    /** A leading day of the next month, filling the last weeks. */
    NextMonth,
}

/** One cell of a [JengaCalendar]: a [date] and where it falls relative to the shown month. */
@Poko
@Immutable
public class JengaCalendarDay(
    public val date: LocalDate,
    public val position: JengaCalendarDayPosition,
)

/**
 * The scroll state of a [JengaCalendar]: which months it can show and which one
 * is visible. Create one with [rememberJengaCalendarState].
 */
@Stable
public class JengaCalendarState internal constructor(
    public val startMonth: YearMonth,
    public val endMonth: YearMonth,
    public val firstDayOfWeek: DayOfWeek,
    internal val pagerState: PagerState,
) {
    /** The month currently shown (the one a swipe settles on). */
    public val currentMonth: YearMonth
        get() = monthAt(pagerState.currentPage)

    /** Jumps to [month], clamped to the calendar's range. */
    public suspend fun scrollToMonth(month: YearMonth) {
        pagerState.scrollToPage(pageOf(month))
    }

    /** Animates to [month], clamped to the calendar's range. */
    public suspend fun animateScrollToMonth(month: YearMonth) {
        pagerState.animateScrollToPage(pageOf(month))
    }

    internal val monthCount: Int get() = startMonth.monthsUntil(endMonth) + 1

    internal fun monthAt(page: Int): YearMonth = startMonth.plus(page, DateTimeUnit.MONTH)

    internal fun pageOf(month: YearMonth): Int = startMonth.monthsUntil(month).coerceIn(0, monthCount - 1)

    internal val daysOfWeek: List<DayOfWeek>
        get() = List(7) { DayOfWeek((firstDayOfWeek.isoDayNumber - 1 + it) % 7 + 1) }

    internal fun weeksOf(month: YearMonth): List<List<JengaCalendarDay>> {
        val leading = (month.firstDay.dayOfWeek.isoDayNumber - firstDayOfWeek.isoDayNumber + 7) % 7
        val gridStart = month.firstDay.minus(leading, DateTimeUnit.DAY)
        return List(WeeksPerMonth) { week ->
            List(7) { day ->
                val date = gridStart.plus(week * 7 + day, DateTimeUnit.DAY)
                val position = when {
                    date < month.firstDay -> JengaCalendarDayPosition.PreviousMonth
                    date > month.lastDay -> JengaCalendarDayPosition.NextMonth
                    else -> JengaCalendarDayPosition.InMonth
                }
                JengaCalendarDay(date, position)
            }
        }
    }
}

private const val WeeksPerMonth = 6

/**
 * Remembers a [JengaCalendarState].
 *
 * @param startMonth the first month the calendar can show.
 * @param endMonth the last month the calendar can show.
 * @param firstVisibleMonth the month shown first; clamped to the range.
 * @param firstDayOfWeek the day each week row starts on.
 */
@Composable
public fun rememberJengaCalendarState(
    startMonth: YearMonth,
    endMonth: YearMonth,
    firstVisibleMonth: YearMonth = startMonth,
    firstDayOfWeek: DayOfWeek = DayOfWeek.MONDAY,
): JengaCalendarState {
    require(startMonth <= endMonth) { "startMonth must not be after endMonth" }
    val count = startMonth.monthsUntil(endMonth) + 1
    val pagerState = key(startMonth, endMonth) {
        rememberPagerState(
            initialPage = startMonth.monthsUntil(firstVisibleMonth).coerceIn(0, count - 1),
            pageCount = { count },
        )
    }
    return remember(startMonth, endMonth, firstDayOfWeek, pagerState) {
        JengaCalendarState(startMonth, endMonth, firstDayOfWeek, pagerState)
    }
}

/**
 * A month calendar that pages horizontally between the months of [state]. Every
 * part is a slot: [dayContent] draws each cell of the six-week grid, including
 * the days of the neighbouring months (check [JengaCalendarDay.position]), and
 * [monthHeader] and [weekHeader] sit above each month's grid. Selection and
 * styling are the caller's; [JengaDatePicker] is a ready-made single-date picker
 * built on it, and [JengaCalendarDefaults.Day] is a cell you can reuse.
 *
 * @sample io.github.joelkanyi.jenga.samples.JengaCalendarRangeSample
 *
 * @param state the calendar state from [rememberJengaCalendarState].
 * @param modifier the [Modifier] for the calendar.
 * @param monthHeader content above each month's grid.
 * @param weekHeader content above each month's grid, under [monthHeader], given the
 *   days of the week in display order.
 * @param dayContent the content of a single day cell; cells are square.
 */
@Composable
public fun JengaCalendar(
    state: JengaCalendarState,
    modifier: Modifier = Modifier,
    monthHeader: @Composable (YearMonth) -> Unit = {},
    weekHeader: @Composable (List<DayOfWeek>) -> Unit = {},
    dayContent: @Composable BoxScope.(JengaCalendarDay) -> Unit,
) {
    HorizontalPager(
        state = state.pagerState,
        modifier = modifier,
        verticalAlignment = Alignment.Top,
    ) { page ->
        val month = state.monthAt(page)
        val weeks = remember(month, state.firstDayOfWeek) { state.weeksOf(month) }
        Column(modifier = Modifier.fillMaxWidth()) {
            monthHeader(month)
            weekHeader(state.daysOfWeek)
            weeks.forEach { week ->
                Row(modifier = Modifier.fillMaxWidth()) {
                    week.forEach { day ->
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .aspectRatio(1f),
                            contentAlignment = Alignment.Center,
                        ) {
                            dayContent(day)
                        }
                    }
                }
            }
        }
    }
}
