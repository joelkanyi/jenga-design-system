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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import io.github.joelkanyi.jenga.component.avatar.JengaAvatar
import io.github.joelkanyi.jenga.component.avatar.JengaAvatarSizeStyle
import io.github.joelkanyi.jenga.component.avatar.JengaAvatarStyle
import io.github.joelkanyi.jenga.component.banner.JengaInfoBarDefaults
import io.github.joelkanyi.jenga.component.banner.JengaInfoBarStyle
import io.github.joelkanyi.jenga.component.button.JengaButton
import io.github.joelkanyi.jenga.component.button.JengaButtonColors
import io.github.joelkanyi.jenga.component.button.JengaButtonDefaults
import io.github.joelkanyi.jenga.component.button.JengaButtonSize
import io.github.joelkanyi.jenga.component.button.JengaButtonSizeStyle
import io.github.joelkanyi.jenga.component.button.JengaButtonStyle
import io.github.joelkanyi.jenga.component.button.JengaButtonVariant
import io.github.joelkanyi.jenga.component.button.JengaIconButton
import io.github.joelkanyi.jenga.component.button.JengaIconButtonStyle
import io.github.joelkanyi.jenga.component.button.JengaIconButtonVariant
import io.github.joelkanyi.jenga.component.button.JengaIconButtonVariantStyle
import io.github.joelkanyi.jenga.component.card.JengaCardDefaults
import io.github.joelkanyi.jenga.component.card.JengaCardFooterDefaults
import io.github.joelkanyi.jenga.component.card.JengaCardFooterStyle
import io.github.joelkanyi.jenga.component.card.JengaCardStyle
import io.github.joelkanyi.jenga.component.chip.JengaChipDefaults
import io.github.joelkanyi.jenga.component.chip.JengaChipStyle
import io.github.joelkanyi.jenga.component.fab.JengaExtendedFab
import io.github.joelkanyi.jenga.component.fab.JengaFabStyle
import io.github.joelkanyi.jenga.component.feedback.JengaDialogColors
import io.github.joelkanyi.jenga.component.feedback.JengaDialogDefaults
import io.github.joelkanyi.jenga.component.feedback.JengaDialogStyle
import io.github.joelkanyi.jenga.component.feedback.JengaListSheetDefaults
import io.github.joelkanyi.jenga.component.feedback.JengaListSheetStyle
import io.github.joelkanyi.jenga.component.feedback.JengaSnackbarDefaults
import io.github.joelkanyi.jenga.component.feedback.JengaSnackbarStyle
import io.github.joelkanyi.jenga.component.icon.JengaIconTile
import io.github.joelkanyi.jenga.component.icon.JengaIconTileSizeStyle
import io.github.joelkanyi.jenga.component.icon.JengaIconTileStyle
import io.github.joelkanyi.jenga.component.link.JengaLinkDefaults
import io.github.joelkanyi.jenga.component.link.JengaLinkStyle
import io.github.joelkanyi.jenga.component.list.JengaKeyValueRow
import io.github.joelkanyi.jenga.component.list.JengaKeyValueRowStyle
import io.github.joelkanyi.jenga.component.list.JengaListItemDefaults
import io.github.joelkanyi.jenga.component.list.JengaListItemStyle
import io.github.joelkanyi.jenga.component.list.JengaSelectionListItemDefaults
import io.github.joelkanyi.jenga.component.list.JengaSelectionListItemStyle
import io.github.joelkanyi.jenga.component.menu.JengaDropdownMenuStyle
import io.github.joelkanyi.jenga.component.progress.JengaCircularProgressIndeterminate
import io.github.joelkanyi.jenga.component.progress.JengaCircularProgressStyle
import io.github.joelkanyi.jenga.component.progress.JengaLinearProgress
import io.github.joelkanyi.jenga.component.progress.JengaProgressStyle
import io.github.joelkanyi.jenga.component.scaffold.JengaTopAppBarDefaults
import io.github.joelkanyi.jenga.component.scaffold.JengaTopAppBarStyle
import io.github.joelkanyi.jenga.component.shelf.JengaThumbnailStripStyle
import io.github.joelkanyi.jenga.component.state.JengaStateDefaults
import io.github.joelkanyi.jenga.component.state.JengaStateStyle
import io.github.joelkanyi.jenga.component.status.JengaStatusPillStyle
import io.github.joelkanyi.jenga.component.stepper.JengaStepperStyle
import io.github.joelkanyi.jenga.component.tabs.JengaSegmentedControl
import io.github.joelkanyi.jenga.component.tabs.JengaSegmentedControlStyle
import io.github.joelkanyi.jenga.component.textfield.JengaTextField
import io.github.joelkanyi.jenga.component.textfield.JengaTextFieldDefaults
import io.github.joelkanyi.jenga.component.textfield.JengaTextFieldStyle
import io.github.joelkanyi.jenga.pattern.JengaSectionHeader
import io.github.joelkanyi.jenga.pattern.JengaSectionHeaderStyle
import io.github.joelkanyi.jenga.pattern.JengaSectionHeaderStyles
import io.github.joelkanyi.jenga.pattern.JengaSectionHeaderVariantStyle
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotEquals
import org.junit.Assert.assertTrue
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

    private val phase2 = JengaComponentStyles(
        listItem = JengaListItemStyle(minHeight = 64.dp),
        sectionHeader = JengaSectionHeaderStyles(
            overline = JengaSectionHeaderVariantStyle(minHeight = 60.dp, contentPadding = PaddingValues(0.dp)),
        ),
        progress = JengaProgressStyle(trackHeight = 4.dp),
        selectionListItem = JengaSelectionListItemStyle(contentPadding = PaddingValues(0.dp), radioSize = 20.dp),
        keyValueRow = JengaKeyValueRowStyle(minHeight = 50.dp, contentPadding = PaddingValues(0.dp)),
        state = JengaStateStyle(spacing = 8.dp, actionSize = JengaButtonSize.Small),
        iconTile = JengaIconTileStyle(medium = JengaIconTileSizeStyle(tileSize = 40.dp, iconSize = 21.dp)),
        listSheet = JengaListSheetStyle(subtitleColor = Color.Magenta, footerSpacing = 10.dp),
        link = JengaLinkStyle(underline = false),
        segmentedControl = JengaSegmentedControlStyle(
            segmentMinHeight = 38.dp,
            trackPadding = 3.dp,
            selectedTextStyle = TextStyle(fontWeight = FontWeight.SemiBold),
        ),
        topAppBar = JengaTopAppBarStyle(height = 56.dp, titleStyle = chipText),
        chip = JengaChipStyle(selectedTextStyle = TextStyle(fontWeight = FontWeight.Medium)),
    )

    @Test
    fun secondBatchDefaultsFollowTheThemeStyle() {
        compose.setContent {
            JengaTheme(components = phase2) {
                assertEquals(64.dp, JengaSelectionListItemDefaults.minHeight)
                assertEquals(20.dp, JengaSelectionListItemDefaults.radioSize)
                assertEquals(JengaButtonSize.Small, JengaStateDefaults.actionSize)
                assertEquals(8.dp, JengaStateDefaults.spacing)
                assertEquals(Color.Magenta, JengaListSheetDefaults.subtitleColor)
                assertEquals(10.dp, JengaListSheetDefaults.footerSpacing)
                assertEquals(false, JengaLinkDefaults.underline)
                assertEquals(chipText, JengaTopAppBarDefaults.titleStyle)
            }
            JengaTheme {
                assertEquals(JengaListItemDefaults.MinHeight, JengaSelectionListItemDefaults.minHeight)
                assertEquals(true, JengaLinkDefaults.underline)
            }
        }
        compose.waitForIdle()
    }

    @Test
    fun secondBatchComponentsMeasureWithTheThemeStyle() {
        compose.setContent {
            JengaTheme(components = phase2) {
                JengaSectionHeader(
                    title = "Customer",
                    style = JengaSectionHeaderStyle.Overline,
                    modifier = Modifier.testTag("overline"),
                )
                JengaLinearProgress(progress = 0.5f, modifier = Modifier.testTag("progress"))
                JengaKeyValueRow(label = "Plan", value = "Monthly", modifier = Modifier.testTag("kv"))
                JengaIconTile(icon = JengaTheme.icons.search, contentDescription = null, modifier = Modifier.testTag("tile"))
                JengaSegmentedControl(
                    selectedIndex = 0,
                    segments = listOf("Yes", "No"),
                    onSelect = {},
                    modifier = Modifier.testTag("segmented"),
                )
            }
        }
        compose.onNodeWithTag("overline").assertHeightIsEqualTo(60.dp)
        compose.onNodeWithTag("progress").assertHeightIsEqualTo(4.dp)
        compose.onNodeWithTag("kv").assertHeightIsEqualTo(50.dp)
        compose.onNodeWithTag("tile").assertWidthIsEqualTo(40.dp)
        compose.onNodeWithTag("segmented").assertHeightIsEqualTo(44.dp)
    }

    @Test
    fun selectedStylesMergeOntoTheCallTextStyle() {
        compose.setContent {
            JengaTheme(components = phase2) {
                val call = TextStyle(fontSize = 18.sp)
                val merged = call.merge(TextStyle(fontWeight = FontWeight.Medium))
                assertEquals(merged, call.merge(JengaTheme.components.chip?.selectedTextStyle))
            }
        }
        compose.waitForIdle()
    }

    @Test
    fun formatRemainingHandlesInfinity() {
        assertTrue(
            io.github.joelkanyi.jenga.component.otp.JengaOtpResendDefaults
                .formatRemaining(kotlin.time.Duration.INFINITE)
                .first()
                .isDigit(),
        )
    }

    private val phase2b = JengaComponentStyles(
        button = JengaButtonStyle(medium = JengaButtonSizeStyle(minHeight = 50.dp)),
        statusPill = JengaStatusPillStyle(indicatorSize = 6.dp),
        avatar = JengaAvatarStyle(medium = JengaAvatarSizeStyle(diameter = 42.dp)),
        circularProgress = JengaCircularProgressStyle(size = 18.dp, strokeWidth = 2.dp),
        cardFooter = JengaCardFooterStyle(textStyle = chipText),
        stepper = JengaStepperStyle(buttonSize = 46.dp),
        dropdownMenu = JengaDropdownMenuStyle(itemMinHeight = 46.dp),
        fab = JengaFabStyle(extendedHeight = 52.dp),
        thumbnailStrip = JengaThumbnailStripStyle(thumbnailSize = 84.dp),
    )

    @Test
    fun thirdBatchFollowsTheThemeStyle() {
        compose.setContent {
            JengaTheme(components = phase2b) {
                assertEquals(50.dp, JengaInfoBarDefaults.minHeight)
                assertEquals(chipText, JengaCardFooterDefaults.textStyle)
                JengaAvatar(name = "Amina Otieno", modifier = Modifier.testTag("avatar"))
                JengaCircularProgressIndeterminate(modifier = Modifier.testTag("spinner"))
                JengaExtendedFab(label = "New", onClick = {}, modifier = Modifier.testTag("fab"))
            }
        }
        compose.onNodeWithTag("avatar").assertWidthIsEqualTo(42.dp)
        compose.onNodeWithTag("spinner").assertWidthIsEqualTo(18.dp)
        compose.onNodeWithTag("fab").assertHeightIsEqualTo(52.dp)
    }

    @Test
    fun infoBarStyleWinsOverTheButtonStyle() {
        compose.setContent {
            JengaTheme(components = phase2b.copy(infoBar = JengaInfoBarStyle(minHeight = 60.dp))) {
                assertEquals(60.dp, JengaInfoBarDefaults.minHeight)
            }
        }
        compose.waitForIdle()
    }
}
