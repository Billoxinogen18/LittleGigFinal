package com.littlegig.app.presentation.auth;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000H\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0010\u0002\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\t\b\u0007\u0018\u00002\u00020\u0001B\u000f\b\u0007\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\u0002\u0010\u0004J\u0006\u0010\u000f\u001a\u00020\u0010J4\u0010\u0011\u001a\u00020\u00102\u0006\u0010\u0012\u001a\u00020\u00132\u0006\u0010\u0014\u001a\u00020\u00132\u0006\u0010\u0015\u001a\u00020\u00132\n\b\u0002\u0010\u0016\u001a\u0004\u0018\u00010\u00132\b\b\u0002\u0010\u0017\u001a\u00020\u0018J\u000e\u0010\u0019\u001a\u00020\u00102\u0006\u0010\u001a\u001a\u00020\u001bJ \u0010\u001c\u001a\u00020\u00102\u0006\u0010\u0016\u001a\u00020\u00132\u0006\u0010\u0015\u001a\u00020\u00132\b\b\u0002\u0010\u0017\u001a\u00020\u0018J\u0016\u0010\u001d\u001a\u00020\u00102\u0006\u0010\u0012\u001a\u00020\u00132\u0006\u0010\u0014\u001a\u00020\u0013J\u0006\u0010\u001e\u001a\u00020\u0010J\u000e\u0010\u001f\u001a\u00020\u00102\u0006\u0010\u001a\u001a\u00020\u001bJ\u000e\u0010 \u001a\u00020\u00102\u0006\u0010\u0016\u001a\u00020\u0013J\u0006\u0010!\u001a\u00020\u0010J*\u0010\"\u001a\u00020\u00102\u0006\u0010\u0012\u001a\u00020\u00132\u0006\u0010\u0014\u001a\u00020\u00132\u0006\u0010\u0017\u001a\u00020\u00182\n\b\u0002\u0010\u0016\u001a\u0004\u0018\u00010\u0013J\u001e\u0010#\u001a\u00020\u00102\u0006\u0010\u0016\u001a\u00020\u00132\u0006\u0010\u0015\u001a\u00020\u00132\u0006\u0010\u0017\u001a\u00020\u0018R\u0014\u0010\u0005\u001a\b\u0012\u0004\u0012\u00020\u00070\u0006X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0002\u001a\u00020\u0003X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0019\u0010\b\u001a\n\u0012\u0006\u0012\u0004\u0018\u00010\n0\t\u00a2\u0006\b\n\u0000\u001a\u0004\b\u000b\u0010\fR\u0017\u0010\r\u001a\b\u0012\u0004\u0012\u00020\u00070\t\u00a2\u0006\b\n\u0000\u001a\u0004\b\u000e\u0010\f\u00a8\u0006$"}, d2 = {"Lcom/littlegig/app/presentation/auth/AuthViewModel;", "Landroidx/lifecycle/ViewModel;", "authRepository", "Lcom/littlegig/app/data/repository/AuthRepository;", "(Lcom/littlegig/app/data/repository/AuthRepository;)V", "_uiState", "Lkotlinx/coroutines/flow/MutableStateFlow;", "Lcom/littlegig/app/presentation/auth/AuthUiState;", "currentUser", "Lkotlinx/coroutines/flow/StateFlow;", "Lcom/littlegig/app/data/model/User;", "getCurrentUser", "()Lkotlinx/coroutines/flow/StateFlow;", "uiState", "getUiState", "clearError", "", "linkAnonymousAccount", "email", "", "password", "displayName", "phoneNumber", "userType", "Lcom/littlegig/app/data/model/UserType;", "linkAnonymousAccountWithGoogle", "googleSignInAccount", "Lcom/google/android/gms/auth/api/signin/GoogleSignInAccount;", "linkAnonymousAccountWithPhone", "signIn", "signInAnonymously", "signInWithGoogle", "signInWithPhone", "signOut", "signUp", "signUpWithPhone", "app_debug"})
@dagger.hilt.android.lifecycle.HiltViewModel()
public final class AuthViewModel extends androidx.lifecycle.ViewModel {
    @org.jetbrains.annotations.NotNull()
    private final com.littlegig.app.data.repository.AuthRepository authRepository = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.MutableStateFlow<com.littlegig.app.presentation.auth.AuthUiState> _uiState = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.StateFlow<com.littlegig.app.presentation.auth.AuthUiState> uiState = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.StateFlow<com.littlegig.app.data.model.User> currentUser = null;
    
    @javax.inject.Inject()
    public AuthViewModel(@org.jetbrains.annotations.NotNull()
    com.littlegig.app.data.repository.AuthRepository authRepository) {
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