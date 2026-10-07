package io.github.joelkanyi.jenga.component.scaffold

import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import io.github.joelkanyi.jenga.component.button.JengaIconButton
import io.github.joelkanyi.jenga.component.icon.JengaIcon
import io.github.joelkanyi.jenga.component.icon.JengaIcons
import io.github.joelkanyi.jenga.component.text.JengaText
import io.github.joelkanyi.jenga.core.preview.JengaBlockPreviews
import io.github.joelkanyi.jenga.core.preview.RtlPreview
import io.github.joelkanyi.jenga.theme.JengaTheme

// ---- Previews --------------------------------------------------------------

@JengaBlockPreviews
@Composable
internal fun JengaTopAppBarPreview() {
    JengaTheme { JengaTopAppBar(title = "Sol Fest 2026", subtitle = "Gate A · Online") }
}

@Preview(name = "RTL", showBackground = true)
@Composable
internal fun JengaTopAppBarRtlPreview() {
    JengaTheme { RtlPreview { JengaTopAppBar(title = "Sol Fest 2026", subtitle = "Gate A · Online") } }
}

@JengaBlockPreviews
@Composable
internal fun JengaTopAppBarSlotPreview() {
    JengaTheme {
        JengaTopAppBar(
            title = {
                Column {
                    JengaText("WO-2026-0142", style = JengaTheme.typography.mono)
                    JengaText("Device repair", style = JengaTheme.typography.caption, color = JengaTheme.colors.textMuted)
                }
            },
            navigationIcon = {
                JengaIconButton(onClick = {}) { JengaIcon(JengaIcons.ArrowBack, contentDescription = "Back") }
            },
            showDivider = false,
        )
    }
}
