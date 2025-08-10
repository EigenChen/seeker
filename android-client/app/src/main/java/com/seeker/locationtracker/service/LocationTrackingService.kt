package com.seeker.locationtracker.service

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.IBinder
import androidx.core.app.NotificationCompat
import com.seeker.locationtracker.R
import com.seeker.locationtracker.data.model.LocationEntity
import com.seeker.locationtracker.domain.location.LocationManager
import com.seeker.locationtracker.domain.repository.LocationRepository
import com.seeker.locationtracker.presentation.MainActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import timber.log.Timber
import java.util.*
import javax.inject.Inject

/**
 * 位置追踪前台服务
 * 
 * @author seeker
 * @date 2025-08-10
 */
@AndroidEntryPoint
class LocationTrackingService : Service() {
    
    @Inject
    lateinit var locationManager: LocationManager
    
    @Inject
    lateinit var locationRepository: LocationRepository
    
    private val serviceScope = CoroutineScope(SupervisorJob() + Dispatchers.IO)
    private var locationTrackingJob: Job? = null
    private var uploadJob: Job? = null
    
    private val deviceId by lazy {
        // 生成或获取设备唯一标识
        getSharedPreferences("location_tracker", Context.MODE_PRIVATE)
            .getString("device_id", null) ?: run {
            val newId = "android_${UUID.randomUUID().toString().substring(0, 8)}"
            getSharedPreferences("location_tracker", Context.MODE_PRIVATE)
                .edit()
                .putString("device_id", newId)
                .apply()
            newId
        }
    }
    
    override fun onCreate() {
        super.onCreate()
        Timber.d("LocationTrackingService创建")
        createNotificationChannel()
    }
    
    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        when (intent?.action) {
            ACTION_START_TRACKING -> startLocationTracking()
            ACTION_STOP_TRACKING -> stopLocationTracking()
            else -> startLocationTracking()
        }
        
        return START_STICKY // 服务被杀死后自动重启
    }
    
    override fun onBind(intent: Intent?): IBinder? = null
    
    override fun onDestroy() {
        super.onDestroy()
        stopLocationTracking()
        serviceScope.cancel()
        Timber.d("LocationTrackingService销毁")
    }
    
    private fun startLocationTracking() {
        Timber.d("开始位置追踪服务")
        
        // 启动前台服务
        val notification = createNotification("正在获取位置信息...")
        startForeground(NOTIFICATION_ID, notification)
        
        // 检查权限
        if (!locationManager.hasLocationPermission()) {
            Timber.w("缺少位置权限，无法启动位置追踪")
            stopSelf()
            return
        }
        
        // 启动位置追踪
        startLocationUpdates()
        
        // 启动数据上传
        startDataUpload()
    }
    
    private fun stopLocationTracking() {
        Timber.d("停止位置追踪服务")
        
        locationTrackingJob?.cancel()
        uploadJob?.cancel()
        locationManager.stopLocationUpdates()
        
        stopForeground(STOP_FOREGROUND_REMOVE)
        stopSelf()
    }
    
    private fun startLocationUpdates() {
        locationTrackingJob = serviceScope.launch {
            locationManager.startLocationUpdates()
                .catch { exception ->
                    Timber.e(exception, "位置更新流异常")
                    updateNotification("位置获取失败: ${exception.message}")
                }
                .collect { location ->
                    try {
                        val locationEntity = LocationEntity(
                            deviceId = deviceId,
                            latitude = location.latitude,
                            longitude = location.longitude,
                            accuracy = location.accuracy,
                            altitude = if (location.hasAltitude()) location.altitude else null,
                            speed = if (location.hasSpeed()) location.speed else null,
                            bearing = if (location.hasBearing()) location.bearing else null,
                            provider = location.provider ?: "unknown",
                            locationTimestamp = location.time
                        )
                        
                        // 保存到本地缓存
                        locationRepository.saveLocationToCache(locationEntity).fold(
                            onSuccess = { id ->
                                Timber.d("位置数据已缓存: id=$id")
                                updateNotification("位置已更新: ${String.format("%.6f", location.latitude)}, ${String.format("%.6f", location.longitude)}")
                            },
                            onFailure = { error ->
                                Timber.e(error, "保存位置数据失败")
                            }
                        )
                    } catch (e: Exception) {
                        Timber.e(e, "处理位置数据异常")
                    }
                }
        }
    }
    
    private fun startDataUpload() {
        uploadJob = serviceScope.launch {
            while (isActive) {
                try {
                    delay(com.seeker.locationtracker.BuildConfig.UPLOAD_INTERVAL_MS)
                    
                    locationRepository.uploadPendingLocations().fold(
                        onSuccess = { count ->
                            if (count > 0) {
                                Timber.d("成功上传 $count 条位置数据")
                                updateNotification("已上传 $count 条数据")
                            }
                        },
                        onFailure = { error ->
                            Timber.w(error, "上传位置数据失败")
                        }
                    )
                    
                    // 定期清理旧数据
                    if (System.currentTimeMillis() % (24 * 60 * 60 * 1000) < com.seeker.locationtracker.BuildConfig.UPLOAD_INTERVAL_MS) {
                        locationRepository.cleanOldData()
                    }
                } catch (e: Exception) {
                    Timber.e(e, "数据上传任务异常")
                }
            }
        }
    }
    
    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                CHANNEL_ID,
                "位置追踪服务",
                NotificationManager.IMPORTANCE_LOW
            ).apply {
                description = "GPS位置追踪和数据上传服务"
                setShowBadge(false)
                setSound(null, null)
            }
            
            val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }
    
    private fun createNotification(content: String): Notification {
        val intent = Intent(this, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        val pendingIntent = PendingIntent.getActivity(
            this, 0, intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
        
        val stopIntent = Intent(this, LocationTrackingService::class.java).apply {
            action = ACTION_STOP_TRACKING
        }
        val stopPendingIntent = PendingIntent.getService(
            this, 1, stopIntent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
        
        return NotificationCompat.Builder(this, CHANNEL_ID)
            .setContentTitle("位置追踪中")
            .setContentText(content)
            .setSmallIcon(R.drawable.ic_location_on_24)
            .setContentIntent(pendingIntent)
            .addAction(R.drawable.ic_stop_24, "停止", stopPendingIntent)
            .setOngoing(true)
            .setSilent(true)
            .build()
    }
    
    private fun updateNotification(content: String) {
        val notification = createNotification(content)
        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.notify(NOTIFICATION_ID, notification)
    }
    
    companion object {
        private const val CHANNEL_ID = "location_tracking_channel"
        private const val NOTIFICATION_ID = 1001
        
        const val ACTION_START_TRACKING = "com.seeker.locationtracker.START_TRACKING"
        const val ACTION_STOP_TRACKING = "com.seeker.locationtracker.STOP_TRACKING"
        
        fun startService(context: Context) {
            val intent = Intent(context, LocationTrackingService::class.java).apply {
                action = ACTION_START_TRACKING
            }
            context.startForegroundService(intent)
        }
        
        fun stopService(context: Context) {
            val intent = Intent(context, LocationTrackingService::class.java).apply {
                action = ACTION_STOP_TRACKING
            }
            context.startService(intent)
        }
    }
}
