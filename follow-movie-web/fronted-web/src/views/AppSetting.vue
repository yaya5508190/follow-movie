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
                <span class="setting-card-title">下载工具</span>
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
              <v-menu activator="parent">
                <v-list>
                  <v-list-item
                    v-for="(setting, index) in mediaResourceDownloaderSettings"
                    :key="index"
                    :value="setting.component"
                    @click="() => openSettingDialog(setting,'insert')"
                  >
                    <v-list-item-title>{{ setting.meta }}</v-list-item-title>
                  </v-list-item>
                </v-list>
              </v-menu>
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
            <!-- 展示已配置的站点列表 -->
            <div v-if="loading" class="text-center pa-4">
              <v-progress-circular color="primary" indeterminate />
            </div>
            <v-list v-else-if="fetcherSettings.length > 0" bg-color="transparent" class="pa-0">
              <v-list-item
                v-for="config in fetcherSettings"
                :key="config.id"
                class="mb-1 rounded"
                color="grey-3"
                @click="() => handleEditSetting(config)"
              >
                <v-list-item-title>{{ config.name }}</v-list-item-title>
                <template #append>
                  <v-icon icon="mdi-chevron-right" size="small" />
                </template>
              </v-list-item>
            </v-list>
            <div v-else class="text-center pa-4 text-grey">
              暂无站点配置
            </div>
            <v-btn
              block
              class=" mb-1 align-center "
              color="grey-3"
              prepend-icon="mdi-plus"
              size="30"
              variant="flat"
            >
              <v-menu activator="parent">
                <v-list>
                  <v-list-item
                    v-for="(setting, index) in mediaResourceFetcherSettings"
                    :key="index"
                    :value="setting.component"
                    @click="() => openSettingDialog(setting,'insert')"
                  >
                    <v-list-item-title>{{ setting.meta }}</v-list-item-title>
                  </v-list-item>
                </v-list>
              </v-menu>
              <template #prepend>
                <v-icon class="setting-card-icon" />
              </template>
              添加
            </v-btn>
          </div>
        </v-card>
      </v-col>
      <!--      <v-col cols="12">-->
      <!--        <v-card-->
      <!--          color="grey-2"-->
      <!--          variant="elevated"-->
      <!--        >-->
      <!--          <v-card-item>-->
      <!--            <div>-->
      <!--              <div class="text-overline mb-1">-->
      <!--                <span class="setting-card-title">下载设置</span>-->
      <!--              </div>-->
      <!--            </div>-->
      <!--          </v-card-item>-->
      <!--          <div class="setting-card-button">-->
      <!--            <v-btn-->
      <!--              block-->
      <!--              class=" mb-1 align-center "-->
      <!--              color="grey-3"-->
      <!--              prepend-icon="mdi-folder"-->
      <!--              size="30"-->
      <!--              variant="flat"-->
      <!--            >-->
      <!--              <template #prepend>-->
      <!--                <v-icon class="setting-card-icon" />-->
      <!--              </template>-->
      <!--              媒体文件夹-->
      <!--            </v-btn>-->
      <!--          </div>-->
      <!--        </v-card>-->
      <!--      </v-col>-->
    </v-row>
    <!-- 新增：弹窗内容 -->
    <v-dialog v-model="settingDialog" max-width="800px" persistent scrollable>
      <PluginView
        v-if="currentSetting"
        :action="currentAction"
        :plugin-name="currentSetting.component"
        @dialog:close="closeSettingDialog"
      />
    </v-dialog>
  </v-container>
</template>

<script lang="ts" setup>
  import type { BasicMediaFetchConfig } from '@/types/media-fetch-config.ts'
  import type { PluginComponent } from '@/types/module-federation.ts'
  import { computed, onMounted, ref } from 'vue'
  import { queryAllFetcherSettings } from '@/api/media-fetch-config.ts'
  import { useModuleFederation } from '@/stores/module-federation.ts'

  const moduleFederation = useModuleFederation()
  const mediaResourceFetcherSettings = computed<PluginComponent[]>(() => {
    return moduleFederation.mfConfig.components.filter(
      component => component.type === 'mediaResourceFetcherSetting',
    )
  })

  const mediaResourceDownloaderSettings = computed<PluginComponent[]>(() => {
    return moduleFederation.mfConfig.components.filter(
      component => component.type === 'mediaResourceDownloaderSetting',
    )
  })

  // 新增：弹窗控制和当前setting
  const settingDialog = ref(false)
  const currentSetting = ref<PluginComponent | null>(null)
  const currentAction = ref<'insert' | 'update'>('insert')

  // 新增：站点配置列表和加载状态
  const fetcherSettings = ref<BasicMediaFetchConfig[]>([])
  const loading = ref(false)

  // 加载站点配置列表
  const loadFetcherSettings = async () => {
    loading.value = true
    try {
      const result = await queryAllFetcherSettings()
      if (result.code === 10_000) {
        fetcherSettings.value = result.data
      }
    } catch (error) {
      console.error('加载站点配置失败:', error)
    } finally {
      loading.value = false
    }
  }

  // 打开弹窗
  const openSettingDialog = (setting: PluginComponent, action: 'insert' | 'update') => {
    currentSetting.value = setting
    currentAction.value = action
    settingDialog.value = true
  }

  // 关闭弹窗
  const closeSettingDialog = () => {
    settingDialog.value = false
    currentSetting.value = null
    // 重新加载列表
    loadFetcherSettings()
  }

  // 编辑站点配置
  const handleEditSetting = (config: BasicMediaFetchConfig) => {
    // 根据 fetcherSource 找到对应的 setting
    const setting = mediaResourceFetcherSettings.value.find(
      s => s.pluginId === config.pluginId,
    )
    if (setting) {
      openSettingDialog(setting, 'update')
    }
  }

  // 组件挂载时加载数据
  onMounted(() => {
    loadFetcherSettings()
  })
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
