<script setup lang="ts">
import {ref, computed, watchEffect, nextTick} from 'vue';
import type {MediaResource, MediaResourcePageReq, PageResult} from '@/types/media-resource-fetcher';
import MediaCard from '@/components/media-card.vue';
import {axiosInstance} from "@/plugins/axios";

//选择的chip
const selectedChips = ref<string[]>(['movie', 'tv', 'anime'])

//定义props keywork
const props = defineProps<{
  keyword: string
}>()

const chips = [
  {label: '电影', value: 'movie'},
  {label: '电视', value: 'tv'},
  {label: '动画', value: 'anime'},
]

const chipCategoryMapping = {
  movie: ["401", "419", "420", "421", "439"],
  tv: ["403", "402", "435", "438"],
  anime: ["405"],
}

const searchResult = ref<PageResult<MediaResource> | null>(null);
const loading = ref(false);
const page = ref(1);

// 标记是否由分页触发的请求
const pageChanged = ref(false);

// 当用户通过分页器触发翻页，标记为分页触发并手动设置 page（避免 v-model 先触发搜索）
const onPaginationUpdate = (newPage: number) => {
  pageChanged.value = true;
  page.value = newPage;
}

// 当 chip 组更新时，重置到第一页，但不要标记为分页触发
const onChipsUpdate = (_newValue: string[]) => {
  page.value = 1;
  pageChanged.value = false;
}

//computed 选中chip对应的类别
const selectedCategories = computed(() => {
  const categories = new Set<string>()
  selectedChips.value.forEach(chip => {
    const mappedCategories = chipCategoryMapping[chip as keyof typeof chipCategoryMapping]
    if (mappedCategories) {
      mappedCategories.forEach(category => categories.add(category))
    }
  })
  return Array.from(categories)
})

watchEffect(async () => {
  if (props.keyword && selectedCategories.value.length > 0) {
    loading.value = true;
    const req: MediaResourcePageReq = {
      keyword: props.keyword,
      pageNum: page.value,
      pageSize: 50, // Adjusted for list view
      extra: {
        categories: selectedCategories.value
      }
    };
    try {
      const response = await axiosInstance.post<PageResult<MediaResource>>('/api/mediaResource/search', req);
      searchResult.value = response.data;
      // 如果是分页触发的请求，在内容更新并渲染后通知聚合组件滚动到顶部
      if (pageChanged.value) {
        await nextTick();
        window.dispatchEvent(new CustomEvent('aggregate-scroll-top'));
        pageChanged.value = false;
      }
    } catch (error) {
      console.error('查询资源异常:', error);
    } finally {
      loading.value = false;
    }
  } else {
    searchResult.value = null;
  }
});

const totalPages = computed(() => {
  if (!searchResult.value || !searchResult.value.total) return 0;
  return Math.ceil(searchResult.value.total / searchResult.value.pageSize);
});

</script>

<template>
  <v-chip-group filter multiple v-model="selectedChips" @update:modelValue="onChipsUpdate">
    <v-chip
      v-for="chip in chips"
      :key="chip.value"
      :value="chip.value"
      class="ma-2"
      color="primary"
      variant="tonal"
    >
      {{ chip.label }}
    </v-chip>
  </v-chip-group>
  <v-divider></v-divider>
  <v-container fluid>
    <v-row>
      <v-col v-if="loading" v-for="n in 50" :key="n" cols="12" sm="6">
        <v-skeleton-loader class="mb-4" type="list-item-avatar-three-line"></v-skeleton-loader>
      </v-col>
      <v-col v-else-if="searchResult && searchResult.records.length > 0"
             v-for="item in searchResult?.records"
             :key="item.id" cols="12" sm="6">
        <media-card
          :key="item.id"
          :resource="item"
        />
      </v-col>
      <v-col v-else cols="12" class="d-flex flex-column align-center justify-center text-center" style="min-height: 240px;">
        <v-icon size="48" color="grey" class="mb-2">mdi-emoticon-sad-outline</v-icon>
        <div class="text-subtitle-1 font-weight-medium mb-1">空空如也 — 哦豁，资源被小精灵偷走了</div>
        <div class="text-caption text-medium-emphasis mb-4">试试换个关键词。别担心，我们已通知管理员（好吧其实没通知）</div>
      </v-col>
    </v-row>
    <v-pagination
      v-if="totalPages > 1"
      size="small"
      :model-value="page"
      :length="totalPages"
      class="mt-4"
      @update:modelValue="onPaginationUpdate"
    ></v-pagination>
  </v-container>

</template>

<style scoped lang="sass">

</style>
