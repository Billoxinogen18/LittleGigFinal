package com.littlegig.app.data.repository;

@javax.inject.Singleton
@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000h\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\t\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010%\n\u0002\u0010\u000e\n\u0000\n\u0002\u0010\u0002\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\b\t\n\u0002\u0010\u000b\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0016\n\u0002\u0010$\n\u0002\b\u0003\b\u0007\u0018\u00002\u00020\u0001B\u001f\b\u0007\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0005\u0012\u0006\u0010\u0006\u001a\u00020\u0007\u00a2\u0006\u0002\u0010\bJ\u0010\u0010\u0015\u001a\u00020\u00162\u0006\u0010\u0017\u001a\u00020\rH\u0002J\u0006\u0010\u0018\u001a\u00020\u0016J\u0010\u0010\u0019\u001a\u00020\r2\u0006\u0010\u001a\u001a\u00020\u0014H\u0002J\"\u0010\u001b\u001a\b\u0012\u0004\u0012\u00020\u00160\u001cH\u0086@\u00f8\u0001\u0000\u00f8\u0001\u0001\u00f8\u0001\u0002\u00f8\u0001\u0002\u00a2\u0006\u0004\b\u001d\u0010\u001eJ\u0010\u0010\u001f\u001a\u00020\u00142\u0006\u0010 \u001a\u00020\u0014H\u0002J\u0018\u0010\u001f\u001a\u00020\u00142\u0006\u0010!\u001a\u00020\u00142\u0006\u0010\"\u001a\u00020\u0014H\u0002J\u0012\u0010#\u001a\u0004\u0018\u00010\r2\u0006\u0010$\u001a\u00020\u0014H\u0002J\b\u0010%\u001a\u00020&H\u0002JP\u0010\'\u001a\b\u0012\u0004\u0012\u00020\r0\u001c2\u0006\u0010 \u001a\u00020\u00142\u0006\u0010(\u001a\u00020\u00142\u0006\u0010!\u001a\u00020\u00142\n\b\u0002\u0010\"\u001a\u0004\u0018\u00010\u00142\b\b\u0002\u0010)\u001a\u00020*H\u0086@\u00f8\u0001\u0000\u00f8\u0001\u0001\u00f8\u0001\u0002\u00f8\u0001\u0002\u00a2\u0006\u0004\b+\u0010,J<\u0010-\u001a\b\u0012\u0004\u0012\u00020\r0\u001c2\u0006\u0010\"\u001a\u00020\u00142\u0006\u0010!\u001a\u00020\u00142\b\b\u0002\u0010)\u001a\u00020*H\u0086@\u00f8\u0001\u0000\u00f8\u0001\u0001\u00f8\u0001\u0002\u00f8\u0001\u0002\u00a2\u0006\u0004\b.\u0010/J2\u00100\u001a\b\u0012\u0004\u0012\u00020\r0\u001c2\u0006\u0010 \u001a\u00020\u00142\u0006\u0010(\u001a\u00020\u0014H\u0086@\u00f8\u0001\u0000\u00f8\u0001\u0001\u00f8\u0001\u0002\u00f8\u0001\u0002\u00a2\u0006\u0004\b1\u00102J\"\u00103\u001a\b\u0012\u0004\u0012\u00020\r0\u001cH\u0086@\u00f8\u0001\u0000\u00f8\u0001\u0001\u00f8\u0001\u0002\u00f8\u0001\u0002\u00a2\u0006\u0004\b4\u0010\u001eJ*\u00105\u001a\b\u0012\u0004\u0012\u00020\r0\u001c2\u0006\u0010\"\u001a\u00020\u0014H\u0086@\u00f8\u0001\u0000\u00f8\u0001\u0001\u00f8\u0001\u0002\u00f8\u0001\u0002\u00a2\u0006\u0004\b6\u00107J\"\u00108\u001a\b\u0012\u0004\u0012\u00020\u00160\u001cH\u0086@\u00f8\u0001\u0000\u00f8\u0001\u0001\u00f8\u0001\u0002\u00f8\u0001\u0002\u00a2\u0006\u0004\b9\u0010\u001eJF\u0010:\u001a\b\u0012\u0004\u0012\u00020\r0\u001c2\u0006\u0010 \u001a\u00020\u00142\u0006\u0010(\u001a\u00020\u00142\u0006\u0010)\u001a\u00020*2\n\b\u0002\u0010\"\u001a\u0004\u0018\u00010\u0014H\u0086@\u00f8\u0001\u0000\u00f8\u0001\u0001\u00f8\u0001\u0002\u00f8\u0001\u0002\u00a2\u0006\u0004\b;\u0010<J:\u0010=\u001a\b\u0012\u0004\u0012\u00020\r0\u001c2\u0006\u0010\"\u001a\u00020\u00142\u0006\u0010!\u001a\u00020\u00142\u0006\u0010)\u001a\u00020*H\u0086@\u00f8\u0001\u0000\u00f8\u0001\u0001\u00f8\u0001\u0002\u00f8\u0001\u0002\u00a2\u0006\u0004\b>\u0010/J6\u0010?\u001a\b\u0012\u0004\u0012\u00020\u00160\u001c2\u0012\u0010@\u001a\u000e\u0012\u0004\u0012\u00020\u0014\u0012\u0004\u0012\u00020\u00010AH\u0086@\u00f8\u0001\u0000\u00f8\u0001\u0001\u00f8\u0001\u0002\u00f8\u0001\u0002\u00a2\u0006\u0004\bB\u0010CR\u000e\u0010\t\u001a\u00020\nX\u0082D\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0002\u001a\u00020\u0003X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0006\u001a\u00020\u0007X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0019\u0010\u000b\u001a\n\u0012\u0006\u0012\u0004\u0018\u00010\r0\f\u00a2\u0006\b\n\u0000\u001a\u0004\b\u000e\u0010\u000fR\u000e\u0010\u0004\u001a\u00020\u0005X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0010\u001a\u00020\u0011X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u001a\u0010\u0012\u001a\u000e\u0012\u0004\u0012\u00020\u0014\u0012\u0004\u0012\u00020\r0\u0013X\u0082\u0004\u00a2\u0006\u0002\n\u0000\u0082\u0002\u000f\n\u0002\b!\n\u0005\b\u00a1\u001e0\u0001\n\u0002\b\u0019\u00a8\u0006D"}, d2 = {"Lcom/littlegig/app/data/repository/AuthRepository;", "", "auth", "Lcom/google/firebase/auth/FirebaseAuth;", "firestore", "Lcom/google/firebase/firestore/FirebaseFirestore;", "context", "Landroid/content/Context;", "(Lcom/google/firebase/auth/FirebaseAuth;Lcom/google/firebase/firestore/FirebaseFirestore;Landroid/content/Context;)V", "CACHE_DURATION", "", "currentUser", "Lkotlinx/coroutines/flow/StateFlow;", "Lcom/littlegig/app/data/model/User;", "getCurrentUser", "()Lkotlinx/coroutines/flow/StateFlow;", "prefs", "Landroid/content/SharedPreferences;", "userCache", "", "", "cacheUser", "", "user", "clearCache", "createAnonymousUser", "uid", "deleteAccount", "Lkotlin/Result;", "deleteAccount-IoAF18A", "(Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "generateUsername", "email", "displayName", "phoneNumber", "getCachedUser", "userId", "isNetworkAvailable", "", "linkAnonymousAccount", "password", "userType", "Lcom/littlegig/app/data/model/UserType;", "linkAnonymousAccount-hUnOzRk", "(Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Lcom/littlegig/app/data/model/UserType;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "linkAnonymousAccountWithPhone", "linkAnonymousAccountWithPhone-BWLJW6A", "(Ljava/lang/String;Ljava/lang/String;Lcom/littlegig/app/data/model/UserType;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "signIn", "signIn-0E7RQCE", "(Ljava/lang/String;Ljava/lang/String;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "signInAnonymously", "signInAnonymously-IoAF18A", "signInWithPhone", "signInWithPhone-gIAlu-s", "(Ljava/lang/String;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "signOut", "signOut-IoAF18A", "signUp", "signUp-yxL6bBk", "(Ljava/lang/String;Ljava/lang/String;Lcom/littlegig/app/data/model/UserType;Ljava/lang/String;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "signUpWithPhone", "signUpWithPhone-BWLJW6A", "updateProfile", "updates", "", "updateProfile-gIAlu-s", "(Ljava/util/Map;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "app_debug"})
public final class AuthRepository {
    @org.jetbrains.annotations.NotNull
    private final com.google.firebase.auth.FirebaseAuth auth = null;
    @org.jetbrains.annotations.NotNull
    private final com.google.firebase.firestore.FirebaseFirestore firestore = null;
    @org.jetbrains.annotations.NotNull
    private final android.content.Context context = null;
    @org.jetbrains.annotations.NotNull
    private final android.content.SharedPreferences prefs = null;
    @org.jetbrains.annotations.NotNull
    private final java.util.Map<java.lang.String, com.littlegig.app.data.model.User> userCache = null;
    private final long CACHE_DURATION = 600000L;
    @org.jetbrains.annotations.NotNull
    private final kotlinx.coroutines.flow.StateFlow<com.littlegig.app.data.model.User> currentUser = null;
    
    @javax.inject.Inject
    public AuthRepository(@org.jetbrains.annotations.NotNull
    com.google.firebase.auth.FirebaseAuth auth, @org.jetbrains.annotations.NotNull
    com.google.firebase.firestore.FirebaseFirestore firestore, @org.jetbrains.annotations.NotNull
    android.content.Context context) {
        super();
    }
    
    private final boolean isNetworkAvailable() {
        return false;
    }
    
    private final com.littlegig.app.data.model.User getCachedUser(java.lang.String userId) {
        return null;
    }
    
    private final void cacheUser(com.littlegig.app.data.model.User user) {
    }
    
    @org.jetbrains.annotations.NotNull
    public final kotlinx.coroutines.flow.StateFlow<com.littlegig.app.data.model.User> getCurrentUser() {
        return null;
    }
    
    private final com.littlegig.app.data.model.User createAnonymousUser(java.lang.String uid) {
        return null;
    }
    
    public final void clearCache() {
    }
    
    private final java.lang.String generateUsername(java.lang.String email) {
        return null;
    }
    
    private final java.lang.String generateUsername(java.lang.String displayName, java.lang.String phoneNumber) {
        return null;
    }
}