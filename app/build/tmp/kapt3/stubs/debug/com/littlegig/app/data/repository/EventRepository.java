package com.littlegig.app.data.repository;

@javax.inject.Singleton
@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000F\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0010\u000e\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\u0002\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0002\u0010 \n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0002\b\t\b\u0007\u0018\u00002\u00020\u0001B\u0017\b\u0007\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0005\u00a2\u0006\u0002\u0010\u0006J*\u0010\u0007\u001a\b\u0012\u0004\u0012\u00020\t0\b2\u0006\u0010\n\u001a\u00020\u000bH\u0086@\u00f8\u0001\u0000\u00f8\u0001\u0001\u00f8\u0001\u0002\u00f8\u0001\u0002\u00a2\u0006\u0004\b\f\u0010\rJ*\u0010\u000e\u001a\b\u0012\u0004\u0012\u00020\u000f0\b2\u0006\u0010\u0010\u001a\u00020\tH\u0086@\u00f8\u0001\u0000\u00f8\u0001\u0001\u00f8\u0001\u0002\u00f8\u0001\u0002\u00a2\u0006\u0004\b\u0011\u0010\u0012J\u0012\u0010\u0013\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u000b0\u00150\u0014J*\u0010\u0016\u001a\b\u0012\u0004\u0012\u00020\u000b0\b2\u0006\u0010\u0010\u001a\u00020\tH\u0086@\u00f8\u0001\u0000\u00f8\u0001\u0001\u00f8\u0001\u0002\u00f8\u0001\u0002\u00a2\u0006\u0004\b\u0017\u0010\u0012J\u001a\u0010\u0018\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u000b0\u00150\u00142\u0006\u0010\u0019\u001a\u00020\u001aJ\u001a\u0010\u001b\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u000b0\u00150\u00142\u0006\u0010\u001c\u001a\u00020\tJ\u0012\u0010\u001d\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u000b0\u00150\u0014J\u001a\u0010\u001e\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u000b0\u00150\u00142\u0006\u0010\u001f\u001a\u00020\tJ2\u0010 \u001a\b\u0012\u0004\u0012\u00020\u000f0\b2\u0006\u0010\u0010\u001a\u00020\t2\u0006\u0010\n\u001a\u00020\u000bH\u0086@\u00f8\u0001\u0000\u00f8\u0001\u0001\u00f8\u0001\u0002\u00f8\u0001\u0002\u00a2\u0006\u0004\b!\u0010\"R\u000e\u0010\u0002\u001a\u00020\u0003X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0004\u001a\u00020\u0005X\u0082\u0004\u00a2\u0006\u0002\n\u0000\u0082\u0002\u000f\n\u0002\b!\n\u0005\b\u00a1\u001e0\u0001\n\u0002\b\u0019\u00a8\u0006#"}, d2 = {"Lcom/littlegig/app/data/repository/EventRepository;", "", "firestore", "Lcom/google/firebase/firestore/FirebaseFirestore;", "storage", "Lcom/google/firebase/storage/FirebaseStorage;", "(Lcom/google/firebase/firestore/FirebaseFirestore;Lcom/google/firebase/storage/FirebaseStorage;)V", "createEvent", "Lkotlin/Result;", "", "event", "Lcom/littlegig/app/data/model/Event;", "createEvent-gIAlu-s", "(Lcom/littlegig/app/data/model/Event;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "deleteEvent", "", "eventId", "deleteEvent-gIAlu-s", "(Ljava/lang/String;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "getAllEvents", "Lkotlinx/coroutines/flow/Flow;", "", "getEventById", "getEventById-gIAlu-s", "getEventsByCategory", "category", "Lcom/littlegig/app/data/model/ContentCategory;", "getEventsByLocation", "city", "getFeaturedEvents", "searchEvents", "query", "updateEvent", "updateEvent-0E7RQCE", "(Ljava/lang/String;Lcom/littlegig/app/data/model/Event;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "app_debug"})
public final class EventRepository {
    @org.jetbrains.annotations.NotNull
    private final com.google.firebase.firestore.FirebaseFirestore firestore = null;
    @org.jetbrains.annotations.NotNull
    private final com.google.firebase.storage.FirebaseStorage storage = null;
    
    @javax.inject.Inject
    public EventRepository(@org.jetbrains.annotations.NotNull
    com.google.firebase.firestore.FirebaseFirestore firestore, @org.jetbrains.annotations.NotNull
    com.google.firebase.storage.FirebaseStorage storage) {
        super();
    }
    
    @org.jetbrains.annotations.NotNull
    public final kotlinx.coroutines.flow.Flow<java.util.List<com.littlegig.app.data.model.Event>> getAllEvents() {
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
    
    @org.jetbrains.annotations.NotNull
    public final kotlinx.coroutines.flow.Flow<java.util.List<com.littlegig.app.data.model.Event>> searchEvents(@org.jetbrains.annotations.NotNull
    java.lang.String query) {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull
    public final kotlinx.coroutines.flow.Flow<java.util.List<com.littlegig.app.data.model.Event>> getEventsByLocation(@org.jetbrains.annotations.NotNull
    java.lang.String city) {
        return null;
    }
}