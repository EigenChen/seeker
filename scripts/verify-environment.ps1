# 环境变量验证脚本

# 检查Java 8 (用于Spring Boot)
echo "=== Java 8 验证 (Spring Boot服务器) ==="
java -version
echo "JAVA_HOME: $env:JAVA_HOME"

# 检查Android环境
echo "=== Android环境验证 ==="
echo "ANDROID_HOME: $env:ANDROID_HOME"

# 测试Spring Boot构建
echo "=== Spring Boot构建测试 ==="
cd c:\UGit\seeker\server
.\mvnw.cmd --version

# 测试Android构建 (Android Studio安装后)
echo "=== Android构建测试 ==="
cd c:\UGit\seeker\android-client
# gradlew.bat --version  # 安装Android Studio后执行
