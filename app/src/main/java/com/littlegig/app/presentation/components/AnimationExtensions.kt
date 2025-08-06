package com.littlegig.app.presentation.components

import androidx.compose.animation.core.*
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.offset
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.debugInspectorInfo
import androidx.compose.ui.unit.IntOffset
import kotlinx.coroutines.delay
import kotlin.math.*

/**
 * Adds a smooth bounce animation when the component is tapped
 */
fun Modifier.bounceClick(
    scaleDown: Float = 0.95f
) = composed(
    inspectorInfo = debugInspectorInfo {
        name = "bounceClick"
        properties["scaleDown"] = scaleDown
    }
) {
    var isPressed by remember { mutableStateOf(false) }
    
    val animatedScale by animateFloatAsState(
        targetValue = if (isPressed) scaleDown else 1f,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessHigh
        ),
        label = "bounce_scale"
    )
    
    this
        .scale(animatedScale)
        .pointerInput(Unit) {
            detectTapGestures(
                onPress = {
                    isPressed = true
                    tryAwaitRelease()
                    isPressed = false
                }
            )
        }
}

/**
 * Adds a floating animation effect
 */
fun Modifier.floatingEffect(
    strength: Float = 5f,
    duration: Int = 3000
) = composed(
    inspectorInfo = debugInspectorInfo {
        name = "floatingEffect"
        properties["strength"] = strength
        properties["duration"] = duration
    }
) {
    val infiniteTransition = rememberInfiniteTransition(label = "floating")
    
    val offsetY by infiniteTransition.animateFloat(
        initialValue = -strength,
        targetValue = strength,
        animationSpec = infiniteRepeatable(
            animation = tween(duration, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "float_y"
    )
    
    val offsetX by infiniteTransition.animateFloat(
        initialValue = -strength * 0.5f,
        targetValue = strength * 0.5f,
        animationSpec = infiniteRepeatable(
            animation = tween(duration + 500, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "float_x"
    )
    
    this.offset { IntOffset(offsetX.roundToInt(), offsetY.roundToInt()) }
}

/**
 * Adds a subtle rotation animation
 */
fun Modifier.gentleRotation(
    degrees: Float = 2f,
    duration: Int = 4000
) = composed(
    inspectorInfo = debugInspectorInfo {
        name = "gentleRotation"
        properties["degrees"] = degrees
        properties["duration"] = duration
    }
) {
    val infiniteTransition = rememberInfiniteTransition(label = "rotation")
    
    val rotation by infiniteTransition.animateFloat(
        initialValue = -degrees,
        targetValue = degrees,
        animationSpec = infiniteRepeatable(
            animation = tween(duration, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "gentle_rotation"
    )
    
    this.graphicsLayer {
        rotationZ = rotation
    }
}

/**
 * Adds a breathing/pulsing animation
 */
fun Modifier.breathingEffect(
    minScale: Float = 0.98f,
    maxScale: Float = 1.02f,
    duration: Int = 2000
) = composed(
    inspectorInfo = debugInspectorInfo {
        name = "breathingEffect"
        properties["minScale"] = minScale
        properties["maxScale"] = maxScale
        properties["duration"] = duration
    }
) {
    val infiniteTransition = rememberInfiniteTransition(label = "breathing")
    
    val scale by infiniteTransition.animateFloat(
        initialValue = minScale,
        targetValue = maxScale,
        animationSpec = infiniteRepeatable(
            animation = tween(duration, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "breathing_scale"
    )
    
    this.scale(scale)
}

/**
 * Adds a shimmer loading effect
 */
fun Modifier.shimmerEffect(
    duration: Int = 1500
) = composed(
    inspectorInfo = debugInspectorInfo {
        name = "shimmerEffect"
        properties["duration"] = duration
    }
) {
    val infiniteTransition = rememberInfiniteTransition(label = "shimmer")
    
    val alpha by infiniteTransition.animateFloat(
        initialValue = 0.4f,
        targetValue = 0.8f,
        animationSpec = infiniteRepeatable(
            animation = tween(duration, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "shimmer_alpha"
    )
    
    this.graphicsLayer {
        this.alpha = alpha
    }
}

/**
 * Adds a delayed entrance animation
 */
fun Modifier.slideInAnimation(
    delay: Int = 0,
    duration: Int = 600
) = composed(
    inspectorInfo = debugInspectorInfo {
        name = "slideInAnimation"
        properties["delay"] = delay
        properties["duration"] = duration
    }
) {
    var isVisible by remember { mutableStateOf(false) }
    
    val animatedOffset by animateIntOffsetAsState(
        targetValue = if (isVisible) IntOffset.Zero else IntOffset(0, 100),
        animationSpec = tween(duration, easing = FastOutSlowInEasing),
        label = "slide_in_offset"
    )
    
    val animatedAlpha by animateFloatAsState(
        targetValue = if (isVisible) 1f else 0f,
        animationSpec = tween(duration, easing = FastOutSlowInEasing),
        label = "slide_in_alpha"
    )
    
    LaunchedEffect(Unit) {
        delay(delay.toLong())
        isVisible = true
    }
    
    this
        .offset { animatedOffset }
        .graphicsLayer {
            alpha = animatedAlpha
        }
}

/**
 * Creates a staggered animation delay based on index
 */
@Composable
fun rememberStaggeredAnimationDelay(
    index: Int,
    baseDelay: Int = 100
): Int {
    return remember(index) { index * baseDelay }
}

/**
 * Creates a smooth transition between states
 */
@Composable
fun rememberSmoothTransition(
    targetState: Boolean,
    duration: Int = 300
): Float {
    return animateFloatAsState(
        targetValue = if (targetState) 1f else 0f,
        animationSpec = tween(duration, easing = FastOutSlowInEasing),
        label = "smooth_transition"
    ).value
}