import { createApp } from 'vue'
import { createVuetify } from 'vuetify'
import * as components from 'vuetify/components'
import * as directives from 'vuetify/directives'
import RemoteRoot from './remote-root.vue'
import { setAppConfig } from '@/config/app-config'

let app: ReturnType<typeof createApp> | null = null

export function mount (el: Element, props?: Record<string, any>, vuetify?: any) {
  if (app) {
    return
  }
  if (!vuetify) {
    vuetify = createVuetify({ components, directives })
  }
  app = createApp(RemoteRoot, props)
  app.use(vuetify)

  // 如果传入了事件总线，通过 provide 注入到整个子应用
  if (props?.eventBus) {
    app.provide('eventBus', props.eventBus)
  }

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
