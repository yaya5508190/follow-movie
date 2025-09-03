<script setup lang="ts">
const tab = ref(null)
const movieInfos = ref([])
let pageNum = 1
const pageSize = 60
let total =-1;

async function fetchMovie () {
  return axiosInstance.get("/api/movieRank/",{
    params: {
      pageNum: pageNum,
      pageSize: pageSize
    }
  })
}
async function loadMovie ({ done }) {
  if(total > 0) {
    if ((pageNum * pageSize) - total > pageSize) {
      done('empty')
      return
    }
  }
  const res = await fetchMovie()
  movieInfos.value.push(...res.data.records)
  total = res.data.total
  pageNum++
  done('ok')
}
</script>

<template>
  <meta name="referrer" content="no-referrer">
  <v-card style="height: calc(100vh - 68px); overflow-y: auto;">
    <v-tabs
      v-model="tab"
      align-tabs="start"
    >
      <v-tab :value="1">热门电影</v-tab>
      <v-tab :value="2">最新电影</v-tab>
      <v-tab :value="3">豆瓣高分</v-tab>
      <v-tab :value="4">冷门佳片</v-tab>
    </v-tabs>

    <v-tabs-window v-model="tab" style="height: calc(100vh - 68px - 48px);overflow-y: auto">
      <v-tabs-window-item
        v-for="n in 4"
        :key="n"
        :value="n"
      >
<!--        <v-container fluid>-->
<!--          <v-row>-->
<!--            <v-col-->
<!--              v-for="(movieInfo, i) in movieInfos"-->
<!--              :key="i"-->
<!--              cols="4"-->
<!--              xl="1"-->
<!--              lg="2"-->
<!--              md="2"-->
<!--              sm="3"-->
<!--            >-->
<!--              <v-img-->
<!--                :lazy-src="movieInfo.coverPic"-->
<!--                :src="movieInfo.coverPic"-->
<!--                :height="300"-->
<!--                cover-->
<!--              ></v-img>-->
<!--            </v-col>-->
<!--          </v-row>-->
<!--        </v-container>-->
        <v-infinite-scroll :items="movieInfos" @load="loadMovie">
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
                <v-img
                  :lazy-src="movieInfo.coverPic"
                  :src="movieInfo.coverPic"
                  :height="300"
                  cover
                ></v-img>
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
