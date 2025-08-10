# Git 工作区域详解

## 📚 Git的四个核心区域

Git管理代码时，文件在以下四个区域之间流转：

```
┌─────────────────┐    ┌─────────────────┐    ┌─────────────────┐    ┌─────────────────┐
│   Working Dir   │───▶│  Staging Area   │───▶│ Local Repository│───▶│Remote Repository│
│   (工作目录)     │    │   (暂存区)      │    │   (本地仓库)     │    │   (远程仓库)     │
└─────────────────┘    └─────────────────┘    └─────────────────┘    └─────────────────┘
        │                       │                       │                       │
        │                       │                       │                       │
   实际编辑文件              git add               git commit             git push
```

---

## 🗂️ **1. 工作目录 (Working Directory)**

### 定义
- **位置**: 您实际编辑文件的地方
- **内容**: 当前检出分支的文件副本
- **状态**: 可以被修改、删除、新增

### 文件状态
```bash
# 查看工作目录状态
git status

# 可能的状态
- Untracked files     # 新文件，Git还不知道
- Modified files      # 已修改的文件
- Deleted files       # 已删除的文件
```

### 实际例子 (seeker项目)
```bash
# 当您在VS Code中编辑代码时
c:\UGit\seeker\server\src\main\java\com\seeker\LocationController.java

# 这个文件就在工作目录中
# 修改后，Git会检测到变化
```

---

## 📋 **2. 暂存区 (Staging Area / Index)**

### 定义
- **位置**: `.git/index` 文件
- **作用**: 准备下次提交的文件快照
- **特点**: 介于工作目录和本地仓库之间的缓冲区

### 核心概念
```bash
# 添加文件到暂存区
git add filename.java           # 添加特定文件
git add .                       # 添加所有修改的文件
git add *.java                  # 添加所有Java文件

# 查看暂存区状态
git status                      # 查看哪些文件在暂存区
git diff --cached               # 查看暂存区与上次提交的差异
```

### 暂存区的作用
1. **精确控制提交内容**: 只提交您想要的修改
2. **分批提交**: 可以将大的修改分成多个小提交
3. **撤销修改**: 可以从暂存区移除不需要的修改

### 实际例子
```bash
# 场景：同时修改了多个文件
git status
# Modified: LocationController.java
# Modified: LocationService.java  
# Modified: README.md

# 只想提交业务代码，不提交文档
git add LocationController.java
git add LocationService.java

# 现在暂存区只有业务代码
git status
# Changes to be committed:
#   modified: LocationController.java
#   modified: LocationService.java
# Changes not staged for commit:
#   modified: README.md
```

---

## 🏛️ **3. 本地仓库 (Local Repository)**

### 定义
- **位置**: `.git` 目录
- **内容**: 所有提交的历史记录
- **特点**: 完整的项目历史，可以离线工作

### 核心组成
```bash
.git/
├── objects/        # 存储所有的Git对象（提交、树、文件）
├── refs/           # 存储分支和标签引用
├── HEAD            # 指向当前分支
├── index           # 暂存区
└── config          # 仓库配置
```

### 提交操作
```bash
# 从暂存区创建提交
git commit -m "提交信息"

# 查看提交历史
git log
git log --oneline       # 简洁格式
git log --graph         # 图形化显示

# 查看特定提交
git show commit-hash
```

### 本地仓库的优势
1. **离线工作**: 不需要网络也能查看历史、创建分支
2. **快速操作**: 本地操作速度快
3. **完整备份**: 包含完整的项目历史

---

## 🌐 **4. 远程仓库 (Remote Repository)**

### 定义
- **位置**: GitHub、GitLab等远程服务器
- **作用**: 团队协作、备份、部署
- **特点**: 多人共享的中央仓库

### 远程操作
```bash
# 查看远程仓库
git remote -v

# 推送到远程仓库
git push origin main

# 从远程仓库拉取
git pull origin main

# 获取远程更新（不合并）
git fetch origin
```

### 我们的seeker项目
```bash
# 远程仓库地址
origin  https://github.com/EigenChen/seeker.git

# 推送本地提交到GitHub
git push origin main
```

---

## 🔄 **文件在各区域间的流转**

### 完整的工作流程
```bash
# 1. 在工作目录中修改文件
echo "新功能" >> LocationController.java

# 2. 查看工作目录状态
git status
# Modified: LocationController.java

# 3. 添加到暂存区
git add LocationController.java

# 4. 查看暂存区状态
git status
# Changes to be committed:
#   modified: LocationController.java

# 5. 提交到本地仓库
git commit -m "feat: 添加新功能"

# 6. 推送到远程仓库
git push origin main
```

### 各种撤销操作
```bash
# 撤销工作目录的修改
git checkout -- filename       # 恢复到上次提交状态
git restore filename            # Git 2.23+新命令

# 撤销暂存区的修改
git reset HEAD filename         # 从暂存区移除，保留工作目录修改
git restore --staged filename   # Git 2.23+新命令

# 撤销本地仓库的提交
git reset HEAD~1                # 撤销最后一次提交，保留修改
git reset --hard HEAD~1         # 撤销最后一次提交，丢弃修改
```

---

## 📊 **seeker项目的区域示例**

### 当前项目状态
```bash
# 工作目录
c:\UGit\seeker\
├── server/                 # Spring Boot后端
├── android-client/         # Android客户端
├── docs/                   # 文档
└── .rules/                 # 开发规范

# 暂存区（刚才的操作）
git add docs/development-log.md

# 本地仓库（已有的提交）
- feat: 初始化位置追踪项目
- cleanup: 删除根目录中重复的.mdc文件
- docs: 添加Git和Maven常用命令参考文档

# 远程仓库
https://github.com/EigenChen/seeker.git
```

### 日常开发流程
```bash
# 1. 拉取最新代码
git pull origin main

# 2. 开发新功能（工作目录）
# 修改 LocationController.java

# 3. 查看修改
git status
git diff

# 4. 添加到暂存区
git add LocationController.java

# 5. 提交到本地仓库
git commit -m "feat: 优化位置上传接口"

# 6. 推送到远程仓库
git push origin main
```

---

## 🎯 **最佳实践建议**

### 1. **暂存区的妙用**
```bash
# 分批提交相关修改
git add database/           # 只添加数据库相关修改
git commit -m "feat: 数据库优化"

git add api/               # 再添加API相关修改
git commit -m "feat: API接口改进"
```

### 2. **工作目录管理**
```bash
# 定期清理工作目录
git clean -f -d            # 删除未跟踪的文件和目录
git checkout .              # 撤销所有未暂存的修改
```

### 3. **提交粒度控制**
```bash
# 好的提交：功能单一，描述清晰
git commit -m "feat: 添加GPS权限检查"

# 避免：一次提交包含太多不相关的修改
git commit -m "修改了很多东西"
```

### 4. **远程仓库同步**
```bash
# 推送前先拉取
git pull origin main
git push origin main

# 或者使用变基方式
git pull --rebase origin main
git push origin main
```

---

## 🔍 **常用查看命令**

### 查看各区域状态
```bash
# 工作目录状态
git status

# 工作目录与暂存区差异
git diff

# 暂存区与最后提交差异
git diff --cached

# 工作目录与最后提交差异
git diff HEAD

# 查看提交历史
git log --oneline --graph --all
```

### 查看文件在各区域的版本
```bash
# 工作目录版本
cat filename.java

# 暂存区版本
git show :filename.java

# 最后提交版本
git show HEAD:filename.java

# 特定提交版本
git show commit-hash:filename.java
```

---

理解这四个区域是掌握Git的关键！每个区域都有其特定的作用，合理使用可以让您的版本控制更加精确和高效。

对于我们的seeker项目，这个理解特别重要，因为我们有多个模块（server、android-client、docs），需要精确控制哪些修改一起提交。 🎯
