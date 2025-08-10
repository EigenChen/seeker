# 位置追踪器 Android 客户端

## 📱 项目简介

这是一个基于Android的GPS位置追踪应用，能够：
- 定期获取设备GPS位置信息（默认30秒间隔）
- 定期上传位置数据到服务器（默认50秒间隔）
- 本地SQLite缓存，网络异常时离线存储
- 支持后台运行，前台服务保证追踪连续性

## 🏗️ 技术架构

### 核心技术栈
- **语言**: Kotlin
- **UI框架**: Jetpack Compose
- **架构模式**: MVVM + Clean Architecture
- **依赖注入**: Dagger Hilt
- **本地数据库**: Room
- **网络请求**: Retrofit + OkHttp
- **异步编程**: Coroutines + Flow
- **位置服务**: Google Play Services Location

### 项目结构
```
app/src/main/java/com/seeker/locationtracker/
├── data/                           # 数据层
│   ├── local/                      # 本地数据（Room数据库）
│   ├── remote/                     # 远程数据（API服务）
│   ├── repository/                 # 数据仓库实现
│   └── model/                      # 数据模型
├── domain/                         # 领域层
│   ├── repository/                 # 仓库接口
│   └── location/                   # 位置管理
├── presentation/                   # 表现层
│   ├── viewmodel/                  # ViewModel
│   └── theme/                      # UI主题
├── service/                        # 服务层
├── di/                            # 依赖注入
└── LocationTrackerApplication.kt   # 应用程序类
```

## 🚀 功能特性

### ✅ 已实现功能
- [x] GPS位置获取（30秒间隔）
- [x] 位置数据本地缓存（SQLite）
- [x] 位置数据上传服务器（50秒间隔）
- [x] 前台服务支持后台运行
- [x] 权限管理（位置权限 + 后台位置权限）
- [x] 网络异常处理和重试机制
- [x] HTTPS连接（支持自签名证书）
- [x] 实时UI状态显示
- [x] 数据统计和健康检查

### 🔧 配置项
- 位置获取间隔：30秒（可配置）
- 数据上传间隔：50秒（可配置）
- 服务器地址：`https://localhost:8443`（开发环境）
- 位置精度要求：高精度GPS
- 最小移动距离：5米

## 📋 权限要求

### 必需权限
- `ACCESS_FINE_LOCATION` - 精确位置访问
- `ACCESS_COARSE_LOCATION` - 粗略位置访问
- `ACCESS_BACKGROUND_LOCATION` - 后台位置访问（Android 10+）
- `FOREGROUND_SERVICE` - 前台服务
- `FOREGROUND_SERVICE_LOCATION` - 位置前台服务

### 网络权限
- `INTERNET` - 网络访问
- `ACCESS_NETWORK_STATE` - 网络状态检查

## 🔧 构建和运行

### 环境要求
- Android Studio Hedgehog | 2023.1.1+
- JDK 8+
- Android SDK API 24+（Android 7.0）
- 目标SDK API 34（Android 14）

### 构建步骤
1. 克隆项目到本地
2. 启动服务器端（Spring Boot）
3. 在Android Studio中打开android-client目录
4. 同步Gradle依赖
5. 连接Android设备或启动模拟器
6. 运行应用

### 调试配置
```kotlin
// BuildConfig中的配置
SERVER_BASE_URL = "https://10.0.2.2:8443"  // 模拟器访问本机
LOCATION_INTERVAL_MS = 30000L              // 30秒获取位置
UPLOAD_INTERVAL_MS = 50000L                // 50秒上传数据
```

## 📊 数据流向

```
GPS位置获取 → 本地SQLite缓存 → 定期上传到服务器 → 标记为已上传
     ↓              ↓                ↓
   位置服务      Room数据库        Retrofit API
     ↓              ↓                ↓
   前台服务      LocationDao      LocationApiService
```

## 🔐 安全特性

- HTTPS加密通信
- 自签名证书支持（开发环境）
- 网络安全配置
- 敏感数据备份排除
- 权限最小化原则

## 🐛 调试和日志

使用Timber日志框架，调试模式下会输出详细日志：
- 位置获取状态
- 数据上传结果
- 网络请求详情
- 权限检查结果

## 📱 UI界面

### 主要界面组件
1. **权限状态卡片** - 显示位置权限授权情况
2. **追踪控制卡片** - 开始/停止位置追踪
3. **数据统计卡片** - 缓存数据统计、手动上传、健康检查
4. **位置记录列表** - 显示最近10条位置记录

## 🔧 自定义配置

### 修改追踪间隔
在`app/build.gradle.kts`中修改：
```kotlin
buildConfigField("long", "LOCATION_INTERVAL_MS", "30000L") // 位置获取间隔
buildConfigField("long", "UPLOAD_INTERVAL_MS", "50000L")   // 数据上传间隔
```

### 修改服务器地址
```kotlin
buildConfigField("String", "SERVER_BASE_URL", "\"https://your-server.com:8443\"")
```

## 📄 许可证

本项目遵循开发规范要求，代码风格符合Kotlin官方规范和Android开发最佳实践。
