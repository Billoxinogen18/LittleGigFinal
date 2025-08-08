package com.littlegig.app.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import com.littlegig.app.presentation.auth.AuthViewModel
import com.littlegig.app.presentation.main.MainScreen

@Composable
fun LittleGigNavigation() {
    val authViewModel: AuthViewModel = hiltViewModel()
    val currentUser by authViewModel.currentUser.collectAsState(initial = null)
    
    // 🔥 ANONYMOUS AUTHENTICATION - TIKTOK STYLE! 🔥
    // Automatically sign in anonymously if no user
    LaunchedEffect(Unit) {
        if (currentUser == null) {
            authViewModel.signInAnonymously()
        }
    }
    
    // Show main app directly since we're using anonymous auth
    MainScreen(
        onSignOut = {
            authViewModel.signOut()
        }
    )
}