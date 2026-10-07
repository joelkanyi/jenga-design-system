package io.github.joelkanyi.jenga.component.feedback

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.tooling.preview.Preview
import io.github.joelkanyi.jenga.component.text.JengaText
import io.github.joelkanyi.jenga.core.preview.JengaBlockPreviews
import io.github.joelkanyi.jenga.core.preview.RtlPreview
import io.github.joelkanyi.jenga.theme.JengaTheme

// ---- Previews --------------------------------------------------------------
// The modal window can't be captured statically, so the previews render the
// sheet's visual surface (handle + content) inline.

@JengaBlockPreviews
@Composable
internal fun JengaBottomSheetPreview() {
    JengaTheme { BottomSheetSurfacePreview() }
}

@Preview(name = "RTL", showBackground = true)
@Composable
internal fun JengaBottomSheetRtlPreview() {
    JengaTheme { RtlPreview { BottomSheetSurfacePreview() } }
}

@Composable
private fun BottomSheetSurfacePreview() {
    Column(
        modifier = Modifier
            .background(JengaTheme.colors.background)
            .padding(JengaTheme.spacing.lg),
    ) {
        Column(
            modifier = Modifier
                .clip(JengaTheme.shapes.card)
                .background(JengaTheme.colors.surface),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            JengaDragHandle()
            Column(
                modifier = Modifier.padding(JengaTheme.spacing.xl),
                verticalArrangement = Arrangement.spacedBy(JengaTheme.spacing.sm),
            ) {
                JengaText(text = "Select a gate", style = JengaTheme.typography.titleLarge)
                JengaText(
                    text = "Choose which gate you're scanning at.",
                    style = JengaTheme.typography.bodySmall,
                    color = JengaTheme.colors.textMuted,
                )
            }
        }
    }
}
