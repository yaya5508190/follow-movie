import type { App } from 'vue'
import axios from 'axios'

const instance = axios.create({
  baseURL: './67c6ec24-a9a7-430e-ae2f-321ab5fe7589',
  timeout: 5000,
})

export default {
  install (app: App) {
    app.config.globalProperties.$axios = instance
  },
}

export { instance as axiosInstance }
