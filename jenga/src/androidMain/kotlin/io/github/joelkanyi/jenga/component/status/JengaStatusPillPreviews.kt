package io.github.joelkanyi.jenga.component.status

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import io.github.joelkanyi.jenga.component.badge.JengaBadgeTone
import io.github.joelkanyi.jenga.component.layout.JengaInline
import io.github.joelkanyi.jenga.core.preview.JengaBlockPreviews
import io.github.joelkanyi.jenga.core.preview.RtlPreview
import io.github.joelkanyi.jenga.theme.JengaTheme

// ---- Previews --------------------------------------------------------------

@JengaBlockPreviews
@Composable
internal fun JengaStatusPillPreview() {
    JengaTheme { StatusPillShowcase() }
}

@Preview(name = "RTL", showBackground = true)
@Composable
internal fun JengaStatusPillRtlPreview() {
    JengaTheme { RtlPreview { StatusPillShowcase() } }
}

@Composable
private fun StatusPillShowcase() {
    androidx.compose.foundation.layout.Column(
        modifier = Modifier
            .background(JengaTheme.colors.background)
            .padding(JengaTheme.spacing.xl),
        verticalArrangement = Arrangement.spacedBy(JengaTheme.spacing.sm),
    ) {
        JengaStatusPill(label = "Synced", tone = JengaBadgeTone.Success)
        JengaStatusPill(label = "3 pending", tone = JengaBadgeTone.Warning, loading = true)
        JengaStatusPill(label = "Offline", tone = JengaBadgeTone.Error)
        JengaStatusPill(label = "Offline mode", tone = JengaBadgeTone.Info)
    }
}

@JengaBlockPreviews
@Composable
internal fun JengaStatusPillNoDotPreview() {
    JengaTheme {
        JengaInline(modifier = Modifier.background(JengaTheme.colors.background).padding(JengaTheme.spacing.lg)) {
            JengaStatusPill(label = "In warranty", tone = JengaBadgeTone.Success, showDot = false)
            JengaStatusPill(label = "Waiting for parts", tone = JengaBadgeTone.Brand, indicatorSize = JengaTheme.spacing.xs)
        }
    }
}
