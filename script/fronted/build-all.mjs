#!/usr/bin/env node

import { execSync } from 'child_process';
import { join, dirname } from 'path';
import { fileURLToPath } from 'url';

const __filename = fileURLToPath(import.meta.url);
const __dirname = dirname(__filename);

// 颜色定义
const colors = {
  reset: '\x1b[0m',
  green: '\x1b[32m',
  red: '\x1b[31m',
  yellow: '\x1b[33m',
};

// 项目配置
const projects = [
  {
    name: 'follow-movie-web',
    path: '../../follow-movie-web/fronted-web',
    command: 'build',
  },
  {
    name: 'q-bittorrent',
    path: '../../plugins/media-download/q-bittorrent/fronted-web',
    command: 'build-with-prefix',
  },
  {
    name: 'z-space',
    path: '../../plugins/pre-auth/z-space/fronted-web',
    command: 'build-with-prefix',
  },
  {
    name: 'media-hub',
    path: '../../plugins/media-hub/fronted-web',
    command: 'build-with-prefix',
  },
  {
    name: 'm-team',
    path: '../../plugins/media-fetch/m-team/fronted-web',
    command: 'build-with-prefix',
  },
];

// 构建单个项目
function buildProject(project, index, total) {
  console.log(`\n[${index}/${total}] 构建 ${project.name}...`);

  const projectPath = join(__dirname, project.path);

  try {
    execSync(`npm run ${project.command}`, {
      cwd: projectPath,
      stdio: 'inherit',
      shell: true,
    });
    console.log(`${colors.green}✓ ${project.name} 构建完成${colors.reset}`);
  } catch (error) {
    console.error(`${colors.red}错误: ${project.name} 构建失败${colors.reset}`);
    process.exit(1);
  }
}

// 主函数
function main() {
  console.log('=====================================');
  console.log('开始构建所有前端项目');
  console.log('=====================================');

  const total = projects.length;
  projects.forEach((project, index) => {
    buildProject(project, index + 1, total);
  });

  console.log('\n=====================================');
  console.log(`${colors.green}✓ 所有前端项目构建完成！${colors.reset}`);
  console.log('=====================================');
}

// 执行
main();

