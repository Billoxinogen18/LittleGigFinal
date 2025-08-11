package com.littlegig.app.presentation.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.littlegig.app.data.model.User
import com.littlegig.app.data.repository.AuthRepository
import com.littlegig.app.data.repository.UserRepository
import com.littlegig.app.data.repository.UserStats
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val userRepository: UserRepository,
    private val authRepository: AuthRepository
) : ViewModel() {

    private val _user = MutableStateFlow<User?>(null)
    val user: StateFlow<User?> = _user.asStateFlow()

    private val _isFollowing = MutableStateFlow<Boolean>(false)
    val isFollowing: StateFlow<Boolean> = _isFollowing.asStateFlow()

    private val _stats = MutableStateFlow<UserStats?>(null)
    val stats: StateFlow<UserStats?> = _stats.asStateFlow()

    private val _loading = MutableStateFlow(false)
    val loading: StateFlow<Boolean> = _loading.asStateFlow()

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error.asStateFlow()

    val currentUserId: String?
        get() = authRepository.currentUser.value?.id

    fun load(username: String) {
        viewModelScope.launch {
            _loading.value = true
            _error.value = null
            try {
                val res = userRepository.getUserByUsername(username)
                val u = res.getOrNull()
                _user.value = u
                _loading.value = false
                if (u != null) {
                    refreshFollowState(u.id)
                    refreshStats(u.id)
                }
            } catch (e: Exception) {
                _loading.value = false
                _error.value = e.message
            }
        }
    }

    fun toggleFollow() {
        val me = currentUserId ?: return
        val target = _user.value?.id ?: return
        viewModelScope.launch {
            val res = userRepository.toggleFollow(me, target)
            res.onSuccess {
                refreshFollowState(target)
                refreshStats(target)
            }.onFailure { _error.value = it.message }
        }
    }

    private fun refreshFollowState(targetId: String) {
        val me = currentUserId ?: return
        viewModelScope.launch {
            val res = userRepository.isFollowing(me, targetId)
            _isFollowing.value = res.getOrDefault(false)
        }
    }

    private fun refreshStats(userId: String) {
        viewModelScope.launch {
            val res = userRepository.getUserStats(userId)
            _stats.value = res.getOrNull()
        }
    }
}