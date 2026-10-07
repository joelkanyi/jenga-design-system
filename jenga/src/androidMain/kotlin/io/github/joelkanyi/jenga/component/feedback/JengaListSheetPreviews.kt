package io.github.joelkanyi.jenga.component.feedback

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import io.github.joelkanyi.jenga.component.button.JengaButton
import io.github.joelkanyi.jenga.component.button.JengaButtonVariant
import io.github.joelkanyi.jenga.component.list.JengaRadioListItem
import io.github.joelkanyi.jenga.core.preview.JengaBlockPreviews
import io.github.joelkanyi.jenga.core.preview.RtlPreview
import io.github.joelkanyi.jenga.theme.JengaTheme

// ---- Previews --------------------------------------------------------------
// The modal window can't be captured statically, so the previews render the
// sheet's visual surface (handle + layout) inline, height-bounded like the sheet.

@JengaBlockPreviews
@Composable
internal fun JengaListSheetPreview() {
    JengaTheme { ListSheetSurfacePreview() }
}

@Preview(name = "RTL", showBackground = true)
@Composable
internal fun JengaListSheetRtlPreview() {
    JengaTheme { RtlPreview { ListSheetSurfacePreview() } }
}

@Composable
private fun ListSheetSurfacePreview() {
    val technicians = listOf("Amina Otieno", "Brian Mwangi", "Chebet Kiprono")
    Column(
        modifier = Modifier
            .background(JengaTheme.colors.background)
            .padding(JengaTheme.spacing.lg),
    ) {
        Column(
            modifier = Modifier
                .clip(JengaTheme.shapes.card)
                .background(JengaTheme.colors.surface)
                .heightIn(max = 480.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            JengaDragHandle()
            JengaListSheetLayout(
                title = "Assign technician",
                subtitle = "Ticket TKT-2041",
                footer = {
                    JengaButton("Cancel", {}, modifier = Modifier.weight(1f), variant = JengaButtonVariant.Outline)
                    JengaButton("Assign", {}, modifier = Modifier.weight(1f))
                },
            ) {
                items(technicians) { name ->
                    JengaRadioListItem(headline = name, selected = name == technicians.first(), onClick = {})
                }
            }
        }
    }
}
