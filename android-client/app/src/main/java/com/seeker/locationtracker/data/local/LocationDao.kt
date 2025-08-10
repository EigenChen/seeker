package com.seeker.locationtracker.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.seeker.locationtracker.data.model.LocationEntity
import kotlinx.coroutines.flow.Flow

/**
 * 位置数据访问对象
 * 
 * @author seeker
 * @date 2025-08-10
 */
@Dao
interface LocationDao {
    
    /**
     * 插入位置数据
     */
    @Insert
    suspend fun insertLocation(location: LocationEntity): Long
    
    /**
     * 批量插入位置数据
     */
    @Insert
    suspend fun insertLocations(locations: List<LocationEntity>): List<Long>
    
    /**
     * 更新位置数据
     */
    @Update
    suspend fun updateLocation(location: LocationEntity)
    
    /**
     * 获取所有未上传的位置数据
     */
    @Query("SELECT * FROM location_cache WHERE is_uploaded = 0 ORDER BY create_time ASC")
    suspend fun getUnuploadedLocations(): List<LocationEntity>
    
    /**
     * 获取所有位置数据（用于显示）
     */
    @Query("SELECT * FROM location_cache ORDER BY create_time DESC")
    fun getAllLocations(): Flow<List<LocationEntity>>
    
    /**
     * 标记位置数据为已上传
     */
    @Query("UPDATE location_cache SET is_uploaded = 1 WHERE id IN (:locationIds)")
    suspend fun markAsUploaded(locationIds: List<Long>)
    
    /**
     * 删除已上传的旧数据（保留最近7天）
     */
    @Query("DELETE FROM location_cache WHERE is_uploaded = 1 AND create_time < :timestamp")
    suspend fun deleteOldUploadedData(timestamp: Long)
    
    /**
     * 获取缓存数据总数
     */
    @Query("SELECT COUNT(*) FROM location_cache")
    suspend fun getCacheCount(): Int
    
    /**
     * 获取未上传数据总数
     */
    @Query("SELECT COUNT(*) FROM location_cache WHERE is_uploaded = 0")
    suspend fun getUnuploadedCount(): Int
}
