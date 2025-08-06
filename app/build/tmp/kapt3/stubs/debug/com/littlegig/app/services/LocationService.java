package com.littlegig.app.services;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000.\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0010\u000b\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0004\b\u0007\u0018\u0000 \u00122\u00020\u0001:\u0001\u0012B\u0007\b\u0007\u00a2\u0006\u0002\u0010\u0002J\u000e\u0010\f\u001a\u00020\r2\u0006\u0010\u000e\u001a\u00020\u000fJ\u000e\u0010\u0010\u001a\u00020\r2\u0006\u0010\u0011\u001a\u00020\u0005R\u0014\u0010\u0003\u001a\b\u0012\u0004\u0012\u00020\u00050\u0004X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0014\u0010\u0006\u001a\b\u0012\u0004\u0012\u00020\u00050\u0004X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0017\u0010\u0007\u001a\b\u0012\u0004\u0012\u00020\u00050\b\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0007\u0010\tR\u0017\u0010\n\u001a\b\u0012\u0004\u0012\u00020\u00050\b\u00a2\u0006\b\n\u0000\u001a\u0004\b\u000b\u0010\t\u00a8\u0006\u0013"}, d2 = {"Lcom/littlegig/app/services/LocationService;", "Landroidx/lifecycle/ViewModel;", "()V", "_isActiveNow", "Lkotlinx/coroutines/flow/MutableStateFlow;", "", "_locationPermissionGranted", "isActiveNow", "Lkotlinx/coroutines/flow/StateFlow;", "()Lkotlinx/coroutines/flow/StateFlow;", "locationPermissionGranted", "getLocationPermissionGranted", "requestLocationPermission", "", "activity", "Landroid/app/Activity;", "toggleActiveNow", "isActive", "Companion", "app_debug"})
@dagger.hilt.android.lifecycle.HiltViewModel
public final class LocationService extends androidx.lifecycle.ViewModel {
    @org.jetbrains.annotations.NotNull
    private final kotlinx.coroutines.flow.MutableStateFlow<java.lang.Boolean> _isActiveNow = null;
    @org.jetbrains.annotations.NotNull
    private final kotlinx.coroutines.flow.StateFlow<java.lang.Boolean> isActiveNow = null;
    @org.jetbrains.annotations.NotNull
    private final kotlinx.coroutines.flow.MutableStateFlow<java.lang.Boolean> _locationPermissionGranted = null;
    @org.jetbrains.annotations.NotNull
    private final kotlinx.coroutines.flow.StateFlow<java.lang.Boolean> locationPermissionGranted = null;
    public static final int LOCATION_PERMISSION_REQUEST_CODE = 1001;
    @org.jetbrains.annotations.NotNull
    public static final com.littlegig.app.services.LocationService.Companion Companion = null;
    
    @javax.inject.Inject
    public LocationService() {
        super();
    }
    
    @org.jetbrains.annotations.NotNull
    public final kotlinx.coroutines.flow.StateFlow<java.lang.Boolean> isActiveNow() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull
    public final kotlinx.coroutines.flow.StateFlow<java.lang.Boolean> getLocationPermissionGranted() {
        return null;
    }
    
    public final void requestLocationPermission(@org.jetbrains.annotations.NotNull
    android.app.Activity activity) {
    }
    
    public final void toggleActiveNow(boolean isActive) {
    }
    
    @kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000\u0012\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\b\n\u0000\b\u0086\u0003\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002R\u000e\u0010\u0003\u001a\u00020\u0004X\u0086T\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u0005"}, d2 = {"Lcom/littlegig/app/services/LocationService$Companion;", "", "()V", "LOCATION_PERMISSION_REQUEST_CODE", "", "app_debug"})
    public static final class Companion {
        
        private Companion() {
            super();
        }
    }
}