# 社区健康管理系统 - 启动说明

## 项目概述

这是一个前后端分离的社区康养管理系统：
- **后端**: Spring Boot 3.2.3 + Java 17 + MySQL
- **前端**: Vue 3 + Vite + Vant UI

---

## ⚠️ 发现的问题

### 1. Java 版本不匹配（严重）
- **项目要求**: Java 17
- **当前系统**: Java 11.0.30
- **影响**: 后端无法编译和运行

### 2. Maven 未安装
- **项目需要**: Maven 来构建 Spring Boot 项目
- **当前状态**: 未安装

### 3. MySQL 数据库需要配置
- **数据库名**: `community_health`
- **端口**: `1919`（非标准端口）
- **默认密码**: `114514`（建议修改）

---

## 环境准备

### 1. 安装 Java 17

```bash
# Ubuntu/Debian
sudo apt update
sudo apt install openjdk-17-jdk

# 验证
java -version  # 应显示 17.x.x
```

### 2. 安装 Maven

```bash
# Ubuntu/Debian
sudo apt install maven

# 验证
mvn -version
```

### 3. 安装并配置 MySQL

```bash
# 安装 MySQL
sudo apt install mysql-server

# 启动 MySQL
sudo systemctl start mysql
sudo systemctl enable mysql

# 创建数据库（使用 1919 端口或修改配置）
sudo mysql -e "CREATE DATABASE IF NOT EXISTS community_health CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;"
```

> **注意**: 项目配置使用 1919 端口，如果 MySQL 使用默认 3306 端口，需要修改 `backend/src/main/resources/application.properties`

---

## 启动步骤

### 后端启动

```bash
cd /home/admin/Desktop/community-health-system/backend

# 方式 1: 使用 Maven Wrapper（推荐）
./mvnw spring-boot:run

# 方式 2: 使用 Maven
mvn spring-boot:run

# 方式 3: 打包后运行
./mvnw clean package
java -jar target/backend-0.0.1-SNAPSHOT.jar
```

后端启动后访问：http://localhost:8080

### 前端启动

```bash
cd /home/admin/Desktop/community-health-system/frontend

# 安装依赖（首次运行需要）
npm install

# 启动开发服务器
npm run dev
```

前端启动后访问：http://localhost:5173

---

## 配置文件说明

### 后端配置 (application.properties)

```properties
# 数据库配置
spring.datasource.url=jdbc:mysql://localhost:1919/community_health
spring.datasource.username=root
spring.datasource.password=114514

# JPA 配置
spring.jpa.hibernate.ddl-auto=update
```

### 前端配置 (vite.config.js)

```javascript
server: {
  port: 5173,
  proxy: {
    '/api': {
      target: 'http://localhost:8080',  // 后端地址
      changeOrigin: true
    }
  }
}
```

---

## API 接口

| 方法 | 路径 | 说明 |
|------|------|------|
| POST | /api/v1/residents | 创建居民档案 |
| GET | /api/v1/residents/{id} | 获取居民详情 |
| POST | /api/v1/records | 创建调理记录 |
| GET | /api/v1/residents/{id}/records | 获取居民调理记录 |

---

## 快速检查清单

启动前请确认：

- [ ] Java 17 已安装
- [ ] Maven 已安装
- [ ] MySQL 已安装并运行
- [ ] 数据库 `community_health` 已创建
- [ ] 数据库端口与配置一致（1919 或 3306）
- [ ] 前端依赖已安装 (`npm install`)

---

## 常见问题

### 1. Java 版本错误
```
Unsupported class file major version 61
```
解决：安装 Java 17

### 2. 数据库连接失败
```
Communications link failure
```
解决：检查 MySQL 是否运行，端口是否正确

### 3. 前端 API 请求失败
```
Network Error
```
解决：确保后端已启动，检查 vite.config.js 中的代理配置

---

## 项目结构

```
community-health-system/
├── backend/              # Spring Boot 后端
│   ├── src/
│   │   ├── main/java/   # Java 源代码
│   │   └── resources/   # 配置文件
│   ├── pom.xml          # Maven 配置
│   └── application.properties
├── frontend/            # Vue 3 前端
│   ├── src/
│   │   ├── api/        # API 调用
│   │   ├── components/ # 组件
│   │   ├── views/      # 页面
│   │   └── stores/     # 状态管理
│   ├── package.json
│   └── vite.config.js
└── .git/
```

---

**文档生成时间**: 2026-04-02  
**项目版本**: 0.0.1-SNAPSHOT
