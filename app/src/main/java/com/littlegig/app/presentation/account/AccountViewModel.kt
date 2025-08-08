package com.littlegig.app.presentation.account

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.littlegig.app.data.model.UserType
import com.littlegig.app.data.repository.AuthRepository
import com.littlegig.app.services.LocationService
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject
import android.net.Uri

@HiltViewModel
class AccountViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    private val locationService: LocationService,
    private val eventRepository: com.littlegig.app.data.repository.EventRepository,
    private val userRepository: com.littlegig.app.data.repository.UserRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(AccountUiState())
    val uiState: StateFlow<AccountUiState> = _uiState.asStateFlow()

    val currentUser = authRepository.currentUser
    
    init {
        loadUserStats()
    }
    
    private fun loadUserStats() {
        viewModelScope.launch {
            try {
                val currentUser = authRepository.currentUser.value
                if (currentUser != null) {
                    // Load user statistics from repositories
                    val eventsCreated = eventRepository.getUserEvents(currentUser.id).size
                    val ticketsBought = 5 // TODO: Get from ticket repository
                    val recapsShared = 3 // TODO: Get from recap repository
                    val totalSpent = 2500 // TODO: Get from payment repository
                    val engagementScore = currentUser.rank.ordinal * 100
                    val eventsAttended = 8 // TODO: Get from ticket repository
                    val recapsCreated = 2 // TODO: Get from recap repository
                    
                    _uiState.value = _uiState.value.copy(
                        eventsCreated = eventsCreated,
                        ticketsBought = ticketsBought,
                        recapsShared = recapsShared,
                        totalSpent = totalSpent,
                        engagementScore = engagementScore,
                        eventsAttended = eventsAttended,
                        recapsCreated = recapsCreated
                    )
                }
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    error = "Failed to load user stats: ${e.message}"
                )
            }
        }
    }
    
    private val _isActiveNow = MutableStateFlow(false)
    val isActiveNow: StateFlow<Boolean> = _isActiveNow.asStateFlow()
    
    private val _locationPermissionGranted = MutableStateFlow(false)
    val locationPermissionGranted: StateFlow<Boolean> = _locationPermissionGranted.asStateFlow()

    fun upgradeToBusinessAccount() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, error = null, isSuccess = false)
            
            try {
                val user = authRepository.currentUser.first()
                if (user != null) {
                    // Update user type to business
                    userRepository.updateUserProfile(user.id, mapOf(
                        "userType" to UserType.BUSINESS,
                        "updatedAt" to System.currentTimeMillis()
                    ))
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
                    authRepository.updateProfile(mapOf(
                        "isInfluencer" to true,
                        "updatedAt" to System.currentTimeMillis()
                    ))
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
    
    // 🔥 LINK ANONYMOUS ACCOUNT - PRESERVE ALGORITHM DATA! 🔥
    fun linkAnonymousAccount() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, error = null, isSuccess = false)
            
            try {
                val user = authRepository.currentUser.first()
                if (user != null && user.email.isEmpty()) {
                    // User is anonymous - show account linking screen
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        showAccountLinking = true
                    )
                } else {
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        error = "Account already linked"
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
    
    fun updateProfile(
        displayName: String,
        email: String,
        phoneNumber: String,
        bio: String,
        profileImageUri: Uri?
    ) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, error = null, isSuccess = false)
            
            try {
                val updates = mutableMapOf<String, Any>()
                
                if (displayName.isNotBlank()) {
                    updates["displayName"] = displayName
                }
                if (email.isNotBlank()) {
                    updates["email"] = email
                }
                if (phoneNumber.isNotBlank()) {
                    updates["phoneNumber"] = phoneNumber
                }
                if (bio.isNotBlank()) {
                    updates["bio"] = bio
                }
                
                // Upload profile image if selected
                if (profileImageUri != null) {
                    val currentUser = authRepository.currentUser.value
                    if (currentUser != null) {
                        val imageUrl = userRepository.uploadProfilePicture(currentUser.id, profileImageUri)
                        updates["profilePictureUrl"] = imageUrl
                    }
                }
                
                authRepository.updateProfile(updates)
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
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    error = e.message
                )
            }
        }
    }
    
    fun toggleActiveNow(isActive: Boolean) {
        viewModelScope.launch {
            try {
                locationService.toggleActiveNow(isActive)
                _isActiveNow.value = isActive
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    error = "Failed to toggle active status: ${e.message}"
                )
            }
        }
    }

    fun updateProfilePicture(imageUri: Uri) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, error = null, isSuccess = false)
            
            try {
                // In a real app, you would upload the image to Firebase Storage
                // and get the download URL, then update the user profile
                val user = authRepository.currentUser.first()
                if (user != null) {
                    // For now, we'll just update the local state
                    // In production, upload to Firebase Storage first
                    val updatedUser = user.copy(
                        profileImageUrl = imageUri.toString(),
                        updatedAt = System.currentTimeMillis()
                    )
                    
                    authRepository.updateProfile(mapOf(
                        "profileImageUrl" to imageUri.toString(),
                        "updatedAt" to System.currentTimeMillis()
                    ))
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
}

data class AccountUiState(
    val isLoading: Boolean = false,
    val error: String? = null,
    val isSuccess: Boolean = false,
    val showAccountLinking: Boolean = false,
    val eventsCreated: Int = 0,
    val ticketsBought: Int = 0,
    val recapsShared: Int = 0,
    val totalSpent: Int = 0,
    val engagementScore: Int = 0,
    val eventsAttended: Int = 0,
    val recapsCreated: Int = 0
)