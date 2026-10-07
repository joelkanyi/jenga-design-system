package io.github.joelkanyi.jenga.component.list

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import io.github.joelkanyi.jenga.component.badge.JengaBadgeTone
import io.github.joelkanyi.jenga.component.icon.JengaIcon
import io.github.joelkanyi.jenga.component.icon.JengaIcons
import io.github.joelkanyi.jenga.core.preview.JengaBlockPreviews
import io.github.joelkanyi.jenga.core.preview.RtlPreview
import io.github.joelkanyi.jenga.foundation.typography.withTabularFigures
import io.github.joelkanyi.jenga.theme.JengaTheme

// ---- Previews --------------------------------------------------------------

@JengaBlockPreviews
@Composable
internal fun JengaKeyValueRowPreview() {
    JengaTheme { KeyValueRowShowcase() }
}

@Preview(name = "RTL", showBackground = true)
@Composable
internal fun JengaKeyValueRowRtlPreview() {
    JengaTheme { RtlPreview { KeyValueRowShowcase() } }
}

@Composable
private fun KeyValueRowShowcase() {
    Column(
        modifier = Modifier
            .background(JengaTheme.colors.surface)
            .padding(JengaTheme.spacing.lg),
    ) {
        JengaKeyValueRow(
            label = "Serial number",
            value = "SN-0042-7781",
            valueStyle = JengaTheme.typography.mono,
            trailingContent = { JengaIcon(JengaIcons.Copy, contentDescription = null) },
        )
        JengaKeyValueRow(label = "Status", value = "Paid", valueTone = JengaBadgeTone.Success)
        JengaKeyValueRow(label = "Address", value = "Plot 12, Mombasa Road, off Likoni Lane, Nairobi")
        JengaKeyValueRow(
            label = "Total",
            value = "KES 12,450",
            valueStyle = JengaTheme.typography.bodyMedium.withTabularFigures(),
            emphasis = JengaKeyValueEmphasis.Total,
        )
    }
}
