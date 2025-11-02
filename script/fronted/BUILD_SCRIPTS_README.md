# 前端项目构建脚本

本目录包含用于一键构建所有前端项目的脚本。

## 脚本说明

### 1. build-all.bat (Windows)
Windows 批处理脚本，适用于 Windows CMD 或 PowerShell。

**使用方法：**
```cmd
cd script\fronted
build-all.bat
```

### 2. build-all.sh (Linux/macOS)
Shell 脚本，适用于 Linux、macOS 或 Windows Git Bash/WSL。

**使用方法：**
```bash

chmod +x build-all.sh  # 首次使用需添加执行权限
./build-all.sh
```

### 3. build-all.mjs (跨平台 Node.js)
Node.js 脚本，适用于所有安装了 Node.js 的平台。

**使用方法：**
```bash

node build-all.mjs
```

## 构建顺序

脚本会按以下顺序构建项目：

1. **follow-movie-web** (主项目)
   - 路径: `follow-movie-web/fronted-web`
   - 命令: `npm run build`

2. **q-bittorrent** (下载插件)
   - 路径: `plugins/media-download/q-bittorrent/fronted-web`
   - 命令: `npm run build-with-prefix`

3. **z-space** (预认证插件)
   - 路径: `plugins/pre-auth/z-space/fronted-web`
   - 命令: `npm run build-with-prefix`

4. **media-hub** (媒体中心插件)
   - 路径: `plugins/media-hub/fronted-web`
   - 命令: `npm run build-with-prefix`

5. **m-team** (资源获取插件)
   - 路径: `plugins/media-fetch/m-team/fronted-web`
   - 命令: `npm run build-with-prefix`

## 注意事项

1. 构建前请确保所有项目的依赖已安装（运行 `npm install`）
2. 如果某个项目构建失败，脚本会立即停止并返回错误码
3. 构建产物会自动复制到各自的 `src/main/resources/static` 目录
4. 主项目使用 `build` 命令，插件项目使用 `build-with-prefix` 命令

## 错误处理

如果构建失败：
1. 检查终端输出的错误信息
2. 确认失败的项目目录中是否已运行 `npm install`
3. 尝试单独构建失败的项目进行调试

## 推荐使用

- **Windows 用户**: `build-all.bat`
- **Linux/macOS 用户**: `build-all.sh`
- **跨平台/CI/CD**: `build-all.mjs`

