package io.github.joelkanyi.jenga.catalog

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import io.github.joelkanyi.jenga.component.layout.JengaBox
import io.github.joelkanyi.jenga.component.layout.JengaGrid
import io.github.joelkanyi.jenga.component.layout.JengaInline
import io.github.joelkanyi.jenga.component.layout.JengaStack
import io.github.joelkanyi.jenga.component.text.JengaText
import io.github.joelkanyi.jenga.foundation.typography.withTabularFigures
import io.github.joelkanyi.jenga.theme.JengaTheme

/**
 * The token gallery: principles, then color, typography and the spacing, shape,
 * elevation, sizing and motion scales, each rendered straight from [JengaTheme].
 */
@Composable
fun FoundationsSection(modifier: Modifier = Modifier) {
    JengaStack(modifier = modifier, space = JengaTheme.spacing.xxl) {
        PrinciplesSection()
        ColorsSection()
        TypographySection()
        ScaleSections()
    }
}

@Composable
private fun Section(title: String, content: @Composable () -> Unit) {
    JengaStack(space = JengaTheme.spacing.lg) {
        JengaText(
            text = title.uppercase(),
            style = JengaTheme.typography.label,
            color = JengaTheme.colors.brand,
        )
        JengaBox(
            modifier = Modifier.fillMaxWidth(),
            padding = PaddingValues(JengaTheme.spacing.xl),
            background = JengaTheme.colors.surface,
            shape = JengaTheme.shapes.card,
            border = BorderStroke(1.dp, JengaTheme.colors.border),
        ) {
            content()
        }
    }
}

@Composable
private fun PrinciplesSection() {
    Section("Principles") {
        JengaStack(space = JengaTheme.spacing.lg) {
            Principle("Tokens, never hardcoded values", "All color/space/type come from JengaTheme.*.")
            Principle("Blocks are stateless & data-driven", "State is hoisted; SDUI-friendly.")
            Principle("Override via Defaults, don't fork", "Every block exposes a Defaults object + params.")
            Principle("Accessible by default", "48dp targets, focus rings, WCAG-tested contrast.")
            Principle("Light & dark are first-class", "Every block ships verified in both.")
        }
    }
}

@Composable
private fun Principle(title: String, description: String) {
    JengaStack(space = JengaTheme.spacing.xxs) {
        JengaText(title, style = JengaTheme.typography.titleSmall)
        JengaText(description, style = JengaTheme.typography.bodySmall, color = JengaTheme.colors.textMuted)
    }
}

@Composable
private fun ColorsSection() {
    val c = JengaTheme.colors
    val swatches = listOf(
        "brand" to c.brand, "brandSubtle" to c.brandSubtle, "ink" to c.ink,
        "background" to c.background, "surface" to c.surface, "surfaceVariant" to c.surfaceVariant,
        "surfaceSunk" to c.surfaceSunk, "inverseSurface" to c.inverseSurface, "surfaceDisabled" to c.surfaceDisabled,
        "textPrimary" to c.textPrimary, "textSecondary" to c.textSecondary, "textMuted" to c.textMuted,
        "textFaint" to c.textFaint, "border" to c.border, "borderStrong" to c.borderStrong,
        "success" to c.success, "successContainer" to c.successContainer,
        "warning" to c.warning, "warningContainer" to c.warningContainer,
        "error" to c.error, "errorContainer" to c.errorContainer,
        "info" to c.info, "infoContainer" to c.infoContainer,
    )
    Section("Color") {
        JengaGrid(columns = 2, verticalSpace = JengaTheme.spacing.md) {
            swatches.forEach { (name, color) -> Swatch(name, color) }
        }
    }
}

@Composable
private fun Swatch(name: String, color: Color) {
    JengaInline(space = JengaTheme.spacing.sm) {
        JengaBox(
            modifier = Modifier.size(36.dp),
            background = color,
            shape = JengaTheme.shapes.sm,
            border = BorderStroke(1.dp, JengaTheme.colors.border),
        ) {}
        JengaText(name, style = JengaTheme.typography.bodySmall, color = JengaTheme.colors.textSecondary)
    }
}

@Composable
private fun TypographySection() {
    val t = JengaTheme.typography
    Section("Typography") {
        JengaStack(space = JengaTheme.spacing.lg) {
            TypeRow("display") { JengaText("Jenga", style = t.display) }
            TypeRow("headingLarge") { JengaText("Heading large", style = t.headingLarge) }
            TypeRow("headingMedium") { JengaText("Heading medium", style = t.headingMedium) }
            TypeRow("titleLarge") { JengaText("Title large", style = t.titleLarge) }
            TypeRow("bodyMedium") { JengaText("Body: default text", style = t.bodyMedium) }
            TypeRow("label") { JengaText("LABEL", style = t.label) }
            TypeRow("caption") { JengaText("Caption", style = t.caption) }
            TypeRow("mono") { JengaText("SN-0042-7781", style = t.mono.withTabularFigures()) }
        }
    }
}

@Composable
private fun TypeRow(name: String, sample: @Composable () -> Unit) {
    JengaStack(space = JengaTheme.spacing.xxs) {
        JengaText(name, style = JengaTheme.typography.caption, color = JengaTheme.colors.textMuted)
        sample()
    }
}

@Composable
private fun ScaleSections() {
    val s = JengaTheme.spacing
    JengaStack(space = JengaTheme.spacing.xxl) {
        Section("Spacing") {
            JengaStack(space = JengaTheme.spacing.sm) {
                listOf(s.xs to "xs", s.sm to "sm", s.md to "md", s.lg to "lg", s.xl to "xl", s.xxl to "xxl").forEach { (value, label) ->
                    JengaInline {
                        JengaBox(modifier = Modifier.width(value).height(16.dp), background = JengaTheme.colors.brand, shape = JengaTheme.shapes.xs) {}
                        JengaText("$label (${value.value.toInt()})", style = JengaTheme.typography.bodySmall, color = JengaTheme.colors.textSecondary)
                    }
                }
            }
        }
        Section("Shape") {
            JengaInline(space = JengaTheme.spacing.lg) {
                ShapeSample("sm", JengaTheme.shapes.sm)
                ShapeSample("control", JengaTheme.shapes.control)
                ShapeSample("card", JengaTheme.shapes.card)
                ShapeSample("pill", JengaTheme.shapes.pill)
            }
        }
        Section("Elevation") {
            JengaInline(space = JengaTheme.spacing.xl) {
                ElevationSample("sm", JengaTheme.elevation.sm)
                ElevationSample("md", JengaTheme.elevation.md)
                ElevationSample("lg", JengaTheme.elevation.lg)
            }
        }
        Section("Sizing") {
            val z = JengaTheme.sizing
            JengaStack(space = JengaTheme.spacing.sm) {
                listOf(
                    z.minTouchTarget to "minTouchTarget",
                    z.iconSmall to "iconSmall",
                    z.iconMedium to "iconMedium",
                    z.iconLarge to "iconLarge",
                    z.controlHeightSmall to "controlHeightSmall",
                    z.controlHeightMedium to "controlHeightMedium",
                    z.controlHeightLarge to "controlHeightLarge",
                    z.fieldHeight to "fieldHeight",
                ).forEach { (value, label) ->
                    JengaInline {
                        JengaBox(
                            modifier = Modifier.size(value),
                            background = JengaTheme.colors.brandSubtle,
                            shape = JengaTheme.shapes.xs,
                            border = BorderStroke(1.dp, JengaTheme.colors.border),
                        ) {}
                        JengaText(
                            "$label (${value.value.toInt()})",
                            style = JengaTheme.typography.bodySmall,
                            color = JengaTheme.colors.textSecondary,
                        )
                    }
                }
            }
        }
        Section("Motion") {
            val m = JengaTheme.motion
            JengaStack(space = JengaTheme.spacing.md) {
                JengaText("Durations", style = JengaTheme.typography.titleSmall)
                listOf(
                    m.durationFast to "fast",
                    m.durationMedium to "medium",
                    m.durationSlow to "slow",
                    m.durationSlowest to "slowest",
                ).forEach { (ms, label) ->
                    JengaInline {
                        JengaBox(
                            modifier = Modifier.width((ms / 4).dp).height(12.dp),
                            background = JengaTheme.colors.brand,
                            shape = JengaTheme.shapes.xs,
                        ) {}
                        JengaText(
                            "$label (${ms}ms)",
                            style = JengaTheme.typography.bodySmall,
                            color = JengaTheme.colors.textSecondary,
                        )
                    }
                }
                JengaText("Easing", style = JengaTheme.typography.titleSmall)
                JengaText(
                    "emphasized · standard · linear",
                    style = JengaTheme.typography.bodySmall,
                    color = JengaTheme.colors.textMuted,
                )
            }
        }
    }
}

@Composable
private fun ShapeSample(name: String, shape: Shape) {
    JengaStack(space = JengaTheme.spacing.sm, horizontalAlignment = Alignment.CenterHorizontally) {
        JengaBox(
            modifier = Modifier.size(56.dp),
            background = JengaTheme.colors.surfaceSunk,
            shape = shape,
            border = BorderStroke(1.dp, JengaTheme.colors.borderStrong),
        ) {}
        JengaText(name, style = JengaTheme.typography.caption, color = JengaTheme.colors.textMuted)
    }
}

@Composable
private fun ElevationSample(name: String, elevation: Dp) {
    JengaStack(space = JengaTheme.spacing.sm, horizontalAlignment = Alignment.CenterHorizontally) {
        Box(
            modifier = Modifier
                .size(56.dp)
                .shadow(elevation, JengaTheme.shapes.md)
                .clip(JengaTheme.shapes.md)
                .background(JengaTheme.colors.surface),
        )
        JengaText(name, style = JengaTheme.typography.caption, color = JengaTheme.colors.textMuted)
    }
}
