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
            <!-- 展示已配置的下载工具列表 -->
            <div v-if="downloadToolLoading" class="text-center pa-4">
              <v-progress-circular color="primary" indeterminate />
            </div>
            <v-list v-else-if="downloadToolConfigs.length > 0" bg-color="transparent" class="pa-0">
              <v-list-item
                v-for="config in downloadToolConfigs"
                :key="config.id"
                class="mb-1 rounded"
                color="grey-3"
                @click="() => handleEditSetting(config.id, config.pluginId)"
              >
                <v-list-item-title>{{ config.name }}</v-list-item-title>
                <template #append>
                  <v-icon icon="mdi-chevron-right" size="small" />
                </template>
              </v-list-item>
            </v-list>
            <div v-else class="text-center pa-4 text-grey">
              暂无下载工具配置
            </div>
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
                @click="() => handleEditSetting(config.id, config.pluginId)"
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
      <v-col cols="12">
        <v-card
          color="grey-2"
          variant="elevated"
        >
          <v-card-item>
            <div>
              <div class="text-overline mb-0">
                <span class="setting-card-title">NAS集成设置</span>
              </div>
            </div>
          </v-card-item>
          <div class="setting-card-button">
            <!-- 展示已配置的NAS集成设置列表 -->
            <div v-if="preAuthLoading" class="text-center pa-4">
              <v-progress-circular color="primary" indeterminate />
            </div>
            <v-list v-else-if="preAuthConfigs.length > 0" bg-color="transparent" class="pa-0">
              <v-list-item
                v-for="config in preAuthConfigs"
                :key="config.id"
                class="mb-1 rounded"
                color="grey-3"
                @click="() => handleEditSetting(config.id, config.pluginId)"
              >
                <v-list-item-title>{{ config.authName }}</v-list-item-title>
                <template #append>
                  <v-icon icon="mdi-chevron-right" size="small" />
                </template>
              </v-list-item>
            </v-list>
            <div v-else class="text-center pa-4 text-grey">
              暂无NAS集成设置
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
                    v-for="(setting, index) in preAuthSettings"
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
    </v-row>
    <!-- 新增：弹窗内容 -->
    <v-dialog
      v-model="settingDialog"
      content-class="dialog-with-max-height"
      max-width="800px"
      persistent
      scrollable
    >
      <PluginView
        v-if="currentSetting"
        :id="currentId"
        :action="currentAction"
        :plugin-name="currentSetting.component"
        @dialog:close="closeSettingDialog"
      />
    </v-dialog>
  </v-container>
</template>

<script lang="ts" setup>
  import type { BasicDownloadToolConfig } from '@/types/download-tool-config.ts'
  import type { BasicMediaFetchConfig } from '@/types/media-fetch-config.ts'
  import type { PluginComponent } from '@/types/module-federation.ts'
  import type { BasicPreAuthConfig } from '@/types/sys-pre-auth.ts'
  import { computed, onMounted, ref } from 'vue'
  import { queryAllDownloadToolSettings } from '@/api/download-tool-config.ts'
  import { queryAllFetcherSettings } from '@/api/media-fetch-config.ts'
  import { queryAllPreAuthSettings } from '@/api/sys-pre-auth.ts'
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

  const preAuthSettings = computed<PluginComponent[]>(() => {
    return moduleFederation.mfConfig.components.filter(
      component => component.type === 'preAuthSetting',
    )
  })

  // 新增：弹窗控制和当前setting
  const settingDialog = ref(false)
  const currentSetting = ref<PluginComponent | null>(null)
  const currentAction = ref<'insert' | 'update'>('insert')
  const currentId = ref<number | undefined>(undefined)

  // 新增：站点配置列表和加载状态
  const fetcherSettings = ref<BasicMediaFetchConfig[]>([])
  const loading = ref(false)

  // 新增：NAS 配置列表和加载状态
  const preAuthConfigs = ref<BasicPreAuthConfig[]>([])
  const preAuthLoading = ref(false)

  // 新增：下载工具配置列表和加载状态
  const downloadToolConfigs = ref<BasicDownloadToolConfig[]>([])
  const downloadToolLoading = ref(false)

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

  // 加载 NAS 配置列表
  const loadPreAuthSettings = async () => {
    preAuthLoading.value = true
    try {
      const result = await queryAllPreAuthSettings()
      if (result.code === 10_000) {
        preAuthConfigs.value = result.data
      }
    } catch (error) {
      console.error('加载 NAS 配置失败:', error)
    } finally {
      preAuthLoading.value = false
    }
  }

  // 加载下载工具配置列表
  const loadDownloadToolSettings = async () => {
    downloadToolLoading.value = true
    try {
      const result = await queryAllDownloadToolSettings()
      if (result.code === 10_000) {
        downloadToolConfigs.value = result.data
      }
    } catch (error) {
      console.error('加载下载工具配置失败:', error)
    } finally {
      downloadToolLoading.value = false
    }
  }

  // 打开弹窗
  const openSettingDialog = (setting: PluginComponent, action: 'insert' | 'update', id?: number) => {
    currentSetting.value = setting
    currentId.value = id
    currentAction.value = action
    settingDialog.value = true
  }

  // 关闭弹窗
  const closeSettingDialog = () => {
    settingDialog.value = false
    currentSetting.value = null
    // 重新加载列表
    loadFetcherSettings()
    loadPreAuthSettings()
    loadDownloadToolSettings()
  }

  // 编辑配置
  const handleEditSetting = (id: number, pluginId: string) => {
    // 根据 fetcherSource 找到对应的 setting
    const setting = moduleFederation.mfConfig.components.find(
      component => component.pluginId === pluginId && component.type.endsWith('Setting'),
    )

    if (setting) {
      openSettingDialog(setting, 'update', id)
    }
  }

  // 组件挂载时加载数据
  onMounted(() => {
    loadFetcherSettings()
    loadPreAuthSettings()
    loadDownloadToolSettings()
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

<style>
/* 为对话框设置最大高度和滚动 - 使用非 scoped 样式 */
.dialog-with-max-height {
  max-height: 90vh !important;
  overflow-y: auto !important;
}

.dialog-with-max-height .v-card {
  max-height: 90vh !important;
  display: flex !important;
  flex-direction: column !important;
}

.dialog-with-max-height .v-card-text {
  overflow-y: auto !important;
  flex: 1 1 auto !important;
}
</style>
