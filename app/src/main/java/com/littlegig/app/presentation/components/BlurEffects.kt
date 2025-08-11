package com.littlegig.app.presentation.components

import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.*
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.littlegig.app.presentation.theme.*
import kotlin.math.*

@Composable
fun BlurredBackground(
    modifier: Modifier = Modifier,
    blurRadius: Dp = 20.dp,
    content: @Composable BoxScope.() -> Unit
) {
    Box(
        modifier = modifier
            .blur(blurRadius)
            .background(
                Brush.radialGradient(
                    colors = listOf(
                        LittleGigPrimary.copy(alpha = 0.1f),
                        LittleGigSecondary.copy(alpha = 0.1f),
                        Color.Transparent
                    )
                )
            )
    ) {
        content()
    }
}

@Composable
fun BlurGlassmorphicCard(
    modifier: Modifier = Modifier,
    cornerRadius: Dp = 20.dp,
    borderWidth: Dp = 1.dp,
    content: @Composable BoxScope.() -> Unit
) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(cornerRadius))
            .background(
                brush = Brush.linearGradient(
                    colors = listOf(
                        Color.White.copy(alpha = 0.1f),
                        Color.White.copy(alpha = 0.05f)
                    ),
                    start = Offset(0f, 0f),
                    end = Offset(Float.POSITIVE_INFINITY, Float.POSITIVE_INFINITY)
                )
            )
            .background(
                color = Color.Transparent,
                shape = RoundedCornerShape(cornerRadius)
            )
    ) {
        // Border effect
        Canvas(
            modifier = Modifier.fillMaxSize()
        ) {
            drawRoundRect(
                brush = Brush.linearGradient(
                    colors = listOf(
                        Color.White.copy(alpha = 0.2f),
                        Color.Transparent,
                        Color.White.copy(alpha = 0.1f)
                    )
                ),
                size = size,
                cornerRadius = CornerRadius(cornerRadius.toPx()),
                style = Stroke(width = borderWidth.toPx())
            )
        }
        
        content()
    }
}

@Composable
fun FloatingOrb(
    modifier: Modifier = Modifier,
    color: Color = LittleGigPrimary,
    size: Dp = 100.dp,
    animationDuration: Int = 3000
) {
    val infiniteTransition = rememberInfiniteTransition(label = "orb_animation")
    
    val offsetX by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 50f,
        animationSpec = infiniteRepeatable(
            animation = tween(animationDuration, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "orb_x"
    )
    
    val offsetY by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 30f,
        animationSpec = infiniteRepeatable(
            animation = tween(animationDuration + 500, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "orb_y"
    )
    
    val scale by infiniteTransition.animateFloat(
        initialValue = 0.8f,
        targetValue = 1.2f,
        animationSpec = infiniteRepeatable(
            animation = tween(animationDuration - 500, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "orb_scale"
    )
    
    Canvas(
        modifier = modifier
            .size(size)
            .offset(offsetX.dp, offsetY.dp)
    ) {
        val center = Offset(this.size.width / 2, this.size.height / 2)
        val radius = (minOf(this.size.width, this.size.height) / 2) * scale
        
        // Draw outer glow
        drawCircle(
            brush = Brush.radialGradient(
                colors = listOf(
                    color.copy(alpha = 0.3f),
                    color.copy(alpha = 0.1f),
                    Color.Transparent
                ),
                center = center,
                radius = radius * 1.5f
            ),
            center = center,
            radius = radius * 1.5f
        )
        
        // Draw main orb
        drawCircle(
            brush = Brush.radialGradient(
                colors = listOf(
                    color.copy(alpha = 0.6f),
                    color.copy(alpha = 0.3f),
                    Color.Transparent
                ),
                center = center,
                radius = radius
            ),
            center = center,
            radius = radius
        )
        
        // Draw highlight
        drawCircle(
            brush = Brush.radialGradient(
                colors = listOf(
                    Color.White.copy(alpha = 0.4f),
                    Color.Transparent
                ),
                center = Offset(center.x - radius * 0.3f, center.y - radius * 0.3f),
                radius = radius * 0.3f
            ),
            center = Offset(center.x - radius * 0.3f, center.y - radius * 0.3f),
            radius = radius * 0.3f
        )
    }
}

@Composable
fun LiquidWaveHeader(
    modifier: Modifier = Modifier,
    height: Dp = 200.dp,
    primaryColor: Color = LittleGigPrimary,
    secondaryColor: Color = LittleGigSecondary
) {
    val infiniteTransition = rememberInfiniteTransition(label = "wave_header")
    
    val waveOffset1 by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 2 * PI.toFloat(),
        animationSpec = infiniteRepeatable(
            animation = tween(4000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "wave1"
    )
    
    val waveOffset2 by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = -2 * PI.toFloat(),
        animationSpec = infiniteRepeatable(
            animation = tween(6000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "wave2"
    )
    
    Canvas(
        modifier = modifier
            .fillMaxWidth()
            .height(height)
    ) {
        val canvasWidth = this.size.width
        val canvasHeight = this.size.height
        
        // Draw background gradient
        drawRect(
            brush = Brush.verticalGradient(
                colors = listOf(
                    primaryColor.copy(alpha = 0.8f),
                    secondaryColor.copy(alpha = 0.6f),
                    Color.Transparent
                )
            ),
            size = androidx.compose.ui.geometry.Size(canvasWidth, canvasHeight)
        )
        
        // Draw first wave
        val path1 = Path()
        path1.moveTo(0f, canvasHeight * 0.7f)
        
        for (x in 0..canvasWidth.toInt()) {
            val normalizedX = x / canvasWidth
            val y = canvasHeight * 0.7f + 
                    canvasHeight * 0.1f * sin(normalizedX * 4 * PI + waveOffset1).toFloat()
            path1.lineTo(x.toFloat(), y)
        }
        
        path1.lineTo(canvasWidth, canvasHeight)
        path1.lineTo(0f, canvasHeight)
        path1.close()
        
        drawPath(
            path = path1,
            brush = Brush.verticalGradient(
                colors = listOf(
                    primaryColor.copy(alpha = 0.3f),
                    primaryColor.copy(alpha = 0.1f),
                    Color.Transparent
                )
            )
        )
        
        // Draw second wave
        val path2 = Path()
        path2.moveTo(0f, canvasHeight * 0.8f)
        
        for (x in 0..canvasWidth.toInt()) {
            val normalizedX = x / canvasWidth
            val y = canvasHeight * 0.8f + 
                    canvasHeight * 0.08f * sin(normalizedX * 3 * PI + waveOffset2).toFloat()
            path2.lineTo(x.toFloat(), y)
        }
        
        path2.lineTo(canvasWidth, canvasHeight)
        path2.lineTo(0f, canvasHeight)
        path2.close()
        
        drawPath(
            path = path2,
            brush = Brush.verticalGradient(
                colors = listOf(
                    secondaryColor.copy(alpha = 0.4f),
                    secondaryColor.copy(alpha = 0.2f),
                    Color.Transparent
                )
            )
        )
        
        // Add shimmer effect
        val shimmerPath = Path()
        shimmerPath.moveTo(0f, 0f)
        
        for (x in 0..canvasWidth.toInt()) {
            val normalizedX = x / canvasWidth
            val y = canvasHeight * 0.3f * sin(normalizedX * 2 * PI + waveOffset1 * 2).toFloat()
            shimmerPath.lineTo(x.toFloat(), y)
        }
        
        shimmerPath.lineTo(canvasWidth, 0f)
        shimmerPath.close()
        
        drawPath(
            path = shimmerPath,
            brush = Brush.verticalGradient(
                colors = listOf(
                    Color.White.copy(alpha = 0.2f),
                    Color.Transparent
                )
            )
        )
    }
}

@Composable
fun ParticleField(
    modifier: Modifier = Modifier,
    particleCount: Int = 20,
    colors: List<Color> = listOf(
        LittleGigPrimary.copy(alpha = 0.3f),
        LittleGigSecondary.copy(alpha = 0.3f),
        LittleGigAccent.copy(alpha = 0.3f)
    )
) {
    val infiniteTransition = rememberInfiniteTransition(label = "particles")
    val density = LocalDensity.current
    
    // Generate random particle data
    val particles = remember {
        (0 until particleCount).map {
            ParticleData(
                startX = (0..100).random(),
                startY = (0..100).random(),
                size = (2..8).random(),
                speed = (1..3).random(),
                color = colors.random()
            )
        }
    }
    
    val animationTime by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 100f,
        animationSpec = infiniteRepeatable(
            animation = tween(10000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "particle_time"
    )
    
    Canvas(
        modifier = modifier.fillMaxSize()
    ) {
        particles.forEach { particle ->
            val progress = (animationTime + particle.startY) % 100f / 100f
            val x = (particle.startX + animationTime * particle.speed) % 100f / 100f * size.width
            val y = progress * size.height
            val alpha = sin(progress * PI).toFloat() * 0.5f + 0.5f
            
            drawCircle(
                color = particle.color.copy(alpha = alpha),
                radius = with(density) { particle.size.dp.toPx() },
                center = Offset(x, y)
            )
        }
    }
}

private data class ParticleData(
    val startX: Int,
    val startY: Int,
    val size: Int,
    val speed: Int,
    val color: Color
)