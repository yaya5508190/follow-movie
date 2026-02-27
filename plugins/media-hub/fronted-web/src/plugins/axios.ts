import type { App } from 'vue'
import axios from 'axios'
import { triggerUnauthorized } from '@/config/app-config'

export const PLUGIN_ID = '9d220be1-8f62-4819-8f00-03911dc00a95'

const instance = axios.create({
  baseURL: `./${PLUGIN_ID}`,
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
