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
public object JengaStatusPillDefaults {
    /** Diameter of the leading status dot / spinner. */
    public val IndicatorSize: Dp = 8.dp

    /** Minimum pill height. */
    public val MinHeight: Dp = 0.dp

    /** Padding inside the pill. */
    public val contentPadding: PaddingValues
        @Composable get() = PaddingValues(horizontal = JengaTheme.spacing.md, vertical = JengaTheme.spacing.xs)

    /** Label text style. */
    public val textStyle: TextStyle
        @Composable get() = JengaTheme.typography.label

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
    indicatorSize: Dp = JengaStatusPillDefaults.IndicatorSize,
    minHeight: Dp = JengaStatusPillDefaults.MinHeight,
    contentPadding: PaddingValues = JengaStatusPillDefaults.contentPadding,
    textStyle: TextStyle = JengaStatusPillDefaults.textStyle,
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
