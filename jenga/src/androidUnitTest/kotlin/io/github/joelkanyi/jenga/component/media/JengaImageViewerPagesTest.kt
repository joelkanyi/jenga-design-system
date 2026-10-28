package io.github.joelkanyi.jenga.component.media

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTouchInput
import androidx.compose.ui.test.swipeLeft
import androidx.compose.ui.unit.IntSize
import io.github.joelkanyi.jenga.component.button.JengaButton
import io.github.joelkanyi.jenga.theme.JengaTheme
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class JengaImageViewerPagesTest {

    @get:Rule
    val compose = createComposeRule()

    private fun showViewer() {
        compose.setContent {
            JengaTheme {
                JengaImageViewer(
                    pageCount = 3,
                    onDismissRequest = {},
                    title = { page -> "Photo ${page + 1} of 3" },
                    closeContentDescription = "Close",
                    bottomBar = { _, goToPage -> JengaButton(text = "Last", onClick = { goToPage(2) }) },
                ) { page ->
                    Box(Modifier.fillMaxSize().testTag("page$page"))
                }
            }
        }
    }

    @Test
    fun swipingAnUnzoomedPageMovesToTheNextPage() {
        showViewer()
        compose.onNodeWithText("Photo 1 of 3").assertExists()
        compose.onNodeWithTag("page0").performTouchInput { swipeLeft() }
        compose.waitForIdle()
        compose.onNodeWithText("Photo 2 of 3").assertExists()
    }

    @Test
    fun theBottomBarCanMoveToAnyPage() {
        showViewer()
        compose.onNodeWithText("Last").performClick()
        compose.waitForIdle()
        compose.onNodeWithText("Photo 3 of 3").assertExists()
    }
}

class ZoomStateTest {

    @Test
    fun panIsClampedToTheZoomedImage() {
        val zoom = ZoomState(maxScale = 4f)
        zoom.size = IntSize(100, 200)
        zoom.transform(zoomChange = 2f, panChange = Offset(500f, -500f))
        assertEquals(2f, zoom.scale)
        assertEquals(Offset(50f, -100f), zoom.offset)
    }

    @Test
    fun doubleTapZoomsInThenBackOut() {
        val zoom = ZoomState(maxScale = 4f)
        zoom.toggle()
        assertEquals(JengaImageViewerDefaults.DoubleTapScale, zoom.scale)
        zoom.toggle()
        assertEquals(1f, zoom.scale)
        assertEquals(Offset.Zero, zoom.offset)
    }
}
