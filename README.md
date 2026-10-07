# Jenga

A Kotlin Multiplatform and Compose Multiplatform design system. Brandable design tokens and
ready-made `Jenga*` blocks that render the same on Android, iOS and Desktop, from one shared
`commonMain` codebase.

> *Jenga* is Swahili for "to build". You build screens out of Jenga blocks, not out of raw
> Compose or Material primitives.

[![CI](https://github.com/joelkanyi/jenga/actions/workflows/ci.yml/badge.svg)](https://github.com/joelkanyi/jenga/actions/workflows/ci.yml)
[![Maven Central](https://img.shields.io/maven-central/v/io.github.joelkanyi/jenga.svg)](https://central.sonatype.com/artifact/io.github.joelkanyi/jenga)
[![License](https://img.shields.io/badge/license-Apache%202.0-blue.svg)](LICENSE)
![Platforms](https://img.shields.io/badge/platforms-Android%20%7C%20iOS%20%7C%20Desktop-brightgreen)

```kotlin
JengaTheme {                                  // light and dark follow the system
    JengaScaffold(topBar = { JengaTopAppBar(title = "Events") }) { padding ->
        JengaStack(Modifier.padding(padding)) {
            JengaText("Tonight", style = JengaTheme.typography.headingMedium)
            JengaButton("Scan a ticket", onClick = { })
        }
    }
}
```

Brand it from one seed, and every block re-themes in light and dark:

```kotlin
JengaTheme(brand = jengaBrand(seed = Color(0xFF6D28D9))) { App() }
```

## Platforms

| Android | iOS | Desktop (JVM) |
|---------|-----|---------------|
| `minSdk 23` | `iosArm64`, `iosSimulatorArm64` | yes |

The whole public API lives in `commonMain`, so every platform shares the same components and
tokens. The Intel iOS simulator slice (`iosX64`) is not shipped, because Compose Multiplatform
no longer publishes it.

## Install

Jenga is on Maven Central. Add `mavenCentral()` to your repositories, then the dependency.

Kotlin Multiplatform:

```kotlin
kotlin {
    sourceSets {
        commonMain.dependencies {
            implementation("io.github.joelkanyi:jenga:<version>")
        }
    }
}
```

Android only (the same coordinate resolves the Android variant automatically):

```kotlin
dependencies {
    implementation("io.github.joelkanyi:jenga:<version>")
}
```

## Usage

Wrap your UI once in `JengaTheme`, then read tokens off `JengaTheme.*` and compose `Jenga*`
blocks below it. You never touch `MaterialTheme`; it is bridged internally and never appears in
the public API.

```kotlin
JengaText(color = JengaTheme.colors.textMuted, style = JengaTheme.typography.bodySmall)
Spacer(Modifier.height(JengaTheme.spacing.lg))
```

Prefer the layout primitives over hand-nesting `Row`/`Column` with manual spacing:

```kotlin
JengaStack(space = JengaTheme.spacing.md) {   // vertical, token-spaced
    JengaInline(space = JengaTheme.spacing.sm) { /* horizontal row */ }
}
```

### Blocks

Every block has a `*Defaults` object and closed `enum` variants. Browse them all, live, in the
[catalog](catalog/).

| Need | Blocks |
|------|--------|
| Text | `JengaText` |
| Buttons | `JengaButton`, `JengaIconButton`, `JengaFab` |
| Inputs | `JengaTextField`, `JengaSearchField`, `JengaSlider`, `JengaStepper` |
| Selection | `JengaToggle`, `JengaCheckbox`, `JengaRadioButton`, `JengaSegmentedControl` |
| Containers | `JengaCard`, `JengaListItem`, `JengaDivider`, `JengaExpandableRow`, `JengaSwipeToDismiss` |
| Status and tags | `JengaBadge`, `JengaChip`, `JengaBanner`, `JengaVerdictBar` |
| Scaffolding | `JengaScaffold`, `JengaTopAppBar`, `JengaNavigationBar`, `JengaTabs` |
| Overlays and feedback | `JengaDialog`, `JengaBottomSheet`, `JengaSnackbar`, `JengaTooltip`, `JengaDropdownMenu` |
| Refresh | `JengaPullToRefresh` |
| Media and identity | `JengaAvatar`, `JengaIcon` + `JengaIcons`, `JengaMediaHero`, `JengaImageShelf` |
| Progress | `JengaLinearProgress`, `JengaCircularProgress`, `JengaDotStrip`, shimmer |
| Empty and error | `JengaEmptyState`, `JengaErrorState` |
| Layout | `JengaStack`, `JengaInline`, `JengaWrap`, `JengaGrid`, `JengaBox`, `JengaSpacer`, `JengaSection` |
| Patterns | `JengaTicketRow`, `JengaStatCard`, `JengaStatTile`, `JengaReactionBar` |

### Tokens

Read these off `JengaTheme.*`. Never inline a raw value.

| Token set | Access | Contents |
|-----------|--------|----------|
| Colors | `JengaTheme.colors` | `brand`, `surface`, `background`, `textPrimary/Secondary/Muted`, `border`, status roles (`success`/`warning`/`error`/`info` + containers), and more. Light and dark aware. |
| Typography | `JengaTheme.typography` | `display`, `heading{Large,Medium,Small}`, `title*`, `body*`, `label`, `caption`, `button`. |
| Spacing | `JengaTheme.spacing` | `none, xxs, xs, sm, md, lg, xl, xxl, xxxl`. |
| Shapes | `JengaTheme.shapes` | corner radii (`sm`, `md`, `lg`, `pill`, and more). |
| Sizing | `JengaTheme.sizing` | `minTouchTarget`, icon sizes, control heights, field sizes. |
| Elevation | `JengaTheme.elevation` | the elevation ladder. |
| Motion | `JengaTheme.motion` | durations and easing. |

## Theming

Jenga ships neutral. One brand definition themes everything: a coherent, accessible light and
dark scheme, plus type, shape and density.

```kotlin
JengaTheme(brand = jengaBrand(seed = Color(0xFF6D28D9))) { App() }
```

The seed drives the brand color family (contrast holds by construction). Neutral surfaces and
status colors (success, warning, error, info) keep their tuned defaults, so meaning never
rotates with your hue. Add more brand intent as you need it:

```kotlin
jengaBrand(
    seed = Color(0xFF6D28D9),
    displayFontFamily = rememberPoppins(),   // headings and titles
    bodyFontFamily = rememberInter(),         // body, labels, captions
    corner = JengaCornerStyle.Sharp,          // Rounded (default), Soft, Sharp
    density = JengaDensity.Compact,           // Comfortable (default), Compact, Spacious
    contrast = JengaContrast.High,            // Standard (default), Medium, High
)
```

Fonts and icons are yours. Jenga does not ship third-party fonts; you supply a `FontFamily`
(load `.ttf` files from your app's `composeResources/font/`, which works on every platform).
`JengaIcon` takes any `ImageVector`, and you can re-brand the glyphs components draw:

```kotlin
JengaTheme(icons = rememberJengaIconSet(chevron = MyChevron)) { App() }
```

For full control, override any single token off the derived theme, or hand-author both schemes
as one `JengaScheme(light, dark)` pair. The full ladder, from zero config to a hand-authored
scheme, is in [docs/theming.md](docs/theming.md).

## Accessibility

Accessibility is built in and enforced in CI:

- Interactive blocks reserve a 48dp touch target.
- Color pairs are contrast-tested to WCAG AA (4.5:1 for text, 3:1 for brand fills), including
  brand-derived schemes across a range of seeds.
- Inputs show a focus ring, and directional icons mirror for right-to-left layouts.
- Every block ships verified light, dark, large-font and RTL screenshots.

## See it running

The [catalog](catalog/) is a sample app that renders every token and block on all three
platforms from one shared UI:

```bash
./gradlew :catalog:run            # desktop window
./gradlew :catalog:installDebug   # android device or emulator
# iOS: open catalog/iosApp/iosApp.xcodeproj in Xcode, pick an Apple-Silicon simulator, Run
```

See [catalog/README.md](catalog/README.md) for details.

## Documentation

- Theming, from one seed to a hand-authored scheme: [docs/theming.md](docs/theming.md)
- Architecture and how to contribute: [jenga/README.md](jenga/README.md)
- API reference: `./gradlew :jenga:dokkaGenerateHtml`, then open `jenga/build/dokka/html/index.html`
- Releasing to Maven Central: [RELEASING.md](RELEASING.md)

## License

Apache 2.0. See [LICENSE](LICENSE).
