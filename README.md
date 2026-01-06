# 次元资讯 Anime_News

## 项目简介

这是一个基于Spring Boot开发的动漫新闻网站，集成了新闻展示、动漫更新、热门音乐、用户评论、点赞等功能。网站使用了现代化的前端技术和后端框架，提供了完整的用户交互体验。

## 网站预览

![网站首页](/2026-01-07%20015657.png)

## 技术栈

### 后端技术
- **Spring Boot 2.7.8** - 核心框架
- **Spring Data JPA** - 数据持久层
- **Spring Boot Cache** - 缓存支持
- **Apache Shiro** - 安全认证框架
- **H2 Database** - 嵌入式数据库
- **Druid** - 数据库连接池
- **Swagger 2** - API文档生成
- **Jsoup** - 网页爬虫

### 前端技术
- **Thymeleaf** - 模板引擎
- **Bootstrap** - UI框架
- **jQuery** - JavaScript库
- **Live2D** - 看板娘动画
- **Canvas-Nest** - 动态背景效果

## 项目结构

```
src/main/java/com/example/anime_news/
├── config/          # 配置类
├── controller/      # 控制器层
├── dao/            # 数据访问层
├── pojo/           # 实体类
├── service/        # 业务逻辑层
├── utils/          # 工具类
└── Application.java # 启动类

src/main/resources/
├── static/         # 静态资源
│   ├── css/       # 样式文件
│   ├── js/        # JavaScript文件
│   ├── img/       # 图片资源
│   ├── bg-music/  # 背景音乐
│   ├── canvas-nest/ # 动态背景
│   └── live2d/    # 看板娘资源
├── templates/      # Thymeleaf模板
└── application.yml # 配置文件
```

## 功能模块

### 1. 新闻管理
- 新闻列表展示
- 新闻详情查看
- 新闻搜索功能
- 新闻后台管理
- 爬虫自动抓取新闻

### 2. 动漫管理
- 动漫信息展示
- 动漫分类浏览
- 动漫后台管理

### 2. 音乐管理
- 音乐信息展示
- 音乐分类浏览
- 音乐后台管理
- 背景音乐控制

### 4. 用户系统
- 用户注册/登录
- 用户权限管理
- 用户信息管理

### 5. 评论系统
- 新闻评论
- 评论管理

### 6. 点赞功能
- 新闻点赞
- 点赞统计

## 快速开始

### 环境要求
- JDK 1.8+
- Maven 3.6+

### 安装步骤

1. **克隆项目**
   ```bash
   git clone https://github.com/Wen-CY-2333/Anime-news.git
   cd anime_news
   ```

2. **编译项目**
   ```bash
   mvn clean compile
   ```

3. **运行项目**
   ```bash
   mvn spring-boot:run
   ```

4. **访问应用**
   - 主页：http://localhost:3399/home
   - H2数据库控制台：http://localhost:3399/h2
   - Swagger API文档：http://localhost:3399/swagger-ui.html

### 数据库配置

项目使用H2嵌入式数据库，配置信息如下：
- 数据库URL：`jdbc:h2:file:./myH2`
- 用户名：`root`
- 密码：`root`

## API文档

项目集成了Swagger 2，启动后可通过以下地址访问API文档：
http://localhost:3399/swagger-ui.html

主要API端点：
- `/news` - 新闻相关接口
- `/anime` - 动漫相关接口
- `/music` - 音乐相关接口
- `/user` - 用户相关接口
- `/comment` - 评论相关接口
- `/like` - 点赞相关接口

## 特色功能

### 1. 爬虫系统
- 自动抓取动漫新闻
- 定时任务更新内容
- 数据清洗和存储

### 2. Live2D看板娘
- 交互式看板娘
- 多种角色模型
- 动态表情和动作
- 加入换装按钮

### 3. 动态背景
- Canvas-Nest粒子效果

### 4. 音乐播放器
- 背景音乐播放
- 播放列表管理
- 音乐可视化效果

### 5. 缓存系统
- 基于Spring Boot Cache实现的高效缓存机制
- 应用于新闻、动漫、音乐和用户数据
- 使用@Cacheable、@CachePut、@CacheEvict等注解管理缓存
- 提升系统响应速度和减轻数据库压力

### 6. 定时任务系统
- 基于Spring Scheduling实现的定时任务管理
- 每1分钟自动增加新闻访问量
- 每天自动爬取最新动漫新闻
- 每天自动更新番剧信息
- 每天自动更新音乐信息
- 支持Cron表达式灵活配置任务执行时间

## 开发指南

### 添加新功能
1. 在`pojo`包中创建实体类
2. 在`dao`包中创建数据访问接口
3. 在`service`包中实现业务逻辑
4. 在`controller`包中创建控制器
5. 在`templates`目录中创建前端页面

### 配置说明
主要配置文件为`application.yml`，包含：
- 服务器端口配置
- 数据库连接配置
- JPA配置
- Shiro安全配置