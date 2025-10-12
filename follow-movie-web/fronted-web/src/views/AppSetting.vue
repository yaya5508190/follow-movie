<template>
  <v-container>
    <v-row justify="center">
      <v-col cols="12">
        <v-card
          color="grey-2"
          variant="elevated"
        >
          <v-card-item>
            <div>
              <div class="text-overline mb-0">
                <span class="setting-card-title">下载工具(必须配置)</span>
              </div>
            </div>
          </v-card-item>
          <div class="setting-card-button">
            <v-btn
              block
              class=" mb-1 align-center"
              color="grey-3"
              prepend-icon="mdi-plus"
              size="30"
              variant="flat"
            >
              <template #prepend>
                <v-icon class="setting-card-icon" />
              </template>
              添加
            </v-btn>
          </div>
        </v-card>
      </v-col>
      <v-col cols="12">
        <v-card
          color="grey-2"
          variant="elevated"
        >
          <v-card-item>
            <div>
              <div class="text-overline mb-0">
                <span class="setting-card-title">站点设置</span>
              </div>
            </div>
          </v-card-item>
          <div class="setting-card-button">
            <v-btn
              block
              class=" mb-1 align-center "
              color="grey-3"
              prepend-icon="mdi-plus"
              size="30"
              variant="flat"
            >
              <template #prepend>
                <v-icon class="setting-card-icon" />
              </template>
              添加
            </v-btn>
          </div>
        </v-card>
      </v-col>
      <v-col cols="12">
        <v-card
          color="grey-2"
          variant="elevated"
        >
          <v-card-item>
            <div>
              <div class="text-overline mb-1">
                <span class="setting-card-title">下载设置</span>
              </div>
            </div>
          </v-card-item>
          <div class="setting-card-button">
            <v-btn
              block
              class=" mb-1 align-center "
              color="grey-3"
              prepend-icon="mdi-folder"
              size="30"
              variant="flat"
            >
              <template #prepend>
                <v-icon class="setting-card-icon" />
              </template>
              媒体文件夹
            </v-btn>
          </div>
        </v-card>
      </v-col>
    </v-row>
  </v-container>
</template>

<script lang="ts" setup>
//
</script>

<style scoped>
.setting-card-title {
  font-weight: 500;
  font-size: 1.2em;
  position: sticky;
  top: 0;
  z-index: 1;
}

.setting-card-button {
  padding: 0 8px;
}

.setting-card-icon {
  padding-top: 2px;
}
</style>
