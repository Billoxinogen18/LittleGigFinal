package com.littlegig.app.presentation.events;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000T\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\u0002\n\u0002\b\u0003\n\u0002\u0010\u000e\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\b\u0002\b\u0007\u0018\u00002\u00020\u0001B/\b\u0007\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0005\u0012\u0006\u0010\u0006\u001a\u00020\u0007\u0012\u0006\u0010\b\u001a\u00020\t\u0012\u0006\u0010\n\u001a\u00020\u000b\u00a2\u0006\u0002\u0010\fJ\u0006\u0010\u0014\u001a\u00020\u0015J\u0006\u0010\u0016\u001a\u00020\u0015J\u0010\u0010\u0017\u001a\u00020\u00152\b\u0010\u0018\u001a\u0004\u0018\u00010\u0019J\u000e\u0010\u001a\u001a\u00020\u00152\u0006\u0010\u001b\u001a\u00020\u0019J\b\u0010\u001c\u001a\u00020\u0015H\u0014J\u000e\u0010\u001d\u001a\u00020\u00152\u0006\u0010\u001e\u001a\u00020\u001fJ\u0006\u0010 \u001a\u00020\u0015R\u0014\u0010\r\u001a\b\u0012\u0004\u0012\u00020\u000f0\u000eX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0004\u001a\u00020\u0005X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0002\u001a\u00020\u0003X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\n\u001a\u00020\u000bX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\b\u001a\u00020\tX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0017\u0010\u0010\u001a\b\u0012\u0004\u0012\u00020\u000f0\u0011\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0012\u0010\u0013R\u000e\u0010\u0006\u001a\u00020\u0007X\u0082\u0004\u00a2\u0006\u0002\n\u0000\u00a8\u0006!"}, d2 = {"Lcom/littlegig/app/presentation/events/EventDetailsViewModel;", "Landroidx/lifecycle/ViewModel;", "eventRepository", "Lcom/littlegig/app/data/repository/EventRepository;", "authRepository", "Lcom/littlegig/app/data/repository/AuthRepository;", "userRepository", "Lcom/littlegig/app/data/repository/UserRepository;", "paymentRepository", "Lcom/littlegig/app/data/repository/PaymentRepository;", "notificationRepository", "Lcom/littlegig/app/data/repository/NotificationRepository;", "(Lcom/littlegig/app/data/repository/EventRepository;Lcom/littlegig/app/data/repository/AuthRepository;Lcom/littlegig/app/data/repository/UserRepository;Lcom/littlegig/app/data/repository/PaymentRepository;Lcom/littlegig/app/data/repository/NotificationRepository;)V", "_uiState", "Lkotlinx/coroutines/flow/MutableStateFlow;", "Lcom/littlegig/app/presentation/events/EventDetailsUiState;", "uiState", "Lkotlinx/coroutines/flow/StateFlow;", "getUiState", "()Lkotlinx/coroutines/flow/StateFlow;", "buyTicket", "", "clearError", "followOrganizer", "organizerId", "", "loadEventDetails", "eventId", "onCleared", "shareEvent", "context", "Landroid/content/Context;", "toggleEventLike", "app_debug"})
@dagger.hilt.android.lifecycle.HiltViewModel()
public final class EventDetailsViewModel extends androidx.lifecycle.ViewModel {
    @org.jetbrains.annotations.NotNull()
    private final com.littlegig.app.data.repository.EventRepository eventRepository = null;
    @org.jetbrains.annotations.NotNull()
    private final com.littlegig.app.data.repository.AuthRepository authRepository = null;
    @org.jetbrains.annotations.NotNull()
    private final com.littlegig.app.data.repository.UserRepository userRepository = null;
    @org.jetbrains.annotations.NotNull()
    private final com.littlegig.app.data.repository.PaymentRepository paymentRepository = null;
    @org.jetbrains.annotations.NotNull()
    private final com.littlegig.app.data.repository.NotificationRepository notificationRepository = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.MutableStateFlow<com.littlegig.app.presentation.events.EventDetailsUiState> _uiState = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.StateFlow<com.littlegig.app.presentation.events.EventDetailsUiState> uiState = null;
    
    @javax.inject.Inject()
    public EventDetailsViewModel(@org.jetbrains.annotations.NotNull()
    com.littlegig.app.data.repository.EventRepository eventRepository, @org.jetbrains.annotations.NotNull()
    com.littlegig.app.data.repository.AuthRepository authRepository, @org.jetbrains.annotations.NotNull()
    com.littlegig.app.data.repository.UserRepository userRepository, @org.jetbrains.annotations.NotNull()
    com.littlegig.app.data.repository.PaymentRepository paymentRepository, @org.jetbrains.annotations.NotNull()
    com.littlegig.app.data.repository.NotificationRepository notificationRepository) {
        super();
    }
    
    @org.jetbrains.annotations.NotNull()
    public final kotlinx.coroutines.flow.StateFlow<com.littlegig.app.presentation.events.EventDetailsUiState> getUiState() {
        return null;
    }
    
    public final void loadEventDetails(@org.jetbrains.annotations.NotNull()
    java.lang.String eventId) {
    }
    
    public final void toggleEventLike() {
    }
    
    public final void followOrganizer(@org.jetbrains.annotations.Nullable()
    java.lang.String organizerId) {
    }
    
    public final void shareEvent(@org.jetbrains.annotations.NotNull()
    android.content.Context context) {
    }
    
    public final void buyTicket() {
    }
    
    public final void clearError() {
    }
    
    @java.lang.Override()
    protected void onCleared() {
    }
}