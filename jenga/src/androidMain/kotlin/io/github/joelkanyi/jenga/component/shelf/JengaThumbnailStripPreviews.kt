package io.github.joelkanyi.jenga.component.shelf

import androidx.compose.foundation.background
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
internal fun JengaThumbnailStripPreview() {
    JengaTheme { JengaThumbnailStripShowcase() }
}

@Preview(name = "RTL", showBackground = true)
@Composable
internal fun JengaThumbnailStripRtlPreview() {
    JengaTheme { RtlPreview { JengaThumbnailStripShowcase() } }
}

@Composable
private fun JengaThumbnailStripShowcase() {
    JengaThumbnailStrip(
        count = 3,
        modifier = Modifier.background(JengaTheme.colors.background).padding(vertical = JengaTheme.spacing.lg),
        onAdd = {},
        addContentDescription = "Add photo",
        addIcon = { JengaIcon(JengaIcons.ImagePlus, contentDescription = null) },
    ) {
        JengaIcon(JengaIcons.Image, contentDescription = null)
    }
}
