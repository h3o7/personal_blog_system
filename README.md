# 🌟 个人博客系统 (Personal Blog System)

[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.4.5-brightgreen.svg)](https://spring.io/projects/spring-boot)
[![Vue.js](https://img.shields.io/badge/Vue.js-3.x-4fc08d.svg)](https://vuejs.org/)
[![Element Plus](https://img.shields.io/badge/Element%20Plus-Latest-409eff.svg)](https://element-plus.org/)
[![MySQL](https://img.shields.io/badge/MySQL-8.0-blue.svg)](https://www.mysql.com/)
[![License](https://img.shields.io/badge/License-MIT-yellow.svg)](LICENSE)

一个基于 Spring Boot + Vue 3 + Element Plus 开发的现代化个人博客系统，采用前后端分离架构，提供完整的博客功能和后台管理系统。

## 📖 项目简介

这是一个功能完整的个人博客系统，支持文章发布、评论互动、用户管理、标签分类等核心功能。系统分为用户端和管理端，提供良好的用户体验和强大的管理功能。

**项目特点：**
- 🎯 前后端分离架构
- 🎨 现代化 UI 设计
- 📱 响应式布局
- 🔐 JWT 身份认证
- 📊 数据可视化
- 🌙 深色主题支持

## ✨ 功能特性

### 👥 用户端功能
- **📝 文章系统**
  - 文章浏览、搜索、分页
  - Markdown 编辑器
  - 文章分类和标签
  - 文章点赞、收藏
  
- **💬 互动功能**
  - 评论系统（支持回复）
  - 评论点赞
  - 用户关注/粉丝
  
- **👤 个人中心**
  - 个人信息管理
  - 我的文章管理
  - 我的收藏
  - 关注/粉丝列表

### 🛠️ 管理端功能
- **📊 数据概览**
  - 系统统计信息
  - 数据图表展示
  - 实时监控面板
  
- **📖 内容管理**
  - 文章管理（增删改查）
  - 评论管理
  - 标签分类管理
  
- **👥 用户管理**
  - 用户列表
  - 用户状态控制
  - 权限管理
  
- **🔧 系统管理**
  - 友情链接管理
  - 广告管理
  - 系统设置

## 🛠️ 技术架构

### 后端技术栈
| 技术 | 版本 | 描述 |
|------|------|------|
| Spring Boot | 3.4.5 | 主框架 |
| MyBatis Plus | 3.5.12 | ORM 框架 |
| MySQL | 8.0+ | 数据库 |
| Redis | Latest | 缓存中间件 |
| JWT | 0.9.1 | 认证授权 |
| Swagger | 2.7.0 | API 文档 |
| Hutool | 5.8.39 | 工具库 |
| 阿里云OSS | 3.17.4 | 文件存储 |

### 前端技术栈
| 技术 | 版本 | 描述 |
|------|------|------|
| Vue.js | 3.x | 前端框架 |
| Vite | Latest | 构建工具 |
| Element Plus | Latest | UI 组件库 |
| Vue Router | Latest | 路由管理 |
| Axios | Latest | HTTP 客户端 |

## 📁 项目结构

```
personal_blog_system/
├── 📁 blog-back-end/                    # 后端服务
│   ├── 📁 src/
│   │   ├── 📁 main/
│   │   │   ├── 📁 java/blogsystem/
│   │   │   │   ├── 📁 config/           # 配置类
│   │   │   │   ├── 📁 controller/       # 控制器层
│   │   │   │   ├── 📁 entity/           # 实体类
│   │   │   │   ├── 📁 mapper/           # 数据访问层
│   │   │   │   ├── 📁 service/          # 业务逻辑层
│   │   │   │   ├── 📁 utils/            # 工具类
│   │   │   │   └── 📁 vo/               # 视图对象
│   │   │   └── 📁 resources/            # 配置文件
│   │   └── 📁 test/                     # 测试代码
│   ├── 📄 pom.xml                       # Maven 配置
│   └── 📄 README.md                     # 后端说明文档
├── 📁 blog-front-end/                   # 前端项目
│   ├── 📁 src/
│   │   ├── 📁 api/                      # API 接口
│   │   ├── 📁 assets/                   # 静态资源
│   │   ├── 📁 components/               # 公共组件
│   │   ├── 📁 router/                   # 路由配置
│   │   ├── 📁 styles/                   # 样式文件
│   │   ├── 📁 utils/                    # 工具函数
│   │   └── 📁 views/                    # 页面组件
│   │       ├── 📁 admins/               # 管理端页面
│   │       └── 📁 users/                # 用户端页面
│   ├── 📄 package.json                  # NPM 配置
│   ├── 📄 vite.config.js               # Vite 配置
│   └── 📄 README.md                     # 前端说明文档
├── 📁 sql/                              # 数据库脚本
│   ├── 📄 blog_system.sql               # 数据库结构
│   ├── 📄 init_data.sql                 # 初始化数据
│   └── 📄 update_*.sql                  # 更新脚本
├── 📄 README.md                         # 项目总览
└── 📄 .gitignore                        # Git 忽略文件
```

## 🚀 快速开始

### 环境准备

确保您的开发环境满足以下要求：

- ☕ **Java**: JDK 17 或更高版本
- 🟢 **Node.js**: 16.x 或更高版本
- 🐬 **MySQL**: 8.0 或更高版本
- 📦 **Redis**: 最新稳定版本
- 🔧 **Maven**: 3.6 或更高版本

### 📥 克隆项目

```bash
git clone https://github.com/h3o7/personal_blog_system.git
cd personal_blog_system
```

### 🗃️ 数据库初始化

1. **创建数据库**
```sql
CREATE DATABASE blog_system CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
```

2. **导入数据库结构**
```bash
# 导入数据库结构和初始数据
mysql -u your_username -p blog_system < sql/blog_system.sql
mysql -u your_username -p blog_system < sql/init_data.sql
```

### ⚙️ 后端配置与启动

1. **配置数据库连接**

修改 `blog-back-end/src/main/resources/application.yml`：

```yaml
spring:
  # 数据库配置
  datasource:
    url: jdbc:mysql://localhost:3306/blog_system?useUnicode=true&characterEncoding=utf8&useSSL=false&serverTimezone=Asia/Shanghai
    username: your_username
    password: your_password
    driver-class-name: com.mysql.cj.jdbc.Driver
  
  # Redis 配置
  redis:
    host: localhost
    port: 6379
    password: your_redis_password
    database: 0
    timeout: 3000ms
    lettuce:
      pool:
        max-active: 8
        max-idle: 8
        min-idle: 0

# 阿里云 OSS 配置（可选）
aliyun:
  oss:
    endpoint: your_oss_endpoint
    bucket-name: your_bucket_name
    region: your_region

# JWT 配置
jwt:
  secret: your_jwt_secret_key
  expiration: 86400000  # 24小时

# 验证码配置
captcha:
  width: 100
  height: 40
  length: 4
  expire-time: 60
```

2. **启动后端服务**
```bash
cd blog-back-end
mvn clean install
mvn spring-boot:run
```

服务启动后，访问：
- API 服务: http://localhost:8080
- Swagger 文档: http://localhost:8080/swagger-ui/index.html

### 🎨 前端配置与启动

1. **安装依赖**
```bash
cd blog-front-end
npm install
```

2. **配置代理**

确认 `vite.config.js` 中的代理配置：

```javascript
export default defineConfig({
  server: {
    proxy: {
      '/api': {
        target: 'http://localhost:8080',
        changeOrigin: true,
        rewrite: (path) => path.replace(/^\/api/, ''),
      },
    },
  },
})
```

3. **启动开发服务器**
```bash
npm run dev
```

访问地址：
- 登录端口: http://localhost:5173/login
- 用户端: http://localhost:5173/users
- 管理端: http://localhost:5173/admins

4. **构建生产版本**
```bash
npm run build
```

## 🔗 访问地址

| 服务 | 地址 | 描述 |
|------|------|------|
| 登录页 ｜ http://localhost:5173/login   ｜需要先登录｜
| 用户端首页 | http://localhost:5173/users | 博客首页 |
| 管理后台 | http://localhost:5173/admins | 后台管理 |
| API 文档 | http://localhost:8080/swagger-ui/index.html | Swagger 文档 |
| 后端接口 | http://localhost:8080 | RESTful API |

## 📊 默认账户

系统提供以下测试账户：

| 角色 | 用户名 | 密码 | 权限 |
|------|--------|------|------|
| 管理员 | 100110 | 200504 | 全部权限 |
| 普通用户 | 13user | 123456 | 基础权限 |

> ⚠️ **注意**: 生产环境请及时修改默认密码！

## 🔧 开发指南

### 后端开发

1. **控制器层**: 处理 HTTP 请求，参数校验
2. **服务层**: 业务逻辑处理
3. **数据访问层**: MyBatis Plus 操作数据库
4. **实体类**: 数据库表映射

### 前端开发

1. **路由配置**: Vue Router 路由管理
2. **组件开发**: Vue 3 Composition API
3. **状态管理**: Ref/Reactive 响应式数据
4. **UI 组件**: Element Plus 组件库

### API 设计规范

```
GET    /api/articles        # 获取文章列表
GET    /api/articles/{id}   # 获取文章详情
POST   /api/articles        # 创建文章
PUT    /api/articles/{id}   # 更新文章
DELETE /api/articles/{id}   # 删除文章
```

## 🐳 Docker 部署（可选）

```bash
# 构建镜像
docker-compose build

# 启动服务
docker-compose up -d

# 查看日志
docker-compose logs -f
```

## 📝 API 文档

项目集成了 Swagger，启动后端服务后访问：
```
http://localhost:8080/swagger-ui/index.html
```

主要 API 接口：

| 模块 | 接口路径 | 描述 |
|------|----------|------|
| 用户认证 | `/auth/*` | 登录、注册、验证码 |
| 文章管理 | `/article/*` | 文章 CRUD 操作 |
| 评论管理 | `/comment/*` | 评论 CRUD 操作 |
| 用户管理 | `/user/*` | 用户信息管理 |
| 文件上传 | `/upload/*` | 文件上传接口 |

## 🤝 贡献指南

我们欢迎所有形式的贡献！

1. 🍴 Fork 本仓库
2. 🔀 创建特性分支: `git checkout -b feature/your-feature`
3. 💾 提交更改: `git commit -m 'Add some feature'`
4. 📤 推送分支: `git push origin feature/your-feature`
5. 🔍 提交 Pull Request

### 开发规范

- 🎯 遵循 RESTful API 设计规范
- 📝 编写清晰的代码注释
- ✅ 确保测试用例通过
- 📋 更新相关文档

## 🗺️ 开发计划

- [ ] 📱 移动端适配优化
- [ ] 🔍 全文搜索功能（Elasticsearch）
- [ ] 📧 邮件通知系统
- [ ] 🖼️ 图片压缩和水印
- [ ] 📈 访问统计分析
- [ ] 🔒 OAuth2 第三方登录
- [ ] 🐳 Docker 容器化部署
- [ ] ☁️ 云原生支持

## ❓ 常见问题

### Q: 启动时出现数据库连接错误？
A: 请检查数据库配置信息，确保 MySQL 服务已启动，数据库已创建。

### Q: 前端页面显示空白？
A: 请确保后端服务已启动，检查浏览器控制台是否有错误信息。

### Q: 文件上传失败？
A: 请检查阿里云 OSS 配置，或使用本地文件存储方案。

### Q: 验证码不显示？
A: 请检查 Redis 服务是否正常运行。

## 📄 许可证

本项目采用 [MIT 许可证](LICENSE) - 查看 LICENSE 文件了解详情。

## 👨‍💻 作者

**h3o7** 
- GitHub: [@h3o7](https://github.com/h3o7)
- 项目地址: [personal_blog_system](https://github.com/h3o7/personal_blog_system)

## 🙏 致谢

感谢以下开源项目的支持：
- [Spring Boot](https://spring.io/projects/spring-boot)
- [Vue.js](https://vuejs.org/)
- [Element Plus](https://element-plus.org/)
- [MyBatis Plus](https://baomidou.com/)

## 📞 联系方式

如果您有任何问题或建议，欢迎通过以下方式联系：

- 📧 提交 Issue
- 💬 发起 Discussion
- 🔀 提交 Pull Request

---

⭐ 如果这个项目对您有帮助，请给个 Star 支持一下！

**让我们一起构建更好的博客系统！** 🚀
