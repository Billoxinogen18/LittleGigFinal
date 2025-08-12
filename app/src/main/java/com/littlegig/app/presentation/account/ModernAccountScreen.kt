package com.littlegig.app.presentation.account

import androidx.compose.animation.core.*
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.*
import androidx.compose.ui.graphics.*
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.littlegig.app.data.model.UserType
import com.littlegig.app.presentation.components.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ModernAccountScreen(
    navController: NavController,
    onSignOut: () -> Unit,
    viewModel: AccountViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val currentUser by viewModel.currentUser.collectAsState()
    
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                if (isSystemInDarkTheme()) {
                    Color(0xFF0F0F23) // Pure dark blue, no grey
                } else {
                    Color(0xFFF8FAFC) // Pure light, no grey
                }
            )
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            // Modern Header
            ModernAccountHeader(
                onBackClick = { navController.popBackStack() }
            )
            
            // Profile Card
            currentUser?.let { user ->
                ModernProfileCard(
                    user = user,
                    onEditClick = { navController.navigate("edit_profile") }
                )
            }
            
            // Quick Stats
            ModernStatsCard(uiState = uiState)
            
            // Account Actions
            ModernAccountActions(
                currentUser = currentUser,
                onPhoneLinkClick = { viewModel.linkAnonymousAccount() },
                onBusinessUpgradeClick = { viewModel.upgradeToBusinessAccount() },
                onCreateDemoUsersClick = { viewModel.createDemoUsers() },
                onCheckExistingUsersClick = { viewModel.checkExistingUsers() },
                onDeleteAllUsersClick = { viewModel.deleteAllUsers() },
                onSettingsClick = { navController.navigate("settings") }
            )
            
            // Sign Out
            ModernSignOutButton(onSignOut = {
                viewModel.signOut()
                onSignOut()
            })
            
            Spacer(modifier = Modifier.height(100.dp)) // Bottom padding for navigation
        }
        
        // Phone linking flow in a glass sheet
        if (uiState.showAccountLinking) {
            androidx.compose.material3.ModalBottomSheet(onDismissRequest = { viewModel.clearAccountLinking() }) {
                com.littlegig.app.presentation.auth.AccountLinkingScreen(
                    onAccountLinked = { viewModel.clearAccountLinking() },
                    onSkip = { viewModel.clearAccountLinking() }
                )
            }
        }

        // Loading overlay
        if (uiState.isLoading) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Black.copy(alpha = 0.3f)),
                contentAlignment = Alignment.Center
            ) {
                ModernLiquidGlassCard() {
                    Column(
                        modifier = Modifier.padding(32.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        CircularProgressIndicator(color = GlassPrimary)
                        Spacer(modifier = Modifier.height(16.dp))
                        Text(
                            text = "Processing...",
                            style = MaterialTheme.typography.bodyMedium,
                            color = GlassOnSurface()
                        )
                    }
                }
            }
        }
        
        // Success message
        if (uiState.isSuccess) {
            LaunchedEffect(Unit) {
                kotlinx.coroutines.delay(2000)
                // Clear success state
            }
        }
    }
}

@Composable
private fun ModernAccountHeader(
    onBackClick: () -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        ModernLiquidGlassCard(
            onClick = onBackClick
        ) {
            Icon(
                imageVector = Icons.Default.ArrowBack,
                contentDescription = "Back",
                tint = GlassOnSurface(),
                modifier = Modifier
                    .padding(12.dp)
                    .size(20.dp)
            )
        }
        
        Text(
            text = "Account",
            style = MaterialTheme.typography.headlineLarge.copy(
                fontWeight = FontWeight.ExtraBold,
                fontSize = 28.sp
            ),
            color = GlassOnSurface()
        )
        
        // Placeholder for balance
        Spacer(modifier = Modifier.size(44.dp))
    }
}

@Composable
private fun ModernProfileCard(
    user: com.littlegig.app.data.model.User,
    onEditClick: () -> Unit
) {
    ModernLiquidGlassCard() {
        Box {
            // Background gradient
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
                    .background(
                        brush = Brush.linearGradient(
                            colors = listOf(GlassPrimary, GlassSecondary)
                        ),
    
                    )
            )
            
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Profile Image
                Box {
                    if (user.profileImageUrl.isNotEmpty()) {
                        AsyncImage(
                            model = user.profileImageUrl,
                            contentDescription = "Profile",
                            modifier = Modifier
                                .size(80.dp)
                                .clip(CircleShape)
                                .border(3.dp, Color.White.copy(alpha = 0.3f), CircleShape),
                            contentScale = ContentScale.Crop
                        )
                    } else {
                        Box(
                            modifier = Modifier
                                .size(80.dp)
                                .background(
                                    Color.White.copy(alpha = 0.2f),
                                    CircleShape
                                )
                                .border(3.dp, Color.White.copy(alpha = 0.3f), CircleShape),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = user.displayName.firstOrNull()?.uppercase() ?: "U",
                                style = MaterialTheme.typography.headlineMedium.copy(
                                    fontWeight = FontWeight.Bold
                                ),
                                color = Color.White
                            )
                        }
                    }
                    
                    // Edit button
                    ModernLiquidGlassCard(
                        onClick = onEditClick,
                        modifier = Modifier
                            .align(Alignment.BottomEnd)
                            .offset(x = 8.dp, y = 8.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Edit,
                            contentDescription = "Edit",
                            tint = GlassPrimary,
                            modifier = Modifier
                                .padding(8.dp)
                                .size(16.dp)
                        )
                    }
                }
                
                Spacer(modifier = Modifier.height(16.dp))
                
                // User Info
                Text(
                    text = user.displayName.ifEmpty { "User" },
                    style = MaterialTheme.typography.headlineSmall.copy(
                        fontWeight = FontWeight.Bold
                    ),
                    color = Color.White
                )
                
                Text(
                    text = user.email,
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color.White.copy(alpha = 0.9f)
                )
                
                Spacer(modifier = Modifier.height(12.dp))
                
                // Account Type Badge
                ModernLiquidGlassCard() {
                    Text(
                        text = when (user.userType) {
                            UserType.BUSINESS -> "Business Account"
                            UserType.ADMIN -> "Admin Account"
                            else -> "Regular Account"
                        },
                        modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
                        style = MaterialTheme.typography.labelMedium.copy(
                            fontWeight = FontWeight.SemiBold
                        ),
                        color = Color.White
                    )
                }
            }
        }
    }
}

@Composable
private fun ModernStatsCard(
    uiState: AccountUiState
) {
    ModernLiquidGlassCard() {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp)
        ) {
            Text(
                text = "Your Activity",
                style = MaterialTheme.typography.titleLarge.copy(
                    fontWeight = FontWeight.Bold
                ),
                color = GlassOnSurface()
            )
            
            Spacer(modifier = Modifier.height(16.dp))
            
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                StatItem(
                    value = uiState.eventsCreated.toString(),
                    label = "Events",
                    Icons.Default.Event,
                    color = GlassPrimary
                )
                
                StatItem(
                    value = uiState.ticketsBought.toString(),
                    label = "Tickets",
                    Icons.Default.ConfirmationNumber,
                    color = GlassSecondary
                )
                
                StatItem(
                    value = uiState.recapsShared.toString(),
                    label = "Recaps",
                    Icons.Default.PhotoCamera,
                    color = GlassAccent
                )
            }
        }
    }
}

@Composable
private fun StatItem(
    value: String,
    label: String,
    icon: ImageVector,
    color: Color
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        ModernLiquidGlassCard() {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = color,
                modifier = Modifier
                    .padding(12.dp)
                    .size(24.dp)
            )
        }
        
        Spacer(modifier = Modifier.height(8.dp))
        
        Text(
            text = value,
            style = MaterialTheme.typography.titleLarge.copy(
                fontWeight = FontWeight.Bold
            ),
            color = GlassOnSurface()
        )
        
        Text(
            text = label,
            style = MaterialTheme.typography.bodySmall,
            color = GlassOnSurfaceVariant()
        )
    }
}

@Composable
private fun ModernAccountActions(
    currentUser: com.littlegig.app.data.model.User?,
    onPhoneLinkClick: () -> Unit,
    onBusinessUpgradeClick: () -> Unit,
    onCreateDemoUsersClick: () -> Unit,
    onCheckExistingUsersClick: () -> Unit,
    onDeleteAllUsersClick: () -> Unit,
    onSettingsClick: () -> Unit
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        // Phone linking (if not linked)
        if (currentUser?.phoneNumber.isNullOrEmpty()) {
            ModernActionButton(
                Icons.Default.Phone,
                title = "Link Phone Number",
                subtitle = "Secure your account with phone verification",
                onClick = onPhoneLinkClick
            )
        }
        
        // Business upgrade (for regular users)
        if (currentUser?.userType == UserType.REGULAR) {
            ModernActionButton(
                Icons.Default.Upgrade,
                title = "Upgrade to Business",
                subtitle = "Start creating events and earning revenue",
                onClick = onBusinessUpgradeClick
            )
        }
        
        // Create demo users (for testing)
        ModernActionButton(
            Icons.Default.Group,
            title = "Create Demo Users",
            subtitle = "Add test users for chat functionality",
            onClick = onCreateDemoUsersClick
        )
        
        // Debug: Check existing users
        ModernActionButton(
            Icons.Default.Search,
            title = "Check Existing Users",
            subtitle = "Debug: Check what users exist in database",
            onClick = onCheckExistingUsersClick
        )
        
        // Debug: Delete all users
        ModernActionButton(
            Icons.Default.Delete,
            title = "Delete All Users",
            subtitle = "Debug: Clean up database (WARNING: destructive)",
            onClick = onDeleteAllUsersClick
        )
        
        // Settings
        ModernActionButton(
            Icons.Default.Settings,
            title = "Settings",
            subtitle = "App preferences and privacy",
            onClick = onSettingsClick
        )
    }
}

@Composable
private fun ModernActionButton(
    icon: ImageVector,
    title: String,
    subtitle: String,
    onClick: () -> Unit
) {
    ModernLiquidGlassCard(
        onClick = onClick
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            ModernLiquidGlassCard() {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = GlassPrimary,
                    modifier = Modifier
                        .padding(12.dp)
                        .size(24.dp)
                )
            }
            
            Spacer(modifier = Modifier.width(16.dp))
            
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.SemiBold
                    ),
                    color = GlassOnSurface()
                )
                
                Text(
                    text = subtitle,
                    style = MaterialTheme.typography.bodySmall,
                    color = GlassOnSurfaceVariant()
                )
            }
            
            Icon(
                imageVector = Icons.Default.ChevronRight,
                contentDescription = null,
                tint = GlassOnSurfaceVariant(),
                modifier = Modifier.size(20.dp)
            )
        }
    }
}

@Composable
private fun ModernSignOutButton(
    onSignOut: () -> Unit
) {
    NeumorphicButton(
        onClick = onSignOut,
        modifier = Modifier.fillMaxWidth(),
        colors = ButtonDefaults.buttonColors(
            containerColor = GlassPink,
            contentColor = Color.White
        ),
        shape = RoundedCornerShape(20.dp)
    ) {
        Icon(
            imageVector = Icons.Default.Logout,
            contentDescription = null,
            modifier = Modifier.size(20.dp)
        )
        Spacer(modifier = Modifier.width(12.dp))
        Text(
            text = "Sign Out",
            style = MaterialTheme.typography.titleMedium.copy(
                fontWeight = FontWeight.SemiBold
            )
        )
    }
}


