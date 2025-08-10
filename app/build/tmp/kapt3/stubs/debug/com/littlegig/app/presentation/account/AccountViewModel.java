package com.littlegig.app.presentation.account;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000f\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0010\u000b\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\b\n\u0002\u0010\u0002\n\u0002\b\r\n\u0002\u0010\u000e\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0002\b\u0004\b\u0007\u0018\u00002\u00020\u0001B7\b\u0007\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0005\u0012\u0006\u0010\u0006\u001a\u00020\u0007\u0012\u0006\u0010\b\u001a\u00020\t\u0012\u0006\u0010\n\u001a\u00020\u000b\u0012\u0006\u0010\f\u001a\u00020\r\u00a2\u0006\u0002\u0010\u000eJ\u0006\u0010\u001f\u001a\u00020 J\u0006\u0010!\u001a\u00020 J\u0006\u0010\"\u001a\u00020 J\u0006\u0010#\u001a\u00020 J\u0006\u0010$\u001a\u00020 J\u0006\u0010%\u001a\u00020 J\u0006\u0010&\u001a\u00020 J\b\u0010\'\u001a\u00020 H\u0002J\u0006\u0010(\u001a\u00020 J\u0006\u0010)\u001a\u00020 J\u000e\u0010*\u001a\u00020 2\u0006\u0010+\u001a\u00020\u0011J0\u0010,\u001a\u00020 2\u0006\u0010-\u001a\u00020.2\u0006\u0010/\u001a\u00020.2\u0006\u00100\u001a\u00020.2\u0006\u00101\u001a\u00020.2\b\u00102\u001a\u0004\u0018\u000103J\u000e\u00104\u001a\u00020 2\u0006\u00105\u001a\u000203J\u0006\u00106\u001a\u00020 R\u0014\u0010\u000f\u001a\b\u0012\u0004\u0012\u00020\u00110\u0010X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0014\u0010\u0012\u001a\b\u0012\u0004\u0012\u00020\u00110\u0010X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0014\u0010\u0013\u001a\b\u0012\u0004\u0012\u00020\u00140\u0010X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0002\u001a\u00020\u0003X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0019\u0010\u0015\u001a\n\u0012\u0006\u0012\u0004\u0018\u00010\u00170\u0016\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0018\u0010\u0019R\u000e\u0010\u0006\u001a\u00020\u0007X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\f\u001a\u00020\rX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0017\u0010\u001a\u001a\b\u0012\u0004\u0012\u00020\u00110\u0016\u00a2\u0006\b\n\u0000\u001a\u0004\b\u001a\u0010\u0019R\u0017\u0010\u001b\u001a\b\u0012\u0004\u0012\u00020\u00110\u0016\u00a2\u0006\b\n\u0000\u001a\u0004\b\u001c\u0010\u0019R\u000e\u0010\u0004\u001a\u00020\u0005X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\n\u001a\u00020\u000bX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0017\u0010\u001d\u001a\b\u0012\u0004\u0012\u00020\u00140\u0016\u00a2\u0006\b\n\u0000\u001a\u0004\b\u001e\u0010\u0019R\u000e\u0010\b\u001a\u00020\tX\u0082\u0004\u00a2\u0006\u0002\n\u0000\u00a8\u00067"}, d2 = {"Lcom/littlegig/app/presentation/account/AccountViewModel;", "Landroidx/lifecycle/ViewModel;", "authRepository", "Lcom/littlegig/app/data/repository/AuthRepository;", "locationService", "Lcom/littlegig/app/services/LocationService;", "eventRepository", "Lcom/littlegig/app/data/repository/EventRepository;", "userRepository", "Lcom/littlegig/app/data/repository/UserRepository;", "paymentRepository", "Lcom/littlegig/app/data/repository/PaymentRepository;", "functions", "Lcom/google/firebase/functions/FirebaseFunctions;", "(Lcom/littlegig/app/data/repository/AuthRepository;Lcom/littlegig/app/services/LocationService;Lcom/littlegig/app/data/repository/EventRepository;Lcom/littlegig/app/data/repository/UserRepository;Lcom/littlegig/app/data/repository/PaymentRepository;Lcom/google/firebase/functions/FirebaseFunctions;)V", "_isActiveNow", "Lkotlinx/coroutines/flow/MutableStateFlow;", "", "_locationPermissionGranted", "_uiState", "Lcom/littlegig/app/presentation/account/AccountUiState;", "currentUser", "Lkotlinx/coroutines/flow/StateFlow;", "Lcom/littlegig/app/data/model/User;", "getCurrentUser", "()Lkotlinx/coroutines/flow/StateFlow;", "isActiveNow", "locationPermissionGranted", "getLocationPermissionGranted", "uiState", "getUiState", "becomeInfluencer", "", "clearAccountLinking", "clearError", "clearPaymentDialog", "clearSuccess", "createDemoUsers", "linkAnonymousAccount", "loadUserStats", "showAccountLinking", "signOut", "toggleActiveNow", "isActive", "updateProfile", "displayName", "", "email", "phoneNumber", "bio", "profileImageUri", "Landroid/net/Uri;", "updateProfilePicture", "imageUri", "upgradeToBusinessAccount", "app_debug"})
@dagger.hilt.android.lifecycle.HiltViewModel()
public final class AccountViewModel extends androidx.lifecycle.ViewModel {
    @org.jetbrains.annotations.NotNull()
    private final com.littlegig.app.data.repository.AuthRepository authRepository = null;
    @org.jetbrains.annotations.NotNull()
    private final com.littlegig.app.services.LocationService locationService = null;
    @org.jetbrains.annotations.NotNull()
    private final com.littlegig.app.data.repository.EventRepository eventRepository = null;
    @org.jetbrains.annotations.NotNull()
    private final com.littlegig.app.data.repository.UserRepository userRepository = null;
    @org.jetbrains.annotations.NotNull()
    private final com.littlegig.app.data.repository.PaymentRepository paymentRepository = null;
    @org.jetbrains.annotations.NotNull()
    private final com.google.firebase.functions.FirebaseFunctions functions = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.MutableStateFlow<com.littlegig.app.presentation.account.AccountUiState> _uiState = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.StateFlow<com.littlegig.app.presentation.account.AccountUiState> uiState = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.StateFlow<com.littlegig.app.data.model.User> currentUser = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.MutableStateFlow<java.lang.Boolean> _isActiveNow = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.StateFlow<java.lang.Boolean> isActiveNow = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.MutableStateFlow<java.lang.Boolean> _locationPermissionGranted = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.StateFlow<java.lang.Boolean> locationPermissionGranted = null;
    
    @javax.inject.Inject()
    public AccountViewModel(@org.jetbrains.annotations.NotNull()
    com.littlegig.app.data.repository.AuthRepository authRepository, @org.jetbrains.annotations.NotNull()
    com.littlegig.app.services.LocationService locationService, @org.jetbrains.annotations.NotNull()
    com.littlegig.app.data.repository.EventRepository eventRepository, @org.jetbrains.annotations.NotNull()
    com.littlegig.app.data.repository.UserRepository userRepository, @org.jetbrains.annotations.NotNull()
    com.littlegig.app.data.repository.PaymentRepository paymentRepository, @org.jetbrains.annotations.NotNull()
    com.google.firebase.functions.FirebaseFunctions functions) {
        super();
    }
    
    @org.jetbrains.annotations.NotNull()
    public final kotlinx.coroutines.flow.StateFlow<com.littlegig.app.presentation.account.AccountUiState> getUiState() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final kotlinx.coroutines.flow.StateFlow<com.littlegig.app.data.model.User> getCurrentUser() {
        return null;
    }
    
    private final void loadUserStats() {
    }
    
    @org.jetbrains.annotations.NotNull()
    public final kotlinx.coroutines.flow.StateFlow<java.lang.Boolean> isActiveNow() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final kotlinx.coroutines.flow.StateFlow<java.lang.Boolean> getLocationPermissionGranted() {
        return null;
    }
    
    public final void upgradeToBusinessAccount() {
    }
    
    public final void becomeInfluencer() {
    }
    
    public final void showAccountLinking() {
    }
    
    public final void clearAccountLinking() {
    }
    
    public final void signOut() {
    }
    
    public final void clearError() {
    }
    
    public final void clearSuccess() {
    }
    
    public final void clearPaymentDialog() {
    }
    
    public final void linkAnonymousAccount() {
    }
    
    public final void updateProfile(@org.jetbrains.annotations.NotNull()
    java.lang.String displayName, @org.jetbrains.annotations.NotNull()
    java.lang.String email, @org.jetbrains.annotations.NotNull()
    java.lang.String phoneNumber, @org.jetbrains.annotations.NotNull()
    java.lang.String bio, @org.jetbrains.annotations.Nullable()
    android.net.Uri profileImageUri) {
    }
    
    public final void toggleActiveNow(boolean isActive) {
    }
    
    public final void updateProfilePicture(@org.jetbrains.annotations.NotNull()
    android.net.Uri imageUri) {
    }
    
    public final void createDemoUsers() {
    }
}