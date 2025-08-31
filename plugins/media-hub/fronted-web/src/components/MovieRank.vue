<script setup lang="ts">
const tab = ref(null)
</script>

<template>
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
        <v-container fluid>
          <v-row>
            <v-col
              v-for="i in 50"
              :key="i"
              cols="4"
              xl="1"
              lg="2"
              md="2"
              sm="3"
            >
              <v-img
                :lazy-src="`https://picsum.photos/10/6?image=${i * n * 5 + 10}`"
                :src="`https://picsum.photos/500/300?image=${i * n * 5 + 10}`"
                width="200"
                height="305"
                cover
              ></v-img>
            </v-col>
          </v-row>
        </v-container>
      </v-tabs-window-item>
    </v-tabs-window>
  </v-card>
</template>

<style scoped lang="sass">

</style>
