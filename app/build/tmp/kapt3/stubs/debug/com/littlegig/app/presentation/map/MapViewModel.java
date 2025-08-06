package com.littlegig.app.presentation.map;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000<\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\u0006\n\u0002\b\u0005\n\u0002\u0010\u0002\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\b\u0002\b\u0007\u0018\u00002\u00020\u0001B\u000f\b\u0007\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\u0002\u0010\u0004J(\u0010\f\u001a\u00020\r2\u0006\u0010\u000e\u001a\u00020\r2\u0006\u0010\u000f\u001a\u00020\r2\u0006\u0010\u0010\u001a\u00020\r2\u0006\u0010\u0011\u001a\u00020\rH\u0002J\u0006\u0010\u0012\u001a\u00020\u0013J\u0006\u0010\u0014\u001a\u00020\u0013J\b\u0010\u0015\u001a\u00020\u0013H\u0002J\u0006\u0010\u0016\u001a\u00020\u0013J\u0018\u0010\u0017\u001a\u00020\u00132\u0006\u0010\u0018\u001a\u00020\u00192\b\b\u0002\u0010\u001a\u001a\u00020\rR\u0014\u0010\u0005\u001a\b\u0012\u0004\u0012\u00020\u00070\u0006X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0002\u001a\u00020\u0003X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0017\u0010\b\u001a\b\u0012\u0004\u0012\u00020\u00070\t\u00a2\u0006\b\n\u0000\u001a\u0004\b\n\u0010\u000b\u00a8\u0006\u001b"}, d2 = {"Lcom/littlegig/app/presentation/map/MapViewModel;", "Landroidx/lifecycle/ViewModel;", "eventRepository", "Lcom/littlegig/app/data/repository/EventRepository;", "(Lcom/littlegig/app/data/repository/EventRepository;)V", "_uiState", "Lkotlinx/coroutines/flow/MutableStateFlow;", "Lcom/littlegig/app/presentation/map/MapUiState;", "uiState", "Lkotlinx/coroutines/flow/StateFlow;", "getUiState", "()Lkotlinx/coroutines/flow/StateFlow;", "calculateDistance", "", "lat1", "lon1", "lat2", "lon2", "clearError", "", "getCurrentLocation", "loadEventsWithLocation", "refreshEvents", "searchEventsNearLocation", "location", "Lcom/google/android/gms/maps/model/LatLng;", "radiusKm", "app_debug"})
@dagger.hilt.android.lifecycle.HiltViewModel
public final class MapViewModel extends androidx.lifecycle.ViewModel {
    @org.jetbrains.annotations.NotNull
    private final com.littlegig.app.data.repository.EventRepository eventRepository = null;
    @org.jetbrains.annotations.NotNull
    private final kotlinx.coroutines.flow.MutableStateFlow<com.littlegig.app.presentation.map.MapUiState> _uiState = null;
    @org.jetbrains.annotations.NotNull
    private final kotlinx.coroutines.flow.StateFlow<com.littlegig.app.presentation.map.MapUiState> uiState = null;
    
    @javax.inject.Inject
    public MapViewModel(@org.jetbrains.annotations.NotNull
    com.littlegig.app.data.repository.EventRepository eventRepository) {
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