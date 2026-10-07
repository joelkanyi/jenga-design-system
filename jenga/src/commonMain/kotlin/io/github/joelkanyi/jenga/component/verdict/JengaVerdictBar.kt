package io.github.joelkanyi.jenga.component.verdict

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.lerp
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import dev.drewhamilton.poko.Poko
import io.github.joelkanyi.jenga.component.action.JengaAction
import io.github.joelkanyi.jenga.component.text.JengaText
import io.github.joelkanyi.jenga.theme.JengaTheme

/** The verdict a [JengaVerdictBar] conveys; drives its color from theme tokens. */
public enum class JengaVerdictTone {
    /** A good outcome, on a rich success band. */
    Positive,

    /** Needs attention, on a rich warning band. */
    Caution,

    /** A bad outcome, on a rich error band. */
    Negative,

    /** A branded highlight, on a rich brand band. */
    Brand,

    /** A calm summary on the surface color, for figures that are informative rather than a verdict. */
    Neutral,
}

/** Resolved colors for a [JengaVerdictBar]. Override via [JengaVerdictBarDefaults.colors]. */
@Poko
@Immutable
public class JengaVerdictBarColors(
    public val fill: Brush,
    public val amount: Color,
    public val accent: Color,
    public val muted: Color,
    public val trackFill: Color,
    public val track: Color,
    public val trackComplete: Color = trackFill,
) {
    public fun copy(
        fill: Brush = this.fill,
        amount: Color = this.amount,
        accent: Color = this.accent,
        muted: Color = this.muted,
        trackFill: Color = this.trackFill,
        track: Color = this.track,
        trackComplete: Color = this.trackComplete,
    ): JengaVerdictBarColors = JengaVerdictBarColors(fill, amount, accent, muted, trackFill, track, trackComplete)
}

/** Defaults and token mappings for [JengaVerdictBar]. */
public object JengaVerdictBarDefaults {
    /** Progress bar height. */
    public val TrackHeight: Dp = 8.dp

    /** Default banner shape. */
    public val shape: Shape
        @Composable get() = JengaTheme.shapes.cardLarge

    /** Padding inside the banner. */
    public val contentPadding: PaddingValues
        @Composable get() = PaddingValues(JengaTheme.spacing.lg)

    /** Amount text style. */
    public val amountStyle: TextStyle
        @Composable get() = JengaTheme.typography.display

    /** Amount suffix text style. */
    public val amountSuffixStyle: TextStyle
        @Composable get() = JengaTheme.typography.titleMedium

    /**
     * Themed colors for [tone]. The verdict hero reads as a rich, saturated
     * band in **both** schemes (a decision surface, not a card), with light
     * text, so the fill deepens the tone color and text stays bright.
     */
    @Composable
    public fun colors(tone: JengaVerdictTone): JengaVerdictBarColors {
        val c = JengaTheme.colors
        val base = when (tone) {
            JengaVerdictTone.Positive -> c.success
            JengaVerdictTone.Caution -> c.warning
            JengaVerdictTone.Negative -> c.error
            JengaVerdictTone.Brand -> c.brand
            JengaVerdictTone.Neutral -> return JengaVerdictBarColors(
                fill = SolidColor(c.surface),
                amount = c.textPrimary,
                accent = c.textSecondary,
                muted = c.textSecondary,
                trackFill = c.brand,
                track = c.surfaceSunk,
                trackComplete = c.success,
            )
        }
        // Light schemes start from an already-dark status color; dark schemes
        // start from a bright one; deepen each into the same rich band, using
        // the theme's own scrim colour (opaque) as the dark anchor.
        val ink = c.scrim.copy(alpha = 1f)
        val topMix = if (c.isLight) 0.12f else 0.50f
        val bottomMix = if (c.isLight) 0.38f else 0.70f
        val accent = lerp(base, c.onOverlay, if (c.isLight) 0.62f else 0.45f)
        return JengaVerdictBarColors(
            fill = Brush.linearGradient(
                listOf(lerp(base, ink, topMix), lerp(base, ink, bottomMix)),
            ),
            amount = c.onOverlay,
            accent = accent,
            muted = c.onOverlayMuted,
            trackFill = accent,
            track = c.onOverlay.copy(alpha = 0.18f),
            trackComplete = accent,
        )
    }
}

/**
 * The two bottom captions of a [JengaVerdictBar]: a [start] line and an optional
 * [end] line, bundled so the honest pair reads as one footnote.
 */
@Poko
@Immutable
public class JengaVerdictSublines(
    public val start: String,
    public val end: String? = null,
) {
    public fun copy(
        start: String = this.start,
        end: String? = this.end,
    ): JengaVerdictSublines = JengaVerdictSublines(start, end)
}

/**
 * A full-width verdict banner: a tone-colored band with an optional [label], a
 * big [amount] (with optional [amountSuffix]), an optional [progress] bar, and an
 * optional pair of honest [sublines]. A one-glance "where do I stand" summary
 * (budget, goal, quota). Generic; numbers and copy are the caller's, color
 * follows [tone].
 *
 * @param amount the headline value (e.g. "KES 1,190").
 * @param modifier the [Modifier] for the banner.
 * @param amountSuffix a small word after the amount (e.g. "left").
 * @param tone the verdict color.
 * @param label an optional eyebrow above the amount.
 * @param progress an optional 0..1 progress fraction; null hides the bar.
 * @param sublines the optional bottom start/end captions.
 * @param action optional trailing header action (label plus handler).
 * @param colors the color set; defaults to [JengaVerdictBarDefaults.colors] for [tone].
 * @param shape the banner shape.
 * @param contentPadding padding inside the banner.
 * @param trackHeight the progress bar height.
 * @param amountStyle the [amount] text style.
 * @param amountSuffixStyle the [amountSuffix] text style.
 */
@Composable
public fun JengaVerdictBar(
    amount: String,
    modifier: Modifier = Modifier,
    amountSuffix: String? = null,
    tone: JengaVerdictTone = JengaVerdictTone.Positive,
    label: String? = null,
    progress: Float? = null,
    sublines: JengaVerdictSublines? = null,
    action: JengaAction? = null,
    colors: JengaVerdictBarColors = JengaVerdictBarDefaults.colors(tone),
    shape: Shape = JengaVerdictBarDefaults.shape,
    contentPadding: PaddingValues = JengaVerdictBarDefaults.contentPadding,
    trackHeight: Dp = JengaVerdictBarDefaults.TrackHeight,
    amountStyle: TextStyle = JengaVerdictBarDefaults.amountStyle,
    amountSuffixStyle: TextStyle = JengaVerdictBarDefaults.amountSuffixStyle,
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .clip(shape)
            .background(colors.fill)
            .padding(contentPadding),
        verticalArrangement = Arrangement.spacedBy(JengaTheme.spacing.sm),
    ) {
        if (label != null || action != null) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                if (label != null) {
                    JengaText(
                        text = label,
                        style = JengaTheme.typography.bodySmall,
                        color = colors.accent,
                        modifier = Modifier.weight(1f),
                        maxLines = 1,
                    )
                } else {
                    Box(Modifier.weight(1f))
                }
                if (action != null) {
                    JengaText(
                        text = action.label,
                        style = JengaTheme.typography.bodySmall,
                        color = colors.muted,
                        maxLines = 1,
                        modifier = Modifier.clickable(onClick = action.onClick),
                    )
                }
            }
        }
        Row(verticalAlignment = Alignment.Bottom) {
            JengaText(text = amount, style = amountStyle, color = colors.amount, maxLines = 1)
            if (amountSuffix != null) {
                JengaText(
                    text = " $amountSuffix",
                    style = amountSuffixStyle,
                    color = colors.accent,
                    maxLines = 1,
                    modifier = Modifier.padding(bottom = JengaTheme.spacing.xs),
                )
            }
        }
        if (progress != null) {
            Box(
                Modifier
                    .fillMaxWidth()
                    .height(trackHeight)
                    .clip(RoundedCornerShape(percent = 50))
                    .background(colors.track),
            ) {
                Box(
                    Modifier
                        .fillMaxWidth(progress.coerceIn(0f, 1f))
                        .height(trackHeight)
                        .clip(RoundedCornerShape(percent = 50))
                        .background(if (progress >= 1f) colors.trackComplete else colors.trackFill),
                )
            }
        }
        if (sublines != null) {
            val end = sublines.end
            Row(modifier = Modifier.fillMaxWidth()) {
                JengaText(
                    text = sublines.start,
                    style = JengaTheme.typography.caption,
                    color = colors.muted,
                    modifier = Modifier.weight(1f),
                    maxLines = 1,
                )
                if (end != null) {
                    JengaText(text = end, style = JengaTheme.typography.caption, color = colors.muted, maxLines = 1)
                }
            }
        }
    }
}
