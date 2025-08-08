package com.littlegig.app.presentation.main

import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.ui.Alignment
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.littlegig.app.presentation.account.AccountScreen
import com.littlegig.app.presentation.account.EditProfileScreen
import com.littlegig.app.presentation.chat.ChatDetailsScreen
import com.littlegig.app.presentation.chat.ChatScreen
import com.littlegig.app.presentation.components.*
import com.littlegig.app.presentation.events.EventsScreen
import com.littlegig.app.presentation.events.EventDetailsScreen
import com.littlegig.app.presentation.map.MapScreen
import com.littlegig.app.presentation.settings.SettingsScreen
import com.littlegig.app.presentation.tickets.TicketsScreen
import com.littlegig.app.presentation.upload.UploadScreen
import com.littlegig.app.presentation.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(
    onSignOut: () -> Unit
) {
    val navController = rememberNavController()
    var currentRoute by remember { mutableStateOf("events") }
    val isDark = isSystemInDarkTheme()
    
    // Proper dark/light mode background
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                if (isDark) {
                    Brush.verticalGradient(
                        colors = listOf(
                            DarkBackground,
                            DarkSurface
                        )
                    )
                } else {
                    Brush.verticalGradient(
                        colors = listOf(
                            LightBackground,
                            LightSurface
                        )
                    )
                }
            )
    ) {
        Scaffold(
            modifier = Modifier.fillMaxSize(),
            containerColor = Color.Transparent,
            bottomBar = {
                LiquidGlassBottomNavigation(
                    currentRoute = currentRoute,
                    onNavigate = { route ->
                        currentRoute = route
                        navController.navigate(route) {
                            popUpTo(navController.graph.startDestinationId) {
                                saveState = true
                            }
                            launchSingleTop = true
                            restoreState = true
                        }
                    }
                )
            }
        ) { paddingValues ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
            ) {
                NavHost(
                    navController = navController,
                    startDestination = "events"
                ) {
                    composable("events") {
                        EventsScreen(navController = navController)
                    }
                    composable("tickets") {
                        TicketsScreen(navController = navController)
                    }
                    composable("map") {
                        MapScreen(navController = navController)
                    }
                    composable("upload") {
                        UploadScreen(navController = navController)
                    }
                    composable("chat") {
                        ChatScreen(navController = navController)
                    }
                    composable("account") {
                        AccountScreen(
                            navController = navController,
                            onSignOut = onSignOut
                        )
                    }
                    composable("event_details/{eventId}") { backStackEntry ->
                        val eventId = backStackEntry.arguments?.getString("eventId") ?: ""
                        EventDetailsScreen(
                            eventId = eventId,
                            navController = navController
                        )
                    }
                    composable("edit_profile") {
                        EditProfileScreen(navController = navController)
                    }
                    composable("settings") {
                        SettingsScreen(
                            onNavigateToEditProfile = { navController.navigate("edit_profile") },
                            onNavigateToPayments = { navController.navigate("payments") },
                            onNavigateToTickets = { navController.navigate("tickets") },
                            onGoogleSignIn = { /* Handle Google Sign-In */ },
                            onNavigateBack = { navController.popBackStack() }
                        )
                    }
                    composable("payments") {
                        // Placeholder Payments Screen
                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            Text("Payments Screen - Coming Soon")
                        }
                    }
                    composable("chat_details/{chatId}") { backStackEntry ->
                        val chatId = backStackEntry.arguments?.getString("chatId") ?: ""
                        ChatDetailsScreen(
                            chatId = chatId,
                            navController = navController
                        )
                    }
                }
            }
        }
    }
}