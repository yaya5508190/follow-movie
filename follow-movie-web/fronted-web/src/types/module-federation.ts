import type { ModuleFederation } from '@module-federation/runtime-core'

export interface MFConfig {
  remotes: Parameters<ModuleFederation['registerRemotes']>[0]
  menus: { name: string, path: string, component: string }[]
}
