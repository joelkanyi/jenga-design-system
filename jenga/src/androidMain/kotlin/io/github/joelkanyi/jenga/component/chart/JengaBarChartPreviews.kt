package io.github.joelkanyi.jenga.component.chart

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import io.github.joelkanyi.jenga.component.text.JengaText
import io.github.joelkanyi.jenga.core.preview.JengaBlockPreviews
import io.github.joelkanyi.jenga.core.preview.RtlPreview
import io.github.joelkanyi.jenga.theme.JengaTheme

// ---- Previews --------------------------------------------------------------

@JengaBlockPreviews
@Composable
internal fun JengaBarChartPreview() {
    JengaTheme { JengaBarChartShowcase() }
}

@Preview(name = "RTL", showBackground = true)
@Composable
internal fun JengaBarChartRtlPreview() {
    JengaTheme { RtlPreview { JengaBarChartShowcase() } }
}

@Composable
private fun JengaBarChartShowcase() {
    val months = listOf(
        JengaBarChartEntry("May", 8200f),
        JengaBarChartEntry("Jun", 12400f),
        JengaBarChartEntry("Jul", 0f),
        JengaBarChartEntry("Aug", 9100f),
        JengaBarChartEntry("Sep", 15800f),
        JengaBarChartEntry("Oct", 6300f),
    )
    Column(
        modifier = Modifier.background(JengaTheme.colors.surface).padding(JengaTheme.spacing.lg),
        verticalArrangement = Arrangement.spacedBy(JengaTheme.spacing.xl),
    ) {
        JengaBarChart(entries = months, barContentDescription = { "${it.label}, ${it.value}" })
        JengaBarChart(
            entries = months,
            barContentDescription = { "${it.label}, ${it.value}" },
            selectedIndex = 4,
            onSelect = {},
            plotHeight = 70.dp,
            minBarHeight = 4.dp,
            barMaxWidth = 34.dp,
            barSpacing = 10.dp,
            barShape = RoundedCornerShape(topStart = 8.dp, topEnd = 8.dp, bottomStart = 3.dp, bottomEnd = 3.dp),
            labelSpacing = 6.dp,
            contentPadding = PaddingValues(start = 16.dp, top = 12.dp, end = 16.dp, bottom = 4.dp),
        )
        JengaBarChart(
            entries = months.take(3),
            barContentDescription = { "${it.label}, ${it.value}" },
            valueLabel = { entry, _ ->
                JengaText("${(entry.value / 1000).toInt()}k", style = JengaTheme.typography.caption)
            },
        )
    }
}
