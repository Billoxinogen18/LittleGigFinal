package com.littlegig.app.data.repository;

@javax.inject.Singleton()
@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000T\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0010\u000e\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\u0010 \n\u0000\n\u0002\u0018\u0002\n\u0002\b\b\n\u0002\u0010\u0006\n\u0002\b\u0002\n\u0002\u0010\u0002\n\u0000\n\u0002\u0010\b\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\b\u0006\b\u0007\u0018\u00002\u00020\u0001B\u000f\b\u0007\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\u0002\u0010\u0004J$\u0010\u0005\u001a\b\u0012\u0004\u0012\u00020\u00070\u00062\u0006\u0010\b\u001a\u00020\tH\u0086@\u00f8\u0001\u0000\u00f8\u0001\u0001\u00a2\u0006\u0004\b\n\u0010\u000bJ\u0012\u0010\f\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\t0\u000e0\rJ$\u0010\u000f\u001a\b\u0012\u0004\u0012\u00020\u00100\u00062\u0006\u0010\u0011\u001a\u00020\u0007H\u0086@\u00f8\u0001\u0000\u00f8\u0001\u0001\u00a2\u0006\u0004\b\u0012\u0010\u0013J$\u0010\u0014\u001a\b\u0012\u0004\u0012\u00020\t0\u00062\u0006\u0010\u0015\u001a\u00020\u0007H\u0086@\u00f8\u0001\u0000\u00f8\u0001\u0001\u00a2\u0006\u0004\b\u0016\u0010\u0013J\u001a\u0010\u0017\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\t0\u000e0\r2\u0006\u0010\u0011\u001a\u00020\u0007J$\u0010\u0018\u001a\b\u0012\u0004\u0012\u00020\u00190\u00062\u0006\u0010\u0011\u001a\u00020\u0007H\u0086@\u00f8\u0001\u0000\u00f8\u0001\u0001\u00a2\u0006\u0004\b\u001a\u0010\u0013J8\u0010\u001b\u001a\b\u0012\u0004\u0012\u00020\u001c0\u00062\u0006\u0010\u0015\u001a\u00020\u00072\b\b\u0002\u0010\u001d\u001a\u00020\u001e2\b\b\u0002\u0010\u001f\u001a\u00020\u001eH\u0086@\u00f8\u0001\u0000\u00f8\u0001\u0001\u00a2\u0006\u0004\b \u0010!J,\u0010\"\u001a\b\u0012\u0004\u0012\u00020\u001c0\u00062\u0006\u0010\u0015\u001a\u00020\u00072\u0006\u0010#\u001a\u00020$H\u0086@\u00f8\u0001\u0000\u00f8\u0001\u0001\u00a2\u0006\u0004\b%\u0010&J,\u0010\'\u001a\b\u0012\u0004\u0012\u00020\u001c0\u00062\u0006\u0010\u0015\u001a\u00020\u00072\u0006\u0010\b\u001a\u00020\tH\u0086@\u00f8\u0001\u0000\u00f8\u0001\u0001\u00a2\u0006\u0004\b(\u0010)R\u000e\u0010\u0002\u001a\u00020\u0003X\u0082\u0004\u00a2\u0006\u0002\n\u0000\u0082\u0002\u000b\n\u0002\b!\n\u0005\b\u00a1\u001e0\u0001\u00a8\u0006*"}, d2 = {"Lcom/littlegig/app/data/repository/AdvertisementRepository;", "", "firestore", "Lcom/google/firebase/firestore/FirebaseFirestore;", "(Lcom/google/firebase/firestore/FirebaseFirestore;)V", "createAdvertisement", "Lkotlin/Result;", "", "advertisement", "Lcom/littlegig/app/data/model/Advertisement;", "createAdvertisement-gIAlu-s", "(Lcom/littlegig/app/data/model/Advertisement;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "getActiveAds", "Lkotlinx/coroutines/flow/Flow;", "", "getAdAnalytics", "Lcom/littlegig/app/data/repository/AdAnalytics;", "influencerId", "getAdAnalytics-gIAlu-s", "(Ljava/lang/String;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "getAdvertisementById", "adId", "getAdvertisementById-gIAlu-s", "getInfluencerAds", "getTotalAdSpend", "", "getTotalAdSpend-gIAlu-s", "incrementAdMetrics", "", "impressions", "", "clicks", "incrementAdMetrics-BWLJW6A", "(Ljava/lang/String;IILkotlin/coroutines/Continuation;)Ljava/lang/Object;", "updateAdStatus", "status", "Lcom/littlegig/app/data/model/AdStatus;", "updateAdStatus-0E7RQCE", "(Ljava/lang/String;Lcom/littlegig/app/data/model/AdStatus;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "updateAdvertisement", "updateAdvertisement-0E7RQCE", "(Ljava/lang/String;Lcom/littlegig/app/data/model/Advertisement;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "app_debug"})
public final class AdvertisementRepository {
    @org.jetbrains.annotations.NotNull()
    private final com.google.firebase.firestore.FirebaseFirestore firestore = null;
    
    @javax.inject.Inject()
    public AdvertisementRepository(@org.jetbrains.annotations.NotNull()
    com.google.firebase.firestore.FirebaseFirestore firestore) {
        super();
    }
    
    @org.jetbrains.annotations.NotNull()
    public final kotlinx.coroutines.flow.Flow<java.util.List<com.littlegig.app.data.model.Advertisement>> getInfluencerAds(@org.jetbrains.annotations.NotNull()
    java.lang.String influencerId) {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final kotlinx.coroutines.flow.Flow<java.util.List<com.littlegig.app.data.model.Advertisement>> getActiveAds() {
        return null;
    }
}