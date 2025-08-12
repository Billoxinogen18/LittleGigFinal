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
import com.littlegig.app.presentation.account.EditProfileScreen
import com.littlegig.app.presentation.components.*

import com.littlegig.app.presentation.events.EventsScreen
import com.littlegig.app.presentation.events.ModernEventsScreen
import com.littlegig.app.presentation.events.EventDetailsScreen
import com.littlegig.app.presentation.events.ModernEventDetailsScreen

import com.littlegig.app.presentation.map.MapScreen
import com.littlegig.app.presentation.settings.SettingsScreen
import com.littlegig.app.presentation.tickets.TicketsScreen
import com.littlegig.app.presentation.upload.UploadScreen
import com.littlegig.app.presentation.payments.PaymentsScreen
import com.littlegig.app.presentation.auth.AuthScreen
import com.littlegig.app.presentation.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(
    onSignOut: () -> Unit
) {
    val navController = rememberNavController()
    var currentRoute by remember { mutableStateOf("events") }
    val isDark = isSystemInDarkTheme()
    val inboxViewModel: com.littlegig.app.presentation.inbox.InboxViewModel = androidx.hilt.navigation.compose.hiltViewModel()
    val unreadCount by inboxViewModel.unreadCount.collectAsState(initial = 0)
    LaunchedEffect(Unit) { inboxViewModel.load() }
    

    
    // Vibrant glassmorphic background inspired by reference images
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(
                    colors = if (isDark) {
                        listOf(
                            DarkBackground,
                            DarkBackground.copy(alpha = 0.95f),
                            DarkBackground.copy(alpha = 0.9f)
                        )
                    } else {
                        listOf(
                            LightBackground,
                            LightBackground.copy(alpha = 0.98f),
                            LightBackground.copy(alpha = 0.95f)
                        )
                    }
                )
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
                    },
                    inboxUnreadCount = unreadCount
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
                        ModernEventsScreen(navController = navController)
                    }
                    composable("tickets") {
                        TicketsScreen(navController = navController)
                    }
                    composable("map") {
                        MapScreen(navController = navController)
                    }
                    composable("chat") {
                        // Temporarily simplified for testing
                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            Text("Chat Screen - Coming Soon")
                        }
                    }
                    composable("chat_details/{chatId}") { backStackEntry ->
                        // Temporarily simplified for testing
                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            Text("Chat Details - Coming Soon")
                        }
                    }
                    composable("event_details/{eventId}") { backStackEntry ->
                        val eventId = backStackEntry.arguments?.getString("eventId") ?: ""
                        ModernEventDetailsScreen(eventId = eventId, navController = navController)
                    }
                    composable("account") {
                        // Temporarily simplified for testing
                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            Text("Account Screen - Coming Soon")
                        }
                    }
                    composable("settings") {
                        SettingsScreen(
                            onNavigateToEditProfile = { navController.navigate("edit_profile") },
                            onNavigateToPayments = { navController.navigate("payments") },
                            onNavigateToTickets = { navController.navigate("tickets") },
                            onGoogleSignIn = { /* Handle Google sign in */ },
                            onNavigateBack = { navController.popBackStack() }
                        )
                    }
                    composable("upload") {
                        UploadScreen(navController = navController)
                    }
                    composable("payments") {
                        PaymentsScreen(navController = navController)
                    }
                    composable("auth") {
                        AuthScreen(
                            onAuthSuccess = { /* Handle auth success */ },
                            onGoogleSignIn = { /* Handle Google sign in */ }
                        )
                    }
                    composable("people_discovery") {
                        // Temporarily simplified for testing
                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            Text("People Discovery - Coming Soon")
                        }
                    }
                    composable("profile/{username}") { backStackEntry ->
                        val username = backStackEntry.arguments?.getString("username") ?: ""
                        com.littlegig.app.presentation.profile.ProfileScreen(
                            navController = navController,
                            username = username
                        )
                    }
                    composable("edit_profile") {
                        EditProfileScreen(navController = navController)
                    }
                    composable("inbox") {
                        com.littlegig.app.presentation.inbox.InboxScreen()
                    }
                    composable("recap_viewer/{recapId}") { backStackEntry ->
                        val recapId = backStackEntry.arguments?.getString("recapId") ?: ""
                        com.littlegig.app.presentation.recaps.RecapsViewerScreen(eventId = recapId)
                    }
                }
            }
        }
    }
}