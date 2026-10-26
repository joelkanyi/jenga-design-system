package io.github.joelkanyi.jenga.component.timeline

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import io.github.joelkanyi.jenga.component.card.JengaCard
import io.github.joelkanyi.jenga.component.card.JengaCardVariant
import io.github.joelkanyi.jenga.component.text.JengaText
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

@JengaBlockPreviews
@Composable
internal fun JengaTimelineNumberedStepsPreview() {
    JengaTheme {
        val steps = listOf("Visit the customer", "Collect the deposit", "Install the system")
        val stepColors = listOf(JengaTheme.colors.success, JengaTheme.colors.warning, JengaTheme.colors.borderStrong)
        Column(modifier = Modifier.background(JengaTheme.colors.surface).padding(JengaTheme.spacing.lg)) {
            steps.forEachIndexed { index, step ->
                JengaTimelineItem(
                    position = JengaTimelinePosition.of(index, steps.size),
                    marker = {
                        JengaTimelineMarker(color = stepColors[index], size = 32.dp) {
                            JengaText(text = "${index + 1}", style = JengaTheme.typography.label)
                        }
                    },
                    lineColor = stepColors[index],
                    markerSize = 32.dp,
                ) {
                    JengaCard(variant = JengaCardVariant.Outlined, modifier = Modifier.fillMaxWidth()) { JengaText(step) }
                }
            }
        }
    }
}

@JengaBlockPreviews
@Composable
internal fun JengaTimelineEventLogPreview() {
    JengaTheme {
        Column(modifier = Modifier.background(JengaTheme.colors.surface).padding(JengaTheme.spacing.lg)) {
            JengaTimelineItem(
                position = JengaTimelinePosition.First,
                marker = { JengaTimelineMarker(color = JengaTheme.colors.brand, style = JengaTimelineMarkerStyle.Ring) },
            ) {
                JengaText("08 MAY 2026 · 05:31", style = JengaTheme.typography.caption)
                JengaText("New number added", modifier = Modifier.padding(top = JengaTheme.spacing.lg))
            }
            JengaTimelineItem(
                position = JengaTimelinePosition.Last,
                marker = { JengaTimelineMarker(color = JengaTheme.colors.borderStrong, style = JengaTimelineMarkerStyle.Ring) },
            ) {
                JengaText("End of recent activities", color = JengaTheme.colors.textMuted)
            }
        }
    }
}
