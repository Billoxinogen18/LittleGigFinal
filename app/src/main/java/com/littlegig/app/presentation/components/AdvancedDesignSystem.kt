package com.littlegig.app.presentation.components

import android.content.Context
import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator
import android.os.VibratorManager
import androidx.compose.animation.core.*
import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.ChevronLeft
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.Restaurant
import androidx.compose.material.icons.filled.Hotel
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.MusicNote
import androidx.compose.material.icons.filled.TrendingUp
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Celebration
import androidx.compose.material.icons.filled.School
import androidx.compose.material.icons.filled.PersonAdd
import androidx.compose.material.icons.filled.LocationOff
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.*
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.*
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.littlegig.app.presentation.theme.*
import com.littlegig.app.data.model.UserRank
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*
import org.json.JSONObject
import java.net.URL
import com.littlegig.app.R

// Enhanced haptic feedback and animations
@Composable
fun HapticButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    content: @Composable () -> Unit
) {
    val context = LocalContext.current
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()
    
    val scale by animateFloatAsState(
        targetValue = if (isPressed) 0.95f else 1f,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessLow
        )
    )
    
    Box(
        modifier = modifier
            .scale(scale)
            .pointerInput(Unit) {
                detectTapGestures(
                    onTap = {
                        // Haptic feedback with error handling
                        try {
                            val vibrator = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                                val vibratorManager = context.getSystemService(Context.VIBRATOR_MANAGER_SERVICE) as VibratorManager
                                vibratorManager.defaultVibrator
                            } else {
                                @Suppress("DEPRECATION")
                                context.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
                            }
                            
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                                vibrator.vibrate(VibrationEffect.createOneShot(50, VibrationEffect.DEFAULT_AMPLITUDE))
                            } else {
                                @Suppress("DEPRECATION")
                                vibrator.vibrate(50)
                            }
                        } catch (e: SecurityException) {
                            // Permission denied, continue without vibration
                        } catch (e: Exception) {
                            // Any other error, continue without vibration
                        }
                        
                        onClick()
                    }
                )
            },
        contentAlignment = Alignment.Center
    ) {
        content()
    }
}

@Composable
fun LoadingPulseAnimation(
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) {
    val infiniteTransition = rememberInfiniteTransition()
    val alpha by infiniteTransition.animateFloat(
        initialValue = 0.3f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(1000, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        )
    )
    
    Box(
        modifier = modifier.graphicsLayer(alpha = alpha),
        contentAlignment = Alignment.Center
    ) {
        content()
    }
}

@Composable
fun ShimmerLoadingCard(
    modifier: Modifier = Modifier
) {
    val infiniteTransition = rememberInfiniteTransition()
    val shimmerOffset by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(1500, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        )
    )
    
    AdvancedNeumorphicCard(
        modifier = modifier
            .fillMaxWidth()
            .height(200.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.linearGradient(
                        colors = listOf(
                            Color.Transparent,
                            Color.White.copy(alpha = 0.1f),
                            Color.Transparent
                        ),
                        start = Offset(shimmerOffset * 1000f, 0f),
                        end = Offset((shimmerOffset + 0.3f) * 1000f, 100f)
                    )
                )
        )
    }
}

@Composable
fun FloatingActionButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    icon: @Composable () -> Unit
) {
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()
    
    val scale by animateFloatAsState(
        targetValue = if (isPressed) 0.9f else 1f,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessLow
        )
    )
    
    HapticButton(
        onClick = onClick,
        modifier = modifier
            .scale(scale)
            .size(56.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .drawBehind {
                    drawCircle(
                        brush = Brush.radialGradient(
                            colors = listOf(
                                LittleGigPrimary.copy(alpha = 0.8f),
                                LittleGigPrimary.copy(alpha = 0.6f),
                                LittleGigPrimary.copy(alpha = 0.4f)
                            )
                        ),
                        radius = size.minDimension / 2
                    )
                }
                .clip(CircleShape)
                .background(
                    Brush.radialGradient(
                        colors = listOf(
                            LittleGigPrimary,
                            LittleGigPrimary.copy(alpha = 0.8f)
                        )
                    )
                ),
            contentAlignment = Alignment.Center
        ) {
            icon()
        }
    }
}

// Liquid Glass Bottom Navigation - Proper glassmorphism with neumorphic effects
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
        BottomNavItem("chat", "Chat", Icons.Default.Chat),
        BottomNavItem("account", "Account", Icons.Default.Person)
    )
    
            // Elevated floating bottom navigation with proper glassmorphism
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp)
                .padding(bottom = 38.dp) // Increased elevation from bottom (24 + 14)
        ) {
        // Frosted glass background with proper neumorphic effects
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .height(88.dp)
                .clip(RoundedCornerShape(28.dp))
                .drawBehind {
                    // Neumorphic shadow for elevation
                    drawRoundRect(
                        color = if (isDark) Color.Black.copy(alpha = 0.5f) else Color.Black.copy(alpha = 0.2f),
                        topLeft = Offset(0f, 8.dp.toPx()),
                        size = size,
                        cornerRadius = CornerRadius(28.dp.toPx())
                    )
                    
                    // Frosted glass edge effects with gradient
                    drawRoundRect(
                        brush = Brush.verticalGradient(
                            colors = listOf(
                                if (isDark) Color.White.copy(alpha = 0.2f) else Color.White.copy(alpha = 0.4f),
                                if (isDark) Color.White.copy(alpha = 0.1f) else Color.White.copy(alpha = 0.2f),
                                Color.Transparent,
                                if (isDark) Color.White.copy(alpha = 0.05f) else Color.White.copy(alpha = 0.1f),
                                if (isDark) Color.White.copy(alpha = 0.15f) else Color.White.copy(alpha = 0.3f)
                            )
                        ),
                        size = size,
                        cornerRadius = CornerRadius(28.dp.toPx())
                    )
                },
            shape = RoundedCornerShape(28.dp),
            color = if (isDark) Color(0xFF141B2E).copy(alpha = 0.85f) else Color(0xFFFFFFFF).copy(alpha = 0.9f),
            border = BorderStroke(
                width = 1.dp,
                color = if (isDark) Color.White.copy(alpha = 0.15f) else Color.Black.copy(alpha = 0.08f)
            )
        ) {
            // Inner frosted glass effect with blur
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        if (isDark) Color(0xFF1A2332).copy(alpha = 0.7f) else Color(0xFFF8FAFF).copy(alpha = 0.8f),
                        RoundedCornerShape(28.dp)
                    )
            )
            
            // Navigation items with proper spacing
            Row(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 28.dp, vertical = 16.dp),
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.CenterVertically
            ) {
                items.forEach { item ->
                    Box {
                        LiquidGlassNavItem(
                            item = item,
                            isSelected = currentRoute == item.route,
                            onClick = { onNavigate(item.route) }
                        )
                        if (item.route == "inbox" && inboxUnreadCount > 0) {
                            Box(
                                modifier = Modifier
                                    .align(Alignment.TopEnd)
                                    .offset(x = 8.dp, y = (-4).dp)
                                    .clip(RoundedCornerShape(8.dp))
                                    .background(LittleGigPrimary)
                            ) {
                                Text(
                                    text = inboxUnreadCount.coerceAtMost(99).toString(),
                                    color = Color.White,
                                    style = MaterialTheme.typography.labelSmall,
                                    modifier = Modifier.padding(horizontal = 4.dp, vertical = 1.dp)
                                )
                            }
                        }
                    }
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
        targetValue = if (isSelected) 1.08f else 1f,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessLow
        ),
        label = "nav_scale"
    )
    
    Box(
        modifier = Modifier
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = rememberRipple(
                    color = if (isDark) Color.White.copy(alpha = 0.15f) else Color.Black.copy(alpha = 0.06f),
                    bounded = false,
                    radius = 28.dp
                )
            ) { onClick() }
            .padding(8.dp)
            .scale(animatedScale)
            .drawBehind {
                if (isSelected) {
                    // Selected background with neumorphic effect
                    drawRoundRect(
                        color = if (isDark) Color.White.copy(alpha = 0.12f) else Color.Black.copy(alpha = 0.06f),
                        cornerRadius = CornerRadius(20.dp.toPx())
                    )
                    
                    // Inner glow for selected state
                    drawRoundRect(
                        color = LittleGigPrimary.copy(alpha = 0.3f),
                        cornerRadius = CornerRadius(20.dp.toPx())
                    )
                }
            },
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier
                .padding(horizontal = 12.dp, vertical = 8.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Icon(
                imageVector = item.icon,
                contentDescription = item.label,
                tint = if (isSelected) {
                    LittleGigPrimary
                } else {
                    if (isDark) Color.White.copy(alpha = 0.95f) else Color.Black.copy(alpha = 0.85f)
                },
                modifier = Modifier.size(24.dp)
            )
            
            Spacer(modifier = Modifier.height(4.dp))
            
            Text(
                text = item.label,
                style = MaterialTheme.typography.labelSmall.copy(
                    fontSize = MaterialTheme.typography.labelSmall.fontSize * 0.85f
                ),
                color = if (isSelected) {
                    LittleGigPrimary
                } else {
                    if (isDark) Color.White.copy(alpha = 0.95f) else Color.Black.copy(alpha = 0.85f)
                },
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}

// Advanced Neumorphic Card with proper depth and glassmorphism
@Composable
fun AdvancedNeumorphicCard(
    modifier: Modifier = Modifier,
    onClick: (() -> Unit)? = null,
    cornerRadius: Dp = 24.dp,
    content: @Composable () -> Unit
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
        modifier = modifier
            .clip(RoundedCornerShape(cornerRadius))
            .then(
                if (onClick != null) {
                    Modifier.clickable(
                        interactionSource = remember { MutableInteractionSource() },
                        indication = rememberRipple(color = LittleGigPrimary.copy(alpha = 0.1f))
                    ) { 
                        pressed = true
                        onClick()
                        CoroutineScope(Dispatchers.Main).launch {
                            delay(150)
                            pressed = false
                        }
                    }
                } else Modifier
            )
            .drawBehind {
                drawAdvancedNeumorphicShadow(
                    isDark = isDark,
                    pressed = animatedPressed > 0.5f,
                    cornerRadius = cornerRadius.toPx()
                )
            },
        shape = RoundedCornerShape(cornerRadius),
        color = if (isDark) Color(0xFF141B2E) else Color(0xFFF8FAFF),
        border = BorderStroke(
            width = 0.5.dp,
            color = if (isDark) Color.White.copy(alpha = 0.1f) else Color.Black.copy(alpha = 0.05f)
        )
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    if (isDark) Color(0xFF1A2332).copy(alpha = 0.8f) else Color(0xFFFFFFFF).copy(alpha = 0.9f)
                )
        ) {
            content()
        }
    }
}

// Glassmorphic Card with proper frosted glass effects
@Composable
fun AdvancedGlassmorphicCard(
    modifier: Modifier = Modifier,
    onClick: (() -> Unit)? = null,
    cornerRadius: Dp = 24.dp,
    content: @Composable () -> Unit
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
            )
            .drawBehind {
                // Frosted glass effect with gradient
                drawRoundRect(
                    brush = Brush.verticalGradient(
                        colors = listOf(
                            if (isDark) Color.White.copy(alpha = 0.15f) else Color.White.copy(alpha = 0.3f),
                            if (isDark) Color.White.copy(alpha = 0.08f) else Color.White.copy(alpha = 0.15f),
                            Color.Transparent,
                            if (isDark) Color.White.copy(alpha = 0.05f) else Color.White.copy(alpha = 0.1f),
                            if (isDark) Color.White.copy(alpha = 0.12f) else Color.White.copy(alpha = 0.25f)
                        )
                    ),
                    size = size,
                    cornerRadius = CornerRadius(cornerRadius.toPx())
                )
            },
        shape = RoundedCornerShape(cornerRadius),
        color = if (isDark) Color(0xFF141B2E).copy(alpha = 0.7f) else Color(0xFFFFFFFF).copy(alpha = 0.8f),
        border = BorderStroke(
            width = 1.dp,
            color = if (isDark) Color.White.copy(alpha = 0.2f) else Color.Black.copy(alpha = 0.1f)
        )
    ) {
        content()
    }
}

// Extension function to draw advanced neumorphic shadows
private fun DrawScope.drawAdvancedNeumorphicShadow(
    isDark: Boolean,
    pressed: Boolean,
    cornerRadius: Float
) {
    val shadowOffset = if (pressed) 2.dp.toPx() else 8.dp.toPx()
    
    val lightShadow = if (isDark) Color.White.copy(alpha = 0.12f) else Color.White.copy(alpha = 0.9f)
    val darkShadow = if (isDark) Color.Black.copy(alpha = 0.5f) else Color.Black.copy(alpha = 0.2f)
    
    // Dark shadow (bottom-right) for depth
    drawRoundRect(
        color = darkShadow,
        topLeft = Offset(shadowOffset, shadowOffset),
        size = size,
        cornerRadius = CornerRadius(cornerRadius)
    )
    
    // Light shadow (top-left) for elevation
    drawRoundRect(
        color = lightShadow,
        topLeft = Offset(-shadowOffset, -shadowOffset),
        size = size,
        cornerRadius = CornerRadius(cornerRadius)
    )
}

// Beautiful Custom Date & Time Picker with Neumorphic Design
@Composable
fun NeumorphicDatePicker(
    selectedDate: Date?,
    onDateSelected: (Date) -> Unit,
    modifier: Modifier = Modifier
) {
    val isDark = isSystemInDarkTheme()
    var showDatePicker by remember { mutableStateOf(false) }
    
    val dateFormatter = remember { SimpleDateFormat("MMM dd, yyyy 'at' HH:mm", Locale.getDefault()) }
    
    AdvancedNeumorphicCard(
        modifier = modifier,
        onClick = { showDatePicker = true }
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Default.DateRange,
                contentDescription = null,
                tint = LittleGigPrimary,
                modifier = Modifier.size(20.dp)
            )
            
            Spacer(modifier = Modifier.width(12.dp))
            
            Text(
                text = selectedDate?.let { dateFormatter.format(it) } ?: "Select Date & Time",
                style = MaterialTheme.typography.bodyMedium,
                color = if (selectedDate != null) {
                    MaterialTheme.colorScheme.onSurface
                } else {
                    MaterialTheme.colorScheme.onSurfaceVariant
                }
            )
            
            Spacer(modifier = Modifier.weight(1f))
            
            Icon(
                imageVector = Icons.Default.KeyboardArrowDown,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.size(20.dp)
            )
        }
    }
    
    if (showDatePicker) {
        NeumorphicDateTimePickerDialog(
            onDismiss = { showDatePicker = false },
            onDateSelected = { date ->
                onDateSelected(date)
                showDatePicker = false
            },
            selectedDate = selectedDate
        )
    }
}

@Composable
private fun NeumorphicDateTimePickerDialog(
    onDismiss: () -> Unit,
    onDateSelected: (Date) -> Unit,
    selectedDate: Date?
) {
    val isDark = isSystemInDarkTheme()
    
    var currentMonth by remember { mutableStateOf(Calendar.getInstance().get(Calendar.MONTH)) }
    var currentYear by remember { mutableStateOf(Calendar.getInstance().get(Calendar.YEAR)) }
    var selectedHour by remember { mutableStateOf(Calendar.getInstance().get(Calendar.HOUR_OF_DAY)) }
    var selectedMinute by remember { mutableStateOf(Calendar.getInstance().get(Calendar.MINUTE)) }
    var selectedDay by remember { mutableStateOf(Calendar.getInstance().get(Calendar.DAY_OF_MONTH)) }
    var showTimePicker by remember { mutableStateOf(false) }
    
    // Initialize with selected date if available
    LaunchedEffect(selectedDate) {
        selectedDate?.let { date ->
            val calendar = Calendar.getInstance().apply { time = date }
            currentMonth = calendar.get(Calendar.MONTH)
            currentYear = calendar.get(Calendar.YEAR)
            selectedHour = calendar.get(Calendar.HOUR_OF_DAY)
            selectedMinute = calendar.get(Calendar.MINUTE)
            selectedDay = calendar.get(Calendar.DAY_OF_MONTH)
        }
    }
    
    Dialog(onDismissRequest = onDismiss) {
        AdvancedGlassmorphicCard(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Column(
                modifier = Modifier.padding(24.dp)
            ) {
                Text(
                    text = "Select Date",
                    style = MaterialTheme.typography.headlineSmall.copy(
                        fontWeight = FontWeight.Bold
                    ),
                    color = MaterialTheme.colorScheme.onSurface
                )
                
                Spacer(modifier = Modifier.height(20.dp))
                
                // Month/Year Header with Year Selection
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(
                        onClick = {
                            if (currentMonth == 0) {
                                currentMonth = 11
                                currentYear--
                            } else {
                                currentMonth--
                            }
                        }
                    ) {
                        Icon(
                            imageVector = Icons.Default.ChevronLeft,
                            contentDescription = "Previous Month",
                            tint = LittleGigPrimary
                        )
                    }
                    
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = Calendar.getInstance().apply { set(currentYear, currentMonth, 1) }.getDisplayName(Calendar.MONTH, Calendar.LONG, Locale.getDefault()),
                            style = MaterialTheme.typography.titleMedium.copy(
                                fontWeight = FontWeight.SemiBold
                            ),
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        
                        // Year Selection with Dropdown
                        var showYearPicker by remember { mutableStateOf(false) }
                        
                        Box {
                            Row(
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = currentYear.toString(),
                                    style = MaterialTheme.typography.bodyMedium.copy(
                                        fontWeight = FontWeight.Medium
                                    ),
                                    color = MaterialTheme.colorScheme.onSurface,
                                    modifier = Modifier.padding(horizontal = 8.dp)
                                )
                                
                                IconButton(
                                    onClick = { showYearPicker = true },
                                    modifier = Modifier.size(24.dp)
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.KeyboardArrowDown,
                                        contentDescription = "Select Year",
                                        tint = LittleGigPrimary,
                                        modifier = Modifier.size(16.dp)
                                    )
                                }
                            }
                            
                            if (showYearPicker) {
                                DropdownMenu(
                                    expanded = showYearPicker,
                                    onDismissRequest = { showYearPicker = false },
                                    modifier = Modifier
                                        .background(
                                            if (isDark) Color(0xFF1A2332) else Color(0xFFF8FAFF),
                                            RoundedCornerShape(12.dp)
                                        )
                                        .border(
                                            1.dp,
                                            if (isDark) Color.White.copy(alpha = 0.1f) else Color.Black.copy(alpha = 0.1f),
                                            RoundedCornerShape(12.dp)
                                        )
                                ) {
                                    val currentYearNow = Calendar.getInstance().get(Calendar.YEAR)
                                    (currentYearNow - 5..currentYearNow + 5).forEach { year ->
                                        DropdownMenuItem(
                                            text = {
                                                Text(
                                                    text = year.toString(),
                                                    color = if (year == currentYear) LittleGigPrimary else MaterialTheme.colorScheme.onSurface
                                                )
                                            },
                                            onClick = {
                                                currentYear = year
                                                showYearPicker = false
                                            }
                                        )
                                    }
                                }
                            }
                        }
                    }
                    
                    IconButton(
                        onClick = {
                            if (currentMonth == 11) {
                                currentMonth = 0
                                currentYear++
                            } else {
                                currentMonth++
                            }
                        }
                    ) {
                        Icon(
                            imageVector = Icons.Default.ChevronRight,
                            contentDescription = "Next Month",
                            tint = LittleGigPrimary
                        )
                    }
                }
                
                Spacer(modifier = Modifier.height(16.dp))
                
                // Day Headers
                Row(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    listOf("Sun", "Mon", "Tue", "Wed", "Thu", "Fri", "Sat").forEach { day ->
                        Text(
                            text = day,
                            modifier = Modifier.weight(1f),
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            textAlign = androidx.compose.ui.text.style.TextAlign.Center
                        )
                    }
                }
                
                Spacer(modifier = Modifier.height(8.dp))
                
                // Calendar Grid
                val firstDayOfMonth = Calendar.getInstance().apply {
                    set(currentYear, currentMonth, 1)
                }
                val lastDayOfMonth = Calendar.getInstance().apply {
                    set(currentYear, currentMonth, 1)
                    add(Calendar.MONTH, 1)
                    add(Calendar.DAY_OF_MONTH, -1)
                }
                
                val daysInMonth = lastDayOfMonth.get(Calendar.DAY_OF_MONTH)
                val firstDayOfWeek = firstDayOfMonth.get(Calendar.DAY_OF_WEEK) - 1
                
                LazyVerticalGrid(
                    columns = GridCells.Fixed(7),
                    horizontalArrangement = Arrangement.spacedBy(4.dp),
                    verticalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    // Empty cells for days before the first day of the month
                    items(firstDayOfWeek) {
                        Box(modifier = Modifier.size(40.dp))
                    }
                    
                    // Days of the month
                    items(daysInMonth) { day ->
                        val dayNumber = day + 1
                        val isSelected = selectedDate?.let { selected ->
                            val selectedCal = Calendar.getInstance().apply { time = selected }
                            selectedCal.get(Calendar.YEAR) == currentYear &&
                            selectedCal.get(Calendar.MONTH) == currentMonth &&
                            selectedCal.get(Calendar.DAY_OF_MONTH) == dayNumber
                        } ?: false
                        
                        val isToday = Calendar.getInstance().run {
                            get(Calendar.YEAR) == currentYear &&
                            get(Calendar.MONTH) == currentMonth &&
                            get(Calendar.DAY_OF_MONTH) == dayNumber
                        }
                        
                        Surface(
                            onClick = {
                                val newDate = Calendar.getInstance().apply {
                                    set(currentYear, currentMonth, dayNumber)
                                }.time
                                onDateSelected(newDate)
                            },
                            modifier = Modifier.size(40.dp),
                            shape = CircleShape,
                            color = when {
                                isSelected -> LittleGigPrimary
                                isToday -> LittleGigPrimary.copy(alpha = 0.2f)
                                else -> Color.Transparent
                            },
                            border = if (isToday && !isSelected) {
                                androidx.compose.foundation.BorderStroke(
                                    1.dp,
                                    LittleGigPrimary
                                )
                            } else null
                        ) {
                            Box(
                                modifier = Modifier.fillMaxSize(),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = dayNumber.toString(),
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = when {
                                        isSelected -> Color.White
                                        isToday -> LittleGigPrimary
                                        else -> MaterialTheme.colorScheme.onSurface
                                    }
                                )
                            }
                        }
                    }
                }
                
                Spacer(modifier = Modifier.height(20.dp))
                
                // Time Selection
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Time",
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.SemiBold
                        ),
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        // Hour Picker
                        AdvancedNeumorphicCard(
                            modifier = Modifier.size(60.dp, 40.dp),
                            onClick = { showTimePicker = true }
                        ) {
                            Box(
                                modifier = Modifier.fillMaxSize(),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = String.format("%02d", selectedHour),
                                    style = MaterialTheme.typography.titleMedium.copy(
                                        fontWeight = FontWeight.Bold
                                    ),
                                    color = MaterialTheme.colorScheme.onSurface
                                )
                            }
                        }
                        
                        Text(
                            text = ":",
                            style = MaterialTheme.typography.titleLarge.copy(
                                fontWeight = FontWeight.Bold
                            ),
                            color = MaterialTheme.colorScheme.onSurface,
                            modifier = Modifier.padding(horizontal = 8.dp)
                        )
                        
                        // Minute Picker
                        AdvancedNeumorphicCard(
                            modifier = Modifier.size(60.dp, 40.dp),
                            onClick = { showTimePicker = true }
                        ) {
                            Box(
                                modifier = Modifier.fillMaxSize(),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = String.format("%02d", selectedMinute),
                                    style = MaterialTheme.typography.titleMedium.copy(
                                        fontWeight = FontWeight.Bold
                                    ),
                                    color = MaterialTheme.colorScheme.onSurface
                                )
                            }
                        }
                    }
                }
                
                Spacer(modifier = Modifier.height(20.dp))
                
                // Confirm Button
                Button(
                    onClick = {
                        val calendar = Calendar.getInstance().apply {
                            set(currentYear, currentMonth, selectedDay, selectedHour, selectedMinute)
                        }
                        onDateSelected(calendar.time)
                    },
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = LittleGigPrimary
                    )
                ) {
                    Icon(
                        imageVector = Icons.Default.Check,
                        contentDescription = null,
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Confirm Date & Time",
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.Bold
                        )
                    )
                }
                
                Spacer(modifier = Modifier.height(20.dp))
                
                // Action Buttons
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    OutlinedButton(
                        onClick = onDismiss,
                        modifier = Modifier.weight(1f)
                    ) {
                        Text("Cancel")
                    }
                    
                    Button(
                        onClick = {
                            selectedDate?.let { onDateSelected(it) }
                            onDismiss()
                        },
                        modifier = Modifier.weight(1f),
                        enabled = selectedDate != null
                    ) {
                        Text("Confirm")
                    }
                }
            }
        }
    }
    
    // Time Picker Dialog
    if (showTimePicker) {
        Dialog(onDismissRequest = { showTimePicker = false }) {
            AdvancedGlassmorphicCard(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                Column(
                    modifier = Modifier.padding(24.dp)
                ) {
                    Text(
                        text = "Select Time",
                        style = MaterialTheme.typography.headlineSmall.copy(
                            fontWeight = FontWeight.Bold
                        ),
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    
                    Spacer(modifier = Modifier.height(20.dp))
                    
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceEvenly,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        // Hour Picker
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(
                                text = "Hour",
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                            
                            Spacer(modifier = Modifier.height(8.dp))
                            
                            LazyColumn(
                                modifier = Modifier.height(120.dp),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                items(24) { hour ->
                                    Surface(
                                        onClick = { selectedHour = hour },
                                        modifier = Modifier
                                            .size(50.dp, 30.dp)
                                            .padding(vertical = 2.dp),
                                        shape = RoundedCornerShape(8.dp),
                                        color = if (hour == selectedHour) LittleGigPrimary else Color.Transparent
                                    ) {
                                        Box(
                                            modifier = Modifier.fillMaxSize(),
                                            contentAlignment = Alignment.Center
                                        ) {
                                            Text(
                                                text = String.format("%02d", hour),
                                                style = MaterialTheme.typography.bodyMedium,
                                                color = if (hour == selectedHour) Color.White else MaterialTheme.colorScheme.onSurface
                                            )
                                        }
                                    }
                                }
                            }
                        }
                        
                        Text(
                            text = ":",
                            style = MaterialTheme.typography.titleLarge.copy(
                                fontWeight = FontWeight.Bold
                            ),
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        
                        // Minute Picker
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(
                                text = "Minute",
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                            
                            Spacer(modifier = Modifier.height(8.dp))
                            
                            LazyColumn(
                                modifier = Modifier.height(120.dp),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                items(60) { minute ->
                                    Surface(
                                        onClick = { selectedMinute = minute },
                                        modifier = Modifier
                                            .size(50.dp, 30.dp)
                                            .padding(vertical = 2.dp),
                                        shape = RoundedCornerShape(8.dp),
                                        color = if (minute == selectedMinute) LittleGigPrimary else Color.Transparent
                                    ) {
                                        Box(
                                            modifier = Modifier.fillMaxSize(),
                                            contentAlignment = Alignment.Center
                                        ) {
                                            Text(
                                                text = String.format("%02d", minute),
                                                style = MaterialTheme.typography.bodyMedium,
                                                color = if (minute == selectedMinute) Color.White else MaterialTheme.colorScheme.onSurface
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    }
                    
                    Spacer(modifier = Modifier.height(20.dp))
                    
                    Button(
                        onClick = { showTimePicker = false },
                        modifier = Modifier.fillMaxWidth(),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = LittleGigPrimary
                        )
                    ) {
                        Text(
                            text = "Confirm Time",
                            style = MaterialTheme.typography.titleMedium.copy(
                                fontWeight = FontWeight.Bold
                            )
                        )
                    }
                }
            }
        }
    }
}

// Beautiful Places Autocomplete with Neumorphic Design
@Composable
fun NeumorphicPlacesAutocomplete(
    query: String,
    onQueryChange: (String) -> Unit,
    suggestions: List<PlaceSuggestion>,
    onPlaceSelected: (PlaceSuggestion) -> Unit,
    modifier: Modifier = Modifier
) {
    val isDark = isSystemInDarkTheme()
    var showSuggestions by remember { mutableStateOf(false) }
    
    Column(modifier = modifier) {
        // Search Input with Neumorphic Design
        AdvancedGlassmorphicCard {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Default.LocationOn,
                    contentDescription = null,
                    tint = LittleGigPrimary,
                    modifier = Modifier.size(20.dp)
                )
                
                Spacer(modifier = Modifier.width(12.dp))
                
                OutlinedTextField(
                    value = query,
                    onValueChange = { 
                        onQueryChange(it)
                        showSuggestions = it.isNotBlank()
                    },
                    placeholder = { 
                        Text(
                            "Search for places...",
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true,
                    trailingIcon = {
                        if (query.isNotBlank()) {
                            IconButton(onClick = {
                                onQueryChange("")
                                showSuggestions = false
                            }) {
                                Icon(imageVector = Icons.Default.Close, contentDescription = "Clear")
                            }
                        }
                    },
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = Color.Transparent,
                        unfocusedBorderColor = Color.Transparent,
                        focusedContainerColor = Color.Transparent,
                        unfocusedContainerColor = Color.Transparent
                    )
                )
            }
        }
        
        // Suggestions Dropdown
        if (showSuggestions && query.isNotBlank() && suggestions.isNotEmpty()) {
            Spacer(modifier = Modifier.height(8.dp))
            
            AdvancedGlassmorphicCard {
                LazyColumn(
                    modifier = Modifier.heightIn(max = 200.dp)
                ) {
                    items(suggestions) { suggestion ->
                        Surface(
                            onClick = {
                                onPlaceSelected(suggestion)
                                showSuggestions = false
                            },
                            modifier = Modifier.fillMaxWidth(),
                            color = Color.Transparent
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(16.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    imageVector = when {
                                        suggestion.types.contains("restaurant") -> Icons.Default.Restaurant
                                        suggestion.types.contains("hotel") -> Icons.Default.Hotel
                                        suggestion.types.contains("shopping") -> Icons.Default.ShoppingCart
                                        else -> Icons.Default.LocationOn
                                    },
                                    contentDescription = null,
                                    tint = LittleGigPrimary,
                                    modifier = Modifier.size(20.dp)
                                )
                                
                                Spacer(modifier = Modifier.width(12.dp))
                                
                                Column(modifier = Modifier.weight(1f)) {
                                    Text(
                                        text = suggestion.name,
                                        style = MaterialTheme.typography.bodyMedium.copy(
                                            fontWeight = FontWeight.Medium
                                        ),
                                        color = MaterialTheme.colorScheme.onSurface
                                    )
                                    
                                    Text(
                                        text = suggestion.address,
                                        style = MaterialTheme.typography.bodySmall,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun MiniMapPreview(latitude: Double, longitude: Double, modifier: Modifier = Modifier) {
    // Placeholder composable area for mini map preview; the actual MapView can be integrated later
    AdvancedGlassmorphicCard(modifier = modifier) {
        Column(Modifier.padding(12.dp), horizontalAlignment = Alignment.Start) {
            Text(text = "Selected location", style = MaterialTheme.typography.labelMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
            Spacer(Modifier.height(6.dp))
            Text(text = "lat: %.5f, lon: %.5f".format(latitude, longitude), style = MaterialTheme.typography.bodySmall)
        }
    }
}

// Data classes for Places API
data class PlaceSuggestion(
    val placeId: String,
    val name: String,
    val address: String,
    val latitude: Double,
    val longitude: Double,
    val types: List<String> = emptyList()
)

// Beautiful Rank Badge with Glow Effects
@Composable
fun NeumorphicRankBadge(
    rank: UserRank,
    modifier: Modifier = Modifier
) {
    val isDark = isSystemInDarkTheme()
    
    val rankColor = when (rank) {
        UserRank.SUPERSTAR -> Color(0xFFFFD700) // Gold
        UserRank.ROCKSTAR -> Color(0xFFE91E63) // Pink
        UserRank.FAMOUS -> Color(0xFF9C27B0) // Purple
        UserRank.INFLUENCER -> Color(0xFF2196F3) // Blue
        UserRank.POPULAR -> Color(0xFF4CAF50) // Green
        UserRank.PARTY_POPPER -> Color(0xFFFF9800) // Orange
        UserRank.BEGINNER -> Color(0xFF607D8B) // Blue Grey
        UserRank.NOVICE -> Color(0xFF9E9E9E) // Grey
    }
    
    val rankText = when (rank) {
        UserRank.SUPERSTAR -> "SUPERSTAR"
        UserRank.ROCKSTAR -> "ROCKSTAR"
        UserRank.FAMOUS -> "FAMOUS"
        UserRank.INFLUENCER -> "INFLUENCER"
        UserRank.POPULAR -> "POPULAR"
        UserRank.PARTY_POPPER -> "PARTY POPPER"
        UserRank.BEGINNER -> "BEGINNER"
        UserRank.NOVICE -> "NOVICE"
    }
    
    Surface(
        modifier = modifier
            .clip(RoundedCornerShape(20.dp))
            .drawBehind {
                // Glow effect
                drawRoundRect(
                    color = rankColor.copy(alpha = 0.3f),
                    cornerRadius = CornerRadius(20.dp.toPx())
                )
                // Inner glow
                drawRoundRect(
                    color = rankColor.copy(alpha = 0.1f),
                    cornerRadius = CornerRadius(20.dp.toPx())
                )
            },
        shape = RoundedCornerShape(20.dp),
        color = rankColor.copy(alpha = 0.1f),
        border = BorderStroke(
            width = 1.dp,
            color = rankColor.copy(alpha = 0.5f)
        )
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Rank icon
            Icon(
                imageVector = when (rank) {
                    UserRank.SUPERSTAR -> Icons.Default.Star
                    UserRank.ROCKSTAR -> Icons.Default.MusicNote
                    UserRank.FAMOUS -> Icons.Default.Person
                    UserRank.INFLUENCER -> Icons.Default.TrendingUp
                    UserRank.POPULAR -> Icons.Default.Favorite
                    UserRank.PARTY_POPPER -> Icons.Default.Celebration
                    UserRank.BEGINNER -> Icons.Default.School
                    UserRank.NOVICE -> Icons.Default.PersonAdd
                },
                contentDescription = null,
                tint = rankColor,
                modifier = Modifier.size(16.dp)
            )
            
            Spacer(modifier = Modifier.width(6.dp))
            
            Text(
                text = rankText,
                style = MaterialTheme.typography.labelSmall.copy(
                    fontWeight = FontWeight.Bold
                ),
                color = rankColor
            )
        }
    }
}

// Beautiful Floating Active Now Toggle
@Composable
fun NeumorphicActiveNowToggle(
    isActive: Boolean,
    onToggle: (Boolean) -> Unit,
    modifier: Modifier = Modifier
) {
    val isDark = isSystemInDarkTheme()
    
    val animatedScale by animateFloatAsState(
        targetValue = if (isActive) 1.1f else 1f,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessLow
        ),
        label = "toggle_scale"
    )
    
    val animatedColor by animateColorAsState(
        targetValue = if (isActive) {
            Color(0xFF4CAF50) // Green when active
        } else {
            MaterialTheme.colorScheme.onSurfaceVariant
        },
        animationSpec = tween(300),
        label = "toggle_color"
    )
    
    Surface(
        onClick = { onToggle(!isActive) },
        modifier = modifier
            .graphicsLayer {
                scaleX = animatedScale
                scaleY = animatedScale
            }
            .size(56.dp)
            .clip(CircleShape)
            .drawBehind {
                // Neumorphic shadow
                drawCircle(
                    color = if (isDark) Color.Black.copy(alpha = 0.4f) else Color.Black.copy(alpha = 0.2f),
                    center = Offset(center.x + 4.dp.toPx(), center.y + 4.dp.toPx()),
                    radius = 28.dp.toPx()
                )
                // Inner glow
                drawCircle(
                    color = if (isActive) {
                        Color(0xFF4CAF50).copy(alpha = 0.3f)
                    } else {
                        if (isDark) Color.White.copy(alpha = 0.1f) else Color.White.copy(alpha = 0.6f)
                    },
                    center = Offset(center.x - 4.dp.toPx(), center.y - 4.dp.toPx()),
                    radius = 28.dp.toPx()
                )
            },
        shape = CircleShape,
        color = if (isActive) {
            Color(0xFF4CAF50).copy(alpha = 0.2f)
        } else {
            if (isDark) Color(0xFF2A2A2A) else Color(0xFFF5F5F5)
        },
        border = BorderStroke(
            width = 2.dp,
            color = animatedColor
        )
    ) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = if (isActive) Icons.Default.LocationOn else Icons.Default.LocationOff,
                contentDescription = if (isActive) "Active Now" else "Inactive",
                tint = animatedColor,
                modifier = Modifier.size(24.dp)
            )
        }
    }
}

 