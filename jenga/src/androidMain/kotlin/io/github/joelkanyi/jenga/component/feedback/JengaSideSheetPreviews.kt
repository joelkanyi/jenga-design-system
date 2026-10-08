package io.github.joelkanyi.jenga.component.feedback

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import io.github.joelkanyi.jenga.component.button.JengaButton
import io.github.joelkanyi.jenga.component.list.JengaRadioListItem
import io.github.joelkanyi.jenga.core.preview.JengaBlockPreviews
import io.github.joelkanyi.jenga.core.preview.RtlPreview
import io.github.joelkanyi.jenga.theme.JengaTheme

// ---- Previews --------------------------------------------------------------

@JengaBlockPreviews
@Composable
internal fun JengaSideSheetPreview() {
    JengaTheme { JengaSideSheetShowcase() }
}

@Preview(name = "RTL", showBackground = true)
@Composable
internal fun JengaSideSheetRtlPreview() {
    JengaTheme { RtlPreview { JengaSideSheetShowcase() } }
}

@Composable
private fun JengaSideSheetShowcase() {
    Box(
        modifier = Modifier.width(480.dp).height(360.dp).background(JengaTheme.colors.scrim),
        contentAlignment = Alignment.CenterEnd,
    ) {
        JengaListSheetLayout(
            title = "Assign technician",
            subtitle = AnnotatedString("Ticket TKT-2041"),
            footer = { JengaButton("Assign", {}, modifier = Modifier.weight(1f)) },
            modifier = Modifier
                .width(360.dp)
                .fillMaxHeight()
                .clip(JengaSideSheetDefaults.shape)
                .background(JengaTheme.colors.surface),
            headerPadding = JengaSideSheetDefaults.headerPadding,
            fillHeight = true,
        ) {
            items(listOf("Amina Otieno", "Brian Mwangi")) { name ->
                JengaRadioListItem(headline = name, selected = name == "Amina Otieno", onClick = {})
            }
        }
    }
}
