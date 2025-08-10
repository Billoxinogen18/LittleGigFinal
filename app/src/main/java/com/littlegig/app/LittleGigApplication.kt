package com.littlegig.app

import android.app.Application
import android.content.pm.ApplicationInfo
import com.google.firebase.crashlytics.FirebaseCrashlytics
import dagger.hilt.android.HiltAndroidApp
import javax.inject.Inject
import com.littlegig.app.utils.NetworkMonitor
import timber.log.Timber

@HiltAndroidApp
class LittleGigApplication : Application() {
    
    @Inject
    lateinit var networkMonitor: NetworkMonitor
    
    override fun onCreate() {
        super.onCreate()
        
        val isDebug = (applicationInfo.flags and ApplicationInfo.FLAG_DEBUGGABLE) != 0
        // Initialize Timber
        if (isDebug) {
            Timber.plant(Timber.DebugTree())
        } else {
            Timber.plant(CrashlyticsTree())
        }
        
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

class CrashlyticsTree : Timber.Tree() {
    override fun log(priority: Int, tag: String?, message: String, t: Throwable?) {
        val crashlytics = FirebaseCrashlytics.getInstance()
        crashlytics.log("[" + (tag ?: "LittleGig") + "] " + message)
        if (t != null) crashlytics.recordException(t)
    }
}