<script setup lang="ts">
import { ref, computed, onMounted, watch } from 'vue'
import type { SysPreAuthInput } from '@/types/sys-pre-auth'
import { AuthType, CredentialType } from '@/types/sys-pre-auth'
import { saveSetting, getSetting } from '@/api/z-space-setting'

const emit = defineEmits(['dialog:close'])

// 定义props action，支持 insert 或 update
const props = defineProps<{
  action: 'insert' | 'update'
  id?: number
}>()

// 加载状态
const loading = ref(false)

// 根据 action 显示不同的标题
const title = computed(() => {
  return props.action === 'insert' ? '极空间设置 - 新增' : '极空间设置 - 编辑'
})

// 凭据类型选项
const credentialTypeOptions = [
  { title: 'Cookie', value: CredentialType.COOKIE }
]

// 表单数据
const formData = ref<SysPreAuthInput>({
  id: undefined,
  authName: '',
  authUrl: '',
  userName: '',
  password: '',
  // 默认认证类型为 无
  authType: AuthType.NONE,
  credential: '',
  credentialType: CredentialType.COOKIE,
  extraMetainfo: '',
  pluginId: '67c6ec24-a9a7-430e-ae2f-321ab5fe7589'
})

// 当选择 AuthType.NONE 时，自动清空与认证相关的敏感字段
watch(
  () => formData.value.authType,
  (newVal) => {
    if (newVal === AuthType.NONE) {
      formData.value.authUrl = ''
      formData.value.userName = ''
      formData.value.password = ''
      formData.value.credential = ''
      // 保留 credentialType 为默认
      formData.value.credentialType = CredentialType.COOKIE
    }
  }
)

// 表单校验规则
const rules = {
  authName: [
    (v: string) => !!v || '认证名称不能为空'
  ],
  authUrl: [
    // 当选择 "无" 时，认证 URL 可为空且不校验
    (v: string) => {
      if (formData.value.authType === AuthType.NONE) return true
      return !!v || '认证URL不能为空'
    },
    (v: string) => {
      if (formData.value.authType === AuthType.NONE) return true
      try {
        new URL(v)
        return true
      } catch {
        return '请输入有效的URL'
      }
    }
  ],
  userName: [
    (v: string) => {
      if (formData.value.authType === AuthType.USERNAME_PASSWORD && !v) {
        return '用户名不能为空'
      }
      return true
    }
  ],
  password: [
    (v: string) => {
      if (formData.value.authType === AuthType.USERNAME_PASSWORD && !v) {
        return '密码不能为空'
      }
      return true
    }
  ]
}

// 表单引用
const form = ref()

// 关闭弹窗
const closeSettingDialog = () => {
  emit('dialog:close', false)
}

// 加载设置数据
const loadSettings = async () => {
  if (props.action === 'update' && props.id) {
    loading.value = true
    try {
      const result = await getSetting(props.id)
      if (result.code === 10000 && result.data) {
        Object.assign(formData.value, result.data)
      }
    } catch (error) {
      console.error('加载设置失败:', error)
    } finally {
      loading.value = false
    }
  }
}

// 保存设置
const saveSettings = async () => {
  // 验证表单
  const { valid } = await form.value.validate()
  if (!valid) {
    return
  }

  loading.value = true
  try {
    const result = await saveSetting(formData.value)
    if (result.code === 10000) {
      emit('dialog:close', true)
    } else {
      console.error('保存失败:', result.message)
    }
  } catch (error) {
    console.error('保存设置失败:', error)
  } finally {
    loading.value = false
  }
}

// 密码可见性控制
const showPassword = ref(false)

// 组件挂载时加载数据
onMounted(() => {
  loadSettings()
})
</script>

<template>
  <v-card>
    <v-card-title class="d-flex justify-space-between align-center">
      <span class="text-h5">{{ title }}</span>
      <v-btn icon="mdi-close" variant="text" @click="closeSettingDialog"/>
    </v-card-title>
    <v-card-text>
      <v-form ref="form" @submit.prevent="saveSettings">
        <v-row>
          <v-col cols="12">
            <v-text-field
              v-model="formData.authName"
              label="认证名称"
              placeholder="请输入认证名称，例如：我的极空间"
              :rules="rules.authName"
              variant="outlined"
              density="comfortable"
              required
            />
          </v-col>

          <!-- 认证 URL 放在认证类型下方，当类型为 NONE 时隐藏 -->
          <v-col v-if="formData.authType !== AuthType.NONE" cols="12">
            <v-text-field
              v-model="formData.authUrl"
              label="认证URL"
              placeholder="请输入极空间访问地址，例如：http://192.168.1.100:5055"
              :rules="rules.authUrl"
              variant="outlined"
              density="comfortable"
              required
            />
          </v-col>
          <v-col v-if="formData.authType === AuthType.USERNAME_PASSWORD" cols="12">
            <v-text-field
              v-model="formData.userName"
              label="用户名"
              placeholder="请输入用户名"
              :rules="rules.userName"
              variant="outlined"
              density="comfortable"
              required
            />
          </v-col>

          <v-col v-if="formData.authType === AuthType.USERNAME_PASSWORD" cols="12">
            <v-text-field
              v-model="formData.password"
              label="密码"
              placeholder="请输入密码"
              :rules="rules.password"
              :type="showPassword ? 'text' : 'password'"
              :append-inner-icon="showPassword ? 'mdi-eye-off' : 'mdi-eye'"
              @click:append-inner="showPassword = !showPassword"
              variant="outlined"
              density="comfortable"
              required
            />
          </v-col>

          <v-col v-if="formData.authType === AuthType.API_KEY" cols="12">
            <v-text-field
              v-model="formData.credential"
              label="API Key"
              placeholder="请输入API Key"
              variant="outlined"
              density="comfortable"
            />
          </v-col>

          <v-col v-if="formData.authType === AuthType.NONE" cols="12">
            <v-select
              v-model="formData.credentialType"
              label="凭据类型"
              :items="credentialTypeOptions"
              variant="outlined"
              density="comfortable"
            />
          </v-col>

          <v-col v-if="formData.authType === AuthType.NONE" cols="12">
            <v-textarea
              v-model="formData.credential"
              label="认证凭据"
              placeholder="请输入认证凭据"
              variant="outlined"
              density="comfortable"
              rows="3"
            />
          </v-col>
        </v-row>
      </v-form>
    </v-card-text>
    <v-card-actions>
      <v-spacer/>
      <v-btn variant="text" @click="closeSettingDialog" :disabled="loading">取消</v-btn>
      <v-btn
        color="primary"
        variant="flat"
        @click="saveSettings"
        :loading="loading"
        :disabled="loading"
      >
        {{ props.action === 'insert' ? '添加' : '保存' }}
      </v-btn>
    </v-card-actions>
  </v-card>
</template>

<style scoped>

</style>
