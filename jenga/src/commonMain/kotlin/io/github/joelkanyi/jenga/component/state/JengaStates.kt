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
/**
 * Theme-wide style for every [JengaEmptyState] and [JengaErrorState], set through
 * [io.github.joelkanyi.jenga.theme.JengaComponentStyles.state]. A `null` value keeps Jenga's
 * default; arguments passed to a call still win.
 *
 * @property titleStyle the title text style.
 * @property descriptionStyle the description text style.
 * @property contentPadding padding around the state.
 * @property spacing the gap between the icon, title and description.
 * @property actionSpacing the gap above the action button; falls back to [spacing].
 * @property actionSize the action button size.
 * @property colors the color set.
 */
@Poko
@Immutable
public class JengaStateStyle(
    public val titleStyle: TextStyle? = null,
    public val descriptionStyle: TextStyle? = null,
    public val contentPadding: PaddingValues? = null,
    public val spacing: Dp? = null,
    public val actionSpacing: Dp? = null,
    public val actionSize: JengaButtonSize? = null,
    public val colors: JengaStateColors? = null,
) {
    public fun copy(
        titleStyle: TextStyle? = this.titleStyle,
        descriptionStyle: TextStyle? = this.descriptionStyle,
        contentPadding: PaddingValues? = this.contentPadding,
        spacing: Dp? = this.spacing,
        actionSpacing: Dp? = this.actionSpacing,
        actionSize: JengaButtonSize? = this.actionSize,
        colors: JengaStateColors? = this.colors,
    ): JengaStateStyle = JengaStateStyle(titleStyle, descriptionStyle, contentPadding, spacing, actionSpacing, actionSize, colors)
}

/** Defaults for [JengaEmptyState] and [JengaErrorState]. Each one follows the theme's [JengaStateStyle]. */
public object JengaStateDefaults {
    /** Title text style. */
    public val titleStyle: TextStyle
        @Composable get() = JengaTheme.components.state?.titleStyle ?: JengaTheme.typography.titleLarge

    /** Description text style. */
    public val descriptionStyle: TextStyle
        @Composable get() = JengaTheme.components.state?.descriptionStyle ?: JengaTheme.typography.bodySmall

    /** Padding around the state. */
    public val contentPadding: PaddingValues
        @Composable get() = JengaTheme.components.state?.contentPadding ?: PaddingValues(JengaTheme.spacing.xl)

    /** Gap between the icon, title and description. */
    public val spacing: Dp
        @Composable get() = JengaTheme.components.state?.spacing ?: JengaTheme.spacing.md

    /** Action button size. */
    public val actionSize: JengaButtonSize
        @Composable get() = JengaTheme.components.state?.actionSize ?: JengaButtonSize.Medium

    /** Themed colors. */
    @Composable
    public fun colors(): JengaStateColors = JengaTheme.components.state?.colors ?: JengaStateColors(
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
 * @param actionSpacing the gap above the action button; defaults to the theme's
 *   [JengaStateStyle.actionSpacing], else [spacing].
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
    actionSpacing: Dp = JengaTheme.components.state?.actionSpacing ?: spacing,
    actionSize: JengaButtonSize = JengaStateDefaults.actionSize,
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
 * @param actionSpacing the gap above the action button; defaults to the theme's
 *   [JengaStateStyle.actionSpacing], else [spacing].
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
    actionSpacing: Dp = JengaTheme.components.state?.actionSpacing ?: spacing,
    actionSize: JengaButtonSize = JengaStateDefaults.actionSize,
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
