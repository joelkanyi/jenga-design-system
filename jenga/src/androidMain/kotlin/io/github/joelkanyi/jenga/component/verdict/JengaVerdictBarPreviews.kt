package io.github.joelkanyi.jenga.component.verdict

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import io.github.joelkanyi.jenga.component.action.JengaAction
import io.github.joelkanyi.jenga.core.preview.JengaBlockPreviews
import io.github.joelkanyi.jenga.core.preview.RtlPreview
import io.github.joelkanyi.jenga.theme.JengaTheme

@JengaBlockPreviews
@Composable
internal fun JengaVerdictBarPreview() {
    JengaTheme { VerdictShowcase() }
}

@Preview(name = "RTL", showBackground = true)
@Composable
internal fun JengaVerdictBarRtlPreview() {
    JengaTheme { RtlPreview { VerdictShowcase() } }
}

@Composable
private fun VerdictShowcase() {
    Column(
        modifier = Modifier
            .background(JengaTheme.colors.background)
            .padding(JengaTheme.spacing.lg),
        verticalArrangement = Arrangement.spacedBy(JengaTheme.spacing.md),
    ) {
        JengaVerdictBar(
            amount = "KES 1,190",
            amountSuffix = "left",
            tone = JengaVerdictTone.Positive,
            label = "This week's budget",
            progress = 0.38f,
            sublines = JengaVerdictSublines("KES 710 of 1,900 spent", "8 of 11 priced"),
            action = JengaAction(label = "Change", onClick = {}),
        )
        JengaVerdictBar(
            amount = "Close to your budget",
            tone = JengaVerdictTone.Caution,
            progress = 0.88f,
            sublines = JengaVerdictSublines("KES 1,670 of 1,900 spent"),
        )
    }
}

@JengaBlockPreviews
@Composable
internal fun JengaVerdictBarNeutralPreview() {
    JengaTheme {
        Column(modifier = Modifier.background(JengaTheme.colors.background).padding(JengaTheme.spacing.lg)) {
            JengaVerdictBar(
                amount = "KES 2,400",
                amountSuffix = "of 4,000",
                tone = JengaVerdictTone.Neutral,
                label = "Paid so far",
                progress = 0.6f,
                sublines = JengaVerdictSublines(start = "Last paid 12 Mar", end = "KES 1,600 left"),
            )
        }
    }
}
