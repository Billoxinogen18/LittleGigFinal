package com.littlegig.app

import android.app.Application
import android.content.pm.ApplicationInfo
import androidx.lifecycle.ProcessLifecycleOwner
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import com.google.firebase.crashlytics.FirebaseCrashlytics
import dagger.hilt.android.HiltAndroidApp
import javax.inject.Inject
import com.littlegig.app.utils.NetworkMonitor
import timber.log.Timber
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

@HiltAndroidApp
class LittleGigApplication : Application() {
    
    @Inject lateinit var networkMonitor: NetworkMonitor
    @Inject lateinit var auth: FirebaseAuth
    @Inject lateinit var firestore: FirebaseFirestore
    
    override fun onCreate() {
        super.onCreate()
        val isDebug = (applicationInfo.flags and ApplicationInfo.FLAG_DEBUGGABLE) != 0
        if (isDebug) Timber.plant(Timber.DebugTree()) else Timber.plant(CrashlyticsTree())
        FirebaseCrashlytics.getInstance().setCrashlyticsCollectionEnabled(true)
        networkMonitor.startMonitoring()
        ProcessLifecycleOwner.get().lifecycle.addObserver(AppLifecycleObserver(auth, firestore))
    }
    
    override fun onTerminate() {
        super.onTerminate()
        networkMonitor.stopMonitoring()
    }
}

class AppLifecycleObserver(
    private val auth: FirebaseAuth,
    private val firestore: FirebaseFirestore
) : DefaultLifecycleObserver {
    override fun onStart(owner: LifecycleOwner) {
        val uid = auth.currentUser?.uid ?: return
        firestore.collection("users").document(uid).set(
            mapOf("online" to true), com.google.firebase.firestore.SetOptions.merge()
        )
    }
    override fun onStop(owner: LifecycleOwner) {
        val uid = auth.currentUser?.uid ?: return
        firestore.collection("users").document(uid).set(
            mapOf("online" to false, "lastSeen" to System.currentTimeMillis()), com.google.firebase.firestore.SetOptions.merge()
        )
    }
}

class CrashlyticsTree : Timber.Tree() {
    override fun log(priority: Int, tag: String?, message: String, t: Throwable?) {
        val crashlytics = FirebaseCrashlytics.getInstance()
        crashlytics.log("[" + (tag ?: "LittleGig") + "] " + message)
        if (t != null) crashlytics.recordException(t)
    }
}