package io.github.joelkanyi.jenga.component.card

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import io.github.joelkanyi.jenga.component.icon.JengaIcon
import io.github.joelkanyi.jenga.component.icon.JengaIcons
import io.github.joelkanyi.jenga.component.text.JengaText
import io.github.joelkanyi.jenga.core.preview.JengaBlockPreviews
import io.github.joelkanyi.jenga.core.preview.RtlPreview
import io.github.joelkanyi.jenga.theme.JengaTheme

// ---- Previews --------------------------------------------------------------

@JengaBlockPreviews
@Composable
internal fun JengaCardFooterPreview() {
    JengaTheme { JengaCardFooterShowcase() }
}

@Preview(name = "RTL", showBackground = true)
@Composable
internal fun JengaCardFooterRtlPreview() {
    JengaTheme { RtlPreview { JengaCardFooterShowcase() } }
}

@Composable
private fun JengaCardFooterShowcase() {
    Column(modifier = Modifier.background(JengaTheme.colors.background).padding(JengaTheme.spacing.lg)) {
        JengaCard(variant = JengaCardVariant.Outlined, contentPadding = PaddingValues()) {
            JengaText("Battery replacement", modifier = Modifier.padding(JengaTheme.spacing.lg))
            JengaCardFooter(
                text = "Waiting for parts since Monday",
                leadingIcon = { JengaIcon(JengaIcons.Clock, contentDescription = null, size = JengaTheme.sizing.iconSmall) },
            )
        }
    }
}
