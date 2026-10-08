package io.github.joelkanyi.jenga.component.avatar

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import io.github.joelkanyi.jenga.core.preview.JengaBlockPreviews
import io.github.joelkanyi.jenga.core.preview.RtlPreview
import io.github.joelkanyi.jenga.theme.JengaTheme

// ---- Previews --------------------------------------------------------------

@JengaBlockPreviews
@Composable
internal fun JengaAvatarPreview() {
    JengaTheme { AvatarShowcase() }
}

@Preview(name = "RTL", showBackground = true)
@Composable
internal fun JengaAvatarRtlPreview() {
    JengaTheme { RtlPreview { AvatarShowcase() } }
}

@Composable
private fun AvatarShowcase() {
    Row(
        modifier = Modifier
            .background(JengaTheme.colors.background)
            .padding(JengaTheme.spacing.xl),
        horizontalArrangement = Arrangement.spacedBy(JengaTheme.spacing.lg),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        JengaAvatar(name = "Joel Kanyi", size = JengaAvatarSize.Small)
        JengaAvatar(name = "Joel Kanyi", size = JengaAvatarSize.Medium)
        JengaAvatar(name = "Ada Lovelace", size = JengaAvatarSize.Large)
    }
}

@JengaBlockPreviews
@Composable
internal fun JengaAvatarCustomSizePreview() {
    JengaTheme {
        Row(
            modifier = Modifier.background(JengaTheme.colors.background).padding(JengaTheme.spacing.lg),
            horizontalArrangement = Arrangement.spacedBy(JengaTheme.spacing.md),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            JengaAvatar(
                name = "Peter Kamau",
                diameter = 42.dp,
                textStyle = JengaTheme.typography.bodyMedium.copy(fontSize = 15.sp),
            )
            JengaAvatar(
                name = "Grace Wanjiru",
                diameter = 36.dp,
                textStyle = JengaTheme.typography.caption,
            )
        }
    }
}
