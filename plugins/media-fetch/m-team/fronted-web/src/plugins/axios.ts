import type { App } from 'vue'
import axios from 'axios'

const instance = axios.create({
  baseURL: './0fb0cde6-545c-4172-82b8-d2404dbbfb51',
  timeout: 5000,
})

export default {
  install (app: App) {
    app.config.globalProperties.$axios = instance
  },
}

export { instance as axiosInstance }
