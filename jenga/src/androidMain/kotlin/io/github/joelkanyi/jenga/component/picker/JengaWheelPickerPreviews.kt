package io.github.joelkanyi.jenga.component.picker

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
import kotlinx.datetime.LocalDate

// ---- Previews --------------------------------------------------------------

@JengaBlockPreviews
@Composable
internal fun JengaDateOfBirthPickerPreview() {
    JengaTheme { JengaDateOfBirthPickerShowcase() }
}

@Preview(name = "RTL", showBackground = true)
@Composable
internal fun JengaDateOfBirthPickerRtlPreview() {
    JengaTheme { RtlPreview { JengaDateOfBirthPickerShowcase() } }
}

@Composable
private fun JengaDateOfBirthPickerShowcase() {
    Column(
        modifier = Modifier.background(JengaTheme.colors.surface).padding(JengaTheme.spacing.lg),
        verticalArrangement = Arrangement.spacedBy(JengaTheme.spacing.lg),
    ) {
        JengaDateOfBirthPicker(
            value = LocalDate(1990, 3, 14),
            onValueChange = {},
            monthLabel = { it.name.lowercase().replaceFirstChar(Char::uppercase) },
            maxDate = LocalDate(2026, 3, 14),
        )
        JengaWheelPicker(
            items = listOf("Morning", "Afternoon", "Evening"),
            selectedIndex = 1,
            onSelectedIndexChange = {},
            visibleItemCount = 3,
        )
    }
}
