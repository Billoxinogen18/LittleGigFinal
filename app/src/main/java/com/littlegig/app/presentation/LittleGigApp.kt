package com.littlegig.app.presentation

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.core.view.WindowCompat
import androidx.hilt.navigation.compose.hiltViewModel
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.littlegig.app.presentation.auth.AuthViewModel
import com.littlegig.app.presentation.navigation.LittleGigNavigation
import com.littlegig.app.presentation.theme.LittleGigTheme
import com.littlegig.app.presentation.theme.LittleGigPrimary

@Composable
fun LittleGigApp() {
    LittleGigTheme {
        val systemUiController = rememberSystemUiController()
        val isDark = isSystemInDarkTheme()
        
        // Set system bars to transparent for edge-to-edge design
        SideEffect {
            systemUiController.setSystemBarsColor(
                color = Color.Transparent,
                darkIcons = !isDark
            )
            systemUiController.setNavigationBarColor(
                color = Color.Transparent
            )
        }
        
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = Color.Transparent
        ) {
            // 🔥 ANONYMOUS AUTHENTICATION FLOW - TIKTOK STYLE! 🔥
            val authViewModel: AuthViewModel = hiltViewModel()
            val currentUser by authViewModel.currentUser.collectAsState()
            
            // Automatically sign in anonymously if no user
            LaunchedEffect(Unit) {
                if (currentUser == null) {
                    authViewModel.signInAnonymously()
                }
            }
            
            if (currentUser != null) {
                // User is authenticated (anonymous or registered) - show main app
                LittleGigNavigation()
            } else {
                // Show loading while anonymous auth is happening
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(
                        color = LittleGigPrimary
                    )
                }
            }
        }
    }
}