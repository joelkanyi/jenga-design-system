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
public object JengaTopAppBarDefaults {
    /** Minimum bar height (excluding the status-bar inset). */
    public val Height: Dp = 56.dp

    /** Padding inside the bar. */
    public val contentPadding: PaddingValues
        @Composable get() = PaddingValues(horizontal = JengaTheme.spacing.lg, vertical = JengaTheme.spacing.sm)

    /** Themed colors. */
    @Composable
    public fun colors(): JengaTopAppBarColors {
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
 * @param height the minimum bar height, excluding the status-bar inset.
 * @param contentPadding padding inside the bar.
 * @param colors the color set; defaults to [JengaTopAppBarDefaults.colors].
 */
@Composable
public fun JengaTopAppBar(
    title: String,
    modifier: Modifier = Modifier,
    subtitle: String? = null,
    navigationIcon: (@Composable () -> Unit)? = null,
    actions: @Composable RowScope.() -> Unit = {},
    showDivider: Boolean = true,
    height: Dp = JengaTopAppBarDefaults.Height,
    contentPadding: PaddingValues = JengaTopAppBarDefaults.contentPadding,
    colors: JengaTopAppBarColors = JengaTopAppBarDefaults.colors(),
) {
    JengaTopAppBar(
        title = {
            Column {
                JengaText(
                    text = title,
                    style = JengaTheme.typography.titleLarge,
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
 * @param height the minimum bar height, excluding the status-bar inset.
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
    height: Dp = JengaTopAppBarDefaults.Height,
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
