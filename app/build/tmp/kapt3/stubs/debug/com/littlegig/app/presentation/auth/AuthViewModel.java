package com.littlegig.app.presentation.auth;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000\\\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\u000e\n\u0000\n\u0002\u0010\u0002\n\u0002\b\u0006\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\r\b\u0007\u0018\u00002\u00020\u0001B\u0017\b\u0007\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0005\u00a2\u0006\u0002\u0010\u0006J\u0006\u0010\u0015\u001a\u00020\u0016J4\u0010\u0017\u001a\u00020\u00162\u0006\u0010\u0018\u001a\u00020\u00142\u0006\u0010\u0019\u001a\u00020\u00142\u0006\u0010\u001a\u001a\u00020\u00142\n\b\u0002\u0010\u001b\u001a\u0004\u0018\u00010\u00142\b\b\u0002\u0010\u001c\u001a\u00020\u001dJ\u000e\u0010\u001e\u001a\u00020\u00162\u0006\u0010\u001f\u001a\u00020 J \u0010!\u001a\u00020\u00162\u0006\u0010\u001b\u001a\u00020\u00142\u0006\u0010\u001a\u001a\u00020\u00142\b\b\u0002\u0010\u001c\u001a\u00020\u001dJ\u0016\u0010\"\u001a\u00020\u00162\u0006\u0010#\u001a\u00020$2\u0006\u0010%\u001a\u00020\u0014J\u0016\u0010&\u001a\u00020\u00162\u0006\u0010\u0018\u001a\u00020\u00142\u0006\u0010\u0019\u001a\u00020\u0014J\u0006\u0010\'\u001a\u00020\u0016J\u000e\u0010(\u001a\u00020\u00162\u0006\u0010\u001f\u001a\u00020 J\u000e\u0010)\u001a\u00020\u00162\u0006\u0010\u001b\u001a\u00020\u0014J\u0006\u0010*\u001a\u00020\u0016J*\u0010+\u001a\u00020\u00162\u0006\u0010\u0018\u001a\u00020\u00142\u0006\u0010\u0019\u001a\u00020\u00142\u0006\u0010\u001c\u001a\u00020\u001d2\n\b\u0002\u0010\u001b\u001a\u0004\u0018\u00010\u0014J\u001e\u0010,\u001a\u00020\u00162\u0006\u0010\u001b\u001a\u00020\u00142\u0006\u0010\u001a\u001a\u00020\u00142\u0006\u0010\u001c\u001a\u00020\u001dJ\u0016\u0010-\u001a\u00020\u00162\u0006\u0010#\u001a\u00020$2\u0006\u0010%\u001a\u00020\u0014J \u0010.\u001a\u00020\u00162\u0006\u0010\u001a\u001a\u00020\u00142\b\b\u0002\u0010\u001c\u001a\u00020\u001d2\u0006\u0010/\u001a\u00020\u0014J\u000e\u00100\u001a\u00020\u00162\u0006\u0010/\u001a\u00020\u0014R\u0014\u0010\u0007\u001a\b\u0012\u0004\u0012\u00020\t0\bX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0002\u001a\u00020\u0003X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0019\u0010\n\u001a\n\u0012\u0006\u0012\u0004\u0018\u00010\f0\u000b\u00a2\u0006\b\n\u0000\u001a\u0004\b\r\u0010\u000eR\u000e\u0010\u0004\u001a\u00020\u0005X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0010\u0010\u000f\u001a\u0004\u0018\u00010\u0010X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u0017\u0010\u0011\u001a\b\u0012\u0004\u0012\u00020\t0\u000b\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0012\u0010\u000eR\u0010\u0010\u0013\u001a\u0004\u0018\u00010\u0014X\u0082\u000e\u00a2\u0006\u0002\n\u0000\u00a8\u00061"}, d2 = {"Lcom/littlegig/app/presentation/auth/AuthViewModel;", "Landroidx/lifecycle/ViewModel;", "authRepository", "Lcom/littlegig/app/data/repository/AuthRepository;", "phoneAuthService", "Lcom/littlegig/app/services/PhoneAuthService;", "(Lcom/littlegig/app/data/repository/AuthRepository;Lcom/littlegig/app/services/PhoneAuthService;)V", "_uiState", "Lkotlinx/coroutines/flow/MutableStateFlow;", "Lcom/littlegig/app/presentation/auth/AuthUiState;", "currentUser", "Lkotlinx/coroutines/flow/StateFlow;", "Lcom/littlegig/app/data/model/User;", "getCurrentUser", "()Lkotlinx/coroutines/flow/StateFlow;", "resendToken", "Lcom/google/firebase/auth/PhoneAuthProvider$ForceResendingToken;", "uiState", "getUiState", "verificationId", "", "clearError", "", "linkAnonymousAccount", "email", "password", "displayName", "phoneNumber", "userType", "Lcom/littlegig/app/data/model/UserType;", "linkAnonymousAccountWithGoogle", "googleSignInAccount", "Lcom/google/android/gms/auth/api/signin/GoogleSignInAccount;", "linkAnonymousAccountWithPhone", "resendPhoneVerification", "activity", "Landroid/app/Activity;", "phoneE164", "signIn", "signInAnonymously", "signInWithGoogle", "signInWithPhone", "signOut", "signUp", "signUpWithPhone", "startPhoneVerification", "verifyOtpAndLink", "code", "verifyOtpAndSignIn", "app_debug"})
@dagger.hilt.android.lifecycle.HiltViewModel()
public final class AuthViewModel extends androidx.lifecycle.ViewModel {
    @org.jetbrains.annotations.NotNull()
    private final com.littlegig.app.data.repository.AuthRepository authRepository = null;
    @org.jetbrains.annotations.NotNull()
    private final com.littlegig.app.services.PhoneAuthService phoneAuthService = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.MutableStateFlow<com.littlegig.app.presentation.auth.AuthUiState> _uiState = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.StateFlow<com.littlegig.app.presentation.auth.AuthUiState> uiState = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.StateFlow<com.littlegig.app.data.model.User> currentUser = null;
    @org.jetbrains.annotations.Nullable()
    private java.lang.String verificationId;
    @org.jetbrains.annotations.Nullable()
    private com.google.firebase.auth.PhoneAuthProvider.ForceResendingToken resendToken;
    
    @javax.inject.Inject()
    public AuthViewModel(@org.jetbrains.annotations.NotNull()
    com.littlegig.app.data.repository.AuthRepository authRepository, @org.jetbrains.annotations.NotNull()
    com.littlegig.app.services.PhoneAuthService phoneAuthService) {
        super();
    }
    
    @org.jetbrains.annotations.NotNull()
    public final kotlinx.coroutines.flow.StateFlow<com.littlegig.app.presentation.auth.AuthUiState> getUiState() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final kotlinx.coroutines.flow.StateFlow<com.littlegig.app.data.model.User> getCurrentUser() {
        return null;
    }
    
    public final void startPhoneVerification(@org.jetbrains.annotations.NotNull()
    android.app.Activity activity, @org.jetbrains.annotations.NotNull()
    java.lang.String phoneE164) {
    }
    
    public final void resendPhoneVerification(@org.jetbrains.annotations.NotNull()
    android.app.Activity activity, @org.jetbrains.annotations.NotNull()
    java.lang.String phoneE164) {
    }
    
    public final void verifyOtpAndLink(@org.jetbrains.annotations.NotNull()
    java.lang.String displayName, @org.jetbrains.annotations.NotNull()
    com.littlegig.app.data.model.UserType userType, @org.jetbrains.annotations.NotNull()
    java.lang.String code) {
    }
    
    public final void verifyOtpAndSignIn(@org.jetbrains.annotations.NotNull()
    java.lang.String code) {
    }
    
    public final void signInAnonymously() {
    }
    
    public final void linkAnonymousAccount(@org.jetbrains.annotations.NotNull()
    java.lang.String email, @org.jetbrains.annotations.NotNull()
    java.lang.String password, @org.jetbrains.annotations.NotNull()
    java.lang.String displayName, @org.jetbrains.annotations.Nullable()
    java.lang.String phoneNumber, @org.jetbrains.annotations.NotNull()
    com.littlegig.app.data.model.UserType userType) {
    }
    
    public final void linkAnonymousAccountWithPhone(@org.jetbrains.annotations.NotNull()
    java.lang.String phoneNumber, @org.jetbrains.annotations.NotNull()
    java.lang.String displayName, @org.jetbrains.annotations.NotNull()
    com.littlegig.app.data.model.UserType userType) {
    }
    
    public final void signUp(@org.jetbrains.annotations.NotNull()
    java.lang.String email, @org.jetbrains.annotations.NotNull()
    java.lang.String password, @org.jetbrains.annotations.NotNull()
    com.littlegig.app.data.model.UserType userType, @org.jetbrains.annotations.Nullable()
    java.lang.String phoneNumber) {
    }
    
    public final void signUpWithPhone(@org.jetbrains.annotations.NotNull()
    java.lang.String phoneNumber, @org.jetbrains.annotations.NotNull()
    java.lang.String displayName, @org.jetbrains.annotations.NotNull()
    com.littlegig.app.data.model.UserType userType) {
    }
    
    public final void signInWithPhone(@org.jetbrains.annotations.NotNull()
    java.lang.String phoneNumber) {
    }
    
    public final void signIn(@org.jetbrains.annotations.NotNull()
    java.lang.String email, @org.jetbrains.annotations.NotNull()
    java.lang.String password) {
    }
    
    public final void signInWithGoogle(@org.jetbrains.annotations.NotNull()
    com.google.android.gms.auth.api.signin.GoogleSignInAccount googleSignInAccount) {
    }
    
    public final void linkAnonymousAccountWithGoogle(@org.jetbrains.annotations.NotNull()
    com.google.android.gms.auth.api.signin.GoogleSignInAccount googleSignInAccount) {
    }
    
    public final void signOut() {
    }
    
    public final void clearError() {
    }
}