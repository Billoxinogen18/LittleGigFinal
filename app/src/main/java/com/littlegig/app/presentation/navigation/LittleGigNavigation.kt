package com.littlegig.app.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import com.littlegig.app.presentation.auth.AuthViewModel
import com.littlegig.app.presentation.main.MainScreen
import com.littlegig.app.presentation.auth.AuthScreen
import timber.log.Timber
import androidx.compose.material3.Box
import androidx.compose.material3.CircularProgressIndicator

@Composable
fun LittleGigNavigation() {
    val authViewModel: AuthViewModel = hiltViewModel()
    val currentUser by authViewModel.currentUser.collectAsState(initial = null)

    LaunchedEffect(currentUser?.id) {
        Timber.i("Navigation root, currentUser=${currentUser?.id ?: "none"}")
    }
    
    // Attempt anonymous sign-in once to avoid flashing the Auth screen
    val attemptedAnon = remember { mutableStateOf(false) }
    LaunchedEffect(currentUser == null && !attemptedAnon.value) {
        if (!attemptedAnon.value && currentUser == null) {
            attemptedAnon.value = true
            authViewModel.signInAnonymously()
        }
    }

    // Splash while trying anon sign-in
    if (currentUser == null && !attemptedAnon.value) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator()
        }
        return
    }
    
    // If still no user after attempting anon sign-in, show Auth
    if (currentUser == null) {
        AuthScreen(
            onAuthSuccess = { /* Navigation handled by AuthStateListener */ },
            onGoogleSignIn = { /* wired in screen */ }
        )
        return
    }
    
    // Show main app when authenticated (anonymous or linked)
    MainScreen(
        onSignOut = {
            Timber.i("Action: signOut")
            authViewModel.signOut()
        }
    )
}