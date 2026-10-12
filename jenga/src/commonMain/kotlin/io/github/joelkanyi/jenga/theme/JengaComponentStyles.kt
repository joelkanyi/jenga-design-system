package io.github.joelkanyi.jenga.theme

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.staticCompositionLocalOf
import dev.drewhamilton.poko.Poko
import io.github.joelkanyi.jenga.component.button.JengaButtonStyle
import io.github.joelkanyi.jenga.component.button.JengaIconButtonStyle
import io.github.joelkanyi.jenga.component.card.JengaCardStyle
import io.github.joelkanyi.jenga.component.chip.JengaChipStyle
import io.github.joelkanyi.jenga.component.feedback.JengaDialogStyle
import io.github.joelkanyi.jenga.component.feedback.JengaSnackbarStyle
import io.github.joelkanyi.jenga.component.list.JengaListItemStyle
import io.github.joelkanyi.jenga.component.search.JengaSearchFieldStyle
import io.github.joelkanyi.jenga.component.textfield.JengaTextFieldStyle

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
    )
}

internal val LocalJengaComponentStyles = staticCompositionLocalOf { JengaComponentStyles() }
