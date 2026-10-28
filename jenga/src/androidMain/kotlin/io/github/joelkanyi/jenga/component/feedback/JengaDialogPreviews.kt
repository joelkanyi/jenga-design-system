package io.github.joelkanyi.jenga.component.feedback

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import io.github.joelkanyi.jenga.component.button.JengaButton
import io.github.joelkanyi.jenga.component.button.JengaButtonVariant
import io.github.joelkanyi.jenga.core.preview.JengaBlockPreviews
import io.github.joelkanyi.jenga.core.preview.RtlPreview
import io.github.joelkanyi.jenga.theme.JengaTheme

// ---- Previews --------------------------------------------------------------

@JengaBlockPreviews
@Composable
internal fun JengaDialogPreview() {
    JengaTheme { DialogShowcase() }
}

@Preview(name = "RTL", showBackground = true)
@Composable
internal fun JengaDialogRtlPreview() {
    JengaTheme { RtlPreview { DialogShowcase() } }
}

@Composable
private fun DialogShowcase() {
    Column(
        modifier = Modifier
            .background(JengaTheme.colors.background)
            .padding(JengaTheme.spacing.xl),
    ) {
        // Render the dialog surface inline (the Dialog window itself can't be
        // captured in a static screenshot).
        JengaDialogSurface(
            modifier = Modifier.overlayOutline(JengaDialogDefaults.border, JengaDialogDefaults.shape),
            title = "Reset device?",
            text = "This clears the cached gate session on this device. You can sign in again anytime.",
            confirmButton = {
                JengaButton(text = "Reset", onClick = {}, variant = JengaButtonVariant.Danger)
            },
            dismissButton = {
                JengaButton(text = "Cancel", onClick = {}, variant = JengaButtonVariant.Ghost)
            },
        )
    }
}
