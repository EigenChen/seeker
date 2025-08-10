package com.seeker.locationtracker.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.seeker.locationtracker.model.entity.LocationEntity;
import org.apache.ibatis.annotations.Mapper;

/**
 * 位置数据Mapper接口
 * 
 * @author seeker
 * @date 2025-08-10
 */
@Mapper
public interface LocationMapper extends BaseMapper<LocationEntity> {
    
}
