package com.littlegig.app.presentation.tickets

import android.graphics.Bitmap
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.unit.dp
import com.google.zxing.BarcodeFormat
import com.google.zxing.MultiFormatWriter
import com.google.zxing.common.BitMatrix
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.tween
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.animateFloat
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.runtime.DisposableEffect
import androidx.compose.ui.platform.LocalContext
import android.app.Activity
import android.view.WindowManager

@Composable
fun TicketDetailsScreen(ticketId: String, ticketCode: String) {
    val context = LocalContext.current
    val activity = context as? Activity
    // Boost screen brightness while on QR screen
    DisposableEffect(Unit) {
        val original = activity?.window?.attributes?.screenBrightness ?: WindowManager.LayoutParams.BRIGHTNESS_OVERRIDE_NONE
        if (activity != null) {
            val lp = activity.window.attributes
            lp.screenBrightness = 1f
            activity.window.attributes = lp
        }
        onDispose {
            if (activity != null) {
                val lp = activity.window.attributes
                lp.screenBrightness = original
                activity.window.attributes = lp
            }
        }
    }
    val infinite = rememberInfiniteTransition(label = "qr_pulse")
    val scaleState = infinite.animateFloat(
        initialValue = 0.95f,
        targetValue = 1.05f,
        animationSpec = infiniteRepeatable(animation = tween(1200), repeatMode = RepeatMode.Reverse),
        label = "qr_scale"
    )
    val scale = scaleState.value
    Column(
        modifier = Modifier.fillMaxSize().padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(text = "Ticket #$ticketId", style = MaterialTheme.typography.titleLarge)
        val qr = generateQrBitmap(ticketCode)
        if (qr != null) {
            Image(bitmap = qr.asImageBitmap(), contentDescription = null, modifier = Modifier.size(240.dp).graphicsLayer(scaleX = scale, scaleY = scale))
        }
        Text(text = ticketCode, style = MaterialTheme.typography.bodyMedium)
    }
}

private fun generateQrBitmap(content: String, size: Int = 512): Bitmap? {
    return try {
        val bitMatrix: BitMatrix = MultiFormatWriter().encode(content, BarcodeFormat.QR_CODE, size, size)
        val width = bitMatrix.width
        val height = bitMatrix.height
        val pixels = IntArray(width * height)
        for (y in 0 until height) {
            val offset = y * width
            for (x in 0 until width) {
                pixels[offset + x] = if (bitMatrix.get(x, y)) 0xFF000000.toInt() else 0xFFFFFFFF.toInt()
            }
        }
        val bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
        bitmap.setPixels(pixels, 0, width, 0, 0, width, height)
        bitmap
    } catch (_: Exception) { null }
}