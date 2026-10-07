package io.github.joelkanyi.jenga.component.icon

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import io.github.joelkanyi.jenga.component.badge.JengaBadgeTone
import io.github.joelkanyi.jenga.component.layout.JengaInline
import io.github.joelkanyi.jenga.component.layout.JengaStack
import io.github.joelkanyi.jenga.core.preview.JengaBlockPreviews
import io.github.joelkanyi.jenga.core.preview.RtlPreview
import io.github.joelkanyi.jenga.theme.JengaTheme

// ---- Previews --------------------------------------------------------------

@JengaBlockPreviews
@Composable
internal fun JengaIconTilePreview() {
    JengaTheme { IconTileShowcase() }
}

@Preview(name = "RTL", showBackground = true)
@Composable
internal fun JengaIconTileRtlPreview() {
    JengaTheme { RtlPreview { IconTileShowcase() } }
}

@Composable
private fun IconTileShowcase() {
    JengaStack(
        modifier = Modifier
            .background(JengaTheme.colors.background)
            .padding(JengaTheme.spacing.xl),
    ) {
        JengaInline {
            JengaIconTile(JengaIcons.Wrench, contentDescription = null)
            JengaIconTile(JengaIcons.Package, contentDescription = null, tone = JengaBadgeTone.Brand)
            JengaIconTile(JengaIcons.CheckCircle, contentDescription = null, tone = JengaBadgeTone.Success)
            JengaIconTile(JengaIcons.Hourglass, contentDescription = null, tone = JengaBadgeTone.Warning)
            JengaIconTile(JengaIcons.XCircle, contentDescription = null, tone = JengaBadgeTone.Error)
            JengaIconTile(JengaIcons.Info, contentDescription = null, tone = JengaBadgeTone.Info)
        }
        JengaInline {
            JengaIconTile(JengaIcons.Truck, contentDescription = null, size = JengaIconTileSize.Small)
            JengaIconTile(JengaIcons.Truck, contentDescription = null, size = JengaIconTileSize.Medium)
            JengaIconTile(JengaIcons.Truck, contentDescription = null, size = JengaIconTileSize.Large)
        }
    }
}

@JengaBlockPreviews
@Composable
internal fun JengaIconTileCustomSizePreview() {
    JengaTheme {
        JengaInline(modifier = Modifier.background(JengaTheme.colors.background).padding(JengaTheme.spacing.xl)) {
            JengaIconTile(
                JengaIcons.Wrench,
                contentDescription = null,
                tone = JengaBadgeTone.Brand,
                shape = JengaTheme.shapes.md,
                tileSize = 40.dp,
                iconSize = 21.dp,
            )
            JengaIconTile(
                JengaIcons.Info,
                contentDescription = null,
                tone = JengaBadgeTone.Info,
                shape = JengaTheme.shapes.md,
                tileSize = 36.dp,
            )
        }
    }
}
