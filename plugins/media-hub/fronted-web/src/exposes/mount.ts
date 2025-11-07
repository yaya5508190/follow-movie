// remote/src/exposes/mount.ts
import { createApp } from 'vue'
import { createVuetify } from 'vuetify'
import * as components from 'vuetify/components'
import * as directives from 'vuetify/directives'
import Root from './RemoteRoot.vue'
import { setAppConfig } from '@/config/app-config'

let app: ReturnType<typeof createApp> | null = null

export function mount (el: Element, props?: Record<string, any>, vuetify?: any) {
  if (app) {
    return
  }
  if (!vuetify) {
    vuetify = createVuetify({ components, directives })
  }
  app = createApp(Root, props)
  app.use(vuetify)
  app.mount(el)
}

export function unmount () {
  if (app) {
    app.unmount()
    app = null
  }
}

// 导出配置函数，供主项目使用
export { setAppConfig }

