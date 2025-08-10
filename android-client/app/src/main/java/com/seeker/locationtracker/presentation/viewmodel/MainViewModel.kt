package com.seeker.locationtracker.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.seeker.locationtracker.data.model.LocationEntity
import com.seeker.locationtracker.domain.location.LocationManager
import com.seeker.locationtracker.domain.repository.CacheStats
import com.seeker.locationtracker.domain.repository.LocationRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

/**
 * 主界面ViewModel
 * 
 * @author seeker
 * @date 2025-08-10
 */
@HiltViewModel
class MainViewModel @Inject constructor(
    private val locationRepository: LocationRepository,
    private val locationManager: LocationManager
) : ViewModel() {
    
    // UI状态
    private val _uiState = MutableStateFlow(MainUiState())
    val uiState: StateFlow<MainUiState> = _uiState.asStateFlow()
    
    // 位置数据列表
    val locations: StateFlow<List<LocationEntity>> = locationRepository.getAllLocations()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )
    
    init {
        // 初始化时检查权限和获取统计信息
        updatePermissionStatus()
        refreshCacheStats()
        
        // 定期刷新统计信息
        viewModelScope.launch {
            while (true) {
                kotlinx.coroutines.delay(10000) // 每10秒刷新一次
                refreshCacheStats()
            }
        }
    }
    
    /**
     * 开始位置追踪
     */
    fun startTracking() {
        _uiState.update { it.copy(isTracking = true) }
        Timber.d("开始位置追踪")
    }
    
    /**
     * 停止位置追踪
     */
    fun stopTracking() {
        _uiState.update { it.copy(isTracking = false) }
        Timber.d("停止位置追踪")
    }
    
    /**
     * 更新权限状态
     */
    fun updatePermissionStatus() {
        val hasLocationPermission = locationManager.hasLocationPermission()
        val hasBackgroundPermission = locationManager.hasBackgroundLocationPermission()
        
        _uiState.update {
            it.copy(
                hasLocationPermission = hasLocationPermission,
                hasBackgroundLocationPermission = hasBackgroundPermission
            )
        }
        
        Timber.d("权限状态更新: 位置权限=$hasLocationPermission, 后台权限=$hasBackgroundPermission")
    }
    
    /**
     * 刷新缓存统计信息
     */
    fun refreshCacheStats() {
        viewModelScope.launch {
            locationRepository.getCacheStats().fold(
                onSuccess = { stats ->
                    _uiState.update { it.copy(cacheStats = stats) }
                },
                onFailure = { error ->
                    Timber.e(error, "获取缓存统计失败")
                }
            )
        }
    }
    
    /**
     * 手动上传数据
     */
    fun uploadData() {
        viewModelScope.launch {
            _uiState.update { it.copy(isUploading = true) }
            
            locationRepository.uploadPendingLocations().fold(
                onSuccess = { count ->
                    _uiState.update { 
                        it.copy(
                            isUploading = false,
                            lastUploadResult = "成功上传 $count 条数据"
                        )
                    }
                    refreshCacheStats()
                },
                onFailure = { error ->
                    _uiState.update { 
                        it.copy(
                            isUploading = false,
                            lastUploadResult = "上传失败: ${error.message}"
                        )
                    }
                }
            )
        }
    }
    
    /**
     * 健康检查
     */
    fun healthCheck() {
        viewModelScope.launch {
            _uiState.update { it.copy(isHealthChecking = true) }
            
            locationRepository.healthCheck().fold(
                onSuccess = { result ->
                    _uiState.update { 
                        it.copy(
                            isHealthChecking = false,
                            healthCheckResult = "服务器正常: $result"
                        )
                    }
                },
                onFailure = { error ->
                    _uiState.update { 
                        it.copy(
                            isHealthChecking = false,
                            healthCheckResult = "服务器异常: ${error.message}"
                        )
                    }
                }
            )
        }
    }
    
    /**
     * 清除状态消息
     */
    fun clearMessages() {
        _uiState.update { 
            it.copy(
                lastUploadResult = null,
                healthCheckResult = null
            )
        }
    }
}

/**
 * 主界面UI状态
 */
data class MainUiState(
    val isTracking: Boolean = false,
    val hasLocationPermission: Boolean = false,
    val hasBackgroundLocationPermission: Boolean = false,
    val cacheStats: CacheStats? = null,
    val isUploading: Boolean = false,
    val isHealthChecking: Boolean = false,
    val lastUploadResult: String? = null,
    val healthCheckResult: String? = null
)
