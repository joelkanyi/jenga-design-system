@file:Suppress("unused", "UNUSED_PARAMETER")

package io.github.joelkanyi.jenga.samples

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.unit.dp
import io.github.joelkanyi.jenga.component.action.JengaAction
import io.github.joelkanyi.jenga.component.avatar.JengaAvatar
import io.github.joelkanyi.jenga.component.badge.JengaBadge
import io.github.joelkanyi.jenga.component.badge.JengaBadgeTone
import io.github.joelkanyi.jenga.component.banner.JengaBanner
import io.github.joelkanyi.jenga.component.banner.JengaBannerTone
import io.github.joelkanyi.jenga.component.banner.JengaInfoBar
import io.github.joelkanyi.jenga.component.button.JengaButton
import io.github.joelkanyi.jenga.component.button.JengaButtonVariant
import io.github.joelkanyi.jenga.component.button.JengaIconButton
import io.github.joelkanyi.jenga.component.button.JengaIconButtonVariant
import io.github.joelkanyi.jenga.component.calendar.JengaCalendar
import io.github.joelkanyi.jenga.component.calendar.JengaCalendarDefaults
import io.github.joelkanyi.jenga.component.calendar.JengaDatePicker
import io.github.joelkanyi.jenga.component.calendar.JengaDatePickerDialog
import io.github.joelkanyi.jenga.component.calendar.rememberJengaCalendarState
import io.github.joelkanyi.jenga.component.card.JengaCard
import io.github.joelkanyi.jenga.component.card.JengaCardFooter
import io.github.joelkanyi.jenga.component.chip.JengaChip
import io.github.joelkanyi.jenga.component.divider.JengaDivider
import io.github.joelkanyi.jenga.component.expandable.JengaExpandableRow
import io.github.joelkanyi.jenga.component.fab.JengaFab
import io.github.joelkanyi.jenga.component.feedback.JengaBottomSheet
import io.github.joelkanyi.jenga.component.feedback.JengaDialog
import io.github.joelkanyi.jenga.component.feedback.JengaListSheet
import io.github.joelkanyi.jenga.component.feedback.JengaSideSheet
import io.github.joelkanyi.jenga.component.feedback.JengaSnackbar
import io.github.joelkanyi.jenga.component.feedback.JengaSnackbarHost
import io.github.joelkanyi.jenga.component.feedback.JengaSnackbarTone
import io.github.joelkanyi.jenga.component.feedback.rememberJengaSnackbarHostState
import io.github.joelkanyi.jenga.component.icon.JengaIcon
import io.github.joelkanyi.jenga.component.icon.JengaIconTile
import io.github.joelkanyi.jenga.component.icon.JengaIcons
import io.github.joelkanyi.jenga.component.image.JengaImage
import io.github.joelkanyi.jenga.component.image.JengaImageFit
import io.github.joelkanyi.jenga.component.layout.JengaBox
import io.github.joelkanyi.jenga.component.layout.JengaFormLayout
import io.github.joelkanyi.jenga.component.layout.JengaGrid
import io.github.joelkanyi.jenga.component.layout.JengaInline
import io.github.joelkanyi.jenga.component.layout.JengaSection
import io.github.joelkanyi.jenga.component.layout.JengaStack
import io.github.joelkanyi.jenga.component.layout.JengaWrap
import io.github.joelkanyi.jenga.component.link.JengaLink
import io.github.joelkanyi.jenga.component.list.JengaCheckboxListItem
import io.github.joelkanyi.jenga.component.list.JengaKeyValueEmphasis
import io.github.joelkanyi.jenga.component.list.JengaKeyValueRow
import io.github.joelkanyi.jenga.component.list.JengaListItem
import io.github.joelkanyi.jenga.component.list.JengaRadioListItem
import io.github.joelkanyi.jenga.component.media.JengaImageViewer
import io.github.joelkanyi.jenga.component.media.JengaMediaHero
import io.github.joelkanyi.jenga.component.menu.JengaDropdownMenu
import io.github.joelkanyi.jenga.component.menu.JengaDropdownMenuItem
import io.github.joelkanyi.jenga.component.navigation.JengaNavigationBar
import io.github.joelkanyi.jenga.component.navigation.JengaNavigationBarItem
import io.github.joelkanyi.jenga.component.navigation.JengaNavigationRailItem
import io.github.joelkanyi.jenga.component.picker.JengaDateOfBirthPicker
import io.github.joelkanyi.jenga.component.picker.JengaWheelPicker
import io.github.joelkanyi.jenga.component.progress.JengaCircularProgress
import io.github.joelkanyi.jenga.component.progress.JengaDotStrip
import io.github.joelkanyi.jenga.component.progress.JengaLabelledProgress
import io.github.joelkanyi.jenga.component.progress.JengaLinearProgress
import io.github.joelkanyi.jenga.component.reaction.JengaReactionBar
import io.github.joelkanyi.jenga.component.refresh.JengaPullToRefresh
import io.github.joelkanyi.jenga.component.scaffold.JengaBottomBar
import io.github.joelkanyi.jenga.component.scaffold.JengaScaffold
import io.github.joelkanyi.jenga.component.scaffold.JengaTopAppBar
import io.github.joelkanyi.jenga.component.scanner.JengaScanFeedback
import io.github.joelkanyi.jenga.component.scanner.JengaScannerStatus
import io.github.joelkanyi.jenga.component.scanner.JengaScannerViewfinder
import io.github.joelkanyi.jenga.component.search.JengaSearchField
import io.github.joelkanyi.jenga.component.search.JengaSearchTrigger
import io.github.joelkanyi.jenga.component.selection.JengaCheckbox
import io.github.joelkanyi.jenga.component.selection.JengaRadioButton
import io.github.joelkanyi.jenga.component.selection.JengaToggle
import io.github.joelkanyi.jenga.component.shelf.JengaImageShelf
import io.github.joelkanyi.jenga.component.shelf.JengaShelfCard
import io.github.joelkanyi.jenga.component.shelf.JengaThumbnailStrip
import io.github.joelkanyi.jenga.component.slider.JengaSlider
import io.github.joelkanyi.jenga.component.stat.JengaStatTile
import io.github.joelkanyi.jenga.component.stat.JengaStatTone
import io.github.joelkanyi.jenga.component.state.JengaEmptyState
import io.github.joelkanyi.jenga.component.state.JengaErrorState
import io.github.joelkanyi.jenga.component.status.JengaStatusPill
import io.github.joelkanyi.jenga.component.stepper.JengaStepper
import io.github.joelkanyi.jenga.component.swipe.JengaSwipeToDismiss
import io.github.joelkanyi.jenga.component.tabs.JengaSegmentedControl
import io.github.joelkanyi.jenga.component.tabs.JengaTabs
import io.github.joelkanyi.jenga.component.text.JengaText
import io.github.joelkanyi.jenga.component.textfield.JengaSelectField
import io.github.joelkanyi.jenga.component.textfield.JengaTextField
import io.github.joelkanyi.jenga.component.timeline.JengaTimelineItem
import io.github.joelkanyi.jenga.component.timeline.JengaTimelinePosition
import io.github.joelkanyi.jenga.component.timeline.JengaTimelineState
import io.github.joelkanyi.jenga.component.tooltip.JengaTooltip
import io.github.joelkanyi.jenga.component.verdict.JengaVerdictBar
import io.github.joelkanyi.jenga.component.verdict.JengaVerdictSublines
import io.github.joelkanyi.jenga.component.verdict.JengaVerdictTone
import io.github.joelkanyi.jenga.pattern.JengaSectionHeader
import io.github.joelkanyi.jenga.pattern.JengaStatCard
import io.github.joelkanyi.jenga.pattern.JengaTicketRow
import io.github.joelkanyi.jenga.theme.JengaTheme
import kotlinx.datetime.LocalDate
import kotlinx.datetime.Month
import kotlinx.datetime.YearMonth

/**
 * Compiled usage samples surfaced in API docs via `@sample`. They are kept
 * `internal` and compile with the library, so the examples in the KDoc are
 * always valid.
 */

@Composable
internal fun JengaTextSample() {
    JengaText(text = "Scan a ticket to begin")
}

@Composable
internal fun JengaButtonSample() {
    JengaButton(text = "Check in", onClick = { }, variant = JengaButtonVariant.Primary)
}

@Composable
internal fun JengaIconButtonSample() {
    JengaIconButton(onClick = { }, variant = JengaIconButtonVariant.Standard) {
        JengaIcon(JengaIcons.Search, contentDescription = "Search")
    }
}

@Composable
internal fun JengaPullToRefreshSample() {
    JengaPullToRefresh(isRefreshing = false, onRefresh = { }) {
        JengaText("Pull down to refresh")
    }
}

@Composable
internal fun JengaBottomBarSample() {
    JengaBottomBar {
        JengaButton(text = "Manual entry", onClick = { })
    }
}

@Composable
internal fun JengaStatusPillSample() {
    JengaStatusPill(label = "Synced", tone = JengaBadgeTone.Success)
}

@Composable
internal fun JengaScannerViewfinderSample() {
    JengaScannerViewfinder(status = JengaScannerStatus.Scanning)
}

@Composable
internal fun JengaScanFeedbackSample() {
    JengaScanFeedback(status = JengaScannerStatus.Success)
}

@Composable
internal fun JengaCardSample() {
    JengaCard {
        JengaText(text = "Gate A · Main entrance")
    }
}

@Composable
internal fun JengaBadgeSample() {
    JengaBadge(text = "Valid", tone = JengaBadgeTone.Success)
}

@Composable
internal fun JengaLinkSample() {
    JengaLink(text = "Privacy policy", onClick = {})
}

@Composable
internal fun JengaChipSample() {
    var selected by remember { mutableStateOf(false) }
    JengaChip(label = "Music", selected = selected, onClick = { selected = !selected })
}

@Composable
internal fun JengaTextFieldSample() {
    var value by remember { mutableStateOf("") }
    JengaTextField(
        value = value,
        onValueChange = { value = it },
        label = "Ticket code",
        placeholder = "TKT-…",
    )
}

@Composable
internal fun JengaToggleSample() {
    var on by remember { mutableStateOf(true) }
    JengaToggle(checked = on, onCheckedChange = { on = it })
}

@Composable
internal fun JengaCheckboxSample() {
    var checked by remember { mutableStateOf(false) }
    JengaCheckbox(checked = checked, onCheckedChange = { checked = it })
}

@Composable
internal fun JengaAvatarSample() {
    JengaAvatar(name = "Ada Lovelace")
}

@Composable
internal fun JengaSnackbarSample() {
    JengaSnackbar(
        message = "Saved offline",
        tone = JengaSnackbarTone.Neutral,
        actionLabel = "Undo",
        onAction = { },
    )
}

@Composable
internal fun JengaDialogSample() {
    var open by remember { mutableStateOf(true) }
    if (open) {
        JengaDialog(
            onDismissRequest = { open = false },
            title = "Reset device?",
            text = "This clears the cached gate session.",
            confirmButton = {
                JengaButton(text = "Reset", onClick = { open = false }, variant = JengaButtonVariant.Danger)
            },
            dismissButton = {
                JengaButton(text = "Cancel", onClick = { open = false }, variant = JengaButtonVariant.Ghost)
            },
        )
    }
}

@Composable
internal fun JengaBottomSheetSample() {
    var open by remember { mutableStateOf(true) }
    if (open) {
        JengaBottomSheet(onDismissRequest = { open = false }) {
            JengaText(text = "Select a gate")
        }
    }
}

@Composable
internal fun JengaListSheetSample() {
    var open by remember { mutableStateOf(true) }
    var selected by remember { mutableStateOf("Gate A") }
    if (open) {
        JengaListSheet(
            onDismissRequest = { open = false },
            title = "Select a gate",
            subtitle = "Choose which gate you're scanning at.",
            footer = { JengaButton(text = "Done", onClick = { open = false }, modifier = Modifier.weight(1f)) },
        ) {
            items(listOf("Gate A", "Gate B")) { gate ->
                JengaRadioListItem(headline = gate, selected = gate == selected, onClick = { selected = gate })
            }
        }
    }
}

@Composable
internal fun JengaTopAppBarSample() {
    JengaTopAppBar(title = "Events")
}

@Composable
internal fun JengaScaffoldSample() {
    JengaScaffold(
        topBar = { JengaTopAppBar(title = "Events") },
    ) { padding ->
        Column(modifier = Modifier.padding(padding)) {
            JengaText(text = "Content")
        }
    }
}

@Composable
internal fun JengaEmptyStateSample() {
    JengaEmptyState(
        title = "No scans yet",
        description = "Validated tickets appear here.",
        actionLabel = "Scan a ticket",
        onAction = { },
    )
}

@Composable
internal fun JengaErrorStateSample() {
    JengaErrorState(
        title = "Couldn't load gates",
        actionLabel = "Retry",
        onAction = { },
    )
}

@Composable
internal fun JengaLinearProgressSample() {
    JengaLinearProgress(progress = 0.6f)
}

@Composable
internal fun JengaTicketRowSample() {
    JengaTicketRow(
        attendeeName = "Ada Lovelace",
        detail = "VIP · TKT-2026-001",
        statusLabel = "Valid",
        statusTone = JengaBadgeTone.Success,
    )
}

@Composable
internal fun JengaIconSample() {
    JengaIcon(JengaIcons.Check, contentDescription = "Valid")
}

@Composable
internal fun JengaIconTileSample() {
    JengaIconTile(JengaIcons.Wrench, contentDescription = null, tone = JengaBadgeTone.Brand)
}

@Composable
internal fun JengaDividerSample() {
    JengaDivider()
}

@Composable
internal fun JengaListItemSample() {
    JengaListItem(
        headline = "Gate A",
        supporting = "Main entrance",
        trailingContent = { JengaIcon(JengaIcons.ChevronRight, contentDescription = null) },
        onClick = { },
    )
}

@Composable
internal fun JengaKeyValueRowSample() {
    Column {
        JengaKeyValueRow(label = "Serial number", value = "SN-0042-7781", valueStyle = JengaTheme.typography.mono)
        JengaDivider()
        JengaKeyValueRow(label = "Total", value = "KES 12,450", emphasis = JengaKeyValueEmphasis.Total)
    }
}

@Composable
internal fun JengaNavigationBarSample() {
    JengaNavigationBar {
        JengaNavigationBarItem(
            selected = true,
            onClick = { },
            icon = { JengaIcon(JengaIcons.Search, contentDescription = null) },
            label = "Events",
        )
        JengaNavigationBarItem(
            selected = false,
            onClick = { },
            icon = { JengaIcon(JengaIcons.Check, contentDescription = null) },
            label = "Scan",
        )
    }
}

@Composable
internal fun JengaTabsSample() {
    JengaTabs(selectedIndex = 0, tabs = listOf("Upcoming", "Past"), onSelect = { })
}

@Composable
internal fun JengaSegmentedControlSample() {
    JengaSegmentedControl(selectedIndex = 0, segments = listOf("Day", "Week"), onSelect = { })
}

@Composable
internal fun JengaRadioButtonSample() {
    var selected by remember { mutableStateOf(true) }
    JengaRadioButton(selected = selected, onClick = { selected = !selected })
}

@Composable
internal fun JengaRadioListItemSample() {
    var selected by remember { mutableIntStateOf(0) }
    Column {
        JengaRadioListItem(headline = "Repair", selected = selected == 0, onClick = { selected = 0 })
        JengaRadioListItem(headline = "Replace", selected = selected == 1, onClick = { selected = 1 })
    }
}

@Composable
internal fun JengaCheckboxListItemSample() {
    var checked by remember { mutableStateOf(true) }
    JengaCheckboxListItem(headline = "Battery", checked = checked, onCheckedChange = { checked = it })
}

@Composable
internal fun JengaSearchFieldSample() {
    var query by remember { mutableStateOf("") }
    JengaSearchField(
        value = query,
        onValueChange = { query = it },
        placeholder = "Search tickets",
        clearContentDescription = "Clear search",
    )
}

@Composable
internal fun JengaSearchTriggerSample() {
    JengaSearchTrigger(
        placeholder = "Search tickets",
        onClick = {},
        trailingContent = { JengaIcon(JengaIcons.QrCode, contentDescription = "Scan") },
    )
}

@Composable
internal fun JengaSelectFieldSample() {
    var open by remember { mutableStateOf(false) }
    var fault by remember { mutableStateOf<String?>(null) }
    JengaSelectField(value = fault, onClick = { open = true }, label = "Fault", placeholder = "Choose a fault")
    if (open) {
        JengaListSheet(onDismissRequest = { open = false }, title = "Fault") {
            items(listOf("Battery not charging", "Panel damaged")) { option ->
                JengaRadioListItem(
                    headline = option,
                    selected = option == fault,
                    onClick = {
                        fault = option
                        open = false
                    },
                )
            }
        }
    }
}

@Composable
internal fun JengaSliderSample() {
    var value by remember { androidx.compose.runtime.mutableFloatStateOf(0.5f) }
    JengaSlider(value = value, onValueChange = { value = it })
}

@Composable
internal fun JengaFabSample() {
    JengaFab(onClick = { }) { JengaIcon(JengaIcons.Add, contentDescription = "Add") }
}

@Composable
internal fun JengaBannerSample() {
    JengaBanner(message = "Working offline, scans will sync later.", tone = JengaBannerTone.Info)
}

@Composable
internal fun JengaDropdownMenuSample() {
    var expanded by remember { mutableStateOf(false) }
    JengaDropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
        JengaDropdownMenuItem(text = "Re-enter", onClick = { expanded = false })
        JengaDropdownMenuItem(text = "View history", onClick = { expanded = false })
    }
}

@Composable
internal fun JengaTooltipSample() {
    JengaTooltip(text = "Re-admit this attendee")
}

@Composable
internal fun JengaStatCardSample() {
    JengaStatCard(label = "Checked in", value = "1,284", trendLabel = "+12%", trendTone = JengaBadgeTone.Success)
}

@Composable
internal fun JengaSectionHeaderSample() {
    JengaSectionHeader(title = "Recent scans", actionLabel = "See all", onActionClick = { })
}

@Composable
internal fun JengaStackSample() {
    JengaStack {
        JengaText("One")
        JengaText("Two")
        JengaText("Three")
    }
}

@Composable
internal fun JengaInlineSample() {
    JengaInline {
        JengaText("A")
        JengaText("B")
        JengaText("C")
    }
}

@Composable
internal fun JengaWrapSample() {
    JengaWrap {
        repeat(5) { JengaBadge(text = "tag${it + 1}") }
    }
}

@Composable
internal fun JengaBoxSample() {
    JengaBox(padding = PaddingValues(16.dp), background = JengaTheme.colors.brandSubtle) {
        JengaText("Boxed", color = JengaTheme.colors.onBrandSubtle)
    }
}

@Composable
internal fun JengaGridSample() {
    JengaGrid(columns = 2) {
        JengaText("1")
        JengaText("2")
        JengaText("3")
        JengaText("4")
    }
}

@Composable
internal fun JengaSectionSample() {
    JengaSection(title = "Recent scans", actionLabel = "See all", onActionClick = { }) {
        JengaText("Row one")
        JengaText("Row two")
    }
}

@Composable
internal fun JengaCircularProgressSample() {
    JengaCircularProgress(progress = 0.6f)
}

@Composable
internal fun JengaSnackbarHostSample() {
    val hostState = rememberJengaSnackbarHostState()
    // Host it in your scaffold, then call showSnackbar from a coroutine. A plain
    // confirmation can stay Short; give an action (e.g. Undo) a Long window so it
    // still auto-dismisses instead of sitting there indefinitely:
    //   hostState.showSnackbar("Removed", actionLabel = "Undo",
    //       duration = JengaSnackbarDuration.Long)
    JengaSnackbarHost(hostState)
}

@Composable
internal fun JengaVerdictBarSample() {
    JengaVerdictBar(
        amount = "KES 1,190",
        amountSuffix = "left",
        tone = JengaVerdictTone.Positive,
        label = "This month",
        progress = 0.62f,
        sublines = JengaVerdictSublines("KES 710 of 1,900 spent", "8 of 11 priced"),
        action = JengaAction("Change") { },
    )
}

@Composable
internal fun JengaStatTileSample() {
    JengaStatTile(label = "Revenue", value = "1,284", unit = "KES", tone = JengaStatTone.Success)
}

@Composable
internal fun JengaStepperSample() {
    var count by remember { mutableIntStateOf(2) }
    JengaStepper(value = count, onValueChange = { count = it }, min = 1, max = 9)
}

@Composable
internal fun JengaDotStripSample() {
    JengaDotStrip(filled = 3, total = 5, contentDescription = "3 of 5 steps done")
}

@Composable
internal fun JengaMediaHeroSample() {
    JengaMediaHero(title = "Night Market", support = "Fri · 7pm · Riverside")
}

@Composable
internal fun JengaImageShelfSample() {
    JengaImageShelf(title = "Picked for tonight", action = JengaAction("See all") { }) {
        JengaShelfCard(title = "Grilled veg bowl", meta = "25 min · ~540 kcal")
        JengaShelfCard(title = "Miso ramen", meta = "30 min · ~610 kcal")
    }
}

@Composable
internal fun JengaReactionBarSample() {
    JengaReactionBar(
        onPositive = { },
        onNegative = { },
        positiveContentDescription = "Helpful",
        negativeContentDescription = "Not helpful",
    )
}

@Composable
internal fun JengaExpandableRowSample() {
    var expanded by remember { mutableStateOf(false) }
    JengaExpandableRow(
        expanded = expanded,
        onExpandedChange = { expanded = it },
        header = { JengaText("Delivery details", modifier = Modifier.weight(1f)) },
    ) {
        JengaText("Ships in 2 to 3 business days. Free returns within 30 days.")
    }
}

@Composable
internal fun JengaSwipeToDismissSample() {
    JengaSwipeToDismiss(onDismiss = { }) {
        JengaListItem(headline = "Swipe me away", supporting = "End to start to dismiss")
    }
}

@Composable
internal fun JengaLabelledProgressSample() {
    JengaLabelledProgress(progress = 0.6f, label = AnnotatedString("Repair · step 3 of 5"), valueLabel = "60%")
}

@Composable
internal fun JengaInfoBarSample() {
    JengaInfoBar(text = "View only", leadingIcon = { JengaIcon(JengaIcons.Lock, contentDescription = null) })
}

@Composable
internal fun JengaCardFooterSample() {
    JengaCard(contentPadding = PaddingValues()) {
        JengaText("Battery replacement", modifier = Modifier.padding(JengaTheme.spacing.lg))
        JengaCardFooter(text = "Waiting for parts since Monday")
    }
}

@Composable
internal fun JengaNavigationRailItemSample() {
    var selected by remember { mutableIntStateOf(0) }
    Column {
        JengaNavigationRailItem(
            label = "Tickets",
            selected = selected == 0,
            onClick = { selected = 0 },
            icon = { JengaIcon(JengaIcons.Ticket, contentDescription = null) },
        )
        JengaNavigationRailItem(
            label = "Work orders",
            selected = selected == 1,
            onClick = { selected = 1 },
            icon = { JengaIcon(JengaIcons.Wrench, contentDescription = null) },
            showDot = true,
        )
    }
}

@Composable
internal fun JengaThumbnailStripSample() {
    val photos = listOf("https://example.com/1.jpg", "https://example.com/2.jpg")
    JengaThumbnailStrip(count = photos.size, onAdd = {}, addContentDescription = "Add photo") { index ->
        JengaImage(url = photos[index], contentDescription = null)
    }
}

@Composable
internal fun JengaTimelineSample() {
    Column {
        JengaTimelineItem(JengaTimelineState.Done, "Lead created", JengaTimelinePosition.First, subtitle = "12 Mar")
        JengaTimelineItem(JengaTimelineState.Current, "Quotation", JengaTimelinePosition.Middle)
        JengaTimelineItem(JengaTimelineState.ToDo, "Installation", JengaTimelinePosition.Last)
    }
}

@Composable
internal fun JengaFormLayoutSample() {
    var name by remember { mutableStateOf("") }
    JengaFormLayout(
        bottomBar = {
            JengaButton(text = "Save", onClick = {}, modifier = Modifier.fillMaxWidth().padding(JengaTheme.spacing.lg))
        },
    ) {
        item { JengaTextField(value = name, onValueChange = { name = it }, label = "Customer name") }
    }
}

@Composable
internal fun JengaSideSheetSample() {
    var open by remember { mutableStateOf(true) }
    if (open) {
        JengaSideSheet(
            onDismissRequest = { open = false },
            title = "Filters",
            footer = { JengaButton(text = "Apply", onClick = { open = false }, modifier = Modifier.weight(1f)) },
        ) {
            item { JengaCheckboxListItem(headline = "Open only", checked = true, onCheckedChange = {}) }
        }
    }
}

@Composable
internal fun JengaImageViewerSample() {
    var open by remember { mutableStateOf(true) }
    if (open) {
        JengaImageViewer(onDismissRequest = { open = false }, title = "photo.jpg", closeContentDescription = "Close") {
            JengaImage(url = "https://example.com/photo.jpg", contentDescription = null, fit = JengaImageFit.Contain)
        }
    }
}

@Composable
internal fun JengaDatePickerSample() {
    var date by remember { mutableStateOf<LocalDate?>(null) }
    JengaDatePicker(
        selectedDate = date,
        onSelectedDateChange = { date = it },
        monthTitle = { "${it.month.name} ${it.year}" },
        dayOfWeekLabel = { it.name.take(2) },
        previousMonthContentDescription = "Previous month",
        nextMonthContentDescription = "Next month",
    )
}

@Composable
internal fun JengaDatePickerDialogSample() {
    var open by remember { mutableStateOf(true) }
    if (open) {
        JengaDatePickerDialog(
            onDismissRequest = { open = false },
            onConfirm = { open = false },
            confirmLabel = "OK",
            dismissLabel = "Cancel",
            monthTitle = { "${it.month.name} ${it.year}" },
            dayOfWeekLabel = { it.name.take(2) },
            previousMonthContentDescription = "Previous month",
            nextMonthContentDescription = "Next month",
        )
    }
}

@Composable
internal fun JengaCalendarRangeSample() {
    val month = YearMonth(2026, Month.MARCH)
    val state = rememberJengaCalendarState(startMonth = month, endMonth = month)
    var start by remember { mutableStateOf<LocalDate?>(null) }
    var end by remember { mutableStateOf<LocalDate?>(null) }
    JengaCalendar(state = state) { day ->
        val from = start
        val to = end
        val inRange = from != null && to != null && day.date in from..to
        JengaCalendarDefaults.Day(
            day = day,
            selected = inRange || day.date == from,
            enabled = true,
            isToday = false,
            onClick = {
                if (from == null || to != null || day.date < from) {
                    start = day.date
                    end = null
                } else {
                    end = day.date
                }
            },
        )
    }
}

@Composable
internal fun JengaWheelPickerSample() {
    var index by remember { mutableIntStateOf(0) }
    JengaWheelPicker(items = listOf("Morning", "Afternoon", "Evening"), selectedIndex = index, onSelectedIndexChange = { index = it })
}

@Composable
internal fun JengaDateOfBirthPickerSample() {
    var birthday by remember { mutableStateOf(LocalDate(1990, 3, 14)) }
    JengaDateOfBirthPicker(value = birthday, onValueChange = { birthday = it }, monthLabel = { it.name })
}
