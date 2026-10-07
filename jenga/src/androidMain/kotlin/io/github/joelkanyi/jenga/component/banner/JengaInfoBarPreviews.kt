package io.github.joelkanyi.jenga.component.banner

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import io.github.joelkanyi.jenga.component.icon.JengaIcon
import io.github.joelkanyi.jenga.component.icon.JengaIcons
import io.github.joelkanyi.jenga.core.preview.JengaBlockPreviews
import io.github.joelkanyi.jenga.core.preview.RtlPreview
import io.github.joelkanyi.jenga.theme.JengaTheme

// ---- Previews --------------------------------------------------------------

@JengaBlockPreviews
@Composable
internal fun JengaInfoBarPreview() {
    JengaTheme { JengaInfoBarShowcase() }
}

@Preview(name = "RTL", showBackground = true)
@Composable
internal fun JengaInfoBarRtlPreview() {
    JengaTheme { RtlPreview { JengaInfoBarShowcase() } }
}

@Composable
private fun JengaInfoBarShowcase() {
    Column(
        modifier = Modifier.background(JengaTheme.colors.background).padding(JengaTheme.spacing.lg),
        verticalArrangement = Arrangement.spacedBy(JengaTheme.spacing.md),
    ) {
        JengaInfoBar(text = "View only", leadingIcon = { JengaIcon(JengaIcons.Lock, contentDescription = null) })
        JengaInfoBar(text = "Closed")
    }
}
