package com.seeker.locationtracker.data.repository

import com.seeker.locationtracker.data.local.LocationDao
import com.seeker.locationtracker.data.model.LocationEntity
import com.seeker.locationtracker.data.model.LocationUploadDto
import com.seeker.locationtracker.data.remote.LocationApiService
import com.seeker.locationtracker.domain.repository.CacheStats
import com.seeker.locationtracker.domain.repository.LocationRepository
import kotlinx.coroutines.flow.Flow
import timber.log.Timber
import java.util.concurrent.TimeUnit
import javax.inject.Inject
import javax.inject.Singleton

/**
 * 位置数据仓库实现
 * 
 * @author seeker
 * @date 2025-08-10
 */
@Singleton
class LocationRepositoryImpl @Inject constructor(
    private val locationDao: LocationDao,
    private val apiService: LocationApiService
) : LocationRepository {
    
    override suspend fun saveLocationToCache(location: LocationEntity): Result<Long> {
        return try {
            val id = locationDao.insertLocation(location)
            Timber.d("位置数据已保存到缓存: id=$id, lat=${location.latitude}, lng=${location.longitude}")
            Result.success(id)
        } catch (e: Exception) {
            Timber.e(e, "保存位置数据到缓存失败")
            Result.failure(e)
        }
    }
    
    override suspend fun uploadPendingLocations(): Result<Int> {
        return try {
            val pendingLocations = locationDao.getUnuploadedLocations()
            if (pendingLocations.isEmpty()) {
                Timber.d("没有待上传的位置数据")
                return Result.success(0)
            }
            
            Timber.d("开始上传 ${pendingLocations.size} 条位置数据")
            var successCount = 0
            val uploadedIds = mutableListOf<Long>()
            
            for (location in pendingLocations) {
                try {
                    val uploadDto = LocationUploadDto(
                        deviceId = location.deviceId,
                        latitude = location.latitude,
                        longitude = location.longitude,
                        accuracy = location.accuracy,
                        altitude = location.altitude,
                        speed = location.speed,
                        bearing = location.bearing,
                        provider = location.provider,
                        locationTimestamp = location.locationTimestamp
                    )
                    
                    val response = apiService.uploadLocation(uploadDto)
                    if (response.isSuccessful) {
                        val apiResponse = response.body()
                        if (apiResponse?.statusCode == 200) {
                            uploadedIds.add(location.id)
                            successCount++
                            Timber.d("位置数据上传成功: id=${location.id}")
                        } else {
                            Timber.w("位置数据上传失败: ${apiResponse?.opDesc}")
                        }
                    } else {
                        Timber.w("位置数据上传HTTP失败: ${response.code()} ${response.message()}")
                    }
                } catch (e: Exception) {
                    Timber.e(e, "上传位置数据异常: id=${location.id}")
                }
            }
            
            // 标记成功上传的数据
            if (uploadedIds.isNotEmpty()) {
                locationDao.markAsUploaded(uploadedIds)
                Timber.d("已标记 ${uploadedIds.size} 条数据为已上传")
            }
            
            Timber.d("位置数据上传完成: 成功 $successCount/${pendingLocations.size}")
            Result.success(successCount)
        } catch (e: Exception) {
            Timber.e(e, "上传位置数据失败")
            Result.failure(e)
        }
    }
    
    override fun getAllLocations(): Flow<List<LocationEntity>> {
        return locationDao.getAllLocations()
    }
    
    override suspend fun getCacheStats(): Result<CacheStats> {
        return try {
            val totalCount = locationDao.getCacheCount()
            val unuploadedCount = locationDao.getUnuploadedCount()
            val stats = CacheStats(totalCount, unuploadedCount, totalCount - unuploadedCount)
            Result.success(stats)
        } catch (e: Exception) {
            Timber.e(e, "获取缓存统计失败")
            Result.failure(e)
        }
    }
    
    override suspend fun cleanOldData(): Result<Unit> {
        return try {
            val sevenDaysAgo = System.currentTimeMillis() - TimeUnit.DAYS.toMillis(7)
            locationDao.deleteOldUploadedData(sevenDaysAgo)
            Timber.d("已清理7天前的旧数据")
            Result.success(Unit)
        } catch (e: Exception) {
            Timber.e(e, "清理旧数据失败")
            Result.failure(e)
        }
    }
    
    override suspend fun healthCheck(): Result<String> {
        return try {
            val response = apiService.healthCheck()
            if (response.isSuccessful) {
                val result = response.body()?.data ?: "OK"
                Timber.d("服务器健康检查成功: $result")
                Result.success(result)
            } else {
                val error = "服务器健康检查失败: ${response.code()}"
                Timber.w(error)
                Result.failure(Exception(error))
            }
        } catch (e: Exception) {
            Timber.e(e, "服务器健康检查异常")
            Result.failure(e)
        }
    }
}
