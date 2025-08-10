package com.littlegig.app.presentation.auth

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
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
import androidx.compose.ui.platform.LocalContext
import android.app.Activity

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AccountLinkingScreen(
    onAccountLinked: () -> Unit,
    onSkip: () -> Unit,
    viewModel: AuthViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState(initial = AuthUiState())
    val currentUser by viewModel.currentUser.collectAsState(initial = null)
    
    var isSignUp by remember { mutableStateOf(true) }
    var phoneNumber by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var displayName by remember { mutableStateOf("") }
    var userType by remember { mutableStateOf(UserType.REGULAR) }
    var passwordVisible by remember { mutableStateOf(false) }
    var isPhoneAuth by remember { mutableStateOf(true) }
    var otpCode by remember { mutableStateOf("") }
    val ctx = LocalContext.current
    val activity = ctx as? Activity
    
    // Navigate to main screen when account is linked
    LaunchedEffect(currentUser) {
        if (currentUser != null && currentUser?.email?.isNotEmpty() == true) {
            onAccountLinked()
        }
    }
    
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colors = listOf(
                        Color(0xFF0B0E1A),
                        Color(0xFF141B2E),
                        Color(0xFF1E293B)
                    )
                )
            )
    ) {
        com.littlegig.app.presentation.auth.FloatingOrbs()
        com.littlegig.app.presentation.auth.LiquidGlassBackground()
        
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(60.dp))
            
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
                    Text(
                        text = "🎉 Welcome to LittleGig!",
                        style = MaterialTheme.typography.headlineMedium.copy(
                            fontWeight = FontWeight.Bold
                        ),
                        color = Color.White,
                        textAlign = TextAlign.Center
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        text = "You're already exploring! Create an account to save your preferences and unlock more features.",
                        style = MaterialTheme.typography.bodyLarge,
                        color = Color.White.copy(alpha = 0.8f),
                        textAlign = TextAlign.Center
                    )
                    Spacer(modifier = Modifier.height(24.dp))
                    NeumorphicButton(
                        onClick = onSkip,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Icon(Icons.Default.SkipNext, contentDescription = null, modifier = Modifier.size(20.dp))
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(text = "Continue as Anonymous", fontWeight = FontWeight.Bold)
                    }
                    Spacer(modifier = Modifier.height(12.dp))
                    NeumorphicButton(
                        onClick = {
                            val activity = ctx as? com.littlegig.app.presentation.MainActivity
                            activity?.startGoogleSignIn()
                        },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Icon(Icons.Default.AccountCircle, contentDescription = null, modifier = Modifier.size(20.dp))
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(text = "Link with Google", fontWeight = FontWeight.Bold)
                    }
                }
            }
            Spacer(modifier = Modifier.height(32.dp))
            
            LiquidGlassCard(
                modifier = Modifier.fillMaxWidth(),
                glowEffect = true
            ) {
                Column(
                    modifier = Modifier.padding(24.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Text(
                        text = "Create Your Account",
                        style = MaterialTheme.typography.headlineMedium.copy(fontWeight = FontWeight.Bold),
                        textAlign = TextAlign.Center,
                        modifier = Modifier.fillMaxWidth(),
                        color = Color.White
                    )
                    Text(
                        text = "🧠 Your preferences and activity will be preserved!",
                        style = MaterialTheme.typography.bodyMedium,
                        color = Color.White.copy(alpha = 0.7f),
                        textAlign = TextAlign.Center,
                        modifier = Modifier.fillMaxWidth()
                    )
                    
                    if (isPhoneAuth) {
                        NeumorphicTextField(
                            value = phoneNumber,
                            onValueChange = { phoneNumber = it },
                            label = "Phone Number",
                            leadingIcon = Icons.Default.Phone,
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                            modifier = Modifier.fillMaxWidth()
                        )
                        NeumorphicTextField(
                            value = displayName,
                            onValueChange = { displayName = it },
                            label = "Full Name",
                            leadingIcon = Icons.Default.Person,
                            modifier = Modifier.fillMaxWidth()
                        )
                        
                        // OTP section
                        if (uiState.otpSent) {
                            NeumorphicTextField(
                                value = otpCode,
                                onValueChange = { otpCode = it },
                                label = "Enter OTP",
                                leadingIcon = Icons.Default.Lock,
                                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                                modifier = Modifier.fillMaxWidth()
                            )
                            Row(horizontalArrangement = Arrangement.spacedBy(8.dp), modifier = Modifier.fillMaxWidth()) {
                                NeumorphicButton(onClick = { viewModel.verifyOtpAndLink(displayName, userType, otpCode) }, modifier = Modifier.weight(1f)) {
                                    Text("Verify & Link")
                                }
                                NeumorphicButton(onClick = {
                                    val e164 = com.littlegig.app.services.PhoneNumberService().normalizeToE164(phoneNumber) ?: phoneNumber
                                    activity?.let { viewModel.resendPhoneVerification(it, e164) }
                                }, modifier = Modifier.weight(1f)) { Text("Resend") }
                            }
                        } else {
                            NeumorphicButton(onClick = {
                                val e164 = com.littlegig.app.services.PhoneNumberService().normalizeToE164(phoneNumber) ?: phoneNumber
                                activity?.let { viewModel.startPhoneVerification(it, e164) }
                            }, modifier = Modifier.fillMaxWidth()) {
                                Text("Send OTP")
                            }
                        }
                    } else {
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
                        NeumorphicTextField(
                            value = displayName,
                            onValueChange = { displayName = it },
                            label = "Full Name",
                            leadingIcon = Icons.Default.Person,
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                    
                    Spacer(modifier = Modifier.height(8.dp))
                    
                    NeumorphicButton(
                        onClick = {
                            if (isPhoneAuth) {
                                // Linking is handled via OTP verify above
                            } else {
                                val emailOk = android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()
                                val passOk = password.length >= 6
                                if (!emailOk) {
                                    viewModel.clearError()
                                    // surface via UI state
                                    // Simple immediate feedback
                                    // Using same error channel
                                    viewModel.signInAnonymously() // no-op to ensure ViewModel active
                                    viewModel.clearError()
                                    // Instead set error state directly
                                    // But we don't have a direct setter; reuse error flow
                                    // Emit by attempting and catching would be heavy; keep inline UI validation text
                                } else if (!passOk) {
                                    // Same note as above
                                } else {
                                    viewModel.linkAnonymousAccount(email, password, displayName, null, userType)
                                }
                            }
                        },
                        enabled = !uiState.isLoading && (
                            (!isPhoneAuth && email.isNotBlank() && password.isNotBlank() && displayName.isNotBlank())
                        ),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        if (uiState.isLoading) {
                            CircularProgressIndicator(modifier = Modifier.size(20.dp), color = Color.White, strokeWidth = 2.dp)
                            Spacer(modifier = Modifier.width(8.dp))
                        }
                        Icon(imageVector = Icons.Default.Link, contentDescription = null, modifier = Modifier.size(20.dp))
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(text = if (isPhoneAuth) "Waiting for OTP…" else "Link Account", fontWeight = FontWeight.Bold)
                    }
                    
                    TextButton(onClick = { isPhoneAuth = !isPhoneAuth }, modifier = Modifier.fillMaxWidth()) {
                        Text(text = if (isPhoneAuth) "Use Email Instead" else "Use Phone Instead", color = Color.White.copy(alpha = 0.7f))
                    }
                    
                    uiState.error?.let { error ->
                        LiquidGlassCard(modifier = Modifier.fillMaxWidth(), glowEffect = false) {
                            Row(modifier = Modifier.fillMaxWidth().padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
                                Icon(imageVector = Icons.Default.Warning, contentDescription = null, tint = Color(0xFFFF6B6B), modifier = Modifier.size(20.dp))
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(text = error, color = Color(0xFFFF6B6B), style = MaterialTheme.typography.bodyMedium)
                            }
                        }
                    }
                    // Inline validation messages
                    if (!isPhoneAuth) {
                        val emailOk = android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()
                        val passOk = password.length >= 6
                        if (email.isNotBlank() && !emailOk) {
                            Text(text = "Please enter a valid email.", color = Color(0xFFFF6B6B))
                        }
                        if (password.isNotBlank() && !passOk) {
                            Text(text = "Password must be at least 6 characters.", color = Color(0xFFFF6B6B))
                        }
                    }
                }
            }
            Spacer(modifier = Modifier.height(32.dp))
        }
    }
}

 