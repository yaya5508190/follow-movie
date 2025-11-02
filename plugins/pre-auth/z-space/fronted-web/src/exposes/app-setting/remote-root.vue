<template>
    <pre-auth-setting :action="action" :id="id" @dialog:close="closeSettingDialog"/>
</template>
<script setup lang="ts">

// 定义 props
defineProps<{
  action: 'insert' | 'update'
  id?: number
}>()

const emit = defineEmits(['dialog:close'])
// 关闭弹窗
const closeSettingDialog = (refresh: boolean) => {
  emit('dialog:close', refresh)
}
</script>
