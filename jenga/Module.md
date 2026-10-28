# Module Jenga

**Jenga** is a Kotlin Multiplatform + Compose Multiplatform design system: brandable design
tokens and ready-made `Jenga*` blocks that render identically on Android, iOS, Desktop (JVM) and the Web (Wasm). *Jenga* is Swahili for "to build": you build screens out of Jenga blocks rather than raw
Compose or Material primitives.

The public API is entirely `JengaTheme.*` (tokens) and `Jenga*` (blocks). Material 3 is bridged
internally and never leaks into the surface.

### Getting started

Wrap your UI once in [io.github.joelkanyi.jenga.theme.JengaTheme], then read tokens and use
blocks below it:

```kotlin
JengaTheme {
    JengaScaffold(topBar = { JengaTopAppBar(title = "Events") }) { padding ->
        JengaStack(modifier = Modifier.padding(padding)) {
            JengaText("Tonight", style = JengaTheme.typography.headingMedium)
            JengaButton(text = "Scan a ticket", onClick = { })
        }
    }
}
```

The three layers are **tokens → blocks → patterns**: primitive palette to semantic roles to
each block's `*Defaults`, then organisms composed from blocks.

# Package io.github.joelkanyi.jenga.theme

The theme entry point. [JengaTheme] wraps content, exposes every token set
(`JengaTheme.colors / typography / spacing / shapes / sizing / elevation / motion`) through
CompositionLocals, and bridges to Material 3 internally. Brand and typeface are injected here:
brand the whole theme from one seed with `JengaTheme(brand = jengaBrand(seed = Color(0xFF6D28D9)))`.

# Package io.github.joelkanyi.jenga.foundation.color

The color system: the generated primitive palette, the semantic [JengaColors] roles
(`brand`, `surface`, `textPrimary`, `success`…) that flip for dark mode, and `withBrand`, a
lower-level helper that rebrands a single palette (prefer `jengaBrand(seed = …)` to brand a
whole light+dark theme).

# Package io.github.joelkanyi.jenga.foundation.typography

The Outfit-based type scale ([JengaTypography]) and `rememberJengaFontFamily`. Swap the family
to re-theme typography while keeping the scale.

# Package io.github.joelkanyi.jenga.component.button

Buttons and button-like actions: `JengaButton`, `JengaIconButton`, `JengaFab`, with their
variants and `*Defaults`.

# Package io.github.joelkanyi.jenga.component.layout

Layout primitives (`JengaStack`, `JengaInline`, `JengaWrap`, `JengaGrid`, `JengaBox`,
`JengaSection`, `JengaSpacer`) that read spacing tokens so you never hand-roll gaps.

# Package io.github.joelkanyi.jenga.component.icon

`JengaIcon` and the bundled `JengaIcons` line-icon set, shipped as Compose Resources vector
drawables (no external icon dependency), tinted from tokens.

# Package io.github.joelkanyi.jenga.pattern

Organisms composed from blocks: `JengaTicketRow`, `JengaStatCard`,
`JengaSectionHeader`.

# Package io.github.joelkanyi.jenga.component.action

`JengaAction`: a label and its tap handler bundled together, taken by blocks that offer an optional action, so a label is never shown without something to tap.

# Package io.github.joelkanyi.jenga.component.avatar

`JengaAvatar`: initials or an image in a circle, in fixed sizes.

# Package io.github.joelkanyi.jenga.component.badge

`JengaBadge`: a small count or label in a tone.

# Package io.github.joelkanyi.jenga.component.banner

Page-level messages: `JengaBanner` in a tone, and `JengaInfoBar`, a button-shaped bar that says why no action is available.

# Package io.github.joelkanyi.jenga.component.calendar

Dates: `JengaDatePicker` (optionally limited to a `dateRange`, with a year grid behind the title), `JengaDatePickerDialog`, and `JengaCalendar`, a paging month grid whose day cells are your own content.

# Package io.github.joelkanyi.jenga.component.camera

Camera screens: `JengaCaptureFrame`, a guide over a camera preview for a face, card or page with hold progress, and `JengaShutterButton`.

# Package io.github.joelkanyi.jenga.component.card

`JengaCard` (filled, outlined, selectable) and `JengaCardFooter`.

# Package io.github.joelkanyi.jenga.component.chart

`JengaBarChart`: vertical bars from a shared baseline, optionally selectable.

# Package io.github.joelkanyi.jenga.component.chip

`JengaChip`: a selectable chip with an optional leading icon and trailing content.

# Package io.github.joelkanyi.jenga.component.divider

`JengaDivider`: a hairline between content.

# Package io.github.joelkanyi.jenga.component.expandable

`JengaExpandableRow`: a row that reveals more content when tapped.

# Package io.github.joelkanyi.jenga.component.fab

`JengaFab` and `JengaExtendedFab`: floating primary actions.

# Package io.github.joelkanyi.jenga.component.feedback

Overlays: `JengaDialog`, `JengaBottomSheet` (with `rememberJengaSheetState`, which can refuse a close), `JengaListSheet`, `JengaSideSheet`, and snackbars (`JengaSnackbar`, `JengaSnackbarHost`).

# Package io.github.joelkanyi.jenga.component.image

`JengaImage`: an image loaded from a URL, with a placeholder.

# Package io.github.joelkanyi.jenga.component.link

`JengaLink`: an inline text action.

# Package io.github.joelkanyi.jenga.component.list

List rows: `JengaListItem`, `JengaKeyValueRow`, and rows with a control (`JengaCheckboxListItem`, `JengaRadioListItem`).

# Package io.github.joelkanyi.jenga.component.media

`JengaMediaHero` and `JengaImageViewer`, a full-screen viewer for one image or several pages with zoom.

# Package io.github.joelkanyi.jenga.component.menu

`JengaDropdownMenu` with `JengaDropdownMenuItem` and `JengaDropdownMenuDivider`.

# Package io.github.joelkanyi.jenga.component.navigation

`JengaNavigationBar` with its items, and `JengaNavigationRailItem`.

# Package io.github.joelkanyi.jenga.component.otp

One-time codes: `JengaOtpField`, `JengaOtpResend`, `JengaOtpVerification` and `rememberJengaCountdown`.

# Package io.github.joelkanyi.jenga.component.picker

Scroll-wheel and time entry: `JengaWheelPicker`, `JengaDateOfBirthPicker`, `JengaTimeInput` and `JengaTimePickerDialog`.

# Package io.github.joelkanyi.jenga.component.progress

Progress: linear and circular (determinate and indeterminate), `JengaLabelledProgress`, `JengaDotStrip` and the shimmer modifier.

# Package io.github.joelkanyi.jenga.component.reaction

`JengaReactionBar`: a positive and a negative reaction button side by side.

# Package io.github.joelkanyi.jenga.component.refresh

`JengaPullToRefresh`.

# Package io.github.joelkanyi.jenga.component.scaffold

Screen frame: `JengaScaffold`, `JengaTopAppBar` and `JengaBottomBar`.

# Package io.github.joelkanyi.jenga.component.scanner

Code scanning: `JengaScannerViewfinder` and `JengaScanFeedback`.

# Package io.github.joelkanyi.jenga.component.search

`JengaSearchField` and `JengaSearchTrigger`, which opens a search screen.

# Package io.github.joelkanyi.jenga.component.selection

`JengaToggle`, `JengaCheckbox` and `JengaRadioButton`.

# Package io.github.joelkanyi.jenga.component.shelf

`JengaImageShelf` with `JengaShelfCard`, and `JengaThumbnailStrip`.

# Package io.github.joelkanyi.jenga.component.slider

`JengaSlider`.

# Package io.github.joelkanyi.jenga.component.stat

`JengaStatTile`: a figure with a label and tone.

# Package io.github.joelkanyi.jenga.component.state

`JengaEmptyState` and `JengaErrorState`.

# Package io.github.joelkanyi.jenga.component.status

`JengaStatusPill`: a status label with a dot or icon.

# Package io.github.joelkanyi.jenga.component.stepindicator

`JengaStepIndicator`: a row of steps (done, current, pending, locked) for a multi-step flow.

# Package io.github.joelkanyi.jenga.component.stepper

`JengaStepper`: a minus / plus quantity control.

# Package io.github.joelkanyi.jenga.component.swipe

`JengaSwipeToDismiss`.

# Package io.github.joelkanyi.jenga.component.tabs

`JengaTabs` and `JengaSegmentedControl`.

# Package io.github.joelkanyi.jenga.component.text

`JengaText`, the text block every other block uses.

# Package io.github.joelkanyi.jenga.component.textfield

`JengaTextField` and `JengaSelectField`.

# Package io.github.joelkanyi.jenga.component.timeline

Timelines: `JengaTimelineItem` (fixed states, or your own marker and content), `JengaTimelineMarker` and `JengaTimelinePosition.of`.

# Package io.github.joelkanyi.jenga.component.tooltip

`JengaTooltip`.

# Package io.github.joelkanyi.jenga.component.verdict

`JengaVerdictBar`: an amount with an eyebrow label, optional progress and captions, in a tone.

# Package io.github.joelkanyi.jenga.foundation.brand

`jengaBrand(seed = …)`: brand a whole light and dark theme from one color, with corner style, density and contrast.

# Package io.github.joelkanyi.jenga.foundation.elevation

The elevation ladder ([JengaElevation]).

# Package io.github.joelkanyi.jenga.foundation.motion

Durations and easing ([JengaMotion]).

# Package io.github.joelkanyi.jenga.foundation.shape

Corner radii ([JengaShapes]).

# Package io.github.joelkanyi.jenga.foundation.sizing

Touch targets, icon sizes and control heights ([JengaSizing]).

# Package io.github.joelkanyi.jenga.foundation.spacing

The spacing scale ([JengaSpacing]).
