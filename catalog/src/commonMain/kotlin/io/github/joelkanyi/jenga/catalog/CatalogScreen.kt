package io.github.joelkanyi.jenga.catalog

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import io.github.joelkanyi.jenga.component.button.JengaButton
import io.github.joelkanyi.jenga.component.button.JengaButtonSize
import io.github.joelkanyi.jenga.component.button.JengaButtonVariant
import io.github.joelkanyi.jenga.component.layout.JengaInline
import io.github.joelkanyi.jenga.component.layout.JengaStack
import io.github.joelkanyi.jenga.component.tabs.JengaTabs
import io.github.joelkanyi.jenga.component.tabs.JengaTabsArrangement
import io.github.joelkanyi.jenga.component.text.JengaText
import io.github.joelkanyi.jenga.theme.JengaTheme

private val surfaces = listOf("Start", "Brand", "Foundations", "Components")

/**
 * The catalog shell, itself built entirely from Jenga. A header with the
 * light/dark toggle, a tab row across the four surfaces (get started, the live
 * brand playground, the token gallery and the searchable component index), and
 * the selected surface below.
 */
@Composable
fun CatalogScreen(
    darkTheme: Boolean,
    onToggleTheme: () -> Unit,
    modifier: Modifier = Modifier,
) {
    var surface by remember { mutableIntStateOf(0) }
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(JengaTheme.colors.background)
            .windowInsetsPadding(WindowInsets.systemBars),
    ) {
        JengaInline(
            modifier = Modifier.padding(horizontal = JengaTheme.spacing.xl, vertical = JengaTheme.spacing.lg),
        ) {
            JengaStack(modifier = Modifier.weight(1f), space = JengaTheme.spacing.xxs) {
                JengaText("Jenga", style = JengaTheme.typography.display)
                JengaText(
                    "Multiplatform design system",
                    style = JengaTheme.typography.bodySmall,
                    color = JengaTheme.colors.textMuted,
                )
            }
            JengaButton(
                text = if (darkTheme) "Dark" else "Light",
                onClick = onToggleTheme,
                variant = JengaButtonVariant.Outline,
                size = JengaButtonSize.Small,
            )
        }
        JengaTabs(
            selectedIndex = surface,
            tabs = surfaces,
            onSelect = { surface = it },
            modifier = Modifier.padding(horizontal = JengaTheme.spacing.xl),
            arrangement = JengaTabsArrangement.Start,
        )
        Box(modifier = Modifier.weight(1f)) {
            when (surface) {
                0 -> Scrollable { GetStartedSection() }
                1 -> Scrollable { BrandPlayground() }
                2 -> Scrollable { FoundationsSection() }
                else -> ComponentsGallery(Modifier.fillMaxSize())
            }
        }
    }
}

@Composable
private fun Scrollable(content: @Composable () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(
                PaddingValues(
                    start = JengaTheme.spacing.xl,
                    end = JengaTheme.spacing.xl,
                    top = JengaTheme.spacing.xl,
                    bottom = JengaTheme.spacing.xxxl,
                ),
            ),
    ) {
        content()
    }
}
