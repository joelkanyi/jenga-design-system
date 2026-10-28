package io.github.joelkanyi.jenga.component.picker

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import io.github.joelkanyi.jenga.core.preview.JengaBlockPreviews
import io.github.joelkanyi.jenga.theme.JengaTheme
import kotlinx.datetime.LocalTime

@JengaBlockPreviews
@Composable
internal fun JengaTimeInputWheelPreview() {
    JengaTheme {
        Column(
            modifier = Modifier.background(JengaTheme.colors.surface).padding(JengaTheme.spacing.md),
            verticalArrangement = Arrangement.spacedBy(JengaTheme.spacing.lg),
        ) {
            PreviewTimeInput(LocalTime(14, 30), JengaTimeInputStyle.Wheel, is24Hour = false)
            PreviewTimeInput(LocalTime(14, 30), JengaTimeInputStyle.Wheel, is24Hour = true)
        }
    }
}

@JengaBlockPreviews
@Composable
internal fun JengaTimeInputFieldsPreview() {
    JengaTheme {
        Column(
            modifier = Modifier.background(JengaTheme.colors.surface).padding(JengaTheme.spacing.md),
            verticalArrangement = Arrangement.spacedBy(JengaTheme.spacing.lg),
        ) {
            PreviewTimeInput(LocalTime(9, 5), JengaTimeInputStyle.Fields, is24Hour = false)
            PreviewTimeInput(LocalTime(21, 5), JengaTimeInputStyle.Fields, is24Hour = true)
        }
    }
}

@Composable
private fun PreviewTimeInput(time: LocalTime, style: JengaTimeInputStyle, is24Hour: Boolean) {
    JengaTimeInput(
        time = time,
        onTimeChange = {},
        hourLabel = "Hour",
        minuteLabel = "Minute",
        amLabel = "AM",
        pmLabel = "PM",
        style = style,
        is24Hour = is24Hour,
    )
}
