<script setup lang="ts">
  import { eventBus } from '@/plugins/event-bus.ts'
  import { loadRemoteComponent } from '@/plugins/module-federation.ts'
  import vuetify from '@/plugins/vuetify.ts'
  import router from '@/router'

  const container = ref<HTMLElement | null>(null)

  const props = defineProps({
    pluginName: {
      type: String,
      required: true,
    },
  })

  const attrs = useAttrs()

  onMounted(async () => {
    // 从 attrs 中排除 pluginName，只透传其它属性
    const { pluginName: _ignore, ...customProps } = attrs
    const remote = await loadRemoteComponent<{ mount: Function, unmount?: Function, setAppConfig?: Function }>(props.pluginName)

    // 如果插件暴露了 setAppConfig，则注入主项目的回调
    if (remote.setAppConfig) {
      remote.setAppConfig({
        // 跳转到登录页的回调
        onUnauthorized: () => {
          router.push('/login')
        },
      })
    }

    // 将事件总线传递给子应用
    remote.mount(container.value!, {
      ...customProps,
      eventBus, // 添加事件总线
    }, vuetify)
  })

  onBeforeUnmount(async () => {
    const remote = await loadRemoteComponent<{ mount: Function, unmount?: Function }>(props.pluginName)
    remote.unmount?.()

    // 清理事件监听
    // eventBus.all.clear()
  })
</script>

<template>
  <main>
    <div ref="container" />
  </main>
</template>
