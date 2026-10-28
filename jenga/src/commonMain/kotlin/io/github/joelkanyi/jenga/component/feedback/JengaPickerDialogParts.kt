package io.github.joelkanyi.jenga.component.feedback

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import io.github.joelkanyi.jenga.component.button.JengaButton
import io.github.joelkanyi.jenga.component.button.JengaButtonSize
import io.github.joelkanyi.jenga.component.button.JengaButtonVariant
import io.github.joelkanyi.jenga.theme.JengaTheme

@Composable
internal fun Modifier.jengaDialogCard(): Modifier = fillMaxWidth()
    .clip(JengaDialogDefaults.shape)
    .background(JengaDialogDefaults.colors().container)
    .padding(JengaTheme.spacing.lg)

@Composable
internal fun JengaDialogActions(
    confirmLabel: String,
    dismissLabel: String,
    confirmEnabled: Boolean,
    onConfirm: () -> Unit,
    onDismiss: () -> Unit,
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(JengaTheme.spacing.sm, Alignment.End),
    ) {
        JengaButton(
            text = dismissLabel,
            onClick = onDismiss,
            variant = JengaButtonVariant.Ghost,
            size = JengaButtonSize.Small,
        )
        JengaButton(
            text = confirmLabel,
            onClick = onConfirm,
            size = JengaButtonSize.Small,
            enabled = confirmEnabled,
        )
    }
}
