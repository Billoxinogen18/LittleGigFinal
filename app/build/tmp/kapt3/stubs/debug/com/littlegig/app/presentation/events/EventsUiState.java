package com.littlegig.app.presentation.events;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u00002\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000b\n\u0000\n\u0002\u0010\u000e\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010$\n\u0002\u0010\b\n\u0002\b\u0018\b\u0086\b\u0018\u00002\u00020\u0001BY\u0012\u000e\b\u0002\u0010\u0002\u001a\b\u0012\u0004\u0012\u00020\u00040\u0003\u0012\b\b\u0002\u0010\u0005\u001a\u00020\u0006\u0012\n\b\u0002\u0010\u0007\u001a\u0004\u0018\u00010\b\u0012\n\b\u0002\u0010\t\u001a\u0004\u0018\u00010\n\u0012\u0014\b\u0002\u0010\u000b\u001a\u000e\u0012\u0004\u0012\u00020\b\u0012\u0004\u0012\u00020\r0\f\u0012\n\b\u0002\u0010\u000e\u001a\u0004\u0018\u00010\b\u00a2\u0006\u0002\u0010\u000fJ\u000f\u0010\u001a\u001a\b\u0012\u0004\u0012\u00020\u00040\u0003H\u00c6\u0003J\t\u0010\u001b\u001a\u00020\u0006H\u00c6\u0003J\u000b\u0010\u001c\u001a\u0004\u0018\u00010\bH\u00c6\u0003J\u000b\u0010\u001d\u001a\u0004\u0018\u00010\nH\u00c6\u0003J\u0015\u0010\u001e\u001a\u000e\u0012\u0004\u0012\u00020\b\u0012\u0004\u0012\u00020\r0\fH\u00c6\u0003J\u000b\u0010\u001f\u001a\u0004\u0018\u00010\bH\u00c6\u0003J]\u0010 \u001a\u00020\u00002\u000e\b\u0002\u0010\u0002\u001a\b\u0012\u0004\u0012\u00020\u00040\u00032\b\b\u0002\u0010\u0005\u001a\u00020\u00062\n\b\u0002\u0010\u0007\u001a\u0004\u0018\u00010\b2\n\b\u0002\u0010\t\u001a\u0004\u0018\u00010\n2\u0014\b\u0002\u0010\u000b\u001a\u000e\u0012\u0004\u0012\u00020\b\u0012\u0004\u0012\u00020\r0\f2\n\b\u0002\u0010\u000e\u001a\u0004\u0018\u00010\bH\u00c6\u0001J\u0013\u0010!\u001a\u00020\u00062\b\u0010\"\u001a\u0004\u0018\u00010\u0001H\u00d6\u0003J\t\u0010#\u001a\u00020\rH\u00d6\u0001J\t\u0010$\u001a\u00020\bH\u00d6\u0001R\u0013\u0010\u0007\u001a\u0004\u0018\u00010\b\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0010\u0010\u0011R\u001d\u0010\u000b\u001a\u000e\u0012\u0004\u0012\u00020\b\u0012\u0004\u0012\u00020\r0\f\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0012\u0010\u0013R\u0017\u0010\u0002\u001a\b\u0012\u0004\u0012\u00020\u00040\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0014\u0010\u0015R\u0011\u0010\u0005\u001a\u00020\u0006\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0005\u0010\u0016R\u0013\u0010\t\u001a\u0004\u0018\u00010\n\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0017\u0010\u0018R\u0013\u0010\u000e\u001a\u0004\u0018\u00010\b\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0019\u0010\u0011\u00a8\u0006%"}, d2 = {"Lcom/littlegig/app/presentation/events/EventsUiState;", "", "events", "", "Lcom/littlegig/app/data/model/Event;", "isLoading", "", "error", "", "selectedCategory", "Lcom/littlegig/app/data/model/ContentCategory;", "eventIdToFriendsGoing", "", "", "snackbarMessage", "(Ljava/util/List;ZLjava/lang/String;Lcom/littlegig/app/data/model/ContentCategory;Ljava/util/Map;Ljava/lang/String;)V", "getError", "()Ljava/lang/String;", "getEventIdToFriendsGoing", "()Ljava/util/Map;", "getEvents", "()Ljava/util/List;", "()Z", "getSelectedCategory", "()Lcom/littlegig/app/data/model/ContentCategory;", "getSnackbarMessage", "component1", "component2", "component3", "component4", "component5", "component6", "copy", "equals", "other", "hashCode", "toString", "app_debug"})
public final class EventsUiState {
    @org.jetbrains.annotations.NotNull()
    private final java.util.List<com.littlegig.app.data.model.Event> events = null;
    private final boolean isLoading = false;
    @org.jetbrains.annotations.Nullable()
    private final java.lang.String error = null;
    @org.jetbrains.annotations.Nullable()
    private final com.littlegig.app.data.model.ContentCategory selectedCategory = null;
    @org.jetbrains.annotations.NotNull()
    private final java.util.Map<java.lang.String, java.lang.Integer> eventIdToFriendsGoing = null;
    @org.jetbrains.annotations.Nullable()
    private final java.lang.String snackbarMessage = null;
    
    public EventsUiState(@org.jetbrains.annotations.NotNull()
    java.util.List<com.littlegig.app.data.model.Event> events, boolean isLoading, @org.jetbrains.annotations.Nullable()
    java.lang.String error, @org.jetbrains.annotations.Nullable()
    com.littlegig.app.data.model.ContentCategory selectedCategory, @org.jetbrains.annotations.NotNull()
    java.util.Map<java.lang.String, java.lang.Integer> eventIdToFriendsGoing, @org.jetbrains.annotations.Nullable()
    java.lang.String snackbarMessage) {
        super();
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.util.List<com.littlegig.app.data.model.Event> getEvents() {
        return null;
    }
    
    public final boolean isLoading() {
        return false;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.String getError() {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final com.littlegig.app.data.model.ContentCategory getSelectedCategory() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.util.Map<java.lang.String, java.lang.Integer> getEventIdToFriendsGoing() {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.String getSnackbarMessage() {
        return null;
    }
    
    public EventsUiState() {
        super();
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.util.List<com.littlegig.app.data.model.Event> component1() {
        return null;
    }
    
    public final boolean component2() {
        return false;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.String component3() {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final com.littlegig.app.data.model.ContentCategory component4() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.util.Map<java.lang.String, java.lang.Integer> component5() {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.String component6() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final com.littlegig.app.presentation.events.EventsUiState copy(@org.jetbrains.annotations.NotNull()
    java.util.List<com.littlegig.app.data.model.Event> events, boolean isLoading, @org.jetbrains.annotations.Nullable()
    java.lang.String error, @org.jetbrains.annotations.Nullable()
    com.littlegig.app.data.model.ContentCategory selectedCategory, @org.jetbrains.annotations.NotNull()
    java.util.Map<java.lang.String, java.lang.Integer> eventIdToFriendsGoing, @org.jetbrains.annotations.Nullable()
    java.lang.String snackbarMessage) {
        return null;
    }
    
    @java.lang.Override()
    public boolean equals(@org.jetbrains.annotations.Nullable()
    java.lang.Object other) {
        return false;
    }
    
    @java.lang.Override()
    public int hashCode() {
        return 0;
    }
    
    @java.lang.Override()
    @org.jetbrains.annotations.NotNull()
    public java.lang.String toString() {
        return null;
    }
}