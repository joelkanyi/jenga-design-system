package io.github.joelkanyi.jenga.component.textfield

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import io.github.joelkanyi.jenga.core.preview.JengaBlockPreviews
import io.github.joelkanyi.jenga.core.preview.RtlPreview
import io.github.joelkanyi.jenga.theme.JengaTheme

// ---- Previews --------------------------------------------------------------

@JengaBlockPreviews
@Composable
internal fun JengaSelectFieldPreview() {
    JengaTheme { SelectFieldShowcase() }
}

@Preview(name = "RTL", showBackground = true)
@Composable
internal fun JengaSelectFieldRtlPreview() {
    JengaTheme { RtlPreview { SelectFieldShowcase() } }
}

@Composable
private fun SelectFieldShowcase() {
    Column(
        modifier = Modifier
            .background(JengaTheme.colors.background)
            .padding(JengaTheme.spacing.lg),
        verticalArrangement = Arrangement.spacedBy(JengaTheme.spacing.md),
    ) {
        JengaSelectField(value = null, onClick = {}, label = "Fault", placeholder = "Choose a fault")
        JengaSelectField(value = "Battery not charging", onClick = {}, label = "Fault")
        JengaSelectField(
            value = null,
            onClick = {},
            label = "Technician",
            placeholder = "Choose a technician",
            status = JengaTextFieldStatus.Error,
            supportingText = "Pick a technician to continue",
        )
        JengaSelectField(value = "Nairobi CBD", onClick = {}, label = "Shop", enabled = false)
    }
}
