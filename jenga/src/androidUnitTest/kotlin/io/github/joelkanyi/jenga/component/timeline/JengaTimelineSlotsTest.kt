package io.github.joelkanyi.jenga.component.timeline

import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import io.github.joelkanyi.jenga.component.text.JengaText
import io.github.joelkanyi.jenga.theme.JengaTheme
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class JengaTimelineSlotsTest {

    @get:Rule
    val compose = createComposeRule()

    @Test
    fun positionOfCoversEveryPlace() {
        assertEquals(JengaTimelinePosition.Only, JengaTimelinePosition.of(0, 1))
        assertEquals(JengaTimelinePosition.First, JengaTimelinePosition.of(0, 3))
        assertEquals(JengaTimelinePosition.Middle, JengaTimelinePosition.of(1, 3))
        assertEquals(JengaTimelinePosition.Last, JengaTimelinePosition.of(2, 3))
    }

    @Test
    fun slotItemShowsMarkerAndContent() {
        compose.setContent {
            JengaTheme {
                JengaTimelineItem(
                    position = JengaTimelinePosition.Only,
                    marker = { JengaTimelineMarker(color = JengaTheme.colors.success) { JengaText("1") } },
                ) {
                    JengaText("Visit the customer")
                }
            }
        }
        compose.onNodeWithText("1").assertExists()
        compose.onNodeWithText("Visit the customer").assertExists()
    }
}
