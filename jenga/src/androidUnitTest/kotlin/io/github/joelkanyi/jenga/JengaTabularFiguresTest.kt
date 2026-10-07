package io.github.joelkanyi.jenga

import androidx.compose.ui.text.TextStyle
import io.github.joelkanyi.jenga.foundation.typography.withTabularFigures
import org.junit.Assert.assertEquals
import org.junit.Test

class JengaTabularFiguresTest {

    private fun features(existing: String?): String? = TextStyle(fontFeatureSettings = existing).withTabularFigures().fontFeatureSettings

    @Test
    fun addsTnumWhenNoneSet() {
        assertEquals("tnum", features(null))
        assertEquals("tnum", features(""))
    }

    @Test
    fun keepsOtherFeatures() {
        assertEquals("'liga' 0, tnum", features("'liga' 0"))
    }

    @Test
    fun doesNotDuplicateTnum() {
        assertEquals("tnum", features("tnum"))
        assertEquals("smcp, tnum", features("tnum, smcp"))
    }

    @Test
    fun replacesTnumTurnedOff() {
        assertEquals("tnum", features("'tnum' 0"))
        assertEquals("'liga' 0, tnum", features("\"tnum\" off, 'liga' 0"))
    }
}
