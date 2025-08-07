package com.littlegig.app.presentation.events;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000\u0080\u0001\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0010\u000e\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0010\u000b\n\u0000\n\u0002\u0010\u0007\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0010\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0010 \n\u0002\b\u0007\n\u0002\u0010\u0006\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0002\b\u0006\b\u0007\u0018\u00002\u00020\u0001B\'\b\u0007\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0005\u0012\u0006\u0010\u0006\u001a\u00020\u0007\u0012\u0006\u0010\b\u001a\u00020\t\u00a2\u0006\u0002\u0010\nJ\u0006\u0010\u001c\u001a\u00020\u001dJ\u001b\u0010\u001e\u001a\u0004\u0018\u00010\r2\u0006\u0010\u001f\u001a\u00020 H\u0086@\u00f8\u0001\u0000\u00a2\u0006\u0002\u0010!J\u0011\u0010\"\u001a\u00020#H\u0082@\u00f8\u0001\u0000\u00a2\u0006\u0002\u0010$J\u000e\u0010%\u001a\u00020\u001d2\u0006\u0010&\u001a\u00020\rJ\u0006\u0010\'\u001a\u00020\u001dJ%\u0010(\u001a\b\u0012\u0004\u0012\u00020 0)2\f\u0010*\u001a\b\u0012\u0004\u0012\u00020 0)H\u0082@\u00f8\u0001\u0000\u00a2\u0006\u0002\u0010+J\u0016\u0010,\u001a\u00020\u001d2\u0006\u0010&\u001a\u00020\r2\u0006\u0010-\u001a\u00020\u0015J\u0006\u0010.\u001a\u00020\u001dJ\u000e\u0010/\u001a\u00020\u001d2\u0006\u0010&\u001a\u00020\rJ\u0018\u00100\u001a\u0002012\u0006\u00102\u001a\u00020 2\u0006\u00103\u001a\u00020#H\u0002J\u0010\u00104\u001a\u00020\u001d2\b\u00105\u001a\u0004\u0018\u000106J\b\u00107\u001a\u00020\u001dH\u0002J\u000e\u00108\u001a\u00020\u001d2\u0006\u0010&\u001a\u00020\rJ\u000e\u00109\u001a\u00020\u001d2\u0006\u0010&\u001a\u00020\rJ\u000e\u0010:\u001a\u00020\u001d2\u0006\u0010;\u001a\u00020\rR\u0014\u0010\u000b\u001a\b\u0012\u0004\u0012\u00020\r0\fX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0014\u0010\u000e\u001a\b\u0012\u0004\u0012\u00020\u000f0\fX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0004\u001a\u00020\u0005X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\b\u001a\u00020\tX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0002\u001a\u00020\u0003X\u0082\u0004\u00a2\u0006\u0002\n\u0000R \u0010\u0010\u001a\u0014\u0012\u0010\u0012\u000e\u0012\u0004\u0012\u00020\r\u0012\u0004\u0012\u00020\u00130\u00120\u0011X\u0082\u0004\u00a2\u0006\u0002\n\u0000R \u0010\u0014\u001a\u0014\u0012\u0010\u0012\u000e\u0012\u0004\u0012\u00020\r\u0012\u0004\u0012\u00020\u00150\u00120\u0011X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0017\u0010\u0016\u001a\b\u0012\u0004\u0012\u00020\r0\u0017\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0018\u0010\u0019R\u000e\u0010\u0006\u001a\u00020\u0007X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0017\u0010\u001a\u001a\b\u0012\u0004\u0012\u00020\u000f0\u0017\u00a2\u0006\b\n\u0000\u001a\u0004\b\u001b\u0010\u0019\u0082\u0002\u0004\n\u0002\b\u0019\u00a8\u0006<"}, d2 = {"Lcom/littlegig/app/presentation/events/EventsViewModel;", "Landroidx/lifecycle/ViewModel;", "eventRepository", "Lcom/littlegig/app/data/repository/EventRepository;", "authRepository", "Lcom/littlegig/app/data/repository/AuthRepository;", "sharingRepository", "Lcom/littlegig/app/data/repository/SharingRepository;", "configRepository", "Lcom/littlegig/app/data/repository/ConfigRepository;", "(Lcom/littlegig/app/data/repository/EventRepository;Lcom/littlegig/app/data/repository/AuthRepository;Lcom/littlegig/app/data/repository/SharingRepository;Lcom/littlegig/app/data/repository/ConfigRepository;)V", "_searchQuery", "Lkotlinx/coroutines/flow/MutableStateFlow;", "", "_uiState", "Lcom/littlegig/app/presentation/events/EventsUiState;", "likeEvents", "Lkotlinx/coroutines/flow/MutableSharedFlow;", "Lkotlin/Pair;", "", "rateEvents", "", "searchQuery", "Lkotlinx/coroutines/flow/StateFlow;", "getSearchQuery", "()Lkotlinx/coroutines/flow/StateFlow;", "uiState", "getUiState", "clearError", "", "createEventShareLink", "event", "Lcom/littlegig/app/data/model/Event;", "(Lcom/littlegig/app/data/model/Event;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "fetchWeights", "Lcom/littlegig/app/data/repository/FeedWeights;", "(Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "joinWaitlist", "eventId", "loadEvents", "personalizeAndFilterEvents", "", "events", "(Ljava/util/List;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "rateEvent", "rating", "refreshEvents", "rsvp", "scoreEvent", "", "e", "w", "selectCategory", "category", "Lcom/littlegig/app/data/model/ContentCategory;", "setupThrottledActions", "subscribePriceDrop", "toggleEventLike", "updateSearchQuery", "query", "app_debug"})
@dagger.hilt.android.lifecycle.HiltViewModel
public final class EventsViewModel extends androidx.lifecycle.ViewModel {
    @org.jetbrains.annotations.NotNull
    private final com.littlegig.app.data.repository.EventRepository eventRepository = null;
    @org.jetbrains.annotations.NotNull
    private final com.littlegig.app.data.repository.AuthRepository authRepository = null;
    @org.jetbrains.annotations.NotNull
    private final com.littlegig.app.data.repository.SharingRepository sharingRepository = null;
    @org.jetbrains.annotations.NotNull
    private final com.littlegig.app.data.repository.ConfigRepository configRepository = null;
    @org.jetbrains.annotations.NotNull
    private final kotlinx.coroutines.flow.MutableStateFlow<com.littlegig.app.presentation.events.EventsUiState> _uiState = null;
    @org.jetbrains.annotations.NotNull
    private final kotlinx.coroutines.flow.StateFlow<com.littlegig.app.presentation.events.EventsUiState> uiState = null;
    @org.jetbrains.annotations.NotNull
    private final kotlinx.coroutines.flow.MutableStateFlow<java.lang.String> _searchQuery = null;
    @org.jetbrains.annotations.NotNull
    private final kotlinx.coroutines.flow.StateFlow<java.lang.String> searchQuery = null;
    @org.jetbrains.annotations.NotNull
    private final kotlinx.coroutines.flow.MutableSharedFlow<kotlin.Pair<java.lang.String, java.lang.Boolean>> likeEvents = null;
    @org.jetbrains.annotations.NotNull
    private final kotlinx.coroutines.flow.MutableSharedFlow<kotlin.Pair<java.lang.String, java.lang.Float>> rateEvents = null;
    
    @javax.inject.Inject
    public EventsViewModel(@org.jetbrains.annotations.NotNull
    com.littlegig.app.data.repository.EventRepository eventRepository, @org.jetbrains.annotations.NotNull
    com.littlegig.app.data.repository.AuthRepository authRepository, @org.jetbrains.annotations.NotNull
    com.littlegig.app.data.repository.SharingRepository sharingRepository, @org.jetbrains.annotations.NotNull
    com.littlegig.app.data.repository.ConfigRepository configRepository) {
        super();
    }
    
    @org.jetbrains.annotations.NotNull
    public final kotlinx.coroutines.flow.StateFlow<com.littlegig.app.presentation.events.EventsUiState> getUiState() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull
    public final kotlinx.coroutines.flow.StateFlow<java.lang.String> getSearchQuery() {
        return null;
    }
    
    private final void setupThrottledActions() {
    }
    
    public final void loadEvents() {
    }
    
    public final void selectCategory(@org.jetbrains.annotations.Nullable
    com.littlegig.app.data.model.ContentCategory category) {
    }
    
    public final void updateSearchQuery(@org.jetbrains.annotations.NotNull
    java.lang.String query) {
    }
    
    private final java.lang.Object fetchWeights(kotlin.coroutines.Continuation<? super com.littlegig.app.data.repository.FeedWeights> $completion) {
        return null;
    }
    
    private final double scoreEvent(com.littlegig.app.data.model.Event e, com.littlegig.app.data.repository.FeedWeights w) {
        return 0.0;
    }
    
    private final java.lang.Object personalizeAndFilterEvents(java.util.List<com.littlegig.app.data.model.Event> events, kotlin.coroutines.Continuation<? super java.util.List<com.littlegig.app.data.model.Event>> $completion) {
        return null;
    }
    
    public final void toggleEventLike(@org.jetbrains.annotations.NotNull
    java.lang.String eventId) {
    }
    
    public final void joinWaitlist(@org.jetbrains.annotations.NotNull
    java.lang.String eventId) {
    }
    
    public final void subscribePriceDrop(@org.jetbrains.annotations.NotNull
    java.lang.String eventId) {
    }
    
    public final void rsvp(@org.jetbrains.annotations.NotNull
    java.lang.String eventId) {
    }
    
    @org.jetbrains.annotations.Nullable
    public final java.lang.Object createEventShareLink(@org.jetbrains.annotations.NotNull
    com.littlegig.app.data.model.Event event, @org.jetbrains.annotations.NotNull
    kotlin.coroutines.Continuation<? super java.lang.String> $completion) {
        return null;
    }
    
    public final void rateEvent(@org.jetbrains.annotations.NotNull
    java.lang.String eventId, float rating) {
    }
    
    public final void clearError() {
    }
    
    public final void refreshEvents() {
    }
}