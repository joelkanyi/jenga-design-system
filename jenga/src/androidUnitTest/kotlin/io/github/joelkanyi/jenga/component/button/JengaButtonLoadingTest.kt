package io.github.joelkanyi.jenga.component.button

import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.test.assertIsNotEnabled
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import io.github.joelkanyi.jenga.component.badge.JengaBadgeTone
import io.github.joelkanyi.jenga.component.status.JengaStatusPill
import io.github.joelkanyi.jenga.component.text.JengaText
import io.github.joelkanyi.jenga.theme.JengaTheme
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class JengaButtonLoadingTest {

    @get:Rule
    val compose = createComposeRule()

    @Test
    fun loadingTextReplacesLabelAndBlocksClicks() {
        var clicks = 0
        compose.setContent {
            JengaTheme {
                JengaButton(
                    text = "Check ID",
                    onClick = { clicks++ },
                    modifier = Modifier.testTag("button"),
                    loading = true,
                    loadingText = "Checking…",
                )
            }
        }
        compose.onNodeWithText("Checking…").assertExists()
        compose.onNodeWithText("Check ID").assertDoesNotExist()
        compose.onNodeWithTag("button").assertIsNotEnabled().performClick()
        assertEquals(0, clicks)
    }

    @Test
    fun loadingTextIgnoredWhenNotLoading() {
        compose.setContent {
            JengaTheme {
                JengaButton(text = "Check ID", onClick = {}, loadingText = "Checking…")
            }
        }
        compose.onNodeWithText("Check ID").assertExists()
        compose.onNodeWithText("Checking…").assertDoesNotExist()
    }

    @Test
    fun loadingWithoutTextHidesLabel() {
        compose.setContent {
            JengaTheme {
                JengaButton(text = "Check ID", onClick = {}, loading = true)
            }
        }
        compose.onNodeWithText("Check ID").assertDoesNotExist()
    }

    @Test
    fun statusPillShowsLeadingIcon() {
        compose.setContent {
            JengaTheme {
                JengaStatusPill(
                    label = "28:41",
                    tone = JengaBadgeTone.Warning,
                    leadingIcon = { JengaText("icon") },
                )
            }
        }
        compose.onNodeWithText("icon").assertExists()
        compose.onNodeWithText("28:41").assertExists()
    }

    @Test
    fun statusPillSpinnerWinsOverLeadingIcon() {
        compose.setContent {
            JengaTheme {
                JengaStatusPill(label = "28:41", loading = true, leadingIcon = { JengaText("icon") })
            }
        }
        compose.onNodeWithText("icon").assertDoesNotExist()
    }
}
