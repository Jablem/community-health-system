# 社区康养管理系统

一个基于 Spring Boot 3 和 Vue 3 的前后端分离社区健康管理系统。

## 技术栈

- **后端**: Spring Boot 3.2.3 + Java 17 + MySQL
- **前端**: Vue 3 + Vite + Vant UI

## 项目结构

```
community-health-system/
├── backend/              # Spring Boot 后端
├ frontend/              # Vue 3 前端
└── README.md
```

## 快速开始

### 后端启动

```bash
cd backend
./mvnw spring-boot:run
```

### 前端启动

```bash
cd frontend
npm install
npm run dev
```

## 数据库配置

- 数据库名: `community_health`
- 配置文件: `backend/src/main/resources/application.properties`
