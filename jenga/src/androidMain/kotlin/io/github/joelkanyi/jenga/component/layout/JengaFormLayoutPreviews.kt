package io.github.joelkanyi.jenga.component.layout

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import io.github.joelkanyi.jenga.component.button.JengaButton
import io.github.joelkanyi.jenga.component.textfield.JengaTextField
import io.github.joelkanyi.jenga.core.preview.JengaBlockPreviews
import io.github.joelkanyi.jenga.core.preview.RtlPreview
import io.github.joelkanyi.jenga.theme.JengaTheme

// ---- Previews --------------------------------------------------------------

@JengaBlockPreviews
@Composable
internal fun JengaFormLayoutPreview() {
    JengaTheme { JengaFormLayoutShowcase() }
}

@Preview(name = "RTL", showBackground = true)
@Composable
internal fun JengaFormLayoutRtlPreview() {
    JengaTheme { RtlPreview { JengaFormLayoutShowcase() } }
}

@Composable
private fun JengaFormLayoutShowcase() {
    JengaFormLayout(
        modifier = Modifier.height(360.dp).background(JengaTheme.colors.background),
        bottomBar = {
            JengaButton(
                text = "Save",
                onClick = {},
                modifier = Modifier.fillMaxWidth().padding(JengaTheme.spacing.lg),
            )
        },
    ) {
        item { JengaTextField(value = "Amina Otieno", onValueChange = {}, label = "Customer name") }
        item { JengaTextField(value = "", onValueChange = {}, label = "Phone", placeholder = "07xx xxx xxx") }
        item { JengaTextField(value = "", onValueChange = {}, label = "Notes") }
    }
}
