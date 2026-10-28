package io.github.joelkanyi.jenga.component.calendar

import androidx.compose.ui.semantics.SemanticsActions
import androidx.compose.ui.test.SemanticsMatcher
import androidx.compose.ui.test.assert
import androidx.compose.ui.test.assertIsEnabled
import androidx.compose.ui.test.assertIsNotEnabled
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import io.github.joelkanyi.jenga.theme.JengaTheme
import kotlinx.datetime.LocalDate
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class JengaDatePickerRangeTest {

    @get:Rule
    val compose = createComposeRule()

    private val picked = mutableListOf<LocalDate>()

    private fun showPicker(
        selectedDate: LocalDate?,
        dateRange: ClosedRange<LocalDate>,
        withYearPicker: Boolean = true,
    ) {
        compose.setContent {
            JengaTheme {
                JengaDatePicker(
                    selectedDate = selectedDate,
                    onSelectedDateChange = { picked += it },
                    monthTitle = { it.toString() },
                    dayOfWeekLabel = { it.name.take(2) },
                    previousMonthContentDescription = "Previous month",
                    nextMonthContentDescription = "Next month",
                    dateRange = dateRange,
                    yearPickerLabel = if (withYearPicker) ({ "Choose year, $it" }) else null,
                )
            }
        }
    }

    @Test
    fun daysOutsideTheRangeAreDisabled() {
        showPicker(LocalDate(2026, 3, 15), LocalDate(2026, 3, 10)..LocalDate(2026, 3, 20))
        compose.onNodeWithContentDescription("2026-03-09").assertIsNotEnabled()
        compose.onNodeWithContentDescription("2026-03-10").assertIsEnabled()
        compose.onNodeWithContentDescription("2026-03-20").assertIsEnabled()
        compose.onNodeWithContentDescription("2026-03-21").assertIsNotEnabled()
        compose.onNodeWithContentDescription("Previous month").assertIsNotEnabled()
        compose.onNodeWithContentDescription("Next month").assertIsNotEnabled()
    }

    @Test
    fun withNoSelectionItOpensOnTheMonthNearestTodayInsideTheRange() {
        showPicker(selectedDate = null, dateRange = LocalDate(1980, 1, 1)..LocalDate(2008, 10, 28))
        compose.onNodeWithContentDescription("Choose year, 2008-10").assertExists()
    }

    @Test
    fun pickingAYearKeepsTheMonthAndSelectsNoDay() {
        showPicker(LocalDate(2005, 10, 5), LocalDate(1980, 1, 1)..LocalDate(2008, 10, 28))
        compose.onNodeWithContentDescription("Choose year, 2005-10").performClick()
        compose.onNodeWithText("1990").performClick()
        compose.onNodeWithContentDescription("Choose year, 1990-10").assertExists()
        assertEquals(emptyList<LocalDate>(), picked)
    }

    @Test
    fun pickingAYearClampsTheMonthToTheRange() {
        showPicker(LocalDate(2005, 10, 5), LocalDate(2000, 1, 1)..LocalDate(2008, 3, 15))
        compose.onNodeWithContentDescription("Choose year, 2005-10").performClick()
        compose.onNodeWithText("2008").performClick()
        compose.onNodeWithContentDescription("Choose year, 2008-03").assertExists()
    }

    @Test
    fun withoutAYearPickerLabelTheTitleIsNotClickable() {
        showPicker(LocalDate(2005, 10, 5), LocalDate(2000, 1, 1)..LocalDate(2008, 3, 15), withYearPicker = false)
        compose.onNodeWithText("2005-10").assert(SemanticsMatcher.keyNotDefined(SemanticsActions.OnClick))
    }
}
