package com.littlegig.app.presentation.components

import androidx.compose.animation.core.*
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
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
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.littlegig.app.presentation.theme.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

// Modern Glassmorphic Card with proper iOS/macOS style
@Composable
fun ModernGlassCard(
    modifier: Modifier = Modifier,
    onClick: (() -> Unit)? = null,
    cornerRadius: Dp = 20.dp,
    content: @Composable BoxScope.() -> Unit
) {
    val isDark = isSystemInDarkTheme()
    
    Surface(
        modifier = modifier
            .clip(RoundedCornerShape(cornerRadius))
            .then(
                if (onClick != null) {
                    Modifier.clickable(
                        interactionSource = remember { MutableInteractionSource() },
                        indication = rememberRipple(color = LittleGigPrimary.copy(alpha = 0.1f))
                    ) { onClick() }
                } else Modifier
            ),
        shape = RoundedCornerShape(cornerRadius),
        color = if (isDark) Color(0xFF1A1A1A).copy(alpha = 0.8f) else Color(0xFFFFFFFF).copy(alpha = 0.8f),
        border = BorderStroke(
            width = 0.5.dp,
            brush = Brush.linearGradient(
                colors = listOf(
                    if (isDark) Color.White.copy(alpha = 0.1f) else Color.Black.copy(alpha = 0.05f),
                    Color.Transparent,
                    if (isDark) Color.White.copy(alpha = 0.05f) else Color.Black.copy(alpha = 0.02f)
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
                            if (isDark) Color(0xFF2A2A2A).copy(alpha = 0.3f) else Color(0xFFFFFFFF).copy(alpha = 0.4f),
                            if (isDark) Color(0xFF1A1A1A).copy(alpha = 0.2f) else Color(0xFFF8F9FA).copy(alpha = 0.3f)
                        )
                    )
                )
        ) {
            content()
        }
    }
}

// Modern Neumorphic Button
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ModernNeumorphicButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    content: @Composable RowScope.() -> Unit
) {
    val isDark = isSystemInDarkTheme()
    var pressed by remember { mutableStateOf(false) }
    
    val animatedPressed by animateFloatAsState(
        targetValue = if (pressed) 1f else 0f,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessLow
        ),
        label = "pressed_animation"
    )
    
    Surface(
        onClick = {
            pressed = true
            onClick()
            // Reset after animation
            CoroutineScope(Dispatchers.Main).launch {
                delay(150)
                pressed = false
            }
        },
        modifier = modifier
            .height(56.dp)
            .drawBehind {
                drawModernNeumorphicShadow(
                    isDark = isDark,
                    pressed = animatedPressed > 0.5f,
                    cornerRadius = 28.dp.toPx()
                )
            },
        enabled = enabled,
        shape = RoundedCornerShape(28.dp),
        color = if (isDark) Color(0xFF2A2A2A) else Color(0xFFF8F9FA),
        border = BorderStroke(
            width = 0.5.dp,
            brush = Brush.linearGradient(
                colors = listOf(
                    if (isDark) Color.White.copy(alpha = 0.1f) else Color.Black.copy(alpha = 0.05f),
                    Color.Transparent
                )
            )
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 24.dp),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically,
            content = content
        )
    }
}

// Modern Bottom Navigation with proper spacing
@Composable
fun ModernBottomNavigation(
    currentRoute: String?,
    onNavigate: (String) -> Unit
) {
    val isDark = isSystemInDarkTheme()
    
    val items = listOf(
        BottomNavItem("events", "Events", Icons.Default.Event),
        BottomNavItem("tickets", "Tickets", Icons.Default.Receipt),
        BottomNavItem("map", "LittleMap", Icons.Default.Map),
        BottomNavItem("upload", "Upload", Icons.Default.Upload),
        BottomNavItem("account", "Account", Icons.Default.AccountCircle)
    )
    
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .height(100.dp), // Increased height to avoid overlap
        color = if (isDark) Color(0xFF1A1A1A).copy(alpha = 0.95f) else Color(0xFFFFFFFF).copy(alpha = 0.95f),
        border = BorderStroke(
            width = 0.5.dp,
            brush = Brush.horizontalGradient(
                colors = listOf(
                    Color.Transparent,
                    if (isDark) Color.White.copy(alpha = 0.1f) else Color.Black.copy(alpha = 0.05f),
                    Color.Transparent
                )
            )
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp, vertical = 12.dp),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {
            items.forEach { item ->
                ModernNavItem(
                    item = item,
                    isSelected = currentRoute == item.route,
                    onClick = { onNavigate(item.route) }
                )
            }
        }
    }
}

@Composable
fun ModernNavItem(
    item: BottomNavItem,
    
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
    
    Column(
        modifier = Modifier
            .clickable { onClick() }
            .padding(8.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Box(
            modifier = Modifier
                .size(48.dp)
                .scale(animatedScale)
                .background(
                    if (isSelected) {
                        if (isDark) Color(0xFF3A3A3A) else Color(0xFFE8E8E8)
                    } else {
                        Color.Transparent
                    },
                    CircleShape
                ),
            contentAlignment = Alignment.Center
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
        }
        
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

// Modern Background without animations
@Composable
fun ModernBackground(
    modifier: Modifier = Modifier,
    content: @Composable BoxScope.() -> Unit
) {
    val isDark = isSystemInDarkTheme()
    
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colors = listOf(
                        if (isDark) Color(0xFF0A0A0A) else Color(0xFFF5F5F5),
                        if (isDark) Color(0xFF1A1A1A) else Color(0xFFFFFFFF)
                    )
                )
            )
    ) {
        content()
    }
}

// Extension function to draw modern neumorphic shadows
private fun DrawScope.drawModernNeumorphicShadow(
    isDark: Boolean,
    pressed: Boolean,
    cornerRadius: Float
) {
    val shadowOffset = if (pressed) 1.dp.toPx() else 4.dp.toPx()
    
    val lightShadow = if (isDark) Color.White.copy(alpha = 0.1f) else Color.White.copy(alpha = 0.8f)
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