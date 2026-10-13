package io.github.joelkanyi.jenga.component.otp

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import io.github.joelkanyi.jenga.component.progress.JengaCircularProgressIndeterminate
import io.github.joelkanyi.jenga.component.text.JengaText
import io.github.joelkanyi.jenga.theme.JengaTheme

/** Defaults for [JengaOtpVerification]. */
public object JengaOtpVerificationDefaults {
    /** Padding around the block. */
    public val contentPadding: PaddingValues
        @Composable get() = PaddingValues(JengaTheme.spacing.xl)

    /** Vertical space between the block's sections. */
    public val spacing: Dp
        @Composable get() = JengaTheme.spacing.lg

    /** Title text style. */
    public val titleStyle: TextStyle
        @Composable get() = JengaTheme.typography.headingSmall

    /** Description text style. */
    public val descriptionStyle: TextStyle
        @Composable get() = JengaTheme.typography.bodyMedium
}

/**
 * A complete code-verification block: optional header, title, description, the code field, a
 * "verifying" row, the resend row and actions, centered and spaced consistently.
 *
 * It is plain content with no surface of its own, so the same block is used in a screen, a
 * bottom sheet or a dialog.
 *
 * @sample io.github.joelkanyi.jenga.samples.JengaOtpVerificationSample
 *
 * @param title the heading, e.g. "Enter the code".
 * @param description where the code was sent; style parts of it (such as the phone number) with
 *   an [AnnotatedString].
 * @param field the code input, normally a [JengaOtpField].
 * @param modifier the [Modifier] for the block.
 * @param verifying whether the code is being checked; shows the verifying row.
 * @param verifyingText the text next to the spinner while [verifying], e.g. "Verifying…".
 * @param header optional content above the title, such as an illustration.
 * @param resend optional resend row, normally a [JengaOtpResend].
 * @param actions optional full-width actions at the bottom, such as a verify button.
 * @param contentPadding padding around the block.
 * @param spacing vertical space between sections.
 * @param titleStyle the title text style.
 * @param descriptionStyle the description text style.
 */
@Composable
public fun JengaOtpVerification(
    title: String,
    description: AnnotatedString,
    field: @Composable () -> Unit,
    modifier: Modifier = Modifier,
    verifying: Boolean = false,
    verifyingText: String? = null,
    header: (@Composable () -> Unit)? = null,
    resend: (@Composable () -> Unit)? = null,
    actions: (@Composable ColumnScope.() -> Unit)? = null,
    contentPadding: PaddingValues = JengaOtpVerificationDefaults.contentPadding,
    spacing: Dp = JengaOtpVerificationDefaults.spacing,
    titleStyle: TextStyle = JengaOtpVerificationDefaults.titleStyle,
    descriptionStyle: TextStyle = JengaOtpVerificationDefaults.descriptionStyle,
) {
    val c = JengaTheme.colors
    Column(
        modifier = modifier.padding(contentPadding),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(spacing),
    ) {
        header?.invoke()
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(JengaTheme.spacing.sm),
        ) {
            JengaText(text = title, style = titleStyle, color = c.textPrimary, textAlign = TextAlign.Center)
            JengaText(text = description, style = descriptionStyle, color = c.textSecondary, textAlign = TextAlign.Center)
        }
        field()
        AnimatedVisibility(
            visible = verifying,
            enter = fadeIn() + expandVertically(),
            exit = fadeOut() + shrinkVertically(),
        ) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(JengaTheme.spacing.sm),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                JengaCircularProgressIndeterminate(size = 16.dp, strokeWidth = 2.dp)
                if (verifyingText != null) {
                    JengaText(text = verifyingText, style = JengaTheme.typography.bodySmall, color = c.textSecondary)
                }
            }
        }
        resend?.invoke()
        if (actions != null) {
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(JengaTheme.spacing.sm),
                content = actions,
            )
        }
    }
}
