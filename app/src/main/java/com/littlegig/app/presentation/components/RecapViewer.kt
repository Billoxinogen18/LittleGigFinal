package com.littlegig.app.presentation.components

import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import coil.compose.AsyncImage
import com.littlegig.app.data.model.Recap
import com.littlegig.app.data.model.RecapViewerState
import com.littlegig.app.data.model.MediaType
import com.littlegig.app.presentation.theme.*
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun NeumorphicRecapViewer(
    recaps: List<Recap>,
    onDismiss: () -> Unit,
    onLikeRecap: (String) -> Unit,
    onViewRecap: (String) -> Unit
) {
    var currentIndex by remember { mutableStateOf(0) }
    var isPlaying by remember { mutableStateOf(true) }
    var progress by remember { mutableStateOf(0f) }
    val coroutineScope = rememberCoroutineScope()
    
    if (recaps.isEmpty()) return
    
    val currentRecap = recaps[currentIndex]
    
    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(
            dismissOnBackPress = true,
            dismissOnClickOutside = false,
            usePlatformDefaultWidth = false
        )
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black.copy(alpha = 0.9f))
        ) {
            // Progress bars at the top
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
                    .padding(top = 32.dp)
            ) {
                recaps.forEachIndexed { index, _ ->
                    val isCurrent = index == currentIndex
                    val isCompleted = index < currentIndex
                    
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .height(3.dp)
                            .padding(horizontal = 2.dp)
                            .clip(RoundedCornerShape(2.dp))
                            .background(
                                when {
                                    isCompleted -> LittleGigPrimary
                                    isCurrent -> LittleGigPrimary.copy(alpha = 0.5f)
                                    else -> Color.White.copy(alpha = 0.3f)
                                }
                            )
                    ) {
                        if (isCurrent) {
                            Box(
                                modifier = Modifier
                                    .fillMaxHeight()
                                    .fillMaxWidth(progress)
                                    .background(LittleGigPrimary)
                            )
                        }
                    }
                }
            }
            
            // Header with user info and close button
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
                    .padding(top = 80.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // User avatar
                    AsyncImage(
                        model = currentRecap.userImageUrl,
                        contentDescription = null,
                        modifier = Modifier
                            .size(40.dp)
                            .clip(CircleShape),
                        contentScale = ContentScale.Crop
                    )
                    
                    Spacer(modifier = Modifier.width(12.dp))
                    
                    Column {
                        Text(
                            text = currentRecap.userName,
                            style = MaterialTheme.typography.bodyLarge.copy(
                                fontWeight = FontWeight.Bold
                            ),
                            color = Color.White
                        )
                        
                        Text(
                            text = "Event Recap",
                            style = MaterialTheme.typography.bodySmall,
                            color = Color.White.copy(alpha = 0.7f)
                        )
                    }
                }
                
                // Close button
                Surface(
                    onClick = onDismiss,
                    modifier = Modifier
                        .size(40.dp)
                        .clip(CircleShape),
                    color = Color.Black.copy(alpha = 0.5f)
                ) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = "Close",
                        tint = Color.White,
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(8.dp)
                    )
                }
            }
            
            // Main content area
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp)
                    .padding(top = 140.dp, bottom = 100.dp)
                    .pointerInput(Unit) {
                        detectTapGestures(
                            onTap = { offset ->
                                val screenWidth = size.width
                                val tapX = offset.x
                                
                                when {
                                    tapX < screenWidth * 0.3f -> {
                                        // Tap left - previous recap
                                        if (currentIndex > 0) {
                                            currentIndex--
                                            progress = 0f
                                        }
                                    }
                                    tapX > screenWidth * 0.7f -> {
                                        // Tap right - next recap
                                        if (currentIndex < recaps.size - 1) {
                                            currentIndex++
                                            progress = 0f
                                        } else {
                                            onDismiss()
                                        }
                                    }
                                    else -> {
                                        // Tap center - toggle play/pause
                                        isPlaying = !isPlaying
                                    }
                                }
                            }
                        )
                    }
            ) {
                // Media content
                when (currentRecap.mediaType) {
                    MediaType.IMAGE -> {
                        AsyncImage(
                            model = currentRecap.mediaUrls.firstOrNull(),
                            contentDescription = null,
                            modifier = Modifier.fillMaxSize(),
                            contentScale = ContentScale.Crop
                        )
                    }
                    MediaType.VIDEO -> {
                        // Video player would go here
                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.PlayArrow,
                                contentDescription = null,
                                tint = Color.White,
                                modifier = Modifier.size(64.dp)
                            )
                        }
                    }
                }
                
                // Caption overlay
                if (currentRecap.caption.isNotBlank()) {
                    Box(
                        modifier = Modifier
                            .align(Alignment.BottomStart)
                            .padding(16.dp)
                    ) {
                        Text(
                            text = currentRecap.caption,
                            style = MaterialTheme.typography.bodyMedium,
                            color = Color.White,
                            modifier = Modifier
                                .background(
                                    Color.Black.copy(alpha = 0.5f),
                                    RoundedCornerShape(8.dp)
                                )
                                .padding(12.dp)
                        )
                    }
                }
            }
            
            // Bottom controls
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.BottomCenter)
                    .padding(16.dp),
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Like button
                Surface(
                    onClick = { onLikeRecap(currentRecap.id) },
                    modifier = Modifier.size(48.dp),
                    shape = CircleShape,
                    color = Color.Black.copy(alpha = 0.5f)
                ) {
                    Icon(
                        imageVector = Icons.Default.Favorite,
                        contentDescription = "Like",
                        tint = Color.White,
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(12.dp)
                    )
                }
                
                // Play/Pause button
                Surface(
                    onClick = { isPlaying = !isPlaying },
                    modifier = Modifier.size(48.dp),
                    shape = CircleShape,
                    color = Color.Black.copy(alpha = 0.5f)
                ) {
                    Icon(
                        imageVector = if (isPlaying) Icons.Default.Pause else Icons.Default.PlayArrow,
                        contentDescription = if (isPlaying) "Pause" else "Play",
                        tint = Color.White,
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(12.dp)
                    )
                }
                
                // Share button
                Surface(
                    onClick = { /* Share functionality */ },
                    modifier = Modifier.size(48.dp),
                    shape = CircleShape,
                    color = Color.Black.copy(alpha = 0.5f)
                ) {
                    Icon(
                        imageVector = Icons.Default.Share,
                        contentDescription = "Share",
                        tint = Color.White,
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(12.dp)
                    )
                }
            }
        }
    }
    
    // Auto-play logic
    LaunchedEffect(currentIndex, isPlaying) {
        if (isPlaying) {
            progress = 0f
            while (progress < 1f) {
                delay(50) // Update every 50ms
                progress += 0.01f // 5 seconds total
            }
            
            // Move to next recap
            if (currentIndex < recaps.size - 1) {
                currentIndex++
            } else {
                onDismiss()
            }
        }
    }
    
    // Track view
    LaunchedEffect(currentRecap.id) {
        onViewRecap(currentRecap.id)
    }
} 