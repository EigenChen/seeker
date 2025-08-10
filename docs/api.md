# API接口文档

## 基础信息
- **协议**：HTTPS
- **数据格式**：JSON
- **字符编码**：UTF-8
- **Content-Type**：application/json

## 接口列表

### 1. 健康检查

**GET** `/api/health`

用于检查服务状态，无需认证。

#### 响应示例
```json
{
  "status": "ok",
  "timestamp": 1691568000000,
  "version": "1.0.0"
}
```

#### 响应字段说明
| 字段 | 类型 | 说明 |
|------|------|------|
| status | string | 服务状态，ok表示正常 |
| timestamp | long | 服务器时间戳 |
| version | string | 服务版本号 |

---

### 2. 设备注册

**POST** `/api/v1/devices/register`

设备首次连接时注册，获取设备ID和配置信息。

#### 请求体
```json
{
  "deviceInfo": {
    "osVersion": "Android 13",
    "deviceModel": "Pixel 7",
    "appVersion": "1.0.0",
    "androidId": "unique_android_id"
  }
}
```

#### 请求字段说明
| 字段 | 类型 | 必填 | 说明 |
|------|------|------|------|
| osVersion | string | 是 | 操作系统版本 |
| deviceModel | string | 是 | 设备型号 |
| appVersion | string | 是 | 应用版本 |
| androidId | string | 是 | Android设备唯一标识 |

#### 响应示例
```json
{
  "success": true,
  "data": {
    "deviceId": "DEV_123456789",
    "serverConfig": {
      "locationInterval": 30000,
      "uploadInterval": 50000,
      "maxBatchSize": 10,
      "retentionDays": 30
    }
  },
  "message": "设备注册成功",
  "timestamp": 1691568000000
}
```

#### 响应字段说明
| 字段 | 类型 | 说明 |
|------|------|------|
| deviceId | string | 服务端分配的设备唯一标识 |
| locationInterval | long | 位置获取间隔(毫秒) |
| uploadInterval | long | 数据上传间隔(毫秒) |
| maxBatchSize | int | 批量上传最大条数 |
| retentionDays | int | 服务端数据保留天数 |

---

### 3. 位置数据上传

**POST** `/api/v1/locations`

批量上传位置数据，支持多条记录同时上传。

#### 请求头
```
Content-Type: application/json
X-Device-ID: DEV_123456789
```

#### 请求体
```json
{
  "deviceId": "DEV_123456789",
  "locations": [
    {
      "latitude": 39.9042,
      "longitude": 116.4074,
      "accuracy": 5.0,
      "altitude": 50.5,
      "speed": 0.0,
      "bearing": 180.0,
      "timestamp": 1691568000000,
      "provider": "fused"
    },
    {
      "latitude": 39.9043,
      "longitude": 116.4075,
      "accuracy": 8.0,
      "altitude": 51.0,
      "speed": 2.5,
      "bearing": 185.0,
      "timestamp": 1691568030000,
      "provider": "gps"
    }
  ],
  "deviceInfo": {
    "osVersion": "Android 13",
    "deviceModel": "Pixel 7",
    "appVersion": "1.0.0",
    "batteryLevel": 85,
    "networkType": "wifi"
  }
}
```

#### 请求字段说明

**locations数组中每个对象的字段：**

| 字段 | 类型 | 必填 | 说明 | 约束 |
|------|------|------|------|------|
| latitude | double | 是 | 纬度 | -90.0 到 90.0 |
| longitude | double | 是 | 经度 | -180.0 到 180.0 |
| accuracy | float | 否 | 精度(米) | ≥ 0 |
| altitude | double | 否 | 海拔(米) | 无限制 |
| speed | float | 否 | 速度(米/秒) | ≥ 0 |
| bearing | float | 否 | 方向角度 | 0.0 到 360.0 |
| timestamp | long | 是 | 时间戳(毫秒) | 有效的Unix时间戳 |
| provider | string | 是 | 位置提供者 | gps/network/fused/passive |

**deviceInfo字段：**

| 字段 | 类型 | 必填 | 说明 |
|------|------|------|------|
| osVersion | string | 是 | 操作系统版本 |
| deviceModel | string | 是 | 设备型号 |
| appVersion | string | 是 | 应用版本 |
| batteryLevel | int | 否 | 电池电量百分比 |
| networkType | string | 否 | 网络类型(wifi/mobile/none) |

#### 响应示例

**成功响应：**
```json
{
  "success": true,
  "data": {
    "processedCount": 2,
    "duplicateCount": 0,
    "failedCount": 0,
    "failedRecords": []
  },
  "message": "位置数据上传成功",
  "timestamp": 1691568000000
}
```

**部分失败响应：**
```json
{
  "success": true,
  "data": {
    "processedCount": 1,
    "duplicateCount": 0,
    "failedCount": 1,
    "failedRecords": [
      {
        "index": 1,
        "reason": "经纬度超出有效范围",
        "timestamp": 1691568030000
      }
    ]
  },
  "message": "部分数据上传成功",
  "timestamp": 1691568000000
}
```

#### 响应字段说明
| 字段 | 类型 | 说明 |
|------|------|------|
| processedCount | int | 成功处理的记录数 |
| duplicateCount | int | 重复记录数(已跳过) |
| failedCount | int | 失败记录数 |
| failedRecords | array | 失败记录详情 |

---

### 4. 获取位置历史

**GET** `/api/v1/locations/{deviceId}`

查询指定设备的位置历史记录。

#### 请求参数
| 参数 | 类型 | 必填 | 说明 | 默认值 |
|------|------|------|------|--------|
| deviceId | string | 是 | 设备ID | - |
| startTime | long | 否 | 开始时间戳 | 24小时前 |
| endTime | long | 否 | 结束时间戳 | 当前时间 |
| limit | int | 否 | 返回记录数限制 | 100 |
| offset | int | 否 | 分页偏移量 | 0 |

#### 请求示例
```
GET /api/v1/locations/DEV_123456789?startTime=1691481600000&endTime=1691568000000&limit=50
```

#### 响应示例
```json
{
  "success": true,
  "data": {
    "locations": [
      {
        "id": 12345,
        "latitude": 39.9042,
        "longitude": 116.4074,
        "accuracy": 5.0,
        "altitude": 50.5,
        "speed": 0.0,
        "bearing": 180.0,
        "timestamp": 1691568000000,
        "provider": "fused",
        "createdAt": 1691568001000
      }
    ],
    "total": 245,
    "hasMore": true
  },
  "message": "查询成功",
  "timestamp": 1691568000000
}
```

---

### 5. 设备配置更新

**PUT** `/api/v1/devices/{deviceId}/config`

更新设备的配置参数。

#### 请求体
```json
{
  "locationInterval": 45000,
  "uploadInterval": 60000,
  "maxBatchSize": 15
}
```

#### 响应示例
```json
{
  "success": true,
  "data": {
    "locationInterval": 45000,
    "uploadInterval": 60000,
    "maxBatchSize": 15,
    "updatedAt": 1691568000000
  },
  "message": "配置更新成功",
  "timestamp": 1691568000000
}
```

---

## 错误处理

### 错误响应格式
```json
{
  "success": false,
  "error": {
    "code": "ERROR_CODE",
    "message": "错误描述",
    "details": {
      "field": "具体字段错误信息",
      "timestamp": 1691568000000
    }
  },
  "timestamp": 1691568000000
}
```

### 常见错误码

| 错误码 | HTTP状态码 | 说明 |
|--------|-----------|------|
| INVALID_DEVICE_ID | 400 | 设备ID无效 |
| INVALID_COORDINATES | 400 | 经纬度坐标无效 |
| INVALID_TIMESTAMP | 400 | 时间戳格式错误 |
| DEVICE_NOT_FOUND | 404 | 设备未找到 |
| BATCH_SIZE_EXCEEDED | 413 | 批量上传超出限制 |
| RATE_LIMIT_EXCEEDED | 429 | 请求频率超限 |
| INTERNAL_ERROR | 500 | 服务器内部错误 |

### HTTP状态码说明

| 状态码 | 说明 |
|--------|------|
| 200 | 请求成功 |
| 400 | 请求参数错误 |
| 401 | 认证失败 |
| 403 | 权限不足 |
| 404 | 资源不存在 |
| 413 | 请求体过大 |
| 429 | 请求频率过高 |
| 500 | 服务器内部错误 |
| 503 | 服务暂不可用 |

---

## 请求限制

### 频率限制
- **位置上传**：每分钟最多60次请求
- **查询接口**：每分钟最多100次请求
- **配置更新**：每分钟最多10次请求

### 数据限制
- **批量上传**：单次最多100条位置记录
- **请求体大小**：最大1MB
- **查询结果**：单次最多返回1000条记录

---

## 数据安全

### HTTPS要求
- 所有API必须通过HTTPS访问
- 支持TLS 1.2及以上版本
- 拒绝不安全的连接

### 数据验证
- 严格验证所有输入参数
- 防止SQL注入和XSS攻击
- 实现请求防重放机制

### 隐私保护
- 位置数据仅保留30天
- 不记录敏感的设备信息
- 支持用户数据删除请求

---

## 测试用例

### 位置上传测试
```bash
# 成功上传
curl -X POST "https://localhost:8443/api/v1/locations" \
  -H "Content-Type: application/json" \
  -H "X-Device-ID: DEV_123456789" \
  -d '{
    "deviceId": "DEV_123456789",
    "locations": [
      {
        "latitude": 39.9042,
        "longitude": 116.4074,
        "accuracy": 5.0,
        "timestamp": 1691568000000,
        "provider": "gps"
      }
    ],
    "deviceInfo": {
      "osVersion": "Android 13",
      "deviceModel": "Pixel 7",
      "appVersion": "1.0.0"
    }
  }'
```

### 健康检查测试
```bash
curl -X GET "https://localhost:8443/api/health"
```
