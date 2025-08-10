# 简化版个人博客系统

一个前后端分离的个人博客系统示例，前端使用 Vue，后端基于 Spring Boot + MyBatis-Plus 实现。本文档重点介绍后端模块的结构、技术栈与核心接口，便于开发与二次扩展。

> 说明：本 README 基于仓库代码自动梳理，可能不完全覆盖全部细节，建议结合源码一并阅读：  
> https://github.com/h3o7/personal_blog_system

---

## 仓库结构

```
personal_blog_system/
├─ blog-front-end/           # 前端（Vue）
└─ blog-back-end/            # 后端（Spring Boot）
   ├─ src/
   │  ├─ main/java/blogsystem/
   │  │  ├─ config/          # 全局配置（Knife4j/OpenAPI 等）
   │  │  ├─ controller/      # 控制器层（REST API）
   │  │  ├─ service/         # 服务接口
   │  │  │  └─ impl/         # 服务实现
   │  │  ├─ mapper/          # MyBatis-Plus Mapper（@MapperScan）
   │  │  ├─ entity/          # 实体模型
   │  │  ├─ dto/             # 数据传输对象 DTO
   │  │  ├─ vo/              # 视图对象 VO / 统一响应
   │  │  └─ utils/           # 工具类（JWT、验证码、OSS 操作、代码生成器等）
   │  └─ test/java/blogsystem/
   │     └─ BlogFrontTest.java  # Redis 与业务相关测试
   └─ .mvn/wrapper/          # Maven Wrapper（3.3.2，Maven 3.9.9）
```

后端启动入口：
- blog-back-end/src/main/java/blogsystem/BlogSystemApplication.java  
  使用 @SpringBootApplication，@MapperScan("blogsystem.mapper")

---

## 后端技术栈

- Spring Boot（REST API）
- MyBatis-Plus（数据访问、条件构造器、代码生成器）
- OpenAPI 3 + Knife4j（在线 API 文档）
- Redis（点赞/收藏等数据持久化与测试验证）
- JWT（鉴权，见 utils.JWTUtils）
- 图片验证码（CaptchaHandler，登录防刷）
- 阿里云 OSS（文件上传：AliyunOSSOperator）
- Lombok、Hutool（工具类）
- Maven Wrapper（简化构建）

---

## 核心功能（后端）

- 认证与登录
  - GET /login/captcha 生成图形验证码，并在响应头暴露 captcha-id
  - POST /login/admin 管理员登录
  - POST /login/user  用户登录
  - 返回统一 Result，其中登录成功通常包含登录态信息（如 token）

- 后台统计
  - GET /admin 汇总文章数、标签数、广告类型数、用户数，附带运行环境信息（os.name + os.version）与本机 IP

- 文章模块
  - GET /article/list             滚动分页（支持本地/UTC 时间转换）
  - GET /article/hot/list         热门文章
  - GET /article/search           关键字搜索
  - PUT /article/like             点赞
  - PUT /article/collect          收藏
  - GET /article/detail           详情
  - GET /article/type/list        按类型筛选
  - POST /article/add             新增
  - POST /article/save            草稿保存
  - GET /article/draft            获取草稿

- 文章列表管理
  - GET  /articleList/list        后台分页
  - DELETE /articleList           删除文章

- 评论模块
  - GET  /comment/list            文章评论列表
  - GET  /comment/reply/list      回复列表
  - POST /comment/add             评论
  - POST /comment/reply/add       回复评论
  - PUT  /comment/like            点赞评论
  - DELETE /comment/delete        删除评论

- 标签模块
  - GET  /articleTag/list         标签列表（时间倒序）
  - DELETE /articleTag            删除标签
  - PUT  /articleTag/update       更新标签
  - POST /articleTag/add          新增标签（UUID、时间自动补齐）

- 广告与类型
  - GET    /ad/list               按类型查广告
  - POST   /ad/add                新增广告
  - PUT    /ad/edit               编辑广告
  - DELETE /ad                    删除广告
  - GET    /ad/type/list          广告类型列表（排序）
  - POST   /ad/type/add           新增广告类型
  - ……（更多类型编辑/删除接口请见源码）

- 友情链接
  - GET    /link/list             列表（排序）
  - DELETE /link                  删除
  - POST   /link/add              新增
  - PUT    /link/edit             编辑

- 文件上传/删除（阿里云 OSS）
  - POST /upload/ad               上传广告图（multipart/form-data, file 字段）
  - POST /upload/user             上传用户头像（multipart/form-data, file 字段）
  - POST /delete                  删除文件（根据上传文件名）

---

## API 文档（Knife4j）

已启用 Knife4j，并配置自定义 OpenAPI 标题与描述：
- 配置类：blog-back-end/src/main/java/blogsystem/config/Knife4jConfig.java
- 默认访问示例（端口按需修改）：  
  http://localhost:8080/doc.html

---

## 快速开始（后端）

### 环境要求
- JDK 17+（建议）
- Maven 3.9+（项目内置 Maven Wrapper）
- MySQL/其他关系型数据库（用于 MyBatis-Plus）
- Redis（部分点赞/收藏等能力依赖，测试用例涉及）

### 配置
在 blog-back-end 的 application 配置中（application.yml 或 properties），按需设置：
- 数据源（spring.datasource.*）
- Redis（spring.redis.*）
- OSS 相关（例如自定义 aliyun.*），用于 AliyunOSSOperator
- 服务器端口（server.port，默认 8080）

> 安全起见，推荐通过环境变量注入敏感配置。

### 启动
```bash
cd blog-back-end
# 启动
./mvnw spring-boot:run

# 或打包后运行
./mvnw clean package -DskipTests
java -jar target/*.jar
```

### 常用命令
```bash
# 运行测试（包含 Redis 相关用例）
./mvnw test

# 代码生成（基于 MyBatis-Plus FastAutoGenerator，自行在 utils/CodeGenerator.java 配置并运行）
```

---

## 统一响应与对象约定

- 统一响应：blogsystem.vo.Result
  - Result.success(data/message)
  - Result.error(message)
- 常用 DTO/VO（示例）
  - LoginDTO：登录参数
  - AdminDTO：后台统计数据返回
  - NewArticleDTO：文章新增/保存
  - PageResult：分页结果

---

## 典型调用示例

- 获取验证码（响应头会返回 captcha-id，并通过 Access-Control-Expose-Headers 暴露给前端）
```bash
curl -i "http://localhost:8080/login/captcha"
# 响应头示例：
# captcha-id: <id>
# Access-Control-Expose-Headers: captcha-id
```

- 管理员登录
```bash
curl -X POST "http://localhost:8080/login/admin" \
  -H "Content-Type: application/json" \
  -d '{"account":"admin","password":"****","captchaId":"<id>","captchaText":"abcd"}'
```

- 获取后台统计
```bash
curl "http://localhost:8080/admin"
```

- 滚动分页文章列表
```bash
curl "http://localhost:8080/article/list?addTime=2025-08-10T00:00:00&limit=10"
```

- 上传广告图
```bash
curl -X POST "http://localhost:8080/upload/ad" \
  -F "file=@/path/to/banner.png"
```

---

## 测试与缓存

- 测试类 BlogFrontTest 覆盖：
  - Redis 连接验证、范围查询等
  - 点赞/收藏数据的持久化流程
  - 文章、评论等 Mapper 与服务调用示例

> 运行测试前请确保 Redis 与数据库已就绪，相关测试数据与键空间会被读写。

---

## 安全与鉴权

- 登录口令结合图形验证码（CaptchaHandler），服务端通过响应头返回 captcha-id，前端需携带验证码文本完成校验。
- 登录成功后由服务层使用 JWTUtils 生成登录凭证，前端可在后续请求中携带 Token（具体拦截与校验逻辑可在安全配置中扩展）。
- 对象字段脱敏/校验可在 DTO/VO 与 Service 层控制。

---

## 前端对接（简述）

前端（blog-front-end）通过统一的 request 工具访问后端 API，示例：
- 登录：POST /login/admin、/login/user（src/api/login.js）
- 管理统计：GET /admin（src/api/admin.js）
- 用户管理：/user/*（src/api/user.js）
- 文件上传：/upload/*（src/api/upload.js）

---

## 相关源码定位（部分）

- 启动类：blog-back-end/src/main/java/blogsystem/BlogSystemApplication.java
- 文档配置：blog-back-end/src/main/java/blogsystem/config/Knife4jConfig.java
- 登录：blog-back-end/src/main/java/blogsystem/controller/LoginController.java
- 后台统计：blog-back-end/src/main/java/blogsystem/controller/AdminController.java
- 文章：blog-back-end/src/main/java/blogsystem/controller/ArticleController.java
- 文章列表管理：blog-back-end/src/main/java/blogsystem/controller/ArticleListController.java
- 评论：blog-back-end/src/main/java/blogsystem/controller/CommentController.java
- 标签：blog-back-end/src/main/java/blogsystem/controller/ArticleTagController.java
- 广告与类型：blog-back-end/src/main/java/blogsystem/controller/AdController.java
- 友情链接：blog-back-end/src/main/java/blogsystem/controller/LinkController.java
- 上传与删除：blog-back-end/src/main/java/blogsystem/controller/UploadController.java、DeleteController.java
- 工具类（验证码/JWT/OSS/代码生成）：blog-back-end/src/main/java/blogsystem/utils/

---

## 贡献与许可

欢迎提交 Issue 与 PR 来完善功能与文档。

（许可协议以仓库实际文件为准）
