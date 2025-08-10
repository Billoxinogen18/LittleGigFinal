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
import timber.log.Timber

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val authRepository: AuthRepository
) : ViewModel() {
    
    private val _uiState = MutableStateFlow(AuthUiState())
    val uiState: StateFlow<AuthUiState> = _uiState.asStateFlow()
    
    val currentUser = authRepository.currentUser
    
    // 🔥 ANONYMOUS AUTHENTICATION - TIKTOK STYLE! 🔥
    fun signInAnonymously() {
        viewModelScope.launch {
            Timber.i("Auth: signInAnonymously start")
            _uiState.value = _uiState.value.copy(isLoading = true, error = null)
            
            authRepository.signInAnonymously()
                .onSuccess {
                    Timber.i("Auth: signInAnonymously success")
                    _uiState.value = _uiState.value.copy(isLoading = false)
                }
                .onFailure { error ->
                    Timber.w(error, "Auth: signInAnonymously failed")
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        error = error.message
                    )
                }
        }
    }
    
    // 🧠 SMART ACCOUNT LINKING - PRESERVE ALGORITHM DATA! 🧠
    fun linkAnonymousAccount(
        email: String, 
        password: String, 
        displayName: String,
        phoneNumber: String? = null,
        userType: UserType = UserType.REGULAR
    ) {
        viewModelScope.launch {
            Timber.i("Auth: linkAnonymousAccount email start")
            _uiState.value = _uiState.value.copy(isLoading = true, error = null)
            
            authRepository.linkAnonymousAccount(email, password, displayName, phoneNumber, userType)
                .onSuccess {
                    Timber.i("Auth: linkAnonymousAccount email success")
                    _uiState.value = _uiState.value.copy(isLoading = false)
                }
                .onFailure { error ->
                    Timber.w(error, "Auth: linkAnonymousAccount email failed")
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        error = error.message
                    )
                }
        }
    }
    
    // 🧠 SMART PHONE ACCOUNT LINKING! 🧠
    fun linkAnonymousAccountWithPhone(
        phoneNumber: String,
        displayName: String,
        userType: UserType = UserType.REGULAR
    ) {
        viewModelScope.launch {
            Timber.i("Auth: linkAnonymousAccount phone start")
            _uiState.value = _uiState.value.copy(isLoading = true, error = null)
            
            authRepository.linkAnonymousAccountWithPhone(phoneNumber, displayName, userType)
                .onSuccess {
                    Timber.i("Auth: linkAnonymousAccount phone success")
                    _uiState.value = _uiState.value.copy(isLoading = false)
                }
                .onFailure { error ->
                    Timber.w(error, "Auth: linkAnonymousAccount phone failed")
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        error = error.message
                    )
                }
        }
    }
    
    fun signUp(email: String, password: String, userType: UserType, phoneNumber: String? = null) {
        viewModelScope.launch {
            Timber.i("Auth: signUp start")
            _uiState.value = _uiState.value.copy(isLoading = true, error = null)
            
            authRepository.signUp(email, password, userType, phoneNumber)
                .onSuccess {
                    Timber.i("Auth: signUp success")
                    _uiState.value = _uiState.value.copy(isLoading = false)
                }
                .onFailure { error ->
                    Timber.w(error, "Auth: signUp failed")
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        error = error.message
                    )
                }
        }
    }
    
    fun signUpWithPhone(phoneNumber: String, displayName: String, userType: UserType) {
        viewModelScope.launch {
            Timber.i("Auth: signUpWithPhone start")
            _uiState.value = _uiState.value.copy(isLoading = true, error = null)
            
            authRepository.signUpWithPhone(phoneNumber, displayName, userType)
                .onSuccess {
                    Timber.i("Auth: signUpWithPhone success")
                    _uiState.value = _uiState.value.copy(isLoading = false)
                }
                .onFailure { error ->
                    Timber.w(error, "Auth: signUpWithPhone failed")
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        error = error.message
                    )
                }
        }
    }
    
    fun signInWithPhone(phoneNumber: String) {
        viewModelScope.launch {
            Timber.i("Auth: signInWithPhone start")
            _uiState.value = _uiState.value.copy(isLoading = true, error = null)
            
            authRepository.signInWithPhone(phoneNumber)
                .onSuccess {
                    Timber.i("Auth: signInWithPhone success")
                    _uiState.value = _uiState.value.copy(isLoading = false)
                }
                .onFailure { error ->
                    Timber.w(error, "Auth: signInWithPhone failed")
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        error = error.message
                    )
                }
        }
    }
    
    fun signIn(email: String, password: String) {
        viewModelScope.launch {
            Timber.i("Auth: signIn start")
            _uiState.value = _uiState.value.copy(isLoading = true, error = null)
            
            authRepository.signIn(email, password)
                .onSuccess {
                    Timber.i("Auth: signIn success")
                    _uiState.value = _uiState.value.copy(isLoading = false)
                }
                .onFailure { error ->
                    Timber.w(error, "Auth: signIn failed")
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        error = error.message
                    )
                }
        }
    }
    
    fun signInWithGoogle(googleSignInAccount: GoogleSignInAccount) {
        viewModelScope.launch {
            Timber.i("Auth: signInWithGoogle start")
            _uiState.value = _uiState.value.copy(isLoading = true, error = null)
            
            authRepository.signInWithGoogle(googleSignInAccount)
                .onSuccess {
                    Timber.i("Auth: signInWithGoogle success")
                    _uiState.value = _uiState.value.copy(isLoading = false)
                }
                .onFailure { error ->
                    Timber.w(error, "Auth: signInWithGoogle failed")
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        error = error.message
                    )
                }
        }
    }
    
    fun linkAnonymousAccountWithGoogle(googleSignInAccount: GoogleSignInAccount) {
        viewModelScope.launch {
            Timber.i("Auth: linkAnonymousAccountWithGoogle start")
            _uiState.value = _uiState.value.copy(isLoading = true, error = null)
            
            authRepository.linkAnonymousAccountWithGoogle(googleSignInAccount)
                .onSuccess {
                    Timber.i("Auth: linkAnonymousAccountWithGoogle success")
                    _uiState.value = _uiState.value.copy(isLoading = false)
                }
                .onFailure { error ->
                    Timber.w(error, "Auth: linkAnonymousAccountWithGoogle failed")
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        error = error.message
                    )
                }
        }
    }
    
    fun signOut() {
        viewModelScope.launch {
            Timber.i("Auth: signOut start")
            _uiState.value = _uiState.value.copy(isLoading = true, error = null)
            
            try {
                authRepository.signOut()
                Timber.i("Auth: signOut success")
                _uiState.value = _uiState.value.copy(isLoading = false)
            } catch (error: Exception) {
                Timber.w(error, "Auth: signOut failed")
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    error = error.message
                )
            }
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