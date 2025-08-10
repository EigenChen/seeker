@echo off
REM Windows批处理脚本 - 生成开发环境自签名证书

echo 正在生成开发环境自签名HTTPS证书...

REM 检查OpenSSL是否安装
where openssl >nul 2>nul
if %ERRORLEVEL% NEQ 0 (
    echo 错误: 未找到 OpenSSL
    echo 请安装 OpenSSL 或使用 Git Bash 运行 generate-dev-cert.sh
    echo 下载地址: https://slproweb.com/products/Win32OpenSSL.html
    pause
    exit /b 1
)

REM 证书目录
set SSL_DIR=server\src\main\resources\ssl
if not exist "%SSL_DIR%" mkdir "%SSL_DIR%"

REM 获取本机IP地址
for /f "tokens=2 delims=:" %%a in ('ipconfig ^| findstr "IPv4"') do (
    set LOCAL_IP=%%a
    goto :break
)
:break
set LOCAL_IP=%LOCAL_IP: =%
if "%LOCAL_IP%"=="" set LOCAL_IP=192.168.1.100

echo 检测到本机IP: %LOCAL_IP%

REM 证书参数
set KEYSTORE_PASSWORD=changeit
set VALIDITY_DAYS=365

REM 创建证书配置文件
(
echo [req]
echo distinguished_name = req_distinguished_name
echo req_extensions = v3_req
echo prompt = no
echo.
echo [req_distinguished_name]
echo C=CN
echo ST=Beijing
echo L=Beijing
echo O=Seeker
echo OU=Development
echo CN=%LOCAL_IP%
echo emailAddress=dev@seeker.com
echo.
echo [v3_req]
echo keyUsage = keyEncipherment, dataEncipherment
echo extendedKeyUsage = serverAuth
echo subjectAltName = @alt_names
echo.
echo [alt_names]
echo DNS.1 = localhost
echo DNS.2 = *.localhost
echo IP.1 = 127.0.0.1
echo IP.2 = %LOCAL_IP%
) > "%SSL_DIR%\cert.conf"

echo 正在生成私钥...
openssl genrsa -out "%SSL_DIR%\server.key" 2048

echo 正在生成证书签名请求...
openssl req -new -key "%SSL_DIR%\server.key" -out "%SSL_DIR%\server.csr" -config "%SSL_DIR%\cert.conf"

echo 正在生成自签名证书...
openssl x509 -req -days %VALIDITY_DAYS% -in "%SSL_DIR%\server.csr" -signkey "%SSL_DIR%\server.key" -out "%SSL_DIR%\server.crt" -extensions v3_req -extfile "%SSL_DIR%\cert.conf"

echo 正在生成PKCS12密钥库...
openssl pkcs12 -export -in "%SSL_DIR%\server.crt" -inkey "%SSL_DIR%\server.key" -out "%SSL_DIR%\keystore.p12" -name tomcat -passout pass:%KEYSTORE_PASSWORD%

echo.
echo ✅ 证书生成成功!
echo 证书位置: %SSL_DIR%\keystore.p12
echo 证书密码: %KEYSTORE_PASSWORD%
echo 证书有效期: %VALIDITY_DAYS% 天
echo 支持的地址:
echo   - https://localhost:8443
echo   - https://%LOCAL_IP%:8443
echo.
echo 下一步:
echo 1. 启动服务器: cd server ^&^& mvnw.cmd spring-boot:run -Dspring-boot.run.profiles=dev
echo 2. 测试连接: curl -k https://localhost:8443/api/health
echo 3. 浏览器访问: https://localhost:8443/api/health (忽略安全警告)

REM 清理临时文件
del /f /q "%SSL_DIR%\server.key" "%SSL_DIR%\server.csr" "%SSL_DIR%\cert.conf" 2>nul

echo.
echo 注意: 这是开发环境证书，浏览器会显示安全警告，这是正常的。
echo Android客户端需要配置信任自签名证书。

pause
