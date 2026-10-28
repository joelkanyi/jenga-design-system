# Jenga

A **Kotlin Multiplatform + Compose Multiplatform** design system: its own semantic token API
and `Jenga*` components built on Compose Foundation, with Material 3 bridged *internally only*.
Runs on Android, iOS, Desktop (JVM) and the Web (Wasm). Reusable, themeable, accessible, and published to Maven
Central (stable public API, screenshot-tested, lint-enforced).

> *Jenga* is Swahili for **"to build."**

> This is the module-level reference. For installation, branding, the full block inventory and
> how to run the sample on each platform, see the [repository README](../README.md).

---

## Quick start

Wrap your UI once in `JengaTheme`, then use tokens and blocks:

```kotlin
JengaTheme {                       // light/dark follows the system by default
    JengaScaffold(
        topBar = { JengaTopAppBar(title = "Events") },
    ) { padding ->
        JengaStack(modifier = Modifier.padding(padding)) {
            JengaText("Tonight", style = JengaTheme.typography.headingMedium)
            JengaButton(text = "Scan a ticket", onClick = { /* … */ })
        }
    }
}
```

Read tokens anywhere below the theme:

```kotlin
JengaText(color = JengaTheme.colors.textMuted, style = JengaTheme.typography.bodySmall)
Spacer(Modifier.height(JengaTheme.spacing.lg))
```

You never touch `MaterialTheme`; the public API is entirely `JengaTheme.*` / `Jenga*`.

---

## Architecture

Three-tier tokens → blocks → patterns:

```
foundation/   primitive palette → semantic JengaColors → typography/spacing/shape/sizing/elevation/motion
theme/        JengaTheme (CompositionLocals) + internal Material 3 bridge
component/    blocks, one package each: button, card, textfield, calendar, picker, camera,
              feedback, timeline, stepindicator, … (all packages are described in Module.md)
pattern/      organisms composed from blocks (TicketRow, StatCard, SectionHeader)
```

- **Primitive** values (raw hex) live only in `foundation/color/JengaPalette.kt`, **generated**
  from `tokens/primitives.json` (see *Token pipeline*).
- **Semantic** roles (`brand`, `surface`, `textPrimary`, `success`…) are the public color contract;
  they flip for dark mode.
- **Component** tokens are each block's `*Defaults` object (reading only semantic tokens).
- **Component styles** (`JengaTheme(components = JengaComponentStyles(...))`) set a block's look
  for a whole app. A value passed to a call wins, then the theme style, then the `*Defaults`.

---

## Layout primitives

Stop nesting `Row`/`Column`/`Modifier`; spacing comes from tokens:

| Block | Use |
|---|---|
| `JengaStack` | vertical, token gap |
| `JengaInline` | horizontal, token gap |
| `JengaWrap` | wrapping row (chips/tags) |
| `JengaBox` | container with `padding`/`background`/`shape`/`border` params |
| `JengaGrid(columns = N)` | equal columns, token gaps, RTL-safe |
| `JengaSpacer` | one-off token gap |
| `JengaSection` | titled section: header + spaced body |

---

## Theming & multi-brand

Every token is overridable via `JengaTheme(...)` params, and `JengaColors` is a `copy`-able
`@Immutable` data class. Brand the whole theme from a single seed: `jengaBrand(seed = …)`
derives a coherent light and dark theme.

```kotlin
JengaTheme(brand = jengaBrand(seed = Color(0xFF6D28D9))) { … }
```

For a single-scheme tweak, `jengaLightColors().withBrand(Color(0xFF6D28D9))` is a lower-level
helper that rebrands one palette. Dark mode is a semantic swap (`jengaLightColors()` /
`jengaDarkColors()`), not duplicated components.

---

## Accessibility

- Interactive blocks reserve a **48dp** touch target (`minimumInteractiveComponentSize`).
- Color pairs are **contrast-tested** (WCAG AA 4.5:1 for text; 3:1 for brand fills), see
  `JengaContrastTest`.
- Inputs show a focus ring; directional icons `autoMirror` for RTL.
- Every block ships verified **light / dark / large-font / RTL** screenshots.

---

## Tooling & quality gates

| Gate | Command |
|---|---|
| Build | `./gradlew :jenga:assembleDebug` |
| Compose lint (compose-lints) | `./gradlew :jenga:lintDebug` |
| No raw colors in components | `./gradlew :jenga:checkJengaTokenUsage` |
| Unit + contrast/a11y tests | `./gradlew :jenga:testDebugUnitTest` |
| Public API compatibility (BCV) | `./gradlew apiCheck` (update: `apiDump`) |
| Screenshot tests (Roborazzi) | `./gradlew :jenga:verifyRoborazziDebug` (update: `recordRoborazziDebug`) |
| API docs (Dokka) | `./gradlew :jenga:dokkaGenerateHtml` |

All of the above run on CI (`.github/workflows/ci.yml`). Kotlin **explicit-API mode** is on,
so the public surface is intentional and tracked.

### Token pipeline

`tokens/primitives.json` is the source of truth (a Figma → JSON export stand-in). Regenerate the
primitive layer and commit:

```
./gradlew :jenga:generateJengaTokens
```

---

## Principles

1. **Tokens, never hardcoded values.** 2. **Blocks are stateless & data-driven.**
3. **Override via `Defaults` or theme component styles, don't fork.** 4. **Accessible by default.**
5. **Light & dark are first-class.**
