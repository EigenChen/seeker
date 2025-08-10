package com.seeker.locationtracker.model.dto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * 位置数据传输对象
 * 
 * @author seeker
 * @date 2025-08-10
 */
public class LocationUploadDTO {
    
    @NotBlank(message = "设备ID不能为空")
    private String deviceId;
    
    @NotNull(message = "纬度不能为空")
    private Double latitude;
    
    @NotNull(message = "经度不能为空")
    private Double longitude;
    
    private Double accuracy;
    
    private Double altitude;
    
    private Double speed;
    
    private Double bearing;
    
    @NotBlank(message = "位置提供者不能为空")
    private String provider;
    
    @NotNull(message = "位置时间戳不能为空")
    private Long locationTimestamp;
    
    // 构造函数
    public LocationUploadDTO() {}
    
    // Getter和Setter方法
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
    
    @Override
    public String toString() {
        return "LocationUploadDTO{" +
                "deviceId='" + deviceId + '\'' +
                ", latitude=" + latitude +
                ", longitude=" + longitude +
                ", accuracy=" + accuracy +
                ", altitude=" + altitude +
                ", speed=" + speed +
                ", bearing=" + bearing +
                ", provider='" + provider + '\'' +
                ", locationTimestamp=" + locationTimestamp +
                '}';
    }
}
