package io.github.joelkanyi.jenga.component.timeline

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
internal fun JengaTimelinePreview() {
    JengaTheme { JengaTimelineShowcase() }
}

@Preview(name = "RTL", showBackground = true)
@Composable
internal fun JengaTimelineRtlPreview() {
    JengaTheme { RtlPreview { JengaTimelineShowcase() } }
}

@Composable
private fun JengaTimelineShowcase() {
    Column(modifier = Modifier.background(JengaTheme.colors.surface).padding(JengaTheme.spacing.lg)) {
        JengaTimelineItem(JengaTimelineState.Done, "Lead created", JengaTimelinePosition.First, subtitle = "12 Mar, Amina")
        JengaTimelineItem(JengaTimelineState.Done, "Site survey", JengaTimelinePosition.Middle, subtitle = "14 Mar")
        JengaTimelineItem(JengaTimelineState.Current, "Quotation", JengaTimelinePosition.Middle, subtitle = "In progress")
        JengaTimelineItem(JengaTimelineState.ToDo, "Installation", JengaTimelinePosition.Middle)
        JengaTimelineItem(JengaTimelineState.Stopped, "Payment", JengaTimelinePosition.Last, subtitle = "Customer cancelled")
    }
}
