package io.github.joelkanyi.jenga.component.icon

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import io.github.joelkanyi.jenga.component.layout.JengaWrap
import io.github.joelkanyi.jenga.core.preview.JengaBlockPreviews
import io.github.joelkanyi.jenga.core.preview.RtlPreview
import io.github.joelkanyi.jenga.theme.JengaTheme

// ---- Previews --------------------------------------------------------------

@JengaBlockPreviews
@Composable
internal fun JengaIconPreview() {
    JengaTheme { IconShowcase() }
}

@Preview(name = "RTL", showBackground = true)
@Composable
internal fun JengaIconRtlPreview() {
    JengaTheme { RtlPreview { IconShowcase() } }
}

@Composable
private fun IconShowcase() {
    JengaWrap(
        modifier = Modifier
            .background(JengaTheme.colors.background)
            .padding(JengaTheme.spacing.xl),
        space = JengaTheme.spacing.md,
    ) {
        JengaIcon(JengaIcons.Check, contentDescription = null, tint = JengaTheme.colors.success)
        JengaIcon(JengaIcons.Close, contentDescription = null, tint = JengaTheme.colors.error)
        JengaIcon(JengaIcons.Add, contentDescription = null)
        JengaIcon(JengaIcons.Search, contentDescription = null)
        JengaIcon(JengaIcons.ChevronRight, contentDescription = null)
        JengaIcon(JengaIcons.ArrowBack, contentDescription = null, tint = JengaTheme.colors.brand)
        JengaIcon(JengaIcons.Mail, contentDescription = null)
        JengaIcon(JengaIcons.Lock, contentDescription = null)
        JengaIcon(JengaIcons.Eye, contentDescription = null)
        JengaIcon(JengaIcons.EyeOff, contentDescription = null)
        JengaIcon(JengaIcons.CheckCircle, contentDescription = null, tint = JengaTheme.colors.success)
        JengaIcon(JengaIcons.Flash, contentDescription = null)
        JengaIcon(JengaIcons.FlashOff, contentDescription = null)
        JengaIcon(JengaIcons.Keyboard, contentDescription = null)
        JengaIcon(JengaIcons.History, contentDescription = null)
        JengaIcon(JengaIcons.Warning, contentDescription = null, tint = JengaTheme.colors.warning)
        JengaIcon(JengaIcons.Swap, contentDescription = null)
        JengaIcon(JengaIcons.Ban, contentDescription = null, tint = JengaTheme.colors.error)
        JengaIcon(JengaIcons.QrCode, contentDescription = null)
        JengaIcon(JengaIcons.Calendar, contentDescription = null)
        JengaIcon(JengaIcons.Chart, contentDescription = null)
        JengaIcon(JengaIcons.User, contentDescription = null)
        JengaIcon(JengaIcons.Volume, contentDescription = null)
        JengaIcon(JengaIcons.Vibrate, contentDescription = null)
        JengaIcon(JengaIcons.Sun, contentDescription = null)
        JengaIcon(JengaIcons.Cloud, contentDescription = null)
        JengaIcon(JengaIcons.CloudOff, contentDescription = null)
        JengaIcon(JengaIcons.Refresh, contentDescription = null)
        JengaIcon(JengaIcons.Bell, contentDescription = null)
        JengaIcon(JengaIcons.Trash, contentDescription = null, tint = JengaTheme.colors.error)
        JengaIcon(JengaIcons.Smartphone, contentDescription = null)
        JengaIcon(JengaIcons.Database, contentDescription = null)
        JengaIcon(JengaIcons.Logout, contentDescription = null)
        JengaIcon(JengaIcons.Shield, contentDescription = null, tint = JengaTheme.colors.brand)
        JengaIcon(JengaIcons.Settings, contentDescription = null)
        JengaIcon(JengaIcons.Phone, contentDescription = null)
        JengaIcon(JengaIcons.Copy, contentDescription = null)
        JengaIcon(JengaIcons.Download, contentDescription = null)
        JengaIcon(JengaIcons.Pencil, contentDescription = null)
        JengaIcon(JengaIcons.Camera, contentDescription = null)
        JengaIcon(JengaIcons.ImagePlus, contentDescription = null)
        JengaIcon(JengaIcons.Images, contentDescription = null)
        JengaIcon(JengaIcons.Send, contentDescription = null)
        JengaIcon(JengaIcons.MapPin, contentDescription = null)
        JengaIcon(JengaIcons.Package, contentDescription = null)
        JengaIcon(JengaIcons.Wrench, contentDescription = null)
        JengaIcon(JengaIcons.Key, contentDescription = null)
        JengaIcon(JengaIcons.Ticket, contentDescription = null)
        JengaIcon(JengaIcons.Receipt, contentDescription = null)
        JengaIcon(JengaIcons.Banknote, contentDescription = null)
        JengaIcon(JengaIcons.Truck, contentDescription = null)
        JengaIcon(JengaIcons.MoreHorizontal, contentDescription = null)
        JengaIcon(JengaIcons.XCircle, contentDescription = null)
        JengaIcon(JengaIcons.AlertCircle, contentDescription = null)
        JengaIcon(JengaIcons.Cpu, contentDescription = null)
        JengaIcon(JengaIcons.Component, contentDescription = null)
        JengaIcon(JengaIcons.Hourglass, contentDescription = null)
        JengaIcon(JengaIcons.UserX, contentDescription = null)
        JengaIcon(JengaIcons.Users, contentDescription = null)
        JengaIcon(JengaIcons.UserCog, contentDescription = null)
    }
}
