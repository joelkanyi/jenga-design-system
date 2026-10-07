package io.github.joelkanyi.jenga.component.list

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import io.github.joelkanyi.jenga.component.badge.JengaBadgeDefaults
import io.github.joelkanyi.jenga.component.badge.JengaBadgeTone
import io.github.joelkanyi.jenga.component.divider.JengaDivider
import io.github.joelkanyi.jenga.component.text.JengaText
import io.github.joelkanyi.jenga.theme.JengaTheme
import io.github.joelkanyi.jenga.theme.LocalJengaContentColor

/** Visual weight of a [JengaKeyValueRow]. */
public enum class JengaKeyValueEmphasis {
    /** A regular detail row: muted label, primary value. */
    Default,

    /** A summary row (e.g. an amount due): bold label and value, with a divider above. */
    Total,
}

/**
 * A label and its value on one row: the label at the start, the value aligned
 * to the end and wrapping onto more lines when long. Stack these for detail
 * and summary sections (serial numbers, dates, amounts).
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
) {
    val isTotal = emphasis == JengaKeyValueEmphasis.Total
    val bold = TextStyle(fontWeight = FontWeight.Bold)
    val labelStyle = JengaTheme.typography.bodyMedium.let { if (isTotal) it.merge(bold) else it }
    val labelColor = if (isTotal) JengaTheme.colors.textPrimary else JengaTheme.colors.textMuted
    val valueColor = valueTone?.let { JengaBadgeDefaults.colors(it).content } ?: JengaTheme.colors.textPrimary
    Column(modifier = modifier.fillMaxWidth()) {
        if (isTotal) {
            JengaDivider()
        }
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .then(if (onClick != null) Modifier.clickable(role = Role.Button, onClick = onClick) else Modifier)
                .padding(vertical = JengaTheme.spacing.sm),
            verticalAlignment = Alignment.Top,
            horizontalArrangement = Arrangement.spacedBy(JengaTheme.spacing.md),
        ) {
            JengaText(
                text = label,
                modifier = Modifier.weight(2f),
                style = labelStyle,
                color = labelColor,
            )
            JengaText(
                text = value,
                modifier = Modifier.weight(3f),
                style = if (isTotal) valueStyle.merge(bold) else valueStyle,
                color = valueColor,
                textAlign = TextAlign.End,
            )
            if (trailingContent != null) {
                CompositionLocalProvider(LocalJengaContentColor provides JengaTheme.colors.textMuted) {
                    trailingContent()
                }
            }
        }
    }
}
