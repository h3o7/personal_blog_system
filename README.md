# 个人博客系统 (Personal Blog System)

一个基于 Spring Boot + Vue 3 + Element Plus 开发的现代化个人博客系统，支持文章发布、评论互动、用户管理等功能。

## 📋 项目简介

这是一个简化版的个人博客系统，采用前后端分离架构设计，提供了完整的博客功能和后台管理系统。系统支持文章管理、用户管理、评论系统、标签分类、广告管理等核心功能。

## ✨ 主要特性

### 前台功能
- 📝 文章浏览与搜索
- 💬 评论系统（支持回复、点赞）
- 🏷️ 标签与分类浏览
- 👤 用户个人中心
- 🌙 深色/浅色主题切换
- 📱 响应式设计
- ❤️ 文章点赞收藏
- 👥 用户关注系统

### 后台管理
- 📊 数据统计面板
- 📖 文章管理
- 👥 用户管理
- 🏷️ 标签分类管理
- 💬 评论管理
- 🔗 友情链接管理
- 📢 广告管理
- ⚙️ 系统设置

### 技术特性
- 🔐 JWT 身份认证
- 📄 分页查询
- 🖼️ 阿里云 OSS 文件存储
- 📊 MyBatis Plus 数据库操作
- 🌐 RESTful API 设计
- 📚 Swagger API 文档

## 🛠️ 技术栈

### 后端技术
- **框架**: Spring Boot 3.4.5
- **数据库**: MySQL + MyBatis Plus
- **缓存**: Redis
- **认证**: JWT
- **文档**: Swagger (SpringDoc OpenAPI)
- **工具库**: Hutool
- **云存储**: 阿里云 OSS
- **构建工具**: Maven

### 前端技术
- **框架**: Vue 3 + Vite
- **UI库**: Element Plus
- **路由**: Vue Router
- **HTTP客户端**: Axios
- **图标**: Element Plus Icons
- **样式**: CSS3 + 响应式设计

## 📁 项目结构

```
personal_blog_system/
├── blog-back-end/          # 后端项目
│   ├── src/
│   │   ├── main/
│   │   │   ├── java/
│   │   │   │   └── blogsystem/
│   │   │   │       ├── config/      # 配置类
│   │   │   │       ├── controller/  # 控制器
│   │   │   │       ├── entity/      # 实体类
│   │   │   │       ├── mapper/      # 数据访问层
│   │   │   │       ├── service/     # 业务逻辑层
│   │   │   │       ├── utils/       # 工具类
│   │   │   │       └── vo/          # 视图对象
│   │   │   └── resources/           # 配置文件
│   │   └── test/                    # 测试代码
│   └── pom.xml                      # Maven配置
└── blog-front-end/         # 前端项目
    ├── src/
    │   ├── api/            # API接口
    │   ├── assets/         # 静态资源
    │   ├── components/     # 组件
    │   ├── router/         # 路由配置
    │   ├── styles/         # 样式文件
    │   ├── utils/          # 工具函数
    │   └── views/          # 页面组件
    │       ├── admins/     # 管理端页面
    │       └── users/      # 用户端页面
    ├── package.json
    └── vite.config.js      # Vite配置
```

## 🚀 快速开始

### 环境要求
- Java 17+
- Node.js 16+
- MySQL 8.0+
- Redis
- Maven 3.6+

### 后端启动

1. 克隆项目
```bash
git clone https://github.com/h3o7/personal_blog_system.git
cd personal_blog_system
```

2. 配置数据库
```bash
# 创建数据库
CREATE DATABASE blog_system CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
```

3. 配置 application.yml
```yaml
# 数据库配置
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/blog_system
    username: your_username
    password: your_password
  
# Redis配置
  redis:
    host: localhost
    port: 6379

# 阿里云OSS配置
aliyun:
  oss:
    endpoint: your_endpoint
    bucket-name: your_bucket
    region: your_region
```

4. 启动后端服务
```bash
cd blog-back-end
mvn spring-boot:run
```

### 前端启动

1. 安装依赖
```bash
cd blog-front-end
npm install
```

2. 启动开发服务器
```bash
npm run dev
```

3. 构建生产版本
```bash
npm run build
```

## 📱 功能展示

### 用户端
- **首页**: 文章列表、热门文章、分类标签
- **文章详情**: 文章内容、评论互动、相关推荐
- **个人中心**: 个人信息、我的文章、关注/粉丝
- **写作页面**: Markdown 编辑器、文章发布

### 管理端
- **数据面板**: 系统统计、图表展示
- **内容管理**: 文章、评论、标签、分类管理
- **用户管理**: 用户列表、权限控制
- **系统管理**: 广告、友链、系统设置

## 🔧 配置说明

### 数据库配置
系统使用 MySQL 数据库，需要配置数据库连接信息。

### Redis 配置
用于缓存和 Session 管理，需要配置 Redis 连接信息。

### 阿里云 OSS 配置
用于图片和文件存储，需要配置阿里云 OSS 相关信息。

### JWT 配置
用于用户身份认证，可在配置文件中设置密钥和过期时间。

## 📄 API 文档

启动后端服务后，可访问 Swagger API 文档：
```
http://localhost:8080/swagger-ui/index.html
```

## 🤝 贡献指南

1. Fork 本仓库
2. 创建特性分支 (`git checkout -b feature/AmazingFeature`)
3. 提交更改 (`git commit -m 'Add some AmazingFeature'`)
4. 推送到分支 (`git push origin feature/AmazingFeature`)
5. 打开 Pull Request

## 📝 开发计划

- [ ] 移动端适配优化
- [ ] 评论系统增强
- [ ] 搜索功能优化
- [ ] 性能优化
- [ ] 单元测试完善
- [ ] Docker 部署支持

## 📄 许可证

本项目采用 MIT 许可证 - 查看 [LICENSE](LICENSE) 文件了解详情。

## 👨‍💻 作者

**h3o7** - [GitHub](https://github.com/h3o7)

## 🙏 致谢

感谢所有为这个项目做出贡献的开发者们！

---

如果这个项目对您有帮助，请给个 ⭐ Star 支持一下！
