package com.littlegig.app.presentation.account

import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.littlegig.app.data.model.UserType
import com.littlegig.app.data.model.UserRank
import com.littlegig.app.presentation.components.*
import com.littlegig.app.services.LocationService
import com.littlegig.app.presentation.theme.*
import androidx.compose.ui.graphics.vector.ImageVector
import android.app.Activity
import androidx.compose.foundation.clickable
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts

@Composable
fun LiquidGlassAccountScreen(
    navController: NavController,
    onSignOut: () -> Unit,
    viewModel: AccountViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState(initial = AccountUiState())
    val currentUser by viewModel.currentUser.collectAsState(initial = null)
    val isActiveNow by viewModel.isActiveNow.collectAsState()
    val locationPermissionGranted by viewModel.locationPermissionGranted.collectAsState()
    val isDark = isSystemInDarkTheme()

    // Image picker launcher
    val imagePickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        uri?.let { selectedUri ->
            viewModel.updateProfilePicture(selectedUri)
        }
    }

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
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            item {
                Spacer(modifier = Modifier.height(40.dp))
                
                // Profile header with neumorphic design
                currentUser?.let { user ->
                    AdvancedNeumorphicCard {
                        Column(
                            modifier = Modifier.padding(24.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            // Profile image with neumorphic border and upload functionality
                            Box(
                                modifier = Modifier
                                    .size(100.dp)
                                    .drawBehind {
                                        // Neumorphic shadow for profile image
                                        drawCircle(
                                            color = if (isDark) Color.Black.copy(alpha = 0.4f) else Color.Black.copy(alpha = 0.2f),
                                            center = Offset(center.x + 4.dp.toPx(), center.y + 4.dp.toPx()),
                                            radius = 50.dp.toPx()
                                        )
                                        drawCircle(
                                            color = if (isDark) Color.White.copy(alpha = 0.2f) else Color.White.copy(alpha = 0.8f),
                                            center = Offset(center.x - 4.dp.toPx(), center.y - 4.dp.toPx()),
                                            radius = 50.dp.toPx()
                                        )
                                    }
                                    .clip(CircleShape)
                                    .clickable { 
                                        imagePickerLauncher.launch("image/*")
                                    }
                            ) {
                                if (user.profileImageUrl.isNotEmpty()) {
                                    AsyncImage(
                                        model = user.profileImageUrl,
                                        contentDescription = user.displayName.ifEmpty { "User" },
                                        modifier = Modifier.fillMaxSize(),
                                        contentScale = ContentScale.Crop
                                    )
                                } else {
                                    Surface(
                                        modifier = Modifier.fillMaxSize(),
                                        color = MaterialTheme.colorScheme.surfaceVariant,
                                        shape = CircleShape
                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.Person,
                                            contentDescription = null,
                                            modifier = Modifier
                                                .fillMaxSize()
                                                .padding(24.dp),
                                            tint = MaterialTheme.colorScheme.onSurfaceVariant
                                        )
                                    }
                                }
                                
                                // Upload overlay
                                Box(
                                    modifier = Modifier
                                        .fillMaxSize()
                                        .background(Color.Black.copy(alpha = 0.3f))
                                        .padding(8.dp),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.CameraAlt,
                                        contentDescription = "Upload Photo",
                                        tint = Color.White,
                                        modifier = Modifier.size(24.dp)
                                    )
                                }
                            }
                            
                            Spacer(modifier = Modifier.height(16.dp))
                            
                            Text(
                                text = user.displayName.ifEmpty { "User" },
                                style = MaterialTheme.typography.headlineMedium.copy(
                                    fontWeight = FontWeight.Bold
                                ),
                                color = MaterialTheme.colorScheme.onSurface
                            )
                            
                            Text(
                                text = user.email,
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                            
                            Spacer(modifier = Modifier.height(12.dp))
                            
                                                       // User type badge with neumorphic effect
                           Surface(
                               modifier = Modifier
                                   .clip(RoundedCornerShape(20.dp))
                                   .drawBehind {
                                       // Neumorphic shadow for badge
                                       drawRoundRect(
                                           color = if (isDark) Color.Black.copy(alpha = 0.3f) else Color.Black.copy(alpha = 0.1f),
                                           topLeft = Offset(0f, 2.dp.toPx()),
                                           size = size,
                                           cornerRadius = CornerRadius(20.dp.toPx())
                                       )
                                   },
                               shape = RoundedCornerShape(20.dp),
                               color = LittleGigPrimary.copy(alpha = 0.1f)
                           ) {
                               Text(
                                   text = user.userType.name,
                                   modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
                                   style = MaterialTheme.typography.labelMedium,
                                   color = LittleGigPrimary
                               )
                           }
                           
                           Spacer(modifier = Modifier.height(8.dp))
                           
                           // Rank Badge
                           NeumorphicRankBadge(
                               rank = UserRank.POPULAR, // TODO: Get actual rank from user data
                               modifier = Modifier
                           )
                           
                           Spacer(modifier = Modifier.height(16.dp))
                           
                                                                                     // Active Now Toggle
                           Text(
                               text = "Active Now",
                               style = MaterialTheme.typography.bodyMedium,
                               color = MaterialTheme.colorScheme.onSurface
                           )
                           
                           Spacer(modifier = Modifier.height(8.dp))
                           
                           Text(
                               text = if (isActiveNow) "Active" else "Inactive",
                               style = MaterialTheme.typography.bodyMedium,
                               color = if (isActiveNow) LittleGigPrimary else MaterialTheme.colorScheme.onSurfaceVariant
                           )
                        }
                    }
                }
            }
            
            item {
                // Account Settings section with neumorphic header
                AdvancedGlassmorphicCard {
                    Column(
                        modifier = Modifier.padding(20.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = Icons.Default.Settings,
                                contentDescription = null,
                                tint = LittleGigPrimary,
                                modifier = Modifier.size(20.dp)
                            )
                            
                            Spacer(modifier = Modifier.width(12.dp))
                            
                            Text(
                                text = "Account Settings",
                                style = MaterialTheme.typography.titleMedium.copy(
                                    fontWeight = FontWeight.SemiBold
                                ),
                                color = MaterialTheme.colorScheme.onSurface
                            )
                        }
                        
                        Spacer(modifier = Modifier.height(16.dp))
                        
                        // Account options with neumorphic design
                        AccountOptionItem(
                            icon = Icons.Default.Person,
                            title = "Edit Profile",
                            subtitle = "Update your personal information",
                            onClick = { 
                                navController.navigate("edit_profile")
                            }
                        )
                        
                        AccountOptionItem(
                            icon = Icons.Default.Business,
                            title = "Business Dashboard",
                            subtitle = "Manage your events and analytics",
                            onClick = { 
                                navController.navigate("business_dashboard")
                            }
                        )
                        
                        AccountOptionItem(
                            icon = Icons.Default.AttachMoney,
                            title = "Revenue Analytics",
                            subtitle = "View your earnings and commissions",
                            onClick = { 
                                navController.navigate("revenue_analytics")
                            }
                        )
                    }
                }
            }
            
            item {
                // General section with neumorphic header
                AdvancedGlassmorphicCard {
                    Column(
                        modifier = Modifier.padding(20.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = Icons.Default.Settings,
                                contentDescription = null,
                                tint = LittleGigPrimary,
                                modifier = Modifier.size(20.dp)
                            )
                            
                            Spacer(modifier = Modifier.width(12.dp))
                            
                            Text(
                                text = "General",
                                style = MaterialTheme.typography.titleMedium.copy(
                                    fontWeight = FontWeight.SemiBold
                                ),
                                color = MaterialTheme.colorScheme.onSurface
                            )
                        }
                        
                        Spacer(modifier = Modifier.height(16.dp))
                        
                        // Sign out button with neumorphic effect
                        Surface(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(16.dp))
                                .drawBehind {
                                    // Neumorphic shadow for button
                                    drawRoundRect(
                                        color = if (isDark) Color.Black.copy(alpha = 0.4f) else Color.Black.copy(alpha = 0.15f),
                                        topLeft = Offset(0f, 4.dp.toPx()),
                                        size = size,
                                        cornerRadius = CornerRadius(16.dp.toPx())
                                    )
                                    drawRoundRect(
                                        color = if (isDark) Color.White.copy(alpha = 0.1f) else Color.White.copy(alpha = 0.8f),
                                        topLeft = Offset(0f, -4.dp.toPx()),
                                        size = size,
                                        cornerRadius = CornerRadius(16.dp.toPx())
                                    )
                                },
                            shape = RoundedCornerShape(16.dp),
                            color = ErrorRed.copy(alpha = 0.1f)
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(16.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Logout,
                                    contentDescription = null,
                                    tint = ErrorRed,
                                    modifier = Modifier.size(20.dp)
                                )
                                
                                Spacer(modifier = Modifier.width(12.dp))
                                
                                Text(
                                    text = "Sign Out",
                                    style = MaterialTheme.typography.bodyLarge,
                                    color = ErrorRed
                                )
                            }
                        }
                    }
                }
            }
            
            // Account linking section for anonymous users
            if (currentUser?.email.isNullOrEmpty()) {
                item {
                    AdvancedGlassmorphicCard {
                        Column(
                            modifier = Modifier.padding(20.dp)
                        ) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Link,
                                    contentDescription = null,
                                    tint = LittleGigPrimary,
                                    modifier = Modifier.size(20.dp)
                                )
                                
                                Spacer(modifier = Modifier.width(12.dp))
                                
                                Text(
                                    text = "Link Account",
                                    style = MaterialTheme.typography.titleMedium.copy(
                                        fontWeight = FontWeight.SemiBold
                                    ),
                                    color = MaterialTheme.colorScheme.onSurface
                                )
                            }
                            
                            Spacer(modifier = Modifier.height(16.dp))
                            
                            Text(
                                text = "Link your anonymous account to access all features",
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                            
                            Spacer(modifier = Modifier.height(16.dp))
                            
                            Button(
                                onClick = { viewModel.showAccountLinking() },
                                modifier = Modifier.fillMaxWidth(),
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = LittleGigPrimary
                                )
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Link,
                                    contentDescription = null
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Text("Link Account")
                            }
                        }
                    }
                }
            }
            
            // Bottom padding for navigation bar
            item {
                Spacer(modifier = Modifier.height(100.dp))
            }
        }
    }
}

@Composable
private fun AccountOptionItem(
    icon: ImageVector,
    title: String,
    subtitle: String,
    onClick: () -> Unit
) {
    val isDark = isSystemInDarkTheme()
    
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .drawBehind {
                // Subtle neumorphic shadow
                drawRoundRect(
                    color = if (isDark) Color.Black.copy(alpha = 0.2f) else Color.Black.copy(alpha = 0.08f),
                    topLeft = Offset(0f, 2.dp.toPx()),
                    size = size,
                    cornerRadius = CornerRadius(12.dp.toPx())
                )
            },
        shape = RoundedCornerShape(12.dp),
        color = if (isDark) Color(0xFF1A2332).copy(alpha = 0.6f) else Color(0xFFF8FAFF).copy(alpha = 0.8f)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = LittleGigPrimary,
                modifier = Modifier.size(24.dp)
            )
            
            Spacer(modifier = Modifier.width(16.dp))
            
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.bodyLarge.copy(
                        fontWeight = FontWeight.Medium
                    ),
                    color = MaterialTheme.colorScheme.onSurface
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
                tint = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.size(20.dp)
            )
        }
    }
    
    Spacer(modifier = Modifier.height(8.dp))
}