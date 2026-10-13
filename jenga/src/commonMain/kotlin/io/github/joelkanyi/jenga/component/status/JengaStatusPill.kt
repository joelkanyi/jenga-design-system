package io.github.joelkanyi.jenga.component.status

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.Immutable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import dev.drewhamilton.poko.Poko
import io.github.joelkanyi.jenga.component.badge.JengaBadgeTone
import io.github.joelkanyi.jenga.component.progress.JengaCircularProgressIndeterminate
import io.github.joelkanyi.jenga.component.text.JengaText
import io.github.joelkanyi.jenga.theme.JengaTheme
import io.github.joelkanyi.jenga.theme.LocalJengaContentColor

/** Resolved colors for a [JengaStatusPill]. */
@Poko
@Immutable
public class JengaStatusPillColors(
    public val container: Color,
    public val content: Color,
    public val accent: Color,
) {
    public fun copy(
        container: Color = this.container,
        content: Color = this.content,
        accent: Color = this.accent,
    ): JengaStatusPillColors = JengaStatusPillColors(container, content, accent)
}

/** Defaults and token mappings for [JengaStatusPill]. */
/**
 * Theme-wide style for every [JengaStatusPill], set through
 * [io.github.joelkanyi.jenga.theme.JengaComponentStyles.statusPill]. A `null` value keeps Jenga's
 * default; arguments passed to a call still win.
 *
 * @property indicatorSize the diameter of the leading dot or spinner.
 * @property minHeight the minimum pill height.
 * @property contentPadding padding inside the pill.
 * @property textStyle the label text style.
 */
@Poko
@Immutable
public class JengaStatusPillStyle(
    public val indicatorSize: Dp? = null,
    public val minHeight: Dp? = null,
    public val contentPadding: PaddingValues? = null,
    public val textStyle: TextStyle? = null,
) {
    public fun copy(
        indicatorSize: Dp? = this.indicatorSize,
        minHeight: Dp? = this.minHeight,
        contentPadding: PaddingValues? = this.contentPadding,
        textStyle: TextStyle? = this.textStyle,
    ): JengaStatusPillStyle = JengaStatusPillStyle(indicatorSize, minHeight, contentPadding, textStyle)
}

/**
 * Defaults for [JengaStatusPill]. The composable members follow the theme's [JengaStatusPillStyle];
 * the constants are Jenga's built-in values, and [JengaStatusPill] resolves the theme's values over them.
 */
public object JengaStatusPillDefaults {
    /** Diameter of the leading status dot / spinner. */
    public val IndicatorSize: Dp = 8.dp

    /** Minimum pill height. */
    public val MinHeight: Dp = 0.dp

    /** Padding inside the pill. */
    public val contentPadding: PaddingValues
        @Composable get() = JengaTheme.components.statusPill?.contentPadding
            ?: PaddingValues(horizontal = JengaTheme.spacing.md, vertical = JengaTheme.spacing.xs)

    /** Label text style. */
    public val textStyle: TextStyle
        @Composable get() = JengaTheme.components.statusPill?.textStyle ?: JengaTheme.typography.label

    /** Themed colors for the given [tone]. */
    @Composable
    public fun colors(tone: JengaBadgeTone): JengaStatusPillColors {
        val c = JengaTheme.colors
        return when (tone) {
            JengaBadgeTone.Success -> JengaStatusPillColors(c.successContainer, c.onSuccessContainer, c.success)
            JengaBadgeTone.Warning -> JengaStatusPillColors(c.warningContainer, c.onWarningContainer, c.warning)
            JengaBadgeTone.Error -> JengaStatusPillColors(c.errorContainer, c.onErrorContainer, c.error)
            JengaBadgeTone.Info -> JengaStatusPillColors(c.infoContainer, c.onInfoContainer, c.info)
            JengaBadgeTone.Brand -> JengaStatusPillColors(c.brandSubtle, c.onBrandSubtle, c.brand)
            JengaBadgeTone.Neutral -> JengaStatusPillColors(c.surfaceVariant, c.textSecondary, c.textMuted)
        }
    }
}

/**
 * A compact status pill: a leading dot (or a spinner while [loading]) plus a short
 * label, tinted by [tone]. Use it for live, glanceable state (connectivity, sync,
 * gate health) anywhere a [io.github.joelkanyi.jenga.component.badge.JengaBadge] is
 * too static. Optionally [onClick]able to open details.
 *
 * @sample io.github.joelkanyi.jenga.samples.JengaStatusPillSample
 *
 * @param label the status text.
 * @param modifier the [Modifier] for this pill.
 * @param tone the semantic tone driving its colors.
 * @param loading when true, the leading dot becomes a small spinner.
 * @param onClick optional tap handler (e.g. open a details sheet).
 * @param colors the color set; defaults to [JengaStatusPillDefaults.colors] for [tone].
 * @param showDot whether the leading dot is shown; the spinner still shows while [loading].
 * @param indicatorSize diameter of the leading dot or spinner.
 * @param minHeight the minimum pill height.
 * @param contentPadding padding inside the pill.
 * @param textStyle the label text style.
 * @param leadingIcon optional icon before the label, tinted with the accent color; replaces the dot.
 *   The spinner still shows while [loading].
 */
@Composable
public fun JengaStatusPill(
    label: String,
    modifier: Modifier = Modifier,
    tone: JengaBadgeTone = JengaBadgeTone.Neutral,
    loading: Boolean = false,
    onClick: (() -> Unit)? = null,
    colors: JengaStatusPillColors = JengaStatusPillDefaults.colors(tone),
    showDot: Boolean = true,
    indicatorSize: Dp = JengaTheme.components.statusPill?.indicatorSize ?: JengaStatusPillDefaults.IndicatorSize,
    minHeight: Dp = JengaTheme.components.statusPill?.minHeight ?: JengaStatusPillDefaults.MinHeight,
    contentPadding: PaddingValues = JengaStatusPillDefaults.contentPadding,
    textStyle: TextStyle = JengaStatusPillDefaults.textStyle,
    leadingIcon: (@Composable () -> Unit)? = null,
) {
    Row(
        modifier = modifier
            .clip(JengaTheme.shapes.pill)
            .background(colors.container)
            .then(if (onClick != null) Modifier.clickable(role = Role.Button, onClick = onClick) else Modifier)
            .defaultMinSize(minHeight = minHeight)
            .padding(contentPadding),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(JengaTheme.spacing.xs),
    ) {
        if (loading) {
            JengaCircularProgressIndeterminate(
                size = indicatorSize,
                strokeWidth = 2.dp,
                color = colors.accent,
                trackColor = colors.accent.copy(alpha = 0.25f),
            )
        } else if (leadingIcon != null) {
            CompositionLocalProvider(LocalJengaContentColor provides colors.accent, content = leadingIcon)
        } else if (showDot) {
            Box(
                Modifier
                    .size(indicatorSize)
                    .clip(JengaTheme.shapes.pill)
                    .background(colors.accent),
            )
        }
        JengaText(text = label, style = textStyle, color = colors.content)
    }
}

@Deprecated("Kept for binary compatibility", level = DeprecationLevel.HIDDEN)
@Composable
public fun JengaStatusPill(
    label: String,
    modifier: Modifier = Modifier,
    tone: JengaBadgeTone = JengaBadgeTone.Neutral,
    loading: Boolean = false,
    onClick: (() -> Unit)? = null,
    colors: JengaStatusPillColors = JengaStatusPillDefaults.colors(tone),
    showDot: Boolean = true,
    indicatorSize: Dp = JengaTheme.components.statusPill?.indicatorSize ?: JengaStatusPillDefaults.IndicatorSize,
    minHeight: Dp = JengaTheme.components.statusPill?.minHeight ?: JengaStatusPillDefaults.MinHeight,
    contentPadding: PaddingValues = JengaStatusPillDefaults.contentPadding,
    textStyle: TextStyle = JengaStatusPillDefaults.textStyle,
) {
    JengaStatusPill(
        label = label,
        modifier = modifier,
        tone = tone,
        loading = loading,
        onClick = onClick,
        colors = colors,
        showDot = showDot,
        indicatorSize = indicatorSize,
        minHeight = minHeight,
        contentPadding = contentPadding,
        textStyle = textStyle,
        leadingIcon = null,
    )
}
