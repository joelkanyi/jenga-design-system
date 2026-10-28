package io.github.joelkanyi.jenga.component.stepindicator

import androidx.compose.ui.semantics.SemanticsActions
import androidx.compose.ui.test.SemanticsMatcher
import androidx.compose.ui.test.assert
import androidx.compose.ui.test.assertIsNotSelected
import androidx.compose.ui.test.assertIsSelected
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.performClick
import io.github.joelkanyi.jenga.theme.JengaTheme
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class JengaStepIndicatorTest {

    @get:Rule
    val compose = createComposeRule()

    private val steps = listOf(
        JengaStep("Device", JengaStepState.Done),
        JengaStep("Payment", JengaStepState.Current),
        JengaStep("Verification", JengaStepState.Pending),
    )

    private fun show(onStepClick: ((Int) -> Unit)?) {
        compose.setContent {
            JengaTheme {
                JengaStepIndicator(
                    steps = steps,
                    stepContentDescription = { index, step -> "Step ${index + 1} of 3, ${step.label}" },
                    onStepClick = onStepClick,
                )
            }
        }
    }

    @Test
    fun eachStepAnnouncesItsDescriptionAndOnlyTheCurrentOneIsSelected() {
        show(onStepClick = null)
        compose.onNodeWithContentDescription("Step 1 of 3, Device").assertIsNotSelected()
        compose.onNodeWithContentDescription("Step 2 of 3, Payment").assertIsSelected()
        compose.onNodeWithContentDescription("Step 3 of 3, Verification").assertIsNotSelected()
    }

    @Test
    fun withoutOnStepClickTheStepsAreNotTappable() {
        show(onStepClick = null)
        compose.onNodeWithContentDescription("Step 1 of 3, Device")
            .assert(SemanticsMatcher.keyNotDefined(SemanticsActions.OnClick))
    }

    @Test
    fun tappingAStepReportsItsIndex() {
        val taps = mutableListOf<Int>()
        show(onStepClick = { taps += it })
        compose.onNodeWithContentDescription("Step 3 of 3, Verification").performClick()
        compose.onNodeWithContentDescription("Step 1 of 3, Device").performClick()
        assertEquals(listOf(2, 0), taps)
    }
}
