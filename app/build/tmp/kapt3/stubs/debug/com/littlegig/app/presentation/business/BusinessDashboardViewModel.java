package com.littlegig.app.presentation.business;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000N\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0010\u0006\n\u0000\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0002\b\u0002\b\u0007\u0018\u00002\u00020\u0001B\u001f\b\u0007\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0005\u0012\u0006\u0010\u0006\u001a\u00020\u0007\u00a2\u0006\u0002\u0010\bJ\u0016\u0010\u0014\u001a\u00020\u00152\f\u0010\u0016\u001a\b\u0012\u0004\u0012\u00020\u00180\u0017H\u0002J\u0006\u0010\u0019\u001a\u00020\u001aJ\u0006\u0010\u001b\u001a\u00020\u001aR\u0016\u0010\t\u001a\n\u0012\u0006\u0012\u0004\u0018\u00010\u000b0\nX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0014\u0010\f\u001a\b\u0012\u0004\u0012\u00020\r0\nX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0019\u0010\u000e\u001a\n\u0012\u0006\u0012\u0004\u0018\u00010\u000b0\u000f\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0010\u0010\u0011R\u000e\u0010\u0006\u001a\u00020\u0007X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0002\u001a\u00020\u0003X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0004\u001a\u00020\u0005X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0017\u0010\u0012\u001a\b\u0012\u0004\u0012\u00020\r0\u000f\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0013\u0010\u0011\u00a8\u0006\u001c"}, d2 = {"Lcom/littlegig/app/presentation/business/BusinessDashboardViewModel;", "Landroidx/lifecycle/ViewModel;", "eventRepository", "Lcom/littlegig/app/data/repository/EventRepository;", "ticketRepository", "Lcom/littlegig/app/data/repository/TicketRepository;", "authRepository", "Lcom/littlegig/app/data/repository/AuthRepository;", "(Lcom/littlegig/app/data/repository/EventRepository;Lcom/littlegig/app/data/repository/TicketRepository;Lcom/littlegig/app/data/repository/AuthRepository;)V", "_analytics", "Lkotlinx/coroutines/flow/MutableStateFlow;", "Lcom/littlegig/app/presentation/business/BusinessAnalytics;", "_uiState", "Lcom/littlegig/app/presentation/business/BusinessDashboardUiState;", "analytics", "Lkotlinx/coroutines/flow/StateFlow;", "getAnalytics", "()Lkotlinx/coroutines/flow/StateFlow;", "uiState", "getUiState", "calculateTotalRevenue", "", "events", "", "Lcom/littlegig/app/data/model/Event;", "loadDashboardData", "", "refreshData", "app_debug"})
@dagger.hilt.android.lifecycle.HiltViewModel()
public final class BusinessDashboardViewModel extends androidx.lifecycle.ViewModel {
    @org.jetbrains.annotations.NotNull()
    private final com.littlegig.app.data.repository.EventRepository eventRepository = null;
    @org.jetbrains.annotations.NotNull()
    private final com.littlegig.app.data.repository.TicketRepository ticketRepository = null;
    @org.jetbrains.annotations.NotNull()
    private final com.littlegig.app.data.repository.AuthRepository authRepository = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.MutableStateFlow<com.littlegig.app.presentation.business.BusinessDashboardUiState> _uiState = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.StateFlow<com.littlegig.app.presentation.business.BusinessDashboardUiState> uiState = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.MutableStateFlow<com.littlegig.app.presentation.business.BusinessAnalytics> _analytics = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.StateFlow<com.littlegig.app.presentation.business.BusinessAnalytics> analytics = null;
    
    @javax.inject.Inject()
    public BusinessDashboardViewModel(@org.jetbrains.annotations.NotNull()
    com.littlegig.app.data.repository.EventRepository eventRepository, @org.jetbrains.annotations.NotNull()
    com.littlegig.app.data.repository.TicketRepository ticketRepository, @org.jetbrains.annotations.NotNull()
    com.littlegig.app.data.repository.AuthRepository authRepository) {
        super();
    }
    
    @org.jetbrains.annotations.NotNull()
    public final kotlinx.coroutines.flow.StateFlow<com.littlegig.app.presentation.business.BusinessDashboardUiState> getUiState() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final kotlinx.coroutines.flow.StateFlow<com.littlegig.app.presentation.business.BusinessAnalytics> getAnalytics() {
        return null;
    }
    
    public final void loadDashboardData() {
    }
    
    private final double calculateTotalRevenue(java.util.List<com.littlegig.app.data.model.Event> events) {
        return 0.0;
    }
    
    public final void refreshData() {
    }
}