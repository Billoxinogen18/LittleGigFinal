package com.littlegig.app.presentation.chat

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.littlegig.app.data.model.Chat
import com.littlegig.app.data.model.User
import com.littlegig.app.data.model.ChatType
import com.littlegig.app.data.repository.AuthRepository
import com.littlegig.app.data.repository.ChatRepository
import com.littlegig.app.data.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ChatViewModel @Inject constructor(
    private val chatRepository: ChatRepository,
    private val userRepository: UserRepository,
    private val authRepository: AuthRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(ChatUiState())
    val uiState: StateFlow<ChatUiState> = _uiState.asStateFlow()

    private val _chats = MutableStateFlow<List<Chat>>(emptyList())
    val chats: StateFlow<List<Chat>> = _chats.asStateFlow()

    private val _searchResults = MutableStateFlow<List<User>>(emptyList())
    val searchResults: StateFlow<List<User>> = _searchResults.asStateFlow()

    fun loadChats() {
        viewModelScope.launch {
            try {
                // For now, just set empty list since we don't have real data
                _chats.value = emptyList()
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    error = "Failed to load chats: ${e.message}"
                )
            }
        }
    }

    fun searchUsers(query: String) {
        viewModelScope.launch {
            try {
                if (query.isBlank()) {
                    _searchResults.value = emptyList()
                    return@launch
                }
                
                // For now, return empty list since we don't have real data
                _searchResults.value = emptyList()
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    error = "Failed to search users: ${e.message}"
                )
            }
        }
    }

    fun createChat(userId: String) {
        viewModelScope.launch {
            try {
                val currentUser = authRepository.currentUser.first()
                if (currentUser != null) {
                    chatRepository.createChat(
                        participants = listOf(currentUser.id, userId),
                        chatType = ChatType.DIRECT,
                        name = null
                    )
                    // Refresh chats list
                    loadChats()
                }
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    error = "Failed to create chat: ${e.message}"
                )
            }
        }
    }

    fun clearError() {
        _uiState.value = _uiState.value.copy(error = null)
    }

    fun clearSearchResults() {
        _searchResults.value = emptyList()
    }
    
    fun startNewChat() {
        // This will trigger the search UI
        _searchResults.value = emptyList()
    }
    
    fun createChatWithUser(userId: String) {
        viewModelScope.launch {
            try {
                val currentUser = authRepository.currentUser.first()
                if (currentUser != null) {
                    chatRepository.createChat(
                        participants = listOf(currentUser.id, userId),
                        chatType = ChatType.DIRECT,
                        name = null
                    )
                    // Refresh chats list
                    loadChats()
                }
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    error = "Failed to create chat: ${e.message}"
                )
            }
        }
    }
}

data class ChatUiState(
    val isLoading: Boolean = false,
    val error: String? = null,
    val isSuccess: Boolean = false
) 