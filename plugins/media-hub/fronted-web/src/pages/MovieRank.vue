<script setup lang="ts">
import type {InfiniteScrollStatus} from "@/types/vuetify";
import type {MovieCache, MovieRankRsp} from "@/types/movie-rank";
import type {AxiosResponse} from "axios";

const currentTab = ref("热门")
const categories = ref(['热门', '最新', '豆瓣高分', '冷门佳片'])
const movieDataCache = ref<MovieCache>({
  '热门': { records: [], pageNum: 1, total: -1 },
  '最新': { records: [], pageNum: 1, total: -1 },
  '豆瓣高分': { records: [], pageNum: 1, total: -1 },
  '冷门佳片': { records: [], pageNum: 1, total: -1 }
});

const pageSize = 60

const infiniteScrollRef = useTemplateRef('scroll')
function resetInfiniteScroll () {
  infiniteScrollRef.value?.["0"].reset()

}

async function fetchMovie(): Promise<AxiosResponse<MovieRankRsp>> {
  return axiosInstance.get<MovieRankRsp>("/api/movieRank/", {
    params: {
      pageNum: movieDataCache.value[currentTab.value].pageNum,
      pageSize: pageSize,
      category: currentTab.value,
    }
  })
}

async function loadMovie({done}: { done: (status: InfiniteScrollStatus) => void; }) {
  const res = await fetchMovie()
  movieDataCache.value[currentTab.value].records.push(...res.data.records)
  movieDataCache.value[currentTab.value].total = res.data.total

  const pageNum = movieDataCache.value[currentTab.value].pageNum
  const total = movieDataCache.value[currentTab.value].total;

  if (total > 0) {
    if ((pageNum * pageSize) > total) {
      done('empty')
      return
    }
  }

  movieDataCache.value[currentTab.value].pageNum ++

  done('ok')
}

function changeTab() {
  movieDataCache.value[currentTab.value].records = []
  resetInfiniteScroll()
  movieDataCache.value[currentTab.value].total = -1
  movieDataCache.value[currentTab.value].pageNum = 1
}

import { PLUGIN_ID } from '@/plugins/axios'

// 将豆瓣图片 URL 转换为代理 URL
function getProxyImageUrl(coverPic: string): string {
  if (!coverPic) return ''
  return `/${PLUGIN_ID}/api/proxy/image?url=${encodeURIComponent(coverPic)}`
}

</script>

<template>
  <meta name="referrer" content="no-referrer">
  <v-card style="height: calc(100vh - 68px); overflow-y: auto;">
    <v-tabs
      v-model="currentTab"
      align-tabs="start"
      @update:model-value="changeTab"
    >
      <v-tab v-for=" (category, n) in categories" :value="category" :key="n">{{ category }}</v-tab>
    </v-tabs>

    <v-tabs-window v-model="currentTab" style="height: calc(100vh - 68px - 48px);overflow-y: auto">
      <v-tabs-window-item
        v-for=" (category, n) in categories"
        :value="category"
        :key="n"
      >
        <v-infinite-scroll v-if="currentTab === category" ref="scroll" :key="n" :items="movieDataCache[category].records" @load="loadMovie">
          <template v-if="movieDataCache[category].total != -1" #empty>
            <div class="pa-8 text-center text-disabled">
              <p>
                <v-icon icon="mdi-emoticon-sad-outline" size="large"></v-icon>
              </p>
              <p>已经到底部啦~</p>
            </div>
          </template>
          <v-container fluid>
            <v-row>
              <v-col
                v-for="(movieInfo, i) in movieDataCache[category].records"
                :key="i"
                cols="4"
                xl="1"
                lg="2"
                md="2"
                sm="3"
              >
                <MovieCard
                  :image-url="getProxyImageUrl(movieInfo.coverPic)"
                  :name="movieInfo.title"
                  :rating="movieInfo.rating"
                  :description="movieInfo.desc"
                  :uri="movieInfo.uri"
                />
              </v-col>
            </v-row>
          </v-container>
        </v-infinite-scroll>

      </v-tabs-window-item>
    </v-tabs-window>
  </v-card>
</template>

<style scoped lang="sass">

</style>
