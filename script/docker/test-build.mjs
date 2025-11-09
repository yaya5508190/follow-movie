#!/usr/bin/env node
/**
 * Docker 打包脚本测试工具
 * 用于验证文件路径和收集逻辑，不实际执行构建
 */

import { join, dirname } from 'path';
import { fileURLToPath } from 'url';
import { existsSync, readdirSync } from 'fs';

const __filename = fileURLToPath(import.meta.url);
const __dirname = dirname(__filename);

const colors = {
  reset: '\x1b[0m',
  green: '\x1b[32m',
  red: '\x1b[31m',
  yellow: '\x1b[33m',
  blue: '\x1b[34m',
};

function log(message, color = colors.reset) {
  console.log(`${color}${message}${colors.reset}`);
}

const projectRoot = join(__dirname, '../..');

console.log('=====================================');
console.log('Docker 打包脚本 - 文件检查');
console.log('=====================================\n');

// 检查主应用
log('检查主应用文件:', colors.blue);
const webTarget = join(projectRoot, 'follow-movie-web/target');
if (existsSync(webTarget)) {
  const files = readdirSync(webTarget);
  const jarFile = files.find(f => f.endsWith('.jar') && !f.endsWith('-sources.jar'));

  if (jarFile) {
    log(`  ✓ 找到 JAR: ${jarFile}`, colors.green);
  } else {
    log(`  ✗ 未找到 JAR 文件`, colors.red);
  }

  const libDir = join(webTarget, 'libs');
  if (existsSync(libDir)) {
    const libFiles = readdirSync(libDir);
    log(`  ✓ lib 目录存在，包含 ${libFiles.length} 个文件`, colors.green);
  } else {
    log(`  ✗ lib 目录不存在`, colors.red);
  }

  const ymlFile = join(projectRoot, 'follow-movie-web/src/main/resources/application.yml');
  if (existsSync(ymlFile)) {
    log(`  ✓ application.yml 存在`, colors.green);
  } else {
    log(`  ✗ application.yml 不存在`, colors.red);
  }
} else {
  log(`  ✗ target 目录不存在: ${webTarget}`, colors.red);
}

// 检查插件
log('\n检查插件 JAR:', colors.blue);
const pluginsRoot = join(projectRoot, 'plugins');
const pluginDirs = [
  'media-download/q-bittorrent',
  'media-fetch/m-team',
  'media-hub',
  'pre-auth/z-space',
];

let foundPlugins = 0;
for (const pluginPath of pluginDirs) {
  const targetDir = join(pluginsRoot, pluginPath, 'target');

  if (existsSync(targetDir)) {
    const files = readdirSync(targetDir);
    const jarFile = files.find(f => f.endsWith('.jar') && !f.endsWith('-sources.jar') && !f.includes('common'));

    if (jarFile) {
      log(`  ✓ ${pluginPath}: ${jarFile}`, colors.green);
      foundPlugins++;
    } else {
      log(`  ⚠ ${pluginPath}: 未找到 JAR`, colors.yellow);
    }
  } else {
    log(`  ⚠ ${pluginPath}: target 目录不存在`, colors.yellow);
  }
}

log(`\n总结: 找到 ${foundPlugins} 个插件\n`, colors.blue);

// 检查构建工具
log('检查构建工具:', colors.blue);

const frontedScript = join(__dirname, '../fronted/build-all.mjs');
if (existsSync(frontedScript)) {
  log(`  ✓ 前端构建脚本存在`, colors.green);
} else {
  log(`  ✗ 前端构建脚本不存在: ${frontedScript}`, colors.red);
}

const pomFile = join(projectRoot, 'pom.xml');
if (existsSync(pomFile)) {
  log(`  ✓ Maven pom.xml 存在`, colors.green);
} else {
  log(`  ✗ Maven pom.xml 不存在`, colors.red);
}

console.log('\n=====================================');
log('检查完成！', colors.green);
console.log('=====================================\n');

