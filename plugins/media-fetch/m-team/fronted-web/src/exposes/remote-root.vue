<template>
  <media-resource-fetcher :keyword="keyword"/>
</template>
<script setup lang="ts">
  //定义props keywork
  defineProps<{
    keyword: string
  }>()
</script>
