package com.littlegig.app.presentation.inbox

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.littlegig.app.data.repository.NotificationRepository
import com.littlegig.app.data.repository.AuthRepository
import com.littlegig.app.data.repository.NotificationRecord
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class InboxViewModel @Inject constructor(
    private val notificationRepository: NotificationRepository,
    private val authRepository: AuthRepository
) : ViewModel() {
    private val _records = MutableStateFlow<List<NotificationRecord>>(emptyList())
    val records: StateFlow<List<NotificationRecord>> = _records.asStateFlow()

    private val _loading = MutableStateFlow(false)
    val loading: StateFlow<Boolean> = _loading.asStateFlow()

    private val _unreadCount = MutableStateFlow(0)
    val unreadCount: StateFlow<Int> = _unreadCount.asStateFlow()

    fun load() {
        viewModelScope.launch {
            val user = authRepository.currentUser.value ?: return@launch
            _loading.value = true
            notificationRepository.getNotificationHistory(user.id).collectLatest {
                _records.value = it
                _unreadCount.value = it.count { rec -> !rec.isRead }
                _loading.value = false
            }
        }
    }

    fun markAsRead(id: String) {
        viewModelScope.launch { notificationRepository.markNotificationAsRead(id) }
    }

    fun delete(id: String) {
        viewModelScope.launch { notificationRepository.deleteNotification(id) }
    }
}