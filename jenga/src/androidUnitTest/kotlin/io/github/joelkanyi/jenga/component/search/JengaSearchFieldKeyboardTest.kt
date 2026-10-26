package io.github.joelkanyi.jenga.component.search

import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.ui.semantics.SemanticsProperties
import androidx.compose.ui.test.SemanticsMatcher
import androidx.compose.ui.test.assert
import androidx.compose.ui.test.hasSetTextAction
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.performImeAction
import androidx.compose.ui.text.input.ImeAction
import io.github.joelkanyi.jenga.theme.JengaTheme
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class JengaSearchFieldKeyboardTest {

    @get:Rule
    val compose = createComposeRule()

    @Test
    fun searchKeyRunsTheSearch() {
        var searches = 0
        compose.setContent {
            JengaTheme {
                JengaSearchField(
                    value = "0712",
                    onValueChange = {},
                    placeholder = "Search customers",
                    clearContentDescription = "Clear",
                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
                    keyboardActions = KeyboardActions(onSearch = { searches++ }),
                )
            }
        }
        val input = compose.onNode(hasSetTextAction())
        input.assert(SemanticsMatcher.expectValue(SemanticsProperties.ImeAction, ImeAction.Search))
        input.performImeAction()
        assertEquals(1, searches)
    }
}
