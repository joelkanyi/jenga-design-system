package io.github.joelkanyi.jenga.component.fab

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import io.github.joelkanyi.jenga.component.icon.JengaIcon
import io.github.joelkanyi.jenga.component.icon.JengaIcons
import io.github.joelkanyi.jenga.component.layout.JengaInline
import io.github.joelkanyi.jenga.core.preview.JengaBlockPreviews
import io.github.joelkanyi.jenga.core.preview.RtlPreview
import io.github.joelkanyi.jenga.theme.JengaTheme

// ---- Previews --------------------------------------------------------------

@JengaBlockPreviews
@Composable
internal fun JengaFabPreview() {
    JengaTheme { FabShowcase() }
}

@Preview(name = "RTL", showBackground = true)
@Composable
internal fun JengaFabRtlPreview() {
    JengaTheme { RtlPreview { FabShowcase() } }
}

@Composable
private fun FabShowcase() {
    Row(
        modifier = Modifier
            .background(JengaTheme.colors.background)
            .padding(JengaTheme.spacing.xl),
        horizontalArrangement = Arrangement.spacedBy(JengaTheme.spacing.lg),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        JengaFab(onClick = {}) { JengaIcon(JengaIcons.Add, contentDescription = "Add") }
        JengaExtendedFab(
            label = "Ask",
            onClick = {},
            icon = { JengaIcon(JengaIcons.MessageCircle, contentDescription = null) },
        )
    }
}

@JengaBlockPreviews
@Composable
internal fun JengaFabGlowPreview() {
    JengaTheme {
        JengaInline(modifier = Modifier.background(JengaTheme.colors.background).padding(JengaTheme.spacing.xl)) {
            JengaExtendedFab(
                label = "New ticket",
                onClick = {},
                icon = { JengaIcon(JengaIcons.Add, contentDescription = null) },
                shape = JengaTheme.shapes.lg,
                shadowColor = JengaTheme.colors.brand,
            )
        }
    }
}
