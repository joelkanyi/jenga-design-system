package io.github.joelkanyi.jenga.component.camera

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import io.github.joelkanyi.jenga.core.preview.JengaBlockPreviews
import io.github.joelkanyi.jenga.theme.JengaTheme

@Preview(name = "Selfie", widthDp = 360, heightDp = 640)
@Composable
internal fun JengaCaptureFrameOvalPreview() {
    CameraBackdrop { JengaCaptureFrame(shape = JengaCaptureShape.Oval, modifier = Modifier.fillMaxSize(), holdProgress = 0.6f) }
}

@Preview(name = "ID card ready", widthDp = 360, heightDp = 640)
@Composable
internal fun JengaCaptureFrameCardPreview() {
    CameraBackdrop {
        JengaCaptureFrame(shape = JengaCaptureShape.Rectangle(1.586f), modifier = Modifier.fillMaxSize(), ready = true)
    }
}

@Preview(name = "ID book page", widthDp = 360, heightDp = 640)
@Composable
internal fun JengaCaptureFrameBookPagePreview() {
    CameraBackdrop { JengaCaptureFrame(shape = JengaCaptureShape.Rectangle(0.714f), modifier = Modifier.fillMaxSize()) }
}

@Preview(name = "Passport landscape", widthDp = 640, heightDp = 360)
@Composable
internal fun JengaCaptureFramePassportLandscapePreview() {
    CameraBackdrop { JengaCaptureFrame(shape = JengaCaptureShape.Rectangle(1.42f), modifier = Modifier.fillMaxSize()) }
}

@JengaBlockPreviews
@Composable
internal fun JengaShutterButtonPreview() {
    JengaTheme {
        Row(
            modifier = Modifier.background(Color.Black).padding(JengaTheme.spacing.lg),
            horizontalArrangement = Arrangement.spacedBy(JengaTheme.spacing.lg),
        ) {
            JengaShutterButton(onClick = {}, contentDescription = "Take photo")
            JengaShutterButton(onClick = {}, contentDescription = "Take photo", enabled = false)
        }
    }
}

@Composable
private fun CameraBackdrop(content: @Composable () -> Unit) {
    JengaTheme {
        Box(Modifier.fillMaxSize().background(Color(0xFF6B6F76))) {
            content()
        }
    }
}
