package io.github.joelkanyi.jenga.component.otp

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.autofill.ContentType
import androidx.compose.ui.semantics.SemanticsActions
import androidx.compose.ui.semantics.SemanticsProperties
import androidx.compose.ui.test.SemanticsMatcher
import androidx.compose.ui.test.assert
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.performTextInput
import androidx.compose.ui.test.performTextReplacement
import io.github.joelkanyi.jenga.theme.JengaTheme
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import kotlin.time.Duration
import kotlin.time.Duration.Companion.milliseconds
import kotlin.time.Duration.Companion.minutes
import kotlin.time.Duration.Companion.seconds
import kotlin.time.TestTimeSource

@RunWith(RobolectricTestRunner::class)
class JengaOtpTest {

    @get:Rule
    val compose = createComposeRule()

    @Test
    fun numericFilterDropsNonDigitsAndCapsLength() {
        assertEquals("123456", filterOtp("Your code is 123-456 7", 6, JengaOtpInputType.Numeric))
        assertEquals("", filterOtp("abc", 6, JengaOtpInputType.Numeric))
        assertEquals("1234", filterOtp("1234", 6, JengaOtpInputType.Numeric))
    }

    @Test
    fun alphanumericFilterKeepsAsciiLettersAndDigits() {
        assertEquals("A7k9", filterOtp("A-7 k_9!", 6, JengaOtpInputType.Alphanumeric))
        assertEquals("AB", filterOtp("ÄAéB", 6, JengaOtpInputType.Alphanumeric))
        assertEquals("ABC", filterOtp("ABCDEF", 3, JengaOtpInputType.Alphanumeric))
    }

    @Test
    fun formatRemainingRoundsUpToWholeSeconds() {
        assertEquals("5:00", JengaOtpResendDefaults.formatRemaining(5.minutes))
        assertEquals("4:59", JengaOtpResendDefaults.formatRemaining(299.seconds))
        assertEquals("0:01", JengaOtpResendDefaults.formatRemaining(1.milliseconds))
        assertEquals("1:01", JengaOtpResendDefaults.formatRemaining(61.seconds))
        assertEquals("0:00", JengaOtpResendDefaults.formatRemaining(Duration.ZERO))
        assertEquals("0:00", JengaOtpResendDefaults.formatRemaining((-3).seconds))
        assertEquals("75:00", JengaOtpResendDefaults.formatRemaining(75.minutes))
    }

    @Test
    fun countdownFollowsTheClockAndRestarts() {
        val clock = TestTimeSource()
        val countdown = JengaCountdownState(5.minutes, clock)
        assertEquals(5.minutes, countdown.remaining)
        assertTrue(countdown.isRunning)

        clock += 10.seconds
        countdown.update()
        assertEquals(4.minutes + 50.seconds, countdown.remaining)

        clock += 6.minutes
        countdown.update()
        assertEquals(Duration.ZERO, countdown.remaining)
        assertFalse(countdown.isRunning)

        countdown.restart()
        assertEquals(5.minutes, countdown.remaining)
        clock += 1.seconds
        countdown.update()
        assertEquals(4.minutes + 59.seconds, countdown.remaining)
    }

    @Test
    fun typingIsFilteredAndCompletesOnce() {
        val completed = mutableListOf<String>()
        var latest = ""
        compose.setContent {
            JengaTheme {
                var code by remember { mutableStateOf("") }
                JengaOtpField(
                    value = code,
                    onValueChange = {
                        code = it
                        latest = it
                    },
                    contentDescription = "Code",
                    length = 4,
                    onComplete = { completed += it },
                )
            }
        }
        val field = compose.onNodeWithContentDescription("Code")
        field.performTextInput("1a2")
        compose.runOnIdle { assertEquals("12", latest) }
        field.performTextInput("34")
        compose.runOnIdle {
            assertEquals("1234", latest)
            assertEquals(listOf("1234"), completed)
        }
        field.performTextInput("5")
        compose.runOnIdle {
            assertEquals("1234", latest)
            assertEquals(listOf("1234"), completed)
        }
    }

    @Test
    fun pasteTakesTheCodeOutOfAMessage() {
        val completed = mutableListOf<String>()
        var latest = ""
        compose.setContent {
            JengaTheme {
                var code by remember { mutableStateOf("") }
                JengaOtpField(
                    value = code,
                    onValueChange = {
                        code = it
                        latest = it
                    },
                    contentDescription = "Code",
                    onComplete = { completed += it },
                )
            }
        }
        compose.onNodeWithContentDescription("Code").performTextReplacement("Your code is 482-913.")
        compose.runOnIdle {
            assertEquals("482913", latest)
            assertEquals(listOf("482913"), completed)
        }
    }

    @Test
    fun exposesOneSmsCodeFieldToAccessibilityAndAutofill() {
        compose.setContent {
            JengaTheme {
                JengaOtpField(value = "12", onValueChange = {}, contentDescription = "Code", masked = true)
            }
        }
        compose.onNodeWithContentDescription("Code")
            .assert(SemanticsMatcher.expectValue(SemanticsProperties.ContentType, ContentType.SmsOtpCode))
            .assert(SemanticsMatcher.keyIsDefined(SemanticsProperties.Password))
    }

    @Test
    fun readOnlyCannotBeEdited() {
        compose.setContent {
            JengaTheme {
                JengaOtpField(value = "12", onValueChange = {}, contentDescription = "Code", readOnly = true)
            }
        }
        compose.onNodeWithContentDescription("Code")
            .assert(SemanticsMatcher.expectValue(SemanticsProperties.IsEditable, false))
            .assert(SemanticsMatcher.keyNotDefined(SemanticsActions.SetText))
    }
}
