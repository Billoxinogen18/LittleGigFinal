package com.littlegig.app.presentation.auth

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.littlegig.app.data.model.UserType
import com.littlegig.app.presentation.components.*
import com.littlegig.app.presentation.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AuthScreen(
    onAuthSuccess: () -> Unit,
    onGoogleSignIn: () -> Unit = {},
    viewModel: AuthViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState(initial = AuthUiState())
    val currentUser by viewModel.currentUser.collectAsState(initial = null)
    val isDark = isSystemInDarkTheme()
    
    var isSignUp by remember { mutableStateOf(false) }
    var phoneNumber by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var displayName by remember { mutableStateOf("") }
    var userType by remember { mutableStateOf(UserType.REGULAR) }
    var passwordVisible by remember { mutableStateOf(false) }
    var isPhoneAuth by remember { mutableStateOf(true) }
    
    // Navigate to main screen when user is authenticated
    LaunchedEffect(currentUser) {
        if (currentUser != null) {
            onAuthSuccess()
        }
    }
    
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colors = if (isDark) listOf(
                        Color(0xFF1A1A1A), // Charcoal grey
                        Color(0xFF2D2D2D), // Lighter charcoal
                        Color(0xFF404040)  // Even lighter charcoal
                    ) else listOf(
                        Color(0xFFF8FAFF),
                        Color(0xFFE2E8F0),
                        Color(0xFFCBD5E1)
                    )
                )
            )
    ) {
        // Animated background orbs
        FloatingOrbs()
        
        // Liquid glass background
        LiquidGlassBackground()
        
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(60.dp))
            
            // Animated logo and branding
            AnimatedLogoSection()
            
            Spacer(modifier = Modifier.height(32.dp))
            
            // Auth method selector
            LiquidGlassCard(
                modifier = Modifier.fillMaxWidth(),
                glowEffect = true
            ) {
                Column(
                    modifier = Modifier.padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "Choose Sign In Method",
                        style = MaterialTheme.typography.headlineSmall.copy(
                            fontWeight = FontWeight.Bold
                        ),
                        color = if (isDark) Color.White else Color.Black,
                        textAlign = TextAlign.Center
                    )
                    
                    Spacer(modifier = Modifier.height(16.dp))
                    
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        NeumorphicButton(
                            onClick = { isPhoneAuth = true },
                            modifier = Modifier.weight(1f)
                        ) {
                            Icon(
                                Icons.Default.Phone,
                                contentDescription = null,
                                modifier = Modifier.size(20.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("Phone")
                        }
                        
                        NeumorphicButton(
                            onClick = { isPhoneAuth = false },
                            modifier = Modifier.weight(1f)
                        ) {
                            Icon(
                                Icons.Default.Email,
                                contentDescription = null,
                                modifier = Modifier.size(20.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("Email")
                        }
                    }
                }
            }
            
            Spacer(modifier = Modifier.height(24.dp))
            
            // Auth form
            LiquidGlassCard(
                modifier = Modifier.fillMaxWidth(),
                glowEffect = true
            ) {
                Column(
                    modifier = Modifier.padding(24.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Text(
                        text = if (isSignUp) "Create Your Account" else "Welcome Back! 👋",
                        style = MaterialTheme.typography.headlineMedium.copy(
                            fontWeight = FontWeight.Bold
                        ),
                        textAlign = TextAlign.Center,
                        modifier = Modifier.fillMaxWidth(),
                        color = if (isDark) Color.White else Color.Black
                    )
                    
                    if (isPhoneAuth) {
                        // Phone number input with beautiful neumorphic design
                        NeumorphicTextField(
                            value = phoneNumber,
                            onValueChange = { phoneNumber = it },
                            label = "Phone Number",
                            leadingIcon = Icons.Default.Phone,
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                            modifier = Modifier.fillMaxWidth()
                        )
                        
                        if (isSignUp) {
                            NeumorphicTextField(
                                value = displayName,
                                onValueChange = { displayName = it },
                                label = "Full Name",
                                leadingIcon = Icons.Default.Person,
                                modifier = Modifier.fillMaxWidth()
                            )
                            
                            // User Type Selection with neumorphic design
                            Column {
                                Text(
                                    text = "Account Type",
                                    style = MaterialTheme.typography.titleSmall.copy(
                                        fontWeight = FontWeight.Medium
                                    ),
                                    color = if (isDark) Color.White else Color.Black
                                )
                                
                                Spacer(modifier = Modifier.height(8.dp))
                                
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                                ) {
                                    NeumorphicChip(
                                        onClick = { userType = UserType.REGULAR },
                                        isSelected = userType == UserType.REGULAR,
                                        modifier = Modifier.weight(1f)
                                    ) {
                                        Text("Regular")
                                    }
                                    NeumorphicChip(
                                        onClick = { userType = UserType.BUSINESS },
                                        isSelected = userType == UserType.BUSINESS,
                                        modifier = Modifier.weight(1f)
                                    ) {
                                        Text("Business")
                                    }
                                }
                            }
                        }
                    } else {
                        // Email auth
                        NeumorphicTextField(
                            value = email,
                            onValueChange = { email = it },
                            label = "Email",
                            leadingIcon = Icons.Default.Email,
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                            modifier = Modifier.fillMaxWidth()
                        )
                        
                        NeumorphicTextField(
                            value = password,
                            onValueChange = { password = it },
                            label = "Password",
                            leadingIcon = Icons.Default.Lock,
                            trailingIcon = if (passwordVisible) Icons.Default.Visibility else Icons.Default.VisibilityOff,
                            onTrailingIconClick = { passwordVisible = !passwordVisible },
                            visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                    
                    Spacer(modifier = Modifier.height(8.dp))
                    
                    // Primary action button
                    NeumorphicButton(
                        onClick = {
                            if (isPhoneAuth) {
                                if (isSignUp) {
                                    viewModel.signUpWithPhone(phoneNumber, displayName, userType)
                                } else {
                                    viewModel.signInWithPhone(phoneNumber)
                                }
                            } else {
                                if (isSignUp) {
                                    viewModel.signUp(email, password, userType, null)
                                } else {
                                    viewModel.signIn(email, password)
                                }
                            }
                        },
                        enabled = !uiState.isLoading && 
                                ((isPhoneAuth && phoneNumber.isNotBlank()) || 
                                 (!isPhoneAuth && email.isNotBlank() && password.isNotBlank())) &&
                                (!isSignUp || displayName.isNotBlank()),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        if (uiState.isLoading) {
                            CircularProgressIndicator(
                                modifier = Modifier.size(20.dp),
                                color = if (isDark) Color.White else Color.Black,
                                strokeWidth = 2.dp
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                        }
                        
                        Icon(
                            imageVector = if (isSignUp) Icons.Default.PersonAdd else Icons.Default.Login,
                            contentDescription = null,
                            modifier = Modifier.size(20.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = if (isSignUp) "Create Account" else "Sign In",
                            fontWeight = FontWeight.Bold
                        )
                    }
                    
                    // Google Sign-In Button
                    HapticButton(
                        onClick = onGoogleSignIn,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        AdvancedNeumorphicCard(
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(16.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Email, // Using Email icon as Google icon may not exist
                                    contentDescription = null,
                                    tint = Color(0xFF4285F4),
                                    modifier = Modifier.size(24.dp)
                                )
                                
                                Spacer(modifier = Modifier.width(12.dp))
                                
                                Text(
                                    text = if (isSignUp) "Create Account with Google" else "Continue with Google",
                                    style = MaterialTheme.typography.bodyLarge.copy(
                                        fontWeight = FontWeight.Medium
                                    ),
                                    color = if (isDark) Color.White else Color.Black
                                )
                            }
                        }
                    }
                    
                    Spacer(modifier = Modifier.height(16.dp))
                    
                    TextButton(
                        onClick = { isSignUp = !isSignUp },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            text = if (isSignUp) "Already have an account? Sign In" 
                            else "Don't have an account? Sign Up",
                            color = if (isDark) Color.White else Color.Black.copy(alpha = 0.7f)
                        )
                    }
                    
                    // Error display with glass effect
                    uiState.error?.let { error ->
                        LiquidGlassCard(
                            modifier = Modifier.fillMaxWidth(),
                            glowEffect = false
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(16.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Warning,
                                    contentDescription = null,
                                    tint = Color(0xFFFF6B6B),
                                    modifier = Modifier.size(20.dp)
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    text = error,
                                    color = Color(0xFFFF6B6B),
                                    style = MaterialTheme.typography.bodyMedium
                                )
                            }
                        }
                    }
                }
            }
            
            Spacer(modifier = Modifier.height(32.dp))
        }
    }
}

@Composable
fun AnimatedLogoSection() {
    val isDark = isSystemInDarkTheme()
    val infiniteTransition = rememberInfiniteTransition(label = "logo_animation")
    
    val rotation by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(8000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "logo_rotation"
    )
    
    val pulse by infiniteTransition.animateFloat(
        initialValue = 0.9f,
        targetValue = 1.1f,
        animationSpec = infiniteRepeatable(
            animation = tween(3000, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "logo_pulse"
    )
    
    LiquidGlassCard(
        modifier = Modifier.fillMaxWidth(),
        glowEffect = true
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(32.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                modifier = Modifier
                    .size(100.dp)
                    .graphicsLayer {
                        rotationZ = rotation
                        scaleX = pulse
                        scaleY = pulse
                    },
                contentAlignment = Alignment.Center
            ) {
                Canvas(
                    modifier = Modifier.fillMaxSize()
                ) {
                    drawCircle(
                        brush = Brush.radialGradient(
                            colors = listOf(
                                LittleGigPrimary,
                                LittleGigSecondary,
                                LittleGigAccent
                            )
                        ),
                        radius = size.minDimension / 2
                    )
                }
                
                Text(
                    text = "🎪",
                    style = MaterialTheme.typography.headlineLarge
                )
            }
            
            Spacer(modifier = Modifier.height(24.dp))
            
            Text(
                text = "LittleGig",
                style = MaterialTheme.typography.headlineLarge.copy(
                    fontWeight = FontWeight.Bold
                ),
                color = Color.White
            )
            
            Text(
                text = "Where you need to be ✨",
                style = MaterialTheme.typography.titleMedium,
                color = (if (isDark) Color.White else Color.Black).copy(alpha = 0.8f),
                textAlign = TextAlign.Center
            )
        }
    }
}

@Composable
fun FloatingOrbs() {
    val infiniteTransition = rememberInfiniteTransition(label = "orbs_animation")
    
    repeat(5) { index ->
        val offset by infiniteTransition.animateFloat(
            initialValue = 0f,
            targetValue = 100f,
            animationSpec = infiniteRepeatable(
                animation = tween(3000 + index * 500, easing = LinearEasing),
                repeatMode = RepeatMode.Reverse
            ),
            label = "orb_offset_$index"
        )
        
        val alpha by infiniteTransition.animateFloat(
            initialValue = 0.3f,
            targetValue = 0.7f,
            animationSpec = infiniteRepeatable(
                animation = tween(2000 + index * 300, easing = FastOutSlowInEasing),
                repeatMode = RepeatMode.Reverse
            ),
            label = "orb_alpha_$index"
        )
        
        Box(
            modifier = Modifier
                .offset(
                    x = (100 + index * 80).dp,
                    y = (200 + index * 100 + offset).dp
                )
                .size(60.dp)
                .clip(RoundedCornerShape(50))
                .background(
                    Brush.radialGradient(
                        colors = listOf(
                            LittleGigPrimary.copy(alpha = alpha),
                            LittleGigSecondary.copy(alpha = alpha * 0.5f),
                            Color.Transparent
                        )
                    )
                )
                .blur(20.dp)
        )
    }
}

@Composable
fun LiquidGlassBackground() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colors = listOf(
                        Color.Transparent,
                        Color.White.copy(alpha = 0.05f),
                        Color.Transparent
                    )
                )
            )
    )
}

@Composable
fun NeumorphicTextField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    leadingIcon: androidx.compose.ui.graphics.vector.ImageVector,
    trailingIcon: androidx.compose.ui.graphics.vector.ImageVector? = null,
    onTrailingIconClick: (() -> Unit)? = null,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    modifier: Modifier = Modifier
) {
    var isFocused by remember { mutableStateOf(false) }
    val isDark = isSystemInDarkTheme()
    
    val animatedBorderColor by animateColorAsState(
        targetValue = if (isFocused) LittleGigPrimary else Color.Transparent,
        animationSpec = tween(300),
        label = "border_color"
    )
    
    // Use proper theme colors for text visibility
    val textColor = if (isDark) Color(0xFFE0E0E0) else Color(0xFF1A1A1A)
    val labelColor = if (isDark) Color(0xFFCCCCCC) else Color(0xFF666666)
    val iconColor = if (isFocused) LittleGigPrimary else (if (isDark) Color(0xFFCCCCCC) else Color(0xFF666666))
    
    LiquidGlassCard(
        modifier = modifier,
        glowEffect = isFocused
    ) {
        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            label = { 
                Text(
                    label,
                    color = labelColor
                )
            },
            leadingIcon = { 
                Icon(
                    leadingIcon, 
                    contentDescription = null,
                    tint = iconColor
                ) 
            },
            trailingIcon = trailingIcon?.let { icon ->
                {
                    IconButton(onClick = { onTrailingIconClick?.invoke() }) {
                        Icon(
                            icon,
                            contentDescription = null,
                            tint = iconColor
                        )
                    }
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(4.dp),
            singleLine = true,
            keyboardOptions = keyboardOptions,
            visualTransformation = visualTransformation,
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = animatedBorderColor,
                unfocusedBorderColor = Color.Transparent,
                focusedContainerColor = Color.Transparent,
                unfocusedContainerColor = Color.Transparent,
                focusedTextColor = textColor,
                unfocusedTextColor = textColor,
                cursorColor = LittleGigPrimary
            ),
            shape = RoundedCornerShape(16.dp)
        )
    }
}

@Composable
fun NeumorphicChip(
    onClick: () -> Unit,
    isSelected: Boolean,
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) {
    val isDark = isSystemInDarkTheme()
    
    LiquidGlassCard(
        modifier = modifier,
        glowEffect = isSelected
    ) {
        Surface(
            onClick = onClick,
            modifier = Modifier
                .fillMaxWidth()
                .padding(4.dp),
            shape = RoundedCornerShape(12.dp),
            color = if (isSelected) {
                if (isDark) LittleGigPrimary else LittleGigPrimary
            } else {
                Color.Transparent
            }
        ) {
            Box(
                modifier = Modifier.padding(12.dp),
                contentAlignment = Alignment.Center
            ) {
                content()
            }
        }
    }
}

