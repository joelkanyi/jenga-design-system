package io.github.joelkanyi.jenga.component.feedback

import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.test.espresso.Espresso
import io.github.joelkanyi.jenga.component.text.JengaText
import io.github.joelkanyi.jenga.theme.JengaTheme
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class JengaBottomSheetDismissTest {

    @get:Rule
    val compose = createComposeRule()

    private var dismissals = 0
    private var confirmations = 0
    private lateinit var sheetState: JengaSheetState
    private lateinit var scope: CoroutineScope

    private fun showSheet(allowDismiss: Boolean) {
        compose.setContent {
            JengaTheme {
                scope = rememberCoroutineScope()
                sheetState = rememberJengaSheetState(
                    confirmDismiss = {
                        confirmations++
                        allowDismiss
                    },
                )
                JengaBottomSheet(onDismissRequest = { dismissals++ }, sheetState = sheetState) {
                    JengaText("Signature")
                }
            }
        }
        compose.waitForIdle()
    }

    @Test
    fun refusedHideKeepsTheSheetOpen() {
        showSheet(allowDismiss = false)
        scope.launch { sheetState.hide() }
        compose.waitForIdle()
        compose.onNodeWithText("Signature").assertIsDisplayed()
        assertEquals(1, confirmations)
        assertEquals(0, dismissals)
    }

    @Test
    fun refusedBackKeepsTheSheetOpenAndDoesNotReportADismiss() {
        showSheet(allowDismiss = false)
        Espresso.pressBack()
        compose.waitForIdle()
        compose.onNodeWithText("Signature").assertIsDisplayed()
        assertEquals(1, confirmations)
        assertEquals(0, dismissals)
    }

    @Test
    fun allowedBackClosesTheSheet() {
        showSheet(allowDismiss = true)
        Espresso.pressBack()
        compose.waitForIdle()
        assertEquals(1, dismissals)
    }
}
