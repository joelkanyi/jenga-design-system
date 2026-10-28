# Change Log

## Unreleased

 * New: `rememberJengaSheetState` takes `confirmDismiss`, called before the sheet closes from a drag, a scrim tap, Back or `hide()`; returning false keeps it open. With it set, Back skips the system shrink animation so a refused close leaves the sheet in place.
 * New: `JengaBottomSheet` (state overload) takes `gesturesEnabled` to turn off dragging the sheet.
 * New: `JengaDatePicker` and `JengaDatePickerDialog` take `dateRange`; days outside it are disabled and the month buttons stop at its ends.
 * New: `JengaDatePicker` and `JengaDatePickerDialog` take `yearPickerLabel`; when set, tapping the month title opens a grid of years.
 * New: `JengaStepIndicator`, a horizontal row of steps (done, current, pending, locked) joined by lines, with `JengaStep`, `JengaStepState` and `JengaStepIndicatorColors`.
 * New: `JengaTimeInput` and `JengaTimePickerDialog` for entering a time with scroll wheels (hour and minute loop) or typed fields, in 12- or 24-hour mode.
 * New: `JengaCaptureFrame`, a capture guide over a camera preview (oval or rectangle at an aspect ratio) with a ready color and hold progress, and `JengaShutterButton`.
 * New: `JengaImageViewer` overload with pages: swipe between images, each zoomed on its own, with a bottom bar that can move to any page.
 * New: `JengaDropdownMenu` draws a 1dp outline in dark themes by default (`JengaDropdownMenuDefaults.border`), so the menu's edge shows on dark screens. Pass `border = null` for none.
 * New: `JengaDialog` (and the date and time picker dialogs) draws the same dark-theme outline (`JengaDialogDefaults.border`); `JengaDialog` takes `border` to change or remove it.
 * Fix: a ring `JengaTimelineMarker` now gives its content the ring color by default, so a number inside it reads on light backgrounds.
 * Fix: `JengaBottomSheet` no longer calls `onDismissRequest` on Back when the sheet did not close.

## Version 0.8.3

_2026-10-26_

 * New: `JengaSearchField` takes `keyboardOptions` and `keyboardActions`.
 * New: `JengaTimelineItem` overload with `marker` and `content` slots and a per-item `lineColor`; `JengaTimelineMarker` (filled or ring, optional content); `JengaTimelinePosition.of(index, count)`.

## Version 0.8.2

_2026-10-15_

 * New: `JengaButton` (text overload) takes `contentPadding` and `textStyle`.
 * New: `JengaProgressTone.Error`, and `error` colors in `JengaProgressStyle`. A `when` over `JengaProgressTone` without an `else` branch needs the new case.
 * New: `JengaCard` takes `selected`, drawing a brand border and reporting the card as selected to accessibility services.
 * New: `JengaButtonVariant.BrandOutline`, a brand border and label on a surface fill, and `brandOutline` colors in `JengaButtonStyle`. A `when` over `JengaButtonVariant` without an `else` branch needs the new case.

## Version 0.8.1

_2026-10-14_

 * New: `JengaStatusPill` takes `leadingIcon`, shown in place of the dot.
 * New: `JengaButton` (text overload) takes `loadingText`, shown beside the spinner while loading.

## Version 0.8.0

_2026-10-13_

 * New: Theme-level component styles. `JengaTheme(components = JengaComponentStyles(...))` sets the look of 31 components for everything below it. A value passed to a call still wins, and an unset value keeps Jenga's default.
 * New: `JengaOtpField`, `JengaOtpResend`, `JengaOtpVerification` and `rememberJengaCountdown` for one-time-code entry.
 * New: `JengaDialog` takes `colors`.
 * New: `JengaButton` (text overload) takes `colors`.
 * New: `JengaRadioListItem` and `JengaCheckboxListItem` take `headlineMaxLines` and `supportingMaxLines`.
 * New: `JengaListSheet` takes `subtitleColor`, `JengaTopAppBar` takes `titleStyle`, `JengaKeyValueRow` takes `labelStyle`, `JengaDropdownMenu` takes `width` and `JengaTabs` takes `selectedTextStyle`.
 * Changed: `JengaTheme`, `JengaDialog`, `JengaButton`, `JengaListSheet`, `JengaTopAppBar`, `JengaKeyValueRow`, `JengaDropdownMenu`, `JengaTabs` and the selection list items gained parameters. Source compatible, but code compiled against 0.7.0 must be recompiled.

## Version 0.7.0

_2026-10-08_

 * New: `JengaBarChart`, a bar chart with fill or scroll layout, selection, value labels and per-call metrics, shapes, styles and colors.
 * New: `JengaListItem` takes `headlineStyle`, `supportingStyle`, `contentSpacing` and `verticalAlignment`, plus an `AnnotatedString` headline overload.
 * New: `JengaListItemColors` has disabled headline, supporting and leading/trailing colors.
 * New: `JengaRadioListItem` and `JengaCheckboxListItem` take `contentSpacing`, `trailingContent`, `headlineStyle`, `supportingStyle` and `colors`. The radio item takes `radioSize`; the checkbox item takes `checkboxColors` and `loading`.
 * New: `JengaEmptyState` and `JengaErrorState` take `titleStyle`, `descriptionStyle`, `contentPadding`, `spacing`, `actionSpacing` and `actionSize`.
 * New: `JengaCard` takes `borderWidth`, `enabled` and `disabledAlpha`.
 * New: `JengaRadioButton` takes `size`.
 * New: `JengaLink` takes `leadingIcon` and `contentPadding`.
 * New: `JengaExpandableRow` takes `shape`, `colors`, `headerPadding` and `contentPadding`.
 * New: `JengaSegmentedControl` takes `selectedTextStyle`.
 * New: `JengaListSheet` and `JengaSideSheet` have `AnnotatedString` subtitle overloads.
 * New: `JengaAvatar` takes `diameter` and `textStyle`, defaulting to the `size` preset.
 * Changed: `JengaRadioButton` and `JengaCheckbox` only reserve the 48dp touch area when they have their own `onClick`. Selection list rows are 56dp tall by default instead of about 72dp.


## Version 0.6.0

_2026-10-07_

 * New: `JengaCalendar`, `JengaDatePicker`, `JengaDatePickerDialog`, `JengaWheelPicker` and `JengaDateOfBirthPicker`. Dates use kotlinx-datetime `LocalDate`, now an API dependency.
 * New: `JengaSelectField` and `JengaSearchTrigger`.
 * New: `JengaInfoBar`, `JengaCardFooter`, `JengaNavigationRailItem`, `JengaThumbnailStrip`, `JengaTimelineItem`, `JengaFormLayout`, `JengaSideSheet` and `JengaImageViewer`.
 * New: `JengaLabelledProgress`.
 * New: Size, padding, style and color options on list rows, the top bar, section header, tabs, segmented control, chip, icon button, icon tile, menu, dialog, status pill, list sheet, FAB, verdict bar, stepper, snackbar and progress indicators.
 * Changed: `JengaBadge` shows its text as written instead of uppercase.
 * Fix: Start-aligned tab indicator position.
 * Fix: `JengaKeyValueRow` inside intrinsic-size layouts.
 * Fix: `JengaSelectField` chevron in right-to-left layouts.


## Version 0.5.0

_2026-10-07_

 * New: `JengaIconTile` and `JengaKeyValueRow`, with colors.
 * New: `JengaRadioListItem` and `JengaCheckboxListItem`.
 * New: `JengaListSheet`, a sheet with a title, subtitle, lazy body and pinned footer. `JengaBottomSheet` stays free-form.
 * New: `JengaListItem` takes `supportingContent`, `enabled` and max lines.
 * New: A mono typography role and `TextStyle.withTabularFigures`.
 * New: 25 line icons in `JengaIcons`.
 * Changed: `JengaSearchField` requires a placeholder and a clear-button label.
 * Fix: The send icon mirrors in right-to-left layouts.
 * Fix: `withTabularFigures` replaces an existing `tnum` setting.


## Version 0.4.1

_2026-10-07_

 * Changed: Android minSdk lowered to 23.
 * Changed: Coil pinned to 3.4.0.
 * Changed: Overlay surfaces use the colors from 0.3.0 again.


## Version 0.4.0

_2026-08-25_

 * New: Web (wasmJs) target.
 * New: `JengaLink`, a tappable text link.
 * Changed: Overlays have more contrast against the page background.


## Version 0.3.0

_2026-07-22_

 * New: `JengaBrand` derives a whole light and dark theme from one seed color.
 * New: A display and body typeface pair in the typography.
 * New: The component icon set is themeable.
 * New: Verdict, stat, stepper, media, shelf and other content blocks.
 * Changed: The dropdown menu container uses the surface color.
 * Fix: Expandable row content padding and the dark focus ring.


## Version 0.2.1

_2026-07-11_

Initial public release: the Jenga Compose Multiplatform design system for Android, iOS and Desktop.
