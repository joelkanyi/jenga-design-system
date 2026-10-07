package io.github.joelkanyi.jenga.component.menu

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.tooling.preview.Preview
import io.github.joelkanyi.jenga.component.icon.JengaIcon
import io.github.joelkanyi.jenga.component.icon.JengaIcons
import io.github.joelkanyi.jenga.core.preview.JengaBlockPreviews
import io.github.joelkanyi.jenga.theme.JengaTheme

// ---- Previews --------------------------------------------------------------
// The real menu is a popup window; the preview renders the item surface inline.

@JengaBlockPreviews
@Composable
internal fun JengaDropdownMenuPreview() {
    JengaTheme { MenuSurfacePreview() }
}

@Preview(name = "RTL", showBackground = true)
@Composable
internal fun JengaDropdownMenuRtlPreview() {
    JengaTheme {
        io.github.joelkanyi.jenga.core.preview.RtlPreview { MenuSurfacePreview() }
    }
}

@Composable
private fun MenuSurfacePreview() {
    Column(
        modifier = Modifier
            .background(JengaTheme.colors.background)
            .padding(JengaTheme.spacing.lg),
    ) {
        Column(
            modifier = Modifier
                .clip(JengaTheme.shapes.card)
                .background(JengaTheme.colors.surface),
        ) {
            JengaDropdownMenuItem(
                text = "Re-enter",
                onClick = {},
                leadingIcon = { JengaIcon(JengaIcons.Check, contentDescription = null) },
            )
            JengaDropdownMenuItem(
                text = "View history",
                onClick = {},
                leadingIcon = { JengaIcon(JengaIcons.Search, contentDescription = null) },
            )
            JengaDropdownMenuItem(text = "Remove", onClick = {}, enabled = false)
        }
    }
}

@JengaBlockPreviews
@Composable
internal fun JengaDropdownMenuStatesPreview() {
    JengaTheme {
        Column(modifier = Modifier.background(JengaTheme.colors.background).padding(JengaTheme.spacing.lg)) {
            Column(modifier = Modifier.clip(JengaTheme.shapes.card).background(JengaTheme.colors.surface)) {
                JengaDropdownMenuItem(
                    text = "Edit",
                    onClick = {},
                    leadingIcon = { JengaIcon(JengaIcons.Pencil, contentDescription = null) },
                )
                JengaDropdownMenuItem(
                    text = "Close ticket",
                    onClick = {},
                    leadingIcon = { JengaIcon(JengaIcons.Check, contentDescription = null) },
                    enabled = false,
                    supportingText = "Finish the open work order first",
                )
                JengaDropdownMenuDivider()
                JengaDropdownMenuItem(
                    text = "Cancel ticket",
                    onClick = {},
                    leadingIcon = { JengaIcon(JengaIcons.XCircle, contentDescription = null) },
                    tone = JengaDropdownMenuItemTone.Danger,
                )
            }
        }
    }
}
