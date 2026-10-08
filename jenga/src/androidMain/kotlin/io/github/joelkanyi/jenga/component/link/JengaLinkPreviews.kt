package io.github.joelkanyi.jenga.component.link

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import io.github.joelkanyi.jenga.component.icon.JengaIcon
import io.github.joelkanyi.jenga.component.icon.JengaIcons
import io.github.joelkanyi.jenga.component.layout.JengaStack
import io.github.joelkanyi.jenga.core.preview.JengaBlockPreviews
import io.github.joelkanyi.jenga.core.preview.RtlPreview
import io.github.joelkanyi.jenga.pattern.JengaSectionHeader
import io.github.joelkanyi.jenga.pattern.JengaSectionHeaderStyle
import io.github.joelkanyi.jenga.theme.JengaTheme

// ---- Previews --------------------------------------------------------------

@JengaBlockPreviews
@Composable
internal fun JengaLinkPreview() {
    JengaTheme { LinkShowcase() }
}

@Preview(name = "RTL", showBackground = true)
@Composable
internal fun JengaLinkRtlPreview() {
    JengaTheme { RtlPreview { LinkShowcase() } }
}

@Composable
private fun LinkShowcase() {
    JengaStack(
        space = JengaTheme.spacing.sm,
        modifier = Modifier
            .background(JengaTheme.colors.background)
            .padding(JengaTheme.spacing.xl),
    ) {
        JengaLink(text = "Privacy policy", onClick = {})
        JengaLink(text = "Learn more", onClick = {}, underline = false)
        JengaLink(text = "Unavailable", onClick = {}, enabled = false)
    }
}

@JengaBlockPreviews
@Composable
internal fun JengaLinkActionPreview() {
    JengaTheme {
        JengaSectionHeader(
            title = "Customer",
            modifier = Modifier.background(JengaTheme.colors.background).padding(JengaTheme.spacing.lg),
            style = JengaSectionHeaderStyle.Overline,
            contentPadding = PaddingValues(),
            trailingContent = {
                JengaLink(
                    text = "Phone",
                    onClick = {},
                    underline = false,
                    leadingIcon = { JengaIcon(JengaIcons.Add, contentDescription = null, size = 18.dp) },
                    contentPadding = PaddingValues(horizontal = 6.dp, vertical = 4.dp),
                )
            },
        )
    }
}
