<template>
  <v-dialog
    v-model="dialogVisible"
    :fullscreen="mobile"
    height="80vh"
    persistent
    scrollable
    width="80%"
  >
    <v-card>
      <v-card-title class="d-flex justify-space-between align-center">
        <span class="text-h5">聚合搜索: "{{ searchKeyword }}"</span>
        <v-btn
          icon="mdi-close"
          variant="text"
          @click="closeDialog"
        />
      </v-card-title>

      <!-- 可滚动内容容器（增加 ref 以便翻页时滚动至顶部） -->
      <div id="aggregate" ref="contentRef" class="aggregate-search-content">
        <v-card-text class="pa-0">
          <v-tabs
            v-model="activeTab"
            class="mb-4"
          >
            <v-tab
              v-for="(fetcher, index) in mediaFetchers"
              :key="fetcher.pluginId"
              :value="index"
            >
              {{ fetcher.meta }}
            </v-tab>
          </v-tabs>

          <v-window v-model="activeTab">
            <v-window-item
              v-for="(fetcher, index) in mediaFetchers"
              :key="fetcher.pluginId"
              :value="index"
            >
              <div class="pa-4 aggregate-inner-scroll" style="min-height: 100vh;">
                <PluginView
                  :keyword="searchKeyword"
                  :plugin-name="fetcher.component"
                  v-bind="additionalProps"
                />
              </div>
            </v-window-item>
          </v-window>
        </v-card-text>
      </div>

      <v-card-actions>
        <v-spacer />
        <v-btn
          color="primary"
          variant="text"
          @click="closeDialog"
        >
          关闭
        </v-btn>
      </v-card-actions>
    </v-card>
  </v-dialog>
</template>

<script setup lang="ts">
  import type { PluginComponent } from '@/types/module-federation'
  import { computed, nextTick, onBeforeUnmount, onMounted, ref, watch } from 'vue'
  import { useDisplay, useGoTo } from 'vuetify'
  import PluginView from '@/components/PluginView.vue'
  import { useModuleFederation } from '@/stores/module-federation'

  // Vuetify helper for scrolling
  const goTo = useGoTo()
  const { mobile } = useDisplay()

  const props = defineProps<{ modelValue: boolean, keyword?: string, [key: string]: any }>()

  const emit = defineEmits<{
    'update:modelValue': [value: boolean]
  }>()

  const moduleFederation = useModuleFederation()
  const activeTab = ref(0)

  // 控制对话框显示状态
  const dialogVisible = computed({
    get: () => props.modelValue,
    set: (value: boolean) => {
      emit('update:modelValue', value)
    },
  })

  // 获取搜索关键词
  const searchKeyword = computed(() => props.keyword || '')

  // 获取除了modelValue和keyword之外的其他props，用于透传给PluginView
  const additionalProps = computed(() => {
    const { modelValue: _modelValue, keyword: _keyword, ...others } = props
    return others
  })

  // 过滤出mediaResourceFetcher类型的组件
  const mediaFetchers = computed<PluginComponent[]>(() => {
    return moduleFederation.mfConfig.components.filter(
      component => component.type === 'mediaResourceFetcher',
    )
  })

  const scrollContentToTop = async () => {
    await nextTick()
    goTo(0, { container: '#aggregate', duration: 200 })
  }

  // 关闭对话框
  const closeDialog = () => {
    dialogVisible.value = false
  }

  // 监听对话框打开，重置tab到第一个并滚动到顶部
  watch(dialogVisible, newValue => {
    if (newValue) {
      activeTab.value = 0
      scrollContentToTop()
    }
  })

  // 监听来自子插件的自定义事件，子插件可以 dispatchEvent(new CustomEvent('aggregate-scroll-top')) 来触发
  const onAggregateScrollTop = (ev: Event) => {
    scrollContentToTop()
  }

  onMounted(() => {
    window.addEventListener('aggregate-scroll-top', onAggregateScrollTop as EventListener)
  })

  onBeforeUnmount(() => {
    window.removeEventListener('aggregate-scroll-top', onAggregateScrollTop as EventListener)
  })
</script>

<style scoped>

/* 新增：给可滚动容器限制高度并显示滚动条 */
.aggregate-search-content {
  overflow: auto;
}
</style>
