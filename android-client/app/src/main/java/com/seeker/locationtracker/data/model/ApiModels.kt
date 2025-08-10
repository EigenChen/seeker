package com.seeker.locationtracker.data.model

import kotlinx.serialization.Serializable

/**
 * 位置上传数据传输对象
 * 
 * @author seeker
 * @date 2025-08-10
 */
@Serializable
data class LocationUploadDto(
    val deviceId: String,
    val latitude: Double,
    val longitude: Double,
    val accuracy: Float? = null,
    val altitude: Double? = null,
    val speed: Float? = null,
    val bearing: Float? = null,
    val provider: String,
    val locationTimestamp: Long
)

/**
 * API响应结果
 */
@Serializable
data class ApiResponse<T>(
    val statusCode: Int,
    val opDesc: String,
    val opCode: Int,
    val businessCode: String,
    val localTime: String,
    val data: T? = null
)
