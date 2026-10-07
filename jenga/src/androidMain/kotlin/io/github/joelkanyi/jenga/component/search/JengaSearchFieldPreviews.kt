package io.github.joelkanyi.jenga.component.search

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import io.github.joelkanyi.jenga.component.icon.JengaIcon
import io.github.joelkanyi.jenga.component.icon.JengaIcons
import io.github.joelkanyi.jenga.component.text.JengaText
import io.github.joelkanyi.jenga.core.preview.JengaBlockPreviews
import io.github.joelkanyi.jenga.core.preview.RtlPreview
import io.github.joelkanyi.jenga.theme.JengaTheme

// ---- Previews --------------------------------------------------------------

@JengaBlockPreviews
@Composable
internal fun JengaSearchFieldPreview() {
    JengaTheme { SearchShowcase() }
}

@Preview(name = "RTL", showBackground = true)
@Composable
internal fun JengaSearchFieldRtlPreview() {
    JengaTheme { RtlPreview { SearchShowcase() } }
}

@Composable
private fun SearchShowcase() {
    Column(
        modifier = Modifier
            .background(JengaTheme.colors.background)
            .padding(JengaTheme.spacing.xl),
        verticalArrangement = androidx.compose.foundation.layout.Arrangement.spacedBy(JengaTheme.spacing.md),
    ) {
        JengaSearchField(value = "", onValueChange = {}, placeholder = "Search", clearContentDescription = "Clear search")
        JengaSearchField(value = "Lovelace", onValueChange = {}, placeholder = "Search", clearContentDescription = "Clear search")
    }
}

@JengaBlockPreviews
@Composable
internal fun JengaSearchFieldSlotsPreview() {
    JengaTheme {
        Column(modifier = Modifier.background(JengaTheme.colors.background).padding(JengaTheme.spacing.lg)) {
            JengaSearchField(
                value = "",
                onValueChange = {},
                placeholder = "Serial number or phone",
                clearContentDescription = "Clear search",
                trailingContent = { JengaIcon(JengaIcons.QrCode, contentDescription = "Scan") },
                supportingContent = { JengaText("Search by serial, phone or ticket number", style = JengaTheme.typography.caption) },
                shape = JengaTheme.shapes.md,
            )
            JengaSearchTrigger(
                placeholder = "Search tickets, work orders, parts",
                onClick = {},
                modifier = Modifier.padding(top = JengaTheme.spacing.lg),
                trailingContent = { JengaIcon(JengaIcons.QrCode, contentDescription = "Scan") },
                shape = JengaTheme.shapes.md,
            )
        }
    }
}
