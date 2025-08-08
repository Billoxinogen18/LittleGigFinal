package com.littlegig.app.presentation.account

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.littlegig.app.data.model.UserType
import com.littlegig.app.presentation.account.LiquidGlassAccountScreen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AccountScreen(
    navController: NavController,
    onSignOut: () -> Unit,
    viewModel: AccountViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val currentUser by viewModel.currentUser.collectAsState()
    
    // Handle account linking for anonymous users
    LaunchedEffect(uiState.showAccountLinking) {
        if (uiState.showAccountLinking) {
            navController.navigate("auth")
        }
    }
    
    // Use the new account dashboard
    AccountDashboard(
        viewModel = viewModel,
        onNavigateToEditProfile = { navController.navigate("edit_profile") },
        onNavigateToSettings = { navController.navigate("settings") },
        onNavigateToPayments = { navController.navigate("payments") },
        onNavigateToTickets = { navController.navigate("tickets") },
        onSignOut = onSignOut,
        onLinkAccount = { viewModel.linkAnonymousAccount() }
    )
}

@Composable
fun LegacyAccountScreen(
    navController: NavController,
    onSignOut: () -> Unit,
    viewModel: AccountViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState(initial = AccountUiState())
    val currentUser by viewModel.currentUser.collectAsState(initial = null)
    
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp)
    ) {
        // Header
        Text(
            text = "Account",
            style = MaterialTheme.typography.headlineMedium.copy(
                fontWeight = FontWeight.Bold
            )
        )
        
        Spacer(modifier = Modifier.height(24.dp))
        
        // Profile Section
        currentUser?.let { user ->
            Card(
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surface
                ),
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
            ) {
                Column(
                    modifier = Modifier.padding(20.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    // Profile Image
                    if (user.profileImageUrl.isNotEmpty()) {
                        AsyncImage(
                            model = user.profileImageUrl,
                            contentDescription = "Profile",
                            modifier = Modifier
                                .size(80.dp)
                                .clip(CircleShape),
                            contentScale = ContentScale.Crop
                        )
                    } else {
                        Icon(
                            imageVector = Icons.Default.AccountCircle,
                            contentDescription = "Profile",
                            modifier = Modifier.size(80.dp),
                            tint = MaterialTheme.colorScheme.primary
                        )
                    }
                    
                    Spacer(modifier = Modifier.height(16.dp))
                    
                    Text(
                        text = user.displayName.ifEmpty { "User" },
                        style = MaterialTheme.typography.titleLarge.copy(
                            fontWeight = FontWeight.Bold
                        )
                    )
                    
                    Text(
                        text = user.email,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    
                    Spacer(modifier = Modifier.height(8.dp))
                    
                    // Account Type Badge
                    Card(
                        colors = CardDefaults.cardColors(
                            containerColor = when (user.userType) {
                                UserType.BUSINESS -> MaterialTheme.colorScheme.primaryContainer
                                UserType.ADMIN -> MaterialTheme.colorScheme.tertiaryContainer
                                else -> MaterialTheme.colorScheme.secondaryContainer
                            }
                        )
                    ) {
                        Text(
                            text = when (user.userType) {
                                UserType.BUSINESS -> "Business Account"
                                UserType.ADMIN -> "Admin Account"
                                else -> "Regular Account"
                            },
                            modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                            style = MaterialTheme.typography.labelMedium.copy(
                                fontWeight = FontWeight.Medium
                            ),
                            color = when (user.userType) {
                                UserType.BUSINESS -> MaterialTheme.colorScheme.primary
                                UserType.ADMIN -> MaterialTheme.colorScheme.tertiary
                                else -> MaterialTheme.colorScheme.secondary
                            }
                        )
                    }
                    
                    if (user.isInfluencer) {
                        Spacer(modifier = Modifier.height(4.dp))
                        Card(
                            colors = CardDefaults.cardColors(
                                containerColor = MaterialTheme.colorScheme.tertiaryContainer
                            )
                        ) {
                            Text(
                                text = "Influencer",
                                modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                                style = MaterialTheme.typography.labelMedium.copy(
                                    fontWeight = FontWeight.Medium
                                ),
                                color = MaterialTheme.colorScheme.tertiary
                            )
                        }
                    }
                }
            }
        }
        
        Spacer(modifier = Modifier.height(24.dp))
        
        // Menu Items
        Column(
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            // Profile Settings
            AccountMenuItem(
                icon = Icons.Default.Person,
                title = "Edit Profile",
                subtitle = "Update your personal information",
                onClick = { /* Navigate to edit profile */ }
            )
            
            // Business Features (only for business accounts)
            if (currentUser?.userType == UserType.BUSINESS || currentUser?.userType == UserType.ADMIN) {
                AccountMenuItem(
                    icon = Icons.Default.Business,
                    title = "Business Dashboard",
                    subtitle = "Manage your events and analytics",
                    onClick = { /* Navigate to business dashboard */ }
                )
                
                AccountMenuItem(
                    icon = Icons.Default.AttachMoney,
                    title = "Revenue Analytics",
                    subtitle = "View your earnings and commissions",
                    onClick = { /* Navigate to revenue analytics */ }
                )
            }
            
            // Influencer Features
            if (currentUser?.isInfluencer == true) {
                AccountMenuItem(
                    icon = Icons.Default.Campaign,
                    title = "Advertisement Manager",
                    subtitle = "Create and manage your ads",
                    onClick = { /* Navigate to ad manager */ }
                )
            }
            
            // Account Upgrade (for regular users)
            if (currentUser?.userType == UserType.REGULAR) {
                AccountMenuItem(
                    icon = Icons.Default.Upgrade,
                    title = "Upgrade to Business",
                    subtitle = "Start creating events and earning revenue",
                    onClick = { viewModel.upgradeToBusinessAccount() }
                )
                
                AccountMenuItem(
                    icon = Icons.Default.Star,
                    title = "Become an Influencer",
                    subtitle = "Monetize your social media presence",
                    onClick = { viewModel.becomeInfluencer() }
                )
            }
            
            Divider(modifier = Modifier.padding(vertical = 8.dp))
            
            // Settings
            AccountMenuItem(
                icon = Icons.Default.Settings,
                title = "Settings",
                subtitle = "App preferences and privacy",
                onClick = { /* Navigate to settings */ }
            )
            
            AccountMenuItem(
                icon = Icons.Default.Help,
                title = "Help & Support",
                subtitle = "Get assistance and contact support",
                onClick = { /* Navigate to help */ }
            )
            
            AccountMenuItem(
                icon = Icons.Default.Info,
                title = "About LittleGig",
                subtitle = "App version and legal information",
                onClick = { /* Navigate to about */ }
            )
            
            Spacer(modifier = Modifier.height(16.dp))
            
            // Sign Out Button
            OutlinedButton(
                onClick = {
                    viewModel.signOut()
                    onSignOut()
                },
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.outlinedButtonColors(
                    contentColor = MaterialTheme.colorScheme.error
                )
            ) {
                Icon(
                    imageVector = Icons.Default.Logout,
                    contentDescription = null
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text("Sign Out")
            }
        }
        
        // Loading and Error States
        if (uiState.isLoading) {
            Box(
                modifier = Modifier.fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        }
        
        uiState.error?.let { error ->
            Spacer(modifier = Modifier.height(16.dp))
            Card(
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.errorContainer
                )
            ) {
                Text(
                    text = error,
                    color = MaterialTheme.colorScheme.onErrorContainer,
                    modifier = Modifier.padding(16.dp),
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }
        
        if (uiState.isSuccess) {
            Spacer(modifier = Modifier.height(16.dp))
            Card(
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.tertiaryContainer
                )
            ) {
                Text(
                    text = "Account updated successfully!",
                    color = MaterialTheme.colorScheme.onTertiaryContainer,
                    modifier = Modifier.padding(16.dp),
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }
        
        Spacer(modifier = Modifier.height(24.dp))
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AccountMenuItem(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    title: String,
    subtitle: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        onClick = onClick,
        modifier = modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier.size(24.dp)
            )
            
            Spacer(modifier = Modifier.width(16.dp))
            
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.Medium
                    )
                )
                Text(
                    text = subtitle,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            
            Icon(
                imageVector = Icons.Default.ChevronRight,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}