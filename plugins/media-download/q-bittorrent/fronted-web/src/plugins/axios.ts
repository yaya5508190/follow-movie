import type { App } from 'vue'
import axios from 'axios'

const instance = axios.create({
  baseURL: './cc659cb6-9d35-4eef-bb08-4602eab88671',
  timeout: 5000,
})

export default {
  install (app: App) {
    app.config.globalProperties.$axios = instance
  },
}

export { instance as axiosInstance }
