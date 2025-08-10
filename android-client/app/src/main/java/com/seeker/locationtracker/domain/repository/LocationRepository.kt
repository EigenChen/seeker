package com.seeker.locationtracker.domain.repository

import com.seeker.locationtracker.data.model.LocationEntity
import kotlinx.coroutines.flow.Flow

/**
 * 位置数据仓库接口
 * 
 * @author seeker
 * @date 2025-08-10
 */
interface LocationRepository {
    
    /**
     * 保存位置数据到本地缓存
     */
    suspend fun saveLocationToCache(location: LocationEntity): Result<Long>
    
    /**
     * 上传未上传的位置数据
     */
    suspend fun uploadPendingLocations(): Result<Int>
    
    /**
     * 获取所有位置数据（用于UI显示）
     */
    fun getAllLocations(): Flow<List<LocationEntity>>
    
    /**
     * 获取缓存统计信息
     */
    suspend fun getCacheStats(): Result<CacheStats>
    
    /**
     * 清理旧的已上传数据
     */
    suspend fun cleanOldData(): Result<Unit>
    
    /**
     * 健康检查
     */
    suspend fun healthCheck(): Result<String>
}

/**
 * 缓存统计信息
 */
data class CacheStats(
    val totalCount: Int,
    val unuploadedCount: Int,
    val uploadedCount: Int
) {
    val uploadedCount: Int
        get() = totalCount - unuploadedCount
}
