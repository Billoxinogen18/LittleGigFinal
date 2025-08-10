package com.littlegig.app.presentation;

@dagger.hilt.android.AndroidEntryPoint()
@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000L\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\b\u0007\u0018\u00002\u00020\u0001B\u0005\u00a2\u0006\u0002\u0010\u0002J\u0010\u0010\u0014\u001a\u00020\u00152\u0006\u0010\u0016\u001a\u00020\u0013H\u0002J\u0010\u0010\u0017\u001a\u00020\u00152\u0006\u0010\u0018\u001a\u00020\u0019H\u0002J\u0010\u0010\u001a\u001a\u00020\u00152\u0006\u0010\u001b\u001a\u00020\u001cH\u0002J\u0012\u0010\u001d\u001a\u00020\u00152\b\u0010\u001e\u001a\u0004\u0018\u00010\u001fH\u0014J\u0010\u0010 \u001a\u00020\u00152\u0006\u0010\u0016\u001a\u00020\u0013H\u0014J\u0006\u0010!\u001a\u00020\u0015R\u001b\u0010\u0003\u001a\u00020\u00048BX\u0082\u0084\u0002\u00a2\u0006\f\n\u0004\b\u0007\u0010\b\u001a\u0004\b\u0005\u0010\u0006R\u000e\u0010\t\u001a\u00020\nX\u0082.\u00a2\u0006\u0002\n\u0000R\u001e\u0010\u000b\u001a\u00020\f8\u0006@\u0006X\u0087.\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\r\u0010\u000e\"\u0004\b\u000f\u0010\u0010R\u0014\u0010\u0011\u001a\b\u0012\u0004\u0012\u00020\u00130\u0012X\u0082\u0004\u00a2\u0006\u0002\n\u0000\u00a8\u0006\""}, d2 = {"Lcom/littlegig/app/presentation/MainActivity;", "Landroidx/activity/ComponentActivity;", "()V", "authViewModel", "Lcom/littlegig/app/presentation/auth/AuthViewModel;", "getAuthViewModel", "()Lcom/littlegig/app/presentation/auth/AuthViewModel;", "authViewModel$delegate", "Lkotlin/Lazy;", "googleSignInClient", "Lcom/google/android/gms/auth/api/signin/GoogleSignInClient;", "paymentRepository", "Lcom/littlegig/app/data/repository/PaymentRepository;", "getPaymentRepository", "()Lcom/littlegig/app/data/repository/PaymentRepository;", "setPaymentRepository", "(Lcom/littlegig/app/data/repository/PaymentRepository;)V", "signInLauncher", "Landroidx/activity/result/ActivityResultLauncher;", "Landroid/content/Intent;", "handleDeepLink", "", "intent", "handleGoogleSignInFailure", "exception", "Lcom/google/android/gms/common/api/ApiException;", "handleGoogleSignInSuccess", "account", "Lcom/google/android/gms/auth/api/signin/GoogleSignInAccount;", "onCreate", "savedInstanceState", "Landroid/os/Bundle;", "onNewIntent", "startGoogleSignIn", "app_debug"})
public final class MainActivity extends androidx.activity.ComponentActivity {
    private com.google.android.gms.auth.api.signin.GoogleSignInClient googleSignInClient;
    @org.jetbrains.annotations.NotNull()
    private final kotlin.Lazy authViewModel$delegate = null;
    @javax.inject.Inject()
    public com.littlegig.app.data.repository.PaymentRepository paymentRepository;
    @org.jetbrains.annotations.NotNull()
    private final androidx.activity.result.ActivityResultLauncher<android.content.Intent> signInLauncher = null;
    
    public MainActivity() {
        super(0);
    }
    
    private final com.littlegig.app.presentation.auth.AuthViewModel getAuthViewModel() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final com.littlegig.app.data.repository.PaymentRepository getPaymentRepository() {
        return null;
    }
    
    public final void setPaymentRepository(@org.jetbrains.annotations.NotNull()
    com.littlegig.app.data.repository.PaymentRepository p0) {
    }
    
    @java.lang.Override()
    protected void onCreate(@org.jetbrains.annotations.Nullable()
    android.os.Bundle savedInstanceState) {
    }
    
    @java.lang.Override()
    protected void onNewIntent(@org.jetbrains.annotations.NotNull()
    android.content.Intent intent) {
    }
    
    private final void handleDeepLink(android.content.Intent intent) {
    }
    
    public final void startGoogleSignIn() {
    }
    
    private final void handleGoogleSignInSuccess(com.google.android.gms.auth.api.signin.GoogleSignInAccount account) {
    }
    
    private final void handleGoogleSignInFailure(com.google.android.gms.common.api.ApiException exception) {
    }
}