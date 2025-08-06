package com.littlegig.app.presentation.recaps;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u00008\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\u0002\n\u0002\b\u0003\n\u0002\u0010\u000e\n\u0000\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0002\b\u0002\b\u0007\u0018\u00002\u00020\u0001B\u0007\b\u0007\u00a2\u0006\u0002\u0010\u0002J\b\u0010\n\u001a\u00020\u000bH\u0002J\b\u0010\f\u001a\u00020\u000bH\u0002J$\u0010\r\u001a\u00020\u000b2\u0006\u0010\u000e\u001a\u00020\u000f2\f\u0010\u0010\u001a\b\u0012\u0004\u0012\u00020\u00120\u00112\u0006\u0010\u0013\u001a\u00020\u000fR\u0014\u0010\u0003\u001a\b\u0012\u0004\u0012\u00020\u00050\u0004X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0017\u0010\u0006\u001a\b\u0012\u0004\u0012\u00020\u00050\u0007\u00a2\u0006\b\n\u0000\u001a\u0004\b\b\u0010\t\u00a8\u0006\u0014"}, d2 = {"Lcom/littlegig/app/presentation/recaps/RecapsUploadViewModel;", "Landroidx/lifecycle/ViewModel;", "()V", "_uiState", "Lkotlinx/coroutines/flow/MutableStateFlow;", "Lcom/littlegig/app/presentation/recaps/RecapsUploadUiState;", "uiState", "Lkotlinx/coroutines/flow/StateFlow;", "getUiState", "()Lkotlinx/coroutines/flow/StateFlow;", "checkLocationValidity", "", "loadUserEvents", "uploadRecap", "eventId", "", "images", "", "Landroid/net/Uri;", "caption", "app_debug"})
@dagger.hilt.android.lifecycle.HiltViewModel
public final class RecapsUploadViewModel extends androidx.lifecycle.ViewModel {
    @org.jetbrains.annotations.NotNull
    private final kotlinx.coroutines.flow.MutableStateFlow<com.littlegig.app.presentation.recaps.RecapsUploadUiState> _uiState = null;
    @org.jetbrains.annotations.NotNull
    private final kotlinx.coroutines.flow.StateFlow<com.littlegig.app.presentation.recaps.RecapsUploadUiState> uiState = null;
    
    @javax.inject.Inject
    public RecapsUploadViewModel() {
        super();
    }
    
    @org.jetbrains.annotations.NotNull
    public final kotlinx.coroutines.flow.StateFlow<com.littlegig.app.presentation.recaps.RecapsUploadUiState> getUiState() {
        return null;
    }
    
    private final void loadUserEvents() {
    }
    
    private final void checkLocationValidity() {
    }
    
    public final void uploadRecap(@org.jetbrains.annotations.NotNull
    java.lang.String eventId, @org.jetbrains.annotations.NotNull
    java.util.List<? extends android.net.Uri> images, @org.jetbrains.annotations.NotNull
    java.lang.String caption) {
    }
}