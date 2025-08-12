package com.littlegig.app.presentation.components

import androidx.compose.animation.core.*
import androidx.compose.foundation.*
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.*
import androidx.compose.ui.zIndex
import coil.compose.AsyncImage

// Modern Glassmorphic Color Palette
val GlassPrimary = Color(0xFF667EEA)
val GlassSecondary = Color(0xFF764BA2)
val GlassAccent = Color(0xFF4FACFE)
val GlassTeal = Color(0xFF00F5FF)
val GlassPink = Color(0xFFFF6B6B)
val GlassGreen = Color(0xFF4ECDC4)
val GlassOrange = Color(0xFFFFB347)
val GlassPurple = Color(0xFF9B59B6)

// Gradient Collections
val PrimaryGradient = Brush.linearGradient(
    colors = listOf(Color(0xFF667EEA), Color(0xFF764BA2))
)
val SecondaryGradient = Brush.linearGradient(
    colors = listOf(Color(0xFF4FACFE), Color(0xFF00F5FF))
)
val SunsetGradient = Brush.linearGradient(
    colors = listOf(Color(0xFFFF6B6B), Color(0xFFFFB347))
)
val OceanGradient = Brush.linearGradient(
    colors = listOf(Color(0xFF667EEA), Color(0xFF4FACFE))
)

// Backgrounds & Surfaces
@Composable
fun GlassBackground(): Color {
    return if (isSystemInDarkTheme()) Color(0xFF0F0F23) else Color(0xFFF8FAFC)
}

@Composable
fun GlassSurface(): Color {
    return if (isSystemInDarkTheme()) Color(0xFF16213E).copy(alpha = 0.7f) else Color(0xFFFFFFFF).copy(alpha = 0.8f)
}

@Composable
fun GlassCard(): Color {
    return if (isSystemInDarkTheme()) Color(0xFF1A1D29).copy(alpha = 0.8f) else Color(0xFFFFFFFF).copy(alpha = 0.9f)
}

// Text Colors
@Composable
fun GlassOnSurface(): Color {
    return if (isSystemInDarkTheme()) Color(0xFFE2E8F0) else Color(0xFF2D3748)
}

@Composable
fun GlassOnSurfaceVariant(): Color {
    return if (isSystemInDarkTheme()) Color(0xFFA0AEC0) else Color(0xFF718096)
}

/**
 * Modern Glassmorphic Card with backdrop blur effect
 */
@Composable
fun ModernLiquidGlassCard(
    modifier: Modifier = Modifier,
    onClick: (() -> Unit)? = null,
    shape: RoundedCornerShape = RoundedCornerShape(24.dp),
    content: @Composable BoxScope.() -> Unit
) {
    val cardModifier = modifier
        .background(
            brush = if (isSystemInDarkTheme()) 
                Brush.linearGradient(
                    colors = listOf(
                        Color(0xFF1A1D29).copy(alpha = 0.4f),
                        Color(0xFF16213E).copy(alpha = 0.2f)
                    )
                ) else 
                Brush.linearGradient(
                    colors = listOf(
                        Color(0xFFFFFFFF).copy(alpha = 0.25f),
                        Color(0xFFFFFFFF).copy(alpha = 0.1f)
                    )
                ),
            shape = shape
        )
        .border(
            width = 1.dp,
            brush = Brush.linearGradient(
                colors = if (isSystemInDarkTheme()) 
                    listOf(
                        Color(0xFFFFFFFF).copy(alpha = 0.2f),
                        Color(0xFFFFFFFF).copy(alpha = 0.1f)
                    ) else 
                    listOf(
                        Color(0xFFFFFFFF).copy(alpha = 0.8f),
                        Color(0xFFFFFFFF).copy(alpha = 0.3f)
                    )
            ),
            shape = shape
        )
        .clip(shape)
        
    if (onClick != null) {
        Box(
            modifier = cardModifier.clickable { onClick() },
            content = content
        )
    } else {
        Box(
            modifier = cardModifier,
            content = content
        )
    }
}

/**
 * Neumorphic Button with soft shadow effect
 */
@Composable
fun NeumorphicButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    colors: ButtonColors = ButtonDefaults.buttonColors(
        containerColor = GlassPrimary,
        contentColor = Color.White
    ),
    elevation: ButtonElevation = ButtonDefaults.buttonElevation(
        defaultElevation = 8.dp,
        pressedElevation = 4.dp
    ),
    shape: Shape = RoundedCornerShape(16.dp),
    contentPadding: PaddingValues = PaddingValues(horizontal = 24.dp, vertical = 12.dp),
    content: @Composable RowScope.() -> Unit
) {
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()
    
    val scale by animateFloatAsState(
        targetValue = if (isPressed) 0.95f else 1f,
        animationSpec = spring(dampingRatio = 0.6f),
        label = "button_scale"
    )
    
    Button(
        onClick = onClick,
        modifier = modifier
            .scale(scale)
            .shadow(
                elevation = if (isPressed) 4.dp else 8.dp,
                shape = shape,
                ambientColor = GlassPrimary.copy(alpha = 0.3f),
                spotColor = GlassPrimary.copy(alpha = 0.3f)
            ),
        enabled = enabled,
        shape = shape,
        colors = colors,
        elevation = elevation,
        contentPadding = contentPadding,
        interactionSource = interactionSource,
        content = content
    )
}

/**
 * Floating Action Button with glassmorphic effect
 */
@Composable
fun GlassFAB(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    icon: ImageVector = Icons.Default.Add,
    containerColor: Color = GlassPrimary,
    contentColor: Color = Color.White
) {
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()
    
    val scale by animateFloatAsState(
        targetValue = if (isPressed) 0.9f else 1f,
        animationSpec = spring(dampingRatio = 0.6f),
        label = "fab_scale"
    )
    
    Box(
        modifier = modifier
            .size(56.dp)
            .scale(scale)
            .background(
                brush = Brush.radialGradient(
                    colors = listOf(
                        containerColor,
                        containerColor.copy(alpha = 0.8f)
                    )
                ),
                shape = CircleShape
            )
            .border(
                width = 1.dp,
                color = Color.White.copy(alpha = 0.3f),
                shape = CircleShape
            )
            .shadow(
                elevation = 12.dp,
                shape = CircleShape,
                ambientColor = containerColor.copy(alpha = 0.4f),
                spotColor = containerColor.copy(alpha = 0.4f)
            )
            .clickable(
                interactionSource = interactionSource,
                indication = null,
                onClick = onClick
            ),
        contentAlignment = Alignment.Center
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = contentColor,
            modifier = Modifier.size(24.dp)
        )
    }
}

/**
 * Modern Event Card with glassmorphic design
 */
@Composable
fun ModernEventCard(
    title: String,
    subtitle: String,
    imageUrl: String?,
    modifier: Modifier = Modifier,
    onClick: () -> Unit = {},
    badge: String? = null,
    attendeeCount: Int? = null
) {
    ModernLiquidGlassCard(
        modifier = modifier
            .fillMaxWidth()
            .height(280.dp),
        onClick = onClick,
        shape = RoundedCornerShape(20.dp)
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            // Background Image
            AsyncImage(
                model = imageUrl,
                contentDescription = null,
                modifier = Modifier
                    .fillMaxSize()
                    .clip(RoundedCornerShape(20.dp)),
                contentScale = ContentScale.Crop
            )
            
            // Gradient Overlay
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        brush = Brush.verticalGradient(
                            colors = listOf(
                                Color.Black.copy(alpha = 0.1f),
                                Color.Black.copy(alpha = 0.7f)
                            ),
                            startY = 0f,
                            endY = Float.POSITIVE_INFINITY
                        )
                    )
            )
            
            // Content
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(20.dp),
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                // Top Section - Badge
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.Top
                ) {
                    badge?.let {
                        ModernLiquidGlassCard(
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            Text(
                                text = it,
                                style = MaterialTheme.typography.labelMedium,
                                color = Color.White,
                                modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                                fontWeight = FontWeight.SemiBold
                            )
                        }
                    }
                    
                    Icon(
                        imageVector = Icons.Default.FavoriteBorder,
                        contentDescription = "Like",
                        tint = Color.White,
                        modifier = Modifier.size(24.dp)
                    )
                }
                
                // Bottom Section - Info
                Column {
                    Text(
                        text = title,
                        style = MaterialTheme.typography.headlineSmall,
                        color = Color.White,
                        fontWeight = FontWeight.Bold
                    )
                    
                    Spacer(modifier = Modifier.height(4.dp))
                    
                    Text(
                        text = subtitle,
                        style = MaterialTheme.typography.bodyMedium,
                        color = Color.White.copy(alpha = 0.9f)
                    )
                    
                    attendeeCount?.let {
                        Spacer(modifier = Modifier.height(8.dp))
                        Row(
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = Icons.Default.Person,
                                contentDescription = null,
                                tint = Color.White.copy(alpha = 0.8f),
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = "+$it attending",
                                style = MaterialTheme.typography.bodySmall,
                                color = Color.White.copy(alpha = 0.8f)
                            )
                        }
                    }
                }
            }
        }
    }
}

/**
 * Modern Bottom Navigation Bar with glassmorphic effect
 */
@Composable
fun ModernBottomNavigation(
    items: List<ModernBottomNavItem>,
    selectedItem: String,
    onItemSelected: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    ModernLiquidGlassCard(
        modifier = modifier
            .fillMaxWidth()
            .height(80.dp),
        shape = RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {
            items.forEach { item ->
                BottomNavIcon(
                    item = item,
                    isSelected = selectedItem == item.route,
                    onClick = { onItemSelected(item.route) }
                )
            }
        }
    }
}

@Composable
private fun BottomNavIcon(
    item: ModernBottomNavItem,
    
    onClick: () -> Unit
) {
    val animatedScale by animateFloatAsState(
        targetValue = if (isSelected) 1.2f else 1f,
        animationSpec = spring(dampingRatio = 0.7f),
        label = "nav_scale"
    )
    
    val animatedAlpha by animateFloatAsState(
        targetValue = if (isSelected) 1f else 0.6f,
        animationSpec = tween(200),
        label = "nav_alpha"
    )
    
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .clickable(
                indication = null,
                interactionSource = remember { MutableInteractionSource() },
                onClick = onClick
            )
            .padding(8.dp)
    ) {
        Icon(
            imageVector = item.icon,
            contentDescription = item.label,
            tint = if (isSelected) GlassPrimary else GlassOnSurfaceVariant(),
            modifier = Modifier
                .size(24.dp)
                .scale(animatedScale)
                .alpha(animatedAlpha)
        )
        
        if (isSelected) {
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = item.label,
                style = MaterialTheme.typography.labelSmall,
                color = GlassPrimary,
                fontSize = 10.sp,
                fontWeight = FontWeight.Medium
            )
        }
    }
}

data class ModernBottomNavItem(
    val route: String,
    val label: String,
    val 
    val badgeCount: Int = 0
)

// Note: Legacy colors are defined in the theme package to avoid conflicts
