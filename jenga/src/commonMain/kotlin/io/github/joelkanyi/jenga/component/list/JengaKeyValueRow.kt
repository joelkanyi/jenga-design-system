package io.github.joelkanyi.jenga.component.list

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
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
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
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
public object JengaKeyValueRowDefaults {
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
 * to the end and wrapping onto more lines when long. Stack these for detail
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
 * @param colors the color set; defaults to [JengaKeyValueRowDefaults.colors].
 */
@Composable
public fun JengaKeyValueRow(
    label: String,
    value: String,
    modifier: Modifier = Modifier,
    valueStyle: TextStyle = JengaTheme.typography.bodyMedium,
    valueTone: JengaBadgeTone? = null,
    emphasis: JengaKeyValueEmphasis = JengaKeyValueEmphasis.Default,
    trailingContent: (@Composable () -> Unit)? = null,
    onClick: (() -> Unit)? = null,
    colors: JengaKeyValueRowColors = JengaKeyValueRowDefaults.colors(emphasis, valueTone),
) {
    val isTotal = emphasis == JengaKeyValueEmphasis.Total
    val bold = TextStyle(fontWeight = FontWeight.Bold)
    val labelStyle = JengaTheme.typography.bodyMedium.let { if (isTotal) it.merge(bold) else it }
    Box(
        modifier = modifier
            .fillMaxWidth()
            .then(
                if (onClick != null) {
                    Modifier
                        .clickable(role = Role.Button, onClick = onClick)
                        .defaultMinSize(minHeight = JengaTheme.sizing.minTouchTarget)
                } else {
                    Modifier
                },
            )
            .padding(vertical = JengaTheme.spacing.sm),
        contentAlignment = Alignment.CenterStart,
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.Top,
            horizontalArrangement = Arrangement.spacedBy(JengaTheme.spacing.md),
        ) {
            JengaText(
                text = label,
                modifier = Modifier.weight(2f),
                style = labelStyle,
                color = colors.label,
            )
            JengaText(
                text = value,
                modifier = Modifier.weight(3f),
                style = if (isTotal) valueStyle.merge(bold) else valueStyle,
                color = colors.value,
                textAlign = TextAlign.End,
            )
            if (trailingContent != null) {
                CompositionLocalProvider(LocalJengaContentColor provides colors.trailing) {
                    trailingContent()
                }
            }
        }
    }
}
