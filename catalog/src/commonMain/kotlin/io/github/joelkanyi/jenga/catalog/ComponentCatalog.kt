package io.github.joelkanyi.jenga.catalog

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import io.github.joelkanyi.jenga.component.action.JengaAction
import io.github.joelkanyi.jenga.component.avatar.JengaAvatar
import io.github.joelkanyi.jenga.component.avatar.JengaAvatarSize
import io.github.joelkanyi.jenga.component.badge.JengaBadge
import io.github.joelkanyi.jenga.component.badge.JengaBadgeTone
import io.github.joelkanyi.jenga.component.banner.JengaBanner
import io.github.joelkanyi.jenga.component.banner.JengaBannerTone
import io.github.joelkanyi.jenga.component.button.JengaButton
import io.github.joelkanyi.jenga.component.button.JengaButtonVariant
import io.github.joelkanyi.jenga.component.button.JengaIconButton
import io.github.joelkanyi.jenga.component.button.JengaIconButtonVariant
import io.github.joelkanyi.jenga.component.card.JengaCard
import io.github.joelkanyi.jenga.component.card.JengaCardVariant
import io.github.joelkanyi.jenga.component.chip.JengaChip
import io.github.joelkanyi.jenga.component.divider.JengaDivider
import io.github.joelkanyi.jenga.component.expandable.JengaExpandableRow
import io.github.joelkanyi.jenga.component.fab.JengaFab
import io.github.joelkanyi.jenga.component.feedback.JengaBottomSheet
import io.github.joelkanyi.jenga.component.feedback.JengaDialog
import io.github.joelkanyi.jenga.component.feedback.JengaSnackbar
import io.github.joelkanyi.jenga.component.feedback.JengaSnackbarTone
import io.github.joelkanyi.jenga.component.icon.JengaIcon
import io.github.joelkanyi.jenga.component.icon.JengaIconTile
import io.github.joelkanyi.jenga.component.icon.JengaIcons
import io.github.joelkanyi.jenga.component.layout.JengaBox
import io.github.joelkanyi.jenga.component.layout.JengaGrid
import io.github.joelkanyi.jenga.component.layout.JengaInline
import io.github.joelkanyi.jenga.component.layout.JengaStack
import io.github.joelkanyi.jenga.component.layout.JengaWrap
import io.github.joelkanyi.jenga.component.list.JengaCheckboxListItem
import io.github.joelkanyi.jenga.component.list.JengaKeyValueEmphasis
import io.github.joelkanyi.jenga.component.list.JengaKeyValueRow
import io.github.joelkanyi.jenga.component.list.JengaListItem
import io.github.joelkanyi.jenga.component.list.JengaRadioListItem
import io.github.joelkanyi.jenga.component.media.JengaMediaHero
import io.github.joelkanyi.jenga.component.menu.JengaDropdownMenu
import io.github.joelkanyi.jenga.component.menu.JengaDropdownMenuItem
import io.github.joelkanyi.jenga.component.navigation.JengaNavigationBar
import io.github.joelkanyi.jenga.component.navigation.JengaNavigationBarItem
import io.github.joelkanyi.jenga.component.progress.JengaCircularProgress
import io.github.joelkanyi.jenga.component.progress.JengaCircularProgressIndeterminate
import io.github.joelkanyi.jenga.component.progress.JengaDotStrip
import io.github.joelkanyi.jenga.component.progress.JengaLinearProgress
import io.github.joelkanyi.jenga.component.progress.JengaLinearProgressIndeterminate
import io.github.joelkanyi.jenga.component.progress.jengaShimmer
import io.github.joelkanyi.jenga.component.reaction.JengaReactionBar
import io.github.joelkanyi.jenga.component.scaffold.JengaBottomBar
import io.github.joelkanyi.jenga.component.scanner.JengaScanFeedback
import io.github.joelkanyi.jenga.component.scanner.JengaScannerStatus
import io.github.joelkanyi.jenga.component.scanner.JengaScannerViewfinder
import io.github.joelkanyi.jenga.component.search.JengaSearchField
import io.github.joelkanyi.jenga.component.selection.JengaCheckbox
import io.github.joelkanyi.jenga.component.selection.JengaRadioButton
import io.github.joelkanyi.jenga.component.selection.JengaToggle
import io.github.joelkanyi.jenga.component.shelf.JengaImageShelf
import io.github.joelkanyi.jenga.component.shelf.JengaShelfCard
import io.github.joelkanyi.jenga.component.slider.JengaSlider
import io.github.joelkanyi.jenga.component.stat.JengaStatTile
import io.github.joelkanyi.jenga.component.stat.JengaStatTone
import io.github.joelkanyi.jenga.component.status.JengaStatusPill
import io.github.joelkanyi.jenga.component.stepper.JengaStepper
import io.github.joelkanyi.jenga.component.swipe.JengaSwipeToDismiss
import io.github.joelkanyi.jenga.component.tabs.JengaSegmentedControl
import io.github.joelkanyi.jenga.component.tabs.JengaTabs
import io.github.joelkanyi.jenga.component.text.JengaText
import io.github.joelkanyi.jenga.component.textfield.JengaTextField
import io.github.joelkanyi.jenga.component.tooltip.JengaTooltip
import io.github.joelkanyi.jenga.component.verdict.JengaVerdictBar
import io.github.joelkanyi.jenga.component.verdict.JengaVerdictSublines
import io.github.joelkanyi.jenga.component.verdict.JengaVerdictTone
import io.github.joelkanyi.jenga.pattern.JengaSectionHeader
import io.github.joelkanyi.jenga.pattern.JengaStatCard
import io.github.joelkanyi.jenga.pattern.JengaTicketRow
import io.github.joelkanyi.jenga.theme.JengaTheme

/**
 * Every catalog entry, in display order. Each pairs a live demo with the exact
 * call that produces it, and carries a group and keywords for the search index.
 * A plain list, not a composable: the demos are only invoked when rendered.
 */
fun componentEntries(): List<CatalogEntry> = listOf(
    CatalogEntry(
        name = "Button",
        group = "Actions",
        description = "Triggers an action or event, in five variants.",
        keywords = "primary ink ghost outline danger loading",
        code = """
            JengaButton("Primary", onClick = {})
            JengaButton("Outline", onClick = {}, variant = JengaButtonVariant.Outline)
            JengaButton("Danger", onClick = {}, variant = JengaButtonVariant.Danger)
            JengaButton("Loading", onClick = {}, loading = true)
        """.trimIndent(),
    ) {
        JengaStack(space = JengaTheme.spacing.sm) {
            JengaInline(space = JengaTheme.spacing.sm) {
                JengaButton("Primary", {}, variant = JengaButtonVariant.Primary)
                JengaButton("Ink", {}, variant = JengaButtonVariant.Ink)
                JengaButton("Ghost", {}, variant = JengaButtonVariant.Ghost)
            }
            JengaInline(space = JengaTheme.spacing.sm) {
                JengaButton("Outline", {}, variant = JengaButtonVariant.Outline)
                JengaButton("Danger", {}, variant = JengaButtonVariant.Danger)
                JengaButton("Loading", {}, loading = true)
            }
        }
    },
    CatalogEntry(
        name = "Icon button",
        group = "Actions",
        description = "A compact, icon-only action in several emphases.",
        keywords = "standard filled tonal overlay",
        code = """
            JengaIconButton(onClick = {}, variant = JengaIconButtonVariant.Filled) {
                JengaIcon(JengaIcons.Add, contentDescription = "Add")
            }
        """.trimIndent(),
    ) {
        JengaInline {
            JengaIconButton(onClick = {}, variant = JengaIconButtonVariant.Standard) {
                JengaIcon(JengaIcons.Search, contentDescription = "Search")
            }
            JengaIconButton(onClick = {}, variant = JengaIconButtonVariant.Filled) {
                JengaIcon(JengaIcons.Add, contentDescription = "Add")
            }
            JengaIconButton(onClick = {}, variant = JengaIconButtonVariant.Tonal) {
                JengaIcon(JengaIcons.Info, contentDescription = "Info")
            }
            Box(Modifier.background(Color.Black)) {
                JengaIconButton(onClick = {}, variant = JengaIconButtonVariant.Overlay) {
                    JengaIcon(JengaIcons.Flash, contentDescription = "Torch")
                }
            }
        }
    },
    CatalogEntry(
        name = "Floating action button",
        group = "Actions",
        description = "A prominent, circular primary action.",
        keywords = "fab add",
        code = "JengaFab(onClick = {}) { JengaIcon(JengaIcons.Add, contentDescription = \"Add\") }",
    ) {
        JengaFab(onClick = {}) { JengaIcon(JengaIcons.Add, contentDescription = "Add") }
    },
    CatalogEntry(
        name = "Text field",
        group = "Inputs",
        description = "A single-line input with label, placeholder and status.",
        keywords = "input email form",
        code = """
            var text by remember { mutableStateOf("") }
            JengaTextField(text, { text = it }, label = "Email", placeholder = "you@example.com")
        """.trimIndent(),
    ) {
        var text by remember { mutableStateOf("") }
        JengaTextField(text, { text = it }, label = "Email", placeholder = "you@example.com")
    },
    CatalogEntry(
        name = "Search field",
        group = "Inputs",
        description = "A pill-shaped input with a search icon and clear button.",
        keywords = "search query filter",
        code = """
            var query by remember { mutableStateOf("") }
            JengaSearchField(query, { query = it })
        """.trimIndent(),
    ) {
        var query by remember { mutableStateOf("") }
        JengaSearchField(query, { query = it })
    },
    CatalogEntry(
        name = "Toggle",
        group = "Selection",
        description = "A switch for an on or off setting.",
        keywords = "switch on off",
        code = """
            var checked by remember { mutableStateOf(true) }
            JengaToggle(checked = checked, onCheckedChange = { checked = it })
        """.trimIndent(),
    ) {
        var checked by remember { mutableStateOf(true) }
        JengaToggle(checked = checked, onCheckedChange = { checked = it })
    },
    CatalogEntry(
        name = "Checkbox",
        group = "Selection",
        description = "A box for a single on or off choice.",
        keywords = "check tick",
        code = """
            var checked by remember { mutableStateOf(true) }
            JengaCheckbox(checked = checked, onCheckedChange = { checked = it })
        """.trimIndent(),
    ) {
        var checked by remember { mutableStateOf(true) }
        JengaCheckbox(checked = checked, onCheckedChange = { checked = it })
    },
    CatalogEntry(
        name = "Radio button",
        group = "Selection",
        description = "Selects one option from a set.",
        keywords = "radio option single",
        code = """
            var selected by remember { mutableIntStateOf(0) }
            JengaRadioButton(selected = selected == 0, onClick = { selected = 0 })
            JengaRadioButton(selected = selected == 1, onClick = { selected = 1 })
        """.trimIndent(),
    ) {
        var selected by remember { mutableIntStateOf(0) }
        JengaInline {
            JengaRadioButton(selected = selected == 0, onClick = { selected = 0 })
            JengaRadioButton(selected = selected == 1, onClick = { selected = 1 })
        }
    },
    CatalogEntry(
        name = "Radio list item",
        group = "Selection",
        description = "A list row with a radio button, for picking one option.",
        keywords = "radio option single list row",
        code = """
            var selected by remember { mutableIntStateOf(0) }
            JengaRadioListItem("Repair", selected = selected == 0, onClick = { selected = 0 })
            JengaRadioListItem("Replace", selected = selected == 1, onClick = { selected = 1 })
        """.trimIndent(),
    ) {
        var selected by remember { mutableIntStateOf(0) }
        JengaStack(space = JengaTheme.spacing.none) {
            JengaRadioListItem("Repair", selected = selected == 0, onClick = { selected = 0 })
            JengaRadioListItem("Replace", selected = selected == 1, onClick = { selected = 1 })
        }
    },
    CatalogEntry(
        name = "Checkbox list item",
        group = "Selection",
        description = "A list row with a checkbox, for picking several options.",
        keywords = "checkbox multi select list row",
        code = """
            var checked by remember { mutableStateOf(true) }
            JengaCheckboxListItem("Battery", checked = checked, onCheckedChange = { checked = it })
        """.trimIndent(),
    ) {
        var battery by remember { mutableStateOf(true) }
        var panel by remember { mutableStateOf(false) }
        JengaStack(space = JengaTheme.spacing.none) {
            JengaCheckboxListItem("Battery", checked = battery, onCheckedChange = { battery = it })
            JengaCheckboxListItem("Panel", checked = panel, onCheckedChange = { panel = it })
        }
    },
    CatalogEntry(
        name = "Chip",
        group = "Selection",
        description = "A compact, selectable filter or tag.",
        keywords = "filter tag",
        code = """
            var selected by remember { mutableStateOf(true) }
            JengaChip("Filter", selected = selected, onClick = { selected = !selected })
        """.trimIndent(),
    ) {
        var selected by remember { mutableStateOf(true) }
        JengaInline {
            JengaChip("Filter", selected = selected, onClick = { selected = !selected })
            JengaChip("Off", selected = false, onClick = {})
        }
    },
    CatalogEntry(
        name = "Segmented control",
        group = "Selection",
        description = "Switches between a few mutually exclusive options.",
        keywords = "segment toggle group",
        code = """
            var index by remember { mutableIntStateOf(0) }
            JengaSegmentedControl(index, listOf("Day", "Week", "Month"), { index = it })
        """.trimIndent(),
    ) {
        var index by remember { mutableIntStateOf(0) }
        JengaSegmentedControl(index, listOf("Day", "Week", "Month"), { index = it })
    },
    CatalogEntry(
        name = "Card",
        group = "Containers",
        description = "A surface for grouped content, elevated or outlined.",
        keywords = "elevated outlined surface",
        code = """
            JengaCard(variant = JengaCardVariant.Elevated) { JengaText("Elevated") }
            JengaCard(variant = JengaCardVariant.Outlined) { JengaText("Outlined") }
        """.trimIndent(),
    ) {
        JengaInline {
            JengaCard(variant = JengaCardVariant.Elevated) { JengaText("Elevated") }
            JengaCard(variant = JengaCardVariant.Outlined) { JengaText("Outlined") }
        }
    },
    CatalogEntry(
        name = "List item",
        group = "Containers",
        description = "A row with leading, headline, supporting and trailing slots.",
        keywords = "row leading trailing",
        code = """
            JengaListItem(
                headline = "Gate A",
                supporting = "Main entrance",
                leadingContent = { JengaIcon(JengaIcons.Check, contentDescription = null) },
                trailingContent = { JengaIcon(JengaIcons.ChevronRight, contentDescription = null) },
                onClick = {},
            )
            JengaListItem(
                headline = "Gate B",
                supporting = "North wing",
                supportingContent = { JengaBadge("Busy", tone = JengaBadgeTone.Warning) },
                onClick = {},
            )
            JengaListItem(headline = "Gate C", supporting = "Closed", onClick = {}, enabled = false)
        """.trimIndent(),
    ) {
        JengaStack(space = JengaTheme.spacing.none) {
            JengaListItem(
                headline = "Gate A",
                supporting = "Main entrance",
                leadingContent = { JengaIcon(JengaIcons.Check, contentDescription = null, tint = JengaTheme.colors.success) },
                trailingContent = { JengaIcon(JengaIcons.ChevronRight, contentDescription = null) },
                onClick = {},
            )
            JengaListItem(
                headline = "Gate B",
                supporting = "North wing",
                supportingContent = { JengaBadge("Busy", tone = JengaBadgeTone.Warning) },
                onClick = {},
            )
            JengaListItem(headline = "Gate C", supporting = "Closed", onClick = {}, enabled = false)
        }
    },
    CatalogEntry(
        name = "Divider",
        group = "Containers",
        description = "A thin rule that separates content.",
        keywords = "separator line rule",
        code = "JengaDivider()",
    ) {
        JengaDivider()
    },
    CatalogEntry(
        name = "Banner",
        group = "Feedback",
        description = "An inline, tinted message tied to a status.",
        keywords = "inline message info error",
        code = """
            JengaBanner("Working offline; scans will sync later.", tone = JengaBannerTone.Info)
            JengaBanner("Already scanned.", tone = JengaBannerTone.Error)
        """.trimIndent(),
    ) {
        JengaStack(space = JengaTheme.spacing.sm) {
            JengaBanner("Working offline; scans will sync later.", tone = JengaBannerTone.Info)
            JengaBanner("Already scanned.", tone = JengaBannerTone.Error)
        }
    },
    CatalogEntry(
        name = "Snackbar",
        group = "Feedback",
        description = "A brief message with an optional action.",
        keywords = "toast action undo",
        code = """
            JengaSnackbar("Saved offline", tone = JengaSnackbarTone.Neutral, actionLabel = "Undo", onAction = {})
        """.trimIndent(),
    ) {
        JengaSnackbar("Saved offline", tone = JengaSnackbarTone.Neutral, actionLabel = "Undo", onAction = {})
    },
    CatalogEntry(
        name = "Tooltip",
        group = "Feedback",
        description = "A small label that explains an element.",
        keywords = "hint label",
        code = "JengaTooltip(\"Re-admit this attendee\")",
    ) {
        JengaTooltip("Re-admit this attendee")
    },
    CatalogEntry(
        name = "Dialog",
        group = "Feedback",
        description = "A modal that interrupts for a decision.",
        keywords = "alert modal confirm",
        code = """
            var open by remember { mutableStateOf(false) }
            JengaButton("Dialog", { open = true }, variant = JengaButtonVariant.Outline)
            if (open) {
                JengaDialog(
                    onDismissRequest = { open = false },
                    title = "Reset device?",
                    text = "This clears the cached gate session on this device.",
                    confirmButton = { JengaButton("Reset", { open = false }, variant = JengaButtonVariant.Danger) },
                    dismissButton = { JengaButton("Cancel", { open = false }, variant = JengaButtonVariant.Ghost) },
                )
            }
        """.trimIndent(),
    ) {
        var open by remember { mutableStateOf(false) }
        JengaButton("Dialog", { open = true }, variant = JengaButtonVariant.Outline)
        if (open) {
            JengaDialog(
                onDismissRequest = { open = false },
                title = "Reset device?",
                text = "This clears the cached gate session on this device.",
                confirmButton = { JengaButton("Reset", { open = false }, variant = JengaButtonVariant.Danger) },
                dismissButton = { JengaButton("Cancel", { open = false }, variant = JengaButtonVariant.Ghost) },
            )
        }
    },
    CatalogEntry(
        name = "Bottom sheet",
        group = "Feedback",
        description = "A panel that slides up from the bottom edge.",
        keywords = "sheet modal drawer",
        code = """
            var open by remember { mutableStateOf(false) }
            JengaButton("Bottom sheet", { open = true }, variant = JengaButtonVariant.Outline)
            if (open) {
                JengaBottomSheet(onDismissRequest = { open = false }) {
                    JengaStack(modifier = Modifier.padding(JengaTheme.spacing.xl)) {
                        JengaText("Select a gate", style = JengaTheme.typography.titleLarge)
                    }
                }
            }
        """.trimIndent(),
    ) {
        var open by remember { mutableStateOf(false) }
        JengaButton("Bottom sheet", { open = true }, variant = JengaButtonVariant.Outline)
        if (open) {
            JengaBottomSheet(onDismissRequest = { open = false }) {
                JengaStack(modifier = Modifier.fillMaxWidth().padding(JengaTheme.spacing.xl)) {
                    JengaText("Select a gate", style = JengaTheme.typography.titleLarge)
                    JengaText("Choose which gate you're scanning at.", color = JengaTheme.colors.textMuted)
                }
            }
        }
    },
    CatalogEntry(
        name = "Dropdown menu",
        group = "Feedback",
        description = "A list of actions anchored to a trigger.",
        keywords = "menu popup overflow",
        code = """
            var open by remember { mutableStateOf(false) }
            Box {
                JengaButton("Menu", { open = true }, variant = JengaButtonVariant.Outline)
                JengaDropdownMenu(expanded = open, onDismissRequest = { open = false }) {
                    JengaDropdownMenuItem("Re-enter", { open = false })
                    JengaDropdownMenuItem("View history", { open = false })
                }
            }
        """.trimIndent(),
    ) {
        var open by remember { mutableStateOf(false) }
        Box {
            JengaButton("Menu", { open = true }, variant = JengaButtonVariant.Outline)
            JengaDropdownMenu(expanded = open, onDismissRequest = { open = false }) {
                JengaDropdownMenuItem("Re-enter", { open = false })
                JengaDropdownMenuItem("View history", { open = false })
            }
        }
    },
    CatalogEntry(
        name = "Tabs",
        group = "Navigation",
        description = "Switches between views with an underline indicator.",
        keywords = "tab bar",
        code = """
            var tab by remember { mutableIntStateOf(0) }
            JengaTabs(tab, listOf("Upcoming", "Live", "Past"), { tab = it })
        """.trimIndent(),
    ) {
        var tab by remember { mutableIntStateOf(0) }
        JengaTabs(tab, listOf("Upcoming", "Live", "Past"), { tab = it })
    },
    CatalogEntry(
        name = "Navigation bar",
        group = "Navigation",
        description = "Top-level destinations along the bottom edge.",
        keywords = "bottom nav destinations",
        code = """
            JengaNavigationBar {
                JengaNavigationBarItem(true, {}, { JengaIcon(JengaIcons.Search, contentDescription = null) }, label = "Events")
                JengaNavigationBarItem(false, {}, { JengaIcon(JengaIcons.Check, contentDescription = null) }, label = "Scan")
            }
        """.trimIndent(),
    ) {
        JengaNavigationBar {
            JengaNavigationBarItem(true, {}, { JengaIcon(JengaIcons.Search, contentDescription = null) }, label = "Events")
            JengaNavigationBarItem(false, {}, { JengaIcon(JengaIcons.Check, contentDescription = null) }, label = "Scan")
            JengaNavigationBarItem(false, {}, { JengaIcon(JengaIcons.Info, contentDescription = null) }, label = "Stats")
        }
    },
    CatalogEntry(
        name = "Bottom bar",
        group = "Navigation",
        description = "A footer row of actions for the current screen.",
        keywords = "action bar footer",
        code = """
            JengaBottomBar {
                JengaIconButton(onClick = {}, variant = JengaIconButtonVariant.Standard) {
                    JengaIcon(JengaIcons.Search, contentDescription = "Search")
                }
                JengaButton("Manual entry", {}, modifier = Modifier.weight(1f))
            }
        """.trimIndent(),
    ) {
        JengaBottomBar {
            JengaIconButton(onClick = {}, variant = JengaIconButtonVariant.Standard) {
                JengaIcon(JengaIcons.Search, contentDescription = "Search")
            }
            JengaButton("Manual entry", {}, modifier = Modifier.weight(1f))
            JengaIconButton(onClick = {}, variant = JengaIconButtonVariant.Standard) {
                JengaIcon(JengaIcons.History, contentDescription = "History")
            }
        }
    },
    CatalogEntry(
        name = "Badge",
        group = "Data display",
        description = "A small status label in several tones.",
        keywords = "label status tone",
        code = """
            JengaBadge("Valid", tone = JengaBadgeTone.Success)
            JengaBadge("Pending", tone = JengaBadgeTone.Warning)
            JengaBadge("Denied", tone = JengaBadgeTone.Error)
        """.trimIndent(),
    ) {
        JengaWrap {
            JengaBadge("Valid", tone = JengaBadgeTone.Success)
            JengaBadge("Pending", tone = JengaBadgeTone.Warning)
            JengaBadge("Denied", tone = JengaBadgeTone.Error)
            JengaBadge("Info", tone = JengaBadgeTone.Info)
        }
    },
    CatalogEntry(
        name = "Icon tile",
        group = "Data display",
        description = "An icon on a tinted rounded square, in each tone.",
        keywords = "icon tile leading tone",
        code = """
            JengaIconTile(JengaIcons.Wrench, contentDescription = null)
            JengaIconTile(JengaIcons.Package, contentDescription = null, tone = JengaBadgeTone.Brand)
            JengaIconTile(JengaIcons.XCircle, contentDescription = null, tone = JengaBadgeTone.Error)
        """.trimIndent(),
    ) {
        JengaInline {
            JengaIconTile(JengaIcons.Wrench, contentDescription = null)
            JengaIconTile(JengaIcons.Package, contentDescription = null, tone = JengaBadgeTone.Brand)
            JengaIconTile(JengaIcons.CheckCircle, contentDescription = null, tone = JengaBadgeTone.Success)
            JengaIconTile(JengaIcons.XCircle, contentDescription = null, tone = JengaBadgeTone.Error)
        }
    },
    CatalogEntry(
        name = "Key value row",
        group = "Data display",
        description = "A label with an end-aligned value, for detail and summary sections.",
        keywords = "detail summary total label value",
        code = """
            JengaKeyValueRow("Serial number", "SN-0042-7781", valueStyle = JengaTheme.typography.mono)
            JengaKeyValueRow("Status", "Paid", valueTone = JengaBadgeTone.Success)
            JengaKeyValueRow("Total", "KES 12,450", emphasis = JengaKeyValueEmphasis.Total)
        """.trimIndent(),
    ) {
        JengaStack(space = JengaTheme.spacing.none) {
            JengaKeyValueRow("Serial number", "SN-0042-7781", valueStyle = JengaTheme.typography.mono)
            JengaKeyValueRow("Status", "Paid", valueTone = JengaBadgeTone.Success)
            JengaKeyValueRow("Total", "KES 12,450", emphasis = JengaKeyValueEmphasis.Total)
        }
    },
    CatalogEntry(
        name = "Avatar",
        group = "Data display",
        description = "Initials or an image, in three sizes.",
        keywords = "initials profile picture",
        code = """
            JengaAvatar("Joel Kanyi", size = JengaAvatarSize.Small)
            JengaAvatar("Ada Lovelace")
            JengaAvatar("Grace Hopper", size = JengaAvatarSize.Large)
        """.trimIndent(),
    ) {
        JengaInline {
            JengaAvatar("Joel Kanyi", size = JengaAvatarSize.Small)
            JengaAvatar("Ada Lovelace")
            JengaAvatar("Grace Hopper", size = JengaAvatarSize.Large)
        }
    },
    CatalogEntry(
        name = "Progress",
        group = "Data display",
        description = "Determinate and indeterminate, linear and circular.",
        keywords = "linear circular loading indeterminate",
        code = """
            JengaLinearProgress(progress = 0.6f)
            JengaLinearProgressIndeterminate()
            JengaCircularProgress(progress = 0.7f)
            JengaCircularProgressIndeterminate()
        """.trimIndent(),
    ) {
        JengaStack(space = JengaTheme.spacing.md) {
            JengaLinearProgress(progress = 0.6f)
            JengaLinearProgressIndeterminate()
            JengaInline {
                JengaCircularProgress(progress = 0.7f)
                JengaCircularProgressIndeterminate()
            }
        }
    },
    CatalogEntry(
        name = "Slider",
        group = "Data display",
        description = "Picks a value from a continuous range.",
        keywords = "range value drag",
        code = """
            var value by remember { mutableFloatStateOf(0.4f) }
            JengaSlider(value = value, onValueChange = { value = it })
        """.trimIndent(),
    ) {
        var value by remember { mutableFloatStateOf(0.4f) }
        JengaSlider(value = value, onValueChange = { value = it })
    },
    CatalogEntry(
        name = "Shimmer",
        group = "Data display",
        description = "A loading placeholder that animates.",
        keywords = "skeleton placeholder loading",
        code = "Box(Modifier.size(width = 160.dp, height = 16.dp).clip(JengaTheme.shapes.sm).jengaShimmer())",
    ) {
        Box(Modifier.size(width = 160.dp, height = 16.dp).clip(JengaTheme.shapes.sm).jengaShimmer())
    },
    CatalogEntry(
        name = "Grid",
        group = "Layout",
        description = "Arranges children into equal columns.",
        keywords = "columns arrange",
        code = """
            JengaGrid(columns = 3) {
                repeat(6) { JengaBox(padding = PaddingValues(JengaTheme.spacing.md)) { JengaText("${'$'}it") } }
            }
        """.trimIndent(),
    ) {
        JengaGrid(columns = 3) {
            repeat(6) { i ->
                JengaBox(
                    padding = PaddingValues(JengaTheme.spacing.md),
                    background = JengaTheme.colors.surfaceSunk,
                    shape = JengaTheme.shapes.md,
                    contentAlignment = androidx.compose.ui.Alignment.Center,
                ) { JengaText("${i + 1}") }
            }
        }
    },
    CatalogEntry(
        name = "Section header",
        group = "Patterns",
        description = "A titled header with an optional action.",
        keywords = "title subtitle action",
        code = """
            JengaSectionHeader(title = "Recent scans", subtitle = "Last 24 hours", actionLabel = "See all", onActionClick = {})
        """.trimIndent(),
    ) {
        JengaSectionHeader(title = "Recent scans", subtitle = "Last 24 hours", actionLabel = "See all", onActionClick = {})
    },
    CatalogEntry(
        name = "Stat card",
        group = "Patterns",
        description = "A metric with a trend indicator.",
        keywords = "metric trend",
        code = """
            JengaStatCard("Checked in", "1,284", trendLabel = "+12%", trendTone = JengaBadgeTone.Success)
        """.trimIndent(),
    ) {
        JengaInline {
            JengaStatCard("Checked in", "1,284", trendLabel = "+12%", trendTone = JengaBadgeTone.Success)
            JengaStatCard("Denied", "37", trendLabel = "+3", trendTone = JengaBadgeTone.Error)
        }
    },
    CatalogEntry(
        name = "Ticket row",
        group = "Patterns",
        description = "A named row with a status badge.",
        keywords = "list ticket status",
        code = """
            JengaTicketRow("Joel Kanyi", "VIP · TKT-2026-001", "Valid", JengaBadgeTone.Success)
        """.trimIndent(),
    ) {
        JengaStack(space = JengaTheme.spacing.sm) {
            JengaTicketRow("Joel Kanyi", "VIP · TKT-2026-001", "Valid", JengaBadgeTone.Success)
            JengaTicketRow("Ada Lovelace", "Regular · TKT-2026-114", "Used", JengaBadgeTone.Neutral)
        }
    },
    CatalogEntry(
        name = "Status pill",
        group = "Patterns",
        description = "A compact status with an optional spinner.",
        keywords = "sync offline loading",
        code = """
            JengaStatusPill("Synced", tone = JengaBadgeTone.Success)
            JengaStatusPill("3 pending", tone = JengaBadgeTone.Warning, loading = true)
        """.trimIndent(),
    ) {
        JengaInline {
            JengaStatusPill("Synced", tone = JengaBadgeTone.Success)
            JengaStatusPill("3 pending", tone = JengaBadgeTone.Warning, loading = true)
            JengaStatusPill("Offline", tone = JengaBadgeTone.Error)
        }
    },
    CatalogEntry(
        name = "Verdict bar",
        group = "Content blocks",
        description = "A headline amount with progress and sublines.",
        keywords = "amount budget progress",
        code = """
            JengaVerdictBar(
                amount = "KES 1,190",
                amountSuffix = "left",
                tone = JengaVerdictTone.Positive,
                label = "This month",
                progress = 0.62f,
                sublines = JengaVerdictSublines("KES 710 of 1,900 spent", "8 of 11 priced"),
                action = JengaAction("Change") {},
            )
        """.trimIndent(),
    ) {
        JengaVerdictBar(
            amount = "KES 1,190",
            amountSuffix = "left",
            tone = JengaVerdictTone.Positive,
            label = "This month",
            progress = 0.62f,
            sublines = JengaVerdictSublines("KES 710 of 1,900 spent", "8 of 11 priced"),
            action = JengaAction("Change") {},
        )
    },
    CatalogEntry(
        name = "Stat tile",
        group = "Content blocks",
        description = "A labelled metric with a unit and tone.",
        keywords = "metric value unit",
        code = """
            JengaStatTile(label = "Revenue", value = "1,284", unit = "KES", tone = JengaStatTone.Success)
        """.trimIndent(),
    ) {
        JengaInline {
            JengaStatTile(label = "Revenue", value = "1,284", unit = "KES", tone = JengaStatTone.Success)
            JengaStatTile(label = "Refunds", value = "37", tone = JengaStatTone.Error)
        }
    },
    CatalogEntry(
        name = "Stepper",
        group = "Content blocks",
        description = "Increments or decrements a bounded number.",
        keywords = "quantity increment decrement",
        code = """
            var value by remember { mutableIntStateOf(2) }
            JengaStepper(value = value, onValueChange = { value = it }, min = 1, max = 9)
        """.trimIndent(),
    ) {
        var value by remember { mutableIntStateOf(2) }
        JengaStepper(value = value, onValueChange = { value = it }, min = 1, max = 9)
    },
    CatalogEntry(
        name = "Dot strip",
        group = "Content blocks",
        description = "Progress or paging shown as filled dots.",
        keywords = "progress steps pager",
        code = "JengaDotStrip(filled = 3, total = 5, contentDescription = \"3 of 5 done\")",
    ) {
        JengaDotStrip(filled = 3, total = 5, contentDescription = "3 of 5 done")
    },
    CatalogEntry(
        name = "Media hero",
        group = "Content blocks",
        description = "A media header with a title and support line.",
        keywords = "header image title",
        code = "JengaMediaHero(title = \"Night Market\", support = \"Fri · 7pm · Riverside\")",
    ) {
        JengaMediaHero(title = "Night Market", support = "Fri · 7pm · Riverside")
    },
    CatalogEntry(
        name = "Expandable row",
        group = "Content blocks",
        description = "A card whose header reveals more content.",
        keywords = "accordion collapse disclosure",
        code = """
            var expanded by remember { mutableStateOf(false) }
            JengaExpandableRow(
                expanded = expanded,
                onExpandedChange = { expanded = it },
                header = { JengaText("Delivery details", modifier = Modifier.weight(1f)) },
            ) {
                JengaText("Ships in 2 to 3 business days.")
            }
        """.trimIndent(),
    ) {
        var expanded by remember { mutableStateOf(false) }
        JengaExpandableRow(
            expanded = expanded,
            onExpandedChange = { expanded = it },
            header = { JengaText("Delivery details", modifier = Modifier.weight(1f)) },
        ) {
            JengaText("Ships in 2 to 3 business days. Free returns within 30 days.")
        }
    },
    CatalogEntry(
        name = "Reaction bar",
        group = "Content blocks",
        description = "Positive and negative feedback actions.",
        keywords = "thumbs helpful feedback",
        code = """
            JengaReactionBar(
                onPositive = {},
                onNegative = {},
                positiveContentDescription = "Helpful",
                negativeContentDescription = "Not helpful",
            )
        """.trimIndent(),
    ) {
        JengaReactionBar(
            onPositive = {},
            onNegative = {},
            positiveContentDescription = "Helpful",
            negativeContentDescription = "Not helpful",
        )
    },
    CatalogEntry(
        name = "Swipe to dismiss",
        group = "Content blocks",
        description = "Swipe a row away to dismiss it.",
        keywords = "swipe delete gesture",
        code = """
            JengaSwipeToDismiss(onDismiss = {}) {
                JengaListItem(headline = "Swipe me away", supporting = "End to start to dismiss")
            }
        """.trimIndent(),
    ) {
        JengaSwipeToDismiss(onDismiss = {}) {
            JengaListItem(headline = "Swipe me away", supporting = "End to start to dismiss")
        }
    },
    CatalogEntry(
        name = "Image shelf",
        group = "Media",
        description = "A horizontal shelf of media cards.",
        keywords = "carousel horizontal cards",
        code = """
            JengaImageShelf(title = "Picked for tonight", action = JengaAction("See all") {}) {
                JengaShelfCard(title = "Grilled veg bowl", meta = "25 min · ~540 kcal")
                JengaShelfCard(title = "Miso ramen", meta = "30 min · ~610 kcal")
            }
        """.trimIndent(),
    ) {
        JengaImageShelf(title = "Picked for tonight", action = JengaAction("See all") {}) {
            JengaShelfCard(title = "Grilled veg bowl", meta = "25 min · ~540 kcal")
            JengaShelfCard(title = "Miso ramen", meta = "30 min · ~610 kcal")
            JengaShelfCard(title = "Poke bowl", meta = "15 min · ~480 kcal")
        }
    },
    CatalogEntry(
        name = "Scanner viewfinder",
        group = "Scanner",
        description = "A camera framing overlay with status.",
        keywords = "qr camera scan",
        code = """
            Box(Modifier.fillMaxWidth().height(220.dp).clip(JengaTheme.shapes.lg).background(Color.Black)) {
                JengaScannerViewfinder(status = JengaScannerStatus.Scanning)
            }
        """.trimIndent(),
    ) {
        JengaStack(space = JengaTheme.spacing.md) {
            Box(
                modifier = Modifier.fillMaxWidth().height(200.dp).clip(JengaTheme.shapes.lg).background(Color.Black),
            ) {
                JengaScannerViewfinder(status = JengaScannerStatus.Scanning)
            }
            Box(
                modifier = Modifier.fillMaxWidth().height(200.dp).clip(JengaTheme.shapes.lg).background(Color.Black),
            ) {
                JengaScanFeedback(status = JengaScannerStatus.Success)
            }
        }
    },
)
