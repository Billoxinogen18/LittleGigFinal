package com.littlegig.app.presentation.chat

import android.Manifest
import android.content.pm.PackageManager
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Contacts
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.littlegig.app.presentation.components.AdvancedGlassmorphicCard
import com.littlegig.app.presentation.components.AdvancedNeumorphicCard
import com.littlegig.app.presentation.components.HapticButton
import com.littlegig.app.presentation.theme.LittleGigPrimary

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PeopleDiscoveryScreen(
    navController: NavController,
    viewModel: ChatViewModel = hiltViewModel()
) {
    val isDark = isSystemInDarkTheme()
    val context = LocalContext.current

    var searchQuery by remember { mutableStateOf("") }
    var showContactsOnly by remember { mutableStateOf(true) }

    val contactsUsers by viewModel.contactsUsers.collectAsState()
    val allUsers by viewModel.allUsers.collectAsState()
    val searchResults by viewModel.searchResults.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.loadAllUsers()
    }

    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { granted ->
        if (granted) {
            val phones = fetchPhoneNumbersFromContacts(context)
            viewModel.loadContacts(phones)
        }
    }

    LaunchedEffect(showContactsOnly) {
        if (showContactsOnly) {
            val hasPermission = ContextCompat.checkSelfPermission(
                context, Manifest.permission.READ_CONTACTS
            ) == PackageManager.PERMISSION_GRANTED
            if (hasPermission) {
                val phones = fetchPhoneNumbersFromContacts(context)
                viewModel.loadContacts(phones)
            } else {
                permissionLauncher.launch(Manifest.permission.READ_CONTACTS)
            }
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                if (isDark) Color(0xFF0F0F23) else Color(0xFFF8FAFC)
            )
    ) {
        Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
            AdvancedNeumorphicCard {
                Column(Modifier.padding(16.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        IconButton(onClick = { navController.popBackStack() }) {
                            Icon(Icons.Default.ArrowBack, contentDescription = "Back", tint = LittleGigPrimary)
                        }
                        Spacer(Modifier.width(8.dp))
                        Text(
                            text = "Start a Chat",
                            style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold)
                        )
                    }

                    Spacer(Modifier.height(12.dp))

                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        FilterChip(
                            selected = showContactsOnly,
                            onClick = { showContactsOnly = true },
                            label = { Text("Contacts on LittleGig") }
                        )
                        FilterChip(
                            selected = !showContactsOnly,
                            onClick = { showContactsOnly = false },
                            label = { Text("All Users") }
                        )
                    }

                    Spacer(Modifier.height(12.dp))

                    OutlinedTextField(
                        value = searchQuery,
                        onValueChange = {
                            searchQuery = it
                            viewModel.searchUsers(it)
                        },
                        placeholder = { Text("Search people by name, username, or email") },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp)
                    )
                }
            }

            Spacer(Modifier.height(16.dp))

            val baseUsers = remember(searchQuery, showContactsOnly, contactsUsers, allUsers, searchResults) {
                if (searchQuery.isBlank()) {
                    if (showContactsOnly) contactsUsers else allUsers
                } else {
                    if (showContactsOnly) {
                        val ids = contactsUsers.map { it.id }.toSet()
                        searchResults.filter { ids.contains(it.id) }
                    } else searchResults
                }
            }

            if (baseUsers.isEmpty()) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    AdvancedGlassmorphicCard {
                        Column(Modifier.padding(24.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                            Icon(
                                imageVector = if (showContactsOnly) Icons.Default.Contacts else Icons.Default.Person,
                                contentDescription = null,
                                tint = LittleGigPrimary,
                                modifier = Modifier.size(48.dp)
                            )
                            Spacer(Modifier.height(12.dp))
                            Text(
                                text = if (searchQuery.isBlank()) {
                                    if (showContactsOnly) "No contacts on LittleGig yet" else "No users available"
                                } else {
                                    "No matches for \"$searchQuery\""
                                },
                                style = MaterialTheme.typography.titleMedium
                            )
                            Spacer(Modifier.height(8.dp))
                            Text(
                                text = if (showContactsOnly) "Grant contacts permission to find friends automatically" else "Try a different query",
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                }
            } else {
                AdvancedGlassmorphicCard {
                    Column(Modifier.padding(12.dp)) {
                        val listState = androidx.compose.foundation.lazy.rememberLazyListState()
                        LazyColumn(state = listState, verticalArrangement = Arrangement.spacedBy(8.dp)) {
                            items(baseUsers) { user ->
                                HapticButton(onClick = {
                                    viewModel.createChatWithUser(user.id)
                                    navController.popBackStack()
                                }) {
                                    AdvancedNeumorphicCard {
                                        Row(
                                            modifier = Modifier.fillMaxWidth().padding(12.dp),
                                            verticalAlignment = Alignment.CenterVertically
                                        ) {
                                            Box(
                                                modifier = Modifier.size(44.dp).clip(CircleShape).background(LittleGigPrimary.copy(alpha = 0.12f)),
                                                contentAlignment = Alignment.Center
                                            ) {
                                                if (user.profilePictureUrl.isNotEmpty()) {
                                                    AsyncImage(
                                                        model = user.profilePictureUrl,
                                                        contentDescription = null,
                                                        modifier = Modifier.fillMaxSize(),
                                                        contentScale = ContentScale.Crop
                                                    )
                                                } else {
                                                    Icon(Icons.Default.Person, contentDescription = null, tint = LittleGigPrimary)
                                                }
                                            }
                                            Spacer(Modifier.width(12.dp))
                                            Column(Modifier.weight(1f)) {
                                                Text(user.displayName.ifEmpty { user.name }, style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Medium))
                                                if (user.email.isNotBlank()) {
                                                    Text(user.email, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}