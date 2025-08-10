# Maven 常用命令参考

## 📚 基础命令

### 项目生命周期
```bash
# 清理编译产物
mvn clean

# 编译源代码
mvn compile

# 编译测试代码
mvn test-compile

# 运行单元测试
mvn test

# 打包项目
mvn package

# 安装到本地仓库
mvn install

# 部署到远程仓库
mvn deploy
```

### 组合命令（常用）
```bash
# 清理并编译
mvn clean compile

# 清理并测试
mvn clean test

# 清理并打包
mvn clean package

# 清理、测试并打包
mvn clean test package

# 清理并安装
mvn clean install

# 跳过测试的打包
mvn clean package -DskipTests

# 跳过测试的安装
mvn clean install -DskipTests
```

## 🚀 Spring Boot 专用命令

### 运行应用
```bash
# 启动Spring Boot应用
mvn spring-boot:run

# 指定配置文件启动
mvn spring-boot:run -Dspring-boot.run.profiles=dev

# 指定JVM参数启动
mvn spring-boot:run -Dspring-boot.run.jvmArguments="-Xmx512m"

# 后台运行
mvn spring-boot:run &
```

### 打包相关
```bash
# 打包成可执行JAR
mvn clean package spring-boot:repackage

# 构建Docker镜像（需要插件配置）
mvn spring-boot:build-image
```

## 🔍 依赖管理

### 查看依赖
```bash
# 显示依赖树
mvn dependency:tree

# 显示项目依赖
mvn dependency:list

# 分析依赖
mvn dependency:analyze

# 解决依赖
mvn dependency:resolve

# 下载源码
mvn dependency:sources

# 下载文档
mvn dependency:resolve -Dclassifier=javadoc
```

### 版本管理
```bash
# 检查依赖更新
mvn versions:display-dependency-updates

# 检查插件更新
mvn versions:display-plugin-updates

# 更新版本号
mvn versions:set -DnewVersion=1.1.0

# 确认版本更新
mvn versions:commit

# 回滚版本更新
mvn versions:revert
```

## 📋 项目信息

### 查看项目信息
```bash
# 显示项目基本信息
mvn help:describe -Dplugin=help

# 显示有效POM
mvn help:effective-pom

# 显示有效设置
mvn help:effective-settings

# 显示项目属性
mvn help:system
```

### 生成报告
```bash
# 生成项目站点
mvn site

# 生成测试报告
mvn surefire-report:report

# 生成代码覆盖率报告（需要JaCoCo插件）
mvn jacoco:report
```

## 🔧 插件操作

### 常用插件命令
```bash
# 编译器插件
mvn compiler:compile
mvn compiler:testCompile

# Surefire测试插件
mvn surefire:test

# 资源插件
mvn resources:resources
mvn resources:testResources

# JAR插件
mvn jar:jar

# 源码插件
mvn source:jar

# JavaDoc插件
mvn javadoc:javadoc
```

### 代码质量检查
```bash
# Checkstyle代码风格检查
mvn checkstyle:check

# PMD代码质量检查
mvn pmd:check

# SpotBugs错误检查
mvn spotbugs:check

# 执行所有质量检查
mvn clean compile checkstyle:check pmd:check spotbugs:check
```

## 🏗️ 构建配置

### Profile配置
```bash
# 查看可用Profile
mvn help:all-profiles

# 激活特定Profile
mvn clean package -P产品环境

# 激活多个Profile
mvn clean package -P环境1,环境2

# 禁用Profile
mvn clean package -P!测试环境
```

### 属性设置
```bash
# 设置系统属性
mvn clean package -Dmaven.test.skip=true

# 设置JVM内存
mvn clean package -Dmaven.compiler.fork=true -Dmaven.compiler.maxmem=512m

# 离线模式
mvn clean package -o

# 强制更新依赖
mvn clean package -U
```

## 🚨 故障排除

### 调试和诊断
```bash
# 详细输出
mvn clean package -X

# 显示调试信息
mvn clean package -e

# 静默模式
mvn clean package -q

# 检查插件配置
mvn help:describe -Dplugin=spring-boot -Ddetail

# 显示依赖冲突
mvn dependency:tree -Dverbose
```

### 清理和重置
```bash
# 清理本地仓库损坏的文件
mvn dependency:purge-local-repository

# 强制重新下载依赖
mvn clean install -U

# 删除target目录
mvn clean

# 重置项目
rm -rf target/
mvn clean install
```

## 📋 seeker项目专用命令

### 开发环境启动
```bash
# 启动开发环境服务器
cd server
mvn spring-boot:run -Dspring-boot.run.profiles=dev

# 后台启动
mvn spring-boot:run -Dspring-boot.run.profiles=dev &

# 指定端口启动
mvn spring-boot:run -Dspring-boot.run.profiles=dev -Dserver.port=8443
```

### 生产环境部署
```bash
# 构建生产版本
mvn clean package -Pprod -DskipTests

# 运行生产JAR包
java -jar target/location-tracker-server-1.0.0.jar --spring.profiles.active=prod
```

### 数据库操作
```bash
# Flyway数据库迁移
mvn flyway:migrate

# 查看迁移状态
mvn flyway:info

# 清理数据库
mvn flyway:clean

# 验证迁移
mvn flyway:validate
```

### 测试相关
```bash
# 运行单元测试
mvn test

# 运行特定测试类
mvn test -Dtest=LocationServiceTest

# 运行特定测试方法
mvn test -Dtest=LocationServiceTest#testUploadLocation

# 生成测试报告
mvn test jacoco:report
```

### 代码质量检查
```bash
# 运行所有检查
mvn clean compile checkstyle:check

# 生成质量报告
mvn clean test site

# SpotBugs安全检查
mvn spotbugs:check
```

## 🔄 Maven Wrapper

我们的项目使用Maven Wrapper，推荐使用：

### Windows
```bash
# 使用Maven Wrapper
.\mvnw.cmd clean package

# 启动应用
.\mvnw.cmd spring-boot:run -Dspring-boot.run.profiles=dev
```

### Linux/macOS
```bash
# 使用Maven Wrapper
./mvnw clean package

# 启动应用
./mvnw spring-boot:run -Dspring-boot.run.profiles=dev
```

## 📚 最佳实践

### 日常开发流程
```bash
# 1. 拉取最新代码后重新构建
mvn clean install

# 2. 开发过程中快速测试
mvn clean test

# 3. 提交前完整验证
mvn clean package

# 4. 启动开发服务器
mvn spring-boot:run -Dspring-boot.run.profiles=dev
```

### 性能优化
```bash
# 并行构建
mvn clean package -T 4

# 离线模式（提高速度）
mvn clean package -o

# 跳过不必要的步骤
mvn clean package -DskipTests -Dcheckstyle.skip -Dpmd.skip
```
