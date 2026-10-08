package io.github.joelkanyi.jenga.component.list

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import io.github.joelkanyi.jenga.component.text.JengaText
import io.github.joelkanyi.jenga.core.preview.JengaBlockPreviews
import io.github.joelkanyi.jenga.core.preview.RtlPreview
import io.github.joelkanyi.jenga.theme.JengaTheme

// ---- Previews --------------------------------------------------------------

@JengaBlockPreviews
@Composable
internal fun JengaSelectionListItemsPreview() {
    JengaTheme { SelectionListItemsShowcase() }
}

@Preview(name = "RTL", showBackground = true)
@Composable
internal fun JengaSelectionListItemsRtlPreview() {
    JengaTheme { RtlPreview { SelectionListItemsShowcase() } }
}

@Composable
private fun SelectionListItemsShowcase() {
    Column(modifier = Modifier.background(JengaTheme.colors.surface)) {
        JengaRadioListItem(headline = "Repair", selected = true, onClick = {}, supporting = "Fix the unit on site")
        JengaRadioListItem(headline = "Replace", selected = false, onClick = {})
        JengaRadioListItem(headline = "Refund", selected = false, onClick = {}, enabled = false)
        JengaCheckboxListItem(headline = "Battery", checked = true, onCheckedChange = {}, supporting = "SN-0042-7781")
        JengaCheckboxListItem(headline = "Charge controller", checked = false, onCheckedChange = {})
        JengaCheckboxListItem(headline = "Panel", checked = true, onCheckedChange = {}, enabled = false)
    }
}

@JengaBlockPreviews
@Composable
internal fun JengaSelectionListItemsTrailingPreview() {
    JengaTheme {
        Column(modifier = Modifier.background(JengaTheme.colors.surface)) {
            JengaCheckboxListItem(
                headline = "Replace the fuse",
                checked = true,
                onCheckedChange = {},
                supporting = "KES 350",
                controlPosition = JengaControlPosition.Trailing,
            )
            JengaRadioListItem(
                headline = "Pick up at the shop",
                selected = false,
                onClick = {},
                controlPosition = JengaControlPosition.Trailing,
            )
        }
    }
}

@JengaBlockPreviews
@Composable
internal fun JengaSelectionListItemsCountPreview() {
    JengaTheme {
        Column(modifier = Modifier.background(JengaTheme.colors.surface)) {
            JengaRadioListItem(
                headline = "Open",
                selected = true,
                onClick = {},
                trailingContent = { JengaText("6", style = JengaTheme.typography.bodySmall) },
            )
            JengaCheckboxListItem(
                headline = "Waiting for parts",
                checked = false,
                onCheckedChange = {},
                controlPosition = JengaControlPosition.Trailing,
                trailingContent = { JengaText("3", style = JengaTheme.typography.bodySmall) },
            )
        }
    }
}

@JengaBlockPreviews
@Composable
internal fun JengaSelectionListItemsCompactPreview() {
    JengaTheme {
        Column(modifier = Modifier.background(JengaTheme.colors.surface)) {
            JengaRadioListItem(
                headline = "Open",
                selected = true,
                onClick = {},
                supporting = "Waiting for a technician",
                minHeight = 0.dp,
                contentPadding = PaddingValues(horizontal = 2.dp, vertical = 13.dp),
                contentSpacing = 14.dp,
                headlineStyle = JengaTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Normal),
                supportingStyle = JengaTheme.typography.caption,
                radioSize = 20.dp,
            )
            JengaRadioListItem(
                headline = "Closed",
                selected = false,
                onClick = {},
                minHeight = 0.dp,
                contentPadding = PaddingValues(horizontal = 2.dp, vertical = 13.dp),
                contentSpacing = 14.dp,
                headlineStyle = JengaTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Normal),
                radioSize = 20.dp,
            )
        }
    }
}
