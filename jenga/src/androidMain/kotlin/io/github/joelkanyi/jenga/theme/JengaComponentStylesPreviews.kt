package io.github.joelkanyi.jenga.theme

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import io.github.joelkanyi.jenga.component.button.JengaButton
import io.github.joelkanyi.jenga.component.button.JengaButtonColors
import io.github.joelkanyi.jenga.component.button.JengaButtonSize
import io.github.joelkanyi.jenga.component.button.JengaButtonSizeStyle
import io.github.joelkanyi.jenga.component.button.JengaButtonStyle
import io.github.joelkanyi.jenga.component.button.JengaButtonVariant
import io.github.joelkanyi.jenga.component.button.JengaIconButton
import io.github.joelkanyi.jenga.component.button.JengaIconButtonColors
import io.github.joelkanyi.jenga.component.button.JengaIconButtonStyle
import io.github.joelkanyi.jenga.component.button.JengaIconButtonVariant
import io.github.joelkanyi.jenga.component.button.JengaIconButtonVariantStyle
import io.github.joelkanyi.jenga.component.card.JengaCard
import io.github.joelkanyi.jenga.component.card.JengaCardVariant
import io.github.joelkanyi.jenga.component.chip.JengaChip
import io.github.joelkanyi.jenga.component.chip.JengaChipColors
import io.github.joelkanyi.jenga.component.chip.JengaChipStyle
import io.github.joelkanyi.jenga.component.feedback.JengaSnackbar
import io.github.joelkanyi.jenga.component.feedback.JengaSnackbarColors
import io.github.joelkanyi.jenga.component.feedback.JengaSnackbarStyle
import io.github.joelkanyi.jenga.component.icon.JengaIcon
import io.github.joelkanyi.jenga.component.layout.JengaInline
import io.github.joelkanyi.jenga.component.layout.JengaStack
import io.github.joelkanyi.jenga.component.list.JengaListItem
import io.github.joelkanyi.jenga.component.list.JengaListItemStyle
import io.github.joelkanyi.jenga.component.search.JengaSearchField
import io.github.joelkanyi.jenga.component.search.JengaSearchFieldStyle
import io.github.joelkanyi.jenga.component.textfield.JengaTextField
import io.github.joelkanyi.jenga.component.textfield.JengaTextFieldColors
import io.github.joelkanyi.jenga.component.textfield.JengaTextFieldStyle
import io.github.joelkanyi.jenga.core.preview.JengaBlockPreviews
import io.github.joelkanyi.jenga.foundation.color.JengaColors

// Plain component calls under a theme that carries component styles: the golden
// must differ from the default look in height, corners, fills and type.
@JengaBlockPreviews
@Composable
internal fun JengaComponentStylesPreview() {
    JengaTheme {
        JengaTheme(
            colors = JengaTheme.colors,
            typography = JengaTheme.typography,
            components = previewStyles(JengaTheme.colors),
        ) { StyledShowcase() }
    }
}

@Composable
private fun StyledShowcase() {
    JengaStack(
        modifier = Modifier
            .background(JengaTheme.colors.background)
            .padding(JengaTheme.spacing.lg),
        space = JengaTheme.spacing.md,
    ) {
        JengaInline(space = JengaTheme.spacing.sm) {
            JengaButton("Save", {})
            JengaButton("Cancel", {}, variant = JengaButtonVariant.Outline)
            JengaButton("Small", {}, size = JengaButtonSize.Small)
            JengaIconButton(onClick = {}, variant = JengaIconButtonVariant.Outlined) {
                JengaIcon(JengaTheme.icons.search, contentDescription = null)
            }
        }
        JengaTextField(value = "", onValueChange = {}, placeholder = "Customer name")
        JengaSearchField(value = "", onValueChange = {}, placeholder = "Search", clearContentDescription = "Clear")
        JengaCard(variant = JengaCardVariant.Outlined, contentPadding = PaddingValues(0.dp)) {
            JengaListItem(headline = "Warranty claim", supporting = "Opened 12 Oct")
        }
        JengaInline(space = JengaTheme.spacing.sm) {
            JengaChip(label = "Open", selected = true, onClick = {})
            JengaChip(label = "Closed", selected = false, onClick = {})
        }
        JengaSnackbar(message = "Claim saved", actionLabel = "Undo", onAction = {})
    }
}

@Composable
private fun previewStyles(c: JengaColors): JengaComponentStyles {
    val type = JengaTheme.typography
    return JengaComponentStyles(
        button = JengaButtonStyle(
            medium = JengaButtonSizeStyle(
                minHeight = 50.dp,
                shape = RoundedCornerShape(14.dp),
                contentPadding = PaddingValues(horizontal = 18.dp),
            ),
            small = JengaButtonSizeStyle(
                minHeight = 38.dp,
                shape = RoundedCornerShape(11.dp),
                contentPadding = PaddingValues(horizontal = 14.dp),
                textStyle = type.button.copy(fontSize = 13.5.sp),
            ),
            outline = JengaButtonColors(
                container = Color.Transparent,
                content = c.textPrimary,
                border = c.borderStrong,
                disabledContainer = Color.Transparent,
                disabledContent = c.contentDisabled,
                disabledBorder = c.borderDisabled,
            ),
        ),
        iconButton = JengaIconButtonStyle(
            outlined = JengaIconButtonVariantStyle(
                size = 50.dp,
                shape = RoundedCornerShape(14.dp),
                colors = JengaIconButtonColors(c.surface, c.textPrimary, c.surfaceDisabled, c.contentDisabled, c.border),
            ),
        ),
        textField = JengaTextFieldStyle(
            shape = RoundedCornerShape(12.dp),
            minHeight = 46.dp,
            contentPadding = PaddingValues(horizontal = 14.dp),
            focusRingWidth = 0.dp,
            colors = JengaTextFieldColors(
                container = c.surfaceSunk,
                focusedContainer = c.surface,
                disabledContainer = c.surfaceDisabled,
                border = c.border,
                focusedBorder = c.brand,
                disabledBorder = c.borderDisabled,
                focusRing = c.focusRing,
                text = c.textPrimary,
                disabledText = c.contentDisabled,
                placeholder = c.textFaint,
                icon = c.textMuted,
            ),
        ),
        searchField = JengaSearchFieldStyle(
            shape = RoundedCornerShape(12.dp),
            contentPadding = PaddingValues(start = 14.dp, end = 4.dp),
        ),
        listItem = JengaListItemStyle(
            minHeight = 64.dp,
            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 12.dp),
            contentSpacing = 14.dp,
        ),
        chip = JengaChipStyle(
            minHeight = 34.dp,
            textStyle = type.bodySmall.copy(fontSize = 13.5.sp),
            selectedTextStyle = type.bodySmall.copy(fontSize = 13.5.sp, fontWeight = FontWeight.Medium),
            colors = JengaChipColors(
                selectedContainer = c.brandSubtle,
                selectedContent = c.brand,
                unselectedContainer = c.surface,
                unselectedContent = c.textSecondary,
                unselectedBorder = c.borderStrong,
                disabledContainer = c.surfaceDisabled,
                disabledContent = c.contentDisabled,
            ),
        ),
        snackbar = JengaSnackbarStyle(
            shape = RoundedCornerShape(12.dp),
            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 13.dp),
            messageStyle = type.bodyMedium,
            actionStyle = type.label.copy(fontWeight = FontWeight.SemiBold),
            colors = JengaSnackbarColors(c.inverseSurface, c.inverseOnSurface, c.brand),
        ),
    )
}
