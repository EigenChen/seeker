package com.seeker.locationtracker.data.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.serialization.Serializable

/**
 * 位置数据实体
 * 
 * @author seeker
 * @date 2025-08-10
 */
@Entity(tableName = "location_cache")
@Serializable
data class LocationEntity(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    val id: Long = 0,
    
    @ColumnInfo(name = "device_id")
    val deviceId: String,
    
    @ColumnInfo(name = "latitude")
    val latitude: Double,
    
    @ColumnInfo(name = "longitude")
    val longitude: Double,
    
    @ColumnInfo(name = "accuracy")
    val accuracy: Float? = null,
    
    @ColumnInfo(name = "altitude")
    val altitude: Double? = null,
    
    @ColumnInfo(name = "speed")
    val speed: Float? = null,
    
    @ColumnInfo(name = "bearing")
    val bearing: Float? = null,
    
    @ColumnInfo(name = "provider")
    val provider: String,
    
    @ColumnInfo(name = "location_timestamp")
    val locationTimestamp: Long,
    
    @ColumnInfo(name = "create_time")
    val createTime: Long = System.currentTimeMillis(),
    
    @ColumnInfo(name = "is_uploaded")
    val isUploaded: Boolean = false
)
