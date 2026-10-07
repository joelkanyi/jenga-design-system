package io.github.joelkanyi.jenga.component.banner

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import dev.drewhamilton.poko.Poko
import io.github.joelkanyi.jenga.component.button.JengaButtonDefaults
import io.github.joelkanyi.jenga.component.text.JengaText
import io.github.joelkanyi.jenga.theme.JengaTheme
import io.github.joelkanyi.jenga.theme.LocalJengaContentColor

/** Resolved colors for a [JengaInfoBar]. Override via [JengaInfoBarDefaults.colors]. */
@Poko
@Immutable
public class JengaInfoBarColors(
    public val container: Color,
    public val content: Color,
) {
    public fun copy(
        container: Color = this.container,
        content: Color = this.content,
    ): JengaInfoBarColors = JengaInfoBarColors(container, content)
}

/** Defaults and token mappings for [JengaInfoBar]. */
public object JengaInfoBarDefaults {
    /** Default shape, matching a button. */
    public val shape: Shape
        @Composable get() = JengaButtonDefaults.shape

    /** Minimum height, matching a medium button. */
    public val minHeight: Dp
        @Composable get() = JengaTheme.sizing.controlHeightMedium

    /** Padding inside the bar. */
    public val contentPadding: PaddingValues
        @Composable get() = PaddingValues(horizontal = JengaTheme.spacing.lg)

    /** Text style. */
    public val textStyle: TextStyle
        @Composable get() = JengaTheme.typography.button

    /** Themed colors. */
    @Composable
    public fun colors(): JengaInfoBarColors = JengaInfoBarColors(
        container = JengaTheme.colors.surfaceSunk,
        content = JengaTheme.colors.textSecondary,
    )
}

/**
 * A button-shaped, non-interactive bar that states why no action is available
 * (e.g. "View only" or "Closed"). Use it where a primary button would otherwise
 * sit. It is read as text, not as a button.
 *
 * @sample io.github.joelkanyi.jenga.samples.JengaInfoBarSample
 *
 * @param text the message.
 * @param modifier the [Modifier] for this bar.
 * @param leadingIcon optional icon before the text; inherits the content color.
 * @param shape the bar shape.
 * @param minHeight the minimum bar height.
 * @param contentPadding padding inside the bar.
 * @param textStyle the text style.
 * @param colors the color set; defaults to [JengaInfoBarDefaults.colors].
 */
@Composable
public fun JengaInfoBar(
    text: String,
    modifier: Modifier = Modifier,
    leadingIcon: (@Composable () -> Unit)? = null,
    shape: Shape = JengaInfoBarDefaults.shape,
    minHeight: Dp = JengaInfoBarDefaults.minHeight,
    contentPadding: PaddingValues = JengaInfoBarDefaults.contentPadding,
    textStyle: TextStyle = JengaInfoBarDefaults.textStyle,
    colors: JengaInfoBarColors = JengaInfoBarDefaults.colors(),
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .clip(shape)
            .background(colors.container)
            .defaultMinSize(minHeight = minHeight)
            .padding(contentPadding),
        horizontalArrangement = Arrangement.spacedBy(JengaTheme.spacing.sm, Alignment.CenterHorizontally),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        CompositionLocalProvider(LocalJengaContentColor provides colors.content) {
            leadingIcon?.invoke()
            JengaText(
                text = text,
                style = textStyle,
                color = colors.content,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.weight(1f, fill = false),
            )
        }
    }
}
