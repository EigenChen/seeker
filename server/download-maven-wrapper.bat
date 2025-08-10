@echo off

echo 正在下载Maven wrapper JAR文件...

REM 创建.mvn\wrapper目录
if not exist ".mvn\wrapper" mkdir ".mvn\wrapper"

REM 下载maven-wrapper.jar
powershell -Command "Invoke-WebRequest -Uri 'https://repo.maven.apache.org/maven2/org/apache/maven/wrapper/maven-wrapper/3.2.0/maven-wrapper-3.2.0.jar' -OutFile '.mvn\wrapper\maven-wrapper.jar'"

echo Maven wrapper下载完成!
echo 现在可以使用 mvnw.cmd 命令了

pause
