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
import com.google.firebase.functions.FirebaseFunctions
import kotlinx.coroutines.tasks.await

@HiltViewModel
class AccountViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    private val locationService: LocationService,
    private val eventRepository: com.littlegig.app.data.repository.EventRepository,
    private val userRepository: com.littlegig.app.data.repository.UserRepository,
    private val paymentRepository: com.littlegig.app.data.repository.PaymentRepository,
    private val functions: FirebaseFunctions
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
                    // Load real user statistics from repositories
                    val eventsCreated = eventRepository.getUserEvents(currentUser.id).size
                    val ticketsBought = 0 // Will be implemented when ticket repository is ready
                    val recapsShared = 0 // Will be implemented when recap repository is ready
                    val totalSpent = 0 // Will be implemented when payment repository is ready
                    val engagementScore = currentUser.rank.ordinal * 100
                    val eventsAttended = 0 // Will be implemented when ticket repository is ready
                    val recapsCreated = 0 // Will be implemented when recap repository is ready
                    
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
                    println("🔥 DEBUG: Upgrading user ${user.id} to business account")
                    
                    // Ensure complete user document exists first (for anonymous users)
                    val ensureDocResult = userRepository.updateUserProfile(user.id, mapOf(
                        "id" to user.id,
                        "email" to user.email,
                        "displayName" to user.displayName,
                        "username" to user.username,
                        "phoneNumber" to user.phoneNumber,
                        "userType" to user.userType.name,
                        "rank" to user.rank.name,
                        "followers" to user.followers,
                        "following" to user.following,
                        "bio" to (user.bio ?: ""),
                        "profilePictureUrl" to user.profilePictureUrl,
                        "profileImageUrl" to user.profileImageUrl,
                        "isInfluencer" to user.isInfluencer,
                        "businessId" to (user.businessId ?: ""),
                        "createdAt" to user.createdAt,
                        "updatedAt" to System.currentTimeMillis()
                    ))
                    
                    if (ensureDocResult.isFailure) {
                        println("🔥 DEBUG: Failed to ensure user document: ${ensureDocResult.exceptionOrNull()?.message}")
                        _uiState.value = _uiState.value.copy(
                            isLoading = false,
                            error = "Failed to prepare user document: ${ensureDocResult.exceptionOrNull()?.message}"
                        )
                        return@launch
                    }
                    
                    // Now upgrade to business account
                    val result = userRepository.updateUserProfile(
                        user.id,
                        mapOf(
                            "userType" to UserType.BUSINESS.name,
                            "updatedAt" to System.currentTimeMillis()
                        )
                    )
                    
                    if (result.isSuccess) {
                        println("🔥 DEBUG: Business upgrade successful")
                        // Force refresh of currentUser cache for immediate UI update
                        authRepository.refreshCurrentUser()
                        _uiState.value = _uiState.value.copy(
                            isLoading = false,
                            isSuccess = true,
                            showPaymentDialog = false,
                            paymentUrl = null
                        )
                    } else {
                        println("🔥 DEBUG: Business upgrade failed: ${result.exceptionOrNull()?.message}")
                        _uiState.value = _uiState.value.copy(
                            isLoading = false,
                            error = result.exceptionOrNull()?.message ?: "Failed to upgrade account"
                        )
                    }
                } else {
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        error = "User not found"
                    )
                }
            } catch (e: Exception) {
                println("🔥 DEBUG: Business upgrade exception: ${e.message}")
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

    fun showAccountLinking() {
        _uiState.value = _uiState.value.copy(showAccountLinking = true)
    }
    
    fun clearAccountLinking() {
        _uiState.value = _uiState.value.copy(showAccountLinking = false)
    }
    
    fun signOut() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, error = null)
            try {
                authRepository.signOut()
                _uiState.value = _uiState.value.copy(isLoading = false)
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    error = e.message
                )
            }
        }
    }

        fun clearError() {
        _uiState.value = _uiState.value.copy(error = null)
    }
    
    fun clearSuccess() {
        _uiState.value = _uiState.value.copy(isSuccess = false)
    }
    
    fun clearPaymentDialog() {
        _uiState.value = _uiState.value.copy(
            showPaymentDialog = false,
            paymentUrl = null
        )
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
                val currentUser = authRepository.currentUser.first()
                if (currentUser == null) {
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        error = "User not found"
                    )
                    return@launch
                }
                
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
                    try {
                        val uploadResult = userRepository.uploadProfilePicture(currentUser.id, profileImageUri)
                        if (uploadResult.isSuccess) {
                            val url = uploadResult.getOrNull()
                            if (!url.isNullOrBlank()) {
                                updates["profilePictureUrl"] = url
                            }
                        } else {
                            println("🔥 DEBUG: Failed to upload profile picture: ${uploadResult.exceptionOrNull()?.message}")
                        }
                    } catch (e: Exception) {
                        println("🔥 DEBUG: Failed to upload profile picture: ${e.message}")
                        // Continue without image upload
                    }
                }
                
                // Update the profile in Firestore
                // Ensure complete user document exists first (for anonymous users)
                println("🔥 DEBUG: Updating profile for user ${currentUser.id}")
                val ensureDocResult = userRepository.updateUserProfile(currentUser.id, mapOf(
                    "id" to currentUser.id,
                    "email" to currentUser.email,
                    "displayName" to currentUser.displayName,
                    "username" to currentUser.username,
                    "phoneNumber" to currentUser.phoneNumber,
                    "userType" to currentUser.userType.name,
                    "rank" to currentUser.rank.name,
                    "followers" to currentUser.followers,
                    "following" to currentUser.following,
                    "bio" to (currentUser.bio ?: ""),
                    "profilePictureUrl" to currentUser.profilePictureUrl,
                    "profileImageUrl" to currentUser.profileImageUrl,
                    "isInfluencer" to currentUser.isInfluencer,
                    "businessId" to (currentUser.businessId ?: ""),
                    "createdAt" to currentUser.createdAt,
                    "updatedAt" to System.currentTimeMillis()
                ))
                
                if (ensureDocResult.isFailure) {
                    println("🔥 DEBUG: Failed to ensure user document: ${ensureDocResult.exceptionOrNull()?.message}")
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        error = "Failed to prepare user document: ${ensureDocResult.exceptionOrNull()?.message}"
                    )
                    return@launch
                }
                
                val result = if (updates.isNotEmpty()) {
                    userRepository.updateUserProfile(currentUser.id, updates)
                } else {
                    Result.success(Unit) // No updates needed
                }
                
                if (result.isSuccess) {
                    // Update the cached user with new data
                    val updatedUser = currentUser.copy(
                        displayName = displayName.ifBlank { currentUser.displayName },
                        email = email.ifBlank { currentUser.email },
                        phoneNumber = phoneNumber.ifBlank { currentUser.phoneNumber },
                        bio = bio.ifBlank { currentUser.bio },
                        profilePictureUrl = updates["profilePictureUrl"] as? String ?: currentUser.profilePictureUrl,
                        updatedAt = System.currentTimeMillis()
                    )
                    
                    // Update the cached user and refresh from Firestore so UI reflects changes
                    authRepository.cacheUser(updatedUser)
                    authRepository.refreshCurrentUser()
                    
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        isSuccess = true
                    )
                } else {
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        error = result.exceptionOrNull()?.message ?: "Failed to update profile"
                    )
                }
            } catch (e: Exception) {
                println("🔥 DEBUG: Profile update error: ${e.message}")
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
    
    fun createDemoUsers() {
        viewModelScope.launch {
            try {
                _uiState.value = _uiState.value.copy(isLoading = true, error = null)
                
                val result = functions.getHttpsCallable("seedDemoUsers")
                    .call(mapOf("count" to 10))
                    .await()
                
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    isSuccess = true,
                    error = null
                )
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    error = "Failed to create demo users: ${e.message}"
                )
            }
        }
    }
    
    fun checkExistingUsers() {
        viewModelScope.launch {
            try {
                _uiState.value = _uiState.value.copy(isLoading = true, error = null)
                
                val result = functions.getHttpsCallable("checkExistingUsers")
                    .call(mapOf<String, Any>())
                    .await()
                
                println("🔥 DEBUG: Existing users check result: $result")
                
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    isSuccess = true,
                    error = null
                )
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    error = "Failed to check existing users: ${e.message}"
                )
            }
        }
    }
    
    fun deleteAllUsers() {
        viewModelScope.launch {
            try {
                _uiState.value = _uiState.value.copy(isLoading = true, error = null)
                
                val result = functions.getHttpsCallable("deleteAllUsers")
                    .call(mapOf<String, Any>())
                    .await()
                
                println("🔥 DEBUG: Delete all users result: $result")
                
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    isSuccess = true,
                    error = null
                )
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    error = "Failed to delete all users: ${e.message}"
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
    val showPaymentDialog: Boolean = false,
    val paymentUrl: String? = null,
    val eventsCreated: Int = 0,
    val ticketsBought: Int = 0,
    val recapsShared: Int = 0,
    val totalSpent: Int = 0,
    val engagementScore: Int = 0,
    val eventsAttended: Int = 0,
    val recapsCreated: Int = 0
)