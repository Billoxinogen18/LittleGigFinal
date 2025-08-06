package com.littlegig.app.presentation.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.littlegig.app.data.model.User
import com.littlegig.app.data.model.UserType
import com.littlegig.app.data.repository.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val authRepository: AuthRepository
) : ViewModel() {
    
    private val _uiState = MutableStateFlow(AuthUiState())
    val uiState: StateFlow<AuthUiState> = _uiState.asStateFlow()
    
    val currentUser = authRepository.currentUser
    
    fun signInWithEmail(email: String, password: String) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, error = null)
            
            authRepository.signInWithEmail(email, password)
                .onSuccess {
                    _uiState.value = _uiState.value.copy(isLoading = false)
                }
                .onFailure { error ->
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        error = error.message
                    )
                }
        }
    }
    
    fun signUpWithEmail(
        email: String, 
        password: String, 
        displayName: String,
        phoneNumber: String,
        userType: UserType
    ) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, error = null)
            
            authRepository.signUpWithEmail(email, password, displayName, phoneNumber, userType)
                .onSuccess {
                    _uiState.value = _uiState.value.copy(isLoading = false)
                }
                .onFailure { error ->
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        error = error.message
                    )
                }
        }
    }
    
    fun signInWithGoogle() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, error = null)
            
            // This will be handled by the activity's onActivityResult
            // The actual Google Sign-In will be initiated from the activity
            _uiState.value = _uiState.value.copy(isLoading = false)
        }
    }
    
    fun signInWithGoogle(account: GoogleSignInAccount) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, error = null)
            
            authRepository.signInWithGoogle(account)
                .onSuccess {
                    _uiState.value = _uiState.value.copy(isLoading = false)
                }
                .onFailure { error ->
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        error = error.message
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
}

data class AuthUiState(
    val isLoading: Boolean = false,
    val error: String? = null
)