# Android Studio 安装和配置指南

## 📥 下载Android Studio

### 官方下载地址
- **官网**: https://developer.android.com/studio
- **版本要求**: Android Studio Hedgehog | 2023.1.1+ (推荐最新稳定版)
- **系统要求**: Windows 10/11 64-bit, 至少8GB RAM

### 下载步骤
1. 访问官网 https://developer.android.com/studio
2. 点击 "Download Android Studio" 
3. 选择适合您操作系统的版本
4. 下载大小约: 1GB+

## 🚀 安装步骤

### 1. 运行安装程序
- 双击下载的 `android-studio-xxx-windows.exe`
- 建议安装到默认路径: `C:\Program Files\Android\Android Studio`

### 2. 首次启动配置
```
1. 选择 "Standard" 安装类型
2. 选择UI主题 (Dark/Light)
3. 验证配置设置:
   - Android SDK Location: C:\Users\{用户名}\AppData\Local\Android\Sdk
   - Android SDK Build-Tools
   - Android Emulator
   - Intel HAXM (硬件加速)
```

### 3. SDK组件安装
Android Studio会自动下载以下组件:
- **Android SDK Platform-Tools**
- **Android SDK Build-Tools 34.0.0**
- **Android API 34 (Android 14)**
- **Android API 24 (Android 7.0)** - 项目最低版本
- **Google Play Services**
- **Android Emulator**

## 🔧 针对我们项目的特殊配置

### 环境变量设置
打开系统环境变量，添加:
```
ANDROID_HOME = C:\Users\{用户名}\AppData\Local\Android\Sdk
# 注意: JAVA_HOME保持现有设置不变 (指向Java 8)
# Android Studio会自动使用内置JDK，无需修改JAVA_HOME
```

**重要说明**: 如果您已经设置JAVA_HOME指向Java 8，请**不要修改**它，因为:
- Spring Boot服务器需要Java 8
- Android Studio会自动检测和使用内置JDK
- 修改JAVA_HOME可能影响现有项目构建

在PATH中添加:
```
%ANDROID_HOME%\platform-tools
%ANDROID_HOME%\tools
%ANDROID_HOME%\tools\bin
```

### 项目特定要求
根据我们的项目配置，需要确保安装:
- **JDK 8+** (Android Studio自带)
- **Android API 24** (最低支持版本)
- **Android API 34** (目标版本)
- **Kotlin编译器** (Android Studio自带)
- **Gradle 8.0+** (项目自动下载)

## 📱 创建测试设备

### 选项1: 物理设备 (推荐)
1. 启用开发者选项:
   - 设置 → 关于手机 → 版本号(连续点击7次)
2. 启用USB调试:
   - 设置 → 开发者选项 → USB调试
3. 连接USB线到电脑

### 选项2: Android虚拟设备 (AVD)
1. 打开AVD Manager
2. 创建新虚拟设备:
   - 设备: Pixel 6 (推荐)
   - 系统镜像: API 34 (Android 14)
   - RAM: 2048MB+
   - 启用硬件加速

## ⏱️ 预计时间
- **下载**: 15-30分钟 (取决于网速)
- **安装**: 5-10分钟
- **首次启动配置**: 10-20分钟
- **SDK下载**: 10-30分钟
- **总计**: 约1小时

## 🎯 安装完成后的验证

### 1. 检查安装
```bash
# 在命令行中运行:
adb version
# 应该显示 Android Debug Bridge version xxx
```

### 2. 检查SDK
```bash
# 检查安装的API版本
android list targets
```

### 3. 导入我们的项目
1. 启动Android Studio
2. 选择 "Open an Existing Project"
3. 浏览到 `c:\UGit\seeker\android-client`
4. 等待Gradle同步完成

## 🚨 常见问题

### 1. Gradle同步失败
- 确保网络连接正常
- 可能需要配置代理(如果在企业网络环境)

### 2. SDK许可证问题
```bash
# 在命令行中运行:
%ANDROID_HOME%\tools\bin\sdkmanager --licenses
# 然后输入 'y' 接受所有许可证
```

### 3. 虚拟设备启动慢
- 确保启用了硬件加速 (Intel HAXM)
- 分配足够的RAM给AVD

## 📋 安装清单
- [ ] 下载Android Studio
- [ ] 安装并首次启动
- [ ] 配置SDK组件
- [ ] 设置环境变量
- [ ] 创建/连接测试设备
- [ ] 导入seeker项目
- [ ] 验证项目构建成功

安装完成后，我们就可以开始构建和测试我们的位置追踪应用了！ 🚀
