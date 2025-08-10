package com.littlegig.app.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import com.littlegig.app.presentation.auth.AuthViewModel
import com.littlegig.app.presentation.main.MainScreen
import com.littlegig.app.presentation.auth.AuthScreen
import timber.log.Timber

@Composable
fun LittleGigNavigation() {
    val authViewModel: AuthViewModel = hiltViewModel()
    val currentUser by authViewModel.currentUser.collectAsState(initial = null)

    LaunchedEffect(currentUser?.id) {
        Timber.i("Navigation root, currentUser=${currentUser?.id ?: "none"}")
    }
    
    // Only show main app if user is authenticated
    if (currentUser != null) {
        MainScreen(
            onSignOut = {
                Timber.i("Action: signOut")
                authViewModel.signOut()
            }
        )
    } else {
        // Show auth screen for unauthenticated users
        AuthScreen(
            onAuthSuccess = { /* Navigation handled by AuthStateListener */ },
            onGoogleSignIn = { /* wired in screen */ }
        )
    }
}