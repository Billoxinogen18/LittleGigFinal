package com.littlegig.app.presentation.components

import androidx.compose.animation.core.*
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.*
import androidx.compose.ui.graphics.*
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.littlegig.app.presentation.theme.*
import dev.chrisbanes.haze.HazeState
import dev.chrisbanes.haze.haze
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.graphics.drawscope.scale
import androidx.compose.ui.graphics.drawscope.translate
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.graphics.drawscope.Stroke
import kotlin.math.*

// 🌊 LIQUID GLASS DESIGN SYSTEM 🌊
// Advanced glassmorphism with real-time refraction, dynamic blur, and liquid effects
// Inspired by iOS 26/macOS 26 sophisticated glass panels

@Composable
fun LiquidGlassCard(
    modifier: Modifier = Modifier,
    cornerRadius: Dp = 20.dp,
    alpha: Float = 0.85f,
    blurRadius: Dp = 24.dp,
    borderWidth: Dp = 1.5.dp,
    borderColor: Color = Color.Transparent,
    refractionIntensity: Float = 0.3f,
    liquidFlow: Boolean = true,
    content: @Composable () -> Unit
) {
    val isDark = isSystemInDarkTheme()
    val density = LocalDensity.current
    
    // Real-time liquid flow animation
    val liquidFlowAnimation by rememberInfiniteTransition(label = "liquid_flow").animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(4000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "liquid_flow"
    )
    
    // Dynamic refraction effect
    val refractionAnimation by rememberInfiniteTransition(label = "refraction").animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(3000, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "refraction"
    )
    
    // Sophisticated glass layering with real-time effects
    Surface(
        modifier = modifier
            .clip(RoundedCornerShape(cornerRadius))
            .drawBehind {
                // Primary glass layer with dynamic transparency
                drawRect(
                    brush = Brush.radialGradient(
                        colors = if (isDark) {
                            listOf(
                                GlassmorphicDark.copy(alpha = alpha * 0.9f),
                                GlassmorphicDark.copy(alpha = alpha * 0.7f),
                                GlassmorphicDark.copy(alpha = alpha * 0.5f)
                            )
                        } else {
                            listOf(
                                GlassmorphicLight.copy(alpha = alpha * 0.95f),
                                GlassmorphicLight.copy(alpha = alpha * 0.8f),
                                GlassmorphicLight.copy(alpha = alpha * 0.6f)
                            )
                        },
                        center = Offset(size.width * 0.3f, size.height * 0.3f),
                        radius = size.minDimension * 0.8f
                    )
                )
                
                // Liquid flow effect layer
                if (liquidFlow) {
                    rotate(liquidFlowAnimation * 360f) {
                        drawRect(
                            brush = Brush.linearGradient(
                                colors = listOf(
                                    Color.Transparent,
                                    Color.White.copy(alpha = 0.1f * refractionIntensity),
                                    Color.Transparent,
                                    Color.White.copy(alpha = 0.05f * refractionIntensity),
                                    Color.Transparent
                                ),
                                start = Offset(-size.width * 0.5f, -size.height * 0.5f),
                                end = Offset(size.width * 1.5f, size.height * 1.5f)
                            )
                        )
                    }
                }
                
                // Real-time refraction edge effects
                val refractionOffset = sin(refractionAnimation * 2 * PI.toFloat()) * 2f
                drawRect(
                    brush = Brush.linearGradient(
                        colors = listOf(
                            Color.Transparent,
                            if (isDark) Color.White.copy(alpha = 0.15f * refractionIntensity) else Color.Black.copy(alpha = 0.08f * refractionIntensity),
                            Color.Transparent
                        ),
                        start = Offset(refractionOffset, 0f),
                        end = Offset(1000f + refractionOffset, 1000f)
                    )
                )
                
                // Sophisticated border with refraction
                drawRoundRect(
                    color = borderColor.copy(alpha = 0.6f),
                    topLeft = androidx.compose.ui.geometry.Offset.Zero,
                    size = size,
                    style = androidx.compose.ui.graphics.drawscope.Stroke(
                        width = borderWidth.toPx()
                    )
                )
            }
            .blur(blurRadius),
        shape = RoundedCornerShape(cornerRadius),
        color = Color.Transparent
    ) {
        Box(
            modifier = Modifier
                .background(
                    brush = Brush.linearGradient(
                        colors = if (isDark) {
                            listOf(
                                Color.White.copy(alpha = 0.08f),
                                Color.Transparent,
                                Color.White.copy(alpha = 0.03f)
                            )
                        } else {
                            listOf(
                                Color.White.copy(alpha = 0.25f),
                                Color.Transparent,
                                Color.White.copy(alpha = 0.08f)
                            )
                        },
                        start = Offset(0f, 0f),
                        end = Offset(1000f, 1000f)
                    )
                )
        ) {
            content()
        }
    }
}

@Composable
fun LiquidGlassButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    cornerRadius: Dp = 20.dp,
    liquidFlow: Boolean = true,
    content: @Composable () -> Unit
) {
    val isDark = isSystemInDarkTheme()
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()
    
    val scale by animateFloatAsState(
        targetValue = if (isPressed) 0.92f else 1f,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessLow
        ),
        label = "button_scale"
    )
    
    val alpha by animateFloatAsState(
        targetValue = if (isPressed) 0.95f else 0.85f,
        animationSpec = tween(150),
        label = "button_alpha"
    )
    
    LiquidGlassCard(
        modifier = modifier
            .scale(scale)
            .clickable(
                interactionSource = interactionSource,
                indication = rememberRipple(
                    color = if (isDark) Color.White.copy(alpha = 0.15f) else Color.Black.copy(alpha = 0.08f),
                    bounded = true
                )
            ) { onClick() },
        cornerRadius = cornerRadius,
        alpha = alpha,
        borderColor = if (isDark) {
            Color.White.copy(alpha = 0.25f)
        } else {
            Color.Black.copy(alpha = 0.12f)
        },
        liquidFlow = liquidFlow && !isPressed
    ) {
        content()
    }
}

@Composable
fun LiquidGlassInputField(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    placeholder: String = "",
    leadingIcon: ImageVector? = null,
    trailingIcon: ImageVector? = null,
    onTrailingIconClick: (() -> Unit)? = null
) {
    val isDark = isSystemInDarkTheme()
    
    LiquidGlassCard(
        modifier = modifier,
        cornerRadius = 24.dp,
        alpha = if (isDark) 0.7f else 0.8f,
        borderColor = if (isDark) {
            Color.White.copy(alpha = 0.2f)
        } else {
            Color.Black.copy(alpha = 0.1f)
        },
        liquidFlow = false // No liquid flow for input fields
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp, vertical = 16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            if (leadingIcon != null) {
                Icon(
                    imageVector = leadingIcon,
                    contentDescription = null,
                    tint = if (isDark) Color.White.copy(alpha = 0.8f) else Color.Black.copy(alpha = 0.6f),
                    modifier = Modifier.size(22.dp)
                )
                Spacer(modifier = Modifier.width(16.dp))
            }
            
            androidx.compose.material3.TextField(
                value = value,
                onValueChange = onValueChange,
                placeholder = {
                    Text(
                        text = placeholder,
                        color = if (isDark) Color.White.copy(alpha = 0.5f) else Color.Black.copy(alpha = 0.4f)
                    )
                },
                colors = androidx.compose.material3.TextFieldDefaults.colors(
                    focusedContainerColor = Color.Transparent,
                    unfocusedContainerColor = Color.Transparent,
                    disabledContainerColor = Color.Transparent,
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent,
                    disabledIndicatorColor = Color.Transparent,
                    focusedTextColor = if (isDark) Color.White else Color.Black,
                    unfocusedTextColor = if (isDark) Color.White else Color.Black
                ),
                modifier = Modifier.weight(1f)
            )
            
            if (trailingIcon != null) {
                Spacer(modifier = Modifier.width(16.dp))
                Icon(
                    imageVector = trailingIcon,
                    contentDescription = null,
                    tint = if (isDark) Color.White.copy(alpha = 0.8f) else Color.Black.copy(alpha = 0.6f),
                    modifier = Modifier
                        .size(22.dp)
                        .clickable { onTrailingIconClick?.invoke() }
                )
            }
        }
    }
}

@Composable
fun LiquidGlassBottomNavigation(
    currentRoute: String?,
    onNavigate: (String) -> Unit,
    inboxUnreadCount: Int = 0,
    hazeState: HazeState
) {
    val isDark = isSystemInDarkTheme()
    
    val items = listOf(
        BottomNavItem("events", "Events", Icons.Default.Event),
        BottomNavItem("tickets", "Tickets", Icons.Default.Receipt),
        BottomNavItem("map", "LittleMap", Icons.Default.Map),
        BottomNavItem("chat", "Chat", Icons.Default.Chat)
    )
    
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp)
            .padding(bottom = 64.dp)
    ) {
        LiquidGlassCard(
            modifier = Modifier
                .fillMaxWidth()
                .height(96.dp)
                .haze(hazeState),
            cornerRadius = 32.dp,
            alpha = if (isDark) 0.7f else 0.8f,
            borderColor = if (isDark) {
                Color.White.copy(alpha = 0.2f)
            } else {
                Color.Black.copy(alpha = 0.1f)
            },
            liquidFlow = true
        ) {
            Row(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 24.dp, vertical = 20.dp),
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.CenterVertically
            ) {
                items.forEach { item ->
                    LiquidGlassNavItem(
                        item = item,
                        isSelected = currentRoute == item.route,
                        onClick = { onNavigate(item.route) }
                    )
                }
            }
        }
    }
}

@Composable
fun LiquidGlassNavItem(
    item: BottomNavItem,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    val isDark = isSystemInDarkTheme()
    
    val animatedScale by animateFloatAsState(
        targetValue = if (isSelected) 1.15f else 1f,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessLow
        ),
        label = "nav_scale"
    )
    
    val animatedAlpha by animateFloatAsState(
        targetValue = if (isSelected) 1f else 0.7f,
        animationSpec = tween(200),
        label = "nav_alpha"
    )
    
    Box(
        modifier = Modifier
            .clickable { onClick() }
            .padding(10.dp)
            .scale(animatedScale),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Icon(
                imageVector = item.icon,
                contentDescription = item.label,
                tint = if (isSelected) {
                    LittleGigPrimary
                } else {
                    if (isDark) Color.White.copy(alpha = animatedAlpha) else Color.Black.copy(alpha = animatedAlpha)
                },
                modifier = Modifier.size(26.dp)
            )
            
            Spacer(modifier = Modifier.height(6.dp))
            
            Text(
                text = item.label,
                style = MaterialTheme.typography.labelSmall,
                color = if (isSelected) {
                    LittleGigPrimary
                } else {
                    if (isDark) Color.White.copy(alpha = animatedAlpha) else Color.Black.copy(alpha = animatedAlpha)
                }
            )
        }
    }
}

@Composable
fun LiquidGlassChip(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    isSelected: Boolean = false,
    icon: ImageVector? = null
) {
    val isDark = isSystemInDarkTheme()
    
    val animatedScale by animateFloatAsState(
        targetValue = if (isSelected) 1.08f else 1f,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessLow
        ),
        label = "chip_scale"
    )
    
    LiquidGlassCard(
        modifier = modifier
            .scale(animatedScale)
            .clickable { onClick() },
        cornerRadius = 24.dp,
        alpha = if (isSelected) {
            if (isDark) 0.85f else 0.9f
        } else {
            if (isDark) 0.6f else 0.7f
        },
        borderColor = if (isSelected) {
            LittleGigPrimary.copy(alpha = 0.6f)
        } else {
            if (isDark) Color.White.copy(alpha = 0.15f) else Color.Black.copy(alpha = 0.08f)
        },
        liquidFlow = isSelected
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 20.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            if (icon != null) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = if (isSelected) {
                        LittleGigPrimary
                    } else {
                        if (isDark) Color.White.copy(alpha = 0.8f) else Color.Black.copy(alpha = 0.6f)
                    },
                    modifier = Modifier.size(18.dp)
                )
                Spacer(modifier = Modifier.width(12.dp))
            }
            
            Text(
                text = text,
                style = MaterialTheme.typography.bodyMedium.copy(
                    fontWeight = if (isSelected) FontWeight.SemiBold else FontWeight.Normal
                ),
                color = if (isSelected) {
                    LittleGigPrimary
                } else {
                    if (isDark) Color.White.copy(alpha = 0.9f) else Color.Black.copy(alpha = 0.8f)
                }
            )
        }
    }
}

@Composable
fun LiquidGlassAvatar(
    imageUrl: String?,
    modifier: Modifier = Modifier,
    size: Dp = 52.dp,
    placeholderIcon: ImageVector = Icons.Default.Person
) {
    val isDark = isSystemInDarkTheme()
    
    LiquidGlassCard(
        modifier = modifier.size(size),
        cornerRadius = size / 2,
        alpha = if (isDark) 0.7f else 0.8f,
        borderColor = if (isDark) {
            Color.White.copy(alpha = 0.25f)
        } else {
            Color.Black.copy(alpha = 0.12f)
        },
        liquidFlow = false
    ) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            if (imageUrl != null) {
                coil.compose.AsyncImage(
                    model = imageUrl,
                    contentDescription = null,
                    modifier = Modifier
                        .fillMaxSize()
                        .clip(CircleShape),
                    contentScale = androidx.compose.ui.layout.ContentScale.Crop
                )
            } else {
                Icon(
                    imageVector = placeholderIcon,
                    contentDescription = null,
                    tint = if (isDark) Color.White.copy(alpha = 0.7f) else Color.Black.copy(alpha = 0.5f),
                    modifier = Modifier.size(size * 0.5f)
                )
            }
        }
    }
}

@Composable
fun LiquidGlassEmptyState(
    icon: ImageVector,
    title: String,
    message: String,
    primaryActionLabel: String? = null,
    onPrimaryAction: (() -> Unit)? = null,
    modifier: Modifier = Modifier
) {
    LiquidGlassCard(
        modifier = modifier,
        cornerRadius = 28.dp,
        alpha = if (isSystemInDarkTheme()) 0.8f else 0.85f,
        liquidFlow = true
    ) {
        Column(
            modifier = Modifier.padding(36.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            val shimmer = rememberInfiniteTransition(label = "empty_state").animateFloat(
                initialValue = 0.7f,
                targetValue = 1f,
                animationSpec = infiniteRepeatable(
                    animation = tween(2000, easing = LinearEasing),
                    repeatMode = RepeatMode.Reverse
                ),
                label = "alpha"
            )
            
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = LittleGigPrimary.copy(alpha = shimmer.value),
                modifier = Modifier.size(72.dp)
            )
            
            Spacer(modifier = Modifier.height(20.dp))
            
            Text(
                text = title,
                style = MaterialTheme.typography.titleLarge.copy(
                    fontWeight = FontWeight.SemiBold
                ),
                color = MaterialTheme.colorScheme.onSurface
            )
            
            Spacer(modifier = Modifier.height(12.dp))
            
            Text(
                text = message,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                textAlign = androidx.compose.ui.text.style.TextAlign.Center
            )
            
            if (primaryActionLabel != null && onPrimaryAction != null) {
                Spacer(modifier = Modifier.height(28.dp))
                
                LiquidGlassButton(
                    onClick = onPrimaryAction,
                    cornerRadius = 24.dp
                ) {
                    Text(
                        text = primaryActionLabel,
                        style = MaterialTheme.typography.bodyMedium.copy(
                            fontWeight = FontWeight.SemiBold
                        ),
                        color = LittleGigPrimary,
                        modifier = Modifier.padding(horizontal = 28.dp, vertical = 16.dp)
                    )
                }
            }
        }
    }
}

// Advanced liquid glass panel with real-time refraction
@Composable
fun LiquidGlassPanel(
    modifier: Modifier = Modifier,
    cornerRadius: Dp = 24.dp,
    alpha: Float = 0.9f,
    blurRadius: Dp = 32.dp,
    refractionIntensity: Float = 0.4f,
    content: @Composable () -> Unit
) {
    val isDark = isSystemInDarkTheme()
    
    // Real-time refraction animation
    val refractionAnimation by rememberInfiniteTransition(label = "panel_refraction").animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(5000, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "refraction"
    )
    
    Surface(
        modifier = modifier
            .clip(RoundedCornerShape(cornerRadius))
            .drawBehind {
                // Multi-layer glass effect with real-time refraction
                val refractionOffset = sin(refractionAnimation * 2 * PI.toFloat()) * 3f
                
                // Primary glass layer
                drawRect(
                    brush = Brush.radialGradient(
                        colors = if (isDark) {
                            listOf(
                                GlassmorphicDark.copy(alpha = alpha * 0.95f),
                                GlassmorphicDark.copy(alpha = alpha * 0.8f),
                                GlassmorphicDark.copy(alpha = alpha * 0.6f)
                            )
                        } else {
                            listOf(
                                GlassmorphicLight.copy(alpha = alpha * 0.98f),
                                GlassmorphicLight.copy(alpha = alpha * 0.85f),
                                GlassmorphicLight.copy(alpha = alpha * 0.7f)
                            )
                        },
                        center = Offset(500f, 500f),
                        radius = 500f
                    )
                )
                
                // Real-time refraction layer
                drawRect(
                    brush = Brush.linearGradient(
                        colors = listOf(
                            Color.Transparent,
                            if (isDark) Color.White.copy(alpha = 0.12f * refractionIntensity) else Color.Black.copy(alpha = 0.06f * refractionIntensity),
                            Color.Transparent,
                            if (isDark) Color.White.copy(alpha = 0.08f * refractionIntensity) else Color.Black.copy(alpha = 0.04f * refractionIntensity),
                            Color.Transparent
                        ),
                        start = Offset(refractionOffset, 0f),
                        end = Offset(1000f + refractionOffset, 1000f)
                    )
                )
                
                // Sophisticated border with dynamic refraction
                drawRoundRect(
                    color = if (isDark) Color.White.copy(alpha = 0.4f) else Color.Black.copy(alpha = 0.2f),
                    topLeft = androidx.compose.ui.geometry.Offset.Zero,
                    size = size,
                    style = androidx.compose.ui.graphics.drawscope.Stroke(
                        width = 2f
                    )
                )
            }
            .blur(blurRadius),
        shape = RoundedCornerShape(cornerRadius),
        color = Color.Transparent
    ) {
        Box(
            modifier = Modifier
                .background(
                    brush = Brush.linearGradient(
                        colors = if (isDark) {
                            listOf(
                                Color.White.copy(alpha = 0.06f),
                                Color.Transparent,
                                Color.White.copy(alpha = 0.02f)
                            )
                        } else {
                            listOf(
                                Color.White.copy(alpha = 0.2f),
                                Color.Transparent,
                                Color.White.copy(alpha = 0.06f)
                            )
                        },
                        start = Offset(0f, 0f),
                        end = Offset(1000f, 1000f)
                    )
                )
        ) {
            content()
        }
    }
}