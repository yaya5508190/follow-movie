import type { App } from 'vue'
import axios from 'axios'

const instance = axios.create({
  baseURL: './api',
  timeout: 5000,
})

export default {
  install (app: App) {
    app.config.globalProperties.$axios = instance
  },
}

export { instance as axiosInstance }
