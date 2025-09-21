/**
 * plugins/index.ts
 *
 * Automatically included in `./src/main.ts`
 */

// Plugins
import vuetify from './vuetify'
import axios from './axios'

// Types
import type { App } from 'vue'

export function registerPlugins (app: App) {
  app.use(vuetify)
    .use(axios)
}
