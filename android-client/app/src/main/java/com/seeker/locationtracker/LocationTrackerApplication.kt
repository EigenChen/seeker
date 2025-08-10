package com.seeker.locationtracker

import android.app.Application
import dagger.hilt.android.HiltAndroidApp
import timber.log.Timber

/**
 * 位置追踪应用程序类
 * 
 * @author seeker
 * @date 2025-08-10
 */
@HiltAndroidApp
class LocationTrackerApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        
        // 初始化日志框架
        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }
        
        Timber.d("LocationTracker应用启动")
    }
}
