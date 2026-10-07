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
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.LaunchedEffect
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
    require(visibleItemCount > 0 && visibleItemCount % 2 == 1) { "visibleItemCount must be a positive odd number" }
    val lastIndex = (items.size - 1).coerceAtLeast(0)
    val listState = rememberLazyListState(initialFirstVisibleItemIndex = selectedIndex.coerceIn(0, lastIndex))
    val scope = rememberCoroutineScope()
    val itemHeightPx = with(LocalDensity.current) { itemHeight.toPx() }
    val centeredIndex by remember(lastIndex, itemHeightPx) {
        derivedStateOf {
            val extra = if (listState.firstVisibleItemScrollOffset > itemHeightPx / 2) 1 else 0
            (listState.firstVisibleItemIndex + extra).coerceIn(0, lastIndex)
        }
    }
    val currentSelected by rememberUpdatedState(selectedIndex)
    val currentOnChange by rememberUpdatedState(onSelectedIndexChange)
    var settleCount by remember { mutableIntStateOf(0) }
    LaunchedEffect(listState) {
        snapshotFlow { listState.isScrollInProgress }
            .filter { !it }
            .collect {
                if (centeredIndex != currentSelected) currentOnChange(centeredIndex)
                settleCount++
            }
    }
    LaunchedEffect(selectedIndex, lastIndex, settleCount) {
        val target = selectedIndex.coerceIn(0, lastIndex)
        if (!listState.isScrollInProgress && centeredIndex != target) listState.scrollToItem(target)
    }
    val sidePadding = itemHeight * (visibleItemCount / 2)
    val selectedLabel = items.getOrNull(centeredIndex)?.let(itemLabel)
    val selectedPosition = centeredIndex.toFloat()
    Box(
        modifier = modifier
            .height(itemHeight * visibleItemCount)
            .clearAndSetSemantics {
                if (contentDescription != null) this.contentDescription = contentDescription
                if (selectedLabel != null) stateDescription = selectedLabel
                progressBarRangeInfo = ProgressBarRangeInfo(
                    current = selectedPosition,
                    range = 0f..lastIndex.toFloat(),
                    steps = (lastIndex - 1).coerceAtLeast(0),
                )
                setProgress { value ->
                    val index = value.roundToInt().coerceIn(0, lastIndex)
                    scope.launch { listState.animateScrollToItem(index) }
                    true
                }
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
        LazyColumn(
            state = listState,
            flingBehavior = rememberSnapFlingBehavior(listState),
            contentPadding = PaddingValues(vertical = sidePadding),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            itemsIndexed(items) { index, item ->
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(itemHeight)
                        .clickable(
                            interactionSource = remember { MutableInteractionSource() },
                            indication = null,
                        ) { scope.launch { listState.animateScrollToItem(index) } },
                    contentAlignment = Alignment.Center,
                ) {
                    itemContent(item, index == centeredIndex)
                }
            }
        }
    }
}
