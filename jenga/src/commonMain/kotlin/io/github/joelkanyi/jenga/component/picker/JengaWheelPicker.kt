package io.github.joelkanyi.jenga.component.picker

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.snapping.rememberSnapFlingBehavior
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.semantics.ProgressBarRangeInfo
import androidx.compose.ui.semantics.clearAndSetSemantics
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.progressBarRangeInfo
import androidx.compose.ui.semantics.setProgress
import androidx.compose.ui.semantics.stateDescription
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import dev.drewhamilton.poko.Poko
import io.github.joelkanyi.jenga.component.text.JengaText
import io.github.joelkanyi.jenga.theme.JengaTheme
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.launch
import kotlin.math.roundToInt

/** Resolved colors for a [JengaWheelPicker]. Override via [JengaWheelPickerDefaults.colors]. */
@Poko
@Immutable
public class JengaWheelPickerColors(
    public val selectedContainer: Color,
    public val selectedContent: Color,
    public val content: Color,
) {
    public fun copy(
        selectedContainer: Color = this.selectedContainer,
        selectedContent: Color = this.selectedContent,
        content: Color = this.content,
    ): JengaWheelPickerColors = JengaWheelPickerColors(selectedContainer, selectedContent, content)
}

/** Defaults and token mappings for [JengaWheelPicker]. */
public object JengaWheelPickerDefaults {
    /** Number of rows visible at once; odd, so one row sits in the middle. */
    public const val VisibleItemCount: Int = 5

    /** Height of each row. */
    public val ItemHeight: Dp = 40.dp

    /** Shape of the band behind the selected row. */
    public val selectionShape: Shape
        @Composable get() = JengaTheme.shapes.md

    /** Row text style. */
    public val textStyle: TextStyle
        @Composable get() = JengaTheme.typography.bodyMedium

    /** Themed colors. */
    @Composable
    public fun colors(): JengaWheelPickerColors {
        val c = JengaTheme.colors
        return JengaWheelPickerColors(
            selectedContainer = c.surfaceSunk,
            selectedContent = c.textPrimary,
            content = c.textMuted,
        )
    }
}

/**
 * A scroll wheel for picking one of [items]: rows scroll vertically and snap so
 * one sits in the highlighted middle band. Tapping a row scrolls it into the
 * middle. For accessibility the wheel is a single adjustable control whose value
 * is the selected row's label.
 *
 * @sample io.github.joelkanyi.jenga.samples.JengaWheelPickerSample
 *
 * @param items the options.
 * @param selectedIndex the index of the selected option.
 * @param onSelectedIndexChange called with the new index once the wheel settles.
 * @param modifier the [Modifier] for the wheel.
 * @param itemLabel the text for an option, also announced by screen readers.
 * @param contentDescription accessibility label for the wheel (e.g. "Year").
 * @param visibleItemCount the number of rows visible at once; a positive odd number.
 * @param itemHeight the height of each row.
 * @param selectionShape the shape of the middle band.
 * @param textStyle the row text style.
 * @param colors the color set; defaults to [JengaWheelPickerDefaults.colors].
 * @param itemContent the content of a row, given the option and whether it is selected.
 */
@Composable
public fun <T> JengaWheelPicker(
    items: List<T>,
    selectedIndex: Int,
    onSelectedIndexChange: (Int) -> Unit,
    modifier: Modifier = Modifier,
    itemLabel: (T) -> String = { it.toString() },
    contentDescription: String? = null,
    visibleItemCount: Int = JengaWheelPickerDefaults.VisibleItemCount,
    itemHeight: Dp = JengaWheelPickerDefaults.ItemHeight,
    selectionShape: Shape = JengaWheelPickerDefaults.selectionShape,
    textStyle: TextStyle = JengaWheelPickerDefaults.textStyle,
    colors: JengaWheelPickerColors = JengaWheelPickerDefaults.colors(),
    itemContent: @Composable (item: T, selected: Boolean) -> Unit = { item, selected ->
        JengaText(
            text = itemLabel(item),
            style = textStyle,
            color = if (selected) colors.selectedContent else colors.content,
            maxLines = 1,
        )
    },
) {
    WheelPicker(
        items = items,
        selectedIndex = selectedIndex,
        onSelectedIndexChange = onSelectedIndexChange,
        modifier = modifier,
        itemLabel = itemLabel,
        contentDescription = contentDescription,
        visibleItemCount = visibleItemCount,
        itemHeight = itemHeight,
        selectionShape = selectionShape,
        colors = colors,
        looping = false,
        itemContent = itemContent,
    )
}

internal class WheelRows(private val optionCount: Int, private val looping: Boolean) {
    val count: Int = if (looping && optionCount > 0) optionCount * LoopCopies else optionCount
    val lastRow: Int get() = (count - 1).coerceAtLeast(0)
    val lastOption: Int get() = (optionCount - 1).coerceAtLeast(0)

    fun optionAt(row: Int): Int = if (looping && optionCount > 0) row % optionCount else row

    fun rowNear(option: Int, row: Int): Int {
        if (!looping) return option
        var step = option - optionAt(row)
        if (step > optionCount / 2) step -= optionCount
        if (step < -optionCount / 2) step += optionCount
        return row + step
    }

    fun firstRow(option: Int): Int = rowNear(option, if (looping) optionCount * (LoopCopies / 2) else 0)
}

private const val LoopCopies = 1000

@Composable
internal fun <T> WheelPicker(
    items: List<T>,
    selectedIndex: Int,
    onSelectedIndexChange: (Int) -> Unit,
    itemLabel: (T) -> String,
    contentDescription: String?,
    visibleItemCount: Int,
    itemHeight: Dp,
    selectionShape: Shape,
    colors: JengaWheelPickerColors,
    looping: Boolean,
    modifier: Modifier = Modifier,
    itemContent: @Composable (item: T, selected: Boolean) -> Unit,
) {
    require(visibleItemCount > 0 && visibleItemCount % 2 == 1) { "visibleItemCount must be a positive odd number" }
    val rows = remember(items.size, looping) { WheelRows(items.size, looping) }
    val listState = rememberLazyListState(initialFirstVisibleItemIndex = rows.firstRow(selectedIndex.coerceIn(0, rows.lastOption)))
    val scope = rememberCoroutineScope()
    val centeredRow by rememberCenteredRow(listState, rows, itemHeight)
    KeepWheelOnSelection(listState, rows, { centeredRow }, selectedIndex, onSelectedIndexChange)
    val selectedLabel = items.getOrNull(rows.optionAt(centeredRow))?.let(itemLabel)
    Box(
        modifier = modifier
            .height(itemHeight * visibleItemCount)
            .wheelSemantics(contentDescription, selectedLabel, rows.optionAt(centeredRow), rows.lastOption) { option ->
                scope.launch { listState.animateScrollToItem(rows.rowNear(option, centeredRow)) }
            },
        contentAlignment = Alignment.Center,
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(itemHeight)
                .clip(selectionShape)
                .background(colors.selectedContainer),
        )
        WheelList(listState, rows, itemHeight, visibleItemCount) { row ->
            itemContent(items[rows.optionAt(row)], row == centeredRow)
        }
    }
}

@Composable
private fun WheelList(
    listState: LazyListState,
    rows: WheelRows,
    itemHeight: Dp,
    visibleItemCount: Int,
    rowContent: @Composable (row: Int) -> Unit,
) {
    val scope = rememberCoroutineScope()
    LazyColumn(
        state = listState,
        flingBehavior = rememberSnapFlingBehavior(listState),
        contentPadding = PaddingValues(vertical = itemHeight * (visibleItemCount / 2)),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        items(rows.count) { row ->
            WheelRow(height = itemHeight, onClick = { scope.launch { listState.animateScrollToItem(row) } }) {
                rowContent(row)
            }
        }
    }
}

@Composable
private fun rememberCenteredRow(listState: LazyListState, rows: WheelRows, itemHeight: Dp): State<Int> {
    val itemHeightPx = with(LocalDensity.current) { itemHeight.toPx() }
    return remember(listState, rows, itemHeightPx) {
        derivedStateOf {
            val extra = if (listState.firstVisibleItemScrollOffset > itemHeightPx / 2) 1 else 0
            (listState.firstVisibleItemIndex + extra).coerceIn(0, rows.lastRow)
        }
    }
}

@Composable
private fun KeepWheelOnSelection(
    listState: LazyListState,
    rows: WheelRows,
    centeredRow: () -> Int,
    selectedIndex: Int,
    onSelectedIndexChange: (Int) -> Unit,
) {
    val currentSelected by rememberUpdatedState(selectedIndex)
    val currentOnChange by rememberUpdatedState(onSelectedIndexChange)
    val currentCenteredRow by rememberUpdatedState(centeredRow)
    var settleCount by remember { mutableIntStateOf(0) }
    LaunchedEffect(listState, rows) {
        snapshotFlow { listState.isScrollInProgress }
            .filter { !it }
            .collect {
                val settled = rows.optionAt(currentCenteredRow())
                if (settled != currentSelected) currentOnChange(settled)
                settleCount++
            }
    }
    LaunchedEffect(selectedIndex, rows, settleCount) {
        val target = selectedIndex.coerceIn(0, rows.lastOption)
        val row = currentCenteredRow()
        if (!listState.isScrollInProgress && rows.optionAt(row) != target) listState.scrollToItem(rows.rowNear(target, row))
    }
}

private fun Modifier.wheelSemantics(
    contentDescription: String?,
    selectedLabel: String?,
    selectedOption: Int,
    lastOption: Int,
    onAdjust: (Int) -> Unit,
): Modifier = clearAndSetSemantics {
    if (contentDescription != null) this.contentDescription = contentDescription
    if (selectedLabel != null) stateDescription = selectedLabel
    progressBarRangeInfo = ProgressBarRangeInfo(
        current = selectedOption.toFloat(),
        range = 0f..lastOption.toFloat(),
        steps = (lastOption - 1).coerceAtLeast(0),
    )
    setProgress { value ->
        onAdjust(value.roundToInt().coerceIn(0, lastOption))
        true
    }
}

@Composable
private fun WheelRow(height: Dp, onClick: () -> Unit, content: @Composable () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(height)
            .clickable(interactionSource = remember { MutableInteractionSource() }, indication = null, onClick = onClick),
        contentAlignment = Alignment.Center,
    ) {
        content()
    }
}
