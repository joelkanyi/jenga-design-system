package io.github.joelkanyi.jenga.component.menu

import androidx.compose.foundation.BorderStroke
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.unit.dp
import io.github.joelkanyi.jenga.theme.JengaTheme
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class JengaDropdownMenuBorderTest {

    @get:Rule
    val compose = createComposeRule()

    @Test
    fun darkThemeMenusGetAHairlineAndLightThemeMenusNone() {
        var dark: BorderStroke? = null
        var darkBorderColor = Color.Unspecified
        var light: BorderStroke? = BorderStroke(9.dp, Color.Red)
        compose.setContent {
            JengaTheme(darkTheme = true) {
                dark = JengaDropdownMenuDefaults.border
                darkBorderColor = JengaTheme.colors.border
            }
            JengaTheme(darkTheme = false) { light = JengaDropdownMenuDefaults.border }
        }
        compose.waitForIdle()
        assertEquals(BorderStroke(1.dp, darkBorderColor), dark)
        assertNull(light)
    }
}
