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
    private val _contactsUsers = MutableStateFlow<List<User>>(emptyList())
    val contactsUsers: StateFlow<List<User>> = _contactsUsers.asStateFlow()
    private val _allUsers = MutableStateFlow<List<User>>(emptyList())
    val allUsers: StateFlow<List<User>> = _allUsers.asStateFlow()

    // Paging state
    private var lastAllUsersId: String? = null
    private var hasMoreAllUsers: Boolean = true
    private var isLoadingUsersPage: Boolean = false

    private var lastChatsTime: Long? = null
    private var hasMoreChats: Boolean = true
    private var isLoadingChatsPage: Boolean = false

    fun loadChats() {
        viewModelScope.launch {
            try {
                val user = authRepository.currentUser.first() ?: return@launch
                chatRepository.getUserChats(user.id).collect { list ->
                    _chats.value = list
                    // track last timestamp for pagination
                    lastChatsTime = list.lastOrNull()?.lastMessageTime
                }
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
                    // default to all users if loaded, else contacts
                    _searchResults.value = if (_allUsers.value.isNotEmpty()) _allUsers.value else _contactsUsers.value
                    return@launch
                }
                _searchResults.value = userRepository.searchUsers(query)
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    error = "Failed to search users: ${e.message}"
                )
            }
        }
    }

    fun loadContacts(contactsPhones: List<String>) {
        viewModelScope.launch {
            val users = userRepository.getUsersByPhoneNumbers(contactsPhones).getOrElse { emptyList() }
            _contactsUsers.value = users
            println("🔥 DEBUG: Contacts on LittleGig loaded: ${users.size}")
            if (!_uiState.value.isSearching) _searchResults.value = users
        }
    }

    fun loadAllUsers(limit: Int = 50) {
        viewModelScope.launch {
            if (isLoadingUsersPage) return@launch
            isLoadingUsersPage = true
            val users = userRepository.listUsersPage(limit, null).getOrElse { emptyList() }
            _allUsers.value = users
            lastAllUsersId = users.lastOrNull()?.id
            hasMoreAllUsers = users.size >= limit
            println("🔥 DEBUG: All users first page loaded: ${users.size}")
            if (!_uiState.value.isSearching) _searchResults.value = users
            isLoadingUsersPage = false
        }
    }

    fun loadMoreAllUsers(limit: Int = 50) {
        viewModelScope.launch {
            if (isLoadingUsersPage || !hasMoreAllUsers) return@launch
            isLoadingUsersPage = true
            val next = userRepository.listUsersPage(limit, lastAllUsersId).getOrElse { emptyList() }
            val combined = (_allUsers.value + next).distinctBy { it.id }
            _allUsers.value = combined
            if (!_uiState.value.isSearching) _searchResults.value = combined
            lastAllUsersId = next.lastOrNull()?.id
            hasMoreAllUsers = next.size >= limit
            println("🔥 DEBUG: Loaded more users: ${next.size}, total=${combined.size}")
            isLoadingUsersPage = false
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

    fun loadMoreChats(limit: Int = 30) {
        viewModelScope.launch {
            try {
                if (isLoadingChatsPage || !hasMoreChats) return@launch
                isLoadingChatsPage = true
                val user = authRepository.currentUser.first() ?: return@launch
                val page = chatRepository.getUserChatsPage(user.id, limit, lastChatsTime).getOrElse { emptyList() }
                val combined = (_chats.value + page).distinctBy { it.id }
                _chats.value = combined.sortedByDescending { it.lastMessageTime }
                lastChatsTime = page.lastOrNull()?.lastMessageTime ?: lastChatsTime
                hasMoreChats = page.size >= limit
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    error = "Failed to load more chats: ${e.message}"
                )
            } finally {
                isLoadingChatsPage = false
            }
        }
    }

    fun blockUser(targetUserId: String) {
        viewModelScope.launch {
            try {
                val me = authRepository.currentUser.first() ?: return@launch
                userRepository.blockUser(me.id, targetUserId)
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(error = "Failed to block user: ${e.message}")
            }
        }
    }

    fun reportUser(targetUserId: String, reason: String) {
        viewModelScope.launch {
            try {
                val me = authRepository.currentUser.first() ?: return@launch
                userRepository.reportUser(me.id, targetUserId, reason)
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(error = "Failed to report user: ${e.message}")
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
        // Expose UI signal if needed later; keep clearing results
        _uiState.value = _uiState.value.copy(isSearching = true)
        _searchResults.value = if (_allUsers.value.isNotEmpty()) _allUsers.value else _contactsUsers.value
    }
    
    fun createChatWithUser(userId: String) {
        viewModelScope.launch {
            try {
                val currentUser = authRepository.currentUser.first()
                if (currentUser != null) {
                    chatRepository.createOrGetDirectChat(currentUser.id, userId)
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

    init {
        loadChats()
    }
}

data class ChatUiState(
    val isLoading: Boolean = false,
    val error: String? = null,
    val isSuccess: Boolean = false,
    val isSearching: Boolean = false
) 