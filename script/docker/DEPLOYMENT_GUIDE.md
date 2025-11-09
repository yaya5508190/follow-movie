# Follow Movie Docker 部署完整指南

> **版本:** 1.0.0  
> **更新日期:** 2025-11-09  
> **维护者:** Follow Movie Team

---

## 📖 目录

- [快速开始](#快速开始)
- [功能说明](#功能说明)
- [目录结构](#目录结构)
- [环境要求](#环境要求)
- [使用方法](#使用方法)
- [构建流程](#构建流程)
- [Docker 部署](#docker-部署)
- [数据库初始化](#数据库初始化)
- [配置说明](#配置说明)
- [使用场景](#使用场景)
- [故障排查](#故障排查)
- [常用命令](#常用命令)
- [最佳实践](#最佳实践)

---

## 快速开始

### 一键打包并部署

**Windows 用户:**

```bash
# 1. 执行打包脚本
cd C:\develop\x-project\follow-movie\script\docker
build-docker.bat

# 2. 构建并启动服务
cd dist
docker build -t follow-movie:latest .
docker-compose up -d

# 3. 访问应用
# 浏览器打开: http://localhost:8080
# 默认用户名: admin, 密码: 12345678
```

**Linux/Mac 用户:**

```bash
# 1. 执行打包脚本
cd /path/to/follow-movie/script/docker
chmod +x build-docker.sh
./build-docker.sh

# 2. 构建并启动服务
cd dist
docker build -t follow-movie:latest .
docker-compose up -d

# 3. 访问应用
# 浏览器打开: http://localhost:8080
# 默认用户名: admin, 密码: 12345678
```

---

## 功能说明

Docker 打包脚本会自动完成以下任务：

### 自动构建

1. **前端构建** - 调用 `script/fronted/build-all.mjs` 构建所有前端项目
   - follow-movie-web 前端
   - q-bittorrent 插件前端
   - z-space 插件前端
   - media-hub 插件前端
   - m-team 插件前端

2. **后端构建** - 执行 Maven 打包
   ```bash
   mvn clean package -Dmaven.test.skip=true
   ```

### 文件收集

自动收集并组织以下文件到 `dist` 目录：

- ✅ 主应用 JAR (`follow-movie-web-0.0.1-SNAPSHOT.jar`)
- ✅ 依赖库目录 (150+ 个 JAR 文件)
- ✅ 配置文件 (`application.yml`)
- ✅ SQL 初始化脚本 (`0.0.1.sql`)
- ✅ 插件 JAR 文件 (4 个插件)
  - q-bittorrent-0.0.1-SNAPSHOT.jar
  - m-team-0.0.1-SNAPSHOT.jar
  - media-hub-0.0.1-SNAPSHOT.jar
  - z-space-0.0.1-SNAPSHOT.jar

### Docker 配置生成

自动生成以下文件：

- ✅ `Dockerfile` - Docker 镜像定义（基于 eclipse-temurin:17-jdk-alpine）
- ✅ `docker-compose.yml` - Docker Compose 配置（包含 PostgreSQL 数据库）
- ✅ `build-docker-image.bat` - Windows 镜像构建脚本
- ✅ `build-docker-image.sh` - Linux/Mac 镜像构建脚本
- ✅ `README.md` - 部署文档

---

## 目录结构

### 脚本目录

```
script/docker/
├── build-docker.mjs          # 主打包脚本 (Node.js)
├── build-docker.bat          # Windows 启动脚本
├── build-docker.sh           # Linux/Mac 启动脚本
├── test-build.mjs            # 测试工具（检查文件是否就绪）
└── dist/                     # 输出目录（执行后生成）
```

### 输出目录 (dist/)
dist/
├── follow-movie-web-0.0.1-SNAPSHOT.jar    # 主应用 JAR (~6 MB)
├── application.yml                         # Spring Boot 配置文件
├── 0.0.1.sql                              # 数据库初始化脚本
├── libs/                                   # 依赖库 (150+ 个文件)
│   ├── spring-boot-*.jar
│   ├── postgresql-*.jar
│   └── ...
├── plugins/                                # 插件目录 (4 个文件)
│   ├── q-bittorrent-0.0.1-SNAPSHOT.jar    # BT 下载插件 (~5 MB)
│   ├── m-team-0.0.1-SNAPSHOT.jar          # M-Team 站点插件 (~5 MB)
├── Dockerfile                              # Docker 镜像定义
├── DEPLOYMENT_GUIDE.md       # 部署完整指南（本文档）
├── docker-compose.yml                      # Docker Compose 配置
├── build-docker-image.bat                  # Windows 构建脚本
├── build-docker-image.sh                   # Linux/Mac 构建脚本
└── README.md                               # 部署文档
```
cd C:\develop\x-project\follow-movie\script\docker
**总大小:** 约 30-40 MB（不包括 libs 目录中的依赖）

---

## 环境要求

### 构建环境

| 工具 | 最低版本 | 用途 |
|------|---------|------|
| Node.js | 16.x | 执行前端构建和打包脚本 |
| Maven | 3.6.x | Java 项目打包 |
| Java JDK | 11+ | 本地构建（运行时需要 17） |

### 运行环境

| Docker | 20.x | 构建和运行容器 |
| Docker Compose | 1.29.x | 多容器编排 |

### 磁盘空间

- 构建过程：约 500 MB
- dist 输出：约 30-40 MB
- Docker 镜像：约 400 MB

---

## 使用方法

### 方式 1: 使用启动脚本（推荐）

**Windows:**

```bash
cd C:\develop\x-project\follow-movie\script\docker
build-docker.bat
```

**Linux/Mac:**

```bash
cd /path/to/follow-movie/script/docker
chmod +x build-docker.sh
./build-docker.sh
```

### 方式 2: 直接使用 Node.js

```bash
cd script/docker
### 方式 1: 直接执行打包脚本（推荐）
### 方式 3: 检查后再构建

```bash

- 清理旧的 dist 目录（如果存在）
- 创建新的目录结构（dist、libs、plugins）
### 方式 2: 检查后再构建
### 步骤 2: 执行前端构建

调用 `script/fronted/build-all.mjs` 构建所有前端项目：
cd C:\develop\x-project\follow-movie\script\docker
```
✓ follow-movie-web 前端构建完成
✓ q-bittorrent 前端构建完成
✓ z-space 前端构建完成
✓ media-hub 前端构建完成
✓ m-team 前端构建完成
```

### 步骤 3: 执行后端构建

运行 Maven 打包命令：

```bash
mvn clean package -Dmaven.test.skip=true
```

### 步骤 4: 收集主应用文件

- 复制主应用 JAR: `follow-movie-web-0.0.1-SNAPSHOT.jar`
- 复制依赖库: 从 `follow-movie-web/target/libs/` 复制 150+ 个依赖文件
- 复制配置文件: `application.yml`
- 复制 SQL 脚本: `0.0.1.sql`

### 步骤 5: 收集插件 JAR

从各插件项目的 target 目录收集 JAR 文件：

- `plugins/media-download/q-bittorrent/target/` → `q-bittorrent-*.jar`
- `plugins/media-fetch/m-team/target/` → `m-team-*.jar`
- `plugins/media-hub/target/` → `media-hub-*.jar`
- `plugins/pre-auth/z-space/target/` → `z-space-*.jar`

**注意:** 脚本会自动跳过文件名包含 "common" 的 JAR 文件。

### 步骤 6: 生成 Dockerfile

生成基于 `eclipse-temurin:17-jdk-alpine` 的 Dockerfile，包含：

- JVM 参数配置
- 环境变量设置
- 应用启动命令

### 步骤 7: 生成 docker-compose.yml

生成 Docker Compose 配置，包含：

- follow-movie 应用服务
- PostgreSQL 数据库服务（端口 5433）
- 自动挂载 SQL 初始化脚本

### 步骤 8: 生成辅助文件

- Docker 镜像构建脚本
- 部署文档

---

## Docker 部署

### 构建 Docker 镜像

进入 dist 目录后，有以下几种方式构建镜像：

**方式 1: 使用生成的脚本**

```bash
cd dist

# Windows
build-docker-image.bat

# Linux/Mac
chmod +x build-docker-image.sh
./build-docker-image.sh
```

**方式 2: 直接使用 Docker 命令**

```bash
cd dist
docker build -t follow-movie:latest .
```

### 启动服务

**使用 Docker Compose（推荐）:**

```bash
cd dist
docker-compose up -d
```

### 步骤 8: 生成部署文档

- 生成 README.md 部署文档
  - 端口: `8080`
  - 连接到内部 PostgreSQL 数据库
  
- ✅ PostgreSQL 数据库
  - 容器名: `follow-movie-db`
  - 端口: `5433` (避免与已有的 PostgreSQL 冲突)
  - 数据库名: `follow_movie`
  - 用户名: `postgres`
进入 dist 目录后构建镜像：
```

### 停止服务

```bash
# 停止服务（保留数据）
docker-compose stop

# 停止并删除容器（保留数据卷）
docker-compose down

# 停止并删除容器和数据卷（危险！会清空数据库）
docker-compose down -v
```

### 访问应用

- **应用地址:** http://localhost:8080
- **数据库地址:** localhost:5433
- **默认管理员账号:**
  - 用户名: `admin`
  - 密码: `12345678`

---

## 数据库初始化

### 自动初始化

Docker Compose 会在 PostgreSQL 容器**首次启动**时自动执行 `0.0.1.sql` 脚本。

### 工作原理

1. `docker-compose.yml` 将 `0.0.1.sql` 挂载到容器的 `/docker-entrypoint-initdb.d/` 目录
2. PostgreSQL 镜像在首次启动且数据库为空时，自动执行该目录下的所有 `.sql` 文件
3. 脚本执行顺序按文件名字母排序

### 创建的数据库表

`0.0.1.sql` 脚本会创建以下表：

1. **media_fetch_config** - 媒体抓取配置（下载站点认证信息）
2. **media_torrent_record** - 种子下载记录
3. **download_tool_config** - 下载工具配置
4. **media_fetch_download_rel** - 媒体抓取和下载工具关联表
5. **sys_pre_auth** - 系统预认证配置
6. **pre_auth_download_rel** - 预认证和下载工具关联表
7. **sys_user** - 系统用户表（包含默认管理员）

### 查看初始化日志

```bash
# 查看 PostgreSQL 容器日志
docker-compose logs -f postgres
```

成功执行后会看到类似输出：

```
/usr/local/bin/docker-entrypoint.sh: running /docker-entrypoint-initdb.d/0.0.1.sql
CREATE TABLE
CREATE INDEX
INSERT 0 1
...
```

### 验证表是否创建成功

```bash
# 进入 PostgreSQL 容器
docker exec -it follow-movie-db psql -U postgres -d follow_movie

# 查看所有表
\dt

# 查看特定表结构
\d sys_user

# 查看用户数据
SELECT * FROM sys_user;

# 退出
\q
```

### 重新初始化数据库

⚠️ **重要提示:** SQL 脚本只在首次启动时执行一次！

如果需要重新初始化数据库：

```bash
# 停止并删除容器和数据卷
docker-compose down -v

# 重新启动（会自动执行 SQL 脚本）
docker-compose up -d
```

---

## 配置说明

### 环境变量

Dockerfile 中预设的环境变量：

| 变量名 | 默认值 | 说明 |
|--------|--------|------|
| `JAVA_OPTS` | `-Xms512m -Xmx1024m` | JVM 内存参数 |
| `SPRING_DATASOURCE_URL` | `jdbc:postgresql://localhost:5432/follow_movie` | 数据库连接 URL |
| `SPRING_DATASOURCE_USERNAME` | `postgres` | 数据库用户名 |
| `SPRING_DATASOURCE_PASSWORD` | `123` | 数据库密码 |
| `PLUGIN_PATH` | `/app/plugins` | 插件目录路径 |
| `PLUGIN_SCAN` | `true` | 是否自动扫描插件 |
| `PLUGIN_CONTEXT_PATH` | `/` | 插件上下文路径 |

### 修改配置

#### 方式 1: 通过 docker-compose.yml（推荐）

编辑 `dist/docker-compose.yml`，修改 `environment` 部分：

```yaml
services:
  follow-movie:
    environment:
      - SPRING_DATASOURCE_URL=jdbc:postgresql://your-db:5432/your_database
      - SPRING_DATASOURCE_USERNAME=your_user
      - SPRING_DATASOURCE_PASSWORD=your_password
      - JAVA_OPTS=-Xms1g -Xmx2g
```

然后重启服务：

```bash
docker-compose down
docker-compose up -d
```

#### 方式 2: 通过 docker run 参数

```bash
docker run -d \
  --name follow-movie-app \
  -p 8080:8080 \
  -e SPRING_DATASOURCE_URL="jdbc:postgresql://your-db:5432/mydb" \
  -e SPRING_DATASOURCE_USERNAME="admin" \
  -e SPRING_DATASOURCE_PASSWORD="secret" \
  -e JAVA_OPTS="-Xms1g -Xmx2g" \
  follow-movie:latest
```

#### 方式 3: 修改 application.yml

编辑 `dist/application.yml` 文件，然后重启容器：

```bash
docker-compose restart follow-movie
```

### 网络配置

#### 容器间通信

在 `docker-compose.yml` 中，应用使用服务名 `postgres` 连接数据库：

```yaml
SPRING_DATASOURCE_URL=jdbc:postgresql://postgres:5432/follow_movie
```

**注意:** 
- 容器内使用端口 `5432`
- 宿主机访问使用端口 `5433`

#### 使用外部数据库

如果使用外部数据库，可以删除 `docker-compose.yml` 中的 `postgres` 服务：

```yaml
version: '3.8'

services:
  follow-movie:
    image: follow-movie:latest
    ports:
      - "8080:8080"
    environment:
      - SPRING_DATASOURCE_URL=jdbc:postgresql://external-db:5432/follow_movie
      - SPRING_DATASOURCE_USERNAME=postgres
      - SPRING_DATASOURCE_PASSWORD=your_password
    volumes:
      - ./plugins:/app/plugins
      - ./application.yml:/app/application.yml
    restart: unless-stopped
```

### 数据持久化

`docker-compose.yml` 使用命名数据卷 `postgres_data` 持久化数据库数据：

```yaml
volumes:
  postgres_data:
```

**作用:**
- 容器删除后，数据库数据不会丢失
- 容器重启后，数据依然存在

**管理数据卷:**

```bash
# 查看数据卷
docker volume ls

# 查看数据卷详情
docker volume inspect dist_postgres_data

# 删除数据卷（会丢失数据库数据！）
docker-compose down -v
```

---

## 使用场景

### 场景 1: 完整构建和部署

从零开始构建和部署应用：

```bash
# 1. 执行打包
cd C:\develop\x-project\follow-movie\script\docker
build-docker.bat

# 2. 构建镜像并启动
cd dist
docker build -t follow-movie:latest .
docker-compose up -d

# 3. 查看日志
docker-compose logs -f

# 4. 访问应用
# http://localhost:8080
```

### 场景 2: 仅更新后端代码

如果只修改了后端代码：

```bash
# 1. 手动构建后端
cd C:\develop\x-project\follow-movie
mvn clean package -Dmaven.test.skip=true

# 2. 编辑 build-docker.mjs，注释掉前端构建
# // buildFrontend();

# 3. 执行打包
cd script\docker
node build-docker.mjs

# 4. 重新构建镜像和启动
cd dist
docker-compose down
docker build -t follow-movie:latest .
docker-compose up -d
```

### 场景 3: 检查文件是否就绪

在完整构建前，先检查：

```bash
cd script\docker
node test-build.mjs

# 输出示例：
# ✓ 找到 JAR: follow-movie-web-0.0.1-SNAPSHOT.jar
# ✓ libs 目录存在，包含 150 个文件
# ✓ application.yml 存在
# ✓ 找到 4 个插件
```

### 场景 4: 更新插件

只添加或更新插件，无需重建镜像：

```bash
# 1. 复制新插件到 dist/plugins
node build-docker.mjs

# 2. 重启应用容器
cd dist
docker-compose restart follow-movie
```

### 场景 5: 使用外部 PostgreSQL

如果已有 PostgreSQL 服务器：

1. 编辑 `dist/docker-compose.yml`，删除 `postgres` 服务
2. 修改数据库连接配置指向外部数据库
3. 手动执行 `0.0.1.sql` 初始化表结构

```bash
# 手动初始化数据库
psql -h your-db-host -U postgres -d follow_movie -f 0.0.1.sql
```

### 场景 6: 备份和恢复数据库

**备份:**

```bash
# 导出数据库
docker exec follow-movie-db pg_dump -U postgres follow_movie > backup.sql

# 或使用 docker-compose
docker-compose exec postgres pg_dump -U postgres follow_movie > backup_20251109.sql
```

**恢复:**

```bash
# 导入数据库
docker exec -i follow-movie-db psql -U postgres follow_movie < backup.sql

# 或使用 docker-compose
docker-compose exec -T postgres psql -U postgres follow_movie < backup.sql
```

---

## 故障排查

### 问题 1: 前端构建失败

**原因:** npm 依赖未安装

**解决方法:**

```bash
# 进入各个前端项目目录安装依赖
cd follow-movie-web/fronted-web
npm install

cd ../../plugins/media-hub/fronted-web
npm install

# 对其他前端项目重复此操作
```

### 问题 2: Maven 构建失败

**原因:** Maven 依赖下载失败或网络问题

**解决方法:**

```bash
# 清理 Maven 缓存
mvn clean

# 重新构建
mvn clean package -Dmaven.test.skip=true
```

### 问题 3: 找不到 JAR 文件

**原因:** target 目录不存在或构建未完成

**解决方法:**

```bash
# 检查 target 目录
dir follow-movie-web\target

# 手动执行 Maven 构建
cd follow-movie-web
mvn clean package -Dmaven.test.skip=true
```

### 问题 4: Docker 构建失败 - 镜像不存在

**错误信息:**

```
ERROR [internal] load metadata for docker.io/library/openjdk:17-jdk-slim
```

**原因:** `openjdk:17-jdk-slim` 镜像已废弃

**解决方法:**

Dockerfile 已更新为使用 `eclipse-temurin:17-jdk-alpine`，重新执行打包脚本即可。

### 问题 5: SQL 脚本未执行

**原因:** 数据库已存在，PostgreSQL 只在首次启动时执行初始化脚本

**解决方法:**

```bash
# 删除数据卷并重新启动
docker-compose down -v
docker-compose up -d

# 查看初始化日志
docker-compose logs -f postgres
```

### 问题 6: 容器无法访问数据库

**错误信息:**

```
java.net.UnknownHostException: dev-postgres
```

**原因:** 容器不在同一 Docker 网络中

**解决方法:**

使用 Docker Compose 启动，它会自动创建自定义网络并将容器加入：

```bash
docker-compose up -d
```

### 问题 7: 端口被占用

**错误信息:**

```
Error starting userland proxy: listen tcp 0.0.0.0:5432: bind: address already in use
```

**原因:** 端口 5432 已被其他 PostgreSQL 使用

**解决方法:**

已在 `docker-compose.yml` 中配置使用端口 5433，如仍有冲突，可修改：

```yaml
postgres:
  ports:
    - "5434:5432"  # 修改为其他端口
```

---

## 常用命令

### Docker Compose 命令

```bash
# 启动所有服务（后台运行）
docker-compose up -d

# 启动所有服务（前台运行，查看日志）
docker-compose up

# 查看运行状态
docker-compose ps

# 查看所有日志
docker-compose logs -f

# 查看特定服务日志
docker-compose logs -f follow-movie
docker-compose logs -f postgres

# 停止所有服务
docker-compose stop

# 启动已停止的服务
docker-compose start

# 重启所有服务
docker-compose restart

# 重启特定服务
docker-compose restart follow-movie

# 停止并删除容器（保留数据卷）
docker-compose down

# 停止并删除容器和数据卷（危险！）
docker-compose down -v
```

### Docker 容器命令

```bash
# 进入应用容器
docker exec -it follow-movie-app bash

# 进入数据库容器
docker exec -it follow-movie-db bash

# 查看容器日志
docker logs -f follow-movie-app
docker logs -f follow-movie-db

# 查看容器资源使用
docker stats

# 停止容器
docker stop follow-movie-app

# 启动容器
docker start follow-movie-app

# 删除容器
docker rm follow-movie-app
```

### Docker 镜像命令

```bash
# 构建镜像
docker build -t follow-movie:latest .

# 查看镜像列表
docker images

# 删除镜像
docker rmi follow-movie:latest

# 清理未使用的镜像
docker image prune

# 清理所有未使用的镜像
docker image prune -a
```

### 数据库命令

```bash
# 连接数据库
docker exec -it follow-movie-db psql -U postgres -d follow_movie

# 在 psql 中：
\dt          # 查看所有表
\d table_name # 查看表结构
\q           # 退出

# 导出数据库
docker exec follow-movie-db pg_dump -U postgres follow_movie > backup.sql

# 导入数据库
docker exec -i follow-movie-db psql -U postgres follow_movie < backup.sql
```

---

## 最佳实践

### 开发环境

1. **首次使用前检查文件**
   ```bash
   node test-build.mjs
   ```

2. **确保依赖已安装**
   ```bash
   # 在各个前端项目中执行
   npm install
   ```

3. **使用前台模式查看日志**
   ```bash
   docker-compose up
   ```

### 生产环境

1. **修改默认密码**
   
   编辑 `docker-compose.yml`：
   ```yaml
   environment:
     - POSTGRES_PASSWORD=your_strong_password
   ```

2. **使用外部数据库**
   
   不要在容器中运行生产数据库，使用独立的 PostgreSQL 服务器或云数据库。

3. **定期备份数据**
   ```bash
   # 设置定时任务备份
   docker exec follow-movie-db pg_dump -U postgres follow_movie > backup_$(date +%Y%m%d).sql
   ```

4. **限制容器资源**
   
   编辑 `docker-compose.yml`：
   ```yaml
   services:
     follow-movie:
       deploy:
         resources:
           limits:
             cpus: '2.0'
             memory: 2G
   ```

5. **使用环境变量文件**
   
   创建 `.env` 文件：
   ```env
   POSTGRES_PASSWORD=your_password
   SPRING_DATASOURCE_URL=jdbc:postgresql://postgres:5432/follow_movie
   ```

6. **配置日志轮转**
   
   编辑 `docker-compose.yml`：
   ```yaml
   logging:
     driver: "json-file"
     options:
       max-size: "10m"
       max-file: "3"
   ```

### 性能优化

1. **调整 JVM 参数**
   ```dockerfile
   ENV JAVA_OPTS="-Xms1g -Xmx2g -XX:+UseG1GC -XX:MaxGCPauseMillis=200"
   ```

2. **优化 PostgreSQL**
   ```yaml
   postgres:
     command: >
       postgres
       -c shared_buffers=256MB
       -c max_connections=200
       -c effective_cache_size=1GB
   ```

3. **定期清理 Docker**
   ```bash
   # 清理未使用的资源
   docker system prune -a
   ```

---

## 附录

### 项目信息

- **项目名称:** Follow Movie
- **主应用端口:** 8080
- **数据库端口:** 5433 (容器内 5432)
- **数据库名称:** follow_movie
- **默认管理员:** admin / 12345678

### 插件列表

| 插件名称 | 功能说明 | JAR 文件 |
|---------|---------|----------|
| q-bittorrent | BT 下载工具集成 | q-bittorrent-0.0.1-SNAPSHOT.jar |
| m-team | M-Team 站点集成 | m-team-0.0.1-SNAPSHOT.jar |
| media-hub | 媒体中心管理 | media-hub-0.0.1-SNAPSHOT.jar |
| z-space | Z-Space 认证 | z-space-0.0.1-SNAPSHOT.jar |

### 相关链接

- [Docker 官方文档](https://docs.docker.com/)
- [Docker Compose 文档](https://docs.docker.com/compose/)
- [PostgreSQL Docker 镜像](https://hub.docker.com/_/postgres)
- [Spring Boot Docker 指南](https://spring.io/guides/gs/spring-boot-docker/)

---

## 许可证

与主项目相同。

---

**如有问题，请查看故障排查部分或联系项目维护者。**

