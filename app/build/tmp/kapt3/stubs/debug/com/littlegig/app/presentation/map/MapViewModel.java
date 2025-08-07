package com.littlegig.app.presentation.map;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000R\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\u0006\n\u0002\b\u0005\n\u0002\u0010\u0002\n\u0000\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\b\n\u0002\b\u0007\b\u0007\u0018\u00002\u00020\u0001B\u001f\b\u0007\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0005\u0012\u0006\u0010\u0006\u001a\u00020\u0007\u00a2\u0006\u0002\u0010\bJ(\u0010\u0010\u001a\u00020\u00112\u0006\u0010\u0012\u001a\u00020\u00112\u0006\u0010\u0013\u001a\u00020\u00112\u0006\u0010\u0014\u001a\u00020\u00112\u0006\u0010\u0015\u001a\u00020\u0011H\u0002J\u0006\u0010\u0016\u001a\u00020\u0017J1\u0010\u0018\u001a\b\u0012\u0004\u0012\u00020\u001a0\u00192\u0006\u0010\u001b\u001a\u00020\u00112\u0006\u0010\u001c\u001a\u00020\u00112\b\b\u0002\u0010\u001d\u001a\u00020\u001eH\u0086@\u00f8\u0001\u0000\u00a2\u0006\u0002\u0010\u001fJ\u0006\u0010 \u001a\u00020\u0017J\b\u0010!\u001a\u00020\u0017H\u0002J\u0006\u0010\"\u001a\u00020\u0017J\u0018\u0010#\u001a\u00020\u00172\u0006\u0010$\u001a\u00020\u001a2\b\b\u0002\u0010\u001d\u001a\u00020\u0011R\u0014\u0010\t\u001a\b\u0012\u0004\u0012\u00020\u000b0\nX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0004\u001a\u00020\u0005X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0002\u001a\u00020\u0003X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0006\u001a\u00020\u0007X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0017\u0010\f\u001a\b\u0012\u0004\u0012\u00020\u000b0\r\u00a2\u0006\b\n\u0000\u001a\u0004\b\u000e\u0010\u000f\u0082\u0002\u0004\n\u0002\b\u0019\u00a8\u0006%"}, d2 = {"Lcom/littlegig/app/presentation/map/MapViewModel;", "Landroidx/lifecycle/ViewModel;", "eventRepository", "Lcom/littlegig/app/data/repository/EventRepository;", "authRepository", "Lcom/littlegig/app/data/repository/AuthRepository;", "functions", "Lcom/google/firebase/functions/FirebaseFunctions;", "(Lcom/littlegig/app/data/repository/EventRepository;Lcom/littlegig/app/data/repository/AuthRepository;Lcom/google/firebase/functions/FirebaseFunctions;)V", "_uiState", "Lkotlinx/coroutines/flow/MutableStateFlow;", "Lcom/littlegig/app/presentation/map/MapUiState;", "uiState", "Lkotlinx/coroutines/flow/StateFlow;", "getUiState", "()Lkotlinx/coroutines/flow/StateFlow;", "calculateDistance", "", "lat1", "lon1", "lat2", "lon2", "clearError", "", "getActiveUsersNearby", "", "Lcom/google/android/gms/maps/model/LatLng;", "lat", "lon", "radiusKm", "", "(DDILkotlin/coroutines/Continuation;)Ljava/lang/Object;", "getCurrentLocation", "loadEventsWithLocation", "refreshEvents", "searchEventsNearLocation", "location", "app_debug"})
@dagger.hilt.android.lifecycle.HiltViewModel
public final class MapViewModel extends androidx.lifecycle.ViewModel {
    @org.jetbrains.annotations.NotNull
    private final com.littlegig.app.data.repository.EventRepository eventRepository = null;
    @org.jetbrains.annotations.NotNull
    private final com.littlegig.app.data.repository.AuthRepository authRepository = null;
    @org.jetbrains.annotations.NotNull
    private final com.google.firebase.functions.FirebaseFunctions functions = null;
    @org.jetbrains.annotations.NotNull
    private final kotlinx.coroutines.flow.MutableStateFlow<com.littlegig.app.presentation.map.MapUiState> _uiState = null;
    @org.jetbrains.annotations.NotNull
    private final kotlinx.coroutines.flow.StateFlow<com.littlegig.app.presentation.map.MapUiState> uiState = null;
    
    @javax.inject.Inject
    public MapViewModel(@org.jetbrains.annotations.NotNull
    com.littlegig.app.data.repository.EventRepository eventRepository, @org.jetbrains.annotations.NotNull
    com.littlegig.app.data.repository.AuthRepository authRepository, @org.jetbrains.annotations.NotNull
    com.google.firebase.functions.FirebaseFunctions functions) {
        super();
    }
    
    @org.jetbrains.annotations.NotNull
    public final kotlinx.coroutines.flow.StateFlow<com.littlegig.app.presentation.map.MapUiState> getUiState() {
        return null;
    }
    
    private final void loadEventsWithLocation() {
    }
    
    public final void getCurrentLocation() {
    }
    
    @org.jetbrains.annotations.Nullable
    public final java.lang.Object getActiveUsersNearby(double lat, double lon, int radiusKm, @org.jetbrains.annotations.NotNull
    kotlin.coroutines.Continuation<? super java.util.List<com.google.android.gms.maps.model.LatLng>> $completion) {
        return null;
    }
    
    public final void searchEventsNearLocation(@org.jetbrains.annotations.NotNull
    com.google.android.gms.maps.model.LatLng location, double radiusKm) {
    }
    
    private final double calculateDistance(double lat1, double lon1, double lat2, double lon2) {
        return 0.0;
    }
    
    public final void refreshEvents() {
    }
    
    public final void clearError() {
    }
}