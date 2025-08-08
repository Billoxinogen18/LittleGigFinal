package com.littlegig.app.data.repository;

@javax.inject.Singleton
@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000x\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\t\n\u0000\n\u0002\u0010%\n\u0002\u0010\u000e\n\u0000\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0002\b\n\n\u0002\u0018\u0002\n\u0002\b\u0007\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\b\n\u0002\b\u0005\n\u0002\u0010\u000b\n\u0002\b\u0007\n\u0002\u0010\u0007\n\u0002\b\n\n\u0002\u0010$\n\u0002\b\u0003\b\u0007\u0018\u00002\u00020\u0001B\u001f\b\u0007\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0005\u0012\u0006\u0010\u0006\u001a\u00020\u0007\u00a2\u0006\u0002\u0010\bJ\u001e\u0010\u0011\u001a\u00020\u00122\u0006\u0010\u0013\u001a\u00020\r2\f\u0010\u0014\u001a\b\u0012\u0004\u0012\u00020\u00100\u000fH\u0002J\u0006\u0010\u0015\u001a\u00020\u0012J*\u0010\u0016\u001a\b\u0012\u0004\u0012\u00020\u00100\u00172\u0006\u0010\u0018\u001a\u00020\u0010H\u0086@\u00f8\u0001\u0000\u00f8\u0001\u0001\u00f8\u0001\u0002\u00f8\u0001\u0002\u00a2\u0006\u0004\b\u0019\u0010\u001aJ\u0011\u0010\u001b\u001a\u00020\u0012H\u0082@\u00f8\u0001\u0002\u00a2\u0006\u0002\u0010\u001cJ*\u0010\u001d\u001a\b\u0012\u0004\u0012\u00020\u00120\u00172\u0006\u0010\u001e\u001a\u00020\rH\u0086@\u00f8\u0001\u0000\u00f8\u0001\u0001\u00f8\u0001\u0002\u00f8\u0001\u0002\u00a2\u0006\u0004\b\u001f\u0010 J\u0012\u0010!\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u00100\u000f0\"J\u0018\u0010#\u001a\n\u0012\u0004\u0012\u00020\u0010\u0018\u00010\u000f2\u0006\u0010\u0013\u001a\u00020\rH\u0002J,\u0010$\u001a\n\u0012\u0006\u0012\u0004\u0018\u00010\u00100\u00172\u0006\u0010\u001e\u001a\u00020\rH\u0086@\u00f8\u0001\u0000\u00f8\u0001\u0001\u00f8\u0001\u0002\u00f8\u0001\u0002\u00a2\u0006\u0004\b%\u0010 J*\u0010&\u001a\b\u0012\u0004\u0012\u00020\u00100\u00172\u0006\u0010\u001e\u001a\u00020\rH\u0086@\u00f8\u0001\u0000\u00f8\u0001\u0001\u00f8\u0001\u0002\u00f8\u0001\u0002\u00a2\u0006\u0004\b\'\u0010 J\u001a\u0010(\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u00100\u000f0\"2\u0006\u0010)\u001a\u00020*J\u0012\u0010+\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u00100\u000f0\"J\'\u0010,\u001a\u00020-2\u0006\u0010\u001e\u001a\u00020\r2\f\u0010.\u001a\b\u0012\u0004\u0012\u00020\r0\u000fH\u0086@\u00f8\u0001\u0002\u00a2\u0006\u0002\u0010/J\u001f\u00100\u001a\b\u0012\u0004\u0012\u00020\u00100\u000f2\u0006\u00101\u001a\u00020\rH\u0086@\u00f8\u0001\u0002\u00a2\u0006\u0002\u0010 J2\u00102\u001a\b\u0012\u0004\u0012\u0002030\u00172\u0006\u0010\u001e\u001a\u00020\r2\u0006\u00101\u001a\u00020\rH\u0086@\u00f8\u0001\u0000\u00f8\u0001\u0001\u00f8\u0001\u0002\u00f8\u0001\u0002\u00a2\u0006\u0004\b4\u00105J\b\u00106\u001a\u000203H\u0002J2\u00107\u001a\b\u0012\u0004\u0012\u00020\u00120\u00172\u0006\u0010\u001e\u001a\u00020\r2\u0006\u00101\u001a\u00020\rH\u0086@\u00f8\u0001\u0000\u00f8\u0001\u0001\u00f8\u0001\u0002\u00f8\u0001\u0002\u00a2\u0006\u0004\b8\u00105J:\u00109\u001a\b\u0012\u0004\u0012\u00020\u00120\u00172\u0006\u0010\u001e\u001a\u00020\r2\u0006\u00101\u001a\u00020\r2\u0006\u0010:\u001a\u00020;H\u0086@\u00f8\u0001\u0000\u00f8\u0001\u0001\u00f8\u0001\u0002\u00f8\u0001\u0002\u00a2\u0006\u0004\b<\u0010=J2\u0010>\u001a\b\u0012\u0004\u0012\u00020\u00120\u00172\u0006\u0010\u001e\u001a\u00020\r2\u0006\u00101\u001a\u00020\rH\u0086@\u00f8\u0001\u0000\u00f8\u0001\u0001\u00f8\u0001\u0002\u00f8\u0001\u0002\u00a2\u0006\u0004\b?\u00105J2\u0010@\u001a\b\u0012\u0004\u0012\u00020\u00120\u00172\u0006\u0010\u001e\u001a\u00020\r2\u0006\u00101\u001a\u00020\rH\u0086@\u00f8\u0001\u0000\u00f8\u0001\u0001\u00f8\u0001\u0002\u00f8\u0001\u0002\u00a2\u0006\u0004\bA\u00105J2\u0010B\u001a\b\u0012\u0004\u0012\u00020\u00120\u00172\u0006\u0010\u001e\u001a\u00020\r2\u0006\u00101\u001a\u00020\rH\u0086@\u00f8\u0001\u0000\u00f8\u0001\u0001\u00f8\u0001\u0002\u00f8\u0001\u0002\u00a2\u0006\u0004\bC\u00105J>\u0010D\u001a\b\u0012\u0004\u0012\u00020\u00120\u00172\u0006\u0010\u001e\u001a\u00020\r2\u0012\u0010E\u001a\u000e\u0012\u0004\u0012\u00020\r\u0012\u0004\u0012\u00020\u00010FH\u0086@\u00f8\u0001\u0000\u00f8\u0001\u0001\u00f8\u0001\u0002\u00f8\u0001\u0002\u00a2\u0006\u0004\bG\u0010HR\u000e\u0010\t\u001a\u00020\nX\u0082D\u00a2\u0006\u0002\n\u0000R\u001a\u0010\u000b\u001a\u000e\u0012\u0004\u0012\u00020\r\u0012\u0004\u0012\u00020\n0\fX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0006\u001a\u00020\u0007X\u0082\u0004\u00a2\u0006\u0002\n\u0000R \u0010\u000e\u001a\u0014\u0012\u0004\u0012\u00020\r\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u00100\u000f0\fX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0002\u001a\u00020\u0003X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0004\u001a\u00020\u0005X\u0082\u0004\u00a2\u0006\u0002\n\u0000\u0082\u0002\u000f\n\u0002\b!\n\u0005\b\u00a1\u001e0\u0001\n\u0002\b\u0019\u00a8\u0006I"}, d2 = {"Lcom/littlegig/app/data/repository/EventRepository;", "", "firestore", "Lcom/google/firebase/firestore/FirebaseFirestore;", "storage", "Lcom/google/firebase/storage/FirebaseStorage;", "context", "Landroid/content/Context;", "(Lcom/google/firebase/firestore/FirebaseFirestore;Lcom/google/firebase/storage/FirebaseStorage;Landroid/content/Context;)V", "CACHE_DURATION", "", "cacheExpiry", "", "", "eventCache", "", "Lcom/littlegig/app/data/model/Event;", "cacheEvents", "", "key", "events", "clearCache", "createEvent", "Lkotlin/Result;", "event", "createEvent-gIAlu-s", "(Lcom/littlegig/app/data/model/Event;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "createTestEvents", "(Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "deleteEvent", "eventId", "deleteEvent-gIAlu-s", "(Ljava/lang/String;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "getAllEvents", "Lkotlinx/coroutines/flow/Flow;", "getCachedEvents", "getEvent", "getEvent-gIAlu-s", "getEventById", "getEventById-gIAlu-s", "getEventsByCategory", "category", "Lcom/littlegig/app/data/model/ContentCategory;", "getFeaturedEvents", "getFriendsGoingCount", "", "followingIds", "(Ljava/lang/String;Ljava/util/List;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "getUserEvents", "userId", "isEventLikedByUser", "", "isEventLikedByUser-0E7RQCE", "(Ljava/lang/String;Ljava/lang/String;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "isNetworkAvailable", "joinWaitlist", "joinWaitlist-0E7RQCE", "rateEvent", "rating", "", "rateEvent-BWLJW6A", "(Ljava/lang/String;Ljava/lang/String;FLkotlin/coroutines/Continuation;)Ljava/lang/Object;", "rsvpEvent", "rsvpEvent-0E7RQCE", "subscribePriceDrop", "subscribePriceDrop-0E7RQCE", "toggleEventLike", "toggleEventLike-0E7RQCE", "updateEvent", "updates", "", "updateEvent-0E7RQCE", "(Ljava/lang/String;Ljava/util/Map;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "app_debug"})
public final class EventRepository {
    @org.jetbrains.annotations.NotNull
    private final com.google.firebase.firestore.FirebaseFirestore firestore = null;
    @org.jetbrains.annotations.NotNull
    private final com.google.firebase.storage.FirebaseStorage storage = null;
    @org.jetbrains.annotations.NotNull
    private final android.content.Context context = null;
    @org.jetbrains.annotations.NotNull
    private final java.util.Map<java.lang.String, java.util.List<com.littlegig.app.data.model.Event>> eventCache = null;
    @org.jetbrains.annotations.NotNull
    private final java.util.Map<java.lang.String, java.lang.Long> cacheExpiry = null;
    private final long CACHE_DURATION = 300000L;
    
    @javax.inject.Inject
    public EventRepository(@org.jetbrains.annotations.NotNull
    com.google.firebase.firestore.FirebaseFirestore firestore, @org.jetbrains.annotations.NotNull
    com.google.firebase.storage.FirebaseStorage storage, @org.jetbrains.annotations.NotNull
    android.content.Context context) {
        super();
    }
    
    private final boolean isNetworkAvailable() {
        return false;
    }
    
    private final java.util.List<com.littlegig.app.data.model.Event> getCachedEvents(java.lang.String key) {
        return null;
    }
    
    private final void cacheEvents(java.lang.String key, java.util.List<com.littlegig.app.data.model.Event> events) {
    }
    
    @org.jetbrains.annotations.NotNull
    public final kotlinx.coroutines.flow.Flow<java.util.List<com.littlegig.app.data.model.Event>> getAllEvents() {
        return null;
    }
    
    private final java.lang.Object createTestEvents(kotlin.coroutines.Continuation<? super kotlin.Unit> $completion) {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull
    public final kotlinx.coroutines.flow.Flow<java.util.List<com.littlegig.app.data.model.Event>> getEventsByCategory(@org.jetbrains.annotations.NotNull
    com.littlegig.app.data.model.ContentCategory category) {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull
    public final kotlinx.coroutines.flow.Flow<java.util.List<com.littlegig.app.data.model.Event>> getFeaturedEvents() {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable
    public final java.lang.Object getUserEvents(@org.jetbrains.annotations.NotNull
    java.lang.String userId, @org.jetbrains.annotations.NotNull
    kotlin.coroutines.Continuation<? super java.util.List<com.littlegig.app.data.model.Event>> $completion) {
        return null;
    }
    
    /**
     * Count how many of the given followingIds have active tickets for the event.
     * Firestore limitation: whereIn supports up to 10 values. We chunk and sum.
     */
    @org.jetbrains.annotations.Nullable
    public final java.lang.Object getFriendsGoingCount(@org.jetbrains.annotations.NotNull
    java.lang.String eventId, @org.jetbrains.annotations.NotNull
    java.util.List<java.lang.String> followingIds, @org.jetbrains.annotations.NotNull
    kotlin.coroutines.Continuation<? super java.lang.Integer> $completion) {
        return null;
    }
    
    public final void clearCache() {
    }
}