<route lang="yaml">
meta:
  layout: false
</route>

<template>
  <div class="login-page">
    <div class="login-background" />
    <div class="login-overlay" />
    <v-container class="login-container fill-height" fluid>
      <v-row align="center" justify="center">
        <v-col cols="12" lg="4" md="5" sm="8">
          <div class="login-header">
            <div class="logo-container">
              <v-icon class="logo-icon" color="white" size="60">mdi-movie-open</v-icon>
            </div>
            <h1 class="app-title">FollowMovie</h1>
            <p class="app-subtitle">追踪你的电影收藏</p>
          </div>

          <v-card class="login-card elevation-24" rounded="lg">
            <v-card-text class="pa-8">
              <h2 class="text-center mb-6 login-title">欢迎登录</h2>
              <v-form @submit.prevent="handleLogin">
                <v-text-field
                  v-model="username"
                  class="mb-3"
                  data-cy="username"
                  hide-details="auto"
                  label="用户名"
                  name="username"
                  prepend-inner-icon="mdi-account"
                  type="text"
                  variant="outlined"
                />
                <v-text-field
                  v-model="password"
                  class="mb-4"
                  data-cy="password"
                  hide-details="auto"
                  label="密码"
                  name="password"
                  prepend-inner-icon="mdi-lock"
                  type="password"
                  variant="outlined"
                  @keyup.enter="handleLogin"
                />
                <v-alert v-if="errorMessage" class="mb-4" dense type="error">
                  {{ errorMessage }}
                </v-alert>
                <v-btn
                  block
                  class="login-button"
                  color="primary"
                  data-cy="login-button"
                  :loading="loading"
                  size="large"
                  @click="handleLogin"
                >
                  登录
                </v-btn>
              </v-form>
            </v-card-text>
          </v-card>

          <div class="footer-text">
            <p>© 2025 FollowMovie. All rights reserved.</p>
          </div>
        </v-col>
      </v-row>
    </v-container>
  </div>
</template>

<script setup lang="ts">
  import { ref } from 'vue'
  import { useRouter } from 'vue-router'
  import { useAuthStore } from '@/stores/auth'

  const username = ref('')
  const password = ref('')
  const errorMessage = ref('')
  const loading = ref(false)
  const authStore = useAuthStore()
  const router = useRouter()

  const handleLogin = async () => {
    if (!username.value || !password.value) {
      errorMessage.value = '请输入用户名和密码'
      return
    }

    try {
      loading.value = true
      errorMessage.value = ''
      await authStore.login(username.value, password.value)
      const redirect = router.currentRoute.value.query.redirect as string || '/'
      await router.push(redirect)
    } catch (error: any) {
      errorMessage.value = error.message || '登录失败，请检查用户名和密码'
    } finally {
      loading.value = false
    }
  }
</script>

<style scoped>
.login-page {
  position: relative;
  width: 100%;
  height: 100vh;
  overflow: hidden;
}

.login-background {
  position: absolute;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  background-image: url('@/assets/img.png');
  background-size: cover;
  background-position: center;
  background-repeat: no-repeat;
  filter: blur(3px);
  transform: scale(1.1);
}

.login-overlay {
  position: absolute;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  background: linear-gradient(135deg, rgba(156, 39, 176, 0.8) 0%, rgba(103, 58, 183, 0.8) 100%);
}

.login-container {
  position: relative;
  z-index: 1;
  padding: 20px;
}

.login-header {
  text-align: center;
  margin-bottom: 30px;
  animation: fadeInDown 0.8s ease-out;
}

@keyframes fadeInDown {
  from {
    opacity: 0;
    transform: translateY(-30px);
  }
  to {
    opacity: 1;
    transform: translateY(0);
  }
}

.logo-container {
  display: inline-flex;
  align-items: center;
  justify-content: center;
  width: 100px;
  height: 100px;
  background: rgba(255, 255, 255, 0.2);
  border-radius: 50%;
  backdrop-filter: blur(10px);
  border: 3px solid rgba(255, 255, 255, 0.3);
  margin-bottom: 20px;
  animation: pulse 2s ease-in-out infinite;
}

@keyframes pulse {
  0%, 100% {
    transform: scale(1);
    box-shadow: 0 0 0 0 rgba(255, 255, 255, 0.7);
  }
  50% {
    transform: scale(1.05);
    box-shadow: 0 0 20px 10px rgba(255, 255, 255, 0);
  }
}

.logo-icon {
  filter: drop-shadow(0 4px 6px rgba(0, 0, 0, 0.3));
}

.app-title {
  color: white;
  font-size: 48px;
  font-weight: 700;
  margin: 0 0 10px 0;
  text-shadow: 0 4px 12px rgba(0, 0, 0, 0.3);
  letter-spacing: 1px;
}

.app-subtitle {
  color: rgba(255, 255, 255, 0.95);
  font-size: 18px;
  margin: 0;
  text-shadow: 0 2px 8px rgba(0, 0, 0, 0.3);
  font-weight: 300;
}

.login-card {
  backdrop-filter: blur(20px);
  border: 1px solid rgba(255, 255, 255, 0.2);
  animation: fadeInUp 0.8s ease-out;
  overflow: hidden;
}

@keyframes fadeInUp {
  from {
    opacity: 0;
    transform: translateY(30px);
  }
  to {
    opacity: 1;
    transform: translateY(0);
  }
}

.login-title {
  font-weight: 600;
  font-size: 28px;
}

.login-button {
  margin-top: 8px;
  height: 48px !important;
  font-size: 16px;
  font-weight: 600;
  letter-spacing: 0.5px;
  text-transform: none;
  transition: all 0.3s ease;
}

.login-button:hover {
  transform: translateY(-2px);
  box-shadow: 0 8px 16px rgba(33, 150, 243, 0.4);
}

.footer-text {
  text-align: center;
  margin-top: 30px;
  animation: fadeIn 1s ease-out 0.5s both;
}

@keyframes fadeIn {
  from {
    opacity: 0;
  }
  to {
    opacity: 1;
  }
}

.footer-text p {
  color: rgba(255, 255, 255, 0.9);
  font-size: 14px;
  margin: 0;
  text-shadow: 0 2px 4px rgba(0, 0, 0, 0.3);
}

/* 响应式设计 */
@media (max-width: 600px) {
  .app-title {
    font-size: 36px;
  }

  .app-subtitle {
    font-size: 16px;
  }

  .logo-container {
    width: 80px;
    height: 80px;
  }

  .logo-icon {
    font-size: 48px !important;
  }

  .login-card {
    margin: 0 10px;
  }
}
</style>
