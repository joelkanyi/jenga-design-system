package io.github.joelkanyi.jenga.pattern

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import io.github.joelkanyi.jenga.component.button.JengaButton
import io.github.joelkanyi.jenga.component.button.JengaButtonSize
import io.github.joelkanyi.jenga.component.button.JengaButtonVariant
import io.github.joelkanyi.jenga.component.text.JengaText
import io.github.joelkanyi.jenga.theme.JengaTheme
import io.github.joelkanyi.jenga.theme.LocalJengaContentColor

/** Visual style of a [JengaSectionHeader]. */
public enum class JengaSectionHeaderStyle {
    /** A title-sized heading in the primary text color. */
    Title,

    /** A small uppercase label in the secondary text color, for dense detail screens. */
    Overline,
}

/** Defaults for [JengaSectionHeader]. */
public object JengaSectionHeaderDefaults {
    /** Minimum header height. */
    public val MinHeight: Dp = 0.dp

    /** Title text style for [style]. */
    @Composable
    public fun titleStyle(style: JengaSectionHeaderStyle): TextStyle = when (style) {
        JengaSectionHeaderStyle.Title -> JengaTheme.typography.titleLarge
        JengaSectionHeaderStyle.Overline -> JengaTheme.typography.label
    }

    /** Padding around the header. */
    public val contentPadding: PaddingValues
        @Composable get() = PaddingValues(vertical = JengaTheme.spacing.sm)
}

/**
 * **Pattern (organism)**, a section header: a title with an optional trailing
 * text action and an optional trailing slot (e.g. a count or an icon action).
 * Composed from [JengaText] + [JengaButton].
 *
 * With [JengaSectionHeaderStyle.Overline] the title is shown in uppercase.
 *
 * @sample io.github.joelkanyi.jenga.samples.JengaSectionHeaderSample
 *
 * @param title the section title.
 * @param modifier the [Modifier] for this header.
 * @param subtitle optional supporting line under the title.
 * @param actionLabel optional trailing action label.
 * @param onActionClick called when the action is tapped (shown with [actionLabel]).
 * @param style the header style; see [JengaSectionHeaderStyle].
 * @param trailingContent optional end slot, after the action; inherits the secondary text color.
 * @param titleStyle the title text style; defaults to the one for [style].
 * @param minHeight the minimum header height.
 * @param contentPadding padding around the header.
 */
@Composable
public fun JengaSectionHeader(
    title: String,
    modifier: Modifier = Modifier,
    subtitle: String? = null,
    actionLabel: String? = null,
    onActionClick: (() -> Unit)? = null,
    style: JengaSectionHeaderStyle = JengaSectionHeaderStyle.Title,
    trailingContent: (@Composable () -> Unit)? = null,
    titleStyle: TextStyle = JengaSectionHeaderDefaults.titleStyle(style),
    minHeight: Dp = JengaSectionHeaderDefaults.MinHeight,
    contentPadding: PaddingValues = JengaSectionHeaderDefaults.contentPadding,
) {
    val isOverline = style == JengaSectionHeaderStyle.Overline
    Row(
        modifier = modifier
            .fillMaxWidth()
            .defaultMinSize(minHeight = minHeight)
            .padding(contentPadding),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(JengaTheme.spacing.md),
    ) {
        Column(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(JengaTheme.spacing.xxs),
        ) {
            JengaText(
                text = if (isOverline) title.uppercase() else title,
                style = titleStyle,
                color = if (isOverline) JengaTheme.colors.textSecondary else Color.Unspecified,
                maxLines = 1,
            )
            if (subtitle != null) {
                JengaText(
                    text = subtitle,
                    style = JengaTheme.typography.bodySmall,
                    color = JengaTheme.colors.textMuted,
                    maxLines = 1,
                )
            }
        }
        if (actionLabel != null && onActionClick != null) {
            JengaButton(
                text = actionLabel,
                onClick = onActionClick,
                variant = JengaButtonVariant.Ghost,
                size = JengaButtonSize.Small,
            )
        }
        if (trailingContent != null) {
            CompositionLocalProvider(LocalJengaContentColor provides JengaTheme.colors.textSecondary) {
                trailingContent()
            }
        }
    }
}
