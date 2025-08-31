import type { ModuleFederation } from '@module-federation/runtime-core'
import type { MFConfig } from '@/types/module-federation.ts'
import { loadRemote, registerRemotes } from '@module-federation/runtime'
import { useModuleFederation } from '@/stores/module-federation.ts'

export async function ensureRemotes (): Promise<ReturnType<ModuleFederation['registerRemotes']>> {
  // [
  //   { alias: 'viteViteRemote', name: 'viteViteRemote', entry: 'http://localhost:3001/mf-manifest.json' },
  //   { alias:'vueViteRemote', name:'vueViteRemote', entry:'http://localhost:5174/remoteEntry.js' }
  // ]
  const { data } = await axiosInstance.get<MFConfig>('/plugin/module-federation')
  console.log(data)
  if (!data?.remotes && !data?.menus) {
    return Promise.resolve()
  }
  // 保存配置到 pinia
  useModuleFederation().setMfConfig(data)
  return registerRemotes(data.remotes)
}

/** 动态加载远程模块（返回其默认导出或模块本体） */
export async function loadRemoteComponent<T = any> (spec: string): Promise<T> {
  const component = await loadRemote(spec)
  // @ts-ignore
  return (component?.default ?? component) as T
}
