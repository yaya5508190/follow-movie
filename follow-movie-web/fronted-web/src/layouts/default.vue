<template>
  <v-layout class="rounded rounded-md border">
    <v-navigation-drawer
      v-model="open"
    >
      <v-list>
        <v-list-item
          prepend-avatar="@/assets/logo.png"
          title="yx"
        />
      </v-list>

      <v-divider />

      <v-list density="compact" nav>
        <template v-for="menu in mfConfig.menus">
          <template v-if="menu.children && menu.children.length > 0">
            <v-list-group
              :key="menu.name + '-group'"
            >
              <template #activator="{ props }">
                <v-list-item
                  v-bind="props"
                  :title="menu.name"
                />
              </template>
              <v-list-item
                v-for="child in menu.children"
                :key="child.name + '-item'"
                :title="child.name"
                :value="child.name"
                @click="$router.push(child.path)"
              />
            </v-list-group>
          </template>
          <template v-else>
            <v-list-item
              :key="menu.name + '-item'"
              :title="menu.name"
              :value="menu.name"
              @click="$router.push(menu.path)"
            />
          </template>
        </template>
      </v-list>
    </v-navigation-drawer>

    <v-app-bar :elevation="0">
      <template #prepend>
        <v-app-bar-nav-icon v-if="mobile" @click.stop="open = !open" />
      </template>

      <!--      <v-app-bar-title>Application Bar</v-app-bar-title>-->
      <template #append>
        <toggle-icon-button
          density="compact"
          :icons="['mdi-white-balance-sunny','mdi-weather-night']"
          style="margin: 1em;"
          @click="theme.cycle(['lightTheme', 'darkTheme'])"
        />
        <v-text-field
          v-model="searchKeyword"
          append-inner-icon="mdi-magnify"
          density="compact"
          hide-details
          label="搜索"
          single-line
          style="width: 10em;margin-right: 1em;"
          variant="solo"
          @click:append-inner="openSearchDialog"
          @keyup.enter="openSearchDialog"
        />
      </template>
    </v-app-bar>
    <v-main>
      <RouterView v-slot="{ Component, route }">
        <component :is="Component" :key="route.fullPath" />
      </RouterView>
    </v-main>

    <!-- 媒体搜索聚合对话框 -->
    <AggregateSearch
      v-model="searchDialogVisible"
      :keyword="searchKeyword"
    />
  </v-layout>
</template>

<script lang="ts" setup>
  import { useDisplay, useTheme } from 'vuetify'
  import AggregateSearch from '@/components/AggregateSearch.vue'
  import { useModuleFederation } from '@/stores/module-federation.ts'

  import '@/styles/global.scss'

  const open = ref(true)
  const theme = useTheme()
  const { mobile } = useDisplay()
  const searchKeyword = ref('')
  const searchDialogVisible = ref(false)

  const mfConfig = useModuleFederation().mfConfig

  // 打开搜索对话框
  const openSearchDialog = () => {
    if (searchKeyword.value.trim()) {
      searchDialogVisible.value = true
    }
  }

  // 监听mobile状态变化，自动关闭侧边栏，初始化的时候立即执行一次
  watch(mobile, isMobile => {
    if (isMobile) {
      open.value = false
    }
  }, { immediate: true })

</script>
