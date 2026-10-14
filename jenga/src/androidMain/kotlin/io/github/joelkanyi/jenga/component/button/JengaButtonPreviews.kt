package io.github.joelkanyi.jenga.component.button

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import io.github.joelkanyi.jenga.core.preview.JengaBlockPreviews
import io.github.joelkanyi.jenga.core.preview.RtlPreview
import io.github.joelkanyi.jenga.theme.JengaTheme

// ---- Previews (scanned by Roborazzi to generate screenshot tests) ----------

// Previews are `internal` (not `private`) so ComposablePreviewScanner picks them
// up; by default it skips private previews. They stay out of the public API.
@JengaBlockPreviews
@Composable
internal fun JengaButtonPreview() {
    JengaTheme { ButtonShowcase() }
}

@Preview(name = "RTL", showBackground = true)
@Composable
internal fun JengaButtonRtlPreview() {
    JengaTheme { RtlPreview { ButtonShowcase() } }
}

@Composable
private fun ButtonShowcase() {
    Column(
        modifier = Modifier
            .background(JengaTheme.colors.background)
            .padding(JengaTheme.spacing.xl),
        verticalArrangement = Arrangement.spacedBy(JengaTheme.spacing.md),
    ) {
        JengaButton(text = "Primary", onClick = {}, variant = JengaButtonVariant.Primary)
        JengaButton(text = "Ink", onClick = {}, variant = JengaButtonVariant.Ink)
        JengaButton(text = "Ghost", onClick = {}, variant = JengaButtonVariant.Ghost)
        JengaButton(text = "Neutral", onClick = {}, variant = JengaButtonVariant.Neutral)
        JengaButton(text = "Outline", onClick = {}, variant = JengaButtonVariant.Outline)
        JengaButton(text = "Danger", onClick = {}, variant = JengaButtonVariant.Danger)
        Row(horizontalArrangement = Arrangement.spacedBy(JengaTheme.spacing.sm)) {
            JengaButton(text = "Small", onClick = {}, size = JengaButtonSize.Small)
            JengaButton(text = "Large", onClick = {}, size = JengaButtonSize.Large)
        }
        JengaButton(text = "Disabled", onClick = {}, enabled = false)
        JengaButton(text = "Loading", onClick = {}, loading = true)
        JengaButton(text = "Check ID", onClick = {}, loading = true, loadingText = "Checking…")
    }
}
