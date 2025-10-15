<script setup lang="ts">
const emit = defineEmits(['dialog:close'])
// 关闭弹窗
const closeSettingDialog = () => {
  emit('dialog:close', false)
}

//定义props action，支持 insert 或 update
const props = defineProps<{
  action: 'insert' | 'update'
}>()

// 根据 action 显示不同的标题
const title = computed(() => {
  return props.action === 'insert' ? 'M-TEAM设置 - 新增' : 'M-TEAM设置 - 编辑'
})

// 表单数据
const formData = ref({
  apiKey: '',
  baseUrl: 'https://api.m-team.cc',
  // 其他配置项
})

// 保存设置
const saveSettings = () => {
  if (props.action === 'insert') {
    // 新增逻辑
    console.log('新增配置:', formData.value)
  } else {
    // 更新逻辑
    console.log('更新配置:', formData.value)
  }
  closeSettingDialog()
}
</script>

<template>
  <v-card>
    <v-card-title class="d-flex justify-space-between align-center">
      <span class="text-h5">{{ title }}</span>
      <v-btn icon="mdi-close" variant="text" @click="closeSettingDialog"/>
    </v-card-title>
    <v-card-text>
      <v-form>
        <v-text-field
          v-model="formData.apiKey"
          label="API Key"
          required
          variant="outlined"
          class="mb-3"
        />
        <v-text-field
          v-model="formData.baseUrl"
          label="Base URL"
          required
          variant="outlined"
          class="mb-3"
        />
      </v-form>
    </v-card-text>
    <v-card-actions>
      <v-spacer/>
      <v-btn variant="text" @click="closeSettingDialog">取消</v-btn>
      <v-btn color="primary" variant="flat" @click="saveSettings">
        {{ props.action === 'insert' ? '添加' : '保存' }}
      </v-btn>
    </v-card-actions>
  </v-card>
</template>

<style scoped>

</style>
