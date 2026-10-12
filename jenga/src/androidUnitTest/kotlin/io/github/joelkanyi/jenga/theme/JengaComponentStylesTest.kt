package io.github.joelkanyi.jenga.theme

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.test.assertHeightIsEqualTo
import androidx.compose.ui.test.assertWidthIsEqualTo
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import io.github.joelkanyi.jenga.component.button.JengaButton
import io.github.joelkanyi.jenga.component.button.JengaButtonColors
import io.github.joelkanyi.jenga.component.button.JengaButtonDefaults
import io.github.joelkanyi.jenga.component.button.JengaButtonSizeStyle
import io.github.joelkanyi.jenga.component.button.JengaButtonStyle
import io.github.joelkanyi.jenga.component.button.JengaButtonVariant
import io.github.joelkanyi.jenga.component.button.JengaIconButton
import io.github.joelkanyi.jenga.component.button.JengaIconButtonStyle
import io.github.joelkanyi.jenga.component.button.JengaIconButtonVariant
import io.github.joelkanyi.jenga.component.button.JengaIconButtonVariantStyle
import io.github.joelkanyi.jenga.component.card.JengaCardDefaults
import io.github.joelkanyi.jenga.component.card.JengaCardStyle
import io.github.joelkanyi.jenga.component.chip.JengaChipDefaults
import io.github.joelkanyi.jenga.component.chip.JengaChipStyle
import io.github.joelkanyi.jenga.component.feedback.JengaDialogColors
import io.github.joelkanyi.jenga.component.feedback.JengaDialogDefaults
import io.github.joelkanyi.jenga.component.feedback.JengaDialogStyle
import io.github.joelkanyi.jenga.component.feedback.JengaSnackbarDefaults
import io.github.joelkanyi.jenga.component.feedback.JengaSnackbarStyle
import io.github.joelkanyi.jenga.component.list.JengaListItemDefaults
import io.github.joelkanyi.jenga.component.list.JengaListItemStyle
import io.github.joelkanyi.jenga.component.textfield.JengaTextField
import io.github.joelkanyi.jenga.component.textfield.JengaTextFieldDefaults
import io.github.joelkanyi.jenga.component.textfield.JengaTextFieldStyle
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotEquals
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class JengaComponentStylesTest {

    @get:Rule
    val compose = createComposeRule()

    private val outline = JengaButtonColors(
        container = Color.Transparent,
        content = Color.Red,
        border = Color.Blue,
        disabledContainer = Color.Transparent,
        disabledContent = Color.Gray,
        disabledBorder = Color.Gray,
    )
    private val dialogColors = JengaDialogColors(Color.White, Color.Black, Color.DarkGray)
    private val chipText = TextStyle(fontSize = 13.5.sp)
    private val listPadding = PaddingValues(horizontal = 16.dp, vertical = 12.dp)

    private val styles = JengaComponentStyles(
        button = JengaButtonStyle(
            medium = JengaButtonSizeStyle(minHeight = 50.dp, contentPadding = PaddingValues(horizontal = 18.dp)),
            outline = outline,
        ),
        iconButton = JengaIconButtonStyle(
            outlined = JengaIconButtonVariantStyle(size = 50.dp, shape = RoundedCornerShape(14.dp)),
        ),
        textField = JengaTextFieldStyle(
            minHeight = 46.dp,
            contentPadding = PaddingValues(horizontal = 14.dp),
            multilineMinHeight = 92.dp,
            focusRingWidth = 0.dp,
        ),
        listItem = JengaListItemStyle(minHeight = 64.dp, contentPadding = listPadding, contentSpacing = 14.dp),
        card = JengaCardStyle(disabledAlpha = 0.75f),
        chip = JengaChipStyle(minHeight = 34.dp, textStyle = chipText),
        dialog = JengaDialogStyle(actionSpacing = 10.dp, colors = dialogColors),
        snackbar = JengaSnackbarStyle(hostPadding = PaddingValues(horizontal = 16.dp, vertical = 10.dp)),
    )

    @Test
    fun defaultsFollowTheThemeStyle() {
        compose.setContent {
            JengaTheme(components = styles) {
                assertEquals(outline, JengaButtonDefaults.colors(JengaButtonVariant.Outline))
                assertEquals(46.dp, JengaTextFieldDefaults.minHeight)
                assertEquals(listPadding, JengaListItemDefaults.contentPadding)
                assertEquals(14.dp, JengaListItemDefaults.contentSpacing)
                assertEquals(chipText, JengaChipDefaults.textStyle)
                assertEquals(10.dp, JengaDialogDefaults.actionSpacing)
                assertEquals(dialogColors, JengaDialogDefaults.colors())
                assertEquals(PaddingValues(horizontal = 16.dp, vertical = 10.dp), JengaSnackbarDefaults.hostPadding)
            }
        }
        compose.waitForIdle()
    }

    @Test
    fun unsetValuesKeepJengaDefaults() {
        compose.setContent {
            JengaTheme(components = styles) {
                val primary = JengaButtonDefaults.colors(JengaButtonVariant.Primary)
                assertEquals(JengaTheme.colors.brand, primary.container)
                assertEquals(JengaTheme.shapes.card, JengaCardDefaults.shape)
                assertEquals(JengaTheme.typography.headingSmall, JengaDialogDefaults.titleStyle)
            }
            JengaTheme {
                assertNotEquals(outline, JengaButtonDefaults.colors(JengaButtonVariant.Outline))
                assertEquals(JengaTheme.sizing.fieldHeight, JengaTextFieldDefaults.minHeight)
            }
        }
        compose.waitForIdle()
    }

    @Test
    fun componentsMeasureWithTheThemeStyle() {
        compose.setContent {
            JengaTheme(components = styles) {
                JengaButton(text = "Save", onClick = {}, modifier = Modifier.testTag("medium"))
                JengaIconButton(
                    onClick = {},
                    variant = JengaIconButtonVariant.Outlined,
                    modifier = Modifier.testTag("outlined"),
                ) {}
                JengaIconButton(
                    onClick = {},
                    variant = JengaIconButtonVariant.Outlined,
                    size = 56.dp,
                    modifier = Modifier.testTag("perCall"),
                ) {}
            }
        }
        compose.onNodeWithTag("medium").assertHeightIsEqualTo(50.dp)
        compose.onNodeWithTag("outlined").assertWidthIsEqualTo(50.dp)
        compose.onNodeWithTag("perCall").assertWidthIsEqualTo(56.dp)
    }

    @Test
    fun multilineTextFieldTakesTheMultilineHeight() {
        compose.setContent {
            JengaTheme(components = styles) {
                JengaTextField(value = "", onValueChange = {}, modifier = Modifier.testTag("single"))
                JengaTextField(value = "", onValueChange = {}, singleLine = false, modifier = Modifier.testTag("multi"))
            }
        }
        compose.onNodeWithTag("single").assertHeightIsEqualTo(46.dp)
        compose.onNodeWithTag("multi").assertHeightIsEqualTo(92.dp)
    }
}
