package com.littlegig.app.presentation.recaps;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000`\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0010\u000b\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\n\n\u0002\u0010\u0002\n\u0002\b\u0002\n\u0002\u0010\u0006\n\u0002\b\r\n\u0002\u0010\u000e\n\u0002\b\u0006\b\u0007\u0018\u00002\u00020\u0001B\u001f\b\u0007\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0005\u0012\u0006\u0010\u0006\u001a\u00020\u0007\u00a2\u0006\u0002\u0010\bJ\u000e\u0010\u001f\u001a\u00020 2\u0006\u0010!\u001a\u00020\u0010J(\u0010\"\u001a\u00020#2\u0006\u0010$\u001a\u00020#2\u0006\u0010%\u001a\u00020#2\u0006\u0010&\u001a\u00020#2\u0006\u0010\'\u001a\u00020#H\u0002J\u0006\u0010(\u001a\u00020 J\u0006\u0010)\u001a\u00020 J\u0006\u0010*\u001a\u00020 J\u0006\u0010+\u001a\u00020 J\u000e\u0010,\u001a\u00020 2\u0006\u0010!\u001a\u00020\u0010J\u000e\u0010-\u001a\u00020 2\u0006\u0010.\u001a\u00020\rJ$\u0010/\u001a\u00020 2\u0006\u00100\u001a\u0002012\f\u00102\u001a\b\u0012\u0004\u0012\u0002010\u000f2\u0006\u00103\u001a\u000201J\u001e\u00104\u001a\u00020 2\u0006\u00100\u001a\u0002012\u0006\u00105\u001a\u00020#2\u0006\u00106\u001a\u00020#R\u0014\u0010\t\u001a\b\u0012\u0004\u0012\u00020\u000b0\nX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0016\u0010\f\u001a\n\u0012\u0006\u0012\u0004\u0018\u00010\r0\nX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u001a\u0010\u000e\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u00100\u000f0\nX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0014\u0010\u0011\u001a\b\u0012\u0004\u0012\u00020\u00120\nX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u001a\u0010\u0013\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\r0\u000f0\nX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0006\u001a\u00020\u0007X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0002\u001a\u00020\u0003X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0017\u0010\u0014\u001a\b\u0012\u0004\u0012\u00020\u000b0\u0015\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0014\u0010\u0016R\u000e\u0010\u0004\u001a\u00020\u0005X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0019\u0010\u0017\u001a\n\u0012\u0006\u0012\u0004\u0018\u00010\r0\u0015\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0018\u0010\u0016R\u001d\u0010\u0019\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u00100\u000f0\u0015\u00a2\u0006\b\n\u0000\u001a\u0004\b\u001a\u0010\u0016R\u0017\u0010\u001b\u001a\b\u0012\u0004\u0012\u00020\u00120\u0015\u00a2\u0006\b\n\u0000\u001a\u0004\b\u001c\u0010\u0016R\u001d\u0010\u001d\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\r0\u000f0\u0015\u00a2\u0006\b\n\u0000\u001a\u0004\b\u001e\u0010\u0016\u00a8\u00067"}, d2 = {"Lcom/littlegig/app/presentation/recaps/RecapsUploadViewModel;", "Landroidx/lifecycle/ViewModel;", "eventRepository", "Lcom/littlegig/app/data/repository/EventRepository;", "recapRepository", "Lcom/littlegig/app/data/repository/RecapRepository;", "authRepository", "Lcom/littlegig/app/data/repository/AuthRepository;", "(Lcom/littlegig/app/data/repository/EventRepository;Lcom/littlegig/app/data/repository/RecapRepository;Lcom/littlegig/app/data/repository/AuthRepository;)V", "_isLocationValid", "Lkotlinx/coroutines/flow/MutableStateFlow;", "", "_selectedEvent", "Lcom/littlegig/app/data/model/Event;", "_selectedMedia", "", "Landroid/net/Uri;", "_uiState", "Lcom/littlegig/app/presentation/recaps/RecapsUploadUiState;", "_userEvents", "isLocationValid", "Lkotlinx/coroutines/flow/StateFlow;", "()Lkotlinx/coroutines/flow/StateFlow;", "selectedEvent", "getSelectedEvent", "selectedMedia", "getSelectedMedia", "uiState", "getUiState", "userEvents", "getUserEvents", "addMedia", "", "uri", "calculateDistance", "", "lat1", "lon1", "lat2", "lon2", "clearError", "clearMedia", "clearSuccess", "loadUserEvents", "removeMedia", "selectEvent", "event", "uploadRecap", "eventId", "", "mediaUrls", "caption", "verifyLocation", "userLatitude", "userLongitude", "app_debug"})
@dagger.hilt.android.lifecycle.HiltViewModel
public final class RecapsUploadViewModel extends androidx.lifecycle.ViewModel {
    @org.jetbrains.annotations.NotNull
    private final com.littlegig.app.data.repository.EventRepository eventRepository = null;
    @org.jetbrains.annotations.NotNull
    private final com.littlegig.app.data.repository.RecapRepository recapRepository = null;
    @org.jetbrains.annotations.NotNull
    private final com.littlegig.app.data.repository.AuthRepository authRepository = null;
    @org.jetbrains.annotations.NotNull
    private final kotlinx.coroutines.flow.MutableStateFlow<com.littlegig.app.presentation.recaps.RecapsUploadUiState> _uiState = null;
    @org.jetbrains.annotations.NotNull
    private final kotlinx.coroutines.flow.StateFlow<com.littlegig.app.presentation.recaps.RecapsUploadUiState> uiState = null;
    @org.jetbrains.annotations.NotNull
    private final kotlinx.coroutines.flow.MutableStateFlow<java.util.List<com.littlegig.app.data.model.Event>> _userEvents = null;
    @org.jetbrains.annotations.NotNull
    private final kotlinx.coroutines.flow.StateFlow<java.util.List<com.littlegig.app.data.model.Event>> userEvents = null;
    @org.jetbrains.annotations.NotNull
    private final kotlinx.coroutines.flow.MutableStateFlow<com.littlegig.app.data.model.Event> _selectedEvent = null;
    @org.jetbrains.annotations.NotNull
    private final kotlinx.coroutines.flow.StateFlow<com.littlegig.app.data.model.Event> selectedEvent = null;
    @org.jetbrains.annotations.NotNull
    private final kotlinx.coroutines.flow.MutableStateFlow<java.lang.Boolean> _isLocationValid = null;
    @org.jetbrains.annotations.NotNull
    private final kotlinx.coroutines.flow.StateFlow<java.lang.Boolean> isLocationValid = null;
    @org.jetbrains.annotations.NotNull
    private final kotlinx.coroutines.flow.MutableStateFlow<java.util.List<android.net.Uri>> _selectedMedia = null;
    @org.jetbrains.annotations.NotNull
    private final kotlinx.coroutines.flow.StateFlow<java.util.List<android.net.Uri>> selectedMedia = null;
    
    @javax.inject.Inject
    public RecapsUploadViewModel(@org.jetbrains.annotations.NotNull
    com.littlegig.app.data.repository.EventRepository eventRepository, @org.jetbrains.annotations.NotNull
    com.littlegig.app.data.repository.RecapRepository recapRepository, @org.jetbrains.annotations.NotNull
    com.littlegig.app.data.repository.AuthRepository authRepository) {
        super();
    }
    
    @org.jetbrains.annotations.NotNull
    public final kotlinx.coroutines.flow.StateFlow<com.littlegig.app.presentation.recaps.RecapsUploadUiState> getUiState() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull
    public final kotlinx.coroutines.flow.StateFlow<java.util.List<com.littlegig.app.data.model.Event>> getUserEvents() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull
    public final kotlinx.coroutines.flow.StateFlow<com.littlegig.app.data.model.Event> getSelectedEvent() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull
    public final kotlinx.coroutines.flow.StateFlow<java.lang.Boolean> isLocationValid() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull
    public final kotlinx.coroutines.flow.StateFlow<java.util.List<android.net.Uri>> getSelectedMedia() {
        return null;
    }
    
    public final void loadUserEvents() {
    }
    
    public final void verifyLocation(@org.jetbrains.annotations.NotNull
    java.lang.String eventId, double userLatitude, double userLongitude) {
    }
    
    public final void uploadRecap(@org.jetbrains.annotations.NotNull
    java.lang.String eventId, @org.jetbrains.annotations.NotNull
    java.util.List<java.lang.String> mediaUrls, @org.jetbrains.annotations.NotNull
    java.lang.String caption) {
    }
    
    public final void selectEvent(@org.jetbrains.annotations.NotNull
    com.littlegig.app.data.model.Event event) {
    }
    
    public final void addMedia(@org.jetbrains.annotations.NotNull
    android.net.Uri uri) {
    }
    
    public final void removeMedia(@org.jetbrains.annotations.NotNull
    android.net.Uri uri) {
    }
    
    public final void clearMedia() {
    }
    
    public final void clearError() {
    }
    
    public final void clearSuccess() {
    }
    
    private final double calculateDistance(double lat1, double lon1, double lat2, double lon2) {
        return 0.0;
    }
}