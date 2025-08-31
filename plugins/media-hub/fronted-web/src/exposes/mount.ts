// remote/src/exposes/mount.ts
import { createApp } from 'vue'
import { createVuetify } from 'vuetify'
import * as components from 'vuetify/components'
import * as directives from 'vuetify/directives'
import Root from './RemoteRoot.vue'

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
