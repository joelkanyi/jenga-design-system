package io.github.joelkanyi.jenga.component.list

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import io.github.joelkanyi.jenga.component.badge.JengaBadge
import io.github.joelkanyi.jenga.component.badge.JengaBadgeTone
import io.github.joelkanyi.jenga.component.icon.JengaIcon
import io.github.joelkanyi.jenga.component.icon.JengaIcons
import io.github.joelkanyi.jenga.core.preview.JengaBlockPreviews
import io.github.joelkanyi.jenga.core.preview.RtlPreview
import io.github.joelkanyi.jenga.theme.JengaTheme

// ---- Previews --------------------------------------------------------------

@JengaBlockPreviews
@Composable
internal fun JengaListItemPreview() {
    JengaTheme { ListItemShowcase() }
}

@Preview(name = "RTL", showBackground = true)
@Composable
internal fun JengaListItemRtlPreview() {
    JengaTheme { RtlPreview { ListItemShowcase() } }
}

@Composable
private fun ListItemShowcase() {
    Column(modifier = Modifier.background(JengaTheme.colors.surface)) {
        JengaListItem(
            headline = "Gate A",
            supporting = "Main entrance · 2 attendants",
            leadingContent = { JengaIcon(JengaIcons.Check, contentDescription = null, tint = JengaTheme.colors.success) },
            trailingContent = { JengaIcon(JengaIcons.ChevronRight, contentDescription = null) },
            onClick = {},
        )
        JengaListItem(
            headline = "Gate B",
            supporting = "North wing",
            trailingContent = { JengaIcon(JengaIcons.ChevronRight, contentDescription = null) },
            onClick = {},
        )
        JengaListItem(
            headline = "Replace the inverter control board and recalibrate",
            supporting = "Ticket TKT-2041",
            supportingContent = { JengaBadge(text = "In progress", tone = JengaBadgeTone.Warning) },
            headlineMaxLines = 2,
            onClick = {},
        )
        JengaListItem(
            headline = "Gate C",
            supporting = "Closed for maintenance",
            trailingContent = { JengaIcon(JengaIcons.ChevronRight, contentDescription = null) },
            onClick = {},
            enabled = false,
        )
    }
}
