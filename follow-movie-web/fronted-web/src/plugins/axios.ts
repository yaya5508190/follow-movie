import type { App } from 'vue'
import axios from 'axios'
import router from '@/router' // Adjust the path as necessary
import { useAuthStore } from '@/stores/auth' // Adjust the path as necessary

const instance = axios.create({
  baseURL: './api',
  timeout: 5000,
})

instance.interceptors.response.use(
  response => response,
  error => {
    if (error.response && error.response.status === 401) {
      const authStore = useAuthStore()
      authStore.logout()
      router.push('/login')
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
