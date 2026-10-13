package io.github.joelkanyi.jenga.component.otp

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.keyframes
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsFocusedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.text.selection.LocalTextSelectionColors
import androidx.compose.foundation.text.selection.TextSelectionColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.autofill.ContentType
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.contentType
import androidx.compose.ui.semantics.password
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.isSpecified
import dev.drewhamilton.poko.Poko
import io.github.joelkanyi.jenga.component.text.JengaText
import io.github.joelkanyi.jenga.component.textfield.JengaTextFieldStatus
import io.github.joelkanyi.jenga.theme.JengaTheme

/** Which characters a [JengaOtpField] accepts. Anything else is dropped as it is typed or pasted. */
public enum class JengaOtpInputType {
    /** Digits `0-9` only, with a numeric keyboard. */
    Numeric,

    /** ASCII letters and digits, with a text keyboard in capitals. */
    Alphanumeric,
}

/** How a [JengaOtpField] draws each cell when no custom `cell` is given. */
public enum class JengaOtpCellStyle {
    /** A bordered box on the surface color. */
    Outlined,

    /** A tinted box with no border until it is active or has a status. */
    Filled,

    /** A single line under each character. */
    Underline,
}

/**
 * What a single cell of a [JengaOtpField] shows, passed to a custom `cell` slot.
 *
 * @property index the cell's position, starting at 0.
 * @property char the character in this cell, or `null` when it is empty.
 * @property isActive whether the next typed character lands in this cell (the field is focused).
 * @property isFilled whether this cell holds a character.
 * @property status the field's validation status.
 * @property masked whether characters should be hidden.
 * @property enabled whether the field is enabled.
 */
@Poko
@Immutable
public class JengaOtpCellState internal constructor(
    public val index: Int,
    public val char: Char?,
    public val isActive: Boolean,
    public val isFilled: Boolean,
    public val status: JengaTextFieldStatus,
    public val masked: Boolean,
    public val enabled: Boolean,
)

/** Resolved colors for a [JengaOtpField]. Override via [JengaOtpFieldDefaults.colors]. */
@Poko
@Immutable
public class JengaOtpFieldColors(
    public val container: Color,
    public val filledContainer: Color,
    public val disabledContainer: Color,
    public val border: Color,
    public val activeBorder: Color,
    public val disabledBorder: Color,
    public val errorBorder: Color,
    public val successBorder: Color,
    public val text: Color,
    public val disabledText: Color,
    public val cursor: Color,
    public val supportingText: Color,
    public val errorSupportingText: Color,
    public val successSupportingText: Color,
) {
    public fun copy(
        container: Color = this.container,
        filledContainer: Color = this.filledContainer,
        disabledContainer: Color = this.disabledContainer,
        border: Color = this.border,
        activeBorder: Color = this.activeBorder,
        disabledBorder: Color = this.disabledBorder,
        errorBorder: Color = this.errorBorder,
        successBorder: Color = this.successBorder,
        text: Color = this.text,
        disabledText: Color = this.disabledText,
        cursor: Color = this.cursor,
        supportingText: Color = this.supportingText,
        errorSupportingText: Color = this.errorSupportingText,
        successSupportingText: Color = this.successSupportingText,
    ): JengaOtpFieldColors = JengaOtpFieldColors(
        container,
        filledContainer,
        disabledContainer,
        border,
        activeBorder,
        disabledBorder,
        errorBorder,
        successBorder,
        text,
        disabledText,
        cursor,
        supportingText,
        errorSupportingText,
        successSupportingText,
    )
}

/** Defaults and token mappings for [JengaOtpField]. */
public object JengaOtpFieldDefaults {
    /** Default number of cells. */
    public const val Length: Int = 6

    /** Largest size of one cell. Cells shrink evenly when the field is narrower than this allows. */
    public val cellSize: DpSize = DpSize(48.dp, 56.dp)

    /** Space between cells. */
    public val cellSpacing: Dp
        @Composable get() = JengaTheme.spacing.sm

    /** Cell shape for [JengaOtpCellStyle.Outlined] and [JengaOtpCellStyle.Filled]. */
    public val shape: Shape
        @Composable get() = JengaTheme.shapes.control

    /** Character text style. */
    public val textStyle: TextStyle
        @Composable get() = JengaTheme.typography.headingSmall

    /** Themed colors. */
    @Composable
    public fun colors(): JengaOtpFieldColors {
        val c = JengaTheme.colors
        return JengaOtpFieldColors(
            container = c.surface,
            filledContainer = c.surfaceSunk,
            disabledContainer = c.surfaceDisabled,
            border = c.borderStrong,
            activeBorder = c.brand,
            disabledBorder = c.borderDisabled,
            errorBorder = c.error,
            successBorder = c.success,
            text = c.textPrimary,
            disabledText = c.contentDisabled,
            cursor = c.brand,
            supportingText = c.textMuted,
            errorSupportingText = c.error,
            successSupportingText = c.success,
        )
    }
}

/**
 * A one-time-code input drawn as a row of cells.
 *
 * One text input sits under all the cells, so typing, backspace, paste, SMS code autofill
 * (on platforms that support it) and screen readers treat the code as a single field. Input is
 * filtered by [inputType] and capped at [length] before [onValueChange] is called, so a pasted
 * `"Your code is 123-456"` becomes `"123456"`.
 *
 * The field never clears itself: after a failed check, set [status] to
 * [JengaTextFieldStatus.Error] (the cells turn red and shake) and clear [value] if the code
 * should be re-entered. The shake runs when [status] changes to Error, so set it back to
 * [JengaTextFieldStatus.Default] when the user edits the code; a later failure then shakes again.
 *
 * @sample io.github.joelkanyi.jenga.samples.JengaOtpFieldSample
 *
 * @param value the code entered so far.
 * @param onValueChange called with the filtered, length-capped code.
 * @param contentDescription what a screen reader announces for the field, e.g. "Verification code".
 * @param modifier the [Modifier] for the whole field (cells + supporting text).
 * @param length the number of cells.
 * @param onComplete called once each time the code reaches [length] characters.
 * @param inputType which characters are accepted; also picks the keyboard.
 * @param status validation status; drives cell borders and the supporting-text color.
 * @param supportingText optional helper or error text under the cells.
 * @param enabled whether the field can be focused and edited.
 * @param readOnly whether the field is focusable but not editable, e.g. while the code is checked.
 * @param masked whether to show a dot instead of each character.
 * @param autoFocus whether to focus the field and show the keyboard when it first appears.
 * @param style how the default cells are drawn; ignored when [cell] is set.
 * @param cellSize the largest size of one cell.
 * @param cellSpacing the space between cells.
 * @param groupSize when above 0, adds a wider gap after every [groupSize] cells (3 gives `123 456`).
 * @param shape the cell shape for [JengaOtpCellStyle.Outlined] and [JengaOtpCellStyle.Filled].
 * @param colors the color set; defaults to [JengaOtpFieldDefaults.colors].
 * @param textStyle the character text style.
 * @param cell optional custom cell; it is drawn inside a box of the cell's size.
 */
@Composable
public fun JengaOtpField(
    value: String,
    onValueChange: (String) -> Unit,
    contentDescription: String,
    modifier: Modifier = Modifier,
    length: Int = JengaOtpFieldDefaults.Length,
    onComplete: ((String) -> Unit)? = null,
    inputType: JengaOtpInputType = JengaOtpInputType.Numeric,
    status: JengaTextFieldStatus = JengaTextFieldStatus.Default,
    supportingText: String? = null,
    enabled: Boolean = true,
    readOnly: Boolean = false,
    masked: Boolean = false,
    autoFocus: Boolean = false,
    style: JengaOtpCellStyle = JengaOtpCellStyle.Outlined,
    cellSize: DpSize = JengaOtpFieldDefaults.cellSize,
    cellSpacing: Dp = JengaOtpFieldDefaults.cellSpacing,
    groupSize: Int = 0,
    shape: Shape = JengaOtpFieldDefaults.shape,
    colors: JengaOtpFieldColors = JengaOtpFieldDefaults.colors(),
    textStyle: TextStyle = JengaOtpFieldDefaults.textStyle,
    cell: (@Composable (JengaOtpCellState) -> Unit)? = null,
) {
    require(length > 0) { "length must be positive, was $length" }
    val code = filterOtp(value, length, inputType)
    val interactionSource = remember { MutableInteractionSource() }
    val focused by interactionSource.collectIsFocusedAsState()
    val focusRequester = remember { FocusRequester() }
    val keyboard = LocalSoftwareKeyboardController.current

    if (autoFocus && enabled) {
        LaunchedEffect(Unit) {
            focusRequester.requestFocus()
            keyboard?.show()
        }
    }

    val shakeOffset = remember { Animatable(0f) }
    val shakeDistance = with(LocalDensity.current) { 8.dp.toPx() }
    LaunchedEffect(status) {
        if (status == JengaTextFieldStatus.Error) {
            shakeOffset.animateTo(
                targetValue = 0f,
                animationSpec = keyframes {
                    durationMillis = 400
                    shakeDistance at 50
                    -shakeDistance at 120
                    shakeDistance * 0.6f at 190
                    -shakeDistance * 0.6f at 260
                    shakeDistance * 0.3f at 330
                },
            )
        }
    }

    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(JengaTheme.spacing.sm),
    ) {
        CompositionLocalProvider(
            LocalTextSelectionColors provides TextSelectionColors(Color.Transparent, Color.Transparent),
        ) {
            BasicTextField(
                value = TextFieldValue(code, TextRange(code.length)),
                onValueChange = { input ->
                    val next = filterOtp(input.text, length, inputType)
                    if (next != code) {
                        onValueChange(next)
                        if (next.length == length) onComplete?.invoke(next)
                    }
                },
                modifier = Modifier
                    .focusRequester(focusRequester)
                    .semantics {
                        this.contentDescription = contentDescription
                        this.contentType = ContentType.SmsOtpCode
                        if (masked) password()
                    },
                enabled = enabled,
                readOnly = readOnly,
                textStyle = textStyle.copy(color = Color.Transparent),
                cursorBrush = SolidColor(Color.Transparent),
                singleLine = true,
                keyboardOptions = keyboardOptionsFor(inputType),
                interactionSource = interactionSource,
                decorationBox = { innerTextField ->
                    Box {
                        Box(Modifier.matchParentSize().alpha(0f)) { innerTextField() }
                        CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Ltr) {
                            OtpCells(
                                code = code,
                                length = length,
                                focused = focused && !readOnly,
                                status = status,
                                enabled = enabled,
                                masked = masked,
                                style = style,
                                cellSize = cellSize,
                                cellSpacing = cellSpacing,
                                groupSize = groupSize,
                                shape = shape,
                                colors = colors,
                                textStyle = textStyle,
                                cell = cell,
                                modifier = Modifier.graphicsLayer { translationX = shakeOffset.value },
                            )
                        }
                    }
                },
            )
        }

        if (supportingText != null) {
            JengaText(
                text = supportingText,
                style = JengaTheme.typography.caption,
                color = when (status) {
                    JengaTextFieldStatus.Error -> colors.errorSupportingText
                    JengaTextFieldStatus.Success -> colors.successSupportingText
                    JengaTextFieldStatus.Default -> colors.supportingText
                },
                textAlign = TextAlign.Center,
            )
        }
    }
}

internal fun filterOtp(input: String, length: Int, inputType: JengaOtpInputType): String {
    val accepted = when (inputType) {
        JengaOtpInputType.Numeric -> input.filter { it in '0'..'9' }
        JengaOtpInputType.Alphanumeric -> input.filter { it in '0'..'9' || it in 'a'..'z' || it in 'A'..'Z' }
    }
    return accepted.take(length)
}

private fun keyboardOptionsFor(inputType: JengaOtpInputType): KeyboardOptions = when (inputType) {
    JengaOtpInputType.Numeric -> KeyboardOptions(
        keyboardType = KeyboardType.Number,
        imeAction = ImeAction.Done,
        autoCorrectEnabled = false,
    )
    JengaOtpInputType.Alphanumeric -> KeyboardOptions(
        keyboardType = KeyboardType.Ascii,
        capitalization = KeyboardCapitalization.Characters,
        imeAction = ImeAction.Done,
        autoCorrectEnabled = false,
    )
}

@Composable
private fun OtpCells(
    code: String,
    length: Int,
    focused: Boolean,
    status: JengaTextFieldStatus,
    enabled: Boolean,
    masked: Boolean,
    style: JengaOtpCellStyle,
    cellSize: DpSize,
    cellSpacing: Dp,
    groupSize: Int,
    shape: Shape,
    colors: JengaOtpFieldColors,
    textStyle: TextStyle,
    cell: (@Composable (JengaOtpCellState) -> Unit)?,
    modifier: Modifier = Modifier,
) {
    val activeIndex = if (code.length < length) code.length else length - 1
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(cellSpacing),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        for (index in 0 until length) {
            if (groupSize > 0 && index > 0 && index % groupSize == 0) {
                Spacer(Modifier.width(cellSpacing))
            }
            val state = JengaOtpCellState(
                index = index,
                char = code.getOrNull(index),
                isActive = focused && enabled && index == activeIndex,
                isFilled = index < code.length,
                status = status,
                masked = masked,
                enabled = enabled,
            )
            Box(
                modifier = Modifier
                    .weight(1f, fill = false)
                    .widthIn(max = cellSize.width)
                    .fillMaxWidth()
                    .height(cellSize.height),
                contentAlignment = Alignment.Center,
            ) {
                if (cell != null) {
                    cell(state)
                } else {
                    DefaultOtpCell(state, style, shape, colors, textStyle)
                }
            }
        }
    }
}

@Composable
private fun DefaultOtpCell(
    state: JengaOtpCellState,
    style: JengaOtpCellStyle,
    shape: Shape,
    colors: JengaOtpFieldColors,
    textStyle: TextStyle,
) {
    val borderColor = when {
        !state.enabled -> colors.disabledBorder
        state.status == JengaTextFieldStatus.Error -> colors.errorBorder
        state.status == JengaTextFieldStatus.Success -> colors.successBorder
        state.isActive -> colors.activeBorder
        else -> colors.border
    }
    val emphasized = state.isActive || state.status != JengaTextFieldStatus.Default
    val borderWidth = if (emphasized) 2.dp else 1.dp
    val container = when {
        !state.enabled -> colors.disabledContainer
        style == JengaOtpCellStyle.Filled -> colors.filledContainer
        style == JengaOtpCellStyle.Underline -> Color.Transparent
        else -> colors.container
    }

    val cellModifier = when (style) {
        JengaOtpCellStyle.Outlined -> Modifier.clip(shape).background(container).border(borderWidth, borderColor, shape)
        JengaOtpCellStyle.Filled -> Modifier.clip(shape).background(container).then(
            if (emphasized) Modifier.border(borderWidth, borderColor, shape) else Modifier,
        )
        JengaOtpCellStyle.Underline -> Modifier.drawBehind {
            val stroke = borderWidth.toPx()
            drawLine(
                color = borderColor,
                start = Offset(0f, size.height - stroke / 2),
                end = Offset(size.width, size.height - stroke / 2),
                strokeWidth = stroke,
            )
        }
    }

    Box(
        modifier = Modifier.fillMaxWidth().fillMaxHeight().then(cellModifier),
        contentAlignment = Alignment.Center,
    ) {
        val textColor = if (state.enabled) colors.text else colors.disabledText
        val duration = JengaTheme.motion.durationFast
        AnimatedContent(
            targetState = state.char,
            transitionSpec = {
                (fadeIn(tween(duration)) + scaleIn(tween(duration), initialScale = 0.6f))
                    .togetherWith(fadeOut(tween(duration)))
            },
            contentAlignment = Alignment.Center,
            label = "otpChar",
        ) { char ->
            if (char != null && state.masked) {
                OtpMaskDot(textColor, textStyle)
            } else if (char != null) {
                JengaText(
                    text = char.toString(),
                    style = textStyle,
                    color = textColor,
                    textAlign = TextAlign.Center,
                )
            } else if (state.isActive) {
                OtpCursor(colors.cursor, textStyle)
            }
        }
    }
}

@Composable
private fun OtpCursor(color: Color, textStyle: TextStyle) {
    val blink = rememberInfiniteTransition(label = "otpCursor")
    val alpha by blink.animateFloat(
        initialValue = 1f,
        targetValue = 0f,
        animationSpec = infiniteRepeatable(
            animation = keyframes {
                durationMillis = 1000
                1f at 499
                0f at 500
            },
            repeatMode = RepeatMode.Restart,
        ),
        label = "otpCursorAlpha",
    )
    val height = with(LocalDensity.current) {
        if (textStyle.fontSize.isSpecified) textStyle.fontSize.toDp() else 20.dp
    }
    Box(
        Modifier
            .width(2.dp)
            .height(height)
            .alpha(alpha)
            .background(color),
    )
}

@Composable
private fun OtpMaskDot(color: Color, textStyle: TextStyle) {
    val diameter = with(LocalDensity.current) {
        if (textStyle.fontSize.isSpecified) (textStyle.fontSize * 0.45f).toDp() else 10.dp
    }
    Box(
        Modifier
            .size(diameter)
            .background(color, CircleShape),
    )
}
