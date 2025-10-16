# Docker方式部署后端

本文档详细说明如何使用Docker和Docker Compose部署AI驱动的爆款内容自动生成与提词一体化系统后端服务。

## 目录结构

```
.
├── Dockerfile              # Docker构建文件
├── docker-compose.yml      # Docker Compose配置文件
├── build.sh                # 项目构建脚本
└── DOCKER_DEPLOYMENT.md    # 本部署说明文件
```

## 部署方式

### 方式一：使用构建脚本（推荐）

项目提供了一个构建脚本 [build.sh](file:///data/koubo-tools/build.sh)，可以自动完成项目编译和Docker镜像构建。这种方式避免了在Docker内部进行复杂的依赖下载和编译过程。

```bash
# 给脚本添加执行权限
chmod +x build.sh

# 运行构建脚本
./build.sh
```

该脚本会：
1. 检查系统是否安装了Maven
2. 在本地编译项目生成JAR文件
3. 使用编译好的JAR文件构建Docker镜像
4. 启动完整的应用环境（MySQL和Redis）

### 方式二：分步部署

您也可以分步执行部署过程：

```bash
# 1. 编译项目生成JAR文件
mvn clean package -DskipTests

# 2. 构建Docker镜像
docker build -t koubo-script-generator .

# 3. 启动所有服务
docker-compose up -d
```

## 部署步骤

### 1. 环境准备

确保已安装以下软件：
- Docker (版本 18.09 或更高)
- Docker Compose (版本 1.25 或更高)
- Maven (用于本地编译)

### 2. 构建和部署

#### 使用构建脚本方式（推荐）：

```bash
# 给脚本添加执行权限
chmod +x build.sh

# 运行构建脚本
./build.sh
```

#### 分步执行方式：

```bash
# 1. 编译项目生成JAR文件
mvn clean package -DskipTests

# 2. 构建Docker镜像
docker build -t koubo-script-generator .

# 3. 启动所有服务
docker-compose up -d
```

### 3. 服务访问

部署成功后，可以通过以下方式访问各服务：

- **后端API**: http://localhost:8080
- **MySQL数据库**: localhost:3306
- **Redis缓存**: localhost:6379

### 4. 管理命令

```bash
# 停止所有服务
docker-compose down

# 重新构建并启动服务
./build.sh

# 查看特定服务日志
docker-compose logs koubo-app

# 进入容器内部
docker exec -it koubo-app sh
docker exec -it koubo-mysql mysql -u root -p
docker exec -it koubo-redis redis-cli
```

## 配置说明

### Dockerfile

Dockerfile采用直接拷贝预编译JAR文件的方式构建镜像，避免在Docker内部进行复杂的编译过程。

### docker-compose.yml

定义了三个服务：
1. **koubo-app**: Java后端应用服务（使用预构建的镜像）
2. **koubo-mysql**: MySQL数据库服务
3. **koubo-redis**: Redis缓存服务

## 环境变量

服务间通过环境变量进行配置：

### 后端应用环境变量

- `SPRING_DATASOURCE_URL`: 数据库连接URL
- `SPRING_REDIS_HOST`: Redis主机地址

### MySQL环境变量

- `MYSQL_ROOT_PASSWORD`: root用户密码
- `MYSQL_DATABASE`: 默认创建的数据库名

### Redis

使用默认配置运行。

## 数据持久化

MySQL数据通过Docker卷进行持久化存储：
- 卷名: `koubo-mysql-data`
- 映射路径: `/var/lib/mysql`

## 网络配置

所有服务通过自定义网络`koubo-network`进行通信，确保服务间可以正常访问。

## 故障排除

### 1. 端口冲突

如果本地8080、3306或6379端口已被占用，可以修改docker-compose.yml中的端口映射：

```yaml
ports:
  - "新端口:容器端口"
```

### 2. 构建失败

如果构建过程中出现错误，可以尝试：

```bash
# 清理Docker缓存
docker system prune -f

# 重新构建
docker build --no-cache -t koubo-script-generator .
```

### 3. 数据库连接问题

检查以下配置是否正确：
- 数据库URL是否正确指向koubo-mysql服务
- 数据库用户名和密码是否匹配
- 数据库服务是否正常运行

### 4. 查看日志

通过以下命令查看详细日志信息：

```bash
# 查看所有服务日志
docker-compose logs

# 查看特定服务日志
docker-compose logs koubo-app
```

## API测试

部署成功后，可以通过以下API端点测试服务：

### 用户管理
- `POST /api/users/register` - 用户注册
- `POST /api/users/login` - 用户登录

### 热点数据
- `GET /api/hotspots` - 获取所有热点

### 文案生成
- `POST /api/scripts/generate?userId={userId}&hotspotId={hotspotId}` - AI生成文案

### 提词器
- `POST /api/teleprompter/push?scriptId={scriptId}` - 推送到提词器

## 注意事项

1. 首次部署时，数据库需要一些时间初始化，请耐心等待。
2. 应用启动后，可以通过`docker-compose logs koubo-app`查看启动日志。
3. 生产环境部署时，建议修改默认的密码和密钥配置。
4. 使用构建脚本方式可以避免Docker内部的网络和依赖问题，推荐在本地开发环境中使用。
5. 项目采用本地编译+Docker部署的方式，避免了在容器内编译Java项目可能遇到的依赖和性能问题。