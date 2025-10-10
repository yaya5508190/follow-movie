import type { ModuleFederation } from '@module-federation/runtime-core'

export interface MenuItem {
  name: string
  path: string
  component: string
  parent: boolean
  local?: boolean
  children: MenuItem[]
}

export interface PluginComponent {
  pluginId: string
  type: string
  meta: string
  componentName: string
  component: string
  desc: string
}

export interface MFConfig {
  remotes: Parameters<ModuleFederation['registerRemotes']>[0]
  menus: MenuItem[]
  components: PluginComponent[]
}
