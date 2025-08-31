// scripts/rewrite-prefix.mjs
import fs from 'node:fs'
import fsp from 'node:fs/promises'
import path from 'node:path'
import { loadConfigFromFile } from 'vite'

const DIST = 'dist'

async function getBase() {
  const result = await loadConfigFromFile({ command: 'build', mode: 'production' })
  let base = result?.config?.base || ''
  // 去掉开头 ./ 或 /，只保留核心目录名
  base = base.replace(/^(\.\/|\/)+/, '')
  // 保证末尾 /
  if (!base.endsWith('/')) base += '/'
  return base
}

async function walk(dir, handler) {
  const ents = await fsp.readdir(dir, { withFileTypes: true })
  for (const ent of ents) {
    const full = path.join(dir, ent.name)
    if (ent.isDirectory()) {
      await walk(full, handler)
    } else {
      await handler(full)
    }
  }
}

async function main() {
  const base = await getBase() // 例如 '943a6b1b-.../'
  const target = '/' + base    // "/943a6b1b-.../"
  const replacement = './' + base // "./943a6b1b-.../"

  console.log(`🔍 替换规则: "${target}" -> "${replacement}"`)

  const distDir = path.resolve(DIST)
  await walk(distDir, async (file) => {
    if (/\.(html|js|css|json|map)$/.test(file)) {
      let txt = await fsp.readFile(file, 'utf8')
      if (txt.includes(target)) {
        const replaced = txt.replaceAll(target, replacement)
        await fsp.writeFile(file, replaced, 'utf8')
        console.log('✳️ 已处理:', path.relative(distDir, file))
      }
    }
  })

  console.log('🎉 替换完成')
}

main().catch(err => {
  console.error(err)
  process.exit(1)
})
