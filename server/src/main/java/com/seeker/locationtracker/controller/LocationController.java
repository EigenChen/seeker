package com.seeker.locationtracker.controller;

import com.seeker.locationtracker.model.dto.LocationUploadDTO;
import com.seeker.locationtracker.model.vo.ResponseResult;
import com.seeker.locationtracker.service.LocationService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;

/**
 * 位置数据控制器
 * 
 * @author seeker
 * @date 2025-08-10
 */
@RestController
@RequestMapping("/api/location")
public class LocationController {
    
    @Resource
    private LocationService locationService;
    
    /**
     * 上传位置数据
     * 
     * @param locationDTO 位置数据传输对象
     * @return 响应结果
     */
    @PostMapping("/upload")
    public ResponseResult<Void> uploadLocation(@Valid @RequestBody LocationUploadDTO locationDTO) {
        return locationService.uploadLocation(locationDTO);
    }
    
    /**
     * 健康检查接口
     * 
     * @return 响应结果
     */
    @GetMapping("/health")
    public ResponseResult<String> health() {
        return ResponseResult.success("位置服务运行正常", "OK");
    }
    
    /**
     * 查询所有位置数据（用于测试验证）
     * 
     * @return 响应结果
     */
    @GetMapping("/list")
    public ResponseResult<Object> listLocations() {
        try {
            // 简单查询所有数据用于验证
            java.util.List<com.seeker.locationtracker.model.entity.LocationEntity> locations = 
                locationService.getAllLocations();
            return ResponseResult.success(locations, "查询成功，共 " + locations.size() + " 条记录");
        } catch (Exception e) {
            return ResponseResult.fail("查询失败: " + e.getMessage());
        }
    }
}
