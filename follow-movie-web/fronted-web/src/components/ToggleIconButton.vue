<!--一个按钮图标切换组件，点击一下切换成另一个图标，图标传递数组，初始化的图标使用第0个,支持绑定事件-->
<template>
  <v-btn
    :icon="icons[currentIconIndex]"
    v-bind="$attrs"
    @click="currentIconIndex = (currentIconIndex + 1) % icons.length"
  />
</template>
<script setup lang="ts">
  defineProps<{
    icons: string[]
  }>()

  const currentIconIndex = ref(0)
</script>
