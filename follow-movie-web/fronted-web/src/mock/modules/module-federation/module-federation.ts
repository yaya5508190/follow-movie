import type { MFConfig } from '@/types/module-federation.ts'
import Mock from 'mockjs'

export default function setup () {
  Mock.mock('./api/plugin/module-federation', 'get', () => {
    return {
      remotes: [
        { alias: 'remote', name: 'remote', entry: 'http://192.168.123.249:3002/mf-manifest.json' },
        // { alias: 'vueViteRemote', name: 'vueViteRemote', entry: 'http://localhost:5174/remoteEntry.js' },
      ],
      menus: [
        { name: '微应用页面1', path: '/remote1', component: 'remote/App', parent: false, children: [] },
      ],
      components: [
        {
          pluginId: '0fb0cde6-545c-4172-82b8-d2404dbbfb51',
          type: 'mediaResourceFetcher',
          meta: '馒头',
          componentName: 'App',
          component: 'remote/App',
          desc: '',
        },
        {
          pluginId: '0fb0cde6-545c-4172-82b8-d2404dbbfb51',
          type: 'mediaResourceFetcherSetting',
          meta: '馒头(M-Team)',
          componentName: 'App-Setting',
          component: 'remote/App-Setting',
          desc: '',
        },
      ],
    } satisfies MFConfig
  })
}
