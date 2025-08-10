package com.littlegig.app;

@dagger.hilt.android.HiltAndroidApp()
@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000\u001c\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0010\u0002\n\u0002\b\u0002\b\u0007\u0018\u00002\u00020\u0001B\u0005\u00a2\u0006\u0002\u0010\u0002J\b\u0010\t\u001a\u00020\nH\u0016J\b\u0010\u000b\u001a\u00020\nH\u0016R\u001e\u0010\u0003\u001a\u00020\u00048\u0006@\u0006X\u0087.\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\u0005\u0010\u0006\"\u0004\b\u0007\u0010\b\u00a8\u0006\f"}, d2 = {"Lcom/littlegig/app/LittleGigApplication;", "Landroid/app/Application;", "()V", "networkMonitor", "Lcom/littlegig/app/utils/NetworkMonitor;", "getNetworkMonitor", "()Lcom/littlegig/app/utils/NetworkMonitor;", "setNetworkMonitor", "(Lcom/littlegig/app/utils/NetworkMonitor;)V", "onCreate", "", "onTerminate", "app_debug"})
public final class LittleGigApplication extends android.app.Application {
    @javax.inject.Inject()
    public com.littlegig.app.utils.NetworkMonitor networkMonitor;
    
    public LittleGigApplication() {
        super();
    }
    
    @org.jetbrains.annotations.NotNull()
    public final com.littlegig.app.utils.NetworkMonitor getNetworkMonitor() {
        return null;
    }
    
    public final void setNetworkMonitor(@org.jetbrains.annotations.NotNull()
    com.littlegig.app.utils.NetworkMonitor p0) {
    }
    
    @java.lang.Override()
    public void onCreate() {
    }
    
    @java.lang.Override()
    public void onTerminate() {
    }
}