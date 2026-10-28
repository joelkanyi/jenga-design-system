package io.github.joelkanyi.jenga.component.feedback

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.window.Dialog
import dev.drewhamilton.poko.Poko
import io.github.joelkanyi.jenga.component.button.JengaButton
import io.github.joelkanyi.jenga.component.text.JengaText
import io.github.joelkanyi.jenga.theme.JengaTheme

/** Resolved colors for a [JengaDialog]. Override via [JengaDialogDefaults.colors]. */
@Poko
@Immutable
public class JengaDialogColors(
    public val container: Color,
    public val title: Color,
    public val text: Color,
) {
    public fun copy(
        container: Color = this.container,
        title: Color = this.title,
        text: Color = this.text,
    ): JengaDialogColors = JengaDialogColors(container, title, text)
}

/**
 * Theme-wide style for every [JengaDialog], set through
 * [io.github.joelkanyi.jenga.theme.JengaComponentStyles.dialog]. A `null` value keeps Jenga's
 * default; arguments passed to a [JengaDialog] call still win.
 *
 * @property shape the dialog shape.
 * @property contentPadding padding inside the dialog.
 * @property titleStyle the title text style.
 * @property textStyle the body text style.
 * @property actionSpacing the gap between the actions.
 * @property colors the color set.
 */
@Poko
@Immutable
public class JengaDialogStyle(
    public val shape: Shape? = null,
    public val contentPadding: PaddingValues? = null,
    public val titleStyle: TextStyle? = null,
    public val textStyle: TextStyle? = null,
    public val actionSpacing: Dp? = null,
    public val colors: JengaDialogColors? = null,
) {
    public fun copy(
        shape: Shape? = this.shape,
        contentPadding: PaddingValues? = this.contentPadding,
        titleStyle: TextStyle? = this.titleStyle,
        textStyle: TextStyle? = this.textStyle,
        actionSpacing: Dp? = this.actionSpacing,
        colors: JengaDialogColors? = this.colors,
    ): JengaDialogStyle = JengaDialogStyle(shape, contentPadding, titleStyle, textStyle, actionSpacing, colors)
}

/** Defaults for [JengaDialog]. Each one follows the theme's [JengaDialogStyle]. */
public object JengaDialogDefaults {
    /**
     * Default dialog outline: a hairline in the border color in dark themes, where a
     * dialog on a dark scrim has no visible edge, and none in light themes.
     */
    public val border: BorderStroke?
        @Composable get() = darkThemeHairline()

    /** Default dialog shape. */
    public val shape: Shape
        @Composable get() = JengaTheme.components.dialog?.shape ?: JengaTheme.shapes.card

    /** Padding inside the dialog. */
    public val contentPadding: PaddingValues
        @Composable get() = JengaTheme.components.dialog?.contentPadding ?: PaddingValues(JengaTheme.spacing.xl)

    /** Title text style. */
    public val titleStyle: TextStyle
        @Composable get() = JengaTheme.components.dialog?.titleStyle ?: JengaTheme.typography.headingSmall

    /** Body text style. */
    public val textStyle: TextStyle
        @Composable get() = JengaTheme.components.dialog?.textStyle ?: JengaTheme.typography.bodyMedium

    /** Gap between the actions. */
    public val actionSpacing: Dp
        @Composable get() = JengaTheme.components.dialog?.actionSpacing ?: JengaTheme.spacing.sm

    /** Themed colors. */
    @Composable
    public fun colors(): JengaDialogColors = JengaTheme.components.dialog?.colors ?: JengaDialogColors(
        container = JengaTheme.colors.surface,
        title = JengaTheme.colors.textPrimary,
        text = JengaTheme.colors.textMuted,
    )
}

/**
 * A modal dialog with an optional title and body, plus confirm/dismiss actions.
 * For a destructive confirmation, pass a danger [JengaButton] as [confirmButton].
 *
 * @sample io.github.joelkanyi.jenga.samples.JengaDialogSample
 *
 * @param onDismissRequest called when the user dismisses (scrim tap or back).
 * @param confirmButton the primary action (e.g. a [JengaButton]).
 * @param modifier the [Modifier] for the dialog surface.
 * @param title optional title.
 * @param text optional body text.
 * @param dismissButton optional secondary action.
 * @param shape the dialog shape.
 * @param contentPadding padding inside the dialog.
 * @param titleStyle the [title] text style.
 * @param textStyle the [text] text style.
 * @param actionSpacing gap between the actions.
 * @param colors the color set; defaults to [JengaDialogDefaults.colors].
 * @param border outline around the dialog; defaults to [JengaDialogDefaults.border]
 *   (a hairline in dark themes only). Pass null for none.
 */
@Composable
public fun JengaDialog(
    onDismissRequest: () -> Unit,
    confirmButton: @Composable () -> Unit,
    modifier: Modifier = Modifier,
    title: String? = null,
    text: String? = null,
    dismissButton: (@Composable () -> Unit)? = null,
    shape: Shape = JengaDialogDefaults.shape,
    contentPadding: PaddingValues = JengaDialogDefaults.contentPadding,
    titleStyle: TextStyle = JengaDialogDefaults.titleStyle,
    textStyle: TextStyle = JengaDialogDefaults.textStyle,
    actionSpacing: Dp = JengaDialogDefaults.actionSpacing,
    colors: JengaDialogColors = JengaDialogDefaults.colors(),
    border: BorderStroke? = JengaDialogDefaults.border,
) {
    Dialog(onDismissRequest = onDismissRequest) {
        JengaDialogSurface(
            confirmButton = confirmButton,
            modifier = modifier.dialogOutline(border, shape),
            title = title,
            text = text,
            dismissButton = dismissButton,
            shape = shape,
            contentPadding = contentPadding,
            titleStyle = titleStyle,
            textStyle = textStyle,
            actionSpacing = actionSpacing,
            colors = colors,
        )
    }
}

@Deprecated("Kept for binary compatibility", level = DeprecationLevel.HIDDEN)
@Composable
public fun JengaDialog(
    onDismissRequest: () -> Unit,
    confirmButton: @Composable () -> Unit,
    modifier: Modifier = Modifier,
    title: String? = null,
    text: String? = null,
    dismissButton: (@Composable () -> Unit)? = null,
    shape: Shape = JengaDialogDefaults.shape,
    contentPadding: PaddingValues = JengaDialogDefaults.contentPadding,
    titleStyle: TextStyle = JengaDialogDefaults.titleStyle,
    textStyle: TextStyle = JengaDialogDefaults.textStyle,
    actionSpacing: Dp = JengaDialogDefaults.actionSpacing,
    colors: JengaDialogColors = JengaDialogDefaults.colors(),
) {
    JengaDialog(
        onDismissRequest = onDismissRequest,
        confirmButton = confirmButton,
        modifier = modifier,
        title = title,
        text = text,
        dismissButton = dismissButton,
        shape = shape,
        contentPadding = contentPadding,
        titleStyle = titleStyle,
        textStyle = textStyle,
        actionSpacing = actionSpacing,
        colors = colors,
        border = JengaDialogDefaults.border,
    )
}

@Composable
internal fun JengaDialogSurface(
    confirmButton: @Composable () -> Unit,
    modifier: Modifier = Modifier,
    title: String? = null,
    text: String? = null,
    dismissButton: (@Composable () -> Unit)? = null,
    shape: Shape = JengaDialogDefaults.shape,
    contentPadding: PaddingValues = JengaDialogDefaults.contentPadding,
    titleStyle: TextStyle = JengaDialogDefaults.titleStyle,
    textStyle: TextStyle = JengaDialogDefaults.textStyle,
    actionSpacing: Dp = JengaDialogDefaults.actionSpacing,
    colors: JengaDialogColors = JengaDialogDefaults.colors(),
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .clip(shape)
            .background(colors.container)
            .padding(contentPadding),
        verticalArrangement = Arrangement.spacedBy(JengaTheme.spacing.md),
    ) {
        if (title != null) {
            JengaText(text = title, style = titleStyle, color = colors.title)
        }
        if (text != null) {
            JengaText(
                text = text,
                style = textStyle,
                color = colors.text,
            )
        }
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(actionSpacing, Alignment.End),
        ) {
            if (dismissButton != null) dismissButton()
            confirmButton()
        }
    }
}

internal fun Modifier.dialogOutline(border: BorderStroke?, shape: Shape): Modifier = if (border == null) this else border(border, shape)
