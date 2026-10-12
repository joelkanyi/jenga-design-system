package io.github.joelkanyi.jenga.component.otp

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import dev.drewhamilton.poko.Poko
import io.github.joelkanyi.jenga.component.link.JengaLink
import io.github.joelkanyi.jenga.component.link.JengaLinkColors
import io.github.joelkanyi.jenga.component.progress.JengaCircularProgressIndeterminate
import io.github.joelkanyi.jenga.component.text.JengaText
import io.github.joelkanyi.jenga.theme.JengaTheme
import kotlinx.coroutines.delay
import kotlin.time.Duration
import kotlin.time.Duration.Companion.milliseconds
import kotlin.time.TimeMark
import kotlin.time.TimeSource

/** Resolved colors for a [JengaOtpResend]. Override via [JengaOtpResendDefaults.colors]. */
@Poko
@Immutable
public class JengaOtpResendColors(
    public val countdown: Color,
    public val action: Color,
    public val disabledAction: Color,
    public val progress: Color,
) {
    public fun copy(
        countdown: Color = this.countdown,
        action: Color = this.action,
        disabledAction: Color = this.disabledAction,
        progress: Color = this.progress,
    ): JengaOtpResendColors = JengaOtpResendColors(countdown, action, disabledAction, progress)
}

/** Defaults and token mappings for [JengaOtpResend]. */
public object JengaOtpResendDefaults {
    /** Text style for the countdown and the resend action. */
    public val textStyle: TextStyle
        @Composable get() = JengaTheme.typography.bodyMedium

    /** Padding around the row. */
    public val contentPadding: PaddingValues
        @Composable get() = PaddingValues(vertical = JengaTheme.spacing.xs)

    /** Themed colors. */
    @Composable
    public fun colors(): JengaOtpResendColors {
        val c = JengaTheme.colors
        return JengaOtpResendColors(
            countdown = c.textMuted,
            action = c.brand,
            disabledAction = c.textMuted,
            progress = c.brand,
        )
    }

    /**
     * Formats [remaining] as `M:SS`, rounding up to the next whole second so the countdown never
     * shows `0:00` while time is left. Hours roll into minutes (`75:00`).
     */
    public fun formatRemaining(remaining: Duration): String {
        val totalSeconds = (remaining.inWholeMilliseconds.coerceAtLeast(0) + 999) / 1000
        val minutes = totalSeconds / 60
        val seconds = totalSeconds % 60
        return "$minutes:${seconds.toString().padStart(2, '0')}"
    }
}

/**
 * The "resend code" row under a [JengaOtpField]: a countdown while [remaining] is positive, then a
 * resend action.
 *
 * Stateless: the caller owns the time. Use [rememberJengaCountdown] for a screen-local countdown,
 * or pass a value computed from a deadline held in a view model so it survives configuration
 * changes.
 *
 * @sample io.github.joelkanyi.jenga.samples.JengaOtpResendSample
 *
 * @param remaining time left before a new code can be requested.
 * @param onResend called when the resend action is tapped.
 * @param countdownText the text shown while waiting, e.g.
 *   `{ "Resend code in ${JengaOtpResendDefaults.formatRemaining(it)}" }`.
 * @param resendText the resend action label.
 * @param modifier the [Modifier] for the row.
 * @param sending whether a resend is in progress; shows a spinner and blocks repeat taps.
 * @param enabled whether the resend action can be tapped.
 * @param textStyle the countdown and action text style.
 * @param colors the color set; defaults to [JengaOtpResendDefaults.colors].
 * @param contentPadding padding around the row.
 */
@Composable
public fun JengaOtpResend(
    remaining: Duration,
    onResend: () -> Unit,
    countdownText: (Duration) -> String,
    resendText: String,
    modifier: Modifier = Modifier,
    sending: Boolean = false,
    enabled: Boolean = true,
    textStyle: TextStyle = JengaOtpResendDefaults.textStyle,
    colors: JengaOtpResendColors = JengaOtpResendDefaults.colors(),
    contentPadding: PaddingValues = JengaOtpResendDefaults.contentPadding,
) {
    val waiting = remaining.isPositive()
    Box(
        modifier = modifier
            .padding(contentPadding)
            .defaultMinSize(minHeight = JengaTheme.sizing.minTouchTarget),
        contentAlignment = Alignment.Center,
    ) {
        val duration = JengaTheme.motion.durationMedium
        AnimatedContent(
            targetState = waiting,
            transitionSpec = {
                fadeIn(tween(duration)).togetherWith(fadeOut(tween(duration)))
            },
            contentAlignment = Alignment.Center,
            label = "otpResend",
        ) { isWaiting ->
            if (isWaiting) {
                JengaText(text = countdownText(remaining), style = textStyle, color = colors.countdown)
            } else {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(JengaTheme.spacing.sm),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    if (sending) {
                        JengaCircularProgressIndeterminate(
                            size = 16.dp,
                            strokeWidth = 2.dp,
                            color = colors.progress,
                        )
                    }
                    JengaLink(
                        text = resendText,
                        onClick = onResend,
                        style = textStyle,
                        colors = JengaLinkColors(content = colors.action, disabledContent = colors.disabledAction),
                        enabled = enabled && !sending,
                        underline = false,
                    )
                }
            }
        }
    }
}

/**
 * A countdown for [JengaOtpResend], measured from a start mark rather than by counting ticks, so
 * it stays correct when the app is paused or frames are dropped.
 *
 * Create it with [rememberJengaCountdown].
 */
@Stable
public class JengaCountdownState internal constructor(
    private val duration: Duration,
    private val timeSource: TimeSource,
) {
    private var start: TimeMark by mutableStateOf(timeSource.markNow())

    /** Time left, never negative. */
    public var remaining: Duration by mutableStateOf(duration)
        private set

    /** Whether time is left. */
    public val isRunning: Boolean
        get() = remaining.isPositive()

    /** Starts the countdown again from the full duration, e.g. after a code is resent. */
    public fun restart() {
        start = timeSource.markNow()
        remaining = duration
    }

    internal fun update() {
        remaining = (duration - start.elapsedNow()).coerceAtLeast(Duration.ZERO)
    }

    internal fun generation(): Any = start
}

/**
 * Remembers a [JengaCountdownState] that starts at [duration] and ticks down while composed.
 *
 * It restarts when [key] changes. It does not survive configuration changes or process death; hold
 * a deadline in a view model for that and pass the remaining time to [JengaOtpResend] directly.
 *
 * @param duration how long to count down from.
 * @param key restarts the countdown when it changes.
 * @param timeSource the clock; pass a `TestTimeSource` in tests.
 */
@Composable
public fun rememberJengaCountdown(
    duration: Duration,
    key: Any? = null,
    timeSource: TimeSource = TimeSource.Monotonic,
): JengaCountdownState {
    val state = remember(duration, key, timeSource) { JengaCountdownState(duration, timeSource) }
    LaunchedEffect(state, state.generation()) {
        state.update()
        while (state.isRunning) {
            delay(CountdownTick)
            state.update()
        }
    }
    return state
}

private val CountdownTick = 200.milliseconds
