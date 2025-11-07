import type { App } from 'vue'
import axios from 'axios'
import { triggerUnauthorized } from '@/config/app-config'

const instance = axios.create({
  baseURL: './67c6ec24-a9a7-430e-ae2f-321ab5fe7589',
  timeout: 5000,
})

instance.interceptors.response.use(
  response => response,
  error => {
    if (error.response && error.response.status === 401) {
      console.log("请登录！")
      // 触发主项目的跳转登录页回调
      triggerUnauthorized()
    }
    return Promise.reject(error)
  },
)

export default {
  install (app: App) {
    app.config.globalProperties.$axios = instance
  },
}

export { instance as axiosInstance }
