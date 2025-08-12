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
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.littlegig.app.presentation.theme.*
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextAlign
import coil.compose.AsyncImage

// 🌟 CLEAN GLASSMORPHIC DESIGN SYSTEM 🌟
// Based on working EventDetailsScreen approach - NO BLUR EFFECTS

@Composable
fun LiquidGlassCard(
    modifier: Modifier = Modifier,
    cornerRadius: Dp = 20.dp,
    alpha: Float = 0.95f,
    borderWidth: Dp = 1.5.dp,
    borderColor: Color = Color.Transparent,
    content: @Composable () -> Unit
) {
    val isDark = isSystemInDarkTheme()
    
    Box(
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
            )
    ) {
        content()
    }
}

@Composable
fun LiquidGlassButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    cornerRadius: Dp = 20.dp,
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
    
    Box(
        modifier = modifier
            .scale(scale)
            .clip(RoundedCornerShape(cornerRadius))
            .background(
                brush = Brush.radialGradient(
                    colors = if (isDark) {
                        listOf(
                            GlassmorphicDark.copy(alpha = 0.9f),
                            GlassmorphicDark.copy(alpha = 0.7f)
                        )
                    } else {
                        listOf(
                            GlassmorphicLight.copy(alpha = 0.95f),
                            GlassmorphicLight.copy(alpha = 0.8f)
                        )
                    }
                )
            )
            .border(
                width = 1.5.dp,
                color = if (isDark) Color.White.copy(alpha = 0.25f) else Color.Black.copy(alpha = 0.12f),
                shape = RoundedCornerShape(cornerRadius)
            )
            .clickable(
                interactionSource = interactionSource,
                indication = rememberRipple(
                    color = if (isDark) Color.White.copy(alpha = 0.15f) else Color.Black.copy(alpha = 0.08f),
                    bounded = true
                )
            ) { onClick() }
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
        cornerRadius = 16.dp,
        alpha = if (isDark) 0.8f else 0.9f,
        borderColor = if (isDark) Color.White.copy(alpha = 0.2f) else Color.Black.copy(alpha = 0.1f)
    ) {
        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            placeholder = { Text(placeholder) },
            leadingIcon = leadingIcon?.let { { Icon(it, contentDescription = null) } },
            trailingIcon = trailingIcon?.let { icon ->
                {
                    Icon(
                        imageVector = icon,
                        contentDescription = null,
                        modifier = Modifier
                            .size(22.dp)
                            .clickable { onTrailingIconClick?.invoke() }
                    )
                }
            },
            modifier = Modifier.fillMaxWidth(),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = Color.Transparent,
                unfocusedBorderColor = Color.Transparent,
                focusedContainerColor = Color.Transparent,
                unfocusedContainerColor = Color.Transparent
            )
        )
    }
}

data class BottomNavItem(
    val route: String,
    val title: String,
    val icon: ImageVector
)

@Composable
fun LiquidGlassBottomNavigation(
    currentRoute: String?,
    onNavigate: (String) -> Unit,
    inboxUnreadCount: Int = 0
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
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(96.dp)
                .clip(RoundedCornerShape(32.dp))
                .background(
                    brush = Brush.radialGradient(
                        colors = if (isDark) {
                            listOf(
                                GlassmorphicDark.copy(alpha = 0.9f),
                                GlassmorphicDark.copy(alpha = 0.7f)
                            )
                        } else {
                            listOf(
                                GlassmorphicLight.copy(alpha = 0.95f),
                                GlassmorphicLight.copy(alpha = 0.8f)
                            )
                        }
                    )
                )
                .border(
                    width = 1.5.dp,
                    color = if (isDark) Color.White.copy(alpha = 0.2f) else Color.Black.copy(alpha = 0.1f),
                    shape = RoundedCornerShape(32.dp)
                )
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
    isSelected: Boolean = false,
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
                contentDescription = item.title,
                tint = if (isSelected) {
                    if (isDark) Color.White else Color.Black
                } else {
                    if (isDark) Color.White.copy(alpha = 0.6f) else Color.Black.copy(alpha = 0.6f)
                },
                modifier = Modifier.size(24.dp)
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = item.title,
                style = MaterialTheme.typography.labelSmall,
                color = if (isSelected) {
                    if (isDark) Color.White else Color.Black
                } else {
                    if (isDark) Color.White.copy(alpha = 0.6f) else Color.Black.copy(alpha = 0.6f)
                },
                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
            )
        }
    }
}

@Composable
fun LiquidGlassChip(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    selected: Boolean = false,
    isSelected: Boolean = false,
    icon: ImageVector? = null
) {
    val isDark = isSystemInDarkTheme()
    
    val scale by animateFloatAsState(
        targetValue = if (selected) 1.05f else 1f,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessLow
        ),
        label = "chip_scale"
    )
    
    Box(
        modifier = modifier
            .scale(scale)
            .clip(RoundedCornerShape(20.dp))
            .background(
                brush = Brush.radialGradient(
                    colors = if (isDark) {
                        listOf(
                            GlassmorphicDark.copy(alpha = if (selected) 0.9f else 0.8f),
                            GlassmorphicDark.copy(alpha = if (selected) 0.7f else 0.6f)
                        )
                    } else {
                        listOf(
                            GlassmorphicLight.copy(alpha = if (selected) 0.95f else 0.85f),
                            GlassmorphicLight.copy(alpha = if (selected) 0.8f else 0.7f)
                        )
                    }
                )
            )
            .border(
                width = 1.5.dp,
                color = if (selected) {
                    if (isDark) Color.White.copy(alpha = 0.4f) else Color.Black.copy(alpha = 0.2f)
                } else {
                    if (isDark) Color.White.copy(alpha = 0.2f) else Color.Black.copy(alpha = 0.1f)
                },
                shape = RoundedCornerShape(20.dp)
            )
            .clickable { onClick() }
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            if (icon != null) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = if (isDark) Color.White else Color.Black,
                    modifier = Modifier.size(16.dp)
                )
            }
            Text(
                text = text,
                style = MaterialTheme.typography.bodyMedium,
                color = if (isDark) Color.White else Color.Black,
                fontWeight = if (selected || isSelected) FontWeight.Bold else FontWeight.Normal
            )
        }
    }
}

@Composable
fun LiquidGlassAvatar(
    imageUrl: String?,
    modifier: Modifier = Modifier,
    size: Dp = 48.dp
) {
    val isDark = isSystemInDarkTheme()
    
    Box(
        modifier = modifier
            .size(size)
            .clip(CircleShape)
            .background(
                brush = Brush.radialGradient(
                    colors = if (isDark) {
                        listOf(
                            GlassmorphicDark.copy(alpha = 0.8f),
                            GlassmorphicDark.copy(alpha = 0.6f)
                        )
                    } else {
                        listOf(
                            GlassmorphicLight.copy(alpha = 0.9f),
                            GlassmorphicLight.copy(alpha = 0.7f)
                        )
                    }
                )
            )
            .border(
                width = 1.5.dp,
                color = if (isDark) Color.White.copy(alpha = 0.3f) else Color.Black.copy(alpha = 0.15f),
                shape = CircleShape
            )
    ) {
        if (imageUrl != null) {
            AsyncImage(
                model = imageUrl,
                contentDescription = "Avatar",
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop
            )
        } else {
            Icon(
                imageVector = Icons.Default.Person,
                contentDescription = "Default Avatar",
                modifier = Modifier
                    .fillMaxSize()
                    .padding(12.dp),
                tint = if (isDark) Color.White.copy(alpha = 0.7f) else Color.Black.copy(alpha = 0.7f)
            )
        }
    }
}

@Composable
fun LiquidGlassEmptyState(
    title: String,
    message: String,
    icon: ImageVector = Icons.Default.Info,
    modifier: Modifier = Modifier,
    primaryActionLabel: String? = null,
    onPrimaryAction: (() -> Unit)? = null
) {
    val isDark = isSystemInDarkTheme()
    
    Box(
        modifier = modifier
            .padding(16.dp)
            .clip(RoundedCornerShape(24.dp))
            .background(
                brush = Brush.radialGradient(
                    colors = if (isDark) {
                        listOf(
                            GlassmorphicDark.copy(alpha = 0.8f),
                            GlassmorphicDark.copy(alpha = 0.6f)
                        )
                    } else {
                        listOf(
                            GlassmorphicLight.copy(alpha = 0.9f),
                            GlassmorphicLight.copy(alpha = 0.7f)
                        )
                    }
                )
            )
            .border(
                width = 1.5.dp,
                color = if (isDark) Color.White.copy(alpha = 0.2f) else Color.Black.copy(alpha = 0.1f),
                shape = RoundedCornerShape(24.dp)
            )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(32.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                modifier = Modifier.size(64.dp),
                tint = if (isDark) Color.White.copy(alpha = 0.6f) else Color.Black.copy(alpha = 0.6f)
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = title,
                style = MaterialTheme.typography.headlineSmall,
                color = if (isDark) Color.White else Color.Black,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = message,
                style = MaterialTheme.typography.bodyMedium,
                color = if (isDark) Color.White.copy(alpha = 0.7f) else Color.Black.copy(alpha = 0.7f),
                textAlign = TextAlign.Center
            )
            
            if (primaryActionLabel != null && onPrimaryAction != null) {
                Spacer(modifier = Modifier.height(16.dp))
                LiquidGlassButton(
                    onClick = onPrimaryAction,
                    cornerRadius = 20.dp
                ) {
                    Text(
                        text = primaryActionLabel,
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.Medium,
                        color = if (isDark) Color.White else Color.Black
                    )
                }
            }
        }
    }
}

// 🌟 MISSING COMPONENTS FOR COMPILATION FIXES 🌟

@Composable
fun AdvancedLiquidGlassCard(
    modifier: Modifier = Modifier,
    cornerRadius: Dp = 16.dp,
    alpha: Float = 0.95f,
    borderWidth: Dp = 1.dp,
    borderColor: Color = Color.Transparent,
    content: @Composable () -> Unit
) {
    LiquidGlassCard(
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
    LiquidGlassButton(
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
    LiquidGlassChip(
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
    
    LiquidGlassCard(
        modifier = modifier.height(120.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    brush = Brush.linearGradient(
                        colors = shimmerColors,
                        start = Offset(translateAnim.value - 1000f, 0f),
                        end = Offset(translateAnim.value, 0f)
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
        modifier = modifier.scale(scale),
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator(
            color = if (isSystemInDarkTheme()) Color.White else Color.Black
        )
    }
}

@Composable
fun FloatingActionButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    icon: ImageVector = Icons.Default.Add,
    contentDescription: String? = null
) {
    LiquidGlassButton(
        onClick = onClick,
        modifier = modifier.size(56.dp),
        cornerRadius = 28.dp
    ) {
        Icon(
            imageVector = icon,
            contentDescription = contentDescription,
            modifier = Modifier.size(24.dp),
            tint = if (isSystemInDarkTheme()) Color.White else Color.Black
        )
    }
}

@Composable
fun GlassEmptyState(
    title: String,
    message: String,
    icon: ImageVector = Icons.Default.Info,
    modifier: Modifier = Modifier
) {
    LiquidGlassEmptyState(
        title = title,
        message = message,
        icon = icon,
        modifier = modifier
    )
}

@Composable
fun StatCard(
    title: String,
    value: String,
    icon: ImageVector,
    modifier: Modifier = Modifier
) {
    LiquidGlassCard(
        modifier = modifier,
        cornerRadius = 16.dp
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = GlassPrimary,
                modifier = Modifier.size(24.dp)
            )
            
            Spacer(modifier = Modifier.height(8.dp))
            
            Text(
                text = value,
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold,
                color = GlassOnSurface()
            )
            
            Text(
                text = title,
                style = MaterialTheme.typography.bodySmall,
                color = GlassOnSurface().copy(alpha = 0.7f)
            )
        }
    }
}

@Composable
fun AccountActionItem(
    icon: ImageVector,
    title: String,
    subtitle: String,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = GlassPrimary,
            modifier = Modifier.size(20.dp)
        )
        
        Spacer(modifier = Modifier.width(12.dp))
        
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = title,
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.Medium,
                color = GlassOnSurface()
            )
            
            Text(
                text = subtitle,
                style = MaterialTheme.typography.bodySmall,
                color = GlassOnSurface().copy(alpha = 0.7f)
            )
        }
        
        Icon(
            imageVector = Icons.Default.ChevronRight,
            contentDescription = null,
            tint = GlassOnSurface().copy(alpha = 0.5f),
            modifier = Modifier.size(16.dp)
        )
    }
}

@Composable
fun AdvancedNeumorphicCard(
    modifier: Modifier = Modifier,
    cornerRadius: Dp = 16.dp,
    alpha: Float = 0.95f,
    borderWidth: Dp = 1.dp,
    borderColor: Color = Color.Transparent,
    content: @Composable () -> Unit
) {
    LiquidGlassCard(
        modifier = modifier,
        cornerRadius = cornerRadius,
        alpha = alpha,
        borderWidth = borderWidth,
        borderColor = borderColor,
        content = content
    )
}