/**
 * plugins/index.ts
 *
 * Automatically included in `./src/main.ts`
 */

// Types
import type { App } from 'vue'
import { ensureRemotes } from '@/plugins/module-federation.ts'
import router, { mountPluginRoutes } from '../router'

import pinia from '../stores'
import axios from './axios'
// Plugins
import vuetify from './vuetify'

export async function registerPlugins (app: App) {
  app
    .use(vuetify)
    .use(pinia)
    .use(axios)

  await ensureRemotes()
  mountPluginRoutes()

  app.use(router)
}
