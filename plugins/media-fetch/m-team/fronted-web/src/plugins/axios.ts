import type { App } from 'vue'
import axios from 'axios'
import { triggerUnauthorized } from '@/config/app-config'

const instance = axios.create({
  baseURL: './0fb0cde6-545c-4172-82b8-d2404dbbfb51',
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
