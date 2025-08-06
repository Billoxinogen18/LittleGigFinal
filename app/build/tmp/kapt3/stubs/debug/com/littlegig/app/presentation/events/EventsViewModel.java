package com.littlegig.app.presentation.events;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000@\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0010\u000e\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\u0002\n\u0002\b\u0006\n\u0002\u0010\u0007\n\u0002\b\u0005\b\u0007\u0018\u00002\u00020\u0001B\u000f\b\u0007\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\u0002\u0010\u0004J\u0010\u0010\u0010\u001a\u00020\u00112\b\u0010\u0012\u001a\u0004\u0018\u00010\tJ\b\u0010\u0013\u001a\u00020\u0011H\u0002J\b\u0010\u0014\u001a\u00020\u0011H\u0002J\u0016\u0010\u0015\u001a\u00020\u00112\u0006\u0010\u0016\u001a\u00020\u00072\u0006\u0010\u0017\u001a\u00020\u0018J\u0006\u0010\u0019\u001a\u00020\u0011J\u000e\u0010\u001a\u001a\u00020\u00112\u0006\u0010\u001b\u001a\u00020\u0007J\u000e\u0010\u001c\u001a\u00020\u00112\u0006\u0010\u0016\u001a\u00020\u0007R\u0014\u0010\u0005\u001a\b\u0012\u0004\u0012\u00020\u00070\u0006X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0016\u0010\b\u001a\n\u0012\u0006\u0012\u0004\u0018\u00010\t0\u0006X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0014\u0010\n\u001a\b\u0012\u0004\u0012\u00020\u000b0\u0006X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0002\u001a\u00020\u0003X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0017\u0010\f\u001a\b\u0012\u0004\u0012\u00020\u000b0\r\u00a2\u0006\b\n\u0000\u001a\u0004\b\u000e\u0010\u000f\u00a8\u0006\u001d"}, d2 = {"Lcom/littlegig/app/presentation/events/EventsViewModel;", "Landroidx/lifecycle/ViewModel;", "eventRepository", "Lcom/littlegig/app/data/repository/EventRepository;", "(Lcom/littlegig/app/data/repository/EventRepository;)V", "_searchQuery", "Lkotlinx/coroutines/flow/MutableStateFlow;", "", "_selectedCategory", "Lcom/littlegig/app/data/model/ContentCategory;", "_uiState", "Lcom/littlegig/app/presentation/events/EventsUiState;", "uiState", "Lkotlinx/coroutines/flow/StateFlow;", "getUiState", "()Lkotlinx/coroutines/flow/StateFlow;", "filterByCategory", "", "category", "loadEvents", "loadFeaturedEvents", "rateEvent", "eventId", "rating", "", "refreshEvents", "searchEvents", "query", "toggleEventLike", "app_debug"})
@dagger.hilt.android.lifecycle.HiltViewModel
public final class EventsViewModel extends androidx.lifecycle.ViewModel {
    @org.jetbrains.annotations.NotNull
    private final com.littlegig.app.data.repository.EventRepository eventRepository = null;
    @org.jetbrains.annotations.NotNull
    private final kotlinx.coroutines.flow.MutableStateFlow<com.littlegig.app.presentation.events.EventsUiState> _uiState = null;
    @org.jetbrains.annotations.NotNull
    private final kotlinx.coroutines.flow.StateFlow<com.littlegig.app.presentation.events.EventsUiState> uiState = null;
    @org.jetbrains.annotations.NotNull
    private final kotlinx.coroutines.flow.MutableStateFlow<java.lang.String> _searchQuery = null;
    @org.jetbrains.annotations.NotNull
    private final kotlinx.coroutines.flow.MutableStateFlow<com.littlegig.app.data.model.ContentCategory> _selectedCategory = null;
    
    @javax.inject.Inject
    public EventsViewModel(@org.jetbrains.annotations.NotNull
    com.littlegig.app.data.repository.EventRepository eventRepository) {
        super();
    }
    
    @org.jetbrains.annotations.NotNull
    public final kotlinx.coroutines.flow.StateFlow<com.littlegig.app.presentation.events.EventsUiState> getUiState() {
        return null;
    }
    
    private final void loadEvents() {
    }
    
    private final void loadFeaturedEvents() {
    }
    
    public final void searchEvents(@org.jetbrains.annotations.NotNull
    java.lang.String query) {
    }
    
    public final void filterByCategory(@org.jetbrains.annotations.Nullable
    com.littlegig.app.data.model.ContentCategory category) {
    }
    
    public final void refreshEvents() {
    }
    
    public final void toggleEventLike(@org.jetbrains.annotations.NotNull
    java.lang.String eventId) {
    }
    
    public final void rateEvent(@org.jetbrains.annotations.NotNull
    java.lang.String eventId, float rating) {
    }
}