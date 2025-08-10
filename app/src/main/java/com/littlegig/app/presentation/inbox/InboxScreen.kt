package com.littlegig.app.presentation.inbox

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.MarkEmailRead
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.littlegig.app.presentation.components.AdvancedNeumorphicCard

@Composable
fun InboxScreen(viewModel: InboxViewModel = hiltViewModel()) {
    val records by viewModel.records.collectAsState()
    val loading by viewModel.loading.collectAsState()

    LaunchedEffect(Unit) { viewModel.load() }
    // Auto-mark unread as read when opening inbox
    LaunchedEffect(records) {
        val unread = records.filter { !it.isRead }.map { it.id }
        unread.forEach { viewModel.markAsRead(it) }
    }

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        Text(text = "Inbox", style = MaterialTheme.typography.headlineMedium)
        Spacer(Modifier.height(12.dp))
        if (loading) {
            Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) { CircularProgressIndicator() }
        } else {
            LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                items(records) { rec ->
                    AdvancedNeumorphicCard {
                        Row(modifier = Modifier.fillMaxWidth().padding(12.dp), verticalAlignment = Alignment.CenterVertically) {
                            Column(Modifier.weight(1f)) {
                                Text(text = rec.title, style = MaterialTheme.typography.titleSmall)
                                Text(text = rec.body, style = MaterialTheme.typography.bodySmall)
                            }
                            IconButton(onClick = { viewModel.markAsRead(rec.id) }) { Icon(Icons.Default.MarkEmailRead, contentDescription = null) }
                            IconButton(onClick = { viewModel.delete(rec.id) }) { Icon(Icons.Default.Delete, contentDescription = null) }
                        }
                    }
                }
            }
        }
    }
}