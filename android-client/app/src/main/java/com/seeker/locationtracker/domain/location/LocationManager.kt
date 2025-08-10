package com.seeker.locationtracker.domain.location

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.os.Looper
import androidx.core.app.ActivityCompat
import com.google.android.gms.location.*
import com.seeker.locationtracker.BuildConfig
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import timber.log.Timber
import javax.inject.Inject
import javax.inject.Singleton

/**
 * GPS位置管理器
 * 
 * @author seeker
 * @date 2025-08-10
 */
@Singleton
class LocationManager @Inject constructor(
    @ApplicationContext private val context: Context
) {
    
    private val fusedLocationClient: FusedLocationProviderClient by lazy {
        LocationServices.getFusedLocationProviderClient(context)
    }
    
    private val locationRequest = LocationRequest.Builder(
        Priority.PRIORITY_HIGH_ACCURACY,
        BuildConfig.LOCATION_INTERVAL_MS
    ).apply {
        setMinUpdateDistanceMeters(5f) // 最小5米移动距离
        setGranularity(Granularity.GRANULARITY_PERMISSION_LEVEL)
        setWaitForAccurateLocation(false)
    }.build()
    
    /**
     * 检查位置权限
     */
    fun hasLocationPermission(): Boolean {
        return ActivityCompat.checkSelfPermission(
            context,
            Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(
                    context,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                ) == PackageManager.PERMISSION_GRANTED
    }
    
    /**
     * 检查后台位置权限
     */
    fun hasBackgroundLocationPermission(): Boolean {
        return if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.Q) {
            ActivityCompat.checkSelfPermission(
                context,
                Manifest.permission.ACCESS_BACKGROUND_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        } else {
            true // Android 10以下不需要后台位置权限
        }
    }
    
    /**
     * 获取当前位置（一次性）
     */
    suspend fun getCurrentLocation(): Result<Location> {
        return try {
            if (!hasLocationPermission()) {
                return Result.failure(SecurityException("缺少位置权限"))
            }
            
            val location = fusedLocationClient.getCurrentLocation(
                Priority.PRIORITY_HIGH_ACCURACY,
                null
            )
            
            val result = location.result
            if (result != null) {
                Timber.d("获取当前位置成功: ${result.latitude}, ${result.longitude}")
                Result.success(result)
            } else {
                Result.failure(Exception("无法获取当前位置"))
            }
        } catch (e: Exception) {
            Timber.e(e, "获取当前位置失败")
            Result.failure(e)
        }
    }
    
    /**
     * 开始位置更新流
     */
    fun startLocationUpdates(): Flow<Location> = callbackFlow {
        if (!hasLocationPermission()) {
            close(SecurityException("缺少位置权限"))
            return@callbackFlow
        }
        
        val locationCallback = object : LocationCallback() {
            override fun onLocationResult(result: LocationResult) {
                result.locations.forEach { location ->
                    Timber.d("收到位置更新: ${location.latitude}, ${location.longitude}, 精度: ${location.accuracy}m")
                    trySend(location)
                }
            }
            
            override fun onLocationAvailability(availability: LocationAvailability) {
                Timber.d("位置可用性: ${availability.isLocationAvailable}")
                if (!availability.isLocationAvailable) {
                    // 可以考虑发送错误或重试
                }
            }
        }
        
        try {
            fusedLocationClient.requestLocationUpdates(
                locationRequest,
                locationCallback,
                Looper.getMainLooper()
            )
            Timber.d("开始位置更新，间隔: ${BuildConfig.LOCATION_INTERVAL_MS}ms")
        } catch (e: SecurityException) {
            Timber.e(e, "启动位置更新失败：权限不足")
            close(e)
        } catch (e: Exception) {
            Timber.e(e, "启动位置更新失败")
            close(e)
        }
        
        awaitClose {
            Timber.d("停止位置更新")
            fusedLocationClient.removeLocationUpdates(locationCallback)
        }
    }
    
    /**
     * 停止位置更新
     */
    fun stopLocationUpdates() {
        fusedLocationClient.removeLocationUpdates(object : LocationCallback() {})
        Timber.d("已停止所有位置更新")
    }
}
