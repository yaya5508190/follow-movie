import type { Router } from 'vue-router'
import type { MenuItem } from '@/types/module-federation.ts'
import { setupLayouts } from 'virtual:generated-layouts'
import PluginView from '@/components/PluginView.vue'
import { useModuleFederation } from '@/stores/module-federation.ts'

/**
 * 把 Pinia 中的 mfConfig 动态注册为路由
 * - path 使用 menu.path
 * - 组件统一为 PluginView
 * - 通过 props 传入 remote 的 alias/name/entry
 * - 自动跳过已存在的路由（按 name 判断）
 */
export function registerPluginRoutes (router: Router) {
  const app = useModuleFederation()
  console.log('注册微前端路由', app.mfConfig.menus)
  for (const menu of app.mfConfig.menus) {
    console.log(menu)
    if (menu.parent) {
      for (const subMenu of menu.children) {
        addRoute(subMenu, router)
      }
    } else {
      addRoute(menu, router)
    }

    console.log(router.getRoutes())
  }
}

function addRoute (menu: MenuItem, router: Router) {
  // 生成稳定的路由名：plugin-<name> 或基于 path
  const routeName = `plugin-${menu.path}`
  if (router.hasRoute(routeName)) {
    return
  }
  console.log(router.getRoutes())
  console.log(menu.component)
  router.addRoute(setupLayouts([{
    name: routeName,
    path: menu.path,
    component: menu.local ? () => import(`@/views/${menu.component}.vue`) : PluginView,
    // 直接把 remote 透传为组件 props
    props: {
      pluginName: menu.component,
    },
    // 也可以保留到 meta 里，便于导航/面包屑
    meta: {
      plugin: true,
      menu,
    },
  }])[0])
}
