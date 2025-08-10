package com.seeker.locationtracker.service;

import com.seeker.locationtracker.model.dto.LocationUploadDTO;
import com.seeker.locationtracker.model.vo.ResponseResult;

/**
 * 位置数据服务接口
 * 
 * @author seeker
 * @date 2025-08-10
 */
public interface LocationService {
    
    /**
     * 上传位置数据
     * 
     * @param locationDTO 位置数据传输对象
     * @return 响应结果
     */
    ResponseResult<Void> uploadLocation(LocationUploadDTO locationDTO);
    
    /**
     * 获取所有位置数据（用于测试验证）
     * 
     * @return 位置数据列表
     */
    java.util.List<com.seeker.locationtracker.model.entity.LocationEntity> getAllLocations();
    
}
