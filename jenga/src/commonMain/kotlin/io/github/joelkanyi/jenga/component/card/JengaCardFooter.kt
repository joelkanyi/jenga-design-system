package io.github.joelkanyi.jenga.component.card

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.Immutable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import dev.drewhamilton.poko.Poko
import io.github.joelkanyi.jenga.component.text.JengaText
import io.github.joelkanyi.jenga.theme.JengaTheme
import io.github.joelkanyi.jenga.theme.LocalJengaContentColor

/** Resolved colors for a [JengaCardFooter]. Override via [JengaCardFooterDefaults.colors]. */
@Poko
@Immutable
public class JengaCardFooterColors(
    public val container: Color,
    public val content: Color,
) {
    public fun copy(
        container: Color = this.container,
        content: Color = this.content,
    ): JengaCardFooterColors = JengaCardFooterColors(container, content)
}

/** Defaults and token mappings for [JengaCardFooter]. */
public object JengaCardFooterDefaults {
    /** Padding inside the footer. */
    public val contentPadding: PaddingValues
        @Composable get() = PaddingValues(horizontal = JengaTheme.spacing.lg, vertical = JengaTheme.spacing.sm)

    /** Text style. */
    public val textStyle: TextStyle
        @Composable get() = JengaTheme.typography.caption

    /** Themed colors. */
    @Composable
    public fun colors(): JengaCardFooterColors = JengaCardFooterColors(
        container = JengaTheme.colors.surfaceSunk,
        content = JengaTheme.colors.textSecondary,
    )
}

/**
 * A quiet footer row for the bottom of a [JengaCard]: a sunk strip with an
 * optional icon and a short line of text (e.g. "Last updated 2h ago"). Place it
 * last inside a card whose `contentPadding` is zero so it spans the full width.
 *
 * @sample io.github.joelkanyi.jenga.samples.JengaCardFooterSample
 *
 * @param text the footer text.
 * @param modifier the [Modifier] for this footer.
 * @param leadingIcon optional icon before the text; inherits the content color.
 * @param contentPadding padding inside the footer.
 * @param textStyle the text style.
 * @param colors the color set; defaults to [JengaCardFooterDefaults.colors].
 */
@Composable
public fun JengaCardFooter(
    text: String,
    modifier: Modifier = Modifier,
    leadingIcon: (@Composable () -> Unit)? = null,
    contentPadding: PaddingValues = JengaCardFooterDefaults.contentPadding,
    textStyle: TextStyle = JengaCardFooterDefaults.textStyle,
    colors: JengaCardFooterColors = JengaCardFooterDefaults.colors(),
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .background(colors.container)
            .padding(contentPadding),
        horizontalArrangement = Arrangement.spacedBy(JengaTheme.spacing.sm),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        CompositionLocalProvider(LocalJengaContentColor provides colors.content) {
            leadingIcon?.invoke()
            JengaText(text = text, style = textStyle, color = colors.content)
        }
    }
}
