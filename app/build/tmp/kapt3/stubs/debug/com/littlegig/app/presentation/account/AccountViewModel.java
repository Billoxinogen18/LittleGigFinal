package com.littlegig.app.presentation.account;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000Z\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0010\u000b\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\b\n\u0002\u0010\u0002\n\u0002\b\n\n\u0002\u0010\u000e\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0002\b\u0004\b\u0007\u0018\u00002\u00020\u0001B\'\b\u0007\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0005\u0012\u0006\u0010\u0006\u001a\u00020\u0007\u0012\u0006\u0010\b\u001a\u00020\t\u00a2\u0006\u0002\u0010\nJ\u0006\u0010\u001b\u001a\u00020\u001cJ\u0006\u0010\u001d\u001a\u00020\u001cJ\u0006\u0010\u001e\u001a\u00020\u001cJ\u0006\u0010\u001f\u001a\u00020\u001cJ\u0006\u0010 \u001a\u00020\u001cJ\b\u0010!\u001a\u00020\u001cH\u0002J\u0006\u0010\"\u001a\u00020\u001cJ\u000e\u0010#\u001a\u00020\u001c2\u0006\u0010$\u001a\u00020\rJ0\u0010%\u001a\u00020\u001c2\u0006\u0010&\u001a\u00020\'2\u0006\u0010(\u001a\u00020\'2\u0006\u0010)\u001a\u00020\'2\u0006\u0010*\u001a\u00020\'2\b\u0010+\u001a\u0004\u0018\u00010,J\u000e\u0010-\u001a\u00020\u001c2\u0006\u0010.\u001a\u00020,J\u0006\u0010/\u001a\u00020\u001cR\u0014\u0010\u000b\u001a\b\u0012\u0004\u0012\u00020\r0\fX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0014\u0010\u000e\u001a\b\u0012\u0004\u0012\u00020\r0\fX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0014\u0010\u000f\u001a\b\u0012\u0004\u0012\u00020\u00100\fX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0002\u001a\u00020\u0003X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0019\u0010\u0011\u001a\n\u0012\u0006\u0012\u0004\u0018\u00010\u00130\u0012\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0014\u0010\u0015R\u000e\u0010\u0006\u001a\u00020\u0007X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0017\u0010\u0016\u001a\b\u0012\u0004\u0012\u00020\r0\u0012\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0016\u0010\u0015R\u0017\u0010\u0017\u001a\b\u0012\u0004\u0012\u00020\r0\u0012\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0018\u0010\u0015R\u000e\u0010\u0004\u001a\u00020\u0005X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0017\u0010\u0019\u001a\b\u0012\u0004\u0012\u00020\u00100\u0012\u00a2\u0006\b\n\u0000\u001a\u0004\b\u001a\u0010\u0015R\u000e\u0010\b\u001a\u00020\tX\u0082\u0004\u00a2\u0006\u0002\n\u0000\u00a8\u00060"}, d2 = {"Lcom/littlegig/app/presentation/account/AccountViewModel;", "Landroidx/lifecycle/ViewModel;", "authRepository", "Lcom/littlegig/app/data/repository/AuthRepository;", "locationService", "Lcom/littlegig/app/services/LocationService;", "eventRepository", "Lcom/littlegig/app/data/repository/EventRepository;", "userRepository", "Lcom/littlegig/app/data/repository/UserRepository;", "(Lcom/littlegig/app/data/repository/AuthRepository;Lcom/littlegig/app/services/LocationService;Lcom/littlegig/app/data/repository/EventRepository;Lcom/littlegig/app/data/repository/UserRepository;)V", "_isActiveNow", "Lkotlinx/coroutines/flow/MutableStateFlow;", "", "_locationPermissionGranted", "_uiState", "Lcom/littlegig/app/presentation/account/AccountUiState;", "currentUser", "Lkotlinx/coroutines/flow/StateFlow;", "Lcom/littlegig/app/data/model/User;", "getCurrentUser", "()Lkotlinx/coroutines/flow/StateFlow;", "isActiveNow", "locationPermissionGranted", "getLocationPermissionGranted", "uiState", "getUiState", "becomeInfluencer", "", "clearAccountLinking", "clearError", "clearSuccess", "linkAnonymousAccount", "loadUserStats", "signOut", "toggleActiveNow", "isActive", "updateProfile", "displayName", "", "email", "phoneNumber", "bio", "profileImageUri", "Landroid/net/Uri;", "updateProfilePicture", "imageUri", "upgradeToBusinessAccount", "app_debug"})
@dagger.hilt.android.lifecycle.HiltViewModel
public final class AccountViewModel extends androidx.lifecycle.ViewModel {
    @org.jetbrains.annotations.NotNull
    private final com.littlegig.app.data.repository.AuthRepository authRepository = null;
    @org.jetbrains.annotations.NotNull
    private final com.littlegig.app.services.LocationService locationService = null;
    @org.jetbrains.annotations.NotNull
    private final com.littlegig.app.data.repository.EventRepository eventRepository = null;
    @org.jetbrains.annotations.NotNull
    private final com.littlegig.app.data.repository.UserRepository userRepository = null;
    @org.jetbrains.annotations.NotNull
    private final kotlinx.coroutines.flow.MutableStateFlow<com.littlegig.app.presentation.account.AccountUiState> _uiState = null;
    @org.jetbrains.annotations.NotNull
    private final kotlinx.coroutines.flow.StateFlow<com.littlegig.app.presentation.account.AccountUiState> uiState = null;
    @org.jetbrains.annotations.NotNull
    private final kotlinx.coroutines.flow.StateFlow<com.littlegig.app.data.model.User> currentUser = null;
    @org.jetbrains.annotations.NotNull
    private final kotlinx.coroutines.flow.MutableStateFlow<java.lang.Boolean> _isActiveNow = null;
    @org.jetbrains.annotations.NotNull
    private final kotlinx.coroutines.flow.StateFlow<java.lang.Boolean> isActiveNow = null;
    @org.jetbrains.annotations.NotNull
    private final kotlinx.coroutines.flow.MutableStateFlow<java.lang.Boolean> _locationPermissionGranted = null;
    @org.jetbrains.annotations.NotNull
    private final kotlinx.coroutines.flow.StateFlow<java.lang.Boolean> locationPermissionGranted = null;
    
    @javax.inject.Inject
    public AccountViewModel(@org.jetbrains.annotations.NotNull
    com.littlegig.app.data.repository.AuthRepository authRepository, @org.jetbrains.annotations.NotNull
    com.littlegig.app.services.LocationService locationService, @org.jetbrains.annotations.NotNull
    com.littlegig.app.data.repository.EventRepository eventRepository, @org.jetbrains.annotations.NotNull
    com.littlegig.app.data.repository.UserRepository userRepository) {
        super();
    }
    
    @org.jetbrains.annotations.NotNull
    public final kotlinx.coroutines.flow.StateFlow<com.littlegig.app.presentation.account.AccountUiState> getUiState() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull
    public final kotlinx.coroutines.flow.StateFlow<com.littlegig.app.data.model.User> getCurrentUser() {
        return null;
    }
    
    private final void loadUserStats() {
    }
    
    @org.jetbrains.annotations.NotNull
    public final kotlinx.coroutines.flow.StateFlow<java.lang.Boolean> isActiveNow() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull
    public final kotlinx.coroutines.flow.StateFlow<java.lang.Boolean> getLocationPermissionGranted() {
        return null;
    }
    
    public final void upgradeToBusinessAccount() {
    }
    
    public final void becomeInfluencer() {
    }
    
    public final void signOut() {
    }
    
    public final void clearError() {
    }
    
    public final void clearSuccess() {
    }
    
    public final void clearAccountLinking() {
    }
    
    public final void linkAnonymousAccount() {
    }
    
    public final void updateProfile(@org.jetbrains.annotations.NotNull
    java.lang.String displayName, @org.jetbrains.annotations.NotNull
    java.lang.String email, @org.jetbrains.annotations.NotNull
    java.lang.String phoneNumber, @org.jetbrains.annotations.NotNull
    java.lang.String bio, @org.jetbrains.annotations.Nullable
    android.net.Uri profileImageUri) {
    }
    
    public final void toggleActiveNow(boolean isActive) {
    }
    
    public final void updateProfilePicture(@org.jetbrains.annotations.NotNull
    android.net.Uri imageUri) {
    }
}