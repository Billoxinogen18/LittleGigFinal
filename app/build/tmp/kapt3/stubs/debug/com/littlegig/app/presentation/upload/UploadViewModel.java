package com.littlegig.app.presentation.upload;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000r\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\u0002\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0010\u0006\n\u0000\n\u0002\u0010\b\n\u0000\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0000\b\u0007\u0018\u00002\u00020\u0001B\u0017\b\u0007\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0005\u00a2\u0006\u0002\u0010\u0006J\u0006\u0010\u0013\u001a\u00020\u0014Jh\u0010\u0015\u001a\u00020\u00142\u0006\u0010\u0016\u001a\u00020\u00172\u0006\u0010\u0018\u001a\u00020\u00172\u0006\u0010\u0019\u001a\u00020\u001a2\u0006\u0010\u001b\u001a\u00020\u00172\u0006\u0010\u001c\u001a\u00020\u00172\u0006\u0010\u001d\u001a\u00020\u00172\u0006\u0010\u001e\u001a\u00020\u001f2\u0006\u0010 \u001a\u00020!2\f\u0010\"\u001a\b\u0012\u0004\u0012\u00020$0#2\b\u0010%\u001a\u0004\u0018\u00010&2\b\u0010\'\u001a\u0004\u0018\u00010&J\u000e\u0010(\u001a\u00020\u00142\u0006\u0010)\u001a\u00020\u0017J\u000e\u0010*\u001a\u00020\u00142\u0006\u0010+\u001a\u00020,R\u0014\u0010\u0007\u001a\b\u0012\u0004\u0012\u00020\t0\bX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0004\u001a\u00020\u0005X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0019\u0010\n\u001a\n\u0012\u0006\u0012\u0004\u0018\u00010\f0\u000b\u00a2\u0006\b\n\u0000\u001a\u0004\b\r\u0010\u000eR\u000e\u0010\u0002\u001a\u00020\u0003X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0017\u0010\u000f\u001a\b\u0012\u0004\u0012\u00020\t0\u0010\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0011\u0010\u0012\u00a8\u0006-"}, d2 = {"Lcom/littlegig/app/presentation/upload/UploadViewModel;", "Landroidx/lifecycle/ViewModel;", "eventRepository", "Lcom/littlegig/app/data/repository/EventRepository;", "authRepository", "Lcom/littlegig/app/data/repository/AuthRepository;", "(Lcom/littlegig/app/data/repository/EventRepository;Lcom/littlegig/app/data/repository/AuthRepository;)V", "_uiState", "Lkotlinx/coroutines/flow/MutableStateFlow;", "Lcom/littlegig/app/presentation/upload/UploadUiState;", "currentUser", "Lkotlinx/coroutines/flow/Flow;", "Lcom/littlegig/app/data/model/User;", "getCurrentUser", "()Lkotlinx/coroutines/flow/Flow;", "uiState", "Lkotlinx/coroutines/flow/StateFlow;", "getUiState", "()Lkotlinx/coroutines/flow/StateFlow;", "clearMessages", "", "createContent", "title", "", "description", "category", "Lcom/littlegig/app/data/model/ContentCategory;", "locationName", "locationAddress", "city", "price", "", "capacity", "", "images", "", "Landroid/net/Uri;", "startDate", "Ljava/util/Date;", "endDate", "searchPlaces", "query", "setSelectedPlace", "place", "Lcom/littlegig/app/presentation/components/PlaceSuggestion;", "app_debug"})
@dagger.hilt.android.lifecycle.HiltViewModel
public final class UploadViewModel extends androidx.lifecycle.ViewModel {
    @org.jetbrains.annotations.NotNull
    private final com.littlegig.app.data.repository.EventRepository eventRepository = null;
    @org.jetbrains.annotations.NotNull
    private final com.littlegig.app.data.repository.AuthRepository authRepository = null;
    @org.jetbrains.annotations.NotNull
    private final kotlinx.coroutines.flow.MutableStateFlow<com.littlegig.app.presentation.upload.UploadUiState> _uiState = null;
    @org.jetbrains.annotations.NotNull
    private final kotlinx.coroutines.flow.StateFlow<com.littlegig.app.presentation.upload.UploadUiState> uiState = null;
    @org.jetbrains.annotations.NotNull
    private final kotlinx.coroutines.flow.Flow<com.littlegig.app.data.model.User> currentUser = null;
    
    @javax.inject.Inject
    public UploadViewModel(@org.jetbrains.annotations.NotNull
    com.littlegig.app.data.repository.EventRepository eventRepository, @org.jetbrains.annotations.NotNull
    com.littlegig.app.data.repository.AuthRepository authRepository) {
        super();
    }
    
    @org.jetbrains.annotations.NotNull
    public final kotlinx.coroutines.flow.StateFlow<com.littlegig.app.presentation.upload.UploadUiState> getUiState() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull
    public final kotlinx.coroutines.flow.Flow<com.littlegig.app.data.model.User> getCurrentUser() {
        return null;
    }
    
    public final void createContent(@org.jetbrains.annotations.NotNull
    java.lang.String title, @org.jetbrains.annotations.NotNull
    java.lang.String description, @org.jetbrains.annotations.NotNull
    com.littlegig.app.data.model.ContentCategory category, @org.jetbrains.annotations.NotNull
    java.lang.String locationName, @org.jetbrains.annotations.NotNull
    java.lang.String locationAddress, @org.jetbrains.annotations.NotNull
    java.lang.String city, double price, int capacity, @org.jetbrains.annotations.NotNull
    java.util.List<? extends android.net.Uri> images, @org.jetbrains.annotations.Nullable
    java.util.Date startDate, @org.jetbrains.annotations.Nullable
    java.util.Date endDate) {
    }
    
    public final void clearMessages() {
    }
    
    public final void searchPlaces(@org.jetbrains.annotations.NotNull
    java.lang.String query) {
    }
    
    public final void setSelectedPlace(@org.jetbrains.annotations.NotNull
    com.littlegig.app.presentation.components.PlaceSuggestion place) {
    }
}