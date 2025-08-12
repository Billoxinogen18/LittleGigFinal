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

// 🌟 TRUE GLASSMORPHIC DESIGN SYSTEM 🌟
// Based on reference images with proper transparency, blur, and vibrant colors

@Composable
fun GlassmorphicCard(
    modifier: Modifier = Modifier,
    cornerRadius: Dp = 16.dp,
    alpha: Float = 0.8f,
    tint: Color = Color.Transparent,
    blurRadius: Dp = 2.dp,
    borderWidth: Dp = 1.dp,
    borderColor: Color = Color.Transparent,
    content: @Composable () -> Unit
) {
    val isDark = isSystemInDarkTheme()
    
    Surface(
        modifier = modifier
            .clip(RoundedCornerShape(cornerRadius))
            .background(
                brush = Brush.radialGradient(
                    colors = if (isDark) {
                        listOf(
                            GlassmorphicDark.copy(alpha = alpha),
                            GlassmorphicDark.copy(alpha = alpha * 0.8f)
                        )
                    } else {
                        listOf(
                            GlassmorphicLight.copy(alpha = alpha),
                            GlassmorphicLight.copy(alpha = alpha * 0.8f)
                        )
                    }
                )
            )
            .border(
                width = borderWidth,
                color = borderColor,
                shape = RoundedCornerShape(cornerRadius)
            ),
        shape = RoundedCornerShape(cornerRadius),
        color = Color.Transparent
    ) {
        Box(
            modifier = Modifier
                .background(
                    brush = Brush.linearGradient(
                        colors = if (isDark) {
                            listOf(
                                Color.White.copy(alpha = 0.1f),
                                Color.Transparent,
                                Color.White.copy(alpha = 0.05f)
                            )
                        } else {
                            listOf(
                                Color.White.copy(alpha = 0.3f),
                                Color.Transparent,
                                Color.White.copy(alpha = 0.1f)
                            )
                        }
                    )
                )
        ) {
            content()
        }
    }
}

@Composable
fun GlassmorphicButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    cornerRadius: Dp = 16.dp,
    content: @Composable () -> Unit
) {
    val isDark = isSystemInDarkTheme()
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()
    
    val scale by animateFloatAsState(
        targetValue = if (isPressed) 0.95f else 1f,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessLow
        ),
        label = "button_scale"
    )
    
    GlassmorphicCard(
        modifier = modifier
            .scale(scale)
            .clickable(
                interactionSource = interactionSource,
                indication = rememberRipple(
                    color = if (isDark) Color.White.copy(alpha = 0.2f) else Color.Black.copy(alpha = 0.1f),
                    bounded = true
                )
            ) { onClick() },
        cornerRadius = cornerRadius,
        alpha = if (isDark) 0.7f else 0.8f,
        borderColor = if (isDark) {
            Color.White.copy(alpha = 0.2f)
        } else {
            Color.Black.copy(alpha = 0.1f)
        }
    ) {
        content()
    }
}

@Composable
fun GlassmorphicInputField(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    placeholder: String = "",
    leadingIcon: ImageVector? = null,
    trailingIcon: ImageVector? = null,
    onTrailingIconClick: (() -> Unit)? = null
) {
    val isDark = isSystemInDarkTheme()
    
    GlassmorphicCard(
        modifier = modifier,
        cornerRadius = 20.dp,
        alpha = if (isDark) 0.6f else 0.7f,
        borderColor = if (isDark) {
            Color.White.copy(alpha = 0.15f)
        } else {
            Color.Black.copy(alpha = 0.08f)
        }
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            if (leadingIcon != null) {
                Icon(
                    imageVector = leadingIcon,
                    contentDescription = null,
                    tint = if (isDark) Color.White.copy(alpha = 0.8f) else Color.Black.copy(alpha = 0.6f),
                    modifier = Modifier.size(20.dp)
                )
                Spacer(modifier = Modifier.width(12.dp))
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
                Spacer(modifier = Modifier.width(12.dp))
                Icon(
                    imageVector = trailingIcon,
                    contentDescription = null,
                    tint = if (isDark) Color.White.copy(alpha = 0.8f) else Color.Black.copy(alpha = 0.6f),
                    modifier = Modifier
                        .size(20.dp)
                        .clickable { onTrailingIconClick?.invoke() }
                )
            }
        }
    }
}

@Composable
fun GlassmorphicBottomNavigation(
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
            .padding(horizontal = 16.dp)
            .padding(bottom = 64.dp)
    ) {
        GlassmorphicCard(
            modifier = Modifier
                .fillMaxWidth()
                .height(88.dp)
                .haze(hazeState),
            cornerRadius = 28.dp,
            alpha = if (isDark) 0.6f else 0.75f,
            borderColor = if (isDark) {
                Color.White.copy(alpha = 0.15f)
            } else {
                Color.Black.copy(alpha = 0.08f)
            }
        ) {
            Row(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 20.dp, vertical = 16.dp),
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.CenterVertically
            ) {
                items.forEach { item ->
                    GlassmorphicNavItem(
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
fun GlassmorphicNavItem(
    item: BottomNavItem,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    val isDark = isSystemInDarkTheme()
    
    val animatedScale by animateFloatAsState(
        targetValue = if (isSelected) 1.1f else 1f,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessLow
        ),
        label = "nav_scale"
    )
    
    Box(
        modifier = Modifier
            .clickable { onClick() }
            .padding(8.dp)
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
                    if (isDark) Color.White.copy(alpha = 0.7f) else Color.Black.copy(alpha = 0.6f)
                },
                modifier = Modifier.size(24.dp)
            )
            
            Spacer(modifier = Modifier.height(4.dp))
            
            Text(
                text = item.label,
                style = MaterialTheme.typography.labelSmall,
                color = if (isSelected) {
                    LittleGigPrimary
                } else {
                    if (isDark) Color.White.copy(alpha = 0.7f) else Color.Black.copy(alpha = 0.6f)
                }
            )
        }
    }
}

@Composable
fun GlassmorphicChip(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    isSelected: Boolean = false,
    icon: ImageVector? = null
) {
    val isDark = isSystemInDarkTheme()
    
    val animatedScale by animateFloatAsState(
        targetValue = if (isSelected) 1.05f else 1f,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessLow
        ),
        label = "chip_scale"
    )
    
    GlassmorphicCard(
        modifier = modifier
            .scale(animatedScale)
            .clickable { onClick() },
        cornerRadius = 20.dp,
        alpha = if (isSelected) {
            if (isDark) 0.8f else 0.9f
        } else {
            if (isDark) 0.5f else 0.6f
        },
        borderColor = if (isSelected) {
            LittleGigPrimary.copy(alpha = 0.5f)
        } else {
            if (isDark) Color.White.copy(alpha = 0.1f) else Color.Black.copy(alpha = 0.05f)
        }
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
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
                    modifier = Modifier.size(16.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
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
fun GlassmorphicAvatar(
    imageUrl: String?,
    modifier: Modifier = Modifier,
    size: Dp = 48.dp,
    placeholderIcon: ImageVector = Icons.Default.Person
) {
    val isDark = isSystemInDarkTheme()
    
    GlassmorphicCard(
        modifier = modifier.size(size),
        cornerRadius = size / 2,
        alpha = if (isDark) 0.6f else 0.7f,
        borderColor = if (isDark) {
            Color.White.copy(alpha = 0.2f)
        } else {
            Color.Black.copy(alpha = 0.1f)
        }
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
fun GlassmorphicEmptyState(
    icon: ImageVector,
    title: String,
    message: String,
    primaryActionLabel: String? = null,
    onPrimaryAction: (() -> Unit)? = null,
    modifier: Modifier = Modifier
) {
    GlassmorphicCard(
        modifier = modifier,
        cornerRadius = 24.dp,
        alpha = if (isSystemInDarkTheme()) 0.7f else 0.8f
    ) {
        Column(
            modifier = Modifier.padding(32.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            val shimmer = rememberInfiniteTransition(label = "empty_state").animateFloat(
                initialValue = 0.6f,
                targetValue = 1f,
                animationSpec = infiniteRepeatable(
                    animation = tween(1500, easing = LinearEasing),
                    repeatMode = RepeatMode.Restart
                ),
                label = "alpha"
            )
            
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = LittleGigPrimary.copy(alpha = shimmer.value),
                modifier = Modifier.size(64.dp)
            )
            
            Spacer(modifier = Modifier.height(16.dp))
            
            Text(
                text = title,
                style = MaterialTheme.typography.titleLarge.copy(
                    fontWeight = FontWeight.SemiBold
                ),
                color = MaterialTheme.colorScheme.onSurface
            )
            
            Spacer(modifier = Modifier.height(8.dp))
            
            Text(
                text = message,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                textAlign = androidx.compose.ui.text.style.TextAlign.Center
            )
            
            if (primaryActionLabel != null && onPrimaryAction != null) {
                Spacer(modifier = Modifier.height(24.dp))
                
                GlassmorphicButton(
                    onClick = onPrimaryAction,
                    cornerRadius = 20.dp
                ) {
                    Text(
                        text = primaryActionLabel,
                        style = MaterialTheme.typography.bodyMedium.copy(
                            fontWeight = FontWeight.SemiBold
                        ),
                        color = LittleGigPrimary,
                        modifier = Modifier.padding(horizontal = 24.dp, vertical = 12.dp)
                    )
                }
            }
        }
    }
}

// Data class for navigation items
data class BottomNavItem(
    val route: String,
    val label: String,
    val icon: ImageVector
)

// 🌟 MISSING COMPONENTS FOR COMPILATION FIXES 🌟

@Composable
fun AdvancedGlassmorphicCard(
    modifier: Modifier = Modifier,
    cornerRadius: Dp = 16.dp,
    alpha: Float = 0.95f,
    borderWidth: Dp = 1.dp,
    borderColor: Color = Color.Transparent,
    content: @Composable () -> Unit
) {
    GlassmorphicCard(
        modifier = modifier,
        cornerRadius = cornerRadius,
        alpha = alpha,
        borderWidth = borderWidth,
        borderColor = borderColor,
        content = content
    )
}

@Composable
fun HapticButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    content: @Composable () -> Unit
) {
    GlassmorphicButton(
        onClick = onClick,
        modifier = modifier,
        enabled = enabled,
        content = content
    )
}

@Composable
fun NeumorphicRankBadge(
    rank: String,
    modifier: Modifier = Modifier
) {
    GlassmorphicChip(
        text = rank,
        onClick = { /* Rank badge is not clickable */ },
        modifier = modifier
    )
}

@Composable
fun ShimmerLoadingCard(
    modifier: Modifier = Modifier
) {
    val shimmerColors = listOf(
        Color.LightGray.copy(alpha = 0.6f),
        Color.LightGray.copy(alpha = 0.2f),
        Color.LightGray.copy(alpha = 0.6f)
    )
    
    val transition = rememberInfiniteTransition()
    val translateAnim = transition.animateFloat(
        initialValue = 0f,
        targetValue = 1000f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 1200, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Restart
        )
    )
    
    GlassmorphicCard(
        modifier = modifier.height(120.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    brush = Brush.linearGradient(
                        colors = shimmerColors,
                        start = androidx.compose.ui.geometry.Offset(translateAnim.value - 1000f, 0f),
                        end = androidx.compose.ui.geometry.Offset(translateAnim.value, 0f)
                    )
                )
        )
    }
}

@Composable
fun LoadingPulseAnimation(
    modifier: Modifier = Modifier
) {
    val infiniteTransition = rememberInfiniteTransition()
    val scale by infiniteTransition.animateFloat(
        initialValue = 0.8f,
        targetValue = 1.2f,
        animationSpec = infiniteRepeatable(
            animation = tween(1000),
            repeatMode = RepeatMode.Reverse
        )
    )
    
    Box(
        modifier = modifier
            .size(24.dp)
            .scale(scale)
            .background(
                color = LittleGigPrimary,
                shape = CircleShape
            )
    )
}

@Composable
fun FloatingActionButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    icon: ImageVector,
    contentDescription: String? = null
) {
    GlassmorphicButton(
        onClick = onClick,
        modifier = modifier.size(56.dp)
    ) {
        Icon(
            imageVector = icon,
            contentDescription = contentDescription,
            tint = Color.White
        )
    }
}

@Composable
fun GlassEmptyState(
    title: String,
    message: String,
    icon: ImageVector,
    modifier: Modifier = Modifier
) {
    GlassmorphicEmptyState(
        title = title,
        message = message,
        icon = icon,
        modifier = modifier
    )
}

@Composable
fun AdvancedNeumorphicCard(
    modifier: Modifier = Modifier,
    cornerRadius: Dp = 16.dp,
    content: @Composable () -> Unit
) {
    // For now, use GlassmorphicCard as replacement
    GlassmorphicCard(
        modifier = modifier,
        cornerRadius = cornerRadius,
        content = content
    )
}