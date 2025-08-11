package com.littlegig.app.presentation.components

import androidx.compose.animation.core.*
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.*
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.*
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.littlegig.app.presentation.theme.*
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.sin

// Neumorphic Button with liquid glass effect
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NeumorphicButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    pressed: Boolean = false,
    glowEffect: Boolean = true,
    content: @Composable RowScope.() -> Unit
) {
    val isDark = isSystemInDarkTheme()
    val density = LocalDensity.current
    
    val animatedPressed by animateFloatAsState(
        targetValue = if (pressed) 1f else 0f,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessLow
        ),
        label = "pressed_animation"
    )
    
    val glowAlpha by animateFloatAsState(
        targetValue = if (glowEffect && enabled) 0.6f else 0f,
        animationSpec = tween(1000),
        label = "glow_animation"
    )
    
    Box(
        modifier = modifier
            .drawBehind {
                if (glowEffect && enabled) {
                    drawGlowEffect(
                        color = LittleGigPrimary,
                        alpha = glowAlpha * 0.3f,
                        blurRadius = 20.dp.toPx()
                    )
                }
            }
    ) {
        Surface(
            onClick = onClick,
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp)
                .drawBehind {
                    drawNeumorphicShadow(
                        isDark = isDark,
                        pressed = animatedPressed > 0.5f,
                        cornerRadius = 28.dp.toPx()
                    )
                },
            enabled = enabled,
            shape = RoundedCornerShape(28.dp),
            color = if (isDark) DarkSurface else LightSurface,
            border = BorderStroke(
                width = 1.dp,
                brush = Brush.linearGradient(
                    colors = listOf(
                        if (isDark) GlassmorphicDark else GlassmorphicLight,
                        Color.Transparent
                    )
                )
            )
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        Brush.linearGradient(
                            colors = listOf(
                                LittleGigPrimary.copy(alpha = 0.1f),
                                LittleGigPrimary.copy(alpha = 0.05f)
                            ),
                            start = Offset(0f, 0f),
                            end = Offset(Float.POSITIVE_INFINITY, Float.POSITIVE_INFINITY)
                        )
                    ),
                contentAlignment = Alignment.Center
            ) {
                Row(
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically,
                    content = content
                )
            }
        }
    }
}

// Liquid Glass Card
@Composable
fun LiquidGlassCard(
    modifier: Modifier = Modifier,
    onClick: (() -> Unit)? = null,
    glowEffect: Boolean = false,
    content: @Composable BoxScope.() -> Unit
) {
    val isDark = isSystemInDarkTheme()
    val infiniteTransition = rememberInfiniteTransition(label = "liquid_animation")
    
    val shimmerOffset by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(2000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "shimmer_offset"
    )
    
    val glowPulse by infiniteTransition.animateFloat(
        initialValue = 0.3f,
        targetValue = 0.8f,
        animationSpec = infiniteRepeatable(
            animation = tween(2000, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "glow_pulse"
    )
    
    Box(
        modifier = modifier
            .drawBehind {
                if (glowEffect) {
                    drawGlowEffect(
                        color = LittleGigSecondary,
                        alpha = glowPulse * 0.4f,
                        blurRadius = 30.dp.toPx()
                    )
                }
            }
    ) {
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .drawBehind {
                    drawNeumorphicShadow(
                        isDark = isDark,
                        pressed = false,
                        cornerRadius = 24.dp.toPx()
                    )
                }
                .clip(RoundedCornerShape(24.dp))
                .then(
                    if (onClick != null) {
                        Modifier.clickable(
                            interactionSource = remember { MutableInteractionSource() },
                            indication = rememberRipple(color = LittleGigPrimary.copy(alpha = 0.1f))
                        ) { onClick() }
                    } else Modifier
                ),
            shape = RoundedCornerShape(24.dp),
            color = if (isDark) DarkSurface else LightSurface,
            border = BorderStroke(
                width = 1.dp,
                brush = Brush.linearGradient(
                    colors = listOf(
                        if (isDark) GlassmorphicDark else GlassmorphicLight,
                        Color.Transparent,
                        if (isDark) GlassmorphicDark else GlassmorphicLight
                    )
                )
            )
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        Brush.linearGradient(
                            colors = listOf(
                                if (isDark) GlassmorphicDark else GlassmorphicLight,
                                Color.Transparent,
                                if (isDark) GlassmorphicDark else GlassmorphicLight
                            ),
                            start = Offset(shimmerOffset * 100f, 0f),
                            end = Offset((shimmerOffset + 0.3f) * 100f, 100f)
                        )
                    )
            ) {
                content()
            }
        }
    }
}

// Neumorphic Circle Button (for floating action buttons)
@Composable
fun NeumorphicCircleButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    size: Dp = 56.dp,
    glowEffect: Boolean = true,
    content: @Composable BoxScope.() -> Unit
) {
    val isDark = isSystemInDarkTheme()
    var pressed by remember { mutableStateOf(false) }
    
    val animatedPressed by animateFloatAsState(
        targetValue = if (pressed) 1f else 0f,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessHigh
        ),
        label = "circle_pressed"
    )
    
    val glowAlpha by animateFloatAsState(
        targetValue = if (glowEffect) 0.8f else 0f,
        animationSpec = tween(1000),
        label = "circle_glow"
    )
    
    Box(
        modifier = modifier
            .size(size)
            .drawBehind {
                if (glowEffect) {
                    drawGlowEffect(
                        color = LittleGigAccent,
                        alpha = glowAlpha * 0.5f,
                        blurRadius = 25.dp.toPx()
                    )
                }
            }
    ) {
        Surface(
            onClick = onClick,
            modifier = Modifier
                .size(size)
                                    .drawBehind {
                drawNeumorphicShadow(
                    isDark = isDark,
                    pressed = animatedPressed > 0.5f,
                    cornerRadius = size.toPx() / 2
                )
            },
            shape = CircleShape,
            color = if (isDark) DarkSurface else LightSurface,
            border = BorderStroke(
                width = 1.dp,
                brush = Brush.radialGradient(
                    colors = listOf(
                        if (isDark) GlassmorphicDark else GlassmorphicLight,
                        Color.Transparent
                    )
                )
            )
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        Brush.radialGradient(
                            colors = listOf(
                                LittleGigAccent.copy(alpha = 0.1f),
                                Color.Transparent
                            )
                        )
                    ),
                contentAlignment = Alignment.Center
            ) {
                content()
            }
        }
    }
}

// Frosted Glass Bottom Bar
@Composable
fun FrostedGlassBottomBar(
    modifier: Modifier = Modifier,
    content: @Composable RowScope.() -> Unit
) {
    val isDark = isSystemInDarkTheme()
    
    Surface(
        modifier = modifier
            .fillMaxWidth()
            .height(80.dp)
            .drawBehind {
                // Draw frosted glass effect
                drawRect(
                    brush = Brush.verticalGradient(
                        colors = listOf(
                            Color.Transparent,
                            if (isDark) DarkSurface.copy(alpha = 0.95f) else LightSurface.copy(alpha = 0.95f)
                        )
                    )
                )
            },
        color = Color.Transparent,
        border = BorderStroke(
            width = 1.dp,
            brush = Brush.horizontalGradient(
                colors = listOf(
                    Color.Transparent,
                    if (isDark) GlassmorphicDark else GlassmorphicLight,
                    Color.Transparent
                )
            )
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp, vertical = 8.dp),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically,
            content = content
        )
    }
}

// Extension function to draw neumorphic shadows
private fun DrawScope.drawNeumorphicShadow(
    isDark: Boolean,
    pressed: Boolean,
    cornerRadius: Float
) {
    val shadowOffset = if (pressed) 2.dp.toPx() else 8.dp.toPx()
    val shadowBlur = if (pressed) 4.dp.toPx() else 16.dp.toPx()
    
    val lightShadow = if (isDark) Color.Black.copy(alpha = 0.3f) else Color.White.copy(alpha = 0.8f)
    val darkShadow = if (isDark) Color.Black.copy(alpha = 0.3f) else Color.Black.copy(alpha = 0.1f)
    
    // Draw dark shadow (bottom-right)
    drawRoundRect(
        color = darkShadow,
        topLeft = Offset(shadowOffset, shadowOffset),
        size = size,
        cornerRadius = CornerRadius(cornerRadius)
    )
    
    // Draw light shadow (top-left)
    drawRoundRect(
        color = lightShadow,
        topLeft = Offset(-shadowOffset, -shadowOffset),
        size = size,
        cornerRadius = CornerRadius(cornerRadius)
    )
}

// Extension function to draw glow effects
private fun DrawScope.drawGlowEffect(
    color: Color,
    alpha: Float,
    blurRadius: Float
) {
    val center = Offset(size.width / 2, size.height / 2)
    val radius = minOf(size.width, size.height) / 2 + blurRadius
    
    drawCircle(
        brush = Brush.radialGradient(
            colors = listOf(
                color.copy(alpha = alpha),
                color.copy(alpha = alpha * 0.5f),
                Color.Transparent
            ),
            center = center,
            radius = radius
        ),
        center = center,
        radius = radius
    )
}

// Animated Wave Background
@Composable
fun AnimatedWaveBackground(
    modifier: Modifier = Modifier,
    colors: List<Color> = listOf(
        LittleGigPrimary.copy(alpha = 0.1f),
        LittleGigSecondary.copy(alpha = 0.1f),
        LittleGigAccent.copy(alpha = 0.1f)
    )
) {
    val infiniteTransition = rememberInfiniteTransition(label = "wave_animation")
    
    val waveOffset by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 2 * PI.toFloat(),
        animationSpec = infiniteRepeatable(
            animation = tween(4000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "wave_offset"
    )
    
    Canvas(
        modifier = modifier.fillMaxSize()
    ) {
        val canvasWidth = size.width
        val canvasHeight = size.height
        val waveHeight = canvasHeight * 0.1f
        
        colors.forEachIndexed { index, color ->
            val path = androidx.compose.ui.graphics.Path()
            val amplitude = waveHeight * (1f - index * 0.3f)
            val frequency = 0.005f * (index + 1)
            val phaseShift = waveOffset + (index * PI.toFloat() / 3)
            
            path.moveTo(0f, canvasHeight / 2)
            
            for (x in 0..canvasWidth.toInt()) {
                val y = canvasHeight / 2 + amplitude * sin(x * frequency + phaseShift).toFloat()
                path.lineTo(x.toFloat(), y)
            }
            
            path.lineTo(canvasWidth, canvasHeight)
            path.lineTo(0f, canvasHeight)
            path.close()
            
            drawPath(
                path = path,
                brush = Brush.verticalGradient(
                    colors = listOf(
                        color,
                        Color.Transparent
                    )
                )
            )
        }
    }
}