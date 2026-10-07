package io.github.joelkanyi.jenga.component.stepper

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

@JengaBlockPreviews
@Composable
internal fun JengaStepperPreview() {
    JengaTheme { StepperShowcase() }
}

@Preview(name = "RTL", showBackground = true)
@Composable
internal fun JengaStepperRtlPreview() {
    JengaTheme { RtlPreview { StepperShowcase() } }
}

@Composable
private fun StepperShowcase() {
    Column(
        modifier = Modifier
            .background(JengaTheme.colors.background)
            .padding(JengaTheme.spacing.xl),
        verticalArrangement = Arrangement.spacedBy(JengaTheme.spacing.md),
    ) {
        JengaStepper(value = 4, onValueChange = {}, min = 1, max = 12)
        JengaStepper(value = 1, onValueChange = {}, min = 1, max = 12) // decrement disabled
        JengaStepper(value = 8, onValueChange = {}, min = 1, max = 8) // increment disabled
    }
}

@JengaBlockPreviews
@Composable
internal fun JengaStepperVariantsPreview() {
    JengaTheme {
        Column(
            modifier = Modifier.background(JengaTheme.colors.background).padding(JengaTheme.spacing.lg),
            verticalArrangement = Arrangement.spacedBy(JengaTheme.spacing.md),
        ) {
            JengaStepper(value = 12, onValueChange = {}, editable = true, max = 99)
            JengaStepper(
                value = 1,
                onValueChange = {},
                min = 1,
                onRemove = {},
                removeContentDescription = "Remove",
                shape = JengaTheme.shapes.md,
                buttonShape = JengaTheme.shapes.control,
            )
        }
    }
}
