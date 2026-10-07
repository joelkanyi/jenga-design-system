package io.github.joelkanyi.jenga.component.layout

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import io.github.joelkanyi.jenga.theme.JengaTheme

/** Defaults for [JengaFormLayout]. */
public object JengaFormLayoutDefaults {
    /** Padding around the form fields. */
    public val contentPadding: PaddingValues
        @Composable get() = PaddingValues(JengaTheme.spacing.lg)

    /** Gap between form items. */
    public val spacing: Dp
        @Composable get() = JengaTheme.spacing.md
}

/**
 * A keyboard-aware form: a lazily laid out body of fields with an optional
 * [bottomBar] (e.g. a submit button) pinned below it. When the keyboard opens,
 * the body shrinks and the bottom bar rides on top of the keyboard, so the
 * focused field and the primary action stay visible.
 *
 * On Android the activity must draw edge to edge (or use `adjustResize`) for the
 * keyboard insets to reach this layout.
 *
 * @sample io.github.joelkanyi.jenga.samples.JengaFormLayoutSample
 *
 * @param modifier the [Modifier] for the layout.
 * @param bottomBar optional content pinned below the form, above the keyboard.
 * @param state the scroll state of the body.
 * @param contentPadding padding around the form fields.
 * @param spacing the gap between form items.
 * @param content the form fields, laid out lazily in a [LazyListScope].
 */
@Composable
public fun JengaFormLayout(
    modifier: Modifier = Modifier,
    bottomBar: (@Composable () -> Unit)? = null,
    state: LazyListState = rememberLazyListState(),
    contentPadding: PaddingValues = JengaFormLayoutDefaults.contentPadding,
    spacing: Dp = JengaFormLayoutDefaults.spacing,
    content: LazyListScope.() -> Unit,
) {
    Column(modifier = modifier.imePadding()) {
        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f),
            state = state,
            contentPadding = contentPadding,
            verticalArrangement = Arrangement.spacedBy(spacing),
            content = content,
        )
        bottomBar?.invoke()
    }
}
