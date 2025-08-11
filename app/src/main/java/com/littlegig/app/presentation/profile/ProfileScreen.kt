package com.littlegig.app.presentation.profile

import androidx.compose.foundation.Image
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.littlegig.app.presentation.components.AdvancedGlassmorphicCard
import com.littlegig.app.presentation.components.AdvancedNeumorphicCard
import com.littlegig.app.presentation.components.HapticButton
import com.littlegig.app.presentation.theme.LittleGigPrimary

@Composable
fun ProfileScreen(
    navController: NavController,
    username: String,
    viewModel: ProfileViewModel = hiltViewModel()
) {
    val isDark = isSystemInDarkTheme()
    val userState = viewModel.user.collectAsState()
    val user = userState.value
    val statsState = viewModel.stats.collectAsState()
    val stats = statsState.value
    val isFollowingState = viewModel.isFollowing.collectAsState()
    val isFollowing = isFollowingState.value
    val loadingState = viewModel.loading.collectAsState()
    val loading = loadingState.value

    LaunchedEffect(username) { viewModel.load(username) }

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        AdvancedNeumorphicCard {
            Row(Modifier.fillMaxWidth().padding(12.dp), verticalAlignment = Alignment.CenterVertically) {
                IconButton(onClick = { navController.popBackStack() }) {
                    Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                }
                Spacer(Modifier.width(8.dp))
                Text(text = "Profile", style = MaterialTheme.typography.titleMedium)
                Spacer(Modifier.weight(1f))
                IconButton(onClick = { /* navigate to edit if self */ }) {
                    Icon(Icons.Default.Edit, contentDescription = "Edit")
                }
            }
        }
        Spacer(Modifier.height(16.dp))
        AdvancedGlassmorphicCard {
            Column(Modifier.fillMaxWidth().padding(16.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                AsyncImage(
                    model = user?.profileImageUrl,
                    contentDescription = null,
                    modifier = Modifier.size(92.dp).clip(CircleShape)
                )
                Spacer(Modifier.height(12.dp))
                Text(text = user?.displayName.orEmpty(), style = MaterialTheme.typography.titleMedium, color = MaterialTheme.colorScheme.onSurface)
                Text(text = "@${user?.username.orEmpty()}", style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
                Spacer(Modifier.height(12.dp))
                Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    StatPill(label = "Followers", value = stats?.followersCount ?: 0)
                    StatPill(label = "Following", value = stats?.followingCount ?: 0)
                    val eventsTotal = (stats?.eventsCreated ?: 0) + (stats?.eventsAttended ?: 0)
                    StatPill(label = "Events", value = eventsTotal)
                }
                Spacer(Modifier.height(16.dp))
                HapticButton(onClick = { viewModel.toggleFollow() }) {
                    AdvancedNeumorphicCard {
                        Text(
                            text = if (isFollowing) "Following" else "Follow",
                            modifier = Modifier.padding(horizontal = 20.dp, vertical = 10.dp),
                            color = LittleGigPrimary
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun StatPill(label: String, value: Int) {
    AdvancedNeumorphicCard(cornerRadius = 16.dp) {
        Row(Modifier.padding(horizontal = 12.dp, vertical = 8.dp), verticalAlignment = Alignment.CenterVertically) {
            Text(text = value.toString(), style = MaterialTheme.typography.titleSmall, color = MaterialTheme.colorScheme.onSurface)
            Spacer(Modifier.width(6.dp))
            Text(text = label, style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
        }
    }
}