package com.seeker.locationtracker.data.remote

import com.seeker.locationtracker.data.model.ApiResponse
import com.seeker.locationtracker.data.model.LocationEntity
import com.seeker.locationtracker.data.model.LocationUploadDto
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

/**
 * 位置追踪API服务接口
 * 
 * @author seeker
 * @date 2025-08-10
 */
interface LocationApiService {
    
    /**
     * 上传位置数据
     */
    @POST("api/location/upload")
    suspend fun uploadLocation(@Body location: LocationUploadDto): Response<ApiResponse<Void>>
    
    /**
     * 健康检查
     */
    @GET("api/location/health")
    suspend fun healthCheck(): Response<ApiResponse<String>>
    
    /**
     * 获取位置列表（用于调试）
     */
    @GET("api/location/list")
    suspend fun getLocationList(): Response<ApiResponse<List<LocationEntity>>>
}
