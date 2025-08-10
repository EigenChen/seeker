# VS Code中Android开发环境配置指南

## 🔧 前置要求
1. 安装 Android SDK 和命令行工具
2. 配置环境变量 ANDROID_HOME
3. 安装 Android Platform Tools (adb, fastboot)
4. 安装 Android Build Tools

## 📱 VS Code扩展推荐
- Android iOS Emulator
- Kotlin Language
- Gradle for Java
- Android Studio App Bundle

## 🚀 命令行构建流程
```bash
# 1. 检查环境
gradlew tasks

# 2. 清理构建
gradlew clean

# 3. 构建Debug APK
gradlew assembleDebug

# 4. 运行单元测试
gradlew test

# 5. 运行Lint检查
gradlew lint

# 6. 安装到设备
adb install app/build/outputs/apk/debug/app-debug.apk
```

## ⚠️ 限制
- 无可视化布局设计器
- 调试功能有限
- 需要手动管理模拟器
