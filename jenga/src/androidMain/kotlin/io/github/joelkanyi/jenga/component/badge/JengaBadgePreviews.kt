package io.github.joelkanyi.jenga.component.badge

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import io.github.joelkanyi.jenga.component.layout.JengaInline
import io.github.joelkanyi.jenga.core.preview.JengaBlockPreviews
import io.github.joelkanyi.jenga.core.preview.RtlPreview
import io.github.joelkanyi.jenga.theme.JengaTheme

// ---- Previews --------------------------------------------------------------

@JengaBlockPreviews
@Composable
internal fun JengaBadgePreview() {
    JengaTheme { BadgeShowcase() }
}

@Preview(name = "RTL", showBackground = true)
@Composable
internal fun JengaBadgeRtlPreview() {
    JengaTheme { RtlPreview { BadgeShowcase() } }
}

@Composable
private fun BadgeShowcase() {
    androidx.compose.foundation.layout.FlowRow(
        modifier = Modifier
            .background(JengaTheme.colors.background)
            .padding(JengaTheme.spacing.xl),
        horizontalArrangement = Arrangement.spacedBy(JengaTheme.spacing.sm),
        verticalArrangement = Arrangement.spacedBy(JengaTheme.spacing.sm),
    ) {
        JengaBadge(text = "Neutral", tone = JengaBadgeTone.Neutral)
        JengaBadge(text = "Brand", tone = JengaBadgeTone.Brand)
        JengaBadge(text = "Valid", tone = JengaBadgeTone.Success)
        JengaBadge(text = "Pending", tone = JengaBadgeTone.Warning)
        JengaBadge(text = "Denied", tone = JengaBadgeTone.Error)
        JengaBadge(text = "Info", tone = JengaBadgeTone.Info)
    }
}

@JengaBlockPreviews
@Composable
internal fun JengaBadgeAsWrittenPreview() {
    JengaTheme {
        JengaInline(modifier = Modifier.background(JengaTheme.colors.background).padding(JengaTheme.spacing.lg)) {
            JengaBadge(text = "6d late", tone = JengaBadgeTone.Error)
            JengaBadge(
                text = buildAnnotatedString {
                    withStyle(SpanStyle(fontWeight = FontWeight.Bold)) { append("3") }
                    append(" new")
                },
                tone = JengaBadgeTone.Success,
                shape = JengaTheme.shapes.sm,
            )
        }
    }
}
