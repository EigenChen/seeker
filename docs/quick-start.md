# 快速启动指南

## 前置要求
- **Java 17+** 已安装
- **OpenSSL** 已安装（用于生成证书）
- **SQLite** 无需单独安装（使用JDBC驱动）

## 第一步：生成开发环境HTTPS证书

### Windows用户
```cmd
cd c:\UGit\seeker
scripts\generate-dev-cert.bat
```

### Linux/macOS用户
```bash
cd /path/to/seeker
chmod +x scripts/generate-dev-cert.sh
./scripts/generate-dev-cert.sh
```

## 第二步：启动开发服务器

### Windows
```cmd
cd server
REM 首次运行需要下载Maven wrapper
download-maven-wrapper.bat
mvnw.cmd spring-boot:run -Dspring-boot.run.profiles=dev
```

### Linux/macOS
```bash
cd server
# 首次运行需要下载Maven wrapper
chmod +x download-maven-wrapper.sh
./download-maven-wrapper.sh
./mvnw spring-boot:run -Dspring-boot.run.profiles=dev
```

服务器将在 `https://localhost:8443` 启动

## 第三步：测试连接

### 使用curl测试
```bash
# 健康检查
curl -k https://localhost:8443/api/health

# 应该返回：
# {"status":"ok","timestamp":1691568000000,"version":"1.0.0"}
```

### 使用浏览器测试
访问：`https://localhost:8443/api/health`

**注意**：浏览器会显示安全警告，点击"高级" -> "继续访问"即可

## 第四步：查看SQLite数据库（可选）

数据库文件位置：`./data/dev/location_tracker_dev.db`

可以使用以下工具查看：
- **DB Browser for SQLite**：图形化SQLite管理工具
- **VSCode SQLite扩展**：在编辑器中直接查看
- **命令行**：`sqlite3 ./data/dev/location_tracker_dev.db`

## 常见问题

### 1. 端口被占用
```bash
# 查看端口占用（Windows）
netstat -ano | findstr :8443

# 查看端口占用（Linux/macOS）
lsof -i :8443
```

### 2. Java版本问题
```bash
# 检查Java版本
java -version

# 应该显示Java 17或更高版本
```

### 3. 证书生成失败
确保OpenSSL已正确安装：
- Windows: 下载安装 [Win32/Win64 OpenSSL](https://slproweb.com/products/Win32OpenSSL.html)
- Linux: `sudo apt-get install openssl` 或 `sudo yum install openssl`
- macOS: `brew install openssl`

### 4. 手机无法连接
1. 确保手机和电脑在同一WiFi网络
2. 使用电脑的IP地址而非localhost
3. 在Android应用中配置信任自签名证书

## 下一步

1. **Android开发**：创建Android项目并配置HTTPS连接
2. **API测试**：使用Postman或curl测试各个API接口
3. **数据库配置**：切换到MySQL进行完整测试
4. **部署准备**：准备生产环境的Let's Encrypt证书
