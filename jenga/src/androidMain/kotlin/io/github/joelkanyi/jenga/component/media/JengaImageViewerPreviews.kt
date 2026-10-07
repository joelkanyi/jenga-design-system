package io.github.joelkanyi.jenga.component.media

import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import io.github.joelkanyi.jenga.component.button.JengaIconButton
import io.github.joelkanyi.jenga.component.button.JengaIconButtonVariant
import io.github.joelkanyi.jenga.component.icon.JengaIcon
import io.github.joelkanyi.jenga.component.icon.JengaIcons
import io.github.joelkanyi.jenga.core.preview.JengaBlockPreviews
import io.github.joelkanyi.jenga.core.preview.RtlPreview
import io.github.joelkanyi.jenga.theme.JengaTheme

// ---- Previews --------------------------------------------------------------

@JengaBlockPreviews
@Composable
internal fun JengaImageViewerPreview() {
    JengaTheme { JengaImageViewerShowcase() }
}

@Preview(name = "RTL", showBackground = true)
@Composable
internal fun JengaImageViewerRtlPreview() {
    JengaTheme { RtlPreview { JengaImageViewerShowcase() } }
}

@Composable
private fun JengaImageViewerShowcase() {
    JengaImageViewerContent(
        onClose = {},
        title = "battery-photo-01.jpg",
        closeContentDescription = "Close",
        modifier = Modifier.height(320.dp),
        actions = {
            JengaIconButton(onClick = {}, variant = JengaIconButtonVariant.Overlay) {
                JengaIcon(JengaIcons.Download, contentDescription = "Download")
            }
            JengaIconButton(onClick = {}, variant = JengaIconButtonVariant.Overlay) {
                JengaIcon(JengaIcons.Share, contentDescription = "Share")
            }
        },
    ) {
        JengaIcon(JengaIcons.Image, contentDescription = null, size = JengaTheme.sizing.iconLarge)
    }
}
