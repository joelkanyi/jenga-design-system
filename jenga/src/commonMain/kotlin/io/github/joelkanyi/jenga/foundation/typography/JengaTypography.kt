package io.github.joelkanyi.jenga.foundation.typography

import androidx.compose.runtime.Immutable
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.LineHeightStyle
import androidx.compose.ui.unit.sp
import dev.drewhamilton.poko.Poko

/**
 * **Typography token set**: the Jenga type scale.
 *
 * Sizes are fixed-point ports of the frontend's fluid (`clamp`) scale, tuned
 * for mobile. Note the negative [TextStyle.letterSpacing] that tightens as type
 * gets larger (a deliberate display-type refinement carried over from the web
 * brand). Read via `JengaTheme.typography`; override per-app with [jengaTypography].
 */
@Poko
@Immutable
public class JengaTypography(
    /** Hero / marketing headline. */
    public val display: TextStyle,
    /** Screen title (h1). */
    public val headingLarge: TextStyle,
    /** Section heading (h2). */
    public val headingMedium: TextStyle,
    /** Sub-section heading (h3). */
    public val headingSmall: TextStyle,
    /** Card / list title (h4). */
    public val titleLarge: TextStyle,
    /** Medium title / row heading. */
    public val titleMedium: TextStyle,
    /** Dense title / emphasized body. */
    public val titleSmall: TextStyle,
    /** Lead paragraph / prominent body. */
    public val bodyLarge: TextStyle,
    /** Default body text. */
    public val bodyMedium: TextStyle,
    /** Secondary / supporting body text. */
    public val bodySmall: TextStyle,
    /** Uppercase eyebrow / field label (use with letterSpacing as-is). */
    public val label: TextStyle,
    /** Smallest supporting text (timestamps, captions). */
    public val caption: TextStyle,
    /** Button / call-to-action label. */
    public val button: TextStyle,
    /** Monospaced text for codes, serials and IDs. Defaults to [bodyMedium] metrics. */
    public val mono: TextStyle = bodyMedium.copy(fontFamily = FontFamily.Monospace),
) {
    public fun copy(
        display: TextStyle = this.display,
        headingLarge: TextStyle = this.headingLarge,
        headingMedium: TextStyle = this.headingMedium,
        headingSmall: TextStyle = this.headingSmall,
        titleLarge: TextStyle = this.titleLarge,
        titleMedium: TextStyle = this.titleMedium,
        titleSmall: TextStyle = this.titleSmall,
        bodyLarge: TextStyle = this.bodyLarge,
        bodyMedium: TextStyle = this.bodyMedium,
        bodySmall: TextStyle = this.bodySmall,
        label: TextStyle = this.label,
        caption: TextStyle = this.caption,
        button: TextStyle = this.button,
        mono: TextStyle = this.mono,
    ): JengaTypography = JengaTypography(
        display,
        headingLarge,
        headingMedium,
        headingSmall,
        titleLarge,
        titleMedium,
        titleSmall,
        bodyLarge,
        bodyMedium,
        bodySmall,
        label,
        caption,
        button,
        mono,
    )
}

private val defaultLineHeightStyle = LineHeightStyle(
    alignment = LineHeightStyle.Alignment.Center,
    trim = LineHeightStyle.Trim.None,
)

/**
 * Builds the default Jenga type scale.
 *
 * A brand may run two typefaces, one for display roles (display, heading*,
 * title*) and one for body text (body*, label, caption, button). Pass
 * [displayFontFamily] and [bodyFontFamily] separately for that; pass a single
 * [fontFamily] to use one face across the whole scale (it defaults both).
 *
 * @param fontFamily one face for the whole scale, and the default for the two
 *   below. Defaults to [FontFamily.Default]; `JengaTheme` injects the Outfit
 *   brand family (see [rememberJengaFontFamily]) when none is supplied.
 * @param displayFontFamily the face for display, heading and title roles.
 * @param bodyFontFamily the face for body, label, caption and button roles.
 * @param monoFontFamily the face for the mono role; [FontFamily.Monospace] by default.
 */
public fun jengaTypography(
    fontFamily: FontFamily = FontFamily.Default,
    displayFontFamily: FontFamily = fontFamily,
    bodyFontFamily: FontFamily = fontFamily,
    monoFontFamily: FontFamily = FontFamily.Monospace,
): JengaTypography = JengaTypography(
    display = TextStyle(
        fontFamily = displayFontFamily,
        fontWeight = FontWeight.ExtraBold,
        fontSize = 40.sp,
        lineHeight = 44.sp,
        letterSpacing = (-0.8).sp,
        lineHeightStyle = defaultLineHeightStyle,
    ),
    headingLarge = TextStyle(
        fontFamily = displayFontFamily,
        fontWeight = FontWeight.Bold,
        fontSize = 32.sp,
        lineHeight = 38.sp,
        letterSpacing = (-0.6).sp,
        lineHeightStyle = defaultLineHeightStyle,
    ),
    headingMedium = TextStyle(
        fontFamily = displayFontFamily,
        fontWeight = FontWeight.Bold,
        fontSize = 26.sp,
        lineHeight = 32.sp,
        letterSpacing = (-0.4).sp,
        lineHeightStyle = defaultLineHeightStyle,
    ),
    headingSmall = TextStyle(
        fontFamily = displayFontFamily,
        fontWeight = FontWeight.SemiBold,
        fontSize = 22.sp,
        lineHeight = 28.sp,
        letterSpacing = (-0.2).sp,
        lineHeightStyle = defaultLineHeightStyle,
    ),
    titleLarge = TextStyle(
        fontFamily = displayFontFamily,
        fontWeight = FontWeight.SemiBold,
        fontSize = 18.sp,
        lineHeight = 24.sp,
        letterSpacing = (-0.1).sp,
        lineHeightStyle = defaultLineHeightStyle,
    ),
    titleMedium = TextStyle(
        fontFamily = displayFontFamily,
        fontWeight = FontWeight.SemiBold,
        fontSize = 17.sp,
        lineHeight = 24.sp,
        letterSpacing = 0.sp,
        lineHeightStyle = defaultLineHeightStyle,
    ),
    titleSmall = TextStyle(
        fontFamily = displayFontFamily,
        fontWeight = FontWeight.SemiBold,
        fontSize = 16.sp,
        lineHeight = 22.sp,
        letterSpacing = 0.sp,
        lineHeightStyle = defaultLineHeightStyle,
    ),
    bodyLarge = TextStyle(
        fontFamily = bodyFontFamily,
        fontWeight = FontWeight.Normal,
        fontSize = 18.sp,
        lineHeight = 28.sp,
        letterSpacing = 0.sp,
        lineHeightStyle = defaultLineHeightStyle,
    ),
    bodyMedium = TextStyle(
        fontFamily = bodyFontFamily,
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp,
        lineHeight = 26.sp,
        letterSpacing = 0.sp,
        lineHeightStyle = defaultLineHeightStyle,
    ),
    bodySmall = TextStyle(
        fontFamily = bodyFontFamily,
        fontWeight = FontWeight.Normal,
        fontSize = 14.sp,
        lineHeight = 20.sp,
        letterSpacing = 0.sp,
        lineHeightStyle = defaultLineHeightStyle,
    ),
    label = TextStyle(
        fontFamily = bodyFontFamily,
        fontWeight = FontWeight.SemiBold,
        fontSize = 12.sp,
        lineHeight = 16.sp,
        letterSpacing = 1.0.sp,
        lineHeightStyle = defaultLineHeightStyle,
    ),
    caption = TextStyle(
        fontFamily = bodyFontFamily,
        fontWeight = FontWeight.Normal,
        fontSize = 11.sp,
        lineHeight = 14.sp,
        letterSpacing = 0.sp,
        lineHeightStyle = defaultLineHeightStyle,
    ),
    button = TextStyle(
        fontFamily = bodyFontFamily,
        fontWeight = FontWeight.SemiBold,
        fontSize = 16.sp,
        lineHeight = 20.sp,
        letterSpacing = 0.2.sp,
        lineHeightStyle = defaultLineHeightStyle,
    ),
    mono = TextStyle(
        fontFamily = monoFontFamily,
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp,
        lineHeight = 26.sp,
        letterSpacing = 0.sp,
        lineHeightStyle = defaultLineHeightStyle,
    ),
)

/**
 * Returns this style with tabular (fixed-width) figures, so digits line up in
 * columns of amounts, counts and timers. Keeps any feature settings already set.
 */
public fun TextStyle.withTabularFigures(): TextStyle {
    val existing = fontFeatureSettings
    val settings = when {
        existing.isNullOrBlank() -> "tnum"
        existing.contains("tnum") -> existing
        else -> "$existing, tnum"
    }
    return copy(fontFeatureSettings = settings)
}

/**
 * Recommended per-tier caps on the system font scale. Larger type is capped
 * tighter (it scales the layout more);
 * body and label allow the full range. Pass these to `JengaText(maxFontScale = …)`.
 */
public object JengaTypographyDefaults {
    /** Display type: cap at 125%. */
    public const val DisplayMaxFontScale: Float = 1.25f

    /** Headings: cap at 150%. */
    public const val HeadingMaxFontScale: Float = 1.5f

    /** Titles: cap at 150%. */
    public const val TitleMaxFontScale: Float = 1.5f

    /** Body text: allow up to 200%. */
    public const val BodyMaxFontScale: Float = 2.0f

    /** Labels/captions: allow up to 200%. */
    public const val LabelMaxFontScale: Float = 2.0f
}
