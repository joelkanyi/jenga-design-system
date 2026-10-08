package io.github.joelkanyi.jenga.component.link

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextDecoration
import dev.drewhamilton.poko.Poko
import io.github.joelkanyi.jenga.component.text.JengaText
import io.github.joelkanyi.jenga.theme.JengaTheme
import io.github.joelkanyi.jenga.theme.LocalJengaContentColor

/** Resolved colors for a [JengaLink]. */
@Poko
@Immutable
public class JengaLinkColors(
    public val content: Color,
    public val disabledContent: Color,
) {
    public fun copy(
        content: Color = this.content,
        disabledContent: Color = this.disabledContent,
    ): JengaLinkColors = JengaLinkColors(content, disabledContent)
}

/** Defaults and token mappings for [JengaLink]. */
public object JengaLinkDefaults {
    /** Padding around the link. */
    public val contentPadding: PaddingValues
        @Composable get() = PaddingValues()

    /** Themed link colors: the brand accent, muted when disabled. */
    @Composable
    public fun colors(): JengaLinkColors {
        val c = JengaTheme.colors
        return JengaLinkColors(content = c.brand, disabledContent = c.textMuted)
    }
}

/**
 * A tappable text link: brand-coloured and underlined by convention, the way a
 * hyperlink reads. The caller decides what a tap does (open a URL through the
 * platform `LocalUriHandler`, navigate, and so on), so the link stays
 * framework-agnostic and cannot leak a URL type into the design system.
 *
 * It carries no ripple: an inline link should feel like text, not a button. It reserves
 * no 48dp layout box, so it fits a tight row (e.g. a section header action); taps just
 * outside a small link still reach it through Compose's minimum touch target.
 *
 * @sample io.github.joelkanyi.jenga.samples.JengaLinkSample
 *
 * @param text the link label.
 * @param onClick invoked when the link is tapped; not called while disabled.
 * @param modifier the [Modifier] for this link.
 * @param style the [TextStyle]; defaults to body text from the theme.
 * @param colors the link colors; defaults to [JengaLinkDefaults.colors].
 * @param enabled whether the link responds to taps and uses its enabled color.
 * @param underline whether the label is underlined (the link affordance).
 * @param leadingIcon optional icon before the label; inherits the link color.
 * @param contentPadding padding around the icon and label.
 */
@Composable
public fun JengaLink(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    style: TextStyle = JengaTheme.typography.bodyMedium,
    colors: JengaLinkColors = JengaLinkDefaults.colors(),
    enabled: Boolean = true,
    underline: Boolean = true,
    leadingIcon: (@Composable () -> Unit)? = null,
    contentPadding: PaddingValues = JengaLinkDefaults.contentPadding,
) {
    val color = if (enabled) colors.content else colors.disabledContent
    Row(
        modifier = modifier
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null,
                enabled = enabled,
                onClick = onClick,
            )
            .padding(contentPadding),
        horizontalArrangement = Arrangement.spacedBy(JengaTheme.spacing.xs),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        if (leadingIcon != null) {
            CompositionLocalProvider(LocalJengaContentColor provides color) { leadingIcon() }
        }
        JengaText(
            text = text,
            color = color,
            style = style.merge(
                TextStyle(textDecoration = if (underline) TextDecoration.Underline else TextDecoration.None),
            ),
        )
    }
}
