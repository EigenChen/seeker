#!/bin/bash

echo "正在下载Maven wrapper JAR文件..."

# 创建.mvn/wrapper目录
mkdir -p .mvn/wrapper

# 下载maven-wrapper.jar
curl -o .mvn/wrapper/maven-wrapper.jar https://repo.maven.apache.org/maven2/org/apache/maven/wrapper/maven-wrapper/3.2.0/maven-wrapper-3.2.0.jar

echo "Maven wrapper下载完成!"
echo "现在可以使用 ./mvnw 命令了"
