package io.github.joelkanyi.jenga.catalog

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import io.github.joelkanyi.jenga.component.layout.JengaStack
import io.github.joelkanyi.jenga.component.search.JengaSearchField
import io.github.joelkanyi.jenga.component.text.JengaText
import io.github.joelkanyi.jenga.theme.JengaTheme

/**
 * The searchable component index: a query filters entries by name, group and
 * keywords; matches are shown grouped, each as a live preview beside its code.
 */
@Composable
fun ComponentsGallery(modifier: Modifier = Modifier) {
    val entries = remember { componentEntries() }
    var query by remember { mutableStateOf("") }
    val groups = entries.filter { it.matches(query) }.groupBy { it.group }

    LazyColumn(
        modifier = modifier.fillMaxSize(),
        contentPadding = PaddingValues(
            start = JengaTheme.spacing.xl,
            end = JengaTheme.spacing.xl,
            top = JengaTheme.spacing.xl,
            bottom = JengaTheme.spacing.xxxl,
        ),
        verticalArrangement = Arrangement.spacedBy(JengaTheme.spacing.xxl),
    ) {
        item {
            JengaSearchField(
                value = query,
                onValueChange = { query = it },
                placeholder = "Search components",
                clearContentDescription = "Clear search",
                modifier = Modifier.fillMaxWidth(),
            )
        }
        if (groups.isEmpty()) {
            item {
                JengaText(
                    "No components match \"$query\".",
                    color = JengaTheme.colors.textMuted,
                )
            }
        }
        groups.forEach { (group, items) ->
            item(key = group) {
                JengaStack(space = JengaTheme.spacing.xl) {
                    JengaText(
                        text = group.uppercase(),
                        style = JengaTheme.typography.label,
                        color = JengaTheme.colors.textMuted,
                    )
                    items.forEach { CatalogEntryCard(it) }
                }
            }
        }
    }
}
