<template>
  <v-card class="d-flex flex-row mb-2 media-card-fixed-height" flat bordered>
    <div class="flex-shrink-0">
      <v-img
        v-if="!imgLoadingError"
        :src="resource.images && resource.images.length > 0 ? resource.images[0] : ''"
        height="200px"
        width="130px"
        cover
        @error="imgLoadingError = true"
      >
        <template #placeholder>
          <v-row class="fill-height ma-0" align="center" justify="center">
            <v-progress-circular indeterminate color="grey-lighten-5"></v-progress-circular>
          </v-row>
        </template>
      </v-img>
      <div v-else
           style="height:200px;width:130px;display:flex;align-items:center;justify-content:center;background:transparent;">
        <v-icon color="grey" size="40">mdi-image-off</v-icon>
      </div>
    </div>

    <div class="d-flex flex-column flex-grow-1 pa-3 content-wrapper">
      <div class="text-subtitle-1 font-weight-bold text-truncate">
        {{ resource.name }}
      </div>

      <p class="text-caption text-medium-emphasis mt-1 description-text">
        {{ resource.desc }}
      </p>

      <v-spacer></v-spacer>
      <div class="mt-1 chip-group-wrapper">
        <v-chip v-if="resource.imdb" @click="openLink(resource.imdb)" color="orange" prepend-icon="mdi-star-circle"
                size="x-small">
          IMDB: {{ resource.imdbRating }}
        </v-chip>
        <v-chip v-if="resource.douban" @click="openLink(resource.douban)" color="green" prepend-icon="mdi-star-circle"
                size="x-small">
          豆瓣: {{ resource.doubanRating }}
        </v-chip>
      </div>

      <div class="d-flex align-center text-caption">
        <v-icon icon="mdi-arrow-up-bold" size="x-small" class="mr-1" color="green"></v-icon>
        <span class="mr-3">{{ resource.seeders }}</span>
        <v-icon icon="mdi-arrow-down-bold" size="x-small" class="mr-1" color="red"></v-icon>
        <span class="mr-3">{{ resource.leechers }}</span>
        <v-icon icon="mdi-harddisk" size="x-small" class="mr-1"></v-icon>
        <span class="mr-3">{{ formattedSize }}</span>
        <v-progress-circular
          v-if="downloading"
          indeterminate
          size="12"
          width="2"
          color="primary"
        ></v-progress-circular>
        <v-icon
          v-else
          icon="mdi-download"
          size="x-small"
          color="primary"
          class="cursor-pointer"
          @click="handleDownload(resource)"
        ></v-icon>
      </div>

      <div class="mt-1 chip-group-wrapper">
        <v-chip v-for="label in resource.labels" :key="label" size="x-small" variant="tonal">
          {{ label }}
        </v-chip>
      </div>
    </div>
  </v-card>
</template>

<script setup lang="ts">
import {computed} from 'vue';
import type {MediaResource} from '@/types/media-resource-fetcher';
import {axiosInstance} from '@/plugins/axios';

const props = defineProps<{
  resource: MediaResource
}>();

const imgLoadingError = ref(false);
const downloading = ref(false);

const formattedSize = computed(() => {
  const sizeInBytes = props.resource.size;
  if (!sizeInBytes || sizeInBytes === 0) return '0 B';
  const k = 1024;
  const sizes = ['B', 'KB', 'MB', 'GB', 'TB', 'PB', 'EB', 'ZB', 'YB'];
  const i = Math.floor(Math.log(sizeInBytes) / Math.log(k));
  return parseFloat((sizeInBytes / Math.pow(k, i)).toFixed(2)) + ' ' + sizes[i];
});

function openLink(url: string) {
  if (typeof window !== 'undefined' && url) {
    window.open(url, '_blank');
  }
}

async function handleDownload(resource: MediaResource) {
  downloading.value = true;
  try {
    const response = await axiosInstance.post(`/api/mediaResource/downloadTorrent/${resource.id}`);

    if (response.data) {
      console.log('下载种子成功:', resource.id);
      // 可以添加成功提示
    } else {
      console.error('下载种子失败:', resource.id);
      // 可以添加失败提示
    }
  } catch (error) {
    console.error('下载种子出错:', error);
    // 可以添加错误提示
  } finally {
    downloading.value = false;
  }
}
</script>

<style scoped>
.media-card-fixed-height {
  height: 200px;
  min-height: 150px;
}

.content-wrapper {
  overflow: hidden;
}

.description-text {
  display: -webkit-box;
  -webkit-box-orient: vertical;
  -webkit-line-clamp: 4; /* 显示两行，超出省略 */
  overflow: hidden;
  text-overflow: ellipsis;
  word-break: break-all;
  white-space: normal;
  line-height: 1.5em;
  max-height: 6em; /* 1.5em * 2 */
}

.chip-group-wrapper {
  flex-wrap: wrap;
  overflow-y: hidden;
  max-height: 50px; /* Allow for two rows of x-small chips */
}
</style>
