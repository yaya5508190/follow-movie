<template>
    <media-resource-downloader-setting />
</template>
<script setup lang="ts">

// 定义 props
defineProps<{
  action: 'insert' | 'update'
}>()

const emit = defineEmits(['dialog:close'])
// 关闭弹窗
const closeSettingDialog = () => {
  emit('dialog:close', false)
}
</script>
