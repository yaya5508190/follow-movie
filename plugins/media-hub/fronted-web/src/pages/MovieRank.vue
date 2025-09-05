<script setup lang="ts">
import type {InfiniteScrollStatus} from "@/types/vuetify";
import type {MovieInfo, MovieRankRsp} from "@/types/movie-rank";
import type {AxiosResponse} from "axios";

const tab = ref(null)
const movieInfos: Ref<MovieInfo[]> = ref([])
const categories = ref(['热门', '最新', '豆瓣高分', '冷门佳片'])
let pageNum = 1
const pageSize = 60
let total = -1;

async function fetchMovie(): Promise<AxiosResponse<MovieRankRsp>> {
  return axiosInstance.get<MovieRankRsp>("/api/movieRank/", {
    params: {
      pageNum: pageNum,
      pageSize: pageSize,
      category: tab.value,
    }
  })
}

async function loadMovie({done}: { done: (status: InfiniteScrollStatus) => void; }) {
  if (total > 0) {
    if ((pageNum * pageSize) - total > pageSize) {
      done('empty')
      return
    }
  } else if (total === 0) {
    done('empty')
    return
  }
  const res = await fetchMovie()
  movieInfos.value.push(...res.data.records)
  total = res.data.total
  pageNum++
  done('ok')
}

function changeTab() {
  movieInfos.value = []
  total = -1;
  pageNum = 1
}
</script>

<template>
  <meta name="referrer" content="no-referrer">
  <v-card style="height: calc(100vh - 68px); overflow-y: auto;">
    <v-tabs
      v-model="tab"
      align-tabs="start"
    >
      <v-tab v-for=" (category, n) in categories" :value="category" :key="n" @click="changeTab">{{ category }}</v-tab>
    </v-tabs>

    <v-tabs-window v-model="tab" style="height: calc(100vh - 68px - 48px);overflow-y: auto">
      <v-tabs-window-item
        v-for=" (category, n) in categories"
        :value="category"
        :key="n"
      >
        <v-infinite-scroll :key="n" :items="movieInfos" @load="loadMovie">
          <v-container fluid>
            <v-row>
              <v-col
                v-for="(movieInfo, i) in movieInfos"
                :key="i"
                cols="4"
                xl="1"
                lg="2"
                md="2"
                sm="3"
              >
                <MovieCard
                  :image-url="movieInfo.coverPic"
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
