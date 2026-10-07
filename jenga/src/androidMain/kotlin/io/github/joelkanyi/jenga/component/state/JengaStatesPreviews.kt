package io.github.joelkanyi.jenga.component.state

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import io.github.joelkanyi.jenga.component.button.JengaButtonSize
import io.github.joelkanyi.jenga.component.icon.JengaIcon
import io.github.joelkanyi.jenga.component.icon.JengaIcons
import io.github.joelkanyi.jenga.core.preview.JengaBlockPreviews
import io.github.joelkanyi.jenga.core.preview.RtlPreview
import io.github.joelkanyi.jenga.theme.JengaTheme

// ---- Previews --------------------------------------------------------------

@JengaBlockPreviews
@Composable
internal fun JengaStatesPreview() {
    JengaTheme { StatesShowcase() }
}

@Preview(name = "RTL", showBackground = true)
@Composable
internal fun JengaStatesRtlPreview() {
    JengaTheme { RtlPreview { StatesShowcase() } }
}

@Composable
private fun StatesShowcase() {
    Column(
        modifier = Modifier
            .background(JengaTheme.colors.background)
            .padding(JengaTheme.spacing.lg),
        verticalArrangement = Arrangement.spacedBy(JengaTheme.spacing.xxl),
    ) {
        JengaEmptyState(
            title = "No scans yet",
            description = "Validated tickets will appear here as attendees check in.",
            actionLabel = "Scan a ticket",
            onAction = {},
        )
        JengaErrorState(
            title = "Couldn't load gates",
            description = "Check your connection and try again.",
            actionLabel = "Retry",
            onAction = {},
        )
    }
}

@JengaBlockPreviews
@Composable
internal fun JengaEmptyStateStylesPreview() {
    JengaTheme {
        JengaEmptyState(
            title = "No work orders yet",
            modifier = Modifier.background(JengaTheme.colors.background),
            description = "New work orders show up here.",
            icon = { JengaIcon(JengaIcons.Package, contentDescription = null, size = 40.dp) },
            actionLabel = "Refresh",
            onAction = {},
            titleStyle = JengaTheme.typography.titleMedium,
            descriptionStyle = JengaTheme.typography.bodySmall,
            contentPadding = PaddingValues(horizontal = 32.dp, vertical = 40.dp),
            spacing = 8.dp,
            actionSpacing = 10.dp,
            actionSize = JengaButtonSize.Small,
        )
    }
}
