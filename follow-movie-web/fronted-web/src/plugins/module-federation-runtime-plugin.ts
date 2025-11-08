import type { ModuleFederationRuntimePlugin } from '@module-federation/enhanced/runtime'

/**
 * Module Federation 运行时插件
 * 用于为所有动态加载的资源添加缓存破坏参数，解决 Safari 缓存问题
 *
 * 官方文档: https://module-federation.io/zh/plugin/dev/index.html
 */

/**
 * 为 URL 添加缓存破坏参数
 */
function addCacheBuster (url: string): string {
  if (!url) {
    return url
  }

  // 只处理 .js 和 .json 文件
  if (!url.endsWith('.js') && !url.endsWith('.json')) {
    return url
  }

  const cacheBuster = `_t=${Date.now()}_r=${Math.random().toString(36).slice(7)}`
  const separator = url.includes('?') ? '&' : '?'

  return `${url}${separator}${cacheBuster}`
}

const runtimePlugin: () => ModuleFederationRuntimePlugin = function () {
  return {
    name: 'safari-cache-fix-plugin',

    /**
     * 拦截 fetch 请求
     * 这个钩子会拦截所有通过 Module Federation 加载的资源
     * 包括 manifest.json 和从 manifest 中读取的所有 JS 文件
     */
    fetch (url: string, options: RequestInit) {
      const urlWithCache = addCacheBuster(url)

      console.debug('[Safari Cache Fix] Fetch:', urlWithCache)

      return fetch(urlWithCache, options)
    },

    /**
     * 拦截 createScript 创建
     * 为动态创建的 script 标签添加缓存破坏参数
     * 必须返回自定义的 HTMLScriptElement 才能生效
     */
    createScript ({ url }: { url: string }) {
      const urlWithCache = addCacheBuster(url)
      console.debug('[Safari Cache Fix] CreateScript:', url, '=>', urlWithCache)

      // 创建自定义 script 元素
      const script = document.createElement('script')
      script.src = urlWithCache
      script.type = 'text/javascript'

      return script
    },
  }
}
export default runtimePlugin
