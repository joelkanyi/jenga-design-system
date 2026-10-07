package io.github.joelkanyi.jenga.component.tabs

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
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
internal fun JengaTabsPreview() {
    JengaTheme { TabsShowcase() }
}

@Preview(name = "RTL", showBackground = true)
@Composable
internal fun JengaTabsRtlPreview() {
    JengaTheme { RtlPreview { TabsShowcase() } }
}

@Composable
private fun TabsShowcase() {
    Column(
        modifier = Modifier
            .background(JengaTheme.colors.surface)
            .padding(vertical = JengaTheme.spacing.md),
        verticalArrangement = Arrangement.spacedBy(JengaTheme.spacing.md),
    ) {
        JengaTabs(selectedIndex = 0, tabs = listOf("Upcoming", "Live", "Past"), onSelect = {})
        JengaTabs(selectedIndex = 1, tabs = listOf("Upcoming", "Live", "Past"), onSelect = {})
    }
}

@JengaBlockPreviews
@Composable
internal fun JengaTabsStartPreview() {
    JengaTheme {
        JengaTabs(
            selectedIndex = 0,
            tabs = listOf("Details", "Parts", "History"),
            onSelect = {},
            modifier = Modifier.background(JengaTheme.colors.background),
            arrangement = JengaTabsArrangement.Start,
            dotted = setOf(1),
        )
    }
}
