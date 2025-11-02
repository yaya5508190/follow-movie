<script setup lang="ts">
import { ref, computed, onMounted } from 'vue'
import type { DownloadToolConfigInput } from '@/types/download-tool-config'
import { AuthType } from '@/types/download-tool-config'
import { saveSetting, getSetting, deleteSetting } from '@/api/download-tool-config'

const emit = defineEmits(['dialog:close'])

// 定义 props action，支持 insert 或 update
const props = defineProps<{
  action: 'insert' | 'update'
  id?: number
}>()

// 加载状态
const loading = ref(false)

// 根据 action 显示不同的标题
const title = computed(() => {
  return props.action === 'insert' ? 'Q-Bittorrent设置 - 新增' : 'Q-Bittorrent设置 - 编辑'
})

// 认证类型选项
const authTypeOptions = [
  { title: '用户名密码', value: AuthType.USERNAME_PASSWORD },
  { title: 'Cookie', value: AuthType.COOKIE }
]

// 表单数据
const formData = ref<DownloadToolConfigInput>({
  id: undefined,
  name: '',
  authType: AuthType.USERNAME_PASSWORD,
  type: 'q-bittorrent',
  url: '',
  username: '',
  password: '',
  cookie: '',
  savePath: '/downloads',
  defaultTool: false
})

// 表单校验规则
const rules = {
  name: [
    (v: string) => !!v || '名称不能为空'
  ],
  url: [
    (v: string) => !!v || '访问地址不能为空',
    (v: string) => {
      try {
        new URL(v)
        return true
      } catch {
        return '请输入有效的URL'
      }
    }
  ],
  username: [
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
  ],
  cookie: [
    (v: string) => {
      if (formData.value.authType === AuthType.COOKIE && !v) {
        return 'Cookie不能为空'
      }
      return true
    }
  ],
  savePath: [
    (v: string) => !!v || '保存路径不能为空'
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

// 删除确认对话框
const showDeleteDialog = ref(false)

// 删除设置
const handleDelete = () => {
  showDeleteDialog.value = true
}

// 确认删除
const confirmDelete = async () => {
  if (!props.id) return

  loading.value = true
  try {
    const result = await deleteSetting(props.id)
    if (result.code === 10000) {
      showDeleteDialog.value = false
      emit('dialog:close', true)
    } else {
      console.error('删除失败:', result.message)
    }
  } catch (error) {
    console.error('删除设置失败:', error)
  } finally {
    loading.value = false
  }
}

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
              v-model="formData.name"
              label="名称"
              placeholder="请输入配置名称，例如：我的Q-Bittorrent"
              :rules="rules.name"
              variant="outlined"
              density="comfortable"
              required
            />
          </v-col>

          <v-col cols="12">
            <v-text-field
              v-model="formData.url"
              label="访问地址"
              placeholder="请输入访问地址，例如：http://192.168.1.100:8080"
              :rules="rules.url"
              variant="outlined"
              density="comfortable"
              required
            />
          </v-col>

          <v-col cols="12">
            <v-select
              v-model="formData.authType"
              label="认证类型"
              :items="authTypeOptions"
              variant="outlined"
              density="comfortable"
              required
            />
          </v-col>

          <v-col v-if="formData.authType === AuthType.USERNAME_PASSWORD" cols="12">
            <v-text-field
              v-model="formData.username"
              label="用户名"
              placeholder="请输入用户名"
              :rules="rules.username"
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

          <v-col v-if="formData.authType === AuthType.COOKIE" cols="12">
            <v-textarea
              v-model="formData.cookie"
              label="Cookie"
              placeholder="请输入Cookie"
              :rules="rules.cookie"
              variant="outlined"
              density="comfortable"
              rows="3"
              required
            />
          </v-col>

          <v-col cols="12">
            <v-text-field
              v-model="formData.savePath"
              label="默认保存路径"
              placeholder="请输入默认保存路径，例如：/downloads"
              :rules="rules.savePath"
              variant="outlined"
              density="comfortable"
              required
            />
          </v-col>

          <v-col cols="12">
            <v-switch
              v-model="formData.defaultTool"
              label="设为默认下载工具"
              color="primary"
              density="comfortable"
            />
          </v-col>
        </v-row>
      </v-form>
    </v-card-text>
    <v-card-actions>
      <!-- 删除按钮（仅在编辑模式显示） -->
      <v-btn
        v-if="props.action === 'update'"
        color="error"
        variant="text"
        @click="handleDelete"
        :disabled="loading"
      >
        删除
      </v-btn>
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

  <!-- 删除确认对话框 -->
  <v-dialog v-model="showDeleteDialog" max-width="400">
    <v-card>
      <v-card-title class="text-h6">
        确认删除
      </v-card-title>
      <v-card-text>
        确定要删除此配置吗？此操作无法撤销。
      </v-card-text>
      <v-card-actions>
        <v-spacer/>
        <v-btn variant="text" @click="showDeleteDialog = false" :disabled="loading">
          取消
        </v-btn>
        <v-btn
          color="error"
          variant="flat"
          @click="confirmDelete"
          :loading="loading"
          :disabled="loading"
        >
          确认删除
        </v-btn>
      </v-card-actions>
    </v-card>
  </v-dialog>
</template>

<style scoped>

</style>
