package io.github.joelkanyi.jenga.component.camera

import androidx.compose.ui.geometry.Size
import androidx.compose.ui.test.assertIsNotEnabled
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.performClick
import io.github.joelkanyi.jenga.theme.JengaTheme
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class JengaCameraPartsTest {

    @get:Rule
    val compose = createComposeRule()

    @Test
    fun idCardFrameInPortraitIsSizedFromTheWidth() {
        val frame = captureFrameBounds(Size(400f, 800f), JengaCaptureShape.Rectangle(1.586f))
        assertEquals(336f, frame.width, 0.5f)
        assertEquals(336f / 1.586f, frame.height, 0.5f)
        assertEquals(32f, frame.left, 0.5f)
        assertEquals(360f, frame.center.y, 0.5f)
    }

    @Test
    fun idCardFrameInLandscapeShrinksToFitTheHeight() {
        val frame = captureFrameBounds(Size(800f, 400f), JengaCaptureShape.Rectangle(1.586f))
        assertEquals(248f, frame.height, 0.5f)
        assertEquals(248f * 1.586f, frame.width, 0.5f)
        assertEquals(400f, frame.center.x, 0.5f)
    }

    @Test
    fun tallPageFrameKeepsItsAspectRatio() {
        val frame = captureFrameBounds(Size(400f, 600f), JengaCaptureShape.Rectangle(0.714f))
        assertEquals(0.714f, frame.width / frame.height, 0.01f)
        assertEquals(372f, frame.height, 0.5f)
    }

    @Test
    fun ovalFrameIsTallerThanWide() {
        val frame = captureFrameBounds(Size(400f, 800f), JengaCaptureShape.Oval)
        assertEquals(252f, frame.width, 0.5f)
        assertEquals(252f / JengaCaptureFrameDefaults.OvalAspectRatio, frame.height, 0.5f)
    }

    @Test
    fun shutterButtonTakesThePhotoOnlyWhenEnabled() {
        var shots = 0
        compose.setContent {
            JengaTheme { JengaShutterButton(onClick = { shots++ }, contentDescription = "Take photo", enabled = true) }
        }
        compose.onNodeWithContentDescription("Take photo").performClick()
        assertEquals(1, shots)
    }

    @Test
    fun disabledShutterButtonIsNotEnabled() {
        compose.setContent {
            JengaTheme { JengaShutterButton(onClick = {}, contentDescription = "Take photo", enabled = false) }
        }
        compose.onNodeWithContentDescription("Take photo").assertIsNotEnabled()
    }
}
