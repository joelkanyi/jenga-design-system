package io.github.joelkanyi.jenga.component.chip

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.selection.selectable
import androidx.compose.material3.minimumInteractiveComponentSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
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
import io.github.joelkanyi.jenga.theme.LocalJengaContentColor

/** Resolved colors for a [JengaChip] across its states. Override via [JengaChipDefaults.colors]. */
@Poko
@Immutable
public class JengaChipColors(
    public val selectedContainer: Color,
    public val selectedContent: Color,
    public val unselectedContainer: Color,
    public val unselectedContent: Color,
    public val unselectedBorder: Color,
    public val disabledContainer: Color,
    public val disabledContent: Color,
    public val selectedBorder: Color = Color.Transparent,
) {
    public fun copy(
        selectedContainer: Color = this.selectedContainer,
        selectedContent: Color = this.selectedContent,
        unselectedContainer: Color = this.unselectedContainer,
        unselectedContent: Color = this.unselectedContent,
        unselectedBorder: Color = this.unselectedBorder,
        disabledContainer: Color = this.disabledContainer,
        disabledContent: Color = this.disabledContent,
        selectedBorder: Color = this.selectedBorder,
    ): JengaChipColors = JengaChipColors(
        selectedContainer,
        selectedContent,
        unselectedContainer,
        unselectedContent,
        unselectedBorder,
        disabledContainer,
        disabledContent,
        selectedBorder,
    )
}

/** Defaults and token mappings for [JengaChip]. Override any of these per call. */
public object JengaChipDefaults {
    /** Default chip shape. */
    public val shape: Shape
        @Composable get() = JengaTheme.shapes.pill

    /** Minimum visual height (touch target is expanded to 48dp separately). */
    public val MinHeight: Dp = 36.dp

    /** Padding inside the chip. */
    public val contentPadding: PaddingValues
        @Composable get() = PaddingValues(horizontal = JengaTheme.spacing.lg, vertical = JengaTheme.spacing.sm)

    /** Label text style. */
    public val textStyle: TextStyle
        @Composable get() = JengaTheme.typography.bodySmall

    /** Themed colors for all chip states. */
    @Composable
    public fun colors(): JengaChipColors {
        val c = JengaTheme.colors
        return JengaChipColors(
            selectedContainer = c.brand,
            selectedContent = c.onBrand,
            unselectedContainer = c.surface,
            unselectedContent = c.textSecondary,
            unselectedBorder = c.borderStrong,
            disabledContainer = c.surfaceDisabled,
            disabledContent = c.contentDisabled,
            selectedBorder = Color.Transparent,
        )
    }
}

/**
 * A compact, toggleable chip for filters and selections.
 *
 * @sample io.github.joelkanyi.jenga.samples.JengaChipSample
 *
 * @param label the chip text.
 * @param selected whether the chip is currently selected.
 * @param onClick called when the chip is tapped.
 * @param modifier the [Modifier] for this chip.
 * @param enabled whether the chip is interactive.
 * @param leadingIcon optional icon before the label (inherits the content color).
 * @param shape the chip shape; defaults to [JengaChipDefaults.shape].
 * @param colors the color set; defaults to [JengaChipDefaults.colors].
 * @param trailingContent optional slot after the label (e.g. a count); inherits the content color.
 * @param textStyle the label text style.
 * @param selectedTextStyle the label text style while selected; defaults to [textStyle].
 * @param minHeight the minimum visual height.
 * @param contentPadding padding inside the chip.
 */
@Composable
public fun JengaChip(
    label: String,
    selected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    leadingIcon: (@Composable () -> Unit)? = null,
    shape: Shape = JengaChipDefaults.shape,
    colors: JengaChipColors = JengaChipDefaults.colors(),
    trailingContent: (@Composable () -> Unit)? = null,
    textStyle: TextStyle = JengaChipDefaults.textStyle,
    selectedTextStyle: TextStyle = textStyle,
    minHeight: Dp = JengaChipDefaults.MinHeight,
    contentPadding: PaddingValues = JengaChipDefaults.contentPadding,
) {
    val container = when {
        !enabled -> colors.disabledContainer
        selected -> colors.selectedContainer
        else -> colors.unselectedContainer
    }
    val contentColor = when {
        !enabled -> colors.disabledContent
        selected -> colors.selectedContent
        else -> colors.unselectedContent
    }
    val borderColor = when {
        !enabled -> Color.Transparent
        selected -> colors.selectedBorder
        else -> colors.unselectedBorder
    }

    Row(
        modifier = modifier
            .minimumInteractiveComponentSize()
            .clip(shape)
            .background(container)
            .then(
                if (borderColor != Color.Transparent) {
                    Modifier.border(1.dp, borderColor, shape)
                } else {
                    Modifier
                },
            )
            .selectable(
                selected = selected,
                enabled = enabled,
                role = Role.Tab,
                onClick = onClick,
            )
            .defaultMinSize(minHeight = minHeight)
            .padding(contentPadding),
        horizontalArrangement = Arrangement.spacedBy(JengaTheme.spacing.xs),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        CompositionLocalProvider(LocalJengaContentColor provides contentColor) {
            leadingIcon?.invoke()
            JengaText(
                text = label,
                style = if (selected) selectedTextStyle else textStyle,
                color = contentColor,
                maxLines = 1,
            )
            trailingContent?.invoke()
        }
    }
}
