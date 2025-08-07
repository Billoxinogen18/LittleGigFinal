package com.littlegig.app

import android.app.Application
import com.google.firebase.crashlytics.FirebaseCrashlytics
import dagger.hilt.android.HiltAndroidApp
import javax.inject.Inject
import com.littlegig.app.utils.NetworkMonitor

@HiltAndroidApp
class LittleGigApplication : Application() {
    
    @Inject
    lateinit var networkMonitor: NetworkMonitor
    
    override fun onCreate() {
        super.onCreate()
        
        // Initialize Crashlytics
        FirebaseCrashlytics.getInstance().setCrashlyticsCollectionEnabled(true)
        
        // Start network monitoring
        networkMonitor.startMonitoring()
    }
    
    override fun onTerminate() {
        super.onTerminate()
        networkMonitor.stopMonitoring()
    }
}