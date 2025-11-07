import type { AxiosError } from 'axios'
import { defineStore } from 'pinia'
import { ref } from 'vue'
import { axiosInstance } from '@/plugins/axios'

interface User {
  userName: string
}

export const useAuthStore = defineStore('auth', () => {
  const user = ref<User | null>(null)
  const isAuthenticated = ref(false)

  async function login (username: string, password: string): Promise<void> {
    try {
      const params = new URLSearchParams()
      params.append('username', username)
      params.append('password', password)
      const response = await axiosInstance.post('/auth/login', params, {
        headers: {
          'Content-Type': 'application/x-www-form-urlencoded',
        },
      })
      if (response.data.code === 10_000) {
        user.value = response.data.data
        isAuthenticated.value = true
      } else {
        throw new Error(response.data.message)
      }
    } catch (error_) {
      const error = error_ as AxiosError<{ message: string }>
      isAuthenticated.value = false
      user.value = null
      throw new Error(error.response?.data?.message || '登录失败')
    }
  }

  async function logout (): Promise<void> {
    try {
      await axiosInstance.post('/auth/logout')
    } finally {
      isAuthenticated.value = false
      user.value = null
    }
  }

  async function checkAuthStatus (): Promise<void> {
    // This is a placeholder. In a real app, you would have an endpoint
    // to verify the session is still valid.
    // For now, we'll just rely on the isAuthenticated flag.
    // If the page is reloaded, the user will be logged out.
    // A more robust solution would involve checking a session cookie or token.
  }

  return {
    user,
    isAuthenticated,
    login,
    logout,
    checkAuthStatus,
  }
})
