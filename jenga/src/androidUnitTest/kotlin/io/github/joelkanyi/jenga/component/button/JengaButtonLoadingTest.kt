package io.github.joelkanyi.jenga.component.button

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.semantics.SemanticsActions
import androidx.compose.ui.test.assertIsNotEnabled
import androidx.compose.ui.test.getUnclippedBoundsInRoot
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performSemanticsAction
import androidx.compose.ui.text.TextLayoutResult
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.unit.width
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

    @Test
    fun contentPaddingAppliesToTextOverload() {
        compose.setContent {
            JengaTheme {
                JengaButton(text = "Request inventory", onClick = {}, modifier = Modifier.testTag("default"))
                JengaButton(
                    text = "Request inventory",
                    onClick = {},
                    modifier = Modifier.testTag("compact"),
                    contentPadding = PaddingValues(horizontal = 10.dp, vertical = 12.dp),
                )
            }
        }
        val default = compose.onNodeWithTag("default").getUnclippedBoundsInRoot().width
        val compact = compose.onNodeWithTag("compact").getUnclippedBoundsInRoot().width
        assertEquals(20f, (default - compact).value, 0.5f)
    }

    @Test
    fun textStyleAppliesToLabel() {
        compose.setContent {
            JengaTheme {
                JengaButton(
                    text = "Request inventory",
                    onClick = {},
                    textStyle = JengaButtonDefaults.textStyle(JengaButtonSize.Medium).copy(fontSize = 13.5.sp),
                )
            }
        }
        val layouts = mutableListOf<TextLayoutResult>()
        compose.onNodeWithText("Request inventory").performSemanticsAction(SemanticsActions.GetTextLayoutResult) {
            it(layouts)
        }
        assertEquals(13.5.sp, layouts.single().layoutInput.style.fontSize)
    }
}
