package com.seeker.locationtracker.service.impl;

import com.seeker.locationtracker.mapper.LocationMapper;
import com.seeker.locationtracker.model.dto.LocationUploadDTO;
import com.seeker.locationtracker.model.entity.LocationEntity;
import com.seeker.locationtracker.model.vo.ResponseResult;
import com.seeker.locationtracker.service.LocationService;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;

/**
 * 位置数据服务实现类
 * 
 * @author seeker
 * @date 2025-08-10
 */
@Service
public class LocationServiceImpl implements LocationService {
    
    @Resource
    private LocationMapper locationMapper;
    
    @Override
    public ResponseResult<Void> uploadLocation(LocationUploadDTO locationDTO) {
        try {
            // 转换DTO为Entity
            LocationEntity entity = new LocationEntity();
            BeanUtils.copyProperties(locationDTO, entity);
            entity.setCreateTime(new Date());
            entity.setUpdateTime(new Date());
            
            // 保存到数据库
            int result = locationMapper.insert(entity);
            
            if (result > 0) {
                return ResponseResult.success("位置数据上传成功");
            } else {
                return ResponseResult.fail("位置数据上传失败");
            }
        } catch (Exception e) {
            return ResponseResult.fail("位置数据上传异常: " + e.getMessage());
        }
    }
    
    @Override
    public java.util.List<LocationEntity> getAllLocations() {
        return locationMapper.selectList(null);
    }
}
