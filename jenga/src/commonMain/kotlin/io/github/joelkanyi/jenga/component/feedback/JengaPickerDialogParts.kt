package io.github.joelkanyi.jenga.component.feedback

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import io.github.joelkanyi.jenga.component.button.JengaButton
import io.github.joelkanyi.jenga.component.button.JengaButtonSize
import io.github.joelkanyi.jenga.component.button.JengaButtonVariant
import io.github.joelkanyi.jenga.theme.JengaTheme

@Composable
internal fun Modifier.jengaDialogCard(): Modifier {
    val shape = JengaDialogDefaults.shape
    val outline = JengaDialogDefaults.border
    return fillMaxWidth()
        .clip(shape)
        .background(JengaDialogDefaults.colors().container)
        .then(if (outline == null) Modifier else Modifier.border(outline, shape))
        .padding(JengaTheme.spacing.lg)
}

@Composable
internal fun darkThemeHairline(): BorderStroke? = if (JengaTheme.colors.isLight) null else BorderStroke(1.dp, JengaTheme.colors.border)

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
