import type { App } from 'vue'
import axios from 'axios'

const instance = axios.create({
  baseURL: './9d220be1-8f62-4819-8f00-03911dc00a95',
  timeout: 5000,
})

export default {
  install (app: App) {
    app.config.globalProperties.$axios = instance
  },
}

export { instance as axiosInstance }
