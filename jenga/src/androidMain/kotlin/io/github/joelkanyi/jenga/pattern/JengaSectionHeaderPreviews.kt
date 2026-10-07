package io.github.joelkanyi.jenga.pattern

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import io.github.joelkanyi.jenga.component.text.JengaText
import io.github.joelkanyi.jenga.core.preview.JengaBlockPreviews
import io.github.joelkanyi.jenga.core.preview.RtlPreview
import io.github.joelkanyi.jenga.theme.JengaTheme

// ---- Previews --------------------------------------------------------------

@JengaBlockPreviews
@Composable
internal fun JengaSectionHeaderPreview() {
    JengaTheme { SectionHeaderShowcase() }
}

@Preview(name = "RTL", showBackground = true)
@Composable
internal fun JengaSectionHeaderRtlPreview() {
    JengaTheme { RtlPreview { SectionHeaderShowcase() } }
}

@Composable
private fun SectionHeaderShowcase() {
    Column(
        modifier = Modifier
            .background(JengaTheme.colors.background)
            .padding(JengaTheme.spacing.lg),
    ) {
        JengaSectionHeader(
            title = "Recent scans",
            subtitle = "Last 24 hours",
            actionLabel = "See all",
            onActionClick = {},
        )
        JengaSectionHeader(title = "Gates")
    }
}

@JengaBlockPreviews
@Composable
internal fun JengaSectionHeaderOverlinePreview() {
    JengaTheme {
        Column(modifier = Modifier.background(JengaTheme.colors.background).padding(JengaTheme.spacing.lg)) {
            JengaSectionHeader(
                title = "Other repairs",
                style = JengaSectionHeaderStyle.Overline,
                actionLabel = "Add",
                onActionClick = {},
            )
            JengaSectionHeader(
                title = "Parts used",
                style = JengaSectionHeaderStyle.Overline,
                trailingContent = { JengaText("3", style = JengaTheme.typography.bodySmall) },
            )
        }
    }
}
