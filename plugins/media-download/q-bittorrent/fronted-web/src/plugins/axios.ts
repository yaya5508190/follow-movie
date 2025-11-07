import type { App } from 'vue'
import axios from 'axios'
import { triggerUnauthorized } from '@/config/app-config'

const instance = axios.create({
  baseURL: './cc659cb6-9d35-4eef-bb08-4602eab88671',
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
