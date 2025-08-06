package com.littlegig.app.presentation.auth

import androidx.compose.animation.animateColorAsState
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
    viewModel: AuthViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState(initial = AuthUiState())
    val currentUser by viewModel.currentUser.collectAsState(initial = null)
    val isDark = isSystemInDarkTheme()
    
    var isSignUp by remember { mutableStateOf(false) }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var displayName by remember { mutableStateOf("") }
    var phoneNumber by remember { mutableStateOf("") }
    var userType by remember { mutableStateOf(UserType.REGULAR) }
    var passwordVisible by remember { mutableStateOf(false) }
    
    // Navigate to main screen when user is authenticated
    LaunchedEffect(currentUser) {
        if (currentUser != null) {
            onAuthSuccess()
        }
    }
    
    ModernBackground {
        
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(60.dp))
            
            // App header
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(24.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(32.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    // App logo placeholder
                    Box(
                        modifier = Modifier.size(80.dp),
                        contentAlignment = Alignment.Center
                    ) {
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
                        color = if (isDark) DarkOnSurface else LightOnSurface
                    )
                    
                    Text(
                        text = "Where you need to be ✨",
                        style = MaterialTheme.typography.titleMedium,
                        color = if (isDark) DarkOnSurfaceVariant else LightOnSurfaceVariant,
                        textAlign = TextAlign.Center
                    )
                }
            }
            
            Spacer(modifier = Modifier.height(32.dp))
            
            // Auth form
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(24.dp)
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
                        color = if (isDark) DarkOnSurface else LightOnSurface
                    )
                    
                    if (isSignUp) {
                        NeumorphicTextField(
                            value = displayName,
                            onValueChange = { displayName = it },
                            label = "Full Name",
                            leadingIcon = Icons.Default.Person,
                            modifier = Modifier.fillMaxWidth()
                        )
                        
                        NeumorphicTextField(
                            value = phoneNumber,
                            onValueChange = { phoneNumber = it },
                            label = "Phone Number",
                            leadingIcon = Icons.Default.Phone,
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                            modifier = Modifier.fillMaxWidth()
                        )
                        
                        // User Type Selection with neumorphic design
                        Column {
                            Text(
                                text = "Account Type",
                                style = MaterialTheme.typography.titleSmall.copy(
                                    fontWeight = FontWeight.Medium
                                ),
                                color = if (isDark) DarkOnSurface else LightOnSurface
                            )
                            
                            Spacer(modifier = Modifier.height(8.dp))
                            
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                FilterChip(
                                    onClick = { userType = UserType.REGULAR },
                                    label = { Text("Regular") },
                                    selected = userType == UserType.REGULAR,
                                    modifier = Modifier.weight(1f)
                                )
                                FilterChip(
                                    onClick = { userType = UserType.BUSINESS },
                                    label = { Text("Business") },
                                    selected = userType == UserType.BUSINESS,
                                    modifier = Modifier.weight(1f)
                                )
                            }
                        }
                    }
                    
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
                    
                    Spacer(modifier = Modifier.height(8.dp))
                    
                    Button(
                        onClick = {
                            if (isSignUp) {
                                viewModel.signUpWithEmail(email, password, displayName, phoneNumber, userType)
                            } else {
                                viewModel.signInWithEmail(email, password)
                            }
                        },
                        enabled = !uiState.isLoading && 
                                email.isNotBlank() && 
                                password.isNotBlank() &&
                                (!isSignUp || displayName.isNotBlank()),
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(16.dp)
                    ) {
                        if (uiState.isLoading) {
                            CircularProgressIndicator(
                                modifier = Modifier.size(20.dp),
                                color = Color.White,
                                strokeWidth = 2.dp
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                        }
                        
                        Icon(
                            imageVector = if (isSignUp) Icons.Default.PersonAdd else Icons.Default.Login,
                            contentDescription = null,
                            modifier = Modifier.size(20.dp),
                            tint = LittleGigPrimary
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = if (isSignUp) "Create Account" else "Sign In",
                            fontWeight = FontWeight.Bold,
                            color = LittleGigPrimary
                        )
                    }
                    
                    // Google Sign-In Button
                    Button(
                        onClick = {
                            viewModel.signInWithGoogle()
                        },
                        enabled = !uiState.isLoading,
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(16.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color.White,
                            contentColor = Color.Black
                        )
                    ) {
                        Icon(
                            imageVector = Icons.Default.AccountCircle,
                            contentDescription = null,
                            modifier = Modifier.size(20.dp),
                            tint = Color.Black
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "Continue with Google",
                            fontWeight = FontWeight.Bold,
                            color = Color.Black
                        )
                    }
                    
                    TextButton(
                        onClick = { isSignUp = !isSignUp },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            text = if (isSignUp) "Already have an account? Sign In" 
                            else "Don't have an account? Sign Up",
                            color = if (isDark) DarkOnSurfaceVariant else LightOnSurfaceVariant
                        )
                    }
                    
                    // Error display 
                    uiState.error?.let { error ->
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(16.dp)
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .background(
                                        Brush.linearGradient(
                                            colors = listOf(
                                                Error.copy(alpha = 0.1f),
                                                Error.copy(alpha = 0.05f)
                                            )
                                        )
                                    )
                                    .padding(16.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Warning,
                                    contentDescription = null,
                                    tint = Error,
                                    modifier = Modifier.size(20.dp)
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    text = error,
                                    color = Error,
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
    val isDark = isSystemInDarkTheme()
    var isFocused by remember { mutableStateOf(false) }
    
    val animatedBorderColor by animateColorAsState(
        targetValue = if (isFocused) LittleGigPrimary else Color.Transparent,
        animationSpec = tween(300),
        label = "border_color"
    )
    
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
                    color = if (isDark) DarkOnSurfaceVariant else LightOnSurfaceVariant
                )
            },
            leadingIcon = { 
                Icon(
                    leadingIcon, 
                    contentDescription = null,
                    tint = if (isFocused) LittleGigPrimary else {
                        if (isDark) DarkOnSurfaceVariant else LightOnSurfaceVariant
                    }
                ) 
            },
            trailingIcon = trailingIcon?.let { icon ->
                {
                    IconButton(onClick = { onTrailingIconClick?.invoke() }) {
                        Icon(
                            icon,
                            contentDescription = null,
                            tint = if (isDark) DarkOnSurfaceVariant else LightOnSurfaceVariant
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
                focusedTextColor = if (isDark) DarkOnSurface else LightOnSurface,
                unfocusedTextColor = if (isDark) DarkOnSurface else LightOnSurface
            ),
            shape = RoundedCornerShape(16.dp)
        )
    }
}

@Composable
fun AnimatedLogo() {
    val infiniteTransition = rememberInfiniteTransition(label = "logo_animation")
    
    val rotation by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(4000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "logo_rotation"
    )
    
    val pulse by infiniteTransition.animateFloat(
        initialValue = 0.9f,
        targetValue = 1.1f,
        animationSpec = infiniteRepeatable(
            animation = tween(2000, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "logo_pulse"
    )
    
    Box(
        modifier = Modifier
            .size(80.dp)
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
            
            drawCircle(
                brush = Brush.radialGradient(
                    colors = listOf(
                        Color.White.copy(alpha = 0.3f),
                        Color.Transparent
                    )
                ),
                radius = size.minDimension / 3,
                center = center.copy(
                    x = center.x - size.width * 0.2f,
                    y = center.y - size.height * 0.2f
                )
            )
        }
    }
}