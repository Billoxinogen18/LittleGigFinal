package com.littlegig.app.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import com.littlegig.app.presentation.auth.AuthViewModel
import com.littlegig.app.presentation.main.MainScreen
import com.littlegig.app.presentation.auth.AuthScreen

@Composable
fun LittleGigNavigation() {
    val authViewModel: AuthViewModel = hiltViewModel()
    val currentUser by authViewModel.currentUser.collectAsState(initial = null)
    
    // Only show main app if user is authenticated
    if (currentUser != null) {
        MainScreen(
            onSignOut = {
                authViewModel.signOut()
            }
        )
    } else {
        // Show auth screen for unauthenticated users
        AuthScreen(
            onAuthSuccess = { /* Navigation will be handled by AuthStateListener */ },
            onGoogleSignIn = { 
                // Handle Google sign-in
            }
        )
    }
}