package com.littlegig.app

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class LittleGigApplication : Application() {
    
    override fun onCreate() {
        super.onCreate()
    }
}