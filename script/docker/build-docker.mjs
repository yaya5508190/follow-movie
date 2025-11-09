#!/usr/bin/env node

import { execSync } from 'child_process';
import { join, dirname } from 'path';
import { fileURLToPath } from 'url';
import { existsSync, mkdirSync, copyFileSync, readdirSync, statSync, rmSync, writeFileSync } from 'fs';

const __filename = fileURLToPath(import.meta.url);
const __dirname = dirname(__filename);

// 颜色定义
const colors = {
  reset: '\x1b[0m',
  green: '\x1b[32m',
  red: '\x1b[31m',
  yellow: '\x1b[33m',
  blue: '\x1b[34m',
};

// 项目根目录
const projectRoot = join(__dirname, '../..');
const distDir = join(__dirname, 'dist');

// 日志函数
function log(message, color = colors.reset) {
  console.log(`${color}${message}${colors.reset}`);
}

function logStep(step, message) {
  console.log(`\n${colors.blue}[步骤 ${step}]${colors.reset} ${message}`);
}

// 清理并创建目录
function prepareDist() {
  logStep(1, '准备 dist 目录...');
  
  if (existsSync(distDir)) {
    log(`  清理旧的 dist 目录...`, colors.yellow);
    rmSync(distDir, { recursive: true, force: true });
  }
  
  mkdirSync(distDir, { recursive: true });
  mkdirSync(join(distDir, 'libs'), { recursive: true });
  mkdirSync(join(distDir, 'plugins'), { recursive: true });
  
  log(`  ✓ dist 目录创建完成`, colors.green);
}

// 执行前端构建
function buildFrontend() {
  logStep(2, '执行前端构建...');
  
  const buildScript = join(__dirname, '../fronted/build-all.mjs');
  
  try {
    execSync(`node "${buildScript}"`, {
      cwd: projectRoot,
      stdio: 'inherit',
      shell: true,
    });
    log(`  ✓ 前端构建完成`, colors.green);
  } catch (error) {
    log(`  ✗ 前端构建失败`, colors.red);
    process.exit(1);
  }
}

// 执行 Maven 打包
function buildBackend() {
  logStep(3, '执行后端构建...');
  
  try {
    execSync('mvn clean package -Dmaven.test.skip=true', {
      cwd: projectRoot,
      stdio: 'inherit',
      shell: true,
    });
    log(`  ✓ 后端构建完成`, colors.green);
  } catch (error) {
    log(`  ✗ 后端构建失败`, colors.red);
    process.exit(1);
  }
}

// 复制文件
function copyFile(src, dest, description) {
  if (!existsSync(src)) {
    log(`  ✗ 文件不存在: ${src}`, colors.red);
    throw new Error(`文件不存在: ${src}`);
  }
  
  copyFileSync(src, dest);
  log(`  ✓ 已复制: ${description}`, colors.green);
}

// 复制目录
function copyDirectory(src, dest) {
  if (!existsSync(src)) {
    log(`  ✗ 目录不存在: ${src}`, colors.red);
    return 0;
  }
  
  if (!existsSync(dest)) {
    mkdirSync(dest, { recursive: true });
  }
  
  const files = readdirSync(src);
  let count = 0;
  
  for (const file of files) {
    const srcPath = join(src, file);
    const destPath = join(dest, file);
    const stat = statSync(srcPath);
    
    if (stat.isDirectory()) {
      copyDirectory(srcPath, destPath);
    } else {
      copyFileSync(srcPath, destPath);
      count++;
    }
  }
  
  return count;
}

// 收集主应用文件
function collectMainApp() {
  logStep(4, '收集主应用文件...');
  
  const targetDir = join(projectRoot, 'follow-movie-web/target');
  
  // 查找 JAR 文件
  const files = readdirSync(targetDir);
  const jarFile = files.find(f => f.endsWith('.jar') && !f.endsWith('-sources.jar'));
  
  if (!jarFile) {
    log(`  ✗ 找不到 JAR 文件`, colors.red);
    process.exit(1);
  }
  
  // 复制 JAR
  copyFile(
    join(targetDir, jarFile),
    join(distDir, jarFile),
    `主应用 JAR: ${jarFile}`
  );
  
  // 复制 libs 目录
  const libDir = join(targetDir, 'libs');
  if (existsSync(libDir)) {
    const count = copyDirectory(libDir, join(distDir, 'libs'));
    log(`  ✓ 已复制 ${count} 个依赖库文件`, colors.green);
  } else {
    log(`  ⚠ libs 目录不存在: ${libDir}`, colors.yellow);
  }
  
  // 复制 application.yml
  copyFile(
    join(projectRoot, 'follow-movie-web/src/main/resources/application.yml'),
    join(distDir, 'application.yml'),
    'application.yml'
  );
  
  // 复制 SQL 初始化脚本
  const sqlFile = join(projectRoot, 'common-repository/src/main/resources/sql/0.0.1.sql');
  if (existsSync(sqlFile)) {
    copyFile(
      sqlFile,
      join(distDir, '0.0.1.sql'),
      'SQL 初始化脚本'
    );
  } else {
    log(`  ⚠ SQL 文件不存在: ${sqlFile}`, colors.yellow);
  }

  return jarFile;
}

// 收集插件 JAR
function collectPlugins() {
  logStep(5, '收集插件 JAR...');
  
  const pluginsRoot = join(projectRoot, 'plugins');
  const pluginDirs = [
    'media-download/q-bittorrent',
    'media-fetch/m-team',
    'media-hub',
    'pre-auth/z-space',
  ];
  
  let pluginCount = 0;
  
  for (const pluginPath of pluginDirs) {
    const targetDir = join(pluginsRoot, pluginPath, 'target');
    
    if (!existsSync(targetDir)) {
      log(`  ⚠ 插件 target 目录不存在: ${pluginPath}`, colors.yellow);
      continue;
    }
    
    const files = readdirSync(targetDir);
    const jarFile = files.find(f => f.endsWith('.jar') && !f.endsWith('-sources.jar') && !f.includes('common'));
    
    if (jarFile) {
      copyFile(
        join(targetDir, jarFile),
        join(distDir, 'plugins', jarFile),
        `插件: ${jarFile}`
      );
      pluginCount++;
    } else {
      log(`  ⚠ 未找到插件 JAR: ${pluginPath}`, colors.yellow);
    }
  }
  
  log(`  ✓ 共收集 ${pluginCount} 个插件`, colors.green);
}

// 生成 Dockerfile
function generateDockerfile(jarFile) {
  logStep(6, '生成 Dockerfile...');
  
  const dockerfile = `FROM eclipse-temurin:17-jdk-alpine

WORKDIR /app

# 复制依赖库
COPY libs/ /app/libs/

# 复制插件
COPY plugins/ /app/plugins/

# 复制配置文件
COPY application.yml /app/application.yml

# 复制主应用 JAR
COPY ${jarFile} /app/app.jar

# 暴露端口
EXPOSE 8080

# 设置环境变量
ENV JAVA_OPTS="-Xms512m -Xmx1024m" \\
    SPRING_DATASOURCE_URL="jdbc:postgresql://localhost:5432/follow_movie" \\
    SPRING_DATASOURCE_USERNAME="postgres" \\
    SPRING_DATASOURCE_PASSWORD="123" \\
    PLUGIN_PATH="/app/plugins" \\
    PLUGIN_SCAN="true" \\
    PLUGIN_CONTEXT_PATH="/"

# 启动应用
ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -Dspring.datasource.url=$SPRING_DATASOURCE_URL -Dspring.datasource.username=$SPRING_DATASOURCE_USERNAME -Dspring.datasource.password=$SPRING_DATASOURCE_PASSWORD -Dplugin.path=$PLUGIN_PATH -Dplugin.scan=$PLUGIN_SCAN -Dplugin.context-path=$PLUGIN_CONTEXT_PATH -jar /app/app.jar"]
`;
  
  writeFileSync(join(distDir, 'Dockerfile'), dockerfile);
  log(`  ✓ Dockerfile 已生成`, colors.green);
}

// 生成 docker-compose.yml
function generateDockerCompose() {
  logStep(7, '生成 docker-compose.yml...');
  
  const dockerCompose = `version: '3.8'

services:
  follow-movie:
    build: .
    container_name: follow-movie-app
    ports:
      - "8080:8080"
    environment:
      - SPRING_DATASOURCE_URL=jdbc:postgresql://postgres:5432/follow_movie
      - SPRING_DATASOURCE_USERNAME=postgres
      - SPRING_DATASOURCE_PASSWORD=123
      - PLUGIN_PATH=/app/plugins
      - PLUGIN_SCAN=true
      - PLUGIN_CONTEXT_PATH=/
    volumes:
      - ./plugins:/app/plugins
      - ./application.yml:/app/application.yml
    depends_on:
      - postgres
    restart: unless-stopped

  postgres:
    image: postgres:15-alpine
    container_name: follow-movie-db
    environment:
      - POSTGRES_DB=follow_movie
      - POSTGRES_USER=postgres
      - POSTGRES_PASSWORD=123
    ports:
      - "5433:5432"
    volumes:
      - postgres_data:/var/lib/postgresql/data
      - ./0.0.1.sql:/docker-entrypoint-initdb.d/0.0.1.sql
    restart: unless-stopped

volumes:
  postgres_data:
`;
  
  writeFileSync(join(distDir, 'docker-compose.yml'), dockerCompose);
  log(`  ✓ docker-compose.yml 已生成`, colors.green);
}

// 生成 README
function generateReadme() {
  logStep(8, '生成 README...');

  const readme = `# Follow Movie Docker 部署

## 目录结构

\`\`\`
dist/
├── follow-movie-web-*.jar    # 主应用 JAR
├── application.yml            # 配置文件
├── 0.0.1.sql                 # 数据库初始化脚本
├── libs/                      # 依赖库
├── plugins/                   # 插件目录
│   ├── q-bittorrent-*.jar
│   ├── m-team-*.jar
│   ├── media-hub-*.jar
│   └── z-space-*.jar
├── Dockerfile                 # Docker 镜像定义
└── docker-compose.yml         # Docker Compose 配置
\`\`\`

## 快速开始

### 1. 构建 Docker 镜像

\`\`\`bash
docker build -t follow-movie:latest .
\`\`\`

### 2. 启动服务

使用 Docker Compose 启动（包含数据库）：
\`\`\`bash
docker-compose up -d
\`\`\`

或仅启动应用容器：
\`\`\`bash
docker run -d \\
  -p 8080:8080 \\
  -v $(pwd)/plugins:/app/plugins \\
  -v $(pwd)/application.yml:/app/application.yml \\
  --name follow-movie-app \\
  follow-movie:latest
\`\`\`

### 3. 访问应用

浏览器访问: http://localhost:8080

### 4. 停止服务

\`\`\`bash
docker-compose down
\`\`\`

## 配置说明

### 修改数据库连接

编辑 \`application.yml\` 文件，修改数据库连接信息：

\`\`\`yaml
spring:
  datasource:
    url: jdbc:postgresql://your-db-host:5432/follow_movie
    username: your-username
    password: your-password
\`\`\`

### 修改插件路径

在 \`docker-compose.yml\` 中修改 \`PLUGIN_PATH\` 环境变量。

### 修改端口

在 \`docker-compose.yml\` 中修改端口映射：
\`\`\`yaml
ports:
  - "你的端口:8080"
\`\`\`

## 日志查看

\`\`\`bash
# 查看应用日志
docker logs -f follow-movie-app

# 查看数据库日志
docker logs -f follow-movie-db
\`\`\`

## 常见问题

### 端口被占用
修改 docker-compose.yml 中的端口映射。

### 数据库连接失败
确保数据库服务已启动，检查网络连接。

### 插件加载失败
检查 plugins 目录挂载是否正确。
`;
  
  writeFileSync(join(distDir, 'README.md'), readme);
  log(`  ✓ README.md 已生成`, colors.green);
}

// 主函数
function main() {
  console.log('=====================================');
  console.log('Follow Movie Docker 打包脚本');
  console.log('=====================================');
  
  try {
    prepareDist();
    buildFrontend();
    buildBackend();
    const jarFile = collectMainApp();
    collectPlugins();
    generateDockerfile(jarFile);
    generateDockerCompose();
    generateReadme();
    
    console.log('\n=====================================');
    log('✓ Docker 打包完成！', colors.green);
    console.log('=====================================');
    log(`\n输出目录: ${distDir}`, colors.blue);
    log('\n下一步:', colors.yellow);
    log('  1. cd script/docker/dist');
    log('  2. docker build -t follow-movie:latest .');
    log('  3. docker-compose up -d\n');
    
  } catch (error) {
    console.error('\n=====================================');
    log('✗ 打包失败', colors.red);
    console.error('=====================================');
    console.error(error);
    process.exit(1);
  }
}

// 执行
main();

