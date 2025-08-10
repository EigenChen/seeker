package com.seeker.locationtracker.presentation

import android.Manifest
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberMultiplePermissionsState
import com.google.accompanist.permissions.shouldShowRationale
import com.seeker.locationtracker.data.model.LocationEntity
import com.seeker.locationtracker.presentation.theme.LocationTrackerTheme
import com.seeker.locationtracker.presentation.viewmodel.MainViewModel
import com.seeker.locationtracker.service.LocationTrackingService
import dagger.hilt.android.AndroidEntryPoint
import java.text.SimpleDateFormat
import java.util.*

/**
 * 主活动
 * 
 * @author seeker
 * @date 2025-08-10
 */
@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    
    private val viewModel: MainViewModel by viewModels()
    
    private val backgroundLocationPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        viewModel.updatePermissionStatus()
        if (!isGranted) {
            // 可以显示说明为什么需要后台位置权限
        }
    }
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        setContent {
            LocationTrackerTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    MainScreen(
                        viewModel = viewModel,
                        onRequestBackgroundPermission = {
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                                backgroundLocationPermissionLauncher.launch(
                                    Manifest.permission.ACCESS_BACKGROUND_LOCATION
                                )
                            }
                        }
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun MainScreen(
    viewModel: MainViewModel,
    onRequestBackgroundPermission: () -> Unit
) {
    val context = LocalContext.current
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val locations by viewModel.locations.collectAsStateWithLifecycle()
    
    // 位置权限状态
    val locationPermissionsState = rememberMultiplePermissionsState(
        permissions = listOf(
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION
        )
    ) { permissions ->
        viewModel.updatePermissionStatus()
    }
    
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // 标题
        Text(
            text = "位置追踪器",
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold
        )
        
        // 权限检查卡片
        PermissionCard(
            hasLocationPermission = uiState.hasLocationPermission,
            hasBackgroundPermission = uiState.hasBackgroundLocationPermission,
            onRequestLocationPermission = {
                locationPermissionsState.launchMultiplePermissionRequest()
            },
            onRequestBackgroundPermission = onRequestBackgroundPermission,
            shouldShowRationale = locationPermissionsState.permissions.any { it.status.shouldShowRationale }
        )
        
        // 追踪控制卡片
        TrackingControlCard(
            isTracking = uiState.isTracking,
            canStartTracking = uiState.hasLocationPermission,
            onStartTracking = {
                viewModel.startTracking()
                LocationTrackingService.startService(context)
            },
            onStopTracking = {
                viewModel.stopTracking()
                LocationTrackingService.stopService(context)
            }
        )
        
        // 统计信息卡片
        StatsCard(
            cacheStats = uiState.cacheStats,
            isUploading = uiState.isUploading,
            isHealthChecking = uiState.isHealthChecking,
            lastUploadResult = uiState.lastUploadResult,
            healthCheckResult = uiState.healthCheckResult,
            onUpload = viewModel::uploadData,
            onHealthCheck = viewModel::healthCheck,
            onRefresh = viewModel::refreshCacheStats,
            onClearMessages = viewModel::clearMessages
        )
        
        // 位置数据列表
        LocationListCard(locations = locations)
    }
}

@Composable
fun PermissionCard(
    hasLocationPermission: Boolean,
    hasBackgroundPermission: Boolean,
    onRequestLocationPermission: () -> Unit,
    onRequestBackgroundPermission: () -> Unit,
    shouldShowRationale: Boolean
) {
    val context = LocalContext.current
    
    Card {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(
                text = "权限状态",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )
            
            PermissionStatus(
                name = "位置权限",
                isGranted = hasLocationPermission
            )
            
            PermissionStatus(
                name = "后台位置权限",
                isGranted = hasBackgroundPermission
            )
            
            if (!hasLocationPermission) {
                if (shouldShowRationale) {
                    Text(
                        text = "需要位置权限来获取GPS数据",
                        style = MaterialTheme.typography.bodySmall
                    )
                    Button(
                        onClick = {
                            val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
                                data = Uri.fromParts("package", context.packageName, null)
                            }
                            context.startActivity(intent)
                        }
                    ) {
                        Text("去设置")
                    }
                } else {
                    Button(onClick = onRequestLocationPermission) {
                        Text("申请位置权限")
                    }
                }
            }
            
            if (hasLocationPermission && !hasBackgroundPermission && Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                Button(onClick = onRequestBackgroundPermission) {
                    Text("申请后台位置权限")
                }
            }
        }
    }
}

@Composable
fun PermissionStatus(name: String, isGranted: Boolean) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(text = name)
        Text(
            text = if (isGranted) "✓ 已授权" else "✗ 未授权",
            color = if (isGranted) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.error
        )
    }
}

@Composable
fun TrackingControlCard(
    isTracking: Boolean,
    canStartTracking: Boolean,
    onStartTracking: () -> Unit,
    onStopTracking: () -> Unit
) {
    Card {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(
                text = "追踪控制",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )
            
            Text(
                text = if (isTracking) "正在追踪位置..." else "位置追踪已停止",
                color = if (isTracking) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface
            )
            
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Button(
                    onClick = onStartTracking,
                    enabled = !isTracking && canStartTracking
                ) {
                    Text("开始追踪")
                }
                
                Button(
                    onClick = onStopTracking,
                    enabled = isTracking
                ) {
                    Text("停止追踪")
                }
            }
        }
    }
}

@Composable
fun StatsCard(
    cacheStats: com.seeker.locationtracker.domain.repository.CacheStats?,
    isUploading: Boolean,
    isHealthChecking: Boolean,
    lastUploadResult: String?,
    healthCheckResult: String?,
    onUpload: () -> Unit,
    onHealthCheck: () -> Unit,
    onRefresh: () -> Unit,
    onClearMessages: () -> Unit
) {
    Card {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(
                text = "数据统计",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )
            
            if (cacheStats != null) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text("总数据: ${cacheStats.totalCount}")
                    Text("待上传: ${cacheStats.unuploadedCount}")
                    Text("已上传: ${cacheStats.uploadedCount}")
                }
            }
            
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Button(
                    onClick = onUpload,
                    enabled = !isUploading
                ) {
                    if (isUploading) {
                        CircularProgressIndicator(modifier = Modifier.size(16.dp))
                    } else {
                        Text("上传数据")
                    }
                }
                
                Button(
                    onClick = onHealthCheck,
                    enabled = !isHealthChecking
                ) {
                    if (isHealthChecking) {
                        CircularProgressIndicator(modifier = Modifier.size(16.dp))
                    } else {
                        Text("健康检查")
                    }
                }
                
                Button(onClick = onRefresh) {
                    Text("刷新")
                }
            }
            
            // 显示结果消息
            lastUploadResult?.let { result ->
                Text(
                    text = "上传结果: $result",
                    style = MaterialTheme.typography.bodySmall
                )
            }
            
            healthCheckResult?.let { result ->
                Text(
                    text = "健康检查: $result",
                    style = MaterialTheme.typography.bodySmall
                )
            }
            
            if (lastUploadResult != null || healthCheckResult != null) {
                TextButton(onClick = onClearMessages) {
                    Text("清除消息")
                }
            }
        }
    }
}

@Composable
fun LocationListCard(locations: List<LocationEntity>) {
    Card {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = "位置记录 (最近10条)",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(bottom = 8.dp)
            )
            
            if (locations.isEmpty()) {
                Text(
                    text = "暂无位置数据",
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.padding(16.dp)
                )
            } else {
                LazyColumn(
                    modifier = Modifier.heightIn(max = 300.dp),
                    verticalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    items(locations.take(10)) { location ->
                        LocationItem(location = location)
                    }
                }
            }
        }
    }
}

@Composable
fun LocationItem(location: LocationEntity) {
    val dateFormat = remember { SimpleDateFormat("MM-dd HH:mm:ss", Locale.getDefault()) }
    
    Card(
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        )
    ) {
        Column(
            modifier = Modifier.padding(12.dp),
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "${String.format("%.6f", location.latitude)}, ${String.format("%.6f", location.longitude)}",
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.Medium
                )
                Text(
                    text = if (location.isUploaded) "✓" else "⧗",
                    color = if (location.isUploaded) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.outline
                )
            }
            
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "精度: ${location.accuracy?.let { "${String.format("%.1f", it)}m" } ?: "N/A"}",
                    style = MaterialTheme.typography.bodySmall
                )
                Text(
                    text = dateFormat.format(Date(location.createTime)),
                    style = MaterialTheme.typography.bodySmall
                )
            }
        }
    }
}
