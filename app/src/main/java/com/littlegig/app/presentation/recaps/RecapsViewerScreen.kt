package com.littlegig.app.presentation.recaps

import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.littlegig.app.data.model.Recap
import com.littlegig.app.data.repository.RecapRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import androidx.lifecycle.viewModelScope

@Composable
fun RecapsViewerScreen(eventId: String, viewModel: RecapsViewerViewModel = hiltViewModel()) {
    val recaps by viewModel.recaps
    val index by viewModel.currentIndex
    val progress by viewModel.progress

    LaunchedEffect(eventId) { viewModel.load(eventId) }

    Box(Modifier.fillMaxSize().background(Color.Black)) {
        if (recaps.isNotEmpty()) {
            val recap = recaps.getOrNull(index)
            recap?.let {
                AsyncImage(
                    model = it.mediaUrls.firstOrNull(),
                    contentDescription = null,
                    modifier = Modifier.fillMaxSize()
                )
            }
            Row(Modifier.fillMaxWidth().padding(8.dp), horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                repeat(recaps.size) { i ->
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .height(3.dp)
                            .background(if (i <= index) Color.White else Color.White.copy(alpha = 0.3f))
                    )
                }
            }
            Box(Modifier.fillMaxSize()) {
                Row(Modifier.fillMaxSize()) {
                    Box(Modifier.weight(1f).fillMaxHeight().clickable { viewModel.prev() })
                    Box(Modifier.weight(1f).fillMaxHeight().clickable { viewModel.next() })
                }
                Text(text = "${index + 1}/${recaps.size}", color = Color.White, modifier = Modifier.align(Alignment.BottomCenter).padding(16.dp))
            }
        } else {
            Text(text = "No recaps", color = Color.White, modifier = Modifier.align(Alignment.Center))
        }
    }
}

@HiltViewModel
class RecapsViewerViewModel @Inject constructor(
    private val recapRepository: RecapRepository
) : ViewModel() {
    private val _recaps = mutableStateOf<List<Recap>>(emptyList())
    val recaps: State<List<Recap>> = _recaps

    private val _currentIndex = mutableStateOf(0)
    val currentIndex: State<Int> = _currentIndex

    private val _progress = mutableStateOf(0f)
    val progress: State<Float> = _progress

    fun load(eventId: String) {
        viewModelScope.launch {
            recapRepository.getEventRecaps(eventId).collect { list ->
                _recaps.value = list
                _currentIndex.value = 0
            }
        }
        viewModelScope.launch { autoAdvance() }
    }

    private suspend fun autoAdvance() {
        while (true) {
            if (_recaps.value.isEmpty()) { delay(250); continue }
            _progress.value = 0f
            repeat(100) {
                delay(50)
                _progress.value += 0.01f
            }
            next()
        }
    }

    fun next() {
        val size = _recaps.value.size
        if (size == 0) return
        _currentIndex.value = (_currentIndex.value + 1) % size
    }

    fun prev() {
        val size = _recaps.value.size
        if (size == 0) return
        _currentIndex.value = if (_currentIndex.value == 0) size - 1 else _currentIndex.value - 1
    }
}