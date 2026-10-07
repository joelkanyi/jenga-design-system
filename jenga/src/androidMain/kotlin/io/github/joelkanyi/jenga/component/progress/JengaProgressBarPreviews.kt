package io.github.joelkanyi.jenga.component.progress

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import io.github.joelkanyi.jenga.core.preview.JengaBlockPreviews
import io.github.joelkanyi.jenga.core.preview.RtlPreview
import io.github.joelkanyi.jenga.theme.JengaTheme

// ---- Previews --------------------------------------------------------------

@JengaBlockPreviews
@Composable
internal fun JengaProgressPreview() {
    JengaTheme { ProgressShowcase() }
}

@Preview(name = "RTL", showBackground = true)
@Composable
internal fun JengaProgressRtlPreview() {
    JengaTheme { RtlPreview { ProgressShowcase() } }
}

@Composable
private fun ProgressShowcase() {
    Column(
        modifier = Modifier
            .background(JengaTheme.colors.background)
            .padding(JengaTheme.spacing.xl),
        verticalArrangement = Arrangement.spacedBy(JengaTheme.spacing.lg),
    ) {
        JengaLinearProgress(progress = 0.25f)
        JengaLinearProgress(progress = 0.6f)
        JengaLinearProgress(progress = 1f)
        JengaLinearProgressIndeterminate()
    }
}

@JengaBlockPreviews
@Composable
internal fun JengaLabelledProgressPreview() {
    JengaTheme {
        Column(
            modifier = Modifier.background(JengaTheme.colors.surface).padding(JengaTheme.spacing.lg),
            verticalArrangement = Arrangement.spacedBy(JengaTheme.spacing.lg),
        ) {
            JengaLabelledProgress(
                progress = 0.6f,
                label = buildAnnotatedString {
                    withStyle(SpanStyle(fontWeight = FontWeight.Medium)) { append("Repair") }
                    append(" · step 3 of 5")
                },
                valueLabel = "60%",
            )
            JengaLabelledProgress(progress = 1f, label = buildAnnotatedString { append("Done") }, tone = JengaProgressTone.Success)
            JengaLinearProgress(progress = 0.4f, tone = JengaProgressTone.Neutral, trackHeight = JengaTheme.spacing.xs)
        }
    }
}
