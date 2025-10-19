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

// 加载状态
const loading = ref(false)

// 根据 action 显示不同的标题
const title = computed(() => {
  return props.action === 'insert' ? 'Q-Bittorrent设置 - 新增' : 'Q-Bittorrent设置 - 编辑'
})

// 保存设置
const saveSettings = async () => {

}
</script>

<template>
  <v-card>
    <v-card-title class="d-flex justify-space-between align-center">
      <span class="text-h5">{{ title }}</span>
      <v-btn icon="mdi-close" variant="text" @click="closeSettingDialog"/>
    </v-card-title>
    <v-card-text>
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
