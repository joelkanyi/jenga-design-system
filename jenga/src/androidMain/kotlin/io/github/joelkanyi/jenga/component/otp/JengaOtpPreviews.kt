package io.github.joelkanyi.jenga.component.otp

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import io.github.joelkanyi.jenga.component.button.JengaButton
import io.github.joelkanyi.jenga.component.button.JengaButtonVariant
import io.github.joelkanyi.jenga.component.layout.JengaStack
import io.github.joelkanyi.jenga.component.text.JengaText
import io.github.joelkanyi.jenga.component.textfield.JengaTextFieldStatus
import io.github.joelkanyi.jenga.core.preview.JengaBlockPreviews
import io.github.joelkanyi.jenga.core.preview.RtlPreview
import io.github.joelkanyi.jenga.theme.JengaTheme
import kotlin.time.Duration
import kotlin.time.Duration.Companion.seconds

@JengaBlockPreviews
@Composable
internal fun JengaOtpFieldPreview() {
    JengaTheme { OtpFieldShowcase() }
}

@Preview(name = "RTL", showBackground = true)
@Composable
internal fun JengaOtpFieldRtlPreview() {
    JengaTheme { RtlPreview { OtpFieldShowcase() } }
}

@Composable
private fun OtpFieldShowcase() {
    JengaStack(
        space = JengaTheme.spacing.lg,
        modifier = Modifier
            .background(JengaTheme.colors.background)
            .padding(JengaTheme.spacing.lg),
    ) {
        JengaOtpField(value = "", onValueChange = {}, contentDescription = "Code")
        JengaOtpField(value = "1234", onValueChange = {}, contentDescription = "Code")
        JengaOtpField(
            value = "123456",
            onValueChange = {},
            contentDescription = "Code",
            status = JengaTextFieldStatus.Error,
            supportingText = "That code is not right. Try again.",
        )
        JengaOtpField(
            value = "123456",
            onValueChange = {},
            contentDescription = "Code",
            status = JengaTextFieldStatus.Success,
            supportingText = "Verified",
        )
        JengaOtpField(value = "1234", onValueChange = {}, contentDescription = "Code", enabled = false)
    }
}

@JengaBlockPreviews
@Composable
internal fun JengaOtpFieldStylesPreview() {
    JengaTheme {
        JengaStack(
            space = JengaTheme.spacing.lg,
            modifier = Modifier
                .background(JengaTheme.colors.background)
                .padding(JengaTheme.spacing.lg),
        ) {
            JengaOtpField(value = "12", onValueChange = {}, contentDescription = "Code", style = JengaOtpCellStyle.Filled)
            JengaOtpField(value = "12", onValueChange = {}, contentDescription = "Code", style = JengaOtpCellStyle.Underline)
            JengaOtpField(value = "123", onValueChange = {}, contentDescription = "Code", groupSize = 3)
            JengaOtpField(value = "12", onValueChange = {}, contentDescription = "PIN", length = 4, masked = true)
            JengaOtpField(
                value = "A7K",
                onValueChange = {},
                contentDescription = "Code",
                length = 5,
                inputType = JengaOtpInputType.Alphanumeric,
            )
        }
    }
}

@JengaBlockPreviews
@Composable
internal fun JengaOtpFieldCustomCellPreview() {
    JengaTheme {
        Box(
            modifier = Modifier
                .background(JengaTheme.colors.background)
                .padding(JengaTheme.spacing.lg),
        ) {
            JengaOtpField(
                value = "482",
                onValueChange = {},
                contentDescription = "Code",
                length = 4,
                cell = { state ->
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(
                                if (state.isFilled) JengaTheme.colors.brandSubtle else JengaTheme.colors.surfaceSunk,
                                JengaTheme.shapes.pill,
                            ),
                        contentAlignment = Alignment.Center,
                    ) {
                        JengaText(
                            text = state.char?.toString() ?: "",
                            style = JengaTheme.typography.titleLarge,
                            color = JengaTheme.colors.onBrandSubtle,
                        )
                    }
                },
            )
        }
    }
}

@JengaBlockPreviews
@Composable
internal fun JengaOtpFieldNarrowPreview() {
    JengaTheme {
        Box(
            modifier = Modifier
                .background(JengaTheme.colors.background)
                .padding(JengaTheme.spacing.sm)
                .width(240.dp),
        ) {
            JengaOtpField(value = "123", onValueChange = {}, contentDescription = "Code", modifier = Modifier.fillMaxWidth())
        }
    }
}

@JengaBlockPreviews
@Composable
internal fun JengaOtpResendPreview() {
    JengaTheme {
        JengaStack(
            space = JengaTheme.spacing.sm,
            modifier = Modifier
                .background(JengaTheme.colors.background)
                .padding(JengaTheme.spacing.lg),
        ) {
            ResendSample(remaining = 299.seconds)
            ResendSample(remaining = Duration.ZERO)
            ResendSample(remaining = Duration.ZERO, sending = true)
        }
    }
}

@Composable
private fun ResendSample(remaining: Duration, sending: Boolean = false) {
    JengaOtpResend(
        remaining = remaining,
        onResend = {},
        countdownText = { "Resend code in ${JengaOtpResendDefaults.formatRemaining(it)}" },
        resendText = "Resend code",
        sending = sending,
    )
}

@JengaBlockPreviews
@Composable
internal fun JengaOtpVerificationPreview() {
    JengaTheme { VerificationShowcase(code = "", verifying = false) }
}

@JengaBlockPreviews
@Composable
internal fun JengaOtpVerificationVerifyingPreview() {
    JengaTheme { VerificationShowcase(code = "123456", verifying = true) }
}

@Composable
private fun VerificationShowcase(code: String, verifying: Boolean) {
    JengaOtpVerification(
        title = "Enter the code",
        description = buildAnnotatedString {
            append("We sent a 6-digit code to ")
            withStyle(SpanStyle(fontWeight = FontWeight.SemiBold)) { append("+254 712 345 678") }
        },
        modifier = Modifier.background(JengaTheme.colors.background),
        field = {
            JengaOtpField(
                value = code,
                onValueChange = {},
                contentDescription = "Verification code",
                readOnly = verifying,
            )
        },
        verifying = verifying,
        verifyingText = "Verifying…",
        resend = { ResendSample(remaining = 272.seconds) },
        actions = {
            JengaButton(
                text = "Verify",
                onClick = {},
                variant = JengaButtonVariant.Primary,
                modifier = Modifier.fillMaxWidth(),
                enabled = code.length == 6 && !verifying,
            )
        },
    )
}
