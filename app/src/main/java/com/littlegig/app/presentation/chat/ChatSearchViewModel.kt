package com.littlegig.app.presentation.chat

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.littlegig.app.data.model.User
import com.littlegig.app.data.model.ChatType
import com.littlegig.app.data.repository.AuthRepository
import com.littlegig.app.data.repository.ChatRepository
import com.littlegig.app.data.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ChatSearchViewModel @Inject constructor(
    private val userRepository: UserRepository,
    private val chatRepository: ChatRepository,
    private val authRepository: AuthRepository
) : ViewModel() {
    
    private val _uiState = MutableStateFlow(ChatSearchUiState())
    val uiState: StateFlow<ChatSearchUiState> = _uiState.asStateFlow()
    
    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()
    
    private val _searchResults = MutableStateFlow<List<User>>(emptyList())
    val searchResults: StateFlow<List<User>> = _searchResults.asStateFlow()
    
    val currentUser = authRepository.currentUser
    
    init {
        // Debounce search queries
        searchQuery
            .debounce(300)
            .onEach { query ->
                if (query.isNotEmpty()) {
                    performSearch(query)
                } else {
                    _searchResults.value = emptyList()
                }
            }
            .launchIn(viewModelScope)
    }
    
    fun updateSearchQuery(query: String) {
        _searchQuery.value = query
    }
    
    private suspend fun performSearch(query: String) {
        try {
            _uiState.value = _uiState.value.copy(isLoading = true)
            
            // Search by username, email, name, and phone number
            val results = userRepository.searchUsers(query)
            _searchResults.value = results
            
            _uiState.value = _uiState.value.copy(isLoading = false)
        } catch (e: Exception) {
            _uiState.value = _uiState.value.copy(
                isLoading = false,
                error = "Search failed: ${e.message}"
            )
        }
    }
    
    fun startChat(user: User) {
        viewModelScope.launch {
            try {
                val currentUser = currentUser.value ?: return@launch
                
                // Create or get existing chat
                val chatId = chatRepository.createChat(
                    participants = listOf(currentUser.id, user.id),
                    chatType = ChatType.DIRECT
                )
                
                // Navigate to chat (this will be handled by navigation)
                _uiState.value = _uiState.value.copy(
                    chatStarted = true,
                    selectedChatId = chatId.getOrNull()
                )
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    error = "Failed to start chat: ${e.message}"
                )
            }
        }
    }
    
    fun toggleFollow(user: User) {
        viewModelScope.launch {
            try {
                val currentUser = currentUser.value ?: return@launch
                
                if (currentUser.following.contains(user.id)) {
                    userRepository.unfollowUser(currentUser.id, user.id)
                } else {
                    userRepository.followUser(currentUser.id, user.id)
                }
                
                // Refresh search results to update follow status
                val currentQuery = searchQuery.value
                if (currentQuery.isNotEmpty()) {
                    performSearch(currentQuery)
                }
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    error = "Failed to update follow status: ${e.message}"
                )
            }
        }
    }
    
    fun clearError() {
        _uiState.value = _uiState.value.copy(error = null)
    }
    
    fun clearChatStarted() {
        _uiState.value = _uiState.value.copy(chatStarted = false, selectedChatId = null)
    }
}

data class ChatSearchUiState(
    val isLoading: Boolean = false,
    val error: String? = null,
    val chatStarted: Boolean = false,
    val selectedChatId: String? = null
) 