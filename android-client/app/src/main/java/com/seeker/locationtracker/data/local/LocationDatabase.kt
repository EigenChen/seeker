package com.seeker.locationtracker.data.local

import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import android.content.Context
import com.seeker.locationtracker.data.model.LocationEntity

/**
 * 位置追踪本地数据库
 * 
 * @author seeker
 * @date 2025-08-10
 */
@Database(
    entities = [LocationEntity::class],
    version = 1,
    exportSchema = false
)
abstract class LocationDatabase : RoomDatabase() {
    
    abstract fun locationDao(): LocationDao
    
    companion object {
        const val DATABASE_NAME = "location_tracker.db"
    }
}
