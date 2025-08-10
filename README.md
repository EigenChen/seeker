# 位置追踪应用开发指南

## 项目概述
一个Android位置追踪应用，定期获取手机GPS位置并上传到服务器。

### 技术栈
- **Android客户端**：Kotlin + Jetpack Compose + MVVM架构
- **服务端**：Spring Boot + Maven + JPA + SQLite
- **通信协议**：HTTPS + JSON
- **数据库**：SQLite（轻量级，适合低并发场景）

## 项目结构
```
seeker/
├── android-client/          # Android应用
│   ├── app/                # 主应用模块
│   ├── feature/            # 功能模块
│   ├── core/               # 核心模块
│   └── data/               # 数据层
├── server/                 # Spring Boot服务端
│   ├── src/main/java/      # Java源码
│   ├── src/main/resources/ # 配置文件
│   └── src/test/          # 测试代码
├── shared/                 # 共享组件
│   ├── protocol/          # 接口协议定义
│   └── utils/             # 通用工具
├── docs/                   # 文档
├── ssl/                    # SSL证书文件
└── scripts/               # 部署脚本
```

## 开发阶段

### 阶段一：本地开发环境
1. **服务端**：本地运行Spring Boot，使用自签名HTTPS证书
2. **客户端**：Android应用连接本地服务器
3. **测试**：同一WiFi网络内测试

### 阶段二：公网部署
1. **服务端**：部署到公司服务器，配置Let's Encrypt证书
2. **客户端**：修改服务器地址为公网IP
3. **测试**：移动网络/外网WiFi测试

## 功能特性

### 核心功能
- 定期获取GPS位置（默认30秒间隔，可配置）
- 定期上传位置数据（默认50秒间隔，可配置）
- 本地数据缓存和离线重传
- 配置参数管理

### 技术特性
- HTTPS安全传输
- 电池优化策略
- 后台运行支持
- 网络异常处理
- 数据完整性校验

## 快速开始

### 服务端启动
```bash
cd server
./mvnw spring-boot:run -Dspring-boot.run.profiles=dev
```

### Android开发
```bash
cd android-client
./gradlew assembleDebug
```

## 配置说明

### 服务端配置
- `application-dev.yml`：开发环境（自签名证书）
- `application-prod.yml`：生产环境（Let's Encrypt证书）

### Android配置
- `debug/`：开发环境配置
- `release/`：发布环境配置

## 部署指南
详见 `docs/deployment.md`

## API文档
详见 `docs/api.md`
