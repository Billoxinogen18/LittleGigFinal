package com.littlegig.app.presentation.account

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.littlegig.app.data.model.UserType
import com.littlegig.app.data.repository.AuthRepository
// import com.littlegig.app.services.LocationService
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AccountViewModel @Inject constructor(
    private val authRepository: AuthRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(AccountUiState())
    val uiState: StateFlow<AccountUiState> = _uiState.asStateFlow()

    val currentUser = authRepository.currentUser

    fun upgradeToBusinessAccount() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, error = null, isSuccess = false)
            
            try {
                val user = authRepository.currentUser.first()
                if (user != null) {
                    val updatedUser = user.copy(
                        userType = UserType.BUSINESS,
                        updatedAt = System.currentTimeMillis()
                    )
                    
                    authRepository.updateUserProfile(updatedUser)
                        .onSuccess {
                            _uiState.value = _uiState.value.copy(
                                isLoading = false,
                                isSuccess = true
                            )
                        }
                        .onFailure { error ->
                            _uiState.value = _uiState.value.copy(
                                isLoading = false,
                                error = error.message
                            )
                        }
                } else {
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        error = "User not found"
                    )
                }
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    error = e.message
                )
            }
        }
    }

    fun becomeInfluencer() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, error = null, isSuccess = false)
            
            try {
                val user = authRepository.currentUser.first()
                if (user != null) {
                    val updatedUser = user.copy(
                        isInfluencer = true,
                        updatedAt = System.currentTimeMillis()
                    )
                    
                    authRepository.updateUserProfile(updatedUser)
                        .onSuccess {
                            _uiState.value = _uiState.value.copy(
                                isLoading = false,
                                isSuccess = true
                            )
                        }
                        .onFailure { error ->
                            _uiState.value = _uiState.value.copy(
                                isLoading = false,
                                error = error.message
                            )
                        }
                } else {
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        error = "User not found"
                    )
                }
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    error = e.message
                )
            }
        }
    }

    fun signOut() {
        viewModelScope.launch {
            authRepository.signOut()
        }
    }

    fun clearError() {
        _uiState.value = _uiState.value.copy(error = null)
    }

    fun clearSuccess() {
        _uiState.value = _uiState.value.copy(isSuccess = false)
    }
    
    fun toggleActiveNow(isActive: Boolean) {
        // TODO: Implement Active Now functionality
        viewModelScope.launch {
            // Placeholder for Active Now toggle
        }
    }
}

data class AccountUiState(
    val isLoading: Boolean = false,
    val error: String? = null,
    val isSuccess: Boolean = false
)