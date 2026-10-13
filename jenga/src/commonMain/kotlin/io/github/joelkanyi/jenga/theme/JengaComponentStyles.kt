package io.github.joelkanyi.jenga.theme

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.staticCompositionLocalOf
import dev.drewhamilton.poko.Poko
import io.github.joelkanyi.jenga.component.avatar.JengaAvatarStyle
import io.github.joelkanyi.jenga.component.banner.JengaInfoBarStyle
import io.github.joelkanyi.jenga.component.button.JengaButtonStyle
import io.github.joelkanyi.jenga.component.button.JengaIconButtonStyle
import io.github.joelkanyi.jenga.component.card.JengaCardFooterStyle
import io.github.joelkanyi.jenga.component.card.JengaCardStyle
import io.github.joelkanyi.jenga.component.chip.JengaChipStyle
import io.github.joelkanyi.jenga.component.fab.JengaFabStyle
import io.github.joelkanyi.jenga.component.feedback.JengaDialogStyle
import io.github.joelkanyi.jenga.component.feedback.JengaListSheetStyle
import io.github.joelkanyi.jenga.component.feedback.JengaSnackbarStyle
import io.github.joelkanyi.jenga.component.icon.JengaIconTileStyle
import io.github.joelkanyi.jenga.component.link.JengaLinkStyle
import io.github.joelkanyi.jenga.component.list.JengaKeyValueRowStyle
import io.github.joelkanyi.jenga.component.list.JengaListItemStyle
import io.github.joelkanyi.jenga.component.list.JengaSelectionListItemStyle
import io.github.joelkanyi.jenga.component.menu.JengaDropdownMenuStyle
import io.github.joelkanyi.jenga.component.navigation.JengaNavigationRailItemStyle
import io.github.joelkanyi.jenga.component.progress.JengaCircularProgressStyle
import io.github.joelkanyi.jenga.component.progress.JengaProgressStyle
import io.github.joelkanyi.jenga.component.scaffold.JengaTopAppBarStyle
import io.github.joelkanyi.jenga.component.search.JengaSearchFieldStyle
import io.github.joelkanyi.jenga.component.shelf.JengaThumbnailStripStyle
import io.github.joelkanyi.jenga.component.state.JengaStateStyle
import io.github.joelkanyi.jenga.component.status.JengaStatusPillStyle
import io.github.joelkanyi.jenga.component.stepper.JengaStepperStyle
import io.github.joelkanyi.jenga.component.tabs.JengaSegmentedControlStyle
import io.github.joelkanyi.jenga.component.tabs.JengaTabsStyle
import io.github.joelkanyi.jenga.component.textfield.JengaTextFieldStyle
import io.github.joelkanyi.jenga.pattern.JengaSectionHeaderStyles

/**
 * Theme-wide component styles, passed to [JengaTheme] so every component below it takes the same
 * look without per-call arguments:
 *
 * ```
 * JengaTheme(
 *     components = JengaComponentStyles(
 *         button = JengaButtonStyle(medium = JengaButtonSizeStyle(minHeight = 50.dp)),
 *         textField = JengaTextFieldStyle(focusRingWidth = 0.dp),
 *     ),
 * ) { App() }
 * ```
 *
 * A `null` style, or a `null` value inside one, keeps Jenga's built-in default. An argument passed
 * to a component call always wins over its theme style.
 *
 * @property button styles every [io.github.joelkanyi.jenga.component.button.JengaButton].
 * @property iconButton styles every [io.github.joelkanyi.jenga.component.button.JengaIconButton].
 * @property textField styles every [io.github.joelkanyi.jenga.component.textfield.JengaTextField].
 * @property searchField styles every [io.github.joelkanyi.jenga.component.search.JengaSearchField];
 *   values it leaves `null` come from [textField].
 * @property searchTrigger styles every [io.github.joelkanyi.jenga.component.search.JengaSearchTrigger];
 *   values it leaves `null` come from [textField].
 * @property listItem styles every [io.github.joelkanyi.jenga.component.list.JengaListItem].
 * @property card styles every [io.github.joelkanyi.jenga.component.card.JengaCard].
 * @property chip styles every [io.github.joelkanyi.jenga.component.chip.JengaChip].
 * @property dialog styles every [io.github.joelkanyi.jenga.component.feedback.JengaDialog].
 * @property snackbar styles every [io.github.joelkanyi.jenga.component.feedback.JengaSnackbar].
 * @property sectionHeader styles every [io.github.joelkanyi.jenga.pattern.JengaSectionHeader].
 * @property progress styles every [io.github.joelkanyi.jenga.component.progress.JengaLinearProgress].
 * @property selectionListItem styles every [io.github.joelkanyi.jenga.component.list.JengaRadioListItem] and [io.github.joelkanyi.jenga.component.list.JengaCheckboxListItem].
 * @property keyValueRow styles every [io.github.joelkanyi.jenga.component.list.JengaKeyValueRow].
 * @property state styles every [io.github.joelkanyi.jenga.component.state.JengaEmptyState] and [io.github.joelkanyi.jenga.component.state.JengaErrorState].
 * @property iconTile styles every [io.github.joelkanyi.jenga.component.icon.JengaIconTile].
 * @property listSheet styles every [io.github.joelkanyi.jenga.component.feedback.JengaListSheet] and [io.github.joelkanyi.jenga.component.feedback.JengaSideSheet].
 * @property link styles every [io.github.joelkanyi.jenga.component.link.JengaLink].
 * @property segmentedControl styles every [io.github.joelkanyi.jenga.component.tabs.JengaSegmentedControl].
 * @property topAppBar styles every [io.github.joelkanyi.jenga.component.scaffold.JengaTopAppBar].
 * @property statusPill styles every [io.github.joelkanyi.jenga.component.status.JengaStatusPill].
 * @property avatar styles every [io.github.joelkanyi.jenga.component.avatar.JengaAvatar].
 * @property infoBar styles every [io.github.joelkanyi.jenga.component.banner.JengaInfoBar].
 * @property circularProgress styles every [io.github.joelkanyi.jenga.component.progress.JengaCircularProgressIndeterminate].
 * @property cardFooter styles every [io.github.joelkanyi.jenga.component.card.JengaCardFooter].
 * @property tabs styles every [io.github.joelkanyi.jenga.component.tabs.JengaTabs].
 * @property stepper styles every [io.github.joelkanyi.jenga.component.stepper.JengaStepper].
 * @property dropdownMenu styles every [io.github.joelkanyi.jenga.component.menu.JengaDropdownMenu].
 * @property fab styles every [io.github.joelkanyi.jenga.component.fab.JengaFab].
 * @property navigationRailItem styles every [io.github.joelkanyi.jenga.component.navigation.JengaNavigationRailItem].
 * @property thumbnailStrip styles every [io.github.joelkanyi.jenga.component.shelf.JengaThumbnailStrip].
 */
@Poko
@Immutable
public class JengaComponentStyles(
    public val button: JengaButtonStyle? = null,
    public val iconButton: JengaIconButtonStyle? = null,
    public val textField: JengaTextFieldStyle? = null,
    public val searchField: JengaSearchFieldStyle? = null,
    public val searchTrigger: JengaSearchFieldStyle? = null,
    public val listItem: JengaListItemStyle? = null,
    public val card: JengaCardStyle? = null,
    public val chip: JengaChipStyle? = null,
    public val dialog: JengaDialogStyle? = null,
    public val snackbar: JengaSnackbarStyle? = null,
    public val sectionHeader: JengaSectionHeaderStyles? = null,
    public val progress: JengaProgressStyle? = null,
    public val selectionListItem: JengaSelectionListItemStyle? = null,
    public val keyValueRow: JengaKeyValueRowStyle? = null,
    public val state: JengaStateStyle? = null,
    public val iconTile: JengaIconTileStyle? = null,
    public val listSheet: JengaListSheetStyle? = null,
    public val link: JengaLinkStyle? = null,
    public val segmentedControl: JengaSegmentedControlStyle? = null,
    public val topAppBar: JengaTopAppBarStyle? = null,
    public val statusPill: JengaStatusPillStyle? = null,
    public val avatar: JengaAvatarStyle? = null,
    public val infoBar: JengaInfoBarStyle? = null,
    public val circularProgress: JengaCircularProgressStyle? = null,
    public val cardFooter: JengaCardFooterStyle? = null,
    public val tabs: JengaTabsStyle? = null,
    public val stepper: JengaStepperStyle? = null,
    public val dropdownMenu: JengaDropdownMenuStyle? = null,
    public val fab: JengaFabStyle? = null,
    public val navigationRailItem: JengaNavigationRailItemStyle? = null,
    public val thumbnailStrip: JengaThumbnailStripStyle? = null,
) {
    public fun copy(
        button: JengaButtonStyle? = this.button,
        iconButton: JengaIconButtonStyle? = this.iconButton,
        textField: JengaTextFieldStyle? = this.textField,
        searchField: JengaSearchFieldStyle? = this.searchField,
        searchTrigger: JengaSearchFieldStyle? = this.searchTrigger,
        listItem: JengaListItemStyle? = this.listItem,
        card: JengaCardStyle? = this.card,
        chip: JengaChipStyle? = this.chip,
        dialog: JengaDialogStyle? = this.dialog,
        snackbar: JengaSnackbarStyle? = this.snackbar,
        sectionHeader: JengaSectionHeaderStyles? = this.sectionHeader,
        progress: JengaProgressStyle? = this.progress,
        selectionListItem: JengaSelectionListItemStyle? = this.selectionListItem,
        keyValueRow: JengaKeyValueRowStyle? = this.keyValueRow,
        state: JengaStateStyle? = this.state,
        iconTile: JengaIconTileStyle? = this.iconTile,
        listSheet: JengaListSheetStyle? = this.listSheet,
        link: JengaLinkStyle? = this.link,
        segmentedControl: JengaSegmentedControlStyle? = this.segmentedControl,
        topAppBar: JengaTopAppBarStyle? = this.topAppBar,
        statusPill: JengaStatusPillStyle? = this.statusPill,
        avatar: JengaAvatarStyle? = this.avatar,
        infoBar: JengaInfoBarStyle? = this.infoBar,
        circularProgress: JengaCircularProgressStyle? = this.circularProgress,
        cardFooter: JengaCardFooterStyle? = this.cardFooter,
        tabs: JengaTabsStyle? = this.tabs,
        stepper: JengaStepperStyle? = this.stepper,
        dropdownMenu: JengaDropdownMenuStyle? = this.dropdownMenu,
        fab: JengaFabStyle? = this.fab,
        navigationRailItem: JengaNavigationRailItemStyle? = this.navigationRailItem,
        thumbnailStrip: JengaThumbnailStripStyle? = this.thumbnailStrip,
    ): JengaComponentStyles = JengaComponentStyles(
        button,
        iconButton,
        textField,
        searchField,
        searchTrigger,
        listItem,
        card,
        chip,
        dialog,
        snackbar,
        sectionHeader,
        progress,
        selectionListItem,
        keyValueRow,
        state,
        iconTile,
        listSheet,
        link,
        segmentedControl,
        topAppBar,
        statusPill,
        avatar,
        infoBar,
        circularProgress,
        cardFooter,
        tabs,
        stepper,
        dropdownMenu,
        fab,
        navigationRailItem,
        thumbnailStrip,
    )
}

internal val LocalJengaComponentStyles = staticCompositionLocalOf { JengaComponentStyles() }
