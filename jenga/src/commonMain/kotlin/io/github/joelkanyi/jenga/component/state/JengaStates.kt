package io.github.joelkanyi.jenga.component.state

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import dev.drewhamilton.poko.Poko
import io.github.joelkanyi.jenga.component.button.JengaButton
import io.github.joelkanyi.jenga.component.button.JengaButtonSize
import io.github.joelkanyi.jenga.component.button.JengaButtonVariant
import io.github.joelkanyi.jenga.component.text.JengaText
import io.github.joelkanyi.jenga.theme.JengaTheme

/** Resolved colors for the empty/error state blocks. Override via [JengaStateDefaults.colors]. */
@Poko
@Immutable
public class JengaStateColors(
    public val title: Color,
    public val description: Color,
) {
    public fun copy(
        title: Color = this.title,
        description: Color = this.description,
    ): JengaStateColors = JengaStateColors(title, description)
}

/** Defaults and token mappings for [JengaEmptyState] / [JengaErrorState]. */
public object JengaStateDefaults {
    /** Title text style. */
    public val titleStyle: TextStyle
        @Composable get() = JengaTheme.typography.titleLarge

    /** Description text style. */
    public val descriptionStyle: TextStyle
        @Composable get() = JengaTheme.typography.bodySmall

    /** Padding around the state. */
    public val contentPadding: PaddingValues
        @Composable get() = PaddingValues(JengaTheme.spacing.xl)

    /** Gap between the icon, title and description. */
    public val spacing: Dp
        @Composable get() = JengaTheme.spacing.md

    /** Themed colors. */
    @Composable
    public fun colors(): JengaStateColors = JengaStateColors(
        title = JengaTheme.colors.textPrimary,
        description = JengaTheme.colors.textMuted,
    )
}

/**
 * A centered empty-state placeholder: optional icon, title, message, and action.
 *
 * @sample io.github.joelkanyi.jenga.samples.JengaEmptyStateSample
 *
 * @param title the headline (e.g. "No scans yet").
 * @param modifier the [Modifier] for this state.
 * @param description optional supporting message.
 * @param icon optional icon/illustration slot above the title.
 * @param actionLabel optional action button label; shown with [onAction].
 * @param onAction called when the action is tapped.
 * @param actionVariant the action button's variant; defaults to Outline.
 * @param colors the color set; defaults to [JengaStateDefaults.colors].
 * @param titleStyle the [title] text style.
 * @param descriptionStyle the [description] text style.
 * @param contentPadding padding around the state.
 * @param spacing the gap between the icon, title and description.
 * @param actionSpacing the gap above the action button.
 * @param actionSize the action button size.
 */
@Composable
public fun JengaEmptyState(
    title: String,
    modifier: Modifier = Modifier,
    description: String? = null,
    icon: (@Composable () -> Unit)? = null,
    actionLabel: String? = null,
    onAction: (() -> Unit)? = null,
    actionVariant: JengaButtonVariant = JengaButtonVariant.Outline,
    colors: JengaStateColors = JengaStateDefaults.colors(),
    titleStyle: TextStyle = JengaStateDefaults.titleStyle,
    descriptionStyle: TextStyle = JengaStateDefaults.descriptionStyle,
    contentPadding: PaddingValues = JengaStateDefaults.contentPadding,
    spacing: Dp = JengaStateDefaults.spacing,
    actionSpacing: Dp = spacing,
    actionSize: JengaButtonSize = JengaButtonSize.Medium,
) {
    StateLayout(
        title = title,
        description = description,
        icon = icon,
        actionLabel = actionLabel,
        actionVariant = actionVariant,
        onAction = onAction,
        colors = colors,
        titleStyle = titleStyle,
        descriptionStyle = descriptionStyle,
        contentPadding = contentPadding,
        spacing = spacing,
        actionSpacing = actionSpacing,
        actionSize = actionSize,
        modifier = modifier,
    )
}

/**
 * A centered error-state placeholder with a retry-style action.
 *
 * @sample io.github.joelkanyi.jenga.samples.JengaErrorStateSample
 *
 * @param title the headline (e.g. "Something went wrong").
 * @param modifier the [Modifier] for this state.
 * @param description optional supporting message.
 * @param icon optional icon/illustration slot above the title.
 * @param actionLabel optional action button label (e.g. "Retry"); shown with [onAction].
 * @param onAction called when the action is tapped.
 * @param actionVariant the action button's variant; defaults to Primary.
 * @param colors the color set; defaults to [JengaStateDefaults.colors].
 * @param titleStyle the [title] text style.
 * @param descriptionStyle the [description] text style.
 * @param contentPadding padding around the state.
 * @param spacing the gap between the icon, title and description.
 * @param actionSpacing the gap above the action button.
 * @param actionSize the action button size.
 */
@Composable
public fun JengaErrorState(
    title: String,
    modifier: Modifier = Modifier,
    description: String? = null,
    icon: (@Composable () -> Unit)? = null,
    actionLabel: String? = null,
    onAction: (() -> Unit)? = null,
    actionVariant: JengaButtonVariant = JengaButtonVariant.Primary,
    colors: JengaStateColors = JengaStateDefaults.colors(),
    titleStyle: TextStyle = JengaStateDefaults.titleStyle,
    descriptionStyle: TextStyle = JengaStateDefaults.descriptionStyle,
    contentPadding: PaddingValues = JengaStateDefaults.contentPadding,
    spacing: Dp = JengaStateDefaults.spacing,
    actionSpacing: Dp = spacing,
    actionSize: JengaButtonSize = JengaButtonSize.Medium,
) {
    StateLayout(
        title = title,
        description = description,
        icon = icon,
        actionLabel = actionLabel,
        actionVariant = actionVariant,
        onAction = onAction,
        colors = colors,
        titleStyle = titleStyle,
        descriptionStyle = descriptionStyle,
        contentPadding = contentPadding,
        spacing = spacing,
        actionSpacing = actionSpacing,
        actionSize = actionSize,
        modifier = modifier,
    )
}

@Composable
private fun StateLayout(
    title: String,
    description: String?,
    icon: (@Composable () -> Unit)?,
    actionLabel: String?,
    actionVariant: JengaButtonVariant,
    onAction: (() -> Unit)?,
    colors: JengaStateColors,
    titleStyle: TextStyle,
    descriptionStyle: TextStyle,
    contentPadding: PaddingValues,
    spacing: Dp,
    actionSpacing: Dp,
    actionSize: JengaButtonSize,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(contentPadding),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(spacing),
    ) {
        if (icon != null) icon()
        JengaText(
            text = title,
            style = titleStyle,
            color = colors.title,
            textAlign = TextAlign.Center,
        )
        if (description != null) {
            JengaText(
                text = description,
                style = descriptionStyle,
                color = colors.description,
                textAlign = TextAlign.Center,
            )
        }
        if (actionLabel != null && onAction != null) {
            JengaButton(
                text = actionLabel,
                onClick = onAction,
                modifier = Modifier.padding(top = (actionSpacing - spacing).coerceAtLeast(0.dp)),
                variant = actionVariant,
                size = actionSize,
            )
        }
    }
}
