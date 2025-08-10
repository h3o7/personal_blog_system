<p align="center">
    <img src="https://img.shields.io/badge/Vue-3.x-green.svg" alt="Vue">
    <img src="https://img.shields.io/badge/Spring%20Boot-2.x-blue.svg" alt="Spring Boot">
    <img src="https://img.shields.io/badge/MyBatis--Plus-3.x-blue.svg" alt="MyBatis-Plus">
    <img src="https://img.shields.io/badge/JDK-17+-green.svg" alt="JDK">
    <img src="https://img.shields.io/badge/License-MIT-blue.svg" alt="License">
    <img src="https://img.shields.io/badge/Author-h3o7-ff69b4.svg" alt="Author">
</p>

<h1 align="center" style="margin: 30px 0 30px; font-weight: bold;">个人博客系统</h1>
<h4 align="center">基于 Vue3 + Spring Boot 前后端分离的简化版个人博客系统</h4>

## ⚡ 项目简介 ⚡

这是一个前后端分离的个人博客系统，采用现代化的技术栈构建。项目结构清晰，功能完整，适合个人博客搭建或作为学习项目使用。

* 前端采用 Vue3 + Vite，提供流畅的用户体验
* 后端基于 Spring Boot + MyBatis-Plus，构建稳定的 API 服务
* 支持文章发布、评论互动、标签管理、文件上传等完整博客功能
* 集成阿里云 OSS 文件存储，支持图片上传
* 使用 JWT 进行身份认证，Redis 缓存提升性能
* 内置 Knife4j API 文档，便于接口调试

## 💥 在线体验 💥

> 如果您部署了项目，可以在这里提供演示地址

## 🌴 技术栈 🌴

### 后端技术栈

| 技术             | 说明              | 版本                |
|----------------|-----------------|-------------------|
| `Spring Boot`  | 基础框架            | 2.x               |
| `MyBatis-Plus` | 数据库框架           | 3.x               |
| `Redis`        | 缓存数据库           | Latest            |
| `MySQL`        | 关系型数据库          | 8.0+              |
| `JWT`          | 身份认证            | Latest            |
| `Knife4j`      | API 文档框架        | Latest            |
| `Hutool`       | Java 工具包        | Latest            |
| `Lombok`       | 代码简化工具          | Latest            |
| `阿里云 OSS`      | 对象存储服务          | Latest            |
| `RabbitMQ`      | 消息队列          | 3.12            |

### 前端技术栈

| 技术       | 说明         | 版本      |
|----------|------------|---------|
| `Vue`    | 前端框架       | 3.x     |
| `Vite`   | 构建工具       | Latest  |
| `JavaScript` | 开发语言   | ES6+    |

## 🐯 项目结构 🐯

```
personal_blog_system/
├── blog-front-end/                 # 前端项目
│   ├── src/
│   │   ├── api/                    # API 接口
│   │   ├── components/             # 通用组件
│   │   ├── views/                  # 页面组件
│   │   └── utils/                  # 工具类
│   └── package.json
│
├── blog-back-end/                  # 后端项目
│   ├── src/main/java/blogsystem/
│   │   ├── controller/             # 控制器层
│   │   ├── service/                # 服务层
│   │   │   └── impl/              # 服务实现
│   │   ├── mapper/                 # 数据访问层
│   │   ├── entity/                 # 实体类
│   │   ├── dto/                    # 数据传输对象
│   │   ├── vo/                     # 视图对象
│   │   ├── config/                 # 配置类
│   │   ├── listener/               # 监听类
│   │   └── utils/                  # 工具类
│   └── pom.xml
└── README.md
```

## 🙊 系统功能 🙊

| 功能模块 | 功能描述                              |
|------|---------------------------------|
| 用户认证 | 用户登录、管理员登录、JWT 身份认证             |
| 文章管理 | 文章发布、编辑、删除、草稿保存、分类筛选、搜索      |
| 评论系统 | 文章评论、回复评论、评论点赞                |
| 标签管理 | 标签创建、编辑、删除、文章标签关联             |
| 广告管理 | 广告位管理、广告类型配置                  |
| 友情链接 | 友链添加、编辑、删除                    |
| 文件上传 | 图片上传、文件管理（阿里云 OSS）            |
| 数据统计 | 后台数据统计、文章浏览量、用户活跃度            |
| 验证码  | 图形验证码防刷                       |

## ✨ 快速开始 ✨

### 环境要求

- JDK 17+
- Node.js 16+
- MySQL 8.0+
- Redis 6.0+
- Maven 3.9+

### 后端启动

```bash
# 1. 克隆项目
git clone https://github.com/h3o7/personal_blog_system.git

# 2. 进入后端目录
cd personal_blog_system/blog-back-end

# 3. 配置数据库和 Redis
# 编辑 src/main/resources/application.yml
# 配置数据源、Redis、阿里云 OSS 等信息

# 4. 安装依赖并启动
./mvnw spring-boot:run

# 或者打包运行
./mvnw clean package -DskipTests
java -jar target/*.jar
```

启动成功后访问：
- API 文档：http://localhost:8080/doc.html
- 后端服务：http://localhost:8080

### 前端启动

```bash
# 1. 进入前端目录
cd personal_blog_system/blog-front-end

# 2. 安装依赖
npm install

# 3. 启动开发服务器
npm run dev

# 4. 构建生产版本
npm run build
```

启动成功后访问：http://localhost:3000

## 📚 API 文档 📚

项目集成了 Knife4j，提供完整的 API 文档：

- 本地文档地址：http://localhost:8080/doc.html
- 支持在线调试和接口测试

主要 API 接口：

| 模块 | 接口 | 说明 |
|------|------|------|
| 认证 | POST /login/admin | 管理员登录 |
| 认证 | POST /login/user | 用户登录 |
| 认证 | GET /login/captcha | 获取验证码 |
| 文章 | GET /article/list | 文章列表 |
| 文章 | POST /article/add | 发布文章 |
| 文章 | PUT /article/like | 文章点赞 |
| 评论 | GET /comment/list | 评论列表 |
| 评论 | POST /comment/add | 发表评论 |
| 上传 | POST /upload/ad | 上传图片 |

## 🌻 配置说明 🌻

### 数据库配置

```yaml
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/blog_system?useUnicode=true&characterEncoding=utf8&serverTimezone=GMT%2B8
    username: your_username
    password: your_password
    driver-class-name: com.mysql.cj.jdbc.Driver
```

### Redis 配置

```yaml
spring:
  redis:
    host: localhost
    port: 6379
    password: your_password
    database: 0
```

### 阿里云 OSS 配置

```yaml
aliyun:
  oss:
    endpoint: your_endpoint
    accessKeyId: your_access_key_id
    accessKeySecret: your_access_key_secret
    bucketName: your_bucket_name
```

## 🎬 交流与支持 🎬

如果您在使用过程中遇到问题或有改进建议，欢迎：

- 提交 Issue：[GitHub Issues](https://github.com/h3o7/personal_blog_system/issues)
- 提交 Pull Request
- 给项目点个 Star ⭐

## 💕 开源协议 💕

本项目基于 MIT 协议开源，您可以自由使用、修改和分发。

## 🎯 后续计划 🎯

- [ ] 支持 Markdown 编辑器
- [ ] 添加文章搜索功能优化
- [ ] 集成第三方登录
- [ ] 移动端适配优化
- [ ] 增加数据备份功能

---

<p align="center">
  <img alt="footer" src="https://img.shields.io/badge/Made%20with-❤️-red.svg">
  <img alt="footer" src="https://img.shields.io/badge/Build%20with-Vue3%20%2B%20Spring%20Boot-blue.svg">
</p>
