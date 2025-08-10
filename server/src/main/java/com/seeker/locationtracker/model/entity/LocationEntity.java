package com.seeker.locationtracker.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.util.Date;

/**
 * 位置数据实体类
 * 
 * @author seeker
 * @date 2025-08-10
 */
@TableName("T_LOCATION")
public class LocationEntity {
    
    @TableId(value = "TID", type = IdType.AUTO)
    private Long tid;
    
    private String deviceId;
    
    private Double latitude;
    
    private Double longitude;
    
    private Double accuracy;
    
    private Double altitude;
    
    private Double speed;
    
    private Double bearing;
    
    private String provider;
    
    private Long locationTimestamp;
    
    private Date createTime;
    
    private Date updateTime;
    
    // 构造函数
    public LocationEntity() {}
    
    // Getter和Setter方法
    public Long getTid() {
        return tid;
    }
    
    public void setTid(Long tid) {
        this.tid = tid;
    }
    
    public String getDeviceId() {
        return deviceId;
    }
    
    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }
    
    public Double getLatitude() {
        return latitude;
    }
    
    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }
    
    public Double getLongitude() {
        return longitude;
    }
    
    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }
    
    public Double getAccuracy() {
        return accuracy;
    }
    
    public void setAccuracy(Double accuracy) {
        this.accuracy = accuracy;
    }
    
    public Double getAltitude() {
        return altitude;
    }
    
    public void setAltitude(Double altitude) {
        this.altitude = altitude;
    }
    
    public Double getSpeed() {
        return speed;
    }
    
    public void setSpeed(Double speed) {
        this.speed = speed;
    }
    
    public Double getBearing() {
        return bearing;
    }
    
    public void setBearing(Double bearing) {
        this.bearing = bearing;
    }
    
    public String getProvider() {
        return provider;
    }
    
    public void setProvider(String provider) {
        this.provider = provider;
    }
    
    public Long getLocationTimestamp() {
        return locationTimestamp;
    }
    
    public void setLocationTimestamp(Long locationTimestamp) {
        this.locationTimestamp = locationTimestamp;
    }
    
    public Date getCreateTime() {
        return createTime;
    }
    
    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }
    
    public Date getUpdateTime() {
        return updateTime;
    }
    
    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }
    
    @Override
    public String toString() {
        return "LocationEntity{" +
                "tid=" + tid +
                ", deviceId='" + deviceId + '\'' +
                ", latitude=" + latitude +
                ", longitude=" + longitude +
                ", accuracy=" + accuracy +
                ", altitude=" + altitude +
                ", speed=" + speed +
                ", bearing=" + bearing +
                ", provider='" + provider + '\'' +
                ", locationTimestamp=" + locationTimestamp +
                ", createTime=" + createTime +
                ", updateTime=" + updateTime +
                '}';
    }
}
