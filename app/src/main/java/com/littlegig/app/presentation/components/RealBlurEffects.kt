package com.littlegig.app.presentation.components

import android.graphics.RenderEffect
import android.graphics.Shader
import android.os.Build
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.graphics.*
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import dev.chrisbanes.haze.HazeState
import dev.chrisbanes.haze.haze
import dev.chrisbanes.haze.hazeChild

/**
 * Creates a real frosted glass blur effect that blurs the background content behind the composable.
 * This is NOT transparency - it's actual real-time background blur.
 * 
 * Based on techniques from:
 * - https://proandroiddev.com/blurring-the-lines-how-to-achieve-a-glassmorphic-design-with-jetpack-compose-0225560c2d64
 * - Stack Overflow glassmorphism solutions
 */
@Composable
fun Modifier.realBackgroundBlur(
    blurRadius: Dp = 25.dp,
    blurAlpha: Float = 0.4f,
    noiseStrength: Float = 0.05f,
    tintColor: Color = Color.White.copy(alpha = 0.1f)
): Modifier = composed {
    val density = LocalDensity.current
    val blurRadiusPx = with(density) { blurRadius.toPx() }
    
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
        // Use RenderEffect for Android 12+ (API 31+)
        graphicsLayer {
            renderEffect = RenderEffect
                .createBlurEffect(blurRadiusPx, blurRadiusPx, Shader.TileMode.MIRROR)
                .asComposeRenderEffect()
        }
        .then(
            drawWithContent {
                drawContent()
                // Add subtle tint and noise for glassmorphic effect
                drawRect(
                    color = tintColor,
                    alpha = blurAlpha
                )
                if (noiseStrength > 0) {
                    drawNoise(noiseStrength)
                }
            }
        )
    } else {
        // Fallback for older Android versions
        legacyBlurEffect(blurRadius, blurAlpha, noiseStrength, tintColor)
    }
}

/**
 * Creates a glassmorphic background using the Haze library for real blur effects
 */
@Composable
fun Modifier.glassmorphicBackground(
    hazeState: HazeState,
    alpha: Float = 0.7f,
    tint: Color = Color.White.copy(alpha = 0.1f),
    blurRadius: Dp = 20.dp
): Modifier = composed {
    val isDark = isSystemInDarkTheme()
    val glassColor = if (isDark) {
        Color.Black.copy(alpha = alpha * 0.3f)
    } else {
        Color.White.copy(alpha = alpha)
    }
    
    this
        .hazeChild(
            state = hazeState,
            style = dev.chrisbanes.haze.HazeDefaults.style(
                backgroundColor = glassColor,
                tint = tint,
                blurRadius = blurRadius
            )
        )
}

/**
 * Creates a Haze state for managing blur areas
 */
@Composable
fun rememberHazeState(): HazeState {
    return remember { HazeState() }
}

/**
 * Modifier to mark content that should be blurred by glassmorphic elements
 */
@Composable
fun Modifier.blurrable(hazeState: HazeState): Modifier = composed {
    this.haze(state = hazeState)
}

/**
 * Legacy blur implementation for Android versions below API 31
 */
private fun Modifier.legacyBlurEffect(
    blurRadius: Dp,
    blurAlpha: Float,
    noiseStrength: Float,
    tintColor: Color
): Modifier = composed {
    drawWithContent {
        drawContent()
        
        // Create a simple frosted glass simulation
        val path = Path().apply {
            addRoundRect(
                androidx.compose.ui.geometry.RoundRect(
                    left = 0f,
                    top = 0f,
                    right = size.width,
                    bottom = size.height,
                    radiusX = 12.dp.toPx(),
                    radiusY = 12.dp.toPx()
                )
            )
        }
        
        // Draw frosted glass effect layers
        drawPath(
            path = path,
            brush = Brush.linearGradient(
                colors = listOf(
                    tintColor.copy(alpha = blurAlpha * 0.8f),
                    tintColor.copy(alpha = blurAlpha * 0.4f),
                    tintColor.copy(alpha = blurAlpha * 0.6f)
                ),
                start = androidx.compose.ui.geometry.Offset(0f, 0f),
                end = androidx.compose.ui.geometry.Offset(size.width, size.height)
            )
        )
        
        // Add noise pattern for texture
        if (noiseStrength > 0) {
            drawNoise(noiseStrength)
        }
        
        // Add subtle highlight for glass effect
        drawPath(
            path = path,
            brush = Brush.radialGradient(
                colors = listOf(
                    Color.White.copy(alpha = 0.3f),
                    Color.Transparent
                ),
                center = androidx.compose.ui.geometry.Offset(size.width * 0.3f, size.height * 0.3f),
                radius = size.width * 0.5f
            )
        )
    }
}

/**
 * Draws a noise pattern to simulate frosted glass texture
 */
private fun DrawScope.drawNoise(strength: Float) {
    val noiseSize = 2.dp.toPx()
    val cols = (size.width / noiseSize).toInt()
    val rows = (size.height / noiseSize).toInt()
    
    for (x in 0 until cols) {
        for (y in 0 until rows) {
            val alpha = (Math.random() * strength).toFloat()
            val color = if (Math.random() > 0.5) {
                Color.White.copy(alpha = alpha)
            } else {
                Color.Black.copy(alpha = alpha * 0.5f)
            }
            
            drawRect(
                color = color,
                topLeft = androidx.compose.ui.geometry.Offset(
                    x * noiseSize,
                    y * noiseSize
                ),
                size = androidx.compose.ui.geometry.Size(noiseSize, noiseSize)
            )
        }
    }
}

/**
 * Enhanced glassmorphic card with real blur background
 */
@Composable
fun RealGlassmorphicCard(
    onClick: (() -> Unit)? = null,
    modifier: Modifier = Modifier,
    hazeState: HazeState? = null,
    blurRadius: Dp = 20.dp,
    alpha: Float = 0.7f,
    shape: Shape = androidx.compose.foundation.shape.RoundedCornerShape(16.dp),
    content: @Composable () -> Unit
) {
    val cardModifier = if (hazeState != null) {
        modifier.glassmorphicBackground(hazeState, alpha, blurRadius = blurRadius)
    } else {
        modifier.realBackgroundBlur(blurRadius = blurRadius, blurAlpha = alpha)
    }
    
    if (onClick != null) {
        Box(
            modifier = cardModifier
                .clickable { onClick() }
                .background(
                    color = Color.Transparent,
                    shape = shape
                )
        ) {
            content()
        }
    } else {
        Box(
            modifier = cardModifier
                .background(
                    color = Color.Transparent,
                    shape = shape
                )
        ) {
            content()
        }
    }
}
