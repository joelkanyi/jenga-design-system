# Change Log

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
