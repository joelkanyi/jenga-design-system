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
internal fun JengaSegmentedControlPreview() {
    JengaTheme { SegmentedShowcase() }
}

@Preview(name = "RTL", showBackground = true)
@Composable
internal fun JengaSegmentedControlRtlPreview() {
    JengaTheme { RtlPreview { SegmentedShowcase() } }
}

@Composable
private fun SegmentedShowcase() {
    Column(
        modifier = Modifier
            .background(JengaTheme.colors.background)
            .padding(JengaTheme.spacing.xl),
        verticalArrangement = Arrangement.spacedBy(JengaTheme.spacing.md),
    ) {
        JengaSegmentedControl(selectedIndex = 0, segments = listOf("Day", "Week", "Month"), onSelect = {})
        JengaSegmentedControl(selectedIndex = 2, segments = listOf("Day", "Week", "Month"), onSelect = {})
    }
}

@JengaBlockPreviews
@Composable
internal fun JengaSegmentedControlStatesPreview() {
    JengaTheme {
        Column(
            modifier = Modifier.background(JengaTheme.colors.background).padding(JengaTheme.spacing.lg),
            verticalArrangement = Arrangement.spacedBy(JengaTheme.spacing.md),
        ) {
            JengaSegmentedControl(selectedIndex = null, segments = listOf("Yes", "No"), onSelect = {})
            JengaSegmentedControl(
                selectedIndex = 0,
                segments = listOf("New", "Used good", "Faulty"),
                onSelect = {},
                isEnabled = { it != 2 },
                shape = JengaTheme.shapes.md,
                segmentShape = JengaTheme.shapes.control,
            )
        }
    }
}
