package com.littlegig.app.presentation.account;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000@\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\u0002\n\u0002\b\u0005\n\u0002\u0010\u000b\n\u0002\b\u0002\b\u0007\u0018\u00002\u00020\u0001B\u000f\b\u0007\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\u0002\u0010\u0004J\u0006\u0010\u0011\u001a\u00020\u0012J\u0006\u0010\u0013\u001a\u00020\u0012J\u0006\u0010\u0014\u001a\u00020\u0012J\u0006\u0010\u0015\u001a\u00020\u0012J\u000e\u0010\u0016\u001a\u00020\u00122\u0006\u0010\u0017\u001a\u00020\u0018J\u0006\u0010\u0019\u001a\u00020\u0012R\u0014\u0010\u0005\u001a\b\u0012\u0004\u0012\u00020\u00070\u0006X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0002\u001a\u00020\u0003X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0019\u0010\b\u001a\n\u0012\u0006\u0012\u0004\u0018\u00010\n0\t\u00a2\u0006\b\n\u0000\u001a\u0004\b\u000b\u0010\fR\u0017\u0010\r\u001a\b\u0012\u0004\u0012\u00020\u00070\u000e\u00a2\u0006\b\n\u0000\u001a\u0004\b\u000f\u0010\u0010\u00a8\u0006\u001a"}, d2 = {"Lcom/littlegig/app/presentation/account/AccountViewModel;", "Landroidx/lifecycle/ViewModel;", "authRepository", "Lcom/littlegig/app/data/repository/AuthRepository;", "(Lcom/littlegig/app/data/repository/AuthRepository;)V", "_uiState", "Lkotlinx/coroutines/flow/MutableStateFlow;", "Lcom/littlegig/app/presentation/account/AccountUiState;", "currentUser", "Lkotlinx/coroutines/flow/Flow;", "Lcom/littlegig/app/data/model/User;", "getCurrentUser", "()Lkotlinx/coroutines/flow/Flow;", "uiState", "Lkotlinx/coroutines/flow/StateFlow;", "getUiState", "()Lkotlinx/coroutines/flow/StateFlow;", "becomeInfluencer", "", "clearError", "clearSuccess", "signOut", "toggleActiveNow", "isActive", "", "upgradeToBusinessAccount", "app_debug"})
@dagger.hilt.android.lifecycle.HiltViewModel
public final class AccountViewModel extends androidx.lifecycle.ViewModel {
    @org.jetbrains.annotations.NotNull
    private final com.littlegig.app.data.repository.AuthRepository authRepository = null;
    @org.jetbrains.annotations.NotNull
    private final kotlinx.coroutines.flow.MutableStateFlow<com.littlegig.app.presentation.account.AccountUiState> _uiState = null;
    @org.jetbrains.annotations.NotNull
    private final kotlinx.coroutines.flow.StateFlow<com.littlegig.app.presentation.account.AccountUiState> uiState = null;
    @org.jetbrains.annotations.NotNull
    private final kotlinx.coroutines.flow.Flow<com.littlegig.app.data.model.User> currentUser = null;
    
    @javax.inject.Inject
    public AccountViewModel(@org.jetbrains.annotations.NotNull
    com.littlegig.app.data.repository.AuthRepository authRepository) {
        super();
    }
    
    @org.jetbrains.annotations.NotNull
    public final kotlinx.coroutines.flow.StateFlow<com.littlegig.app.presentation.account.AccountUiState> getUiState() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull
    public final kotlinx.coroutines.flow.Flow<com.littlegig.app.data.model.User> getCurrentUser() {
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
    
    public final void toggleActiveNow(boolean isActive) {
    }
}