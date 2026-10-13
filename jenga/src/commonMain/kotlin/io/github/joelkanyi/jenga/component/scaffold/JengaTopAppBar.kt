package io.github.joelkanyi.jenga.component.scaffold

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.Immutable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import dev.drewhamilton.poko.Poko
import io.github.joelkanyi.jenga.component.text.JengaText
import io.github.joelkanyi.jenga.theme.JengaTheme
import io.github.joelkanyi.jenga.theme.LocalJengaContentColor

/** Resolved colors for a [JengaTopAppBar]. Override via [JengaTopAppBarDefaults.colors]. */
@Poko
@Immutable
public class JengaTopAppBarColors(
    public val container: Color,
    public val content: Color,
    public val divider: Color,
) {
    public fun copy(
        container: Color = this.container,
        content: Color = this.content,
        divider: Color = this.divider,
    ): JengaTopAppBarColors = JengaTopAppBarColors(container, content, divider)
}

/** Defaults and token mappings for [JengaTopAppBar]. */
/**
 * Theme-wide style for every [JengaTopAppBar], set through
 * [io.github.joelkanyi.jenga.theme.JengaComponentStyles.topAppBar]. A `null` value keeps Jenga's
 * default; arguments passed to a call still win.
 *
 * @property height the minimum bar height, excluding the status-bar inset.
 * @property contentPadding padding inside the bar.
 * @property titleStyle the title text style of the text overload.
 * @property colors the color set.
 */
@Poko
@Immutable
public class JengaTopAppBarStyle(
    public val height: Dp? = null,
    public val contentPadding: PaddingValues? = null,
    public val titleStyle: TextStyle? = null,
    public val colors: JengaTopAppBarColors? = null,
) {
    public fun copy(
        height: Dp? = this.height,
        contentPadding: PaddingValues? = this.contentPadding,
        titleStyle: TextStyle? = this.titleStyle,
        colors: JengaTopAppBarColors? = this.colors,
    ): JengaTopAppBarStyle = JengaTopAppBarStyle(height, contentPadding, titleStyle, colors)
}

/**
 * Defaults for [JengaTopAppBar]. The composable members follow the theme's [JengaTopAppBarStyle];
 * [Height] is Jenga's built-in value, and [JengaTopAppBar] resolves the theme's height over it.
 */
public object JengaTopAppBarDefaults {
    /** Minimum bar height (excluding the status-bar inset). */
    public val Height: Dp = 56.dp

    /** Padding inside the bar. */
    public val contentPadding: PaddingValues
        @Composable get() = JengaTheme.components.topAppBar?.contentPadding
            ?: PaddingValues(horizontal = JengaTheme.spacing.lg, vertical = JengaTheme.spacing.sm)

    /** Title text style. */
    public val titleStyle: TextStyle
        @Composable get() = JengaTheme.components.topAppBar?.titleStyle ?: JengaTheme.typography.titleLarge

    /** Themed colors. */
    @Composable
    public fun colors(): JengaTopAppBarColors {
        JengaTheme.components.topAppBar?.colors?.let { return it }
        val c = JengaTheme.colors
        return JengaTopAppBarColors(
            container = c.surface,
            content = c.textPrimary,
            divider = c.border,
        )
    }
}

/**
 * A top app bar with a title, optional navigation icon and trailing actions.
 *
 * Applies status-bar inset padding itself, so it sits correctly under the system
 * bar whether or not it is hosted in a [JengaScaffold].
 *
 * @sample io.github.joelkanyi.jenga.samples.JengaTopAppBarSample
 *
 * @param title the bar title.
 * @param modifier the [Modifier] for this bar.
 * @param subtitle optional secondary line under the title (e.g. context/details).
 * @param navigationIcon optional leading icon (e.g. back); inherits content color.
 * @param actions trailing actions laid out in a [RowScope]; inherit content color.
 * @param showDivider whether the bottom divider is drawn (e.g. only once content scrolls under it).
 * @param height the minimum bar height, excluding the status-bar inset; defaults to the theme's
 *   [JengaTopAppBarStyle.height], else [JengaTopAppBarDefaults.Height].
 * @param contentPadding padding inside the bar.
 * @param colors the color set; defaults to [JengaTopAppBarDefaults.colors].
 * @param titleStyle the [title] text style.
 */
@Composable
public fun JengaTopAppBar(
    title: String,
    modifier: Modifier = Modifier,
    subtitle: String? = null,
    navigationIcon: (@Composable () -> Unit)? = null,
    actions: @Composable RowScope.() -> Unit = {},
    showDivider: Boolean = true,
    height: Dp = JengaTheme.components.topAppBar?.height ?: JengaTopAppBarDefaults.Height,
    contentPadding: PaddingValues = JengaTopAppBarDefaults.contentPadding,
    colors: JengaTopAppBarColors = JengaTopAppBarDefaults.colors(),
    titleStyle: TextStyle = JengaTopAppBarDefaults.titleStyle,
) {
    JengaTopAppBar(
        title = {
            Column {
                JengaText(
                    text = title,
                    style = titleStyle,
                    color = colors.content,
                    maxLines = 1,
                )
                if (subtitle != null) {
                    JengaText(
                        text = subtitle,
                        style = JengaTheme.typography.caption,
                        color = JengaTheme.colors.textMuted,
                        maxLines = 1,
                    )
                }
            }
        },
        modifier = modifier,
        navigationIcon = navigationIcon,
        actions = actions,
        showDivider = showDivider,
        height = height,
        contentPadding = contentPadding,
        colors = colors,
    )
}

/**
 * A top app bar whose [title] is a slot, for titles that are more than one
 * string (e.g. a monospaced id with a subtitle, or a tappable switcher line).
 *
 * @param title the title content; inherits content color.
 * @param modifier the [Modifier] for this bar.
 * @param navigationIcon optional leading icon (e.g. back); inherits content color.
 * @param actions trailing actions laid out in a [RowScope]; inherit content color.
 * @param showDivider whether the bottom divider is drawn (e.g. only once content scrolls under it).
 * @param height the minimum bar height, excluding the status-bar inset; defaults to the theme's
 *   [JengaTopAppBarStyle.height], else [JengaTopAppBarDefaults.Height].
 * @param contentPadding padding inside the bar.
 * @param colors the color set; defaults to [JengaTopAppBarDefaults.colors].
 */
@Composable
public fun JengaTopAppBar(
    title: @Composable () -> Unit,
    modifier: Modifier = Modifier,
    navigationIcon: (@Composable () -> Unit)? = null,
    actions: @Composable RowScope.() -> Unit = {},
    showDivider: Boolean = true,
    height: Dp = JengaTheme.components.topAppBar?.height ?: JengaTopAppBarDefaults.Height,
    contentPadding: PaddingValues = JengaTopAppBarDefaults.contentPadding,
    colors: JengaTopAppBarColors = JengaTopAppBarDefaults.colors(),
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .background(colors.container)
            .drawBehind {
                if (showDivider) {
                    val stroke = 1.dp.toPx()
                    drawRect(
                        color = colors.divider,
                        topLeft = Offset(0f, size.height - stroke),
                        size = Size(size.width, stroke),
                    )
                }
            }
            .windowInsetsPadding(WindowInsets.statusBars)
            .heightIn(min = height)
            .padding(contentPadding),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(JengaTheme.spacing.sm),
    ) {
        CompositionLocalProvider(LocalJengaContentColor provides colors.content) {
            navigationIcon?.invoke()
            Box(modifier = Modifier.weight(1f)) {
                title()
            }
            actions()
        }
    }
}
