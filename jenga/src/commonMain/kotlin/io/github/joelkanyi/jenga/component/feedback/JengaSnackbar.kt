package io.github.joelkanyi.jenga.component.feedback

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import dev.drewhamilton.poko.Poko
import io.github.joelkanyi.jenga.component.text.JengaText
import io.github.joelkanyi.jenga.theme.JengaTheme

/** Semantic tone of a [JengaSnackbar]. */
public enum class JengaSnackbarTone { Neutral, Success, Error }

/** Resolved colors for a [JengaSnackbar]. Override via [JengaSnackbarDefaults.colors]. */
@Poko
@Immutable
public class JengaSnackbarColors(
    public val container: Color,
    public val content: Color,
    public val action: Color = content,
) {
    public fun copy(
        container: Color = this.container,
        content: Color = this.content,
        action: Color = this.action,
    ): JengaSnackbarColors = JengaSnackbarColors(container, content, action)
}

/** Defaults and token mappings for [JengaSnackbar]. */
public object JengaSnackbarDefaults {
    /** Default snackbar shape. */
    public val shape: Shape
        @Composable get() = JengaTheme.shapes.control

    /** Diameter of the leading tone dot. */
    public val DotSize: Dp = 8.dp

    /** Space around a snackbar inside a [JengaSnackbarHost]. */
    public val hostPadding: PaddingValues
        @Composable get() = PaddingValues(JengaTheme.spacing.lg)

    /** Padding inside the snackbar. */
    public val contentPadding: PaddingValues
        @Composable get() = PaddingValues(horizontal = JengaTheme.spacing.lg, vertical = JengaTheme.spacing.md)

    /** Message text style. */
    public val messageStyle: TextStyle
        @Composable get() = JengaTheme.typography.bodySmall

    /** Action label text style. */
    public val actionStyle: TextStyle
        @Composable get() = JengaTheme.typography.label

    /** Themed colors (high-contrast inverse surface). */
    @Composable
    public fun colors(): JengaSnackbarColors = JengaSnackbarColors(
        container = JengaTheme.colors.inverseSurface,
        content = JengaTheme.colors.inverseOnSurface,
        action = JengaTheme.colors.inverseOnSurface,
    )

    /** Accent dot color for [tone], or `null` for [JengaSnackbarTone.Neutral]. */
    @Composable
    public fun toneColor(tone: JengaSnackbarTone): Color? = when (tone) {
        JengaSnackbarTone.Neutral -> null
        JengaSnackbarTone.Success -> JengaTheme.colors.success
        JengaSnackbarTone.Error -> JengaTheme.colors.error
    }
}

/**
 * A transient message surface with an optional action.
 *
 * This is the visual component; host it yourself (e.g. in a `Box` overlay or via
 * your navigation shell). It does not manage its own visibility or timing.
 *
 * @sample io.github.joelkanyi.jenga.samples.JengaSnackbarSample
 *
 * @param message the message to show.
 * @param modifier the [Modifier] for this snackbar.
 * @param tone the semantic tone; see [JengaSnackbarTone].
 * @param actionLabel optional action label; when set with [onAction], shows a
 *   trailing action.
 * @param onAction called when the action is tapped.
 * @param shape the snackbar shape; defaults to [JengaSnackbarDefaults.shape].
 * @param colors the color set; defaults to [JengaSnackbarDefaults.colors].
 * @param contentPadding padding inside the snackbar.
 * @param messageStyle the message text style.
 * @param actionStyle the action label text style.
 */
@Composable
public fun JengaSnackbar(
    message: String,
    modifier: Modifier = Modifier,
    tone: JengaSnackbarTone = JengaSnackbarTone.Neutral,
    actionLabel: String? = null,
    onAction: (() -> Unit)? = null,
    shape: Shape = JengaSnackbarDefaults.shape,
    colors: JengaSnackbarColors = JengaSnackbarDefaults.colors(),
    contentPadding: PaddingValues = JengaSnackbarDefaults.contentPadding,
    messageStyle: TextStyle = JengaSnackbarDefaults.messageStyle,
    actionStyle: TextStyle = JengaSnackbarDefaults.actionStyle,
) {
    val toneColor = JengaSnackbarDefaults.toneColor(tone)

    Row(
        modifier = modifier
            .fillMaxWidth()
            .clip(shape)
            .background(colors.container)
            .padding(contentPadding),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(JengaTheme.spacing.md),
    ) {
        if (toneColor != null) {
            Box(
                modifier = Modifier
                    .size(JengaSnackbarDefaults.DotSize)
                    .clip(JengaTheme.shapes.pill)
                    .background(toneColor),
            )
        }
        JengaText(
            text = message,
            style = messageStyle,
            color = colors.content,
            modifier = Modifier.weight(1f),
        )
        if (actionLabel != null && onAction != null) {
            JengaText(
                text = actionLabel,
                style = actionStyle,
                color = colors.action,
                modifier = Modifier
                    .clip(JengaTheme.shapes.sm)
                    .clickable(role = Role.Button, onClick = onAction)
                    .padding(horizontal = JengaTheme.spacing.sm, vertical = JengaTheme.spacing.xs),
            )
        }
    }
}
