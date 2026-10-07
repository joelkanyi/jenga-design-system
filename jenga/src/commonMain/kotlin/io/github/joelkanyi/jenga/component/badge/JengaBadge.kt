package io.github.joelkanyi.jenga.component.badge

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.TextStyle
import dev.drewhamilton.poko.Poko
import io.github.joelkanyi.jenga.component.text.JengaText
import io.github.joelkanyi.jenga.theme.JengaTheme

/** Semantic tone of a [JengaBadge]. */
public enum class JengaBadgeTone { Neutral, Brand, Success, Warning, Error, Info }

/** Resolved colors for a [JengaBadge]. */
@Poko
@Immutable
public class JengaBadgeColors(
    public val container: Color,
    public val content: Color,
) {
    public fun copy(
        container: Color = this.container,
        content: Color = this.content,
    ): JengaBadgeColors = JengaBadgeColors(container, content)
}

/** Defaults and token mappings for [JengaBadge]. */
public object JengaBadgeDefaults {
    /** Default badge shape. */
    public val shape: Shape
        @Composable get() = JengaTheme.shapes.pill

    /** Padding inside the badge. */
    public val contentPadding: PaddingValues
        @Composable get() = PaddingValues(horizontal = JengaTheme.spacing.sm, vertical = JengaTheme.spacing.xxs)

    /** Badge text style. */
    public val textStyle: TextStyle
        @Composable get() = JengaTheme.typography.label

    /** Themed colors per [tone]. */
    @Composable
    public fun colors(tone: JengaBadgeTone): JengaBadgeColors {
        val c = JengaTheme.colors
        return when (tone) {
            JengaBadgeTone.Neutral -> JengaBadgeColors(c.surfaceSunk, c.textMuted)
            JengaBadgeTone.Brand -> JengaBadgeColors(c.brandSubtle, c.onBrandSubtle)
            JengaBadgeTone.Success -> JengaBadgeColors(c.successContainer, c.onSuccessContainer)
            JengaBadgeTone.Warning -> JengaBadgeColors(c.warningContainer, c.onWarningContainer)
            JengaBadgeTone.Error -> JengaBadgeColors(c.errorContainer, c.onErrorContainer)
            JengaBadgeTone.Info -> JengaBadgeColors(c.infoContainer, c.onInfoContainer)
        }
    }
}

/**
 * A small status/label pill. The text is shown as written.
 *
 * @sample io.github.joelkanyi.jenga.samples.JengaBadgeSample
 *
 * @param text the badge label.
 * @param modifier the [Modifier] for this badge.
 * @param tone the semantic tone; see [JengaBadgeTone].
 * @param colors the color set; defaults to [JengaBadgeDefaults.colors] for [tone].
 * @param shape the badge shape.
 * @param contentPadding padding inside the badge.
 * @param textStyle the label text style.
 */
@Composable
public fun JengaBadge(
    text: String,
    modifier: Modifier = Modifier,
    tone: JengaBadgeTone = JengaBadgeTone.Neutral,
    colors: JengaBadgeColors = JengaBadgeDefaults.colors(tone),
    shape: Shape = JengaBadgeDefaults.shape,
    contentPadding: PaddingValues = JengaBadgeDefaults.contentPadding,
    textStyle: TextStyle = JengaBadgeDefaults.textStyle,
) {
    JengaBadge(
        text = AnnotatedString(text),
        modifier = modifier,
        tone = tone,
        colors = colors,
        shape = shape,
        contentPadding = contentPadding,
        textStyle = textStyle,
    )
}

/**
 * A small status/label pill with styled text (e.g. a bold count).
 *
 * @param text the badge label.
 * @param modifier the [Modifier] for this badge.
 * @param tone the semantic tone; see [JengaBadgeTone].
 * @param colors the color set; defaults to [JengaBadgeDefaults.colors] for [tone].
 * @param shape the badge shape.
 * @param contentPadding padding inside the badge.
 * @param textStyle the label text style.
 */
@Composable
public fun JengaBadge(
    text: AnnotatedString,
    modifier: Modifier = Modifier,
    tone: JengaBadgeTone = JengaBadgeTone.Neutral,
    colors: JengaBadgeColors = JengaBadgeDefaults.colors(tone),
    shape: Shape = JengaBadgeDefaults.shape,
    contentPadding: PaddingValues = JengaBadgeDefaults.contentPadding,
    textStyle: TextStyle = JengaBadgeDefaults.textStyle,
) {
    Row(
        modifier = modifier
            .clip(shape)
            .background(colors.container)
            .padding(contentPadding),
        horizontalArrangement = Arrangement.Center,
    ) {
        JengaText(
            text = text,
            style = textStyle,
            color = colors.content,
            maxLines = 1,
        )
    }
}
