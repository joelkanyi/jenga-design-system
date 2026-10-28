package io.github.joelkanyi.jenga.component.picker

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import io.github.joelkanyi.jenga.component.text.JengaText
import io.github.joelkanyi.jenga.theme.JengaTheme
import kotlinx.datetime.LocalTime

/** How a [JengaTimeInput] lets the user enter a time. */
public enum class JengaTimeInputStyle {
    /** Scroll wheels for hour, minute and AM / PM. Nothing to type. */
    Wheel,

    /** Typed hour and minute fields, with an AM / PM toggle in 12-hour mode. */
    Fields,
}

/**
 * A time input: hour and minute, plus AM / PM in 12-hour mode, as scroll wheels or
 * typed fields. Every visible string comes from the caller.
 *
 * @sample io.github.joelkanyi.jenga.samples.JengaTimeInputSample
 *
 * @param time the selected time.
 * @param onTimeChange called with the new time.
 * @param hourLabel the label for the hour wheel or field (e.g. "Hour").
 * @param minuteLabel the label for the minute wheel or field (e.g. "Minute").
 * @param amLabel the label for the morning half (e.g. "AM").
 * @param pmLabel the label for the afternoon half (e.g. "PM").
 * @param modifier the [Modifier] for the input.
 * @param style wheels or typed fields; see [JengaTimeInputStyle].
 * @param is24Hour whether hours run 0 to 23 with no AM / PM choice.
 * @param minuteStep the gap between minutes on the minute wheel (e.g. 5); typed
 *   fields accept any minute.
 */
@Composable
public fun JengaTimeInput(
    time: LocalTime,
    onTimeChange: (LocalTime) -> Unit,
    hourLabel: String,
    minuteLabel: String,
    amLabel: String,
    pmLabel: String,
    modifier: Modifier = Modifier,
    style: JengaTimeInputStyle = JengaTimeInputStyle.Wheel,
    is24Hour: Boolean = false,
    minuteStep: Int = 1,
) {
    val clock = TwelveHourClock(is24Hour)
    when (style) {
        JengaTimeInputStyle.Wheel -> TimeWheels(time, onTimeChange, clock, hourLabel, minuteLabel, amLabel, pmLabel, minuteStep, modifier)
        JengaTimeInputStyle.Fields -> TimeFields(time, onTimeChange, clock, hourLabel, minuteLabel, amLabel, pmLabel, modifier)
    }
}

@Composable
private fun TimeRow(modifier: Modifier = Modifier, content: @Composable RowScope.() -> Unit) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(JengaTheme.spacing.sm, Alignment.CenterHorizontally),
        verticalAlignment = Alignment.Top,
        content = content,
    )
}

internal class TwelveHourClock(val is24Hour: Boolean) {
    val hours: IntRange = if (is24Hour) 0..23 else 1..12

    fun shownHour(time: LocalTime): Int {
        if (is24Hour) return time.hour
        val hour = time.hour % 12
        return if (hour == 0) 12 else hour
    }

    fun isPm(time: LocalTime): Boolean = time.hour >= 12

    fun withHour(time: LocalTime, shownHour: Int): LocalTime {
        if (is24Hour) return LocalTime(shownHour, time.minute)
        val afternoon = if (isPm(time)) 12 else 0
        return LocalTime(shownHour % 12 + afternoon, time.minute)
    }

    fun withPm(time: LocalTime, pm: Boolean): LocalTime {
        if (pm == isPm(time)) return time
        val hour = if (pm) time.hour + 12 else time.hour - 12
        return LocalTime(hour, time.minute)
    }
}

@Composable
private fun TimeWheels(
    time: LocalTime,
    onTimeChange: (LocalTime) -> Unit,
    clock: TwelveHourClock,
    hourLabel: String,
    minuteLabel: String,
    amLabel: String,
    pmLabel: String,
    minuteStep: Int,
    modifier: Modifier = Modifier,
) = TimeRow(modifier) {
    val hours = clock.hours.toList()
    val minutes = (0 until 60 step minuteStep.coerceIn(1, 30)).toList()
    TimeWheel(hours, hours.indexOf(clock.shownHour(time)), hourLabel, looping = true) {
        onTimeChange(clock.withHour(time, hours[it]))
    }
    TimeWheel(minutes, minutes.indexOfLast { it <= time.minute }, minuteLabel, looping = true) {
        onTimeChange(LocalTime(time.hour, minutes[it]))
    }
    if (!clock.is24Hour) {
        TimeWheel(listOf(amLabel, pmLabel), if (clock.isPm(time)) 1 else 0, null, looping = false) {
            onTimeChange(clock.withPm(time, pm = it == 1))
        }
    }
}

@Composable
private fun <T> TimeWheel(
    items: List<T>,
    selectedIndex: Int,
    contentDescription: String?,
    looping: Boolean,
    onSelectedIndexChange: (Int) -> Unit,
) {
    val colors = JengaWheelPickerDefaults.colors()
    val textStyle = JengaWheelPickerDefaults.textStyle
    WheelPicker(
        items = items,
        selectedIndex = selectedIndex.coerceAtLeast(0),
        onSelectedIndexChange = onSelectedIndexChange,
        modifier = Modifier.width(WheelWidth),
        itemLabel = ::twoDigits,
        contentDescription = contentDescription,
        visibleItemCount = JengaWheelPickerDefaults.VisibleItemCount,
        itemHeight = JengaWheelPickerDefaults.ItemHeight,
        selectionShape = JengaWheelPickerDefaults.selectionShape,
        colors = colors,
        looping = looping,
    ) { item, selected ->
        JengaText(
            text = twoDigits(item),
            style = textStyle,
            color = if (selected) colors.selectedContent else colors.content,
            maxLines = 1,
        )
    }
}

private fun twoDigits(item: Any?): String = if (item is Int) item.toString().padStart(2, '0') else item.toString()

@Composable
private fun TimeFields(
    time: LocalTime,
    onTimeChange: (LocalTime) -> Unit,
    clock: TwelveHourClock,
    hourLabel: String,
    minuteLabel: String,
    amLabel: String,
    pmLabel: String,
    modifier: Modifier = Modifier,
) = TimeRow(modifier) {
    TimeField(clock.shownHour(time), clock.hours, hourLabel) { onTimeChange(clock.withHour(time, it)) }
    Box(modifier = Modifier.height(FieldHeight), contentAlignment = Alignment.Center) {
        JengaText(":", style = JengaTheme.typography.display, color = JengaTheme.colors.textPrimary)
    }
    TimeField(time.minute, 0..59, minuteLabel) { onTimeChange(LocalTime(time.hour, it)) }
    if (!clock.is24Hour) {
        AmPmToggle(clock.isPm(time), amLabel, pmLabel) { onTimeChange(clock.withPm(time, it)) }
    }
}

@Composable
private fun TimeField(value: Int, range: IntRange, label: String, onValueChange: (Int) -> Unit) {
    var text by remember { mutableStateOf(twoDigits(value)) }
    var focused by remember { mutableStateOf(false) }
    LaunchedEffect(value, focused) {
        if (!focused) text = twoDigits(value)
    }
    Column(horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.spacedBy(JengaTheme.spacing.xs)) {
        BasicTextField(
            value = text,
            onValueChange = { typed ->
                if (fitsField(typed, range)) {
                    text = typed
                    typed.toIntOrNull()?.takeIf { it in range }?.let(onValueChange)
                }
            },
            textStyle = digitStyle(focused),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            singleLine = true,
            cursorBrush = SolidColor(JengaTheme.colors.brand),
            modifier = Modifier
                .size(width = FieldWidth, height = FieldHeight)
                .fieldBox(focused)
                .onFocusChanged { focused = it.isFocused }
                .semantics { contentDescription = label },
            decorationBox = { input -> Box(contentAlignment = Alignment.Center) { input() } },
        )
        JengaText(label, style = JengaTheme.typography.caption, color = JengaTheme.colors.textMuted)
    }
}

@Composable
private fun digitStyle(focused: Boolean): TextStyle = JengaTheme.typography.display.copy(
    fontWeight = FontWeight.Medium,
    textAlign = TextAlign.Center,
    color = if (focused) JengaTheme.colors.onBrandSubtle else JengaTheme.colors.textPrimary,
)

internal fun fitsField(typed: String, range: IntRange): Boolean {
    if (typed.length > 2 || !typed.all { it.isDigit() }) return false
    return typed.isEmpty() || typed.toInt() <= range.last
}

@Composable
private fun Modifier.fieldBox(focused: Boolean): Modifier {
    val shape = JengaTheme.shapes.md
    val fill = if (focused) JengaTheme.colors.brandSubtle else JengaTheme.colors.surfaceSunk
    val border = if (focused) Modifier.border(2.dp, JengaTheme.colors.brand, shape) else Modifier
    return clip(shape).background(fill).then(border)
}

@Composable
private fun AmPmToggle(pm: Boolean, amLabel: String, pmLabel: String, onChange: (Boolean) -> Unit) {
    val shape = JengaTheme.shapes.md
    Column(
        modifier = Modifier
            .height(FieldHeight)
            .clip(shape)
            .border(1.dp, JengaTheme.colors.border, shape),
    ) {
        AmPmHalf(amLabel, selected = !pm, onClick = { onChange(false) }, modifier = Modifier.weight(1f))
        AmPmHalf(pmLabel, selected = pm, onClick = { onChange(true) }, modifier = Modifier.weight(1f))
    }
}

@Composable
private fun AmPmHalf(label: String, selected: Boolean, onClick: () -> Unit, modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .width(AmPmWidth)
            .background(if (selected) JengaTheme.colors.brandSubtle else Color.Transparent)
            .selectable(selected = selected, role = Role.RadioButton, onClick = onClick),
        contentAlignment = Alignment.Center,
    ) {
        JengaText(
            label,
            style = JengaTheme.typography.label,
            color = if (selected) JengaTheme.colors.onBrandSubtle else JengaTheme.colors.textMuted,
            modifier = Modifier.fillMaxWidth(),
            textAlign = TextAlign.Center,
        )
    }
}

private val WheelWidth = 64.dp
private val FieldWidth = 84.dp
private val FieldHeight = 64.dp
private val AmPmWidth = 52.dp
