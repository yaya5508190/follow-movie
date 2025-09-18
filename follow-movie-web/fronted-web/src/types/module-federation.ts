import type { ModuleFederation } from '@module-federation/runtime-core'

export interface MenuItem {
  name: string
  path: string
  component: string
  parent: boolean
  children: MenuItem[]
}

export interface MFConfig {
  remotes: Parameters<ModuleFederation['registerRemotes']>[0]
  menus: MenuItem[]
}
