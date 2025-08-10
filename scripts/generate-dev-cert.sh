#!/bin/bash

# 生成开发环境自签名证书脚本
# 在 Windows 上请使用 Git Bash 或 WSL 运行

set -e

echo "正在生成开发环境自签名HTTPS证书..."

# 证书目录
SSL_DIR="server/src/main/resources/ssl"
mkdir -p "$SSL_DIR"

# 获取本机IP地址（用于证书CN）
if command -v ipconfig &> /dev/null; then
    # Windows
    LOCAL_IP=$(ipconfig | grep -A 5 "无线局域网适配器" | grep "IPv4" | awk '{print $NF}' | head -1)
elif command -v ifconfig &> /dev/null; then
    # Linux/macOS
    LOCAL_IP=$(ifconfig | grep -E "inet.*broadcast" | awk '{print $2}' | head -1)
else
    LOCAL_IP="192.168.1.100"
    echo "警告: 无法自动获取IP地址，使用默认值 $LOCAL_IP"
fi

echo "检测到本机IP: $LOCAL_IP"

# 证书参数
KEYSTORE_PASSWORD="changeit"
VALIDITY_DAYS=365

# 创建证书配置文件
cat > "$SSL_DIR/cert.conf" << EOF
[req]
distinguished_name = req_distinguished_name
req_extensions = v3_req
prompt = no

[req_distinguished_name]
C=CN
ST=Beijing
L=Beijing
O=Seeker
OU=Development
CN=$LOCAL_IP
emailAddress=dev@seeker.com

[v3_req]
keyUsage = keyEncipherment, dataEncipherment
extendedKeyUsage = serverAuth
subjectAltName = @alt_names

[alt_names]
DNS.1 = localhost
DNS.2 = *.localhost
IP.1 = 127.0.0.1
IP.2 = $LOCAL_IP
EOF

echo "正在生成私钥..."
openssl genrsa -out "$SSL_DIR/server.key" 2048

echo "正在生成证书签名请求..."
openssl req -new -key "$SSL_DIR/server.key" -out "$SSL_DIR/server.csr" -config "$SSL_DIR/cert.conf"

echo "正在生成自签名证书..."
openssl x509 -req -days $VALIDITY_DAYS -in "$SSL_DIR/server.csr" -signkey "$SSL_DIR/server.key" \
    -out "$SSL_DIR/server.crt" -extensions v3_req -extfile "$SSL_DIR/cert.conf"

echo "正在生成PKCS12密钥库..."
openssl pkcs12 -export -in "$SSL_DIR/server.crt" -inkey "$SSL_DIR/server.key" \
    -out "$SSL_DIR/keystore.p12" -name tomcat -passout pass:$KEYSTORE_PASSWORD

echo "正在验证证书..."
openssl x509 -in "$SSL_DIR/server.crt" -text -noout | grep -E "(Subject:|DNS:|IP Address:)"

echo ""
echo "✅ 证书生成成功!"
echo "证书位置: $SSL_DIR/keystore.p12"
echo "证书密码: $KEYSTORE_PASSWORD"
echo "证书有效期: $VALIDITY_DAYS 天"
echo "支持的地址:"
echo "  - https://localhost:8443"
echo "  - https://$LOCAL_IP:8443"
echo ""
echo "下一步:"
echo "1. 启动服务器: cd server && ./mvnw spring-boot:run -Dspring-boot.run.profiles=dev"
echo "2. 测试连接: curl -k https://localhost:8443/api/health"
echo "3. 浏览器访问: https://localhost:8443/api/health (忽略安全警告)"

# 清理临时文件
rm -f "$SSL_DIR/server.key" "$SSL_DIR/server.csr" "$SSL_DIR/cert.conf"

echo ""
echo "注意: 这是开发环境证书，浏览器会显示安全警告，这是正常的。"
echo "Android客户端需要配置信任自签名证书。"
