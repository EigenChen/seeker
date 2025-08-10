# Git 常用命令参考

## 📚 基础操作

### 初始化和配置
```bash
# 初始化仓库
git init

# 配置用户信息
git config --global user.name "您的用户名"
git config --global user.email "您的邮箱"

# 查看配置
git config --list
```

### 远程仓库操作
```bash
# 添加远程仓库
git remote add origin https://github.com/用户名/仓库名.git

# 查看远程仓库
git remote -v

# 修改远程仓库URL
git remote set-url origin https://github.com/新用户名/新仓库名.git

# 删除远程仓库
git remote remove origin
```

## 📝 日常工作流

### 查看状态和历史
```bash
# 查看工作区状态
git status

# 查看提交历史
git log

# 简洁的提交历史
git log --oneline

# 图形化显示分支
git log --graph --oneline --all
```

### 添加和提交
```bash
# 添加文件到暂存区
git add 文件名
git add .                # 添加所有修改的文件
git add *.java          # 添加所有Java文件

# 提交到本地仓库
git commit -m "提交信息"

# 添加并提交（跳过暂存区）
git commit -am "提交信息"

# 修改最后一次提交
git commit --amend -m "新的提交信息"
```

### 推送和拉取
```bash
# 推送到远程仓库
git push origin main

# 首次推送并设置上游分支
git push -u origin main

# 拉取远程更新
git pull origin main

# 获取远程更新但不合并
git fetch origin
```

## 🌿 分支管理

### 分支基础操作
```bash
# 查看分支
git branch              # 查看本地分支
git branch -r           # 查看远程分支
git branch -a           # 查看所有分支

# 创建分支
git branch 分支名

# 切换分支
git checkout 分支名
git switch 分支名        # Git 2.23+新命令

# 创建并切换分支
git checkout -b 分支名
git switch -c 分支名     # Git 2.23+新命令

# 删除分支
git branch -d 分支名     # 安全删除
git branch -D 分支名     # 强制删除
```

### 分支合并
```bash
# 合并分支到当前分支
git merge 分支名

# 变基合并（保持线性历史）
git rebase 分支名

# 取消合并
git merge --abort
```

## 🔄 撤销和恢复

### 撤销修改
```bash
# 撤销工作区的修改
git checkout -- 文件名
git restore 文件名       # Git 2.23+新命令

# 撤销暂存区的修改
git reset HEAD 文件名
git restore --staged 文件名  # Git 2.23+新命令

# 撤销最后一次提交（保留修改）
git reset HEAD~1

# 撤销最后一次提交（丢弃修改）
git reset --hard HEAD~1
```

### 查看差异
```bash
# 查看工作区与暂存区的差异
git diff

# 查看暂存区与最后提交的差异
git diff --cached

# 查看两次提交之间的差异
git diff commit1 commit2
```

## 🏷️ 标签管理

```bash
# 创建标签
git tag v1.0.0

# 创建带注释的标签
git tag -a v1.0.0 -m "版本1.0.0发布"

# 查看标签
git tag

# 推送标签到远程
git push origin v1.0.0
git push origin --tags    # 推送所有标签

# 删除标签
git tag -d v1.0.0         # 删除本地标签
git push origin :refs/tags/v1.0.0  # 删除远程标签
```

## 🔍 高级操作

### 贮藏（Stash）
```bash
# 贮藏当前修改
git stash

# 贮藏时添加描述
git stash save "描述信息"

# 查看贮藏列表
git stash list

# 应用最新贮藏
git stash apply

# 应用并删除最新贮藏
git stash pop

# 删除贮藏
git stash drop stash@{0}

# 清空所有贮藏
git stash clear
```

### 忽略文件
```bash
# 查看忽略规则
cat .gitignore

# 取消跟踪已提交的文件
git rm --cached 文件名

# 强制添加被忽略的文件
git add -f 文件名
```

## 🚨 紧急情况处理

### 冲突解决
```bash
# 查看冲突文件
git status

# 解决冲突后继续合并
git add 冲突文件
git commit

# 取消合并
git merge --abort
```

### 恢复误删的提交
```bash
# 查看所有操作历史
git reflog

# 恢复到指定提交
git reset --hard commit-hash
```

## 📋 seeker项目常用命令

### 日常开发流程
```bash
# 1. 拉取最新代码
git pull origin main

# 2. 创建功能分支
git checkout -b feature/新功能名

# 3. 开发完成后提交
git add .
git commit -m "feat: 添加新功能

- 功能描述1
- 功能描述2"

# 4. 推送分支
git push origin feature/新功能名

# 5. 合并到主分支
git checkout main
git merge feature/新功能名
git push origin main

# 6. 删除功能分支
git branch -d feature/新功能名
```

### 版本发布
```bash
# 创建发布标签
git tag -a v1.0.0 -m "位置追踪器 v1.0.0 发布

- Android客户端GPS追踪功能
- Spring Boot服务端API
- HTTPS安全通信"

# 推送标签
git push origin v1.0.0
```
