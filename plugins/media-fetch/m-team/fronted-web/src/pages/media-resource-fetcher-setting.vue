<script setup lang="ts">
import {ref, computed, onMounted} from 'vue'
import {showMessage} from '@/plugins/showMessage'
import {useEventBus} from '@/plugins/useEventBus'
import {axiosInstance} from '@/plugins/axios'

const emit = defineEmits(['dialog:close'])
// 使用事件总线
const eventBus = useEventBus()

// 关闭弹窗
const closeSettingDialog = () => {
  emit('dialog:close', false)
}

//定义props action，支持 insert 或 update
const props = defineProps<{
  action: 'insert' | 'update'
  id?: number
}>()

// 根据 action 显示不同的标题
const title = computed(() => {
  return props.action === 'insert' ? 'M-TEAM设置 - 新增' : 'M-TEAM设置 - 编辑'
})

// 表单引用
const formRef = ref()

// 表单数据
const formData = ref({
  id: null as number | null,
  name: 'M-TEAM',
  url: 'https://api.m-team.cc',
  apiKey: '',
  authType: 1,
  fetcherSource: 'm-team',
  pluginId: '',
})

// 表单校验规则
const rules = {
  name: [
    (v: string) => !!v || '站点名称不能为空',
    (v: string) => (v && v.length <= 50) || '站点名称不能超过50个字符',
  ],
  url: [
    (v: string) => !!v || '站点URL不能为空',
    (v: string) => {
      try {
        new URL(v)
        return true
      } catch {
        return '请输入有效的URL地址'
      }
    },
  ],
  apiKey: [
    (v: string) => !!v || 'API Key不能为空',
    (v: string) => (v && v.length >= 10) || 'API Key至少需要10个字符',
  ],
}

// 加载状态
const loading = ref(false)

// 删除确认对话框
const showDeleteDialog = ref(false)

// 删除设置
const handleDelete = () => {
  showDeleteDialog.value = true
}

// 确认删除
const confirmDelete = async () => {
  if (!props.id) return

  try {
    loading.value = true
    const response = await axiosInstance.post(`/api/mediaResource/deleteSetting/${props.id}`)

    if (response.data.code === 10000) {
      console.log('删除成功')
      showMessage('删除成功', 'success', eventBus)
      showDeleteDialog.value = false
      closeSettingDialog()
    } else {
      console.error('删除失败:', response.data.message)
      showMessage('删除失败: ' + (response.data.message || '未知错误'), 'error', eventBus)
    }
  } catch (error) {
    console.error('删除配置失败:', error)
    showMessage('删除配置失败，请检查网络连接', 'error', eventBus)
  } finally {
    loading.value = false
  }
}

// 获取设置
const getSetting = async (id: number) => {
  try {
    loading.value = true
    const response = await axiosInstance.post(`/api/mediaResource/getSetting/${id}`)

    if (response.data.code === 10000 && response.data.data) {
      const data = response.data.data
      formData.value = {
        id: data.id || null,
        name: data.name || 'M-TEAM',
        url: data.url || 'https://api.m-team.cc',
        apiKey: data.apiKey || '',
        authType: data.authType || 1,
        fetcherSource: data.fetcherSource || 'm-team',
        pluginId: data.pluginId,
      }
    } else {
      console.error('获取配置失败:', response.data.message)
      showMessage('获取配置失败: ' + (response.data.message || '未知错误'), 'error', eventBus)
    }
  } catch (error) {
    console.error('获取配置失败:', error)
    showMessage('获取配置失败，请检查网络连接', 'error', eventBus)
  } finally {
    loading.value = false
  }
}

// 保存设置
const saveSettings = async () => {
  // 先进行表单校验
  const {valid} = await formRef.value.validate()
  if (!valid) {
    showMessage('请检查表单填写是否正确', 'warning', eventBus)
    return
  }

  try {
    loading.value = true

    // 新增时，清空id
    if (props.action === 'insert') {
      formData.value.id = null
    }

    const response = await axiosInstance.post('/api/mediaResource/saveSetting', formData.value)

    if (response.data.code === 10000) {
      console.log('保存成功')
      showMessage('保存成功', 'success', eventBus)
      closeSettingDialog()
    } else {
      console.error('保存失败:', response.data.message)
      showMessage('保存失败: ' + (response.data.message || '未知错误'), 'error', eventBus)
    }
  } catch (error) {
    console.error('保存配置失败:', error)
    showMessage('保存配置失败，请联系管理员', 'error', eventBus)
  } finally {
    loading.value = false
  }
}

// 组件挂载时，如果是更新模式则获取配置
onMounted(() => {
  if (props.action === 'update' && props.id) {
    getSetting(props.id)
  }
})
</script>

<template>
  <v-card>
    <v-card-title class="d-flex justify-space-between align-center">
      <span class="text-h5">{{ title }}</span>
      <v-btn icon="mdi-close" variant="text" @click="closeSettingDialog"/>
    </v-card-title>
    <v-card-text>
      <v-form ref="formRef">
        <v-text-field
          v-model="formData.name"
          label="站点名称"
          required
          variant="outlined"
          class="mb-3"
          :disabled="loading"
          :rules="rules.name"
        />
        <v-text-field
          v-model="formData.url"
          label="站点URL"
          required
          variant="outlined"
          class="mb-3"
          :disabled="loading"
          :rules="rules.url"
          placeholder="https://api.m-team.cc"
        />
        <v-text-field
          v-model="formData.apiKey"
          label="API Key"
          required
          variant="outlined"
          class="mb-3"
          :disabled="loading"
          :rules="rules.apiKey"
        />
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
