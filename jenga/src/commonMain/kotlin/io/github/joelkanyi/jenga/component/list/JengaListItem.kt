package io.github.joelkanyi.jenga.component.list

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
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
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import dev.drewhamilton.poko.Poko
import io.github.joelkanyi.jenga.component.text.JengaText
import io.github.joelkanyi.jenga.theme.JengaTheme
import io.github.joelkanyi.jenga.theme.LocalJengaContentColor

/** Resolved colors for a [JengaListItem]. Override via [JengaListItemDefaults.colors]. */
@Poko
@Immutable
public class JengaListItemColors(
    public val container: Color,
    public val headline: Color,
    public val supporting: Color,
    public val leadingTrailing: Color,
    public val disabledHeadline: Color = headline.copy(alpha = 0.38f),
    public val disabledSupporting: Color = supporting.copy(alpha = 0.38f),
    public val disabledLeadingTrailing: Color = leadingTrailing.copy(alpha = 0.38f),
) {
    public fun copy(
        container: Color = this.container,
        headline: Color = this.headline,
        supporting: Color = this.supporting,
        leadingTrailing: Color = this.leadingTrailing,
        disabledHeadline: Color = this.disabledHeadline,
        disabledSupporting: Color = this.disabledSupporting,
        disabledLeadingTrailing: Color = this.disabledLeadingTrailing,
    ): JengaListItemColors = JengaListItemColors(
        container,
        headline,
        supporting,
        leadingTrailing,
        disabledHeadline,
        disabledSupporting,
        disabledLeadingTrailing,
    )
}

/** Defaults and token mappings for [JengaListItem]. */
public object JengaListItemDefaults {
    /** Minimum row height. */
    public val MinHeight: Dp = 56.dp

    /** Gap between the leading content, the text and the trailing content. */
    public val contentSpacing: Dp
        @Composable get() = JengaTheme.spacing.md

    /** Headline text style. */
    public val headlineStyle: TextStyle
        @Composable get() = JengaTheme.typography.titleSmall

    /** Supporting text style. */
    public val supportingStyle: TextStyle
        @Composable get() = JengaTheme.typography.bodySmall

    /** Inner padding around the row content. */
    public val contentPadding: PaddingValues
        @Composable get() = PaddingValues(horizontal = JengaTheme.spacing.lg, vertical = JengaTheme.spacing.md)

    /** Themed colors. */
    @Composable
    public fun colors(): JengaListItemColors {
        val c = JengaTheme.colors
        return JengaListItemColors(
            container = Color.Transparent,
            headline = c.textPrimary,
            supporting = c.textMuted,
            leadingTrailing = c.textMuted,
            disabledHeadline = c.contentDisabled,
            disabledSupporting = c.contentDisabled,
            disabledLeadingTrailing = c.contentDisabled,
        )
    }
}

/**
 * A single row in a list: a headline, optional supporting line, and optional
 * leading/trailing slots (icons, avatars, controls).
 *
 * @sample io.github.joelkanyi.jenga.samples.JengaListItemSample
 *
 * @param headline the primary text.
 * @param modifier the [Modifier] for this row.
 * @param supporting optional secondary text below the headline.
 * @param supportingContent optional slot below the headline (and below [supporting]
 *   when both are given), for rich supporting content such as badges or a status line.
 *   Inherits the supporting color.
 * @param leadingContent optional start slot (inherits the leading/trailing color).
 * @param trailingContent optional end slot (inherits the leading/trailing color).
 * @param onClick optional click handler; makes the row focusable with a ripple.
 * @param enabled when false, the row uses the disabled colors from [colors] and [onClick] is ignored.
 * @param headlineMaxLines the maximum lines for [headline] before it is truncated.
 * @param supportingMaxLines the maximum lines for [supporting] before it is truncated.
 * @param minHeight the minimum row height.
 * @param contentPadding inner padding around the row content.
 * @param headlineStyle the [headline] text style.
 * @param supportingStyle the [supporting] text style.
 * @param contentSpacing the gap between the leading content, the text and the trailing content.
 * @param verticalAlignment how the leading content, the text and the trailing content align in a tall row.
 * @param colors the color set; defaults to [JengaListItemDefaults.colors].
 */
@Composable
public fun JengaListItem(
    headline: String,
    modifier: Modifier = Modifier,
    supporting: String? = null,
    supportingContent: (@Composable () -> Unit)? = null,
    leadingContent: (@Composable () -> Unit)? = null,
    trailingContent: (@Composable () -> Unit)? = null,
    onClick: (() -> Unit)? = null,
    enabled: Boolean = true,
    headlineMaxLines: Int = 1,
    supportingMaxLines: Int = 1,
    minHeight: Dp = JengaListItemDefaults.MinHeight,
    contentPadding: PaddingValues = JengaListItemDefaults.contentPadding,
    headlineStyle: TextStyle = JengaListItemDefaults.headlineStyle,
    supportingStyle: TextStyle = JengaListItemDefaults.supportingStyle,
    contentSpacing: Dp = JengaListItemDefaults.contentSpacing,
    verticalAlignment: Alignment.Vertical = Alignment.CenterVertically,
    colors: JengaListItemColors = JengaListItemDefaults.colors(),
) {
    JengaListItem(
        headline = AnnotatedString(headline),
        modifier = modifier,
        supporting = supporting,
        supportingContent = supportingContent,
        leadingContent = leadingContent,
        trailingContent = trailingContent,
        onClick = onClick,
        enabled = enabled,
        headlineMaxLines = headlineMaxLines,
        supportingMaxLines = supportingMaxLines,
        minHeight = minHeight,
        contentPadding = contentPadding,
        headlineStyle = headlineStyle,
        supportingStyle = supportingStyle,
        contentSpacing = contentSpacing,
        verticalAlignment = verticalAlignment,
        colors = colors,
    )
}

/**
 * A [JengaListItem] whose headline is styled text (e.g. a monospaced id beside plain words).
 *
 * @param headline the primary text, with styled spans.
 * @param modifier the [Modifier] for this row.
 * @param supporting optional secondary text below the headline.
 * @param supportingContent optional slot below the headline (and below [supporting]
 *   when both are given), for rich supporting content such as badges or a status line.
 *   Inherits the supporting color.
 * @param leadingContent optional start slot (inherits the leading/trailing color).
 * @param trailingContent optional end slot (inherits the leading/trailing color).
 * @param onClick optional click handler; makes the row focusable with a ripple.
 * @param enabled when false, the row uses the disabled colors from [colors] and [onClick] is ignored.
 * @param headlineMaxLines the maximum lines for [headline] before it is truncated.
 * @param supportingMaxLines the maximum lines for [supporting] before it is truncated.
 * @param minHeight the minimum row height.
 * @param contentPadding inner padding around the row content.
 * @param headlineStyle the [headline] text style; spans in [headline] override it.
 * @param supportingStyle the [supporting] text style.
 * @param contentSpacing the gap between the leading content, the text and the trailing content.
 * @param verticalAlignment how the leading content, the text and the trailing content align in a tall row.
 * @param colors the color set; defaults to [JengaListItemDefaults.colors].
 */
@Composable
public fun JengaListItem(
    headline: AnnotatedString,
    modifier: Modifier = Modifier,
    supporting: String? = null,
    supportingContent: (@Composable () -> Unit)? = null,
    leadingContent: (@Composable () -> Unit)? = null,
    trailingContent: (@Composable () -> Unit)? = null,
    onClick: (() -> Unit)? = null,
    enabled: Boolean = true,
    headlineMaxLines: Int = 1,
    supportingMaxLines: Int = 1,
    minHeight: Dp = JengaListItemDefaults.MinHeight,
    contentPadding: PaddingValues = JengaListItemDefaults.contentPadding,
    headlineStyle: TextStyle = JengaListItemDefaults.headlineStyle,
    supportingStyle: TextStyle = JengaListItemDefaults.supportingStyle,
    contentSpacing: Dp = JengaListItemDefaults.contentSpacing,
    verticalAlignment: Alignment.Vertical = Alignment.CenterVertically,
    colors: JengaListItemColors = JengaListItemDefaults.colors(),
) {
    val headlineColor = if (enabled) colors.headline else colors.disabledHeadline
    val supportingColor = if (enabled) colors.supporting else colors.disabledSupporting
    val leadingTrailingColor = if (enabled) colors.leadingTrailing else colors.disabledLeadingTrailing
    Row(
        modifier = modifier
            .fillMaxWidth()
            .background(colors.container)
            .then(
                if (onClick != null) {
                    Modifier.clickable(enabled = enabled, role = Role.Button, onClick = onClick)
                } else {
                    Modifier
                },
            )
            .defaultMinSize(minHeight = minHeight)
            .padding(contentPadding),
        verticalAlignment = verticalAlignment,
        horizontalArrangement = Arrangement.spacedBy(contentSpacing),
    ) {
        if (leadingContent != null) {
            CompositionLocalProvider(LocalJengaContentColor provides leadingTrailingColor) {
                leadingContent()
            }
        }
        Column(modifier = Modifier.weight(1f)) {
            JengaText(
                text = headline,
                style = headlineStyle,
                color = headlineColor,
                maxLines = headlineMaxLines,
            )
            if (supporting != null) {
                JengaText(
                    text = supporting,
                    style = supportingStyle,
                    color = supportingColor,
                    maxLines = supportingMaxLines,
                )
            }
            if (supportingContent != null) {
                CompositionLocalProvider(LocalJengaContentColor provides supportingColor) {
                    Box(modifier = Modifier.padding(top = JengaTheme.spacing.xxs)) {
                        supportingContent()
                    }
                }
            }
        }
        if (trailingContent != null) {
            CompositionLocalProvider(LocalJengaContentColor provides leadingTrailingColor) {
                trailingContent()
            }
        }
    }
}
