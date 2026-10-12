package io.github.joelkanyi.jenga.component.list

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.Immutable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.layout
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import dev.drewhamilton.poko.Poko
import io.github.joelkanyi.jenga.component.badge.JengaBadgeTone
import io.github.joelkanyi.jenga.component.text.JengaText
import io.github.joelkanyi.jenga.theme.JengaTheme
import io.github.joelkanyi.jenga.theme.LocalJengaContentColor

/** Visual weight of a [JengaKeyValueRow]. */
public enum class JengaKeyValueEmphasis {
    /** A regular detail row: muted label, primary value. */
    Default,

    /** A summary row (e.g. an amount due): bold label and value. */
    Total,
}

/** Resolved colors for a [JengaKeyValueRow]. Override via [JengaKeyValueRowDefaults.colors]. */
@Poko
@Immutable
public class JengaKeyValueRowColors(
    public val label: Color,
    public val value: Color,
    public val trailing: Color,
) {
    public fun copy(
        label: Color = this.label,
        value: Color = this.value,
        trailing: Color = this.trailing,
    ): JengaKeyValueRowColors = JengaKeyValueRowColors(label, value, trailing)
}

/** Defaults and token mappings for [JengaKeyValueRow]. */
/**
 * Theme-wide style for every [JengaKeyValueRow], set through
 * [io.github.joelkanyi.jenga.theme.JengaComponentStyles.keyValueRow]. A `null` value keeps Jenga's
 * default; arguments passed to a call still win.
 *
 * @property minHeight the minimum row height.
 * @property contentPadding inner padding around the label and value.
 * @property labelStyle the label text style (made bold for [JengaKeyValueEmphasis.Total]).
 * @property valueStyle the value text style (made bold for [JengaKeyValueEmphasis.Total]).
 */
@Poko
@Immutable
public class JengaKeyValueRowStyle(
    public val minHeight: Dp? = null,
    public val contentPadding: PaddingValues? = null,
    public val labelStyle: TextStyle? = null,
    public val valueStyle: TextStyle? = null,
) {
    public fun copy(
        minHeight: Dp? = this.minHeight,
        contentPadding: PaddingValues? = this.contentPadding,
        labelStyle: TextStyle? = this.labelStyle,
        valueStyle: TextStyle? = this.valueStyle,
    ): JengaKeyValueRowStyle = JengaKeyValueRowStyle(minHeight, contentPadding, labelStyle, valueStyle)
}

public object JengaKeyValueRowDefaults {
    /** Minimum row height; a clickable row is never shorter than the touch target. */
    public val MinHeight: Dp = 0.dp

    /** Default share of the row width the value may take before it wraps or truncates. */
    public const val ValueMaxWidthFraction: Float = 0.6f

    /** Inner padding around the label and value. */
    public val contentPadding: PaddingValues
        @Composable get() = JengaTheme.components.keyValueRow?.contentPadding ?: PaddingValues(vertical = JengaTheme.spacing.sm)

    /** Label text style. */
    public val labelStyle: TextStyle
        @Composable get() = JengaTheme.components.keyValueRow?.labelStyle ?: JengaTheme.typography.bodyMedium

    /** Value text style. */
    public val valueStyle: TextStyle
        @Composable get() = JengaTheme.components.keyValueRow?.valueStyle ?: JengaTheme.typography.bodyMedium

    /**
     * Themed colors for [emphasis]; a non-null [valueTone] tints the value
     * (e.g. success for "Paid").
     */
    @Composable
    public fun colors(
        emphasis: JengaKeyValueEmphasis = JengaKeyValueEmphasis.Default,
        valueTone: JengaBadgeTone? = null,
    ): JengaKeyValueRowColors {
        val c = JengaTheme.colors
        val value = when (valueTone) {
            null -> c.textPrimary
            JengaBadgeTone.Neutral -> c.textMuted
            JengaBadgeTone.Brand -> c.onBrandSubtle
            JengaBadgeTone.Success -> c.onSuccessContainer
            JengaBadgeTone.Warning -> c.onWarningContainer
            JengaBadgeTone.Error -> c.onErrorContainer
            JengaBadgeTone.Info -> c.onInfoContainer
        }
        return JengaKeyValueRowColors(
            label = if (emphasis == JengaKeyValueEmphasis.Total) c.textPrimary else c.textMuted,
            value = value,
            trailing = c.textMuted,
        )
    }
}

/**
 * A label and its value on one row: the label at the start, the value aligned
 * to the end, taking up to [valueMaxWidthFraction] of the row and wrapping up
 * to [valueMaxLines] lines before it is truncated with an ellipsis. Stack these for detail
 * and summary sections (serial numbers, dates, amounts). Place a
 * [io.github.joelkanyi.jenga.component.divider.JengaDivider] above a total row
 * to set it apart.
 *
 * @sample io.github.joelkanyi.jenga.samples.JengaKeyValueRowSample
 *
 * @param label the key text.
 * @param value the value text.
 * @param modifier the [Modifier] for this row.
 * @param valueStyle the value's [TextStyle]; pass `JengaTheme.typography.mono` for codes.
 * @param valueTone optional tone that colors the value (e.g. success for "Paid").
 * @param emphasis the row's weight; see [JengaKeyValueEmphasis].
 * @param trailingContent optional end slot after the value (e.g. a copy button).
 * @param onClick optional click handler; makes the whole row clickable.
 * @param valueMaxLines the maximum lines for [value] before it is truncated.
 * @param valueMaxWidthFraction the largest share of the row width the value may take.
 * @param minHeight the minimum row height; defaults to the theme's [JengaKeyValueRowStyle.minHeight], else
 *   [JengaKeyValueRowDefaults.MinHeight].
 * @param contentPadding inner padding around the label and value.
 * @param colors the color set; defaults to [JengaKeyValueRowDefaults.colors].
 * @param labelStyle the label's [TextStyle].
 */
@Composable
public fun JengaKeyValueRow(
    label: String,
    value: String,
    modifier: Modifier = Modifier,
    valueStyle: TextStyle = JengaKeyValueRowDefaults.valueStyle,
    valueTone: JengaBadgeTone? = null,
    emphasis: JengaKeyValueEmphasis = JengaKeyValueEmphasis.Default,
    trailingContent: (@Composable () -> Unit)? = null,
    onClick: (() -> Unit)? = null,
    valueMaxLines: Int = Int.MAX_VALUE,
    valueMaxWidthFraction: Float = JengaKeyValueRowDefaults.ValueMaxWidthFraction,
    minHeight: Dp = JengaTheme.components.keyValueRow?.minHeight ?: JengaKeyValueRowDefaults.MinHeight,
    contentPadding: PaddingValues = JengaKeyValueRowDefaults.contentPadding,
    colors: JengaKeyValueRowColors = JengaKeyValueRowDefaults.colors(emphasis, valueTone),
    labelStyle: TextStyle = JengaKeyValueRowDefaults.labelStyle,
) {
    val isTotal = emphasis == JengaKeyValueEmphasis.Total
    val bold = TextStyle(fontWeight = FontWeight.Bold)
    val resolvedLabelStyle = if (isTotal) labelStyle.merge(bold) else labelStyle
    val rowMinHeight = if (onClick != null) maxOf(minHeight, JengaTheme.sizing.minTouchTarget) else minHeight
    Box(
        modifier = modifier
            .fillMaxWidth()
            .then(if (onClick != null) Modifier.clickable(role = Role.Button, onClick = onClick) else Modifier)
            .defaultMinSize(minHeight = rowMinHeight)
            .padding(contentPadding),
        contentAlignment = Alignment.CenterStart,
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.Top,
            horizontalArrangement = Arrangement.spacedBy(JengaTheme.spacing.md),
        ) {
            JengaText(
                text = label,
                modifier = Modifier.weight(1f),
                style = resolvedLabelStyle,
                color = colors.label,
            )
            JengaText(
                text = value,
                modifier = Modifier.layout { measurable, constraints ->
                    val cap = (constraints.maxWidth * valueMaxWidthFraction).toInt()
                    val placeable = measurable.measure(constraints.copy(maxWidth = cap.coerceAtLeast(constraints.minWidth)))
                    layout(placeable.width, placeable.height) { placeable.place(0, 0) }
                },
                style = if (isTotal) valueStyle.merge(bold) else valueStyle,
                color = colors.value,
                textAlign = TextAlign.End,
                maxLines = valueMaxLines,
                overflow = TextOverflow.Ellipsis,
            )
            if (trailingContent != null) {
                CompositionLocalProvider(LocalJengaContentColor provides colors.trailing) {
                    trailingContent()
                }
            }
        }
    }
}
