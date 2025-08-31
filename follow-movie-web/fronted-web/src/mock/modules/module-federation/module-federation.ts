import type { MFConfig } from '@/types/module-federation.ts'
import Mock from 'mockjs'

export default function setup () {
  Mock.mock('./api/plugin/module-federation', 'get', () => {
    return {
      remotes: [
        { alias: 'remote', name: 'remote', entry: 'http://localhost:3001/mf-manifest.json' },
        // { alias: 'vueViteRemote', name: 'vueViteRemote', entry: 'http://localhost:5174/remoteEntry.js' },
      ],
      menus: [
        { name: '微应用页面', path: '/remote', component: 'remote/App' },
      ],
    } satisfies MFConfig
  })
}
