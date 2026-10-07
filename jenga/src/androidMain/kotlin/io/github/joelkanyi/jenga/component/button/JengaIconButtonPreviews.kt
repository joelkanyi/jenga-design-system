package io.github.joelkanyi.jenga.component.button

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import io.github.joelkanyi.jenga.component.icon.JengaIcon
import io.github.joelkanyi.jenga.component.icon.JengaIcons
import io.github.joelkanyi.jenga.component.layout.JengaInline
import io.github.joelkanyi.jenga.core.preview.JengaBlockPreviews
import io.github.joelkanyi.jenga.core.preview.RtlPreview
import io.github.joelkanyi.jenga.theme.JengaTheme

// ---- Previews --------------------------------------------------------------

@JengaBlockPreviews
@Composable
internal fun JengaIconButtonPreview() {
    JengaTheme { IconButtonShowcase() }
}

@Preview(name = "RTL", showBackground = true)
@Composable
internal fun JengaIconButtonRtlPreview() {
    JengaTheme { RtlPreview { IconButtonShowcase() } }
}

@Composable
private fun IconButtonShowcase() {
    Row(
        modifier = Modifier
            .background(JengaTheme.colors.background)
            .padding(JengaTheme.spacing.xl),
        horizontalArrangement = Arrangement.spacedBy(JengaTheme.spacing.md),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        JengaIconButton(onClick = {}, variant = JengaIconButtonVariant.Standard) {
            JengaIcon(JengaIcons.Search, contentDescription = "Search")
        }
        JengaIconButton(onClick = {}, variant = JengaIconButtonVariant.Filled) {
            JengaIcon(JengaIcons.Add, contentDescription = "Add")
        }
        JengaIconButton(onClick = {}, variant = JengaIconButtonVariant.Tonal) {
            JengaIcon(JengaIcons.Info, contentDescription = "Info")
        }
        JengaIconButton(onClick = {}, variant = JengaIconButtonVariant.Overlay) {
            JengaIcon(JengaIcons.Flash, contentDescription = "Torch")
        }
        JengaIconButton(onClick = {}, enabled = false) {
            JengaIcon(JengaIcons.Close, contentDescription = "Close")
        }
    }
}

@JengaBlockPreviews
@Composable
internal fun JengaIconButtonOutlinedPreview() {
    JengaTheme {
        JengaInline(modifier = Modifier.background(JengaTheme.colors.background).padding(JengaTheme.spacing.lg)) {
            JengaIconButton(onClick = {}, variant = JengaIconButtonVariant.Outlined, shape = JengaTheme.shapes.md, size = 50.dp) {
                JengaIcon(JengaIcons.MoreHorizontal, contentDescription = "More")
            }
            JengaIconButton(onClick = {}, variant = JengaIconButtonVariant.Outlined, size = 48.dp) {
                JengaIcon(JengaIcons.Phone, contentDescription = "Call")
            }
            JengaIconButton(onClick = {}, variant = JengaIconButtonVariant.Outlined, enabled = false) {
                JengaIcon(JengaIcons.Copy, contentDescription = "Copy")
            }
        }
    }
}
