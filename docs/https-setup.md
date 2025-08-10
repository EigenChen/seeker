# HTTPS证书配置指南

## 概述
本项目支持两种HTTPS证书配置方式：
1. **自签名证书**：用于本地开发环境
2. **Let's Encrypt证书**：用于公网部署

## 阶段一：本地开发环境（自签名证书）

### 1. 生成自签名证书

在 `seeker/ssl/` 目录下执行以下命令：

```bash
# 创建SSL目录
mkdir -p ssl
cd ssl

# 生成私钥
openssl genrsa -out server.key 2048

# 生成证书签名请求
openssl req -new -key server.key -out server.csr

# 生成自签名证书（有效期1年）
openssl x509 -req -days 365 -in server.csr -signkey server.key -out server.crt

# 生成PKCS12格式证书（Spring Boot需要）
openssl pkcs12 -export -in server.crt -inkey server.key -out keystore.p12 -name tomcat
```

### 2. 证书信息填写示例
```
Country Name (2 letter code) [XX]: CN
State or Province Name (full name) []: Beijing
Locality Name (eg, city) []: Beijing
Organization Name (eg, company) []: YourCompany
Organizational Unit Name (eg, section) []: IT
Common Name (eg, your name or your server's hostname) []: localhost
Email Address []: your.email@company.com
```

**重要**：Common Name 必须填写 `localhost` 或你的开发机器IP地址

### 3. Spring Boot HTTPS配置

在 `server/src/main/resources/application-dev.yml` 中添加：

```yaml
server:
  port: 8443
  ssl:
    enabled: true
    key-store: classpath:ssl/keystore.p12
    key-store-password: your-password
    key-store-type: PKCS12
    key-alias: tomcat
    protocol: TLS
    enabled-protocols: TLSv1.2,TLSv1.3

# HTTP重定向到HTTPS
security:
  require-ssl: true
```

### 4. Android客户端配置

#### 4.1 添加网络安全配置
在 `android-client/app/src/main/res/xml/network_security_config.xml`：

```xml
<?xml version="1.0" encoding="utf-8"?>
<network-security-config>
    <!-- 开发环境：信任自签名证书 -->
    <debug-overrides>
        <trust-anchors>
            <certificates src="user"/>
            <certificates src="system"/>
        </trust-anchors>
    </debug-overrides>
    
    <!-- 生产环境：只信任系统证书 -->
    <base-config cleartextTrafficPermitted="false">
        <trust-anchors>
            <certificates src="system"/>
        </trust-anchors>
    </base-config>
    
    <!-- 本地开发服务器 -->
    <domain-config cleartextTrafficPermitted="false">
        <domain includeSubdomains="false">192.168.1.100</domain> <!-- 替换为你的开发机IP -->
        <domain includeSubdomains="false">localhost</domain>
        <trust-anchors>
            <certificates src="user"/>
            <certificates src="system"/>
        </trust-anchors>
    </domain-config>
</network-security-config>
```

#### 4.2 在AndroidManifest.xml中引用
```xml
<application
    android:networkSecurityConfig="@xml/network_security_config"
    ... >
</application>
```

#### 4.3 OkHttp配置（开发环境）
```kotlin
@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {
    
    @Provides
    @Singleton
    fun provideOkHttpClient(): OkHttpClient {
        val builder = OkHttpClient.Builder()
        
        if (BuildConfig.DEBUG) {
            // 开发环境：信任所有证书（仅用于自签名证书测试）
            builder.trustAllCertificates()
        }
        
        return builder
            .connectTimeout(30, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .writeTimeout(30, TimeUnit.SECONDS)
            .build()
    }
}

// 仅开发环境使用的扩展函数
private fun OkHttpClient.Builder.trustAllCertificates(): OkHttpClient.Builder {
    val trustAllCerts = arrayOf<TrustManager>(object : X509TrustManager {
        override fun checkClientTrusted(chain: Array<X509Certificate>, authType: String) {}
        override fun checkServerTrusted(chain: Array<X509Certificate>, authType: String) {}
        override fun getAcceptedIssuers(): Array<X509Certificate> = arrayOf()
    })
    
    val sslContext = SSLContext.getInstance("SSL")
    sslContext.init(null, trustAllCerts, SecureRandom())
    
    return this
        .sslSocketFactory(sslContext.socketFactory, trustAllCerts[0] as X509TrustManager)
        .hostnameVerifier { _, _ -> true }
}
```

## 阶段二：公网部署（Let's Encrypt证书）

### 1. 服务器环境准备

确保你的公司服务器满足以下条件：
- 有公网IP和域名（或可以配置子域名）
- 开放80和443端口
- 已安装Docker或直接运行Java环境

### 2. 使用Certbot获取Let's Encrypt证书

#### 2.1 安装Certbot
```bash
# CentOS/RHEL
sudo yum install certbot

# Ubuntu/Debian
sudo apt-get install certbot

# 或使用Docker
docker pull certbot/certbot
```

#### 2.2 获取证书
```bash
# 如果有域名
sudo certbot certonly --standalone -d your-domain.com

# 如果只有IP，可以使用DNS验证（需要域名控制权）
sudo certbot certonly --manual --preferred-challenges dns -d your-domain.com
```

#### 2.3 转换证书格式
```bash
# Let's Encrypt证书位置: /etc/letsencrypt/live/your-domain.com/
# 转换为PKCS12格式
sudo openssl pkcs12 -export \
    -in /etc/letsencrypt/live/your-domain.com/fullchain.pem \
    -inkey /etc/letsencrypt/live/your-domain.com/privkey.pem \
    -out /path/to/your/app/keystore.p12 \
    -name tomcat
```

### 3. Spring Boot生产环境配置

在 `server/src/main/resources/application-prod.yml`：

```yaml
server:
  port: 8443
  ssl:
    enabled: true
    key-store: file:/path/to/keystore.p12
    key-store-password: ${SSL_KEYSTORE_PASSWORD}
    key-store-type: PKCS12
    key-alias: tomcat
    protocol: TLS
    enabled-protocols: TLSv1.2,TLSv1.3

# 环境变量
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/location_tracker
    username: ${DB_USERNAME}
    password: ${DB_PASSWORD}
```

### 4. 证书自动续期

Let's Encrypt证书有效期90天，需要自动续期：

```bash
# 添加到crontab
sudo crontab -e

# 每月1号凌晨2点检查并续期证书
0 2 1 * * /usr/bin/certbot renew --quiet && systemctl restart your-app-service
```

## 部署脚本

### 开发环境启动脚本
```bash
#!/bin/bash
# scripts/start-dev.sh

echo "启动开发环境..."

# 生成自签名证书（如果不存在）
if [ ! -f "ssl/keystore.p12" ]; then
    echo "生成自签名证书..."
    mkdir -p ssl
    cd ssl
    openssl genrsa -out server.key 2048
    openssl req -new -key server.key -out server.csr -subj "/CN=localhost"
    openssl x509 -req -days 365 -in server.csr -signkey server.key -out server.crt
    openssl pkcs12 -export -in server.crt -inkey server.key -out keystore.p12 -name tomcat -passout pass:changeit
    cd ..
fi

# 启动服务
cd server
./gradlew bootRun --args='--spring.profiles.active=dev'
```

### 生产环境部署脚本
```bash
#!/bin/bash
# scripts/deploy-prod.sh

echo "部署到生产环境..."

# 构建应用
cd server
./gradlew build -x test

# 创建部署目录
sudo mkdir -p /opt/location-tracker
sudo cp build/libs/*.jar /opt/location-tracker/app.jar
sudo cp ../ssl/keystore.p12 /opt/location-tracker/

# 创建systemd服务
sudo tee /etc/systemd/system/location-tracker.service > /dev/null <<EOF
[Unit]
Description=Location Tracker Service
After=network.target

[Service]
Type=simple
User=app-user
WorkingDirectory=/opt/location-tracker
ExecStart=/usr/bin/java -jar app.jar --spring.profiles.active=prod
Restart=always
RestartSec=10

Environment=SSL_KEYSTORE_PASSWORD=your-password
Environment=DB_USERNAME=your-db-user
Environment=DB_PASSWORD=your-db-password

[Install]
WantedBy=multi-user.target
EOF

# 启动服务
sudo systemctl daemon-reload
sudo systemctl enable location-tracker
sudo systemctl start location-tracker
```

## 测试验证

### 1. 证书验证
```bash
# 检查证书信息
openssl x509 -in ssl/server.crt -text -noout

# 测试HTTPS连接
curl -k https://localhost:8443/api/health
```

### 2. Android客户端测试
- 确保手机和开发机在同一WiFi网络
- 使用开发机的IP地址作为服务器地址
- 检查网络日志确认HTTPS连接成功

## 常见问题

### 1. 自签名证书警告
- **问题**：浏览器显示"不安全"警告
- **解决**：这是正常的，点击"高级"→"继续访问"

### 2. Android网络安全限制
- **问题**：Android 9+默认禁止HTTP
- **解决**：使用network_security_config.xml配置

### 3. 证书CN不匹配
- **问题**：证书CN与访问域名不匹配
- **解决**：重新生成证书时确保CN正确

### 4. 端口访问问题
- **问题**：无法访问8443端口
- **解决**：检查防火墙设置，开放对应端口

这个HTTPS配置方案如何？可以让你从本地开发平滑过渡到公网部署，而且证书配置相对简单。有什么不清楚的地方吗？
