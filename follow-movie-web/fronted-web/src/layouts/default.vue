<template>
  <v-layout class="rounded rounded-md border">
    <v-navigation-drawer
      permanent
      :rail="rail"
    >
      <v-list>
        <v-list-item
          prepend-avatar="@/assets/logo.png"
          title="yx"
        />
      </v-list>

      <v-divider />

      <v-list density="compact" nav>
        <v-list-item
          v-for="menu in mfConfig.menus"
          :key="menu.name"
          prepend-icon="mdi-home-city"
          :title="menu.name"
          :value="menu.name"
          @click="$router.push(menu.path)"
        />
      </v-list>
    </v-navigation-drawer>

    <v-app-bar :elevation="0">
      <template #prepend>
        <v-app-bar-nav-icon @click.stop="rail = !rail" />
      </template>

      <!--      <v-app-bar-title>Application Bar</v-app-bar-title>-->
      <template #append>
        <toggle-icon-button
          density="compact"
          :icons="['mdi-weather-night', 'mdi-white-balance-sunny']"
          style="margin: 1em;"
          @click="theme.toggle()"
        />
        <v-text-field
          append-inner-icon="mdi-magnify"
          density="compact"
          hide-details
          label="搜索"
          single-line
          style="width: 10em;margin-right: 1em;"
          variant="solo"
        />
      </template>
    </v-app-bar>

    <v-main>
      <RouterView v-slot="{ Component, route }">
        <component :is="Component" :key="route.fullPath" />
      </RouterView>
    </v-main>
  </v-layout>
</template>

<script lang="ts" setup>
  import { useTheme } from 'vuetify'
  import { useModuleFederation } from '@/stores/module-federation.ts'
  import '@/styles/global.scss'

  const rail = ref(false)
  const theme = useTheme()

  const mfConfig = useModuleFederation().mfConfig
</script>
