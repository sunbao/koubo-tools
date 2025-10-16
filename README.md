# AI驱动的爆款内容自动生成与提词一体化系统

方便录口播类视频
如果只做一个最牛逼的功能，建议你聚焦于：

"AI驱动的爆款内容自动生成与提词一体化系统"

## 核心亮点：

- 实时抓取全网热点数据，AI自动生成最适合你账号风格的爆款口播文案
- 一键推送到提词器，支持多端同步、智能语速提醒
- 支持个性化语气、情感、风格定制，内容自动适配抖音/快手等平台
- 让创作者只需专注录制，内容、提词、热点全部自动化

这个功能能极大提升创作者效率和内容质量，真正实现"爆款内容自动来，录制无压力"，在行业内极具差异化和竞争力。

## 项目结构

```
src/
├── main/
│   ├── java/
│   │   └── com/koubotools/scriptgenerator/
│   │       ├── ScriptGeneratorApplication.java
│   │       ├── controller/
│   │       ├── service/
│   │       ├── repository/
│   │       ├── model/
│   │       ├── dto/
│   │       ├── config/
│   │       ├── exception/
│   │       └── util/
│   └── resources/
│       ├── application.yml
│       └── static/
└── test/
```

## 技术栈

- 后端：Spring Boot + Spring AI + JPA + MySQL + Redis
- AI服务：阿里通义千问系列大模型（通过阿里百炼平台）
- 实时通信：WebSocket
- 前端：微信小程序（后续开发）

## 核心功能

### 1. 用户管理
- 用户注册、登录、登出
- 用户信息管理
- 权限认证与鉴权

### 2. 热点数据管理
- 实时抓取全网热点数据
- 热点数据分类与标签化
- 热点数据缓存优化

### 3. AI文案生成
- 基于用户风格和热点生成个性化文案
- 集成阿里通义千问大模型（通过阿里百炼平台）
- 文案质量评估与优化

### 4. 提词器功能
- 文案一键推送到提词器
- 多端实时同步（WebSocket）
- **智能语速调节（100-300字/分钟）**
- **文本滚动动画效果**
- **实时滚动频率控制**

### 5. 性能优化
- Redis缓存热点数据和用户信息
- 数据库查询优化

## API接口

### 用户管理
- `POST /api/users/register` - 用户注册
- `POST /api/users/login` - 用户登录
- `POST /api/users/logout` - 用户登出
- `GET /api/users/{id}` - 获取用户信息
- `PUT /api/users/{id}` - 更新用户信息
- `DELETE /api/users/{id}` - 删除用户

### 热点数据
- `GET /api/hotspots` - 获取所有热点
- `GET /api/hotspots/{id}` - 获取热点详情
- `GET /api/hotspots/platform/{platform}` - 按平台获取热点
- `GET /api/hotspots/type/{type}` - 按类型获取热点
- `POST /api/hotspots` - 创建热点
- `PUT /api/hotspots/{id}` - 更新热点
- `DELETE /api/hotspots/{id}` - 删除热点

### 文案生成
- `GET /api/scripts` - 获取所有文案
- `GET /api/scripts/{id}` - 获取文案详情
- `POST /api/scripts` - 创建文案
- `POST /api/scripts/generate?userId={userId}&hotspotId={hotspotId}` - AI生成文案
- `PUT /api/scripts/{id}` - 更新文案
- `DELETE /api/scripts/{id}` - 删除文案

### 提词器
- `POST /api/teleprompter/push?scriptId={scriptId}` - 推送到提词器
- `GET /api/teleprompter/status` - 获取提词器状态
- `POST /api/teleprompter/control?command={command}` - 控制提词器
- `POST /api/teleprompter/speed?scriptId={scriptId}` - 计算最佳语速
- `POST /api/teleprompter/duration?scriptId={scriptId}&speed={speed}` - 计算滚动持续时间

## 阿里百炼平台API配置

### 获取API Key
1. 访问 [阿里云官网](https://www.aliyun.com/)
2. 注册或登录账号
3. 进入[阿里百炼平台](https://help.aliyun.com/zh/bailian)
4. 创建并获取您的API Key

### 配置方式
API Key已经在application.yml中配置：
```yaml
dashscope:
  api:
    key: sk-b551605ddd164c9990ac784ad013b472
  model:
    name: qwen-turbo
```

### 支持的模型
- `qwen-turbo` - 推理速度较快的模型
- `qwen-plus` - 效果平衡的模型
- `qwen-max` - 效果最好的模型

## 配置说明

### 数据库配置
```yaml
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/script_generator
    username: root
    password: root
```

### Redis配置
```yaml
spring:
  redis:
    host: localhost
    port: 6379
```

### 阿里通义千问配置
```yaml
dashscope:
  api:
    key: your-dashscope-api-key
  model:
    name: qwen-turbo
```

## 快速开始

1. 克隆项目
2. 配置MySQL数据库和Redis
3. 设置阿里百炼平台API Key（已配置）
4. 运行项目：`mvn spring-boot:run`
5. 访问API：http://localhost:8080

## 测试

项目包含完整的单元测试和集成测试：
- 运行单元测试：`mvn test`
- 运行集成测试：`mvn verify`

## 部署

支持Docker容器化部署：
```bash
# 构建Docker镜像
docker build -t script-generator .

# 运行容器
docker run -p 8080:8080 script-generator
```