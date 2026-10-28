package io.github.joelkanyi.jenga.component.card

import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.semantics.SemanticsProperties
import androidx.compose.ui.test.SemanticsMatcher
import androidx.compose.ui.test.assert
import androidx.compose.ui.test.assertIsSelected
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import io.github.joelkanyi.jenga.component.progress.JengaProgressDefaults
import io.github.joelkanyi.jenga.component.progress.JengaProgressTone
import io.github.joelkanyi.jenga.component.text.JengaText
import io.github.joelkanyi.jenga.theme.JengaTheme
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class JengaCardSelectedTest {

    @get:Rule
    val compose = createComposeRule()

    @Test
    fun selectedCardIsReportedSelected() {
        compose.setContent {
            JengaTheme {
                JengaCard(modifier = Modifier.testTag("selected"), onClick = {}, selected = true) { JengaText("A") }
                JengaCard(modifier = Modifier.testTag("plain"), onClick = {}) { JengaText("B") }
            }
        }
        compose.onNodeWithTag("selected").assertIsSelected()
        compose.onNodeWithTag("plain").assert(SemanticsMatcher.keyNotDefined(SemanticsProperties.Selected))
    }

    @Test
    fun selectedColorsUseBrandBorder() {
        compose.setContent {
            JengaTheme {
                val selected = JengaCardDefaults.colors(JengaCardVariant.Outlined, selected = true)
                val plain = JengaCardDefaults.colors(JengaCardVariant.Outlined)
                assertEquals(JengaTheme.colors.brand, selected.border)
                assertEquals(plain.container, selected.container)
                assertEquals(JengaTheme.colors.border, plain.border)
            }
        }
    }

    @Test
    fun errorProgressToneUsesErrorColor() {
        compose.setContent {
            JengaTheme {
                assertEquals(JengaTheme.colors.error, JengaProgressDefaults.colors(JengaProgressTone.Error).indicator)
            }
        }
    }

    @Test
    fun elevatedCardGetsAnOutlineOnlyInDarkThemes() {
        var dark = JengaCardColors(Color.Unspecified, Color.Unspecified)
        var light = JengaCardColors(Color.Unspecified, Color.Unspecified)
        var darkBorder = Color.Unspecified
        compose.setContent {
            JengaTheme(darkTheme = true) {
                dark = JengaCardDefaults.colors(JengaCardVariant.Elevated)
                darkBorder = JengaTheme.colors.border
            }
            JengaTheme(darkTheme = false) { light = JengaCardDefaults.colors(JengaCardVariant.Elevated) }
        }
        compose.waitForIdle()
        assertEquals(darkBorder, dark.border)
        assertEquals(Color.Transparent, light.border)
    }
}
