// 插件项目的全局配置
// 用于存储主项目注入的回调函数

interface AppConfig {
  // 跳转到登录页的回调
  onUnauthorized?: () => void
  // 可以扩展其他主项目提供的功能
  [key: string]: any
}

const appConfig: AppConfig = {}

/**
 * 设置应用配置（由主项目调用）
 */
export function setAppConfig(config: Partial<AppConfig>) {
  Object.assign(appConfig, config)
}

/**
 * 获取应用配置
 */
export function getAppConfig(): AppConfig {
  return appConfig
}

/**
 * 触发未授权事件（跳转到登录页）
 */
export function triggerUnauthorized() {
  if (appConfig.onUnauthorized) {
    appConfig.onUnauthorized()
  } else {
    console.warn('未配置 onUnauthorized 回调，无法跳转到登录页')
  }
}

