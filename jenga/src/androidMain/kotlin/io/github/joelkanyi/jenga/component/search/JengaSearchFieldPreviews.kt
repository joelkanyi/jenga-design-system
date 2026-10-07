package io.github.joelkanyi.jenga.component.search

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
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
