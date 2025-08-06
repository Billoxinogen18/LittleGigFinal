package com.littlegig.app.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.littlegig.app.presentation.auth.AuthScreen
import com.littlegig.app.presentation.auth.AuthViewModel
import com.littlegig.app.presentation.main.MainScreen

@Composable
fun LittleGigNavigation() {
    val navController = rememberNavController()
    val authViewModel: AuthViewModel = hiltViewModel()
    val currentUser by authViewModel.currentUser.collectAsState(initial = null)
    
    // Use LaunchedEffect to handle navigation based on auth state
    LaunchedEffect(currentUser) {
        if (currentUser != null) {
            navController.navigate("main") {
                popUpTo("auth") { inclusive = true }
            }
        } else {
            navController.navigate("auth") {
                popUpTo("main") { inclusive = true }
            }
        }
    }
    
    NavHost(
        navController = navController,
        startDestination = "auth"
    ) {
        composable("auth") {
            AuthScreen(
                onAuthSuccess = {
                    // Navigation will be handled by LaunchedEffect
                }
            )
        }
        
        composable("main") {
            MainScreen(
                onSignOut = {
                    authViewModel.signOut()
                    // Navigation will be handled by LaunchedEffect
                }
            )
        }
    }
}