<script setup lang="ts">
import {ref, computed, watchEffect} from 'vue';
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
      const response = await axiosInstance.post<PageResult<MediaResource>>('/api/media-resource/search', req);
      searchResult.value = response.data;
    } catch (error) {
      console.error('查询资源异常:', error);
    } finally {
      loading.value = false;
    }
  }else{
    searchResult.value = null;
  }
});

const totalPages = computed(() => {
  if (!searchResult.value || !searchResult.value.total) return 0;
  return Math.ceil(searchResult.value.total / searchResult.value.pageSize);
});

</script>

<template>
  <v-chip-group filter multiple v-model="selectedChips" @update:modelValue="page = 1">
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
      <v-col v-for="item in searchResult?.records" :key="item.id" cols="12" sm="6">
        <div v-if="loading">
          <v-skeleton-loader v-for="n in 5" :key="n" class="mb-4"
                             type="list-item-avatar-three-line"></v-skeleton-loader>
        </div>
        <div v-else-if="searchResult && searchResult.records.length > 0">
          <media-card
            :key="item.id"
            :resource="item"
          />
        </div>
        <div v-else class="text-center py-8">
          <p>没有内容.</p>
        </div>
      </v-col>
    </v-row>
    <v-pagination
      v-if="totalPages > 1"
      v-model="page"
      :length="totalPages"
      class="mt-4"
    ></v-pagination>
  </v-container>

</template>

<style scoped lang="sass">

</style>
